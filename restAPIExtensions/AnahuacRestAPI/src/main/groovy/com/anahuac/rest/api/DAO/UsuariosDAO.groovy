package com.anahuac.rest.api.DAO

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.process.ProcessDefinition
import org.bonitasoft.engine.identity.ContactDataCreator
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserCreator
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.profile.Profile
import org.bonitasoft.engine.profile.ProfileMemberCreator
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Usuarios
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class UsuariosDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ListadoDAO.class);
	
	public Result getTest(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		List<String> lstResultado = new ArrayList<String>();
		
		lstResultado.add("Informacion de prueba");
		resultado.setData(lstResultado);
		resultado.setSuccess(true);
		
		return resultado;
	}
	
	public Result postRegistrarUsuario(Integer parameterP,Integer parameterC, String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		//List<Usuarios> lstResultado = new ArrayList<Usuarios>();
		List<String> lstResultado = new ArrayList<String>();
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		
		Integer start = 0;
		Integer end = 99999;
		
		Usuarios objUsuario= new Usuarios();
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()
			
			
			// Datos de la cuenta del Usuario
			UserCreator creator = new UserCreator(object.nombreusaurio,object.password);
			creator.setFirstName(object.nombre).setLastName(object.apellido);
			ContactDataCreator proContactDataCreator = new ContactDataCreator().setEmail(object.nombreusaurio);
		creator.setProfessionalContactData(proContactDataCreator)
			//inicializa la cuenta con la cual tendras permisos para registrar el usuario
			apiClient.login("acuna.karol@correo.com", "bpm")
			//Registro del usuario
			IdentityAPI identityAPI = apiClient.getIdentityAPI()
			final User user = identityAPI.createUser(creator);
			
			lstResultado.add(object);
			
			apiClient.login(user.getUserName(), object.password)
			final IdentityAPI identityAPI2 = apiClient.getIdentityAPI()
			UserMembership membership = identityAPI2.addUserMembership(user.getId(), 23, 2)
			
			
			resultado.setData(lstResultado);
			resultado.setSuccess(true);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error "ERROR=================================";
			LOGGER.error e.getMessage();
			e.printStackTrace();
		}
		return resultado;
	}
	
}
