package com.anahuac.rest.api.DB

class Statements {
	
	public static final String GET_CAT_PREGUNTAS = "SELECT * FROM CATPREGUNTAS [WHERE] [ORDERBY] [LIMITOFFSET]"
	
	public static final String GET_CAT_PREGUNTAS_EXAMEN = "SELECT * FROM CATPREGUNTAS [WHERE]";
	
	public static final String GET_SESION_LOGIN = "SELECT DISTINCT ses.persistenceid as idsesion, ses.nombre as nombresesion, tipo.descripcion,p.nombre as nombre_prueba,p.persistenceid as id_prueba, p.aplicacion, p.entrada, p.salida, ap.username FROM aspirantespruebas AS ap INNER JOIN SESIONES AS ses ON ses.persistenceid = ap.sesiones_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS tipo ON tipo.descripcion = 'Examen Psicométrico'AND tipo.persistenceid = p.cattipoprueba_pid WHERE p.aplicacion = TO_TIMESTAMP(?, 'YYYY-MM-DD') AND ap.username = ?";
	
	public static final String GET_SESION_LOGIN_HORA = "SELECT DISTINCT ses.persistenceid as idsesion, ses.nombre as nombresesion, tipo.descripcion,p.nombre as nombre_prueba,p.persistenceid as id_prueba, p.aplicacion, p.entrada, p.salida, ap.username FROM aspirantespruebas AS ap INNER JOIN SESIONES AS ses ON ses.persistenceid = ap.sesiones_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS tipo ON tipo.descripcion = 'Examen Psicométrico' AND tipo.persistenceid = p.cattipoprueba_pid WHERE p.aplicacion = TO_TIMESTAMP(?, 'YYYY-MM-DD') AND ap.username = ? AND TO_TIMESTAMP(TO_CHAR(CURRENT_TIMESTAMP, 'HH24:MI'), 'HH24:MI') BETWEEN TO_TIMESTAMP(p.entrada, 'HH24:MI') AND TO_TIMESTAMP(?, 'HH24:MI')";
	
	public static final String GET_DATOS_SESION_LOGIN = "SELECT DISTINCT ses.persistenceid as idsesion, ses.nombre as nombresesion, tipo.descripcion,p.nombre as nombre_prueba,p.persistenceid as id_prueba, p.aplicacion, p.entrada, p.salida, ap.username FROM aspirantespruebas AS ap INNER JOIN SESIONES AS ses ON ses.persistenceid = ap.sesiones_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS tipo ON tipo.descripcion = 'Examen Psicométrico' AND tipo.persistenceid = p.cattipoprueba_pid WHERE ap.username = ?";
	
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
	
	public static final String INSERT_TERMINADO_EXAMEN = "INSERT INTO invpexamenterminado (persistenceId, persistenceVersion, username, terminado, fechainicio, fechafin) VALUES ( case when (SELECT max(persistenceId)+1 from invpexamenterminado ) is null then 1 else (SELECT max(persistenceId)+1 from invpexamenterminado) end, 0, ?, ?, NOW(), NULL);";
	
	public static final String UPDATE_TERMINADO_EXAMEN = "UPDATE invpexamenterminado SET terminado = ?, fechafin = NOW() WHERE username = ?";
	
	public static final String GET_TERMINADO_EXAMEN = "SELECT terminado FROM invpexamenterminado WHERE username = ?";
	
	public static final String GET_COUNT_PREGUNTASCONTESTADAS_BY_USERNAME = "SELECT COUNT(*) AS totalpreguntas FROM respuestainvp WHERE username = ?;";
	
	public static final String INSERT_BLOQUEO_USUARIO = "INSERT INTO idiomainvpusuario (persistenceId , persistenceVersion, havesesion, username ) values (case when (SELECT max(persistenceId)+1 from idiomainvpusuario ) is null then 1 else (SELECT max(persistenceId)+1 from idiomainvpusuario) end,0,?,?) RETURNING persistenceid;";
	
	public static final String GET_ID_BANNER_BY_CORREO = "SELECT ds.idbanner, sda.telefono, sda.telefonocelular from solicituddeadmision sda INNER JOIN detallesolicitud ds on ds.caseid::INTEGER = sda.caseid WHERE sda.correoelectronico = ? ;";
	
	public static final String GET_ID_USER_BY_USERNAME = "SELECT id FROM public.user_ where username = ?";
	
	public static final String GET_CELULAR_BY_USERNAME = "SELECT telefonocelular from solicituddeadmision WHERE correoelectronico = ?";
	
	public static final String GET_SESONES_TODAS = "SELECT DISTINCT(ses.persistenceid) AS id_sesion, ses.descripcion AS descripcion_sesion, ses.nombre AS nombre_sesion, ses.fecha_inicio AS fecha_inicio_sesion, cca.descripcion AS campus, cca.grupobonita, ctp.descripcion AS tipo_prueba,  p.aplicacion AS fecha_prueba, p.entrada AS entrada_prueba, p.salida AS salida_prueba, p.cupo AS cupo_prueba, p.registrados AS registrados_prueba,  p.lugar AS lugar_prueba  FROM SESIONES AS ses  INNER JOIN CATCAMPUS AS cca ON cca.persistenceid = ses.campus_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS ctp ON ctp.descripcion = 'Examen Psicométrico' AND ctp.persistenceid = p.cattipoprueba_pid  [WHERE] [ORDERBY] LIMIT ? OFFSET ?;";
	
	public static final String GET_COUNT_SESONES_TODAS = "SELECT COUNT(*) AS total_registros FROM (SELECT DISTINCT(ses.persistenceid)  FROM SESIONES AS ses  INNER JOIN CATCAMPUS AS cca ON cca.persistenceid = ses.campus_pid INNER JOIN pruebas AS p ON ses.persistenceid = p.sesion_pid INNER JOIN cattipoprueba AS ctp ON ctp.descripcion = 'Examen Psicométrico' AND ctp.persistenceid = p.cattipoprueba_pid [WHERE]) AS query";
	
	public static final String GET_ASPIRANTES_SESIONES = "SELECT DISTINCT(creg.caseid), creg.caseid AS idbpm, creg.primernombre, creg.segundonombre, creg.apellidopaterno, creg.apellidomaterno, creg.correoelectronico, ccam.descripcion  AS campus, sdad.telefono, sdad.telefonocelular, (SELECT COUNT(persistenceid) AS total_preguntas FROM CatPreguntas WHERE iseliminado = false) AS total_preguntas, (SELECT COUNT(persistenceid) AS total_respuestas FROM RespuestaINVP WHERE username = creg.correoelectronico) AS total_respuestas, prue.nombre AS nombre_prueba, ctpr.descripcion AS tipo_prueba, prue.descripcion AS descripcion_prueba, extr.fechainicio, extr.fechafin, extr.terminado, dets.idbanner,  CASE WHEN (extr.fechainicio::timestamp > now() OR extr.fechainicio is null ) THEN 'Prueba aun no iniciada' WHEN extr.fechainicio::timestamp <= now() AND extr.fechafin::timestamp >= now() THEN 'Prueba en proceso'  WHEN extr.fechafin::timestamp <= now() THEN 'Prueba finalizada' END  estatus, idio.idioma, idio.usuariobloqueado  FROM CatRegistro  AS creg LEFT JOIN AspirantesPruebas AS aspr ON aspr.caseid = creg.caseid LEFT JOIN Pruebas AS prue ON prue.persistenceid = aspr.prueba_pid LEFT JOIN CatTipoPrueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid INNER JOIN SolicitudDeAdmision  AS sdad ON sdad.caseid = creg.caseid LEFT JOIN INVPExamenTerminado AS extr ON extr.username = creg.correoelectronico INNER JOIN CatCampus AS ccam ON ccam.persistenceid = sdad.catcampusestudio_pid  LEFT JOIN DetalleSolicitud AS dets ON dets.caseid::BIGINT = sdad.caseid LEFT JOIN IdiomaINVPUsuario AS idio ON idio.username = sdad.correoelectronico     [WHERE]  [ORDERBY]   LIMIT ? OFFSET ?";
	
	public static final String GET_ASPIRANTES_SESIONES_COUNT = "SELECT COUNT(creg.caseid) AS total_registros FROM CatRegistro  AS creg LEFT JOIN AspirantesPruebas AS aspr ON aspr.caseid = creg.caseid LEFT JOIN Pruebas AS prue ON prue.persistenceid = aspr.prueba_pid LEFT JOIN CatTipoPrueba AS ctpr ON ctpr.persistenceid = prue.cattipoprueba_pid LEFT JOIN SolicitudDeAdmision  AS sdad ON sdad.caseid = creg.caseid LEFT JOIN INVPExamenTerminado AS extr ON extr.username = creg.correoelectronico LEFT JOIN CatCampus AS ccam ON ccam.persistenceid = sdad.catcampusestudio_pid   LEFT JOIN DetalleSolicitud AS dets ON dets.caseid::BIGINT = sdad.caseid   [WHERE] ";
}
