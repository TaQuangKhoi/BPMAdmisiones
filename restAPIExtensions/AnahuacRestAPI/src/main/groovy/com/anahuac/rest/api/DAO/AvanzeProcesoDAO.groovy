package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement

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
		String errorLog = "";
		try {
			//errorLog +=jsonData;
			//closeCon = validarConexion();
			
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
			
			sleep(30000);
			
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
						Date date = new Date(60)
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
			
			resultado.setSuccess(true);
			resultado.setError(errorLog);
		}catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError(errorLog);
			resultado.setError_info(e.getMessage())
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
	
	
	
}
