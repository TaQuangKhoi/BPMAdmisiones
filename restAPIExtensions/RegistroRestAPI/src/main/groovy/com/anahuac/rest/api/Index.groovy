package com.anahuac.rest.api;

import com.anahuac.rest.api.DAO.BonitaGetsDAO
import com.anahuac.rest.api.Entity.Result
import groovy.json.JsonBuilder

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.http.HttpHeaders
import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.bonitasoft.web.extension.rest.RestAPIContext
import org.bonitasoft.web.extension.rest.RestApiController

import java.time.LocalDate

class Index implements RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Index.class)

    @Override
    RestApiResponse doHandle(HttpServletRequest request, RestApiResponseBuilder responseBuilder, RestAPIContext context) {
		responseBuilder.withCharacterSet("UTF-8");
		Result resultado = new Result();
        // To retrieve query parameters use the request.getParameter(..) method.
        // Be careful, parameter values are always returned as String values

        // Retrieve url parameter
        def url = request.getParameter "url"
        if (url == null) {
            return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter url is missing"}""")
        }
		switch(url) {
			case "": 
			break;
			
			case "humanTask":
			
			String caseid=request.getParameter "caseid";
			String name=request.getParameter "name";
			if(name == null || name == 'null') {
				name = "";
			}
			
			resultado = new BonitaGetsDAO().getUserHumanTask(Long.parseLong(caseid),name,context)
			responseBuilder.withMediaType("application/json")
			if (resultado.isSuccess()) {
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(resultado.getData()).toString())
			}else {
				return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(resultado).toString())
			}
			
			break;
			
			case "userTask":
			
			String traskid = request.getParameter "taskId";
			resultado = new BonitaGetsDAO().getUserTask(Long.parseLong(traskid),context)
			responseBuilder.withMediaType("application/json")
			if (resultado.isSuccess()) {
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(resultado?.getData()?.get(0)).toString())
			}else {
				return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(resultado).toString())
			}
			
			break;
			
			case "archivedHumanTask":
			
			String caseid=request.getParameter "caseid";
			
			resultado = new BonitaGetsDAO().getUserArchivedHumanTask(Long.parseLong(caseid),context)
			responseBuilder.withMediaType("application/json")
			if (resultado.isSuccess()) {
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(resultado.getData()).toString())
			}else {
				return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(resultado).toString())
			}
			
			break;
			
			case "process":
			resultado = new BonitaGetsDAO().getUserProcess(context)
			responseBuilder.withMediaType("application/json")
			if (resultado.isSuccess()) {
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(resultado.getData()).toString())
			}else {
				return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(resultado).toString())
			}
			
			break;
			
			case "case":
			resultado = new BonitaGetsDAO().getCase()
			responseBuilder.withMediaType("application/json")
			if (resultado.isSuccess()) {
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(resultado.getData()).toString())
			}else {
				return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(resultado).toString())
			}
			
			break;
			
			case "caseVariable":
			
			String caseid=request.getParameter "caseid";
			String name=request.getParameter "name";
			if(name == null || name == 'null') {
				name = "";
			}
			resultado = new BonitaGetsDAO().getCaseVariable(Long.parseLong(caseid),name,context)
			responseBuilder.withMediaType("application/json")
			if (resultado.isSuccess()) {
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(resultado.getData()).toString())
			}else {
				return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(resultado).toString())
			}
			
			break;
			
			case "context":
			
			String caseid=request.getParameter "caseid";
			resultado = new BonitaGetsDAO().getUserContext(Long.parseLong(caseid),context)
			responseBuilder.withMediaType("application/json")
			if (resultado.isSuccess()) {
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(resultado?.getData()?.get(0)).toString())
			}else {
				return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(resultado).toString())
			}
			
			break;
			
			case "caseDocument":
			
			String caseid=request.getParameter "caseid";
			resultado = new BonitaGetsDAO().caseDocument(Long.parseLong(caseid), context)
			responseBuilder.withMediaType("application/json")
			if (resultado.isSuccess()) {
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(resultado?.getData()).toString())
			}else {
				return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(resultado).toString())
			}
			
			break;
			
			case "archivedCase":
			
			String caseid=request.getParameter "caseid";
			resultado = new BonitaGetsDAO().archivedCase(Long.parseLong(caseid), context)
			responseBuilder.withMediaType("application/json")
			if (resultado.isSuccess()) {
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(resultado?.getData()).toString())
			}else {
				return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(resultado).toString())
			}
			
			break;
			
			case "getUserIdentity":
			
			String user=request.getParameter "userId";
			
			resultado = new BonitaGetsDAO().getUserIdentity(Long.parseLong(user),context)
			responseBuilder.withMediaType("application/json")
			if (resultado.isSuccess()) {
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(resultado.getData()).toString())
			}else {
				return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(resultado).toString())
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
        // Prepare the result
        def result = [ "url" : url , "myParameterKey" : paramValue, "currentDate" : LocalDate.now().toString() ]

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
