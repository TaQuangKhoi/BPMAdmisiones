package com.anahuac.rest.api

import groovy.json.JsonBuilder

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.http.HttpHeaders
import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.anahuac.rest.api.DAO.UsuariosDAO
import com.bonitasoft.web.extension.rest.RestAPIContext
import com.bonitasoft.web.extension.rest.RestApiController

class IndexGet implements RestApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexGet.class)

	@Override
	RestApiResponse doHandle(HttpServletRequest request, RestApiResponseBuilder responseBuilder, RestAPIContext context) {
		// To retrieve query parameters use the request.getParameter(..) method.
		// Be careful, parameter values are always returned as String values
		RestApiResponseBuilder rb;
		def result = null;
		// Retrieve p parameter
		def correo = request.getParameter "correo"
		if (correo == null) {
			return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST, """{"error " : "the parameter correo is missing "}""")
		}
		def p = request.getParameter "p";
		if (p == null) {
			return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter p is missing"}""")
        }
        def c = request.getParameter "c";
        if (c == null) {
            return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter c is missing"}""")
		}
		def url = request.getParameter "url";
		if (url == null) {
			return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter url is missing"}""")
        }
		
		Integer parameterP = Integer.valueOf(p);
		Integer parameterC = Integer.valueOf(c);
		//String jsonData = request.reader.readLines().join("\n")

		//def result = ["correo": correo, "myParameterKey": ""]
		
		//VARIABLES DAO===============================
		UsuariosDAO uDAO = new UsuariosDAO();
		
		//MAPEO DE SERVICIOS==================================================
		try{
			switch(url) {
				case "habilitarUsuario":
					result = uDAO.getHabilitarUsaurio(parameterP, parameterC, correo, context);
					break;
			}
		}catch (Exception e) {
			e.printStackTrace()
		}
		// Send the result as a JSON representation
		// You may use buildPagedResponse if your result is multiple
		if(result.isSuccess()){
			responseBuilder.withMediaType("text/html; charset=utf-8")
			return buildResponse(responseBuilder, HttpServletResponse.SC_OK, (result.getData().size()>0)?result.getData().get(0):new JsonBuilder(result).toString())
		}else {
			return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
		}
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
			withContentRange(p, c, total)
			withResponse(body)
			build()
		}
	}

	/**
	 * Load a property file into a java.util.Properties
	 */
	Properties loadProperties(String fileName, ResourceProvider resourceProvider) {
		Properties props = new Properties()
		resourceProvider.getResourceAsStream(fileName).withStream {
			InputStream s->
				props.load s
		}
		props
	}

}