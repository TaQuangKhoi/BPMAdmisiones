package com.anahuac.rest.api.DAO

import org.bonitasoft.web.extension.rest.RestAPIContext

import com.anahuac.catalogos.CatConfiguracion
import com.anahuac.catalogos.CatConfiguracionDAO
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.model.SolicitudDeAdmisionDAO
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utils.FileManagement

import groovy.json.JsonSlurper

class DocumentDAO {
	public Result getDocs(String jsonData,RestAPIContext context) {
		Map<String, Serializable> datos = new HashMap<String, Serializable>();
		List < Map < String, Serializable >> rows = new ArrayList < Map < String, Serializable >> ();
		Result result = new Result();
		String errorLog="";
		try {
			def catConfiguracion = context.apiClient.getDAO(CatConfiguracionDAO.class)
			errorLog+="CatConfiguracionDAO"
			def cc = catConfiguracion.findByClave("SASAzure", 0, 1);
			def fm = new FileManagement()
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def num = Math.random();
			for(def i =0; i<object.items.required.size; i++) {
				datos = new HashMap<String, Serializable>();
				def detalleSolicitud = context.apiClient.getDAO(DetalleSolicitudDAO.class);
				errorLog+="DetalleSolicitudDAO "+ object.items.required[i]
				def ds = detalleSolicitud.findByIdBanner(object.items.required[i], 0, 1)
				if(ds.empty) {
					throw new Exception("No existe aspirante "+ object.items.required[i])
				}
				def solicitudDeAdmision = context.apiClient.getDAO(SolicitudDeAdmisionDAO.class);
				errorLog+="SolicitudDeAdmisionDAO"
				def sda = solicitudDeAdmision.findByCaseId(Long.parseLong(ds[0].caseId), 0, 0);
				datos.put("idBanner", ds[0].idBanner);
				datos.put("urlactanacimiento", sda[0].urlActaNacimiento);
				datos.put("urlconstancia", sda[0].urlConstancia);
				datos.put("urlfoto", sda[0].urlFoto);
				datos.put("actaNacimiento",fm.b64Url(sda[0].urlActaNacimiento + cc[0].valor + "&v="+num))
				datos.put("constancia",fm.b64Url(sda[0].urlConstancia + cc[0].valor + "&v="+num))
				datos.put("foto",fm.b64Url(sda[0].urlFoto + cc[0].valor + "&v="+num))
				rows.add(datos)
			}
			result.setSuccess(true);
			result.setData(rows)
		}catch(Exception e) {
			result.setSuccess(false)
			result.setError(e.getMessage())
			result.setError_info(errorLog)
		}
		
		
		
		return result;
	}
}
