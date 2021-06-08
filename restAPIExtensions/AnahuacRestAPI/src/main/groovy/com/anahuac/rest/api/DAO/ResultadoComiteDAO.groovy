package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.DB.Statements

import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.SimpleDateFormat
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.bpm.document.Document


class ResultadoComiteDAO {
	
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
		return retorno
	}
	
	
	public Result postValidarUsuario(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				
				List<Map<String, Object>> estatus = new ArrayList<Map<String, Object>>();
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				
				 String[] idBanner = object.IDBANNER.split(",");
				 for(int j = 0; j < idBanner.size(); ++j) {
					 //errorLog += Statements.GET_EXISTE_Y_DATOS_DUPLICADOS.replace("[VALOR]",object.IDBANNER)
					 pstm = con.prepareStatement(Statements.GET_EXISTE_Y_DATOS_DUPLICADOS.replace("[VALOR]",idBanner[j]))
					 rs= pstm.executeQuery();
					 columns = new LinkedHashMap<String, Object>();
					 columns.put("idBanner", idBanner[j] )
					 columns.put("Registrado",false)
					 columns.put("Existe",false)
					 columns.put("EstaEnCarga",false)
					 columns.put("mismaFecha",false)
					 if(rs.next()) {
						 columns.put("Registrado",isNullOrEmpty(rs.getString("idbanner")))
						 columns.put("Existe",isNullOrEmpty(rs.getString("dsbanner")))
						 columns.put("EstaEnCarga",isNullOrEmpty(rs.getString("primernombre")))
						 columns.put("mismaFecha",(rs.getInt("mismafecha") == 1?true:false))
					 }
					 estatus.add(columns)
				 }
				
				
				resultado.setSuccess(true)
				resultado.setData(estatus)
				resultado.setError_info(errorLog)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Boolean isNullOrEmpty(String text) {
		
		if(text?.equals("null") || text?.equals("") || text?.equals(" ") || text?.length() < 1) {
			return false
		}
		return true
	}
	
	
	
	
}
