package com.anahuac.rest.api.DAO

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.ZoneId

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.json.JSONArray
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatRegistro
import com.anahuac.catalogos.CatRegistroDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.model.PadresTutor
import com.anahuac.model.PadresTutorDAO
import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.Entity.HubSpotData
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class HubspotDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(HubspotDAO.class);

	public Result createOrUpdateRegistro(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String nombreCompleto = "";
		String catLugarExamenDescripcion = "";
		String lugarExamen = "";
		String estadoExamen = "";
		String ciudadExamen ="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			
			LOGGER.error "lstCatRegistro";
			LOGGER.error "email: "+object.email;
			if(lstCatRegistro != null) {
				LOGGER.error "lstCatRegistro in join";
				strError = strError + " | object.email: "+object.email;
				
				strError = strError + " | lstSolicitudDeAdmision.size: "+lstSolicitudDeAdmision.size();
				strError = strError + " | lstSolicitudDeAdmision.empty: "+lstSolicitudDeAdmision.empty;
				strError = strError + " | lstCatRegistro.size: "+lstCatRegistro.size();
				strError = strError + " | lstCatRegistro.empty: "+lstCatRegistro.empty;
				
				if(!lstCatRegistro.empty && !lstSolicitudDeAdmision.empty) {
					LOGGER.error "size: "+lstCatRegistro.size();
					objHubSpotData = new HashMap<String, String>();
					objHubSpotData.put("campus_admision", lstSolicitudDeAdmision.get(0).getCatCampusEstudio().getClave());
					//PENDIENTE ESTATUS DE GESTION ESCOLAR------------------
					//objHubSpotData.put("carrera", "[CLAVE-GESTION-ESCOLAR]");
					
					catLugarExamenDescripcion = lstSolicitudDeAdmision.get(0).getCatLugarExamen().descripcion;
					
					if(catLugarExamenDescripcion.equals("En un estado")){
						lugarExamen = "México, "+lstSolicitudDeAdmision.get(0).getCatEstadoExamen().getDescripcion()+", "+lstSolicitudDeAdmision.get(0).getCiudadExamen().getDescripcion();
					}
					if(catLugarExamenDescripcion.equals("En el extranjero (solo si vives fuera de México)")){
						lugarExamen = lstSolicitudDeAdmision.get(0).getCatPaisExamen().getDescripcion()+", "+lstSolicitudDeAdmision.get(0).getCiudadExamenPais().getDescripcion();
					}
					if(catLugarExamenDescripcion.equals("En el mismo campus en donde realizaré mi licenciatura")){
						lugarExamen = lstSolicitudDeAdmision.get(0).getCatCampus().getDescripcion();
					}
					
					strError = strError + " | catLugarExamenDescripcion: "+catLugarExamenDescripcion;
					strError = strError + " | lugarExamen: "+lugarExamen;
										
					objHubSpotData.put("lugar_de_examen", lugarExamen);
					objHubSpotData.put("periodo_de_ingreso", lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave());
					objHubSpotData.put("campus_vpd", lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					objHubSpotData.put("firstname", lstCatRegistro.get(0).getPrimernombre()+" "+(lstCatRegistro.get(0).getSegundonombre() == null ? "" : lstCatRegistro.get(0).getSegundonombre()));
					objHubSpotData.put("lastname", lstCatRegistro.get(0).getApellidopaterno()+" "+lstCatRegistro.get(0).getApellidomaterno());
					objHubSpotData.put("email", object.email);
					objHubSpotData.put("propiedades_de_smart_campus", "Solicitud");
					
					resultado = createOrUpdateHubspot(object.email, objHubSpotData);
				}
				else {
					strError = strError + " | ------------------------------------------";
					strError = strError + " | lstSolicitudDeAdmision.empty: "+lstSolicitudDeAdmision.empty;
					strError = strError + " | lstCatRegistro.empty: "+lstCatRegistro.empty;
					strError = strError + " | ------------------------------------------";
				}
				
			}
			LOGGER.error "listo";
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateEnviada(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		List<PadresTutor> lstPadresTutor = new ArrayList<PadresTutor>();
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String nombreCompleto = "";
		String catLugarExamenDescripcion ="";
		String lugarExamen = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def objPadresTutorDAO = context.apiClient.getDAO(PadresTutorDAO.class);
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);

			LOGGER.error "lstCatRegistro";
			LOGGER.error "email: "+object.email;
			
			//Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
			Date fechaNacimiento = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd");
			
			if(lstSolicitudDeAdmision != null) {
				LOGGER.error "lstCatRegistro in join";
				if(!lstSolicitudDeAdmision.empty) {
					
					objHubSpotData = new HashMap<String, String>();
					lstPadresTutor = objPadresTutorDAO.findByCaseId(lstSolicitudDeAdmision.get(0).getCaseId(), 0, 999);
					if(lstPadresTutor != null) {
						if(!lstPadresTutor.empty) {
							for(PadresTutor objPadresTutor : lstPadresTutor) {
								if(objPadresTutor.isIsTutor()) {
									strError = strError + ", ENTRO A TUTOR"
									objHubSpotData.put("correo_tutor", objPadresTutor.getCorreoElectronico() == null ? "" : objPadresTutor.getCorreoElectronico());
									objHubSpotData.put("nombre_de_tutor", (objPadresTutor.getNombre() == null ? "" : objPadresTutor.getNombre())+(objPadresTutor.getApellidos() == null ? "" : " " + objPadresTutor.getApellidos()) );
								}
								if(objPadresTutor.getCatParentezco().getDescripcion().equals("Padre")) {
									objHubSpotData.put("nombre_del_padre", (objPadresTutor.getNombre() == null ? "" : objPadresTutor.getNombre())+(objPadresTutor.getApellidos() == null ? "" : " " + objPadresTutor.getApellidos()));
									objHubSpotData.put("correo_del_padre", objPadresTutor.getCorreoElectronico());
									objHubSpotData.put("telefono_del_padre", objPadresTutor.getTelefono());
								}
								if(objPadresTutor.getCatParentezco().getDescripcion().equals("Madre")) {
									objHubSpotData.put("nombre_de_la_madre", (objPadresTutor.getNombre() == null ? "" : objPadresTutor.getNombre())+(objPadresTutor.getApellidos() == null ? "" : " " + objPadresTutor.getApellidos()));
									objHubSpotData.put("correo_de_la_madre", objPadresTutor.getCorreoElectronico());
									objHubSpotData.put("telefono_de_la_madre", objPadresTutor.getTelefono());
								}
							}
						}
					}
					
					
					
					LOGGER.error "size: "+lstSolicitudDeAdmision.size();					
					
					catLugarExamenDescripcion = lstSolicitudDeAdmision.get(0).getCatLugarExamen().descripcion;
					
					if(catLugarExamenDescripcion.equals("En un estado")){
						lugarExamen = "México, "+lstSolicitudDeAdmision.get(0).getCatEstadoExamen().getDescripcion()+", "+lstSolicitudDeAdmision.get(0).getCiudadExamen().getDescripcion();
					}
					if(catLugarExamenDescripcion.equals("En el extranjero (solo si vives fuera de México)")){
						lugarExamen = lstSolicitudDeAdmision.get(0).getCatPaisExamen().getDescripcion()+", "+lstSolicitudDeAdmision.get(0).getCiudadExamenPais().getDescripcion();
					}
					if(catLugarExamenDescripcion.equals("En el mismo campus en donde realizaré mi licenciatura")){
						lugarExamen = lstSolicitudDeAdmision.get(0).getCatCampus().getDescripcion();
					}
					
					strError = strError + " | catLugarExamenDescripcion: "+catLugarExamenDescripcion;
					strError = strError + " | lugarExamen: "+lugarExamen;
										
					objHubSpotData.put("lugar_de_examen", lugarExamen);
					fechaNacimiento = Date.from(lstSolicitudDeAdmision.get(0).getFechaNacimiento().atZone(ZoneId.systemDefault()).toInstant());
					
					//objHubSpotData.put("ciudades", (lstSolicitudDeAdmision.get(0).getCiudad()=="" ? "" : lstSolicitudDeAdmision.get(0).getCiudad())+", "+(lstSolicitudDeAdmision.get(0).getDelegacionMunicipio()==null ? "" : lstSolicitudDeAdmision.get(0).getDelegacionMunicipio()));
					objHubSpotData.put("pais", lstSolicitudDeAdmision.get(0).getCatPais().descripcion);
					objHubSpotData.put("campus_admision", lstSolicitudDeAdmision.get(0).getCatCampusEstudio().getClave());
					objHubSpotData.put("periodo_de_ingreso", lstSolicitudDeAdmision.get(0).getCatPeriodo().getClave());
					objHubSpotData.put("campus_vpd", lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					objHubSpotData.put("firstname", lstCatRegistro.get(0).getPrimernombre()+" "+(lstCatRegistro.get(0).getSegundonombre() == null ? "" : lstCatRegistro.get(0).getSegundonombre()));
					objHubSpotData.put("lastname", lstCatRegistro.get(0).getApellidopaterno()+" "+lstCatRegistro.get(0).getApellidomaterno());
					objHubSpotData.put("email", object.email);
					objHubSpotData.put("date_of_birth", dfSalida.format(fechaNacimiento));
					objHubSpotData.put("gender", lstSolicitudDeAdmision.get(0).getCatSexo().getDescripcion() == null ? "" : lstSolicitudDeAdmision.get(0).getCatSexo().getDescripcion());
					//objHubSpotData.put("relationship_status", lstSolicitudDeAdmision.get(0).getCatEstadoCivil().getDescripcion() == null ? "" : lstSolicitudDeAdmision.get(0).getCatEstadoCivil().getDescripcion());
					objHubSpotData.put("promedio", lstSolicitudDeAdmision.get(0).getPromedioGeneral() == null ? "" : lstSolicitudDeAdmision.get(0).getPromedioGeneral());
					objHubSpotData.put("propiedades_de_smart_campus", "Envío de Solicitud");
					strError = strError + "| INFORMACION DE REGISTRO";
					strError = strError + ", GENDER-ZIP";
										
					resultado = createOrUpdateHubspot(object.email, objHubSpotData);
					strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|"); 
				}
			}
			LOGGER.error "listo";
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateModificar(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String nombreCompleto = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			
			
			Date fecha = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			if(lstCatRegistro != null) {
				if(!lstCatRegistro.empty) {
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+lstCatRegistro.get(0).getCaseId();
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+(lstCatRegistro.get(0).getCaseId() == null ? "" : lstCatRegistro.get(0).getCaseId().toString());
					lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);
					
					strError = strError + " | lstDetalleSolicitud.empty: "+lstDetalleSolicitud.empty;
					strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();
					
					if(lstDetalleSolicitud != null) {
						if(!lstDetalleSolicitud.empty) {
							objHubSpotData.put("mensaje", lstDetalleSolicitud.get(0).getObservacionesCambio());
							objHubSpotData.put("fecha_actualizacion_sc", dfSalida.format(fecha));
							objHubSpotData.put("propiedades_de_smart_campus", "Solicitud Cambios");
							resultado = createOrUpdateHubspot(object.email, objHubSpotData);
							strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
						}
					}
					
				}
			}
			LOGGER.error "listo";
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateValidar(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String residencia = "";
		String nombreCompleto = "";
		String tipoAdmision = "";
		String descuento = "";
		String catDescuento = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			
			
			Date fecha = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			if(lstCatRegistro != null) {
				if(!lstCatRegistro.empty) {
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+lstCatRegistro.get(0).getCaseId();
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+(lstCatRegistro.get(0).getCaseId() == null ? "" : lstCatRegistro.get(0).getCaseId().toString());
					lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);
					
					strError = strError + " | lstDetalleSolicitud.empty: "+lstDetalleSolicitud.empty;
					strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();
					
					
					if(lstDetalleSolicitud != null) {
						if(!lstDetalleSolicitud.empty) {
							residencia = lstDetalleSolicitud.get(0).getCatResidencia().getClave().equals("F") ? "L" : (lstDetalleSolicitud.get(0).getCatResidencia().getClave().equals("R") ? "R" : "E");
							tipoAdmision = lstDetalleSolicitud.get(0).getCatTipoAdmision().getClave();
							
							strError = strError + " | residencia: "+residencia;
							strError = strError + " | tipoAdmision: "+tipoAdmision;
							strError = strError + " | getDescuento: "+lstDetalleSolicitud.get(0).getDescuento();
							
							descuento = ""+lstDetalleSolicitud.get(0).getDescuento();
							catDescuento = ""+(lstDetalleSolicitud.get(0).getCatDescuentos()== null ? "" : lstDetalleSolicitud.get(0).getCatDescuentos().getDescuento());
							
							strError = strError + " | descuento: "+descuento;
							strError = strError + " | catDescuento: "+catDescuento;
							
							objHubSpotData.put("tipo_de_alumno", lstDetalleSolicitud.get(0).getCatTipoAlumno().getClave());
							objHubSpotData.put("porcentaje_de_descuento", lstDetalleSolicitud.get(0).getDescuento()==null? (lstDetalleSolicitud.get(0).getCatDescuentos() == null ? "0" : lstDetalleSolicitud.get(0).getCatDescuentos().getDescuento()):"0");
							objHubSpotData.put("id_banner", lstDetalleSolicitud.get(0).getIdBanner());
							objHubSpotData.put("fecha_actualizacion_sc", dfSalida.format(fecha));
							objHubSpotData.put("tipo_de_admision", tipoAdmision);
							objHubSpotData.put("aac_bp", residencia);
							//objHubSpotData.put("propiedades_de_smart_campus", "Solicitud Cambios"); PENDIENTE
							resultado = createOrUpdateHubspot(object.email, objHubSpotData);
							strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
						}
					}
					
				}
			}
			LOGGER.error "listo";
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateRechazoLRoja(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String mensaje = "";
		String estatus = "";
		String nombreCompleto = "";
		
		Boolean isRechazo=false;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			isRechazo=object.isRechazo;
			
			Date fecha = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			if(lstCatRegistro != null) {
				if(!lstCatRegistro.empty) {
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+lstCatRegistro.get(0).getCaseId();
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+(lstCatRegistro.get(0).getCaseId() == null ? "" : lstCatRegistro.get(0).getCaseId().toString());
					lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);
					
					strError = strError + " | lstDetalleSolicitud.empty: "+lstDetalleSolicitud.empty;
					strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();
					
					if(lstDetalleSolicitud != null) {
						if(!lstDetalleSolicitud.empty) {
							if(isRechazo) {
								mensaje = lstDetalleSolicitud.get(0).getObservacionesRechazo() == null ? "" : lstDetalleSolicitud.get(0).getObservacionesRechazo();
								estatus = "Rechazado";
							}
							else {
								mensaje = lstDetalleSolicitud.get(0).getObservacionesListaRoja() == null ? "" : lstDetalleSolicitud.get(0).getObservacionesListaRoja();
								estatus = "Rechazado";
							}
							objHubSpotData.put("mensaje", mensaje);
							objHubSpotData.put("fecha_actualizacion_sc", dfSalida.format(fecha));
							objHubSpotData.put("propiedades_de_smart_campus", estatus);
							resultado = createOrUpdateHubspot(object.email, objHubSpotData);
							strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
						}
					}
					
				}
			}
			LOGGER.error "listo";
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			//resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdateHubspot(String email, Map<String, String> objHubSpotData) {
		Result resultado = new Result();
		String data="8b-.-.-.b0-.-.-.a-.-.-.1ac-df-.-.-.54-40-.-.-.bf-b5-.-.-.69-40-.-.-.e8-.-.-.7f-.-.-.90-.-.-.c0-.-.-.99"
		String targetURL = "https://api.hubapi.com/contacts/v1/contact/createOrUpdate/email/[EMAIL]/?hapikey="+data.replace("-.-.-.", "");
		String jsonInputString = "{\"properties\":[{\"property\":\"firstname\",\"value\":\"java\"},{\"property\":\"nombre\",\"value\":\"Arturo\"},{\"property\":\"lastname\",\"value\":\"Zamorano\"},{\"property\":\"nombre_completo\",\"value\":\"Arturo Zamorano\"},{\"property\":\"correo_electrnico\",\"value\":\"jasz189@hotmail.com\"},{\"property\":\"date_of_birth\",\"value\":\"2020-11-30T23:51:03.309Z\"},{\"property\":\"fecha_de_nacimiento\",\"value\":\"654307200000\"},{\"property\":\"twitterhandle\",\"value\":\"arturoZCZ\"},{\"property\":\"gender\",\"value\":\"Masculino\"},{\"property\":\"country\",\"value\":\"México\"},{\"property\":\"state\",\"value\":\"Sonora\"},{\"property\":\"ciudad\",\"value\":\"Navojoa\"},{\"property\":\"city\",\"value\":\"Navojoa\"},{\"property\":\"address\",\"value\":\"Callejon 3\"},{\"property\":\"celular\",\"value\":\"6421344161\"},{\"property\":\"phone\",\"value\":\"6421344161\"},{\"property\":\"zip\",\"value\":\"85890\"},{\"property\":\"promedio\",\"value\":\"9.5\"},{\"property\":\"promedio_de_preparatoria\",\"value\":\"9.5\"},{\"property\":\"relationship_status\",\"value\":\"Casado\"},{\"property\":\"nombre_de_tutor\",\"value\":\"Arturo\"},{\"property\":\"apellido_tutor\",\"value\":\"Zamorano\"},{\"property\":\"celular_de_tutor\",\"value\":\"6421344161\"},{\"property\":\"correo_tutor\",\"value\":\"arturo.zamorano@gmail.com\"},{\"property\":\"telefono_tutor\",\"value\":\"6421344161\"},{\"property\":\"nombre_del_padre\",\"value\":\"Arturo\"},{\"property\":\"apellido_paterno\",\"value\":\"Zamorano\"},{\"property\":\"celular_del_padre\",\"value\":\"6421344161\"},{\"property\":\"correo_del_padre\",\"value\":\"arturo.zamorano@gmail.com\"},{\"property\":\"telefono_del_padre\",\"value\":\"6421344161\"},{\"property\":\"nombre_de_la_madre\",\"value\":\"Guadalupe\"},{\"property\":\"apellido_materno\",\"value\":\"Sainz\"},{\"property\":\"celular_de_la_madre\",\"value\":\"6421344161\"},{\"property\":\"correo_de_la_madre\",\"value\":\"eva.sainz@gmail.com\"},{\"property\":\"telefono_de_la_madre\",\"value\":\"6421344161\"},{\"property\":\"ua_vpd\",\"value\":\"UAM\"},{\"property\":\"campus_destino\",\"value\":\"AMAY\"},{\"property\":\"tipo_de_alumno\",\"value\":\"N\"}]}";
		String strError = "";
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		
		JSONObject jsonItem = new JSONObject();
		JSONObject jsonProperties = new JSONObject();
		JSONArray jsonList = new JSONArray();
		
		try {
			strError = strError + ", INICIO";
			Iterator it = objHubSpotData.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				jsonItem = new JSONObject();
				jsonItem.put("property", pair.getKey());
				jsonItem.put("value", pair.getValue() == null ? "" : pair.getValue());
				jsonList.put(jsonItem);
				it.remove();
			}
			jsonProperties.put("properties", jsonList);
			strError = strError + ", "+jsonProperties.toString();
			LOGGER.error jsonList.toString();
			LOGGER.error jsonProperties.toString();
			strError = strError + ", EMAIL: "+email;
			targetURL = targetURL.replace("[EMAIL]", email);
			HttpPost request = new HttpPost(targetURL);
			StringEntity params = new StringEntity(jsonProperties.toString(), "UTF-8");
			request.setHeader("content-type", "application/json");
			request.setHeader("Accept-Encoding", "UTF-8");
			request.setEntity(params);
			
			CloseableHttpResponse response = httpClient.execute(request);
			strError = strError + ", "+ response.getEntity().getContentType().getName();
			strError = strError + ", "+ response.getEntity().getContentType().getValue();
			strError = strError + ", "+ EntityUtils.toString(response.getEntity(), "UTF-8");
			
			resultado.setError_info(strError);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setError_info(strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
}

