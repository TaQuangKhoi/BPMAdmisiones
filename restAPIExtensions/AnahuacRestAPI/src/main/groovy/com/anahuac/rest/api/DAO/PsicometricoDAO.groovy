package com.anahuac.rest.api.DAO

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper
import groovy.sql.Sql

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLType
import java.sql.Statement
import java.sql.Types

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PsicometricoDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PsicometricoDAO.class);
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
	
	private String generateUpdate(def testPsicomInput) {
		def catPersonaSaludable = null;
		def catVivesEstadoDiscapacidad = null;
		def catProblemaSaludAtencionContinua = null;
		def catRequieresAsistencia = null;
		
		String columnaUpdate = "";
		
		if(testPsicomInput.ajusteEfectivo != null && testPsicomInput.ajusteEfectivo != ""){
			columnaUpdate = columnaUpdate + "ajusteEfectivo = '"+testPsicomInput.ajusteEfectivo+"', ";
		}
		if(testPsicomInput.ajusteEscolarPrevio != null && testPsicomInput.ajusteEscolarPrevio != ""){
			columnaUpdate = columnaUpdate + "ajusteEscolarPrevio = '"+testPsicomInput.ajusteEscolarPrevio+"', ";
		}
		if(testPsicomInput.ajusteExistencial != null && testPsicomInput.ajusteExistencial != ""){
			columnaUpdate = columnaUpdate + "ajusteExistencial = '"+testPsicomInput.ajusteExistencial+"', ";
		}
		if(testPsicomInput.ajusteMedioFamiliar != null && testPsicomInput.ajusteMedioFamiliar != ""){
			columnaUpdate = columnaUpdate + "ajusteMedioFamiliar = '"+testPsicomInput.ajusteMedioFamiliar+"', ";
		}
		if(testPsicomInput.ajusteMedioSocial != null && testPsicomInput.ajusteMedioSocial != ""){
			columnaUpdate = columnaUpdate + "ajusteMedioSocial = '"+testPsicomInput.ajusteMedioSocial+"', ";
		}
		if(testPsicomInput.ajusteReligioso != null && testPsicomInput.ajusteReligioso != ""){
			columnaUpdate = columnaUpdate + "ajusteReligioso = '"+testPsicomInput.ajusteReligioso+"', ";
		}
		if(testPsicomInput.califAjusteAfectivo != null && testPsicomInput.califAjusteAfectivo != ""){
			columnaUpdate = columnaUpdate + "califAjusteAfectivo = '"+testPsicomInput.califAjusteAfectivo+"', ";
		}
		if(testPsicomInput.califAjusteEscolarPrevio != null && testPsicomInput.califAjusteEscolarPrevio != ""){
			columnaUpdate = columnaUpdate + "califAjusteEscolarPrevio = '"+testPsicomInput.califAjusteEscolarPrevio+"', ";
		}
		if(testPsicomInput.califAjusteExistencial != null && testPsicomInput.califAjusteExistencial != ""){
			columnaUpdate = columnaUpdate + "califAjusteExistencial = '"+testPsicomInput.califAjusteExistencial+"', ";
		}
		if(testPsicomInput.califAjusteMedioFamiliar != null && testPsicomInput.califAjusteMedioFamiliar != ""){
			columnaUpdate = columnaUpdate + "califAjusteMedioFamiliar = '"+testPsicomInput.califAjusteMedioFamiliar+"', ";
		}
		if(testPsicomInput.califAjusteMedioSocial != null && testPsicomInput.califAjusteMedioSocial != ""){
			columnaUpdate = columnaUpdate + "califAjusteMedioSocial = '"+testPsicomInput.califAjusteMedioSocial+"', ";
		}
		if(testPsicomInput.califAjusteReligioso != null && testPsicomInput.califAjusteReligioso != ""){
			columnaUpdate = columnaUpdate + "califAjusteReligioso = '"+testPsicomInput.califAjusteReligioso+"', ";
		}
		if(testPsicomInput.conclusioneINVP != null && testPsicomInput.conclusioneINVP != ""){
			columnaUpdate = columnaUpdate + "conclusioneINVP = '"+testPsicomInput.conclusioneINVP+"', ";
		}
		if(testPsicomInput.fechaEntrevista != null && testPsicomInput.fechaEntrevista != ""){
			columnaUpdate = columnaUpdate + "fechaEntrevista = '"+testPsicomInput.fechaEntrevista+"', ";
		}
		if(testPsicomInput.finalizado != null && testPsicomInput.finalizado != ""){
			columnaUpdate = columnaUpdate + "finalizado = '"+testPsicomInput.finalizado+"', ";
		}
		if(testPsicomInput.hasParticipadoActividadesAyuda != null && testPsicomInput.hasParticipadoActividadesAyuda != ""){
			columnaUpdate = columnaUpdate + "hasParticipadoActividadesAyuda = '"+testPsicomInput.hasParticipadoActividadesAyuda+"', ";
		}
		if(testPsicomInput.interpretacionINVP != null && testPsicomInput.interpretacionINVP != ""){
			columnaUpdate = columnaUpdate + "interpretacionINVP = '"+testPsicomInput.interpretacionINVP+"', ";
		}
		if(testPsicomInput.otroTipoAsistencia != null && testPsicomInput.otroTipoAsistencia != ""){
			columnaUpdate = columnaUpdate + "otroTipoAsistencia = '"+testPsicomInput.otroTipoAsistencia+"', ";
		}
		if(testPsicomInput.participacionActividadesVoluntaria != null && testPsicomInput.participacionActividadesVoluntaria != ""){
			columnaUpdate = columnaUpdate + "participacionActividadesVoluntaria = '"+testPsicomInput.participacionActividadesVoluntaria+"', ";
		}
		if(testPsicomInput.puntuacionINVP != null && testPsicomInput.puntuacionINVP != ""){
			columnaUpdate = columnaUpdate + "puntuacionINVP = '"+testPsicomInput.puntuacionINVP+"', ";
		}
		if(testPsicomInput.quienIntegro != null && testPsicomInput.quienIntegro != ""){
			columnaUpdate = columnaUpdate + "quienIntegro = '"+testPsicomInput.quienIntegro+"', ";
		}
		if(testPsicomInput.quienRealizoEntrevista != null && testPsicomInput.quienRealizoEntrevista != ""){
			columnaUpdate = columnaUpdate + "quienRealizoEntrevista = '"+testPsicomInput.quienRealizoEntrevista+"', ";
		}
		if(testPsicomInput.resumenSalud != null && testPsicomInput.resumenSalud != ""){
			columnaUpdate = columnaUpdate + "resumenSalud = '"+testPsicomInput.resumenSalud+"', ";
		}
				
		//OBJ
		if (testPsicomInput.catPersonaSaludable != null && testPsicomInput.catPersonaSaludable != "") {
			catPersonaSaludable = testPsicomInput.catPersonaSaludable
			assert catPersonaSaludable instanceof Map;
			if (catPersonaSaludable.persistenceId != null && catPersonaSaludable.persistenceId != "") {
				columnaUpdate = columnaUpdate + "catpersonasaludable_pid = " + catPersonaSaludable.persistenceId + ", ";
			}
		}
		if (testPsicomInput.catVivesEstadoDiscapacidad != null && testPsicomInput.catVivesEstadoDiscapacidad != "") {
			catVivesEstadoDiscapacidad = testPsicomInput.catVivesEstadoDiscapacidad
			assert catVivesEstadoDiscapacidad instanceof Map;
			if (catVivesEstadoDiscapacidad.persistenceId != null && catVivesEstadoDiscapacidad.persistenceId != "") {
				columnaUpdate = columnaUpdate + "catvivesestadodiscapacidad_pid = " + catVivesEstadoDiscapacidad.persistenceId + ", ";
			}
		}
		if (testPsicomInput.catProblemaSaludAtencionContinua != null && testPsicomInput.catProblemaSaludAtencionContinua != "") {
			catProblemaSaludAtencionContinua = testPsicomInput.catProblemaSaludAtencionContinua
			assert catProblemaSaludAtencionContinua instanceof Map;
			if (catProblemaSaludAtencionContinua.persistenceId != null && catProblemaSaludAtencionContinua.persistenceId != "") {
				columnaUpdate = columnaUpdate + "catproblemasaludatencionco_pid = " + catProblemaSaludAtencionContinua.persistenceId + ", ";
			}
		}
		if (testPsicomInput.catRequieresAsistencia != null && testPsicomInput.catRequieresAsistencia != "") {
			catRequieresAsistencia = testPsicomInput.catRequieresAsistencia
			assert catRequieresAsistencia instanceof Map;
			if (catRequieresAsistencia.persistenceId != null && catRequieresAsistencia.persistenceId != "") {
				columnaUpdate = columnaUpdate + "catrequieresasistencia_pid = " + catRequieresAsistencia.persistenceId + ", ";
			}
		}
		columnaUpdate = columnaUpdate.substring(0, columnaUpdate.length()-2);
		return Statements.UPDATE_TESTPSICOMETRICO.replace("[COLUMNA]", columnaUpdate);
	}
	
	public Result insertUpdatePsicometrico(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
				
		String strError = "";
		String columnaUpdate = "";
		String columnaInsert = "";
		String valorInsert = "";
		String consultaInsert = "";
		String consultaUpdate = "";
		String caseId = "";
			
		def catPersonaSaludable = null;
		def catVivesEstadoDiscapacidad = null;
		def catProblemaSaludAtencionContinua = null;
		def catRequieresAsistencia = null;
			
		Boolean closeCon = false;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			assert object instanceof Map;
			
			def testPsicomInput = object.testPsicomInput;
			assert testPsicomInput instanceof Map;
			
			if(object.caseId != null && object.caseId != ""){
				caseId=object.caseId;
			}
						
			consultaInsert = Statements.INSERT_TESTPSICOMETRICO;
			consultaUpdate = generateUpdate(testPsicomInput);
			
			strError = strError + " | " + consultaInsert;
			strError = strError + " | " + consultaUpdate;
			strError = strError + " | caseId: " + caseId
			
			//
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.SELECT_TESTPSICOMETRICO_BY_CASEID);
			pstm.setString(1, caseId);
			rs = pstm.executeQuery();
			if(rs.next()) {
				pstm = con.prepareStatement(consultaUpdate);
				pstm.setString(1, caseId);
				pstm.executeUpdate();
			}
			else {
				
				if (testPsicomInput.catPersonaSaludable != null && testPsicomInput.catPersonaSaludable != "") {
					catPersonaSaludable = testPsicomInput.catPersonaSaludable
					assert catPersonaSaludable instanceof Map;
			
				}
				if (testPsicomInput.catVivesEstadoDiscapacidad != null && testPsicomInput.catVivesEstadoDiscapacidad != "") {
					catVivesEstadoDiscapacidad = testPsicomInput.catVivesEstadoDiscapacidad
					assert catVivesEstadoDiscapacidad instanceof Map;
					
				}
				if (testPsicomInput.catProblemaSaludAtencionContinua != null && testPsicomInput.catProblemaSaludAtencionContinua != "") {
					catProblemaSaludAtencionContinua = testPsicomInput.catProblemaSaludAtencionContinua
					assert catProblemaSaludAtencionContinua instanceof Map;
					
				}
				if (testPsicomInput.catRequieresAsistencia != null && testPsicomInput.catRequieresAsistencia != "") {
					catRequieresAsistencia = testPsicomInput.catRequieresAsistencia
					assert catRequieresAsistencia instanceof Map;
					
				}
				
				strError = strError + " | " +  ((testPsicomInput.ajusteEfectivo != null && testPsicomInput.ajusteEfectivo != "") ? testPsicomInput.ajusteEfectivo : "");
				strError = strError + " | " +  ((testPsicomInput.ajusteEscolarPrevio != null && testPsicomInput.ajusteEscolarPrevio != "") ? testPsicomInput.ajusteEscolarPrevio : "");
				strError = strError + " | " +  ((testPsicomInput.ajusteExistencial != null && testPsicomInput.ajusteExistencial != "") ? testPsicomInput.ajusteExistencial : "");
				strError = strError + " | " +  ((testPsicomInput.ajusteMedioFamiliar != null && testPsicomInput.ajusteMedioFamiliar != "") ? testPsicomInput.ajusteMedioFamiliar : "");
				strError = strError + " | " +  ((testPsicomInput.ajusteMedioSocial != null && testPsicomInput.ajusteMedioSocial != "") ? testPsicomInput.ajusteMedioSocial : "");
				strError = strError + " | " +  ((testPsicomInput.ajusteReligioso != null && testPsicomInput.ajusteReligioso != "") ? testPsicomInput.ajusteReligioso : "");
				strError = strError + " | " +  ((testPsicomInput.califAjusteAfectivo != null && testPsicomInput.califAjusteAfectivo != "") ? testPsicomInput.califAjusteAfectivo : "");
				strError = strError + " | " +  ((testPsicomInput.califAjusteEscolarPrevio != null && testPsicomInput.califAjusteEscolarPrevio != "") ? testPsicomInput.califAjusteEscolarPrevio : "");
				strError = strError + " | " +  ((testPsicomInput.califAjusteExistencial != null && testPsicomInput.califAjusteExistencial != "") ? testPsicomInput.califAjusteExistencial : "");
				strError = strError + " | " +  ((testPsicomInput.califAjusteMedioFamiliar != null && testPsicomInput.califAjusteMedioFamiliar != "") ? testPsicomInput.califAjusteMedioFamiliar : "");
				strError = strError + " | " +  ((testPsicomInput.califAjusteMedioSocial != null && testPsicomInput.califAjusteMedioSocial != "") ? testPsicomInput.califAjusteMedioSocial : "");
				strError = strError + " | " +  ((testPsicomInput.califAjusteReligioso != null && testPsicomInput.califAjusteReligioso != "") ? testPsicomInput.califAjusteReligioso : "");
				strError = strError + " | " +  (caseId);
				strError = strError + " | " +  ((testPsicomInput.conclusioneINVP != null && testPsicomInput.conclusioneINVP != "") ? testPsicomInput.conclusioneINVP : "");
				strError = strError + " | " +  ((testPsicomInput.fechaEntrevista != null && testPsicomInput.fechaEntrevista != "") ? testPsicomInput.fechaEntrevista : "");
				strError = strError + " | " +  ((testPsicomInput.finalizado != null && testPsicomInput.finalizado != "") ? testPsicomInput.finalizado : false);
				strError = strError + " | " +  ((testPsicomInput.hasParticipadoActividadesAyuda != null && testPsicomInput.hasParticipadoActividadesAyuda != "") ? testPsicomInput.hasParticipadoActividadesAyuda : "");
				strError = strError + " | " +  ((testPsicomInput.interpretacionINVP != null && testPsicomInput.interpretacionINVP != "") ? testPsicomInput.interpretacionINVP : "");
				strError = strError + " | " +  ((testPsicomInput.otroTipoAsistencia != null && testPsicomInput.otroTipoAsistencia != "") ? testPsicomInput.otroTipoAsistencia : "");
				strError = strError + " | " +  ((testPsicomInput.participacionActividadesVoluntaria != null && testPsicomInput.participacionActividadesVoluntaria != "") ? testPsicomInput.participacionActividadesVoluntaria : "");
				strError = strError + " | " +  (0);
				strError = strError + " | " +  ((testPsicomInput.puntuacionINVP != null && testPsicomInput.puntuacionINVP != "") ? testPsicomInput.puntuacionINVP : 0);
				strError = strError + " | " +  ((testPsicomInput.quienIntegro != null && testPsicomInput.quienIntegro != "") ? testPsicomInput.quienIntegro : "");
				strError = strError + " | " +  ((testPsicomInput.quienRealizoEntrevista != null && testPsicomInput.quienRealizoEntrevista != "") ? testPsicomInput.quienRealizoEntrevista : "");
				strError = strError + " | " +  ((testPsicomInput.resumenSalud != null && testPsicomInput.resumenSalud != "") ? testPsicomInput.resumenSalud : "");
				strError = strError + " | " +  ((catPersonaSaludable.persistenceId != null && catPersonaSaludable.persistenceId != "") ? catPersonaSaludable.persistenceId : 0);
				strError = strError + " | " +  ((catProblemaSaludAtencionContinua.persistenceId != null && catProblemaSaludAtencionContinua.persistenceId != "") ? catProblemaSaludAtencionContinua.persistenceId : 0);
				strError = strError + " | " +  ((catRequieresAsistencia.persistenceId != null && catRequieresAsistencia.persistenceId != "") ? catRequieresAsistencia.persistenceId : 0 );
				strError = strError + " | " +  ((catVivesEstadoDiscapacidad.persistenceId != null && catVivesEstadoDiscapacidad.persistenceId != "") ? catVivesEstadoDiscapacidad.persistenceId : 0);
				
				pstm = con.prepareStatement(consultaInsert);
				pstm.setString(1, (testPsicomInput.ajusteEfectivo != null && testPsicomInput.ajusteEfectivo != "") ? testPsicomInput.ajusteEfectivo : "");
				pstm.setString(2, (testPsicomInput.ajusteEscolarPrevio != null && testPsicomInput.ajusteEscolarPrevio != "") ? testPsicomInput.ajusteEscolarPrevio : "");
				pstm.setString(3, (testPsicomInput.ajusteExistencial != null && testPsicomInput.ajusteExistencial != "") ? testPsicomInput.ajusteExistencial : "");
				pstm.setString(4, (testPsicomInput.ajusteMedioFamiliar != null && testPsicomInput.ajusteMedioFamiliar != "") ? testPsicomInput.ajusteMedioFamiliar : "");
				pstm.setString(5, (testPsicomInput.ajusteMedioSocial != null && testPsicomInput.ajusteMedioSocial != "") ? testPsicomInput.ajusteMedioSocial : "");
				pstm.setString(6, (testPsicomInput.ajusteReligioso != null && testPsicomInput.ajusteReligioso != "") ? testPsicomInput.ajusteReligioso : "");
				pstm.setString(7, (testPsicomInput.califAjusteAfectivo != null && testPsicomInput.califAjusteAfectivo != "") ? testPsicomInput.califAjusteAfectivo : "");
				pstm.setString(8, (testPsicomInput.califAjusteEscolarPrevio != null && testPsicomInput.califAjusteEscolarPrevio != "") ? testPsicomInput.califAjusteEscolarPrevio : "");
				pstm.setString(9, (testPsicomInput.califAjusteExistencial != null && testPsicomInput.califAjusteExistencial != "") ? testPsicomInput.califAjusteExistencial : "");
				pstm.setString(10, (testPsicomInput.califAjusteMedioFamiliar != null && testPsicomInput.califAjusteMedioFamiliar != "") ? testPsicomInput.califAjusteMedioFamiliar : "");
				pstm.setString(11, (testPsicomInput.califAjusteMedioSocial != null && testPsicomInput.califAjusteMedioSocial != "") ? testPsicomInput.califAjusteMedioSocial : "");
				pstm.setString(12, (testPsicomInput.califAjusteReligioso != null && testPsicomInput.califAjusteReligioso != "") ? testPsicomInput.califAjusteReligioso : "");
				pstm.setString(13, caseId);
				pstm.setString(14, (testPsicomInput.conclusioneINVP != null && testPsicomInput.conclusioneINVP != "") ? testPsicomInput.conclusioneINVP : "");
				pstm.setString(15, (testPsicomInput.fechaEntrevista != null && testPsicomInput.fechaEntrevista != "") ? testPsicomInput.fechaEntrevista : "");
				pstm.setBoolean(16, (testPsicomInput.finalizado != null && testPsicomInput.finalizado != "") ? testPsicomInput.finalizado : false);
				pstm.setString(17, (testPsicomInput.hasParticipadoActividadesAyuda != null && testPsicomInput.hasParticipadoActividadesAyuda != "") ? testPsicomInput.hasParticipadoActividadesAyuda : "");
				pstm.setString(18, (testPsicomInput.interpretacionINVP != null && testPsicomInput.interpretacionINVP != "") ? testPsicomInput.interpretacionINVP : "");
				pstm.setString(19, (testPsicomInput.otroTipoAsistencia != null && testPsicomInput.otroTipoAsistencia != "") ? testPsicomInput.otroTipoAsistencia : "");
				pstm.setString(20, (testPsicomInput.participacionActividadesVoluntaria != null && testPsicomInput.participacionActividadesVoluntaria != "") ? testPsicomInput.participacionActividadesVoluntaria : "");
				pstm.setInt(21, 0);
				pstm.setInt(22, (testPsicomInput.puntuacionINVP != null && testPsicomInput.puntuacionINVP != "") ? testPsicomInput.puntuacionINVP : 0);
				pstm.setString(23, (testPsicomInput.quienIntegro != null && testPsicomInput.quienIntegro != "") ? testPsicomInput.quienIntegro : "");
				pstm.setString(24, (testPsicomInput.quienRealizoEntrevista != null && testPsicomInput.quienRealizoEntrevista != "") ? testPsicomInput.quienRealizoEntrevista : "");
				pstm.setString(25, (testPsicomInput.resumenSalud != null && testPsicomInput.resumenSalud != "") ? testPsicomInput.resumenSalud : "");
				
				if(catPersonaSaludable.persistenceId != null && catPersonaSaludable.persistenceId != ""){
					pstm.setInt(26, catPersonaSaludable.persistenceId);	
				}
				else {
					pstm.setNull(26, Types.INTEGER);	
				}
				if(catProblemaSaludAtencionContinua.persistenceId != null && catProblemaSaludAtencionContinua.persistenceId != ""){
					pstm.setInt(27, catProblemaSaludAtencionContinua.persistenceId);	
				}
				else {
					pstm.setNull(27, Types.INTEGER);	
				}
				if(catRequieresAsistencia.persistenceId != null && catRequieresAsistencia.persistenceId != ""){
					pstm.setInt(28, catRequieresAsistencia.persistenceId);	
				}
				else {
					pstm.setNull(28, Types.INTEGER);	
				}
				if(catVivesEstadoDiscapacidad.persistenceId != null && catVivesEstadoDiscapacidad.persistenceId != ""){
					pstm.setInt(29, catVivesEstadoDiscapacidad.persistenceId);	
				}
				else {
					pstm.setNull(29, Types.INTEGER);	
				}
				
				
				pstm.executeUpdate();
			}
			
			
			
			resultado.setError_info(strError);
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
}
