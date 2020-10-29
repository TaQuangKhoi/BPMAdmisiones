package com.anahuac.rest.api.DAO

import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.process.ProcessDefinition
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.CatBachilleratosCustom
import com.anahuac.rest.api.Entity.Custom.CatCampusCustom
import com.anahuac.rest.api.Entity.Custom.CatEstadosCustom
import com.anahuac.rest.api.Entity.Custom.CatLicenciaturaCustom
import com.anahuac.rest.api.Entity.Custom.CatPeriodoCustom
import com.anahuac.rest.api.Entity.Custom.SolicitudAdmisionCustom
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class ListadoDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ListadoDAO.class);
	
	public Result getSolicitudTramite(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<SolicitudAdmisionCustom> lstResultado = new ArrayList<SolicitudAdmisionCustom>();
		
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
		
		Boolean isFound = true;
		
		String nombreCandidato = "";
		
		ProcessDefinition objProcessDefinition;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			if(object.lstFiltro != null) {
				assert object.lstFiltro instanceof List;
			}

			userLogged = context.getApiSession().getUserId();
			
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			List<HumanTaskInstance> lstHumanTaskInstance = context.getApiClient().getProcessAPI().getPendingHumanTaskInstances(userLogged, 0, 99999, ActivityInstanceCriterion.DEFAULT);
			for(HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstance) {
				objSolicitudAdmisionCustom = new SolicitudAdmisionCustom();
				objCatCampusCustom = new CatCampusCustom();

				if(objHumanTaskInstance.getName().equals(object.tarea)) {
					
					LOGGER.error "=================================";
					LOGGER.error "tarea = "+ objHumanTaskInstance.getName();
					
										
					caseId = objHumanTaskInstance.getRootContainerId();
					objSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCaseId(caseId, start, end).get(0);
					LOGGER.error objSolicitudDeAdmision.getPrimerNombre();
					LOGGER.error "=================================";
					
					
					if(object.lstFiltro != null) {
						LOGGER.error "=================================ENTRA=================================";
						for(def row: object.lstFiltro) {
							isFound = false;
							assert row instanceof Map;
							LOGGER.error "=================================";
							LOGGER.error row.columna;
							LOGGER.error row.operador;
							LOGGER.error row.valor;
							LOGGER.error "=================================";
							nombreCandidato = objSolicitudDeAdmision.getPrimerNombre()+" "+objSolicitudDeAdmision.getSegundoNombre()+" "+objSolicitudDeAdmision.getApellidoPaterno()+" "+objSolicitudDeAdmision.getApellidoMaterno();
							if(row.operador == 'Que contenga') {
								if (row.columna == 'NOMBRE') {
									if(nombreCandidato.contains(row.valor)) {
										isFound = true;
										break;
									}
								} else {
									if (row.columna == 'EMAIL') {
										if(objSolicitudDeAdmision.getCorreoElectronico().contains(row.valor)) {
											isFound = true;
											break;
										}
									} else {
										if (row.columna == 'CURP') {
											if(objSolicitudDeAdmision.getCurp().contains(row.valor)) {
												isFound = true;
												break;
											}
										}
									}
								}
							}
							else {
								if (row.columna == 'NOMBRE') {
									if(nombreCandidato.equals(row.valor)) {
										isFound = true;
										break;
									}
								} else {
									if (row.columna == 'EMAIL') {
										if(objSolicitudDeAdmision.getCorreoElectronico().equals(row.valor)) {
											isFound = true;
											break;
										}
									} else {
										if (row.columna == 'CURP') {
											if(objSolicitudDeAdmision.getCurp().equals(row.valor)) {
												isFound = true;
												break;
											}
										}
									}
								}
							}
						}
					}
					if(isFound) {
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
						objSolicitudAdmisionCustom.setCiudadExamen(objSolicitudDeAdmision.getCiudadExamen());
						objSolicitudAdmisionCustom.setTaskId(objHumanTaskInstance.getId());
						
						
						objCatCampusCustom.setPersistenceId(objSolicitudDeAdmision.getCatCampus().getPersistenceId());
						objCatCampusCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatCampus().getPersistenceVersion());
						objCatCampusCustom.setDescripcion(objSolicitudDeAdmision.getCatCampus().getDescripcion());
						objCatCampusCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatCampus().getUsuarioBanner());
						//objCatCampusCustom.setFechaImplementacion(objSolicitudDeAdmision.getCatCampus().getFechaImplementacion());
						objCatCampusCustom.setClave(objSolicitudDeAdmision.getCatCampus().getClave());
						objSolicitudAdmisionCustom.setCatCampus(objCatCampusCustom);
						
						objCatPeriodoCustom.setPersistenceId(objSolicitudDeAdmision.getCatPeriodo().getPersistenceId());
						objCatPeriodoCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatPeriodo().getPersistenceVersion());
						objCatPeriodoCustom.setDescripcion(objSolicitudDeAdmision.getCatPeriodo().getDescripcion());
						objCatPeriodoCustom.setUsuarioBanner(objSolicitudDeAdmision.getCatPeriodo().getUsuarioBanner());
						objCatPeriodoCustom.setClave(objSolicitudDeAdmision.getCatPeriodo().getClave());
						objSolicitudAdmisionCustom.setCatPeriodo(objCatPeriodoCustom);
						
						objCatEstadosCustom.setPersistenceId(objSolicitudDeAdmision.getCatEstado().getPersistenceId());
						objCatEstadosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatEstado().getPersistenceVersion());
						objCatEstadosCustom.setClave(objSolicitudDeAdmision.getCatEstado().getClave());
						objCatEstadosCustom.setDescripcion(objSolicitudDeAdmision.getCatEstado().getDescripcion());
						objCatEstadosCustom.setUsuarioCreacion(objSolicitudDeAdmision.getCatEstado().getUsuarioCreacion());
						objSolicitudAdmisionCustom.setCatEstado(objCatEstadosCustom);
						
						objCatLicenciaturaCustom.setPersistenceId(objSolicitudDeAdmision.getCatLicenciatura().getPersistenceId());
						objCatLicenciaturaCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatLicenciatura().getPersistenceVersion());
						objCatLicenciaturaCustom.setClave(objSolicitudDeAdmision.getCatLicenciatura().getClave());
						objCatLicenciaturaCustom.setDescripcion(objSolicitudDeAdmision.getCatLicenciatura().getDescripcion());
						objCatLicenciaturaCustom.setUsuarioCreacion(objSolicitudDeAdmision.getCatLicenciatura().getUsuarioCreacion());
						objSolicitudAdmisionCustom.setCatLicenciatura(objCatLicenciaturaCustom);
						
						objCatBachilleratosCustom.setPersistenceId(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceId());
						objCatBachilleratosCustom.setPersistenceVersion(objSolicitudDeAdmision.getCatBachilleratos().getPersistenceVersion());
						objCatBachilleratosCustom.setClave(objSolicitudDeAdmision.getCatBachilleratos().getClave());
						objCatBachilleratosCustom.setDescripcion(objSolicitudDeAdmision.getCatBachilleratos().getDescripcion());
						objCatBachilleratosCustom.setPais(objSolicitudDeAdmision.getCatBachilleratos().getPais());
						objCatBachilleratosCustom.setEstado(objSolicitudDeAdmision.getCatBachilleratos().getEstado());
						objCatBachilleratosCustom.setCiudad(objSolicitudDeAdmision.getCatBachilleratos().getCiudad());
						objCatBachilleratosCustom.setTipoconvenio(objSolicitudDeAdmision.getCatBachilleratos().getTipoconvenio());
						objCatBachilleratosCustom.setDescuento(objSolicitudDeAdmision.getCatBachilleratos().getDescuento());
						objCatBachilleratosCustom.setDocumento(objSolicitudDeAdmision.getCatBachilleratos().getDocumento());
						objSolicitudAdmisionCustom.setCatBachilleratos(objCatBachilleratosCustom);
						
						lstResultado.add(objSolicitudAdmisionCustom);
						LOGGER.error "JALA=================================";
					}
				}
				else {
					LOGGER.error "=================================";
					LOGGER.error "no entra = "+ objHumanTaskInstance.getName();
					LOGGER.error "=================================";
				}
			}
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
			e.printStackTrace();
		}
		return resultado
	}
	
	//public Result getSolicitudTramite(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
	//		Result resultado = new Result();
	//		List<String> lstResultado = new ArrayList<String>();
	//		Long userLogged = 0L;
	//		Long caseId = 0L;
	//		Long total = 0L;
	//		Integer start = 0;
	//		Integer end = 99999;
	//		TableTest tableTest = null;
	//
	//		Boolean isFound = true;
	//
	//		try {
	//			def jsonSlurper = new JsonSlurper();
	//			def object = jsonSlurper.parseText(jsonData);
	//
	//			assert object instanceof Map;
	//			assert object.lstFiltro instanceof List;
	
	//			userLogged = context.getApiSession().getUserId();
	//
	//			def tableTestVar = context.apiClient.getDAO(TableTestDAO.class);
	//			List<HumanTaskInstance> lstHumanTaskInstance = context.getApiClient().getProcessAPI().getPendingHumanTaskInstances(userLogged, 0, 99999, ActivityInstanceCriterion.DEFAULT);
	//			for(HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstance) {
	//				if(objHumanTaskInstance.getName().equals(object.tarea)) {
	//					LOGGER.error "=================================";
	//					LOGGER.error "tarea = "+ objHumanTaskInstance.getName();
	//
	//					caseId = objHumanTaskInstance.getRootContainerId();
	//					tableTest = tableTestVar.findByCaseId(caseId, start, end).get(0);
	//					LOGGER.error "TABLETEST=================================";
	//					LOGGER.error "Campo1: "+tableTest.getCampo1();
	//					LOGGER.error "Campo2: "+tableTest.getCampo2();
	//					LOGGER.error "Campo3: "+tableTest.getCampo3();
	//					LOGGER.error "Campo4: "+tableTest.getCampo4();
	//					LOGGER.error "Campo5: "+tableTest.getCampo5();
	//					LOGGER.error "TABLETEST=================================";
	//					for(def row: object.lstFiltro) {
	//						isFound = false;
	//						assert row instanceof Map;
	//						LOGGER.error "=================================";
	//						LOGGER.error row.columna;
	//						LOGGER.error row.operador;
	//						LOGGER.error row.valor;
	//						LOGGER.error "=================================";
	//						if(row.operador == 'Que contenga') {
	//							if (row.columna == 'Campo1') {
	//								if(tableTest.getCampo1().contains(row.valor)) {
	//									isFound = true;
	//									break;
	//								}
	//							} else {
	//								if (row.columna == 'Campo2') {
	//									if(tableTest.getCampo2().contains(row.valor)) {
	//										isFound = true;
	//										break;
	//									}
	//								} else {
	//									if (row.columna == 'Campo3') {
	//										if(tableTest.getCampo3().contains(row.valor)) {
	//											isFound = true;
	//											break;
	//										}
	//									} else {
	//										if (row.columna == 'Campo4') {
	//											if(tableTest.getCampo4().contains(row.valor)) {
	//												isFound = true;
	//												break;
	//											}
	//										} else {
	//											if (row.columna == 'Campo5') {
	//												if(tableTest.getCampo5().contains(row.valor)) {
	//													isFound = true;
	//													break;
	//												}
	//											}
	//										}
	//									}
	//								}
	//							}
	//						}
	//						else {
	//							if (row.columna == 'Campo1') {
	//								if(tableTest.getCampo1().equals(row.valor)) {
	//									isFound = true;
	//									break;
	//								}
	//							} else {
	//								if (row.columna == 'Campo2') {
	//									if(tableTest.getCampo2().equals(row.valor)) {
	//										isFound = true;
	//										break;
	//									}
	//								} else {
	//									if (row.columna == 'Campo3') {
	//										if(tableTest.getCampo3().equals(row.valor)) {
	//											isFound = true;
	//											break;
	//										}
	//									} else {
	//										if (row.columna == 'Campo4') {
	//											if(tableTest.getCampo4().equals(row.valor)) {
	//												isFound = true;
	//												break;
	//											}
	//										} else {
	//											if (row.columna == 'Campo5') {
	//												if(tableTest.getCampo5().equals(row.valor)) {
	//													isFound = true;
	//													break;
	//												}
	//											}
	//										}
	//									}
	//								}
	//							}
	//						}
	//					}
	//					if(isFound) {
	//						lstResultado.add(tableTest.getCaseId()+" - "+tableTest.getCampo1()+" - "+tableTest.getCampo2()+" - "+tableTest.getCampo3()+" - "+tableTest.getCampo4()+" - "+tableTest.getCampo5());
	//					}
	//				}
	//				else {
	//					LOGGER.error "=================================";
	//					LOGGER.error "no entra = "+ objHumanTaskInstance.getName();
	//					LOGGER.error "=================================";
	//				}
	//			}
	//			resultado.setData(lstResultado);
	//			resultado.setSuccess(true);
	//		} catch (Exception e) {
	//			resultado.setSuccess(false);
	//			resultado.setError(e.getMessage());
	//			LOGGER.error "ERROR=================================";
	//			LOGGER.error e.getMessage();
	//			e.printStackTrace();
	//		}
	//		return resultado
	//	}
}
