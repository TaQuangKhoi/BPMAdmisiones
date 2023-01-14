package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.LoginSesion
import com.anahuac.rest.api.Entity.Result

import groovy.json.JsonSlurper

class LoginSesionesDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginSesionesDAO.class);
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

	public Result getSesionLogin(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		String horafinal = "";
		try {
			Long campus_pid = 0L;
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			LoginSesion row = new LoginSesion();
			List<LoginSesion> rows = new ArrayList<LoginSesion>();
			closeCon = validarConexion();
			String consulta = Statements.GET_CAT_CAMPUS_PID_BY_CORREO;
			pstm = con.prepareStatement(consulta);
			pstm.setString(1, object.username);
			rs = pstm.executeQuery();
			if(rs.next()) {
				errorlog = " | " + errorlog + " | "
				campus_pid = rs.getLong("catcampus_pid");
			}
			
			errorlog = " | campus_pid " + campus_pid + " | "
			consulta = Statements.GET_SESION_LOGIN;
			pstm = con.prepareStatement(consulta);
			pstm.setString(1, object.aplicacion);
			pstm.setString(2, object.username);
			//pstm.setLong(3, campus_pid);
			rs = pstm.executeQuery();
			while (rs.next()) {
				/*row = new LoginSesion()
				row.setDescripcion(rs.getString("descripcion"))
				row.setUsername(rs.getString("username"))
				row.setEntrada(rs.getString("entrada"));
				try {
					row.setAplicacion(rs.getString("aplicacion"))
				} catch (Exception e) {
					LOGGER.error "[ERROR] " + e.getMessage();
					errorlog += e.getMessage()
				}
				row.setPersistenceId(rs.getLong("persistenceId"))
				rows.add(row)*/
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
				LocalTime hora = LocalTime.parse(rs.getString("entrada"));
				errorlog = " | consulta_hora " + hora.format(formatter) + " | "
				LocalTime horaModificada = hora.plusMinutes(6000);
				
				horafinal = horaModificada.format(formatter);
			}
			
			errorlog = " | consulta_hora " + horafinal + " | "
			consulta = Statements.GET_SESION_LOGIN_HORA;
			pstm = con.prepareStatement(consulta);
			pstm.setString(1, object.aplicacion);
			pstm.setString(2, object.username);
			pstm.setString(3, horafinal);
			rs = pstm.executeQuery();
			while (rs.next()) {
				errorlog += " dentro de la consulta " + " | "
				row = new LoginSesion()
				row.setPersistenceId(rs.getLong("idsesion"));
				row.setNombre_sesion(rs.getString("nombresesion"));
				
				//tipoexamen
				row.setDescripcion(rs.getString("descripcion"))
				row.setNombre_prueba(rs.getString("nombre_prueba"));
				row.setId_prueba(rs.getLong("id_prueba"));
				try {
					row.setAplicacion(rs.getString("aplicacion"))
				} catch (Exception e) {
					LOGGER.error "[ERROR] " + e.getMessage();
					errorlog += e.getMessage()
				}
				row.setEntrada(rs.getString("entrada"));
				row.setSalida(rs.getString("salida"));
				row.setUsername(rs.getString("username"));
				rows.add(row)
			}
			
			
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updateUsuarioSesion(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		Boolean success = false;
		String error_log = "";
		String success_log = "";
		Long resultReq = 0;
		Boolean havesesion = false;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			con.setAutoCommit(false);
			
			pstm = con.prepareStatement(Statements.GET_SESION_USUARIO);
			pstm.setString(1, object.username);
			rs = pstm.executeQuery();
			if (rs.next()) {
				
				havesesion = rs.getBoolean("havesesion");
			}
			
			
			error_log += " havesesion  | valor " + havesesion;
			if(!havesesion) {
				pstm = con.prepareStatement(Statements.INSERT_BLOQUEO_USUARIO);
				pstm.setBoolean(1, Boolean.valueOf(object.havesesion));
				pstm.setString(2, object.username);
				rs = pstm.executeQuery();
				if(rs.next()) {
					resultReq = rs.getLong("persistenceid")
				}
				success = true;
				if(resultReq > 0) {
					error_log = resultReq + " Exito! query INSERT_BLOQUEO_USUARIO"
				} else {
					error_log = resultReq + " Error! query INSERT_BLOQUEO_USUARIO"
				}
				con.commit();
			}else {
				pstm = con.prepareStatement(Statements.UPDATE_SESION_USUARIO);
				pstm.setBoolean(1, Boolean.valueOf(object.havesesion));
				pstm.setString(2, object.username);
				pstm.executeUpdate();
				con.commit();
				
				success = true;
				if(resultReq > 0) {
					error_log += resultReq + " Exito! query UPDATE_IDIOMA_REGISTRO_BY_USERNAME"
				} else {
					error_log += resultReq + " Error! query UPDATE_IDIOMA_REGISTRO_BY_USERNAME"
				}
			}
			
			resultado.setSuccess(true)
			resultado.setError_info(errorlog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getSesionActiva(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		try {
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
			Map<String,Object> row = new HashMap<String,Object>(); 
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.GET_SESION_USUARIO);
			pstm.setString(1, object.username);
			rs = pstm.executeQuery();
			while (rs.next()) {
				row = new HashMap<String,Object>();
				row.put("havesesion", rs.getBoolean("havesesion")); 
				rows.add(row)
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result insertterminado(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		Boolean success = false;
		String error_log = "";
		String success_log = "";
		Long resultReq = 0;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.INSERT_TERMINADO_EXAMEN);
			pstm.setString(1, object.username);
			pstm.setBoolean(2, object.terminado);
			
			pstm.executeUpdate();
			/*rs = pstm.executeQuery();
			if(rs.next()) {
				resultReq = rs.getLong("persistenceid")
			}
			
			success = true;
			if(resultReq > 0) {
				error_log = resultReq + " Exito! query INSERT_TERMINADO_EXAMEN"
			} else {
				error_log = resultReq + " Error! query INSERT_TERMINADO_EXAMEN"
			}*/
			con.commit();
			
			resultado.setSuccess(true)
			resultado.setError_info(errorlog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result updateterminado(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		Boolean success = false;
		String error_log = "";
		String success_log = "";
		Long resultReq = 0;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.UPDATE_TERMINADO_EXAMEN);
			pstm.setBoolean(1, object.terminado);
			pstm.setString(2, object.username);
			pstm.executeUpdate();
			con.commit();
			
			success = true;
			if(resultReq > 0) {
				error_log = resultReq + " Exito! query UPDATE_TERMINADO_EXAMEN"
			} else {
				error_log = resultReq + " Error! query UPDATE_TERMINADO_EXAMEN"
			}
			
			resultado.setSuccess(true)
			resultado.setError_info(errorlog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getTotalPreguntasContestadas(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		try {
			Map<String,Integer> row = new HashMap<String,Integer>();
			List<Map<String,Integer>> rows = new ArrayList<Map<String,Integer>>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_COUNT_PREGUNTASCONTESTADAS_BY_USERNAME);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			while (rs.next()) {
				row = new HashMap<String,Integer>();
				row.put("totalPreguntas", rs.getInt("totalpreguntas"))
				rows.add(row)
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getTerminadoExamen(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		try {
			Map<String,Boolean> row = new HashMap<String,Boolean>();
			List<Map<String,Integer>> rows = new ArrayList<Map<String,Integer>>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_TERMINADO_EXAMEN);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			while (rs.next()) {
				row = new HashMap<String,Integer>();
				row.put("terminado", rs.getBoolean("terminado"))
				rows.add(row)
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getDatosSesionLogin(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		String horafinal = "";
		try {
			Long campus_pid = 0L;
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			LoginSesion row = new LoginSesion();
			List<LoginSesion> rows = new ArrayList<LoginSesion>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_DATOS_SESION_LOGIN);
			pstm.setString(1, object.username);
			//pstm.setLong(3, campus_pid);
			rs = pstm.executeQuery();
			while (rs.next()) {
				errorlog += " dentro de la consulta " + " | "
				row = new LoginSesion()
				row.setPersistenceId(rs.getLong("idsesion"));
				row.setNombre_sesion(rs.getString("nombresesion"));
				
				//tipoexamen
				row.setDescripcion(rs.getString("descripcion"))
				row.setNombre_prueba(rs.getString("nombre_prueba"));
				row.setId_prueba(rs.getLong("id_prueba"));
				try {
					row.setAplicacion(rs.getString("aplicacion"))
				} catch (Exception e) {
					LOGGER.error "[ERROR] " + e.getMessage();
					errorlog += e.getMessage()
				}
				row.setEntrada(rs.getString("entrada"));
				row.setSalida(rs.getString("salida"));
				row.setUsername(rs.getString("username"));
				rows.add(row)
			}
			
			
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getidbanner(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		try {
			Map<String,Boolean> row = new HashMap<String,Boolean>();
			List<Map<String,Integer>> rows = new ArrayList<Map<String,Integer>>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_ID_BANNER_BY_CORREO);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			while (rs.next()) {
				row = new HashMap<String,Integer>();
				row.put("idbanner", rs.getInt("idbanner"))
				row.put("telefono", rs.getString("telefono"));
				row.put("telefonocelular", rs.getString("telefonocelular"));
				rows.add(row)
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getcelularusuariotemporal(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		try {
			Map<String,Boolean> row = new HashMap<String,Boolean>();
			List<Map<String,Integer>> rows = new ArrayList<Map<String,Integer>>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.GET_CELULAR_BY_USERNAME);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			while (rs.next()) {
				row = new HashMap<String,Integer>();
				row.put("telefonocelular", rs.getString("telefonocelular"));
				rows.add(row)
			}
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			errorlog = errorlog + " | " + e.getMessage();
			resultado.setError_info(errorlog);
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
}
