package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.document.DocumentValue
import org.bonitasoft.engine.bpm.document.impl.DocumentImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.model.SolicitudDeAdmision
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.engine.api.APIClient
import com.bonitasoft.web.extension.rest.RestAPIContext

class ProcessFileDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessFileDAO.class);
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
	
	public Result obtenerDocumentos(Long caseId, RestAPIContext context) {
		Result resultado = new Result();
		List<Object> lstResultado = new ArrayList<Object>();
		Map<String, String> mapDocumentos = new LinkedHashMap<String, String>();
		
		try {
			String username = "";
			String password = "";
			Properties prop = new Properties();
			String propFileName = "configuration.properties";
			InputStream inputStream;
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			username = prop.getProperty("USERNAME");
			password = prop.getProperty("PASSWORD");
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			apiClient.login(username, password);
			
			// TODO
			//Continuar con la lectura de archivos
			
			mapDocumentos.put("fotoPasaporte", getFileBase64(apiClient, caseId, "fotoPasaporte"));
			mapDocumentos.put("actaNacimiento", getFileBase64(apiClient, caseId, "actaNacimiento"));
			mapDocumentos.put("constancia", getFileBase64(apiClient, caseId, "constancia"));
			mapDocumentos.put("descuento", getFileBase64(apiClient, caseId, "descuento"));
			mapDocumentos.put("resultadoCB", getFileBase64(apiClient, caseId, "resultadoCB"));
		
//			DocumentImpl fotoPasaporte = apiClient.getProcessAPI().getLastDocument(caseId, "fotoPasaporte");
//			DocumentImpl fotoPasaporte = apiClient.getProcessAPI().getDocumentList(caseId, "fotoPasaporte", 0, 1).get(0);
//			Document fotoPasaporteDoc = apiClient.getProcessAPI().getDocument(fotoPasaporte.id);
//			DocumentValue fotoPasaporteDocValue = new DocumentValue(apiClient.getProcessAPI().getDocumentContent(fotoPasaporteDoc.getContentStorageId()), fotoPasaporteDoc.getContentMimeType(), fotoPasaporteDoc.getContentFileName());
//			String fileBase64 = Base64.getEncoder().encodeToString(fotoPasaporteDocValue.content);
//			
//			mapDocumentos.put("fotoPasaporte", fileBase64);
			
			lstResultado.add(mapDocumentos);
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		}catch(Exception ex) {
			LOGGER.error ex.getMessage()
			resultado.setSuccess(false)
			resultado.setError(ex.getMessage())
		}
		
		return resultado;
	}
	
	public String getFileBase64(org.bonitasoft.engine.api.APIClient apiClient, Long caseId, String fileName) {
		String fileBase64 = "";
		try {
			List<DocumentImpl> lstDocument = apiClient.getProcessAPI().getDocumentList(caseId, fileName, 0, 1);
			if(lstDocument.size() > 0) {
				DocumentImpl archivo = lstDocument.get(0);
				Document archivoDoc = apiClient.getProcessAPI().getDocument(archivo.id);
				DocumentValue archivoDocValue = new DocumentValue(apiClient.getProcessAPI().getDocumentContent(archivoDoc.getContentStorageId()), archivoDoc.getContentMimeType(), archivoDoc.getContentFileName());
				fileBase64 = Base64.getEncoder().encodeToString(archivoDocValue.content);
			} else {
				throw new Exception("Documento no encontrado");
			}
		} catch(Exception e) {
			fileBase64 = e.getMessage();
		}
		
		return fileBase64;
	}
}
