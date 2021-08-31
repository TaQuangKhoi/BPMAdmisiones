package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement

import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor
import org.bonitasoft.engine.bpm.flownode.TimerEventTriggerInstance
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.search.Order
import org.bonitasoft.engine.search.SearchResult

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper

class AvanzeProcesoDAO {
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
	
	public Result postAsistenciaProceso(String jsonData, RestAPIContext context) {
		Result resultado = new Result()
		Boolean closeCon = false;
		try {
			//List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			ProcessAPI processAPI = context.getApiClient().getProcessAPI()
			
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
			searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID, object.caseid);
			searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);
			final SearchOptions searchOptions = searchBuilder.done();
			SearchResult < HumanTaskInstance > SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
			List < HumanTaskInstance > lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
			Boolean isSeleccionCita = false,generoCredencial = false;;
			for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {
				if (objHumanTaskInstance.getName().equals("Seleccionar cita")) {
					isSeleccionCita = true;
					processAPI.assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
					processAPI.executeUserTask(objHumanTaskInstance.getId());
				}
				if (objHumanTaskInstance.getName().equals("Generar credencial")) {
					generoCredencial = true;
					processAPI.assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
					processAPI.executeUserTask(objHumanTaskInstance.getId());
				}
			}
			
			// revisa si  avanzo silo lo de seleccionar cita
			if(isSeleccionCita && !generoCredencial) {
				int count = 0;
				while((count < 50 && !generoCredencial)) {
					count++;
					SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
					lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
					for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {
						if (objHumanTaskInstance.getName().equals("Generar credencial")) {
							generoCredencial = true;
							processAPI.assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
							processAPI.executeUserTask(objHumanTaskInstance.getId());
						}
					}
				}
				
			}
			
			
			// Se pasan las variables del proceso a true
			Map<String, Serializable> rows = new HashMap<String, Serializable>();
			
			rows.put("asistenciaCollegeBoard", true);
			rows.put("asistenciaPsicometrico", true);
			rows.put("asistenciaEntrevista", true);
			
			processAPI.updateProcessDataInstances(object.caseid, rows);
			
			
			//vencer el timer
			SearchResult < TimerEventTriggerInstance > SearchTimmerTaskSearch = context.getApiClient().getProcessAPI().searchTimerEventTriggerInstances(searchOptions)
			List < TimerEventTriggerInstance > lstTimerTaskSearch = SearchHumanTaskInstanceSearch.getResult();
			for (TimerEventTriggerInstance objTimerInstance: lstTimerTaskSearch) {
				if (objTimerInstance.getEventInstanceName().equals("Timmer pase de lista") ) {
					processAPI.updateExecutionDateOfTimerEventTriggerInstance(objTimerInstance.getEventInstanceId(), 60)
				}
			}
			// revisa en que status esta marcado el aspirnate
			/*String status ="";
			pstm = con.prepareStatement("Select estatussolicitud from solicituddeadmision where caseid = ?");
			pstm.setLong(1, Integer.parseInt(object.caseid));
			rs = pstm.executeQuery()
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				status = rs.getString("estatussolicitud")
			}*/
			
			
			resultado.setSuccess(true)
		}catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError("500 Internal Server Error")
			resultado.setError_info(e.getMessage())
		} finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
	
		return resultado
	}
	
	
}
