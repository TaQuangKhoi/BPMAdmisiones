package com.anahuac.rest.api.DAO

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatDefectos
import com.anahuac.catalogos.CatDefectosDAOImpl
import com.anahuac.rest.api.Entity.Result

class TestDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestDAO.class)
	
	public Result testFuction(Integer parameterP, Integer parameterC, String jsonData) {
		Result resultado = new Result();
		List<String> lstResultado = new ArrayList<String>();
		try {
			for(Integer i=0; i< parameterC; i++) {
				lstResultado.add("objeto "+i);
			}
			
			CatDefectos catDefectos = new CatDefectos();
			
			
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado
	}
}
