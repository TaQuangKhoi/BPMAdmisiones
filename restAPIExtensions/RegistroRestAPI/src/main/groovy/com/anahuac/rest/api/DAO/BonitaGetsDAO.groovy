package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.SimpleDateFormat
import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.flownode.ActivityInstance
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion
import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstance
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException
import org.bonitasoft.engine.exception.BonitaRuntimeException
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.bonitasoft.engine.search.Order
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utilities.LoadParametros
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.bonitasoft.web.extension.rest.RestApiController

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class BonitaGetsDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(BonitaGetsDAO.class)
	Connection con
	Statement stm
	ResultSet rs
	ResultSet rs2
	PreparedStatement pstm
	
	
	public Result getUserHumanTask(Long caseid,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean conection = false;
		try {
			String username = "";
			String password = "";
			
			List<ActivityInstance> activityInstances = [];
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)
			
			//org.bonitasoft.engine.api.APIClient apiClient = context.getApiClient();
			try {
				activityInstances = context.getApiClient().getProcessAPI().getActivities(caseid, 0, 1);
			}catch(Exception ex) {
				errorLog += ex;
			}
			
			resultado.setSuccess(true);
			resultado.setData(activityInstances)
			resultado.setError(errorLog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog)
			e.printStackTrace();
		}
		return resultado;
	}
	
	public Result getUserArchivedHumanTask(Long caseid,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean closeCon = false;
		try {
			
			List<ArchivedActivityInstance> activityInstances = [];
			
			String username = "";
			String password = "";
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)
			
			//org.bonitasoft.engine.api.APIClient apiClient = context.getApiClient();
			try {
				
				closeCon = validarConexionBonita()
				
				pstm = con.prepareStatement("SELECT * FROM arch_flownode_instance WHERE sourceobjectid = ${caseid}");
				rs = pstm.executeQuery();
				if(rs.next()) {
					try {
						errorLog+=rs.getLong("id")
						activityInstances = context.getApiClient().getProcessAPI().getArchivedActivityInstances(rs.getLong("id"), 0, 10, ActivityInstanceCriterion.DEFAULT)
					}catch(BonitaRuntimeException a) {
						errorLog+="error"+a
					}
					
				}
				
			}catch(Exception ex) {
				errorLog += ex;
			}
			
			resultado.setSuccess(true);
			resultado.setData(activityInstances)
			resultado.setError(errorLog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog)
			e.printStackTrace();
		}
		finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result getUserProcess(RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean closeCon = false;
		try {
			String username = "";
			String password = "";
			
			List<ProcessDeploymentInfo> map = [];
			ProcessDeploymentInfo info;
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)
			
			//org.bonitasoft.engine.api.APIClient apiClient = context.getApiClient();
			try {
				closeCon = validarConexionBonita()
				pstm = con.prepareStatement(" SELECT processid FROM process_definition WHERE NAME = 'Proceso admisiones' and activationstate = 'ENABLED' ");
				rs = pstm.executeQuery();
				if(rs.next()) {
					info = apiClient.getProcessAPI().getProcessDeploymentInfo(rs.getLong("processid"));
				}
			}catch(Exception ex) {
				errorLog += ex;
			}
			map.add(info)
			resultado.setSuccess(true);
			resultado.setData(map)
			resultado.setError(errorLog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog)
			e.printStackTrace();
		} 
		finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	
	public Result getCase() {
		Result resultado = new Result();
		
		resultado.setSuccess(true)
		resultado.setData([]);
		return resultado;
	}
	
	public Result getCaseVariable(Long caseid,String name,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="", where = "";
		Boolean closeCon = false;
		try {
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			if(name.length() > 0) {
				where = "AND name =  '${name}'"
			}
			
			closeCon = validarConexionBonita()
			pstm = con.prepareStatement(" SELECT containerid AS case_id,name,description,classname AS type,booleanvalue,shorttextvalue,clobvalue,longvalue FROM data_instance WHERE containerid = ${caseid} ${where}");
			rs = pstm.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("type") || metaData.getColumnLabel(i).toLowerCase().equals("case_id") || metaData.getColumnLabel(i).toLowerCase().equals("name") || metaData.getColumnLabel(i).toLowerCase().equals("description")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
					if(metaData.getColumnLabel(i).toLowerCase().equals("type")) {
						if(rs.getString(i).equals("java.lang.Boolean")) {
							columns.put("value", rs.getString("booleanvalue")?.toLowerCase())
						} else 
							if(rs.getString(i).equals("java.lang.String")) {
							columns.put("value", rs.getString("shorttextvalue"))
						} else 
							if(rs.getString(i).equals("java.lang.Long")) {
							columns.put("value", rs.getString("longvalue"))
						} else
							 if(rs.getString(i).equals("java.util.Map")) {
							columns.put("value", rs.getString("clobvalue"))
						} else 
							if(rs.getString(i).equals("java.util.Date")) {
								if(rs.getString("longvalue") != null && rs.getString("longvalue") != 'null') {
									Date currentDate = new Date(rs.getLong("longvalue"));
									columns.put("value", currentDate.toString() )
								} else {
									columns.put("value", null )
								}
								
						}
						
					}
				}
	
					rows.add(columns);
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows)
			resultado.setError(errorLog);
			
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog)
			e.printStackTrace();
		}
		finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	
	public Result getUserContext(Long caseid,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		Boolean closeCon = false;
		try {
			String username = "";
			String password = "";
			def ref;
			List < Map < String, Serializable >> rows = new ArrayList < Map < String, Serializable >> ();
			Map<String, Serializable> contexto;
			Map<String, Serializable> contexto2 = new HashMap<String, Serializable>();
			Map<String, Serializable> contextoDetalle = new HashMap<String, Serializable>();
			List < Map < String, Serializable >> foto = new ArrayList < Map < String, Serializable >> ();
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)
			
			
			try {
				contexto = apiClient.getProcessAPI().getProcessInstanceExecutionContext(caseid);
				boolean link = false
				for ( prop in contexto ) {
					
					contextoDetalle = new HashMap<String, Serializable>();
					
					if(prop.key == "fotoPasaporte_ref") {
						contextoDetalle.put("id", contexto[prop.key][0]?.id);
						contextoDetalle.put("processInstanceId", contexto[prop.key][0]?.processInstanceId);
						contextoDetalle.put("author", contexto[prop.key][0]?.author);
						contextoDetalle.put("creationDate", contexto[prop.key][0]?.creationDate);
						contextoDetalle.put("fileName", contexto[prop.key][0]?.fileName);
						contextoDetalle.put("contentMimeType", contexto[prop.key][0]?.contentMimeType);
						contextoDetalle.put("contentStorageId", contexto[prop.key][0]?.contentStorageId);
						contextoDetalle.put("url", contexto[prop.key][0]?.url);
						contextoDetalle.put("description", contexto[prop.key][0]?.description);
						contextoDetalle.put("version", contexto[prop.key][0]?.version);
						contextoDetalle.put("index", contexto[prop.key][0]?.index);
						contextoDetalle.put("contentFileName", contexto[prop.key][0]?.contentFileName);
						
						foto.add(contextoDetalle);
						contexto2.put(prop.key, foto)
					}else {
						
						contextoDetalle.put("name", contexto[prop.key]?.name);
						contextoDetalle.put("type", contexto[prop.key]?.type);
						try {
							contextoDetalle.put("storageId", contexto[prop.key]?.storageId);
							contextoDetalle.put("storageId_string", contexto[prop.key]?.storageIdAsString);
							contextoDetalle.put("link", "API/bdm/businessData/"+contexto[prop.key]?.type+"/"+ (contexto[prop.key]?.storageId != null ?contexto[prop.key]?.storageId: "" ));
						}catch(Exception ex) {
							contextoDetalle.put("storageIds", contexto[prop.key]?.storageIds);
							contextoDetalle.put("storageIds_string", contexto[prop.key]?.storageIdsAsString);
							contextoDetalle.put("link", "API/bdm/businessData/"+contexto[prop.key]?.type+"/findByIds?ids="+ (contexto[prop.key]?.storageIds?.join(",")));
							
						}
						
						contexto2.put(prop.key, contextoDetalle)
					
					}
					 
					 
				}
				
			}catch(ProcessInstanceNotFoundException ex) {\
				closeCon = validarConexionBonita();
				pstm = con.prepareStatement(" SELECT id FROM ARCH_PROCESS_INSTANCE WHERE sourceobjectid = ${caseid} ");
				rs = pstm.executeQuery();
				if(rs.next()) {
					contexto = apiClient.getProcessAPI().getArchivedProcessInstanceExecutionContext( Long.parseLong(rs.getString("id")));
				}
			}
			
			/**/
			rows.add(contexto2);	
			resultado.setSuccess(true);
			resultado.setData(rows)
			resultado.setError_info( errorLog)
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info( errorLog)
			e.printStackTrace();
		}
		finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	
	public Boolean validarConexionBonita() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno = true
		}
		return retorno
	}
	
}
