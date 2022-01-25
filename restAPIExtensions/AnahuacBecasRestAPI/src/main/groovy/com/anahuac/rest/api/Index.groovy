package com.anahuac.rest.api;

import groovy.json.JsonBuilder

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.http.HttpHeaders
import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DAO.CatalogosDAO
import com.anahuac.rest.api.Entity.Result

import org.bonitasoft.web.extension.rest.RestAPIContext
import org.bonitasoft.web.extension.rest.RestApiController

import java.time.LocalDate

class Index implements RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Index.class)

    @Override
    RestApiResponse doHandle(HttpServletRequest request, RestApiResponseBuilder responseBuilder, RestAPIContext context) {
		Result result = new Result();
		def p = request.getParameter "p"
        if (p == null) {
            return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter p is missing"}""")
        }
        def c = request.getParameter "c"
        if (c == null) {
            return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter c is missing"}""")
        }
        def url = request.getParameter "url"
        if (url == null) {
            return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter url is missing"}""")
        }
		
		//VARIABLES===========================================================
		Integer parameterP = Integer.valueOf(p);
		Integer parameterC = Integer.valueOf(c);
		String jsonData = "{}"
		try {
			jsonData = request.reader.readLines().join("\n")
		}catch (Exception e) {
			jsonData = "{}" 
		}
		
		try {
			switch(url) {
				case "test":
					result = dao.testFuction(parameterP, parameterC, jsonData);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "simpleSelect":
					result = new CatalogosDAO().simpleSelect(parameterP, parameterC, jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "simpleSelectBonita":
					result = new CatalogosDAO().simpleSelectBonita(parameterP, parameterC, jsonData, context)
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "getCatGenerico":
					result = new CatalogosDAO().getCatGenerico(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertUpdateCatGenerico":
					result = new CatalogosDAO().insertUpdateCatGenerico(jsonData, context)

					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertUpdateCatTipoMoneda":
					result = new CatalogosDAO().insertUpdateCatTipoMoneda(jsonData, context);

					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
					break;
				/*Cat manejo documentos*/
				case "getCatManejoDocumento":
					result = new CatalogosDAO().getCatManejoDocumento(jsonData, context)
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "insertManejoDocumento":
					result = new CatalogosDAO().insertManejoDocumento(jsonData, context);
					responseBuilder.withMediaType("application/json")
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
				case "deleteCatManejoDocumentos":
					result = new CatalogosDAO().deleteCatManejoDocumentos(jsonData, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
					break;
				/*Fin Cat manejo documentos*/
				/*Cat TipoApoyo*/
				case "getCatTipoAoyo":
					result = new CatalogosDAO().getCatTipoAoyo(jsonData, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
					break;
				case "insertUpdateCatTipoApoyo":
					result = new CatalogosDAO().insertUpdateCatTipoApoyo(jsonData, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
					break;
				case "updateTipoApoyoVideo":
					result = new CatalogosDAO().updateTipoApoyoVideo(jsonData, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
					break;
				case "deleteCatTipoApoyo":
					result = new CatalogosDAO().deleteCatTipoApoyo(jsonData, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
					break;
				case "getCampusByTipoApoyo":
					result = new CatalogosDAO().getCampusByTipoApoyo(jsonData, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
					break;
				case "switchCampusTipoApoyo":
					result = new CatalogosDAO().switchCampusTipoApoyo(jsonData, context);
					responseBuilder.withMediaType("application/json");
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString());
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString());
					}
					break;
					
				/*Fin Cat TipoApoyo*/
				default:
					result = notFound(url);
					if (result.isSuccess()) {
						return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
					}else {
						return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
					}
					break;
			}
			
		} catch (Exception e) {
			result.setSuccess(false)
			result.setError("500 INTERNAL SERVER ERROR")
			result.setError_info(e.getMessage())
			return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,new JsonBuilder(result).toString())
		}
		// Send the result as a JSON representation
		
		// You may use buildPagedResponse if your result is multiple
		return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
		
		//return buildResponse(responseBuilder, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  new JsonBuilder(result).toString())
		
		/*
        Properties props = loadProperties("configuration.properties", context.resourceProvider)
        String paramValue = props["myParameterKey"] 
        def result = [ "p" : p ,"c" : c ,"url" : url , "myParameterKey" : paramValue, "currentDate" : LocalDate.now().toString() ] 
        // Send the result as a JSON representation
        // You may use buildPagedResponse if your result is multiple
        return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder(result).toString())
		*/
    }
	public Result notFound(String url) {
		Result resultado = new Result();
		resultado.setSuccess(false);
		resultado.setError("No se reconoce el servicio: "+url);
		return resultado
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
