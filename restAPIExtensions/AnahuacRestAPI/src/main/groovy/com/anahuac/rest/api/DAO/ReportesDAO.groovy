package com.anahuac.rest.api.DAO

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement

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
	public Result generarReporte() {
		Result resultado = new Result();
		String errorLog = "";
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
			String consulta= "SELECT cr.primernombre || ' ' || cr.segundonombre as nombre, cr.apellidopaterno as apaterno,cr.apellidomaterno as amaterno,sda.correoelectronico as email2,cc.clave || cda.idbanner as usuario,sda.fechanacimiento, '' as edad, cda.idbanner as matricula, '' as curp, 'H' as sesion, '22/04/2021' as fecha_examen, cc.clave as campusVPD FROM catregistro cr inner join DETALLESOLICITUD cda on cda.caseid::bigint=cr.caseid inner join solicituddeadmision sda on sda.caseid=cda.caseid::bigint inner join catcampus cc on cc.persistenceid=sda.catcampusestudio_pid"
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
			
			def titulos = ["nombre","apaterno","amaterno","email","usuario","clave","edad","matrícula","curp","sesión","fecha examen","campus(VPD)"]
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
			
			
			def info = ["nombre","apaterno","amaterno","email","usuario","clave","edad","matricula","curp","sesión","fecha examen","campusVPD"]
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
				outputStream.close();
					
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
	private String encodeFileToBase64Binary(String fileName)
	throws IOException {

		File file = new File(fileName);
		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encoder.encode(bytes);
		String encodedString = new String(encoded);

		return encodedString;
	}
	public Boolean validarConexion() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno = true
		}
		return retorno
	}
}
