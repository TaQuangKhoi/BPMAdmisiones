package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

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
			pstm.setLong(3, campus_pid);
			rs = pstm.executeQuery();
			while (rs.next()) {
				row = new LoginSesion()
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
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			closeCon = validarConexion();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(Statements.UPDATE_SESION_USUARIO);
			pstm.setBoolean(1, object.havesesion);
			pstm.setString(2, object.username);
			pstm.executeUpdate();
			con.commit();
			
			success = true;
			if(resultReq > 0) {
				error_log = resultReq + " Exito! query UPDATE_IDIOMA_REGISTRO_BY_USERNAME"
			} else {
				error_log = resultReq + " Error! query UPDATE_IDIOMA_REGISTRO_BY_USERNAME"
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
}
