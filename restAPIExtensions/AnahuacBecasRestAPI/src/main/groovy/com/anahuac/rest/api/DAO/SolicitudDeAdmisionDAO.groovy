package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.SimpleDateFormat

import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatCampus
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.DB.StatementsCatalogos
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.db.CatGenerico
import com.anahuac.rest.api.Entity.db.CatImagenesSocioAcademico
import com.anahuac.rest.api.Entity.db.CatManejoDocumentos
import com.anahuac.rest.api.Entity.db.CatTypoApoyo
import com.anahuac.rest.api.Entity.db.InformacionEscolar
import com.anahuac.rest.api.Entity.db.PadresTutor
import com.anahuac.rest.api.Entity.db.SolicitudDeAdmision

import groovy.json.JsonSlurper

class SolicitudDeAdmisionDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(SolicitudDeAdmisionDAO.class)
	Connection con
	Statement stm
	ResultSet rs
	PreparedStatement pstm
	
	public Boolean validarConexion() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection()
			retorno = true
		}
		return retorno
	}
	
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita()
			retorno=true
		}
		return retorno
	}
	
	/**
	 * Obtiene  el objeto solicitudDeAdmision relacionado a un email
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result getSolicitudDeAdmision(String email) {
		Result resultado = new Result()
		Boolean closeCon = false
		String errorLog = ""
		
		try {
			
			String consulta = Statements.GET_SOLICITUD_DE_ADMISION_BY_CORREO
			SolicitudDeAdmision row = new SolicitudDeAdmision()
			List < SolicitudDeAdmision > rows = new ArrayList < SolicitudDeAdmision > ()
			
			closeCon = validarConexion()
			pstm = con.prepareStatement(consulta)
			
			pstm.setString(1, email)
			
			rs = pstm.executeQuery()
			
			while (rs.next()) {
				row = new SolicitudDeAdmision()
				row.setPrimerNombre(rs.getString("primernombre"))
				row.setSegundoNombre(rs.getString("segundonombre"))
				row.setPrimerApellido(rs.getString("apellidopaterno"))
				row.setSegundoApellido(rs.getString("apellidomaterno"))
				row.setCorreoElectronico(rs.getString("correoelectronico"))
				row.setSexo(rs.getString("sexo"))
				row.setTipoIngreso(rs.getString("tipoadmision"))
				row.setLicenciatura(rs.getString("licenciatura"))
				row.setSemestreIngreso(rs.getString("semestre"))
				row.setIdAlumno(rs.getString("idalumno"))
				row.setFechaNacimiento(rs.getString("fechanacimiento"))
				row.setTelefonoCelular(rs.getString("telefono"))
				row.setNacionalidad(rs.getString("nacionalidad"))
				row.setTrabajas(rs.getString("trabajas"))
				row.setEstadoCivil(rs.getString("estadocivil"));
				row.setCaseid(rs.getString("caseid"));
				rows.add(row)
			}
			
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setError_info(errorLog)
		} catch (Exception e) {
			resultado.setError_info(errorLog)
			LOGGER.error "[ERROR] " + e.getMessage()
			resultado.setSuccess(false)
			resultado.setError(e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado
	}
	
	/**
	 * Obtiene  el objeto compuesto llamado InformacionEscolar relacionado a un caseid
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result getInfrmacionEscolar(String email) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		
		try {
			
			String consulta = Statements.GET_INFORMACION_ESCOLAR_BY_CORREO;
			InformacionEscolar row = new InformacionEscolar();
			List < InformacionEscolar > rows = new ArrayList < InformacionEscolar > ();
			
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			
			pstm.setString(1, email);
			
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new InformacionEscolar();
				
				row.setPreparatoria(rs.getString("descripcion"));
				row.setPromedio(rs.getString("promedioPreparatoria"));
				row.setClave(rs.getString("id"));
				row.setCiudad(rs.getString("ciudad"));
				row.setEstado(rs.getString("estado"));
				row.setPais(rs.getString("pais"));
				row.setCostoMensual(rs.getString("costoMensualPReparatoria"));
				row.setCertificado(rs.getString("urlconstancia"));
				
				rows.add(row);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			resultado.setError_info(errorLog);
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
	
	/**
	 * Obtiene  el objeto compuesto llamado InformacionEscolar relacionado a un caseid
	 * @author José Carlos García Romero
	 * @param jsonData (String)
	 * @param context (RestAPIContext)
	 * @return resultado (Result)
	 */
	public Result getPadresTutorByCaseid(String caseid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		
		try {
			
			String consulta = Statements.GET_PADRES_TUTOT_BY_CASEID;
			PadresTutor row = new PadresTutor();
			List < PadresTutor > rows = new ArrayList < PadresTutor > ();
			
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			
			pstm.setLong(1, Long.valueOf(caseid));
			
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				row = new PadresTutor();
				
				row.setNombre(rs.getString("nombre"));
				row.setApellidos(rs.getString("apellidos"));
				row.setVive(rs.getString("vive"));
				row.setSexo(rs.getString("sexo"));
				row.setParentesco(rs.getString("parentesco"));
				row.setMismoDomicilio(rs.getBoolean("vivecontigo"));
				row.setTelefonoCasa(rs.getString("telefonoCasa"));
				row.setTelefonoCelular(rs.getString("telefonoCelular"));
				row.setCorreoElectronico(rs.getString("correoElectronico"));
				row.setMaximoNivelEstudios(rs.getString("maximoNivelEstudios"));
				row.setOcupacion(rs.getString(""));
				row.setEmpresa(rs.getString("empresatrabaja"));
				row.setPuesto(rs.getString("puesto"));
				row.setGiroEmpresa(rs.getString("giroempresa"));
				row.setIsTutor(rs.getBoolean("istutor"));
				
				rows.add(row);
			}
			
			resultado.setSuccess(true)
			resultado.setData(rows)
			resultado.setError_info(errorLog)
		} catch (Exception e) {
			resultado.setError_info(errorLog)
			LOGGER.error "[ERROR] " + e.getMessage()
			resultado.setSuccess(false)
			resultado.setError(e.getMessage())
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado
	}
}
