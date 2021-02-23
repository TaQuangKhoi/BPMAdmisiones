package com.anahuac.rest.api.Entity.Custom

class PruebasCustom {
	public static final String GET_PRUEBAS_FECHAS="SELECT persistenceid,aplicacion from pruebas where sesion_pid=? and cattipoprueba_pid=1 and iseliminado=false order by aplicacion asc ";
	public static final String GET_HORARIOS="SELECT p.*, r.horario, r.RESPONSABLEID , r.DISPONIBLE, c.descripcion   AS tipo, r.persistenceId AS rid, r.ocupado, r.licenciaturas, disponibles.disponibles FROM PRUEBAS p LEFT JOIN RESPONSABLEDISPONIBLE r ON r.PRUEBA_PID =p.PERSISTENCEID AND r.iseliminado=false LEFT JOIN cattipoprueba c ON c.PERSISTENCEID =p.cattipoprueba_pid LEFT JOIN (SELECT p.persistenceid  AS prueba_pid, COUNT(r.ocupado) AS disponibles FROM PRUEBAS p LEFT JOIN RESPONSABLEDISPONIBLE r ON r.PRUEBA_PID =p.PERSISTENCEID AND r.iseliminado=false WHERE p.SESION_PID =? AND p.persistenceId=? AND p.iseliminado=false AND p.cattipoprueba_pid=1 AND r.disponible=true AND ocupado=false AND  (r.licenciaturas ='' or r.licenciaturas is null or r.licenciaturas like '%' || (SELECT g.descripcion from solicituddeadmision sda LEFT JOIN CATGESTIONESCOLAR g ON g.persistenceid=sda.CATGESTIONESCOLAR_PID where sda.correoelectronico=? limit 1) ||'%') GROUP BY p.persistenceid ORDER BY p.registrados ASC) disponibles ON disponibles.prueba_pid=p.persistenceid WHERE p.SESION_PID =? AND  (r.licenciaturas ='' or r.licenciaturas is null or r.licenciaturas like '%' || (SELECT g.descripcion from solicituddeadmision sda LEFT JOIN CATGESTIONESCOLAR g ON g.persistenceid=sda.CATGESTIONESCOLAR_PID where sda.correoelectronico=? limit 1) ||'%') AND p.iseliminado=false AND p.cattipoprueba_pid=1 AND r.disponible=true AND ocupado=false ORDER BY p.registrados ASC, disponibles.disponibles DESC"
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
