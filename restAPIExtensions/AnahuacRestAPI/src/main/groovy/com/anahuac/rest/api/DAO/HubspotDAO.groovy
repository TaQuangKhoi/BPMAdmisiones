
package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.text.DateFormat
import java.text.DecimalFormat
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
import java.text.NumberFormat


import com.anahuac.catalogos.CatRegistro
import com.anahuac.catalogos.CatRegistroDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.model.PadresTutor
import com.anahuac.model.PadresTutorDAO
import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.HubSpotData
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class HubspotDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(HubspotDAO.class);
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
	
	public Result createOrUpdateRegistro(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		List<String> lstValueProperties = new ArrayList<String>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String nombreCompleto = "";
		String catLugarExamenDescripcion = "";
		String lugarExamen = "";
		String estadoExamen = "";
		String ciudadExamen ="";
		String apikeyHubspot ="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			
			
			
			if(lstCatRegistro != null) {
				
				strError = strError + " | object.email: "+object.email;
				
				strError = strError + " | lstSolicitudDeAdmision.size: "+lstSolicitudDeAdmision.size();
				strError = strError + " | lstSolicitudDeAdmision.empty: "+lstSolicitudDeAdmision.empty;
				strError = strError + " | lstCatRegistro.size: "+lstCatRegistro.size();
				strError = strError + " | lstCatRegistro.empty: "+lstCatRegistro.empty;
				
				if(!lstCatRegistro.empty && !lstSolicitudDeAdmision.empty) {
					
					resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					apikeyHubspot = (String) resultadoApiKey.getData().get(0);
					strError = strError + " | apikeyHubspot: "+apikeyHubspot;
					objHubSpotData = new HashMap<String, String>();
					objHubSpotData.put("campus_admision", lstSolicitudDeAdmision.get(0).getCatCampusEstudio().getClave());
					
					if(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave() != null) {
						strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
						if(!lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave().equals("")) {
							strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
							lstValueProperties = getLstValueProperties();
							strError = strError + " | lstValueProperties.size() "+lstValueProperties.size();
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave())) {
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
								objHubSpotData.put("carrera", lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave());
							}
						}
					}
					
					
					
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
					
					resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
				}
				else {
					strError = strError + " | ------------------------------------------";
					strError = strError + " | lstSolicitudDeAdmision.empty: "+lstSolicitudDeAdmision.empty;
					strError = strError + " | lstCatRegistro.empty: "+lstCatRegistro.empty;
					strError = strError + " | ------------------------------------------";
				}
				
			}
			
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
		Result resultadoApiKey = new Result();
		
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		List<PadresTutor> lstPadresTutor = new ArrayList<PadresTutor>();
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<String> lstValueProperties = new ArrayList<String>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String nombreCompleto = "";
		String catLugarExamenDescripcion ="";
		String lugarExamen = "";
		String apikeyHubspot ="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def objPadresTutorDAO = context.apiClient.getDAO(PadresTutorDAO.class);
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);

			
			
			
			//Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
			Date fechaNacimiento = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd");
			
			if(lstSolicitudDeAdmision != null) {
				
				if(!lstSolicitudDeAdmision.empty) {
					resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					apikeyHubspot = (String) resultadoApiKey.getData().get(0);
					strError = strError + " | apikeyHubspot: "+apikeyHubspot;
					if(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave() != null) {
						if(!lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave().equals("")) {
							lstValueProperties = getLstValueProperties();
							if(lstValueProperties.contains(lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave())) {
								strError = strError + " | lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave(): "+lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave();
								objHubSpotData.put("carrera", lstSolicitudDeAdmision.get(0).getCatGestionEscolar().getClave());
							}
						}
					}
					
					
					objHubSpotData = new HashMap<String, String>();
					lstPadresTutor = objPadresTutorDAO.findByCaseId(lstSolicitudDeAdmision.get(0).getCaseId(), 0, 999);
					if(lstPadresTutor != null) {
						if(!lstPadresTutor.empty) {
							for(PadresTutor objPadresTutor : lstPadresTutor) {
								if(objPadresTutor.isIsTutor()) {
									strError = strError + ", ENTRO A TUTOR"
									objHubSpotData.put("correo_tutor", objPadresTutor.getCorreoElectronico() == null ? "" : objPadresTutor.getCorreoElectronico());
									objHubSpotData.put("nombre_de_tutor", (objPadresTutor.getNombre() == null ? "" : objPadresTutor.getNombre())+(objPadresTutor.getApellidos() == null ? "" : " " + objPadresTutor.getApellidos()) );
									objHubSpotData.put("telefono_tutor", objPadresTutor.getTelefono());
									
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
					
					
					
					
					
					catLugarExamenDescripcion = lstSolicitudDeAdmision.get(0).getCatLugarExamen().descripcion;
					
					if(catLugarExamenDescripcion.equals("En un estado")){
						lugarExamen = "México, "+(lstSolicitudDeAdmision.get(0).getCatEstadoExamen() == null ? "" : lstSolicitudDeAdmision.get(0).getCatEstadoExamen().getDescripcion()+", ") + (lstSolicitudDeAdmision.get(0).getCiudadExamen() == null ? "" : lstSolicitudDeAdmision.get(0).getCiudadExamen().getDescripcion());
						strError = strError + " | lugarExamen: "+lugarExamen;
					}
					if(catLugarExamenDescripcion.equals("En el extranjero (solo si vives fuera de México)")){
						lugarExamen = (lstSolicitudDeAdmision.get(0).getCatPaisExamen() == null ? "" : lstSolicitudDeAdmision.get(0).getCatPaisExamen().getDescripcion()+", ")+(lstSolicitudDeAdmision.get(0).getCiudadExamenPais() == null ? "" : lstSolicitudDeAdmision.get(0).getCiudadExamenPais().getDescripcion());
						strError = strError + " | lugarExamen: "+lugarExamen;
					}
					if(catLugarExamenDescripcion.equals("En el mismo campus en donde realizaré mi licenciatura")){
						lugarExamen = lstSolicitudDeAdmision.get(0).getCatCampus() == null ? "" : lstSolicitudDeAdmision.get(0).getCatCampus().getDescripcion();
						strError = strError + " | lugarExamen: "+lugarExamen;
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
					objHubSpotData.put("firstname", lstSolicitudDeAdmision.get(0).getPrimerNombre()+" "+(lstSolicitudDeAdmision.get(0).getSegundoNombre() == null ? "" : lstSolicitudDeAdmision.get(0).getSegundoNombre()));
					objHubSpotData.put("lastname", lstSolicitudDeAdmision.get(0).getApellidoPaterno()+" "+lstSolicitudDeAdmision.get(0).getApellidoMaterno());
					objHubSpotData.put("email", object.email);
					objHubSpotData.put("date_of_birth", dfSalida.format(fechaNacimiento));
					objHubSpotData.put("gender", lstSolicitudDeAdmision.get(0).getCatSexo().getClave() == null ? "" : lstSolicitudDeAdmision.get(0).getCatSexo().getClave());
					//objHubSpotData.put("relationship_status", lstSolicitudDeAdmision.get(0).getCatEstadoCivil().getDescripcion() == null ? "" : lstSolicitudDeAdmision.get(0).getCatEstadoCivil().getDescripcion());
					objHubSpotData.put("promedio", lstSolicitudDeAdmision.get(0).getPromedioGeneral() == null ? "" : lstSolicitudDeAdmision.get(0).getPromedioGeneral());
					objHubSpotData.put("propiedades_de_smart_campus", "Envío de Solicitud");
					strError = strError + "| INFORMACION DE REGISTRO";
					strError = strError + ", GENDER-ZIP";
										
					resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
					strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
				}
			}
			
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
		Result resultadoApiKey = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String nombreCompleto = "";
		String apikeyHubspot="";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			
			Date fecha = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			if(lstCatRegistro != null) {
				if(!lstCatRegistro.empty) {
					resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					apikeyHubspot = (String) resultadoApiKey.getData().get(0);
					strError = strError + " | apikeyHubspot: "+apikeyHubspot;
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
							resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
							strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
						}
					}
					
				}
			}
			
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
		Result resultadoApiKey = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String residencia = "";
		String nombreCompleto = "";
		String tipoAdmision = "";
		String descuento = "";
		String catDescuento = "";
		String apikeyHubspot = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
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
					lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
					strError = strError + " | lstDetalleSolicitud.empty: "+lstDetalleSolicitud.empty;
					strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();
					
					
					if(lstDetalleSolicitud != null) {
						if(!lstDetalleSolicitud.empty) {
							resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
							apikeyHubspot = (String) resultadoApiKey.getData().get(0);
							strError = strError + " | apikeyHubspot: "+apikeyHubspot;
							residencia = lstDetalleSolicitud.get(0).getCatResidencia().getClave().equals("F") ? "R" : (lstDetalleSolicitud.get(0).getCatResidencia().getClave().equals("R") ? "L" : "E");
							tipoAdmision = lstDetalleSolicitud.get(0).getCatTipoAdmision().getClave();
							
							strError = strError + " | residencia: "+residencia;
							strError = strError + " | tipoAdmision: "+tipoAdmision;
							strError = strError + " | getDescuento: "+lstDetalleSolicitud.get(0).getDescuento();
							
							descuento = ""+lstDetalleSolicitud.get(0).getDescuento();
							catDescuento = ""+(lstDetalleSolicitud.get(0).getCatDescuentos()== null ? "" : lstDetalleSolicitud.get(0).getCatDescuentos().getDescuento());
							
							strError = strError + " | descuento: "+descuento;
							strError = strError + " | catDescuento: "+catDescuento;
							
							objHubSpotData.put("tipo_de_alumno", lstDetalleSolicitud.get(0).getCatTipoAlumno().getClave());
							objHubSpotData.put("porcentaje_de_descuento", lstDetalleSolicitud.get(0).getDescuento()==null ? (lstDetalleSolicitud.get(0).getCatDescuentos() == null ? "0" : lstDetalleSolicitud.get(0).getCatDescuentos().getDescuento()):lstDetalleSolicitud.get(0).getDescuento());
							objHubSpotData.put("id_banner", lstDetalleSolicitud.get(0).getIdBanner());
							objHubSpotData.put("fecha_actualizacion_sc", dfSalida.format(fecha));
							objHubSpotData.put("tipo_de_admision", tipoAdmision);
							objHubSpotData.put("aac_bp", residencia);
							objHubSpotData.put("propiedades_de_smart_campus", "Validado");
							resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
							strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
						}
					}
					
				}
			}
			
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
		Result resultadoApiKey = new Result();
		
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String mensaje = "";
		String estatus = "";
		String nombreCompleto = "";
		String apikeyHubspot = "";
		
		Boolean isRechazo=false;
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
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
					
					resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
					apikeyHubspot = (String) resultadoApiKey.getData().get(0);
					strError = strError + " | apikeyHubspot: "+apikeyHubspot;
					if(lstDetalleSolicitud != null) {
						if(!lstDetalleSolicitud.empty) {
							if(isRechazo) {
								mensaje = lstDetalleSolicitud.get(0).getObservacionesRechazo() == null ? "" : lstDetalleSolicitud.get(0).getObservacionesRechazo();
								estatus = "Rechazado";
							}
							else {
								mensaje = lstDetalleSolicitud.get(0).getObservacionesListaRoja() == null ? "" : lstDetalleSolicitud.get(0).getObservacionesListaRoja();
								estatus = "Lista roja";
							}
							objHubSpotData.put("mensaje", mensaje);
							objHubSpotData.put("fecha_actualizacion_sc", dfSalida.format(fecha));
							objHubSpotData.put("propiedades_de_smart_campus", estatus);
							resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
							strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
						}
					}
					
				}
			}
			
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
	
	public Result createOrUpdateRestaurarRechazoLRoja(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoApiKey = new Result();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String apikeyHubspot = "";
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
			apikeyHubspot = (String) resultadoApiKey.getData().get(0);
			strError = strError + " | apikeyHubspot: "+apikeyHubspot;
			Date fecha = new Date();
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			objHubSpotData.put("fecha_actualizacion_sc", dfSalida.format(fecha));
			objHubSpotData.put("propiedades_de_smart_campus", "Envío de Solicitud");
			resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
			strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
			
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
		} catch (Exception e) {
			LOGGER.error "e: "+e.getMessage();
			resultado.setError_info(strError+" | "+(resultado.getError_info() == null ? "" : resultado.getError_info()));
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
	
	public Result createOrUpdatePago(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoCDAO = new Result();
		Result resultadoApiKey = new Result();
				
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		List<SolicitudDeAdmision> lstSolicitudDeAdmision = new ArrayList<SolicitudDeAdmision>();
		List<Map<String, String>> lstOrderDetails = new ArrayList<Map<String, String>>();
		
		Map<String, String> objHubSpotData = new HashMap<String, String>();
		
		String strError = "";
		String mensaje = "";
		String estatus = "";
		String descuento = "";
		String catDescuento = "";
		String nombreCompleto = "";
		String apikeyHubspot = "";
		String jsonPago = "{\"order_id\":\"[ORDERID]\",\"campus_id\":\"[CAMPUSID]\"}";
		
		Date fechaConekta = new Date();
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			assert object instanceof Map;
			def objCatRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			def objDetalleSolicitudDAO = context.apiClient.getDAO(DetalleSolicitudDAO.class);
			def objSolicitudDeAdmisionDAO = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
			
			lstCatRegistro = objCatRegistroDAO.findByCorreoelectronico(object.email, 0, 1);
			lstSolicitudDeAdmision = objSolicitudDeAdmisionDAO.findByCorreoElectronico(object.email, 0, 1);
			
			Date fecha = new Date();
			
			DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			DateFormat dfSalidaHS = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat dfEntradaConekta = new SimpleDateFormat("dd/MM/yyyy");
			
			DecimalFormat df = new DecimalFormat("0");
			
			ConektaDAO cDAO = new ConektaDAO();
			
			NumberFormat formatNumber = NumberFormat.getCurrencyInstance();
			def pesoSigno = '$';
			if(lstCatRegistro != null) {
				if(!lstCatRegistro.empty) {
					strError = strError + " | lstCatRegistro.get(0).getCaseId: "+(lstCatRegistro.get(0).getCaseId() == null ? "" : lstCatRegistro.get(0).getCaseId().toString());
					lstDetalleSolicitud = objDetalleSolicitudDAO.findByCaseId(String.valueOf(lstCatRegistro.get(0).getCaseId()), 0, 1);
					
					strError = strError + " | lstDetalleSolicitud.empty: "+lstDetalleSolicitud.empty;
					strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();

					if(lstDetalleSolicitud != null) {
						if(!lstDetalleSolicitud.empty) {
							
							resultadoApiKey = getApikeyHubspot(lstSolicitudDeAdmision.get(0).getCatCampus().getClave());
							apikeyHubspot = (String) resultadoApiKey.getData().get(0);
							strError = strError + " | apikeyHubspot: "+apikeyHubspot;
							descuento = ""+lstDetalleSolicitud.get(0).getDescuento();
							catDescuento = ""+(lstDetalleSolicitud.get(0).getCatDescuentos()== null ? "" : lstDetalleSolicitud.get(0).getCatDescuentos().getDescuento());
							
							strError = strError + " | descuento: "+descuento;
							strError = strError + " | getOrdenPago: "+lstDetalleSolicitud.get(0).getOrdenPago();
							strError = strError + " | getCatCampus().getPersistenceId: " + lstSolicitudDeAdmision.get(0).getCatCampus().getPersistenceId()
							strError = strError + " | jsonPago: " +jsonPago.replace("[ORDERID]", String.valueOf(lstDetalleSolicitud.get(0).getOrdenPago())).replace("[CAMPUSID]", String.valueOf(lstSolicitudDeAdmision.get(0).getCatCampus().getPersistenceId()));
							resultadoCDAO = cDAO.getOrderDetails(parameterP, parameterC, jsonPago.replace("[ORDERID]", String.valueOf(lstDetalleSolicitud.get(0).getOrdenPago())).replace("[CAMPUSID]", String.valueOf(lstSolicitudDeAdmision.get(0).getCatCampus().getPersistenceId())), context);
							if(resultadoCDAO.isSuccess()) {
								lstOrderDetails = (List<Map<String, String>>) resultadoCDAO.getData();
								strError = strError + " | speiBank: " +lstOrderDetails.get(0).get("speiBank");
								strError = strError + " | CLABE: " +lstOrderDetails.get(0).get("CLABE");
								strError = strError + " | amount: " +lstOrderDetails.get(0).get("amount");
								strError = strError + " | id: " +lstOrderDetails.get(0).get("id");
								strError = strError + " | createdAtDate: " +lstOrderDetails.get(0).get("createdAtDate");
								strError = strError + " | createdAtTime: " +lstOrderDetails.get(0).get("createdAtTime");
								strError = strError + " | type: " +lstOrderDetails.get(0).get("type");
								strError = strError + " | referencia: " +lstOrderDetails.get(0).get("referencia");
								strError = strError + " | cardNumber: " +lstOrderDetails.get(0).get("cardNumber");
								strError = strError + " | authorizationCode: " +lstOrderDetails.get(0).get("authorizationCode");
								strError = strError + " | name: " +lstOrderDetails.get(0).get("name");
								
								strError = strError + " | lstDetalleSolicitud.size: "+lstDetalleSolicitud.size();
								estatus = "Pago";
								if(lstOrderDetails.get(0).get("createdAtDate") != null) {
									fechaConekta = dfEntradaConekta.parse(lstOrderDetails.get(0).get("createdAtDate"));
									objHubSpotData.put("pago_inscripcion", fechaConekta.getTime().toString());
								}
								if(lstOrderDetails.get(0).get("amount") != null) {
									Float monto=Float.parseFloat(lstOrderDetails.get(0).get("amount").toString().replace(pesoSigno, "").replace(" MXN", "").replace("MXN", ""));
									
									objHubSpotData.put("aap_bp", df.format(monto));
								}
								objHubSpotData.put("porcentaje_de_descuento", lstDetalleSolicitud.get(0).getDescuento()==null ? (lstDetalleSolicitud.get(0).getCatDescuentos() == null ? "0" : lstDetalleSolicitud.get(0).getCatDescuentos().getDescuento()):lstDetalleSolicitud.get(0).getDescuento());
								objHubSpotData.put("fecha_actualizacion_sc", dfSalida.format(fecha));
								objHubSpotData.put("propiedades_de_smart_campus", estatus);
								resultado = createOrUpdateHubspot(object.email, apikeyHubspot, objHubSpotData);
								strError = strError + (resultado.getError_info() == null ? "NULL INFO" : "|" + resultado.getError_info() + "|");
							}
							else {
								throw new Exception(resultadoCDAO.getError());
							}
						}
					}
					
				}
			}

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
	
	public List<String> getLstValueProperties() throws Exception {
		List<String> lstResultado = new ArrayList<String>();
		String jsonData = "";
		String data="8b-.-.-.b0-.-.-.a-.-.-.1ac-df-.-.-.54-40-.-.-.bf-b5-.-.-.69-40-.-.-.e8-.-.-.7f-.-.-.90-.-.-.c0-.-.-.99"
		String urlParaVisitar = "https://api.hubapi.com/properties/v1/contacts/properties/named/carrera?hapikey="+data.replace("-.-.-.", "");
		StringBuilder resultado = new StringBuilder();
		
		URL url = new URL(urlParaVisitar);
		HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
		conexion.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
		String linea;
		while ((linea = rd.readLine()) != null) {
			resultado.append(linea);
		}
		rd.close();
		
		jsonData = resultado.toString()
		
		def jsonSlurper = new JsonSlurper();
		def object = jsonSlurper.parseText(jsonData);
		assert object instanceof Map;
		if(object.options != null) {
			assert object.options instanceof List;
		}
		
		
		for(def row: object.options) {
			assert row instanceof Map;
			if(!row.value.equals("")){
				lstResultado.add(row.value);
			}
		}
		
		return lstResultado;
	}
	
	public Result createOrUpdateHubspot(String email, String apikeyHubspot, Map<String, String> objHubSpotData) {
		Result resultado = new Result();
		//String data="8b-.-.-.b0-.-.-.a-.-.-.1ac-df-.-.-.54-40-.-.-.bf-b5-.-.-.69-40-.-.-.e8-.-.-.7f-.-.-.90-.-.-.c0-.-.-.99"
		String targetURL = "https://api.hubapi.com/contacts/v1/contact/createOrUpdate/email/[EMAIL]/?hapikey="+apikeyHubspot;
		String jsonInputString = "{\"properties\":[{\"property\":\"firstname\",\"value\":\"java\"},{\"property\":\"nombre\",\"value\":\"Arturo\"},{\"property\":\"lastname\",\"value\":\"Zamorano\"},{\"property\":\"nombre_completo\",\"value\":\"Arturo Zamorano\"},{\"property\":\"correo_electrnico\",\"value\":\"jasz189@hotmail.com\"},{\"property\":\"date_of_birth\",\"value\":\"2020-11-30T23:51:03.309Z\"},{\"property\":\"fecha_de_nacimiento\",\"value\":\"654307200000\"},{\"property\":\"twitterhandle\",\"value\":\"arturoZCZ\"},{\"property\":\"gender\",\"value\":\"Masculino\"},{\"property\":\"country\",\"value\":\"México\"},{\"property\":\"state\",\"value\":\"Sonora\"},{\"property\":\"ciudad\",\"value\":\"Navojoa\"},{\"property\":\"city\",\"value\":\"Navojoa\"},{\"property\":\"address\",\"value\":\"Callejon 3\"},{\"property\":\"celular\",\"value\":\"6421344161\"},{\"property\":\"phone\",\"value\":\"6421344161\"},{\"property\":\"zip\",\"value\":\"85890\"},{\"property\":\"promedio\",\"value\":\"9.5\"},{\"property\":\"promedio_de_preparatoria\",\"value\":\"9.5\"},{\"property\":\"relationship_status\",\"value\":\"Casado\"},{\"property\":\"nombre_de_tutor\",\"value\":\"Arturo\"},{\"property\":\"apellido_tutor\",\"value\":\"Zamorano\"},{\"property\":\"celular_de_tutor\",\"value\":\"6421344161\"},{\"property\":\"correo_tutor\",\"value\":\"arturo.zamorano@gmail.com\"},{\"property\":\"telefono_tutor\",\"value\":\"6421344161\"},{\"property\":\"nombre_del_padre\",\"value\":\"Arturo\"},{\"property\":\"apellido_paterno\",\"value\":\"Zamorano\"},{\"property\":\"celular_del_padre\",\"value\":\"6421344161\"},{\"property\":\"correo_del_padre\",\"value\":\"arturo.zamorano@gmail.com\"},{\"property\":\"telefono_del_padre\",\"value\":\"6421344161\"},{\"property\":\"nombre_de_la_madre\",\"value\":\"Guadalupe\"},{\"property\":\"apellido_materno\",\"value\":\"Sainz\"},{\"property\":\"celular_de_la_madre\",\"value\":\"6421344161\"},{\"property\":\"correo_de_la_madre\",\"value\":\"eva.sainz@gmail.com\"},{\"property\":\"telefono_de_la_madre\",\"value\":\"6421344161\"},{\"property\":\"ua_vpd\",\"value\":\"UAM\"},{\"property\":\"campus_destino\",\"value\":\"AMAY\"},{\"property\":\"tipo_de_alumno\",\"value\":\"N\"}]}";
		String strError = "";
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		
		JSONObject jsonItem = new JSONObject();
		JSONObject jsonProperties = new JSONObject();
		JSONArray jsonList = new JSONArray();
		
		try {
			strError = strError + ", INICIO";
			strError = strError + "| ==============================================";
			strError = strError + "| apikeyHubspot: "+apikeyHubspot;
			strError = strError + "| ==============================================";
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
	
	public Result getApikeyHubspot(String claveCampus) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String strError = "";

		List<String> lstResultado = new ArrayList<String>();

		try {
			  closeCon = validarConexion();
			  pstm = con.prepareStatement(Statements.GET_HUBSPOT_KEY_BY_CAMPUS)
			  pstm.setString(1, claveCampus)
			  rs = pstm.executeQuery()
			  if (rs.next()) {
					lstResultado.add(rs.getString("hubspotkey"));
			  }
			  resultado.setData(lstResultado);
			  resultado.setError_info(strError);
			  resultado.setSuccess(true);
		} catch (Exception e) {
			  resultado.setError_info(strError);
			  resultado.setSuccess(false);
			  resultado.setError(e.getMessage());
			  e.printStackTrace();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
  }
}

