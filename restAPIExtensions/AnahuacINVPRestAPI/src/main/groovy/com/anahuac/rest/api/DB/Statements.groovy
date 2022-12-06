package com.anahuac.rest.api.DB

class Statements {
	
	public static final String GET_CAT_PREGUNTAS = "SELECT * FROM CATPREGUNTAS [WHERE] [ORDERBY] [LIMITOFFSET]"
	
	public static final String GET_SESION_LOGIN = "SELECT  ses.persistenceid, tipo.descripcion, p.aplicacion, p.entrada, ap.username FROM SESIONES AS ses INNER JOIN pruebas AS p on ses.persistenceid = p.sesion_pid INNER JOIN aspirantespruebas AS ap ON ses.persistenceid = ap.sesiones_pid INNER JOIN cattipoprueba as tipo on  tipo.persistenceid = AP.cattipoprueba_pid WHERE p.aplicacion = ? AND ap.username = ? AND tipo.descripcion = 'Examen Psicométrico' AND ses.campus_pid = ? AND p.nombre = 'EP'";
	
	public static final String GET_USERS_BY_USERNAME = "SELECT tenantid, id, enabled, username, password, firstname, lastname, title, jobtitle, manageruserid, createdby, creationdate, lastupdate, iconid FROM user_ WHERE LOWER(username) LIKE LOWER(CONCAT('%',?,'%'))";
	
	public static final String UPDATE_IDIOMA_REGISTRO_BY_USERNAME = "INSERT INTO idioma (persistenceId , persistenceVersion   , idioma, usuario ) values (case when (SELECT max(persistenceId)+1 from idioma ) is null then 1 else (SELECT max(persistenceId)+1 from idioma) end,0,?, ?);";
	
	public static final String UPDATE_TABLE_CATREGISTRO = "CREATE TABLE idioma (persistenceId bigserial, persistenceVersion bigint DEFAULT 0, idioma CHARACTER VARYING(255), usuario CHARACTER VARYING(255), PRIMARY KEY (persistenceId) )";
}
