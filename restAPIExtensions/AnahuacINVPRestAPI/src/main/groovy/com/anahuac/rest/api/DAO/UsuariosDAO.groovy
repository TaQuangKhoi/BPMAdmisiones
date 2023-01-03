package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.identity.ContactDataCreator
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.UserCreator
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserUpdater
import org.bonitasoft.web.extension.rest.RestAPIContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.IdiomaExamen
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Usuarios
import com.anahuac.rest.api.Utilities.LoadParametros
import groovy.json.JsonSlurper

class UsuariosDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(UsuariosDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true
		}
		return retorno;
	}

	public Boolean validarConexion() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno = true
		}
		return retorno
	}
	
	public Result getDatosUsername(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		try {
		
				List<Map<String, Boolean>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexionBonita();
				pstm = con.prepareStatement(Statements.GET_USERS_BY_USERNAME)
				pstm.setString(1, username)
				
				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				while(rs.next()) {
					rows.add(true);
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
				LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result postRegistrarUsuario(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Result resultadoN = new Result();
		//List<Usuarios> lstResultado = new ArrayList<Usuarios>();
		List < String > lstResultado = new ArrayList < String > ();
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Long resultReq = 0;
		Long resultReqA = 0;
		Integer start = 0;
		Integer end = 99999;
		Long step = 0;
		Usuarios objUsuario = new Usuarios();
	
		Boolean success = false;
		String error_log = "";
		String success_log = "";
		Boolean closeCon = false;
		
		try {
			String username = "";
			String password = "";
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			error_log = error_log + " | "+username + password + " ";
			/*-------------------------------------------------------------*/

			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			error_log = error_log + " | ";
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			// Datos de la cuenta del Usuario
			UserCreator creator = new UserCreator(object.nombreusuario, object.password);
			creator.setFirstName(object.nombre).setLastName(object.apellido);
			ContactDataCreator proContactDataCreator = new ContactDataCreator().setEmail(object.nombreusuario);
			creator.setProfessionalContactData(proContactDataCreator);
			//inicializa la cuenta con la cual tendras permisos para registrar el usuario
			apiClient.login(username, password)
			error_log = error_log + " | "+apiClient.login(username, password);
			error_log = error_log + " | apiClient.login(username, password)";
			
			closeCon = validarConexion();
			
				try {
					con.setAutoCommit(false);
					pstm = con.prepareStatement(Statements.UPDATE_IDIOMA_REGISTRO_BY_USERNAME);
					pstm.setString(1, object.idioma);
					pstm.setString(2, object.nombreusuario);
					pstm.setBoolean(3, false);
					pstm.setBoolean(4, false);
			
					//resultReq = pstm.executeUpdate();
					rs = pstm.executeQuery();
					if(rs.next()) {
						resultReq = rs.getLong("persistenceid")
					}
					
					
					success = true;
					if(resultReq > 0) {
						error_log = resultReq + " Exito! query update_idioma_registro_by_username_1 insertado " + resultReq + " | " + object.idioma + object.nombreusuario
						//error_log = resultReq + " Exito! query update_idioma_registro_by_username_1"
					} else {
						error_log = resultReq + " Error! query update_idioma_registro_by_username_1"
					}
					
				} catch (Exception e) {
					con.rollback();
					/*if (success == false) {
						try {
							con.setAutoCommit(false);
							pstm = con.prepareStatement(Statements.UPDATE_TABLE_CATREGISTRO);
							resultReqA = pstm.executeUpdate();
							con.commit();

							if (resultReqA > 0) {
								con.setAutoCommit(false);
								pstm = con.prepareStatement(Statements.UPDATE_IDIOMA_REGISTRO_BY_USERNAME);
								pstm.setString(1, parameterIdioma);
								pstm.setString(2, object.nombreusuario);
			
								resultReq = pstm.executeUpdate();
								con.commit();
								if(resultReq > 0) {
									error_log = resultReq + " Exito! query update_idioma_registro_by_username_2"
								} else {
									error_log = resultReq + " Error! query update_idioma_registro_by_username_2"
								}
								
							} else {
								error_log = resultReqA + " Error! query update_table_registro"
							}
						} catch (Exception e1) {
							con.rollback();
							error_log = "Error catch_1: " + e + " error query1: " + resultReq + " error query2: " + resultReqA;
						}
					}*/

				}
				
			//Registro del usuario
			IdentityAPI identityAPI = apiClient.getIdentityAPI()
			final User user = identityAPI.createUser(creator);
			error_log = error_log + " | final User user = identityAPI.createUser(creator);";
	
			apiClient.login(user.getUserName(), object.password)
			final IdentityAPI identityAPI2 = apiClient.getIdentityAPI()
			error_log = error_log + " | final IdentityAPI identityAPI2 = apiClient.getIdentityAPI()";
			UserMembership membership = identityAPI2.addUserMembership(user.getId(), identityAPI2.getGroupByPath("/ASPIRANTE").getId(), identityAPI2.getRoleByName("ASPIRANTE").getId())
			error_log = error_log + " | UserMembership membership = identityAPI2.addUserMembership(user.getId(), identityAPI2.getGroupByPath(/ASPIRANTE).getId(), identityAPI2.getRoleByName(ASPIRANTE).getId())";
			UserUpdater update_user = new UserUpdater();
			update_user.setEnabled(true);
			error_log = error_log + " | UserUpdater update_user = new UserUpdater();";
			final User user_update = identityAPI.updateUser(user.getId(), update_user);
			error_log = error_log + " | final User user_update= identityAPI.updateUser(user.getId(), update_user);";
	
			def str = jsonSlurper.parseText('{"campus": "' + object.campus + '","correo":"' + object.nombreusuario + '", "codigo": "registrar","isEnviar":false}');
			error_log = error_log + " | def str = jsonSlurper.parseText";
			error_log = error_log + " | " + str;
			con.commit();
			resultado.setSuccess(true);
			resultado.setError_info(error_log)
		} catch (Exception e) {
			try {
				con.rollback();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setError_info(error_log)
			resultado.setData(lstResultado);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}finally{
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result getIdiomaUsuario(String username) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String error_log = ""; 
		IdiomaExamen idioma = new IdiomaExamen();
		List<IdiomaExamen> lstIdioma = new ArrayList<IdiomaExamen>();
		
		try {
		
			error_log = " USUARIO | " + username
				closeCon = validarConexion();
				pstm = con.prepareStatement(Statements.GET_IDIOMA_USUARIO)
				pstm.setString(1, username)
				
				rs = pstm.executeQuery()
				lstIdioma = new ArrayList<IdiomaExamen>();
				while(rs.next()) {
					idioma = new IdiomaExamen();
					error_log = " | EN EL WHILE " + rs.getString("idioma") + " | "
					idioma.setPersistenceId(rs.getLong("persistenceId"));
					error_log = " | EN EL WHILE " + rs.getString("persistenceId") + " | "
					idioma.setPersistenceVersion(rs.getString("persistenceVersion"));
					error_log = " | EN EL WHILE " + rs.getString("persistenceVersion") + " | "
					idioma.setIdioma(rs.getString("idioma"));
					error_log = " | EN EL WHILE " + rs.getString("idioma") + " | "
					idioma.setUsuario(rs.getString("username"));
					error_log = " | EN EL WHILE " + rs.getString("username") + " | "
					
					lstIdioma.add(idioma);
				}
				resultado.setSuccess(true)
				
				resultado.setData(lstIdioma)
				resultado.setError_info(error_log);
				
			} catch (Exception e) {
				LOGGER.error "[ERROR] " + e.getMessage();
				resultado.setError_info(error_log)
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
}
