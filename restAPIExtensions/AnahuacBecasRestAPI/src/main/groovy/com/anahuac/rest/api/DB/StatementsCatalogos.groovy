package com.anahuac.rest.api.DB

class StatementsCatalogos {
	//INSERTS
	//EJEMPLO insert autoincrementado: 		INSERT tabla (persistenceid) VALUES ((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM tabla));
	
	public static final String INSERT_CATTIPOMONEDA = "INSERT INTO CATTIPOMONEDA (PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, USUARIOCREACION) VALUES ((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM CATTIPOMONEDA), ?, ?, now(), false, 1, ?);";
	
	public static final String INSERT_CATPROVIENENINGRESOS = "INSERT INTO CATPROVIENENINGRESOS (PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, USUARIOCREACION) VALUES ((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM CATPROVIENENINGRESOS), ?, ?, now(), false, 1, ?);";
	
	public static final String INSERT_CAT_GENERICO = "INSERT INTO CATGEN (PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, USUARIOCREACION) VALUES ((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM CATGEN), ?, ?, now(), false, 1, ?);";
	//UPDATES
	
	public static final String UPDATE_CATTIPOMONEDA = "UPDATE CATTIPOMONEDA SET CLAVE = ?, DESCRIPCION = ?, ISELIMINADO = ?, USUARIOCREACION = ? WHERE PERSISTENCEID = ?;";
	
	public static final String UPDATE_CATPROVIENENINGRESOS = "UPDATE CATPROVIENENINGRESOS SET CLAVE = ?, DESCRIPCION = ?, ISELIMINADO = ?, USUARIOCREACION = ? WHERE PERSISTENCEID = ?;";
	
	public static final String UPDATE_CAT_GENERICO = "UPDATE CATGEN SET CLAVE = ?, DESCRIPCION = ?, ISELIMINADO = ?, USUARIOCREACION = ? WHERE PERSISTENCEID = ?;";
	//GETS
	
	public static final String GET_CATGENERICO = "SELECT PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, USUARIOCREACION FROM [CATALOGO] [WHERE] [ORDERBY] [LIMITOFFSET]";
	
	public static final String GET_COUNT_CATGENERICO = "SELECT COUNT(persistenceid) as registros FROM [CATALOGO] [WHERE] ";
	
	/*Catálogo de manejo documentos*/
	public static final String GET_COUNT_CAT_MANEJO_DOCUMENTOS = "SELECT COUNT(PERSISTENCEID) AS registros FROM CatManejoDocumentos [WHERE] ";
	public static final String GET_CAT_MANEJO_DOCUMENTOS = "SELECT PERSISTENCEID, IDCAMPUS, IDTIPOAPOYO, NOMBREDOCUMENTO, ISOBLIGATORIO, URLDOCUMENTOAZURE, FECHACREACION, USUARIOCREACION, DESCRIPCIONDOCUMENTO, ISELIMINADO, requiereEjemplo FROM CatManejoDocumentos [WHERE] [ORDERBY] [LIMITOFFSET]";
	public static final String INSERT_CAT_MANEJO_DOCUMENTOS = " INSERT INTO CATMANEJODOCUMENTOS (PERSISTENCEID, IDCAMPUS, IDTIPOAPOYO, ISOBLIGATORIODOC, NOMBREDOCUMENTO, DESCRIPCIONDOCUMENTO, PERSISTENCEVERSION, URLDOCUMENTOAZURE, FECHACREACION, ISELIMINADO, USUARIOCREACION, requiereEjemplo) VALUES ((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM CatManejoDocumentos), ?, ?, ?, ?, ? , 1 , ? , now(), false, ?, ?);";
	public static final String UPDATE_CAT_MANEJO_DOCUMENTOS = "UPDATE CATMANEJODOCUMENTOS SET IDCAMPUS = ?, IDTIPOAPOYO = ?, ISOBLIGATORIODOC = ?, NOMBREDOCUMENTO = ?, URLDOCUMENTOAZURE = ?, DESCRIPCIONDOCUMENTO = ?, requiereEjemplo = ? WHERE PERSISTENCEID = ?;";
	public static final String DELETE_CAT_MANEJO_DOCUMENTOS = "UPDATE CatManejoDocumentos SET isEliminado = true WHERE PERSISTENCEID = ?";
	public static final String GET_COCUMENTOS_BY_APOYO_AND_CAMPUS = " SELECT * FROM CATMANEJODOCUMENTOS AS doc INNER JOIN CATTIPOAPOYO AS cta ON doc.idtipoapoyo = cta.PERSISTENCEID INNER JOIN RELACIONCAMPUSTIPOAPOYO AS rel ON rel.idtypoapoyo  = cta.PERSISTENCEID INNER JOIN CATCAMPUS AS cc ON cc.PERSISTENCEID = REL.IDCAMPUS [WHERE] ";
	public static final String GET_DOCUMENTOS_SOLICITANTE_BY_CASEID = "SELECT  doc.persistenceid AS doc_persistenceid, doc.caseid AS doc_caseid, doc.urlDocumento AS doc_urlDocumento, doc.catmanejodocumentos_pid AS doc_catmanejodocumentos_pid, cma.persistenceid AS cma_persistenceid, cma.descripciondocumento AS cma_descripciondocumento, cma.fechacreacion AS cma_fechacreacion, cma.idcampus AS cma_dcampus, cma.idtipoapoyo AS cma_idtipoapoyo, cma.isobligatoriodoc AS cma_isobligatoriodoc, cma.nombredocumento AS cma_nombredocumento, cma.urldocumentoazure AS cma_urldocumentoazure, cma.requiereejemplo AS cma_requiereejemplo  FROM DocumentosSolicitante AS doc	INNER JOIN CATMANEJODOCUMENTOS AS cma  ON cma.persistenceId = doc.catManejoDocumentos_id WHERE doc.caseid = ? ORDER BY doc.persistenceid ASC ";
	/*Fin del catálogo de manejo documentos*/
	
	/*Catálogo de tipo de apoyo*/
	public static final String GET_COUNT_CAT_TIPO_APOYO = "SELECT COUNT(PERSISTENCEID) AS TOTAL FROM CATTIPOAPOYO [WHERE] ";
	public static final String GET_CAT_TIPO_APOYO = "SELECT PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, USUARIOCREACION, ISARTISTICA, ISDEPORTIVA, ISACADEMICA, ISFINANCIAMIENTO FROM CATTIPOAPOYO [WHERE] [ORDERBY] [LIMITOFFSET]";
	public static final String INSERT_CAT_TIPO_APOYO = "INSERT INTO CATTIPOAPOYO  (PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, USUARIOCREACION, ISARTISTICA, ISDEPORTIVA, ISACADEMICA, ISFINANCIAMIENTO ) VALUES((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM CATTIPOAPOYO), ?, ?, now(), false, 1, ?, ?, ?, ?, ?);";
	public static final String UPDATE_CAT_TIPO_APOYO = "UPDATE CATTIPOAPOYO SET CLAVE = ?, DESCRIPCION = ?, ISELIMINADO = ?, ISARTISTICA = ?, ISDEPORTIVA = ?, ISACADEMICA = ?, ISFINANCIAMIENTO = ? WHERE PERSISTENCEID = ?;";
	public static final String UPDATE_CAT_TIPO_APOYO_VIDEO = "UPDATE RELACIONCAMPUSTIPOAPOYO SET REQUIEREVIDEO = ?, CONICIONESVIDEO = ?, ESSOCIOECONOMICO = ?, requiereCurriculum = ?, urlEjemploCurriculum = ? WHERE idCampus = ? AND idTypoApoyo = ?";
	public static final String DELETE_CAT_TIPO_APOYO = "UPDATE CATTIPOAPOYO SET isEliminado = true WHERE PERSISTENCEID = ?";
	public static final String GET_TIPO_APOYO_BY_CAMPUS = "SELECT ta.PERSISTENCEID, ta.DESCRIPCION, rct.URLEJEMPLOCURRICULUM, rct.REQUIERECURRICULUM, rct.REQUIEREVIDEO, rct.CONICIONESVIDEO, rct.ESSOCIOECONOMICO, ta.ISARTISTICA, ta.ISDEPORTIVA, ta.ISACADEMICA, ta.ISFINANCIAMIENTO FROM CATTIPOAPOYO AS ta INNER JOIN RELACIONCAMPUSTIPOAPOYO AS rct ON ta.PERSISTENCEID = rct.IDTYPOAPOYO INNER JOIN CATCAMPUS AS cc ON cc.PERSISTENCEID = rct.IDCAMPUS [WHERE]";
	public static final String GET_CAMPUS_BY_TIPO_APOYO = "SELECT CC.* FROM CATCAMPUS AS CC INNER JOIN RELACIONCAMPUSTIPOAPOYO AS REL  ON CC.PERSISTENCEID = REL.IDCAMPUS INNER JOIN CATTIPOAPOYO AS CTA ON CTA.PERSISTENCEID = REL.IDTYPOAPOYO   WHERE CC.ISELIMINADO = FALSE AND CTA.PERSISTENCEID = ?";
	public static final String GET_CAMPUS_TIPO_APOYO_EXIST = "SELECT * FROM RELACIONCAMPUSTIPOAPOYO WHERE IDCAMPUS = ? AND IDTYPOAPOYO = ?";
	public static final String INSERT_CAMPUS_TIPO_APOYO_EXIST = "INSERT INTO RELACIONCAMPUSTIPOAPOYO  (PERSISTENCEID, PERSISTENCEVERSION, IDCAMPUS, IDTYPOAPOYO ) VALUES ((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM RELACIONCAMPUSTIPOAPOYO), 1, ?, ?)";
	public static final String DELETE_CAMPUS_TIPO_APOYO_EXIST = "DELETE FROM  RELACIONCAMPUSTIPOAPOYO WHERE IDCAMPUS = ? AND IDTYPOAPOYO = ?";
	public static final String GET_IMAGENES_BY_TIPO_APOYO = "SELECT IMG.* FROM CATCAMPUS AS CC INNER JOIN RELACIONCAMPUSTIPOAPOYO AS REL ON REL.IDCAMPUS = CC.PERSISTENCEID INNER JOIN CATTIPOAPOYO AS CTA ON CTA.PERSISTENCEID = REL.IDTYPOAPOYO INNER JOIN CATIMAGENESSOCIOECONOMICO AS IMG ON IMG.IDCAMPUS = CC.PERSISTENCEID  AND CTA.PERSISTENCEID = IMG.IDTIPOAPOYO WHERE IMG.ISELIMINADO = false AND CC.GRUPOBONITA = ? AND CTA.PERSISTENCEID = ?";
	public static final String INSERT_IMAGEN_SOCIO_ECONOMICO = "INSERT INTO CATIMAGENESSOCIOECONOMICO  (PERSISTENCEID, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, URLAZURE, USUARIOCREACION, IDCAMPUS, IDTIPOAPOYO) VALUES ((SELECT CASE WHEN max(PERSISTENCEID) IS NULL THEN 1 ELSE max(PERSISTENCEID)+1 END FROM CATIMAGENESSOCIOECONOMICO  ) , ?, now(), false, 1, '', ?, ?, ?)";
	public static final String UPDATE_IMAGEN_SOCIO_ECONOMICO = "UPDATE CATIMAGENESSOCIOECONOMICO  SET DESCRIPCION = ?, ISELIMINADO = ?, IDCAMPUS = ?, IDTIPOAPOYO = ?  WHERE PERSISTENCEID = ?";
	public static final String DELETE_IMAGEN_SOCIO_ECONOMICO = "UPDATE CATIMAGENESSOCIOECONOMICO  SET ISELIMINADO = ? WHERE PERSISTENCEID = ?";
	public static final String GET_CAT_TIPO_APOYO_BY_ID = "SELECT PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, ISELIMINADO, PERSISTENCEVERSION, USUARIOCREACION, ISARTISTICA, ISDEPORTIVA, ISACADEMICA, ISFINANCIAMIENTO FROM CATTIPOAPOYO WHERE PERSISTENCEID = ?;";
	/*Fin del catálogo de tipo de apoyo*/
	
	public static final String GET_CAT_TIENES_HIJOS = "SELECT PERSISTENCEID, CLAVE, DESCRIPCION, FECHACREACION, isEliminadoBool,  USUARIOCREACION FROM CATTIENESHIJOS WHERE isEliminadoBool = false ORDER BY  PERSISTENCEID";
	public static final String GET_COUNT_CAT_TIENES_HIJOS = "SELECT COUNT(PERSISTENCEID) AS total FROM CATTIENESHIJOS WHERE isEliminadoBool = false";
	
	public static final String UPDATE_ESTATUS_SOLICITUD = "UPDATE SolicitudApoyoEducativo SET estatusSolicitud = ? WHERE caseId = ?";
	
	/*Gestion Escolar */
	public static final String INSERT_SDAECAT_GESTION_ESCOLAR = "INSERT INTO SDAECATGESTIONESCOLAR (PERSISTENCEID,PERSISTENCEVERSION,PARCIALIDAD,CREDITOSEMESTRE,MANEJAAPOYO,CATGESTIONESCOLAR_PID) VALUES (case when (SELECT max(persistenceId)+1 from SDAECatGestionEscolar ) is null then 1 else (SELECT max(persistenceId)+1 from SDAECatGestionEscolar) end,0,?,?,?,?)";
	public static final String INSERT_SDAECAT_CREDITO_GE = "INSERT INTO SDAECATCREDITOGE (PERSISTENCEID,PERSISTENCEVERSION,CREDITOENERO,CREDITOMAYO,CREDITOAGOSTO,CREDITOSEPTIEMBRE,FECHA,SDAECATGESTIONESCOLAR_PID) VALUES (case when (SELECT max(persistenceId)+1 from SDAECATCREDITOGE ) is null then 1 else (SELECT max(persistenceId)+1 from SDAECATCREDITOGE) end,0,?,?,?,?,?,?)";
	public static final String GET_EXISTE_SDAECAT_GESTION_ESCOLAR = "SELECT PERSISTENCEID FROM SDAECATGESTIONESCOLAR WHERE CATGESTIONESCOLAR_PID = ?";
	public static final String GET_EXISTE_SDAECAT_CREDITO_GE = "SELECT PERSISTENCEID FROM SDAECATCREDITOGE WHERE sdaeCatGestionEscolar_pid = ? AND FECHA = ?";
	public static final String UPDATE_SDAECAT_GESTION_ESCOLAR = "UPDATE SDAECATGESTIONESCOLAR SET PARCIALIDAD = ?,CREDITOSEMESTRE = ?,MANEJAAPOYO = ? WHERE PERSISTENCEID = ?";
	public static final String UPDATE_SDAECAT_CREDITO_GE = "UPDATE SDAECATCREDITOGE SET CREDITOENERO = ?, CREDITOMAYO = ?, CREDITOAGOSTO = ?, CREDITOSEPTIEMBRE = ? WHERE PERSISTENCEID = ?";
	public static final String GET_SDAECAT_CREDITO_GE = "SELECT PERSISTENCEID,CREDITOENERO,CREDITOMAYO,CREDITOAGOSTO,CREDITOSEPTIEMBRE,FECHA,SDAECATGESTIONESCOLAR_PID FROM SDAECATCREDITOGE WHERE SDAECATGESTIONESCOLAR_PID = ? AND FECHA = ?";
	public static final String GET_SDAECAT_GESTION_ESCOLAR = "SELECT PERSISTENCEID,PARCIALIDAD,CREDITOSEMESTRE,MANEJAAPOYO,CATGESTIONESCOLAR_PID FROM SDAECATGESTIONESCOLAR WHERE CATGESTIONESCOLAR_PID = ?"
	
	public static final String GET_YEAR = "SELECT date_part('year', CURRENT_DATE) as fecha ";
	
	public static final String GET_CAMPUS_CONFIG = "SELECT PERSISTENCEID, DESCUENTOPRONTOPAGO, IDCAMPUS, INTERESCOLEGIATURA, INTERESINSCRIPCION, PORCENTAJEINTERESFINANCIAMIENTO, TIENEFINANCIAMIENTO, URLREGLAMENTO FROM CONFIGURACIONESCAMPUS WHERE  IDCAMPUS = ?;";
	public static final String INSERT_CAMPUS_CONFIG = "INSERT INTO CONFIGURACIONESCAMPUS (PERSISTENCEID, PERSISTENCEVERSION, DESCUENTOPRONTOPAGO, IDCAMPUS, INTERESCOLEGIATURA, INTERESINSCRIPCION, PORCENTAJEINTERESFINANCIAMIENTO, TIENEFINANCIAMIENTO, URLREGLAMENTO) VALUES (case when (SELECT max(persistenceId)+1 from CONFIGURACIONESCAMPUS ) is null then 1 else (SELECT max(persistenceId)+1 from CONFIGURACIONESCAMPUS) end, 0, ?, ?, ?, ?, ?, ?, ?);";
	public static final String UPDATE_CAMPUS_CONFIG = "UPDATE CONFIGURACIONESCAMPUS SET DESCUENTOPRONTOPAGO = ?, INTERESCOLEGIATURA = ?, INTERESINSCRIPCION = ?, PORCENTAJEINTERESFINANCIAMIENTO = ?, TIENEFINANCIAMIENTO = ?, URLREGLAMENTO = ? WHERE IDCAMPUS = ?";
	
}
