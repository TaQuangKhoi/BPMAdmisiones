package com.anahuac.rest.api;

import groovy.json.JsonSlurper

import javax.servlet.http.HttpServletRequest

import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder

import spock.lang.Specification
import org.bonitasoft.web.extension.rest.RestAPIContext

import java.time.LocalDate;

/**
 * @see http://spockframework.github.io/spock/docs/
 */
class IndexTest extends Specification {

    // Declare mocks here
    // Mocks are used to simulate external dependencies behavior
    def httpRequest = Mock(HttpServletRequest)
    def resourceProvider = Mock(ResourceProvider)
    def context = Mock(RestAPIContext)

    /**
     * You can configure mocks before each tests in the setup method
     */
    def setup(){
        // Simulate access to configuration.properties resource
        context.resourceProvider >> resourceProvider
        resourceProvider.getResourceAsStream("configuration.properties") >> IndexTest.class.classLoader.getResourceAsStream("testConfiguration.properties")
    }

    def should_return_a_json_representation_as_result() {
        given: "a RestAPIController"
        def index = new Index()
        // Simulate a request with a value for each parameter
        httpRequest.getParameter("url") >> "aValue1"

        when: "Invoking the REST API"
        def apiResponse = index.doHandle(httpRequest, new RestApiResponseBuilder(), context)

        then: "A JSON representation is returned in response body"
        def jsonResponse = new JsonSlurper().parseText(apiResponse.response)
        // Validate returned response
        apiResponse.httpStatus == 200 || apiResponse.httpStatus == 400
        jsonResponse.url == "aValue1"
        jsonResponse.myParameterKey == "testValue"
        jsonResponse.currentDate == LocalDate.now().toString()
    }

    def should_return_an_error_response_if_url_is_not_set() {
        given: "a request without url"
        def index = new Index()
        httpRequest.getParameter("url") >> null
        // Other parameters return a valid value

        when: "Invoking the REST API"
        def apiResponse = index.doHandle(httpRequest, new RestApiResponseBuilder(), context)

        then: "A JSON response is returned with a HTTP Bad Request Status (400) and an error message in body"
        def jsonResponse = new JsonSlurper().parseText(apiResponse.response)
        // Validate returned response
        apiResponse.httpStatus == 400
        jsonResponse.error == "the parameter url is missing"
    }

}