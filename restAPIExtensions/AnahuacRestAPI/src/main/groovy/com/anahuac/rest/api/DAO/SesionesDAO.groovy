package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.catalogos.CatNotificacionesFirma
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.Calendario
import com.anahuac.rest.api.Entity.Custom.PruebaCustom
import com.anahuac.rest.api.Entity.Custom.PruebasCustom
import com.anahuac.rest.api.Entity.Custom.ResponsableCustom
import com.anahuac.rest.api.Entity.Custom.SesionCustom
import com.anahuac.rest.api.Entity.Custom.SesionesAspiranteCustom
import com.anahuac.rest.api.Entity.db.Responsable
import com.anahuac.rest.api.Entity.db.ResponsableDisponible
import com.anahuac.rest.api.Entity.db.Sesion_Aspirante
import com.anahuac.rest.api.Entity.db.CatTipoPrueba
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date;

import org.apache.commons.collections4.functors.ComparatorPredicate.Criterion
import org.bonitasoft.engine.bpm.data.DataInstance
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion


class SesionesDAO {
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	ResultSet rs2;

	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
		return retorno
	}
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true
		}
		return retorno
	}
	public Result getCatTipoPrueba(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", orderby="ORDER BY ", errorlog=""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatTipoPrueba row = new CatTipoPrueba()
			List<CatTipoPrueba> rows = new ArrayList<CatTipoPrueba>();
			closeCon = validarConexion();
			
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
					case "DESCRIPCION":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(DESCRIPCION) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
				}
			}
			orderby+=object.orderby
			if(orderby.equals("ORDER BY ")) {
				orderby+="PERSISTENCEID";
			}
			orderby+=" "+object.orientation;
			String consulta = Statements.GET_CATTIPOPRUEBA;
			consulta=consulta.replace("[WHERE]", where);
			errorlog+="consulta:"
			errorlog+=consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "")
				pstm = con.prepareStatement(consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				rs = pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog+="consulta:"
				errorlog+=consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				rs = pstm.executeQuery()
				while(rs.next()) {
					row = new CatTipoPrueba()
					
					row.setDescripcion(rs.getString("descripcion"))
					row.setPersistenceId(rs.getLong("persistenceId"))
					row.setPersistenceVersion(rs.getLong("persistenceVersion"))
					rows.add(row)
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getLastFechaExamenByUsername(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		String errorlog="";
		String fechaFinalStr="";
		
		List<Map<String, Object>> lstResultado = new ArrayList<Map<String, Object>>();
		Map<String, Object> objResultado = new HashMap<String, Object>();
		
		Integer contador = 1;
		
		DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DateFormat dfSalidaTXT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendario = new GregorianCalendar();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
						
			errorlog+="| consulta: "+Statements.GET_LAST_FECHA_EXAMEN;
			pstm = con.prepareStatement(Statements.GET_LAST_FECHA_EXAMEN);
			pstm.setString(1, object.username);
			rs = pstm.executeQuery();
			while(rs.next()) {
				fechaFinalStr = (rs.getString("fechaFinal") == null ? "" : rs.getString("fechaFinal"))+" "+(rs.getString("salida")==null ? "" : rs.getString("salida"));
				if(!fechaFinalStr.equals(" ")) {
					objResultado = new HashMap<String, Object>();
					calendario.setTime(dfSalida.parse(fechaFinalStr));
					calendario.add(Calendar.DAY_OF_YEAR, +3);
					calendario.set(Calendar.HOUR_OF_DAY, 23);
					calendario.set(Calendar.MINUTE, 59);
					calendario.set(Calendar.SECOND, 59);
					
					objResultado.put("tiempo", calendario.getTimeInMillis());
					objResultado.put("tiempoText", dfSalidaTXT.format(calendario.getTime()));
					objResultado.put("descripcion", rs.getString("descripcion"));
					lstResultado.add(objResultado);
				}
			}
			
			lstResultado = ordenarList(lstResultado);
			for(Map<String, Object> row : lstResultado) {
				row.put("contador", contador);
				contador++;
			}
			
			resultado.setError_info(errorlog);
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public List<Map<String, Object>> ordenarList(List<Map<String, Object>> lstResultado){
		
		Map<String, Object> objAux = new HashMap<String, Object>();
		
		int i, j;
		
		for(i = 0; i < lstResultado.size() - 1; i++) {
			for(j = 0; j < lstResultado.size() - i - 1; j++) {
				if (((Long) lstResultado.get(j + 1).get("tiempo")) < ((Long) lstResultado.get(j).get("tiempo"))) {
					objAux = lstResultado.get(j + 1);
					lstResultado.set((j + 1), lstResultado.get(j));
					lstResultado.set(j, objAux);
				}
			}
		}
		return lstResultado;
	}
	
	public Result getCatPsicologo(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", orderby="ORDER BY ", errorlog=""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			Responsable row = new Responsable()
			List<Responsable> rows = new ArrayList<Responsable>();
			closeCon = validarConexion();
			
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
					case "NOMBRE":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(DESCRIPCION) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
				}
			}
			orderby+=object.orderby
			if(orderby.equals("ORDER BY ")) {
				orderby+="PERSISTENCEID";
			}
			orderby+=" "+object.orientation;
			String consulta = Statements.GET_CATPSICOLOGO;
			consulta=consulta.replace("[WHERE]", where);
			errorlog+="consulta:"
			errorlog+=consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "")
				pstm = con.prepareStatement(consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				rs = pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog+="consulta:"
				errorlog+=consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				rs = pstm.executeQuery()
				while(rs.next()) {
					row = new CatTipoPrueba()
					
					row.setNombre(rs.getString("nombre"))
					row.setPersistenceId(rs.getLong("persistenceId"))
					row.setPersistenceVersion(rs.getLong("persistenceVersion"))
					rows.add(row)
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	public Result getUserBonita(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		String where ="WHERE u.enabled=true ", orderby="ORDER BY ", errorlog="", role="", group="";
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				try {
					closeCon = validarConexion()
					pstm = con.prepareStatement("SELECT descripcion,grupobonita FROM catcampus where iseliminado=false");
					rs = pstm.executeQuery()
					while(rs.next()){
						objGrupoCampus = new HashMap<String, String>();
						objGrupoCampus.put("descripcion",rs.getString("descripcion"));
						objGrupoCampus.put("valor",rs.getString("grupobonita"));
						lstGrupoCampus.add(objGrupoCampus);
					}
				} catch (Exception e) {
					e.printStackTrace()
				}finally {
					if(closeCon) {
						new DBConnect().closeObj(con, stm, rs, pstm)
					}
				}
				
				/*assert object instanceof List;
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Cancún");
				objGrupoCampus.put("valor","CAMPUS-CANCUN");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Mayab");
				objGrupoCampus.put("valor","CAMPUS-MAYAB");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac México Norte");
				objGrupoCampus.put("valor","CAMPUS-MNORTE");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac México Sur");
				objGrupoCampus.put("valor","CAMPUS-MSUR");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Oaxaca");
				objGrupoCampus.put("valor","CAMPUS-OAXACA");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Puebla");
				objGrupoCampus.put("valor","CAMPUS-PUEBLA");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Querétaro");
				objGrupoCampus.put("valor","CAMPUS-QUERETARO");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Xalapa");
				objGrupoCampus.put("valor","CAMPUS-XALAPA");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Querétaro");
				objGrupoCampus.put("valor","CAMPUS-QUERETARO");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Juan Pablo II");
				objGrupoCampus.put("valor","CAMPUS-JP2");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Cordoba");
				objGrupoCampus.put("valor","CAMPUS-CORDOBA");
				lstGrupoCampus.add(objGrupoCampus);*/
						
				userLogged = context.getApiSession().getUserId();
				
				List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
				for(UserMembership objUserMembership : lstUserMembership) {
					for(Map<String, String> rowGrupo : lstGrupoCampus) {
						if(objUserMembership.getGroupName().equals(rowGrupo.get("valor"))) {
							lstGrupo.add(rowGrupo.get("valor"));
							break;
						}
					}
				}
				group+=" AND ("
				for(Integer i=0; i<lstGrupo.size(); i++) {
					String campusMiembro=lstGrupo.get(i);
					group+="g.name='"+campusMiembro+"'"
					if(i==(lstGrupo.size()-1)) {
						group+=") "
					}
					else {
						group+=" OR "
					}
				}
				
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						case "NOMBRE":
						if(where.contains("WHERE")) {
							where+= " OR "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(firstname) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "APELLIDO":
						if(where.contains("WHERE")) {
								  where+= " OR "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(lastname) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "GRUPO":
							 if(filtro.get("operador").equals("Igual a")) {
								  group+="AND LOWER(g.name)=LOWER('[valor]')"
							 }else {
								  group+="AND LOWER(g.name) LIKE LOWER('[valor]')"
							 }
							 group = group.replace("[valor]", filtro.get("valor"))
							 break;
						case "ROL":
						if(where.contains("WHERE")) {
								  where+= " OR "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(r.name) ";
							 
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
								 
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
								 
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 
							 break;
					}
				}
				switch(object.orderby) {
					case "NOMBRE":
					orderby+="u.firstname";
					break;
					case "APELLIDO":
					orderby+="lastname";
					break;
					case "GRUPO":
					orderby+="g.name";
					break;
					case "ROL":
					orderby+="r.name";
					break;
					default:
					//orderby+="u.id"
					orderby+="u.firstname,u.lastname";
					break;
				}
				orderby+=" "+object.orientation;
				String consulta = Statements.GET_USER_BONITA;
				consulta=consulta.replace("[ROLE]", role);
				consulta=consulta.replace("[GROUP]", group);
				consulta=consulta.replace("[WHERE]", where);
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexionBonita();
				pstm = con.prepareStatement(consulta.replace("u.id, u.firstname, u.lastname", "COUNT(u.id) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog+="consulta:"
				errorlog+=consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				
				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
	
					rows.add(columns);
				}
				resultado.setError_info(errorlog)
				resultado.setSuccess(true)
				
				resultado.setData(rows)
			
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	public Result insertSesion(SesionCustom sesion) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Boolean singleTime=true
		try {
				List<SesionCustom> rows = new ArrayList<SesionCustom>();
				closeCon = validarConexion();
				con.setAutoCommit(false)
				if(sesion.getPersistenceId()>0) {
					pstm = con.prepareStatement(Statements.UPDATE_SESION)
				}else {
					pstm = con.prepareStatement(Statements.INSERT_SESION, Statement.RETURN_GENERATED_KEYS)
				}
				pstm.setString(1, sesion.getNombre())
				pstm.setString(2, sesion.getDescripcion())
				try {
					pstm.setDate(3, convert(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:00.000'Z'").parse(sesion.getFecha_inicio())))
				}catch(Exception e) {
					pstm.setDate(3, convert(new SimpleDateFormat("yyyy-MM-dd").parse(sesion.getFecha_inicio())))
				}
				(sesion.getIsmedicina()==null)?pstm.setBoolean(4, false):pstm.setBoolean(4, sesion.getIsmedicina())
				(sesion.getBachillerato_pid()==null)?pstm.setLong(5, 0L):pstm.setLong(5, sesion.getBachillerato_pid())
				(sesion.getEstado_pid()==null)?pstm.setLong(6,0L):pstm.setLong(6, sesion.getEstado_pid())
				(sesion.getPais_pid()==null)?pstm.setLong(7, 0L):pstm.setLong(7, sesion.getPais_pid())
				(sesion.getBorrador()==null)?pstm.setBoolean(8, true):pstm.setBoolean(8, sesion.getBorrador())
				pstm.setLong(9, sesion.getCampus_pid())
				pstm.setString(10, sesion.getTipo())
				(sesion.getCiudad_pid()==null)?pstm.setLong(11, 0L):pstm.setLong(11, sesion.getCiudad_pid())
				if(sesion.getUltimo_dia_inscripcion().contains(":")) {
					pstm.setString(12, sesion.getUltimo_dia_inscripcion())
				}else {
					pstm.setString(12, sesion.getUltimo_dia_inscripcion()+" 23:59:59")
				}
				pstm.setBoolean(13, sesion.getIsEliminado())
				(sesion.getPeriodo_pid()==null)?pstm.setLong(14, 0L):pstm.setLong(14, sesion.getPeriodo_pid())
				if(sesion.getPersistenceId()>0) {
					pstm.setLong(15, sesion.getPersistenceId())
					pstm.executeUpdate()
				}
				else {
					pstm.executeUpdate();
					rs = pstm.getGeneratedKeys();
					if(rs.next()) {
						sesion.setPersistenceId(rs.getLong("persistenceid"))
					}
				}
				
					
				for (PruebaCustom prueba: sesion.getPruebas()) {
					if(prueba.getPersistenceId()>0) {
						pstm = con.prepareStatement(Statements.VALIDAR_FECHA_PRUEBA_REGISTRADOS)
						pstm.setLong(1, prueba.getPersistenceId())
						rs = pstm.executeQuery();
						if(rs.next()) {
							if(!rs.getString("aplicacion").equals(prueba.getAplicacion())) {
								throw new Exception("No es posible guardar, la prueba tiene aspirantes registrados.")
							}
						}
						
						pstm = con.prepareStatement(Statements.UPDATE_PRUEBA)
					}else {
						pstm = con.prepareStatement(Statements.INSERT_PRUEBA, Statement.RETURN_GENERATED_KEYS)
					}
					pstm.setString(1, prueba.getNombre())
					try {
						pstm.setDate(2, convert(new SimpleDateFormat("yyyy-MM-dd").parse(prueba.getAplicacion())))
					}catch(Exception e) {
						pstm.setNull(2, java.sql.Types.NULL)
					}

					pstm.setString(3, prueba.getEntrada())
					pstm.setString(4, prueba.getSalida())
					pstm.setInt(5, prueba.getRegistrados())
					pstm.setNull(6,java.sql.Types.NULL)
					pstm.setInt(7, prueba.getCupo())
					pstm.setString(8, prueba.getLugar())
					(prueba.getCampus_pid()==null)?pstm.setNull(9, java.sql.Types.NULL):pstm.setLong(9, prueba.getCampus_pid())
					(prueba.online)?pstm.setNull(10, java.sql.Types.NULL):pstm.setString(10, prueba.getCalle())
					(prueba.online)?pstm.setNull(11, java.sql.Types.NULL):pstm.setString(11, prueba.getNumero_int())
					(prueba.online)?pstm.setNull(12, java.sql.Types.NULL):pstm.setString(12, prueba.getNumero_ext())
					(prueba.online)?pstm.setNull(13, java.sql.Types.NULL):pstm.setString(13, prueba.getColonia())
					(prueba.online)?pstm.setNull(14, java.sql.Types.NULL):pstm.setString(14, prueba.getCodigo_postal())
					(prueba.online)?pstm.setNull(15, java.sql.Types.NULL):pstm.setString(15, prueba.getMunicipio())
					(prueba.getPais_pid()==null)?pstm.setNull(16, java.sql.Types.NULL):pstm.setLong(16, prueba.getPais_pid())
					(prueba.getEstado_pid()==null)?pstm.setNull(17, java.sql.Types.NULL):pstm.setLong(17, prueba.getEstado_pid())
					pstm.setBoolean(18, (prueba.getIseliminado()==null)?false:prueba.getIseliminado())
					pstm.setLong(19, sesion.getPersistenceId())
					pstm.setString(20, prueba.getDuracion())
					pstm.setString(21, prueba.getDescripcion())
					pstm.setLong(22, prueba.getCattipoprueba_pid())
					pstm.setBoolean(23, prueba.getOnline())
					if(prueba.getPersistenceId()>0) {
						pstm.setLong(24, prueba.getPersistenceId())
						
						pstm.executeUpdate();
					}else {
						pstm.executeUpdate()
						rs = pstm.getGeneratedKeys();
						if(rs.next()) {
							prueba.setPersistenceId(rs.getLong("persistenceId"))
						}
					}
					
					if(prueba.getCattipoprueba_pid()==1L) {
					if(prueba.getCambioDuracion()) {
						pstm = con.prepareStatement(Statements.DELETE_IF_CAMBIO_DURACION)
						pstm.setLong(1, prueba.getPersistenceId())
					}
					
					
					for (ResponsableCustom responsable: prueba.getPsicologos()) {
						for (ResponsableDisponible disponible:responsable.getLstFechasDisponibles()) {
							
							if(responsable.getIseliminado() && disponible.getPersistenceId()>0) {
								pstm = con.prepareStatement(Statements.DELETEHORARIOSRESPONSABLE)
								pstm.setLong(1, disponible.getPersistenceId())
								pstm.executeUpdate()
							}
					
							if(disponible.getPersistenceId()>0) {
								pstm = con.prepareStatement(Statements.UPDATE_RESPONSABLEDISPONIBLE)
							}else {
								if(singleTime) {
									pstm = con.prepareStatement(Statements.DELETEIFBEFORECREATED)
									pstm.setLong(1, prueba.getPersistenceId())
									pstm.setLong(2, responsable.getId())
									pstm.executeUpdate()
									singleTime=false
								}
								
								
								pstm = con.prepareStatement("SELECT persistenceid FROM responsabledisponible where prueba_pid=? and responsableid=?  and horario=? and iseliminado=false")
								pstm.setLong(1, prueba.getPersistenceId())
								pstm.setLong(2, responsable.getId())
								pstm.setString(3, disponible.getHorario())
								rs = pstm.executeQuery()
								if(rs.next()) {
									disponible.setPersistenceId(rs.getLong("persistenceid"))
									pstm = con.prepareStatement(Statements.UPDATE_RESPONSABLEDISPONIBLE)
								}else {
									pstm = con.prepareStatement(Statements.INSERT_RESPONSABLEDISPONIBLE, Statement.RETURN_GENERATED_KEYS)
								}
							}
							
							pstm.setString(1, disponible.getHorario())
							pstm.setBoolean(2, disponible.getDisponible())
							pstm.setLong(3, responsable.getId())
							pstm.setLong(4,prueba.getPersistenceId())
							pstm.setString(5, responsable.getLicenciaturas())
							if(disponible.getPersistenceId()>0){
								pstm.setBoolean(6, (responsable.getIseliminado()==null)?false:responsable.getIseliminado())
								pstm.setLong(7, disponible.getPersistenceId())
								pstm.executeUpdate()
							}else {
								pstm.executeUpdate()
								rs = pstm.getGeneratedKeys();
								if (rs.next()) {
									disponible.setPersistenceId(rs.getLong("persistenceId"))
									responsable.setPersistenceId(rs.getLong("persistenceId"))
								}
							}
							if(!prueba.iseliminado && !responsable.getIseliminado()) {
								pstm = con.prepareStatement(Statements.REVISAR_DISPONIBLE_RESPONSABLE)
								pstm.setDate(1, convert(new SimpleDateFormat("yyyy-MM-dd").parse(prueba.getAplicacion())))
								pstm.setLong(2, responsable.getId())
								pstm.setLong(3, prueba.getPersistenceId())
								pstm.setString(4, prueba.getAplicacion() + " " + prueba.getEntrada());
								pstm.setString(5, prueba.getAplicacion() + " " + prueba.getSalida());
								rs=pstm.executeQuery()
								if(rs.next()) {
									throw new Exception("Un responsable seleccionado para la prueba " + prueba.getNombre() + " no se encuentra disponible el día " + prueba.getAplicacion() + " horario " + disponible.getHorario())
								}
							}
						}
					}
					}else {
						for (ResponsableCustom responsable: prueba.getPsicologos()) {
							if(responsable.getPersistenceId()>0) {
										   
								pstm = con.prepareStatement(Statements.UPDATE_RESPONSABLEDISPONIBLE)
							}else {
								pstm = con.prepareStatement(Statements.INSERT_RESPONSABLEDISPONIBLE, Statement.RETURN_GENERATED_KEYS)
							}
							pstm.setNull(1, java.sql.Types.NULL)
							pstm.setNull(2, java.sql.Types.NULL)
							pstm.setLong(3, responsable.getId())
							pstm.setLong(4,prueba.getPersistenceId())
							pstm.setNull(5,java.sql.Types.NULL)
							if(responsable.getPersistenceId()>0){
								pstm.setBoolean(6, (responsable.getIseliminado()==null)?false:responsable.getIseliminado())
								pstm.setLong(7, responsable.getPersistenceId())
								pstm.executeUpdate()
							}else {
							pstm.executeUpdate()
							rs = pstm.getGeneratedKeys();
							if (rs.next()) {
								responsable.setLstFechasDisponibles(new ArrayList())
								responsable.getLstFechasDisponibles().add(new ResponsableDisponible())
								responsable.getLstFechasDisponibles().get(0).setPersistenceId(rs.getLong("persistenceId"))
								responsable.setPersistenceId(rs.getLong("persistenceId"))
							}
							}
							
						}
					}
					
					
				}
				int count=0;
				for(int i=0; i<sesion.pruebas.size();i++) {
					if(!sesion.pruebas.get(0).iseliminado) {
						count++;
					}
				}
				if(count>0) {
					pstm = con.prepareStatement(Statements.UPDATE_SESION_FECHA)
					pstm.setLong(1, sesion.persistenceId)
					pstm.setLong(2, sesion.persistenceId)
					pstm.executeUpdate()
					
					/*pstm = con.prepareStatement(Statements.UPDATE_PRUEBA_FECHA)
					pstm.setLong(1, sesion.persistenceId)
					pstm.setLong(2, sesion.persistenceId)
					pstm.executeUpdate()*/
					
					
				}
				
				con.commit();
				rows.add(sesion)
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result insertSesionAspirante(Sesion_Aspirante sesionAspirante,  RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long responsabledisponible_pid = 0L
		try {
				List<Sesion_Aspirante> rows = new ArrayList<Sesion_Aspirante>();
				closeCon = validarConexion();
				con.setAutoCommit(false)
				pstm = con.prepareStatement(Statements.GET_REGISTRADOS_CUPO)
				pstm.setLong(1, sesionAspirante.getSesiones_pid())
				rs = pstm.executeQuery()
				while(rs.next()) {
					if((rs.getInt("registrados")+1)>rs.getInt("cupo")) {
						throw new Exception("No hay cupo");
					}
				}
				
				pstm = con.prepareStatement(Statements.GET_OCUPADO_RESPONSABLE_DISPONIBLE)
				pstm.setLong(1, sesionAspirante.getResponsabledisponible_pid())
				rs = pstm.executeQuery()
				if(rs.next()) {
					throw new Exception("Se encuentra ocupado")
				}
				
				pstm = con.prepareStatement(Statements.GET_SESIONASPIRANTE)
				pstm.setString(1, sesionAspirante.getUsername())
				rs = pstm.executeQuery()
				if(rs.next()) {
					
					responsabledisponible_pid = rs.getLong("responsabledisponible_pid")
					if(sesionAspirante.getResponsabledisponible_pid().equals(0L)){
						sesionAspirante.setResponsabledisponible_pid(responsabledisponible_pid)
					}
					pstm = con.prepareStatement(Statements.UPDATE_REGISTRADOS_INV)
					pstm.setLong(1, rs.getLong("sesiones_pid"))
					pstm.setLong(2, rs.getLong("sesiones_pid"))
					pstm.executeUpdate();
					
					pstm = con.prepareStatement(Statements.UPDATE_OCUPADO_RESPONSABLE_DISPONIBLE_INV)
					pstm.setLong(1, rs.getLong("responsabledisponible_pid"))
					pstm.executeUpdate();
					
					pstm = con.prepareStatement(Statements.UPDATE_REGISTRADOS_PRUEBAS_INV)
					pstm.setLong(1, rs.getLong("responsabledisponible_pid"))
					pstm.setLong(2, rs.getLong("responsabledisponible_pid"))
					pstm.executeUpdate();
					
					pstm = con.prepareStatement(Statements.UPDATE_REGISTRADOS)
					pstm.setLong(1, sesionAspirante.getSesiones_pid())
					pstm.setLong(2, sesionAspirante.getSesiones_pid())
					pstm.executeUpdate();
					
					pstm = con.prepareStatement(Statements.UPDATE_OCUPADO_RESPONSABLE_DISPONIBLE)
					pstm.setLong(1, sesionAspirante.getResponsabledisponible_pid())
					pstm.executeUpdate();
					
					pstm = con.prepareStatement(Statements.UPDATE_REGISTRADOS_PRUEBAS)
					pstm.setLong(1, sesionAspirante.getResponsabledisponible_pid())
					pstm.setLong(2, sesionAspirante.getResponsabledisponible_pid())
					pstm.executeUpdate();
					
					pstm = con.prepareStatement(Statements.UPDATE_sesionaspirante)
					pstm.setString(1, sesionAspirante.getUsername())
					pstm.setLong(2, sesionAspirante.getSesiones_pid())
					pstm.setLong(3, sesionAspirante.getResponsabledisponible_pid())
					pstm.setLong(4, rs.getLong("persistenceid"))
					
					pstm.executeUpdate();
					sesionAspirante.setPersistenceId(rs.getLong("persistenceId"))
					
				}else {
					pstm = con.prepareStatement(Statements.UPDATE_REGISTRADOS)
					pstm.setLong(1, sesionAspirante.getSesiones_pid())
					pstm.setLong(2, sesionAspirante.getSesiones_pid())
					pstm.executeUpdate();
					
					pstm = con.prepareStatement(Statements.UPDATE_OCUPADO_RESPONSABLE_DISPONIBLE)
					pstm.setLong(1, sesionAspirante.getResponsabledisponible_pid())
					pstm.executeUpdate();
					
					pstm = con.prepareStatement(Statements.UPDATE_REGISTRADOS_PRUEBAS)
					pstm.setLong(1, sesionAspirante.getResponsabledisponible_pid())
					pstm.setLong(2, sesionAspirante.getResponsabledisponible_pid())
					pstm.executeUpdate();
					
					pstm = con.prepareStatement(Statements.INSERT_sesionaspirante, Statement.RETURN_GENERATED_KEYS)
					pstm.setString(1, sesionAspirante.getUsername())
					pstm.setLong(2, sesionAspirante.getSesiones_pid())
					pstm.setLong(3, sesionAspirante.getResponsabledisponible_pid())
					
					pstm.executeUpdate();
					rs = pstm.getGeneratedKeys()
					
					if(rs.next()) {
						sesionAspirante.setPersistenceId(rs.getLong("persistenceId"))
					}
				}
				Long caseid=0L
				Integer rechazo=null
				pstm=con.prepareStatement("SELECT caseid, countRechazos from solicituddeadmision where correoelectronico=? limit 1")
				pstm.setString(1, sesionAspirante.getUsername())
				rs = pstm.executeQuery()
				if(rs.next()) {
					caseid= rs.getLong("caseid")
					rechazo= rs.getInt("countRechazos")
				}
				DataInstance asistenciaCollegeBoard= context.getApiClient().getProcessAPI().getProcessDataInstance("asistenciaCollegeBoard", caseid)
				DataInstance asistenciaPsicometrico= context.getApiClient().getProcessAPI().getProcessDataInstance("asistenciaPsicometrico", caseid)
				DataInstance asistenciaEntrevista= context.getApiClient().getProcessAPI().getProcessDataInstance("asistenciaEntrevista", caseid)
				
				asistenciaCollegeBoard.getValue()
				
				pstm=con.prepareStatement("SELECT p.persistenceid, p.cattipoprueba_pid from pruebas p where p.sesion_pid=? and p.cattipoprueba_pid!=1")
				pstm.setLong(1, sesionAspirante.getSesiones_pid())
				rs = pstm.executeQuery()
				while(rs.next()) {
					pstm = con.prepareStatement(Statements.INSERT_ASPIRANTESPRUEBAS, Statement.RETURN_GENERATED_KEYS);
					
					pstm.setString(1,sesionAspirante.getUsername());
					pstm.setLong(2,rs.getInt("persistenceid"));
					pstm.setBoolean(3,false);
					pstm.setInt(4,rechazo);
					pstm.setLong(5,sesionAspirante.getSesiones_pid());
					pstm.setLong(6,rs.getInt("cattipoprueba_pid"));
					pstm.setNull(7, java.sql.Types.NULL);
					pstm.setBoolean(8,(rs.getInt("cattipoprueba_pid")==2)?(Boolean)asistenciaPsicometrico.getValue():(Boolean)asistenciaCollegeBoard.getValue());
					pstm.executeUpdate();
				}
				
				pstm=con.prepareStatement("SELECT p.persistenceid, p.cattipoprueba_pid, rd.persistenceid Responsabledisponible_pid from pruebas p inner join responsabledisponible rd on rd.prueba_pid=p.persistenceid where p.sesion_pid=? and p.cattipoprueba_pid=1 and rd.persistenceid=?")
				pstm.setLong(1, sesionAspirante.getSesiones_pid())
				pstm.setLong(2, sesionAspirante.getResponsabledisponible_pid())
				rs = pstm.executeQuery()
				if(rs.next()) {
					pstm = con.prepareStatement(Statements.INSERT_ASPIRANTESPRUEBAS, Statement.RETURN_GENERATED_KEYS);
					
					pstm.setString(1,sesionAspirante.getUsername());
					pstm.setLong(2,rs.getLong("persistenceid"));
					pstm.setBoolean(3,false);
					pstm.setInt(4,rechazo);
					pstm.setLong(5,sesionAspirante.getSesiones_pid());
					pstm.setInt(6,rs.getInt("cattipoprueba_pid"));
					pstm.setLong(7,sesionAspirante.getResponsabledisponible_pid());
					pstm.setBoolean(8,(Boolean)asistenciaEntrevista.getValue());
					pstm.executeUpdate();
				}else {
					pstm=con.prepareStatement("select persistenceid from pruebas where sesion_pid = ? and cattipoprueba_pid = 1 order by persistenceid desc limit 1");
					pstm.setLong(1, sesionAspirante.getSesiones_pid())
					rs = pstm.executeQuery();
					if(rs.next()) {
						pstm = con.prepareStatement(Statements.INSERT_ASPIRANTESPRUEBAS, Statement.RETURN_GENERATED_KEYS);
						pstm.setString(1,sesionAspirante.getUsername());
						pstm.setLong(2,rs.getLong("persistenceid"));
						pstm.setBoolean(3,false);
						pstm.setInt(4,rechazo);
						pstm.setLong(5,sesionAspirante.getSesiones_pid());
						pstm.setInt(6,1);
						pstm.setNull(7, java.sql.Types.NULL);
						pstm.setBoolean(8,true);
						pstm.executeUpdate();
					}
				}
				con.commit();
				rows.add(sesionAspirante)
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	public Result getSesionesCalendario(String fecha,String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<Calendario> lstCalendario = new ArrayList();
		Calendario calendario = new Calendario();
		String where=" WHERE (s.FECHA_INICIO between ? and  ?) AND s.isEliminado!=true ", consulta =""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
					case "CAMPUS":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" s.campus_pid ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor]"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
				}
			}
			Calendar calendar = dateToCalendar(new SimpleDateFormat("yyyy-MM-dd").parse(fecha));
			calendar.add(Calendar.MONTH, -1);
			Calendar calendar2 = dateToCalendar(new SimpleDateFormat("yyyy-MM-dd").parse(fecha));
			calendar2.add(Calendar.MONTH, 1);
			int lastDate = calendar2.getActualMaximum(Calendar.DATE);
			calendar2.set(Calendar.DATE, lastDate);
			closeCon = validarConexion();
			consulta = Statements.GET_SESIONES_CALENDARIO.replace("[WHERE]", where);
			
			pstm = con.prepareStatement(Statements.GET_SESIONES_CALENDARIO.replace("[WHERE]", where))
			pstm.setDate(1, convert(calendar.getTime()))
			pstm.setDate(2, convert(calendar2.getTime()))
			rs = pstm.executeQuery()
			while(rs.next()) {
				calendario = new Calendario();
				calendario.setColor(rs.getString("color"))
				calendario.setEnd_date(rs.getString("end_date"))
				calendario.setId(rs.getLong("id"))
				calendario.setStart_date(rs.getString("start_date"))
				calendario.setText(rs.getString("text"))
				lstCalendario.add(calendario)
			}
			resultado.setError_info(consulta)
			resultado.setData(lstCalendario)
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result getSesionesCalendarioAspirante(String fecha,Boolean isMedicina,String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<Calendario> lstCalendario = new ArrayList();
		Calendario calendario = new Calendario();
		String where=" WHERE s.isEliminado!=true and (s.ismedicina=false OR s.ismedicina=?) AND s.ultimo_dia_inscripcion is not null AND TO_TIMESTAMP(s.ultimo_dia_inscripcion, 'YYYY-MM-DD HH24:MI:SS')>=now() AND s.FECHA_INICIO between ? AND ? AND s.borrador=false ", consulta =""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
					case "CAMPUS":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" s.campus_pid ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor]"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
					case "BACHILLERATO":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" (s.preparatoria_pid";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor] OR s.preparatoria_pid=0)"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
					case "RESIDENCIA":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" s.tipo ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor]"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
					case "ESTADO":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" (s.estado_pid ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor] OR s.estado_pid=0)"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
					case "PAIS":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" (s.pais_pid ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor] OR s.pais_pid=0)"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
					case "CIUDAD":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" (s.ciudad_pid ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor] OR s.ciudad_pid=0)"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
					case "PERIODO":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" (s.periodo_pid ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor] OR s.periodo_pid=0 OR s.periodo_pid=null)"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
				}
			}
			Calendar calendar = dateToCalendar(new SimpleDateFormat("yyyy-MM-dd").parse(fecha));
			calendar.add(Calendar.MONTH, -1);
			Calendar calendar2 = dateToCalendar(new SimpleDateFormat("yyyy-MM-dd").parse(fecha));
			calendar2.add(Calendar.MONTH, 1);
			int lastDate = calendar2.getActualMaximum(Calendar.DATE);
			calendar2.set(Calendar.DATE, lastDate);
			closeCon = validarConexion();
			consulta = Statements.GET_SESIONES_CALENDARIOASPIRANTE.replace("[WHERE]", where);
			
			pstm = con.prepareStatement(Statements.GET_SESIONES_CALENDARIOASPIRANTE.replace("[WHERE]", where))
			pstm.setBoolean(1, isMedicina)
			pstm.setDate(2, convert(calendar.getTime()))
			pstm.setDate(3, convert(calendar2.getTime()))
			rs = pstm.executeQuery()
			while(rs.next()) {
				calendario = new Calendario();
				calendario.setColor(rs.getString("color"))
				calendario.setEnd_date(rs.getString("end_date"))
				calendario.setId(rs.getLong("id"))
				calendario.setStart_date(rs.getString("start_date"))
				calendario.setText(rs.getString("text"))
				lstCalendario.add(calendario)
			}
			resultado.setError_info(consulta)
			resultado.setData(lstCalendario)
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError_info(consulta)
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result getSesionesReporte(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<Calendario> lstCalendario = new ArrayList();
		Calendario calendario = new Calendario();
		String where=" WHERE s.isEliminado!=true AND  s.borrador=false ", consulta ="", errorlog=""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				errorlog+="COLUMNA:" + filtro.get("columna") +", VALOR:"+filtro.get("valor")
				switch(filtro.get("columna")) {
					case "CAMPUS":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" s.campus_pid ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor]"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
					case "BACHILLERATO":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" (s.preparatoria_pid";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor] OR s.preparatoria_pid=0)"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
					case "RESIDENCIA":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" s.tipo ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor]"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
					case "ESTADO":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" (s.estado_pid ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor] OR s.estado_pid=0)"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
					case "PAIS":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" (s.pais_pid ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor] OR s.pais_pid=0)"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
					case "CIUDAD":
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" (s.ciudad_pid ";
					if(filtro.get("operador").equals("Igual a")) {
						where+="=[valor] OR s.ciudad_pid=0)"
					}else {
						where+="LIKE '%[valor]%'"
					}
					where = where.replace("[valor]", filtro.get("valor")+"")
					break;
				}
			}
			errorlog+=" WHERE:" + where
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_SESIONES_CALENDARIOASPIRANTE.replace("[WHERE]", where).replace("and p.registrados<p.cupo", ""))
			rs = pstm.executeQuery()
			while(rs.next()) {
				calendario = new Calendario();
				calendario.setColor(rs.getString("color"))
				calendario.setEnd_date(rs.getString("end_date"))
				calendario.setId(rs.getLong("id"))
				calendario.setStart_date(rs.getString("start_date"))
				calendario.setText(rs.getString("text"))
				lstCalendario.add(calendario)
			}
			resultado.setError_info(consulta)
			resultado.setData(lstCalendario)
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError_info(errorlog)
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}


	
	
	public Result getSesion(Long persistenceId, RestAPIContext context) {


		Result resultado = new Result();
		Boolean closeCon = false;
		List<SesionCustom> lstSesion = new ArrayList();
		SesionCustom sesion = new SesionCustom()
		String where=" ", consulta =""
		try {

			closeCon = validarConexion();
	
			
			pstm = con.prepareStatement(Statements.GET_SESION_ID)
			pstm.setLong(1, persistenceId)
			rs = pstm.executeQuery()
			if(rs.next()) {
				sesion = new SesionCustom();
				sesion.setBachillerato_pid(rs.getLong("PREPARATORIA_PID"))
				sesion.setBorrador(rs.getBoolean("borrador"))
				sesion.setDescripcion(rs.getString("descripcion"))
				sesion.setEstado_pid(rs.getLong("estado_pid"))
				sesion.setFecha_inicio(rs.getString("fecha_inicio"))
				sesion.setIsmedicina(rs.getBoolean("ismedicina"))
				sesion.setNombre(rs.getString("nombre"))
				sesion.setPais_pid(rs.getLong("pais_pid"))
				sesion.setPersistenceId(rs.getLong("persistenceId"))
				sesion.setPersistenceVersion(rs.getLong("persistenceVersion"))
				sesion.setPruebas(new ArrayList())
				sesion.setCampus_pid(rs.getLong("campus_pid"))
				sesion.setTipo(rs.getString("tipo"))
				sesion.setCiudad_pid(rs.getLong("ciudad_pid"))
				sesion.setIsEliminado(rs.getBoolean("isEliminado"))
				sesion.setUltimo_dia_inscripcion(rs.getString("ultimo_dia_inscripcion"))
				sesion.setPeriodo_pid(rs.getLong("periodo_pid"))
				pstm = con.prepareStatement(Statements.GET_PRUEBAS_SESION_PID)
				pstm.setLong(1, sesion.getPersistenceId())
				rs = pstm.executeQuery()
				while(rs.next()) {
					
					PruebaCustom p = new PruebaCustom()
					p.setAplicacion(rs.getString("aplicacion"))
					p.setCalle(rs.getString("calle"))
					p.setCampus_pid(rs.getLong("campus_pid"))
					p.setCattipoprueba_pid(rs.getLong("cattipoprueba_pid"))
					p.setCodigo_postal(rs.getString("codigo_postal"))
					p.setColonia(rs.getString("colonia"))
					p.setCupo(rs.getInt("cupo"))
					p.setDescripcion(rs.getString("descripcion"))
					p.setDuracion(rs.getString("duracion"))
					p.setEntrada(rs.getString("entrada"))
					p.setEstado_pid(rs.getLong("estado_pid"))
					p.setIseliminado(rs.getBoolean("iseliminado"))
					p.setLugar(rs.getString("lugar"))
					p.setMunicipio(rs.getString("municipio"))
					p.setNombre(rs.getString("nombre"))
					p.setNumero_ext(rs.getString("numero_ext"))
					p.setNumero_int(rs.getString("numero_int"))
					p.setPais_pid(rs.getLong("pais_pid"))
					p.setPersistenceId(rs.getLong("persistenceId"))
					p.setPersistenceVersion(rs.getLong("persistenceVersion"))
					p.setRegistrados(rs.getInt("registrados"))
					p.setSalida(rs.getString("salida"))
					p.setSesion_pid(rs.getLong("sesion_pid"))
					p.setUltimo_dia_inscripcion(rs.getString("ultimo_dia_inscripcion"))
					p.setOnline(rs.getBoolean("online"))
					p.setTipo(new CatTipoPrueba())
					p.getTipo().setDescripcion(rs.getString("tipo"))
					
					p.setPsicologos(new ArrayList())
					
					User usr;
					UserMembership membership
					if(rs.getLong("RESPONSABLEID")>0) {
						usr = context.getApiClient().getIdentityAPI().getUser(rs.getLong("RESPONSABLEID"))
						membership=context.getApiClient().getIdentityAPI().getUserMemberships(usr.getId(), 0, 100, UserMembershipCriterion.GROUP_NAME_ASC).get(0)
					}
					
					if(sesion.getPruebas().contains(p)) {
						ResponsableCustom psi = new ResponsableCustom()
						ResponsableDisponible fd = new ResponsableDisponible()
						fd.setDisponible(rs.getBoolean("disponible"))
						fd.setOcupado(rs.getBoolean("ocupado"))
						fd.setHorario(rs.getString("horario"))
						fd.setPersistenceId(rs.getLong("rid"))
						fd.setResponsableId(rs.getLong("RESPONSABLEID"))
						psi.setLicenciaturas(rs.getString("licenciaturas"))
						psi.setPersistenceId(rs.getLong("rid"))
						
						try {
							psi.setFirstname(usr.getFirstName())
							psi.setLastname(usr.getLastName())
							psi.setGrupo(membership.groupName)
							psi.setRol(membership.roleName)
						}catch(Exception e) {
							resultado.setError_info(e.getMessage())
						}
						
						
						psi.setId(rs.getLong("RESPONSABLEID"))
					
						if(sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().contains(psi)) {
							sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().get(sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().indexOf(psi)).getLstFechasDisponibles().add(fd)
							
						}else {
							if(fd.getResponsableId()>0) {
							psi.setLstFechasDisponibles(new ArrayList())
							psi.getLstFechasDisponibles().add(fd)
							sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().add(psi)
							}
						}
					}else {
						ResponsableCustom psi = new ResponsableCustom()
						ResponsableDisponible fd = new ResponsableDisponible()
						fd.setDisponible(rs.getBoolean("disponible"))
						fd.setOcupado(rs.getBoolean("ocupado"))
						fd.setHorario(rs.getString("horario"))
						psi.setId(rs.getLong("RESPONSABLEID"))
						fd.setPersistenceId(rs.getLong("rid"))
						fd.setResponsableId(rs.getLong("RESPONSABLEID"))
						psi.setLicenciaturas(rs.getString("licenciaturas"))
						psi.setPersistenceId(rs.getLong("rid"))
						psi.setLstFechasDisponibles(new ArrayList())
						try {
							psi.setGrupo(membership.groupName)
							psi.setRol(membership.roleName)
							psi.setFirstname(usr.getFirstName())
							psi.setLastname(usr.getLastName())
							
							psi.getLstFechasDisponibles().add(fd)
							p.getPsicologos().add(psi)
						}catch(Exception e) {
							resultado.setError_info(e.getMessage())
						}
						
						
						
						sesion.getPruebas().add(p)
					}
				}
			}
			lstSesion.add(sesion)
			resultado.setError_info(consulta)
			resultado.setData(lstSesion)
			resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result insertFaltas(String username) {
		List<Long> pruebas = new ArrayList<Long>()
		pstm = con.prepareStatement(Statements.GET_PRUEBAS_ASPIRANTE)
		pstm.setString(1,  username)
		rs = pstm.executeQuery()
		while(rs.next()) {
			pruebas.add(rs.getLong("prueba_pid"))
		}
		
		for(Long pa:pruebas) {
			pstm = con.prepareStatement(Statements.GET_ASISTENCIA_PRUEBA_FALTA)
			pstm.setString(1, username)
			pstm.setLong(2, pa)
			rs = pstm.executeQuery()
			if(!rs.next()) {
				pstm = con.prepareStatement(Statements.INSERT_PASEDELISTA, Statement.RETURN_GENERATED_KEYS)
				pstm.setLong(1, pa);
				pstm.setString(2, username);
				pstm.setBoolean(3,false);
				pstm.setString(4,"");
				
				pstm.executeUpdate();
			}
		}
		
	}
	
	
	public Result getSesionAspirante(Long persistenceId, RestAPIContext context) {
		
		
				Result resultado = new Result();
				Boolean closeCon = false;
				List<SesionCustom> lstSesion = new ArrayList();
				SesionCustom sesion = new SesionCustom()
				String where=" ", consulta =""
				String username = context.getApiSession().getUserName()
				try {
		
					closeCon = validarConexion();
			
					
					pstm = con.prepareStatement(Statements.GET_SESION_ID)
					pstm.setLong(1, persistenceId)
					rs = pstm.executeQuery()
					if(rs.next()) {
						sesion = new SesionCustom();
						sesion.setBachillerato_pid(rs.getLong("PREPARATORIA_PID"))
						sesion.setBorrador(rs.getBoolean("borrador"))
						sesion.setDescripcion(rs.getString("descripcion"))
						sesion.setEstado_pid(rs.getLong("estado_pid"))
						sesion.setFecha_inicio(rs.getString("fecha_inicio"))
						sesion.setIsmedicina(rs.getBoolean("ismedicina"))
						sesion.setNombre(rs.getString("nombre"))
						sesion.setPais_pid(rs.getLong("pais_pid"))
						sesion.setPersistenceId(rs.getLong("persistenceId"))
						sesion.setPersistenceVersion(rs.getLong("persistenceVersion"))
						sesion.setPruebas(new ArrayList())
						sesion.setCampus_pid(rs.getLong("campus_pid"))
						sesion.setTipo(rs.getString("tipo"))
						sesion.setCiudad_pid(rs.getLong("ciudad_pid"))
						sesion.setUltimo_dia_inscripcion(rs.getString("ultimo_dia_inscripcion"))
						pstm = con.prepareStatement(Statements.GET_PRUEBAS_SESION_PID_ASPIRANTE)
						pstm.setLong(1, sesion.getPersistenceId())
						rs = pstm.executeQuery()
						while(rs.next()) {
							
							PruebaCustom p = new PruebaCustom()
							p.setAplicacion(rs.getString("aplicacion"))
							p.setCalle(rs.getString("calle"))
							p.setCampus_pid(rs.getLong("campus_pid"))
							p.setCattipoprueba_pid(rs.getLong("cattipoprueba_pid"))
							p.setCodigo_postal(rs.getString("codigo_postal"))
							p.setColonia(rs.getString("colonia"))
							p.setCupo(rs.getInt("cupo"))
							p.setDescripcion(rs.getString("descripcion"))
							p.setDuracion(rs.getString("duracion"))
							p.setEntrada(rs.getString("entrada"))
							p.setEstado_pid(rs.getLong("estado_pid"))
							p.setIseliminado(rs.getBoolean("iseliminado"))
							p.setLugar(rs.getString("lugar"))
							p.setMunicipio(rs.getString("municipio"))
							p.setNombre(rs.getString("nombre"))
							p.setNumero_ext(rs.getString("numero_ext"))
							p.setNumero_int(rs.getString("numero_int"))
							p.setPais_pid(rs.getLong("pais_pid"))
							p.setPersistenceId(rs.getLong("persistenceId"))
							p.setPersistenceVersion(rs.getLong("persistenceVersion"))
							p.setRegistrados(rs.getInt("registrados"))
							p.setSalida(rs.getString("salida"))
							p.setSesion_pid(rs.getLong("sesion_pid"))
							p.setUltimo_dia_inscripcion(rs.getString("ultimo_dia_inscripcion"))
							p.setOnline(rs.getBoolean("online"))
							p.setTipo(new CatTipoPrueba())
							p.getTipo().setDescripcion(rs.getString("tipo"))
							p.setPsicologos(new ArrayList())
							
							User usr;
							UserMembership membership
							if(rs.getLong("RESPONSABLEID")>0) {
								usr = context.getApiClient().getIdentityAPI().getUser(rs.getLong("RESPONSABLEID"))
								membership=context.getApiClient().getIdentityAPI().getUserMemberships(usr.getId(), 0, 100, UserMembershipCriterion.GROUP_NAME_ASC).get(0)
							}
							
							if(sesion.getPruebas().contains(p)) {
								ResponsableCustom psi = new ResponsableCustom()
								ResponsableDisponible fd = new ResponsableDisponible()
								fd.setDisponible(rs.getBoolean("disponible"))
								fd.setOcupado(rs.getBoolean("ocupado"))
								fd.setHorario(rs.getString("horario"))
								fd.setPersistenceId(rs.getLong("rid"))
								fd.setResponsableId(rs.getLong("RESPONSABLEID"))
								psi.setLicenciaturas(rs.getString("licenciaturas"))
								psi.setPersistenceId(rs.getLong("rid"))
								
								try {
									psi.setFirstname(usr.getFirstName())
									psi.setLastname(usr.getLastName())
									psi.setGrupo(membership.groupName)
									psi.setRol(membership.roleName)
								}catch(Exception e) {
									resultado.setError_info(e.getMessage())
								}
								
								
								psi.setId(rs.getLong("RESPONSABLEID"))
							
								if(sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().contains(psi)) {
									sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().get(sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().indexOf(psi)).getLstFechasDisponibles().add(fd)
									
								}else {
									if(fd.getResponsableId()>0) {
									psi.setLstFechasDisponibles(new ArrayList())
									psi.getLstFechasDisponibles().add(fd)
									sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().add(psi)
									}
								}
							}else {
								ResponsableCustom psi = new ResponsableCustom()
								ResponsableDisponible fd = new ResponsableDisponible()
								fd.setDisponible(rs.getBoolean("disponible"))
								fd.setOcupado(rs.getBoolean("ocupado"))
								fd.setHorario(rs.getString("horario"))
								psi.setId(rs.getLong("RESPONSABLEID"))
								fd.setPersistenceId(rs.getLong("rid"))
								fd.setResponsableId(rs.getLong("RESPONSABLEID"))
								psi.setLicenciaturas(rs.getString("licenciaturas"))
								psi.setPersistenceId(rs.getLong("rid"))
								psi.setLstFechasDisponibles(new ArrayList())
								try {
									psi.setGrupo(membership.groupName)
									psi.setRol(membership.roleName)
									psi.setFirstname(usr.getFirstName())
									psi.setLastname(usr.getLastName())
									
									psi.getLstFechasDisponibles().add(fd)
									p.getPsicologos().add(psi)
								}catch(Exception e) {
									resultado.setError_info(e.getMessage())
								}
								
								
								
								sesion.getPruebas().add(p)
							}
						}
					}
					/*pstm = con.prepareStatement(Statements.GET_PRUEBAS_SESION_ENTREVISTA_ASPIRANTE)
					pstm.setLong(1, sesion.getPersistenceId())
					pstm.setString(2, username)
					pstm.setLong(3, sesion.getPersistenceId())
					pstm.setString(4, username)
					rs=pstm.executeQuery()
					if(rs.next()) {
						
						PruebaCustom p = new PruebaCustom()
						p.setAplicacion(rs.getString("aplicacion"))
						p.setCalle(rs.getString("calle"))
						p.setCampus_pid(rs.getLong("campus_pid"))
						p.setCattipoprueba_pid(rs.getLong("cattipoprueba_pid"))
						p.setCodigo_postal(rs.getString("codigo_postal"))
						p.setColonia(rs.getString("colonia"))
						p.setCupo(rs.getInt("cupo"))
						p.setDescripcion(rs.getString("descripcion"))
						p.setDuracion(rs.getString("duracion"))
						p.setEntrada(rs.getString("entrada"))
						p.setEstado_pid(rs.getLong("estado_pid"))
						p.setIseliminado(rs.getBoolean("iseliminado"))
						p.setLugar(rs.getString("lugar"))
						p.setMunicipio(rs.getString("municipio"))
						p.setNombre(rs.getString("nombre"))
						p.setNumero_ext(rs.getString("numero_ext"))
						p.setNumero_int(rs.getString("numero_int"))
						p.setPais_pid(rs.getLong("pais_pid"))
						p.setPersistenceId(rs.getLong("persistenceId"))
						p.setPersistenceVersion(rs.getLong("persistenceVersion"))
						p.setRegistrados(rs.getInt("registrados"))
						p.setSalida(rs.getString("salida"))
						p.setSesion_pid(rs.getLong("sesion_pid"))
						p.setUltimo_dia_inscripcion(rs.getString("ultimo_dia_inscripcion"))
						p.setTipo(new CatTipoPrueba())
						p.getTipo().setDescripcion(rs.getString("tipo"))
						p.setPsicologos(new ArrayList())
						
						User usr;
						UserMembership membership
						if(rs.getLong("RESPONSABLEID")>0) {
							usr = context.getApiClient().getIdentityAPI().getUser(rs.getLong("RESPONSABLEID"))
							membership=context.getApiClient().getIdentityAPI().getUserMemberships(usr.getId(), 0, 100, UserMembershipCriterion.GROUP_NAME_ASC).get(0)
						}
						sesion.getPruebas().add(p)
						pstm = con.prepareStatement(Statements.GET_HORARIOS_PRUEBAS_ENTREVISTA_ASPIRANTE)
						pstm.setLong(1, p.getPersistenceId())
						rs = pstm.executeQuery()
						while(rs.next()) {
							ResponsableCustom psi = new ResponsableCustom()
							ResponsableDisponible fd = new ResponsableDisponible()
							fd.setDisponible(rs.getBoolean("disponible"))
							fd.setOcupado(rs.getBoolean("ocupado"))
							fd.setHorario(rs.getString("horario"))
							fd.setPersistenceId(rs.getLong("rid"))
							fd.setResponsableId(rs.getLong("RESPONSABLEID"))
							psi.setLicenciaturas(rs.getString("licenciaturas"))
							psi.setPersistenceId(rs.getLong("rid"))
							
							try {
								psi.setFirstname(usr.getFirstName())
								psi.setLastname(usr.getLastName())
								psi.setGrupo(membership.groupName)
								psi.setRol(membership.roleName)
							}catch(Exception e) {
								resultado.setError_info(e.getMessage())
							}
							
							
							psi.setId(rs.getLong("RESPONSABLEID"))
						
							if(sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().contains(psi)) {
								sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().get(sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().indexOf(psi)).getLstFechasDisponibles().add(fd)
								
							}else {
								if(fd.getResponsableId()>0) {
								psi.setLstFechasDisponibles(new ArrayList())
								psi.getLstFechasDisponibles().add(fd)
								sesion.getPruebas().get(sesion.getPruebas().indexOf(p)).getPsicologos().add(psi)
								}
							}
						}
						
					}else {
						throw new Exception("Los psicólogos disponibles para esta sesión son especializados para otra licenciatura")
					}*/
					lstSesion.add(sesion)
					resultado.setError_info(consulta)
					resultado.setData(lstSesion)
					resultado.setSuccess(true)
				} catch (Exception e) {
					resultado.setSuccess(false);
					resultado.setError(e.getMessage());
				}finally {
					if(closeCon) {
						new DBConnect().closeObj(con, stm, rs, pstm)
					}
				}
				return resultado;
			}
	
	
	
	
	
	
	public Result getSesionesCalendarizadas(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		List<PruebasCustom> lstSesion = new ArrayList();
		String where ="", orderby="ORDER BY ", errorlog="", role="", group="", residencia="WHERE ( (CAST(ultimaaplicacion AS DATE) + integer '2') >= (CAST(TO_CHAR(NOW(),'YYYY-MM-DD') as DATE) ) )";
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				String consulta = Statements.GET_SESIONESCALENDARIZADAS_REGISTRADOS;
				PruebaCustom row = new PruebaCustom();
				List<PruebasCustom> rows = new ArrayList<PruebasCustom>();
				closeCon = validarConexion();
				errorlog+="llego a filtro "+object.lstFiltro.toString()
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						
						
					case "TIPO DE PRUEBA, RESIDENCIA":
						residencia +=" AND ( LOWER(residencia) LIKE LOWER('%[valor]%')";
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						
						residencia +=" OR LOWER(tipo_prueba) LIKE LOWER('%[valor]%') )";
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						
						break;
					case "ID":
						where +=" AND CAST(Pruebas.persistenceid as varchar)";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE DE LA PRUEBA":
						where +=" AND LOWER(Pruebas.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CUPO DE LA PRUEBA":
						where +=" AND CAST(Pruebas.cupo as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ALUMNOS REGISTRADOS":
						if (residencia.contains("WHERE")) {
							residencia += " AND "
						} else {
							residencia += " WHERE "
						}
						
						residencia +=" CAST(registrados as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							residencia+="='[valor]'"
						}else {
							residencia+="LIKE '%[valor]%'"
						}
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						
						/*where +=" AND CAST(Pruebas.registrados as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))*/
						break;
						
						
					case "RESIDENCIA":
						if (residencia.contains("WHERE")) {
							residencia += " AND "
						} else {
							residencia += " WHERE "
						}
						residencia +=" LOWER(residencia) ";
						if(filtro.get("operador").equals("Igual a")) {
							residencia+="=LOWER('[valor]')"
						}else {
							residencia+="LIKE LOWER('%[valor]%')"
						}
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						break;
						
					case "FECHA":
						where +=" AND  ( LOWER( CAST(TO_CHAR(Pruebas.aplicacion, 'DD-MM-YYYY') as varchar)) LIKE LOWER('%[valor]%') ";
						where += "OR LOWER(Pruebas.entrada) LIKE LOWER('%[valor]%') "
						where += "OR LOWER(Pruebas.salida) LIKE LOWER('%[valor]%') )"
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "LUGAR":
						where +=" AND LOWER(Pruebas.lugar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
						
					case "TIPO DE PRUEBA":
						where +=" AND LOWER(ctipoprueba.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
								where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE DE LA SESION":
						where +=" AND LOWER(Sesion.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
								where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					}
					
					
				}
					errorlog+="llego al orderby "
					switch(object.orderby) {
						
						case "ID":
						orderby+="pruebas_id";
						break;
						case "NOMBRE":
						orderby+="Pruebas.nombre";
						break;
						case "ALUMNOS REGISTRADOS":
						orderby+="registrados";
						break;
						case "CUPO":
						orderby+="Pruebas.cupo";
						break;
						case "RESIDENCIA":
						orderby+="Sesion.tipo";
						break;
						case "FECHA":
						orderby+="Pruebas.aplicacion";
						break;
						case "LUGAR":
						orderby+="Pruebas.lugar";
						break;
						case "TIPO_PRUEBA":
						orderby+="ctipoprueba.descripcion";
						break;
						case "NOMBRE SESION":
						orderby+="Sesion.nombre";
						break;
						default:
						orderby+="Pruebas.aplicacion"
						break;
						
					}
					errorlog+="paso el order "
					orderby+=" "+object.orientation;
					consulta=consulta.replace("[WHERE]", where);
					
					errorlog+="paso el where"
					String consulta_aspirante =  Statements.EXT_SESIONESCALENDARIZADAS_REGISTRADOS.replace("[RESPONSABLE]",object.usuario.toString());
					
					String conteo = Statements.COUNT_SESIONESCALENDARIZADAS_REGISTRADOS;
					conteo=conteo.replace("[COUNTASPIRANTES]", consulta_aspirante)
					conteo=conteo.replace("[WHERE]", where);
					conteo=conteo.replace("[RESIDENCIA]", residencia);
					pstm = con.prepareStatement(conteo)
					
					pstm.setInt(1, object.usuario)
					
					rs= pstm.executeQuery()
					if(rs.next()) {
						resultado.setTotalRegistros(rs.getInt("registros"))
					}
					errorlog+="paso el registro"
					
					
					
					consulta=consulta.replace("[ORDERBY]", orderby)
					consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
					consulta=consulta.replace("[RESIDENCIA]", residencia)
					consulta=consulta.replace("[COUNTASPIRANTES]", consulta_aspirante)
					errorlog+="conteo exitoso "
					
					pstm = con.prepareStatement(consulta)
					pstm.setInt(1, object.usuario)
					pstm.setInt(2, object.limit)
					pstm.setInt(3, object.offset)
					
					PruebasCustom Pruebas = new PruebasCustom();
					rs = pstm.executeQuery()
					errorlog+="Listado "
					while(rs.next()) {
						
						row = new PruebaCustom();
						row.setNombre(rs.getString("nombre"))
						row.setRegistrados(rs.getInt("registrados"));
						row.setLugar(rs.getString("lugar"));
						row.setPersistenceId(rs.getLong("pruebas_id"));
						row.setCupo(rs.getInt("cupo"));
						row.setTipo(new CatTipoPrueba())
						row.getTipo().setDescripcion(rs.getString("tipo_prueba"));
						row.setEntrada(rs.getString("entrada"));
						row.setSalida(rs.getString("salida"))
						
						SesionCustom row2 = new SesionCustom();
						row2.setFecha_inicio(rs.getString("aplicacion"));
						row2.setTipo(rs.getString("residencia"));
						row2.setPersistenceId(rs.getLong("sesiones_id"));
						row2.setNombre(rs.getString("nombre_sesion"));
						
						
						Pruebas = new PruebasCustom();
						Pruebas.setPrueba(row);
						Pruebas.setSesion(row2);
						
						rows.add(Pruebas)
					}
					
				resultado.setError_info(consulta +" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
			} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
				resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getSesionesAspirantes(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
		String where ="", orderby="ORDER BY AP.persistenceid, ", errorlog="", role="", group="", orderbyUsuario="ORDER BY " ;
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				
				
				String consulta = Statements.GET_ASPIRANTEPRUEBAASISTIOYREAGENDOACTIVOS;
				SesionesAspiranteCustom row = new SesionesAspiranteCustom();
				List<SesionesAspiranteCustom> rows = new ArrayList<SesionesAspiranteCustom>();
				List<Map<String, Object>> aspirante = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				
				
				int tipo = 0;
				pstm = con.prepareStatement(Statements.GET_TIPOPRUEBA)
				pstm.setInt(1, object.prueba)
				rs= pstm.executeQuery();
				if(rs.next()) {
					tipo = (rs.getInt("tipoprueba_pid"))
				}
				
				String SSA = "";
				pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
				rs= pstm.executeQuery();
				if(rs.next()) {
					SSA = rs.getString("valor")
				}
				
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						
						
					case "NOMBRE, EMAIL, CURP":
						where +=" AND ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.curp) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CAMPUS, PROGRAMA, INGRESO":
						where +=" AND ( LOWER(campus.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(gestionescolar.nombre) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(CPO.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PROCEDENCIA, PREPARATORIA, PROMEDIO":
						/*where +=" AND ( LOWER(estado.DESCRIPCION) like lower('%[valor]%') ";*/
						where +=" AND (LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%')"
						where = where.replace("[valor]", filtro.get("valor"))
					
						where +="  OR LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.bachillerato ELSE prepa.descripcion END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
					
						where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ID BANNER":
						where +=" AND CAST(DS.idbanner as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE":
						where +="  AND LOWER(concat(sda.primernombre,' ', sda.segundonombre,' ',sda.apellidopaterno,' ',sda.apellidomaterno)) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "EMAIL":
						where +=" AND LOWER(sda.correoelectronico) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PROMEDIO":
                            errorlog+="PROMEDIO"
							where +=" AND  CAST(sda.PROMEDIOGENERAL as varchar) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+=" ='[valor]' "
							}else {
								where+="LIKE '%[valor]%'"
							}
                            where = where.replace("[valor]", filtro.get("valor"))
                            break;
							
					case "PREPARATORIA":
							where +=" AND LOWER(prepa.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where= where.replace("[valor]", filtro.get("valor"))
							break;
							
					case "RESIDENCIA":
						where +=" AND LOWER(R.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "SEXO":
						where +=" AND LOWER(sx.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "LICENCIATURA":
						where +=" AND LOWER(gestionescolar.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "UNIVERSIDAD":
						where +=" AND LOWER(campus.DESCRIPCION) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "HORA DE LA ENTREVISTA":
						if(tipo == 1) {
							where+=" AND CAST(rd.horario as varchar) "
						}else {
							where+=" AND (CAST( concat(p.entrada,' - ',p.salida) as varchar)) "
						}
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ASISTENCIA":
						where +=" AND (PL.asistencia ";
						where+="= [valor] "
						where = where.replace("[valor]",  (filtro.get("valor").toString().equals("Sí")?"true)":"false OR PL.asistencia is NULL)"))
						break;
						
					
						
					}
					
				}
				
				errorlog+="llego al orderby "
				switch(object.orderby) {
					
					case "IDBANNER":
					orderby+="DS.idbanner";
					break;
					case "NOMBRE":
					orderby+="sda.apellidopaterno";
					break;
					case "EMAIL":
					orderby+="SDA.correoelectronico";
					break;
					case "PREPARATORIA":
					orderby+="prepa.descripcion"
					break;
					case "CAMPUS":
					orderby+="campus"
					break;
					case "RESIDENCIA":
					orderby+="residencia";
					break;
					case "CURP":
					orderby+="SDA.curp";
					break;
					case "PROCEDENCIA":
					orderby+="preparatoriaEstado";
					break;
					case "INGRESO":
					orderby+="CPO.descripcion";
					break;
					case "SEXO":
					orderby+="sx.descripcion";
					break;
					case "LICENCIATURA":
					orderby+="gestionescolar.nombre";
					break;
					case "PROMEDIO":
					orderby+="SDA.promediogeneral";
					break;
					case "ASISTENCIA":
					orderby+= "AP.asistencia";
					break;
					
					case "HORARIO":
					orderby+= "RD.horario";
					break;
					
					default:
					orderby+="AP.username"
					break;
					
				}
						
				//orderby+="SA.username"
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[WHERE]", where);
				if(tipo == 1) {
					consulta=consulta.replace("[ENTREVISTA]", "AND RD.responsableid ="+object.usuario)
				}else {
					consulta=consulta.replace("[ENTREVISTA]", "")
				}
				 errorlog += "conteo= "+ consulta.replace("distinct on (AP.persistenceid) AP.persistenceid,P.nombre as nombre_prueba,P.Lugar as lugar_prueba,DS.IDBANNER,sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre,SDA.CORREOELECTRONICO,SDA.CURP,campus.descripcion AS campus,gestionescolar.nombre AS licenciatura, CPO.descripcion as periodo,CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia,sda.PROMEDIOGENERAL, CASE WHEN prepa.descripcion = 'Otro' THEN sda.bachillerato ELSE prepa.descripcion END AS preparatoria, R.descripcion as residencia, sx.descripcion as sexo, PL.ASISTENCIA, P.aplicacion, c.descripcion as tipo_prueba, case when C.persistenceid=1 then rd.horario  else concat(p.entrada,' - ',p.salida) end as horario, RD.PERSISTENCEID AS RD, DS.CASEID, sda.urlfoto,le.descripcion as lugarexamen,sda.telefonocelular,DS.cbCoincide,AP.acreditado,c.PERSISTENCEID as tipoprueba_pid,AP.USERNAME", " count(distinct (AP.persistenceid)) as  registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "");
				pstm = con.prepareStatement(consulta.replace("distinct on (AP.persistenceid) AP.persistenceid,P.nombre as nombre_prueba,P.Lugar as lugar_prueba,DS.IDBANNER,sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre,SDA.CORREOELECTRONICO,SDA.CURP,campus.descripcion AS campus,gestionescolar.nombre AS licenciatura, CPO.descripcion as periodo,CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia,sda.PROMEDIOGENERAL, CASE WHEN prepa.descripcion = 'Otro' THEN sda.bachillerato ELSE prepa.descripcion END AS preparatoria, R.descripcion as residencia, sx.descripcion as sexo, PL.ASISTENCIA, P.aplicacion, c.descripcion as tipo_prueba, case when C.persistenceid=1 then rd.horario  else concat(p.entrada,' - ',p.salida) end as horario, RD.PERSISTENCEID AS RD, DS.CASEID, sda.urlfoto,le.descripcion as lugarexamen,sda.telefonocelular,DS.cbCoincide,AP.acreditado,c.PERSISTENCEID as tipoprueba_pid,AP.USERNAME", " count(distinct (AP.persistenceid)) as  registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				pstm.setInt(1, object.prueba);
				pstm.setInt(2, object.sesion);
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				
				
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog+="conteo exitoso "+ consulta;
				
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.prueba)
				pstm.setInt(2, object.sesion)
				pstm.setInt(3, object.limit)
				pstm.setInt(4, object.offset)
				
				rs = pstm.executeQuery()
				
				aspirante = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();

					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
							String encoded = "";
							boolean noAzure = false;
							try {
								String urlFoto = rs.getString("urlfoto");
								if(urlFoto != null && !urlFoto.isEmpty()) {
									noAzure = true;
									columns.put("fotografiab64", rs.getString("urlfoto") +SSA);
								}else {
									List<Document>doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
									for(Document doc : doc1) {
										encoded = "../API/formsDocumentImage?document="+doc.getId();
										columns.put("fotografiab64", encoded);
									}
								}
								
							}catch(Exception e) {
								//columns.put("fotografiab64", "");
								if(noAzure){
									columns.put("fotografiab64", "");
								}
								errorlog+= ""+e.getMessage();
							}
						}
					}
					//aspirante.add(columns);
					rows.add(columns);
				}
						
				resultado.setError_info(consulta+" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
				
			} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
				resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	
	public Result getSesionesCalendarizadasPasadas(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		List<PruebasCustom> lstSesion = new ArrayList();
		String where ="", orderby="ORDER BY ", errorlog="", role="", campus="", group="",residencia="";
		List<String> lstGrupo = new ArrayList<String>();
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				String consulta = Statements.GET_SESIONESCALENDARIZADASPASADAS_REGISTRADOS;
				//AND CAST(P.aplicacion AS DATE) [ORDEN] CAST([FECHA] AS DATE)
				PruebaCustom row = new PruebaCustom();
				List<PruebasCustom> rows = new ArrayList<PruebasCustom>();
				closeCon = validarConexion();
				errorlog+="llego a filtro "+object.lstFiltro.toString()
				
				def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
				List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
				
				userLogged = context.getApiSession().getUserId();
				
				List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
				for(UserMembership objUserMembership : lstUserMembership) {
					for(CatCampus rowGrupo : lstCatCampus) {
						if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita()) && !lstGrupo.contains(rowGrupo.getDescripcion()) ) {
							lstGrupo.add(rowGrupo.getDescripcion());
							break;
						}
					}
				}
				
				if(lstGrupo.size()>0) {
					campus+=" AND ("
				}
				for(Integer i=0; i<lstGrupo.size(); i++) {
					String campusMiembro=lstGrupo.get(i);
					campus+="campus.descripcion='"+campusMiembro+"'"
					if(i==(lstGrupo.size()-1)) {
						campus+=") "
					}
					else {
						campus+=" OR "
					}
				}
				
				if(object.campus != null) {
					campus +=" AND LOWER(campus.DESCRIPCION) = LOWER('"+object.campus+"')";
					where +=" AND LOWER(campus.DESCRIPCION)  = LOWER('"+object.campus+"')";
				}
			
				Boolean isHaving = false, TR= false, AR= false;
				String  TRs= "", ARs ="";
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						
						
					case "TIPO DE PRUEBA, RESIDENCIA":
						residencia +=" WHERE ( LOWER(residencia) LIKE LOWER('%[valor]%')";
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						
						residencia +=" OR LOWER(tipo_prueba) LIKE LOWER('%[valor]%') )";
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						TRs = ""+filtro.get("valor");
						TR = true;
						break;
						
					case "FECHA, LUGAR":
						where +=" AND  ( LOWER( CAST(TO_CHAR(Pruebas.aplicacion, 'DD-MM-YYYY') as varchar)) LIKE LOWER('%[valor]%') ";
						where += "OR LOWER(Pruebas.entrada) LIKE LOWER('%[valor]%') "
						where += "OR LOWER(Pruebas.salida) LIKE LOWER('%[valor]%') "
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(Pruebas.lugar) LIKE LOWER('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ID":
						where +=" AND CAST(Pruebas.persistenceid as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE DE LA PRUEBA":
						where +=" AND LOWER(Pruebas.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CAMPUS":
						errorlog+="CAMPUS"
						campus +=" AND LOWER(campus.DESCRIPCION) ";
						where +=" AND LOWER(campus.DESCRIPCION)  "
						if(filtro.get("operador").equals("Igual a")) {
							campus+="=LOWER('[valor]')"
							where +="=LOWER('[valor]')"
						}else {
							campus+="LIKE LOWER('%[valor]%')"
							where+="LIKE LOWER('%[valor]%')"
						}
						campus = campus.replace("[valor]", filtro.get("valor"))
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CUPO DE LA PRUEBA":
						where +=" AND CAST(Pruebas.cupo as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ALUMNOS REGISTRADOS":
						if (residencia.contains("WHERE")) {
							residencia += " AND "
						} else {
							residencia += " WHERE "
						}
				
						residencia +=" CAST(registrados as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							residencia+="='[valor]'"
						}else {
							residencia+="LIKE '%[valor]%'"
						}
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						break;
						
						
					case "RESIDENCIA":
						if (residencia.contains("WHERE")) {
							residencia += " AND "
						} else {
							residencia += " WHERE "
						}
						residencia +=" LOWER(residencia) ";
						if(filtro.get("operador").equals("Igual a")) {
							residencia+="=LOWER('[valor]')"
						}else {
							residencia+="LIKE LOWER('%[valor]%')"
						}
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						break;
						
					case "FECHA":
						where +=" AND  ( LOWER( CAST(TO_CHAR(Pruebas.aplicacion, 'DD-MM-YYYY') as varchar)) LIKE LOWER('%[valor]%') ";
						where += "OR LOWER(Pruebas.entrada) LIKE LOWER('%[valor]%') "
						where += "OR LOWER(Pruebas.salida) LIKE LOWER('%[valor]%') )"
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "LUGAR":
						where +=" AND LOWER(Pruebas.lugar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
						
					case "TIPO DE PRUEBA":
						where +=" AND LOWER(ctipoprueba.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
								where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE DE LA SESION":
						where +=" AND LOWER(Sesion.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
								where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ALUMNOS ASISTIERON":
						if (residencia.contains("WHERE")) {
							residencia += " AND "
						} else {
							residencia += " WHERE "
						}
					
						residencia +=" CAST(asistencias as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							residencia+="='[valor]'"
						}else {
							residencia+="LIKE '%[valor]%'"
						}
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						break;
					
					}
					
					
				}
				
				
					errorlog+="llego al orderby "
					switch(object.orderby) {
						
						case "ID":
						orderby+="pruebas_id";
						break;
						case "NOMBRE":
						orderby+="Pruebas.nombre";
						break;
						case "ALUMNOS REGISTRADOS":
						orderby+="registrados";
						break;
						case "CUPO":
						orderby+="Pruebas.cupo";
						break;
						case "RESIDENCIA":
						orderby+="Sesion.residencia";
						break;
						case "FECHA":
						orderby+="Pruebas.aplicacion";
						break;
						case "LUGAR":
						orderby+="Pruebas.lugar";
						break;
						case "TIPO_PRUEBA":
						orderby+="ctipoprueba.descripcion";
						break;
						case "NOMBRE_SESION":
						orderby+="Sesion.nombre";
						break;
						case "CAMPUS":
						orderby+="campus.descripcion";
						break;
						case "ASISTENCIA":
						orderby+="asistencias"
						break;
						default:
						orderby+="Pruebas.aplicacion"
						break;
						
					}
					errorlog+="paso el order "
					orderby+=" "+object.orientation;
					consulta=consulta.replace("[CAMPUS]", campus)
					consulta=consulta.replace("[WHERE]", where);
					consulta=consulta.replace("[ORDEN]", object.orden);
					String consulta_EXT = Statements.EXT_SESIONESCALENDARIZADASLISTADO;
					consulta=consulta.replace("[WHERE]", where);
					consulta=consulta.replace("[RESIDENCIA]", residencia);
					consulta=consulta.replace("[COUNTASPIRANTES]", consulta_EXT);
					String groupBy = "group by Pruebas.nombre, Pruebas.aplicacion, Sesion.tipo, Pruebas.persistenceid, Sesion.persistenceid, Pruebas.lugar, Pruebas.registrados, Sesion.nombre, ctipoprueba.descripcion, Pruebas.cupo, Pruebas.entrada,Pruebas.salida, campus.descripcion";
					consulta=consulta.replace("[GROUPBY]", groupBy)
					
					errorlog += "conteo = "+consulta.replace("* from (", "count(*) registros from ( ").replace("[LIMITOFFSET]", "").replace("[ORDERBY]","");
					pstm = con.prepareStatement(consulta.replace("* from (", "count(*) registros from ( ").replace("[LIMITOFFSET]", "").replace("[ORDERBY]",""));

					rs= pstm.executeQuery();
					while(rs.next()) {
						resultado.setTotalRegistros(rs.getInt("registros"))
					}
					
					errorlog+="conteo exitoso "
					
					
					consulta=consulta.replace("[ORDERBY]", orderby)
					consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
					
					pstm = con.prepareStatement(consulta)
					pstm.setInt(1, object.limit)
					pstm.setInt(2, object.offset)
					
					PruebasCustom Pruebas = new PruebasCustom();
					rs = pstm.executeQuery()
					errorlog+="Listado "
					while(rs.next()) {
						
						row = new PruebaCustom();
						row.setNombre(rs.getString("nombre"))
						row.setRegistrados(rs.getInt("registrados"));
						row.setLugar(rs.getString("lugar"));
						row.setPersistenceId(rs.getLong("pruebas_id"));
						row.setCupo(rs.getInt("cupo"));
						row.setTipo(new CatTipoPrueba())
						row.getTipo().setDescripcion(rs.getString("tipo_prueba"));
						row.setEntrada(rs.getString("entrada"));
						row.setSalida(rs.getString("salida"))
						row.setAsistencia(rs.getString("asistencias"))
						
						SesionCustom row2 = new SesionCustom();
						row2.setFecha_inicio(rs.getString("aplicacion"));
						row2.setTipo(rs.getString("residencia"));
						row2.setPersistenceId(rs.getLong("sesiones_id"));
						row2.setNombre(rs.getString("nombre_sesion"));
						row2.setDescripcion(rs.getString("campus"))
						
						User usr;
						UserMembership membership
						String responsables = rs.getString("responsables");
						String nombres= "";
						if(!responsables.equals("null") && responsables != null) {
							errorlog+=" "+responsables;
							String[] arrOfStr = responsables.split(",");
							for (String a: arrOfStr) {
								if(Long.parseLong(a)>0) {
									usr = context.getApiClient().getIdentityAPI().getUser(Long.parseLong(a))
									nombres+=(nombres.length()>1?", ":"")+usr.getFirstName()+" "+usr.getLastName()
								}
							}
						}
						
						row.setResponsables(nombres)
						
						Pruebas = new PruebasCustom();
						Pruebas.setPrueba(row);
						Pruebas.setSesion(row2);
						
						rows.add(Pruebas)
					}
					
				resultado.setError_info(consulta+" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
			} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
				resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	public Result getDatosSesionUsername(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
		
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_DATOS_SESION_USERNAME)
				pstm.setString(1, username)
				
				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
	
					rows.add(columns);
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getPsicologoSesiones(Long id) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
		
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_PSICOLOGOS_SESIONES)
				pstm.setLong(1, id)
				
				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
	
					rows.add(columns);
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result insertAspirantesPruebas(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				
				object.each{
					//List<Map<String, Object>> estatus = new ArrayList<Map<String, Object>>();
						con.setAutoCommit(false)
						
						
						pstm = con.prepareStatement(Statements.INSERT_ASPIRANTESPRUEBAS, Statement.RETURN_GENERATED_KEYS);
						
						pstm.setString(1,it.username);
						pstm.setInt(2,it.prueba_pid);
						pstm.setBoolean(3,it.asistencia);
						pstm.setInt(4,it.countRechazo);
						pstm.setInt(5,it.sesiones_pid);
						pstm.setInt(6,it.cattipoprueba_pid);
						pstm.setInt(7,it.responsabledisponible_pid);
												
						pstm.executeUpdate();
					}
					
				con.commit();
				resultado.setSuccess(true)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result deleteAspirantesPruebas(String sesiones,String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
				
				closeCon = validarConexion();
				con.setAutoCommit(false)
				
				pstm = con.prepareStatement(Statements.DELETE_ASPIRANTESPRUEBAS, Statement.RETURN_GENERATED_KEYS);
				pstm.setInt(1,sesiones);
				pstm.setString(2,username);
				
				pstm.executeUpdate();
				con.commit();
				resultado.setSuccess(true)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updateAspirantesPruebas(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				con.setAutoCommit(false)
				
				pstm = con.prepareStatement(Statements.UPDATE_ASPIRANTESPRUEBAS);
				pstm.setBoolean(1,object.asistencia);
				pstm.setLong(2,object.prueba);
				pstm.setString(3,object.username);
				
				
				pstm.executeUpdate();
				con.commit();
				resultado.setSuccess(true)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result insertPaseLista( String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				con.setAutoCommit(false)
				pstm = con.prepareStatement(Statements.INSERT_PASEDELISTA, Statement.RETURN_GENERATED_KEYS)
				pstm.setLong(1, object.prueba);
				pstm.setString(2, object.username);
				pstm.setBoolean(3,object.asistencia);
				pstm.setString(4,object.usuarioPaseLista);
				
				pstm.executeUpdate();
				
				con.commit();
				
				Result dataResult = updateAspirantesPruebas(jsonData, context);
				
				resultado.setSuccess(true)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result updatePaseLista(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				con.setAutoCommit(false)
				pstm = con.prepareStatement(Statements.UPDATE_PASEDELISTA)
				pstm.setBoolean(1,object.asistencia);
				pstm.setString(2,object.usuarioPaseLista);
				pstm.setLong(3, object.prueba)
				pstm.setString(4, object.username)
				pstm.executeUpdate();
				
				con.commit();
				Result dataResult = updateAspirantesPruebas(jsonData, context);
				errorLog += dataResult;
				resultado.setSuccess(true)
				resultado.setError_info(errorLog)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updatePrepaSolicitudDeAdmision(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				con.setAutoCommit(false)
				pstm = con.prepareStatement(Statements.UPDATEPREPASOLICITUDDEADMISION)
				pstm.setLong(1,object.catbachilleratos_pid);
				pstm.setLong(2,object.persistenceid);
				
				pstm.executeUpdate();
				
				con.commit();
				resultado.setSuccess(true)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getSesionesAspirantesPasados(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		String where ="", orderby="ORDER BY AP.persistenceid, ", errorlog="", role="", group="", orderbyUsuario="ORDER BY sda.primernombre";
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				String consulta = Statements.GET_ASPIRANTEPRUEBAASISTIOYREAGENDO;
				//AND CAST(S.fecha_inicio P.aplicacion AS DATE) < CAST([FECHA] AS DATE)
				SesionesAspiranteCustom row = new SesionesAspiranteCustom();
				List<SesionesAspiranteCustom> rows = new ArrayList<SesionesAspiranteCustom>();
				List<Map<String, Object>> aspirante = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				
				int tipo = 0;
				pstm = con.prepareStatement(Statements.GET_TIPOPRUEBA)
				pstm.setInt(1, object.prueba)
				rs= pstm.executeQuery();
				if(rs.next()) {
					tipo = (rs.getInt("tipoprueba_pid"))
				}
				
				String SSA = "";
				pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
				rs= pstm.executeQuery();
				if(rs.next()) {
					SSA = rs.getString("valor")
				}
				errorlog+="tipo "+tipo
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						
						
					case "NOMBRE, EMAIL, CURP":
						where +=" AND ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ', sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.curp) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CAMPUS, PROGRAMA, INGRESO":
						where +=" AND ( LOWER(campus.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(gestionescolar.nombre) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(CPO.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PROCEDENCIA, PREPARATORIA, PROMEDIO":
						/*where +=" AND ( LOWER(estado.DESCRIPCION) like lower('%[valor]%') ";*/
						where +=" AND (LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%')"
						where = where.replace("[valor]", filtro.get("valor"))
					
						where +="  OR LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.bachillerato ELSE prepa.descripcion END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
					
						where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
									
						
					case "ID BANNER":
					
						where +=" AND CAST(ds.idbanner as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE":
						where +="  AND LOWER(concat(sda.primernombre,' ', sda.segundonombre,' ',sda.apellidopaterno,' ',sda.apellidomaterno)) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "EMAIL":
						where +=" AND LOWER(sda.correoelectronico) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PROMEDIO":
							where +=" AND CAST(sda.PROMEDIOGENERAL as varchar )";
							if(filtro.get("operador").equals("Igual a")) {
								where+="='[valor]'"
							}else {
								where+="LIKE '%[valor]%'"
							}
                            where = where.replace("[valor]", filtro.get("valor"))
                            break;
							
					case "PREPARATORIA":
							where +=" AND LOWER(prepa.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where= where.replace("[valor]", filtro.get("valor"))
							break;
							
					case "RESIDENCIA":
						where +="AND  LOWER(R.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "SEXO":
						where +=" AND LOWER(sx.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "LICENCIATURA":
						where +=" AND LOWER(gestionescolar.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "UNIVERSIDAD":
						where +=" AND LOWER(campus.DESCRIPCION) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
						
					case "HORA DE LA ENTREVISTA":
						if(tipo == 1) {
							where+=" AND CAST(rd.horario as varchar) "
						}else {
							where+=" AND (CAST( concat(p.entrada,' - ',p.salida) as varchar)) "
						}
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ASISTENCIA":
						where +=" AND (PL.asistencia ";
						where+="= [valor] "
						where = where.replace("[valor]",  (filtro.get("valor").toString().equals("Sí")?"true)":"false OR PL.asistencia is NULL)"))
						break;
						
					}
					
					
					
					
				}
				
				errorlog+="llego al orderby "
				switch(object.orderby) {
					
					case "IDBANNER":
					orderby+="ds.idbanner";
					break;
					case "NOMBRE":
					orderby+="sda.apellidopaterno";
					break;
					case "EMAIL":
					orderby+="sda.correoelectronico";
					break;
					case "PREPARATORIA":
					orderby+="preparatoria"
					break;
					case "CAMPUS":
					orderby+="campus.descripcion"
					break;
					case "RESIDENCIA":
					orderby+="R.descripcion";
					break;
					case "CURP":
					orderby+="sda.curp";
					break;
					case "PROCEDENCIA":
					orderby+="estado.DESCRIPCION";
					break;
					case "INGRESO":
					orderby+="CPO.descripcion";
					break;
					case "SEXO":
					orderby+="sx.descripcion";
					break;
					case "LICENCIATURA":
					orderby+="gestionescolar.nombre";
					break;
					case "PROMEDIO":
					orderby+="sda.promediogeneral";
					break;
					case "ASISTENCIA":
					orderby+= "asistencia";
					break;
					
					case "HORARIO":
					orderby+= "horario";
					break;
					
					default:
					orderby+="AP.username"
					break;
					
				}
										
				//orderby+="SA.username"
				orderby+=" "+object.orientation;
				errorlog+="order by = "+orderby
				consulta=consulta.replace("[WHERE]", where);
				if(tipo == 1) {
					//consulta=consulta.replace("[ENTREVISTA]", "AND RD.responsableid ="+object.usuario)
					consulta=consulta.replace("[ENTREVISTA]", "");
					if(object.reporte && object.type == null) {
						consulta=consulta.replace("[REPORTE]", "AND rd.responsableid = "+object.usuario)
					}else {
					 	consulta=consulta.replace("[REPORTE]", "")
				 	}
				}else {
					consulta=consulta.replace("[ENTREVISTA]", "");
					consulta=consulta.replace("[REPORTE]", "");
				}
				
				errorlog+=" conteo = "+consulta.replace("distinct on (AP.persistenceid) AP.persistenceid,P.nombre as nombre_prueba,P.Lugar as lugar_prueba,DS.IDBANNER,sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre,SDA.CORREOELECTRONICO,SDA.CURP,campus.descripcion AS campus,gestionescolar.nombre AS licenciatura, CPO.descripcion as periodo,CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia,sda.PROMEDIOGENERAL, CASE WHEN prepa.descripcion = 'Otro' THEN sda.bachillerato ELSE prepa.descripcion END AS preparatoria, R.descripcion as residencia, sx.descripcion as sexo, PL.ASISTENCIA, P.aplicacion, c.descripcion as tipo_prueba, case when C.persistenceid=1 then rd.horario  else concat(p.entrada,' - ',p.salida) end as horario, RD.PERSISTENCEID AS RD, DS.CASEID, sda.urlfoto,le.descripcion as lugarexamen,sda.telefonocelular,DS.cbCoincide,AP.acreditado,c.PERSISTENCEID as tipoprueba_pid,AP.USERNAME", " count(distinct AP.persistenceid) as  registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "")
				pstm = con.prepareStatement(consulta.replace("distinct on (AP.persistenceid) AP.persistenceid,P.nombre as nombre_prueba,P.Lugar as lugar_prueba,DS.IDBANNER,sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre,SDA.CORREOELECTRONICO,SDA.CURP,campus.descripcion AS campus,gestionescolar.nombre AS licenciatura, CPO.descripcion as periodo,CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia,sda.PROMEDIOGENERAL, CASE WHEN prepa.descripcion = 'Otro' THEN sda.bachillerato ELSE prepa.descripcion END AS preparatoria, R.descripcion as residencia, sx.descripcion as sexo, PL.ASISTENCIA, P.aplicacion, c.descripcion as tipo_prueba, case when C.persistenceid=1 then rd.horario  else concat(p.entrada,' - ',p.salida) end as horario, RD.PERSISTENCEID AS RD, DS.CASEID, sda.urlfoto,le.descripcion as lugarexamen,sda.telefonocelular,DS.cbCoincide,AP.acreditado,c.PERSISTENCEID as tipoprueba_pid,AP.USERNAME", " count(distinct AP.persistenceid) as  registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""));
				pstm.setInt(1, object.prueba);
				pstm.setInt(2, object.sesion);
				
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog+="conteo exitoso "
				
				errorlog+=" consulta :"+consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.prueba)
				pstm.setInt(2, object.sesion)
				pstm.setInt(3, object.limit)
				pstm.setInt(4, object.offset)
				
				rs = pstm.executeQuery()
				
				
				
				errorlog+="otra llamada "
				aspirante = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();

					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
							String encoded = "";
							try {
								String urlFoto = rs.getString("urlfoto");
								if(urlFoto != null && !urlFoto.isEmpty()) {
									columns.put("fotografiab64", rs.getString("urlfoto") +SSA);
								}else {
									List<Document>doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
									for(Document doc : doc1) {
										encoded = "../API/formsDocumentImage?document="+doc.getId();
										columns.put("fotografiab64", encoded);
									}
								}
							}catch(Exception e) {
								columns.put("fotografiab64", "");
								errorlog+= "esto = "+e.getMessage();
							}
						}
					}
					rows.add(columns);
				}	
						
				resultado.setError_info(" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
				
			} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
				resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getSesionesCalendarizadasReporte(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		List<PruebasCustom> lstSesion = new ArrayList();
		String where ="", orderby="ORDER BY ", errorlog="", role="", campus="", group="", residencia="";
		Boolean isHaving = false;
		List<String> lstGrupo = new ArrayList<String>();
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		String consulta = Statements.GET_SESIONESCALENDARIZADASREPORTE_REGISTRADOS;
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				PruebaCustom row = new PruebaCustom();
				List<PruebasCustom> rows = new ArrayList<PruebasCustom>();
				closeCon = validarConexion();
				
				Boolean TR=false, AR = false;
				String  TRs= "", ARs ="";
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						
					case "TIPO DE PRUEBA, RESIDENCIA":
						residencia +=" WHERE ( LOWER(residencia) LIKE LOWER('%[valor]%')";
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						
						residencia +=" OR LOWER(tipo_prueba) LIKE LOWER('%[valor]%') )";
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						TRs = filtro.get("valor")+"";
						TR = true;
						break;
						
					case "ID":
						where +=" AND CAST(Pruebas.persistenceid as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE DE LA PRUEBA":
						where +=" AND LOWER(Pruebas.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CUPO DE LA PRUEBA":
						where +=" AND CAST(Pruebas.cupo as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ALUMNOS REGISTRADOS":
					
						if (residencia.contains("WHERE")) {
							residencia += " AND "
						} else {
							residencia += " WHERE "
						}
					
						residencia +=" CAST(registrados as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							residencia+="='[valor]'"
						}else {
							residencia+="LIKE '%[valor]%'"
						}
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						//AR = true;
						//ARs = filtro.get("valor")+"";
						break;
						
						
					case "RESIDENCIA":
						if (residencia.contains("WHERE")) {
							residencia += " AND "
						} else {
							residencia += " WHERE "
						}
						
						residencia +=" LOWER(residencia) ";
						if(filtro.get("operador").equals("Igual a")) {
							residencia+="=LOWER('[valor]')"
						}else {
							residencia+="LIKE LOWER('%[valor]%')"
						}
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						break;
						
					case "FECHA":
						where +=" AND   ( LOWER( CAST(TO_CHAR(Pruebas.aplicacion, 'DD-MM-YYYY') as varchar)) LIKE LOWER('%[valor]%') ";
						where += "OR LOWER(Pruebas.entrada) LIKE LOWER('%[valor]%') "
						where += "OR LOWER(Pruebas.salida) LIKE LOWER('%[valor]%') )"
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "LUGAR":
						where +=" AND LOWER(Pruebas.lugar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
						
					case "TIPO DE PRUEBA":
						where +=" AND LOWER(ctipoprueba.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
								where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE DE LA SESION":
						where +=" AND LOWER(Sesion.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
								where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ALUMNOS ASISTIERON":
						if (residencia.contains("WHERE")) {
							residencia += " AND "
						} else {
							residencia += " WHERE "
						}
					
						residencia +=" CAST(asistencias as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							residencia+="='[valor]'"
						}else {
							residencia+="LIKE '%[valor]%'"
						}
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						break;
					
					
					}
					
					
				}
					errorlog+="llego al orderby "
					switch(object.orderby) {
						
						case "ID":
						orderby+="pruebas_id";
						break;
						case "NOMBRE":
						orderby+="Pruebas.nombre";
						break;
						case "ALUMNOS REGISTRADOS":
						orderby+="registrados";
						break;
						case "CUPO":
						orderby+="Pruebas.cupo";
						break;
						case "RESIDENCIA":
						orderby+="Sesion.tipo";
						break;
						case "FECHA":
						orderby+="Pruebas.aplicacion";
						break;
						case "LUGAR":
						orderby+="Pruebas.lugar";
						break;
						case "TIPO_PRUEBA":
						orderby+="ctipoprueba.descripcion";
						break;
						case "NOMBRE_SESION":
						orderby+="Sesion.nombre";
						break;
						case "ASISTENCIA":
						orderby+="asistencias"
						break;
						default:
						orderby+="Pruebas.aplicacion"
						break;
						
					}
					orderby+=" "+object.orientation;
					
					String consulta_EXT = Statements.EXT_SESIONESCALENDARIZADAS.replace("[RESPONSABLE]",object.usuario.toString());
					consulta=consulta.replace("[WHERE]", where);
					consulta=consulta.replace("[RESIDENCIA]", residencia);
					consulta=consulta.replace("[COUNTASPIRANTES]", consulta_EXT);
					String groupBy = "group by ResponsableD.prueba_pid,Pruebas.persistenceid, Pruebas.nombre, Pruebas.aplicacion, Sesion.tipo, Sesion.persistenceid, Pruebas.lugar, Pruebas.registrados, Sesion.nombre, ctipoprueba.descripcion, Pruebas.cupo, Pruebas.entrada,Pruebas.salida,ResponsableD.PERSISTENCEID";
					consulta=consulta.replace("[GROUPBY]", groupBy);
					
					pstm = con.prepareStatement(consulta.replace("* from (", "count(*) registros from ( ").replace("[LIMITOFFSET]", "").replace("[ORDERBY]",""));
					pstm.setInt(1, object.usuario)
					
					rs= pstm.executeQuery();
										
					while(rs.next()) {
						resultado.setTotalRegistros(rs.getInt("registros"))
					}
					
					consulta=consulta.replace("[ORDERBY]", orderby)
					
					consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
					
					
					pstm = con.prepareStatement(consulta)
					pstm.setInt(1, object.usuario)
					pstm.setInt(2, object.limit)
					pstm.setInt(3, object.offset)
					
					PruebasCustom Pruebas = new PruebasCustom();
					rs = pstm.executeQuery()
					Date date = new Date();
					while(rs.next()) {
						
						row = new PruebaCustom();
						row.setNombre(rs.getString("nombre"))
						row.setRegistrados(rs.getInt("registrados"));
						row.setLugar(rs.getString("lugar"));
						row.setPersistenceId(rs.getLong("pruebas_id"));
						row.setCupo(rs.getInt("cupo"));
						row.setTipo(new CatTipoPrueba())
						row.getTipo().setDescripcion(rs.getString("tipo_prueba"));
						row.setEntrada(rs.getString("entrada"));
						row.setSalida(rs.getString("salida"))
						row.setAsistencia(rs.getString("asistencias"))
						
						SesionCustom row2 = new SesionCustom();
						row2.setFecha_inicio(rs.getString("aplicacion"));
						row2.setTipo(rs.getString("residencia"));
						row2.setPersistenceId(rs.getLong("sesiones_id"));
						row2.setNombre(rs.getString("nombre_sesion"));
						row2.setDescripcion(date.toString())
						
						
						Pruebas = new PruebasCustom();
						Pruebas.setPrueba(row);
						Pruebas.setSesion(row2);
						
						rows.add(Pruebas)
					}
					
				resultado.setError_info(consulta+" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
			} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
				resultado.setError_info(consulta+" errorLog = "+errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getAspirantes3Asistencias(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
		String where ="", orderby="ORDER BY ", errorlog="", role="", group="",  campus="";
		List<String> lstGrupo = new ArrayList<String>();
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				
				
				String consulta = Statements.GET_ASPIRANTEEXAMENESTERMINADOS
				SesionesAspiranteCustom row = new SesionesAspiranteCustom();
				List<SesionesAspiranteCustom> rows = new ArrayList<SesionesAspiranteCustom>();
				List<Map<String, Object>> aspirante = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				
				def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
				List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
				
				userLogged = context.getApiSession().getUserId();
				
				List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
				for(UserMembership objUserMembership : lstUserMembership) {
					for(CatCampus rowGrupo : lstCatCampus) {
						if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
							lstGrupo.add(rowGrupo.getDescripcion());
							break;
						}
					}
				}
				
				if(lstGrupo.size()>0) {
					campus+=" AND ("
				}
				for(Integer i=0; i<lstGrupo.size(); i++) {
					String campusMiembro=lstGrupo.get(i);
					campus+="campus.descripcion='"+campusMiembro+"'"
					if(i==(lstGrupo.size()-1)) {
						campus+=") "
					}
					else {
						campus+=" OR "
					}
				}
				
				where+= " and LOWER(campus.grupoBonita) = LOWER('"+object.campus+"') "
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
					case "ID BANNER":
						where +=" AND CAST(da.idbanner as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE":
						where +="  AND LOWER(concat(sda.primernombre,' ', sda.segundonombre,' ',sda.apellidopaterno,' ',sda.apellidomaterno)) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "EMAIL":
						where +=" AND LOWER(sda.correoelectronico) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PROMEDIO":
							errorlog+="PROMEDIO"
							where +=" AND  CAST(sda.PROMEDIOGENERAL as varchar) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+=" ='[valor]' "
							}else {
								where+="LIKE '%[valor]%'"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
							
					case "PREPARATORIA":
							where +=" AND LOWER(prepa.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where= where.replace("[valor]", filtro.get("valor"))
							break;
							
					case "RESIDENCIA":
						where +=" AND LOWER(R.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "SEXO":
						where +=" AND LOWER(sx.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "LICENCIATURA":
						where +=" AND LOWER(gestionescolar.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "UNIVERSIDAD":
						where +=" AND LOWER(campus.DESCRIPCION) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					}
					
				}
				
				errorlog+="llego al orderby "
				switch(object.orderby) {
					
					case "IDBANNER":
					orderby+="da.idbanner";
					break;
					case "NOMBRE":
					orderby+="sda.primernombre";
					break;
					case "EMAIL":
					orderby+="sda.correoelectronico";
					break;
					case "PREPARATORIA":
					orderby+="prepa.DESCRIPCION"
					break;
					case "CAMPUS":
					orderby+="campus.DESCRIPCION"
					break;
					case "RESIDENCIA":
					orderby+="R.descripcion";
					break;
					case "SEXO":
					orderby+="sx.descripcion";
					break;
					case "LICENCIATURA":
					orderby+="gestionescolar.DESCRIPCION";
					break;
					case "PROMEDIO":
					orderby+="sda.PROMEDIOGENERAL";
					break;
					
					default:
					orderby+="sda.caseId"
					break;
					
				}
						
				//orderby+="SA.username"
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[WHERE]", where);
				//String count = Statements.COUNT_ASPIRANTEEXAMENTERMINADOS;
				//count = count.replace("[WHERE]", where);
				
				pstm = con.prepareStatement(consulta.replace("sda.aceptado, sda.caseId, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.telefonocelular, sda.correoelectronico, campus.descripcion AS campus, gestionescolar.nombre AS licenciatura,  prepa.DESCRIPCION AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, da.idbanner, campus.grupoBonita, le.descripcion as lugarexamen, sx.descripcion as sexo, CPO.descripcion as periodo, R.descripcion as residencia", "COUNT(sda.persistenceId) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "") )
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				
				//consulta=consulta.replace("[GROUPBY]", " group by PL.username, sda.aceptado,sda.caseId, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.telefonocelular, sda.correoelectronico, campus.descripcion , gestionescolar.nombre ,  prepa.DESCRIPCION , sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, da.idbanner, campus.grupoBonita, le.descripcion, sx.descripcion, CPO.descripcion, R.descripcion  ");
				//consulta=consulta.replace("[HAVING]", " HAVING count(1)>=3 ");
				consulta=consulta.replace("[ORDERBY]", orderby);
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
				errorlog+="conteo exitoso "
				errorlog+="  "+consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				rs = pstm.executeQuery()
				aspirante = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
					
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
							String encoded = "";
							try {
								for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
									encoded = "../API/formsDocumentImage?document="+doc.getId();
									columns.put("fotografiab64", encoded);
								}
							}catch(Exception e) {
								columns.put("fotografiab64", "");
								errorlog+= ""+e.getMessage();
							}
						}
					}
					aspirante.add(columns);
				}
				
				//row.setAspirantes(aspirante);
				//rows.add(aspirante);

				
				
						
				resultado.setError_info(" errorLog = "+errorlog)
				resultado.setData(aspirante)
				resultado.setSuccess(true)
				
			} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
				resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getPruebasFechas(Long sesion_pid, String aspirante) {
		Result resultado = new Result();
		Boolean closeCon = false;
		PruebaCustom row = new PruebaCustom();
		List<PruebaCustom> rows = new ArrayList();
		try {
				closeCon = validarConexion();
				pstm = con.prepareStatement(PruebasCustom.GET_PRUEBAS_FECHAS_COMBINADOS)
				pstm.setString(1, aspirante)
				pstm.setLong(2, sesion_pid)
				rs = pstm.executeQuery()
				while(rs.next()) {
					row = new PruebaCustom()
					row.setPersistenceId(rs.getLong("persistenceId"));
					row.setAplicacion(rs.getString("aplicacion"))
					rows.add(row)
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info("sesion_pid:" + sesion_pid + ", aspirante: " + aspirante)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	public Result getHorarios(Long sesion_pid, Long prueba_pid, String correoAspirante, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		PruebaCustom p = new PruebaCustom();
		List<PruebaCustom> rows = new ArrayList();
		try {
				closeCon = validarConexion();
				pstm = con.prepareStatement(PruebasCustom.GET_HORARIOS_COMBINADOS)
				pstm.setLong(1, sesion_pid)
				pstm.setLong(2, prueba_pid)
				pstm.setString(3, correoAspirante)
				rs = pstm.executeQuery()
				if(rs.next()) {
						
						p = new PruebaCustom()
						p.setAplicacion(rs.getString("aplicacion"))
						p.setCalle(rs.getString("calle"))
						p.setCampus_pid(rs.getLong("campus_pid"))
						p.setCattipoprueba_pid(rs.getLong("cattipoprueba_pid"))
						p.setCodigo_postal(rs.getString("codigo_postal"))
						p.setColonia(rs.getString("colonia"))
						p.setCupo(rs.getInt("cupo"))
						p.setDescripcion(rs.getString("descripcion"))
						p.setDuracion(rs.getString("duracion"))
						p.setEntrada(rs.getString("entrada"))
						p.setEstado_pid(rs.getLong("estado_pid"))
						p.setIseliminado(rs.getBoolean("iseliminado"))
						p.setLugar(rs.getString("lugar"))
						p.setMunicipio(rs.getString("municipio"))
						p.setNombre(rs.getString("nombre"))
						p.setNumero_ext(rs.getString("numero_ext"))
						p.setNumero_int(rs.getString("numero_int"))
						p.setPais_pid(rs.getLong("pais_pid"))
						p.setPersistenceId(rs.getLong("persistenceId"))
						p.setPersistenceVersion(rs.getLong("persistenceVersion"))
						p.setRegistrados(rs.getInt("registrados"))
						p.setSalida(rs.getString("salida"))
						p.setSesion_pid(rs.getLong("sesion_pid"))
						p.setUltimo_dia_inscripcion(rs.getString("ultimo_dia_inscripcion"))
						p.setTipo(new CatTipoPrueba())
						p.getTipo().setDescripcion(rs.getString("tipo"))
						p.setOnline(rs.getBoolean("online"))
						p.setPsicologos(new ArrayList())
						
						User usr;
						UserMembership membership
						if(rs.getLong("RESPONSABLEID")>0) {
							usr = context.getApiClient().getIdentityAPI().getUser(rs.getLong("RESPONSABLEID"))
							membership=context.getApiClient().getIdentityAPI().getUserMemberships(usr.getId(), 0, 100, UserMembershipCriterion.GROUP_NAME_ASC).get(0)
						}
						rows.add(p)
						pstm = con.prepareStatement(Statements.GET_HORARIOS_PRUEBAS_ENTREVISTA_ASPIRANTE)
						pstm.setLong(1, p.getPersistenceId())
						pstm.setString(2, correoAspirante)
						rs = pstm.executeQuery()
						while(rs.next()) {
							ResponsableCustom psi = new ResponsableCustom()
							ResponsableDisponible fd = new ResponsableDisponible()
							fd.setDisponible(rs.getBoolean("disponible"))
							fd.setOcupado(rs.getBoolean("ocupado"))
							fd.setHorario(rs.getString("horario"))
							fd.setPersistenceId(rs.getLong("rid"))
							fd.setResponsableId(rs.getLong("RESPONSABLEID"))
							psi.setLicenciaturas(rs.getString("licenciaturas"))
							psi.setPersistenceId(rs.getLong("rid"))
							
							try {
								psi.setFirstname(usr.getFirstName())
								psi.setLastname(usr.getLastName())
								psi.setGrupo(membership.groupName)
								psi.setRol(membership.roleName)
							}catch(Exception e) {
								resultado.setError_info(e.getMessage())
							}
							
							
							psi.setId(rs.getLong("RESPONSABLEID"))
						
							if(rows.get(rows.indexOf(p)).getPsicologos().contains(psi)) {
								rows.get(rows.indexOf(p)).getPsicologos().get(rows.get(rows.indexOf(p)).getPsicologos().indexOf(psi)).getLstFechasDisponibles().add(fd)
								
							}else {
								if(fd.getResponsableId()>0) {
								psi.setLstFechasDisponibles(new ArrayList())
								psi.getLstFechasDisponibles().add(fd)
								rows.get(rows.indexOf(p)).getPsicologos().add(psi)
								}
							}
						}
						
					}else {
						throw new Exception("Los psicólogos disponibles para esta sesión son especializados para otra licenciatura")
					}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result updateAceptado(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				con.setAutoCommit(false)
				pstm = con.prepareStatement(Statements.UPDATE_ASPIRANTEACEPTADO)
				pstm.setBoolean(1,object.aceptado);
				pstm.setLong(2,Long.parseLong(object.caseId));
				pstm.executeUpdate();
				
				con.commit();
				resultado.setSuccess(true)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	
	
	public Result listadoSesiones(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				con.setAutoCommit(false)
				pstm = con.prepareStatement(Statements.UPDATE_ASPIRANTEACEPTADO)
				pstm.setBoolean(1,object.aceptado);
				pstm.setLong(2,Long.parseLong(object.caseId));
				pstm.executeUpdate();
				
				con.commit();
				resultado.setSuccess(true)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	//optener los responsables de la prueba
	public Result getResponsables(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				String consulta = "select String_AGG(distinct rd.responsableid::varchar,',') as responsables from responsabledisponible as rd where rd.isEliminado = false and rd.prueba_pid = "+object.prueba;
				errorLog += "consulta:"+consulta;
				List<String> rows = new ArrayList<String>();
				closeCon = validarConexion();
				pstm = con.prepareStatement(consulta);
				rs = pstm.executeQuery()
				
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				
				while(rs.next()) {
					User usr;
					//UserMembership membership
					String responsables = rs.getString("responsables");
					String nombres= "";
					if(!responsables.equals("null") && responsables != null) {
						String[] arrOfStr = responsables.split(",");
						for (String a: arrOfStr) {
							if(Long.parseLong(a)>0) {
								usr = context.getApiClient().getIdentityAPI().getUser(Long.parseLong(a))
								nombres+=(nombres.length()>1?", ":"")+usr.getFirstName()+" "+usr.getLastName()
							}
						}
					}
					rows.add(nombres);
				}
				resultado.setSuccess(true)
				resultado.setData(rows)
				resultado.setError_info(errorLog)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog+" "+e.getMessage())
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getAspirantesPasadosExcel(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		String where ="", orderby="ORDER BY ", errorlog="", role="", group="", orderbyUsuario="ORDER BY sda.primernombre";
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				String consulta = Statements.GET_ASPIRANTESEXCELLISTADO;
				SesionesAspiranteCustom row = new SesionesAspiranteCustom();
				List<SesionesAspiranteCustom> rows = new ArrayList<SesionesAspiranteCustom>();
				List<Map<String, Object>> aspirante = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				
				int tipo = 0;
				pstm = con.prepareStatement(Statements.GET_TIPOPRUEBA)
				pstm.setInt(1, object.prueba)
				rs= pstm.executeQuery();
				if(rs.next()) {
					tipo = (rs.getInt("tipoprueba_pid"))
				}
				
				
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						
						
					case "NOMBRE, EMAIL, CURP":
						where +=" AND ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.curp) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CAMPUS, PROGRAMA, INGRESO":
						where +=" AND ( LOWER(campus.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(gestionescolar.nombre) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(CPO.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PROCEDENCIA, PREPARATORIA, PROMEDIO":
						/*where +=" AND ( LOWER(estado.DESCRIPCION) like lower('%[valor]%') ";*/
						where +=" AND (LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%')"
						where = where.replace("[valor]", filtro.get("valor"))
					
						where +="  OR LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.bachillerato ELSE prepa.descripcion END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
					
						where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ID BANNER":
						where +=" AND CAST(DS.idbanner as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE":
						where +="  AND LOWER(concat(sda.primernombre,' ', sda.segundonombre,' ',sda.apellidopaterno,' ',sda.apellidomaterno)) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "EMAIL":
						where +=" AND LOWER(sda.correoelectronico) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PROMEDIO":
                            errorlog+="PROMEDIO"
							where +=" AND  CAST(sda.PROMEDIOGENERAL as varchar) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+=" ='[valor]' "
							}else {
								where+="LIKE '%[valor]%'"
							}
                            where = where.replace("[valor]", filtro.get("valor"))
                            break;
							
					case "PREPARATORIA":
							where +=" AND LOWER(prepa.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where= where.replace("[valor]", filtro.get("valor"))
							break;
							
					case "RESIDENCIA":
						where +=" AND LOWER(R.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "SEXO":
						where +=" AND LOWER(sx.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "LICENCIATURA":
						where +=" AND LOWER(gestionescolar.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "UNIVERSIDAD":
						where +=" AND LOWER(campus.DESCRIPCION) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "HORA DE LA ENTREVISTA":
						if(tipo == 1) {
							where+=" AND CAST(rd.horario as varchar) "
						}else {
							where+=" AND (CAST( concat(p.entrada,' - ',p.salida) as varchar)) "
						}
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ASISTENCIA":
						where +=" AND (PL.asistencia ";
						where+="= [valor] "
						where = where.replace("[valor]",  (filtro.get("valor").toString().equals("Sí")?"true)":"false OR PL.asistencia is NULL)"))
						break;
						
					}
					
					
					
					
				}
				
				errorlog+="llego al orderby "
				switch(object.orderby) {
					
					case "IDBANNER":
					orderby+="DS.idbanner";
					break;
					case "NOMBRE":
					orderby+="sda.apellidopaterno";
					break;
					case "EMAIL":
					orderby+="SDA.correoelectronico";
					break;
					case "PREPARATORIA":
					orderby+="prepa.descripcion"
					break;
					case "CAMPUS":
					orderby+="campus"
					break;
					case "RESIDENCIA":
					orderby+="residencia";
					break;
					case "CURP":
					orderby+="SDA.curp";
					break;
					case "PROCEDENCIA":
					orderby+="preparatoriaEstado";
					break;
					case "INGRESO":
					orderby+="CPO.descripcion";
					break;
					case "SEXO":
					orderby+="sx.descripcion";
					break;
					case "LICENCIATURA":
					orderby+="gestionescolar.nombre";
					break;
					case "PROMEDIO":
					orderby+="SDA.promediogeneral";
					break;
					case "ASISTENCIA":
					orderby+= "AP.asistencia";
					break;
					
					case "HORARIO":
					orderby+= "RD.horario";
					break;
					
					default:
					orderby+="AP.username"
					break;
					
				}
										
				
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[WHERE]", where);
				
				consulta=consulta.replace("[ENTREVISTA]", "")
				consulta=consulta.replace("[REPORTE]", "")
				/*if(tipo == 1) {
					consulta=consulta.replace("[ENTREVISTA]", "AND rd.persistenceid = sa.responsabledisponible_pid")
				}else {
					consulta=consulta.replace("[ENTREVISTA]", "")
				}*/
				
				//String HavingGroup ="GROUP BY p.persistenceid,sa.username,sa.persistenceid,sa.persistenceversion,sa.sesiones_pid,sa.responsabledisponible_pid,rd.responsableid,RD.prueba_pid, P.aplicacion, P.nombre,c.descripcion,c.persistenceid,rd.horario,pl.asistencia,sda.curp,estado.DESCRIPCION ,sda.caseId, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.telefonocelular, sda.correoelectronico, campus.descripcion, gestionescolar.nombre,  prepa.DESCRIPCION, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, da.idbanner, campus.grupoBonita, le.descripcion, sx.descripcion, CPO.descripcion, R.descripcion , da.cbCoincide,prepa.estado,sda.bachillerato,sda.estadobachillerato"
				//String Group = "GROUP BY p.persistenceid,sa.username,sa.persistenceid,sa.persistenceversion,sa.sesiones_pid,sa.responsabledisponible_pid,rd.responsableid,RD.prueba_pid, P.aplicacion, P.nombre,c.descripcion,c.persistenceid,rd.horario,pl.asistencia,sda.curp,estado.DESCRIPCION,sda.caseId, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.telefonocelular, sda.correoelectronico, campus.descripcion, gestionescolar.nombre,  prepa.DESCRIPCION, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, da.idbanner, campus.grupoBonita, le.descripcion, sx.descripcion, CPO.descripcion , R.descripcion , da.cbCoincide,prepa.estado,sda.bachillerato,sda.estadobachillerato";
				
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				//consulta=consulta.replace("[HAVING]", HavingGroup)
				//consulta=consulta.replace("[GROUPBY]", Group)
				//consulta=consulta.replace("[COUNT]", "")
				//consulta=consulta.replace("[COUNTFIN]", "")
				
				
				errorlog+=" consulta :"+consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.prueba);
				pstm.setInt(2, object.sesion);
				pstm.setInt(3, object.limit);
				pstm.setInt(4, object.offset);
				
				rs = pstm.executeQuery()
				
				
				
				errorlog+="otra llamada "
				aspirante = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();

					for (int i = 1; i <= columnCount; i++) {
						
						if(metaData.getColumnLabel(i).toLowerCase().equals("responsables")) {
							User usr;
							String responsables = rs.getString(i);
							String nombres= "";
							if(!responsables.equals("null") && responsables != null) {
								String[] arrOfStr = responsables.split(",");
								for (String a: arrOfStr) {
									if(Long.parseLong(a)>0) {
										usr = context.getApiClient().getIdentityAPI().getUser(Long.parseLong(a))
										nombres+=(nombres.length()>1?", ":"")+usr.getFirstName()+" "+usr.getLastName()
									}
								}
							}
							columns.put(metaData.getColumnLabel(i).toLowerCase(), nombres);
						}else {
							columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						}
					}
					rows.add(columns);
				}
				
				
						
				resultado.setError_info(" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
				
			} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
				resultado.setError_info("errorLog = "+errorlog+" Error = "+e.getMessage())
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getPaletteColor() {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
		
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.PALETTE_COLOR_1)
				
				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
	
					rows.add(columns);
				}
				
				pstm = con.prepareStatement(Statements.PALETTE_COLOR_2)
				rs = pstm.executeQuery()
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
	
					rows.add(columns);
				}
				pstm = con.prepareStatement(Statements.PALETTE_COLOR_5)
				rs = pstm.executeQuery()
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
	
					rows.add(columns);
				}
				pstm = con.prepareStatement(Statements.PALETTE_COLOR_6)
				rs = pstm.executeQuery()
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
	
					rows.add(columns);
				}
				pstm = con.prepareStatement(Statements.PALETTE_COLOR_7)
				rs = pstm.executeQuery()
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
	
					rows.add(columns);
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	
	public Result getInfoPrueba(Integer P, Integer C, String prueba_pid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<PruebasCustom> rows = new ArrayList<PruebasCustom>();
		try {
				
				closeCon = validarConexion();
				
				pstm = con.prepareStatement(Statements.GET_PRUEBAS)
				pstm.setLong(1,Long.parseLong(prueba_pid));
				rs = pstm.executeQuery()
				PruebaCustom row = new PruebaCustom();
				while(rs.next()) {
					
					row = new PruebaCustom();
					row.setNombre(rs.getString("nombre"))
					row.setTipo(new CatTipoPrueba())
					row.getTipo().setDescripcion(rs.getString("tipo_prueba"));
					row.setEntrada(rs.getString("entrada"));
					row.setSalida(rs.getString("salida"))
					row.setAplicacion(rs.getString("aplicacion"))
					rows.add(row)
				}
				resultado.setData(rows)
				resultado.setSuccess(true)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getFechaServidor(Integer P, Integer C) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
				
				closeCon = validarConexion();
				
				pstm = con.prepareStatement(Statements.GET_DAY)
				rs = pstm.executeQuery()
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
				
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();

					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
					info.add(columns)
				}
				resultado.setData(info)
				resultado.setSuccess(true)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result eliminarSesionAspirante(String username, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		int count = 0;
		Boolean cbcoincide = false;
		try {
				
				closeCon = validarConexion();
				con.setAutoCommit(false)
				
				pstm = con.prepareStatement(Statements.GET_COUNT_ASISTENCIA)
				pstm.setString(1,username);
				rs = pstm.executeQuery()
				while(rs.next()) {
					count = rs.getInt("asistencias");
					cbcoincide = rs.getBoolean("cbcoincide")
				}
				
				if(!count == 3 || !(count >= 2 && cbcoincide == true)) {
					
					pstm = con.prepareStatement(Statements.GET_SESIONASPIRANTE)
					pstm.setString(1, username)
					rs = pstm.executeQuery()
					if(rs.next()) {
						pstm = con.prepareStatement(Statements.UPDATE_REGISTRADOS_INV)
						pstm.setLong(1, rs.getLong("sesiones_pid"))
						pstm.setLong(2, rs.getLong("sesiones_pid"))
						pstm.executeUpdate();
						
						pstm = con.prepareStatement(Statements.UPDATE_OCUPADO_RESPONSABLE_DISPONIBLE_INV)
						pstm.setLong(1, rs.getLong("responsabledisponible_pid"))
						pstm.executeUpdate();
						
						pstm = con.prepareStatement(Statements.UPDATE_REGISTRADOS_PRUEBAS_INV)
						pstm.setLong(1, rs.getLong("responsabledisponible_pid"))
						pstm.setLong(2, rs.getLong("responsabledisponible_pid"))
						pstm.executeUpdate();
						
						pstm = con.prepareStatement(Statements.DELETE_SESIONASPIRANTE)
						pstm.setString(1,username);
						pstm.executeUpdate();
						
						pstm = con.prepareStatement(Statements.DELETE_ASPIRANTESPRUEBAS);
						pstm.setLong(1,rs.getLong("sesiones_pid"));
						pstm.setString(2,username);
						pstm.executeUpdate();
						
						
					}
					
				}
				
				
				
				pstm.executeUpdate();
				
				con.commit();
				resultado.setSuccess(true)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result postBitacoraSesiones(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_BITACORA_SESIONES_BY_USERNAME);
			pstm.setString(1, object.idbanner);
			pstm.setString(2, object.username);
			
			rs= pstm.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				
				info.add(columns)
			}
			resultado.setSuccess(true);
			resultado.setData(info);
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog);
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getResponsableEntrevista(String responsabledisponible, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_RESPONSABLE_DISPONIBLE_ENTREVISTA);
			pstm.setLong(1, Long.parseLong(responsabledisponible));
			
			rs= pstm.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				//obtiene el nombre de los responsables
				User usr;
				UserMembership membership
				String responsables = rs.getString("responsableid");
				String nombres= "";
				if(!responsables.equals("null") && responsables != null) {
					errorlog+=" "+responsables;
					String[] arrOfStr = responsables.split(",");
					for (String a: arrOfStr) {
						if(Long.parseLong(a)>0) {
							usr = context.getApiClient().getIdentityAPI().getUser(Long.parseLong(a))
							nombres+=(nombres.length()>1?", ":"")+usr.getFirstName()+" "+usr.getLastName()
						}
					}
				}
				columns.put("responsablesnombre", nombres);
				
				info.add(columns)
			}
			resultado.setSuccess(true);
			resultado.setData(info);
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog);
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getResponsablesPrueba(String prueba, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_RESPONSABLES_PRUEBA);
			pstm.setLong(1, Long.parseLong(prueba));
			
			rs= pstm.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				//obtiene el nombre de los responsables
				User usr;
				UserMembership membership
				String responsables = rs.getString("responsableid");
				String nombres= "";
				if(!responsables.equals("null") && responsables != null) {
					errorlog+=" "+responsables;
					String[] arrOfStr = responsables.split(",");
					for (String a: arrOfStr) {
						if(Long.parseLong(a)>0) {
							usr = context.getApiClient().getIdentityAPI().getUser(Long.parseLong(a))
							nombres+=(nombres.length()>1?", ":"")+usr.getFirstName()+" "+usr.getLastName()
						}
					}
				}
				columns.put("responsablesnombre", nombres);
				
				info.add(columns)
			}
			resultado.setSuccess(true);
			resultado.setData(info);
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog);
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getSesionesCalendarizadasPsicologoSupervisor(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		List<PruebasCustom> lstSesion = new ArrayList();
		String where ="", orderby="ORDER BY ", errorlog="", role="", group="", residencia="WHERE ( (CAST(ultimaaplicacion AS DATE) + integer '2') >= (CAST(TO_CHAR(NOW(),'YYYY-MM-DD') as DATE) ) )",campus="";
		List<String> lstGrupo = new ArrayList<String>();
		//Map<String, String> objGrupoCampus = new HashMap<String, String>();
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
				List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
				
				userLogged = context.getApiSession().getUserId();
				
				List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
				for(UserMembership objUserMembership : lstUserMembership) {
					//errorlog+=" Role: "+objUserMembership.getRoleName()
					if(objUserMembership.getRoleName().trim().equals("PSICOLOGO SUPERVISOR") ) {
						for(CatCampus rowGrupo : lstCatCampus) {
							if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
								lstGrupo.add(rowGrupo.getDescripcion());
								break;
							}
						}
					}
					
				}
				
				if(lstGrupo.size()>0) {
					campus+=" AND ("
				}
				for(Integer i=0; i<lstGrupo.size(); i++) {
					String campusMiembro=lstGrupo.get(i);
					campus+="campus.descripcion='"+campusMiembro+"'"
					if(i==(lstGrupo.size()-1)) {
						campus+=") "
					}
					else {
						campus+=" OR "
					}
				}
				
				
				//errorlog+="campus = "+campus
				String consulta = Statements.GET_SESIONESPSICOLOGO
				PruebaCustom row = new PruebaCustom();
				List<PruebasCustom> rows = new ArrayList<PruebasCustom>();
				closeCon = validarConexion();
				errorlog+="llego a filtro "+object.lstFiltro.toString()
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						
						
					case "TIPO DE PRUEBA, RESIDENCIA":
						residencia +=" AND ( LOWER(residencia) LIKE LOWER('%[valor]%')";
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						
						residencia +=" OR LOWER(tipo_prueba) LIKE LOWER('%[valor]%') )";
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						
						break;
					case "ID":
						where +=" AND CAST(Pruebas.persistenceid as varchar)";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE DE LA PRUEBA":
						where +=" AND LOWER(Pruebas.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CUPO DE LA PRUEBA":
						where +=" AND CAST(Pruebas.cupo as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ALUMNOS REGISTRADOS":
						if (residencia.contains("WHERE")) {
							residencia += " AND "
						} else {
							residencia += " WHERE "
						}
						
						residencia +=" CAST(registrados as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							residencia+="='[valor]'"
						}else {
							residencia+="LIKE '%[valor]%'"
						}
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						break;
						
						
					case "RESIDENCIA":
						if (residencia.contains("WHERE")) {
							residencia += " AND "
						} else {
							residencia += " WHERE "
						}
						residencia +=" LOWER(residencia) ";
						if(filtro.get("operador").equals("Igual a")) {
							residencia+="=LOWER('[valor]')"
						}else {
							residencia+="LIKE LOWER('%[valor]%')"
						}
						residencia = residencia.replace("[valor]", filtro.get("valor"))
						break;
						
					case "FECHA":
						where +=" AND  ( LOWER( CAST(TO_CHAR(Pruebas.aplicacion, 'DD-MM-YYYY') as varchar)) LIKE LOWER('%[valor]%') ";
						where += "OR LOWER(Pruebas.entrada) LIKE LOWER('%[valor]%') "
						where += "OR LOWER(Pruebas.salida) LIKE LOWER('%[valor]%') )"
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "LUGAR":
						where +=" AND LOWER(Pruebas.lugar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
						
					case "TIPO DE PRUEBA":
						where +=" AND LOWER(ctipoprueba.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
								where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE DE LA SESION":
						where +=" AND LOWER(Sesion.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
								where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CAMPUS":
						where +=" AND LOWER(campus.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
								where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					}
					
					
				}
					errorlog+="llego al orderby "
					switch(object.orderby) {
						
						case "ID":
						orderby+="pruebas_id";
						break;
						case "NOMBRE":
						orderby+="Pruebas.nombre";
						break;
						case "ALUMNOS REGISTRADOS":
						orderby+="registrados";
						break;
						case "CUPO":
						orderby+="Pruebas.cupo";
						break;
						case "RESIDENCIA":
						orderby+="Sesion.tipo";
						break;
						case "FECHA":
						orderby+="Pruebas.aplicacion";
						break;
						case "LUGAR":
						orderby+="Pruebas.lugar";
						break;
						case "TIPO_PRUEBA":
						orderby+="ctipoprueba.descripcion";
						break;
						case "NOMBRE SESION":
						orderby+="Sesion.nombre";
						break;
						default:
						orderby+="Pruebas.aplicacion"
						break;
						
					}
					errorlog+="paso el order "
					orderby+=" "+object.orientation;
					consulta=consulta.replace("[WHERE]", where);
					consulta=consulta.replace("[CAMPUS]", campus);
					
					
					errorlog+="paso el where:"
					String consulta_aspirante =  Statements.EXT_SESIONESPSICOLOGO;
					
					String conteo = Statements.COUNT_SESIONESPSICOLOGO
					conteo=conteo.replace("[COUNTASPIRANTES]", consulta_aspirante)
					conteo=conteo.replace("[WHERE]", where);
					conteo=conteo.replace("[CAMPUS]", campus);
					conteo=conteo.replace("[RESIDENCIA]", residencia);
					pstm = con.prepareStatement(conteo)
					
					rs= pstm.executeQuery()
					if(rs.next()) {
						resultado.setTotalRegistros(rs.getInt("registros"))
					}
					errorlog+="paso el registro"
					
					
					// 
					consulta=consulta.replace("[ORDERBY]", orderby)
					consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
					consulta=consulta.replace("[RESIDENCIA]", residencia)
					consulta=consulta.replace("[COUNTASPIRANTES]", consulta_aspirante)
					errorlog+="conteo exitoso "
					
					pstm = con.prepareStatement(consulta)
					pstm.setInt(1, object.limit)
					pstm.setInt(2, object.offset)
					
					PruebasCustom Pruebas = new PruebasCustom();
					rs = pstm.executeQuery()
					errorlog+="Listado "
					while(rs.next()) {
						
						row = new PruebaCustom();
						row.setNombre(rs.getString("nombre"))
						row.setRegistrados(rs.getInt("registrados"));
						row.setLugar(rs.getString("lugar"));
						row.setPersistenceId(rs.getLong("pruebas_id"));
						row.setCupo(rs.getInt("cupo"));
						row.setTipo(new CatTipoPrueba())
						row.getTipo().setDescripcion(rs.getString("tipo_prueba"));
						row.setEntrada(rs.getString("entrada"));
						row.setSalida(rs.getString("salida"))
						
						SesionCustom row2 = new SesionCustom();
						row2.setFecha_inicio(rs.getString("aplicacion"));
						row2.setTipo(rs.getString("residencia"));
						row2.setPersistenceId(rs.getLong("sesiones_id"));
						row2.setNombre(rs.getString("nombre_sesion"));
						
						
						Pruebas = new PruebasCustom();
						Pruebas.setPrueba(row);
						Pruebas.setSesion(row2);
						
						rows.add(Pruebas)
					}
					
				resultado.setError_info(consulta +" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
			} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
				resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getSesionesPsicologoAdministradorAspirantes(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
		String where ="", orderby="ORDER BY AP.persistenceid, ", errorlog="", role="", group="", orderbyUsuario="ORDER BY " ;
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				
				
				String consulta = Statements.GET_ASPIRANTESPSICOLOGO
				SesionesAspiranteCustom row = new SesionesAspiranteCustom();
				List<SesionesAspiranteCustom> rows = new ArrayList<SesionesAspiranteCustom>();
				List<Map<String, Object>> aspirante = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				
				
				int tipo = 0;
				pstm = con.prepareStatement(Statements.GET_TIPOPRUEBA)
				pstm.setInt(1, object.prueba)
				rs= pstm.executeQuery();
				if(rs.next()) {
					tipo = (rs.getInt("tipoprueba_pid"))
				}
				
				String SSA = "";
				pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
				rs= pstm.executeQuery();
				if(rs.next()) {
					SSA = rs.getString("valor")
				}
				
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						
						
					case "NOMBRE, EMAIL, CURP":
						where +=" AND ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.curp) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CAMPUS, PROGRAMA, INGRESO":
						where +=" AND ( LOWER(campus.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(gestionescolar.nombre) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(CPO.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PROCEDENCIA, PREPARATORIA, PROMEDIO":
						/*where +=" AND ( LOWER(estado.DESCRIPCION) like lower('%[valor]%') ";*/
						where +=" AND (LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%')"
						where = where.replace("[valor]", filtro.get("valor"))
					
						where +="  OR LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.bachillerato ELSE prepa.descripcion END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
					
						where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ID BANNER":
						where +=" AND CAST(ds.idbanner as varchar) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="='[valor]'"
						}else {
							where+="LIKE '%[valor]%'"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "NOMBRE":
						where +="  AND LOWER(concat(sda.primernombre,' ', sda.segundonombre,' ',sda.apellidopaterno,' ',sda.apellidomaterno)) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "EMAIL":
						where +=" AND LOWER(sda.correoelectronico) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PROMEDIO":
							errorlog+="PROMEDIO"
							where +=" AND  CAST(sda.PROMEDIOGENERAL as varchar) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+=" ='[valor]' "
							}else {
								where+="LIKE '%[valor]%'"
							}
							where = where.replace("[valor]", filtro.get("valor"))
							break;
							
					case "PREPARATORIA":
							where +=" AND LOWER(prepa.DESCRIPCION) ";
							if(filtro.get("operador").equals("Igual a")) {
								where+="=LOWER('[valor]')"
							}else {
								where+="LIKE LOWER('%[valor]%')"
							}
							where= where.replace("[valor]", filtro.get("valor"))
							break;
							
					case "RESIDENCIA":
						where +=" AND LOWER(R.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "SEXO":
						where +=" AND LOWER(sx.descripcion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "LICENCIATURA":
						where +=" AND LOWER(gestionescolar.nombre) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "UNIVERSIDAD":
						where +=" AND LOWER(campus.DESCRIPCION) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "HORA DE LA ENTREVISTA":
						if(tipo == 1) {
							where+=" AND CAST(rd.horario as varchar) "
						}else {
							where+=" AND (CAST( concat(p.entrada,' - ',p.salida) as varchar)) "
						}
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ASISTENCIA":
						where +=" AND (PL.asistencia ";
						where+="= [valor] "
						where = where.replace("[valor]",  (filtro.get("valor").toString().equals("Sí")?"true)":"false OR PL.asistencia is NULL)"))
						break;
						
					
						
					}
					
					
					
					
				}
				
				errorlog+="llego al orderby "
				switch(object.orderby) {
					
					case "IDBANNER":
					orderby+="idbanner";
					break;
					case "NOMBRE":
					orderby+="apellidopaterno";
					break;
					case "EMAIL":
					orderby+="correoelectronico";
					break;
					case "PREPARATORIA":
					orderby+="preparatoria"
					break;
					case "CAMPUS":
					orderby+="campus"
					break;
					case "RESIDENCIA":
					orderby+="residencia";
					break;
					case "CURP":
					orderby+="curp";
					break;
					case "PROCEDENCIA":
					orderby+="preparatoriaEstado";
					break;
					case "INGRESO":
					orderby+="periodo";
					break;
					case "SEXO":
					orderby+="sexo";
					break;
					case "LICENCIATURA":
					orderby+="licenciatura";
					break;
					case "PROMEDIO":
					orderby+="promediogeneral";
					break;
					case "ASISTENCIA":
					orderby+= "asistencia";
					break;
					
					case "HORARIO":
					orderby+= "horario";
					break;
					
					default:
					orderby+="username"
					break;
					
				}
						
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[WHERE]", where);
				
				//String Group = "GROUP BY p.persistenceid,sa.username, sa.persistenceid,sa.persistenceversion,sa.sesiones_pid,sa.responsabledisponible_pid,rd.responsableid,RD.prueba_pid, P.aplicacion, P.nombre,c.descripcion,c.persistenceid,rd.horario,pl.asistencia,sda.curp,estado.DESCRIPCION,sda.caseId, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.telefonocelular, sda.correoelectronico, campus.descripcion, gestionescolar.nombre,  prepa.DESCRIPCION, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, da.idbanner, campus.grupoBonita, le.descripcion, sx.descripcion, CPO.descripcion , R.descripcion , da.cbCoincide,prepa.estado,sda.bachillerato,sda.estadobachillerato, sda.urlfoto";
				/*if(tipo == 1) {
					consulta=consulta.replace("[ENTREVISTA]", "AND rd.persistenceid = sa.responsabledisponible_pid")
				}else {
					consulta=consulta.replace("[ENTREVISTA]", "")
				}*/
				
				consulta=consulta.replace("[ENTREVISTA]", "")
				
				pstm = con.prepareStatement(consulta.replace("distinct on (AP.persistenceid) AP.persistenceid,P.nombre as nombre_prueba,P.Lugar as lugar_prueba,DS.IDBANNER,sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre,SDA.CORREOELECTRONICO,SDA.CURP,campus.descripcion AS campus,gestionescolar.nombre AS licenciatura, CPO.descripcion as periodo,CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia,sda.PROMEDIOGENERAL, CASE WHEN prepa.descripcion = 'Otro' THEN sda.bachillerato ELSE prepa.descripcion END AS preparatoria, R.descripcion as residencia, sx.descripcion as sexo, PL.ASISTENCIA, P.aplicacion, c.descripcion as tipo_prueba, case when C.persistenceid=1 then rd.horario  else concat(p.entrada,' - ',p.salida) end as horario, RD.PERSISTENCEID AS RD, DS.CASEID, sda.urlfoto,le.descripcion as lugarexamen,sda.telefonocelular,DS.cbCoincide,AP.acreditado,c.PERSISTENCEID as tipoprueba_pid,AP.USERNAME", "count(distinct AP.persistenceid) as  registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				pstm.setInt(1, object.prueba);
				pstm.setInt(2, object.sesion);
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				
				
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				consulta=consulta.replace("[GROUPBY]", "")
				errorlog+="conteo exitoso "
				
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.prueba);
				pstm.setInt(2, object.sesion);
				pstm.setInt(3, object.limit);
				pstm.setInt(4, object.offset);
				
				rs = pstm.executeQuery()
				
				aspirante = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();

					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
							String encoded = "";
							try {
								String urlFoto = rs.getString("urlfoto");
								if(urlFoto != null && !urlFoto.isEmpty()) {
									columns.put("fotografiab64", rs.getString("urlfoto") +SSA);
								}else {
									List<Document>doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
									for(Document doc : doc1) {
										encoded = "../API/formsDocumentImage?document="+doc.getId();
										columns.put("fotografiab64", encoded);
									}
								}
								
							}catch(Exception e) {
								columns.put("fotografiab64", "");
								errorlog+= ""+e.getMessage();
							}
						}
					}
					rows.add(columns);
				}
				
				
						
				resultado.setError_info(consulta+" errorLog = "+errorlog)
				resultado.setData(rows)
				resultado.setSuccess(true)
				
			} catch (Exception e) {
				resultado.setSuccess(false);
				resultado.setError(e.getMessage());
				resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getSesionesINVP(String sesion, String fecha, String uni, String id) {
		Result resultado = new Result()
		Boolean closeCon = false
		String where =""
		try {
			where += (sesion==null || sesion=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"s.nombre='"+sesion+"'"
			where += (fecha==null || fecha=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"to_char(p.aplicacion, 'DD/MM/YYYY')='"+fecha+"'"
			where += (uni==null || uni=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"cc.clave='"+uni+"'"
			where += (id==null || id=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"s.persistenceid="+id
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion()
			//SELECT s.persistenceid, s.nombre sesion, prueba.nombre prueba, prueba.cupo, prueba.aplicacion fecha, prueba.lugar from paselista pl inner join pruebas prueba on prueba.persistenceid=pl.prueba_pid and prueba.cattipoprueba_pid=2 inner join sesiones s on s.persistenceid=prueba.sesion_pid where pl.asistencia=true
			pstm = con.prepareStatement("SELECT distinct case when cr.segundonombre='' then cr.primernombre else cr.primernombre || ' ' || cr.segundonombre end as nombres, cr.apellidopaterno as APELLIDOP,cr.apellidomaterno as APELLIDOM,sda.correoelectronico as email,cc.clave || cda.idbanner as usuario,to_char(to_date(substring(sda.fechanacimiento,1,10),'YYYY-MM-DD'), 'DD/MM/YYYY') as fechanacimiento , cda.idbanner as id_siu,  s.nombre as sesion,s.persistenceid as id_sesion, to_char(p.aplicacion, 'DD/MM/YYYY') as fecharegistro, cc.clave as campusVPD, sexo.clave as sexo, '1' as activo, periodo.clave as periodo, '' tipousuario, cec.descripcion as ESTADO_CIVIL,sda.calle ||' #' || cc.numeroexterior || ' '|| sda.colonia ||', '||ce.descripcion || ' ' || sda.ciudad || ' CP. ' || sda.codigopostal direccion FROM catregistro cr inner join DETALLESOLICITUD cda on cda.caseid::bigint=cr.caseid inner join solicituddeadmision sda on sda.caseid=cda.caseid::bigint inner join catcampus cc on cc.persistenceid=sda.catcampusestudio_pid inner join paselista sa on sa.username=sda.correoelectronico AND sa.asistencia=true inner join pruebas p on sa.prueba_pid=p.persistenceid and p.cattipoprueba_pid=2 inner join sesiones s on s.persistenceid=p.sesion_pid INNER JOIN catsexo sexo ON sexo.persistenceid=sda.catsexo_pid INNER JOIN catperiodo periodo ON sda.catPeriodo_pid=periodo.persistenceid INNER JOIN catestadocivil cec on  sda.catestadocivil_pid=cec.persistenceid INNER JOIN catestados ce on ce.persistenceid=sda.catestado_pid "+ where)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
		} catch (Exception e) {
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
	public Result getResultadosINVPIndividuales() {
		Result resultado = new Result()
		Boolean closeCon = false
		String where =""
		String orderby= " ORDER BY ri.fecha_registro "
		String orientation =" DESC "
		try {
			
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion()
			//SELECT s.persistenceid, s.nombre sesion, prueba.nombre prueba, prueba.cupo, prueba.aplicacion fecha, prueba.lugar from paselista pl inner join pruebas prueba on prueba.persistenceid=pl.prueba_pid and prueba.cattipoprueba_pid=2 inner join sesiones s on s.persistenceid=prueba.sesion_pid where pl.asistencia=true
			pstm = con.prepareStatement("SELECT distinct  ri.idbanner, CASE WHEN cr.apellidomaterno=''THEN cr.apellidopaterno || ' ' || CASE WHEN cr.segundonombre=''THEN cr.primernombre ELSE cr.primernombre || ' ' || cr.segundonombre END ELSE cr.apellidopaterno||' '||cr.apellidomaterno ||' ' || CASE WHEN cr.segundonombre=''THEN cr.primernombre ELSE cr.primernombre || ' ' || cr.segundonombre END END                   AS nombre, s.persistenceid sesion_id, s.nombre sesion, p.aplicacion fecha_prueba, ri.fecha_registro FROM resultadoinvp ri INNER JOIN detallesolicitud ds on ds.idbanner=ri.idbanner INNER JOIN solicituddeadmision sda on sda.caseid::character varying=ds.caseid INNER JOIN catregistro cr on cr.caseid=sda.caseid INNER JOIN sesiones s on s.persistenceid=ri.sesiones_pid INNER JOIN pruebas p on p.sesion_pid=s.persistenceid and p.cattipoprueba_pid=2  "+ where + orderby + orientation)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
		} catch (Exception e) {
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
	public Result getSesiones(String sesion, String fecha, String uni, String id) {
		Result resultado = new Result()
		Boolean closeCon = false
		String where ="WHERE s.borrador=false"
		try {
			where = (sesion==null || sesion=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"s.nombre='"+sesion+"'"
			where = (fecha==null || fecha=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"to_char(p.aplicacion, 'DD/MM/YYYY')='"+fecha+"'"
			where = (uni==null || uni=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"cc.clave='"+uni+"'"
			where = (id==null || id=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"s.persistenceid="+id
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion()
			//SELECT s.persistenceid, s.nombre sesion, prueba.nombre prueba, prueba.cupo, prueba.aplicacion fecha, prueba.lugar from paselista pl inner join pruebas prueba on prueba.persistenceid=pl.prueba_pid and prueba.cattipoprueba_pid=2 inner join sesiones s on s.persistenceid=prueba.sesion_pid where pl.asistencia=true
			pstm = con.prepareStatement("SELECT distinct s.nombre as sesion, s.persistenceid as id_sesion, to_char(p.aplicacion, 'DD/MM/YYYY') as fecharegistro, cc.clave as campusVPD FROM sesiones s inner join catcampus cc on cc.persistenceid=s.campus_pid inner join pruebas p on s.persistenceid=p.sesion_pid  and p.cattipoprueba_pid=2 and p.registrados>0 inner join aspirantespruebas ap on ap.sesiones_pid=s.persistenceid and ap.asistencia=true "+ where)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
		} catch (Exception e) {
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
	public Result getSesionesINVPTabla(String sesion, String fecha, String uni, String id) {
		Result resultado = new Result()
		Boolean closeCon = false
		String where =" WHERE s.persistenceid not in (SELECT distinct sesiones_pid from resultadoinvp) "
		try {
			where += (sesion==null || sesion=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"s.nombre='"+sesion+"'"
			where += (fecha==null || fecha=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"to_char(p.aplicacion, 'DD/MM/YYYY')='"+fecha+"'"
			where += (uni==null || uni=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"cc.clave='"+uni+"'"
			where += (id==null || id=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"s.persistenceid="+id
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion()
			//SELECT s.persistenceid, s.nombre sesion, prueba.nombre prueba, prueba.cupo, prueba.aplicacion fecha, prueba.lugar from paselista pl inner join pruebas prueba on prueba.persistenceid=pl.prueba_pid and prueba.cattipoprueba_pid=2 inner join sesiones s on s.persistenceid=prueba.sesion_pid where pl.asistencia=true
			pstm = con.prepareStatement("SELECT distinct s.nombre as sesion,s.persistenceid as id_sesion,p.persistenceid id_prueba, to_char(p.aplicacion, 'DD/MM/YYYY') as fecharegistro, cc.clave as campusVPD, sexo.clave as sexo, '1' as activo, periodo.clave as periodo, '' tipousuario, cec.descripcion as ESTADO_CIVIL,sda.calle ||' #' || cc.numeroexterior || ' '|| sda.colonia ||', '||ce.descripcion || ' ' || sda.ciudad || ' CP. ' || sda.codigopostal direccion, p.nombre prueba, p.cupo, p.registrados, p.entrada, p.salida, p.lugar FROM catregistro cr inner join DETALLESOLICITUD cda on cda.caseid::bigint=cr.caseid inner join solicituddeadmision sda on sda.caseid=cda.caseid::bigint inner join catcampus cc on cc.persistenceid=sda.catcampusestudio_pid inner join paselista sa on sa.username=sda.correoelectronico AND sa.asistencia=true inner join pruebas p on sa.prueba_pid=p.persistenceid and p.cattipoprueba_pid=2 inner join sesiones s on s.persistenceid=p.sesion_pid INNER JOIN catsexo sexo ON sexo.persistenceid=sda.catsexo_pid INNER JOIN catperiodo periodo ON sda.catPeriodo_pid=periodo.persistenceid INNER JOIN catestadocivil cec on  sda.catestadocivil_pid=cec.persistenceid INNER JOIN catestados ce on ce.persistenceid=sda.catestado_pid INNER JOIN responsabledisponible rd on rd.prueba_pid=p.persistenceid "+ where)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
		} catch (Exception e) {
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
	public Result getSesionesINVPTablaProcesadas(String sesion, String fecha, String uni, String id) {
		Result resultado = new Result()
		Boolean closeCon = false
		String where =" WHERE s.persistenceid in (SELECT distinct sesiones_pid from resultadoinvp) "
		try {
			where += (sesion==null || sesion=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"s.nombre='"+sesion+"'"
			where += (fecha==null || fecha=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"to_char(p.aplicacion, 'DD/MM/YYYY')='"+fecha+"'"
			where += (uni==null || uni=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"cc.clave='"+uni+"'"
			where += (id==null || id=='')?"":(where.contains("WHERE")?" AND ":" WHERE ")+"s.persistenceid="+id
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion()
			//SELECT s.persistenceid, s.nombre sesion, prueba.nombre prueba, prueba.cupo, prueba.aplicacion fecha, prueba.lugar from paselista pl inner join pruebas prueba on prueba.persistenceid=pl.prueba_pid and prueba.cattipoprueba_pid=2 inner join sesiones s on s.persistenceid=prueba.sesion_pid where pl.asistencia=true
			pstm = con.prepareStatement("SELECT distinct s.nombre as sesion,s.persistenceid as id_sesion,p.persistenceid id_prueba, to_char(p.aplicacion, 'DD/MM/YYYY') as fecharegistro, cc.clave as campusVPD, sexo.clave as sexo, '1' as activo, periodo.clave as periodo, '' tipousuario, cec.descripcion as ESTADO_CIVIL,sda.calle ||' #' || cc.numeroexterior || ' '|| sda.colonia ||', '||ce.descripcion || ' ' || sda.ciudad || ' CP. ' || sda.codigopostal direccion, p.nombre prueba, p.cupo, p.registrados, p.entrada, p.salida, p.lugar FROM catregistro cr inner join DETALLESOLICITUD cda on cda.caseid::bigint=cr.caseid inner join solicituddeadmision sda on sda.caseid=cda.caseid::bigint inner join catcampus cc on cc.persistenceid=sda.catcampusestudio_pid inner join paselista sa on sa.username=sda.correoelectronico AND sa.asistencia=true inner join pruebas p on sa.prueba_pid=p.persistenceid and p.cattipoprueba_pid=2 inner join sesiones s on s.persistenceid=p.sesion_pid INNER JOIN catsexo sexo ON sexo.persistenceid=sda.catsexo_pid INNER JOIN catperiodo periodo ON sda.catPeriodo_pid=periodo.persistenceid INNER JOIN catestadocivil cec on  sda.catestadocivil_pid=cec.persistenceid INNER JOIN catestados ce on ce.persistenceid=sda.catestado_pid INNER JOIN responsabledisponible rd on rd.prueba_pid=p.persistenceid "+ where)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
		} catch (Exception e) {
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
	public Result getTipoEscala() {
		Result resultado = new Result()
		Boolean closeCon = false
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion()
			pstm = con.prepareStatement("SELECT escala,tipo FROM catescalatipo ")
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
		} catch (Exception e) {
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
	public Result getResultadoINVP(String idbanner,Long sesionid) {
		Result resultado = new Result()
		Boolean closeCon = false
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion()
			pstm = con.prepareStatement("SELECT r.idbanner,r.escala,r.puntuacion,r.sesiones_pid, ce.tipo FROM resultadoinvp r inner join catescalatipo ce on ce.escala=r.escala where r.idbanner=? and r.sesiones_pid=?")
			pstm.setString(1, idbanner)
			pstm.setLong(2, sesionid)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
		} catch (Exception e) {
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
	
	public Result getUsersByPrueba(Long pruebapid) {
		Result resultado = new Result()
		Boolean closeCon = false
		String where =""
		try {
			
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion()
			//SELECT s.persistenceid, s.nombre sesion, prueba.nombre prueba, prueba.cupo, prueba.aplicacion fecha, prueba.lugar from paselista pl inner join pruebas prueba on prueba.persistenceid=pl.prueba_pid and prueba.cattipoprueba_pid=2 inner join sesiones s on s.persistenceid=prueba.sesion_pid where pl.asistencia=true
			pstm = con.prepareStatement("SELECT distinct sda.urlfoto,sda.curp,case when cr.segundonombre='' then cr.primernombre else cr.primernombre || ' ' || cr.segundonombre end as nombres, cr.apellidopaterno as APELLIDOP,cr.apellidomaterno as APELLIDOM,sda.correoelectronico as email,cc.clave || cda.idbanner as usuario,to_char(to_date(substring(sda.fechanacimiento,1,10),'YYYY-MM-DD'), 'DD/MM/YYYY') as fechanacimiento , cda.idbanner as id_siu,  s.nombre as sesion,s.persistenceid as id_sesion,p.persistenceid id_prueba, to_char(p.aplicacion, 'DD/MM/YYYY') as fecharegistro, cc.clave as campusVPD,cc.descripcion campus, sexo.descripcion as sexo, '1' as activo, periodo.clave as periodo, '' tipousuario, cec.descripcion as ESTADO_CIVIL,sda.calle ||' #' || cc.numeroexterior || ' '|| sda.colonia ||', '||ce.descripcion || ' ' || sda.ciudad || ' CP. ' || sda.codigopostal direccion, p.nombre prueba, p.cupo, p.registrados, p.entrada, p.salida, p.lugar, cge.nombre licenciatura, prepa.descripcion as preparatoria, prepa.estado as preparatoriaestado, sda.promediogeneral, res.descripcion residencia FROM catregistro cr inner join DETALLESOLICITUD cda on cda.caseid::bigint=cr.caseid inner join solicituddeadmision sda on sda.caseid=cda.caseid::bigint inner join catcampus cc on cc.persistenceid=sda.catcampusestudio_pid inner join paselista sa on sa.username=sda.correoelectronico AND sa.asistencia=true inner join pruebas p on sa.prueba_pid=p.persistenceid and p.cattipoprueba_pid=2 inner join sesiones s on s.persistenceid=p.sesion_pid INNER JOIN catsexo sexo ON sexo.persistenceid=sda.catsexo_pid INNER JOIN catperiodo periodo ON sda.catPeriodo_pid=periodo.persistenceid INNER JOIN catestadocivil cec on  sda.catestadocivil_pid=cec.persistenceid INNER JOIN catestados ce on ce.persistenceid=sda.catestado_pid INNER JOIN responsabledisponible rd on rd.prueba_pid=p.persistenceid inner join catgestionescolar cge on cge.persistenceid=sda.catgestionescolar_pid left JOIN catbachilleratos prepa on prepa.persistenceid=sda.catbachilleratos_pid inner join catresidencia res on res.persistenceid=cda.catresidencia_pid WHERE p.persistenceid=? "+ where)
			pstm.setLong(1, pruebapid)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
		} catch (Exception e) {
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
	public Result getUserByIdbanner(String idBanner) {
		Result resultado = new Result()
		Boolean closeCon = false
		String where =""
		try {
			
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion()
			//SELECT s.persistenceid, s.nombre sesion, prueba.nombre prueba, prueba.cupo, prueba.aplicacion fecha, prueba.lugar from paselista pl inner join pruebas prueba on prueba.persistenceid=pl.prueba_pid and prueba.cattipoprueba_pid=2 inner join sesiones s on s.persistenceid=prueba.sesion_pid where pl.asistencia=true
			pstm = con.prepareStatement("SELECT distinct sda.urlfoto,sda.curp,case when cr.segundonombre='' then cr.primernombre else cr.primernombre || ' ' || cr.segundonombre end as nombres, cr.apellidopaterno as APELLIDOP,cr.apellidomaterno as APELLIDOM,sda.correoelectronico as email,cc.clave || cda.idbanner as usuario,to_char(to_date(substring(sda.fechanacimiento,1,10),'YYYY-MM-DD'), 'DD/MM/YYYY') as fechanacimiento , cda.idbanner as id_siu,  s.nombre as sesion,s.persistenceid as id_sesion,p.persistenceid id_prueba, to_char(p.aplicacion, 'DD/MM/YYYY') as fecharegistro, cc.clave as campusVPD,cc.descripcion campus, sexo.descripcion as sexo, '1' as activo, periodo.clave as periodo, '' tipousuario, cec.descripcion as ESTADO_CIVIL,sda.calle ||' #' || cc.numeroexterior || ' '|| sda.colonia ||', '||ce.descripcion || ' ' || sda.ciudad || ' CP. ' || sda.codigopostal direccion, p.nombre prueba, p.cupo, p.registrados, p.entrada, p.salida, p.lugar, cge.nombre licenciatura, prepa.descripcion as preparatoria, prepa.estado as preparatoriaestado, sda.promediogeneral, res.descripcion residencia,nacio.descripcion nacionalidad, pais.descripcion pais FROM catregistro cr inner join DETALLESOLICITUD cda on cda.caseid::bigint=cr.caseid inner join solicituddeadmision sda on sda.caseid=cda.caseid::bigint inner join catcampus cc on cc.persistenceid=sda.catcampusestudio_pid inner join paselista sa on sa.username=sda.correoelectronico AND sa.asistencia=true inner join pruebas p on sa.prueba_pid=p.persistenceid and p.cattipoprueba_pid=2 inner join sesiones s on s.persistenceid=p.sesion_pid INNER JOIN catsexo sexo ON sexo.persistenceid=sda.catsexo_pid INNER JOIN catperiodo periodo ON sda.catPeriodo_pid=periodo.persistenceid INNER JOIN catestadocivil cec on  sda.catestadocivil_pid=cec.persistenceid INNER JOIN catestados ce on ce.persistenceid=sda.catestado_pid INNER JOIN responsabledisponible rd on rd.prueba_pid=p.persistenceid inner join catgestionescolar cge on cge.persistenceid=sda.catgestionescolar_pid left JOIN catbachilleratos prepa on prepa.persistenceid=sda.catbachilleratos_pid inner join catresidencia res on res.persistenceid=cda.catresidencia_pid inner join catnacionalidad nacio on nacio.persistenceid=sda.catnacionalidad_pid inner join catpais pais on pais.persistenceid=sda.catpais_pid  WHERE cda.idbanner=? "+ where)
			pstm.setString(1, idBanner)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
		} catch (Exception e) {
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
	public Result insertRespuesta(String jsonData) {
		def jsonSlurper = new JsonSlurper();
		def object = jsonSlurper.parseText(jsonData);
		Result resultado = new Result()
		Boolean closeCon = false
		String where =""
		String idbanner=object.idbanner//"00845125"
		String respuesta = object.respuestas//"FCCCFCFCCCF*CCFFFFCCFFFFFFFFCFFFF*FFFFFFFFCFCCCFCFCFFFCFCCFFCCCCFFCFFFFFFFCCFFCFFFCFFCFCFCCFCFCFFCFFFCFFFCFCCFFCCFCFCFFC*CFCCCFCFFCCFFFFFFCCCCFFFFFCFFCCFFCFCFFCCFCCCFFFFCFFFCFCCFCFCFFFFCCCCFFCFCFFFFCFFFCCFCCCFCCCFFFFFFFFFCCCFCFFCFCFFFFFCFCFFFCCFFFFCFFFFFCFFFFCCCCFFCCFCFCCFFFCFCFCFFFFCFFFFCCFCFCFCFFFFFFFFFFFCFFFCCFFFCFFCFFFFFFFFCCFFFCFFFFCFFCFCCFFFCCCFCFFFFCCFFCFCFFFFCFCFFFFFFFFFFCFCFFCFFCFFFFFFCFFCFCCCCFFFCFFFCFCFFFFFCFFFCCFCFFFFFFFFCCCFCCFFFFFFFFCCFCFFCCCCFFFFFCFFFFCCCFFCFCFFFFFFFFFFCFCCCFCFFFFCFFCFFFFCFFFFFFFFFCFCFCFFFFFFFCCCCFFCFFFCFFFFFCFFFCFFFFFFCCCCCFCFFF"
		String sesiones_pid=object.id_sesion//"1"
		List < Map < String, Object >> respuestas = new ArrayList < Map < String, Object >> ();
		List < Map < String, String >> additionalData = new ArrayList < Map < String, String >> ();
		List < Map < String, Integer >> rows = new ArrayList < Map < String, Integer >> ();
		Map<String,Integer> respuestainvp= new HashMap  < String, Integer >()
		Map<String,String> aData= new HashMap  < String, String >()
		aData.put("idbanner", idbanner)
		aData.put("respuestas",respuesta)
		aData.put("id_sesion",sesiones_pid)
		try {
			closeCon = validarConexion()
			pstm = con.prepareStatement("SELECT persistenceid,  escala,  genero,  persistenceversion, pregunta, puntuacion, respuesta  FROM catrespuestasinvp where genero='ambos' OR genero=lower((SELECT cs.descripcion from solicituddeadmision sda inner join catsexo cs on cs.persistenceid=sda.catsexo_pid inner join detallesolicitud ds on ds.caseid::bigint=sda.caseid where ds.idbanner=? )) order by pregunta asc")
			pstm.setString(1, idbanner)
			rs = pstm.executeQuery()
			respuestas = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				respuestas.add(columns);
			}
			for(int i=0; i<respuestas.size(); i++) {
				if((respuesta.charAt(Integer.parseInt(respuestas.get(i).get("pregunta"))-1)=='C')==(respuestas.get(i).get("respuesta")=='t')) {
					if(respuestainvp.get(respuestas.get(i).get("escala"))==null) {
						respuestainvp.put(respuestas.get(i).get("escala"), Integer.parseInt(respuestas.get(i).get("puntuacion")))
					}else {
						respuestainvp.put(respuestas.get(i).get("escala"), respuestainvp.get(respuestas.get(i).get("escala"))+Integer.parseInt(respuestas.get(0).get("puntuacion")))
					}
					
				}else {
					if(respuestainvp.get(respuestas.get(i).get("escala"))==null) {
						respuestainvp.put(respuestas.get(i).get("escala"), 0)
					}
				}	
			}
			try {
				TimeZone tz = TimeZone.getTimeZone("UTC")
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS") // Quoted "Z" to indicate UTC, no timezone offset
				df.setTimeZone(tz)
				String nowAsISO = df.format(new Date())
				pstm = con.prepareStatement("SELECT idbanner,escala,puntuacion FROM resultadoinvp where idbanner=? and sesiones_pid=?")
				pstm.setString(1, idbanner)
				pstm.setLong(2, Long.parseLong(sesiones_pid))
				rs = pstm.executeQuery()
				if(!rs.next()) {
					for (Map.Entry<String,Integer> entry: respuestainvp) {
						pstm = con.prepareStatement("INSERT INTO resultadoinvp (idbanner,escala,puntuacion,sesiones_pid, persistenceid,persistenceversion,fecha_registro) values (?,?,?,?,case when (SELECT max(persistenceId)+1 from resultadoinvp ) is null then 1 else (SELECT max(persistenceId)+1 from resultadoinvp) end,0,?)")
						pstm.setString(1, idbanner)
						pstm.setString(2, entry.getKey())
						pstm.setInt(3, entry.getValue())
						pstm.setLong(4, Long.parseLong(sesiones_pid))
						pstm.setString(5, nowAsISO)
						pstm.execute()
					}
				}
			}
			catch(Exception test) {
				resultado.setError("Error")
				resultado.setError_info(test.getMessage())
			}
			
			rows.add(respuestainvp)
			additionalData.add(aData)
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setAdditional_data(additionalData)
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError(e.getMessage())
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	private static java.sql.Date convert(java.util.Date uDate) {
		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}
	//Convert Date to Calendar
	private Calendar dateToCalendar(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;

	}
}
