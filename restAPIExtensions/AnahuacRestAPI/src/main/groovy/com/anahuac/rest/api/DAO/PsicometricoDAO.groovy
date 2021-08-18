package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatTerapiaDAO
import com.anahuac.model.Autodescripcion
import com.anahuac.model.AutodescripcionDAO
import com.anahuac.model.AutodescripcionV2DAO
import com.anahuac.model.TestPsicometrico
import com.anahuac.model.TestPsicometricoDAO
import com.anahuac.model.TestPsicometricoRasgos
import com.anahuac.model.TestPsicometricoRasgosDAO
import com.anahuac.model.TestPsicometricoRelativosDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper
import groovy.sql.Sql

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
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

		def catParentezco = null;
		def vive = null;
		def carrera = null;
		
		def rasgo = null;
		def calificacion = null;
		
		def catTipoTerapia = null;
		def catRecibidoTerapia = null;
		
        Boolean closeCon = false;
        
        Integer testPsicomInput_persistenceId = 0;
        Integer contador = 0;
        
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
            
            closeCon = validarConexion();
            
            
            /*========================================================TEST PSICOMETRICO ACCIONES========================================================*/
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
                if(testPsicomInput.fechaEntrevista != null && testPsicomInput.fechaEntrevista != "") {
                    pstm.setString(15, testPsicomInput.fechaEntrevista);
                }
                else {
                    pstm.setNull(15, Types.TIMESTAMP);
                }
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
            
            //caseId;
            pstm = con.prepareStatement(Statements.SELECT_TESTPSICOMETRICO_BY_CASEID);
            pstm.setString(1, caseId);
            rs = pstm.executeQuery();
            if(rs.next()) {
                testPsicomInput_persistenceId = rs.getInt("persistenceid");
            }
            strError = strError + " | testPsicomInput_persistenceId: " + testPsicomInput_persistenceId;
            strError = strError + " | " + "-------------------------------------------";
            
            strError = strError + " | " + Statements.DELETE_TESTPSICOMETRICO_FIENTESINFLUYE;
            
            /*==============================================TESTPSICOMETRICO_FIENTESINFLUYE INICIO==============================================*/
            pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_FIENTESINFLUYE);
            pstm.setInt(1, testPsicomInput_persistenceId);
            pstm.executeUpdate();
            strError = strError + " | " + "-------------------------------------------";
            if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
                strError = strError + " | " + "-------------------------------------------";
                if (testPsicomInput.fientesInfluyeronDesicion != null && testPsicomInput.fientesInfluyeronDesicion != "") {
                    assert testPsicomInput.fientesInfluyeronDesicion instanceof List;
                    for (def row: testPsicomInput.fientesInfluyeronDesicion) {
                        if(row.persistenceId != null && row.persistenceId != ""){
                            pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_FIENTESINFLUYE);
                            pstm.setInt(1, testPsicomInput_persistenceId);
                            pstm.setInt(2, row.persistenceId);
                            pstm.setInt(3, contador);
                            pstm.executeUpdate();
                            contador++;
                            strError = strError + " | contador: " + contador;
                        }
                    }
                }
            }
            /*==============================================TESTPSICOMETRICO_FIENTESINFLUYE FIN==============================================*/
            /*==============================================TESTPSICOMETRICO_RAZONESINGRESO INICIO==============================================*/
            contador = 0;
            pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_RAZONESINGRESO);
            pstm.setInt(1, testPsicomInput_persistenceId);
            pstm.executeUpdate();
            strError = strError + " | " + "-------------------------------------------";
            if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
                strError = strError + " | " + "-------------------------------------------";
                if (testPsicomInput.razonesIngreso != null && testPsicomInput.razonesIngreso != "") {
                    assert testPsicomInput.razonesIngreso instanceof List;
                    for (def row: testPsicomInput.razonesIngreso) {
                        if(row.persistenceId != null && row.persistenceId != ""){
                            pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_RAZONESINGRESO);
                            pstm.setInt(1, testPsicomInput_persistenceId);
                            pstm.setInt(2, row.persistenceId);
                            pstm.setInt(3, contador);
                            pstm.executeUpdate();
                            contador++;
                            strError = strError + " | contador: " + contador;
                        }
                    }
                }
            }
            /*==============================================TESTPSICOMETRICO_RAZONESINGRESO FIN==============================================*/
            /*==============================================TESTPSICOMETRICO_DISCAPACIDADES INICIO==============================================*/
            contador = 0;
            pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_DISCAPACIDADES);
            pstm.setInt(1, testPsicomInput_persistenceId);
            pstm.executeUpdate();
            strError = strError + " | " + "-------------------------------------------";
            if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
                strError = strError + " | " + "-------------------------------------------";
                if (testPsicomInput.discapacidades != null && testPsicomInput.discapacidades != "") {
                    assert testPsicomInput.discapacidades instanceof List;
                    for (def row: testPsicomInput.discapacidades) {
                        if(row.persistenceId != null && row.persistenceId != ""){
                            pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_DISCAPACIDADES);
                            pstm.setInt(1, testPsicomInput_persistenceId);
                            pstm.setInt(2, row.persistenceId);
                            pstm.setInt(3, contador);
                            pstm.executeUpdate();
                            contador++;
                            strError = strError + " | contador: " + contador;
                        }
                    }
                }
            }
            /*==============================================TESTPSICOMETRICO_DISCAPACIDADES FIN==============================================*/
            /*==============================================TESTPSICOMETRICO_CUSTOSRECOMEND INICIO==============================================*/
            contador = 0;
            pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_CUSTOSRECOMEND);
            pstm.setInt(1, testPsicomInput_persistenceId);
            pstm.executeUpdate();
            strError = strError + " | " + "-------------------------------------------";
            if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
                strError = strError + " | " + "-------------------------------------------";
                if (testPsicomInput.custosRecomendados != null && testPsicomInput.custosRecomendados != "") {
                    assert testPsicomInput.custosRecomendados instanceof List;
                    for (def row: testPsicomInput.custosRecomendados) {
                        if(row.persistenceId != null && row.persistenceId != ""){
                            pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_CUSTOSRECOMEND);
                            pstm.setInt(1, testPsicomInput_persistenceId);
                            pstm.setInt(2, row.persistenceId);
                            pstm.setInt(3, contador);
                            pstm.executeUpdate();
                            contador++;
                            strError = strError + " | contador: " + contador;
                        }
                    }
                }
            }
            /*==============================================TESTPSICOMETRICO_CUSTOSRECOMEND FIN==============================================*/
            /*==============================================TESTPSICOMETRICO_PROBLEMASSALUD INICIO==============================================*/
            contador = 0;
            pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_PROBLEMASSALUD);
            pstm.setInt(1, testPsicomInput_persistenceId);
            pstm.executeUpdate();
            strError = strError + " | " + "-------------------------------------------";
			
//            if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
//                strError = strError + " | " + "-------------------------------------------";
//                if (testPsicomInput.problemasSalud != null && testPsicomInput.problemasSalud != "") {
//                    assert testPsicomInput.problemasSalud instanceof List;
//                    for (def row: testPsicomInput.problemasSalud) {
//                        if(row.persistenceId != null && row.persistenceId != ""){
//                            pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_PROBLEMASSALUD);
//                            pstm.setInt(1, testPsicomInput_persistenceId);
//                            pstm.setInt(2, row.persistenceId);
//                            pstm.setInt(3, contador);
//                            pstm.executeUpdate();
//                            contador++;
//                            strError = strError + " | contador: " + contador;
//                        }
//                    }
//                }
//            }
			
            /*==============================================TESTPSICOMETRICO_PROBLEMASSALUD FIN==============================================*/
            /*==============================================TESTPSICOMETRICO_TIPOASISTENCIA INICIO==============================================*/
            contador = 0;
            pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_TIPOASISTENCIA);
            pstm.setInt(1, testPsicomInput_persistenceId);
            pstm.executeUpdate();
            strError = strError + " | " + "-------------------------------------------";
            if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
                strError = strError + " | " + "-------------------------------------------";
                if (testPsicomInput.tipoAsistencia != null && testPsicomInput.tipoAsistencia != "") {
                    assert testPsicomInput.tipoAsistencia instanceof List;
                    for (def row: testPsicomInput.tipoAsistencia) {
                        if(row.persistenceId != null && row.persistenceId != ""){
                            pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_TIPOASISTENCIA);                                                                         
                            pstm.setInt(1, testPsicomInput_persistenceId);
                            pstm.setInt(2, row.persistenceId);
                            pstm.setInt(3, contador);
                            pstm.executeUpdate();
                            contador++;
                            strError = strError + " | contador: " + contador;
                        }
                    }
                }
            }
            /*==============================================TESTPSICOMETRICO_TIPOASISTENCIA FIN==============================================*/
            /*========================================================TEST PSICOMETRICO ACCIONES========================================================*/
			/*========================================================TEST PSICOMETRICO OBSERVACIONES ACCIONES========================================================*/
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_OBSERVACIONES);
			pstm.setString(1, caseId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomObservacionesInput != null && object.testPsicomObservacionesInput != "") {
					assert object.testPsicomObservacionesInput instanceof List;
					for (def row: object.testPsicomObservacionesInput) {
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_OBSERVACIONES);
						pstm.setString(1, (row.orden !=null && row.orden !="") ? row.orden : "");
						pstm.setString(2, (row.universidad !=null && row.universidad !="") ? row.universidad : "");
						pstm.setBoolean(3, (row.examen !=null && row.examen !="") ? row.examen : false);
						pstm.setBoolean(4, (row.admitido !=null && row.admitido !="") ? row.admitido : false);
						pstm.setBoolean(5, (row.vencido !=null && row.vencido !="") ? row.vencido : false);
						pstm.setInt(6, (row.porcentajeBeca !=null && row.porcentajeBeca !="") ? row.porcentajeBeca : 0);
						pstm.setInt(7, (row.porcentajeCredito !=null && row.porcentajeCredito !="") ? row.porcentajeCredito : 0);
						pstm.setString(8, caseId);
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			
			
			/*========================================================TEST PSICOMETRICO OBSERVACIONES ACCIONES========================================================*/
			/*========================================================TEST PSICOMETRICO RELATIVOS ACCIONES========================================================*/
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_RELATIVOS);
			pstm.setString(1, caseId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomRelativosInput != null && object.testPsicomRelativosInput != "") {
					assert object.testPsicomRelativosInput instanceof List;
					for (def row: object.testPsicomRelativosInput) {
						catParentezco = row.catParentezco;
						vive = row.vive;
						
						assert catParentezco instanceof Map;
						assert vive instanceof Map;
						
						strError = strError + " | contador: " + (1 + " - " + (row.nombre != null && row.nombre != "") ? row.nombre : "");
						strError = strError + " | contador: " + (2 + " - " + (row.apellidos != null && row.apellidos != "") ? row.apellidos : "");
						strError = strError + " | contador: " + (3 + " - " + (row.empresaTrabaja != null && row.empresaTrabaja != "") ? row.empresaTrabaja : "");
						strError = strError + " | contador: " + (4 + " - " + (row.otroParentesco != null && row.otroParentesco != "") ? row.otroParentesco : "");
						strError = strError + " | contador: " + (5 + " - " + caseId);
						strError = strError + " | contador: " + (6 + " - " + (row.jubilado != null && row.jubilado != "") ? row.jubilado : false);
						strError = strError + " | contador: " + (7 + " - " + (row.vencido != null && row.vencido != "") ? row.vencido : false);
						
						if (catParentezco.persistenceId != null && catParentezco.persistenceId != "") {
							strError = strError + " | contador: " + (8 + " - " + catParentezco.persistenceId);
						} else {
							strError = strError + " | contador: " + (8 + " - Types.INTEGER");
						}
						if (vive.persistenceId != null && vive.persistenceId != "") {
							strError = strError + " | contador: " + (9 + " - " + catParentezco.persistenceId);
						} else {
							strError = strError + " | contador: " + (9 + " - Types.INTEGER");
						}
						
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_RELATIVOS);
						pstm.setString(1, (row.nombre !=null && row.nombre !="") ? row.nombre : "");
						pstm.setString(2, (row.apellidos !=null && row.apellidos !="") ? row.apellidos : "");
						pstm.setString(3, (row.empresaTrabaja !=null && row.empresaTrabaja !="") ? row.empresaTrabaja : "");
						pstm.setString(4, (row.otroParentesco !=null && row.otroParentesco !="") ? row.otroParentesco : "");
						pstm.setString(5, caseId);
						pstm.setBoolean(6, (row.jubilado !=null && row.jubilado !="") ? row.jubilado : false);
						pstm.setBoolean(7, (row.vencido !=null && row.vencido !="") ? row.vencido : false);
						
						if(catParentezco.persistenceId != null && catParentezco.persistenceId != ""){
							pstm.setInt(8, catParentezco.persistenceId);
						}
						else {
							pstm.setNull(8, Types.INTEGER);
						}
						if(vive.persistenceId != null && vive.persistenceId != ""){
							pstm.setInt(9, vive.persistenceId);
						}
						else {
							pstm.setNull(9, Types.INTEGER);
						}
						
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			
			
			/*========================================================TEST PSICOMETRICO RELATIVOS ACCIONES========================================================*/
			/*========================================================TEST PSICOMETRICO CARRERAS REC ACCIONES========================================================*/
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_CARRERASREC);
			pstm.setString(1, caseId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomCarrerasRecInput != null && object.testPsicomCarrerasRecInput != "") {
					assert object.testPsicomCarrerasRecInput instanceof List;
					for (def row: object.testPsicomCarrerasRecInput) {
						carrera = row.carrera;
						
						assert carrera instanceof Map;
												
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_CARRERASREC);
						pstm.setInt(1, (row.orden !=null && row.orden !="") ? row.orden : "");
						pstm.setString(2, (row.comentarios !=null && row.comentarios !="") ? row.comentarios : "");
						pstm.setString(3, caseId);
						pstm.setBoolean(4, (row.vencido !=null && row.vencido !="") ? row.vencido : "");
						
						if(carrera.persistenceId != null && carrera.persistenceId != ""){
							pstm.setInt(5, carrera.persistenceId);
						}
						else {
							pstm.setNull(5, Types.INTEGER);
						}
						
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			/*========================================================TEST PSICOMETRICO CARRERAS REC ACCIONES========================================================*/
			/*========================================================TEST PSICOMETRICO RASGOS ACCIONES========================================================*/
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_RASGOS);
			pstm.setString(1, caseId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomRasgosInput != null && object.testPsicomRasgosInput != "") {
					assert object.testPsicomRasgosInput instanceof List;
					for (def row: object.testPsicomRasgosInput) {
						rasgo = row.rasgo;
						calificacion = row.calificacion;
						
						assert rasgo instanceof Map;
						assert calificacion instanceof Map;
												
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_RASGOS);
						if(rasgo.persistenceId != null && rasgo.persistenceId != ""){
							pstm.setInt(1, rasgo.persistenceId);
						}
						else {
							pstm.setNull(1, Types.INTEGER);
						}
						if(calificacion.persistenceId != null && calificacion.persistenceId != ""){
							pstm.setInt(2, calificacion.persistenceId);
						}
						else {
							pstm.setNull(2, Types.INTEGER);
						}
						pstm.setString(3, caseId);
						pstm.setBoolean(4, (row.vencido !=null && row.vencido !="") ? row.vencido : "");
						
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			/*========================================================TEST PSICOMETRICO RASGOS ACCIONES========================================================*/
			/*========================================================TEST PSICOMETRICO TERAPIAS ACCIONES========================================================*/
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_TERAPIAS);
			pstm.setLong(1, Long.valueOf(caseId));
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomTerapiasInput != null && object.testPsicomTerapiasInput != "") {
					assert object.testPsicomTerapiasInput instanceof List;
					for (def row: object.testPsicomTerapiasInput) {
						catTipoTerapia = row.catTipoTerapia;
						catRecibidoTerapia = row.catRecibidoTerapia;
						
						assert catTipoTerapia instanceof Map;
						assert catRecibidoTerapia instanceof Map;
												
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_TERAPIAS);
						pstm.setString(1, (row.tipoTerapia != null && row.tipoTerapia != "") ? row.tipoTerapia : "");
						pstm.setString(2, (row.cuantoTiempo != null && row.cuantoTiempo != "") ? row.cuantoTiempo : "");
						pstm.setString(3, (row.recibidoTerapiaString != null && row.recibidoTerapiaString != "") ? row.recibidoTerapiaString : "");
						pstm.setString(4, (row.otraTerapia != null && row.otraTerapia != "") ? row.otraTerapia : "");
						pstm.setLong(5, Long.valueOf(caseId));
						pstm.setBoolean(6, (row.vencido !=null && row.vencido !="") ? row.vencido : "");
						if(catTipoTerapia.persistenceId != null && catTipoTerapia.persistenceId != ""){
							pstm.setInt(7, catTipoTerapia.persistenceId);
						}
						else {
							pstm.setNull(7, Types.INTEGER);
						}
						if(catRecibidoTerapia.persistenceId != null && catRecibidoTerapia.persistenceId != ""){
							pstm.setInt(8, catRecibidoTerapia.persistenceId);
						}
						else {
							pstm.setNull(8, Types.INTEGER);
						}
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			/*========================================================TEST PSICOMETRICO TERAPIAS ACCIONES========================================================*/
			
			/*========================================================TEST PSICOMETRICO RELATIVOS ACCIONES========================================================*/
			//
            

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
    
	private Result generateUpdateResult(def testPsicomInput) {
		Result result = new Result();
		def catPersonaSaludable = null;
		def catVivesEstadoDiscapacidad = null;
		def catProblemaSaludAtencionContinua = null;
		def catRequieresAsistencia = null;
		def catRecibidoTerapia = null;
		
		String columnaUpdate = "";
		String strError = "error log :: ";
		try {
			if(testPsicomInput.ajusteEfectivo != null && testPsicomInput.ajusteEfectivo != ""){
				columnaUpdate = columnaUpdate + "ajusteEfectivo = '"+testPsicomInput.ajusteEfectivo + "', ";
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
			if(testPsicomInput.fuentesInfluyeronDesicion != null && testPsicomInput.fuentesInfluyeronDesicion != ""){
				columnaUpdate = columnaUpdate + "fuentesInfluyeronDesicion = '"+testPsicomInput.fuentesInfluyeronDesicion+"', ";
			}
			if(testPsicomInput.personasInfluyeronDesicion != null && testPsicomInput.personasInfluyeronDesicion != ""){
				columnaUpdate = columnaUpdate + "personasInfluyeronDesicion = '"+testPsicomInput.personasInfluyeronDesicion+"', ";
			}
			if(testPsicomInput.problemasSaludAtencionContinua != null && testPsicomInput.problemasSaludAtencionContinua != ""){
				columnaUpdate = columnaUpdate + "problemasSaludAtencionContinua = '"+testPsicomInput.problemasSaludAtencionContinua+"', ";
			}
			if(testPsicomInput.tipoDiscapacidad != null && testPsicomInput.tipoDiscapacidad != ""){
				columnaUpdate = columnaUpdate + "tipoDiscapacidad = '"+testPsicomInput.tipoDiscapacidad+"', ";
			}
			
			if(testPsicomInput.fuentesInfluyeronDesicion != null && testPsicomInput.fuentesInfluyeronDesicion != ""){
				columnaUpdate = columnaUpdate + "fuentesInfluyeronDesicion = '"+testPsicomInput.fuentesInfluyeronDesicion+"', ";
			}
			if(testPsicomInput.personasInfluyeronDesicion != null && testPsicomInput.personasInfluyeronDesicion != ""){
				columnaUpdate = columnaUpdate + "personasInfluyeronDesicion = '"+testPsicomInput.personasInfluyeronDesicion+"', ";
			}
			if(testPsicomInput.problemasSaludAtencionContinua != null && testPsicomInput.problemasSaludAtencionContinua != ""){
				columnaUpdate = columnaUpdate + "problemasSaludAtencionContinua = '"+testPsicomInput.problemasSaludAtencionContinua+"', ";
			}
			if(testPsicomInput.tipoDiscapacidad != null && testPsicomInput.tipoDiscapacidad != ""){
				columnaUpdate = columnaUpdate + "tipoDiscapacidad = '"+testPsicomInput.tipoDiscapacidad+"', ";
			}
			if(testPsicomInput.hasRecibidoAlgunaTerapia != null && testPsicomInput.hasRecibidoAlgunaTerapia != ""){
				columnaUpdate = columnaUpdate + "hasRecibidoAlgunaTerapia = '"+testPsicomInput.hasRecibidoAlgunaTerapia+"', ";
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
			
			if (testPsicomInput.catRecibidoTerapia != null && testPsicomInput.catRecibidoTerapia != "") {
				catRecibidoTerapia = testPsicomInput.catRecibidoTerapia;
				assert catRecibidoTerapia instanceof Map;
				if (catRecibidoTerapia.persistenceId != null && catRecibidoTerapia.persistenceId != "") {
					columnaUpdate = columnaUpdate + "catRecibidoTerapia_pid = " + catRecibidoTerapia.persistenceId + ", ";
				}
			}
			
			strError += " |columnaUpdate :: " + columnaUpdate + " ||| ";
			if(columnaUpdate.length() > 0) {
				columnaUpdate = columnaUpdate.substring(0, columnaUpdate.length() - 2);
			}
			
			List<String> data = new ArrayList<String>();
			data.add(Statements.UPDATE_TESTPSICOMETRICO.replace("[COLUMNA]", columnaUpdate));
			
			result.setData(data);
			result.setError_info(strError);
			result.setSuccess(true);
		} catch(Exception e) {
			LOGGER.error "e: "+e.getMessage();
			result.setError_info(strError);
			result.setSuccess(false);
			result.setError(e.getMessage());
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e2) {
			LOGGER.error "e2: "+e2.getMessage();
			result.setError_info(strError);
			result.setSuccess(false);
			result.setError(e2.getMessage());
			e2.printStackTrace();
		}
		
		return result
	}
		
	public Result insertUpdatePsicometricoV2(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
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

		def catParentezco = null;
		def vive = null;
		def carrera = null;
		
		def rasgo = null;
		def calificacion = null;
		
		def catTipoTerapia = null;
		def catRecibidoTerapia = null;
		
		Boolean closeCon = false;
		
		Integer testPsicomInput_persistenceId = 0;
		Integer contador = 0;
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			assert object instanceof Map;
			
			def testPsicomInput = object.testPsicomInput;
			assert testPsicomInput instanceof Map;
			
			if(object.caseId != null && object.caseId != ""){
				caseId=object.caseId;
			}
						
			consultaInsert = Statements.INSERT_TESTPSICOMETRICO_V2;
			
			Result resultConsultaUpdate = generateUpdateResult(testPsicomInput);
			if(!resultConsultaUpdate.isSuccess()) {
				throw new Exception("Error en el update :: " + resultConsultaUpdate.getError() + " || " + resultConsultaUpdate.getError_info());
			} else {
				consultaUpdate = resultConsultaUpdate.getData().get(0);
			}
			
			closeCon = validarConexion();
			
			/*========================================================TEST PSICOMETRICO ACCIONES========================================================*/
			pstm = con.prepareStatement(Statements.SELECT_TESTPSICOMETRICO_BY_CASEID_V2);
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
				
				strError += " comienza el insert | "
			
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
				if(testPsicomInput.fechaEntrevista != null && testPsicomInput.fechaEntrevista != "") {
					pstm.setString(15, testPsicomInput.fechaEntrevista);
				}
				else {
					pstm.setNull(15, Types.TIMESTAMP);
				}
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
				
				if(catPersonaSaludable != null) {
					if(catPersonaSaludable.persistenceId != null && catPersonaSaludable.persistenceId != ""){
						pstm.setInt(26, catPersonaSaludable.persistenceId);
					} else {
						pstm.setNull(26, Types.INTEGER);
					}
				} else {
					pstm.setNull(26, Types.INTEGER);
				}
				
				if(catProblemaSaludAtencionContinua != null) {
					if(catProblemaSaludAtencionContinua.persistenceId != null && catProblemaSaludAtencionContinua.persistenceId != ""){
						pstm.setInt(27, catProblemaSaludAtencionContinua.persistenceId);
					} else {
						pstm.setNull(27, Types.INTEGER);
					}
				} else {
					pstm.setNull(27, Types.INTEGER);
				}
				if(catRequieresAsistencia != null) {
					if(catRequieresAsistencia.persistenceId != null && catRequieresAsistencia.persistenceId != ""){
						pstm.setInt(28, catRequieresAsistencia.persistenceId);
					} else {
						pstm.setNull(28, Types.INTEGER);
					}
				} else {
					pstm.setNull(28, Types.INTEGER);
				}
				if(catVivesEstadoDiscapacidad != null) {
					if(catVivesEstadoDiscapacidad.persistenceId != null && catVivesEstadoDiscapacidad.persistenceId != ""){
						pstm.setInt(29, catVivesEstadoDiscapacidad.persistenceId);
					}
					else {
						pstm.setNull(29, Types.INTEGER);
					}
				}else {
					pstm.setNull(29, Types.INTEGER);
				}
				
				pstm.setString(30, (testPsicomInput.fuentesInfluyeronDesicion != null && testPsicomInput.fuentesInfluyeronDesicion != "") ? testPsicomInput.fuentesInfluyeronDesicion : "");
				pstm.setString(31, (testPsicomInput.personasInfluyeronDesicion != null && testPsicomInput.personasInfluyeronDesicion != "") ? testPsicomInput.personasInfluyeronDesicion : "");
				pstm.setString(32, (testPsicomInput.problemasSaludAtencionContinua != null && testPsicomInput.problemasSaludAtencionContinua != "") ? testPsicomInput.problemasSaludAtencionContinua : "");
				pstm.setString(33, (testPsicomInput.tipoDiscapacidad != null && testPsicomInput.tipoDiscapacidad != "") ? testPsicomInput.tipoDiscapacidad : "");
				pstm.setString(34, (testPsicomInput.hasRecibidoAlgunaTerapia != null && testPsicomInput.hasRecibidoAlgunaTerapia != "") ? testPsicomInput.hasRecibidoAlgunaTerapia : "");
				
				pstm.executeUpdate();
			}
			
			pstm = con.prepareStatement(Statements.SELECT_TESTPSICOMETRICO_BY_CASEID);
			pstm.setString(1, caseId);
			rs = pstm.executeQuery();
			if(rs.next()) {
				testPsicomInput_persistenceId = rs.getInt("persistenceid");
			}
			
			/*==============================================TESTPSICOMETRICO_RELATIVOS INICIO==============================================*/
			strError += "Relativos | "
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_RELATIVOS);
			pstm.setString(1, caseId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomRelativosInput != null && object.testPsicomRelativosInput != "") {
					assert object.testPsicomRelativosInput instanceof List;
					for (def row: object.testPsicomRelativosInput) {
						catParentezco = row.catParentezco;
						vive = row.vive;
						
						assert catParentezco instanceof Map;
						if(vive != null) {
							assert vive instanceof Map;
						}
						
						if (catParentezco.persistenceId != null && catParentezco.persistenceId != "") {
							strError = strError + " | contador: " + (8 + " - " + catParentezco.persistenceId);
						} else {
							strError = strError + " | contador: " + (8 + " - Types.INTEGER");
						}
						if(vive != null ) {
							if (vive.persistenceId != null && vive.persistenceId != "") {
								strError = strError + " | contador: " + (9 + " - " + catParentezco.persistenceId);
							} else {
								strError = strError + " | contador: " + (9 + " - Types.INTEGER");
							}
						} else {
							strError = strError + " | contador: " + (9 + " - Types.INTEGER");
						}
						
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_RELATIVOS);
						pstm.setString(1, (row.nombre !=null && row.nombre !="") ? row.nombre : "");
						pstm.setString(2, (row.apellidos !=null && row.apellidos !="") ? row.apellidos : "");
						pstm.setString(3, (row.empresaTrabaja !=null && row.empresaTrabaja !="") ? row.empresaTrabaja : "");
						pstm.setString(4, (row.otroParentesco !=null && row.otroParentesco !="") ? row.otroParentesco : "");
						pstm.setString(5, caseId);
						pstm.setBoolean(6, (row.jubilado !=null && row.jubilado !="") ? row.jubilado : false);
						pstm.setBoolean(7, (row.vencido !=null && row.vencido !="") ? row.vencido : false);
						
						if(catParentezco.persistenceId != null && catParentezco.persistenceId != ""){
							pstm.setInt(8, catParentezco.persistenceId);
						}
						else {
							pstm.setNull(8, Types.INTEGER);
						}
						
						if(vive != null ) {
							if(vive.persistenceId != null && vive.persistenceId != ""){
								pstm.setInt(9, vive.persistenceId);
							}
							else {
								pstm.setNull(9, Types.INTEGER);
							}
						} else {
							pstm.setNull(9, Types.INTEGER);
						}
						
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			if(testPsicomInput.puntuacionINVP!= null) {
				pstm = con.prepareStatement(Statements.UPDATE_PUNTUACION_INVP);
				pstm.setString(1,testPsicomInput.puntuacionINVP+"")
				pstm.setString(2, caseId);
				pstm.executeUpdate();
			}
			/*==============================================TESTPSICOMETRICO_RELATIVOS FIN==============================================*/
			
			/*==============================================TESTPSICOMETRICO_CUSTOSRECOMEND INICIO==============================================*/
			strError += "Cursos reocmendados | "
			contador = 0;
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_CUSTOSRECOMEND);
			pstm.setInt(1, testPsicomInput_persistenceId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (testPsicomInput.custosRecomendados != null && testPsicomInput.custosRecomendados != "") {
					assert testPsicomInput.custosRecomendados instanceof List;
					for (def row: testPsicomInput.custosRecomendados) {
						if(row.persistenceId != null && row.persistenceId != ""){
							pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_CUSTOSRECOMEND);
							pstm.setInt(1, testPsicomInput_persistenceId);
							pstm.setInt(2, row.persistenceId);
							pstm.setInt(3, contador);
							pstm.executeUpdate();
							contador++;
							strError = strError + " | contador: " + contador;
						}
					}
				}
			}
			/*==============================================TESTPSICOMETRICO_CUSTOSRECOMEND FIN==============================================*/
			
			/*========================================================TEST PSICOMETRICO RASGOS ACCIONES========================================================*/
			strError += "Rasgos | "
			contador = 0;
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_RASGOS);
			pstm.setString(1, caseId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomRasgosInput != null && object.testPsicomRasgosInput != "") {
					assert object.testPsicomRasgosInput instanceof List;
					for (def row: object.testPsicomRasgosInput) {
						rasgo = row.rasgo;
						calificacion = row.calificacion;
						
						assert rasgo instanceof Map;
						if(calificacion != null ) {
							assert calificacion instanceof Map;
						}
												
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_RASGOS);
						if(rasgo.persistenceId != null && rasgo.persistenceId != ""){
							pstm.setInt(1, rasgo.persistenceId);
						}
						else {
							pstm.setNull(1, Types.INTEGER);
						}
						if(calificacion != null) {
							if(calificacion.persistenceId != null && calificacion.persistenceId != ""){
								pstm.setInt(2, calificacion.persistenceId);
							}
							else {
								pstm.setNull(2, Types.INTEGER);
							}
						} else {
							pstm.setNull(2, Types.INTEGER);
						}
						pstm.setString(3, caseId);
						pstm.setBoolean(4, false);
						
						pstm.executeUpdate();
						contador++;
					}
				}
			}
			/*========================================================TEST PSICOMETRICO RASGOS FIN========================================================*/
			
			/*========================================================TEST PSICOMETRICO CARRERAS REC ACCIONES========================================================*/
			strError += "Carreras recomendadas | "
			contador = 0;
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_CARRERASREC);
			pstm.setString(1, caseId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomCarrerasRecInput != null && object.testPsicomCarrerasRecInput != "") {
					assert object.testPsicomCarrerasRecInput instanceof List;
					for (def row: object.testPsicomCarrerasRecInput) {
						carrera = row.carrera;
						
						assert carrera instanceof Map;
												
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_CARRERASREC);
						pstm.setInt(1, (row.orden !=null && row.orden !="") ? row.orden : "");
						pstm.setString(2, (row.comentarios !=null && row.comentarios !="") ? row.comentarios : "");
						pstm.setString(3, caseId);
						pstm.setBoolean(4, (row.vencido !=null && row.vencido !="") ? row.vencido : "");
						
						if(carrera.persistenceId != null && carrera.persistenceId != ""){
							pstm.setInt(5, carrera.persistenceId);
						}
						else {
							pstm.setNull(5, Types.INTEGER);
						}
						
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			/*========================================================TEST PSICOMETRICO CARRERAS REC FIN========================================================*/
			
			/*========================================================TEST PSICOMETRICO OBSERVACIONES ACCIONES========================================================*/
			strError += "Observaciones | "
			contador = 0;
			pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_OBSERVACIONES);
			pstm.setString(1, caseId);
			pstm.executeUpdate();
			strError = strError + " | " + "-------------------------------------------";
			if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
				strError = strError + " | " + "-------------------------------------------";
				if (object.testPsicomObservacionesInput != null && object.testPsicomObservacionesInput != "") {
					assert object.testPsicomObservacionesInput instanceof List;
					for (def row: object.testPsicomObservacionesInput) {
						pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_OBSERVACIONES);
						pstm.setString(1, (row.orden !=null && row.orden !="") ? row.orden.toString() : "");
						pstm.setString(2, (row.universidad !=null && row.universidad !="") ? row.universidad : "");
						pstm.setBoolean(3, (row.examen !=null && row.examen !="") ? row.examen : false);
						pstm.setBoolean(4, (row.admitido !=null && row.admitido !="") ? row.admitido : false);
						pstm.setBoolean(5, (row.vencido !=null && row.vencido !="") ? row.vencido : false);
						pstm.setInt(6, (row.porcentajeBeca !=null && row.porcentajeBeca !="") ? Integer.parseInt(row.porcentajeBeca) : 0);
						pstm.setInt(7, (row.porcentajeCredito !=null && row.porcentajeCredito !="") ? Integer.parseInt(row.porcentajeCredito) : 0);
						pstm.setString(8, caseId);
						pstm.executeUpdate();
						contador++;
						strError = strError + " | contador: " + contador;
					}
				}
			}
			/*========================================================TEST PSICOMETRICO OBSERVACIONES ACCIONES========================================================*/
			
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
	
public Result getPsicometricoCompleto(String caseId, RestAPIContext context) {
    String errorinfo="";
    try {
    Result resultado = new Result();
    Boolean closeCon = false;
    List<?> rows = new ArrayList<?>();
    String strError = "";
    Map<String, Object> objetoCompleto = new LinkedHashMap<String, Object>();
    Map<String, Object> testPsicomInput = new LinkedHashMap<String, Object>();
    List<Map<String, Object>> testPsicomObservacionesInput = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> testPsicomRelativosInput = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> testPsicomRasgosInput = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> custosRecomendados = new ArrayList<Map<String, Object>>();
    errorinfo+="[1] Preparacion de variables "
    try {
        errorinfo+="[2] AutodescripcionDAO.findByCaseId "
        closeCon = validarConexion();
        errorinfo+="[3] Conexion "
        pstm = con.prepareStatement(Statements.SELECT_TESTPSICOMETRICO_BY_CASEID_V2);
        pstm.setString(1, caseId);
        rs = pstm.executeQuery();
        
        while(rs.next()) {
            testPsicomInput = new LinkedHashMap<String, Object>();
            testPsicomInput.put("fechaEntrevista", rs.getString("fechaEntrevista"));
            testPsicomInput.put("ajusteMedioFamiliar", rs.getString("ajusteMedioFamiliar"));
            testPsicomInput.put("califAjusteMedioFamiliar", rs.getString("califAjusteMedioFamiliar"));
            testPsicomInput.put("ajusteEscolarPrevio", rs.getString("ajusteEscolarPrevio"));
            testPsicomInput.put("califAjusteEscolarPrevio", rs.getString("califAjusteEscolarPrevio"));
            testPsicomInput.put("ajusteMedioSocial", rs.getString("ajusteMedioSocial"));
            testPsicomInput.put("califAjusteMedioSocial", rs.getString("califAjusteMedioSocial"));
            testPsicomInput.put("ajusteEfectivo", rs.getString("ajusteEfectivo"));
            testPsicomInput.put("califAjusteAfectivo", rs.getString("califAjusteAfectivo"));
            testPsicomInput.put("ajusteReligioso", rs.getString("ajusteReligioso"));
            testPsicomInput.put("califAjusteReligioso", rs.getString("califAjusteReligioso"));
            testPsicomInput.put("ajusteExistencial", rs.getString("ajusteExistencial"));
            testPsicomInput.put("califAjusteExistencial", rs.getString("califAjusteExistencial"));
            testPsicomInput.put("interpretacionINVP", rs.getString("interpretacionINVP"));
            testPsicomInput.put("puntuacionINVP", rs.getString("puntuacionINVP"));
            testPsicomInput.put("resumenSalud", rs.getString("resumenSalud"));
            testPsicomInput.put("finalizado", rs.getBoolean("finalizado"));
            testPsicomInput.put("otroTipoAsistencia", rs.getString("otroTipoAsistencia"));
            testPsicomInput.put("quienRealizoEntrevista", rs.getString("quienRealizoEntrevista"));
            testPsicomInput.put("quienIntegro", rs.getString("quienIntegro"));
            testPsicomInput.put("hasParticipadoActividadesAyuda", rs.getString("hasParticipadoActividadesAyuda"));
            testPsicomInput.put("participacionActividadesVoluntaria", rs.getString("participacionActividadesVoluntaria"));
            testPsicomInput.put("fuentesInfluyeronDesicion", rs.getString("fuentesInfluyeronDesicion"));
            testPsicomInput.put("personasInfluyeronDesicion", rs.getString("personasInfluyeronDesicion"));
            testPsicomInput.put("problemasSaludAtencionContinua", rs.getString("problemasSaludAtencionContinua"));
            testPsicomInput.put("tipoDiscapacidad", rs.getString("tipoDiscapacidad"));
            testPsicomInput.put("catrecibidoterapia_pid", rs.getLong("catrecibidoterapia_pid"));
            testPsicomInput.put("catproblemasaludatencionco_pid", rs.getLong("catproblemasaludatencionco_pid"));
            testPsicomInput.put("catrequieresasistencia_pid", rs.getLong("catrequieresasistencia_pid"));
            testPsicomInput.put("catvivesestadodiscapacidad_pid", rs.getLong("catvivesestadodiscapacidad_pid"));
            testPsicomInput.put("catpersonasaludable_pid", rs.getLong("catrecibidoterapia_pid"));
			testPsicomInput.put("conclusioneINVP", rs.getString("conclusioneINVP"));
			
            
        }
        errorinfo+="[4] testPsicomInput lleno "
        
        objetoCompleto.put("caseId", caseId);
		objetoCompleto.put("quienRealizoEntrevista", testPsicomInput.get("quienRealizoEntrevista"));
		objetoCompleto.put("quienIntegro", testPsicomInput.get("quienIntegro"));
		objetoCompleto.put("puntuacionINVP", testPsicomInput.get("puntuacionINVP"));
		objetoCompleto.put("fechaEntrevista", testPsicomInput.get("fechaEntrevista"));
		objetoCompleto.put("hasParticipadoActividadesAyuda", testPsicomInput.get("hasParticipadoActividadesAyuda"));
		objetoCompleto.put("participacionActividadesVoluntaria", testPsicomInput.get("participacionActividadesVoluntaria"));
		objetoCompleto.put("ajusteMedioFamiliar", testPsicomInput.get("ajusteMedioFamiliar"));
		objetoCompleto.put("ajusteEscolarPrevio", testPsicomInput.get("ajusteEscolarPrevio"));
		objetoCompleto.put("califAjusteMedioFamiliar", testPsicomInput.get("califAjusteMedioFamiliar"));
		objetoCompleto.put("ajusteMedioSocial", testPsicomInput.get("ajusteMedioSocial"));
		objetoCompleto.put("califAjusteEscolarPrevio", testPsicomInput.get("califAjusteEscolarPrevio"));
		objetoCompleto.put("califAjusteMedioSocial", testPsicomInput.get("califAjusteMedioSocial"));
		objetoCompleto.put("ajusteEfectivo", testPsicomInput.get("ajusteEfectivo"));
		objetoCompleto.put("califAjusteAfectivo", testPsicomInput.get("califAjusteAfectivo"));
		objetoCompleto.put("ajusteReligioso", testPsicomInput.get("ajusteReligioso"));
		objetoCompleto.put("califAjusteReligioso", testPsicomInput.get("califAjusteReligioso"));
		objetoCompleto.put("ajusteExistencial", testPsicomInput.get("ajusteExistencial"));
		objetoCompleto.put("califAjusteExistencial", testPsicomInput.get("califAjusteExistencial"));
		objetoCompleto.put("resumenSalud", testPsicomInput.get("resumenSalud"));
		objetoCompleto.put("interpretacionINVP", testPsicomInput.get("interpretacionINVP"));
		objetoCompleto.put("conclusioneINVP", testPsicomInput.get("conclusioneINVP"));
		
        pstm = con.prepareStatement("SELECT * FROM TestPsicometricoRelativos where caseid=?");
			pstm.setString(1, caseId)
			rs= pstm.executeQuery();
			
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i), rs.getString(i));
				}
				testPsicomRelativosInput.add(columns)
			}
			
        errorinfo+="[5] TestPsicometricoRelativosDAO.findByCaseId "
        objetoCompleto.put("testPsicomRelativosInput", testPsicomRelativosInput);

        pstm = con.prepareStatement("SELECT * FROM TestPsicometricoRasgos where caseid=?");
			pstm.setString(1, caseId)
			rs= pstm.executeQuery();
			
			metaData = rs.getMetaData();
			columnCount = metaData.getColumnCount();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i), rs.getString(i));
				}
				testPsicomRasgosInput.add(columns)
			}
			pstm = con.prepareStatement("SELECT * FROM TestPsicometricoObservaciones where caseid=?");
			pstm.setString(1, caseId)
			rs= pstm.executeQuery();
			metaData = rs.getMetaData();
			columnCount = metaData.getColumnCount();
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).equals("admitido") || metaData.getColumnLabel(i).equals("examen")|| metaData.getColumnLabel(i).equals("vencido")) {
						columns.put(metaData.getColumnLabel(i), rs.getBoolean(i));
					}else if(metaData.getColumnLabel(i).equals("porcentajebeca")) {
						columns.put("porcentajeBeca", rs.getString(i));
					}else if(metaData.getColumnLabel(i).equals("porcentajecredito")) {
						columns.put("porcentajeCredito", rs.getString(i));
					} else {
						columns.put(metaData.getColumnLabel(i), rs.getString(i));
					}
				}
				testPsicomObservacionesInput.add(columns)
			}
			Integer testPsicomInput_persistenceId=0
			pstm = con.prepareStatement(Statements.SELECT_TESTPSICOMETRICO_BY_CASEID);
			pstm.setString(1, caseId);
			rs = pstm.executeQuery();
			if(rs.next()) {
				testPsicomInput_persistenceId = rs.getInt("persistenceid");
			}
			pstm = con.prepareStatement("SELECT cc.* FROM testpsicometri_custosrecomend curso inner join catcursos cc on curso.catcursos_pid=cc.persistenceid where curso.testpsicometrico_pid=?");
			pstm.setInt(1, testPsicomInput_persistenceId)
			rs= pstm.executeQuery();
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				columns.put("persistenceId", rs.getLong("persistenceId"));
				columns.put("persistenceId_string",rs.getString("persistenceId"))
				columns.put("persistenceVersion",rs.getLong("persistenceVersion"))
				columns.put("persistenceVersion_string",rs.getString("persistenceVersion"))
				columns.put("clave",rs.getString("clave"))
				columns.put("descripcion",rs.getString("descripcion"))
				columns.put("fechaCreacion",rs.getString("fechaCreacion"))
				columns.put("usuarioCreacion",rs.getString("usuarioCreacion"))
				columns.put("isEliminado",rs.getBoolean("isEliminado"))
				
				custosRecomendados.add(columns)
			}
		testPsicomInput.put("custosRecomendados", custosRecomendados)
		objetoCompleto.put("testPsicomInput", testPsicomInput);
        errorinfo+="[6] TestPsicometricoRasgosDAO.findByCaseId "
        objetoCompleto.put("testPsicomRasgosInput", testPsicomRasgosInput);
		objetoCompleto.put("testPsicomObservacionesInput", testPsicomObservacionesInput);
        rows.add(objetoCompleto);
        
        errorinfo+="[7] rows.add "
        resultado.setError_info(strError);
        resultado.setData(rows);
        resultado.setSuccess(true);
    }catch(Exception e) {
        resultado.setError_info(strError);
        resultado.setSuccess(false);
        resultado.setError(e.getMessage());
    }finally {
        if(closeCon) {
            new DBConnect().closeObj(con, stm, rs, pstm)
        }
    }
    
    return resultado;
    }catch(Exception fullEx) {
        Result result= new Result()
        result.setError(fullEx.getMessage())
        result.setSuccess(false)
        result.setError_info(errorinfo)
        return result
    }
}

	public Result getPsicometricoCursos(Long persistenceid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<?> rows = new ArrayList<?>();
		String strError = "";
		Map<String, Object> curso = new LinkedHashMap<String, Object>();
		
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(Statements.SELECT_TESTPSICOMETRICO_BY_CASEID_V2);
			pstm.setString(1, persistenceid);
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				curso = new LinkedHashMap<String, Object>();
				curso.put("fechaEntrevista", rs.getString("fechaEntrevista"));
				curso.put("ajusteMedioFamiliar", rs.getString("ajusteMedioFamiliar"));
				curso.put("califAjusteMedioFamiliar", rs.getString("califAjusteMedioFamiliar"));
				
				rows.add(curso);
			}
			
			
			strError += "Se obtuvieron los resultados | ";
			resultado.setError_info(strError);
			resultado.setData(rows);
			resultado.setSuccess(true);
		}catch(Exception e) {
			resultado.setError_info(strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
}
