package com.anahuac.rest.api.DB

class Statements {
	
	public static final String CONFIGURACIONES="SELECT * FROM CATCONFIGURACION";
	
	public static final String UPDATE_FILE_URLS = "UPDATE SOLICITUDDEADMISION SET urlFoto = ?, urlActaNacimiento = ?, urlConstancia = ?, urlDescuentos = ?, urlResultadoPAA = ?, urlCartaAA = ? WHERE CASEID = ?";
	
	public static final String UPDATE_FILE_URLFOTO = "UPDATE SOLICITUDDEADMISION SET urlFoto = ? WHERE CASEID = ?";
	
	public static final String UPDATE_FILE_URLACTANACIMIENTO = "UPDATE SOLICITUDDEADMISION SET urlActaNacimiento = ? WHERE CASEID = ?";
	
	public static final String UPDATE_FILE_URLCONSTANCIA = "UPDATE SOLICITUDDEADMISION SET urlConstancia = ? WHERE CASEID = ?";
	
	public static final String UPDATE_FILE_URLDESCUENTOS = "UPDATE SOLICITUDDEADMISION SET urlDescuentos = ? WHERE CASEID = ?";
	
	public static final String UPDATE_FILE_URLRESULTADOPAA = "UPDATE SOLICITUDDEADMISION SET urlResultadoPAA = ? WHERE CASEID = ?";
	
	public static final String UPDATE_FILE_URLURLCARTAAA = "UPDATE SOLICITUDDEADMISION SET urlCartaAA = ? WHERE CASEID = ?";

	public static final String GET_COUNT_CONSTANCIAS = "SELECT COUNT(caseId) AS countConstancias FROM LISTACONSTANCIAFILE WHERE caseId = ?";
	
	public static final String INSERT_CONSTANCIA = "INSERT INTO LISTACONSTANCIAFILE (persistenceid, caseId, urlConstancia) VALUES (case when (SELECT max(persistenceId) + 1 from LISTACONSTANCIAFILE ) is null then 1 else (SELECT max(persistenceId) + 1 from LISTACONSTANCIAFILE) end, ?, ?)";
	
	public static final String GET_IDBANNER="SELECT * from detallesolicitud where caseid=? limit 1"
	
	public static final String GET_DOCUMENT_TO_DELETE = "SELECT tenantid, id, processinstanceid, documentid, name, description, version, index_ FROM document_mapping WHERE processinstanceid = ? AND name != 'fotoPasaporte'"
	
	public static final String DELETE_DOCUMENT = "DELETE FROM document WHERE id IN ([LSTDOCUMENTOID]);";
	
	public static final String DELETE_DOCUMENT_MAPPING = "DELETE FROM document_mapping WHERE processinstanceid = ? AND name != 'fotoPasaporte'";
}
