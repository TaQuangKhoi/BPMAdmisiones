package com.anahuac.rest.api.DAO
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import net.sf.jasperreports.engine.JRDataSource
import net.sf.jasperreports.engine.JREmptyDataSource
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.JasperReport
import org.bonitasoft.engine.bpm.document.Document
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utils.FileDownload
import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper
import com.itextpdf.io.image.ImageData
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document as DocumentItext
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.VerticalPositionMark

class PDFDocumentDAO {
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
	public Result PdfFileCatalogo(String jsonData) {
		Result resultado = new Result();
		InputStream targetStream;
		String log = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			Result dataResult = new Result();
			List<List < Object >> lstParams;

			List<?> info = getInfoReportes(object.email, object.intento).getData();
			//log += isNullOrBlanck(info.get(0)?.idbanner.toString())
			Map < String, Object > columns = new LinkedHashMap < String, Object > ();
			columns.put("idbanner", isNullOrBlanck(info.get(0)?.idbanner.toString()));
			columns.put("nombreAspirante", isNullOrBlanck(info.get(0)?.nombre.toString()));
			columns.put("fechaNacimiento", isNullOrBlanck(info.get(0)?.fechanacimiento.toString()));
			columns.put("fotoPerfil", isNullOrBlanck(info.get(0)?.urlfoto.toString()));
			columns.put("preparatoria", isNullOrBlanck(info.get(0)?.preparatoria.toString()));
			columns.put("ciudad", isNullOrBlanck(info.get(0)?.ciudad.toString()));
			columns.put("pais", isNullOrBlanck(info.get(0)?.pais.toString()));
			columns.put("carreraEstudiar", isNullOrBlanck(info.get(0)?.carrera.toString()));
			columns.put("edad", isNullOrBlanck(info.get(0)?.edad.toString()));
			columns.put("promedio", isNullOrBlanck(info.get(0)?.promedio.toString()));
			columns.put("fecha", isNullOrBlanck(info.get(0)?.fechafinalizacion.toString()));
			columns.put("paav", isNullOrBlanck(info.get(0)?.paav.toString()));
			columns.put("paan", isNullOrBlanck(info.get(0)?.paan.toString()));
			columns.put("para", isNullOrBlanck(info.get(0)?.para.toString()));
			columns.put("paat", isNullOrBlanck(info.get(0)?.promedio.toString()));
			columns.put("invp", isNullOrBlanck(info.get(0)?.invp.toString()));
			columns.put("tipoAdmision", isNullOrBlanck(info.get(0)?.tipoadmision.toString()));
			columns.put("periodoIngreso", isNullOrBlanck(info.get(0)?.periodo.toString()));
			columns.put("entrevisto", isNullOrBlanck(info.get(0)?.quienrealizoentrevista.toString()));
			columns.put("integro", isNullOrBlanck(info.get(0)?.quienintegro.toString()));
			info = getInfoRelativos(info.get(0)?.caseid.toString()).getData();
			String caseid = info.get(0)?.caseid.toString()
			Boolean[] familiares = [false,false,false]
			info.each{
				if(it?.parentesco.toString().equals("Padre") && !familiares[0]) {
					if(it?.desconozcodatospadres.toString().equals("t")) {
						columns.put("nombrePadre", "Se desconoce");
						columns.put("ocupacionPadre", "Se desconoce");
						columns.put("empresaPadre", "Se desconoce");
						columns.put("universidadPadre", "Se desconoce");
					}else {
						columns.put("nombrePadre", isNullOrBlanck(it?.nombre.toString()));
						columns.put("ocupacionPadre", isNullOrBlanck(it?.puesto.toString()));
						columns.put("empresaPadre", isNullOrBlanck(it?.empresatrabaja.toString()));
						columns.put("universidadPadre", isNullOrBlanck(it?.campusanahuac.toString()));
					}
					familiares[0] = true;
				}
				
				if(it?.parentesco.toString().equals("Madre") && !familiares[1]) {
					if(it?.desconozcodatospadres.toString().equals("t")) {
						columns.put("nombreMadre", "Se desconoce");
						columns.put("ocupacionMadre", "Se desconoce");
						columns.put("empresaMadre", "Se desconoce");
						columns.put("universidadMadre", "Se desconoce");
					}else {
						columns.put("nombreMadre", isNullOrBlanck(it?.nombre.toString()));
						columns.put("ocupacionMadre", isNullOrBlanck(it?.puesto.toString()));
						columns.put("empresaMadre", isNullOrBlanck(it?.empresatrabaja.toString()));
						columns.put("universidadMadre", isNullOrBlanck(it?.campusanahuac.toString()));
					}
					familiares[1] = true;
				}
				
				if(it?.istutor.toString().equals("t") && !familiares[2]) {
					columns.put("nombreTutor", isNullOrBlanck(it?.nombre.toString()));
					columns.put("ocupacionTutor", isNullOrBlanck(it?.puesto.toString()));
					columns.put("empresaTutor", isNullOrBlanck(it?.empresatrabaja.toString()));
					columns.put("universidadTutor", isNullOrBlanck(it?.campusanahuac.toString()));
					familiares[2] = true;
				}
				
				
			}
			
			info = getInfoFuentesInfluyeron(caseid,object.intento);
			columns.put("fuentesInfluyeron", '"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est prueba."');
			columns.put("saludAparente", 'Deficiente');
			
			Properties prop = new Properties();
			String propFileName = "configuration.properties";
			InputStream inputStream;
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			String plantilla = prop.getProperty("jasperBase64")
			columns.put("fotoFondo", prop.getProperty("fondo"));
			inputStream.close();
			
			byte [] file = Base64.getDecoder().decode(plantilla)
			targetStream = new ByteArrayInputStream(file);
			JasperReport jasperReport = JasperCompileManager.compileReport(targetStream)
			
			JRDataSource dataSource = new JREmptyDataSource();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, columns, dataSource);
			byte[] encode = Base64.getEncoder().encode(JasperExportManager.exportReportToPdf(jasperPrint));
			String result = new String(encode);
			
			List < Object > lstResultado = new ArrayList < Object > ();
			lstResultado.add(result)
			
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
			resultado.setError_info(log)
			
		} catch (Exception e) {
            resultado.setSuccess(false);
            resultado.setError(e.getMessage()+" || error 1");
        }finally {
			targetStream.close();
		}
		
		return resultado;
	}
	
	private String isNullOrBlanck(String text) {
		if(text == null || text.equals(null) || text.equals("null") || text.equals("") || text.length() == 0) {
			return "N/A"
		}
		
	    return text;
	}
	
	private String encodeFileToBase64Binary(String fileName) throws IOException {

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

	
	
	public Result getInfoReportes(String usuario,Long intento) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs = pstm.executeQuery();
			if (rs.next()) {
				SSA = rs.getString("valor")
			}
			
			pstm = con.prepareStatement(Statements.INFO_REPORTE);
			pstm.setString(1, usuario)
			pstm.setLong(2, intento)
			rs = pstm.executeQuery();
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("urlfoto")) {
						String urlFoto = rs.getString("urlfoto");
						columns.put("urlfoto",  urlFoto+SSA );
					}else {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
					
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getInfoRelativos(String caseid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT distinct on (pt.catparentezco_pid) pt.catparentezco_pid,cp.descripcion as parentesco,empresatrabaja,puesto,pt.vive_pid, cv.descripcion AS vive, pt.desconozcoDatosPadres ,cc.descripcion as campusAnahuac, CONCAT(pt.nombre,' ',pt.apellidos) AS nombre, pt.isTutor FROM PadresTutor AS pt LEFT JOIN catParentesco as cp ON cp.persistenceid = pt.catparentezco_pid LEFT JOIN catCampus AS CC ON cc.persistenceid = catcampusegreso_pid LEFT JOIN catVive AS cv ON cv.persistenceid = pt.vive_pid where  pt.caseid =  "+caseid)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getInfoRelativosHermanos(String caseid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT CONCAT(nombres,' ',apellidos) AS nombre, isestudia,istrabaja,escuelaestudia,empresatrabaja from hermano where caseid = "+caseid)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getInfoFuentesInfluyeron(String caseid, Long intentos) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Boolean autov1 = false;
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement("SELECT idlc.descripcion as fuentes  FROM CatinformacionDeLaCarrera AS idlc LEFT JOIN AUTODESCRIPCIO_INFORMACIONCAR AS ai ON idlc.persistenceid = ai.catinformaciondelacarrera_pid LEFT JOIN Autodescripcion AS auto ON auto.persistenceid = ai.autodescripcion_pid  where caseid = "+caseid)
			rs = pstm.executeQuery()
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					columns.put("autodescripcion", true);
					autov1 = true;
				}

				rows.add(columns);
			}
			
			if(!autov1) {
				//pstm = con.prepareStatement("SELECT fuentesInfluyeronDesicion as fuentes FROM autodescripcionv2 where caseid = "+caseid)
				pstm = con.prepareStatement("SELECT fuentesInfluyeronDesicion as fuentes FROM TestPsicometrico where caseid = '"+caseid+"' AND countRechazo = "+intentos)
				rs = pstm.executeQuery()
				rows = new ArrayList < Map < String, Object >> ();
				metaData = rs.getMetaData();
				columnCount = metaData.getColumnCount();
				while (rs.next()) {
					Map < String, Object > columns = new LinkedHashMap < String, Object > ();
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						columns.put("autodescripcionv2", true);
					}
	
					rows.add(columns);
				}
			}
			
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getInfoRasgos(String caseid, Long intentos) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT rc.descripcion AS calificacion, cro.descripcion AS rasgo FROM TestPsicometricoRasgos AS tpr LEFT JOIN CatRasgosCalif AS rc ON rc.persistenceid = tpr.calificacion_pid LEFT JOIN CatRasgosObservados AS cro ON cro.persistenceid = tpr.rasgo_pid where tpr.caseid = ?  AND tpr.countRechazo = ?")
			pstm.setString(1, caseid);
			pstm.setLong(2, intentos);
			rs = pstm.executeQuery();
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getInfoCapacidadAdaptacion(String caseid, Long intentos) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			pstm = con.prepareStatement("SELECT ajusteMedioFamiliar,califAjusteMedioFamiliar,ajusteEscolarPrevio,califAjusteEscolarPrevio,ajusteMedioSocial,califAjusteMedioSocial,ajusteEfectivo,califAjusteAfectivo,ajusteReligioso,califAjusteReligioso,ajusteExistencial,califAjusteExistencial FROM TestPsicometrico where caseid = ? AND countRechazo = ?")
			pstm.setString(1, caseid);
			pstm.setLong(2, intentos);
			rs = pstm.executeQuery();
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					columns.put("autodescripcionv2", true);
				}

				rows.add(columns);
			}
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result getInfoSaludPSeccion(String caseid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Boolean querySuccess = false;
		String strError = "";
		
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			Map < String, Object > columns = new LinkedHashMap < String, Object > ();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.SELECT_SITUACION_SALUD);
			pstm.setLong(1, Long.parseLong(caseid));
			rs = pstm.executeQuery();

			strError += "SELECT_SITUACION_SALUD: Success | ";
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (rs.next()) {
				columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				
				rows.add(columns);
			}
			
			
			strError += "Se obtuvieron los resultados | ";
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			resultado.setError_info(errorLog+" - Error: "+strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
	}
	
	public Result getInfoSaludSSeccion(String caseid, Long intentos) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Boolean querySuccess = false;
		String strError = "";
		
		try {
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			Map < String, Object > columns = new LinkedHashMap < String, Object > ();
			List<Map<String, Object>> lstCursos = new ArrayList<Map<String, Object>>();
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.SELECT_RECOMENDACIONES_CONCLUSIONES);
			pstm.setString(1, caseid);
			pstm.setLong(2, intentos);
			rs = pstm.executeQuery();
				
			strError += "SELECT_RECOMENDACIONES_CONCLUSIONES: Success | ";
			
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while (rs.next()) {
				columns = new LinkedHashMap < String, Object > ();
				
				for (int i = 1; i <= columnCount; i++) {
						columns.put("salud", rs.getString("salud"));
						columns.put("conclusiones_recomendaciones", rs.getString("conclusiones_recomendaciones"));
						columns.put("interpretacion", rs.getString("interpretacion"));
						columns.put("cursos_recomendados", rs.getString("cursos_recomendados"));
				}
				rows.add(columns);
			}


			
			strError += "Se obtuvieron los resultados | ";
			resultado.setSuccess(true);
			resultado.setData(rows);
		}catch (Exception e) {
			resultado.setError_info(errorLog+" - Error: "+strError);
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm);
			}
		}
		return resultado;
	}
	
	public Boolean deleteJasper() {
		boolean fileSuccessfullyDeleted = false;
		try {
			fileSuccessfullyDeleted =  new File("Psicometrico_report.jrxml").delete()
		}catch(Exception e) {
			fileSuccessfullyDeleted = false; 
		}
		
		return fileSuccessfullyDeleted
	}
	
	public String base64Imagen(String url)  throws Exception {
		String b64 = "";
		if(url.toLowerCase().contains(".jpeg")) {
				b64 = ( "data:image/jpeg;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".png")) {
				b64 = ( "data:image/png;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".jpg")) {
				b64 = ( "data:image/jpg;base64, "+(new FileDownload().b64Url(url)));
			}else if(url.toLowerCase().contains(".jfif")) {
				b64 = ( "data:image/jfif;base64, "+(new FileDownload().b64Url(url)));
			}
		return  b64
	}
	
	
}
