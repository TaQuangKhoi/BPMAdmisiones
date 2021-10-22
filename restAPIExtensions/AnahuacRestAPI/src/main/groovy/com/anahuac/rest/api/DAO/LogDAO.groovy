package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatNotificacionesFirma
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
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
}
