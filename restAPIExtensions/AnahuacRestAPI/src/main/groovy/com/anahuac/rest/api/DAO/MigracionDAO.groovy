package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.identity.User
import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.map.ObjectWriter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.EntitySesionAspirante
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.db.Sesion_Aspirante
import com.bonitasoft.engine.api.ProcessAPI
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
		Result resultInsertSesionAspirante = new Result();
		String errorInfo="";
		String sesion_pid="";

		Boolean closeCon = false;
		
		List<EntitySesionAspirante> lstEntitySesionAspirante = new ArrayList<EntitySesionAspirante>();
		List<HumanTaskInstance> lstHumanTaskInstance = new ArrayList<HumanTaskInstance>();
		
		EntitySesionAspirante objEntitySesionAspirante = new EntitySesionAspirante();
		
		User objUser = null;
		
		Sesion_Aspirante sesionAspirante = new Sesion_Aspirante();
		
		Long idResponsable = 0L;
		Integer responsabledisponible_pid = 0L;
		
		Map < String, Serializable > inputs = new HashMap < String, Serializable > ();
		
		
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
							throw new Exception("[EXCEPTION] - NO ENCONTRO RESULTADOS");
						}
					}
					else {
						errorInfo +=" | FALTANTES-----------------------------------------------------";
						errorInfo +=" | username="+row.username;
						errorInfo +=" | sesionPadre="+row.sesionPadre;
						errorInfo +=" | fechaInicioPadre="+row.fechaInicioPadre;
						errorInfo +=" | campus_pid="+row.campus_pid;
						errorInfo +=" | --------------------------------------------------------------";
						throw new Exception("[EXCEPTION] - INFORMACION FALTANTE EN ARCHIVO JSON");
					}
					
				}
				
				if(lstEntitySesionAspirante.size()>0) {
					for(EntitySesionAspirante rowData : lstEntitySesionAspirante) {
						
						errorInfo +=" | --------------------------------------------------------------";
						errorInfo +=" | getUsername="+ rowData.getUsername();
						errorInfo +=" | getResponsable="+ rowData.getResponsable();
						errorInfo +=" | getHorario="+ rowData.getHorario();
						errorInfo +=" | getSesiones_pid="+ rowData.getSesiones_pid().toString();
						errorInfo +=" | getResponsabledisponible_pid="+ rowData.getResponsabledisponible_pid().toString();
						errorInfo +=" | --------------------------------------------------------------";
						errorInfo +=" | GET_RESPONSABLE_MIGRACION="+ Statements.GET_RESPONSABLE_MIGRACION
						errorInfo +=" | getSesiones_pid="+ rowData.getSesiones_pid().toString();
						errorInfo +=" | getHorario="+ rowData.getHorario();
						errorInfo +=" | --------------------------------------------------------------";
						
						pstm = con.prepareStatement(Statements.GET_RESPONSABLE_MIGRACION);
						pstm.setInt(1, rowData.getSesiones_pid());
						pstm.setString(2, rowData.getHorario());
						rs = pstm.executeQuery();
						while(rs.next()) {
							idResponsable = rs.getLong("responsableid");
							responsabledisponible_pid = rs.getInt("persistenceid");
							objUser = context.getApiClient().getIdentityAPI().getUser(idResponsable);
							errorInfo +=" | RESPONSABLES DE SESIONES--------------------------------------";
							errorInfo +=" | Consulta="+objUser.firstName+" "+objUser.lastName;
							errorInfo +=" | Responsable="+rowData.getResponsable();
							errorInfo +=" | --------------------------------------------------------------";
							errorInfo +=" | CONDICION="+String.valueOf( (objUser.firstName+" "+objUser.lastName).replace("  ", " ").toLowerCase().equals(rowData.getResponsable().replace("  ", " ").toLowerCase()) );
							errorInfo +=" | objUser="+ (objUser.firstName+" "+objUser.lastName).replace("  ", " ").toLowerCase().trim();
							errorInfo +=" | rowData="+ rowData.getResponsable().replace("  ", " ").toLowerCase().trim();
							
							if((objUser.firstName+" "+objUser.lastName).replace("  ", " ").toLowerCase().trim().equals(rowData.getResponsable().replace("  ", " ").toLowerCase().trim())) {
								errorInfo +=" | MATCH EN USUARIOS---------------------------------------------";
								errorInfo +=" | responsabledisponible_pid="+responsabledisponible_pid;
								rowData.setResponsabledisponible_pid(responsabledisponible_pid)
								errorInfo +=" | rowData="+rowData.toString();
							}
						}
						if(rowData.getUsername() != null && rowData.getSesiones_pid() != null && rowData.getResponsabledisponible_pid() != null) {
							errorInfo +=" | --------------------------------------------------------------";
							errorInfo +=" | insertSesionAspirante=";
							
							sesionAspirante = new Sesion_Aspirante();
							sesionAspirante.setResponsabledisponible_pid(Long.valueOf(rowData.getResponsabledisponible_pid()));
							sesionAspirante.setSesiones_pid(Long.valueOf(rowData.getSesiones_pid()));
							sesionAspirante.setUsername(rowData.getUsername());
							resultInsertSesionAspirante = new SesionesDAO().insertSesionAspirante(sesionAspirante)
							if(!resultInsertSesionAspirante.isSuccess()) {
								throw new Exception("[EXCEPTION] - insertSesionAspirante - "+resultInsertSesionAspirante.getError());
							}
							else {
								pstm = con.prepareStatement(Statements.GET_CASEID_BY_CORREO);
								pstm.setString(1, rowData.getUsername());
								rs = pstm.executeQuery();
								if(rs.next()) {
									lstHumanTaskInstance = context.getApiClient().getProcessAPI().getHumanTaskInstances(rs.getLong("caseid"), "Seleccionar cita", 0, 1);
									if(lstHumanTaskInstance.size()>0) {
										errorInfo +=" | SI SE ENCONTRO UNA TAREA";
										context.getApiClient().getProcessAPI().assignUserTaskIfNotAssigned(lstHumanTaskInstance.get(0).id, context.getApiSession().getUserId());
										context.getApiClient().getProcessAPI().executeUserTask(lstHumanTaskInstance.get(0).id, inputs);
									}
									else {
										throw new Exception("[EXCEPTION] - NO SE ENCONTRO NINGUNA TAREA PARA EL CASO: "+rs.getLong("caseid"));
									}
								}
								else {
									throw new Exception("[EXCEPTION] - NO SE ENCONTRO EL CASO PARA EL USUARIO: "+rowData.getUsername());
								}
							}
						}
						else {
							errorInfo +=" | FALTA INFORMACION---------------------------------------------";
							errorInfo +=" | getUsername="+ rowData.getUsername();
							errorInfo +=" | getSesiones_pid="+ rowData.getSesiones_pid().toString();
							errorInfo +=" | getResponsabledisponible_pid="+ rowData.getResponsabledisponible_pid().toString();
							throw new Exception("[EXCEPTION] - INFORMACION FALTANTE");
						}
					}
				} else {
					errorInfo +=" | NO COINCIDE NINGUN USUARIO------------------------------------";
					errorInfo +=" | username="+lstEntitySesionAspirante.size();
					throw new Exception("[EXCEPTION] - NO COINCIDE NINGUN USUARIO");
				}
				LOGGER.error "4=====================================================================";
			}
			LOGGER.error "5=====================================================================";
			resultado.setError_info(errorInfo);
			resultado.setSuccess(true);
			
		} catch (Exception e) {
			LOGGER.error "6=====================================================================";
			errorInfo +=" | "+e.getMessage();
			resultado.setError(e.getMessage());
			resultado.setError_info(errorInfo);
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
}
