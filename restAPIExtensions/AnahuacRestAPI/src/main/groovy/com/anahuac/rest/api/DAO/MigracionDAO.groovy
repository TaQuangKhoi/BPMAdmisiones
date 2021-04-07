package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import org.bonitasoft.engine.identity.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.EntitySesionAspirante
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class MigracionDAO {
	
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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MigracionDAO.class);
	
	public Result callSesiones(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		String errorInfo="";
		String sesion_pid="";
				
		Boolean closeCon = false;
		
		List<EntitySesionAspirante> lstEntitySesionAspirante = new ArrayList<EntitySesionAspirante>();
		
		EntitySesionAspirante objEntitySesionAspirante = new EntitySesionAspirante();
		
		User objUser = null;
		try {
			
			closeCon = validarConexion();
			
			LOGGER.error "1=====================================================================";
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			assert object instanceof List;
			LOGGER.error "2=====================================================================";
			
			pstm = con.prepareStatement(Statements.GET_SESION_MIGRACION);
		
			if(object != null) {
				LOGGER.error "3=====================================================================";
				for(def row: object) {
					/*
					errorInfo +=" | idAspirante="+row.idAspirante;
					errorInfo +=" | sesion="+row.sesion;
					errorInfo +=" | horario="+row.horario;
					errorInfo +=" | asistenciaCollegeBoard="+row.asistenciaCollegeBoard;
					errorInfo +=" | asistenciaPsicometrico="+row.asistenciaPsicometrico;
					errorInfo +=" | asistenciaEntrevista="+row.asistenciaEntrevista;
					errorInfo +=" | responsable="+row.responsable;
					errorInfo +=" | sesionPadre="+row.sesionPadre;
					errorInfo +=" | fechaInicioPadre="+row.fechaInicioPadre;
					errorInfo +=" | fechaFinPadre="+row.fechaFinPadre;
					errorInfo +=" | fechaInicioHijo="+row.fechaInicioHijo;
					errorInfo +=" | fechaFinHijo="+row.fechaFinHijo;
					errorInfo +=" | campus_pid="+row.campus_pid;
					if(row.username != null) {
						errorInfo +=" | username="+row.username;
					}
					errorInfo +=" | --------------------------------------------------------------";
					*/
					if(row.campus_pid != null && row.fechaInicioPadre != null && row.sesionPadre != null && !row.fechaInicioPadre.equals("") && !row.sesionPadre.equals("")) {
						
						errorInfo +=" | GET_SESION_MIGRACION="+Statements.GET_SESION_MIGRACION;
						errorInfo +=" | row.campus_pid="+String.valueOf(row.campus_pid);
						errorInfo +=" | row.fechaInicioPadre="+row.fechaInicioPadre;
						errorInfo +=" | row.sesionPadre="+row.sesionPadre;
						
						pstm.setInt(1, Integer.valueOf(String.valueOf(row.campus_pid)));
						pstm.setString(2, row.fechaInicioPadre);
						pstm.setString(3, row.sesionPadre);
						rs = pstm.executeQuery();
						errorInfo +=" | ------------------------------------------------------";
						
						if(rs.next()) {
							
							sesion_pid = rs.getString("persistenceid");
							errorInfo +=" | SE ENCONTRO RESULTADOS----------------------------------------";
							errorInfo +=" | persistenceid="+String.valueOf(rs.getInt("persistenceid"));
							
							objEntitySesionAspirante = new EntitySesionAspirante();
							objEntitySesionAspirante.setSesiones_pid(rs.getInt("persistenceid"));
							objEntitySesionAspirante.setUsername(row.username);
							objEntitySesionAspirante.setResponsable(row.responsable);
							objEntitySesionAspirante.setHorario(row.horario);
							lstEntitySesionAspirante.add(objEntitySesionAspirante);
							
						} else {
							errorInfo +=" | NO ENCONTRO RESULTADOS----------------------------------------";
							errorInfo +=" | username="+row.username;
							errorInfo +=" | sesionPadre="+row.sesionPadre;
							errorInfo +=" | fechaInicioPadre="+row.fechaInicioPadre;
							errorInfo +=" | campus_pid="+row.campus_pid;
							errorInfo +=" | --------------------------------------------------------------";
						}
					}
					else {
						errorInfo +=" | FALTANTES-----------------------------------------------------";
						errorInfo +=" | username="+row.username;
						errorInfo +=" | sesionPadre="+row.sesionPadre;
						errorInfo +=" | fechaInicioPadre="+row.fechaInicioPadre;
						errorInfo +=" | campus_pid="+row.campus_pid;
						errorInfo +=" | --------------------------------------------------------------";
					}
					
				}
				
				if(lstEntitySesionAspirante.size()>0) {
					pstm = con.prepareStatement(Statements.GET_RESPONSABLE_MIGRACION);
					for(EntitySesionAspirante rowData : lstEntitySesionAspirante) {
						pstm.setInt(1, rowData.getSesiones_pid());
						pstm.setString(2, rowData.getHorario());
						rs = pstm.executeQuery();
						while(rs.next()) {
							objUser = context.getApiClient().getIdentityAPI().getUser(rs.getLong("responsableid"));
							errorInfo +=" | RESPONSABLES DE SESIONES--------------------------------------";
							errorInfo +=" | Consulta="+objUser.firstName+" "+objUser.lastName;
							errorInfo +=" | Responsable="+rowData.getResponsable();
						}
					}
				} else {
					errorInfo +=" | NO COINCIDE NINGUN USUARIO------------------------------------";
					errorInfo +=" | username="+lstEntitySesionAspirante.size();
				}
				LOGGER.error "4=====================================================================";
			}
			LOGGER.error "5=====================================================================";
			resultado.setError_info(errorInfo);
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			LOGGER.error "6=====================================================================";
			errorInfo +=" | "+e.getMessage();
			resultado.setError_info(errorInfo);
			
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
}
