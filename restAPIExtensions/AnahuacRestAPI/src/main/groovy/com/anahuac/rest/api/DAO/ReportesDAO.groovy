package com.anahuac.rest.api.DAO

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result
import groovy.json.JsonSlurper
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.SimpleDateFormat

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.IndexedColorMap
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bonitasoft.engine.identity.UserMembership
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ReportesDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportesDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	public Result generarReporte(String jsonData) {
		Result resultado = new Result();
		String errorLog = "", where = "";
		Boolean closeCon = false;
		try {
			Result dataResult = new Result();
			int rowCount = 0;
			List<Object> lstParams;
			//String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Registros");
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
			style.setAlignment(HorizontalAlignment.CENTER)
			//color
			IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
			XSSFColor  color = new XSSFColor(new java.awt.Color(191,220,249),colorMap);
			
			style.setFillForegroundColor(color)
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			where += " WHERE sda.catcampus_pid="+object.campus
			where += (object.periodo==null || object.periodo.equals(""))?"": " AND sda.catperiodo_pid in ("+object.periodo +")"
			where += (object.carrera==null|| object.carrera.equals(""))?"":" AND sda.catgestionescolar_pid in ("+object.carrera+")"
			where += (object.preparatoria==null|| object.preparatoria.equals(""))?"":" AND sda.catbachilleratos_pid in ("+object.preparatoria+")"
			where += (object.sesion==null|| object.sesion.equals(""))? "":" AND s.persistenceid in ("+object.sesion+")"
			where += (object.idbanner==null|| object.idbanner.equals(""))? "":" AND cda.idbanner = '"+object.idbanner+"'"
			
			
			
			String consulta= "SELECT case when cr.segundonombre='' then cr.primernombre else cr.primernombre || ' ' || cr.segundonombre end as nombre, cr.apellidopaterno as apaterno,cr.apellidomaterno as amaterno,'' as email,cc.clave || cda.idbanner as usuario,sda.fechanacimiento as clave, '' as edad, cda.idbanner as matricula, '' as curp, s.nombre as sesion, to_char(p.aplicacion, 'DD/MM/YYYY') as fecha_examen, cc.clave as campusVPD FROM catregistro cr inner join DETALLESOLICITUD cda on cda.caseid::bigint=cr.caseid inner join solicituddeadmision sda on sda.caseid=cda.caseid::bigint inner join catcampus cc on cc.persistenceid=sda.catcampusestudio_pid inner join sesionaspirante sa on sa.username=sda.correoelectronico inner join pruebas p on sa.sesiones_pid=p.sesion_pid and p.cattipoprueba_pid=4 inner join sesiones s on s.persistenceid=sa.sesiones_pid " + where
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta)
			
			
			rs = pstm.executeQuery()
			rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			dataResult.setData(rows)
			dataResult.setSuccess(true)
			if (dataResult.success) {
				lstParams = dataResult.getData();
				
			} else {
				throw new Exception("No encontro datos");
			}
			FileOutputStream fw=new FileOutputStream("out.txt");    
			
			   
				   
			def titulos = ["nombre","apaterno","amaterno","email","usuario","clave","edad","matrícula","curp","sesión","fecha examen","campus(VPD)"]
			def titulosRua = ["nombre","apaterno","amaterno","email","usuario","clave","edad","matricula","curp","sesión","fecha examen","campus(VPD)"]
			if(object.encabezado) {
				fw.write((titulosRua.join(",")+"\r\n").getBytes());		
				Row headersRow = sheet.createRow(rowCount);
				++rowCount;
				List<Cell> header = new ArrayList<Cell>();
				for(int i = 0; i < titulos.size(); ++i) {
					header.add(headersRow.createCell(i))
					header[i].setCellValue(titulos.get(i))
					header[i].setCellStyle(style)
				}
			}
			CellStyle bodyStyle = workbook.createCellStyle();
			bodyStyle.setWrapText(true);
			bodyStyle.setAlignment(HorizontalAlignment.CENTER);
			SimpleDateFormat sdfin = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
			SimpleDateFormat sdfin2 = new SimpleDateFormat("yyyy-MM-dd't'HH:mm:ss.SSS")
			SimpleDateFormat sdfout = new SimpleDateFormat("yyyy/MM/dd")
			
			def info = ["nombre","apaterno","amaterno","email","usuario","clave","edad","matricula","curp","sesion","fecha_examen","campusvpd"]
			List<Cell> body;
			String line=""
			for (int i = 0; i < lstParams.size(); ++i){
				line=""
				Row row = sheet.createRow(rowCount);
				++rowCount;
				body = new ArrayList<Cell>()
				for(int j=0;  j < info.size(); ++j) {
					body.add(row.createCell(j))
					try {
						body[j].setCellValue((info.get(j).equals("clave"))?sdfout.format(sdfin.parse(lstParams[i][info.get(j)])):lstParams[i][info.get(j)])
						line+=(info.get(j).equals("clave")?sdfout.format(sdfin.parse(lstParams[i][info.get(j)])):lstParams[i][info.get(j)])+((info.get(j).equals("campusvpd"))?"\r\n":",")
					}catch(Exception e) {
						body[j].setCellValue((info.get(j).equals("clave"))?sdfout.format(sdfin2.parse(lstParams[i][info.get(j)])):lstParams[i][info.get(j)])
						line+=(info.get(j).equals("clave")?sdfout.format(sdfin2.parse(lstParams[i][info.get(j)])):lstParams[i][info.get(j)])+((info.get(j).equals("campusvpd"))?"\r\n":",")
					}
					
					body[j].setCellStyle(bodyStyle);
					
				}
				fw.write(line.getBytes());
			}
			
			if(lstParams.size()>0) {
				for(int i=0; i<=138; ++i) {
					sheet.autoSizeColumn(i);
				}
				String fecha = lstParams[0]["fecharegistro"].toString()+"";
				String nameFile = "Reporte-"+fecha+".xls";
				FileOutputStream outputStream = new FileOutputStream(nameFile);
				workbook.write(outputStream);
				List < Object > lstResultado = new ArrayList < Object > ();
				lstResultado.add(encodeFileToBase64Binary(nameFile));
				lstResultado.add(encodeFileToBase64Binary("out.txt"))
				resultado.setData(lstResultado)
				outputStream.close();
				fw.close();
					
				} else {
					throw new Exception("No encontro datos:"+errorLog+dataResult.getError());
				}
				
			
			
			
			//List<Object> lstResultado = new ArrayList<Object>();
			//lstResultado.add(encodeFileToBase64Binary(nameFile));
			
			resultado.setSuccess(true);
			//resultado.setData(lstResultado);
			resultado.setError_info(errorLog);
			
		}catch(Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
			e.printStackTrace();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}
	private writeFile2() throws IOException {
		FileWriter fw = new FileWriter("out.txt");
	 
		for (int i = 0; i < 10; i++) {
			fw.write("something");
		}
	 
		fw.close();
	}
	private String encodeFileToBase64Binary(String fileName)
	throws IOException {

		File file = new File(fileName);
		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encoder.encode(bytes);
		String encodedString = new String(encoded);

		return encodedString;
	}
	private static byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		is.close();
		return bytes;
	}
	public Boolean validarConexion() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno = true
		}
		return retorno
	}
	public Result generarReporteResultadosExamenes(String jsonData) {
		Result resultado = new Result();
		String errorLog = "", where = "";
		Boolean closeCon = false;
		try {
			Result dataResult = new Result();
			int rowCount = 0;
			List<Object> lstParams;
			//String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Registros");
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
			style.setAlignment(HorizontalAlignment.CENTER)
			//color
			IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
			XSSFColor  color = new XSSFColor(new java.awt.Color(191,220,249),colorMap);
			
			style.setFillForegroundColor(color)
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			where += " WHERE sda.catcampus_pid="+object.campus
			where += (object.periodo==null || object.periodo.equals(""))?"": " AND sda.catperiodo_pid in ("+object.periodo +")"
			where += (object.carrera==null|| object.carrera.equals(""))?"":" AND sda.catgestionescolar_pid in ("+object.carrera+")"
			where += (object.preparatoria==null|| object.preparatoria.equals(""))?"":" AND sda.catbachilleratos_pid in ("+object.preparatoria+")"
			where += (object.sesion==null|| object.sesion.equals(""))? "":" AND s.persistenceid in ("+object.sesion+")"
			where += (object.idbanner==null|| object.idbanner.equals(""))? "":" AND cda.idbanner = '"+object.idbanner+"'"
			
			
			String consulta= "SELECT campus.descripcion universidad,periodo.clave periodo,cda.idbanner numerodematricula,case when cr.segundonombre='' then cr.primernombre ||' '|| case when cr.apellidomaterno='' then cr.apellidopaterno else cr.apellidopaterno||' '||cr.apellidomaterno end else cr.primernombre || ' ' || cr.segundonombre ||' '|| case when cr.apellidomaterno='' then cr.apellidopaterno else cr.apellidopaterno||' '||cr.apellidomaterno end end as nombre,gestionescolar.nombre as carrera,preparatoria.descripcion preparatoriadeprocedencia,religion.descripcion religion,sexo.descripcion sexo,tipoadmision.descripcion tipodeadmision, s.nombre as sesion,sda.promediogeneral promedio,importacionpa.invp invp,ImportacionPA.paav paaverbal,ImportacionPA.lexiumPaav clex,ImportacionPA.paan paanumerica,ImportacionPA.lexiumPaan mlex,ImportacionPA.para para,ImportacionPA.lexiumPara hlex,ImportacionPA.total paa,infocarta.pdp,infocarta.pdu,infocarta.sse,infocarta.pcda,infocarta.pca,campusingreso.descripcion  campusingreso,tipoalumno.descripcion tipodeestudiante, sda.curp as curp, '' decisiondeadmision,'' cpdp,'' cpdu,'' csse,'' cpcda,'' cpca,'' observaciones FROM catregistro cr inner join DETALLESOLICITUD cda on cda.caseid::bigint=cr.caseid inner join solicituddeadmision sda on sda.caseid=cda.caseid::bigint inner join catcampus cc on cc.persistenceid=sda.catcampusestudio_pid inner join sesionaspirante sa on sa.username=sda.correoelectronico inner join pruebas p on sa.sesiones_pid=p.sesion_pid and p.cattipoprueba_pid=4 inner join sesiones s on s.persistenceid=sa.sesiones_pid INNER JOIN catcampus campus on campus.persistenceid=sda.catcampus_pid inner join catperiodo periodo on sda.catPeriodo_pid=periodo.persistenceid inner join catgestionescolar gestionescolar  on sda.catgestionescolar_pid=gestionescolar.persistenceid inner join catbachilleratos preparatoria on sda.catbachilleratos_pid=preparatoria.persistenceid inner join autodescripcion autodescripcion on autodescripcion.caseid=sda.caseid inner join catreligion religion on religion.persistenceid=autodescripcion.catreligion_pid inner join catsexo sexo on sexo.persistenceid=sda.catsexo_pid inner join cattipoadmision tipoadmision on cattipoadmision_pid=tipoadmision.persistenceid inner join catcampus campusingreso on campusingreso.persistenceid=catcampusestudio_pid inner join cattipoalumno tipoalumno on tipoalumno.persistenceid=cda.cattipoalumno_pid left join importacionpaa importacionpa on importacionpa.idbanner=cda.idbanner left join infocarta infocarta on infocarta.numerodematricula=cda.idbanner " + where
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta)
			
			
			rs = pstm.executeQuery()
			rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			errorLog+="|Consulta:"+consulta +"|"
			dataResult.setData(rows)
			dataResult.setSuccess(true)
			if (dataResult.success) {
				lstParams = dataResult.getData();
				errorLog+="|Parametros:"+lstParams.toString() +"|"
			} else {
				throw new Exception("No encontro datos");
			}
			FileOutputStream fw=new FileOutputStream("out.txt");
			
			   
				   
			def titulos = ["universidad","periodo","número de matrícula","nombre","carrera","preparatoria de procedencia","religión","sexo","tipo de admisión","sesión","promedio","invp","paa verbal","clex","paa numérica","mlex","para","hlex","paa","pdp","pdu","sse","pcda","pca","campus ingreso","tipo de estudiante","curp","decisión de admisión","pdp","pdu","sse","pcda","pca","observaciones"]
			if(object.encabezado) {
				fw.write((titulos.join(",")+"\r\n").getBytes());
				Row headersRow = sheet.createRow(rowCount);
				++rowCount;
				List<Cell> header = new ArrayList<Cell>();
				for(int i = 0; i < titulos.size(); ++i) {
					header.add(headersRow.createCell(i))
					header[i].setCellValue(titulos.get(i))
					header[i].setCellStyle(style)
				}
			}
			CellStyle bodyStyle = workbook.createCellStyle();
			bodyStyle.setWrapText(true);
			bodyStyle.setAlignment(HorizontalAlignment.CENTER);
			
			def info = ["universidad","periodo","numerodematricula","nombre","carrera","preparatoriadeprocedencia","religion","sexo","tipodeadmision","sesion","promedio","invp","paaverbal","clex","paanumerica","mlex","para","hlex","paa","pdp","pdu","sse","pcda","pca","campusingreso","tipodeestudiante","curp","decisiondeadmision","cpdp","cpdu","csse","cpcda","cpca","observaciones"]
			List<Cell> body;
			String line=""
			for (int i = 0; i < lstParams.size(); ++i){
				line=""
				Row row = sheet.createRow(rowCount);
				++rowCount;
				body = new ArrayList<Cell>()
				for(int j=0;  j < info.size(); ++j) {
					body.add(row.createCell(j))
					try {
						body[j].setCellValue(lstParams[i][info.get(j)])
						line+=(lstParams[i][info.get(j)])+((info.get(j).equals("observaciones"))?"\r\n":",")
					}catch(Exception e) {
						body[j].setCellValue(lstParams[i][info.get(j)])
						line+=(lstParams[i][info.get(j)])+((info.get(j).equals("observaciones"))?"\r\n":",")
					}
					
					body[j].setCellStyle(bodyStyle);
					
				}
				fw.write(line.getBytes());
			}
			
			if(lstParams.size()>0) {
				for(int i=0; i<=138; ++i) {
					sheet.autoSizeColumn(i);
				}
				String fecha = lstParams[0]["fecharegistro"].toString()+"";
				String nameFile = "Reporte-"+fecha+".xls";
				FileOutputStream outputStream = new FileOutputStream(nameFile);
				workbook.write(outputStream);
				List < Object > lstResultado = new ArrayList < Object > ();
				lstResultado.add(encodeFileToBase64Binary(nameFile));
				lstResultado.add(encodeFileToBase64Binary("out.txt"))
				resultado.setData(lstResultado)
				outputStream.close();
				fw.close();
					
				} else {
					throw new Exception("No encontro datos:"+errorLog+dataResult.getError());
				}
				
			
			
			
			//List<Object> lstResultado = new ArrayList<Object>();
			//lstResultado.add(encodeFileToBase64Binary(nameFile));
			
			resultado.setSuccess(true);
			//resultado.setData(lstResultado);
			resultado.setError_info(errorLog);
			
		}catch(Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
			e.printStackTrace();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}

	public Result generarReporteAdmitidosPropedeutico(String jsonData) {
		Result resultado = new Result();
		String errorLog = "", where = "";
		Boolean closeCon = false;
		try {
			Result dataResult = new Result();
			int rowCount = 0;
			List<Object> lstParams;
			//String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Registros");
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
			style.setAlignment(HorizontalAlignment.CENTER)
			//color
			IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
			XSSFColor  color = new XSSFColor(new java.awt.Color(191,220,249),colorMap);
			
			style.setFillForegroundColor(color)
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			where += " WHERE sda.catcampus_pid="+object.campus
			where += (object.periodo==null || object.periodo.equals(""))?"": " AND sda.catperiodo_pid in ("+object.periodo +")"
			where += (object.carrera==null|| object.carrera.equals(""))?"":" AND sda.catgestionescolar_pid in ("+object.carrera+")"
			where += (object.preparatoria==null|| object.preparatoria.equals(""))?"":" AND sda.catbachilleratos_pid in ("+object.preparatoria+")"
			where += (object.idbanner==null|| object.idbanner.equals(""))? "":" AND cda.idbanner = '"+object.idbanner+"'"
			
			
			String consulta= "SELECT case when cr.segundonombre='' then cr.primernombre ||' '|| case when cr.apellidomaterno='' then cr.apellidopaterno else cr.apellidopaterno||' '||cr.apellidomaterno end else cr.primernombre || ' ' || cr.segundonombre ||' '|| case when cr.apellidomaterno='' then cr.apellidopaterno else cr.apellidopaterno||' '||cr.apellidomaterno end end as nombre,cda.idbanner nomatricula,sda.correoelectronico email, sda.telefono,sda.telefonocelular celular,sda.ciudad,preparatoria.descripcion preparatoria,sda.promediogeneral promedio,gestionescolar.clave as clavecarrera,cp.descripcion propedeutico,tipoadmision.descripcion tipodeadmision, 'x' solicitudadmision,'x' pagoadmision,'x' autodescripcion,'x' examenadmision,'x' esperandoresultados,'x' resultadoadmision,sda.estatussolicitud,'25-mar-21' fechaadmision,'' pagocurso, tipoalumno.descripcion tipoestudiante FROM catregistro cr inner join DETALLESOLICITUD cda on cda.caseid::bigint=cr.caseid inner join solicituddeadmision sda on sda.caseid=cda.caseid::bigint inner join catcampus cc on cc.persistenceid=sda.catcampusestudio_pid inner join sesionaspirante sa on sa.username=sda.correoelectronico INNER JOIN catcampus campus on campus.persistenceid=sda.catcampus_pid inner join catperiodo periodo on sda.catPeriodo_pid=periodo.persistenceid inner join catgestionescolar gestionescolar  on sda.catgestionescolar_pid=gestionescolar.persistenceid inner join catbachilleratos preparatoria on sda.catbachilleratos_pid=preparatoria.persistenceid inner join autodescripcion autodescripcion on autodescripcion.caseid=sda.caseid inner join catreligion religion on religion.persistenceid=autodescripcion.catreligion_pid inner join catsexo sexo on sexo.persistenceid=sda.catsexo_pid inner join cattipoadmision tipoadmision on cattipoadmision_pid=tipoadmision.persistenceid inner join catcampus campusingreso on campusingreso.persistenceid=catcampusestudio_pid inner join cattipoalumno tipoalumno on tipoalumno.persistenceid=cda.cattipoalumno_pid inner join catpropedeutico cp on cp.persistenceid=sda.catpropedeutico_pid " + where
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta)
			
			
			rs = pstm.executeQuery()
			rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			errorLog+="|Consulta:"+consulta +"|"
			dataResult.setData(rows)
			dataResult.setSuccess(true)
			if (dataResult.success) {
				lstParams = dataResult.getData();
				errorLog+="|Parametros:"+lstParams.toString() +"|"
			} else {
				throw new Exception("No encontro datos");
			}
			FileOutputStream fw=new FileOutputStream("out.txt");
			
			   
				   
			def titulos = ["Nombre","No. Matrícula","Email","Teléfonos","Celular","Ciudad","Preparatoria","Promedio","Clave carrera","Nombre de propedeutico","Tipo Admisión","Solicitud Admisión","Pago de Admisión","Examen Admisión","Resultado de admisión","Fecha admisión","Pago curso","Tipo de Estudiante"]
			if(object.encabezado) {
				fw.write((titulos.join(",")+"\r\n").getBytes());
				Row headersRow = sheet.createRow(rowCount);
				++rowCount;
				List<Cell> header = new ArrayList<Cell>();
				for(int i = 0; i < titulos.size(); ++i) {
					header.add(headersRow.createCell(i))
					header[i].setCellValue(titulos.get(i))
					header[i].setCellStyle(style)
				}
			}
			CellStyle bodyStyle = workbook.createCellStyle();
			bodyStyle.setWrapText(true);
			bodyStyle.setAlignment(HorizontalAlignment.CENTER);
			
			def info = ["nombre","nomatricula","email","telefono","celular","ciudad","preparatoria","promedio","clavecarrera","propedeutico","tipodeadmision","solicitudadmision","pagoadmision","autodescripcion","examenadmision","esperandoresultados","resultadoadmision","fechaadmision","pagocurso","tipoestudiante"]
			List<Cell> body;
			String line=""
			for (int i = 0; i < lstParams.size(); ++i){
				line=""
				Row row = sheet.createRow(rowCount);
				++rowCount;
				body = new ArrayList<Cell>()
				for(int j=0;  j < info.size(); ++j) {
					body.add(row.createCell(j))
					try {
						body[j].setCellValue(lstParams[i][info.get(j)])
						line+=(lstParams[i][info.get(j)])+((info.get(j).equals("observaciones"))?"\r\n":",")
					}catch(Exception e) {
						body[j].setCellValue(lstParams[i][info.get(j)])
						line+=(lstParams[i][info.get(j)])+((info.get(j).equals("observaciones"))?"\r\n":",")
					}
					
					body[j].setCellStyle(bodyStyle);
					
				}
				fw.write(line.getBytes());
			}
			
			if(lstParams.size()>0) {
				for(int i=0; i<=138; ++i) {
					sheet.autoSizeColumn(i);
				}
				String fecha = lstParams[0]["fecharegistro"].toString()+"";
				String nameFile = "Reporte-"+fecha+".xls";
				FileOutputStream outputStream = new FileOutputStream(nameFile);
				workbook.write(outputStream);
				List < Object > lstResultado = new ArrayList < Object > ();
				lstResultado.add(encodeFileToBase64Binary(nameFile));
				lstResultado.add(encodeFileToBase64Binary("out.txt"))
				resultado.setData(lstResultado)
				outputStream.close();
				fw.close();
					
				} else {
					throw new Exception("No encontro datos:"+errorLog+dataResult.getError());
				}
				
			
			
			
			//List<Object> lstResultado = new ArrayList<Object>();
			//lstResultado.add(encodeFileToBase64Binary(nameFile));
			
			resultado.setSuccess(true);
			//resultado.setData(lstResultado);
			resultado.setError_info(errorLog);
			
		}catch(Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
			e.printStackTrace();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}

	public Result generarReporteDatosFamiliares(String jsonData) {
		Result resultado = new Result();
		String errorLog = "", where = "";
		Boolean closeCon = false;
		try {
			Result dataResult = new Result();
			int rowCount = 0;
			List<Object> lstParams;
			//String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Registros");
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
			style.setAlignment(HorizontalAlignment.CENTER)
			//color
			IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
			XSSFColor  color = new XSSFColor(new java.awt.Color(191,220,249),colorMap);
			
			style.setFillForegroundColor(color)
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			where += " WHERE sda.catcampus_pid="+object.campus
			where += (object.periodo==null || object.periodo.equals(""))?"": " AND sda.catperiodo_pid in ("+object.periodo +")"
			where += (object.idbanner==null|| object.idbanner.equals(""))? "":" AND cda.idbanner = '"+object.idbanner+"'"
			
			String consulta= "SELECT distinct cda.idbanner id,case when cr.segundonombre='' then cr.primernombre ||' '|| case when cr.apellidomaterno='' then cr.apellidopaterno else cr.apellidopaterno||' '||cr.apellidomaterno end else cr.primernombre || ' ' || cr.segundonombre ||' '|| case when cr.apellidomaterno='' then cr.apellidopaterno else cr.apellidopaterno||' '||cr.apellidomaterno end end as nombre, pt.nombre || ' '|| pt.apellidos nombepadres, cp.clave relacion, pt.empresatrabaja as empleador, pt.puesto as titulo, pt.correoelectronico as correo, pt.calle ||' #' || pt.numeroexterior || ' '|| pt.colonia ||', '||ce.descripcion || ' ' ||pt.ciudad || ' ' || pt.codigopostal direccion, pt.telefono, 'AD Admitido' as codigodedecision FROM catregistro cr inner join DETALLESOLICITUD cda on cda.caseid::bigint=cr.caseid inner join solicituddeadmision sda on sda.caseid=cda.caseid::bigint inner join padrestutor pt on pt.caseid=cda.caseid::bigint inner join CatParentesco cp on cp.persistenceid=pt.catparentezco_pid inner join catestados ce on ce.persistenceid=pt.catestado_pid " + where
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta)
			
			
			rs = pstm.executeQuery()
			rows = new ArrayList<Map<String, Object>>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			errorLog+="|Consulta:"+consulta +"|"
			dataResult.setData(rows)
			dataResult.setSuccess(true)
			if (dataResult.success) {
				lstParams = dataResult.getData();
				errorLog+="|Parametros:"+lstParams.toString() +"|"
			} else {
				throw new Exception("No encontro datos");
			}
			FileOutputStream fw=new FileOutputStream("out.txt");
			
			   
				   
			def titulos = ["ID",	"Nombre Alumno",	"Nombre de los Padres",	"Relación",	"Empleador",	"Titulo",	"Correo",	"Dirección",	"Teléfono",	"Código de Decisión"]
			if(object.encabezado) {
				fw.write((titulos.join(",")+"\r\n").getBytes());
				Row headersRow = sheet.createRow(rowCount);
				++rowCount;
				List<Cell> header = new ArrayList<Cell>();
				for(int i = 0; i < titulos.size(); ++i) {
					header.add(headersRow.createCell(i))
					header[i].setCellValue(titulos.get(i))
					header[i].setCellStyle(style)
				}
			}
			CellStyle bodyStyle = workbook.createCellStyle();
			bodyStyle.setWrapText(true);
			bodyStyle.setAlignment(HorizontalAlignment.CENTER);
			
			def info = ["id", "nombre", "nombepadres", "relacion", "empleador", "titulo", "correo", "direccion", "telefono", "codigodedecision"]
			List<Cell> body;
			String line=""
			for (int i = 0; i < lstParams.size(); ++i){
				line=""
				Row row = sheet.createRow(rowCount);
				++rowCount;
				body = new ArrayList<Cell>()
				for(int j=0;  j < info.size(); ++j) {
					body.add(row.createCell(j))
					try {
						body[j].setCellValue(lstParams[i][info.get(j)])
						line+=(lstParams[i][info.get(j)])+((info.get(j).equals("observaciones"))?"\r\n":",")
					}catch(Exception e) {
						body[j].setCellValue(lstParams[i][info.get(j)])
						line+=(lstParams[i][info.get(j)])+((info.get(j).equals("observaciones"))?"\r\n":",")
					}
					
					body[j].setCellStyle(bodyStyle);
					
				}
				fw.write(line.getBytes());
			}
			
			if(lstParams.size()>0) {
				for(int i=0; i<=138; ++i) {
					sheet.autoSizeColumn(i);
				}
				String fecha = lstParams[0]["fecharegistro"].toString()+"";
				String nameFile = "Reporte-"+fecha+".xls";
				FileOutputStream outputStream = new FileOutputStream(nameFile);
				workbook.write(outputStream);
				List < Object > lstResultado = new ArrayList < Object > ();
				lstResultado.add(encodeFileToBase64Binary(nameFile));
				lstResultado.add(encodeFileToBase64Binary("out.txt"))
				resultado.setData(lstResultado)
				outputStream.close();
				fw.close();
					
				} else {
					throw new Exception("No encontro datos:"+errorLog+dataResult.getError());
				}
				
			
			
			
			//List<Object> lstResultado = new ArrayList<Object>();
			//lstResultado.add(encodeFileToBase64Binary(nameFile));
			
			resultado.setSuccess(true);
			//resultado.setData(lstResultado);
			resultado.setError_info(errorLog);
			
		}catch(Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
			e.printStackTrace();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		
		return resultado;
	}

}
