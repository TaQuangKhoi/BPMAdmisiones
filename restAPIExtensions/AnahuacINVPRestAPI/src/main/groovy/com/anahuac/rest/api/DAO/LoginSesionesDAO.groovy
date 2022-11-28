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
	
	public Result getCatPreguntas(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			LoginSesion row = new LoginSesion();
			List<LoginSesion> rows = new ArrayList<LoginSesion>();
			closeCon = validarConexion();
			String consulta = Statements.GET_SESION_LOGIN;
			pstm = con.prepareStatement(consulta);
			pstm.setString(1, object.aplicacion);
			pstm.setString(2, object.username);
			pstm.setLong(3, object.campus_pid);
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
}
