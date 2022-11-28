package com.anahuac.rest.api.DB

class Statements {
	
	public static final String GET_CAT_PREGUNTAS = "SELECT * FROM CATPREGUNTAS [WHERE] [ORDERBY] [LIMITOFFSET]"
	
	public static final String GET_SESION_LOGIN = "SELECT  ses.persistenceid, tipo.descripcion, p.aplicacion, p.entrada, ap.username FROM SESIONES AS ses INNER JOIN pruebas AS p on ses.persistenceid = p.sesion_pid INNER JOIN aspirantespruebas AS ap ON ses.persistenceid = ap.sesiones_pid INNER JOIN cattipoprueba as tipo on  tipo.persistenceid = AP.cattipoprueba_pid WHERE p.aplicacion = ? AND ap.username = ? AND tipo.descripcion = 'Examen Psicométrico' AND ses.campus_pid = ? AND p.nombre = 'EP'";
}
