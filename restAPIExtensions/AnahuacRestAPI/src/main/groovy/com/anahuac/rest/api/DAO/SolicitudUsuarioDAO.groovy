package com.anahuac.rest.api.DAO

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.ContactoEmergenciaEntity
import com.anahuac.rest.api.Entity.Custom.PadresTutorEntity
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement

class SolicitudUsuarioDAO {
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
	public Result getIdbanner(Integer parameterP, Integer parameterC, String idbanner, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_IDBANNER_BY_IDBANNER)
				pstm.setString(1, idbanner)

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
			
				resultado.setError_info("error al obtener GET_IDBANNER_BY_IDBANNER")
				resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getPadresTutorVencido(Integer parameterP,Integer parameterC,String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			PadresTutorEntity row = new PadresTutorEntity()
			List<PadresTutorEntity> rows = new ArrayList<PadresTutorEntity>();
			closeCon = validarConexion();
			assert object instanceof Map;
			String consulta = Statements.GET_PADRESTUTOR_VENCIDOS;
			errorlog+="consulta:"
			errorlog+=consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.caseid)
			rs = pstm.executeQuery()
			while(rs.next()) {
				row = new PadresTutorEntity()
				row.setPersistenceId(rs.getLong("persistenceId"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				row.setApellidos(rs.getString("apellidos"))
				row.setCalle(rs.getString("calle"))
				row.setCaseid(rs.getLong("caseid"))
				row.setCiudad(rs.getString("ciudad"))
				row.setCodigopostal(rs.getString("codigopostal"))
				row.setColonia(rs.getString("colonia"))
				row.setCorreoelectronico(rs.getString("correoelectronico"))
				row.setDelegacionmunicipio(rs.getString("delegacionmunicipio"))
				row.setDesconozcodatospadres(rs.getBoolean("desconozcodatospadres"))
				row.setEmpresatrabaja(rs.getString("empresatrabaja"))
				row.setEstadoextranjero(rs.getString("estadoextranjero"))
				row.setGiroempresa(rs.getString("giroempresa"))
				row.setIstutor(rs.getBoolean("istutor"))
				row.setNombre(rs.getString("nombre"))
				row.setNumeroexterior(rs.getString("numeroexterior"))
				row.setNumerointerior(rs.getString("numerointerior"))
				row.setOtroparentesco(rs.getString("otroparentesco"))
				row.setPuesto(rs.getString("puesto"))
				row.setTelefono(rs.getString("telefono"))
				row.setVivecontigo(rs.getBoolean("vivecontigo"))
				row.setCampusegreso(rs.getString("campusegreso"))
				row.setEgresoanahuac(rs.getString("egresoanahuac"))
				row.setEscolaridad(rs.getString("escolaridad"))
				row.setEstado(rs.getString("estado"))
				row.setPais(rs.getString("pais"))
				row.setParentesco(rs.getString("parentesco"))
				row.setTitulo(rs.getString("titulo"))
				row.setTrabaja(rs.getString("trabaja"))
				row.setVive(rs.getString("vive"))
				row.setVencido(rs.getBoolean("vencido"))
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
	
	public Result getPadreVencido(Integer parameterP,Integer parameterC,String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			PadresTutorEntity row = new PadresTutorEntity()
			List<PadresTutorEntity> rows = new ArrayList<PadresTutorEntity>();
			closeCon = validarConexion();
			assert object instanceof Map;
			String consulta = Statements.GET_PADRE_VENCIDO;
			errorlog+="consulta:"
			errorlog+=consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.caseid)
			rs = pstm.executeQuery()
			while(rs.next()) {
				row = new PadresTutorEntity()
				row.setPersistenceId(rs.getLong("persistenceId"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				row.setApellidos(rs.getString("apellidos"))
				row.setCalle(rs.getString("calle"))
				row.setCaseid(rs.getLong("caseid"))
				row.setCiudad(rs.getString("ciudad"))
				row.setCodigopostal(rs.getString("codigopostal"))
				row.setColonia(rs.getString("colonia"))
				row.setCorreoelectronico(rs.getString("correoelectronico"))
				row.setDelegacionmunicipio(rs.getString("delegacionmunicipio"))
				row.setDesconozcodatospadres(rs.getBoolean("desconozcodatospadres"))
				row.setEmpresatrabaja(rs.getString("empresatrabaja"))
				row.setEstadoextranjero(rs.getString("estadoextranjero"))
				row.setGiroempresa(rs.getString("giroempresa"))
				row.setIstutor(rs.getBoolean("istutor"))
				row.setNombre(rs.getString("nombre"))
				row.setNumeroexterior(rs.getString("numeroexterior"))
				row.setNumerointerior(rs.getString("numerointerior"))
				row.setOtroparentesco(rs.getString("otroparentesco"))
				row.setPuesto(rs.getString("puesto"))
				row.setTelefono(rs.getString("telefono"))
				row.setVivecontigo(rs.getBoolean("vivecontigo"))
				row.setCampusegreso(rs.getString("campusegreso"))
				row.setEgresoanahuac(rs.getString("egresoanahuac"))
				row.setEscolaridad(rs.getString("escolaridad"))
				row.setEstado(rs.getString("estado"))
				row.setPais(rs.getString("pais"))
				row.setParentesco(rs.getString("parentesco"))
				row.setTitulo(rs.getString("titulo"))
				row.setTrabaja(rs.getString("trabaja"))
				row.setVive(rs.getString("vive"))
				row.setVencido(rs.getBoolean("vencido"))
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
	
	public Result getMadreVencido(Integer parameterP,Integer parameterC,String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			PadresTutorEntity row = new PadresTutorEntity()
			List<PadresTutorEntity> rows = new ArrayList<PadresTutorEntity>();
			closeCon = validarConexion();
			assert object instanceof Map;
			String consulta = Statements.GET_MADRE_VENCIDO;
			errorlog+="consulta:"
			errorlog+=consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.caseid)
			rs = pstm.executeQuery()
			while(rs.next()) {
				row = new PadresTutorEntity()
				row.setPersistenceId(rs.getLong("persistenceId"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				row.setApellidos(rs.getString("apellidos"))
				row.setCalle(rs.getString("calle"))
				row.setCaseid(rs.getLong("caseid"))
				row.setCiudad(rs.getString("ciudad"))
				row.setCodigopostal(rs.getString("codigopostal"))
				row.setColonia(rs.getString("colonia"))
				row.setCorreoelectronico(rs.getString("correoelectronico"))
				row.setDelegacionmunicipio(rs.getString("delegacionmunicipio"))
				row.setDesconozcodatospadres(rs.getBoolean("desconozcodatospadres"))
				row.setEmpresatrabaja(rs.getString("empresatrabaja"))
				row.setEstadoextranjero(rs.getString("estadoextranjero"))
				row.setGiroempresa(rs.getString("giroempresa"))
				row.setIstutor(rs.getBoolean("istutor"))
				row.setNombre(rs.getString("nombre"))
				row.setNumeroexterior(rs.getString("numeroexterior"))
				row.setNumerointerior(rs.getString("numerointerior"))
				row.setOtroparentesco(rs.getString("otroparentesco"))
				row.setPuesto(rs.getString("puesto"))
				row.setTelefono(rs.getString("telefono"))
				row.setVivecontigo(rs.getBoolean("vivecontigo"))
				row.setCampusegreso(rs.getString("campusegreso"))
				row.setEgresoanahuac(rs.getString("egresoanahuac"))
				row.setEscolaridad(rs.getString("escolaridad"))
				row.setEstado(rs.getString("estado"))
				row.setPais(rs.getString("pais"))
				row.setParentesco(rs.getString("parentesco"))
				row.setTitulo(rs.getString("titulo"))
				row.setTrabaja(rs.getString("trabaja"))
				row.setVive(rs.getString("vive"))
				row.setVencido(rs.getBoolean("vencido"))
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
	
	public Result getContactoEmergencia(Integer parameterP,Integer parameterC,String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			ContactoEmergenciaEntity row = new ContactoEmergenciaEntity()
			List<ContactoEmergenciaEntity> rows = new ArrayList<ContactoEmergenciaEntity>();
			closeCon = validarConexion();
			assert object instanceof Map;
			String consulta = Statements.GET_CONTACTO_EMERGENCIA_VENCIDO;
			errorlog+="consulta:"
			errorlog+=consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.caseid)
			rs = pstm.executeQuery()
			while(rs.next()) {
				row = new ContactoEmergenciaEntity()
				row.setPersistenceId(rs.getLong("persistenceId"))
				row.setPersistenceVersion(rs.getLong("persistenceVersion"))
				row.setNombre(rs.getString("nombre"))
				row.setOtroparentesco(rs.getString("otroparentesco"))
				row.setTelefono(rs.getString("telefono"))
				row.setTelefonocelular(rs.getString("telefonocelular"))
				row.setParentesco(rs.getString("parentesco"))
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
	
	public Result getTempKeyAzure() {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			List<String> rows = new ArrayList<String>();
			closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA);
			rs= pstm.executeQuery();
			
			if(rs.next()) {
				SSA = rs.getString("valor")
			}
			
			rows.add(SSA);
			resultado.setSuccess(true);
			resultado.setData(rows);
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
}
