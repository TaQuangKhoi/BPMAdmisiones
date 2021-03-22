package com.anahuac.rest.api.DAO
import java.nio.charset.StandardCharsets
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.DateFormat
import java.time.LocalDate

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.DetalleSolicitudDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.engine.api.APIClient
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class ResultadosAdmisionDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogosDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
		return retorno
	}
	
	/*
	 * Servicio apara subida del archivo Excel de resultados
	 * */
	public Result obtieneDatosDelB64(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			String excelB64 = object.fileBase64;
			byte[] decodedExcel = Base64.getDecoder().decode(excelB64.getBytes(StandardCharsets.UTF_8));
			InputStream fis = new ByteArrayInputStream(decodedExcel);//Para manejar el archivo
			List<LinkedHashMap<String, String>> valoresExcel = new ArrayList<>();
			XSSFWorkbook workbook = new XSSFWorkbook(fis);//Manejo del Excel
			XSSFSheet sheet = workbook.getSheetAt(0);//Hoja
			Iterator<Row> rowIterator = sheet.iterator();//Para iterar las filas
			Row row;
			List<String> titulos = new ArrayList<>();//Para el nombre de la columna en el objeto
			int rowCount = 0;
			int maxCellCount = 0;
			
			//Iterando las filas usando un iterator
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				LinkedHashMap<String, String> objetoRow = new LinkedHashMap<>();
				objetoRow.put("filaExcel", row.getRowNum());
//				Iterator<Cell> cellIterator = row.cellIterator();
				Cell celda;
				int cellIndex = 0;
				DataFormatter dataFormatter = new DataFormatter();
				if(dataFormatter.formatCellValue(row.getCell(0)).equals("") || dataFormatter.formatCellValue(row.getCell(0)) == null) {
					break;	
				}
				//Iterando las columnas con un for por que iterator presentaba datos incompletos
				for(int i = 0; i < 54; i++){
					//Se obtiene el valor de la celda en específico
					celda = row.getCell(i);
					if (row.getRowNum() == 0) {
						titulos.add(celda.getStringCellValue().toLowerCase().trim());
					}

					int numCelda = 0;
					numCelda = i;
					
					//Obtener el título
					String tituloActual = "";
					if (numCelda < titulos.size()) {
						tituloActual = titulos.get(numCelda).toString().toLowerCase();
						if (tituloActual == null) {
							tituloActual = "";
						}
					}
					
					tituloActual = tituloActual.trim();
					if(tituloActual.equals("iversidad")) {
						tituloActual = "universidad"
					}
					
					//Se quitan los acentos, espacios y ñ
					tituloActual = tituloActual.replace('á', 'a');
					tituloActual = tituloActual.replace('é', 'e');
					tituloActual = tituloActual.replace('í', 'i');
					tituloActual = tituloActual.replace('ó', 'o');
					tituloActual = tituloActual.replace('ú', 'u');
					tituloActual = tituloActual.replace('ñ', 'n');
					tituloActual = tituloActual.replace(' ', '');
					
					String value = dataFormatter.formatCellValue(celda);
					
					//Validar valor de campos específicos
					if(tituloActual.toLowerCase().trim().equals("numerodematricula")) {
						value = "00" + value;
					} else if (tituloActual.toLowerCase().trim().equals("carta")) {
						if(value.equals("A")) {
							value = "Aceptado";
						} else if(value.equals("N")) {
							value = "Rechazado";
						} else if(value.equals("R")) {
							value = "Enviado a revisión";
						}
					} else if (tituloActual.toLowerCase().trim().equals("statuspdu")) {
						if(value.equals("1")) {
							value = "Hace PDU";
						} else if(value.equals("0")) {
							value = "No hace PDU";
						}
					}
					
					//Poner el dato con su clave dentro del objeto
					objetoRow.put(tituloActual.toLowerCase().trim(), value);
				}
				
				if(rowCount  > 0) {
					valoresExcel.add(objetoRow);
				}
				
				rowCount ++;
			}
			
			workbook.close();
			fis.close();
			resultado.setData(valoresExcel);
			resultado.setSuccess(true);
		} catch (IOException ioEx) {
			  ioEx.printStackTrace();
			  resultado.setSuccess(false);
			  resultado.setError(ioEx.getMessage());
		} catch (IllegalArgumentException ilegalExc) {
			resultado.setSuccess(false);
			resultado.setError(ilegalExc.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(ex.getMessage());
		}
		
		return resultado;
	}
	
	/*
	 * Servicio para limpiar la tabla de cartas temporales
	 * */
	public Result clearInfoCartaTemporal() {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog="", campus = "";
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			String consulta = Statements.CLEAR_INFO_CARTA_TEMPORAL;
			pstm = con.prepareStatement(consulta);
			pstm.executeUpdate();
			
			resultado.setSuccess(true);
			resultado.setError_info(errorlog);
			
		} catch (Exception e) {
			resultado.setError_info(errorlog)
			//resultado.setError_info(consulta)
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result selectInfoCartaTemporal(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = "";
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			
			List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
			
			userLogged = context.getApiSession().getUserId();
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(CatCampus rowGrupo : lstCatCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}
			
			assert object instanceof Map;
			where+=" WHERE INFTEMP.persistenceid IS NOT null AND SOLAD.estatussolicitud = 'Carga y consulta de resultados' ";
//			where+=" WHERE INFTEMP.persistenceid IS NOT null AND SOLAD.estatussolicitud = 'Carga y consulta de resultados' ";
//			if(object.campus != null){
//				if(!object.campus.equals("")) {
//					where+=" AND LOWER(universidad) = LOWER('" + object.campus + "') ";
//				}
//			}
//			
//			if(object.campus != null){
//				if(!object.campus.equals("Todos los campus")) {
//					where+=" AND LOWER(universidad) = LOWER('" + object.campus + "') ";
//				}
//			}
//			
//			if(lstGrupo.size() > 0) {
//				campus+=" AND ("
//			}
//			
//			for(Integer i=0; i<lstGrupo.size(); i++) {
//				String campusMiembro=lstGrupo.get(i);
//				campus += "universidad='" + campusMiembro + "'"
//				if(i == (lstGrupo.size() - 1)) {
//					campus += ") "
//				} else {
//					campus += " OR "
//				}
//			}
			
			if(object.campus != null){
				if(object.campus.equals("Todos los campus")) {
					if(lstGrupo.size() > 0) {
						campus+=" AND ("
					}
					
					for(Integer i=0; i < lstGrupo.size(); i++) {
						String campusMiembro = lstGrupo.get(i);
						campus += "universidad='" + campusMiembro + "'";
						if(i == (lstGrupo.size() - 1)) {
							campus += ") ";
						} else {
							campus += " OR ";
						}
					}
					
//					where+=" AND LOWER(universidad) = LOWER('" + object.campus + "') ";
				} else {
					campus = "";
				}
			}
			
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			closeCon = validarConexion();
			
			String consulta = Statements.GET_INFO_CARTA_TEMPORAL;
			
			errorlog += " | | " + where;
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				errorlog+=", columna "+ filtro.get("columna")
				switch(filtro.get("columna")) {
					case "ID":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(ID) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "IDBANNER,NOMBRE,EMAIL":
						if(where.contains("WHERE")) {
							where += " AND ";
						}else {
							where += " WHERE ";
						}
						where +=" ( LOWER(DETSOL.IDBANNER) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(INFTEMP.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(SOLAD.CORREOELECTRONICO) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "CAMPUS,LICENCIATURA":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(INFTEMP.UNIVERSIDAD) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(INFTEMP.CARRERA) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "CAMPUS":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						
						where +=" ( LOWER(INFTEMP.UNIVERSIDAD) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "PDU":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(PDU) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					/*====================================================================*/
				}
			}
				
			switch(object.orderby) {
				case "ID":
					orderby += "SOLAD.PERSISTENCEID";
				break;
				case "IDBANNER":
					orderby += "DETSOL.IDBANNER";
				break;
				case "NOMBRE":
					orderby += "INFTEMP.NOMBRE";
				break;
				case "EMAIL":
					orderby += "SOLAD.CORREOELECTRONICO";
				break;
				case "CAMPUS":
					orderby += "INFTEMP.UNIVERSIDAD";
				break;
				case "LICENCIATURA":
					orderby += "INFTEMP.CARRERA";
				break;
				case "PDU":
					orderby += "INFTEMP.PDU";
				break;
				case "MATE":
					orderby += "INFTEMP.SIHACEONOMATEMATICAS";
				break;
				case "ESP":
					orderby += "INFTEMP.ESPANOL";
				break;
				case "RESPUESTA":
					orderby += "INFTEMP.DESICIONDEADMISION";
				break;
				default:
					orderby += "INFTEMP.NOMBRE";
				break;
			}
			
			orderby += " " + object.orientation;
			
//			consulta = consulta.replace("[CAMPUS]", campus);
			where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno;
			
			errorlog += " | | " + where;
			consulta = consulta.replace("[WHERE]", where);
			
			String countQuery = Statements.GET_INFO_CARTA_TEMPORAL_COUNT;
			countQuery = countQuery.replace("[WHERE]", where);
			pstm = con.prepareStatement(countQuery);

			rs= pstm.executeQuery()
			if(rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"));
			}
			consulta = consulta.replace("[ORDERBY]", orderby);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery()
			rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
//			errorlog = consulta;
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("persistenceid")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					} else if (metaData.getColumnLabel(i).toLowerCase().equals("seleccionado")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getBoolean(i));
					} else {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			
			resultado.setError_info(errorlog);
			resultado.setData(rows);
		} catch (Exception e) {
			resultado.setError_info(errorlog);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error e.getMessage();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result selectInfoCartaTemporalNoResultados(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = "";
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			
			List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
			
			userLogged = context.getApiSession().getUserId();
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(CatCampus rowGrupo : lstCatCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}
			
			assert object instanceof Map;
			where+=" WHERE DETSOL.IDBANNER IS NULL OR SOLAD.estatussolicitud <> 'Carga y consulta de resultados' ";
			if(object.campus != null){
				if(object.campus.equals("Todos los campus")) {
					if(lstGrupo.size() > 0) {
						campus+=" AND ("
					}
					
					for(Integer i=0; i < lstGrupo.size(); i++) {
						String campusMiembro = lstGrupo.get(i);
						campus += "universidad='" + campusMiembro + "'";
						if(i == (lstGrupo.size() - 1)) {
							campus += ") ";
						} else {
							campus += " OR ";
						}
					}
				} else {
					campus = "";
				}
			}
			
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			closeCon = validarConexion();
			
			String consulta = Statements.GET_INFO_CARTA_TEMPORAL_NO_RESULTADOS;
			
			errorlog += " | | " + where;
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				errorlog+=", columna "+ filtro.get("columna")
				switch(filtro.get("columna")) {
					case "ID":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(ID) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "IDBANNER,NOMBRE,EMAIL":
						if(where.contains("WHERE")) {
							where += " AND ";
						}else {
							where += " WHERE ";
						}
						where +=" ( LOWER(DETSOL.IDBANNER) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(INFTEMP.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(SOLAD.CORREOELECTRONICO) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "CAMPUS,LICENCIATURA":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(INFTEMP.UNIVERSIDAD) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(INFTEMP.CARRERA) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "CAMPUS":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						
						where +=" ( LOWER(INFTEMP.UNIVERSIDAD) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "PDU":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(PDU) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					/*====================================================================*/
				}
			}
				
			switch(object.orderby) {
				case "ID":
					orderby += "SOLAD.PERSISTENCEID";
				break;
				case "IDBANNER":
					orderby += "DETSOL.IDBANNER";
				break;
				case "NOMBRE":
					orderby += "INFTEMP.NOMBRE";
				break;
				case "EMAIL":
					orderby += "SOLAD.CORREOELECTRONICO";
				break;
				case "CAMPUS":
					orderby += "INFTEMP.UNIVERSIDAD";
				break;
				case "LICENCIATURA":
					orderby += "INFTEMP.CARRERA";
				break;
				case "PDU":
					orderby += "INFTEMP.PDU";
				break;
				case "MATE":
					orderby += "INFTEMP.SIHACEONOMATEMATICAS";
				break;
				case "ESP":
					orderby += "INFTEMP.ESPANOL";
				break;
				case "RESPUESTA":
					orderby += "INFTEMP.DESICIONDEADMISION";
				break;
				default:
					orderby += "INFTEMP.NOMBRE";
				break;
			}
			
			orderby += " " + object.orientation;
			
//			consulta = consulta.replace("[CAMPUS]", campus);
			where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno;
			
			errorlog += " | | " + where;
			consulta = consulta.replace("[WHERE]", where);
			
			String countQuery = Statements.GET_INFO_CARTA_TEMPORAL_COUNT;
			countQuery = countQuery.replace("[WHERE]", where);
			pstm = con.prepareStatement(countQuery);

			rs= pstm.executeQuery()
			if(rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"));
			}
			consulta = consulta.replace("[ORDERBY]", orderby);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery()
			rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
//			errorlog = consulta;
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("persistenceid")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					} else if (metaData.getColumnLabel(i).toLowerCase().equals("seleccionado")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getBoolean(i));
					} else if (metaData.getColumnLabel(i).toLowerCase().equals("idnocoincide")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getBoolean(i));
					} else if (metaData.getColumnLabel(i).toLowerCase().equals("estatusincorrecto")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getBoolean(i));
					} else {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			
			resultado.setError_info(errorlog);
			resultado.setData(rows);
		} catch (Exception e) {
			resultado.setError_info(errorlog);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error e.getMessage();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result selectConsultaDeResultados(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = "";
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			
			List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
			
			userLogged = context.getApiSession().getUserId();
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(CatCampus rowGrupo : lstCatCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}
			
			assert object instanceof Map;
			where+=" WHERE INFTEMP.persistenceid IS NOT null";
			if(object.campus != null){
				if(!object.campus.equals("Todos los campus")) {
					where+=" AND LOWER(universidad) = LOWER('" + object.campus + "') ";
				}

			}
			
			if(lstGrupo.size()>0) {
				campus+=" AND ("
			}
			
			for(Integer i=0; i<lstGrupo.size(); i++) {
				String campusMiembro=lstGrupo.get(i);
				campus += "universidad='" + campusMiembro + "'"
				if(i == (lstGrupo.size() - 1)) {
					campus += ") "
				} else {
					campus += " OR "
				}
			}
			
			errorlog += "universidad" + campus;
			errorlog += "object.lstFiltro" + object.lstFiltro;
			errorlog="1";
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			closeCon = validarConexion();
			errorlog="2";
			String consulta = Statements.GET_INFO_CONSULTA_RESULTADOS;
			/*
			if(object.tipoResultado != null){
				if(object.tipoResultado.equals("Aceptado")) {
					consulta = consulta.replace("[TABLAINFO]", "InfoCarta");
				}else if(object.tipoResultado.equals("Rechazada")){
					consulta = consulta.replace("[TABLAINFO]", "InfoCarta");
					consulta = consulta.replace("[TABLAINFO]", "InfoCartaTemporal");
				}else if(object.tipoResultado.equals("Sin resultado")){
					consulta = consulta.replace("[TABLAINFO]", "InfoCarta");
					consulta = consulta.replace("[TABLAINFO]", "InfoCartaTemporal");
				}
			}
			*/
			errorlog="3";
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				errorlog="4";
				switch(filtro.get("columna")) {
					case "IDBANNER":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(INFTEMP.NUMERODEMATRICULA) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "NOMBRE,EMAIL,CURP":
					
						if(where.contains("WHERE")) {
							where += " AND ";
						}else {
							where += " WHERE ";
						}
						where +=" ( LOWER(INFTEMP.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(SOLAD.CORREOELECTRONICO) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(SOLAD.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "CAMPUS,PROGRAMA,PERÍODO":
					errorlog="5";
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));

						where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					
					case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(estado) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(preparatoria) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));

						where +=" OR LOWER(SOLAD.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;

					case "RESIDENCIA,TIPO DE ADMISIÓN,TIPO DE ALUMNO":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(R.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						
						where +=" OR LOWER(TA.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));

						where +=" OR LOWER(TAL.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;

					case "FECHA DE ENVÍO":
						if(where.contains("WHERE")) {
							where+= " AND ";
						}else {
							where+= " WHERE ";
						}
						where +=" ( LOWER(to_char( TO_TIMESTAMP(SOLAD.fechasolicitudenviada, 'YYYY-MM-DDTHH:MI'), 'DD/MM/YYYY') ) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "CIUDAD":
					if(where.contains("WHERE")) {
						where+= " AND ";
					}else {
						where+= " WHERE ";
					}
					where +=" ( LOWER(SOLAD.ciudad) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "PAÍS":
					if(where.contains("WHERE")) {
						where+= " AND ";
					}else {
						where+= " WHERE ";
					}
					where +=" ( LOWER(paisvives.descripcion) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "PROCEDENCIA":
					if(where.contains("WHERE")) {
						where+= " AND ";
					}else {
						where+= " WHERE ";
					}
					where +=" ( LOWER(estado) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "CAMPUS INGRESO":
					if(where.contains("WHERE")) {
						where+= " AND ";
					}else {
						where+= " WHERE ";
					}
					where +=" ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"));
					break;
					case "PERÍODO":
					if(where.contains("WHERE")) {
						where+= " AND ";
					}else {
						where+= " WHERE ";
					}
					where +=" ( LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"));
					break;
					/*====================================================================*/
				}
				errorlog="7";
				/*switch(filtro.get("columna")) {
					case "NOMBRE,EMAIL,CURP":
						errorlog+="NOMBRE,EMAIL,CURP"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(concat(sda.primernombre,' ', sda.segundonombre,' ',sda.apellidopaterno,' ',sda.apellidomaterno)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				}*/
			}
			   
			
			// Seleccion de resultados
			if(object.tipoResultado != null){
				if(object.tipoResultado.equals("Aceptado")) {
					where+="  AND (carta='Aceptado') ";
				}else if(object.tipoResultado.equals("Rechazada")){
					where+="  AND (carta='Rechazado') ";
				}else if(object.tipoResultado.equals("Sin resultado")){
					where+="  AND (carta='Enviado a Revisión') ";
				}
			}
			// Seleccion de resultados
	
			switch(object.orderby) {
				case "IDBANNER":
					orderby += "INFTEMP.NUMERODEMATRICULA";
				break;
				case "NOMBRE":
					orderby += "INFTEMP.NOMBRE";
				break;
				case "EMAIL":
					orderby += "SOLAD.CORREOELECTRONICO";
				break;
				case "CURP":
				orderby += "SOLAD.curp";
				break;
				case "CAMPUS":
					orderby += "campus";
				break;
				case "PROGRAMA":
					orderby += "licenciatura";
				break;
				case "INGRESO":
					orderby += "ingreso";
				break;
				case "ESTADO":
					orderby += "estado";
				break;
				case "PREPARATORIA":
					orderby += "preparatoria";
				break;
				case "PROMEDIO":
					orderby += "SOLAD.PROMEDIOGENERAL";
				break;
				case "RESIDEICA":
				orderby += "residensia";
				break;
				case "TIPODEADMISION":
				orderby += "tipoadmision";
				break;
				case "TIPODEALUMNO":
				orderby += "tipoDeAlumno";
				break;
				case "RESULTADOADMISION":
				orderby += "INFTEMP.carta";
				break;
				case "FECHAENVIO":
				orderby += "fechaSolicitudEnviadaFormato";
				break;
				case "NOMBRESESION":
				orderby += "INFTEMP.NOMBRE";
				break;
				case "FECHASESION":
				orderby += "INFTEMP.NOMBRE";
				break;
				default:
					orderby += "INFTEMP.NOMBRE";
				break;
			}
			errorlog="8";
			
			orderby += " " + object.orientation;
			
			LOGGER.error "orderby : : " + orderby;
			
			consulta = consulta.replace("[CAMPUS]", campus);
			where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno;
			consulta = consulta.replace("[WHERE]", where);
			String countQuery = Statements.GET_INFO_CONSULTA_RESULTADOS_COUNT;
			
			/*
			 if(object.tipoResultado != null){
				 if(object.tipoResultado.equals("Aceptado")) {
					 countQuery = countQuery.replace("[TABLAINFO]", "InfoCarta");
				 }else if(object.tipoResultado.equals("Rechazada")){
					 countQuery = countQuery.replace("[TABLAINFO]", "InfoCarta");
					 countQuery = countQuery.replace("[TABLAINFO]", "InfoCartaTemporal");
				 }else if(object.tipoResultado.equals("Sin resultado")){
					 countQuery = countQuery.replace("[TABLAINFO]", "InfoCarta");
					 countQuery = countQuery.replace("[TABLAINFO]", "InfoCartaTemporal");
				 }
			 }
			 */
			
			countQuery = countQuery.replace("[WHERE]", where);
			errorlog="9 "+consulta;
			pstm = con.prepareStatement(countQuery);
			errorlog="10"+consulta;
			rs= pstm.executeQuery()
			if(rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"));
			}
			consulta = consulta.replace("[ORDERBY]", orderby);
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
			errorlog="11";
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit);
			pstm.setInt(2, object.offset);
			rs = pstm.executeQuery()
			rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			errorlog = consulta;
			errorlog="12";
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("persistenceid")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					} else if (metaData.getColumnLabel(i).toLowerCase().equals("seleccionado")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getBoolean(i));
					}else if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
							String encoded = "";
							try {
								for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
									encoded = "../API/formsDocumentImage?document="+doc.getId();
									columns.put("fotografiab64", encoded);
								}
							}catch(Exception e) {
								columns.put("fotografiab64", "");
								errorlog+= ""+e.getMessage();
							}
						} else {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
				}
	
				rows.add(columns);
			}
			resultado.setSuccess(true);
			
			resultado.setError_info(errorlog);
			resultado.setData(rows);
			
		} catch (Exception e) {
			resultado.setError_info(errorlog);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			LOGGER.error e.getMessage();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	
	public Result seleccionarCarta(Integer parameterP, Integer parameterC, String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorlog="", campus = "";
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			Boolean todos = object.todos;
			Boolean seleccionado = object.seleccionado;
			String persistenceid = object.persistenceid;
			String consulta = "";
			
			LOGGER.error object.todos.toString();
			LOGGER.error object.seleccionado.toString();
			LOGGER.error object.persistenceid;
			
			if(todos) {
				consulta = Statements.SELECCIONAR_TODAS_CARTAS;
				LOGGER.error consulta;
				pstm = con.prepareStatement(consulta);
				pstm.setBoolean(1, seleccionado);
			} else {
				consulta = Statements.SELECCIONAR_CARTA;
				LOGGER.error consulta;
				pstm = con.prepareStatement(consulta);
				pstm.setBoolean(1, seleccionado);
				pstm.setLong(2, Long.valueOf(persistenceid));
			} 
			
			pstm.executeUpdate();
			
			resultado.setSuccess(true);
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			resultado.setError_info(errorlog)
			//resultado.setError_info(consulta)
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result enviarCartas(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		List<?> lstData = new ArrayList<?>();
		List<?> lstEnviados = new ArrayList<?>();
		List<?> lstNoEnviados = new ArrayList<?>();
		String errorlog = "";
		try {
			closeCon = validarConexion();
			NotificacionDAO nDAO = new NotificacionDAO();
			String consulta = "";
			consulta = Statements.GET_CARTAS_A_ENVIAR;
			pstm = con.prepareStatement(consulta);
			
			rs = pstm.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while(rs.next()) {
				String correo = rs.getString("email");
				String carta = rs.getString("carta");
				String campusCorreo = "CAMPUS-CANCUN";
				String codigo = "";
				
				if(carta.equals("Aceptado")) {
					codigo = "carta-aceptar";
				} else if (carta.equals("Rechazado")) {
					codigo = "carta-rechazo";
				} else {
					codigo = "carta-rechazo";
				}
				
				Map<String, Object> columns = new LinkedHashMap<String, Object>();
				
				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("persistenceid")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					} else if (metaData.getColumnLabel(i).toLowerCase().equals("seleccionado")) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getBoolean(i));
					} else {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
				}
				
				LocalDate local = LocalDate.now(); 
				columns.put("fechaEnvio", local);
				String jsonCorreo = '{"campus":"' + campusCorreo + '","correo":"' + correo +  '","codigo":"' + codigo + '","isEnviar":true}"';
				Result resultadoEnvioCarta = nDAO.generateHtml(parameterP, parameterC, jsonCorreo, context);
				Map<String, Object> contract = new LinkedHashMap<String, Object>();
				contract.put("isTransferencia", false);
				contract.put("infoCartaInput", columns);
				errorlog = columns.get("numerodematricula") +  "||" + contract.toString() +  "||" + context.toString();
				
				String correoLog = "";
				if(resultadoEnvioCarta.isSuccess()) {
//					Result resultadoEjecucionTarea = ejecutarCargaResultado(columns.get("numerodematricula"), columns, context);
//					if(resultadoEjecucionTarea.isSuccess()) {
//						Map<String, Object> correoEnviado = new LinkedHashMap<String, Object>();
//						correoLog += "Se envió la carta";
//						correoEnviado.put("correo", correo);
//						correoEnviado.put("correoLog", correoLog);
//						correoEnviado.put("error", false);
//						lstEnviados.add(correoEnviado);
//					} else {
//						Map<String, Object> correoError = new LinkedHashMap<String, Object>();
//						correoError.put("correo", correo);
//						correoError.put("error", resultadoEjecucionTarea.getError());
//						correoError.put("errorInfo", "IDBANNER: " + columns.get("numerodematricula"));
//						lstNoEnviados.add(correo);
//					}
					
					Map<String, Object> correoEnviado = new LinkedHashMap<String, Object>();
					correoLog += "Se envió la carta";
					correoEnviado.put("correo", correo);
					correoEnviado.put("correoLog", correoLog);
					correoEnviado.put("error", false);
					lstEnviados.add(correoEnviado);
				} else {
					correoLog += "No se envió la carta";
					Map<String, Object> correoError = new LinkedHashMap<String, Object>();
					correoError.put("correo", correo);
					correoError.put("correoLog", resultadoEnvioCarta.getError());
					correoError.put("error", true);
					lstNoEnviados.add(correoError);
				}
				
//				Result resultadoEjecucionTarea = ejecutarCargaResultado(columns.get("numerodematricula"), contract, context);
//				
//				if(resultadoEjecucionTarea.isSuccess()) {
//					lstEnviados.add(correo);
//				} else {
//					Map<String, Object> correoError = new LinkedHashMap<String, Object>();
//					correoError.put("correo", correo);
//					correoError.put("error", resultadoEjecucionTarea.getError());
//					correoError.put("errorInfo", "IDBANNER: " + columns.get("numerodematricula"));
//					lstNoEnviados.add(correoError);
//				}
			}
			
			lstData.add(lstEnviados);
			lstData.add(lstNoEnviados);
			
			resultado.setData(lstData);
			resultado.setSuccess(true);
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			resultado.setError_info(errorlog)
			//resultado.setError_info(consulta)
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}	
	
	public Result ejecutarCargaResultado(String idBanner, Map<String, Serializable> contract, RestAPIContext context) {
		Result resultado = new Result();
		List<DetalleSolicitud> lstResultado = new ArrayList<DetalleSolicitud>();
		
		try {
			String username = "";
			String password = "";
			Properties prop = new Properties();
			String propFileName = "configuration.properties";
			InputStream inputStream;
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			username = prop.getProperty("USERNAME");
			password = prop.getProperty("PASSWORD");
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
			def objDetalleSolicitudDAO = context.getApiClient().getDAO(DetalleSolicitudDAO.class);
			List<DetalleSolicitud> detalleSolicitud = objDetalleSolicitudDAO.findByIdBanner(idBanner, 0, 1);
			apiClient.login(username, password);
			String caseId = detalleSolicitud.get(0).caseId;
			def startedBy = apiClient.getProcessAPI().getProcessInstance(Integer.parseInt(caseId)).startedBy;
			Long taskID = apiClient.processAPI.getHumanTaskInstances(Long.valueOf(caseId), "Carga y consulta de resultados", 0, 1).get(0).getId();
//			apiClient.processAPI.executeUserTask(startedBy, taskID, contract);
			apiClient.processAPI.assignAndExecuteUserTask(startedBy, taskID, contract);
			resultado.setSuccess(true);
		}catch(Exception ex) {
			LOGGER.error ex.getMessage()
			resultado.setSuccess(false)
			resultado.setError(ex.getMessage())
		}
		
		return resultado;
	}
}