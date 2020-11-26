package com.anahuac.rest.api.DAO

import java.sql.RowId
import java.text.DateFormat
import java.text.SimpleDateFormat

import org.bonitasoft.engine.bpm.data.DataDefinition
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessDefinition
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.bpm.process.ProcessInstanceCriterion
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatNotificacionesDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.model.ProcesoCasoDAO
import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.CatBachilleratosCustom
import com.anahuac.rest.api.Entity.Custom.CatCampusCustom
import com.anahuac.rest.api.Entity.Custom.CatEstadosCustom
import com.anahuac.rest.api.Entity.Custom.CatGestionEscolar
import com.anahuac.rest.api.Entity.Custom.CatLicenciaturaCustom
import com.anahuac.rest.api.Entity.Custom.CatPeriodoCustom
import com.anahuac.rest.api.Entity.Custom.DetalleSolicitudCustom
import com.anahuac.rest.api.Entity.Custom.SolicitudAdmisionCustom
import com.bonitasoft.engine.bpm.parameter.ParameterCriterion
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class ListadoDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ListadoDAO.class);
	
	public Result getNuevasSolicitudes(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<SolicitudAdmisionCustom> lstResultado = new ArrayList<SolicitudAdmisionCustom>();
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
		Integer start = 0;
		Integer end = 99999;
		
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		SolicitudDeAdmision objSolicitudDeAdmision = null;
		SolicitudAdmisionCustom objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
		CatCampusCustom objCatCampusCustom = new CatCampusCustom();
		CatPeriodoCustom objCatPeriodoCustom = new CatPeriodoCustom();
		CatEstadosCustom objCatEstadosCustom = new CatEstadosCustom();
		CatLicenciaturaCustom objCatLicenciaturaCustom = new CatLicenciaturaCustom();
		CatBachilleratosCustom objCatBachilleratosCustom = new CatBachilleratosCustom();
		CatGestionEscolar objCatGestionEscolar = new CatGestionEscolar();

		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		
		DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		Boolean isFound = true;
		Boolean isFoundCampus = false;
		
		String nombreCandidato = "";
		String gestionEscolar = "";
		String catCampusStr = "";
		String catPeriodoStr = "";
		String catEstadoStr = "";
		String catBachilleratoStr = "";
		String strError = "";
		
		ProcessDefinition objProcessDefinition;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			if(object.lstFiltro != null) {
				assert object.lstFiltro instanceof List;
			}

			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Cancún");
			objGrupoCampus.put("valor","CAMPUS-CANCUN");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Mayab");
			objGrupoCampus.put("valor","CAMPUS-MAYAB");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac México Norte");
			objGrupoCampus.put("valor","CAMPUS-MNORTE");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac México Sur");
			objGrupoCampus.put("valor","CAMPUS-MSUR");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Oaxaca");
			objGrupoCampus.put("valor","CAMPUS-OAXACA");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Puebla");
			objGrupoCampus.put("valor","CAMPUS-PUEBLA");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Querétaro");
			objGrupoCampus.put("valor","CAMPUS-QUERETARO");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Xalapa");
			objGrupoCampus.put("valor","CAMPUS-XALAPA");
			lstGrupoCampus.add(objGrupoCampus);
					
			userLogged = context.getApiSession().getUserId();
			
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(Map<String, String> rowGrupo : lstGrupoCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.get("valor"))) {
						lstGrupo.add(rowGrupo.get("descripcion"));
						break;
					}
				}
			}
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def procesoCasoDAO = context.apiClient.getDAO(ProcesoCasoDAO.class);
			//procesoCasoDAO.getCaseId("", "");
			List<HumanTaskInstance> lstHumanTaskInstance = context.getApiClient().getProcessAPI().getAssignedHumanTaskInstances(userLogged, 0, 99999, ActivityInstanceCriterion.DEFAULT);
			List<HumanTaskInstance> lstHumanTaskInstancePending = context.getApiClient().getProcessAPI().getPendingHumanTaskInstances(userLogged, 0, 99999, ActivityInstanceCriterion.DEFAULT);
			for (HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstancePending) {
				lstHumanTaskInstance.add(objHumanTaskInstance);
			}
			
			for(HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstance) {
				
				objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
				objCatCampusCustom = new CatCampusCustom();
				if(objHumanTaskInstance.getName().equals(object.tarea)) {
					
					
					caseId = objHumanTaskInstance.getRootContainerId();
					lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end);
					if(!lstSolicitudDeAdmision.empty){
						objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end).get(0);
					
						catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
						isFoundCampus = false;
						for(String objGrupo : lstGrupo) {
							if(catCampusStr.toLowerCase().equals(objGrupo.toLowerCase())) {
								isFoundCampus = true;
							}
						}
						if(isFoundCampus) {
							if(object.lstFiltro != null) {
								for(def row: object.lstFiltro) {
									isFound = false;
									assert row instanceof Map;
								
									nombreCandidato = objSolicitudDeAdmision.getPrimerNombre()+" "+objSolicitudDeAdmision.getSegundoNombre()+" "+objSolicitudDeAdmision.getApellidoPaterno()+" "+objSolicitudDeAdmision.getApellidoMaterno();

									if(row.operador == 'Que contenga') {
										if (row.columna == "NOMBRE") {
											if (nombreCandidato.toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "EMAIL") {
											if (objSolicitudDeAdmision.getCorreoElectronico().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "CURP") {
											if (objSolicitudDeAdmision.getCurp().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "CAMPUS") {
											catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
											if(catCampusStr.toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PROGRAMA") {
											gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null ? "" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
											if(gestionEscolar.contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "INGRESO") {
											catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null ? "" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
											if(catPeriodoStr.toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "ESTADO") {
											catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
											if(catEstadoStr.toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PREPARATORIA") {
											catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null ? "" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
											if(catBachilleratoStr.toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PROMEDIO") {
											if(objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "ESTATUS") {
											if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "SOLICITUD") {
											if(objSolicitudDeAdmision.getCaseId().toString().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "TELÉFONO") {
											if(objSolicitudDeAdmision.getTelefonoCelular().toString().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PROCEDENCIA") {
											catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
											if(catEstadoStr.toString().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "FECHA DE ENVIO") {
											if(dfSalida.format(objHumanTaskInstance.getLastUpdateDate()).toString().toLowerCase().contains(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										}
									}
									else {
										if (row.columna == "NOMBRE") {
											if (nombreCandidato.toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "EMAIL") {
											if (objSolicitudDeAdmision.getCorreoElectronico().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "CURP") {
											if (objSolicitudDeAdmision.getCurp().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "CAMPUS") {
											catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
											if(catCampusStr.toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PROGRAMA") {
											gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null ? "" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
											if(gestionEscolar.equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "INGRESO") {
											catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null ? "" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
											if(catPeriodoStr.toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "ESTADO") {
											catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
											if(catEstadoStr.toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PREPARATORIA") {
											catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null ? "" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
											if(catBachilleratoStr.toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PROMEDIO") {
											if(objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "ESTATUS") {
											if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "SOLICITUD") {
											if(objSolicitudDeAdmision.getCaseId().toString().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "TELÉFONO") {
											if(objSolicitudDeAdmision.getTelefonoCelular().toString().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "PROCEDENCIA") {
											catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
											if(catEstadoStr.toString().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										} else
										if (row.columna == "FECHA DE ENVIO") {
											if(dfSalida.format(objHumanTaskInstance.getLastUpdateDate()).toString().toLowerCase().equals(row.valor.toLowerCase())) {
												isFound = true;
												break;
											}
										}
										
									}
								}
							}
						}
						else {
							isFound = false;
						}
						
						if(isFound) {
							String encoded = "";
							for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(objSolicitudDeAdmision.getCaseId(), "fotoPasaporte", 0, 10)) {
								encoded ="data:image/png;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
								//LOGGER.error encoded;
							}
							
							//objSolicitudAdmisionCustom.setUpdateDate(updateDate);
							
							objProcessDefinition = context.getApiClient().getProcessAPI().getProcessDefinition(objHumanTaskInstance.getProcessDefinitionId());
							objSolicitudAdmisionCustom.setTaskName(objHumanTaskInstance.getName());
							objSolicitudAdmisionCustom.setUpdateDate(dfSalida.format(objHumanTaskInstance.getLastUpdateDate()));
							objSolicitudAdmisionCustom.setProcessName(objProcessDefinition.getName());
							objSolicitudAdmisionCustom.setProcessVersion(objProcessDefinition.getVersion());
							objSolicitudAdmisionCustom.setPersistenceId(objSolicitudDeAdmision.getPersistenceId());
							objSolicitudAdmisionCustom.setPersistenceVersion(objSolicitudDeAdmision.getPersistenceVersion());
							objSolicitudAdmisionCustom.setCaseId(objSolicitudDeAdmision.getCaseId());
							objSolicitudAdmisionCustom.setPrimerNombre(objSolicitudDeAdmision.getPrimerNombre());
							objSolicitudAdmisionCustom.setSegundoNombre(objSolicitudDeAdmision.getSegundoNombre());
							objSolicitudAdmisionCustom.setApellidoPaterno(objSolicitudDeAdmision.getApellidoPaterno());
							objSolicitudAdmisionCustom.setApellidoMaterno(objSolicitudDeAdmision.getApellidoMaterno());
							objSolicitudAdmisionCustom.setCorreoElectronico(objSolicitudDeAdmision.getCorreoElectronico());
							objSolicitudAdmisionCustom.setCurp(objSolicitudDeAdmision.getCurp());
							objSolicitudAdmisionCustom.setUsuarioFacebook(objSolicitudDeAdmision.getUsuarioFacebook());
							objSolicitudAdmisionCustom.setUsiarioTwitter(objSolicitudDeAdmision.getUsiarioTwitter());
							objSolicitudAdmisionCustom.setUsuarioInstagram(objSolicitudDeAdmision.getUsuarioInstagram());
							objSolicitudAdmisionCustom.setTelefonoCelular(objSolicitudDeAdmision.getTelefonoCelular());
							objSolicitudAdmisionCustom.setFoto(objSolicitudDeAdmision.getFoto());
							objSolicitudAdmisionCustom.setActaNacimiento(objSolicitudDeAdmision.getActaNacimiento());
							objSolicitudAdmisionCustom.setCalle(objSolicitudDeAdmision.getCalle());
							objSolicitudAdmisionCustom.setCodigoPostal(objSolicitudDeAdmision.getCodigoPostal());
							objSolicitudAdmisionCustom.setCiudad(objSolicitudDeAdmision.getCiudad());
							objSolicitudAdmisionCustom.setCalle2(objSolicitudDeAdmision.getCalle2());
							objSolicitudAdmisionCustom.setNumExterior(objSolicitudDeAdmision.getNumExterior());
							objSolicitudAdmisionCustom.setNumInterior(objSolicitudDeAdmision.getNumInterior());
							objSolicitudAdmisionCustom.setColonia(objSolicitudDeAdmision.getColonia());
							objSolicitudAdmisionCustom.setTelefono(objSolicitudDeAdmision.getTelefono());
							objSolicitudAdmisionCustom.setOtroTelefonoContacto(objSolicitudDeAdmision.getOtroTelefonoContacto());
							objSolicitudAdmisionCustom.setPromedioGeneral(objSolicitudDeAdmision.getPromedioGeneral());
							objSolicitudAdmisionCustom.setComprobanteCalificaciones(objSolicitudDeAdmision.getComprobanteCalificaciones());
							//objSolicitudAdmisionCustom.setCiudadExamen(objSolicitudDeAdmision.getCiudadExamen());
							objSolicitudAdmisionCustom.setTaskId(objHumanTaskInstance.getId());
							
														
							objCatGestionEscolar = new CatGestionEscolar();
							if(objSolicitudDeAdmision.getCatGestionEscolar() != null ){
								strError = strError+", " + "if(objSolicitudDeAdmision.getCatGestionEscolar() != null ){";
								objCatGestionEscolar.setPersistenceId(objSolicitudDeAdmision.getCatGestionEscolar().getPersistenceId());
								objCatGestionEscolar.setPersistenceVersion(objSolicitudDeAdmision.getCatGestionEscolar().getPersistenceVersion());
								objCatGestionEscolar.setNombre(objSolicitudDeAdmision.getCatGestionEscolar().getNombre());
								objCatGestionEscolar.setDescripcion(objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion());
								objCatGestionEscolar.setEnlace(objSolicitudDeAdmision.getCatGestionEscolar().getEnlace());
								objCatGestionEscolar.setPropedeutico(objSolicitudDeAdmision.getCatGestionEscolar().isPropedeutico());
								objCatGestionEscolar.setProgramaparcial(objSolicitudDeAdmision.getCatGestionEscolar().isProgramaparcial());
								objCatGestionEscolar.setMatematicas(objSolicitudDeAdmision.getCatGestionEscolar().isMatematicas());
								objCatGestionEscolar.setInscripcionenero(objSolicitudDeAdmision.getCatGestionEscolar().getInscripcionenero());
								objCatGestionEscolar.setInscripcionagosto(objSolicitudDeAdmision.getCatGestionEscolar().getInscripcionagosto());
								objCatGestionEscolar.setIsEliminado(objSolicitudDeAdmision.getCatGestionEscolar().isIsEliminado());
								objCatGestionEscolar.setUsuarioCreacion(objSolicitudDeAdmision.getCatGestionEscolar().getUsuarioCreacion());
								objCatGestionEscolar.setCampus(objSolicitudDeAdmision.getCatGestionEscolar().getCampus());
								objCatGestionEscolar.setCaseId(objSolicitudDeAdmision.getCatGestionEscolar().getCaseId());
								strError = strError+", " + "end if(objSolicitudDeAdmision.getCatGestionEscolar() != null ){";
							}
							objSolicitudAdmisionCustom.setObjCatGestionEscolar(objCatGestionEscolar);
							
							objCatCampusCustom = new CatCampusCustom();
							if(objSolicitudDeAdmision.getCatCampus() != null){
								strError = strError+", " + "if(objSolicitudDeAdmision.getCatCampus() != null){";
								objCatCampusCustom.setPersistenceId(objSolicitudDeAdmision.getCatCampus().getPersistenceId());
								objCatCampusCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatCampus().getPersistenceVersion());
								objCatCampusCustom.setDescripcion(objSolicitudDeAdmision.getCatCampus().getDescripcion());
								objCatCampusCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatCampus().getUsuarioBanner());
								objCatCampusCustom.setClave(objSolicitudDeAdmision.getCatCampus().getClave());
								strError = strError+", " + "end if(objSolicitudDeAdmision.getCatCampus() != null){";
							}
							objSolicitudAdmisionCustom.setCatCampus(objCatCampusCustom);
							
							objCatPeriodoCustom = new CatPeriodoCustom();
							if(objSolicitudDeAdmision.getCatPeriodo() != null){
								strError = strError+", " + "if(objSolicitudDeAdmision.getCatPeriodo() != null){";
								objCatPeriodoCustom.setPersistenceId(objSolicitudDeAdmision.getCatPeriodo().getPersistenceId());
								objCatPeriodoCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatPeriodo().getPersistenceVersion());
								objCatPeriodoCustom.setDescripcion(objSolicitudDeAdmision.getCatPeriodo().getDescripcion());
								objCatPeriodoCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatPeriodo().getUsuarioBanner());
								objCatPeriodoCustom.setClave(objSolicitudDeAdmision.getCatPeriodo().getClave());
								strError = strError+", " + "end if(objSolicitudDeAdmision.getCatPeriodo() != null){";
							}
							objSolicitudAdmisionCustom.setCatPeriodo(objCatPeriodoCustom);
							
							objCatEstadosCustom = new CatEstadosCustom();
							if(objSolicitudDeAdmision.getCatEstado() != null){
								strError = strError+", " + "if(objSolicitudDeAdmision.getCatEstado() != null){";
								objCatEstadosCustom.setPersistenceId(objSolicitudDeAdmision.getCatEstado().getPersistenceId());
								objCatEstadosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatEstado().getPersistenceVersion());
								objCatEstadosCustom.setClave(objSolicitudDeAdmision.getCatEstado().getClave());
								objCatEstadosCustom.setDescripcion(objSolicitudDeAdmision.getCatEstado().getDescripcion());
								objCatEstadosCustom.setUsuarioCreacion(objSolicitudDeAdmision.getCatEstado().getUsuarioCreacion());
								strError = strError+", " + "end if(objSolicitudDeAdmision.getCatEstado() != null){";
							}
							objSolicitudAdmisionCustom.setCatEstado(objCatEstadosCustom);
							objSolicitudAdmisionCustom.setCatLicenciatura(objCatLicenciaturaCustom);
							
							objCatBachilleratosCustom = new CatBachilleratosCustom();
							if(objSolicitudDeAdmision.getCatBachilleratos() != null){
								strError = strError+", " + "if(objSolicitudDeAdmision.getCatBachilleratos() != null){";
								objCatBachilleratosCustom.setPersistenceId(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceId());
								objCatBachilleratosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceVersion());
								objCatBachilleratosCustom.setClave(objSolicitudDeAdmision.getCatBachilleratos().getClave());
								objCatBachilleratosCustom.setDescripcion(objSolicitudDeAdmision.getCatBachilleratos().getDescripcion());
								objCatBachilleratosCustom.setPais(objSolicitudDeAdmision.getCatBachilleratos().getPais());
								objCatBachilleratosCustom.setEstado(objSolicitudDeAdmision.getCatBachilleratos().getEstado());
								objCatBachilleratosCustom.setCiudad(objSolicitudDeAdmision.getCatBachilleratos().getCiudad());
								strError = strError+", " + "end if(objSolicitudDeAdmision.getCatBachilleratos() != null){";
							}
							objSolicitudAdmisionCustom.setCatBachilleratos(objCatBachilleratosCustom);

							objSolicitudAdmisionCustom.setFotografiaB64(encoded);
							lstResultado.add(objSolicitudAdmisionCustom);
						}
					}
				}
				else {
				}
			}
			resultado.setError_info(strError);
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setError_info(strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result getAspirantesProceso(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<SolicitudAdmisionCustom> lstResultado = new ArrayList<SolicitudAdmisionCustom>();
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
		Integer start = 0;
		Integer end = 99999;
		
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		List<SolicitudDeAdmision> lstAllSolicitud = new ArrayList<SolicitudDeAdmision>();
		SolicitudDeAdmision objSolicitudDeAdmision = null;
		SolicitudAdmisionCustom objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
		CatCampusCustom objCatCampusCustom = new CatCampusCustom();
		CatPeriodoCustom objCatPeriodoCustom = new CatPeriodoCustom();
		CatEstadosCustom objCatEstadosCustom = new CatEstadosCustom();
		CatLicenciaturaCustom objCatLicenciaturaCustom = new CatLicenciaturaCustom();
		CatBachilleratosCustom objCatBachilleratosCustom = new CatBachilleratosCustom();
		CatGestionEscolar objCatGestionEscolar = new CatGestionEscolar();
		DetalleSolicitudCustom objDetalleSolicitud = new DetalleSolicitudCustom();
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		
		Boolean isFound = true;
		Boolean isFoundCampus = false;
		
		String nombreCandidato = "";
		String nombreProceso = "";
		
		String gestionEscolar = "";
		String catCampusStr = "";
		String catPeriodoStr = "";
		String catEstadoStr = "";
		String catBachilleratoStr = "";
		
		ProcessDefinition objProcessDefinition;
		
		DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			if(object.lstFiltro != null) {
				assert object.lstFiltro instanceof List;
			}

			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Cancún");
			objGrupoCampus.put("valor","CAMPUS-CANCUN");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Mayab");
			objGrupoCampus.put("valor","CAMPUS-MAYAB");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac México Norte");
			objGrupoCampus.put("valor","CAMPUS-MNORTE");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac México Sur");
			objGrupoCampus.put("valor","CAMPUS-MSUR");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Oaxaca");
			objGrupoCampus.put("valor","CAMPUS-OAXACA");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Puebla");
			objGrupoCampus.put("valor","CAMPUS-PUEBLA");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Querétaro");
			objGrupoCampus.put("valor","CAMPUS-QUERETARO");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Xalapa");
			objGrupoCampus.put("valor","CAMPUS-XALAPA");
			lstGrupoCampus.add(objGrupoCampus);
					
			userLogged = context.getApiSession().getUserId();
			
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(Map<String, String> rowGrupo : lstGrupoCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.get("valor"))) {
						lstGrupo.add(rowGrupo.get("descripcion"));
						break;
					}
				}
			}
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def procesoCasoDAO = context.apiClient.getDAO(ProcesoCasoDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			
			//context.getApiClient().getProcessAPI().getHumanTaskInstances(rootProcessInstanceId, taskName, startIndex, maxResults)
			LOGGER.error "--------------------------------------------------------------------------------------------------"
			/*for(ProcessInstance objProcessInstance : lstProcessInstance) {
				objProcessInstance = lstProcessInstance.get(0).getState();
				LOGGER.error "RootProcessInstance "+objProcessInstance.getRootProcessInstanceId();
			}*/
			LOGGER.error "--------------------------------------------------------------------------------------------------"			
			

			lstAllSolicitud = objSolicitudDeAdmisionDAO.find(0, 99999);
			
			for(SolicitudDeAdmision objAllSolicitud : lstAllSolicitud) {
				objAllSolicitud.getCaseId()
			}
			
			Long processDefinitionId = context.getApiClient().getProcessAPI().getProcessDefinitionId("Proceso admisiones", "1.0");
			
			
			final SearchOptions searchOptions = new SearchOptionsBuilder(0, 99999).filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, processDefinitionId.toString()).done();
			SearchResult<HumanTaskInstance>  SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
			List<HumanTaskInstance> lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
			
			for(HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstanceSearch) {
				
				LOGGER.error "--------------------------------------------------------------------------------------------------"
				LOGGER.error objHumanTaskInstance.getName()
				LOGGER.error "--------------------------------------------------------------------------------------------------"
				
				objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
				objCatCampusCustom = new CatCampusCustom();
				 
				caseId = objHumanTaskInstance.getRootContainerId();
				lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end);
				lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(caseId.toString(), start, end);
				if(!lstDetalleSolicitud.empty)
				{
					LOGGER.error "--------------------------------------------------------------------------------------------------"
					LOGGER.error "entra"
					LOGGER.error "--------------------------------------------------------------------------------------------------"
					
					objDetalleSolicitud = new DetalleSolicitudCustom();
					objDetalleSolicitud.setPersistenceId(lstDetalleSolicitud.get(0).getPersistenceId());
					objDetalleSolicitud.setPersistenceVersion(lstDetalleSolicitud.get(0).getPersistenceVersion());
					objDetalleSolicitud.setIdBanner(lstDetalleSolicitud.get(0).getIdBanner());
					objDetalleSolicitud.setIsCurpValidado(lstDetalleSolicitud.get(0).isIsCurpValidado());
					objDetalleSolicitud.setPromedioCoincide(lstDetalleSolicitud.get(0).isPromedioCoincide());
					objDetalleSolicitud.setRevisado(lstDetalleSolicitud.get(0).isRevisado());
					objDetalleSolicitud.setTipoAlumno(lstDetalleSolicitud.get(0).getTipoAlumno());
					objDetalleSolicitud.setDescuento(lstDetalleSolicitud.get(0).getDescuento());
					objDetalleSolicitud.setObservacionesDescuento(lstDetalleSolicitud.get(0).getObservacionesDescuento());
					objDetalleSolicitud.setObservacionesCambio(lstDetalleSolicitud.get(0).getObservacionesCambio());
					objDetalleSolicitud.setObservacionesRechazo(lstDetalleSolicitud.get(0).getObservacionesRechazo());
					objDetalleSolicitud.setObservacionesListaRoja(lstDetalleSolicitud.get(0).getObservacionesListaRoja());
					objDetalleSolicitud.setOrdenPago(lstDetalleSolicitud.get(0).getOrdenPago());
					objDetalleSolicitud.setCaseId(lstDetalleSolicitud.get(0).getCaseId());
					objSolicitudAdmisionCustom.setObjDetalleSolicitud(objDetalleSolicitud);
					
				}
				LOGGER.error "--------------------------------------------------------------------------------------------------"
				LOGGER.error "sale"
				LOGGER.error "--------------------------------------------------------------------------------------------------"
				
				
				if(!lstSolicitudDeAdmision.empty){
					objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end).get(0);
					
					catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
					isFoundCampus = false;
					for(String objGrupo : lstGrupo) {
						if(catCampusStr.toLowerCase().equals(objGrupo.toLowerCase())) {
							isFoundCampus = true;
						}
					}
					if(isFoundCampus) {
						if(object.lstFiltro != null) {
							for(def row: object.lstFiltro) {
								isFound = false;
								assert row instanceof Map;
							
								nombreCandidato = objSolicitudDeAdmision.getPrimerNombre()+" "+objSolicitudDeAdmision.getSegundoNombre()+" "+objSolicitudDeAdmision.getApellidoPaterno()+" "+objSolicitudDeAdmision.getApellidoMaterno();
																
								
								if(row.operador == 'Que contenga') {
									if (row.columna == "NOMBRE") {
										if (nombreCandidato.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "EMAIL") {
										if (objSolicitudDeAdmision.getCorreoElectronico().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CURP") {
										if (objSolicitudDeAdmision.getCurp().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CAMPUS") {
										catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
										if(catCampusStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROGRAMA") {
										gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null ? "" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
										if(gestionEscolar.contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "INGRESO") {
										catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null ? "" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
										if(catPeriodoStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTADO") {
										catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
										if(catEstadoStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PREPARATORIA") {
										catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null ? "" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
										if(catBachilleratoStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROMEDIO") {
										if(objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTATUS") {
										if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ÚLTIMA MODIFICACION") {
										if(dfSalida.format(objHumanTaskInstance.getReachedStateDate()).toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "TIPO") {
										if(objDetalleSolicitud.getTipoAlumno().toString().toLowerCase().contains(row.valor.toString().toLowerCase())) {
											isFound = true;
											break;
										}
									}
									
									//
									
								}
								else {
									if (row.columna == "NOMBRE") {
										if (nombreCandidato.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "EMAIL") {
										if (objSolicitudDeAdmision.getCorreoElectronico().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CURP") {
										if (objSolicitudDeAdmision.getCurp().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CAMPUS") {
										catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
										if(catCampusStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROGRAMA") {
										gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null ? "" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
										if(gestionEscolar.equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "INGRESO") {
										catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null ? "" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
										if(catPeriodoStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTADO") {
										catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
										if(catEstadoStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PREPARATORIA") {
										catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null ? "" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
										if(catBachilleratoStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROMEDIO") {
										if(objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTATUS") {
										if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ÚLTIMA MODIFICACION") {
										if(dfSalida.format(objHumanTaskInstance.getReachedStateDate()).toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "TIPO") {
										if(objDetalleSolicitud.getTipoAlumno().toString().toLowerCase().equals(row.valor.toString().toLowerCase())) {
											isFound = true;
											break;
										}
									}
									
								}
							}
						}
					}
					else {
						isFound = false;
					}
					
					if(isFound) {
						String encoded = "";
						for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(objSolicitudDeAdmision.getCaseId(), "fotoPasaporte", 0, 10)) {
							encoded ="data:image/png;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
							//LOGGER.error encoded;
						}
						objProcessDefinition = context.getApiClient().getProcessAPI().getProcessDefinition(objHumanTaskInstance.getProcessDefinitionId());
						objSolicitudAdmisionCustom.setTaskName(objHumanTaskInstance.getName());
						objSolicitudAdmisionCustom.setProcessName(objProcessDefinition.getName());
						objSolicitudAdmisionCustom.setProcessVersion(objProcessDefinition.getVersion());
						objSolicitudAdmisionCustom.setPersistenceId(objSolicitudDeAdmision.getPersistenceId());
						objSolicitudAdmisionCustom.setPersistenceVersion(objSolicitudDeAdmision.getPersistenceVersion());
						objSolicitudAdmisionCustom.setCaseId(objSolicitudDeAdmision.getCaseId());
						objSolicitudAdmisionCustom.setPrimerNombre(objSolicitudDeAdmision.getPrimerNombre());
						objSolicitudAdmisionCustom.setSegundoNombre(objSolicitudDeAdmision.getSegundoNombre());
						objSolicitudAdmisionCustom.setApellidoPaterno(objSolicitudDeAdmision.getApellidoPaterno());
						objSolicitudAdmisionCustom.setApellidoMaterno(objSolicitudDeAdmision.getApellidoMaterno());
						objSolicitudAdmisionCustom.setCorreoElectronico(objSolicitudDeAdmision.getCorreoElectronico());
						objSolicitudAdmisionCustom.setCurp(objSolicitudDeAdmision.getCurp());
						objSolicitudAdmisionCustom.setUsuarioFacebook(objSolicitudDeAdmision.getUsuarioFacebook());
						objSolicitudAdmisionCustom.setUsiarioTwitter(objSolicitudDeAdmision.getUsiarioTwitter());
						objSolicitudAdmisionCustom.setUsuarioInstagram(objSolicitudDeAdmision.getUsuarioInstagram());
						objSolicitudAdmisionCustom.setTelefonoCelular(objSolicitudDeAdmision.getTelefonoCelular());
						objSolicitudAdmisionCustom.setFoto(objSolicitudDeAdmision.getFoto());
						objSolicitudAdmisionCustom.setActaNacimiento(objSolicitudDeAdmision.getActaNacimiento());
						objSolicitudAdmisionCustom.setCalle(objSolicitudDeAdmision.getCalle());
						objSolicitudAdmisionCustom.setCodigoPostal(objSolicitudDeAdmision.getCodigoPostal());
						objSolicitudAdmisionCustom.setCiudad(objSolicitudDeAdmision.getCiudad());
						objSolicitudAdmisionCustom.setCalle2(objSolicitudDeAdmision.getCalle2());
						objSolicitudAdmisionCustom.setNumExterior(objSolicitudDeAdmision.getNumExterior());
						objSolicitudAdmisionCustom.setNumInterior(objSolicitudDeAdmision.getNumInterior());
						objSolicitudAdmisionCustom.setColonia(objSolicitudDeAdmision.getColonia());
						objSolicitudAdmisionCustom.setTelefono(objSolicitudDeAdmision.getTelefono());
						objSolicitudAdmisionCustom.setOtroTelefonoContacto(objSolicitudDeAdmision.getOtroTelefonoContacto());
						objSolicitudAdmisionCustom.setPromedioGeneral(objSolicitudDeAdmision.getPromedioGeneral());
						objSolicitudAdmisionCustom.setComprobanteCalificaciones(objSolicitudDeAdmision.getComprobanteCalificaciones());
						//objSolicitudAdmisionCustom.setCiudadExamen(objSolicitudDeAdmision.getCiudadExamen());
						objSolicitudAdmisionCustom.setTaskId(objHumanTaskInstance.getId());
						
													
						objCatGestionEscolar = new CatGestionEscolar();
						if(objSolicitudDeAdmision.getCatGestionEscolar() != null ){
							objCatGestionEscolar.setPersistenceId(objSolicitudDeAdmision.getCatGestionEscolar().getPersistenceId());
							objCatGestionEscolar.setPersistenceVersion(objSolicitudDeAdmision.getCatGestionEscolar().getPersistenceVersion());
							objCatGestionEscolar.setNombre(objSolicitudDeAdmision.getCatGestionEscolar().getNombre());
							objCatGestionEscolar.setDescripcion(objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion());
							objCatGestionEscolar.setEnlace(objSolicitudDeAdmision.getCatGestionEscolar().getEnlace());
							objCatGestionEscolar.setPropedeutico(objSolicitudDeAdmision.getCatGestionEscolar().isPropedeutico());
							objCatGestionEscolar.setProgramaparcial(objSolicitudDeAdmision.getCatGestionEscolar().isProgramaparcial());
							objCatGestionEscolar.setMatematicas(objSolicitudDeAdmision.getCatGestionEscolar().isMatematicas());
							objCatGestionEscolar.setInscripcionenero(objSolicitudDeAdmision.getCatGestionEscolar().getInscripcionenero());
							objCatGestionEscolar.setInscripcionagosto(objSolicitudDeAdmision.getCatGestionEscolar().getInscripcionagosto());
							objCatGestionEscolar.setIsEliminado(objSolicitudDeAdmision.getCatGestionEscolar().isIsEliminado());
							objCatGestionEscolar.setUsuarioCreacion(objSolicitudDeAdmision.getCatGestionEscolar().getUsuarioCreacion());
							objCatGestionEscolar.setCampus(objSolicitudDeAdmision.getCatGestionEscolar().getCampus());
							objCatGestionEscolar.setCaseId(objSolicitudDeAdmision.getCatGestionEscolar().getCaseId());
						}
						objSolicitudAdmisionCustom.setObjCatGestionEscolar(objCatGestionEscolar);
						
						objCatCampusCustom = new CatCampusCustom();
						if(objSolicitudDeAdmision.getCatCampus() != null){
							objCatCampusCustom.setPersistenceId(objSolicitudDeAdmision.getCatCampus().getPersistenceId());
							objCatCampusCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatCampus().getPersistenceVersion());
							objCatCampusCustom.setDescripcion(objSolicitudDeAdmision.getCatCampus().getDescripcion());
							objCatCampusCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatCampus().getUsuarioBanner());
							objCatCampusCustom.setClave(objSolicitudDeAdmision.getCatCampus().getClave());
						}
						objSolicitudAdmisionCustom.setCatCampus(objCatCampusCustom);
						
						objCatPeriodoCustom = new CatPeriodoCustom();
						if(objSolicitudDeAdmision.getCatPeriodo() != null){
							objCatPeriodoCustom.setPersistenceId(objSolicitudDeAdmision.getCatPeriodo().getPersistenceId());
							objCatPeriodoCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatPeriodo().getPersistenceVersion());
							objCatPeriodoCustom.setDescripcion(objSolicitudDeAdmision.getCatPeriodo().getDescripcion());
							objCatPeriodoCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatPeriodo().getUsuarioBanner());
							objCatPeriodoCustom.setClave(objSolicitudDeAdmision.getCatPeriodo().getClave());
						}
						objSolicitudAdmisionCustom.setCatPeriodo(objCatPeriodoCustom);
						
						objCatEstadosCustom = new CatEstadosCustom();
						if(objSolicitudDeAdmision.getCatEstado() != null){
							objCatEstadosCustom.setPersistenceId(objSolicitudDeAdmision.getCatEstado().getPersistenceId());
							objCatEstadosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatEstado().getPersistenceVersion());
							objCatEstadosCustom.setClave(objSolicitudDeAdmision.getCatEstado().getClave());
							objCatEstadosCustom.setDescripcion(objSolicitudDeAdmision.getCatEstado().getDescripcion());
							objCatEstadosCustom.setUsuarioCreacion(objSolicitudDeAdmision.getCatEstado().getUsuarioCreacion());
						}
						objSolicitudAdmisionCustom.setCatEstado(objCatEstadosCustom);
						objSolicitudAdmisionCustom.setCatLicenciatura(objCatLicenciaturaCustom);
						
						objCatBachilleratosCustom = new CatBachilleratosCustom();
						if(objSolicitudDeAdmision.getCatBachilleratos() != null){
							objCatBachilleratosCustom.setPersistenceId(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceId());
							objCatBachilleratosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceVersion());
							objCatBachilleratosCustom.setClave(objSolicitudDeAdmision.getCatBachilleratos().getClave());
							objCatBachilleratosCustom.setDescripcion(objSolicitudDeAdmision.getCatBachilleratos().getDescripcion());
							objCatBachilleratosCustom.setPais(objSolicitudDeAdmision.getCatBachilleratos().getPais());
							objCatBachilleratosCustom.setEstado(objSolicitudDeAdmision.getCatBachilleratos().getEstado());
							objCatBachilleratosCustom.setCiudad(objSolicitudDeAdmision.getCatBachilleratos().getCiudad());
						}
						objSolicitudAdmisionCustom.setCatBachilleratos(objCatBachilleratosCustom);
						
						objSolicitudAdmisionCustom.setFotografiaB64(encoded);
						objSolicitudAdmisionCustom.setEstatusSolicitud(objSolicitudDeAdmision.getEstatusSolicitud());
						objSolicitudAdmisionCustom.setUpdateDate(dfSalida.format(objHumanTaskInstance.getReachedStateDate()));
						
						lstResultado.add(objSolicitudAdmisionCustom);
					}
				}
			}
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result getDocumentoTest(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Long caseId =11001L;
		try {
				
			for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(caseId, "DocInformacionDocumento", 0, 10)) {
				String encoded ="data:image/png;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
			}
			
			
			
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result getAspirantesByStatus(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<SolicitudAdmisionCustom> lstResultado = new ArrayList<SolicitudAdmisionCustom>();
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		List<SolicitudDeAdmision> lstAllSolicitud = new ArrayList<SolicitudDeAdmision>();
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
		Integer start = 0;
		Integer end = 99999;
		

		SolicitudDeAdmision objSolicitudDeAdmision = null;
		SolicitudAdmisionCustom objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
		CatCampusCustom objCatCampusCustom = new CatCampusCustom();
		CatPeriodoCustom objCatPeriodoCustom = new CatPeriodoCustom();
		CatEstadosCustom objCatEstadosCustom = new CatEstadosCustom();
		CatLicenciaturaCustom objCatLicenciaturaCustom = new CatLicenciaturaCustom();
		CatBachilleratosCustom objCatBachilleratosCustom = new CatBachilleratosCustom();
		CatGestionEscolar objCatGestionEscolar = new CatGestionEscolar();
		DetalleSolicitudCustom objDetalleSolicitud = new DetalleSolicitudCustom();
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		
		Boolean isFound = true;
		Boolean isFoundCampus = false;
		
		String nombreCandidato = "";
		String nombreProceso = "";
		String estatusSolicitud = "";
		
		String gestionEscolar = "";
		String catCampusStr = "";
		String catPeriodoStr = "";
		String catEstadoStr = "";
		String catBachilleratoStr = "";		
		String strError = "";
		ProcessDefinition objProcessDefinition;
		
		DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			estatusSolicitud = object.estatusSolicitud
			
			assert object instanceof Map;
			if(object.lstFiltro != null) {
				assert object.lstFiltro instanceof List;
			}

			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Cancún");
			objGrupoCampus.put("valor","CAMPUS-CANCUN");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Mayab");
			objGrupoCampus.put("valor","CAMPUS-MAYAB");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac México Norte");
			objGrupoCampus.put("valor","CAMPUS-MNORTE");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac México Sur");
			objGrupoCampus.put("valor","CAMPUS-MSUR");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Oaxaca");
			objGrupoCampus.put("valor","CAMPUS-OAXACA");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Puebla");
			objGrupoCampus.put("valor","CAMPUS-PUEBLA");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Querétaro");
			objGrupoCampus.put("valor","CAMPUS-QUERETARO");
			lstGrupoCampus.add(objGrupoCampus);
		
			objGrupoCampus = new HashMap<String, String>();
			objGrupoCampus.put("descripcion","Anáhuac Xalapa");
			objGrupoCampus.put("valor","CAMPUS-XALAPA");
			lstGrupoCampus.add(objGrupoCampus);
					
			userLogged = context.getApiSession().getUserId();
			LOGGER.error userLogged+""; 
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(Map<String, String> rowGrupo : lstGrupoCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.get("valor"))) {
						lstGrupo.add(rowGrupo.get("descripcion"));
						break;
					}
				}
			}
			LOGGER.error "for mebership";
			
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def procesoCasoDAO = context.apiClient.getDAO(ProcesoCasoDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			
			//context.getApiClient().getProcessAPI().getHumanTaskInstances(rootProcessInstanceId, taskName, startIndex, maxResults)
			LOGGER.error "--------------------------------------------------------------------------------------------------"
			/*for(ProcessInstance objProcessInstance : lstProcessInstance) {
				objProcessInstance = lstProcessInstance.get(0).getState();
				LOGGER.error "RootProcessInstance "+objProcessInstance.getRootProcessInstanceId();
			}*/
			LOGGER.error "--------------------------------------------------------------------------------------------------"
			

			lstAllSolicitud = objSolicitudDeAdmisionDAO.find(0, 99999);
			
			for(SolicitudDeAdmision objAllSolicitud : lstAllSolicitud) {
				objAllSolicitud.getCaseId()
			}
			
			Long processDefinitionId = context.getApiClient().getProcessAPI().getProcessDefinitionId("Proceso admisiones", "1.0");
			
			
			final SearchOptions searchOptions = new SearchOptionsBuilder(0, 99999).filter(HumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID, processDefinitionId.toString()).done();
			SearchResult<HumanTaskInstance>  SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
			List<HumanTaskInstance> lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();

			for(HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstanceSearch) {
								
				objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
				objCatCampusCustom = new CatCampusCustom();
				 
				caseId = objHumanTaskInstance.getRootContainerId();
				lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end);
				
				lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(caseId.toString(), start, end);
				if(!lstDetalleSolicitud.empty)
				{
					objDetalleSolicitud = new DetalleSolicitudCustom();
					objDetalleSolicitud.setPersistenceId(lstDetalleSolicitud.get(0).getPersistenceId());
					objDetalleSolicitud.setPersistenceVersion(lstDetalleSolicitud.get(0).getPersistenceVersion());
					objDetalleSolicitud.setIdBanner(lstDetalleSolicitud.get(0).getIdBanner());
					objDetalleSolicitud.setIsCurpValidado(lstDetalleSolicitud.get(0).isIsCurpValidado());
					objDetalleSolicitud.setPromedioCoincide(lstDetalleSolicitud.get(0).isPromedioCoincide());
					objDetalleSolicitud.setRevisado(lstDetalleSolicitud.get(0).isRevisado());
					objDetalleSolicitud.setTipoAlumno(lstDetalleSolicitud.get(0).getTipoAlumno());
					objDetalleSolicitud.setDescuento(lstDetalleSolicitud.get(0).getDescuento());
					objDetalleSolicitud.setObservacionesDescuento(lstDetalleSolicitud.get(0).getObservacionesDescuento());
					objDetalleSolicitud.setObservacionesCambio(lstDetalleSolicitud.get(0).getObservacionesCambio());
					objDetalleSolicitud.setObservacionesRechazo(lstDetalleSolicitud.get(0).getObservacionesRechazo());
					objDetalleSolicitud.setObservacionesListaRoja(lstDetalleSolicitud.get(0).getObservacionesListaRoja());
					objDetalleSolicitud.setOrdenPago(lstDetalleSolicitud.get(0).getOrdenPago());
					objDetalleSolicitud.setCaseId(lstDetalleSolicitud.get(0).getCaseId());
					objSolicitudAdmisionCustom.setObjDetalleSolicitud(objDetalleSolicitud);
					
				}
				if(!lstSolicitudDeAdmision.empty){
					objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end).get(0);
					
					catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
					isFoundCampus = false;
					for(String objGrupo : lstGrupo) {
						if(catCampusStr.toLowerCase().equals(objGrupo.toLowerCase())) {
							isFoundCampus = true;
						}
					}
					if(isFoundCampus) {
						if(object.lstFiltro != null) {
							for(def row: object.lstFiltro) {
								isFound = false;
								assert row instanceof Map;
							
								nombreCandidato = objSolicitudDeAdmision.getPrimerNombre()+" "+objSolicitudDeAdmision.getSegundoNombre()+" "+objSolicitudDeAdmision.getApellidoPaterno()+" "+objSolicitudDeAdmision.getApellidoMaterno();
								if(row.operador == 'Que contenga') {
									if (row.columna == "NOMBRE") {
										if (nombreCandidato.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "EMAIL") {
										if (objSolicitudDeAdmision.getCorreoElectronico().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CURP") {
										if (objSolicitudDeAdmision.getCurp().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CAMPUS") {
										catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
										if(catCampusStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROGRAMA") {
										gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null ? "" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
										if(gestionEscolar.contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "INGRESO") {
										catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null ? "" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
										if(catPeriodoStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTADO") {
										catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
										if(catEstadoStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PREPARATORIA") {
										catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null ? "" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
										if(catBachilleratoStr.toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROMEDIO") {
										if(objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTATUS") {
										if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna.toString() == "ID BANNER") {
										if(objDetalleSolicitud.getIdBanner().toString().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}
									else
									if (row.columna == "TELÉFONO") {
										if(objSolicitudDeAdmision.getTelefonoCelular().toString().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}
									else
									if (row.columna == "MOTIVO RECHAZO") {
										if(objDetalleSolicitud.getObservacionesRechazo().toString().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "MOTIVO LISTA ROJA") {
										if(objDetalleSolicitud.getObservacionesListaRoja().toString().toLowerCase().contains(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}

									
								}
								else {
									if (row.columna == "NOMBRE") {
										if (nombreCandidato.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "EMAIL") {
										if (objSolicitudDeAdmision.getCorreoElectronico().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CURP") {
										if (objSolicitudDeAdmision.getCurp().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "CAMPUS") {
										catCampusStr = objSolicitudDeAdmision.getCatCampus() == null ? "" : objSolicitudDeAdmision.getCatCampus().getDescripcion();
										if(catCampusStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROGRAMA") {
										gestionEscolar = objSolicitudDeAdmision.getCatGestionEscolar() == null ? "" : objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion().toLowerCase();
										if(gestionEscolar.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "INGRESO") {
										catPeriodoStr = objSolicitudDeAdmision.getCatPeriodo() == null ? "" : objSolicitudDeAdmision.getCatPeriodo().getDescripcion();
										if(catPeriodoStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTADO") {
										catEstadoStr = objSolicitudDeAdmision.getCatEstado() == null ? "" : objSolicitudDeAdmision.getCatEstado().getDescripcion();
										if(catEstadoStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PREPARATORIA") {
										catBachilleratoStr = objSolicitudDeAdmision.getCatBachilleratos() == null ? "" : objSolicitudDeAdmision.getCatBachilleratos().getDescripcion();
										if(catBachilleratoStr.toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "PROMEDIO") {
										if(objSolicitudDeAdmision.getPromedioGeneral().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ESTATUS") {
										if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									} else
									if (row.columna == "ID BANNER") {
										
										if(objDetalleSolicitud.getIdBanner().toString().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}
									else
									if (row.columna == "TELÉFONO") {
										if(objSolicitudDeAdmision.getTelefonoCelular().toString().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}
									else
									if (row.columna == "MOTIVO RECHAZO") {
										if(objDetalleSolicitud.getObservacionesRechazo().toString().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}
									else
									if (row.columna == "MOTIVO LISTA ROJA") {
										if(objDetalleSolicitud.getObservacionesListaRoja().toString().toLowerCase().equals(row.valor.toLowerCase())) {
											isFound = true;
											break;
										}
									}
								}
							}
						}
					}
					else {
						isFound = false;
					}
					LOGGER.error "isFound: "+isFound;
					if(isFound) {
						
						
						LOGGER.error "IF"
						LOGGER.error "isFound: "+objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase()
						if(objSolicitudDeAdmision.getEstatusSolicitud().toLowerCase().equals(estatusSolicitud.toLowerCase())) {
							String encoded = "";
							for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(objSolicitudDeAdmision.getCaseId(), "fotoPasaporte", 0, 10)) {
								encoded ="data:image/png;base64, "+ Base64.getEncoder().encodeToString(context.getApiClient().getProcessAPI().getDocumentContent(doc.contentStorageId))
								//LOGGER.error encoded;
							}
							objProcessDefinition = context.getApiClient().getProcessAPI().getProcessDefinition(objHumanTaskInstance.getProcessDefinitionId());
							objSolicitudAdmisionCustom.setTaskName(objHumanTaskInstance.getName());
							objSolicitudAdmisionCustom.setProcessName(objProcessDefinition.getName());
							objSolicitudAdmisionCustom.setProcessVersion(objProcessDefinition.getVersion());
							objSolicitudAdmisionCustom.setPersistenceId(objSolicitudDeAdmision.getPersistenceId());
							objSolicitudAdmisionCustom.setPersistenceVersion(objSolicitudDeAdmision.getPersistenceVersion());
							objSolicitudAdmisionCustom.setCaseId(objSolicitudDeAdmision.getCaseId());
							objSolicitudAdmisionCustom.setPrimerNombre(objSolicitudDeAdmision.getPrimerNombre());
							objSolicitudAdmisionCustom.setSegundoNombre(objSolicitudDeAdmision.getSegundoNombre());
							objSolicitudAdmisionCustom.setApellidoPaterno(objSolicitudDeAdmision.getApellidoPaterno());
							objSolicitudAdmisionCustom.setApellidoMaterno(objSolicitudDeAdmision.getApellidoMaterno());
							objSolicitudAdmisionCustom.setCorreoElectronico(objSolicitudDeAdmision.getCorreoElectronico());
							objSolicitudAdmisionCustom.setCurp(objSolicitudDeAdmision.getCurp());
							objSolicitudAdmisionCustom.setUsuarioFacebook(objSolicitudDeAdmision.getUsuarioFacebook());
							objSolicitudAdmisionCustom.setUsiarioTwitter(objSolicitudDeAdmision.getUsiarioTwitter());
							objSolicitudAdmisionCustom.setUsuarioInstagram(objSolicitudDeAdmision.getUsuarioInstagram());
							objSolicitudAdmisionCustom.setTelefonoCelular(objSolicitudDeAdmision.getTelefonoCelular());
							objSolicitudAdmisionCustom.setFoto(objSolicitudDeAdmision.getFoto());
							objSolicitudAdmisionCustom.setActaNacimiento(objSolicitudDeAdmision.getActaNacimiento());
							objSolicitudAdmisionCustom.setCalle(objSolicitudDeAdmision.getCalle());
							objSolicitudAdmisionCustom.setCodigoPostal(objSolicitudDeAdmision.getCodigoPostal());
							objSolicitudAdmisionCustom.setCiudad(objSolicitudDeAdmision.getCiudad());
							objSolicitudAdmisionCustom.setCalle2(objSolicitudDeAdmision.getCalle2());
							objSolicitudAdmisionCustom.setNumExterior(objSolicitudDeAdmision.getNumExterior());
							objSolicitudAdmisionCustom.setNumInterior(objSolicitudDeAdmision.getNumInterior());
							objSolicitudAdmisionCustom.setColonia(objSolicitudDeAdmision.getColonia());
							objSolicitudAdmisionCustom.setTelefono(objSolicitudDeAdmision.getTelefono());
							objSolicitudAdmisionCustom.setOtroTelefonoContacto(objSolicitudDeAdmision.getOtroTelefonoContacto());
							objSolicitudAdmisionCustom.setPromedioGeneral(objSolicitudDeAdmision.getPromedioGeneral());
							objSolicitudAdmisionCustom.setComprobanteCalificaciones(objSolicitudDeAdmision.getComprobanteCalificaciones());
							//objSolicitudAdmisionCustom.setCiudadExamen(objSolicitudDeAdmision.getCiudadExamen());
							objSolicitudAdmisionCustom.setTaskId(objHumanTaskInstance.getId());
							
														
							objCatGestionEscolar = new CatGestionEscolar();
							if(objSolicitudDeAdmision.getCatGestionEscolar() != null ){
								strError = strError+", " + "if(objSolicitudDeAdmision.getCatGestionEscolar() != null ){";
								objCatGestionEscolar.setPersistenceId(objSolicitudDeAdmision.getCatGestionEscolar().getPersistenceId());
								objCatGestionEscolar.setPersistenceVersion(objSolicitudDeAdmision.getCatGestionEscolar().getPersistenceVersion());
								objCatGestionEscolar.setNombre(objSolicitudDeAdmision.getCatGestionEscolar().getNombre());
								objCatGestionEscolar.setDescripcion(objSolicitudDeAdmision.getCatGestionEscolar().getDescripcion());
								objCatGestionEscolar.setEnlace(objSolicitudDeAdmision.getCatGestionEscolar().getEnlace());
								objCatGestionEscolar.setPropedeutico(objSolicitudDeAdmision.getCatGestionEscolar().isPropedeutico());
								objCatGestionEscolar.setProgramaparcial(objSolicitudDeAdmision.getCatGestionEscolar().isProgramaparcial());
								objCatGestionEscolar.setMatematicas(objSolicitudDeAdmision.getCatGestionEscolar().isMatematicas());
								objCatGestionEscolar.setInscripcionenero(objSolicitudDeAdmision.getCatGestionEscolar().getInscripcionenero());
								objCatGestionEscolar.setInscripcionagosto(objSolicitudDeAdmision.getCatGestionEscolar().getInscripcionagosto());
								objCatGestionEscolar.setIsEliminado(objSolicitudDeAdmision.getCatGestionEscolar().isIsEliminado());
								objCatGestionEscolar.setUsuarioCreacion(objSolicitudDeAdmision.getCatGestionEscolar().getUsuarioCreacion());
								objCatGestionEscolar.setCampus(objSolicitudDeAdmision.getCatGestionEscolar().getCampus());
								objCatGestionEscolar.setCaseId(objSolicitudDeAdmision.getCatGestionEscolar().getCaseId());
								strError = strError+", " + "end if(objSolicitudDeAdmision.getCatGestionEscolar() != null ){";
							}
							objSolicitudAdmisionCustom.setObjCatGestionEscolar(objCatGestionEscolar);
							
							objCatCampusCustom = new CatCampusCustom();
							if(objSolicitudDeAdmision.getCatCampus() != null){
								strError = strError+", " + "if(objSolicitudDeAdmision.getCatCampus() != null){";
								objCatCampusCustom.setPersistenceId(objSolicitudDeAdmision.getCatCampus().getPersistenceId());
								objCatCampusCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatCampus().getPersistenceVersion());
								objCatCampusCustom.setDescripcion(objSolicitudDeAdmision.getCatCampus().getDescripcion());
								objCatCampusCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatCampus().getUsuarioBanner());
								objCatCampusCustom.setClave(objSolicitudDeAdmision.getCatCampus().getClave());
								strError = strError+", " + "end if(objSolicitudDeAdmision.getCatCampus() != null){";
							}
							objSolicitudAdmisionCustom.setCatCampus(objCatCampusCustom);
							
							objCatPeriodoCustom = new CatPeriodoCustom();
							if(objSolicitudDeAdmision.getCatPeriodo() != null){
								strError = strError+", " + "if(objSolicitudDeAdmision.getCatPeriodo() != null){";
								objCatPeriodoCustom.setPersistenceId(objSolicitudDeAdmision.getCatPeriodo().getPersistenceId());
								objCatPeriodoCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatPeriodo().getPersistenceVersion());
								objCatPeriodoCustom.setDescripcion(objSolicitudDeAdmision.getCatPeriodo().getDescripcion());
								objCatPeriodoCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatPeriodo().getUsuarioBanner());
								objCatPeriodoCustom.setClave(objSolicitudDeAdmision.getCatPeriodo().getClave());
								strError = strError+", " + "end if(objSolicitudDeAdmision.getCatPeriodo() != null){";
							}
							objSolicitudAdmisionCustom.setCatPeriodo(objCatPeriodoCustom);
							
							objCatEstadosCustom = new CatEstadosCustom();
							if(objSolicitudDeAdmision.getCatEstado() != null){
								strError = strError+", " + "if(objSolicitudDeAdmision.getCatEstado() != null){";
								objCatEstadosCustom.setPersistenceId(objSolicitudDeAdmision.getCatEstado().getPersistenceId());
								objCatEstadosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatEstado().getPersistenceVersion());
								objCatEstadosCustom.setClave(objSolicitudDeAdmision.getCatEstado().getClave());
								objCatEstadosCustom.setDescripcion(objSolicitudDeAdmision.getCatEstado().getDescripcion());
								objCatEstadosCustom.setUsuarioCreacion(objSolicitudDeAdmision.getCatEstado().getUsuarioCreacion());
								strError = strError+", " + "end if(objSolicitudDeAdmision.getCatEstado() != null){";
							}
							objSolicitudAdmisionCustom.setCatEstado(objCatEstadosCustom);

							objSolicitudAdmisionCustom.setUpdateDate(dfSalida.format(objHumanTaskInstance.getReachedStateDate()));
							objSolicitudAdmisionCustom.setCatLicenciatura(objCatLicenciaturaCustom);

							objCatBachilleratosCustom = new CatBachilleratosCustom();
							if(objSolicitudDeAdmision.getCatBachilleratos() != null){
								strError = strError+", " + "if(objSolicitudDeAdmision.getCatBachilleratos() != null){";
								objCatBachilleratosCustom.setPersistenceId(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceId());
								objCatBachilleratosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceVersion());
								objCatBachilleratosCustom.setClave(objSolicitudDeAdmision.getCatBachilleratos().getClave());
								objCatBachilleratosCustom.setDescripcion(objSolicitudDeAdmision.getCatBachilleratos().getDescripcion());
								objCatBachilleratosCustom.setPais(objSolicitudDeAdmision.getCatBachilleratos().getPais());
								objCatBachilleratosCustom.setEstado(objSolicitudDeAdmision.getCatBachilleratos().getEstado());
								objCatBachilleratosCustom.setCiudad(objSolicitudDeAdmision.getCatBachilleratos().getCiudad());
								strError = strError+", " + "end if(objSolicitudDeAdmision.getCatBachilleratos() != null){";
							}
							objSolicitudAdmisionCustom.setCatBachilleratos(objCatBachilleratosCustom);
							
							objSolicitudAdmisionCustom.setFotografiaB64(encoded);
							objSolicitudAdmisionCustom.setEstatusSolicitud(objSolicitudDeAdmision.getEstatusSolicitud())
							lstResultado.add(objSolicitudAdmisionCustom);
						}
					}
				}
			}
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
}
