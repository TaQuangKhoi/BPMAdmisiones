package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatBachilleratos
import com.anahuac.catalogos.CatBachilleratosDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.CatBachillerato
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Custom.AzureConfig
import com.bonitasoft.engine.api.ProcessAPI
import com.bonitasoft.web.extension.rest.RestAPIContext

class BannerDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(BannerDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	public Boolean validarConexion() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno = true
		}
		return retorno
	}

	public Result buscarCambiosBannerPreparatoria(RestAPIContext context, String operacion) {
		Result resultado = new Result();
		Result resultadoGetConsumeJSON = new Result();

		String errorLog = "";
		String barrerToken = "";
		String jsonResultado = "";
		String strGetConsumeJSON = "";
		try {
			errorLog += " | " + ("START JSON======================================");
			barrerToken = getBarreToken();
			errorLog += " | " + barrerToken;
			errorLog += " | " + ("================================================");
			jsonResultado = getConsumePrepa(barrerToken);
			errorLog += " | " + jsonResultado;
			//CREAR-------------------------------------------------------
			//jsonResultado = "[{\"id\":\"24\",\"published\":\"2021-04-21 22:36:38.602364+00\",\"resource\":{\"name\":\"educational-institutions\",\"id\":\"" + operacion + "\",\"version\":\"application/vnd.hedtech.integration.v6+json\"},\"operation\":\"created\",\"contentType\":\"resource-representation\",\"content\":{\"homeInstitution\":\"external\",\"id\":\"" + operacion + "\",\"title\":\"Nueva Escuela JABB\",\"type\":\"secondarySchool\",\"code\":\"JABB\"},\"publisher\":{\"id\":\"c9d2d963-68db-445d-a874-c9c103aa32ba\",\"applicationName\":\"RUAD INTEGRATION API (Shared Data)\",\"tenant\":{\"id\":\"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\":\"uatest\",\"name\":\"Universidad Anahuac\",\"environment\":\"Test\"}}},{\"id\":\"25\",\"published\":\"2021-04-21 22:43:54.645787+00\",\"resource\":{\"name\":\"addresses\",\"id\":\"23bc3dbf-f3fa-4c4b-aeac-9178e651343d\",\"version\":\"application/vnd.hedtech.integration.v11.1.0+json\"},\"operation\":\"created\",\"contentType\":\"resource-representation\",\"content\":{\"addressLines\":[\"Avenida siempre viva 123\",\"Springfield\",\"USA\"],\"id\":\"23bc3dbf-f3fa-4c4b-aeac-9178e651343d\",\"place\":{\"country\":{\"code\":\"DEU\",\"locality\":\"Springfield\",\"postalCode\":\"123\",\"region\":{\"title\":\"Delaware\"},\"subRegion\":{\"title\":\"AMECA\"},\"title\":\"Alemania\"}}},\"publisher\":{\"id\":\"a216d744-fb37-413e-8430-7f187c223bda\",\"applicationName\":\"RUAD INTEGRATION API-UAN\",\"tenant\":{\"id\":\"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\":\"uatest\",\"name\":\"Universidad Anahuac\",\"environment\":\"Test\"}}}]";
			//jsonResultado = "[{\"id\": \"55\", \"published\": \"2021-05-13 18:49:18.263057+00\", \"resource\": {\"name\": \"educational-institutions\", \"id\": \"31a33fc2-c9e4-4a62-ad56-a2135994acbb\", \"version\": \"application/vnd.hedtech.integration.v6+json\"}, \"operation\": \"created\", \"contentType\": \"resource-representation\", \"content\": {\"homeInstitution\": \"external\", \"id\": \"31a33fc2-c9e4-4a62-ad56-a2135994acbb\", \"title\": \"Nueva Prepa 3\", \"type\": \"secondarySchool\", \"code\": \"JABB3\", \"typeInd\": \"H\"}, \"publisher\": {\"id\": \"c9d2d963-68db-445d-a874-c9c103aa32ba\", \"applicationName\": \"RUAD INTEGRATION API (Shared Data)\", \"tenant\": {\"id\": \"184dddce-65c5-4621-92a3-5703037fb3ed\", \"alias\": \"uatest\", \"name\": \"Universidad Anahuac\", \"environment\": \"Test\"} } },{\"id\": \"56\", \"published\": \"2021-05-13 18:57:23.862561+00\", \"resource\": {\"name\": \"addresses\", \"id\": \"aaaa2fc7-99c5-42e3-b15a-cbc114a4faa4\", \"version\": \"application/vnd.hedtech.integration.v11.1.0+json\"}, \"operation\": \"created\", \"contentType\": \"resource-representation\", \"content\": {\"addressLines\": [\"Calle 1\", \"Calle 2\", \"Calle 3\"], \"id\": \"aaaa2fc7-99c5-42e3-b15a-cbc114a4faa4\", \"place\": {\"country\": {\"code\": \"USA\", \"locality\": \"000000\", \"postalTitle\": \"UNITED STATES OF AMERICA\", \"region\": {\"title\": \"Estado Extranjero\"}, \"subRegion\": {\"title\": \"MUNICIPIO EXTRANJERO\"}, \"title\": \"Estados Unidos de América\"} }, \"addressExtended\": [{\"streetLine1\": \"Calle 1\", \"streetLine2\": \"Calle 2\", \"streetLine3\": \"Calle 3\", \"nationCode\": \"157\", \"stateCode\": \"FR\", \"countyCode\": \"20000\"} ] }, \"publisher\": {\"id\": \"a216d744-fb37-413e-8430-7f187c223bda\", \"applicationName\": \"RUAD INTEGRATION API-UAN\", \"tenant\": {\"id\": \"184dddce-65c5-4621-92a3-5703037fb3ed\", \"alias\": \"uatest\", \"name\": \"Universidad Anahuac\", \"environment\": \"Test\"} } } ]";
			//jsonResultado = "[{\"id\": \"55\",\"published\": \"2021-05-13 18:49:18.263057+00\",\"resource\":{\"name\": \"educational-institutions\",\"id\": \"31a33fc2-c9e4-4a62-ad56-a2135994acbb\",\"version\": \"application/vnd.hedtech.integration.v6+json\"},\"operation\": \"created\",\"contentType\": \"resource-representation\",\"content\":{\"homeInstitution\": \"external\",\"id\": \"31a33fc2-c9e4-4a62-ad56-a2135994acbb\",\"title\": \"Nueva Prepa 33\",\"type\": \"secondarySchool\",\"code\": \"JABB3\",\"typeInd\": \"S\"},\"publisher\":{\"id\": \"c9d2d963-68db-445d-a874-c9c103aa32ba\",\"applicationName\": \"RUAD INTEGRATION API (Shared Data)\",\"tenant\":{\"id\": \"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\": \"uatest\",\"name\": \"Universidad Anahuac\",\"environment\": \"Test\"}}},{\"id\": \"56\",\"published\": \"2021-05-13 18:57:23.862561+00\",\"resource\":{\"name\": \"addresses\",\"id\": \"aaaa2fc7-99c5-42e3-b15a-cbc114a4faa4\",\"version\": \"application/vnd.hedtech.integration.v11.1.0+json\"},\"operation\": \"created\",\"contentType\": \"resource-representation\",\"content\":{\"addressLines\": [\"Calle 1\", \"Calle 2\", \"Calle 3\"],\"id\": \"aaaa2fc7-99c5-42e3-b15a-cbc114a4faa4\",\"place\":{\"country\":{\"code\": \"USA\",\"locality\": \"000000\",\"postalTitle\": \"UNITED STATES OF AMERICA\",\"region\":{\"title\": \"Estado Extranjero\"},\"subRegion\":{\"title\": \"MUNICIPIO EXTRANJERO\"},\"title\": \"Estados Unidos de América\"}},\"addressExtended\": [{\"streetLine1\": \"Calle 123\",\"streetLine2\": \"Calle 2\",\"streetLine3\": \"Calle 3\",\"nationCode\": \"157\",\"stateCode\": \"FR\",\"countyCode\": \"20000\"}]},\"publisher\":{\"id\": \"a216d744-fb37-413e-8430-7f187c223bda\",\"applicationName\": \"RUAD INTEGRATION API-UAN\",\"tenant\":{\"id\": \"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\": \"uatest\",\"name\": \"Universidad Anahuac\",\"environment\": \"Test\"}}}]";
			//----jsonResultado = "[{\"id\": \"70\",\"published\": \"2021-05-20 18:51:45.607535+00\",\"resource\":{\"name\": \"educational-institutions\",\"id\": \"c45bd9a4-7411-4de7-a6f4-7666701be071\",\"version\": \"application/vnd.hedtech.integration.v6+json\"},\"operation\": \"created\",\"contentType\": \"resource-representation\",\"content\":{\"homeInstitution\": \"external\",\"id\": \"c45bd9a4-7411-4de7-a6f4-7666701be071\",\"title\": \"Preparatoria Oficial 80\",\"type\": \"secondarySchool\",\"code\": \"9327\",\"typeInd\": \"D\"},\"publisher\":{\"id\": \"c9d2d963-68db-445d-a874-c9c103aa32ba\",\"applicationName\": \"RUAD INTEGRATION API (Shared Data)\",\"tenant\":{\"id\": \"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\": \"uatest\",\"name\": \"Universidad Anahuac\",\"environment\": \"Test\"}}},{\"id\": \"71\",\"published\": \"2021-05-20 18:53:22.086887+00\",\"resource\":{\"name\": \"addresses\",\"id\": \"e78b3644-0ad9-49c1-9ca7-f5cc63fa83ce\",\"version\": \"application/vnd.hedtech.integration.v11.1.0+json\"},\"operation\": \"created\",\"contentType\": \"resource-representation\",\"content\":{\"addressLines\": [\"Cedro 11\", \"Col. Viveros Xalostoc\"],\"id\": \"e78b3644-0ad9-49c1-9ca7-f5cc63fa83ce\",\"place\":{\"country\":{\"code\": \"MEX\",\"locality\": \"Edo de M\u00e9xico\",\"postalCode\": \"55340\",\"postalTitle\": \"MEXICO\",\"region\":{\"title\": \"Estado de M\u00e9xico\"},\"subRegion\":{\"title\": \"Ecatepec de Morelos\"},\"title\": \"M\u00e9xico\"}},\"addressExtended\": [{\"streetLine1\": \"Cedro 11\",\"streetLine2\": null,\"streetLine3\": \"Col. Viveros Xalostoc\",\"nationCode\": \"99\",\"stateCode\": \"M11\",\"countyCode\": \"15033\"}]},\"publisher\":{\"id\": \"a216d744-fb37-413e-8430-7f187c223bda\",\"applicationName\": \"RUAD INTEGRATION API-UAN\",\"tenant\":{\"id\": \"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\": \"uatest\",\"name\": \"Universidad Anahuac\",\"environment\": \"Test\"}}}]";
			//jsonResultado = "[{\"id\": \"60\",\"published\": \"2021-05-20 18:04:02.140895+00\",\"resource\":{\"name\": \"educational-institutions\",\"id\": \"0053d615-6f0e-43c2-9a87-d6db9b1fbfc9\",\"version\": \"application/vnd.hedtech.integration.v6+json\"},\"operation\": \"created\",\"contentType\": \"resource-representation\",\"content\":{\"homeInstitution\": \"external\",\"id\": \"0053d615-6f0e-43c2-9a87-d6db9b1fbfc9\",\"title\": \"Preparatoria Oficial 77\",\"type\": \"secondarySchool\",\"code\": \"9324\",\"typeInd\": \"H\"},\"publisher\":{\"id\": \"c9d2d963-68db-445d-a874-c9c103aa32ba\",\"applicationName\": \"RUAD INTEGRATION API (Shared Data)\",\"tenant\":{\"id\": \"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\": \"uatest\",\"name\": \"Universidad Anahuac\",\"environment\": \"Test\"}}},{\"id\": \"61\",\"published\": \"2021-05-20 18:06:41.102315+00\",\"resource\":{\"name\": \"addresses\",\"id\": \"8fe3c196-524e-4b00-adb1-34a0f6888894\",\"version\": \"application/vnd.hedtech.integration.v11.1.0+json\"},\"operation\": \"created\",\"contentType\": \"resource-representation\",\"content\":{\"addressLines\": [\"Calle Florida 89\", \"Col. Miraflores\"],\"id\": \"8fe3c196-524e-4b00-adb1-34a0f6888894\",\"place\":{\"country\":{\"code\": \"MEX\",\"locality\": \"M\u00e9rida\",\"postalCode\": \"97000\",\"postalTitle\": \"MEXICO\",\"region\":{\"title\": \"Yucat\u00e1n\"},\"subRegion\":{\"title\": \"M\u00e9rida\"},\"title\": \"M\u00e9xico\"}},\"addressExtended\": [{\"streetLine1\": \"Calle Florida 89\",\"streetLine2\": null,\"streetLine3\": \"Col. Miraflores\",\"nationCode\": \"99\",\"stateCode\": \"M31\",\"countyCode\": \"31050\"}]},\"publisher\":{\"id\": \"a216d744-fb37-413e-8430-7f187c223bda\",\"applicationName\": \"RUAD INTEGRATION API-UAN\",\"tenant\":{\"id\": \"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\": \"uatest\",\"name\": \"Universidad Anahuac\",\"environment\": \"Test\"}}}]";
			
			//EDITAR------------------------------------------------------
			//jsonResultado = "[{\"id\":\"30\",\"published\":\"2021-04-22 18:26:29.274756+00\",\"resource\":{\"name\":\"addresses\",\"id\":\"fe9eda15-74e3-4f1c-b249-e413c86bf49f\",\"version\":\"application/vnd.hedtech.integration.v11.1.0+json\"},\"operation\":\"replaced\",\"contentType\":\"resource-representation\",\"content\":{\"addressLines\":[\"Blvd. La Mirada 3050\",\"Los Angeles\"],\"id\":\"fe9eda15-74e3-4f1c-b249-e413c86bf49f\",\"place\":{\"country\":{\"code\":\"MEX\",\"locality\":\"Culiac\u00e1n 25006\",\"postalCode\":\"80014\",\"postalTitle\":\"MEXICO\",\"region\":{\"title\":\"Sinaloa\"},\"subRegion\":{\"title\":\"Culiac\u00e1n\"},\"title\":\"M\u00e9xico\"}}},\"publisher\":{\"id\":\"a216d744-fb37-413e-8430-7f187c223bda\",\"applicationName\":\"RUAD INTEGRATION API-UAN\",\"tenant\":{\"id\":\"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\":\"uatest\",\"name\":\"Universidad Anahuac\",\"environment\":\"Test\"}}}]";
			//jsonResultado = "[{\"id\":\"29\",\"published\":\"2021-04-22 18:22:21.321824+00\",\"resource\":{\"name\":\"addresses\",\"id\":\"23bc3dbf-f3fa-4c4b-aeac-9178e651343d\",\"version\":\"application/vnd.hedtech.integration.v11.1.0+json\"},\"operation\":\"replaced\",\"contentType\":\"resource-representation\",\"content\":{\"addressLines\":[\"Avenida siempre viva 123\",\"Springfield\",\"USA\"],\"id\":\"23bc3dbf-f3fa-4c4b-aeac-9178e651343d\",\"place\":{\"country\":{\"code\":\"AZE\",\"locality\":\"Springfield\",\"postalCode\":\"123\",\"region\":{\"title\":\"Delaware\"},\"subRegion\":{\"title\":\"AMECA\"},\"title\":\"Azerbaiyan\"}}},\"publisher\":{\"id\":\"a216d744-fb37-413e-8430-7f187c223bda\",\"applicationName\":\"RUAD INTEGRATION API-UAN\",\"tenant\":{\"id\":\"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\":\"uatest\",\"name\":\"Universidad Anahuac\",\"environment\":\"Test\"}}}]";/
			//jsonResultado = "[{\"id\":\"31\",\"published\":\"2021-04-22 18:29:39.352476+00\",\"resource\":{\"name\":\"addresses\",\"id\":\"72b9f043-036f-4f99-a526-ebb0af16a671\",\"version\":\"application/vnd.hedtech.integration.v11.1.0+json\"},\"operation\":\"replaced\",\"contentType\":\"resource-representation\",\"content\":{\"addressLines\":[\"2da De Circunvalaci\u00f3n no 49\",\"Centro\"],\"id\":\"72b9f043-036f-4f99-a526-ebb0af16a671\",\"place\":{\"country\":{\"code\":\"MEX\",\"locality\":\"Macuspana\",\"postalCode\":\"86700\",\"postalTitle\":\"MEXICO\",\"region\":{\"title\":\"Morelos\"},\"subRegion\":{\"title\":\"ALDAMA\"},\"title\":\"M\u00e9xico\"}}},\"publisher\":{\"id\":\"a216d744-fb37-413e-8430-7f187c223bda\",\"applicationName\":\"RUAD INTEGRATION API-UAN\",\"tenant\":{\"id\":\"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\":\"uatest\",\"name\":\"Universidad Anahuac\",\"environment\":\"Test\"}}},{\"id\":\"32\",\"published\":\"2021-04-22 18:31:03.538532+00\",\"resource\":{\"name\":\"addresses\",\"id\":\"845b1b5c-52c6-4f2e-9265-fe358192000c\",\"version\":\"application/vnd.hedtech.integration.v11.1.0+json\"},\"operation\":\"replaced\",\"contentType\":\"resource-representation\",\"content\":{\"addressLines\":[\"Calle 143, tablaje catastral 18631 no 314\",\"entre perif\u00e9rico\",\"Emiliano Zapata\"],\"id\":\"845b1b5c-52c6-4f2e-9265-fe358192000c\",\"place\":{\"country\":{\"code\":\"AGO\",\"locality\":\"Cd Obreg\u00f3n\",\"postalCode\":\"97139\",\"region\":{\"title\":\"Yucat\u00e1n\"},\"subRegion\":{\"title\":\"M\u00e9rida\"},\"title\":\"Angola\"}}},\"publisher\":{\"id\":\"a216d744-fb37-413e-8430-7f187c223bda\",\"applicationName\":\"RUAD INTEGRATION API-UAN\",\"tenant\":{\"id\":\"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\":\"uatest\",\"name\":\"Universidad Anahuac\",\"environment\":\"Test\"}}}]";
			//----jsonResultado = "[{\"id\": \"72\",\"published\": \"2021-04-22 18:29:39.352476+00\",\"resource\":{\"name\": \"addresses\",\"id\": \"e78b3644-0ad9-49c1-9ca7-f5cc63fa83ce\",\"version\": \"application/vnd.hedtech.integration.v11.1.0+json\"},\"operation\": \"replaced\",\"contentType\": \"resource-representation\",\"content\":{\"addressLines\": [\"Cedro 11\", \"Col. Viveros Xalostoc\"],\"id\": \"e78b3644-0ad9-49c1-9ca7-f5cc63fa83ce\",\"place\":{\"country\":{\"code\": \"MEX\",\"locality\": \"Edo de México\",\"postalCode\": \"55340\",\"postalTitle\": \"MEXICO\",\"region\":{\"title\": \"Estado de México\"},\"subRegion\":{\"title\": \"Ecatepec de Morelos\"},\"title\": \"México\"}},\"addressExtended\": [{\"streetLine1\": \"Cedro 11\",\"streetLine2\": null,\"streetLine3\": \"Col. Viveros Xalostoc\",\"nationCode\": \"99\",\"stateCode\": \"M11\",\"countyCode\": \"15033\"}]},\"publisher\":{\"id\": \"a216d744-fb37-413e-8430-7f187c223bda\",\"applicationName\": \"RUAD INTEGRATION API-UAN\",\"tenant\":{\"id\": \"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\": \"uatest\",\"name\": \"Universidad Anahuac\",\"environment\": \"Test\"}}}]";
			//jsonResultado = "[{\"id\": \"70\",\"published\": \"2021-05-20 18:51:45.607535+00\",\"resource\":{\"name\": \"educational-institutions\",\"id\": \"c45bd9a4-7411-4de7-a6f4-7666701be071\",\"version\": \"application/vnd.hedtech.integration.v6+json\"},\"operation\": \"replaced\",\"contentType\": \"resource-representation\",\"content\":{\"homeInstitution\": \"external\",\"id\": \"c45bd9a4-7411-4de7-a6f4-7666701be071\",\"title\": \"Preparatoria Oficial 80\",\"type\": \"secondarySchool\",\"code\": \"9327\",\"typeInd\": \"H\"},\"publisher\":{\"id\": \"c9d2d963-68db-445d-a874-c9c103aa32ba\",\"applicationName\": \"RUAD INTEGRATION API (Shared Data)\",\"tenant\":{\"id\": \"184dddce-65c5-4621-92a3-5703037fb3ed\",\"alias\": \"uatest\",\"name\": \"Universidad Anahuac\",\"environment\": \"Test\"}}}]";
			
			//jsonResultado = "[{\"id\": \"60\", \"published\": \"2021-05-20 18:04:02.140895+00\", \"resource\": {\"name\": \"educational-institutions\", \"id\": \"0053d615-6f0e-43c2-9a87-d6db9b1fbfc9\", \"version\": \"application/vnd.hedtech.integration.v6+json\"}, \"operation\": \"replaced\", \"contentType\": \"resource-representation\", \"content\": {\"homeInstitution\": \"external\", \"id\": \"0053d615-6f0e-43c2-9a87-d6db9b1fbfc9\", \"title\": \"Preparatoria Oficial 77\", \"type\": \"secondarySchool\", \"code\": \"9324\", \"typeInd\": \"H\"}, \"publisher\": {\"id\": \"c9d2d963-68db-445d-a874-c9c103aa32ba\", \"applicationName\": \"RUAD INTEGRATION API (Shared Data)\", \"tenant\": {\"id\": \"184dddce-65c5-4621-92a3-5703037fb3ed\", \"alias\": \"uatest\", \"name\": \"Universidad Anahuac\", \"environment\": \"Test\"} } }]";
			//jsonResultado = "[{\"id\": \"61\", \"published\": \"2021-05-20 18:06:41.102315+00\", \"resource\": {\"name\": \"addresses\", \"id\": \"8fe3c196-524e-4b00-adb1-34a0f6888894\", \"version\": \"application/vnd.hedtech.integration.v11.1.0+json\"}, \"operation\": \"replaced\", \"contentType\": \"resource-representation\", \"content\": {\"addressLines\": [\"Calle Florida 89\", \"Col. Miraflores\"], \"id\": \"8fe3c196-524e-4b00-adb1-34a0f6888894\", \"place\": {\"country\": {\"code\": \"MEX\", \"locality\": \"M\u00e9rida\", \"postalCode\": \"97000\", \"postalTitle\": \"MEXICO\", \"region\": {\"title\": \"Yucat\u00e1n\"}, \"subRegion\": {\"title\": \"M\u00e9rida\"}, \"title\": \"M\u00e9xico\"} }, \"addressExtended\": [{\"streetLine1\": \"Calle Florida 89\", \"streetLine2\": null, \"streetLine3\": \"Col. Miraflores\", \"nationCode\": \"99\", \"stateCode\": \"M31\", \"countyCode\": \"31050\"}] }, \"publisher\": {\"id\": \"a216d744-fb37-413e-8430-7f187c223bda\", \"applicationName\": \"RUAD INTEGRATION API-UAN\", \"tenant\": {\"id\": \"184dddce-65c5-4621-92a3-5703037fb3ed\", \"alias\": \"uatest\", \"name\": \"Universidad Anahuac\",\"environment\": \"Test\"}}}]";
			
			//errorLog += " | jsonResultado: " + jsonResultado;
			errorLog += " | " + ("END JSON========================================");

			resultadoGetConsumeJSON = getConsumeJSON(jsonResultado, context, operacion, barrerToken);
			errorLog += " | " + strGetConsumeJSON;
			//resultadoGetConsumeJSON.setSuccess(true);
			resultadoGetConsumeJSON.setError_info(errorLog + resultadoGetConsumeJSON.getError_info());
		} catch (Exception e) {
			errorLog += " | " + e.getMessage();
			resultadoGetConsumeJSON.setError_info(errorLog);
			e.printStackTrace()
		}

		return resultadoGetConsumeJSON;
	}

	private String getBarreToken() {
		String urlParaVisitar = "https://integrate.elluciancloud.com/auth";
		String barrerKey = "Bearer ";
		StringBuilder resultado = new StringBuilder();
		Boolean closeCon = false;
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(AzureConfig.GET_CONFIGURACIONES_CLAVE)
			pstm.setString(1, "BannerToken")
			rs = pstm.executeQuery()
			if (rs.next()) {

				barrerKey += rs.getString("valor")
			}

			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization", barrerKey.replace("-.-.-", ""));
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
			LOGGER.error "[ERROR]" + e.getMessage();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado.toString();
	}

	private String getConsumePrepa(String barrerToken) {
		String urlParaVisitar = "https://integrate.elluciancloud.com/consume";
		StringBuilder resultado = new StringBuilder();

		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization", "Bearer " + barrerToken);
			conexion.setRequestProperty("Accept", "application/vnd.hedtech.change-notifications.v2+json");
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
			LOGGER.error "[ERROR]" + e.getMessage();
			e.printStackTrace();
		}
		return resultado.toString();
	}

	private Result getConsumeJSON(String jsonResultado, RestAPIContext context, String operacion, String barrerToken) {
		Result resultado = new Result();

		Integer indexAddress = null;

		JSONObject objJson = null;
		JSONObject objJsonAddressesData = null;
		JSONObject objJsonAddressExtended = null;
		JSONObject objJsonAddressesDataAddress = null;
		JSONObject objJsonResource = null;
		JSONObject objJsonContent = null;
		JSONObject objJsonPlace = null;
		JSONObject objJsonCountry = null;
		JSONObject objJsonRegion = null;
		JSONObject objJsonPublisher = null;
		JSONObject objJsonAddressData = null;

		JSONArray objJsonAddresses = null;
		JSONArray lstAddressExtended = null;
		JSONArray lstJson = null;

		JSONParser parser = new JSONParser();

		CatBachillerato objEducationalInstitutions = new CatBachillerato();
		List < CatBachillerato > lstEducationalInstitutions = new ArrayList < CatBachillerato > ();

		CatBachillerato objAddresses = new CatBachillerato();
		List < CatBachillerato > lstAddresses = new ArrayList < CatBachillerato > ();

		CatBachillerato objCatBachillerato = new CatBachillerato();
		//List<CatBachillerato> lstCatBachillerato= new ArrayList<CatBachillerato>();

		DateFormat dfSalida = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		String errorLog = "";
		String strCountyCode = "";
		String strStateCode = "";
		String strNationCode = "";
		String resultEducationalInstitutions = "";

		Boolean isCountyCodeOk = false;
		Boolean isStateCodeOk = false;
		Boolean isNationCodeOk = false;
		Boolean isNationCodeLetterOk = false;
		Boolean isMexicoOk = false;
		Boolean isUsaOk = false;
		Boolean isOtroPaisOk = false;

		List < CatBachilleratos > lstCatBachilleratos = new ArrayList < CatBachilleratos > ();

		Long processId = null;
		Long processIdCrear = null;

		ProcessInstance processInstance = null;
		ProcessAPI processAPI = null;

		Map < String, Serializable > contracto = new HashMap < String, Serializable > ();
		Map < String, Serializable > objCatBachilleratosInput = new HashMap < String, Serializable > ();
		List < Map < String, Serializable >> lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
		SearchOptionsBuilder searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
		List < ProcessDeploymentInfo > lstProcessDeploymentInfo = new ArrayList < ProcessDeploymentInfo > ();
		SearchOptions searchOptionsProccess = null;

		Pattern pattern = Pattern.compile("[a-zA-Z]+");

		Matcher matcher = null;

		try {

			errorLog = errorLog + " | jsonResultado: " + jsonResultado;
			lstJson = (org.json.simple.JSONArray) parser.parse(jsonResultado);

			Iterator < JSONObject > iterator = lstJson.iterator();

			errorLog = errorLog + " | " + "--------------------------------------------------------";
			while (iterator.hasNext()) {

				objJson = iterator.next();
				objJsonResource = (JSONObject) objJson.get("resource");
				objJsonContent = (JSONObject) objJson.get("content");
				objJsonPublisher = (JSONObject) objJson.get("publisher");

				objEducationalInstitutions = new CatBachillerato();
				objAddresses = new CatBachillerato();

				switch (objJsonResource.get("name").toString()) {
					case "educational-institutions":

						errorLog = errorLog + " | educational-institutions";
						errorLog = errorLog + " | " + ("idBachillerato: " + objJsonContent.get("id").toString());
						errorLog = errorLog + " | " + ("code: " + objJsonContent.get("code").toString());
						errorLog = errorLog + " | " + ("descripcion: " + objJsonContent.get("title").toString());
						errorLog = errorLog + " | " + ("usuarioBanner: " + objJsonPublisher.get("applicationName").toString());
						errorLog = errorLog + " | " + ("fechaImportacion: " + objJson.get("published").toString());
						errorLog = errorLog + " | " + ("fechaCreacion: " + objJson.get("published").toString());


						objEducationalInstitutions.setIdBachillerato(objJsonContent.get("id").toString());
						objEducationalInstitutions.setDescripcion(objJsonContent.get("title").toString());
						objEducationalInstitutions.setUsuarioBanner(objJsonPublisher.get("applicationName").toString());
						objEducationalInstitutions.setFechaImportacion(objJson.get("published").toString());
						objEducationalInstitutions.setFechaCreacion(objJson.get("published").toString());
						objEducationalInstitutions.setOperation(objJson.get("operation").toString());
						objEducationalInstitutions.setClave(objJsonContent.get("code").toString());

						errorLog = errorLog + " | " + "objEducationalInstitutions.setStreetLine1";

						objEducationalInstitutions.setStreetLine1("");
						objEducationalInstitutions.setStreetLine2("");
						objEducationalInstitutions.setStreetLine3("");
						objEducationalInstitutions.setNationCode("");
						objEducationalInstitutions.setStateCode("");
						objEducationalInstitutions.setCountyCode("");
						errorLog = errorLog + " | " + "objEducationalInstitutions.setCountyCode";
						objEducationalInstitutions.setIsEliminado(false);
						objEducationalInstitutions.setIsEnabled(true);
						objEducationalInstitutions.setPerteneceRed(false);
						objCatBachillerato.setPais("");
						objCatBachillerato.setEstado("");
						objCatBachillerato.setCiudad("");

						/*
						Iterator<JSONObject> iteratorAddresses = objJsonAddresses.iterator();
						while (iteratorAddresses.hasNext()) {
						  
						  objJsonAddressesData = iteratorAddresses.next();
						  objJsonAddressesDataAddress = (JSONObject) objJsonAddressesData.get("address");
						  errorLog = errorLog + " | " + ("idDireccion: "+objJsonAddressesDataAddress.get("id").toString());
						  objEducationalInstitutions.setIdDireccion(objJsonAddressesDataAddress.get("id").toString());
						}*/

						if (!objJson.get("operation").toString().equals("created")) {
							objJsonAddresses = (JSONArray) objJsonContent.get("addresses");
							Iterator < JSONObject > iteratorAddresses = objJsonAddresses.iterator();
							while (iteratorAddresses.hasNext()) {
								objJsonAddressesData = iteratorAddresses.next();
								objJsonAddressesDataAddress = (JSONObject) objJsonAddressesData.get("address");
								errorLog = errorLog + " | " + ("idDireccion: " + objJsonAddressesDataAddress.get("id").toString());
								objEducationalInstitutions.setIdDireccion(objJsonAddressesDataAddress.get("id").toString());
							}
						} else {
							errorLog = errorLog + " | objEducationalInstitutions.getIdBachillerato(): " + (objEducationalInstitutions.getIdBachillerato());
							errorLog = errorLog + " | objEducationalInstitutions.getDescripcion(): " + (objEducationalInstitutions.getDescripcion());
							errorLog = errorLog + " | objEducationalInstitutions.getUsuarioBanner(): " + (objEducationalInstitutions.getUsuarioBanner());
							errorLog = errorLog + " | objEducationalInstitutions.getFechaImportacion(): " + (objEducationalInstitutions.getFechaImportacion());
							errorLog = errorLog + " | objEducationalInstitutions.getFechaCreacion(): " + (objEducationalInstitutions.getFechaCreacion());
							errorLog = errorLog + " | objEducationalInstitutions.getOperation(): " + (objEducationalInstitutions.getOperation());
							errorLog = errorLog + " | barrerToken: " + (barrerToken);
							errorLog = errorLog + " | objJsonContent.get(id).toString(): " + (objJsonContent.get("id").toString());

							resultEducationalInstitutions = getConsumeEducationalInstitutions(barrerToken, objJsonContent.get("id").toString());
							errorLog = errorLog + " | " + ("getConsumeEducationalInstitutions ====================================");
							errorLog = errorLog + " | " + (resultEducationalInstitutions == null ? "resultEducationalInstitutions is null" : resultEducationalInstitutions);

							objJsonAddressData = (JSONObject) parser.parse(resultEducationalInstitutions);
							objJsonAddresses = (JSONArray) objJsonAddressData.get("addresses");
							Iterator < JSONObject > iteratorAddresses = objJsonAddresses.iterator();
							while (iteratorAddresses.hasNext()) {
								objJsonAddressesData = iteratorAddresses.next();
								objJsonAddressesDataAddress = (JSONObject) objJsonAddressesData.get("address");
								errorLog = errorLog + " | " + ("idDireccion: " + objJsonAddressesDataAddress.get("id").toString());
								objEducationalInstitutions.setIdDireccion(objJsonAddressesDataAddress.get("id").toString());
							}
						}
						objEducationalInstitutions.setTypeInd(objJsonContent.get("typeInd").toString());
						lstEducationalInstitutions.add(objEducationalInstitutions);

					break;
					case "addresses":
						errorLog = errorLog + " | addresses";
						objJsonPlace = (JSONObject) objJsonContent.get("place");
						lstAddressExtended = (JSONArray) objJsonContent.get("addressExtended");
						objJsonCountry = (JSONObject) objJsonPlace.get("country");
						objJsonRegion = (JSONObject) objJsonCountry.get("region");

						errorLog = errorLog + " | " + ("idDireccion: " + objJsonContent.get("id").toString());
						errorLog = errorLog + " | " + ("pais: " + objJsonCountry.get("title").toString());
						errorLog = errorLog + " | " + ("Estado: " + objJsonRegion.get("title").toString());
						errorLog = errorLog + " | " + ("ciudad: " + objJsonCountry.get("locality").toString());
						objAddresses.setIdDireccion(objJsonContent.get("id").toString());

						objAddresses.setOperation(objJson.get("operation").toString());
						if (objJsonCountry.get("title").toString().equals("México")) {
							objAddresses.setPais(objJsonCountry.get("title").toString());
							objAddresses.setEstado(objJsonRegion.get("title").toString());
							objAddresses.setCiudad(objJsonCountry.get("locality").toString());
						} else {
							objAddresses.setPais(objJsonCountry.get("title").toString());
							objAddresses.setEstado(objJsonRegion.get("title").toString().equals("Estado Extranjero") ? "" : objJsonRegion.get("title").toString());
							objAddresses.setCiudad(objJsonCountry.get("locality").toString().equals("000000") ? "" : objJsonCountry.get("locality").toString());
						}

						Iterator < JSONObject > iteratorAddressExtended = lstAddressExtended.iterator();
						while (iteratorAddressExtended.hasNext()) {
							objJsonAddressExtended = iteratorAddressExtended.next();
							objAddresses.setStreetLine1(objJsonAddressExtended.get("streetLine1").toString());
							objAddresses.setStreetLine2(objJsonAddressExtended.get("streetLine2").toString());
							objAddresses.setStreetLine3(objJsonAddressExtended.get("streetLine3").toString());
							objAddresses.setNationCode(objJsonAddressExtended.get("nationCode").toString());
							objAddresses.setStateCode(objJsonAddressExtended.get("stateCode").toString());
							objAddresses.setCountyCode(objJsonAddressExtended.get("countyCode").toString());
						}



						lstAddresses.add(objAddresses);
					break;
					default:
						errorLog = errorLog + " | " + (objJsonResource.get("name").toString());
					break;
				}

			}
			errorLog = errorLog + " | " + ("--------------------------------------------------------");

			/*for(CatBachillerato row : lstEducationalInstitutions) {
		objCatBachillerato= new CatBachillerato();
		objCatBachillerato.setIdBachillerato(row.getIdBachillerato());
		objCatBachillerato.setDescripcion(row.getDescripcion());
		objCatBachillerato.setUsuarioBanner(row.getUsuarioBanner());
		objCatBachillerato.setFechaImportacion(row.getFechaImportacion());
		objCatBachillerato.setFechaCreacion(row.getFechaCreacion());
		objCatBachillerato.setOperation(row.getOperation());
		objCatBachillerato.setClave(row.getClave());
		objCatBachillerato.setIsEliminado(false);
		objCatBachillerato.setIsEnabled(true);
		objCatBachillerato.setPerteneceRed(false);
		objCatBachillerato.setTypeInd(row.getTypeInd())
		
		objCatBachillerato.setStreetLine1(row.getStreetLine1())
		objCatBachillerato.setStreetLine2(row.getStreetLine2())
		objCatBachillerato.setStreetLine3(row.getStreetLine3())
		objCatBachillerato.setNationCode(row.getNationCode())
		objCatBachillerato.setStateCode(row.getStateCode())
		objCatBachillerato.setCountyCode(row.getCountyCode())
		objCatBachillerato.setIdDireccion(row.getIdDireccion());

		errorLog = errorLog + " | row.getIdBachillerato(): "+(row.getIdBachillerato());
		errorLog = errorLog + " | row.getDescripcion(): "+(row.getDescripcion());
		errorLog = errorLog + " | row.getUsuarioBanner(): "+(row.getUsuarioBanner());
		errorLog = errorLog + " | row.getFechaImportacion(): "+(row.getFechaImportacion());
		errorLog = errorLog + " | row.getFechaCreacion(): "+(row.getFechaCreacion());
		errorLog = errorLog + " | row.getOperation(): "+(row.getOperation());
		errorLog = errorLog + " | row.getClave(): "+(row.getClave());
		objCatBachillerato.setPais("");
		objCatBachillerato.setEstado("");
		objCatBachillerato.setCiudad("");
				

		lstCatBachillerato.add(objCatBachillerato);
	  }*/

			processInstance = null;
			processAPI = context.getApiClient().getProcessAPI();

			contracto = new HashMap < String, Serializable > ();
			objCatBachilleratosInput = new HashMap < String, Serializable > ();
			lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();

			searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Editar Bachilleratos");
			searchOptionsProccess = searchBuilderProccess.done();
			SearchResult < ProcessDeploymentInfo > SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
			lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
			for (ProcessDeploymentInfo objProcessDeploymentInfo: lstProcessDeploymentInfo) {
				if (objProcessDeploymentInfo.getActivationState().toString().equals("ENABLED")) {
					processId = objProcessDeploymentInfo.getProcessId();
				}
			}

			searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Agregar Bachilleratos");
			searchOptionsProccess = searchBuilderProccess.done();
			SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
			lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
			for (ProcessDeploymentInfo objProcessDeploymentInfo: lstProcessDeploymentInfo) {
				if (objProcessDeploymentInfo.getActivationState().toString().equals("ENABLED")) {
					processIdCrear = objProcessDeploymentInfo.getProcessId();
				}
			}

			/*--------------------------------------------------------------------------------------------------------------*/

			errorLog = errorLog + " | " + ("====================================");
			errorLog = errorLog + " | lstEducationalInstitutions.size():" + (lstEducationalInstitutions.size());
			def catBachilleratosDAO = context.getApiClient().getDAO(CatBachilleratosDAO.class);
			for (CatBachillerato row: lstEducationalInstitutions) {
				errorLog = errorLog + " | row.getOperation(): " + (row.getOperation());
				errorLog = errorLog + " | row.getUsuarioBanner(): " + (row.getUsuarioBanner());
				errorLog = errorLog + " | row.getEstado(): " + (row.getEstado());
				errorLog = errorLog + " | row.getCiudad(): " + (row.getCiudad());
				errorLog = errorLog + " | row.getPais(): " + (row.getPais());
				errorLog = errorLog + " | row.getIdBachillerato(): " + (row.getIdBachillerato());
				errorLog = errorLog + " | row.getOperation(): " + (row.getOperation());
				errorLog = errorLog + " | row.getClave(): " + (row.getClave());
				errorLog = errorLog + " | row.getDescripcion(): " + (row.getDescripcion());

				if (row.getOperation().equals("replaced")) {
					//if(operacion.equals("replaced")) {
					errorLog = errorLog + " | " + row.getOperation();

					lstCatBachilleratos = catBachilleratosDAO.findById(row.getIdBachillerato(), 0, 100);
					if (lstCatBachilleratos != null) {
						for (CatBachilleratos objRow: lstCatBachilleratos) {
							
							isMexicoOk = false;
							isUsaOk = false;
							isOtroPaisOk = false;
							isStateCodeOk = false;
							isNationCodeOk = false;
							isCountyCodeOk = false;
							isNationCodeLetterOk = false;
							
							if (objRow.getPais().equals("México")) {
								strStateCode = objRow.getStateCode() == null ? "" : objRow.getStateCode();
								if (!strStateCode.equals("")) {
									isStateCodeOk = strStateCode.substring(0, 1).toLowerCase().equals("m")
								}
								strNationCode = objRow.getNationCode() == null ? "" : objRow.getNationCode();
								
								if (!strNationCode.equals("")) {
									isNationCodeOk = strNationCode.equals("99")
								}
							
								isMexicoOk = (isNationCodeOk && isStateCodeOk);
							} else {
								if (objRow.getPais().equals("Estados Unidos de América")) {
									strNationCode = objRow.getNationCode() == null ? "" : objRow.getNationCode();
									
									if (!strNationCode.equals("")) {
										isNationCodeOk = strNationCode.equals("157")
										matcher = pattern.matcher(strNationCode)
										isNationCodeLetterOk = !matcher.find();
									}
							
									strCountyCode = objRow.getCountyCode() == null ? "" : objRow.getCountyCode();
									
									if (!strCountyCode.equals("")) {
										isCountyCodeOk = strCountyCode.equals("20000")
									}
									isUsaOk = (isNationCodeOk && isCountyCodeOk && isNationCodeLetterOk);
								} else {
									strNationCode = objRow.getNationCode() == null ? "" : objRow.getNationCode();
									
									if (!strNationCode.equals("")) {
										isNationCodeOk = !strNationCode.equals("157") && !strNationCode.equals("99");
										matcher = pattern.matcher(strNationCode)
										isNationCodeLetterOk = !matcher.find();
									}
									strCountyCode = objRow.getCountyCode() == null ? "" : objRow.getCountyCode();
									
									if (!strCountyCode.equals("")) {
										isCountyCodeOk = strCountyCode.equals("20000")
									}
									isOtroPaisOk = (isNationCodeOk && isCountyCodeOk && isNationCodeLetterOk);

								}
							}
						
							//if (!objRow.isIsEliminado()) {
								if (row.getTypeInd().equals("H")) {
									lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
									objCatBachilleratosInput = new HashMap < String, Serializable > ();
									contracto = new HashMap < String, Serializable > ();
									errorLog = errorLog + " | CON H ---------------------------------------------------------";
									
									errorLog = errorLog + " | persistenceId: " + objRow.getPersistenceId();
									errorLog = errorLog + " | persistenceVersion: " + objRow.getPersistenceVersion();
									errorLog = errorLog + " | isEliminado: " + !(row.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) );
									errorLog = errorLog + " | isEnabled: " + true;
									errorLog = errorLog + " | todelete: " + (row.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ) ? "false" : "true";
									errorLog = errorLog + " | perteneceRed: " + false;
									errorLog = errorLog + " | region: " + null;
									errorLog = errorLog + " | caseId: " + null;
									errorLog = errorLog + " | clave: " + objRow.getClave();
									errorLog = errorLog + " | fechaImportacion: " + null;
									errorLog = errorLog + " | fechaCreacion: " + null;
									errorLog = errorLog + " | usuarioCreacion: " + "Administrador";
									errorLog = errorLog + " | descripcion: " + objRow.getDescripcion();
									errorLog = errorLog + " | usuarioBanner: " + row.getUsuarioBanner();
									errorLog = errorLog + " | estado: " + objRow.getEstado();
									errorLog = errorLog + " | ciudad: " + objRow.getCiudad();
									errorLog = errorLog + " | pais: " + objRow.getPais();
									errorLog = errorLog + " | id: " + row.getIdBachillerato();
									errorLog = errorLog + " | streetLine1: " + objRow.getStreetLine1();
									errorLog = errorLog + " | streetLine2: " + objRow.getStreetLine2();
									errorLog = errorLog + " | streetLine3: " + objRow.getStreetLine3();
									errorLog = errorLog + " | nationCode: " + objRow.getNationCode();
									errorLog = errorLog + " | stateCode: " + objRow.getStateCode();
									errorLog = errorLog + " | countyCode: " + objRow.getCountyCode();
									errorLog = errorLog + " | typeInd: " + row.getTypeInd();
									errorLog = errorLog + " | CON H ---------------------------------------------------------";
									
									/*CONSTRUCCION DE CONTRATO=====================================================================*/
									objCatBachilleratosInput.put("persistenceId", objRow.getPersistenceId());
									objCatBachilleratosInput.put("persistenceVersion", objRow.getPersistenceVersion());
									objCatBachilleratosInput.put("isEliminado", !(row.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ));
									objCatBachilleratosInput.put("isEnabled", true);
									objCatBachilleratosInput.put("todelete", (row.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ) ? "false" : "true");
									objCatBachilleratosInput.put("perteneceRed", false);
									objCatBachilleratosInput.put("region", null);
									objCatBachilleratosInput.put("caseId", null);
									objCatBachilleratosInput.put("clave", objRow.getClave());
									objCatBachilleratosInput.put("fechaImportacion", null);
									objCatBachilleratosInput.put("fechaCreacion", null);
									objCatBachilleratosInput.put("usuarioCreacion", "Administrador");
									objCatBachilleratosInput.put("descripcion", objRow.getDescripcion());
									objCatBachilleratosInput.put("usuarioBanner", row.getUsuarioBanner());
									objCatBachilleratosInput.put("estado", objRow.getEstado());
									objCatBachilleratosInput.put("ciudad", objRow.getCiudad());
									objCatBachilleratosInput.put("pais", objRow.getPais());
									objCatBachilleratosInput.put("id", row.getIdBachillerato());

									objCatBachilleratosInput.put("streetLine1", objRow.getStreetLine1());
									objCatBachilleratosInput.put("streetLine2", objRow.getStreetLine2());
									objCatBachilleratosInput.put("streetLine3", objRow.getStreetLine3());
									objCatBachilleratosInput.put("nationCode", objRow.getNationCode());
									objCatBachilleratosInput.put("stateCode", objRow.getStateCode());
									objCatBachilleratosInput.put("countyCode", objRow.getCountyCode());
									objCatBachilleratosInput.put("typeInd", row.getTypeInd());

									lstCatBachilleratosInput.add(objCatBachilleratosInput);
									contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
									processInstance = processAPI.startProcessWithInputs(processId, contracto);
								} else {
									lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
									objCatBachilleratosInput = new HashMap < String, Serializable > ();
									contracto = new HashMap < String, Serializable > ();
									/*CONSTRUCCION DE CONTRATO====================================================================*/
									errorLog = errorLog + " | SIN H ---------------------------------------------------------";
									errorLog = errorLog + " | persistenceId " + objRow.getPersistenceId();
									errorLog = errorLog + " | persistenceVersion " + objRow.getPersistenceVersion();
									errorLog = errorLog + " | isEliminado " + true;
									errorLog = errorLog + " | isEnabled " + true;
									errorLog = errorLog + " | todelete " + "true";
									errorLog = errorLog + " | perteneceRed " + false;
									errorLog = errorLog + " | region " + null;
									errorLog = errorLog + " | caseId " + null;
									errorLog = errorLog + " | clave " + objRow.getClave();
									errorLog = errorLog + " | fechaImportacion " + null;
									errorLog = errorLog + " | fechaCreacion " + null;
									errorLog = errorLog + " | usuarioCreacion " + "Administrador";
									errorLog = errorLog + " | descripcion " + objRow.getDescripcion();
									errorLog = errorLog + " | usuarioBanner " + row.getUsuarioBanner();
									errorLog = errorLog + " | estado " + row.getEstado();
									errorLog = errorLog + " | ciudad " + row.getCiudad();
									errorLog = errorLog + " | pais " + row.getPais();
									errorLog = errorLog + " | id " + row.getIdBachillerato();
									errorLog = errorLog + " | streetLine1 " + objRow.getStreetLine1();
									errorLog = errorLog + " | streetLine2 " + objRow.getStreetLine2();
									errorLog = errorLog + " | streetLine3 " + objRow.getStreetLine3();
									errorLog = errorLog + " | nationCode " + objRow.getNationCode();
									errorLog = errorLog + " | stateCode " + objRow.getStateCode();
									errorLog = errorLog + " | countyCode " + objRow.getCountyCode();
									errorLog = errorLog + " | typeInd " + row.getTypeInd();
									errorLog = errorLog + " | SIN H ---------------------------------------------------------";
									
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

									objCatBachilleratosInput.put("streetLine1", objRow.getStreetLine1());
									objCatBachilleratosInput.put("streetLine2", objRow.getStreetLine2());
									objCatBachilleratosInput.put("streetLine3", objRow.getStreetLine3());
									objCatBachilleratosInput.put("nationCode", objRow.getNationCode());
									objCatBachilleratosInput.put("stateCode", objRow.getStateCode());
									objCatBachilleratosInput.put("countyCode", objRow.getCountyCode());
									objCatBachilleratosInput.put("typeInd", row.getTypeInd());

									lstCatBachilleratosInput.add(objCatBachilleratosInput);
									contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
									processInstance = processAPI.startProcessWithInputs(processId, contracto);

								}
							//}
						}
					}
				} else {
					if (row.getOperation().equals("created")) {
						errorLog = errorLog + " | created===================================================================================================";
						lstCatBachilleratos = catBachilleratosDAO.findById(row.getIdBachillerato(), 0, 100);

						if (lstCatBachilleratos != null && lstCatBachilleratos.size() > 0) {
							errorLog = errorLog + " | IF BACHILLERATO NULL======================================================================================";
							for (CatBachilleratos objRow: lstCatBachilleratos) {
								if (!objRow.isIsEliminado()) {
									if (row.getTypeInd().equals("H")) {
										errorLog = errorLog + " | IF IS ELIMANDO ===========================================================================================";
										errorLog = errorLog + " | PersistenceId:" + objRow.getPersistenceId();
										errorLog = errorLog + " | Descripcion:" + objRow.getDescripcion();
										errorLog = errorLog + " | ================================================================== | ";
										
										isMexicoOk = false;
										isUsaOk = false;
										isOtroPaisOk = false;
										isStateCodeOk = false;
										isNationCodeOk = false;
										isCountyCodeOk = false;
										isNationCodeLetterOk = false;
										
										errorLog = errorLog + " | " + ("====================================");
										errorLog = errorLog + " | " + ("Validar Pais y estado");
										if (row.getPais().equals("México")) {
											errorLog = errorLog + " | " + (row.getPais());
										
											strStateCode = row.getStateCode() == null ? "" : row.getStateCode();
											errorLog = errorLog + " | strStateCode: " + (strStateCode);
											if (!strStateCode.equals("")) {
												isStateCodeOk = strStateCode.substring(0, 1).toLowerCase().equals("m")
											}
										
											strNationCode = row.getNationCode() == null ? "" : row.getNationCode();
											errorLog = errorLog + " | strNationCode: " + (strNationCode);
											if (!strNationCode.equals("")) {
												isNationCodeOk = strNationCode.equals("99")
											}
										
											isMexicoOk = (isNationCodeOk && isStateCodeOk);
										} else {
											if (row.getPais().equals("Estados Unidos de América")) {
												errorLog = errorLog + " | " + (row.getPais());
												strNationCode = row.getNationCode() == null ? "" : row.getNationCode();
												errorLog = errorLog + " | strNationCode: " + (strNationCode);
												if (!strNationCode.equals("")) {
													isNationCodeOk = strNationCode.equals("157")
													matcher = pattern.matcher(strNationCode)
													isNationCodeLetterOk = !matcher.find();
												}
										
												strCountyCode = row.getCountyCode() == null ? "" : row.getCountyCode();
												errorLog = errorLog + " | strCountyCode: " + (strCountyCode);
												if (!strCountyCode.equals("")) {
													isCountyCodeOk = strCountyCode.equals("20000")
												}
												isUsaOk = (isNationCodeOk && isCountyCodeOk && isNationCodeLetterOk);
											} else {
												errorLog = errorLog + " | " + (row.getPais());
												strNationCode = row.getNationCode() == null ? "" : row.getNationCode();
												errorLog = errorLog + " | strNationCode: " + (strNationCode);
												if (!strNationCode.equals("")) {
													isNationCodeOk = !strNationCode.equals("157") && !strNationCode.equals("99");
													matcher = pattern.matcher(strNationCode)
													isNationCodeLetterOk = !matcher.find();
												}
												strCountyCode = row.getCountyCode() == null ? "" : row.getCountyCode();
												errorLog = errorLog + " | strCountyCode: " + (strCountyCode);
												if (!strCountyCode.equals("")) {
													isCountyCodeOk = strCountyCode.equals("20000")
												}
												isOtroPaisOk = (isNationCodeOk && isCountyCodeOk && isNationCodeLetterOk);
											}
										}
										
										
										
										
										lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
										objCatBachilleratosInput = new HashMap < String, Serializable > ();
										contracto = new HashMap < String, Serializable > ();
										/*CONSTRUCCION DE CONTRATO=====================================================================*/
										objCatBachilleratosInput.put("persistenceId", objRow.getPersistenceId());
										objCatBachilleratosInput.put("persistenceVersion", objRow.getPersistenceVersion());
										objCatBachilleratosInput.put("isEliminado", !(row.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ));
										objCatBachilleratosInput.put("isEnabled", true);
										objCatBachilleratosInput.put("todelete", (row.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ) ? "false" : "true");
										objCatBachilleratosInput.put("perteneceRed", false);
										objCatBachilleratosInput.put("region", null);
										objCatBachilleratosInput.put("caseId", null);
										objCatBachilleratosInput.put("clave", objRow.getClave());
										objCatBachilleratosInput.put("fechaImportacion", null);
										objCatBachilleratosInput.put("fechaCreacion", null);
										objCatBachilleratosInput.put("usuarioCreacion", "Administrador");
										objCatBachilleratosInput.put("descripcion", row.getDescripcion());
										objCatBachilleratosInput.put("usuarioBanner", row.getUsuarioBanner());
										objCatBachilleratosInput.put("estado", row.getEstado());
										objCatBachilleratosInput.put("ciudad", row.getCiudad());
										objCatBachilleratosInput.put("pais", row.getPais());
										objCatBachilleratosInput.put("id", row.getIdBachillerato());

										objCatBachilleratosInput.put("streetLine1", row.getStreetLine1());
										objCatBachilleratosInput.put("streetLine2", row.getStreetLine2());
										objCatBachilleratosInput.put("streetLine3", row.getStreetLine3());
										objCatBachilleratosInput.put("nationCode", row.getNationCode());
										objCatBachilleratosInput.put("stateCode", row.getStateCode());
										objCatBachilleratosInput.put("countyCode", row.getCountyCode());
										objCatBachilleratosInput.put("typeInd", row.getTypeInd());

										lstCatBachilleratosInput.add(objCatBachilleratosInput);

										contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
										processInstance = processAPI.startProcessWithInputs(processId, contracto);
									} else {
										errorLog = errorLog + " | CREATED DELETE";

										errorLog = errorLog + " | PersistenceId:" + objRow.getPersistenceId();
										errorLog = errorLog + " | Descripcion:" + objRow.getDescripcion();
										errorLog = errorLog + " | ================================================================== | ";
										lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
										objCatBachilleratosInput = new HashMap < String, Serializable > ();
										contracto = new HashMap < String, Serializable > ();
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

										objCatBachilleratosInput.put("streetLine1", objRow.getStreetLine1());
										objCatBachilleratosInput.put("streetLine2", objRow.getStreetLine2());
										objCatBachilleratosInput.put("streetLine3", objRow.getStreetLine3());
										objCatBachilleratosInput.put("nationCode", objRow.getNationCode());
										objCatBachilleratosInput.put("stateCode", objRow.getStateCode());
										objCatBachilleratosInput.put("countyCode", objRow.getCountyCode());
										objCatBachilleratosInput.put("typeInd", row.getTypeInd());

										lstCatBachilleratosInput.add(objCatBachilleratosInput);
										contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
										processInstance = processAPI.startProcessWithInputs(processId, contracto);

									}
								}
							}
						} else {
							if (row.getTypeInd().equals("H")) {
								errorLog = errorLog + " | ELSE CREAR DATA ==========================================================================================";
								errorLog = errorLog + " | " + row.getOperation();
								errorLog = errorLog + " | processIdCrear: " + processIdCrear;
								errorLog = errorLog + " | TypeInd: " + row.getTypeInd()
								lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
								objCatBachilleratosInput = new HashMap < String, Serializable > ();
								contracto = new HashMap < String, Serializable > ();
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
								objCatBachilleratosInput.put("idDireccion", row.getIdDireccion());

								/*eddressExtended--------------------------------------------------------------------------------*/
								objCatBachilleratosInput.put("streetLine1", row.getStreetLine1());
								objCatBachilleratosInput.put("streetLine2", row.getStreetLine2());
								objCatBachilleratosInput.put("streetLine3", row.getStreetLine3());
								objCatBachilleratosInput.put("nationCode", row.getNationCode());
								objCatBachilleratosInput.put("stateCode", row.getStateCode());
								objCatBachilleratosInput.put("countyCode", row.getCountyCode());
								objCatBachilleratosInput.put("typeInd", row.getTypeInd());

								lstCatBachilleratosInput.add(objCatBachilleratosInput);
								contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
								processInstance = processAPI.startProcessWithInputs(processIdCrear, contracto);
								errorLog = errorLog + " | processInstance: " + processInstance;
							} else {
								errorLog = errorLog + " | CREATED DELETEeeeeeeeeeeeeeeeeeee";
								errorLog = errorLog + " | TypeInd: " + row.getTypeInd()
								errorLog = errorLog + " | ELSE CREAR DATA DELETE ==========================================================================================";
								errorLog = errorLog + " | " + row.getOperation();
								errorLog = errorLog + " | processIdCrear: " + processIdCrear;
								lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
								objCatBachilleratosInput = new HashMap < String, Serializable > ();
								contracto = new HashMap < String, Serializable > ();
								/*CONSTRUCCION DE CONTRATO=====================================================================*/
								objCatBachilleratosInput.put("isEliminado", true);
								objCatBachilleratosInput.put("isEnabled", true);
								objCatBachilleratosInput.put("todelete", "true");
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
								objCatBachilleratosInput.put("idDireccion", row.getIdDireccion());

								/*eddressExtended--------------------------------------------------------------------------------*/
								objCatBachilleratosInput.put("streetLine1", row.getStreetLine1());
								objCatBachilleratosInput.put("streetLine2", row.getStreetLine2());
								objCatBachilleratosInput.put("streetLine3", row.getStreetLine3());
								objCatBachilleratosInput.put("nationCode", row.getNationCode());
								objCatBachilleratosInput.put("stateCode", row.getStateCode());
								objCatBachilleratosInput.put("countyCode", row.getCountyCode());
								objCatBachilleratosInput.put("typeInd", row.getTypeInd());
								
								errorLog = errorLog + " | ---------------------------------------------------------";
								errorLog = errorLog + " | " + ("isEliminado: "+ objCatBachilleratosInput.get("isEliminado"));
								errorLog = errorLog + " | " + ("isEnabled: "+ objCatBachilleratosInput.get("isEnabled"));
								errorLog = errorLog + " | " + ("todelete: "+ objCatBachilleratosInput.get("todelete"));
								errorLog = errorLog + " | " + ("perteneceRed: "+ objCatBachilleratosInput.get("perteneceRed"));
								errorLog = errorLog + " | " + ("region: "+ objCatBachilleratosInput.get("region"));
								errorLog = errorLog + " | " + ("caseId: "+ objCatBachilleratosInput.get("caseId"));
								errorLog = errorLog + " | " + ("clave: "+ objCatBachilleratosInput.get("clave"));
								errorLog = errorLog + " | " + ("fechaImportacion: "+ objCatBachilleratosInput.get("fechaImportacion"));
								errorLog = errorLog + " | " + ("fechaCreacion: "+ objCatBachilleratosInput.get("fechaCreacion"));
								errorLog = errorLog + " | " + ("usuarioCreacion: "+ objCatBachilleratosInput.get("usuarioCreacion"));
								errorLog = errorLog + " | " + ("descripcion: "+ objCatBachilleratosInput.get("descripcion"));
								errorLog = errorLog + " | " + ("usuarioBanner: "+ objCatBachilleratosInput.get("usuarioBanner"));
								errorLog = errorLog + " | " + ("estado: "+ objCatBachilleratosInput.get("estado"));
								errorLog = errorLog + " | " + ("ciudad: "+ objCatBachilleratosInput.get("ciudad"));
								errorLog = errorLog + " | " + ("pais: "+ objCatBachilleratosInput.get("pais"));
								errorLog = errorLog + " | " + ("id: "+ objCatBachilleratosInput.get("id"));
								errorLog = errorLog + " | " + ("idDireccion: "+ objCatBachilleratosInput.get("idDireccion"));
								errorLog = errorLog + " | " + ("streetLine1: "+ objCatBachilleratosInput.get("streetLine1"));
								errorLog = errorLog + " | " + ("streetLine2: "+ objCatBachilleratosInput.get("streetLine2"));
								errorLog = errorLog + " | " + ("streetLine3: "+ objCatBachilleratosInput.get("streetLine3"));
								errorLog = errorLog + " | " + ("nationCode: "+ objCatBachilleratosInput.get("nationCode"));
								errorLog = errorLog + " | " + ("stateCode: "+ objCatBachilleratosInput.get("stateCode"));
								errorLog = errorLog + " | " + ("countyCode: "+ objCatBachilleratosInput.get("countyCode"));
								
								errorLog = errorLog + " | ---------------------------------------------------------";
								
								lstCatBachilleratosInput.add(objCatBachilleratosInput);
								contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
								processInstance = processAPI.startProcessWithInputs(processIdCrear, contracto);
								errorLog = errorLog + " | CREATED DELETE SUCCESS";
							}
						}
					} else {
						if (row.getOperation().equals("deleted")) {
							//if(operacion.equals("deleted")) {
							errorLog = errorLog + " | " + row.getOperation();

							lstCatBachilleratos = catBachilleratosDAO.findById(row.getIdBachillerato(), 0, 100);
							if (lstCatBachilleratos != null) {
								for (CatBachilleratos objRow: lstCatBachilleratos) {
									errorLog = errorLog + " | PersistenceId:" + objRow.getPersistenceId();
									errorLog = errorLog + " | Descripcion:" + objRow.getDescripcion();
									errorLog = errorLog + " | ================================================================== | ";
									lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
									objCatBachilleratosInput = new HashMap < String, Serializable > ();
									contracto = new HashMap < String, Serializable > ();
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

									objCatBachilleratosInput.put("streetLine1", objRow.getStreetLine1());
									objCatBachilleratosInput.put("streetLine2", objRow.getStreetLine2());
									objCatBachilleratosInput.put("streetLine3", objRow.getStreetLine3());
									objCatBachilleratosInput.put("nationCode", objRow.getNationCode());
									objCatBachilleratosInput.put("stateCode", objRow.getStateCode());
									objCatBachilleratosInput.put("countyCode", objRow.getCountyCode());
									objCatBachilleratosInput.put("typeInd", row.getTypeInd());

									lstCatBachilleratosInput.add(objCatBachilleratosInput);
									contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
									processInstance = processAPI.startProcessWithInputs(processId, contracto);
								}
							}
						}
					}
				}
			}

			errorLog = errorLog + " | " + ("====================================");
			errorLog = errorLog + " | lstAddresses.size():" + (lstAddresses.size());
			for (CatBachillerato objLstAddresses: lstAddresses) {
				//if(objLstAddresses.getOperation().equals("replaced")) {
				errorLog = errorLog + " | objLstAddresses.getOperation():" + (objLstAddresses.getOperation());
				errorLog = errorLog + " | objLstAddresses.getIdDireccion():" + (objLstAddresses.getIdDireccion());

				isMexicoOk = false;
				isUsaOk = false;
				isOtroPaisOk = false;
				isStateCodeOk = false;
				isNationCodeOk = false;
				isCountyCodeOk = false;

				errorLog = errorLog + " | " + ("====================================");
				errorLog = errorLog + " | " + ("Validar Pais y estado");
				if (objLstAddresses.getPais().equals("México")) {
					errorLog = errorLog + " | " + (objLstAddresses.getPais());

					strStateCode = objLstAddresses.getStateCode() == null ? "" : objLstAddresses.getStateCode();
					errorLog = errorLog + " | strStateCode: " + (strStateCode);
					if (!strStateCode.equals("")) {
						isStateCodeOk = strStateCode.substring(0, 1).toLowerCase().equals("m")
					}

					strNationCode = objLstAddresses.getNationCode() == null ? "" : objLstAddresses.getNationCode();
					errorLog = errorLog + " | strNationCode: " + (strNationCode);
					if (!strNationCode.equals("")) {
						isNationCodeOk = strNationCode.equals("99")
					}

					isMexicoOk = (isNationCodeOk && isStateCodeOk);
				} else {
					if (objLstAddresses.getPais().equals("Estados Unidos de América")) {
						errorLog = errorLog + " | " + (objLstAddresses.getPais());
						strNationCode = objLstAddresses.getNationCode() == null ? "" : objLstAddresses.getNationCode();
						errorLog = errorLog + " | strNationCode: " + (strNationCode);
						if (!strNationCode.equals("")) {
							isNationCodeOk = strNationCode.equals("157")
							matcher = pattern.matcher(strNationCode)
							isNationCodeLetterOk = !matcher.find();
						}

						strCountyCode = objLstAddresses.getCountyCode() == null ? "" : objLstAddresses.getCountyCode();
						errorLog = errorLog + " | strCountyCode: " + (strCountyCode);
						if (!strCountyCode.equals("")) {
							isCountyCodeOk = strCountyCode.equals("20000")
						}
						isUsaOk = (isNationCodeOk && isCountyCodeOk && isNationCodeLetterOk);
					} else {
						errorLog = errorLog + " | " + (objLstAddresses.getPais());
						strNationCode = objLstAddresses.getNationCode() == null ? "" : objLstAddresses.getNationCode();
						errorLog = errorLog + " | strNationCode: " + (strNationCode);
						if (!strNationCode.equals("")) {
							isNationCodeOk = !strNationCode.equals("157") && !strNationCode.equals("99");
							matcher = pattern.matcher(strNationCode)
							isNationCodeLetterOk = !matcher.find();
						}
						strCountyCode = objLstAddresses.getCountyCode() == null ? "" : objLstAddresses.getCountyCode();
						errorLog = errorLog + " | strCountyCode: " + (strCountyCode);
						if (!strCountyCode.equals("")) {
							isCountyCodeOk = strCountyCode.equals("20000")
						}
						isOtroPaisOk = (isNationCodeOk && isCountyCodeOk && isNationCodeLetterOk);
					}
				}

				lstCatBachilleratos = catBachilleratosDAO.findByIdDireccion(objLstAddresses.getIdDireccion(), 0, 100);
				for (CatBachilleratos objRow: lstCatBachilleratos) {
					//if (!objRow.isIsEliminado()) {

						errorLog = errorLog + " | PersistenceId:" + objRow.getPersistenceId();
						errorLog = errorLog + " | Descripcion:" + objRow.getDescripcion();
						errorLog = errorLog + " | ================================================================== | ";

						lstCatBachilleratosInput = new ArrayList < Map < String, Serializable >> ();
						objCatBachilleratosInput = new HashMap < String, Serializable > ();
						contracto = new HashMap < String, Serializable > ();
						/*CONSTRUCCION DE CONTRATO=====================================================================*/
						objCatBachilleratosInput.put("persistenceId", objRow.getPersistenceId());
						objCatBachilleratosInput.put("persistenceVersion", objRow.getPersistenceVersion());

						objCatBachilleratosInput.put("isEliminado", !(objRow.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ) );
						objCatBachilleratosInput.put("isEnabled", objRow.isIsEnabled());
						objCatBachilleratosInput.put("todelete", (objRow.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ) ? "false" : "true");

						objCatBachilleratosInput.put("perteneceRed", objRow.isPerteneceRed());
						objCatBachilleratosInput.put("region", null);
						objCatBachilleratosInput.put("caseId", null);
						objCatBachilleratosInput.put("clave", objRow.getClave());
						objCatBachilleratosInput.put("fechaImportacion", null);
						objCatBachilleratosInput.put("fechaCreacion", null);
						objCatBachilleratosInput.put("usuarioCreacion", "Administrador");
						objCatBachilleratosInput.put("descripcion", objRow.getDescripcion());
						objCatBachilleratosInput.put("usuarioBanner", objRow.getUsuarioBanner());
						objCatBachilleratosInput.put("estado", objLstAddresses.getEstado());
						objCatBachilleratosInput.put("ciudad", objLstAddresses.getCiudad());
						objCatBachilleratosInput.put("pais", objLstAddresses.getPais());
						objCatBachilleratosInput.put("id", objRow.getId());
						/*eddressExtended--------------------------------------------------------------------------------*/
						objCatBachilleratosInput.put("streetLine1", objLstAddresses.getStreetLine1());
						objCatBachilleratosInput.put("streetLine2", objLstAddresses.getStreetLine2());
						objCatBachilleratosInput.put("streetLine3", objLstAddresses.getStreetLine3());
						objCatBachilleratosInput.put("nationCode", objLstAddresses.getNationCode());
						objCatBachilleratosInput.put("stateCode", objLstAddresses.getStateCode());
						objCatBachilleratosInput.put("countyCode", objLstAddresses.getCountyCode());
						objCatBachilleratosInput.put("typeInd", objRow.getTypeInd());

						
						
						errorLog = errorLog + " | DIRECCION==========================================================================================";
						errorLog = errorLog + " | " + ("persistenceId - " + objRow.getPersistenceId());
						errorLog = errorLog + " | " + ("persistenceVersion - " + objRow.getPersistenceVersion());
						errorLog = errorLog + " | " + ("isEliminado - " + !( objRow.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ) );
						errorLog = errorLog + " | " + ("isEnabled - " + objRow.isIsEnabled());
						errorLog = errorLog + " | " + ("todelete - " + ( ( objRow.getTypeInd().equals("H") && (isMexicoOk || isUsaOk || isOtroPaisOk) ) ? "false" : "true" ) );
						errorLog = errorLog + " | " + ("perteneceRed - " + objRow.isPerteneceRed());
						errorLog = errorLog + " | " + ("region - " + null);
						errorLog = errorLog + " | " + ("caseId - " + null);
						errorLog = errorLog + " | " + ("clave - " + objRow.getClave());
						errorLog = errorLog + " | " + ("fechaImportacion - " + null);
						errorLog = errorLog + " | " + ("fechaCreacion - " + null);
						errorLog = errorLog + " | " + ("usuarioCreacion - " + "Administrador");
						errorLog = errorLog + " | " + ("descripcion - " + objRow.getDescripcion());
						errorLog = errorLog + " | " + ("usuarioBanner - " + objRow.getUsuarioBanner());
						errorLog = errorLog + " | " + ("estado - " + objLstAddresses.getEstado());
						errorLog = errorLog + " | " + ("ciudad - " + objLstAddresses.getCiudad());
						errorLog = errorLog + " | " + ("pais - " + objLstAddresses.getPais());
						errorLog = errorLog + " | " + ("id - " + objRow.getId());
						errorLog = errorLog + " | " + ("streetLine1 - " + objLstAddresses.getStreetLine1());
						errorLog = errorLog + " | " + ("streetLine2 - " + objLstAddresses.getStreetLine2());
						errorLog = errorLog + " | " + ("streetLine3 - " + objLstAddresses.getStreetLine3());
						errorLog = errorLog + " | " + ("nationCode - " + objLstAddresses.getNationCode());
						errorLog = errorLog + " | " + ("stateCode - " + objLstAddresses.getStateCode());
						errorLog = errorLog + " | " + ("countyCode - " + objLstAddresses.getCountyCode());
						errorLog = errorLog + " | DIRECCION==========================================================================================";

						lstCatBachilleratosInput.add(objCatBachilleratosInput);
						contracto.put("lstCatBachilleratosInput", lstCatBachilleratosInput);
						processInstance = processAPI.startProcessWithInputs(processId, contracto);
					/*} else {

						errorLog = errorLog + " | PersistenceId:" + objRow.getPersistenceId();
						errorLog = errorLog + " | Descripcion:" + objRow.getDescripcion();
						errorLog = errorLog + " | is eliminado";
						errorLog = errorLog + " | ================================================================== | ";

					}*/
				}
				/*}
				else {
				  errorLog = errorLog + " | ELSE ADDRESSES";
				  errorLog = errorLog + " | objLstAddresses.getOperation():" + (objLstAddresses.getOperation());
				}*/
			}

			resultado.setData(lstEducationalInstitutions);

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
		String urlParaVisitar = "https://integrate.elluciancloud.com/api/educational-institutions/" + idBachillerato;
		StringBuilder resultado = new StringBuilder();

		try {
			URL url = new URL(urlParaVisitar);
			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setRequestProperty("Authorization", "Bearer " + barrerToken);
			conexion.setRequestProperty("Accept", "application/vnd.hedtech.integration.v6+json");
			conexion.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
			String linea;
			while ((linea = rd.readLine()) != null) {
				resultado.append(linea);
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultado.toString();
	}
}