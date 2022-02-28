package com.anahuac.rest.api;

import com.anahuac.rest.api.Entity.Result
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.http.HttpHeaders
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.bonitasoft.web.extension.rest.RestAPIContext
import org.bonitasoft.web.extension.rest.RestApiController

import java.time.LocalDate

class IndexPost implements RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexPost.class)

    @Override
    RestApiResponse doHandle(HttpServletRequest request, RestApiResponseBuilder responseBuilder, RestAPIContext context) {
        // To retrieve query parameters use the request.getParameter(..) method.
        // Be careful, parameter values are always returned as String values
		String errorlog=""
        // Retrieve url parameter
        def url = request.getParameter "url"
        if (url == null) {
            return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter url is missing"}""")
        }
		Result result = new Result()
		switch(url) {
			case "instantiation": 
			String jsonData = request.reader.readLines().join("\n")
			try {
				errorlog+="[1] "
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				//assert object instanceof Map
				Map<String, Serializable> contract = new HashMap<String, Serializable>();
				Map<String, Serializable> catRegistroInput = new HashMap<String, Serializable>();
				
				catRegistroInput.put("apellidomaterno",object.catRegistroInput.apellidomaterno);
				catRegistroInput.put("apellidopaterno",object.catRegistroInput.apellidopaterno);
				catRegistroInput.put("ayuda",object.catRegistroInput.ayuda);
				catRegistroInput.put("catCampus",object.catRegistroInput.catCampus);
				catRegistroInput.put("catGestionEscolar",object.catRegistroInput.catGestionEscolar);
				catRegistroInput.put("correoelectronico",object.catRegistroInput.correoelectronico);
				catRegistroInput.put("isEliminado",object.catRegistroInput.isEliminado);
				catRegistroInput.put("nombreusuario",object.catRegistroInput.nombreusuario);
				catRegistroInput.put("password",object.catRegistroInput.password);
				catRegistroInput.put("primernombre",object.catRegistroInput.primernombre);
				catRegistroInput.put("segundonombre",object.catRegistroInput.segundonombre);
				
				contract.put("catRegistroInput",catRegistroInput)
				errorlog+="[2] "
				Map<String, Serializable> catSolicitudDeAdmisionInput = new HashMap<String, Serializable>();
				
				catSolicitudDeAdmisionInput.put("apellidoMaterno",object.catSolicitudDeAdmisionInput.apellidoMaterno);
				catSolicitudDeAdmisionInput.put("apellidoPaterno",object.catSolicitudDeAdmisionInput.apellidoPaterno);
				catSolicitudDeAdmisionInput.put("avisoPrivacidad",object.catSolicitudDeAdmisionInput.avisoPrivacidad);
				
				def mapcatCampus = [:]
				for ( prop in object.catSolicitudDeAdmisionInput.catCampus ) {
					mapcatCampus[prop.key] = prop.value
					if(prop.key == "persistenceId") {
						mapcatCampus["persistenceId_string"] = prop.value+"";
					}
				}
				catSolicitudDeAdmisionInput.put("catCampus",mapcatCampus);
				
				def mapcatCampusEstudio = [:]
				for ( prop in object.catSolicitudDeAdmisionInput.catCampusEstudio ) {
					mapcatCampusEstudio[prop.key] = prop.value
					if(prop.key == "persistenceId") {
						mapcatCampusEstudio["persistenceId_string"] = prop.value+"";
					}
				}
				catSolicitudDeAdmisionInput.put("catCampusEstudio",mapcatCampusEstudio);
				
				def mapcatEstadoExamen = [:]
				for ( prop in object.catSolicitudDeAdmisionInput.catEstadoExamen ) {
					mapcatEstadoExamen[prop.key] = prop.value
					if(prop.key == "persistenceId") {
						mapcatEstadoExamen["persistenceId_string"] = prop.value+"";
					}
				}
				if(object.catSolicitudDeAdmisionInput.catEstadoExamen == null) {
					catSolicitudDeAdmisionInput.put("catEstadoExamen",null);
				}else {
					catSolicitudDeAdmisionInput.put("catEstadoExamen",mapcatEstadoExamen);
				}
				
				def mapcatGestionEscolar = [:]
				for ( prop in object.catSolicitudDeAdmisionInput.catGestionEscolar ) {
					mapcatGestionEscolar[prop.key] = prop.value
					if(prop.key == "persistenceId") {
						mapcatGestionEscolar["persistenceId_string"] = prop.value+"";
					}
				}				
				catSolicitudDeAdmisionInput.put("catGestionEscolar",mapcatGestionEscolar);
				
				def mapcatLugarExamen = [:]
				for ( prop in object.catSolicitudDeAdmisionInput.catLugarExamen ) {
					mapcatLugarExamen[prop.key] = prop.value
					if(prop.key == "persistenceId") {
						mapcatLugarExamen["persistenceId_string"] = prop.value+"";
					}
				}
				catSolicitudDeAdmisionInput.put("catLugarExamen",mapcatLugarExamen);
				
				def mapcatPaisExamen = [:]
				for ( prop in object.catSolicitudDeAdmisionInput.catPaisExamen ) {
					mapcatPaisExamen[prop.key] = prop.value
					if(prop.key == "persistenceId") {
						mapcatPaisExamen["persistenceId_string"] = prop.value+"";
					}
				}
				if(object.catSolicitudDeAdmisionInput.catPaisExamen == null) {
					catSolicitudDeAdmisionInput.put("catPaisExamen",null);
				}else {
					catSolicitudDeAdmisionInput.put("catPaisExamen",mapcatPaisExamen);
				}
				
				
				//assert object.catSolicitudDeAdmisionInput.catPeriodo instanceof Map
				def mapcatPeriodo = [:]
				for ( prop in object.catSolicitudDeAdmisionInput.catPeriodo ) {
					mapcatPeriodo[prop.key] = prop.value
					if(prop.key == "persistenceId") {
						mapcatPeriodo["persistenceId_string"] = prop.value+"";
					}
				}
				errorlog+="[3] "
				catSolicitudDeAdmisionInput.put("catPeriodo",mapcatPeriodo);
				
				def mapcatPropedeutico = [:]
				for ( prop in object.catSolicitudDeAdmisionInput.catPropedeutico ) {
					mapcatPropedeutico[prop.key] = prop.value
					if(prop.key == "persistenceId") {
						mapcatPropedeutico["persistenceId_string"] = prop.value+"";
					}
				}
				if(object.catSolicitudDeAdmisionInput.catPropedeutico == null) {
					errorlog+="[catPropedeutico] es null"
					catSolicitudDeAdmisionInput.put("catPropedeutico",null);
				}else {
					errorlog+="[catPropedeutico] es tiene valor"
					catSolicitudDeAdmisionInput.put("catPropedeutico",mapcatPropedeutico);
				}
				
				catSolicitudDeAdmisionInput.put("ciudadExamen",object.catSolicitudDeAdmisionInput.ciudadExamen);
				catSolicitudDeAdmisionInput.put("ciudadExamenPais",object.catSolicitudDeAdmisionInput.ciudadExamenPais);
				catSolicitudDeAdmisionInput.put("correoElectronico",object.catSolicitudDeAdmisionInput.correoElectronico);
				catSolicitudDeAdmisionInput.put("isEliminado",object.catSolicitudDeAdmisionInput.isEliminado);
				catSolicitudDeAdmisionInput.put("necesitoAyuda",object.catSolicitudDeAdmisionInput.necesitoAyuda);
				catSolicitudDeAdmisionInput.put("primerNombre",object.catSolicitudDeAdmisionInput.primerNombre);
				catSolicitudDeAdmisionInput.put("segundoNombre",object.catSolicitudDeAdmisionInput.segundoNombre);
				//catSolicitudDeAdmisionInput.put("segundonombre",object.segundonombre);
				errorlog+="[4] "
				contract.put("catSolicitudDeAdmisionInput",catSolicitudDeAdmisionInput)
				contract.put("nuevaoportunidad",object.nuevaoportunidad)
				errorlog+="[5] "
				Long processId = context.getApiClient().getProcessAPI().getLatestProcessDefinitionId("Proceso admisiones");
				errorlog+="[6] " + contract.toMapString()
				ProcessInstance processInstance = context.getApiClient().getProcessAPI().startProcessWithInputs(processId, contract);
				errorlog+="[7] "
				Long caseId = processInstance.getRootProcessInstanceId();
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK,"{\"caseId\": "+caseId+"}")
				}catch(Exception ex) {
					result.setSuccess(false)
					result.setError(ex.getMessage())
					result.setError_info(errorlog)
					return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
				}
			break;
		}
        // Here is an example of how you can retrieve configuration parameters from a properties file
        // It is safe to remove this if no configuration is required
        Properties props = loadProperties("configuration.properties", context.resourceProvider)
        String paramValue = props["myParameterKey"]
		
        /* 
         * Execute business logic here
         * 
         * 
         * Your code goes here
         * 
         * 
		 */

        // Send the result as a JSON representation
        // You may use buildPagedResponse if your result is multiple
        return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
    }

    /**
     * Build an HTTP response.
     *
     * @param  responseBuilder the Rest API response builder
     * @param  httpStatus the status of the response
     * @param  body the response body
     * @return a RestAPIResponse
     */
    RestApiResponse buildResponse(RestApiResponseBuilder responseBuilder, int httpStatus, Serializable body) {
        return responseBuilder.with {
            withResponseStatus(httpStatus)
            withResponse(body)
            build()
        }
    }

    /**
     * Returns a paged result like Bonita BPM REST APIs.
     * Build a response with a content-range.
     *
     * @param  responseBuilder the Rest API response builder
     * @param  body the response body
     * @param  p the page index
     * @param  c the number of result per page
     * @param  total the total number of results
     * @return a RestAPIResponse
     */
    RestApiResponse buildPagedResponse(RestApiResponseBuilder responseBuilder, Serializable body, int p, int c, long total) {
        return responseBuilder.with {
            withContentRange(p,c,total)
            withResponse(body)
            build()
        }
    }

    /**
     * Load a property file into a java.util.Properties
     */
    Properties loadProperties(String fileName, ResourceProvider resourceProvider) {
        Properties props = new Properties()
        resourceProvider.getResourceAsStream(fileName).withStream { InputStream s ->
            props.load s
        }
        props
    }

}
