package com.anahuac.rest.api.DB

class Statements {
	
	public static final String GET_CAT_PREGUNTAS = "SELECT * FROM CATPREGUNTAS [WHERE] [ORDERBY] [LIMITOFFSET]"
	
	public static final String GET_CAT_PREGUNTAS_EXAMEN = "SELECT * FROM CATPREGUNTAS [WHERE]";
	
	public static final String GET_SESION_LOGIN = "SELECT DISTINCT ses.persistenceid as idsesion, ses.nombre as nombresesion, tipo.descripcion,p.nombre as nombre_prueba, p.aplicacion, p.entrada, p.salida, ap.username FROM aspirantespruebas AS ap INNER JOIN SESIONES AS ses ON ses.persistenceid = ap.sesiones_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS tipo ON tipo.descripcion = 'Examen Psicométrico'AND tipo.persistenceid = p.cattipoprueba_pid WHERE p.aplicacion = TO_TIMESTAMP(?, 'YYYY-MM-DD') AND ap.username = ?";
	
	public static final String GET_SESION_LOGIN_HORA = "SELECT DISTINCT ses.persistenceid as idsesion, ses.nombre as nombresesion, tipo.descripcion,p.nombre as nombre_prueba,p.persistenceid as id_prueba, p.aplicacion, p.entrada, p.salida, ap.username FROM aspirantespruebas AS ap INNER JOIN SESIONES AS ses ON ses.persistenceid = ap.sesiones_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS tipo ON tipo.descripcion = 'Examen Psicométrico' AND tipo.persistenceid = p.cattipoprueba_pid WHERE p.aplicacion = TO_TIMESTAMP(?, 'YYYY-MM-DD') AND ap.username = ? AND TO_TIMESTAMP(TO_CHAR(CURRENT_TIMESTAMP, 'HH24:MI'), 'HH24:MI') BETWEEN TO_TIMESTAMP(p.entrada, 'HH24:MI') AND TO_TIMESTAMP(?, 'HH24:MI')";
	
	public static final String GET_USERS_BY_USERNAME = "SELECT tenantid, id, enabled, username, password, firstname, lastname, title, jobtitle, manageruserid, createdby, creationdate, lastupdate, iconid FROM user_ WHERE LOWER(username) LIKE LOWER(CONCAT('%',?,'%'))";
	
	public static final String UPDATE_IDIOMA_REGISTRO_BY_USERNAME = "INSERT INTO idiomainvpusuario (persistenceId , persistenceVersion, idioma, username, havesesion, usuariobloqueado, nombresesion ) values (case when (SELECT max(persistenceId)+1 from idiomainvpusuario ) is null then 1 else (SELECT max(persistenceId)+1 from idiomainvpusuario) end,0,?, ?, ?, ?, ?) RETURNING persistenceid;";
	
	public static final String UPDATE_TABLE_CATREGISTRO = "CREATE TABLE IdiomaINVPUsuario (persistenceId bigserial, persistenceVersion bigint DEFAULT 0, idioma CHARACTER VARYING(255), username CHARACTER VARYING(255), PRIMARY KEY (persistenceId) )";
	
	public static final String SELECT_PROPERTIES_BONITA = "SELECT A.tenantid, A.id, A.process_id, A.name, A.value FROM proc_parameter AS A INNER JOIN process_definition AS B ON B.processid = A.process_id AND B.activationstate='ENABLED' WHERE A.name IN ('usuario','password') ORDER BY B.version DESC Limit 2";
	
	public static final String INSERT_RESPUESTA_EXAMEN = "INSERT INTO respuestainvp (persistenceid,pregunta, respuesta,caseid,idusuario,username) VALUES ( case when (SELECT max(persistenceId)+1 from RespuestaINVP ) is null then 1 else (SELECT max(persistenceId)+1 from RespuestaINVP) end,?,?,?,?,?) ";
	
	public static final String GET_RESPUESTAS_BY_USUARIOCASO = "SELECT persistenceid, caseid, pregunta, respuesta, idusuario  FROM RESPUESTAINVP WHERE idusuario = ? AND caseid = ?;";
	
	public static final String GET_RESPUESTAS_BY_USUARIO = "SELECT persistenceid, caseid, pregunta, respuesta, idusuario  FROM RESPUESTAINVP WHERE idusuario = ?;";
	
	public static final String UPDATE_RESPUESTAS = "UPDATE respuestainvp SET respuesta = ?  WHERE pregunta = ? AND idusuario = ? AND caseid = ?";
	
	public static final String  GET_IDIOMA_USUARIO = "SELECT persistenceId , persistenceVersion, idioma, username, havesesion, usuariobloqueado, nombresesion FROM idiomainvpusuario WHERE username = ?";
	
	public static final String GET_CAT_CAMPUS_PID_BY_CORREO = "SELECT catcampus_pid FROM solicituddeadmision WHERE correoelectronico = ?";
	
	public static final String UPDATE_SESION_USUARIO = "UPDATE idiomainvpusuario SET havesesion = ? WHERE username = ?";
	
	public static final String BLOQUEO_USUARIO = "UPDATE idiomainvpusuario SET usuariobloqueado = ? WHERE username = ?";
	
	public static final String GET_SESION_USUARIO = "SELECT havesesion FROM idiomainvpusuario WHERE username = ?";
	
	public static final String INSERT_TERMINADO_EXAMEN = "INSERT INTO invpexamenterminado (persistenceId, persistenceVersion, username, terminado) VALUES ( case when (SELECT max(persistenceId)+1 from invpexamenterminado ) is null then 1 else (SELECT max(persistenceId)+1 from invpexamenterminado) end, 0, ?, ?);";
	
	public static final String UPDATE_TERMINADO_EXAMEN = "UPDATE invpexamenterminado SET terminado = ? WHERE username = ?";
	
	public static final String GET_TERMINADO_EXAMEN = "SELECT terminado FROM invpexamenterminado WHERE username = ?";
	
	public static final String GET_COUNT_PREGUNTASCONTESTADAS_BY_USERNAME = "SELECT COUNT(*) AS totalpreguntas FROM respuestainvp WHERE username = ?;";
}
