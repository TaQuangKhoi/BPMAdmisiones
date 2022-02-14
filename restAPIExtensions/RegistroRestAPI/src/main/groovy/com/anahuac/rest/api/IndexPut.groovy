package com.anahuac.rest.api;

import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utilities.LoadParametros
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.http.HttpHeaders
import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.bonitasoft.web.extension.rest.RestAPIContext
import org.bonitasoft.web.extension.rest.RestApiController

import java.time.LocalDate

class IndexPut implements RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexPut.class)

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
			case "userTask": 
			String jsonData = request.reader.readLines().join("\n")
			try {
				errorlog+="[1] "
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				String username = "";
				String password = "";
				def task_id = request.getParameter "task_id"
				/*-------------------------------------------------------------*/
				LoadParametros objLoad = new LoadParametros();
				PropertiesEntity objProperties = objLoad.getParametros();
				username = objProperties.getUsuario();
				password = objProperties.getPassword();
				/*-------------------------------------------------------------*/
				
				org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
				apiClient.login(username, password);
				
				apiClient.processAPI.assignUserTask(task_id, object.assigned_id);
				return buildResponse(responseBuilder, HttpServletResponse.SC_OK,"{\"task_id\": "+task_id+"}")
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
