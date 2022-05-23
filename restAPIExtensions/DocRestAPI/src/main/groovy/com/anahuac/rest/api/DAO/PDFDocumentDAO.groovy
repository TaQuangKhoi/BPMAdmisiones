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
import com.anahuac.rest.api.Entity.Result
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
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			Result dataResult = new Result();
			List<List < Object >> lstParams;

			Map < String, Object > columns = new LinkedHashMap < String, Object > ();
			columns.put("idbanner", object.idbanner)
			byte [] file = Base64.getDecoder().decode(jasperB64)
			targetStream = new ByteArrayInputStream(file);
			JasperReport jasperReport = JasperCompileManager.compileReport(targetStream)
			
			JRDataSource dataSource = new JREmptyDataSource();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, columns, dataSource);
			byte[] encode = Base64.getEncoder().encode(JasperExportManager.exportReportToPdf(jasperPrint));
			String result = new String(encode);
			
			List < Object > lstResultado = new ArrayList < Object > ();
			lstResultado.add(result)
			//resultado.setError_info(result)
			//lstResultado.add(encodeFileToBase64Binary("Psicometrico.pdf"));
			
			resultado.setSuccess(true);
			resultado.setData(lstResultado);
			//boolean filedelete = deleteJasper()
			//resultado.setError_info("Fue eliminado:"+filedelete.toString())
			
			//boolean fileSuccessfullyDeleted =  new File("Psicometrico.pdf").delete()
			//resultado.setError_info("Fue eliminado:"+fileSuccessfullyDeleted.toString())
		} catch (Exception e) {
            resultado.setSuccess(false);
            resultado.setError(e.getMessage()+" || error 1");
        } catch (IOException e) {
            resultado.setSuccess(false);
            resultado.setError(e.getMessage()+" || error 2");
        }finally {
			targetStream.close();
		}
		
		return resultado;
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

	
	
	public Result getInfoReportes(String usuario,Long intento,RestAPIContext context) {
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
			
			pstm = con.prepareStatement("SELECT  sda.resultadoPAA,sda.caseid,ds.idbanner,tp.fechaFinalizacion,sda.urlfoto,CONCAT(sda.apellidopaterno,' ',sda.apellidomaterno,CASE WHEN (sda.apellidomaterno != '' ) THEN ' ' END,sda.segundonombre,CASE WHEN ( sda.segundonombre != '' ) THEN ' ' END,sda.primernombre) AS nombre,  TO_CHAR(sda.fechanacimiento::DATE, 'dd-Mon-yyyy') AS fechanacimiento ,(CASE WHEN cb.descripcion = 'Otro' THEN sda.bachillerato ELSE cb.descripcion END) AS preparatoria, (CASE WHEN cb.descripcion = 'Otro' THEN sda.ciudadBachillerato ELSE cb.ciudad END) AS ciudad, cp.descripcion as pais, cge.nombre as carrera, IPAA.INVP,IPAA.PARA,IPAA.PAAN,IPAA.PAAV, sda.promediogeneral as promedio, cta.descripcion AS tipoAdmision, catP.clave as periodo,tp.quienIntegro, tp.quienRealizoEntrevista, date_part('year', age( sda.fechanacimiento::DATE)) as edad FROM SolicitudDeAdmision AS sda INNER JOIN DetalleSolicitud AS ds ON sda.caseid = ds.caseid::INTEGER INNER JOIN catbachilleratos AS cb ON cb.persistenceid = sda.catbachilleratos_pid INNER JOIN catpais AS cp ON cp.persistenceid = sda.catpais_pid INNER JOIN catGestionEscolar as CGE ON CGE.persistenceid = sda.catGestionEscolar_pid INNER JOIN importacionPAA AS IPAA ON IPAA.idbanner = DS.idbanner INNER JOIN catTipoAdmision AS cta ON cta.persistenceid = ds.cattipoadmision_pid INNER JOIN catPeriodo AS catP ON catP.persistenceid = sda.catperiodo_pid INNER JOIN testPsicometrico AS tp ON tp.caseid::INTEGER = sda.caseid WHERE sda.correoelectronico = ? AND countrechazo = ?")
			pstm.setString(1, usuario)
			pstm.setLong(2, intento)
			rs = pstm.executeQuery();
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
						String encoded = "";
						try {
							String urlFoto = rs.getString("urlfoto");
							/*if(urlFoto != null && !urlFoto.isEmpty()) {
								columns.put("fotografiab64", rs.getString("urlfoto") +SSA);
							}else {*/
								List<Document>doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
								for(Document doc : doc1) {
									encoded = "../API/formsDocumentImage?document="+doc.getId();
									columns.put("fotografiab64", encoded);
								}
							//}
						}catch(Exception e) {
							columns.put("fotografiab64", "");
						}
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
	
	public String jasperB64 = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPCEtLSBDcmVhdGVkIHdpdGggSmFzcGVyc29mdCBTdHVkaW8gdmVyc2lvbiA2LjE5LjEuZmluYWwgdXNpbmcgSmFzcGVyUmVwb3J0cyBMaWJyYXJ5IHZlcnNpb24gNi4xOS4xLTg2N2MwMGJmODhjZDRkNzg0ZDQwNDM3OWQ2YzA1ZTFiNDE5ZThhNGMgIC0tPgo8amFzcGVyUmVwb3J0IHhtbG5zPSJodHRwOi8vamFzcGVycmVwb3J0cy5zb3VyY2Vmb3JnZS5uZXQvamFzcGVycmVwb3J0cyIgeG1sbnM6eHNpPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYS1pbnN0YW5jZSIgeHNpOnNjaGVtYUxvY2F0aW9uPSJodHRwOi8vamFzcGVycmVwb3J0cy5zb3VyY2Vmb3JnZS5uZXQvamFzcGVycmVwb3J0cyBodHRwOi8vamFzcGVycmVwb3J0cy5zb3VyY2Vmb3JnZS5uZXQveHNkL2phc3BlcnJlcG9ydC54c2QiIG5hbWU9IlBzaWNvbWV0cmljb19yZXBvcnQiIHBhZ2VXaWR0aD0iNTk1IiBwYWdlSGVpZ2h0PSI4NDIiIGNvbHVtbldpZHRoPSI1NTUiIGxlZnRNYXJnaW49IjIwIiByaWdodE1hcmdpbj0iMjAiIHRvcE1hcmdpbj0iMjAiIGJvdHRvbU1hcmdpbj0iMjAiIHV1aWQ9Ijk0YTVmZGZiLTg0ZjctNDBkNy1iZmU1LTBiMzE1NzMyNTIyNiI+Cgk8cHJvcGVydHkgbmFtZT0iY29tLmphc3BlcnNvZnQuc3R1ZGlvLmRhdGEuZGVmYXVsdGRhdGFhZGFwdGVyIiB2YWx1ZT0iUHNpY29tZXRyaWNvX3JlcG9ydCIvPgoJPHBhcmFtZXRlciBuYW1lPSJpZGJhbm5lciIgY2xhc3M9ImphdmEubGFuZy5TdHJpbmciLz4KCTxxdWVyeVN0cmluZz4KCQk8IVtDREFUQVtdXT4KCTwvcXVlcnlTdHJpbmc+Cgk8YmFja2dyb3VuZD4KCQk8YmFuZCBzcGxpdFR5cGU9IlN0cmV0Y2giLz4KCTwvYmFja2dyb3VuZD4KCTx0aXRsZT4KCQk8YmFuZCBoZWlnaHQ9IjExMCIgc3BsaXRUeXBlPSJTdHJldGNoIj4KCQkJPHN0YXRpY1RleHQ+CgkJCQk8cmVwb3J0RWxlbWVudCB4PSIwIiB5PSIyMCIgd2lkdGg9IjM0MCIgaGVpZ2h0PSIzMCIgdXVpZD0iZGU1ZDFjNmItNWMzYi00MWI3LWI3Y2ItNTQ4NmQ1N2FmM2RkIi8+CgkJCQk8dGV4dEVsZW1lbnQgbWFya3VwPSJub25lIj4KCQkJCQk8Zm9udCBzaXplPSIxNCIvPgoJCQkJPC90ZXh0RWxlbWVudD4KCQkJCTx0ZXh0PjwhW0NEQVRBW0RFUEFSVEFNRU5UTyBERSBPUklFTlRBQ0nDk04gVk9DQUNJT05BTF1dPjwvdGV4dD4KCQkJPC9zdGF0aWNUZXh0PgoJCQk8aW1hZ2U+CgkJCQk8cmVwb3J0RWxlbWVudCB4PSIzNzAiIHk9IjI4IiB3aWR0aD0iMTgwIiBoZWlnaHQ9IjU1IiB1dWlkPSI0MDNkNmFjMS1jNDY5LTRmOWItYjEzOS1lMjU0ZjFkNmIyZWYiPgoJCQkJCTxwcm9wZXJ0eSBuYW1lPSJjb20uamFzcGVyc29mdC5zdHVkaW8udW5pdC53aWR0aCIgdmFsdWU9InB4Ii8+CgkJCQk8L3JlcG9ydEVsZW1lbnQ+CgkJCQk8aW1hZ2VFeHByZXNzaW9uPjwhW0NEQVRBWyJodHRwczovL2JwbWludGVncmEuYmxvYi5jb3JlLndpbmRvd3MubmV0L3B1YmxpY28vTG9nb1JVQS5wbmciXV0+PC9pbWFnZUV4cHJlc3Npb24+CgkJCTwvaW1hZ2U+CgkJCTxzdGF0aWNUZXh0PgoJCQkJPHJlcG9ydEVsZW1lbnQgeD0iMCIgeT0iNjAiIHdpZHRoPSIzMzgiIGhlaWdodD0iMzAiIHV1aWQ9ImNlMTJmYmUxLTU4YmQtNGNjNS1iYmY0LWRiZWI5NzI5YjIxNyIvPgoJCQkJPHRleHRFbGVtZW50IG1hcmt1cD0ibm9uZSI+CgkJCQkJPGZvbnQgc2l6ZT0iMTQiLz4KCQkJCTwvdGV4dEVsZW1lbnQ+CgkJCQk8dGV4dD48IVtDREFUQVtSRVBPUlRFIFBTSUNPTMOTR0lDT11dPjwvdGV4dD4KCQkJPC9zdGF0aWNUZXh0PgoJCTwvYmFuZD4KCTwvdGl0bGU+Cgk8cGFnZUhlYWRlcj4KCQk8YmFuZCBoZWlnaHQ9IjM5IiBzcGxpdFR5cGU9IlN0cmV0Y2giPgoJCQk8c3RhdGljVGV4dD4KCQkJCTxyZXBvcnRFbGVtZW50IHg9IjAiIHk9IjQiIHdpZHRoPSIxOTQiIGhlaWdodD0iMzAiIHV1aWQ9IjZiMmRmOWE2LTBkZjMtNGE4Ni1hYmU0LWQwYjk3MTVhODhlNiIvPgoJCQkJPHRleHRFbGVtZW50PgoJCQkJCTxmb250IHNpemU9IjEyIi8+CgkJCQk8L3RleHRFbGVtZW50PgoJCQkJPHRleHQ+PCFbQ0RBVEFbSW5mb3JtYWNpw7NuIHBlcnNvbmFsDV1dPjwvdGV4dD4KCQkJPC9zdGF0aWNUZXh0PgoJCTwvYmFuZD4KCTwvcGFnZUhlYWRlcj4KCTxjb2x1bW5IZWFkZXI+CgkJPGJhbmQgaGVpZ2h0PSI2MSIgc3BsaXRUeXBlPSJTdHJldGNoIj4KCQkJPHN0YXRpY1RleHQ+CgkJCQk8cmVwb3J0RWxlbWVudCB4PSIwIiB5PSIwIiB3aWR0aD0iMTAwIiBoZWlnaHQ9IjE4IiB1dWlkPSJiMWFlMjQxOC01YmFjLTQ4YjQtODE2My03YzkyYjRkY2I5YTAiLz4KCQkJCTx0ZXh0PjwhW0NEQVRBW0lkIGJhbm5lcjpdXT48L3RleHQ+CgkJCTwvc3RhdGljVGV4dD4KCQkJPHRleHRGaWVsZD4KCQkJCTxyZXBvcnRFbGVtZW50IHg9IjExMCIgeT0iMCIgd2lkdGg9IjEwMCIgaGVpZ2h0PSIzMCIgdXVpZD0iNTM5NjIzMDYtYzhmMi00MjA1LWExZTQtOGMwZWM0ZGQzZThiIi8+CgkJCQk8dGV4dEZpZWxkRXhwcmVzc2lvbj48IVtDREFUQVskUHtpZGJhbm5lcn1dXT48L3RleHRGaWVsZEV4cHJlc3Npb24+CgkJCTwvdGV4dEZpZWxkPgoJCTwvYmFuZD4KCTwvY29sdW1uSGVhZGVyPgoJPGRldGFpbD4KCQk8YmFuZCBoZWlnaHQ9IjEyNSIgc3BsaXRUeXBlPSJTdHJldGNoIi8+Cgk8L2RldGFpbD4KCTxjb2x1bW5Gb290ZXI+CgkJPGJhbmQgaGVpZ2h0PSI0NSIgc3BsaXRUeXBlPSJTdHJldGNoIi8+Cgk8L2NvbHVtbkZvb3Rlcj4KCTxwYWdlRm9vdGVyPgoJCTxiYW5kIGhlaWdodD0iNTQiIHNwbGl0VHlwZT0iU3RyZXRjaCIvPgoJPC9wYWdlRm9vdGVyPgoJPHN1bW1hcnk+CgkJPGJhbmQgaGVpZ2h0PSI0MiIgc3BsaXRUeXBlPSJTdHJldGNoIi8+Cgk8L3N1bW1hcnk+CjwvamFzcGVyUmVwb3J0Pgo="
	
}
