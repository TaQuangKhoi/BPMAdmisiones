package com.anahuac.rest.api.DB

class Statements {
	public static final String CONFIGURACIONESSSA="SELECT * FROM CATCONFIGURACION WHERE CLAVE = 'SASAzure'";
	
	public static final String INFO_REPORTE = "SELECT sda.resultadoPAA,sda.caseid,ds.idbanner,TO_CHAR(tp.fechaFinalizacion::DATE, 'dd-Mon-yyyy') AS fechaFinalizacion,sda.urlfoto,CONCAT(sda.apellidopaterno,' ',sda.apellidomaterno,CASE WHEN (sda.apellidomaterno != '' ) THEN ' ' END,sda.segundonombre,CASE WHEN ( sda.segundonombre != '' ) THEN ' ' END,sda.primernombre) AS nombre,  TO_CHAR(sda.fechanacimiento::DATE, 'dd-Mon-yyyy') AS fechanacimiento ,(CASE WHEN cb.descripcion = 'Otro' THEN sda.bachillerato ELSE cb.descripcion END) AS preparatoria, (CASE WHEN cb.descripcion = 'Otro' THEN sda.ciudadBachillerato ELSE cb.ciudad END) AS ciudad, cp.descripcion as pais, cge.nombre as carrera, IPAA.INVP,IPAA.PARA,IPAA.PAAN,IPAA.PAAV, sda.promediogeneral as promedio, cta.descripcion AS tipoAdmision, catP.clave as periodo,tp.quienIntegro, tp.quienRealizoEntrevista, date_part('year', age( sda.fechanacimiento::DATE)) as edad FROM SolicitudDeAdmision AS sda LEFT JOIN DetalleSolicitud AS ds ON sda.caseid = ds.caseid::INTEGER LEFT JOIN catbachilleratos AS cb ON cb.persistenceid = sda.catbachilleratos_pid LEFT JOIN catpais AS cp ON cp.persistenceid = sda.catpais_pid LEFT JOIN catGestionEscolar as CGE ON CGE.persistenceid = sda.catGestionEscolar_pid LEFT JOIN importacionPAA AS IPAA ON IPAA.idbanner = DS.idbanner and sda.caseid::varchar = IPAA.caseid LEFT JOIN catTipoAdmision AS cta ON cta.persistenceid = ds.cattipoadmision_pid LEFT JOIN catPeriodo AS catP ON catP.persistenceid = sda.catperiodo_pid LEFT JOIN testPsicometrico AS tp ON tp.caseid::INTEGER = sda.caseid WHERE sda.correoelectronico = ? AND countrechazo = ?"
	
	public static final String SELECT_SITUACION_SALUD = "SELECT auto.problemassaludatencioncontinua AS situacion_discapacidad, auto.hasRecibidoAlgunaTerapia AS tipo_terapia, cd.descripcion AS cat_situacion_discapacidad_descripcion, cps.descripcion AS cat_persona_saludable_descripcion, ct.descripcion AS cat_terapia_descripcion FROM autodescripcionV2 AS auto INNER JOIN CatDiscapacidad AS cd ON auto.catvivesestadodiscapacidad_pid = cd.persistenceid INNER JOIN CatPersonaSaludable cps ON auto.catpersonasaludable_pid = cps.persistenceid INNER JOIN CatTerapia AS ct ON auto.catrecibidoterapia_pid = ct.persistenceid WHERE auto.caseid = ?";
	
	public static final String SELECT_RECOMENDACIONES_CONCLUSIONES = "SELECT tp.resumenSalud AS salud, tp.conclusioneinvp AS conclusiones_recomendaciones, tp.interpretacioninvp AS interpretacion, cc.descripcion AS cursos_recomendados FROM testpsicometrico AS tp LEFT JOIN testpsicometri_custosrecomend AS tc ON tp.persistenceid = tc.testpsicometrico_pid LEFT JOIN catcursos AS cc ON tc.catcursos_pid = cc.persistenceid WHERE tp.caseid = ? and tp.countRechazo = ?"
	
	public static final String DATOS_BY_IDBANNER = " SELECT CASE WHEN sda.countrechazos is NULL THEN 0 ELSE sda.countrechazos END AS intentos ,sda.correoelectronico,DS.idbanner,SDA.urlFoto, SDA.urlConstancia, SDA.urlActaNacimiento  FROM DETALLESOLICITUD AS DS LEFT JOIN SOLICITUDDEADMISION AS SDA ON SDA.CASEID = DS.CASEID::INTEGER WHERE IDBANNER = ? limit 1"
}
