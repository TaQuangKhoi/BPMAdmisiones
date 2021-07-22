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

		def catParentezco = null;
		def vive = null;
		
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
            strError = strError + " | " + "==============================================TESTPSICOMETRICO_FIENTESINFLUYE INICIO==============================================";
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
            strError = strError + " | " + "==============================================TESTPSICOMETRICO_FIENTESINFLUYE FIN==============================================";
            /*==============================================TESTPSICOMETRICO_RAZONESINGRESO INICIO==============================================*/
            strError = strError + " | " + "==============================================TESTPSICOMETRICO_RAZONESINGRESO INICIO==============================================";
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
            strError = strError + " | " + "==============================================TESTPSICOMETRICO_RAZONESINGRESO FIN==============================================";
            /*==============================================TESTPSICOMETRICO_DISCAPACIDADES INICIO==============================================*/
            strError = strError + " | " + "==============================================TESTPSICOMETRICO_DISCAPACIDADES INICIO==============================================";
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
            strError = strError + " | " + "==============================================TESTPSICOMETRICO_DISCAPACIDADES FIN==============================================";
            /*==============================================TESTPSICOMETRICO_CUSTOSRECOMEND INICIO==============================================*/
            strError = strError + " | " + "==============================================TESTPSICOMETRICO_CUSTOSRECOMEND INICIO==============================================";
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
            strError = strError + " | " + "==============================================TESTPSICOMETRICO_CUSTOSRECOMEND FIN==============================================";
            /*==============================================TESTPSICOMETRICO_PROBLEMASSALUD INICIO==============================================*/
            strError = strError + " | " + "==============================================TESTPSICOMETRICO_PROBLEMASSALUD INICIO==============================================";
            contador = 0;
            pstm = con.prepareStatement(Statements.DELETE_TESTPSICOMETRICO_PROBLEMASSALUD);
            pstm.setInt(1, testPsicomInput_persistenceId);
            pstm.executeUpdate();
            strError = strError + " | " + "-------------------------------------------";
            if(testPsicomInput_persistenceId != null && testPsicomInput_persistenceId != 0) {
                strError = strError + " | " + "-------------------------------------------";
                if (testPsicomInput.problemasSalud != null && testPsicomInput.problemasSalud != "") {
                    assert testPsicomInput.problemasSalud instanceof List;
                    for (def row: testPsicomInput.problemasSalud) {
                        if(row.persistenceId != null && row.persistenceId != ""){
                            pstm = con.prepareStatement(Statements.INSERT_TESTPSICOMETRICO_PROBLEMASSALUD);
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
            /*==============================================TESTPSICOMETRICO_PROBLEMASSALUD FIN==============================================*/
            strError = strError + " | " + "==============================================TESTPSICOMETRICO_PROBLEMASSALUD FIN==============================================";
            /*==============================================TESTPSICOMETRICO_TIPOASISTENCIA INICIO==============================================*/
            strError = strError + " | " + "==============================================TESTPSICOMETRICO_TIPOASISTENCIA INICIO==============================================";
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
            strError = strError + " | " + "==============================================TESTPSICOMETRICO_TIPOASISTENCIA FIN==============================================";
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
