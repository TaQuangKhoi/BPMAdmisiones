package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.RowId
import java.sql.Statement
import java.text.DateFormat
import java.text.SimpleDateFormat

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.IndexedColorMap
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.commons.codec.binary.Hex;

import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.DB.DBConnect

import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper

class BecasDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ListadoDAO.class);
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
	
	
	public Result getPlantillaHermanos() {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			String consulta = "select * from plantillaHermanos where CAST(fecharegistro as DATE) = (CAST(TO_CHAR(NOW(),'YYYY-MM-DD') as DATE) - integer '1')"
			List<String> rows = new ArrayList<String>();
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
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
			resultado.setError_info(" errorLog = "+errorLog)
			resultado.setData(rows)
			resultado.setSuccess(true)
			resultado.setTotalRegistros(columnCount)
		}catch(Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog+" "+e.getMessage())
		}
		finally{
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getPlantillaRegistro() {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			String consulta = "select * from plantillaRegistro where CAST(fecharegistro as DATE) = (CAST(TO_CHAR(NOW(),'YYYY-MM-DD') as DATE) - integer '1')"
			List<String> rows = new ArrayList<String>();
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
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
			resultado.setError_info(" errorLog = "+errorLog)
			resultado.setData(rows)
			resultado.setSuccess(true)
			resultado.setTotalRegistros(columnCount)
		}catch(Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog+" "+e.getMessage())
		}
		finally{
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result excelPlantillaRegistro() {
		Result resultado = new Result();
		String errorLog = "";
		
		try {
			Result dataResult = new Result();
			int rowCount = 0;
			List<Object> lstParams;
			//String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("plantillaregistros");
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
			style.setAlignment(HorizontalAlignment.CENTER)
			//color
			IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
			XSSFColor  color = new XSSFColor(new java.awt.Color(191,220,249),colorMap);
			
			style.setFillForegroundColor(color)
			
			dataResult = getPlantillaRegistro();
			int countColumns = 0;
			if (dataResult.success) {
				lstParams = dataResult.getData();
				countColumns = dataResult.getTotalRegistros();
			} else {
				throw new Exception("No encontro datos");
			}
			
			def titulos = ["#","EXPEDIENTE","NOMBRE","SEGUNDO NOMBRE","APELL PATERNO","APELL MATERNO","CAMPUS","ESTADO","MUNICIPIO","PAIS","CORREO","CONTRASEÑA","SEXO","FECHA NACI","NACIONALIDAD","RELIGION","CURP","CELULAR","AVATAR","ESTCICIL","CCALLE","NUMEXT","NUMINT","COLONIA","CP","TEL-OTRO","PAA","UNIVERSIDAD","LICENCIATURA","PERIODO INGRESO","OTRA UNIVERSIDAD","PREPA","PREPA PAIS","PREPA ESTADO","PREPA CIUDAD","PREPA PROMEDIO","TUTOR TITULO","TUTOR NOMBRE","TUTOR APELLIDO","TUTOR EGRESADO","TUTOR UNI EGRESADO","TUTOR ESCOLARIDAD","TUTOR PARENTESCO","TRABAJO TUTOR","TUTOR EMPRESA","TUTOR GIRO EMPRESA","TUTOR PUESTO","TUTOR CALLE","TUTOR NUM EXT","TUTOR NUM INT","TUTOR COLONIA","TUTOR CP","TUTOR MUNICIPIO","TUTOR PAIS","TUTOR ESTADO","TUTOR CELULAR","PADRE TITULO","PADRE NOMBRE","PADRE APELLIDOS","PADRE VIVO","PADRE EGRESADO","PADRE UNI EGRESO","PADRE ESCOLARIDAD","PADRE TUTOR","PADRE CORREO","PADRE TRABAJA","PADRE EMPRESA","PADRE GIRO EMPRESA","PADRE","PADRE CALLE","PADRE NUM EXT","PADRE NUM INT","PADRE COLONIA","PADRE CP","PADRE MUNICIPIO","PADRE PAIS","PADRE ESTADO","PADRE CEL","MADRE TITULO","MADRE NOMBRE","MADRE APELLIDO","MADRE VIVE","MADRE EGRESADA","MADRE UNI EGRESADA","MADRE CORREO","MADRE ESCOL","MADRE CALLE","MADRE NUM EXT","MADRE NUM INT","MADRE COLONIA","MADRE CP","MADRE MUNICIPIO","MADRE PAIS","MADRE ESTADO","MADRE CEL","EMERGENCIA","EMERGENCIA NOMBRE","EMERGENCIA TELEFONO","EMERGENCIA CELULAR","CON QUIEN VIVES","MADRE VIVE","ESTADO PADRES"]
			Row headersRow = sheet.createRow(rowCount);
			++rowCount;
			List<Cell> header = new ArrayList<Cell>();
			for(int i = 0; i < titulos.size(); ++i) {
				header.add(headersRow.createCell(i))
				header[i].setCellValue(titulos.get(i))
				header[i].setCellStyle(style)
			}
			
			CellStyle bodyStyle = workbook.createCellStyle();
			bodyStyle.setWrapText(true);
			bodyStyle.setAlignment(HorizontalAlignment.CENTER);
			
			
			def info = ["nregistro","expediente","primernombre","segundonombre","apellidopaterno","apellidomaterno","campus","estado","municipio","pais","correo","contrasena","sexo","fechanacimiento","nacionalidad","religion","curp","celular","foto","estadocivil","casacalle","numext","numint","colonia","cp","otrotelefono","puntajepaa","universidad","licenciatura","periodoingreso","otrauniversidad","preparatoria","prepapais","prepaestado","prepaciudad","prepapromedio","tutortitulo","tutornombre","tutorapellido","tutoregresado","tutoruniegresado","tutorescolaridad","tutorparentesco","tutortrabaja","tutorempresa","tutorgiroempresa","tutorpuesto","tutorcalle","tutornumext","tutornumint","tutorcolonia","tutorcp","tutormunicipio","tutorpais","tutorestado","tutorcelular","padretitulo","padrenombre","padreapellido","padrevivo","padreegresado","padreuniegresado","padreescolaridad","padretutor","padrecorreo","padretrabaja","padreempresa","padregiroempresa","padrepuesto","padrecalle","padrenumext","padrenumint","padrecolonia","padrecp","padremunicipio","padrepais","padreestado","padrecel","madretitulo","madrenombre","madreapellido","madrevive","madreegresada","madreuniegresada","madrecorreo","madreescolaridad","madrecalle","madrenumext","madrenumint","madrecolonia","madrecp","madremunicipio","madrepais","madreestado","madrecel","emergencia","emergencianombre","emergenciatelefono","emergenciacelular","conquienvives","madrevive","estadopadres"]
			List<Cell> body;
			for (int i = 0; i < lstParams.size(); ++i){
				Row row = sheet.createRow(rowCount);
				++rowCount;
				body = new ArrayList<Cell>()
				for(int j=0;  j < info.size(); ++j) {
					body.add(row.createCell(j))
					body[j].setCellValue(lstParams[i][info.get(j)])
					body[j].setCellStyle(bodyStyle);
					
				}
			}
			
			for(int i=0; i<=15; ++i) {
				sheet.autoSizeColumn(i);
			}
			
			FileOutputStream outputStream = new FileOutputStream("PlantillaRegistro.xls");
			workbook.write(outputStream);
		
			List<Object> lstResultado = new ArrayList<Object>();
			lstResultado.add(encodeFileToBase64Binary("PlantillaRegistro.xls"));
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
			resultado.setError_info(errorLog);
			
		}catch(Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	public Result excelPlantillaHermanos() {
		Result resultado = new Result();
		String errorLog = "";
		
		try {
			Result dataResult = new Result();
			int rowCount = 0;
			List<Object> lstParams;
			//String type = object.type;
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("plantillahermanos");
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setBold(true);
			style.setFont(font);
			style.setAlignment(HorizontalAlignment.CENTER)
			//color
			IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
			XSSFColor  color = new XSSFColor(new java.awt.Color(191,220,249),colorMap);
			
			style.setFillForegroundColor(color)
			
			dataResult = getPlantillaHermanos();
			int countColumns = 0;
			if (dataResult.success) {
				lstParams = dataResult.getData();
				countColumns = dataResult.getTotalRegistros();
			} else {
				throw new Exception("No encontro datos");
			}
			
			def titulos = ["#","NOMBRE","SEGUNDO NOMBRE","APELL PATERNO","APELL MATERNO","CAMPUS","EXPEDIENTE","ID REG","NOMBRE HERMANO","APELLIDO HERMANO","FECHA NAC HERMANO","ESTUDIO HERMANO","INSTITUCIÓN HERMANO","TRABAJO HERMANO","EMPRESA HERMANO"]
			Row headersRow = sheet.createRow(rowCount);
			++rowCount;
			List<Cell> header = new ArrayList<Cell>();
			for(int i = 0; i < titulos.size(); ++i) {
				header.add(headersRow.createCell(i))
				header[i].setCellValue(titulos.get(i))
				header[i].setCellStyle(style)
			}
			
			CellStyle bodyStyle = workbook.createCellStyle();
			bodyStyle.setWrapText(true);
			bodyStyle.setAlignment(HorizontalAlignment.CENTER);
			
			def info = ["idreg","primernombre","segundonombre","apellidopaterno","apellidomaterno","campus","expediente","idreg","nombrehermano","apellidohermano","fechanacimientohermano","estudiohermano","institucionhermano","trabajohermano","empresahermano"]
			List<Cell> body;
			for (int i = 0; i < lstParams.size(); ++i){
				Row row = sheet.createRow(rowCount);
				++rowCount;
				body = new ArrayList<Cell>()
				for(int j=0;  j < info.size(); ++j) {
					body.add(row.createCell(j))
					body[j].setCellValue(lstParams[i][info.get(j)])
					body[j].setCellStyle(bodyStyle);
					
				}
			}
			
			for(int i=0; i<=15; ++i) {
				sheet.autoSizeColumn(i);
			}
			
			FileOutputStream outputStream = new FileOutputStream("PlantillaHermanos.xls");
			workbook.write(outputStream);
		
			List<Object> lstResultado = new ArrayList<Object>();
			lstResultado.add(encodeFileToBase64Binary("PlantillaHermanos.xls"));
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
			resultado.setError_info(errorLog);
			
		}catch(Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
			e.printStackTrace();
		}
		
		return resultado;
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
		byte[] bytes = new byte[(int)length];
		
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
			   && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}
		
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "+file.getName());
		}
		
		is.close();
		return bytes;
	}
	
}
