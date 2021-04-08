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
					calendario.add(Calendar.DAY_OF_YEAR, +5);
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
					orderby+="firstname";
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
					orderby+="u.id"
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
				(sesion.getIsmedicina()==null)?pstm.setNull(4, java.sql.Types.NULL):pstm.setBoolean(4, sesion.getIsmedicina())
				(sesion.getBachillerato_pid()==null)?pstm.setNull(5, java.sql.Types.NULL):pstm.setLong(5, sesion.getBachillerato_pid())
				(sesion.getEstado_pid()==null)?pstm.setNull(6, java.sql.Types.NULL):pstm.setLong(6, sesion.getEstado_pid())
				(sesion.getPais_pid()==null)?pstm.setNull(7, java.sql.Types.NULL):pstm.setLong(7, sesion.getPais_pid())
				(sesion.getBorrador()==null)?pstm.setBoolean(8, true):pstm.setBoolean(8, sesion.getBorrador())
				pstm.setLong(9, sesion.getCampus_pid())
				pstm.setString(10, sesion.getTipo())
				(sesion.getCiudad_pid()==null)?pstm.setNull(11, java.sql.Types.NULL):pstm.setLong(11, sesion.getCiudad_pid())
				if(sesion.getUltimo_dia_inscripcion().contains(":")) {
					pstm.setString(12, sesion.getUltimo_dia_inscripcion())
				}else {
					pstm.setString(12, sesion.getUltimo_dia_inscripcion()+" 23:59:59")
				}
				pstm.setBoolean(13, sesion.getIsEliminado())
				
				if(sesion.getPersistenceId()>0) {
					pstm.setLong(14, sesion.getPersistenceId())
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
								
								pstm = con.prepareStatement(Statements.INSERT_RESPONSABLEDISPONIBLE, Statement.RETURN_GENERATED_KEYS)
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
								try {
									pstm.setDate(1, convert(new SimpleDateFormat("yyyy-MM-dd").parse(prueba.getAplicacion())))
								}catch(Exception e) {
									pstm.setNull(1,java.sql.Types.NULL)
								}
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
	
	public Result insertSesionAspirante(Sesion_Aspirante sesionAspirante) {
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
		String where ="", orderby="ORDER BY ", errorlog="", role="", group="", residencia="";
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				String consulta = Statements.GET_SESIONESCALENDARIZADAS_REGISTRADOS
				PruebaCustom row = new PruebaCustom();
				List<PruebasCustom> rows = new ArrayList<PruebasCustom>();
				closeCon = validarConexion();
				errorlog+="llego a filtro "+object.lstFiltro.toString()
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						
						
					case "TIPO DE PRUEBA, RESIDENCIA":
						residencia +=" WHERE ( LOWER(residencia) LIKE LOWER('%[valor]%')";
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
						orderby+="Pruebas.registrados";
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
					//consulta=consulta.replace("[FECHA]", "'"+object.fecha+"'");
					errorlog+="paso el where"
					pstm = con.prepareStatement(consulta.replace("* from (SELECT DISTINCT(Pruebas.persistenceid)  as pruebas_id,   Pruebas.nombre, Pruebas.aplicacion, ( CASE WHEN Sesion.tipo LIKE '%R,F,E%'OR  Sesion.tipo LIKE '%R,E,F%'OR  Sesion.tipo LIKE '%F,R,E%'OR  Sesion.tipo LIKE '%F,E,R%'OR  Sesion.tipo LIKE '%E,F,R%'OR  Sesion.tipo LIKE '%E,R,F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false ) ELSE CASE WHEN Sesion.tipo LIKE '%R,F%'OR  Sesion.tipo LIKE '%F,R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='R')) ELSE CASE WHEN Sesion.tipo LIKE '%E,F%'OR  Sesion.tipo LIKE '%F,E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='E'))ELSE CASE WHEN Sesion.tipo LIKE '%R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R')) ELSE CASE WHEN Sesion.tipo LIKE '%E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'E')) ELSE CASE WHEN Sesion.tipo LIKE '%F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F')) ELSE(select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R' OR clave ='E'))END END END END END END ) as residencia, Sesion.persistenceid as sesiones_id, Pruebas.lugar, Pruebas.registrados as alumnos_generales, Sesion.nombre as nombre_sesion, ctipoprueba.descripcion as tipo_prueba, Pruebas.cupo, Pruebas.entrada,Pruebas.salida, [COUNTASPIRANTES]", "COUNT( DISTINCT(Pruebas.persistenceid)) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace(") as datos [RESIDENCIA]", ""))
					pstm.setInt(1, object.usuario)
					
					rs= pstm.executeQuery()
					if(rs.next()) {
						resultado.setTotalRegistros(rs.getInt("registros"))
					}
					errorlog+="paso el registro"
					
					String consulta_aspirante =  Statements.EXT_SESIONESCALENDARIZADAS_REGISTRADOS
					
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
		
		String where ="", orderby="ORDER BY ", errorlog="", role="", group="", orderbyUsuario="ORDER BY " ;
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				
				
				String consulta = Statements.GET_ASPIRANTEPRUEBAASISTIOYREAGENDOACTIVOS
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
						
				//orderby+="SA.username"
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[WHERE]", where);
				//consulta=consulta.replace("[FECHA]", "'"+object.fecha+"'");
				//String HavingGroup ="GROUP BY p.persistenceid,sa.username, sa.persistenceid,rd.responsableid,RD.prueba_pid, P.aplicacion, P.nombre,c.descripcion,c.persistenceid,rd.horario,pl.asistencia,sda.curp,estado.DESCRIPCION,sda.caseId, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.telefonocelular, sda.correoelectronico, campus.descripcion, gestionescolar.nombre,  prepa.DESCRIPCION, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, da.idbanner, campus.grupoBonita, le.descripcion, sx.descripcion, CPO.descripcion , R.descripcion , da.cbCoincide HAVING ((select count( CASE WHEN paseL.asistencia THEN 1 END) from PaseLista as paseL INNER JOIN PRUEBAS as P2 on paseL.prueba_pid = P2.persistenceid where paseL.prueba_pid != P.persistenceid and paseL.username =  SA.USERNAME AND P.cattipoprueba_pid = P2.cattipoprueba_pid) = 0)"
				String Group = "GROUP BY p.persistenceid,sa.username, sa.persistenceid,sa.persistenceversion,sa.sesiones_pid,sa.responsabledisponible_pid,rd.responsableid,RD.prueba_pid, P.aplicacion, P.nombre,c.descripcion,c.persistenceid,rd.horario,pl.asistencia,sda.curp,estado.DESCRIPCION,sda.caseId, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.telefonocelular, sda.correoelectronico, campus.descripcion, gestionescolar.nombre,  prepa.DESCRIPCION, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, da.idbanner, campus.grupoBonita, le.descripcion, sx.descripcion, CPO.descripcion , R.descripcion , da.cbCoincide,prepa.estado,sda.bachillerato,sda.estadobachillerato";
				if(tipo == 1) {
					consulta=consulta.replace("[ENTREVISTA]", "AND rd.persistenceid = sa.responsabledisponible_pid")
				}else {
					consulta=consulta.replace("[ENTREVISTA]", "")
				}
				//errorlog+="tipo "+tipo+" consulta count = "+consulta.replace("[COUNT]", "SELECT COUNT(*) as registros from(").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("[GROUPBY]", Group).replace("[COUNTFIN]", ") as CONTEO")
				//pstm = con.prepareStatement(consulta.replace("SELECT * from ", "SELECT COUNT(*) as registros from").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				pstm = con.prepareStatement(consulta.replace("[COUNT]", "SELECT COUNT(*) as registros from(").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("[GROUPBY]", Group).replace("[COUNTFIN]", ") as CONTEO"))
				pstm.setInt(1, object.sesion)
				pstm.setInt(2, object.prueba)
				pstm.setInt(3, object.usuario)
				pstm.setInt(4, object.sesion)
				pstm.setInt(5, object.prueba)
				//pstm.setInt(6, object.usuario)
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				
				
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				consulta=consulta.replace("[GROUPBY]", "")
				consulta=consulta.replace("[COUNT]", "")
				consulta=consulta.replace("[COUNTFIN]", "")
				errorlog+="conteo exitoso "
				//errorlog+=" consulta = "+consulta
				
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.sesion)
				pstm.setInt(2, object.prueba)
				pstm.setInt(3, object.usuario)
				pstm.setInt(4, object.sesion)
				pstm.setInt(5, object.prueba)
				//pstm.setInt(6, object.usuario)
				pstm.setInt(6, object.limit)
				pstm.setInt(7, object.offset)
				
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
					//aspirante.add(columns);
					rows.add(columns);
				}
				
				
				/*while(rs.next()) {	
					row = new SesionesAspiranteCustom();
					row.setUsername(rs.getString("username"))
					row.setSession_pid(rs.getLong("sesiones_pid"));
					row.setPersistenceid(rs.getLong("persistenceid"));
					row.setFecha(rs.getString("aplicacion"));
					row.setLugar_prueba(rs.getString("lugar_prueba"));
					row.setTipo_prueba(rs.getString("tipo_prueba"));
					row.setNombre_prueba(rs.getString("nombre_prueba"));
					row.setHorario(rs.getString("horario"));
					row.setTipoprueba_PID(rs.getString("tipoprueba_pid"));
					row.setEntrada(rs.getString("asistencia"))
					if(row.getEntrada() != null) {
						errorlog+="entro if "
						row.setAsistencia(rs.getBoolean("asistencia"));
					}
					
					
					//pstm = con.prepareStatement(Statements.GET_ASPIRANTESDELASESION)
					//pstm.setString(1, row.getUsername())
					//rs2 = pstm.executeQuery()
					errorlog+="otra llamada "
					aspirante = new ArrayList<Map<String, Object>>();
					ResultSetMetaData metaData = rs2.getMetaData();
					int columnCount = metaData.getColumnCount();
					
					while(rs2.next()) {
						Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
						for (int i = 1; i <= columnCount; i++) {
							columns.put(metaData.getColumnLabel(i).toLowerCase(), rs2.getString(i));
							if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
								String encoded = "";
								try {
									for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs2.getString(i)), "fotoPasaporte", 0, 10)) {
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
					row.setAspirantes(aspirante);
					rows.add(row);

				}*/
				
						
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
		String where ="", orderby="ORDER BY ", errorlog="", role="", campus="", group="",residencia="",having=" HAVING  (CAST(count(CASE WHEN PaseLista.asistencia THEN 1 END) as varchar) ";
		List<String> lstGrupo = new ArrayList<String>();
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				String consulta = Statements.GET_SESIONESCALENDARIZADASPASADAS_REGISTRADOS
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
						ARs = filtro.get("valor")+"";
						AR = true;
					/*
						where +=" AND CAST(Pruebas.registrados as varchar) ";
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
						
					case "ALUMNOS ASISTIERON":
						isHaving = true;
						if(filtro.get("operador").equals("Igual a")) {
							having+=" = LOWER('[valor]'))"
						}else {
								having+=" LIKE LOWER('%[valor]%'))"
						}
						having = having.replace("[valor]", filtro.get("valor"))
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
						orderby+="Pruebas.registrados";
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
						orderby+=" CAST(count(CASE WHEN PaseLista.asistencia THEN 1 END) as varchar)"
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
					String groupBy = "group by Pruebas.nombre, Pruebas.aplicacion, Sesion.tipo, Pruebas.persistenceid, Sesion.persistenceid, Pruebas.lugar, Pruebas.registrados, Sesion.nombre, ctipoprueba.descripcion, Pruebas.cupo, Pruebas.entrada,Pruebas.salida, campus.descripcion";
					String paginado = "";
					if(TR) {
						paginado += " AND ( LOWER (( CASE WHEN Sesion.tipo LIKE '%R,F,E%'OR Sesion.tipo LIKE '%R,E,F%'OR Sesion.tipo LIKE '%F,R,E%'OR Sesion.tipo LIKE '%F,E,R%'OR Sesion.tipo LIKE '%E,F,R%'OR Sesion.tipo LIKE '%E,R,F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false ) ELSE CASE WHEN Sesion.tipo LIKE '%R,F%'OR Sesion.tipo LIKE '%F,R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='R')) ELSE CASE WHEN Sesion.tipo LIKE '%E,F%'OR Sesion.tipo LIKE '%F,E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='E'))ELSE CASE WHEN Sesion.tipo LIKE '%R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R')) ELSE CASE WHEN Sesion.tipo LIKE '%E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'E')) ELSE CASE WHEN Sesion.tipo LIKE '%F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F')) ELSE(select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R' OR clave ='E'))END END END END END END )) like LOWER('%[VALOR]%') OR lower(ctipoprueba.descripcion) like lower('%[VALOR]%') )";
						paginado = paginado.replace("[VALOR]", TRs)
					}
					if(AR) {
						paginado += " AND CAST((SELECT COUNT(*) as registros from( SELECT * from ( SELECT distinct on (SA.persistenceid) SA.*, (select count( CASE WHEN paseL.asistencia THEN 1 END) from PaseLista as paseL INNER JOIN PRUEBAS as P2 on paseL.prueba_pid = P2.persistenceid where paseL.prueba_pid != P.persistenceid and paseL.username =  SA.USERNAME AND P.cattipoprueba_pid = P2.cattipoprueba_pid) as tieneOtraAsistencia FROM responsabledisponible as RD left join PRUEBAS  as P on P.persistenceid = RD.prueba_pid LEFT JOIN cattipoprueba c on c.PERSISTENCEID =p.cattipoprueba_pid LEFT JOIN SESIONES as S on S.persistenceid = P.sesion_pid LEFT JOIN sesionaspirante as SA on SA.sesiones_pid = S.persistenceid LEFT JOIN PaseLista as PL on PL.USERNAME = SA.USERNAME  AND PL.prueba_pid = P.persistenceid LEFT JOIN SOLICITUDDEADMISION sda ON sda.correoelectronico = SA.USERNAME LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID LEFT JOIN catPeriodo as CPO on CPO.persistenceid = sda.CATPERIODO_PID LEFT JOIN CATESTADOs estado ON estado.persistenceid =sda.CATESTADO_PID  LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID LEFT JOIN catLugarExamen as le on le.persistenceid = sda.CATLUGAREXAMEN_PID LEFT JOIN catSexo as sx on sx.persistenceid = sda.CATSEXO_PID LEFT JOIN CATRESIDENCIA as R on R.persistenceid = da.catresidencia_pid WHERE sda.caseid is not null AND SA.sesiones_pid = Sesion.persistenceid AND P.persistenceid = Pruebas.persistenceid       GROUP BY p.persistenceid,sa.username, sa.persistenceid HAVING ((select count( CASE WHEN paseL.asistencia THEN 1 END) from PaseLista as paseL INNER JOIN PRUEBAS as P2 on paseL.prueba_pid = P2.persistenceid where  paseL.prueba_pid != P.persistenceid and paseL.username =  SA.USERNAME AND P.cattipoprueba_pid = P2.cattipoprueba_pid) = 0) ORDER BY SA.persistenceid ASC  ) as datos where tieneOtraAsistencia = 0   UNION  select * from (SELECT distinct on (SA.persistenceid) SA.*,(select count( CASE WHEN paseL.asistencia THEN 1 END) from PaseLista as paseL INNER JOIN PRUEBAS as P2 on paseL.prueba_pid = P2.persistenceid where paseL.prueba_pid != P.persistenceid and paseL.username = SA.USERNAME AND P.cattipoprueba_pid = P2.cattipoprueba_pid) as tieneOtraAsistencia from PASELISTA PL LEFT JOIN PRUEBAS P on PL.prueba_pid = P.persistenceId LEFT JOIN SESIONES S on S.persistenceid = P.sesion_pid LEFT JOIN responsabledisponible as RD on P.persistenceid = RD.prueba_pid LEFT JOIN cattipoprueba c on c.PERSISTENCEID =p.cattipoprueba_pid LEFT JOIN SOLICITUDDEADMISION sda ON sda.correoelectronico = PL.USERNAME LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID LEFT JOIN catPeriodo as CPO on CPO.persistenceid = sda.CATPERIODO_PID LEFT JOIN CATESTADOs estado ON estado.persistenceid =sda.CATESTADO_PID LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID LEFT JOIN catSexo as sx on sx.persistenceid = sda.CATSEXO_PID LEFT JOIN CATRESIDENCIA as R on R.persistenceid = da.catresidencia_pid LEFT JOIN catLugarExamen as le on le.persistenceid = sda.CATLUGAREXAMEN_PID LEFT JOIN sesionaspirante as SA on SA.username = PL.username  WHERE sda.caseid is not null AND PL.asistencia = 'true'  AND S.persistenceid = Sesion.persistenceid AND P.persistenceid = Pruebas.persistenceid        GROUP BY p.persistenceid,sa.username, sa.persistenceid,sa.persistenceversion,sa.sesiones_pid,sa.responsabledisponible_pid ORDER BY SA.persistenceid ASC ) as asistencia  ) as CONTEO) as varchar) like '%[VALOR]%'";
						paginado = paginado.replace("[VALOR]", ARs)
					}
					
					//consulta=consulta.replace("[FECHA]", "'"+object.fecha+"'");					
					//errorlog+="paso el where2  consulta =="+consulta.replace("* from (SELECT Pruebas.nombre, Pruebas.aplicacion, ( CASE WHEN Sesion.tipo LIKE '%R,F,E%'OR Sesion.tipo LIKE '%R,E,F%'OR Sesion.tipo LIKE '%F,R,E%'OR Sesion.tipo LIKE '%F,E,R%'OR Sesion.tipo LIKE '%E,F,R%'OR Sesion.tipo LIKE '%E,R,F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false ) ELSE CASE WHEN Sesion.tipo LIKE '%R,F%'OR Sesion.tipo LIKE '%F,R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='R')) ELSE CASE WHEN Sesion.tipo LIKE '%E,F%'OR Sesion.tipo LIKE '%F,E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='E'))ELSE CASE WHEN Sesion.tipo LIKE '%R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R')) ELSE CASE WHEN Sesion.tipo LIKE '%E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'E')) ELSE CASE WHEN Sesion.tipo LIKE '%F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F')) ELSE(select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R' OR clave ='E'))END END END END END END ) as residencia, Pruebas.persistenceid as pruebas_id, Sesion.persistenceid as sesiones_id, Pruebas.lugar, Pruebas.registrados as alumnos_generales, Sesion.nombre as nombre_sesion,ctipoprueba.descripcion as tipo_prueba, Pruebas.cupo, Pruebas.entrada,Pruebas.salida, campus.descripcion AS campus, count(CASE WHEN PaseLista.asistencia THEN 1 END) as asistencias, (select String_AGG(distinct rd.responsableid::varchar,',') from responsabledisponible as rd where rd.isEliminado = false and rd.prueba_pid = Pruebas.persistenceid) as responsables, [COUNTASPIRANTES]", "COUNT(DISTINCT(Pruebas.persistenceid)) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("[GROUPBY]", "").replace("[HAVING]", "").replace(") as datos [RESIDENCIA]", paginado)
					pstm = con.prepareStatement(consulta.replace("* from (SELECT Pruebas.nombre, Pruebas.aplicacion, ( CASE WHEN Sesion.tipo LIKE '%R,F,E%'OR Sesion.tipo LIKE '%R,E,F%'OR Sesion.tipo LIKE '%F,R,E%'OR Sesion.tipo LIKE '%F,E,R%'OR Sesion.tipo LIKE '%E,F,R%'OR Sesion.tipo LIKE '%E,R,F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false ) ELSE CASE WHEN Sesion.tipo LIKE '%R,F%'OR Sesion.tipo LIKE '%F,R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='R')) ELSE CASE WHEN Sesion.tipo LIKE '%E,F%'OR Sesion.tipo LIKE '%F,E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='E'))ELSE CASE WHEN Sesion.tipo LIKE '%R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R')) ELSE CASE WHEN Sesion.tipo LIKE '%E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'E')) ELSE CASE WHEN Sesion.tipo LIKE '%F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F')) ELSE(select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R' OR clave ='E'))END END END END END END ) as residencia, Pruebas.persistenceid as pruebas_id, Sesion.persistenceid as sesiones_id, Pruebas.lugar, Pruebas.registrados as alumnos_generales, Sesion.nombre as nombre_sesion,ctipoprueba.descripcion as tipo_prueba, Pruebas.cupo, Pruebas.entrada,Pruebas.salida, campus.descripcion AS campus, count(CASE WHEN PaseLista.asistencia THEN 1 END) as asistencias, (select String_AGG(distinct rd.responsableid::varchar,',') from responsabledisponible as rd where rd.isEliminado = false and rd.prueba_pid = Pruebas.persistenceid) as responsables, [COUNTASPIRANTES]", "COUNT(DISTINCT(Pruebas.persistenceid)) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("[GROUPBY]", (isHaving?groupBy:"")).replace("[HAVING]", (isHaving?having:"")).replace(") as datos [RESIDENCIA]", paginado))
					
					rs= pstm.executeQuery()
					int count = 0;
					if(!isHaving && rs.next()) {
						count = (rs.getInt("registros"));
					}else{
						while(rs.next()) {
							count += rs.getInt("registros");
						}
					}
					resultado.setTotalRegistros(count)
					errorlog+="conteo exitoso "
					
					String consulta_EXT = Statements.EXT_SESIONESCALENDARIZADAS;
					
					consulta=consulta.replace("[COUNTASPIRANTES]", consulta_EXT)
					consulta=consulta.replace("[GROUPBY]", groupBy)
					consulta=consulta.replace("[HAVING]", (isHaving ? having:""))
					consulta=consulta.replace("[ORDERBY]", orderby)
					consulta=consulta.replace("[RESIDENCIA]", residencia)
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
						
						//psi.setFirstname(usr.getFirstName())
						//psi.setLastname(usr.getLastName())
						String responsables = rs.getString("responsables");
						String nombres= "";
						if(!responsables.equals("null") && responsables != null) {
							errorlog+=" "+responsables;
							String[] arrOfStr = responsables.split(",");
							for (String a: arrOfStr) {
								if(Long.parseLong(a)>0) {
									usr = context.getApiClient().getIdentityAPI().getUser(Long.parseLong(a))
									//membership=context.getApiClient().getIdentityAPI().getUserMemberships(usr.getId(), 0, 100, UserMembershipCriterion.GROUP_NAME_ASC).get(0)
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
		String where ="", orderby="ORDER BY ", errorlog="", role="", group="", orderbyUsuario="ORDER BY sda.primernombre";
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				String consulta = Statements.GET_ASPIRANTEPRUEBAASISTIOYREAGENDO
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
										
				//orderby+="SA.username"
				orderby+=" "+object.orientation;
				errorlog+="order by = "+orderby
				consulta=consulta.replace("[WHERE]", where);
				//consulta=consulta.replace("[FECHA]", "'"+object.fecha+"'");
				String HavingGroup ="GROUP BY p.persistenceid,sa.username, sa.persistenceid,sa.persistenceversion,sa.sesiones_pid,sa.responsabledisponible_pid,rd.responsableid,RD.prueba_pid, P.aplicacion, P.nombre,c.descripcion,c.persistenceid,rd.horario,pl.asistencia,sda.curp,estado.DESCRIPCION ,sda.caseId, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.telefonocelular, sda.correoelectronico, campus.descripcion, gestionescolar.nombre,  prepa.DESCRIPCION, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, da.idbanner, campus.grupoBonita, le.descripcion, sx.descripcion, CPO.descripcion, R.descripcion , da.cbCoincide,prepa.estado,sda.bachillerato,sda.estadobachillerato HAVING ((select count( CASE WHEN paseL.asistencia THEN 1 END) from PaseLista as paseL INNER JOIN PRUEBAS as P2 on paseL.prueba_pid = P2.persistenceid where paseL.prueba_pid != P.persistenceid and paseL.username =  SA.USERNAME AND P.cattipoprueba_pid = P2.cattipoprueba_pid) = 0)"
				String Group = "GROUP BY p.persistenceid,sa.username, sa.persistenceid,sa.persistenceversion,sa.sesiones_pid,sa.responsabledisponible_pid,rd.responsableid,RD.prueba_pid, P.aplicacion, P.nombre,c.descripcion,c.persistenceid,rd.horario,pl.asistencia,sda.curp,estado.DESCRIPCION,sda.caseId, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.telefonocelular, sda.correoelectronico, campus.descripcion, gestionescolar.nombre,  prepa.DESCRIPCION, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, da.idbanner, campus.grupoBonita, le.descripcion, sx.descripcion, CPO.descripcion , R.descripcion , da.cbCoincide,prepa.estado,sda.bachillerato,sda.estadobachillerato";
				if(tipo == 1 ) {
					consulta=consulta.replace("[ENTREVISTA]", "AND rd.persistenceid = sa.responsabledisponible_pid")
					if(object.reporte && object.type == null) {
						consulta=consulta.replace("[REPORTE]", "AND rd.responsableid = "+object.usuario)
					}else {
						consulta=consulta.replace("[REPORTE]", "")
					}
				}else {
					consulta=consulta.replace("[ENTREVISTA]", "")
					consulta=consulta.replace("[REPORTE]", "")
				}
				
				
				//errorlog+=" consulta =  "+ consulta.replace("[COUNT]", "SELECT COUNT(*) as registros from(").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("[HAVING]", HavingGroup).replace("[GROUPBY]", Group).replace("[COUNTFIN]", ") as CONTEO")
				//pstm = con.prepareStatement(consulta.replace("SELECT * from ", "SELECT COUNT(*) as registros FROM").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				pstm = con.prepareStatement(consulta.replace("[COUNT]", "SELECT COUNT(*) as registros from(").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("[HAVING]", HavingGroup).replace("[GROUPBY]", Group).replace("[COUNTFIN]", ") as CONTEO"))
				pstm.setInt(1, object.sesion)
				pstm.setInt(2, object.prueba)
				pstm.setInt(3, object.sesion)
				pstm.setInt(4, object.prueba)
				
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				consulta=consulta.replace("[HAVING]", "")
				consulta=consulta.replace("[GROUPBY]", "")
				consulta=consulta.replace("[COUNT]", "")
				consulta=consulta.replace("[COUNTFIN]", "")
				errorlog+="conteo exitoso "
				
				errorlog+=" consulta :"+consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.sesion)
				pstm.setInt(2, object.prueba)
				pstm.setInt(3, object.sesion)
				pstm.setInt(4, object.prueba)
				pstm.setInt(5, object.limit)
				pstm.setInt(6, object.offset)
				
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
								for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
									encoded = "../API/formsDocumentImage?document="+doc.getId();
									columns.put("fotografiab64", encoded);
								}
							}catch(Exception e) {
								columns.put("fotografiab64", "");
								errorlog+= "esto = "+e.getMessage();
							}
						}
					}
					//aspirante.add(columns);
					rows.add(columns);
				}
				
				
				/*
				while(rs.next()) {
					row = new SesionesAspiranteCustom();
					row.setUsername(rs.getString("username"))
					row.setSession_pid(rs.getLong("sesiones_pid"));
					row.setPersistenceid(rs.getLong("persistenceid"));
					row.setFecha(rs.getString("aplicacion"));
					row.setLugar_prueba(rs.getString("lugar_prueba"));
					row.setTipo_prueba(rs.getString("tipo_prueba"));
					row.setNombre_prueba(rs.getString("nombre_prueba"));
					row.setHorario(rs.getString("horario"));
					row.setTipoprueba_PID(rs.getString("tipoprueba_pid"));
					row.setEntrada(rs.getString("asistencia"))
					if(row.getEntrada() != null) {
						errorlog+="entro if "
						row.setAsistencia(rs.getBoolean("asistencia"));
					}
					
					
					//pstm = con.prepareStatement(Statements.GET_ASPIRANTESDELASESION)
					//pstm.setString(1, row.getUsername())
					
					//rs2 = pstm.executeQuery()
					errorlog+="otra llamada "
					aspirante = new ArrayList<Map<String, Object>>();
					ResultSetMetaData metaData = rs2.getMetaData();
					int columnCount = metaData.getColumnCount();
					
					while(rs2.next()) {
						Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
						for (int i = 1; i <= columnCount; i++) {
							columns.put(metaData.getColumnLabel(i).toLowerCase(), rs2.getString(i));
							if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
								String encoded = "";
								try {
									for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs2.getString(i)), "fotoPasaporte", 0, 10)) {
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
					rows.add(row);

				}
				
				*/
				
				
						
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
		String where ="", orderby="ORDER BY ", errorlog="", role="", campus="", group="", residencia="",having=" HAVING  (CAST(count(CASE WHEN PaseLista.asistencia THEN 1 END) as varchar)";
		Boolean isHaving = false;
		List<String> lstGrupo = new ArrayList<String>();
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		String consulta = Statements.GET_SESIONESCALENDARIZADASREPORTE_REGISTRADOS
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				//AND CAST(P.aplicacion AS DATE) [ORDEN] CAST([FECHA] AS DATE)
				PruebaCustom row = new PruebaCustom();
				List<PruebasCustom> rows = new ArrayList<PruebasCustom>();
				closeCon = validarConexion();
				errorlog+="llego a filtro "+object.lstFiltro.toString()
				
				
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
						AR = true;
						ARs = filtro.get("valor")+"";
						/*
						where +=" AND CAST(Pruebas.registrados as varchar) ";
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
						isHaving = true;
						if(filtro.get("operador").equals("Igual a")) {
							having+=" = LOWER('[valor]'))"
						}else {
								having+=" LIKE LOWER('%[valor]%'))"
						}
						having = having.replace("[valor]", filtro.get("valor"))
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
						orderby+="Pruebas.registrados";
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
						orderby+=" count(CASE WHEN PaseLista.asistencia THEN 1 END)"
						break;
						default:
						orderby+="Pruebas.aplicacion"
						break;
						
					}
					errorlog+="paso el order "
					orderby+=" "+object.orientation;
					consulta=consulta.replace("[WHERE]", where);
					//consulta=consulta.replace("[FECHA]", "'"+object.fecha+"'");
					errorlog+="paso el where"
					String groupBy = "group by Pruebas.persistenceid, Pruebas.nombre, Pruebas.aplicacion, Sesion.tipo, Sesion.persistenceid, Pruebas.lugar, Pruebas.registrados, Sesion.nombre, ctipoprueba.descripcion, Pruebas.cupo, Pruebas.entrada,Pruebas.salida";
					String paginado = "";
					if(TR) {
						paginado += " AND ( LOWER (( CASE WHEN Sesion.tipo LIKE '%R,F,E%'OR Sesion.tipo LIKE '%R,E,F%'OR Sesion.tipo LIKE '%F,R,E%'OR Sesion.tipo LIKE '%F,E,R%'OR Sesion.tipo LIKE '%E,F,R%'OR Sesion.tipo LIKE '%E,R,F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false ) ELSE CASE WHEN Sesion.tipo LIKE '%R,F%'OR Sesion.tipo LIKE '%F,R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='R')) ELSE CASE WHEN Sesion.tipo LIKE '%E,F%'OR Sesion.tipo LIKE '%F,E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='E'))ELSE CASE WHEN Sesion.tipo LIKE '%R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R')) ELSE CASE WHEN Sesion.tipo LIKE '%E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'E')) ELSE CASE WHEN Sesion.tipo LIKE '%F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F')) ELSE(select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R' OR clave ='E'))END END END END END END )) like LOWER('%[VALOR]%') OR lower(ctipoprueba.descripcion) like lower('%[VALOR]%') )";
						paginado = paginado.replace("[VALOR]", TRs)
					}
					if(AR) {
						paginado += " AND CAST((SELECT COUNT(*) as registros from( SELECT * from ( SELECT distinct on (SA.persistenceid) SA.*, (select count( CASE WHEN paseL.asistencia THEN 1 END) from PaseLista as paseL INNER JOIN PRUEBAS as P2 on paseL.prueba_pid = P2.persistenceid where paseL.prueba_pid != P.persistenceid and paseL.username =  SA.USERNAME AND P.cattipoprueba_pid = P2.cattipoprueba_pid) as tieneOtraAsistencia FROM responsabledisponible as RD left join PRUEBAS  as P on P.persistenceid = RD.prueba_pid LEFT JOIN cattipoprueba c on c.PERSISTENCEID =p.cattipoprueba_pid LEFT JOIN SESIONES as S on S.persistenceid = P.sesion_pid LEFT JOIN sesionaspirante as SA on SA.sesiones_pid = S.persistenceid LEFT JOIN PaseLista as PL on PL.USERNAME = SA.USERNAME  AND PL.prueba_pid = P.persistenceid LEFT JOIN SOLICITUDDEADMISION sda ON sda.correoelectronico = SA.USERNAME LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID LEFT JOIN catPeriodo as CPO on CPO.persistenceid = sda.CATPERIODO_PID LEFT JOIN CATESTADOs estado ON estado.persistenceid =sda.CATESTADO_PID  LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID LEFT JOIN catLugarExamen as le on le.persistenceid = sda.CATLUGAREXAMEN_PID LEFT JOIN catSexo as sx on sx.persistenceid = sda.CATSEXO_PID LEFT JOIN CATRESIDENCIA as R on R.persistenceid = da.catresidencia_pid WHERE sda.caseid is not null AND SA.sesiones_pid = Sesion.persistenceid AND P.persistenceid = Pruebas.persistenceid       GROUP BY p.persistenceid,sa.username, sa.persistenceid HAVING ((select count( CASE WHEN paseL.asistencia THEN 1 END) from PaseLista as paseL INNER JOIN PRUEBAS as P2 on paseL.prueba_pid = P2.persistenceid where  paseL.prueba_pid != P.persistenceid and paseL.username =  SA.USERNAME AND P.cattipoprueba_pid = P2.cattipoprueba_pid) = 0) ORDER BY SA.persistenceid ASC  ) as datos where tieneOtraAsistencia = 0   UNION  select * from (SELECT distinct on (SA.persistenceid) SA.*,(select count( CASE WHEN paseL.asistencia THEN 1 END) from PaseLista as paseL INNER JOIN PRUEBAS as P2 on paseL.prueba_pid = P2.persistenceid where paseL.prueba_pid != P.persistenceid and paseL.username = SA.USERNAME AND P.cattipoprueba_pid = P2.cattipoprueba_pid) as tieneOtraAsistencia from PASELISTA PL LEFT JOIN PRUEBAS P on PL.prueba_pid = P.persistenceId LEFT JOIN SESIONES S on S.persistenceid = P.sesion_pid LEFT JOIN responsabledisponible as RD on P.persistenceid = RD.prueba_pid LEFT JOIN cattipoprueba c on c.PERSISTENCEID =p.cattipoprueba_pid LEFT JOIN SOLICITUDDEADMISION sda ON sda.correoelectronico = PL.USERNAME LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID LEFT JOIN catPeriodo as CPO on CPO.persistenceid = sda.CATPERIODO_PID LEFT JOIN CATESTADOs estado ON estado.persistenceid =sda.CATESTADO_PID LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID LEFT JOIN catSexo as sx on sx.persistenceid = sda.CATSEXO_PID LEFT JOIN CATRESIDENCIA as R on R.persistenceid = da.catresidencia_pid LEFT JOIN catLugarExamen as le on le.persistenceid = sda.CATLUGAREXAMEN_PID LEFT JOIN sesionaspirante as SA on SA.username = PL.username  WHERE sda.caseid is not null AND PL.asistencia = 'true'  AND S.persistenceid = Sesion.persistenceid AND P.persistenceid = Pruebas.persistenceid        GROUP BY p.persistenceid,sa.username, sa.persistenceid,sa.persistenceversion,sa.sesiones_pid,sa.responsabledisponible_pid ORDER BY SA.persistenceid ASC ) as asistencia  ) as CONTEO) as varchar) like '%[VALOR]%'";
						paginado = paginado.replace("[VALOR]", ARs)
					}
					
					errorlog+=" conteo = "+consulta.replace("* from (SELECT DISTINCT(Pruebas.persistenceid)  as pruebas_id,   Pruebas.nombre, Pruebas.aplicacion, ( CASE WHEN Sesion.tipo LIKE '%R,F,E%'OR Sesion.tipo LIKE '%R,E,F%'OR Sesion.tipo LIKE '%F,R,E%'OR Sesion.tipo LIKE '%F,E,R%'OR Sesion.tipo LIKE '%E,F,R%'OR Sesion.tipo LIKE '%E,R,F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false ) ELSE CASE WHEN Sesion.tipo LIKE '%R,F%'OR Sesion.tipo LIKE '%F,R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='R')) ELSE CASE WHEN Sesion.tipo LIKE '%E,F%'OR Sesion.tipo LIKE '%F,E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='E'))ELSE CASE WHEN Sesion.tipo LIKE '%R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R')) ELSE CASE WHEN Sesion.tipo LIKE '%E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'E')) ELSE CASE WHEN Sesion.tipo LIKE '%F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F')) ELSE(select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R' OR clave ='E'))END END END END END END ) as residencia, Sesion.persistenceid as sesiones_id, Pruebas.lugar, Pruebas.registrados as alumnos_generales, Sesion.nombre as nombre_sesion, ctipoprueba.descripcion as tipo_prueba, Pruebas.cupo, Pruebas.entrada,Pruebas.salida, count(CASE WHEN PaseLista.asistencia THEN 1 END) as asistencias, [COUNTASPIRANTES]", "COUNT( DISTINCT(Pruebas.persistenceid)) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("[HAVING]", (isHaving ? having:"")).replace("[GROUPBY]", (isHaving?groupBy:"")).replace(") as datos [RESIDENCIA]",paginado)
					pstm = con.prepareStatement(consulta.replace("* from (SELECT DISTINCT(Pruebas.persistenceid)  as pruebas_id,   Pruebas.nombre, Pruebas.aplicacion, ( CASE WHEN Sesion.tipo LIKE '%R,F,E%'OR Sesion.tipo LIKE '%R,E,F%'OR Sesion.tipo LIKE '%F,R,E%'OR Sesion.tipo LIKE '%F,E,R%'OR Sesion.tipo LIKE '%E,F,R%'OR Sesion.tipo LIKE '%E,R,F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false ) ELSE CASE WHEN Sesion.tipo LIKE '%R,F%'OR Sesion.tipo LIKE '%F,R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='R')) ELSE CASE WHEN Sesion.tipo LIKE '%E,F%'OR Sesion.tipo LIKE '%F,E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F' OR clave ='E'))ELSE CASE WHEN Sesion.tipo LIKE '%R%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R')) ELSE CASE WHEN Sesion.tipo LIKE '%E%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'E')) ELSE CASE WHEN Sesion.tipo LIKE '%F%'THEN (select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'F')) ELSE(select String_AGG(R.descripcion,',') from catresidencia as R where isEliminado = false and (clave = 'R' OR clave ='E'))END END END END END END ) as residencia, Sesion.persistenceid as sesiones_id, Pruebas.lugar, Pruebas.registrados as alumnos_generales, Sesion.nombre as nombre_sesion, ctipoprueba.descripcion as tipo_prueba, Pruebas.cupo, Pruebas.entrada,Pruebas.salida, count(CASE WHEN PaseLista.asistencia THEN 1 END) as asistencias, [COUNTASPIRANTES]", "COUNT( DISTINCT(Pruebas.persistenceid)) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("[HAVING]", (isHaving ? having:"")).replace("[GROUPBY]", (isHaving?groupBy:"")).replace(") as datos [RESIDENCIA]", paginado))
					pstm.setInt(1, object.usuario)
					
					rs= pstm.executeQuery()
					int count = 0;
					if(!isHaving && rs.next()) {
						count = (rs.getInt("registros"));
					}else{
						while(rs.next()) {
							count += rs.getInt("registros");
						}
					}
					resultado.setTotalRegistros(count)
					
					String consulta_EXT = Statements.EXT_SESIONESCALENDARIZADAS;
					
					consulta=consulta.replace("[ORDERBY]", orderby)
					consulta=consulta.replace("[GROUPBY]", groupBy)
					consulta=consulta.replace("[HAVING]", (isHaving ? having:""))
					consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
					consulta=consulta.replace("[RESIDENCIA]", residencia)
					consulta=consulta.replace("[COUNTASPIRANTES]", consulta_EXT)
					errorlog+="having "+isHaving
					
					pstm = con.prepareStatement(consulta)
					pstm.setInt(1, object.usuario)
					pstm.setInt(2, object.limit)
					pstm.setInt(3, object.offset)
					
					PruebasCustom Pruebas = new PruebasCustom();
					rs = pstm.executeQuery()
					errorlog+="Listado "
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
				
				String consulta = Statements.GET_ASPIRANTESEXCELLISTADO
				SesionesAspiranteCustom row = new SesionesAspiranteCustom();
				List<SesionesAspiranteCustom> rows = new ArrayList<SesionesAspiranteCustom>();
				List<Map<String, Object>> aspirante = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				
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
						
						where +=" AND (LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%')"
						where = where.replace("[valor]", filtro.get("valor"))
					
						where +="  OR LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.bachillerato ELSE prepa.descripcion END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
					
						where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
									
						
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
				
				String HavingGroup ="GROUP BY p.persistenceid,sa.username,sa.persistenceid,rd.responsableid,RD.prueba_pid, P.aplicacion, P.nombre,c.descripcion,c.persistenceid,rd.horario,pl.asistencia,sda.curp,estado.DESCRIPCION ,sda.caseId, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.telefonocelular, sda.correoelectronico, campus.descripcion, gestionescolar.nombre,  prepa.DESCRIPCION, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, da.idbanner, campus.grupoBonita, le.descripcion, sx.descripcion, CPO.descripcion, R.descripcion , da.cbCoincide,prepa.estado,sda.bachillerato,sda.estadobachillerato"
				String Group = "GROUP BY p.persistenceid,sa.username,sa.persistenceid,rd.responsableid,RD.prueba_pid, P.aplicacion, P.nombre,c.descripcion,c.persistenceid,rd.horario,pl.asistencia,sda.curp,estado.DESCRIPCION,sda.caseId, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.telefonocelular, sda.correoelectronico, campus.descripcion, gestionescolar.nombre,  prepa.DESCRIPCION, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, da.idbanner, campus.grupoBonita, le.descripcion, sx.descripcion, CPO.descripcion , R.descripcion , da.cbCoincide,prepa.estado,sda.bachillerato,sda.estadobachillerato";
				
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				consulta=consulta.replace("[HAVING]", HavingGroup)
				consulta=consulta.replace("[GROUPBY]", Group)
				consulta=consulta.replace("[COUNT]", "")
				consulta=consulta.replace("[COUNTFIN]", "")
				consulta=consulta.replace("[ENTREVISTA]", "")
				consulta=consulta.replace("[REPORTE]", "")
				
				errorlog+=" consulta :"+consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.sesion)
				pstm.setInt(2, object.prueba)
				pstm.setInt(3, object.sesion)
				pstm.setInt(4, object.prueba)
				pstm.setInt(5, object.limit)
				pstm.setInt(6, object.offset)
				
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
