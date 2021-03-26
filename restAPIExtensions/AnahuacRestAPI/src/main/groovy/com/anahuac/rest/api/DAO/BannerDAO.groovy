package com.anahuac.rest.api.DAO

import groovy.json.JsonSlurper

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalTime

import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.json.JSONArray
import org.json.JSONObject
import org.json.simple.parser.JSONParser

import com.anahuac.catalogos.CatBachilleratos
import com.anahuac.catalogos.CatBachilleratosDAO
import com.anahuac.rest.api.Entity.CatBachillerato
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.engine.api.ProcessAPI
import com.bonitasoft.web.extension.rest.RestAPIContext

class BannerDAO {
	
	public Result buscarCambiosBannerPreparatoria(RestAPIContext context, String operacion) {
		Result resultado = new Result();
		Result resultadoGetConsumeJSON = new Result();
		
		String errorLog = "";
		String barrerToken = "";
		String jsonResultado = "";
		String strGetConsumeJSON = "";
		try {
			errorLog += " | "+("START JSON======================================");
			barrerToken = getBarreToken();
			errorLog += " | "+barrerToken;
			errorLog += " | "+("================================================");
			jsonResultado = getConsumePrepa(barrerToken);
			errorLog += " | "+jsonResultado;
			errorLog += " | "+("END JSON========================================");
			//jsonResultado = "[{\"id\": \"9\",\"published\": \"2021-02-25 20:24:49.790915+00\",\"resource\": {\"name\": \"educational-institutions\",\"id\": \"a6693f89-8c92-47cd-9b13-5a50c21f019a\",\"version\": \"application/vnd.hedtech.integration.v6+json\"},\"operation\": \"replaced\",\"contentType\": \"resource-representation\",\"content\": {\"addresses\": [{\"address\": {\"id\": \"b7cb1570-f8b9-4452-bb8b-a873a52f4a5a\"},\"type\": {\"addressType\": \"school\"}}],\"homeInstitution\": \"external\",\"id\": \"a6693f89-8c92-47cd-9b13-5a50c21f019a\",\"title\": \"BACH GRUPO EDUCATIVO IMEI JABB\",\"type\": \"secondarySchool\"},\"publisher\": {\"id\": \"c9d2d963-68db-445d-a874-c9c103aa32ba\",\"applicationName\": \"RUAD INTEGRATION API (Shared Data)\"}}, {\"id\": \"10\",\"published\": \"2021-02-25 20:25:28.786177+00\",\"resource\": {\"name\": \"addresses\",\"id\": \"b7cb1570-f8b9-4452-bb8b-a873a52f4a5a\",\"version\": \"application/vnd.hedtech.integration.v11.1.0+json\"},\"operation\": \"replaced\",\"contentType\": \"resource-representation\",\"content\": {\"addressLines\": [\"Miguel Domínguez 63\",\"Mz 37 Lt 23\",\"Nueva Aragón JABB\"],\"id\": \"b7cb1570-f8b9-4452-bb8b-a873a52f4a5a\",\"place\": {\"country\": {\"code\": \"MEX\",\"locality\": \"Ecatepec de Morelos\",\"postalCode\": \"55260\",\"postalTitle\": \"MEXICO\",\"region\": {\"title\": \"Estado de México\"},\"subRegion\": {\"title\": \"Ecatepec de Morelos\"},\"title\": \"México\"}}},\"publisher\": {\"id\": \"a216d744-fb37-413e-8430-7f187c223bda\",\"applicationName\": \"RUAD INTEGRATION API-UAN\"}}]";
			//jsonResultado = "[{\"id\": \"13\", \"published\": \"2021-03-10 18:05:34.167213+00\", \"resource\": {\"name\": \"educational-institutions\", \"id\": \"14b58e15-66d4-4dfa-9e7e-c0993e0d8f41\", \"version\": \"application/vnd.hedtech.integration.v6+json\"}, \"operation\": \"created\", \"contentType\": \"resource-representation\", \"content\": {\"homeInstitution\": \"external\", \"id\": \"14b58e15-66d4-4dfa-9e7e-c0993e0d8f41\", \"title\": \"Preparatoria Oficial 34\", \"type\": \"secondarySchool\"}, \"publisher\": {\"id\": \"c9d2d963-68db-445d-a874-c9c103aa32ba\", \"applicationName\": \"RUAD INTEGRATION API (Shared Data)\"} }, {\"id\": \"14\", \"published\": \"2021-03-10 18:07:07.846474+00\", \"resource\": {\"name\": \"addresses\", \"id\": \"e19a7cee-46b8-48e8-bdb7-1a9625b77d64\", \"version\": \"application/vnd.hedtech.integration.v11.1.0+json\"}, \"operation\": \"created\", \"contentType\": \"resource-representation\", \"content\": {\"addressLines\": [\"Calle Lilas 87\", \"Col. San Rafael\"], \"id\": \"e19a7cee-46b8-48e8-bdb7-1a9625b77d64\", \"place\": {\"country\": {\"code\": \"MEX\", \"locality\": \"Ciudad de México\", \"postalCode\": \"06470\", \"postalTitle\": \"MEXICO\", \"region\": {\"title\": \"Ciudad de México\"}, \"subRegion\": {\"title\": \"Cuauhtémoc\"}, \"title\": \"México\"} } }, \"publisher\": {\"id\": \"a216d744-fb37-413e-8430-7f187c223bda\", \"applicationName\": \"RUAD INTEGRATION API-UAN\"} } ]";
			resultadoGetConsumeJSON = getConsumeJSON(jsonResultado, context, operacion, barrerToken);
			errorLog += " | "+strGetConsumeJSON;
			resultado.setSuccess(true);
			resultado.setError_info(errorLog);
		} catch (Exception e) {
			errorLog += " | "+e.getMessage();
			resultado.setError_info(errorLog);
			e.printStackTrace()
		}
		
		return resultadoGetConsumeJSON;
	}
	
	private String getBarreToken() {
		String urlParaVisitar = "https://integrate.elluciancloud.com/auth";
		String barrerKey = "Bearer 00b3-.-.-990b-5c1b-4-.-.-a4d-840c-69-.-.-a47eab34f0";
		StringBuilder resultado = new StringBuilder();
		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization",barrerKey.replace("-.-.-", ""));
			conexion.setRequestMethod("POST");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
			//System.out.println(urlParaVisitar);
			//System.out.println(resultado.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultado.toString();
	}
	
	private String getConsumePrepa(String barrerToken) {
		String urlParaVisitar = "https://integrate.elluciancloud.com/consume";
		StringBuilder resultado = new StringBuilder();
		
		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization","Bearer "+barrerToken);
			conexion.setRequestProperty("Accept","application/vnd.hedtech.change-notifications.v2+json");
			conexion.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
			//System.out.println(urlParaVisitar);
			//System.out.println(resultado.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultado.toString();
	}
	
	private Result getConsumeJSON(String jsonResultado, RestAPIContext context, String operacion, String barrerToken) {
		Result resultado = new Result();
		
		Integer indexAddress = null;
		
		JSONObject objJson = null;
		JSONObject objJsonAddressesData = null;
		JSONObject objJsonAddressesDataAddress = null;
		JSONObject objJsonResource = null;
		JSONObject objJsonContent = null;
		JSONObject objJsonPlace = null;
		JSONObject objJsonCountry = null;
		JSONObject objJsonRegion = null;
		JSONObject objJsonPublisher=null;
		JSONObject objJsonAddressData=null;
		
		JSONArray objJsonAddresses = null;
		
		JSONArray lstJson = null;
		
		JSONParser parser = new JSONParser();
		
		CatBachillerato objEducationalInstitutions= new CatBachillerato();
		List<CatBachillerato> lstEducationalInstitutions= new ArrayList<CatBachillerato>();
		
		CatBachillerato objAddresses = new CatBachillerato();
		List<CatBachillerato> lstAddresses= new ArrayList<CatBachillerato>();
		
		CatBachillerato objCatBachillerato= new CatBachillerato();
		List<CatBachillerato> lstCatBachillerato= new ArrayList<CatBachillerato>();
		
		DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		
		String errorLog = "";
		String resultEducationalInstitutions = "";
		
		List<CatBachilleratos> lstCatBachilleratos = new ArrayList<CatBachilleratos>();
		
		Long processId = null;
		Long processIdCrear = null;
		
		ProcessInstance processInstance = null;
		ProcessAPI processAPI = null;
		
		Map<String, Serializable> contracto = new HashMap<String, Serializable>();
		Map<String, Serializable> objCatBachilleratosInput = new HashMap<String, Serializable>();
		List<Map<String, Serializable>>lstCatBachilleratosInput = new ArrayList<Map<String, Serializable>>();
		SearchOptionsBuilder searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
		List<ProcessDeploymentInfo> lstProcessDeploymentInfo = new ArrayList<ProcessDeploymentInfo>();
		SearchOptions searchOptionsProccess = null;
		try {
			lstJson = (JSONArray) parser.parse(jsonResultado);
			
			Iterator<JSONObject> iterator = lstJson.iterator();
			
			System.out.println("--------------------------------------------------------");
            while (iterator.hasNext()) {
            	
            	objJson = iterator.next();
            	objJsonResource = (JSONObject) objJson.get("resource");
            	objJsonContent = (JSONObject) objJson.get("content");
            	objJsonPublisher = (JSONObject) objJson.get("publisher");
            	
            	
            	objEducationalInstitutions= new CatBachillerato();
            	objAddresses = new CatBachillerato();
            	
            	switch(objJsonResource.get("name").toString()) {
            		case "educational-institutions":
						errorLog = errorLog + " | educational-institutions";
						errorLog = errorLog + " | " + ("idBachillerato: "+objJsonContent.get("id").toString());
						errorLog = errorLog + " | " + ("descripcion: "+objJsonContent.get("title").toString());
						errorLog = errorLog + " | " + ("usuarioBanner: "+objJsonPublisher.get("applicationName").toString());
						errorLog = errorLog + " | " + ("fechaImportacion: "+objJson.get("published").toString());
						errorLog = errorLog + " | " + ("fechaCreacion: "+objJson.get("published").toString());
            			
            			objEducationalInstitutions.setIdBachillerato(objJsonContent.get("id").toString());
            			objEducationalInstitutions.setDescripcion(objJsonContent.get("title").toString());
            			objEducationalInstitutions.setUsuarioBanner(objJsonPublisher.get("applicationName").toString());
            			objEducationalInstitutions.setFechaImportacion(objJson.get("published").toString());
            			objEducationalInstitutions.setFechaCreacion(objJson.get("published").toString());
            			objEducationalInstitutions.setOperation(objJson.get("operation").toString());
            			
						/*
            			Iterator<JSONObject> iteratorAddresses = objJsonAddresses.iterator();
            			while (iteratorAddresses.hasNext()) {
							
            				objJsonAddressesData = iteratorAddresses.next();
            				objJsonAddressesDataAddress = (JSONObject) objJsonAddressesData.get("address");
            				errorLog = errorLog + " | " + ("idDireccion: "+objJsonAddressesDataAddress.get("id").toString());
            				objEducationalInstitutions.setIdDireccion(objJsonAddressesDataAddress.get("id").toString());
            			}*/
						
						if(!objJson.get("operation").toString().equals("created")) {
							objJsonAddresses =(JSONArray) objJsonContent.get("addresses");
							Iterator<JSONObject> iteratorAddresses = objJsonAddresses.iterator();
							while (iteratorAddresses.hasNext()) {
								objJsonAddressesData = iteratorAddresses.next();
								objJsonAddressesDataAddress = (JSONObject) objJsonAddressesData.get("address");
								errorLog = errorLog + " | " + ("idDireccion: "+objJsonAddressesDataAddress.get("id").toString());
								objEducationalInstitutions.setIdDireccion(objJsonAddressesDataAddress.get("id").toString());
							}
						}
						else {
							resultEducationalInstitutions = getConsumeEducationalInstitutions(barrerToken, objJsonContent.get("id").toString());
							errorLog = errorLog + " | " + ("====================================");
							errorLog = errorLog + " | " + (resultEducationalInstitutions);
							objJsonAddressData = (JSONObject) parser.parse(resultEducationalInstitutions);
							objJsonAddresses = (JSONArray) objJsonAddressData.get("addresses");
							Iterator<JSONObject> iteratorAddresses = objJsonAddresses.iterator();
							while (iteratorAddresses.hasNext()) {
								objJsonAddressesData = iteratorAddresses.next();
								objJsonAddressesDataAddress = (JSONObject) objJsonAddressesData.get("address");
								errorLog = errorLog + " | " + ("idDireccion: "+objJsonAddressesDataAddress.get("id").toString());
								objEducationalInstitutions.setIdDireccion(objJsonAddressesDataAddress.get("id").toString());
							}
							
						}
            			lstEducationalInstitutions.add(objEducationalInstitutions);
            			
            			break;
            		case "addresses":
						errorLog = errorLog + " | addresses";
            			objJsonPlace = (JSONObject) objJsonContent.get("place");
            			objJsonCountry = (JSONObject) objJsonPlace.get("country");
            			objJsonRegion = (JSONObject) objJsonCountry.get("region");
						
						errorLog = errorLog + " | " + ("idDireccion: "+objJsonContent.get("id").toString());
						errorLog = errorLog + " | " + ("pais: "+objJsonCountry.get("title").toString());
						errorLog = errorLog + " | " + ("Estado: "+objJsonRegion.get("title").toString());
						errorLog = errorLog + " | " + ("ciudad: "+objJsonCountry.get("locality").toString());
            			
            			objAddresses.setIdDireccion(objJsonContent.get("id").toString());
            			objAddresses.setPais(objJsonCountry.get("title").toString());
            			objAddresses.setEstado(objJsonRegion.get("title").toString());
            			objAddresses.setCiudad(objJsonCountry.get("locality").toString());
            			
            			lstAddresses.add(objAddresses);
            			break;
            		default:
            			errorLog = errorLog + " | " + (objJsonResource.get("name").toString());
            			break;
            	}
            	
            }
            errorLog = errorLog + " | " + ("--------------------------------------------------------");
            
            for(CatBachillerato row : lstEducationalInstitutions) {
            	objCatBachillerato= new CatBachillerato();
				objCatBachillerato.setIdBachillerato(row.getIdBachillerato());
            	objCatBachillerato.setDescripcion(row.getDescripcion());
            	objCatBachillerato.setUsuarioBanner(row.getUsuarioBanner());
            	objCatBachillerato.setFechaImportacion(row.getFechaImportacion());
            	objCatBachillerato.setFechaCreacion(row.getFechaCreacion());
            	objCatBachillerato.setOperation(row.getOperation());
            	objCatBachillerato.setIsEliminado(false);
            	objCatBachillerato.setIsEnabled(true);
            	objCatBachillerato.setPerteneceRed(false);
            	
            	if(lstAddresses.contains(row)) {
            		indexAddress = lstAddresses.indexOf(row);
            		if(indexAddress!= -1) {
            			objCatBachillerato.setIdDireccion(lstAddresses.get(indexAddress).getIdDireccion());
            			objCatBachillerato.setPais(lstAddresses.get(indexAddress).getPais());
            			objCatBachillerato.setEstado(lstAddresses.get(indexAddress).getEstado());
            			objCatBachillerato.setCiudad(lstAddresses.get(indexAddress).getCiudad());
            		}
            	}
            	
            	lstCatBachillerato.add(objCatBachillerato);
            }
			
			processInstance = null;
			processAPI = context.getApiClient().getProcessAPI();
			
			contracto = new HashMap<String, Serializable>();
			objCatBachilleratosInput = new HashMap<String, Serializable>();
			lstCatBachilleratosInput = new ArrayList<Map<String, Serializable>>();
			
			searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Editar Bachilleratos");
			searchOptionsProccess = searchBuilderProccess.done();
			SearchResult<ProcessDeploymentInfo> SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
			lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
			for(ProcessDeploymentInfo  objProcessDeploymentInfo : lstProcessDeploymentInfo) {
				if(objProcessDeploymentInfo.getActivationState().toString().equals("ENABLED")) {
					processId = objProcessDeploymentInfo.getProcessId();
				}
			}
			
			searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Agregar Bachilleratos");
			searchOptionsProccess = searchBuilderProccess.done();
			SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
			lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
			for(ProcessDeploymentInfo  objProcessDeploymentInfo : lstProcessDeploymentInfo) {
				if(objProcessDeploymentInfo.getActivationState().toString().equals("ENABLED")) {
					processIdCrear = objProcessDeploymentInfo.getProcessId();
				}
			}
			
			/*--------------------------------------------------------------------------------------------------------------*/
			for(CatBachillerato row : lstCatBachillerato) {
				if(row.getOperation().equals("replaced")) {
				//if(operacion.equals("replaced")) {					
					errorLog = errorLog + " | " + row.getOperation();
					
					def catBachilleratosDAO = context.getApiClient().getDAO(CatBachilleratosDAO.class);
					lstCatBachilleratos = catBachilleratosDAO.findByDescripcion(row.getDescripcion(), 0, 100);
					if(lstCatBachilleratos != null) {
						for(CatBachilleratos objRow : lstCatBachilleratos) {
							errorLog = errorLog + " | PersistenceId:" + objRow.getPersistenceId();
							errorLog = errorLog + " | Descripcion:" + objRow.getDescripcion();
							errorLog = errorLog + " | ================================================================== | ";
							lstCatBachilleratosInput = new ArrayList<Map<String, Serializable>>();
							objCatBachilleratosInput = new HashMap<String, Serializable>();
							contracto = new HashMap<String, Serializable>();
							/*CONSTRUCCION DE CONTRATO=====================================================================*/
							objCatBachilleratosInput.put("persistenceId", objRow.getPersistenceId());
							objCatBachilleratosInput.put("persistenceVersion", objRow.getPersistenceVersion());
							objCatBachilleratosInput.put("isEliminado", false);
							objCatBachilleratosInput.put("isEnabled", true);
							objCatBachilleratosInput.put("todelete", "false");
							objCatBachilleratosInput.put("perteneceRed", false);
							objCatBachilleratosInput.put("region", null);
							objCatBachilleratosInput.put("caseId", null);
							objCatBachilleratosInput.put("clave", objRow.getClave());
							objCatBachilleratosInput.put("fechaImportacion", null);
							objCatBachilleratosInput.put("fechaCreacion", null);
							objCatBachilleratosInput.put("usuarioCreacion", "Administrador");
							objCatBachilleratosInput.put("descripcion", objRow.getDescripcion());
							objCatBachilleratosInput.put("usuarioBanner", row.getUsuarioBanner());
							objCatBachilleratosInput.put("estado", row.getEstado());
							objCatBachilleratosInput.put("ciudad", row.getCiudad());
							objCatBachilleratosInput.put("pais", row.getPais());
							objCatBachilleratosInput.put("id", row.getIdBachillerato());
							lstCatBachilleratosInput.add(objCatBachilleratosInput);
							contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
							processInstance = processAPI.startProcessWithInputs(processId, contracto);
						}
					}
				}
				else {
					if(row.getOperation().equals("created")) {
					//if(operacion.equals("created")) {
						errorLog = errorLog + " | " + row.getOperation();
						errorLog = errorLog + " | processIdCrear: " + processIdCrear;
						lstCatBachilleratosInput = new ArrayList<Map<String, Serializable>>();
						objCatBachilleratosInput = new HashMap<String, Serializable>();
						contracto = new HashMap<String, Serializable>();
						/*CONSTRUCCION DE CONTRATO=====================================================================*/
						objCatBachilleratosInput.put("isEliminado", false);
						objCatBachilleratosInput.put("isEnabled", true);
						objCatBachilleratosInput.put("todelete", "false");
						objCatBachilleratosInput.put("perteneceRed", false);
						objCatBachilleratosInput.put("region", null);
						objCatBachilleratosInput.put("caseId", null);
						objCatBachilleratosInput.put("clave", row.getClave());
						objCatBachilleratosInput.put("fechaImportacion", null);
						objCatBachilleratosInput.put("fechaCreacion", null);
						objCatBachilleratosInput.put("usuarioCreacion", "Administrador");
						objCatBachilleratosInput.put("descripcion", row.getDescripcion());
						objCatBachilleratosInput.put("usuarioBanner", row.getUsuarioBanner());
						objCatBachilleratosInput.put("estado", row.getEstado());
						objCatBachilleratosInput.put("ciudad", row.getCiudad());
						objCatBachilleratosInput.put("pais", row.getPais());
						objCatBachilleratosInput.put("id", row.getIdBachillerato());
						lstCatBachilleratosInput.add(objCatBachilleratosInput);
						contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
						processInstance = processAPI.startProcessWithInputs(processIdCrear, contracto);
						errorLog = errorLog + " | processInstance: " + processInstance;
					}
					else {
						if(row.getOperation().equals("deleted")) {
						//if(operacion.equals("deleted")) {
							errorLog = errorLog + " | " + row.getOperation();
							
							def catBachilleratosDAO = context.getApiClient().getDAO(CatBachilleratosDAO.class);
							lstCatBachilleratos = catBachilleratosDAO.findByDescripcion(row.getDescripcion(), 0, 100);
							if(lstCatBachilleratos != null) {
								for(CatBachilleratos objRow : lstCatBachilleratos) {
									errorLog = errorLog + " | PersistenceId:" + objRow.getPersistenceId();
									errorLog = errorLog + " | Descripcion:" + objRow.getDescripcion();
									errorLog = errorLog + " | ================================================================== | ";
									lstCatBachilleratosInput = new ArrayList<Map<String, Serializable>>();
									objCatBachilleratosInput = new HashMap<String, Serializable>();
									contracto = new HashMap<String, Serializable>();
									/*CONSTRUCCION DE CONTRATO====================================================================*/
									objCatBachilleratosInput.put("persistenceId", objRow.getPersistenceId());
									objCatBachilleratosInput.put("persistenceVersion", objRow.getPersistenceVersion());
									objCatBachilleratosInput.put("isEliminado", true);
									objCatBachilleratosInput.put("isEnabled", true);
									objCatBachilleratosInput.put("todelete", "true");
									objCatBachilleratosInput.put("perteneceRed", false);
									objCatBachilleratosInput.put("region", null);
									objCatBachilleratosInput.put("caseId", null);
									objCatBachilleratosInput.put("clave", objRow.getClave());
									objCatBachilleratosInput.put("fechaImportacion", null);
									objCatBachilleratosInput.put("fechaCreacion", null);
									objCatBachilleratosInput.put("usuarioCreacion", "Administrador");
									objCatBachilleratosInput.put("descripcion", objRow.getDescripcion());
									objCatBachilleratosInput.put("usuarioBanner", row.getUsuarioBanner());
									objCatBachilleratosInput.put("estado", row.getEstado());
									objCatBachilleratosInput.put("ciudad", row.getCiudad());
									objCatBachilleratosInput.put("pais", row.getPais());
									objCatBachilleratosInput.put("id", row.getIdBachillerato());
									lstCatBachilleratosInput.add(objCatBachilleratosInput);
									contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
									processInstance = processAPI.startProcessWithInputs(processId, contracto);
								}
							}					
						}
					}
				}
			}
            
            resultado.setData(lstCatBachillerato);
			resultado.setSuccess(true);
			resultado.setError_info(errorLog);
            
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError_info(errorLog);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado;
	}
	
	private static String getConsumeEducationalInstitutions(String barrerToken, String idBachillerato) {
		String urlParaVisitar = "https://integrate.elluciancloud.com/api/educational-institutions/"+idBachillerato;
		StringBuilder resultado = new StringBuilder();
		
		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization","Bearer "+barrerToken);
			conexion.setRequestProperty("Accept","application/vnd.hedtech.integration.v6+json");
			conexion.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
			/*System.out.println(urlParaVisitar);
			System.out.println(resultado.toString());*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultado.toString();
	}
}
