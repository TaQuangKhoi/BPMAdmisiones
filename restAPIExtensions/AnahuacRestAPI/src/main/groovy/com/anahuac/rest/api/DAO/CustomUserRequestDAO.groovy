package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import org.bonitasoft.engine.bpm.process.ActivationState
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.engine.api.APIClient
import com.bonitasoft.engine.api.ProcessAPI
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class CustomUserRequestDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogosDAO.class);
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
	
	public Result getActiveProcess(RestAPIContext context) {
		Result result = new Result();
		List<String> data = new ArrayList<String>();
		
		try {
			SearchOptionsBuilder searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Proceso admisiones");
			final SearchOptions searchOptionsProccess = searchBuilderProccess.done();
			SearchResult<ProcessDeploymentInfo> SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
			List<ProcessDeploymentInfo> lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
			
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
			
			for(ProcessDeploymentInfo  objProcessDeploymentInfo : lstProcessDeploymentInfo) {
				ActivationState acs = objProcessDeploymentInfo.getActivationState();
				if(acs.toString().equals("ENABLED")) {
					data.add(objProcessDeploymentInfo.getProcessId().toString());
				}
			}
			
			result.setData(data);
			result.setSuccess(true);
		} catch(Exception e) {
			LOGGER.error e.getMessage();
			LOGGER.error e.getCause();
			LOGGER.error e.getLocalizedMessage();
			result.setSuccess(false);
			result.setError(e.getMessage());
		}
		return result;
	}
}
