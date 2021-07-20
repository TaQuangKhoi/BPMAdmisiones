package com.anahuac.rest.api.DAO

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

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
		
		if(testPsicomInput.fechaEntrevista != null && testPsicomInput.fechaEntrevista != ""){
			columnaUpdate = columnaUpdate + "fechaEntrevista = '"+testPsicomInput.fechaEntrevista+"', ";
		}
		if(testPsicomInput.ajusteMedioFamiliar != null && testPsicomInput.ajusteMedioFamiliar != ""){
			columnaUpdate = columnaUpdate + "ajusteMedioFamiliar = '"+testPsicomInput.ajusteMedioFamiliar+"', ";
		}
		if(testPsicomInput.califAjusteMedioFamiliar != null && testPsicomInput.califAjusteMedioFamiliar != ""){
			columnaUpdate = columnaUpdate + "califAjusteMedioFamiliar = '"+testPsicomInput.califAjusteMedioFamiliar+"', ";
		}
		if(testPsicomInput.ajusteEscolarPrevio != null && testPsicomInput.ajusteEscolarPrevio != ""){
			columnaUpdate = columnaUpdate + "ajusteEscolarPrevio = '"+testPsicomInput.ajusteEscolarPrevio+"', ";
		}
		if(testPsicomInput.califAjusteEscolarPrevio != null && testPsicomInput.califAjusteEscolarPrevio != ""){
			columnaUpdate = columnaUpdate + "califAjusteEscolarPrevio = '"+testPsicomInput.califAjusteEscolarPrevio+"', ";
		}
		if(testPsicomInput.ajusteMedioSocial != null && testPsicomInput.ajusteMedioSocial != ""){
			columnaUpdate = columnaUpdate + "ajusteMedioSocial = '"+testPsicomInput.ajusteMedioSocial+"', ";
		}
		if(testPsicomInput.califAjusteMedioSocial != null && testPsicomInput.califAjusteMedioSocial != ""){
			columnaUpdate = columnaUpdate + "califAjusteMedioSocial = '"+testPsicomInput.califAjusteMedioSocial+"', ";
		}
		if(testPsicomInput.ajusteEfectivo != null && testPsicomInput.ajusteEfectivo != ""){
			columnaUpdate = columnaUpdate + "ajusteEfectivo = '"+testPsicomInput.ajusteEfectivo+"', ";
		}
		if(testPsicomInput.califAjusteAfectivo != null && testPsicomInput.califAjusteAfectivo != ""){
			columnaUpdate = columnaUpdate + "califAjusteAfectivo = '"+testPsicomInput.califAjusteAfectivo+"', ";
		}
		if(testPsicomInput.ajusteReligioso != null && testPsicomInput.ajusteReligioso != ""){
			columnaUpdate = columnaUpdate + "ajusteReligioso = '"+testPsicomInput.ajusteReligioso+"', ";
		}
		if(testPsicomInput.califAjusteReligioso != null && testPsicomInput.califAjusteReligioso != ""){
			columnaUpdate = columnaUpdate + "califAjusteReligioso = '"+testPsicomInput.califAjusteReligioso+"', ";
		}
		if(testPsicomInput.ajusteExistencial != null && testPsicomInput.ajusteExistencial != ""){
			columnaUpdate = columnaUpdate + "ajusteExistencial = '"+testPsicomInput.ajusteExistencial+"', ";
		}
		if(testPsicomInput.califAjusteExistencial != null && testPsicomInput.califAjusteExistencial != ""){
			columnaUpdate = columnaUpdate + "califAjusteExistencial = '"+testPsicomInput.califAjusteExistencial+"', ";
		}
		if(testPsicomInput.interpretacionINVP != null && testPsicomInput.interpretacionINVP != ""){
			columnaUpdate = columnaUpdate + "interpretacionINVP = '"+testPsicomInput.interpretacionINVP+"', ";
		}
		if(testPsicomInput.conclusioneINVP != null && testPsicomInput.conclusioneINVP != ""){
			columnaUpdate = columnaUpdate + "conclusioneINVP = '"+testPsicomInput.conclusioneINVP+"', ";
		}
		if(testPsicomInput.recomendadoPDU != null && testPsicomInput.recomendadoPDU != ""){
			columnaUpdate = columnaUpdate + "recomendadoPDU = '"+testPsicomInput.recomendadoPDU+"', ";
		}
		if(testPsicomInput.recomendadoSSE != null && testPsicomInput.recomendadoSSE != ""){
			columnaUpdate = columnaUpdate + "recomendadoSSE = '"+testPsicomInput.recomendadoSSE+"', ";
		}
		if(testPsicomInput.recomendadoPDP != null && testPsicomInput.recomendadoPDP != ""){
			columnaUpdate = columnaUpdate + "recomendadoPDP = '"+testPsicomInput.recomendadoPDP+"', ";
		}
		if(testPsicomInput.recomendadoPIA != null && testPsicomInput.recomendadoPIA != ""){
			columnaUpdate = columnaUpdate + "recomendadoPIA = '"+testPsicomInput.recomendadoPIA+"', ";
		}
		if(testPsicomInput.recomendadoPCDA != null && testPsicomInput.recomendadoPCDA != ""){
			columnaUpdate = columnaUpdate + "recomendadoPCDA = '"+testPsicomInput.recomendadoPCDA+"', ";
		}
		if(testPsicomInput.recomendadoPCA != null && testPsicomInput.recomendadoPCA != ""){
			columnaUpdate = columnaUpdate + "recomendadoPCA = '"+testPsicomInput.recomendadoPCA+"', ";
		}
		if(testPsicomInput.puntuacionINVP != null && testPsicomInput.puntuacionINVP != ""){
			columnaUpdate = columnaUpdate + "puntuacionINVP = '"+testPsicomInput.puntuacionINVP+"', ";
		}
		if(testPsicomInput.resumenSalud != null && testPsicomInput.resumenSalud != ""){
			columnaUpdate = columnaUpdate + "resumenSalud = '"+testPsicomInput.resumenSalud+"', ";
		}
		if(testPsicomInput.finalizado != null && testPsicomInput.finalizado != ""){
			columnaUpdate = columnaUpdate + "finalizado = '"+testPsicomInput.finalizado+"', ";
		}
		if(testPsicomInput.otroTipoAsistencia != null && testPsicomInput.otroTipoAsistencia != ""){
			columnaUpdate = columnaUpdate + "otroTipoAsistencia = '"+testPsicomInput.otroTipoAsistencia+"', ";
		}
		if(testPsicomInput.quienRealizoEntrevista != null && testPsicomInput.quienRealizoEntrevista != ""){
			columnaUpdate = columnaUpdate + "quienRealizoEntrevista = '"+testPsicomInput.quienRealizoEntrevista+"', ";
		}
		if(testPsicomInput.quienIntegro != null && testPsicomInput.quienIntegro != ""){
			columnaUpdate = columnaUpdate + "quienIntegro = '"+testPsicomInput.quienIntegro+"', ";
		}
		if(testPsicomInput.hasParticipadoActividadesAyuda != null && testPsicomInput.hasParticipadoActividadesAyuda != ""){
			columnaUpdate = columnaUpdate + "hasParticipadoActividadesAyuda = '"+testPsicomInput.hasParticipadoActividadesAyuda+"', ";
		}
		if(testPsicomInput.participacionActividadesVoluntaria != null && testPsicomInput.participacionActividadesVoluntaria != ""){
			columnaUpdate = columnaUpdate + "participacionActividadesVoluntaria = '"+testPsicomInput.participacionActividadesVoluntaria+"', ";
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
				
				pstm = con.prepareStatement(consultaInsert);
				pstm.setString(1, (testPsicomInput.ajusteefectivo != null && testPsicomInput.ajusteefectivo != "") ? testPsicomInput.ajusteefectivo  : "");
				pstm.setString(2, (testPsicomInput.ajusteescolarprevio != null && testPsicomInput.ajusteescolarprevio != "") ? testPsicomInput.ajusteescolarprevio  : "");
				pstm.setString(3, (testPsicomInput.ajusteexistencial != null && testPsicomInput.ajusteexistencial != "") ? testPsicomInput.ajusteexistencial  : "");
				pstm.setString(4, (testPsicomInput.ajustemediofamiliar != null && testPsicomInput.ajustemediofamiliar != "") ? testPsicomInput.ajustemediofamiliar  : "");
				pstm.setString(5, (testPsicomInput.ajustemediosocial != null && testPsicomInput.ajustemediosocial != "") ? testPsicomInput.ajustemediosocial  : "");
				pstm.setString(6, (testPsicomInput.ajustereligioso != null && testPsicomInput.ajustereligioso != "") ? testPsicomInput.ajustereligioso  : "");
				pstm.setString(7, (testPsicomInput.califajusteafectivo != null && testPsicomInput.califajusteafectivo != "") ? testPsicomInput.califajusteafectivo  : "");
				pstm.setString(8, (testPsicomInput.califajusteescolarprevio != null && testPsicomInput.califajusteescolarprevio != "") ? testPsicomInput.califajusteescolarprevio  : "");
				pstm.setString(9, (testPsicomInput.califajusteexistencial != null && testPsicomInput.califajusteexistencial != "") ? testPsicomInput.califajusteexistencial  : "");
				pstm.setString(10, (testPsicomInput.califajustemediofamiliar != null && testPsicomInput.califajustemediofamiliar != "") ? testPsicomInput.califajustemediofamiliar  : "");
				pstm.setString(11, (testPsicomInput.califajustemediosocial != null && testPsicomInput.califajustemediosocial != "") ? testPsicomInput.califajustemediosocial  : "");
				pstm.setString(12, (testPsicomInput.califajustereligioso != null && testPsicomInput.califajustereligioso != "") ? testPsicomInput.califajustereligioso  : "");
				pstm.setString(13, (testPsicomInput.conclusioneinvp != null && testPsicomInput.conclusioneinvp != "") ? testPsicomInput.conclusioneinvp  : "");
				pstm.setString(14, (testPsicomInput.interpretacioninvp != null && testPsicomInput.interpretacioninvp != "") ? testPsicomInput.interpretacioninvp  : "");
				pstm.setInt(15, (testPsicomInput.persistenceversion != null && testPsicomInput.persistenceversion != "") ? testPsicomInput.persistenceversion  : 0);
				pstm.setInt(16, (testPsicomInput.puntuacioninvp != null && testPsicomInput.puntuacioninvp != "") ? testPsicomInput.puntuacioninvp : 0);
				pstm.setString(17, (testPsicomInput.recomendadopca != null && testPsicomInput.recomendadopca != "") ? testPsicomInput.recomendadopca  : "");
				pstm.setString(18, (testPsicomInput.recomendadopcda != null && testPsicomInput.recomendadopcda != "") ? testPsicomInput.recomendadopcda  : "");
				pstm.setString(19, (testPsicomInput.recomendadopdp != null && testPsicomInput.recomendadopdp != "") ? testPsicomInput.recomendadopdp  : "");
				pstm.setString(20, (testPsicomInput.recomendadopdu != null && testPsicomInput.recomendadopdu != "") ? testPsicomInput.recomendadopdu  : "");
				pstm.setString(21, (testPsicomInput.recomendadopia != null && testPsicomInput.recomendadopia != "") ? testPsicomInput.recomendadopia  : "");
				pstm.setString(22, (testPsicomInput.recomendadosse != null && testPsicomInput.recomendadosse != "") ? testPsicomInput.recomendadosse  : "");
				pstm.setString(23, (testPsicomInput.fechaentrevista != null && testPsicomInput.fechaentrevista != "") ? testPsicomInput.fechaentrevista  : "");
				pstm.setBoolean(24, (testPsicomInput.finalizado != null && testPsicomInput.finalizado != "") ? testPsicomInput.finalizado  : false);
				pstm.setString(25, (testPsicomInput.resumensalud != null && testPsicomInput.resumensalud != "") ? testPsicomInput.resumensalud  : "");
				pstm.setString(26, (testPsicomInput.otrotipoasistencia != null && testPsicomInput.otrotipoasistencia != "") ? testPsicomInput.otrotipoasistencia : "");
				pstm.setInt(27, (catPersonaSaludable.persistenceId != null && catPersonaSaludable.persistenceId != "") ? catPersonaSaludable.persistenceId : 0);
				pstm.setInt(28, (catProblemaSaludAtencionContinua.persistenceId != null && catProblemaSaludAtencionContinua.persistenceId != "") ? catProblemaSaludAtencionContinua.persistenceId : 0);
				pstm.setInt(29, (catRequieresAsistencia.persistenceId != null && catRequieresAsistencia.persistenceId != "") ? catRequieresAsistencia.persistenceId : 0 );
				pstm.setInt(30, (catVivesEstadoDiscapacidad.persistenceId != null && catVivesEstadoDiscapacidad.persistenceId != "") ? catVivesEstadoDiscapacidad.persistenceId : 0);
				pstm.setString(31, (testPsicomInput.hasparticipadoactividadesayuda != null && testPsicomInput.hasparticipadoactividadesayuda != "") ? testPsicomInput.hasparticipadoactividadesayuda : "");
				pstm.setString(32, (testPsicomInput.participacionactividadesvoluntaria != null && testPsicomInput.participacionactividadesvoluntaria != "") ? testPsicomInput.participacionactividadesvoluntaria : "");
				pstm.setString(33, (testPsicomInput.quienintegro != null && testPsicomInput.quienintegro != "") ? testPsicomInput.quienintegro : "");
				pstm.setString(34, (testPsicomInput.quienrealizoentrevista != null && testPsicomInput.quienrealizoentrevista != "") ? testPsicomInput.quienrealizoentrevista : "");
				pstm.setString(35, (testPsicomInput.caseid != null && testPsicomInput.caseid != "") ? testPsicomInput.caseid : "");
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
