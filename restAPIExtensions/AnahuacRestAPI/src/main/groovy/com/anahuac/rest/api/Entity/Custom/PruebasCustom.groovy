package com.anahuac.rest.api.Entity.Custom

class PruebasCustom {
	public static final String GET_PRUEBAS_FECHAS="SELECT persistenceid,aplicacion from pruebas where sesion_pid=? and cattipoprueba_pid=1 and iseliminado=false order by aplicacion asc ";
	public static final String GET_PRUEBAS_FECHAS_COMBINADOS="SELECT p.persistenceid, p.aplicacion FROM pruebas p INNER JOIN responsabledisponible r on r.prueba_pid=p.persistenceid AND (r.licenciaturas =''OR  r.licenciaturas IS NULL OR  r.licenciaturas LIKE '%' || (SELECT g.nombre FROM solicituddeadmision sda LEFT JOIN CATGESTIONESCOLAR g ON g.persistenceid=sda.CATGESTIONESCOLAR_PID WHERE sda.correoelectronico=? limit 1) ||'%') and r.disponible=true or r.ocupado!=true WHERE p.sesion_pid=? AND p.cattipoprueba_pid=1 AND p.iseliminado=false AND p.cupo>p.registrados group by p.persistenceid, p.aplicacion ORDER BY p.aplicacion ASC"
	public static final String GET_HORARIOS="SELECT p.*, r.horario, r.RESPONSABLEID , r.DISPONIBLE, c.descripcion   AS tipo, r.persistenceId AS rid, r.ocupado, r.licenciaturas, disponibles.disponibles FROM PRUEBAS p LEFT JOIN RESPONSABLEDISPONIBLE r ON r.PRUEBA_PID =p.PERSISTENCEID AND r.iseliminado=false LEFT JOIN cattipoprueba c ON c.PERSISTENCEID =p.cattipoprueba_pid LEFT JOIN (SELECT p.persistenceid  AS prueba_pid, COUNT(r.ocupado) AS disponibles FROM PRUEBAS p LEFT JOIN RESPONSABLEDISPONIBLE r ON r.PRUEBA_PID =p.PERSISTENCEID AND r.iseliminado=false WHERE p.SESION_PID =? AND p.persistenceId=? AND p.iseliminado=false AND p.cattipoprueba_pid=1 AND r.disponible=true AND ocupado=false AND  (r.licenciaturas ='' or r.licenciaturas is null or r.licenciaturas like '%' || (SELECT g.descripcion from solicituddeadmision sda LEFT JOIN CATGESTIONESCOLAR g ON g.persistenceid=sda.CATGESTIONESCOLAR_PID where sda.correoelectronico=? limit 1) ||'%') GROUP BY p.persistenceid ORDER BY p.registrados ASC) disponibles ON disponibles.prueba_pid=p.persistenceid WHERE p.SESION_PID =? AND  (r.licenciaturas ='' or r.licenciaturas is null or r.licenciaturas like '%' || (SELECT g.descripcion from solicituddeadmision sda LEFT JOIN CATGESTIONESCOLAR g ON g.persistenceid=sda.CATGESTIONESCOLAR_PID where sda.correoelectronico=? limit 1) ||'%') AND p.iseliminado=false AND p.cattipoprueba_pid=1 AND r.disponible=true AND ocupado=false and p.persistenceid=? ORDER BY p.registrados ASC, disponibles.disponibles DESC"
	public static final String GET_HORARIOS_COMBINADOS="SELECT MAX(r.responsableid) responsableid, p.PERSISTENCEID , p.PERSISTENCEVERSION, p.NOMBRE , p.APLICACION , p.ENTRADA , p.SALIDA , p.REGISTRADOS , p.ULTIMO_DIA_INSCRIPCION , p.CUPO , p.LUGAR , p.CAMPUS_PID , p.CALLE , p.NUMERO_INT , p.NUMERO_EXT , p.COLONIA , p.CODIGO_POSTAL, p.MUNICIPIO , p.PAIS_PID , p.ESTADO_PID , p.ISELIMINADO , p.SESION_PID , p.DURACION , p.DESCRIPCION , p.CATTIPOPRUEBA_PID , p.ONLINE, r.horario, r.disponible, c.descripcion AS tipo FROM PRUEBAS p LEFT JOIN RESPONSABLEDISPONIBLE r ON r.PRUEBA_PID =p.PERSISTENCEID LEFT JOIN cattipoprueba c ON c.PERSISTENCEID =p.cattipoprueba_pid WHERE p.sesion_pid=? AND p.cattipoprueba_pid=1 AND r.disponible=true AND aplicacion= (SELECT ptemp.aplicacion FROM PRUEBAS ptemp WHERE ptemp.persistenceid=? AND ptemp.iseliminado=false limit 1) AND (r.licenciaturas =''OR  r.licenciaturas IS NULL OR  (SELECT g.nombre FROM solicituddeadmision sda LEFT JOIN CATGESTIONESCOLAR g ON g.persistenceid=sda.CATGESTIONESCOLAR_PID WHERE sda.correoelectronico=? limit 1) IN (SELECT unnest(string_to_array(ere.licenciaturas, ',')) as licenciatura from responsabledisponible ere where persistenceid=r.persistenceid)) AND ocupado=false AND p.iseliminado=false GROUP BY p.PERSISTENCEID , p.PERSISTENCEVERSION, p.NOMBRE , p.APLICACION , p.ENTRADA , p.SALIDA , p.REGISTRADOS , p.ULTIMO_DIA_INSCRIPCION , p.CUPO , p.LUGAR , p.CAMPUS_PID , p.CALLE , p.NUMERO_INT , p.NUMERO_EXT , p.COLONIA , p.CODIGO_POSTAL, p.MUNICIPIO , p.PAIS_PID , p.ESTADO_PID , p.ISELIMINADO , p.SESION_PID , p.DURACION , p.DESCRIPCION , p.CATTIPOPRUEBA_PID , p.ONLINE, r.horario, r.disponible, c.descripcion ORDER BY r.horario"
	private SesionCustom sesion;
	private PruebaCustom prueba;
	
	public SesionCustom getSesion() {
		return sesion;
	}
	public void setSesion(SesionCustom sesion) {
		this.sesion = sesion;
	}
	public PruebaCustom getPrueba() {
		return prueba;
	}
	public void setPrueba(PruebaCustom prueba) {
		this.prueba = prueba;
	}
	
	
}
