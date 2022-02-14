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

        // Retrieve url parameter
        def url = request.getParameter "url"
        if (url == null) {
            return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter url is missing"}""")
        }
		Result result = new Result()
		switch(url) {
			case "instantiation": 
			try {
				String jsonData = request.reader.readLines().join("\n")
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				assert object instanceof Map
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
				
				Map<String, Serializable> catSolicitudDeAdmisionInput = new HashMap<String, Serializable>();
				
				catSolicitudDeAdmisionInput.put("apellidoMaterno",object.catSolicitudDeAdmisionInput.apellidoMaterno);
				catSolicitudDeAdmisionInput.put("apellidoPaterno",object.catSolicitudDeAdmisionInput.apellidoPaterno);
				catSolicitudDeAdmisionInput.put("avisoPrivacidad",object.catSolicitudDeAdmisionInput.avisoPrivacidad);
				catSolicitudDeAdmisionInput.put("catCampus",object.catSolicitudDeAdmisionInput.catCampus);
				catSolicitudDeAdmisionInput.put("catCampusEstudio",object.catSolicitudDeAdmisionInput.catCampusEstudio);
				catSolicitudDeAdmisionInput.put("catEstadoExamen",object.catSolicitudDeAdmisionInput.catEstadoExamen);
				catSolicitudDeAdmisionInput.put("catGestionEscolar",object.catSolicitudDeAdmisionInput.catGestionEscolar);
				catSolicitudDeAdmisionInput.put("catLugarExamen",object.catSolicitudDeAdmisionInput.catLugarExamen);
				catSolicitudDeAdmisionInput.put("catPaisExamen",object.catSolicitudDeAdmisionInput.catPaisExamen);
				
				assert object.catSolicitudDeAdmisionInput.catPeriodo instanceof Map
				def map = [:]
				for ( prop in object.catSolicitudDeAdmisionInput.catPeriodo ) {
					map[prop.key] = prop.value
				}
				
				catSolicitudDeAdmisionInput.put("catPeriodo",map);
				catSolicitudDeAdmisionInput.put("catPropedeutico",object.catSolicitudDeAdmisionInput.catPropedeutico);
				catSolicitudDeAdmisionInput.put("ciudadExamen",object.catSolicitudDeAdmisionInput.ciudadExamen);
				catSolicitudDeAdmisionInput.put("ciudadExamenPais",object.catSolicitudDeAdmisionInput.ciudadExamenPais);
				catSolicitudDeAdmisionInput.put("correoElectronico",object.catSolicitudDeAdmisionInput.correoElectronico);
				catSolicitudDeAdmisionInput.put("isEliminado",object.catSolicitudDeAdmisionInput.isEliminado);
				catSolicitudDeAdmisionInput.put("necesitoAyuda",object.catSolicitudDeAdmisionInput.necesitoAyuda);
				catSolicitudDeAdmisionInput.put("primerNombre",object.catSolicitudDeAdmisionInput.primerNombre);
				catSolicitudDeAdmisionInput.put("segundoNombre",object.catSolicitudDeAdmisionInput.segundoNombre);
				//catSolicitudDeAdmisionInput.put("segundonombre",object.segundonombre);
				
				contract.put("catSolicitudDeAdmisionInput",catSolicitudDeAdmisionInput)
				contract.put("nuevaoportunidad",object.nuevaoportunidad)
				Long processId = context.getApiClient().getProcessAPI().getLatestProcessDefinitionId("Proceso admisiones");
				ProcessInstance processInstance = context.getApiClient().getProcessAPI().startProcessWithInputs(processId, contract);
				Long caseId = processInstance.getRootProcessInstanceId();
					return buildResponse(responseBuilder, HttpServletResponse.SC_OK,"{\"caseId\": "+caseId+"}")
				}catch(Exception ex) {
					result.setSuccess(false)
					result.setError("")
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
