package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement

import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.custom.CatPreguntasCustomFiltro
import com.anahuac.rest.api.Entity.custom.SesionesCustom

import groovy.json.JsonSlurper

class CatalogosDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogosDAO.class);
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
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true
		}
		return retorno;
	}
	public Result simpleSelect(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof List;
			
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				for(def row: object) {
				pstm = con.prepareStatement(row)
				
				
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
				}
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
	
	public Result simpleSelectBonita(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof List;
			
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexionBonita();
				for(def row: object) {
				pstm = con.prepareStatement(row)
				
				
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
				}
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
	
public Result getCatPreguntas(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "WHERE ISELIMINADO=false", orderby = "ORDER BY ", errorlog = ""

		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatPreguntasCustomFiltro row = new CatPreguntasCustomFiltro()
			List < CatPreguntasCustomFiltro > rows = new ArrayList < CatPreguntasCustomFiltro > ();
			closeCon = validarConexion();

			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				switch (filtro.get("columna")) {
					case "ORDEN":
						if (where.contains("WHERE")) {
							where += " AND ";
						} else {
							where += " WHERE ";
						}
						where += " LOWER(COALESCE(orden, 0)::VARCHAR) ";
						if (filtro.operador.equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.valor);
						break;
					case "PREGUNTA":
						if (where.contains("WHERE")) {
							where += " AND ";
						} else {
							where += " WHERE ";
						}
						where += " LOWER(PREGUNTA) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "QUESTION":
						if (where.contains("WHERE")) {
							where += " AND ";
						} else {
							where += " WHERE ";
						}
						where += " LOWER(QUESTION) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "USUARIO CREACIÓN":
						if (where.contains("WHERE")) {
							where += " AND ";
						} else {
							where += " WHERE ";
						}
						where += " LOWER(usuariocreacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')";
						} else {
							where += "LIKE LOWER('%[valor]%')";
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "FECHA CREACIÓN":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " TO_CHAR(TO_TIMESTAMP(FECHACREACION, 'YYYY-MM-DD HH24:MI:SS'), 'DD-MM-YYYY HH24:MI:SS') ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				}
			}

			switch (object.orderby) {
				case "PREGUNTA":
					orderby += "clave";
					break;
				case "QUESTION":
					orderby += "descripcion";
					break;
				case "USUARIO CREACIÓN":
					orderby += "usuariocreacion";
					break;
				case "FECHA CREACIÓN":
					orderby += "fechacreacion";
					break;
				default:
					orderby += "ORDEN";
					break;
			}

			orderby += " " + object.orientation;
			String consulta = Statements.GET_CAT_PREGUNTAS;
			consulta = consulta.replace("[WHERE]", where);
			errorlog += "consulta:";
			errorlog += consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", "");
			pstm = con.prepareStatement(consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""));
			rs = pstm.executeQuery();
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"));
			}
			consulta = consulta.replace("[ORDERBY]", orderby);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
			errorlog += "consulta:";
			errorlog += consulta;
			pstm = con.prepareStatement(consulta);
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new CatPreguntasCustomFiltro()
				row.setPregunta(rs.getString("pregunta"));
				row.setCaseId(rs.getString("CASEID"));
				row.setOrden(rs.getLong("orden"));
				row.setQuestion(rs.getString("question"));
				row.setIsEliminado(rs.getBoolean("isEliminado"));
				row.setUsuarioCreacion(rs.getString("usuarioCreacion"));
				try {
					row.setFechaCreacion(rs.getString("fechacreacion"))
				} catch (Exception e) {
					LOGGER.error "[ERROR] " + e.getMessage();
					errorlog += e.getMessage()
				}
				row.setPersistenceId(rs.getLong("persistenceId"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				rows.add(row)
			}
			resultado.setSuccess(true)

			resultado.setData(rows)
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getCatPreguntasExamen() {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "WHERE ISELIMINADO=false"
		String errorlog = "";
		try {
			CatPreguntasCustomFiltro row = new CatPreguntasCustomFiltro()
			List < CatPreguntasCustomFiltro > rows = new ArrayList < CatPreguntasCustomFiltro > ();
			closeCon = validarConexion();

			errorlog += "consulta:";
			pstm = con.prepareStatement(Statements.GET_CAT_PREGUNTAS_EXAMEN.replace("[WHERE]", where));
			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new CatPreguntasCustomFiltro()
				row.setPregunta(rs.getString("pregunta"));
				row.setCaseId(rs.getString("CASEID"));
				row.setOrden(rs.getLong("orden"));
				row.setQuestion(rs.getString("question"));
				row.setIsEliminado(rs.getBoolean("isEliminado"));
				row.setUsuarioCreacion(rs.getString("usuarioCreacion"));
				try {
					row.setFechaCreacion(rs.getString("fechacreacion"))
				} catch (Exception e) {
					LOGGER.error "[ERROR] " + e.getMessage();
					errorlog += e.getMessage()
				}
				row.setPersistenceId(rs.getLong("persistenceId"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				rows.add(row)
			}
			resultado.setSuccess(true)

			resultado.setData(rows)
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getSesiones(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "WHERE ctp.iseliminado <> true ";
		String errorlog = "";
		String orderBy = "";
		List < String > lstGrupo = new ArrayList < String > ();
		String errorLog = "";
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)
			Long userLogged = context.getApiSession().getUserId();
			List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)

			for (UserMembership objUserMembership: lstUserMembership) {
				for (CatCampus rowGrupo: lstCatCampus) {
					if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}
			
			if (lstGrupo.size() > 0 && object.campus != null ) {
				where += " AND (";
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					where += " cca.descripcion = '" + campusMiembro + "'"
					if (i == (lstGrupo.size() - 1)) {
						where += ") "
					} else {
						where += " OR "
					}
				}
			}
			

//			assert object instanceof Map;
			
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				switch (filtro.get("columna")) {

					case "No. ":
						errorlog += "id_sesion "
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(id_sesion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Nombre":
						errorlog += "ses.nombre"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(ses.nombre) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Uni.":
						errorlog += "cca.descripcion"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(cca.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Sesión":
						errorlog += " ses.nombre"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(ses.nombre) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Fecha":
						errorlog += "ses.fecha_inicio"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( ses.fecha_inicio::VARCHAR like '%[valor]%' )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Hr. Inicio":
						errorlog += "p.entrada"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(p.entrada) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Hr. Término":
						errorlog += " p.salida"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(p.salida) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Estatus":
//						errorlog += "cca.descripcion"
//						if (where.contains("WHERE")) {
//							where += " AND "
//						} else {
//							where += " WHERE "
//						}
//						where += " ( LOWER(cca.descripcion) like lower('%[valor]%') )";
//						where = where.replace("[valor]", filtro.get("valor"))
//						break;
					case "Aspirantes":
						errorlog += " p.registrados"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( p.registrados::VARCHAR like ('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "Idioma":
						errorlog += "cca.descripcion"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(cca.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					default:
						break;
				}
			}
			
			switch(object.orderby) {
				case "id_sesion":
					orderBy = " ORDER BY id_sesion " + object.orientation;
				break;
				case "nombre":
					orderBy = " ORDER BY ses.nombre " + object.orientation;
				break;
				case "uni":
					orderBy = " ORDER BY cca.descripcion " + object.orientation;
				break;
				case "sesion":
					orderBy = " ORDER BY ses.nombre " + object.orientation;
				break;
				case "fecha_prueba":
					orderBy = " ORDER BY ses.fecha_inicio " + object.orientation;
				break;
				case "entrada_prueba":
					orderBy = " ORDER BY p.entrada " + object.orientation;
				break;
				case "salida_prueba":
					orderBy = " ORDER BY p.salida " + object.orientation;
				break;
				case "estatus":
//					orderBy = " ORDER BY id_sesion " + object.orientation;
				break;
				case "registrados_prueba":
					orderBy = " ORDER BY p.registrados " + object.orientation;
				break;
				case "idioma":
//					orderBy = " ORDER BY id_sesion " + object.orientation;
				break;
				default:
					orderBy = " ORDER BY id_sesion " + object.orientation;
				break;
			}
			
//			errorLog += where;
			
			SesionesCustom row = new SesionesCustom();
			List <SesionesCustom> rows = new ArrayList <SesionesCustom>();
			closeCon = validarConexion();
			
			String consultaCcount = Statements.GET_COUNT_SESONES_TODAS.replace("[WHERE]", where);
			pstm = con.prepareStatement(consultaCcount);
			rs = pstm.executeQuery();
			while (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("total_registros"));
			}
			
			String consulta = Statements.GET_SESONES_TODAS.replace("[WHERE]", where).replace("[ORDERBY]", orderBy)
			pstm = con.prepareStatement(consulta);
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery();

			while (rs.next()) {
				row = new SesionesCustom();
				row.setIdSesion(rs.getLong("id_sesion"));
				row.setNombreSesion(rs.getString("nombre_sesion"));
				row.setUni(rs.getString("campus"));
				row.setSesion(rs.getString("descripcion_sesion"));
				row.setFecha(rs.getString("fecha_prueba"));
				row.setHoraInicio(rs.getString("entrada_prueba"));
				row.setHoraTermino(rs.getString("salida_prueba"));
				row.setEstatus("");
				row.setAspirantes(rs.getString("registrados_prueba"));
				
				rows.add(row);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		
		return resultado;
	}
	
}
