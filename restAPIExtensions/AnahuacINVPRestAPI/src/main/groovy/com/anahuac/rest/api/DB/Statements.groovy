package com.anahuac.rest.api.DB

class Statements {
	
	public static final String GET_CAT_PREGUNTAS = "SELECT * FROM CATPREGUNTAS [WHERE] [ORDERBY] [LIMITOFFSET]"
	
	public static final String GET_CAT_PREGUNTAS_EXAMEN = "SELECT * FROM CATPREGUNTAS [WHERE]";
	
	public static final String GET_SESION_LOGIN = "SELECT  ses.persistenceid, tipo.descripcion, p.aplicacion, p.entrada, ap.username FROM SESIONES AS ses INNER JOIN pruebas AS p on ses.persistenceid = p.sesion_pid INNER JOIN aspirantespruebas AS ap ON ses.persistenceid = ap.sesiones_pid INNER JOIN cattipoprueba as tipo on  tipo.persistenceid = AP.cattipoprueba_pid WHERE p.aplicacion = TO_TIMESTAMP(?, 'YYYY-MM-DD') AND ap.username = ? AND tipo.descripcion = 'Examen Psicométrico' AND ses.campus_pid = ? AND p.nombre = 'EP'";
	
	public static final String GET_USERS_BY_USERNAME = "SELECT tenantid, id, enabled, username, password, firstname, lastname, title, jobtitle, manageruserid, createdby, creationdate, lastupdate, iconid FROM user_ WHERE LOWER(username) LIKE LOWER(CONCAT('%',?,'%'))";
	
	public static final String UPDATE_IDIOMA_REGISTRO_BY_USERNAME = "INSERT INTO idiomainvpusuario (persistenceId , persistenceVersion, idioma, username, havesesion, usuariobloqueado ) values (case when (SELECT max(persistenceId)+1 from idiomainvpusuario ) is null then 1 else (SELECT max(persistenceId)+1 from idiomainvpusuario) end,0,?, ?, ?, ?) RETURNING persistenceid;";
	
	public static final String UPDATE_TABLE_CATREGISTRO = "CREATE TABLE IdiomaINVPUsuario (persistenceId bigserial, persistenceVersion bigint DEFAULT 0, idioma CHARACTER VARYING(255), username CHARACTER VARYING(255), PRIMARY KEY (persistenceId) )";
	
	public static final String SELECT_PROPERTIES_BONITA = "SELECT A.tenantid, A.id, A.process_id, A.name, A.value FROM proc_parameter AS A INNER JOIN process_definition AS B ON B.processid = A.process_id AND B.activationstate='ENABLED' WHERE A.name IN ('usuario','password') ORDER BY B.version DESC Limit 2";
	
	public static final String INSERT_RESPUESTA_EXAMEN = "INSERT INTO respuestainvp (persistenceid,pregunta, respuesta,caseid,idusuario) VALUES ( case when (SELECT max(persistenceId)+1 from RespuestaINVP ) is null then 1 else (SELECT max(persistenceId)+1 from RespuestaINVP) end,?,?,?,?) ";
	
	public static final String GET_RESPUESTAS_BY_USUARIOCASO = "SELECT persistenceid, caseid, pregunta, respuesta, idusuario  FROM RESPUESTAINVP WHERE idusuario = ? AND caseid = ?;";
	
	public static final String GET_RESPUESTAS_BY_USUARIO = "SELECT persistenceid, caseid, pregunta, respuesta, idusuario  FROM RESPUESTAINVP WHERE idusuario = ?;";
	
	public static final String UPDATE_RESPUESTAS = "UPDATE respuestainvp SET respuesta = ?  WHERE pregunta = ? AND idusuario = ? AND caseid = ?";
	
	public static final String  GET_IDIOMA_USUARIO = "SELECT persistenceId , persistenceVersion, idioma, username FROM idiomainvpusuario WHERE username = ?";
	
	public static final String GET_CAT_CAMPUS_PID_BY_CORREO = "SELECT catcampus_pid FROM solicituddeadmision WHERE correoelectronico = ?";
	
	public static final String UPDATE_SESION_USUARIO = "UPDATE IdiomaINVPUsuario SET havesesion = ? WHERE username = ?";
	
	public static final String BLOQUEO_USUARIO = "UPDATE IdiomaINVPUsuario SET usuariobloqueado = ? WHERE username = ?";
	
	public static final String GET_SESION_USUARIO = "SELECT havesesion FROM IdiomaINVPUsuario WHERE username = ?";
}
