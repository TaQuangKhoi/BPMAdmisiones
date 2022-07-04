package com.anahuac.rest.api.DAO

import groovy.json.JsonSlurper
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.bonitasoft.web.extension.rest.RestAPIContext

import com.anahuac.rest.api.Entity.Result

import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor

class BitacoraSDAEDAO {
	
	public Result cargarBitacora(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		ProcessInstance processInstance = null;
		ProcessAPI processAPI = null;
		Map < String, Serializable > contracto = new HashMap < String, Serializable > ();
		Map < String, Serializable > objBitacoraSDAEInput = new HashMap < String, Serializable > ();
		List < Map < String, Serializable >> lstBitacoraSDAEInput = new ArrayList < Map < String, Serializable >> ();
		
		String info = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			SearchOptionsBuilder searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			List < ProcessDeploymentInfo > lstProcessDeploymentInfo = new ArrayList < ProcessDeploymentInfo > ();
			SearchOptions searchOptionsProccess = null;
			Long processId = null;
			
			searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Agregar BitacoraSDAE");
			searchOptionsProccess = searchBuilderProccess.done();
			SearchResult < ProcessDeploymentInfo > SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
			lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
			for (ProcessDeploymentInfo objProcessDeploymentInfo: lstProcessDeploymentInfo) {
				if (objProcessDeploymentInfo.getActivationState().toString().equals("ENABLED")) {
					processId = objProcessDeploymentInfo.getProcessId();
					info+="entro";
				}
			}
			
			 
			objBitacoraSDAEInput.put("idBanner", object?.idBanner)
			objBitacoraSDAEInput.put("estatus", object?.estatus);
			objBitacoraSDAEInput.put("correo", object?.correo)
			objBitacoraSDAEInput.put("usuario", context.getApiSession().getUserName())
			objBitacoraSDAEInput.put("comentario", object?.comentario)
			objBitacoraSDAEInput.put("beca", object?.beca)
			objBitacoraSDAEInput.put("financiamiento", object?.financiamiento);
			lstBitacoraSDAEInput.add(objBitacoraSDAEInput);
			contracto.put("lstBitacoraSDAEInput", lstBitacoraSDAEInput);
			
			processAPI = context.getApiClient().getProcessAPI();
			
			processInstance = processAPI.startProcessWithInputs(processId, contracto);
			resultado.setSuccess(true);
		}catch(Exception e) {
			resultado.setError(e.getMessage())
			resultado.setSuccess(false);
			resultado.setError_info(info)
		}
		
		return resultado;
	}
	
}
