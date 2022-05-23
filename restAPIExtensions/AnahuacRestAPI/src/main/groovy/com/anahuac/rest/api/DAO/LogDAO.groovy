package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatNotificacionesFirma
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.sql.Statement

class LogDAO {
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
		return retorno;
	}
	public Result insertTransactionLog(String metodo, String estatus, String url, String respuesta, String descripcion) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
				List<CatNotificacionesFirma> rows = new ArrayList<CatNotificacionesFirma>();
				closeCon = validarConexion();
				pstm = con.prepareStatement("INSERT INTO transaction_log ( metodo, estatus, url, respuesta, descripcion) VALUES ( ?, ?, ?, ?, ?);")
				pstm.setString(1, metodo)
				pstm.setString(2, estatus)
				pstm.setString(3, url)
				pstm.setString(4, respuesta)
				pstm.setString(5, descripcion)
				pstm.execute()
				
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			}catch(SQLException sqle) {
				if(sqle.getMessage().toLowerCase().contains("exist")) {
					pstm=con.prepareStatement("CREATE TABLE transaction_log (id bigserial NOT NULL, metodo CHARACTER VARYING(500) NOT NULL, estatus CHARACTER VARYING(255) NOT NULL, url CHARACTER VARYING(500) NOT NULL, respuesta text NOT NULL, descripcion text NOT NULL, fecha_ejecucion TIMESTAMP without TIME zone DEFAULT NOW() NOT NULL, PRIMARY KEY (id) )")
					pstm.execute();
					resultado=insertTransactionLog(metodo, estatus, url, respuesta, descripcion)
				}else {
					resultado.setSuccess(false)
					resultado.setError("insertTransactionLog SQLException")
					resultado.setError_info(sqle.getMessage())
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
	public Result getTransactionLog() {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				pstm = con.prepareStatement("SELECT * FROM transaction_log order by id desc limit 100")
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
	
	public Result getHubspotLog() {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				pstm = con.prepareStatement("SELECT * FROM transaction_log where url like '%https://api.hubapi.com/contacts/v1/contact/createOrUpdate/email/%' order by id desc limit 1000")
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
	
	public Result insertBachilleratoLog(String operation, String usuarioBanner, String idBachillerato, String pais, String estado, String ciudad, String descripcion, String typeInd, String postalCode, String isEliminado, String isEstadoOk, String isCodigoPostalOk, String isMatchOk) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
				List<CatNotificacionesFirma> rows = new ArrayList<CatNotificacionesFirma>();
				closeCon = validarConexion();
				pstm = con.prepareStatement("INSERT INTO bachillerato_log ( operation, usuarioBanner, idBachillerato, pais, estado, ciudad, descripcion, typeInd, postalCode, isEliminado, isEstadoOk, isCodigoPostalOk, isMatchOk) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")
				pstm.setString(1, operation)
				pstm.setString(2, usuarioBanner)
				pstm.setString(3, idBachillerato)
				pstm.setString(4, pais)
				pstm.setString(5, estado)
				pstm.setString(6, ciudad)
				pstm.setString(7, descripcion)
				pstm.setString(8, typeInd)
				pstm.setString(9, postalCode)
				pstm.setString(10, isEliminado)
				pstm.setString(11, isEstadoOk)
				pstm.setString(12, isCodigoPostalOk)
				pstm.setString(13, isMatchOk)
				pstm.execute()
				
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			}catch(SQLException sqle) {
				if(sqle.getMessage().toLowerCase().contains("exist")) {
					pstm=con.prepareStatement("CREATE TABLE bachillerato_log (id bigserial NOT NULL, operation CHARACTER VARYING(50) NOT NULL, usuarioBanner CHARACTER VARYING(50) NOT NULL, idBachillerato CHARACTER VARYING(50) NOT NULL, pais CHARACTER VARYING(50) NOT NULL, estado CHARACTER VARYING(50) NOT NULL, ciudad CHARACTER VARYING(50) NOT NULL, descripcion CHARACTER VARYING(150) NOT NULL, typeInd CHARACTER VARYING(50) NOT NULL, postalCode CHARACTER VARYING(20) NOT NULL, isEliminado CHARACTER VARYING(10) NOT NULL, isEstadoOk CHARACTER VARYING(10) NOT NULL, isCodigoPostalOk CHARACTER VARYING(10) NOT NULL, isMatchOk CHARACTER VARYING(10) NOT NULL , fecha_ejecucion TIMESTAMP without TIME zone DEFAULT NOW() NOT NULL, PRIMARY KEY (id) )")
					pstm.execute();
					resultado=insertBachilleratoLog(operation, usuarioBanner, idBachillerato, pais, estado, ciudad, descripcion, typeInd, postalCode, isEliminado)
				}else {
					resultado.setSuccess(false)
					resultado.setError("insertBachilleratoLog SQLException")
					resultado.setError_info(sqle.getMessage())
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
	public Result getBachilleratoLog() {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				pstm = con.prepareStatement("SELECT * FROM bachillerato_log order by id desc limit 100")
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
}
