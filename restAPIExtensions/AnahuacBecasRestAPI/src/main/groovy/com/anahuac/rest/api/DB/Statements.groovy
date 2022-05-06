package com.anahuac.rest.api.DB

class Statements {
	public static final String GET_SOLICITUD_DE_ADMISION_BY_CORREO = "SELECT SAD.primernombre, SAD.segundonombre, SAD.apellidopaterno, SAD.apellidomaterno, SAD.correoelectronico, SAD.fechanacimiento, SAD.telefono, SEX.descripcion AS sexo, CTA.descripcion AS tipoAdmision, CGE.nombre AS licenciatura, CPE.descripcion AS semestre, DET.IDBANNER AS idalumno, CNA.descripcion AS nacionalidad, CTR.descripcion AS trabajas, CEC.descripcion AS estadocivil, SAD.caseid FROM SOLICITUDDEADMISION AS SAD INNER JOIN CATSEXO AS SEX ON SEX.PERSISTENCEID = SAD.CATSEXO_PID INNER JOIN DETALLESOLICITUD AS DET ON DET.CASEID = cast(SAD.CASEID as varchar)  INNER JOIN CATTIPOADMISION AS CTA ON CTA.PERSISTENCEID = DET.CATTIPOADMISION_PID INNER JOIN CATGESTIONESCOLAR AS CGE ON CGE.PERSISTENCEID = SAD.catgestionescolar_pid INNER JOIN CATPERIODO AS CPE ON CPE.PERSISTENCEID = SAD.CATPERIODO_PID INNER JOIN CATNACIONALIDAD AS CNA ON CNA.PERSISTENCEID = SAD.CATNACIONALIDAD_PID INNER JOIN AUTODESCRIPCIONV2 AS AUD ON AUD.CASEID = SAD.CASEID INNER JOIN CATACTUALMENTETRABAJAS AS CTR ON CTR.PERSISTENCEID = AUD.CATACTUALNENTETRABAJAS_PID INNER JOIN CATESTADOCIVIL AS CEC ON CEC.PERSISTENCEID = SAD.CATESTADOCIVIL_PID WHERE CORREOELECTRONICO = ?; ";
	
	public static final String GET_INFORMACION_ESCOLAR_BY_CORREO = "SELECT CBA.descripcion, SAD.promediogeneral AS promedioPreparatoria, CBA.id, CBA.ciudad, CBA.estado, CBA.pais, '' AS costoMensualPReparatoria, SAD.urlconstancia  FROM SOLICITUDDEADMISION AS SAD INNER JOIN CATBACHILLERATOS AS CBA ON CBA.PERSISTENCEID = SAD.CATBACHILLERATOS_PID WHERE CORREOELECTRONICO = ?;";
	
	public static final String GET_PADRES_TUTOT_BY_CASEID = "SELECT * FROM PADRESTUTOR WHERE CASEID = ?";

	public static final String SELECT_PROPERTIES_BONITA = "SELECT A.tenantid, A.id, A.process_id, A.name, A.value FROM proc_parameter AS A LEFT JOIN process_definition AS B ON B.processid = A.process_id WHERE A.name IN ('usuario','password') ORDER BY B.version DESC Limit 2";
	
	public static final String CONFIGURACIONESSSA="SELECT * FROM CATCONFIGURACION WHERE CLAVE = 'SASAzure'";
	
	public static final String GET_ASPIRANTES_EN_PROCESO = "SELECT periodo.fechafin as periodofin,CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') AS tiempoultimamodificacion, sda.fechasolicitudenviada, sda.fechaultimamodificacion, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, sda.telefono, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura FROM SOLICITUDDEADMISION sda LEFT JOIN catcampus campus ON campus.persistenceid=sda.CATCAMPUS_PID [CAMPUS] LEFT JOIN catcampus campusEstudio ON campusEstudio.persistenceid=sda.catcampusestudio_pid LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID [PROGRAMA] LEFT JOIN CATPERIODO periodo ON periodo.PERSISTENCEID =sda.CATPERIODO_PID [INGRESO] LEFT JOIN CATESTADOs estado ON estado.persistenceid =sda.CATESTADO_PID [ESTADO] LEFT JOIN catbachilleratos prepa ON prepa.PERSISTENCEID =sda.CATBACHILLERATOS_PID [BACHILLERATO] LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER AND da.vencido IS NOT true  LEFT JOIN CatTipoAdmision  as TA on TA.PERSISTENCEID = da.CATTIPOADMISION_PID LEFT JOIN CatResidencia  as R on R.PERSISTENCEID = da.CATRESIDENCIA_PID LEFT JOIN CatTipoAlumno  as TAL on TAL.PERSISTENCEID = da.CATTIPOALUMNO_PID LEFT JOIN CatCampus catcampus ON catcampus .persistenceid=sda.CATCAMPUS_PID LEFT JOIN CatPais AS catPais ON catPais.persistenceid=sda.CATPAIS_PID [TIPOALUMNO] [WHERE] GROUP BY periodo.fechafin,prepa.descripcion,sda.estadobachillerato, prepa.estado, sda.fechaultimamodificacion, sda.fechasolicitudenviada, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion,campus.descripcion, gestionescolar.nombre, periodo.descripcion, estado.descripcion, sda.estadoextranjero,sda.bachillerato,sda.promediogeneral,sda.estatussolicitud,da.tipoalumno,sda.caseid,sda.telefonocelular,sda.telefono,da.observacioneslistaroja,da.observacionesrechazo,da.idbanner,campus.grupobonita,ta.descripcion,r.descripcion,tal.descripcion,catcampus.descripcion,campusestudio.clave,gestionescolar.clave, sda.persistenceid [ORDERBY] [LIMITOFFSET] ";

	public static final String GET_SOLICITUDES_APOYO_BY_ESTATUS = "SELECT SDAE.fechaEnvioArea, SDAE.fechaRespuestaArea, SDAE.caseId, CASE SDA.aceptado WHEN 't' THEN 'Aceptado' WHEN 'f' THEN 'Rechazado' ELSE 'En proceso de admisión' END AS aceptado, SDAE.caseIdAdmisiones, SDAE.estatusSolicitud, SDAE.estatusSolicitud, SDAE.fechaRegistro, SDAE.fechaUltimaModificacion, SDA.urlfoto, SDA.apellidopaterno, SDA.apellidomaterno, SDA.primernombre, SDA.segundonombre, SDA.correoelectronico, SDA.curp, campus.descripcion AS campussede, gestionescolar.nombre AS licenciatura, gestionescolar.clave as claveLicenciatura, periodo.DESCRIPCION AS ingreso, tipoapoyo.descripcion AS tipoApoyo, sda.PROMEDIOGENERAL, TA.descripcion as tipoadmision, da.TIPOALUMNO,campusEstudio.descripcion as campusestudio FROM SolicitudApoyoEducativo SDAE INNER JOIN SOLICITUDDEADMISION SDA ON SDA.caseid =  SDAE.caseIdAdmisiones LEFT JOIN catcampus CAMPUS ON CAMPUS.persistenceid = SDA.CATCAMPUS_PID LEFT JOIN catcampus campusEstudio ON campusEstudio.persistenceid=sda.catcampusestudio_pid LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID LEFT JOIN CATPERIODO periodo ON periodo.PERSISTENCEID =sda.CATPERIODO_PID LEFT JOIN CatTipoApoyo tipoapoyo ON tipoapoyo.PersistenceId = SDAE.catTipoApoyo_id LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER AND da.vencido IS NOT true LEFT JOIN CatTipoAdmision TA on TA.PERSISTENCEID = da.CATTIPOADMISION_PID [WHERE] [ORDERBY] [LIMITOFFSET] ";
	
	public static final String GET_COUNT_SOLICITUDES_APOYO_BY_ESTATUS = "SELECT COUNT(SDAE.caseid) as registros FROM SolicitudApoyoEducativo SDAE INNER JOIN SOLICITUDDEADMISION SDA ON SDA.caseid =  SDAE.caseIdAdmisiones LEFT JOIN catcampus CAMPUS ON CAMPUS.persistenceid = SDA.CATCAMPUS_PID LEFT JOIN catcampus campusEstudio ON campusEstudio.persistenceid=sda.catcampusestudio_pid LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID LEFT JOIN CATPERIODO periodo ON periodo.PERSISTENCEID =sda.CATPERIODO_PID LEFT JOIN CatTipoApoyo tipoapoyo ON tipoapoyo.PersistenceId = SDAE.catTipoApoyo_id LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER AND da.vencido IS NOT true LEFT JOIN CatTipoAdmision TA on TA.PERSISTENCEID = da.CATTIPOADMISION_PID [WHERE] ";
	
	public static final String GET_SOLICITUD_APOYO_BY_CASE_ID = "SELECT SDAE.fechaEnvioArea, SDAE.fechaRespuestaArea, tipoapoyo.isArtistica, tipoapoyo.isDeportiva, tipoapoyo.isFinanciamiento, tipoapoyo.isAcademica, SDAE.caseId, CASE SDA.aceptado WHEN 't' THEN 'Aceptado' WHEN 'f' THEN 'Rechazado' ELSE 'En proceso de admisión' END AS aceptado, SDAE.caseIdAdmisiones, SDAE.estatusSolicitud, SDAE.estatusSolicitud, SDAE.fechaRegistro, SDAE.fechaUltimaModificacion, SDA.urlfoto, SDA.apellidopaterno, SDA.apellidomaterno, SDA.primernombre, SDA.segundonombre, SDA.correoelectronico, SDA.curp, campus.descripcion AS campussede, gestionescolar.nombre AS licenciatura, gestionescolar.clave as claveLicenciatura, periodo.DESCRIPCION AS ingreso, tipoapoyo.descripcion AS tipoApoyo, sda.PROMEDIOGENERAL, TA.descripcion as tipoadmision, da.TIPOALUMNO, SDAE.costoMensualColegiatura, SDAE.tieneHermanos, SDAE.motivoBeca, SDAE.cantMensualPagarUni, SDAE.contruccionM2Casa, SDAE.terrenoM2Casa, SDAE.valorAproxCasa, SDAE.calle, SDAE.delegacionCiudad, SDAE.numExterior, SDAE.numInterior, SDAE.pais, SDAE.colonia, SDAE.estado, SDAE.codigoPostal, SDAE.ingresoPadre, SDAE.ingresoMadre, SDAE.ingresoHermano, SDAE.ingresoTio, SDAE.ingresoAbuelo, SDAE.ingresoAspirante, SDAE.ingresoTotal, SDAE.egresoRenta, SDAE.egresoServicios, SDAE.egresoEducacion, SDAE.egresoGastosMedicos, SDAE.egresoAlimentacion, SDAE.egresoVestido, SDAE.egresoSeguro, SDAE.egresoDiversion, SDAE.egresoAhorro, SDAE.egresoCreditos, SDAE.egresoOtros, SDAE.egresoTotal, SDAE.urlVideoYouTube, SDAE.isSolDeportivaAprobada, SDAE.motivoRechazoDeportiva, SDAE.motivoAprobadaDeportiva, SDAE.isSolArtisticaAprobada, SDAE.motivoRechazoArtistica, SDAE.motivoAprobadaArtistica, SDAE.tieneHijos, provieneningresos.descripcion AS provienenIngresos, porcentajebeca.descripcion AS porcentajeBeca, porcentajefinanciamiento.descripcion AS porcentajeFinanciamiento, casadondevives.descripcion AS casaDondeVives FROM SolicitudApoyoEducativo SDAE INNER JOIN SOLICITUDDEADMISION SDA ON SDA.caseid =  SDAE.caseIdAdmisiones LEFT JOIN catcampus CAMPUS ON CAMPUS.persistenceid = SDA.CATCAMPUS_PID LEFT JOIN CATGESTIONESCOLAR gestionescolar ON gestionescolar.persistenceid=sda.CATGESTIONESCOLAR_PID LEFT JOIN CATPERIODO periodo ON periodo.PERSISTENCEID =sda.CATPERIODO_PID LEFT JOIN CatTipoApoyo tipoapoyo ON tipoapoyo.PersistenceId = SDAE.catTipoApoyo_id LEFT JOIN detallesolicitud da ON sda.caseid::INTEGER=da.caseid::INTEGER AND da.vencido IS NOT true LEFT JOIN CatTipoAdmision TA on TA.PERSISTENCEID = da.CATTIPOADMISION_PID LEFT JOIN CatProvienenIngresos provieneningresos ON provieneningresos.PersistenceId = SDAE.catProvienenIngresos_id LEFT JOIN CatPorcentajeBeca porcentajebeca ON porcentajebeca.PersistenceId = SDAE.catPorcentajeBeca_id  LEFT JOIN CatPorcentajeFinanciamiento porcentajefinanciamiento ON porcentajefinanciamiento.PersistenceId = SDAE.catPorcentajeFinanciamiento_id LEFT JOIN CatCasaDondeVives casadondevives ON casadondevives.PersistenceId = SDAE.catCasaDondeVives_id WHERE SDAE.eliminado =  false AND SDAE.caseId = ?";
	
	public static final String GET_DOCUMENTO_BASE64_BY_PERSISTENCE_ID = "SELECT D.urlDocumento, C.nombreDocumento , C.descripcionDocumento , C.isObligatorioDoc FROM DocumentosSolicitante D JOIN CatManejoDocumentos C ON C.PersistenceId = D.catManejoDocumentos_id WHERE D.PersistenceId = ?";
	
	public static final String GET_COMENTARIOS_BITACORA = "SELECT  COMENTARIO, FECHACREACION, MODULO, PERSISTENCEVERSION,USUARIO, USUARIOCOMENTARIO,  CASEID FROM BitacoraComentariosSDAE WHERE CASEID = ? AND ISELIMINADO <> true ORDER BY FECHACREACION DESC";
	
	public static final String INSERT_COMENTARIO_SDAE = "INSERT INTO BitacoraComentariosSDAE PERSISTENCEID, PERSISTENCEVERSION, COMENTARIO, FECHACREACION, MODULO, USUARIO, USUARIOCOMENTARIO,  CASEID VALUES ((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM BitacoraComentariosSDAE  ), 1, ?, now(), ?, ?, ?, ?)";
	
	public static final String DELETE_COMENTARIO_SDAE = "UPDATE INTO BitacoraComentariosSDAE ISELIMINADO = true WHERE CASEID = ?";
	
	public static final String UPDATE_ESTATUS_SDAE = "UPDATE SolicitudApoyoEducativo SET estatusSolicitud = ? WHERE caseId = ?";
}
