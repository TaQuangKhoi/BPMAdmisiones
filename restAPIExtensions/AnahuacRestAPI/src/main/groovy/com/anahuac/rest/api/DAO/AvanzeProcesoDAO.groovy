package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.time.LocalDateTime
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor
import org.bonitasoft.engine.bpm.flownode.TimerEventTriggerInstance
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.search.Order
import org.bonitasoft.engine.search.SearchResult

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper

class AvanzeProcesoDAO {
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	public Boolean validarConexion() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno = true
		}
		return retorno
	}
	
	public Result postAsistenciaProceso(String jsonData, RestAPIContext context) {
		Result resultado = new Result()
		Boolean closeCon = false;
		Boolean update = false;
		String errorLog = "";
		try {
			//errorLog +=jsonData;
			//
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			Map < String, Serializable > inputs = new HashMap < String, Serializable > ();
			ProcessAPI processAPI = context.getApiClient().getProcessAPI()
			
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
			searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID, object.caseid);
			searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);
			SearchOptions searchOptions = searchBuilder.done();
			
			SearchResult < HumanTaskInstance > SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
			List < HumanTaskInstance > lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
			Boolean isSeleccionCita = false,generoCredencial = false;;
			for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {
				
				if (objHumanTaskInstance.getName().equals("Seleccionar cita")) {
					errorLog+="Sesion"
					isSeleccionCita = true;
					processAPI.assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
					processAPI.executeUserTask(objHumanTaskInstance.getId(),inputs);
				}
				
			}
			
			sleep(15000);
			
			SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
			lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
			for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {
				if (objHumanTaskInstance.getName().equals("Generar credencial")) {
					errorLog+="credencial"
					generoCredencial = true;
					processAPI.assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
					processAPI.executeUserTask(objHumanTaskInstance.getId(),inputs);
				}
			}
			
			if(generoCredencial) {
				// Se pasan las variables del proceso a true
				Map<String, Serializable> rows = new HashMap<String, Serializable>();
				
				rows.put("asistenciaCollegeBoard", true);
				rows.put("asistenciaPsicometrico", true);
				rows.put("asistenciaEntrevista", true);
				
				processAPI.updateProcessDataInstances(Long.parseLong(object.caseid), rows);
				
				
				//vencer el timer
				searchBuilder = new SearchOptionsBuilder(0, 99999);
				searchOptions = searchBuilder.done();
				SearchResult < TimerEventTriggerInstance > SearchTimmerTaskSearch = context.getApiClient().getProcessAPI().searchTimerEventTriggerInstances(Long.parseLong(object.caseid),searchOptions)
	
				List < TimerEventTriggerInstance > lstTimerTaskSearch = SearchTimmerTaskSearch.getResult();
	
				for (TimerEventTriggerInstance objTimerInstance: lstTimerTaskSearch) {
					if (objTimerInstance.getEventInstanceName().equals("Timmer pase de lista") ) {
						Date date = new Date(90)
						processAPI.updateExecutionDateOfTimerEventTriggerInstance(objTimerInstance.getId(), date)
					}
				}
			}
			
			
			for(int i = 0; i<3; i++) {
				errorLog +="5"
				if(object.asistencia[i].necesita == true) {
					//errorLog +="6"
					Result resultado2 = new Result();
					String data = '{ "prueba": [PRUEBA] , "username": "[USERNAME]", "asistencia": [ASISTENCIA],"usuarioPaseLista":"[USUARIOPASELISTA]" }';
					data = data.replace("[PRUEBA]",object.asistencia[i].prueba.toString()).replace("[USERNAME]", object.asistencia[i].username.toString()).replace("[ASISTENCIA]", object.asistencia[i].asistencia.toString()).replace("[USUARIOPASELISTA]", object.asistencia[i].usuarioPaseLista.toString())
					//errorLog += data;
					resultado2 = new SesionesDAO().insertPaseLista(data,context);
					//errorLog +="Error"+i+":"+resultado2.getError();
				}
			}
			errorLog +="6"
			closeCon = validarConexion();
			errorLog +="7"
			
			pstm = con.prepareStatement("select  persistenceid from solicituddeadmision where caseid = ? order by persistenceid DESC limit 1");
			pstm.setLong(1,Long.parseLong(object.caseid));
			rs = pstm.executeQuery()
			errorLog +="8"
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				errorLog +="9"
				for (int i = 1; i <= columnCount; i++) {
					errorLog +="10"
					update = true;
					con.setAutoCommit(false)
					pstm = con.prepareStatement("update  solicituddeadmision set estatussolicitud = 'Carga y consulta de resultados'  where persistenceid = ? ");
					pstm.setLong(1, rs.getLong(i));
					pstm.executeUpdate();
				}
				
			}
			
			resultado.setSuccess(true);
			resultado.setError(errorLog);
		}catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError(errorLog);
			resultado.setError_info(e.getMessage())
			if(update) {
				con.rollback();
			}
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado
	}
	
	public Result getInfoByIdBanner(String idbanner) {
		Result resultado = new Result()
		Boolean closeCon = false;
		try {
			closeCon = validarConexion();
			
			pstm = con.prepareStatement("select  concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre) as nombre,DS.idbanner, sda.correoelectronico as usuario,cc.descripcion as vpd,cc2.descripcion as campus, DS.caseid, DS.cbcoincide from solicituddeadmision as sda inner join Detallesolicitud as DS ON DS.caseid::integer = sda.caseid inner join catcampus as cc on cc.persistenceid = sda.catcampusestudio_pid inner join catcampus as cc2 on cc2.persistenceid = sda.catcampus_pid where DS.idbanner = ?");
			pstm.setString(1, idbanner);
			rs = pstm.executeQuery()
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				
				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError("500 Internal Server Error")
			resultado.setError_info(e.getMessage())
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado
	}
	
	public Result getIsPeriodoActivo(String usuario) {
		Result resultado = new Result()
		Boolean closeCon = false;
		try {
			closeCon = validarConexion();
			
			pstm = con.prepareStatement("SELECT (CAST(cp.fechaFin AS DATE) > CAST(TO_CHAR(NOW(),'YYYY-MM-DD') as DATE) ) as periodoActivo FROM solicitudDeAdmision AS sda INNER JOIN CatPeriodo AS cp on sda.catperiodo_pid = cp.persistenceid where sda.correoelectronico = ? ");
			pstm.setString(1, usuario);
			rs = pstm.executeQuery()
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				
				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError("500 Internal Server Error")
			resultado.setError_info(e.getMessage())
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado
	}
	
	
	public Result updatePeriodo(String clave, String tipo, String correo) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String tipoCast = "";
		
		try {
			
			closeCon = validarConexion();
			
			
			if (tipo.equals("Semestral")) {
				tipoCast = "issemestral";
			}
			if (tipo.equals("Cuatrimestral")) {
				tipoCast = "iscuatrimestral";
			}
			if (tipo.equals("Anual")) {
				tipoCast = "isanual";
			}
			
			String sda_persistenceid= "";
			
			pstm = con.prepareStatement("select persistenceid from solicituddeadmision order by persistenceid DESC limit 1");
			pstm.setString(1, correo);
			rs = pstm.executeQuery()
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					sda_persistenceid =  rs.getString(i);
				}	
			}
			
			
			String consulta = "SELECT persistenceid FROM catperiodo where clave = ? and iseliminado is false and  activo is true and "+tipoCast+" is true "
			pstm = con.prepareStatement(consulta);
			pstm.setString(1, clave);
			rs = pstm.executeQuery()
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			
			metaData = rs.getMetaData();
			columnCount = metaData.getColumnCount();
			
			while (rs.next()) {

				for (int i = 1; i <= columnCount; i++) {
					con.setAutoCommit(false)
					pstm = con.prepareStatement("update solicituddeadmision set catperiodo_pid = ? where persistenceid = ?");
					pstm.setLong(1, Long.parseLong(rs.getString(i)) );
					pstm.setString(2, sda_persistenceid);
						
					pstm.executeUpdate();
					con.commit();
				}
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError("500 Internal Server Error")
			resultado.setError_info(e.getMessage())
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado
	}
	
	public Result getFechaPruebasByUsername(String username) {
		Result resultado = new Result()
		Boolean closeCon = false;
		try {
			closeCon = validarConexion();
			
			pstm = con.prepareStatement("select AP.asistencia,AP.cattipoprueba_pid, AP.acreditado,P.aplicacion,AP.prueba_pid,S.nombre as nombresesion, P.nombre as nombreprueba from aspirantespruebas as AP inner join Pruebas as P on P.persistenceid = AP.prueba_pid inner join sesiones as S on S.persistenceid = AP.sesiones_pid where AP.username = ? Order by AP.persistenceid DESC,AP.cattipoprueba_pid DESC  limit 3");
			pstm.setString(1, username);
			rs = pstm.executeQuery()
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				
				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError("500 Internal Server Error")
			resultado.setError_info(e.getMessage())
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado
	}
	
	public Result getEstatusDelAspirante(String username) {
		Result resultado = new Result()
		Boolean closeCon = false;
		try {
			closeCon = validarConexion();
			
			pstm = con.prepareStatement("select estatusSolicitud from SolicitudDeAdmision where correoelectronico = ?");
			pstm.setString(1, username);
			rs = pstm.executeQuery()
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				
				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError("500 Internal Server Error")
			resultado.setError_info(e.getMessage())
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado
	}
	
	
	public Result updateTimmerPeriodoVencido(Integer P, Integer C, RestAPIContext context) {
		//Vencimiento periodo
		Result resultado = new Result()
		Boolean closeCon = false;
		String errorLog = "";
		try {
			
			ProcessAPI processAPI = context.getApiClient().getProcessAPI()
			//Select para optener usuario
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT caseid FROM SolicitudDeAdmision order by persistenceid DESC LIMIT ? OFFSET ?");
			pstm.setInt(1, C);
			pstm.setInt(2, P);
			rs = pstm.executeQuery()
			
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			
			Date fechaFin = new Date();
			fechaFin = Date.parse("yyyy-MM-dd","2121-10-01");
			
			Map<String, Serializable> rows2 = new HashMap<String, Serializable>();
			rows2.put("fechavencimientoperiodo", fechaFin);
			
			
			while (rs.next()) {
				//Long.parseLong(rs.getString(i))
				
				for (int i = 1; i <= columnCount; i++) {
					
					SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
					SearchOptions searchOptions = searchBuilder.done();
					
					try {
						//cambio en la variable de fechavencimientoperiodo
						processAPI.updateProcessDataInstances(Long.parseLong(rs.getString(i)), rows2);
					}catch(Exception ex) {
						errorLog+="Caseid:"+Long.parseLong(rs.getString(i))+"Error:"+ex.getMessage();
					}
					
					try {
						
						
						SearchResult < TimerEventTriggerInstance > SearchTimmerTaskSearch = context.getApiClient().getProcessAPI().searchTimerEventTriggerInstances( Long.parseLong(rs.getString(i)),searchOptions)
						List < TimerEventTriggerInstance > lstTimerTaskSearch = SearchTimmerTaskSearch.getResult();
						for (TimerEventTriggerInstance objTimerInstance: lstTimerTaskSearch) {
							if (objTimerInstance.getEventInstanceName().equals("Vencimiento periodo") ) {
								Date date = new Date(4123465200000);
								processAPI.updateExecutionDateOfTimerEventTriggerInstance(objTimerInstance.getId(), date)
							}			
							if (objTimerInstance.getEventInstanceName().equals("Solicitud vencida") ) {
								Date date = new Date(4123465200000);
								processAPI.updateExecutionDateOfTimerEventTriggerInstance(objTimerInstance.getId(), date)
							}
							if (objTimerInstance.getEventInstanceName().equals("Mod ven.") ) {
								Date date = new Date(4123465200000);
								processAPI.updateExecutionDateOfTimerEventTriggerInstance(objTimerInstance.getId(), date)
							}
							if (objTimerInstance.getEventInstanceName().equals("Val ven") ) {
								Date date = new Date(4123465200000);
								processAPI.updateExecutionDateOfTimerEventTriggerInstance(objTimerInstance.getId(), date)
							}
							if (objTimerInstance.getEventInstanceName().equals("Pago ven") ) {
								Date date = new Date(4123465200000);
								processAPI.updateExecutionDateOfTimerEventTriggerInstance(objTimerInstance.getId(), date)
							}
							if (objTimerInstance.getEventInstanceName().equals("Espera ven") ) {
								Date date = new Date(4123465200000);
								processAPI.updateExecutionDateOfTimerEventTriggerInstance(objTimerInstance.getId(), date)
							}
							if (objTimerInstance.getEventInstanceName().equals("Carga ven") ) {
								Date date = new Date(4123465200000);
								processAPI.updateExecutionDateOfTimerEventTriggerInstance(objTimerInstance.getId(), date)
							}
							if (objTimerInstance.getEventInstanceName().equals("Gen ven") ) {
								Date date = new Date(4123465200000);
								processAPI.updateExecutionDateOfTimerEventTriggerInstance(objTimerInstance.getId(), date)
							}
							if (objTimerInstance.getEventInstanceName().equals("Selec cita") ) {
								Date date = new Date(4123465200000);
								processAPI.updateExecutionDateOfTimerEventTriggerInstance(objTimerInstance.getId(), date)
							}
							
						}
					}catch(Exception ex) {
						errorLog+="Caseid:"+Long.parseLong(rs.getString(i))+"Error:"+ex.getMessage();
					}
					
					
					
				}
				
			}
			
			resultado.setSuccess(true);
			resultado.setError_info(errorLog)
		}catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError(errorLog)
			resultado.setError_info(e.getMessage())
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado
	}
	
	
	
}
