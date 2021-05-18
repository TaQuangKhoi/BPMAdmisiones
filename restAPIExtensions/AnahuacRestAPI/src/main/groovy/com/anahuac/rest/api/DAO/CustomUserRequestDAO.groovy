package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

import org.bonitasoft.engine.bpm.process.ActivationState
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor

import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.bpm.process.ProcessInstanceSearchDescriptor

import org.bonitasoft.engine.bpm.data.DataInstance
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor
import org.bonitasoft.engine.search.Order

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
	
	public Result getCurrentTaskId(String caseId, RestAPIContext context) {
		Result result = new Result();
		List<HumanTaskInstance> data = new ArrayList<HumanTaskInstance>();
		Integer inicioContador = 0;
		Integer finContador = 0;
		String taskid = "";
		try {
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
			searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID, caseId);
			
			final SearchOptions searchOptions = searchBuilder.done();
			SearchResult<HumanTaskInstance>  SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
			List<HumanTaskInstance> lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
			
			for(HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstanceSearch) {
				taskid = objHumanTaskInstance.getId().toString();
				data.add(objHumanTaskInstance);
			}
			
			result.setData(data);
			result.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error e.getMessage();
			LOGGER.error e.getCause();
			LOGGER.error e.getLocalizedMessage();
			result.setSuccess(false);
			result.setError(e.getMessage());
		}
		return result;
	}
	
	//TO-DO 
	public Result getCaseVariables(String caseId, RestAPIContext context) {
		
		Result result = new Result();
		Integer inicioContador = 0;
		Integer finContador = 0;
		String taskid = "";
		List<ProcessInstance> data = new ArrayList<ProcessInstance>();
		try {
			
			SearchOptionsBuilder searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Proceso admisiones");
			final SearchOptions searchOptionsProccess = searchBuilderProccess.done();
			SearchResult<ProcessDeploymentInfo> SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
			List<ProcessDeploymentInfo> lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
			
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
			searchBuilder.filter(ProcessInstanceSearchDescriptor.ID, caseId);
			final SearchOptions searchOptions = searchBuilder.done();
			SearchResult<ProcessDeploymentInfo> instances = context.getApiClient().getProcessAPI().searchProcessInstances(searchOptions);
			List<ProcessDeploymentInfo> lstInstances = SearchProcessDeploymentInfo.getResult();
			
			for(ProcessInstance objInstance : lstInstances) {
				data.add(objInstance);
				
			}
			
			result.setData(data);
			result.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error e.getMessage();
			LOGGER.error e.getCause();
			LOGGER.error e.getLocalizedMessage();
			result.setSuccess(false);
			result.setError(e.getMessage());
		}
		return result;
	}
	
	public Result reAssignTask(String task_id, String user_id, RestAPIContext context) {
		Result result = new Result();
		List<HumanTaskInstance> data = new ArrayList<HumanTaskInstance>();
		Integer inicioContador = 0;
		Integer finContador = 0;
//		String taskid = "";
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
			
			apiClient.processAPI.assignUserTask(task_id, user_id);
			
			result.setData(data);
			result.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error e.getMessage();
			LOGGER.error e.getCause();
			LOGGER.error e.getLocalizedMessage();
			result.setSuccess(false);
			result.setError(e.getMessage());
		}
		return result;
	}
}
