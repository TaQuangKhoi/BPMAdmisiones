package com.anahuac.rest.api.DAO

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext
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
	
	ResultSet rs2;
	PreparedStatement pstm2;
	
    public Result generarReporte(String jsonData) {
        Result resultado = new Result();
        String errorLog = "", where = "";
        Boolean closeCon = false;
        try {
            Result dataResult = new Result();
            int rowCount = 0;
            List < Object > lstParams;
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
            XSSFColor color = new XSSFColor(new java.awt.Color(191, 220, 249), colorMap);

            style.setFillForegroundColor(color)
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
			String condicion=""
			for (int i=0;i< object.multicampus.split(",").size();i++) {
				condicion+="'"+object.multicampus.split(",")[i]+"'"+((i==object.multicampus.split(",").size()-1)?"":",")
			}
            //where += " WHERE sda.catcampus_pid=" + object.campus
			where += " WHERE campus.clave in (" + condicion +") "
            where += (object.periodo == null || object.periodo.equals("")) ? "" : " AND sda.catperiodo_pid in (" + object.periodo + ")"
            where += (object.carrera == null || object.carrera.equals("")) ? "" : " AND sda.catgestionescolar_pid in (" + object.carrera + ")"
            where += (object.preparatoria == null || object.preparatoria.equals("")) ? "" : " AND sda.catbachilleratos_pid in (" + object.preparatoria + ")"
            where += (object.sesion == null || object.sesion.equals("")) ? "" : " AND s.persistenceid in (" + object.sesion + ")"
            where += (object.idbanner == null || object.idbanner.equals("")) ? "" : " AND cda.idbanner = '" + object.idbanner + "'"


            String consulta = "SELECT distinct case when cr.segundonombre='' then cr.primernombre else cr.primernombre || ' ' || cr.segundonombre end as nombre, cr.apellidopaterno as apaterno,cr.apellidomaterno as amaterno,sda.correoelectronico as email,cc.clave || cda.idbanner as usuario,sda.fechanacimiento as clave, '' as edad, cda.idbanner as matricula, '' as curp, s.nombre as sesion, to_char(p.aplicacion, 'DD/MM/YYYY') as fecha_examen, cc.clave as campusVPD, s.persistenceid as IdSesion, s.nombre as NombreSesion FROM solicituddeadmision cr inner join DETALLESOLICITUD cda on cda.caseid::bigint=cr.caseid  AND (cda.cbcoincide is false OR cda.cbcoincide is null) inner join solicituddeadmision sda on sda.caseid=cda.caseid::bigint inner join catcampus cc on cc.persistenceid=sda.catcampusestudio_pid inner join sesionaspirante sa on sa.username=sda.correoelectronico inner join pruebas p on sa.sesiones_pid=p.sesion_pid and p.cattipoprueba_pid=4 inner join sesiones s on s.persistenceid=sa.sesiones_pid INNER JOIN catcampus campus on campus.persistenceid=sda.catcampus_pid " + where
			errorLog += consulta;
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            closeCon = validarConexion();
            pstm = con.prepareStatement(consulta)


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
            dataResult.setData(rows)
            dataResult.setSuccess(true)
            if (dataResult.success) {
                lstParams = dataResult.getData();

            } else {
                throw new Exception("No encontro datos");
            }
            FileOutputStream fw = new FileOutputStream("out.txt");



            def titulos = ["Nombre", "Apaterno", "Amaterno", "Email", "Usuario", "Clave", "Edad", "Matrícula", "Curp", "Sesión", "Fecha examen", "Campus(VPD)","IdSesión"]
            def titulosRua = ["nombre", "apaterno", "amaterno", "email", "usuario", "clave", "edad", "matricula", "curp", "sesión", "fecha examen", "campus(VPD)","idSesión"]
            if (object.encabezado) {
                fw.write((titulosRua.join(",") + "\r\n").getBytes());
                Row headersRow = sheet.createRow(rowCount);
                ++rowCount;
                List < Cell > header = new ArrayList < Cell > ();
                for (int i = 0; i < titulos.size(); ++i) {
                    header.add(headersRow.createCell(i))
                    header[i].setCellValue(titulos.get(i))
                    header[i].setCellStyle(style)
                }
            }
            CellStyle bodyStyle = workbook.createCellStyle();
            bodyStyle.setWrapText(true);
            bodyStyle.setAlignment(HorizontalAlignment.LEFT);
            SimpleDateFormat sdfin = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            SimpleDateFormat sdfin2 = new SimpleDateFormat("yyyy-MM-dd't'HH:mm:ss.SSS")
            SimpleDateFormat sdfout = new SimpleDateFormat("yyyy/MM/dd")

            def info = ["nombre", "apaterno", "amaterno", "email", "usuario", "clave", "edad", "matricula", "curp", "sesion", "fecha_examen", "campusvpd","idsesion"]
            List < Cell > body;
            String line = ""
            for (int i = 0; i < lstParams.size(); ++i) {
                line = ""
                Row row = sheet.createRow(rowCount);
                ++rowCount;
                body = new ArrayList < Cell > ()
                for (int j = 0; j < info.size(); ++j) {
                    body.add(row.createCell(j))
                    try {
                        body[j].setCellValue((info.get(j).equals("clave")) ? sdfout.format(sdfin.parse(lstParams[i][info.get(j)])) : lstParams[i][info.get(j)])
                        line += (info.get(j).equals("clave") ? sdfout.format(sdfin.parse(lstParams[i][info.get(j)])) : lstParams[i][info.get(j)]) + ((info.get(j).equals("idsesion")) ? "\r\n" : ",")
                    } catch (Exception e) {
                        body[j].setCellValue((info.get(j).equals("clave")) ? sdfout.format(sdfin2.parse(lstParams[i][info.get(j)])) : lstParams[i][info.get(j)])
                        line += (info.get(j).equals("clave") ? sdfout.format(sdfin2.parse(lstParams[i][info.get(j)])) : lstParams[i][info.get(j)]) + ((info.get(j).equals("idsesion")) ? "\r\n" : ",")
                    }

                    body[j].setCellStyle(bodyStyle);

                }
                fw.write(line.getBytes());
            }

            if (lstParams.size() > 0) {
                for (int i = 0; i <= 20; ++i) {
                    sheet.autoSizeColumn(i);
                }
                String fecha = lstParams[0]["fecharegistro"].toString() + "";
                String nameFile = "Reporte-" + fecha + ".xls";
                FileOutputStream outputStream = new FileOutputStream(nameFile);
                workbook.write(outputStream);
                List < Object > lstResultado = new ArrayList < Object > ();
                lstResultado.add(encodeFileToBase64Binary(nameFile));
                lstResultado.add(encodeFileToBase64Binary("out.txt"))
                resultado.setData(lstResultado)
                outputStream.close();
                fw.close();

            } else {
                throw new Exception("No encontro datos:" + errorLog + dataResult.getError());
            }




            //List<Object> lstResultado = new ArrayList<Object>();
            //lstResultado.add(encodeFileToBase64Binary(nameFile));

            resultado.setSuccess(true);
            //resultado.setData(lstResultado);
            resultado.setError_info(errorLog);

        } catch (Exception e) {
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            resultado.setError_info(errorLog);
            e.printStackTrace();
        } finally {
            if (closeCon) {
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
            List < Object > lstParams;
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
            XSSFColor color = new XSSFColor(new java.awt.Color(191, 220, 249), colorMap);

            style.setFillForegroundColor(color)
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
			String condicion=""
			for (int i=0;i< object.multicampus.split(",").size();i++) {
				condicion+="'"+object.multicampus.split(",")[i]+"'"+((i==object.multicampus.split(",").size()-1)?"":",")
			}
			
            where += " WHERE  ( autodescripcion.persistenceid IS NOT NULL OR autodescripcionv2.persistenceid IS NOT NULL  ) and campus.clave in (" + condicion +") "
            where += (object.periodo == null || object.periodo.equals("")) ? "" : " AND sda.catperiodo_pid in (" + object.periodo + ")"
            where += (object.carrera == null || object.carrera.equals("")) ? "" : " AND sda.catgestionescolar_pid in (" + object.carrera + ")"
            where += (object.preparatoria == null || object.preparatoria.equals("")) ? "" : " AND sda.catbachilleratos_pid in (" + object.preparatoria + ")"
            where += (object.sesion == null || object.sesion.equals("")) ? "" : " AND s.persistenceid in (" + object.sesion + ")"
            where += (object.idbanner == null || object.idbanner.equals("")) ? "" : " AND cda.idbanner = '" + object.idbanner + "'"


            String consulta = "SELECT DISTINCT campus.descripcion universidad, periodo.clave      periodo, cda.idbanner       numerodematricula, CASE WHEN cr.apellidomaterno=''THEN cr.apellidopaterno || ' ' || CASE WHEN cr.segundonombre=''THEN cr.primernombre ELSE cr.primernombre || ' ' || cr.segundonombre END ELSE cr.apellidopaterno||' '||cr.apellidomaterno ||' ' || CASE WHEN cr.segundonombre=''THEN cr.primernombre ELSE cr.primernombre || ' ' || cr.segundonombre END END                      AS nombre, gestionescolar.nombre    AS carrera, preparatoria.descripcion    preparatoriadeprocedencia, religion.descripcion        religion, sexo.descripcion            sexo, tipoadmision.descripcion    tipodeadmision, s.nombre                 AS sesion, sda.promediogeneral         promedio, importacionpa.invp          invp, ImportacionPA.paav          paaverbal, ImportacionPA.lexiumPaav    clex, ImportacionPA.paan          paanumerica, ImportacionPA.lexiumPaan    mlex, ImportacionPA.para          para, ImportacionPA.lexiumPara    hlex, ImportacionPA.total         paa, infocarta.pdp, infocarta.pdu, infocarta.sse, infocarta.pcda, infocarta.pca, campusingreso.descripcion    campusingreso, tipoalumno.descripcion       tipodeestudiante, sda.curp                  AS curp, ''                           decisiondeadmision, ''                           cpdp, ''                           cpdu, ''                           csse, ''                           cpcda, ''                           cpca, ''                           observaciones FROM catregistro cr INNER JOIN DETALLESOLICITUD cda ON cda.caseid::bigint=cr.caseid INNER JOIN solicituddeadmision sda ON sda.caseid=cda.caseid::bigint INNER JOIN catcampus cc ON cc.persistenceid=sda.catcampusestudio_pid INNER JOIN sesionaspirante sa ON sa.username=sda.correoelectronico INNER JOIN pruebas p ON sa.sesiones_pid=p.sesion_pid AND p.cattipoprueba_pid=4 INNER JOIN sesiones s ON s.persistenceid=sa.sesiones_pid INNER JOIN catcampus campus ON campus.persistenceid=sda.catcampus_pid INNER JOIN catperiodo periodo ON sda.catPeriodo_pid=periodo.persistenceid INNER JOIN catgestionescolar gestionescolar ON sda.catgestionescolar_pid=gestionescolar.persistenceid INNER JOIN catbachilleratos preparatoria ON sda.catbachilleratos_pid=preparatoria.persistenceid LEFT JOIN autodescripcion autodescripcion ON autodescripcion.caseid=sda.caseid LEFT JOIN autodescripcionV2 autodescripcionv2 ON autodescripcionv2.caseid=sda.caseid INNER JOIN catreligion religion ON religion.persistenceid=sda.catreligion_pid INNER JOIN catsexo sexo ON sexo.persistenceid=sda.catsexo_pid INNER JOIN cattipoadmision tipoadmision ON cattipoadmision_pid=tipoadmision.persistenceid INNER JOIN catcampus campusingreso ON campusingreso.persistenceid=catcampusestudio_pid INNER JOIN cattipoalumno tipoalumno ON tipoalumno.persistenceid=cda.cattipoalumno_pid LEFT JOIN importacionpaa importacionpa ON importacionpa.idbanner=cda.idbanner LEFT JOIN infocarta infocarta ON infocarta.numerodematricula=cda.idbanner INNER JOIN catcampus campus on campus.persistenceid=sda.catcampus_pid " + where
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            closeCon = validarConexion();
            pstm = con.prepareStatement(consulta)


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
            errorLog += "|Consulta:" + consulta + "|"
            dataResult.setData(rows)
            dataResult.setSuccess(true)
            if (dataResult.success) {
                lstParams = dataResult.getData();
                errorLog += "|Parametros:" + lstParams.toString() + "|"
            } else {
                throw new Exception("No encontro datos");
            }
            FileOutputStream fw = new FileOutputStream("out.txt");



            def titulos = ["Universidad", "Periodo", "Número de matrícula", "Nombre", "Carrera", "Preparatoria de procedencia", "Religión", "Sexo", "Tipo de admisión", "Sesión", "Promedio", "INVP", "PAAV", "CLEX", "PAAN", "MLEX", "PARA", "HLEX", "PAAT", "PDP", "PDU", "SSE",  "PCA", "Campus ingreso", "Tipo de estudiante", "CURP", "Decisión de admisión", "PDP", "PDU", "SSE",  "PCA", "Observaciones"]
            if (object.encabezado) {
                fw.write((titulos.join(",") + "\r\n").getBytes());
                Row headersRow = sheet.createRow(rowCount);
                ++rowCount;
                List < Cell > header = new ArrayList < Cell > ();
                for (int i = 0; i < titulos.size(); ++i) {
                    header.add(headersRow.createCell(i))
                    header[i].setCellValue(titulos.get(i))
                    header[i].setCellStyle(style)
                }
            }
            CellStyle bodyStyle = workbook.createCellStyle();
            bodyStyle.setWrapText(true);
            bodyStyle.setAlignment(HorizontalAlignment.LEFT);

            def info = ["universidad", "periodo", "numerodematricula", "nombre", "carrera", "preparatoriadeprocedencia", "religion", "sexo", "tipodeadmision", "sesion", "promedio", "invp", "paaverbal", "clex", "paanumerica", "mlex", "para", "hlex", "paa", "pdp", "pdu", "sse", "pca", "campusingreso", "tipodeestudiante", "curp", "decisiondeadmision", "cpdp", "cpdu", "csse",  "cpca", "observaciones"]
            List < Cell > body;
            String line = ""
            for (int i = 0; i < lstParams.size(); ++i) {
                line = ""
                Row row = sheet.createRow(rowCount);
                ++rowCount;
                body = new ArrayList < Cell > ()
                for (int j = 0; j < info.size(); ++j) {
                    body.add(row.createCell(j))
                    try {
                        body[j].setCellValue(lstParams[i][info.get(j)])
                        line += (lstParams[i][info.get(j)]) + ((info.get(j).equals("observaciones")) ? "\r\n" : ",")
                    } catch (Exception e) {
                        body[j].setCellValue(lstParams[i][info.get(j)])
                        line += (lstParams[i][info.get(j)]) + ((info.get(j).equals("observaciones")) ? "\r\n" : ",")
                    }

                    body[j].setCellStyle(bodyStyle);

                }
                fw.write(line.getBytes());
            }

            if (lstParams.size() > 0) {
                for (int i = 0; i <= 138; ++i) {
                    sheet.autoSizeColumn(i);
                }
                String fecha = lstParams[0]["fecharegistro"].toString() + "";
                String nameFile = "Reporte-" + fecha + ".xls";
                FileOutputStream outputStream = new FileOutputStream(nameFile);
                workbook.write(outputStream);
                List < Object > lstResultado = new ArrayList < Object > ();
                lstResultado.add(encodeFileToBase64Binary(nameFile));
                lstResultado.add(encodeFileToBase64Binary("out.txt"))
                resultado.setData(lstResultado)
                outputStream.close();
                fw.close();

            } else {
                throw new Exception("No encontro datos:" + errorLog + dataResult.getError());
            }




            //List<Object> lstResultado = new ArrayList<Object>();
            //lstResultado.add(encodeFileToBase64Binary(nameFile));

            resultado.setSuccess(true);
            //resultado.setData(lstResultado);
            resultado.setError_info(errorLog);

        } catch (Exception e) {
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            resultado.setError_info(errorLog);
            e.printStackTrace();
        } finally {
            if (closeCon) {
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
            List < Object > lstParams;
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
            XSSFColor color = new XSSFColor(new java.awt.Color(191, 220, 249), colorMap);

            style.setFillForegroundColor(color)
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);

            String condicion=""
			for (int i=0;i< object.multicampus.split(",").size();i++) {
				condicion+="'"+object.multicampus.split(",")[i]+"'"+((i==object.multicampus.split(",").size()-1)?"":",")
			}
            //where += " WHERE sda.catcampus_pid=" + object.campus
			where += " WHERE campus.clave in (" + condicion +") "
            where += (object.periodo == null || object.periodo.equals("")) ? "" : " AND sda.catperiodo_pid in (" + object.periodo + ")"
            where += (object.carrera == null || object.carrera.equals("")) ? "" : " AND sda.catgestionescolar_pid in (" + object.carrera + ")"
            where += (object.preparatoria == null || object.preparatoria.equals("")) ? "" : " AND sda.catbachilleratos_pid in (" + object.preparatoria + ")"
            where += (object.idbanner == null || object.idbanner.equals("")) ? "" : " AND cda.idbanner = '" + object.idbanner + "'"
			where += (object.sesion == null || object.sesion.equals("")) ? "" : " AND s.persistenceid in (" + object.sesion + ")"


            String consulta = "SELECT DISTINCT CASE WHEN cr.apellidomaterno=''THEN cr.apellidopaterno || ' ' || CASE WHEN cr.segundonombre=''THEN cr.primernombre ELSE cr.primernombre || ' ' || cr.segundonombre END ELSE cr.apellidopaterno||' '||cr.apellidomaterno ||' ' || CASE WHEN cr.segundonombre=''THEN cr.primernombre ELSE cr.primernombre || ' ' || cr.segundonombre END END                   AS nombre, cda.idbanner             nomatricula, sda.correoelectronico    email, sda.telefono, sda.telefonocelular celular, preparatoria.ciudad, preparatoria.descripcion    preparatoria, sda.promediogeneral         promedio, gestionescolar.clave     AS clavecarrera,cp.clave clavepropedeutico, cp.descripcion              propedeutico, tipoadmision.descripcion    tipodeadmision, 'x'                         solicitudadmision, CASE WHEN  sda.estatussolicitud='Resultado final del comité' OR sda.estatussolicitud='Carga y consulta de resultados' OR sda.estatussolicitud='Selección de fechas' OR sda.estatussolicitud='Ya se imprimió su credencial' OR sda.estatussolicitud='Autodescripción concluida' OR sda.estatussolicitud='Solicitud con pago aceptado' OR sda.estatussolicitud='Validación descuento 100%' OR sda.estatussolicitud='Solicitud con pagoc condenado' OR sda.estatussolicitud='Autodescripción en proceso'                     then 'x' else  '' END   pagoadmision, CASE WHEN  sda.estatussolicitud='Resultado final del comité' OR sda.estatussolicitud='Carga y consulta de resultados' OR sda.estatussolicitud='Selección de fechas' OR sda.estatussolicitud='Ya se imprimió su credencial' OR sda.estatussolicitud='Autodescripción concluida'  then 'x' else  '' END                         autodescripcion, CASE WHEN  sda.estatussolicitud='Resultado final del comité' OR sda.estatussolicitud='Carga y consulta de resultados' OR sda.estatussolicitud='Selección de fechas' OR sda.estatussolicitud='Ya se imprimió su credencial'  then 'x' else  '' END                           examenadmision, CASE WHEN  sda.estatussolicitud='Resultado final del comité' OR sda.estatussolicitud='Carga y consulta de resultados' then 'x' else  '' END                           esperandoresultados, CASE WHEN  sda.estatussolicitud='Resultado final del comité' then 'x' else '' END                     resultadoadmision, sda.estatussolicitud, ''            fechaadmision, ''                     pagocurso, tipoalumno.descripcion tipoestudiante FROM catregistro cr INNER JOIN DETALLESOLICITUD cda ON cda.caseid::bigint=cr.caseid INNER JOIN solicituddeadmision sda ON sda.caseid=cda.caseid::bigint INNER JOIN catcampus cc ON cc.persistenceid=sda.catcampusestudio_pid INNER JOIN sesionaspirante sa ON sa.username=sda.correoelectronico INNER JOIN catcampus campus ON campus.persistenceid=sda.catcampus_pid INNER JOIN catperiodo periodo ON sda.catPeriodo_pid=periodo.persistenceid INNER JOIN catgestionescolar gestionescolar ON sda.catgestionescolar_pid=gestionescolar.persistenceid INNER JOIN catbachilleratos preparatoria ON sda.catbachilleratos_pid=preparatoria.persistenceid INNER JOIN autodescripcion autodescripcion ON autodescripcion.caseid=sda.caseid INNER JOIN catreligion religion ON religion.persistenceid=autodescripcion.catreligion_pid INNER JOIN catsexo sexo ON sexo.persistenceid=sda.catsexo_pid INNER JOIN cattipoadmision tipoadmision ON cattipoadmision_pid=tipoadmision.persistenceid INNER JOIN catcampus campusingreso ON campusingreso.persistenceid=catcampusestudio_pid INNER JOIN cattipoalumno tipoalumno ON tipoalumno.persistenceid=cda.cattipoalumno_pid INNER JOIN catpropedeutico cp ON cp.persistenceid=sda.catpropedeutico_pid INNER JOIN catcampus campus on campus.persistenceid=sda.catcampus_pid" + where
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            closeCon = validarConexion();
            pstm = con.prepareStatement(consulta)


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
            errorLog += "|Consulta:" + consulta + "|"
            dataResult.setData(rows)
            dataResult.setSuccess(true)
            if (dataResult.success) {
                lstParams = dataResult.getData();
                errorLog += "|Parametros:" + lstParams.toString() + "|"
            } else {
                throw new Exception("No encontro datos");
            }
            FileOutputStream fw = new FileOutputStream("out.txt");



            def titulos = ["Nombre", "No. Matrícula", "Email", "Teléfonos", "Celular", "Ciudad(procedencia preparatoria)", "Preparatoria", "Promedio", "Clave carrera","Periodo Propedéutico", "Nombre de propedeutico", "Tipo Admisión", "Solicitud Admisión", "Pago de Examen", "Autodescripción", "Selección Fechas", "Esperando resultados", "Resultado de admisión", "Fecha admisión", "Pago curso", "Tipo de Estudiante"]
            if (object.encabezado) {
                fw.write((titulos.join(",") + "\r\n").getBytes());
                Row headersRow = sheet.createRow(rowCount);
                ++rowCount;
                List < Cell > header = new ArrayList < Cell > ();
                for (int i = 0; i < titulos.size(); ++i) {
                    header.add(headersRow.createCell(i))
                    header[i].setCellValue(titulos.get(i))
                    header[i].setCellStyle(style)
                }
            }
            CellStyle bodyStyle = workbook.createCellStyle();
            bodyStyle.setWrapText(true);
            bodyStyle.setAlignment(HorizontalAlignment.LEFT);

            def info = ["nombre", "nomatricula", "email", "telefono", "celular", "ciudad", "preparatoria", "promedio", "clavecarrera","clavepropedeutico", "propedeutico", "tipodeadmision", "solicitudadmision", "pagoadmision", "autodescripcion", "examenadmision", "esperandoresultados", "resultadoadmision", "fechaadmision", "pagocurso", "tipoestudiante"]
            List < Cell > body;
            String line = ""
            for (int i = 0; i < lstParams.size(); ++i) {
                line = ""
                Row row = sheet.createRow(rowCount);
                ++rowCount;
                body = new ArrayList < Cell > ()
                for (int j = 0; j < info.size(); ++j) {
                    body.add(row.createCell(j))
                    try {
                        body[j].setCellValue(lstParams[i][info.get(j)])
                        line += (lstParams[i][info.get(j)]) + ((info.get(j).equals("observaciones")) ? "\r\n" : ",")
                    } catch (Exception e) {
                        body[j].setCellValue(lstParams[i][info.get(j)])
                        line += (lstParams[i][info.get(j)]) + ((info.get(j).equals("observaciones")) ? "\r\n" : ",")
                    }

                    body[j].setCellStyle(bodyStyle);

                }
                fw.write(line.getBytes());
            }

            if (lstParams.size() > 0) {
                for (int i = 0; i <= 138; ++i) {
                    sheet.autoSizeColumn(i);
                }
                String fecha = lstParams[0]["fecharegistro"].toString() + "";
                String nameFile = "Reporte-" + fecha + ".xls";
                FileOutputStream outputStream = new FileOutputStream(nameFile);
                workbook.write(outputStream);
                List < Object > lstResultado = new ArrayList < Object > ();
                lstResultado.add(encodeFileToBase64Binary(nameFile));
                lstResultado.add(encodeFileToBase64Binary("out.txt"))
                resultado.setData(lstResultado)
                outputStream.close();
                fw.close();

            } else {
                throw new Exception("No encontro datos:" + errorLog + dataResult.getError());
            }




            //List<Object> lstResultado = new ArrayList<Object>();
            //lstResultado.add(encodeFileToBase64Binary(nameFile));

            resultado.setSuccess(true);
            //resultado.setData(lstResultado);
            resultado.setError_info(errorLog);

        } catch (Exception e) {
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            resultado.setError_info(errorLog);
            e.printStackTrace();
        } finally {
            if (closeCon) {
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
            List < Object > lstParams;
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
            XSSFColor color = new XSSFColor(new java.awt.Color(191, 220, 249), colorMap);

            style.setFillForegroundColor(color)
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
			String condicion=""
			for (int i=0;i< object.multicampus.split(",").size();i++) {
				condicion+="'"+object.multicampus.split(",")[i]+"'"+((i==object.multicampus.split(",").size()-1)?"":",")
			}
			//where += " WHERE sda.catcampus_pid=" + object.campus
			where += " WHERE campus.clave in (" + condicion +") AND (ap.asistencia IS TRUE OR (PL.asistencia IS NULL AND ap.acreditado IS NULL) OR (cda.cbcoincide IS TRUE AND ap.acreditado IS TRUE)) "
            where += (object.periodo == null || object.periodo.equals("")) ? "" : " AND sda.catperiodo_pid in (" + object.periodo + ")"
            where += (object.carrera == null || object.carrera.equals("")) ? "" : " AND sda.catgestionescolar_pid in (" + object.carrera + ")"
            where += (object.preparatoria == null || object.preparatoria.equals("")) ? "" : " AND sda.catbachilleratos_pid in (" + object.preparatoria + ")"
            where += (object.sesion == null || object.sesion.equals("")) ? "" : " AND sesion.persistenceid in (" + object.sesion + ") "
            where += (object.idbanner == null || object.idbanner.equals("")) ? "" : " AND cda.idbanner = '" + object.idbanner + "'"
			

            //String consulta = "SELECT DISTINCT cda.idbanner AS id ,  CASE WHEN cr.apellidomaterno=''THEN cr.apellidopaterno || ' ' || CASE WHEN cr.segundonombre=''THEN cr.primernombre ELSE cr.primernombre || ' ' || cr.segundonombre END ELSE cr.apellidopaterno||' '||cr.apellidomaterno ||' ' || CASE WHEN cr.segundonombre=''THEN cr.primernombre ELSE cr.primernombre || ' ' || cr.segundonombre END END    AS nombre, pt.nombre || ' '|| pt.apellidos AS nombepadres, cp.descripcion AS relacion,case when pt.istutor then 'Si' else 'No' end AS istutor ,case when pt.cattrabaja_pid is null then 'No' else trabaja.descripcion end as trabaja, pt.empresatrabaja AS empleador, pt.puesto AS titulo, pt.correoelectronico AS correo, pt.calle ||' #' || pt.numeroexterior || ' '|| pt.colonia ||', '||ce.descripcion || ' ' || pt.ciudad || ' ' || pt.codigopostal direccion, pt.telefono, sda.estatussolicitud  AS codigodedecision, pt.calle ||' #' || pt.numeroexterior AS calle, pt.colonia, pt.delegacionmunicipio, pt.ciudad, ce.descripcion  AS estado, pt.codigopostal AS cp, cpa.descripcion AS pais, to_char(to_date(sda.fechaultimamodificacion, 'YYYY-MM-DD\"T\"HH24:MI:SS'),'YYYY-MM-DD HH24:MI')   ultimamod, ''   resultadoad FROM catregistro cr INNER JOIN DETALLESOLICITUD cda ON cda.caseid::bigint=cr.caseid AND cda.idbanner != '' INNER JOIN solicituddeadmision sda ON sda.caseid=cda.caseid::bigint AND sda.estatussolicitud != 'Solicitud vencida' INNER JOIN padrestutor pt ON pt.caseid=cda.caseid::bigint INNER JOIN CatParentesco cp ON cp.persistenceid=pt.catparentezco_pid INNER JOIN catestados ce ON ce.persistenceid=pt.catestado_pid LEFT JOIN sesionaspirante sa ON sa.username=sda.correoelectronico LEFT JOIN pruebas p ON sa.sesiones_pid=p.sesion_pid AND p.cattipoprueba_pid=4 LEFT JOIN sesiones s ON s.persistenceid=sa.sesiones_pid INNER JOIN catpais cpa ON pt.catpais_pid=cpa.persistenceid LEFT JOIN catpadretrabaja trabaja on trabaja.persistenceid=pt.cattrabaja_pid INNER JOIN catcampus campus on campus.persistenceid=sda.catcampus_pid " + where + " ORDER BY cda.idbanner "
			String consulta = "SELECT * FROM (SELECT DISTINCT cda.idbanner AS id, (CASE WHEN sda.countRechazos is null THEN 0 ELSE sda.countRechazos END) actual FROM catregistro cr INNER JOIN DETALLESOLICITUD cda ON cda.caseid::bigint=cr.caseid AND cda.idbanner != '' INNER JOIN solicituddeadmision sda ON sda.caseid=cda.caseid::bigint AND sda.estatussolicitud != 'Solicitud vencida' AND sda.estatussolicitud not like '%Período vencido en:%' INNER JOIN padrestutor pt ON pt.caseid=cda.caseid::bigint INNER JOIN CatParentesco cp ON cp.persistenceid=pt.catparentezco_pid INNER JOIN catestados ce ON ce.persistenceid=pt.catestado_pid LEFT JOIN aspirantespruebas ap ON ap.username=sda.correoelectronico LEFT JOIN paseLista AS PL ON PL.username = ap.username AND pl.prueba_pid = ap.prueba_pid  LEFT JOIN sesiones sesion ON ap.sesiones_pid=sesion.persistenceid INNER JOIN catpais cpa ON pt.catpais_pid=cpa.persistenceid  LEFT JOIN catpadretrabaja trabaja on trabaja.persistenceid=pt.cattrabaja_pid INNER JOIN catcampus campus on campus.persistenceid=sda.catcampus_pid INNER JOIN PadresTutorRespaldo as ptr ON ptr.caseid=cda.caseid::bigint  ${where}  UNION ALL  SELECT DISTINCT cda.idbanner AS id, (CASE WHEN sda.countRechazos is null THEN 0 ELSE sda.countRechazos END) actual FROM catregistro cr INNER JOIN DETALLESOLICITUDRESPALDO cda ON cda.caseid::bigint=cr.caseid AND cda.idbanner != '' INNER JOIN solicituddeadmisionrespaldo sda ON sda.caseid=cda.caseid::bigint AND sda.estatussolicitud != 'Solicitud vencida' AND sda.estatussolicitud not like '%Período vencido en:%' INNER JOIN padrestutor pt ON pt.caseid=cda.caseid::bigint INNER JOIN CatParentesco cp ON cp.persistenceid=pt.catparentezco_pid INNER JOIN catestados ce ON ce.persistenceid=pt.catestado_pid LEFT JOIN aspirantespruebas ap ON ap.username=sda.correoelectronico LEFT JOIN paseLista AS PL ON PL.username = ap.username AND pl.prueba_pid = ap.prueba_pid  LEFT JOIN sesiones sesion ON ap.sesiones_pid=sesion.persistenceid INNER JOIN catpais cpa ON pt.catpais_pid=cpa.persistenceid  LEFT JOIN catpadretrabaja trabaja on trabaja.persistenceid=pt.cattrabaja_pid INNER JOIN catcampus campus ON campus.persistenceid=sda.catcampus_pid INNER JOIN PadresTutorRespaldo as ptr ON ptr.caseid=cda.caseid::bigint  ${where} ) datos ORDER BY id, actual  ";
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            closeCon = validarConexion();
            pstm = con.prepareStatement(consulta)
            rs = pstm.executeQuery()
			
            rows = new ArrayList < Map < String, Object >> ();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            String idbanner = "";
			Boolean entro = false;
            while (rs.next()) {
                Map < String, Object > columns = new LinkedHashMap < String, Object > ();
				//columns.put('id', rs.getString("id"));
				String consultaFamiliares ="", ids= "";
				
				// Obtienes a los tutores
				consultaFamiliares = "SELECT distinct ptr.persistenceid FROM solicituddeadmision AS sda INNER JOIN DETALLESOLICITUD cda ON cda.caseid::bigint=sda.caseid AND cda.idbanner != '' AND sda.estatussolicitud != 'Solicitud vencida' AND sda.estatussolicitud not like '%Período vencido en:%'  LEFT JOIN padrestutorRespaldo as ptr ON ptr.caseid=cda.caseid::bigint INNER JOIN CatParentesco cp ON cp.persistenceid=ptr.catparentezco_pid LEFT JOIN catestados ce ON ce.persistenceid=ptr.catestado_pid LEFT JOIN sesionaspirante sa ON sa.username=sda.correoelectronico LEFT JOIN pruebas p ON sa.sesiones_pid=p.sesion_pid AND p.cattipoprueba_pid=4 LEFT JOIN sesiones s ON s.persistenceid=sa.sesiones_pid LEFT JOIN catpais cpa ON ptr.catpais_pid=cpa.persistenceid LEFT JOIN catpadretrabaja trabaja on trabaja.persistenceid=ptr.cattrabaja_pid INNER JOIN catcampus campus on campus.persistenceid=sda.catcampus_pid  WHERE cda.idbanner= '${rs.getString('id')}' AND ptr.countintento = ${rs.getString('actual')} AND ptr.vive_pid IS NULL AND ptr.istutor IS TRUE AND ptr.countintento is not null ORDER BY  ptr.persistenceid ASC";
				pstm2 = con.prepareStatement(consultaFamiliares)
				rs2 = pstm2.executeQuery();
				while(rs2.next()) {
					ids+= (ids.length()>0?",":"")+rs2.getString('persistenceid');
				}
				
				// Obtienes a los padres
				consultaFamiliares = "SELECT distinct ptr.persistenceid FROM solicituddeadmision AS sda INNER JOIN DETALLESOLICITUD cda ON cda.caseid::bigint=sda.caseid AND cda.idbanner != '' AND sda.estatussolicitud != 'Solicitud vencida' AND sda.estatussolicitud not like '%Período vencido en:%' LEFT JOIN padrestutorRespaldo as ptr ON ptr.caseid=cda.caseid::bigint  INNER JOIN CatParentesco cp ON cp.persistenceid=ptr.catparentezco_pid LEFT JOIN catestados ce ON ce.persistenceid=ptr.catestado_pid LEFT JOIN sesionaspirante sa ON sa.username=sda.correoelectronico LEFT JOIN pruebas p ON sa.sesiones_pid=p.sesion_pid AND p.cattipoprueba_pid=4 LEFT JOIN sesiones s ON s.persistenceid=sa.sesiones_pid LEFT JOIN catpais cpa ON ptr.catpais_pid=cpa.persistenceid LEFT JOIN catpadretrabaja trabaja on trabaja.persistenceid=ptr.cattrabaja_pid LEFT JOIN Catvive as vv ON vv.persistenceid = ptr.vive_pid INNER JOIN catcampus campus on campus.persistenceid=sda.catcampus_pid  WHERE cda.idbanner= '${rs.getString('id')}' AND ptr.countintento = ${rs.getString('actual')} AND ptr.desconozcodatospadres IS NOT NULL AND ptr.desconozcodatospadres IS FALSE AND vv.descripcion = 'Sí'  ORDER BY ptr.persistenceid ASC "
				pstm2 = con.prepareStatement(consultaFamiliares)
				rs2 = pstm2.executeQuery()
				while(rs2.next()) {
					ids+= (ids.length()>0?",":"")+rs2.getString('persistenceid');
				}
				
				errorLog+="|id:"+ids
				consultaFamiliares ="SELECT distinct ptr.countIntento AS intento, ptr.countIntento+1 as countIntento,ptr.persistenceid, CASE WHEN sda.apellidomaterno=''THEN sda.apellidopaterno || ' ' || CASE WHEN sda.segundonombre=''THEN sda.primernombre ELSE sda.primernombre || ' ' || sda.segundonombre END ELSE sda.apellidopaterno||' '||sda.apellidomaterno ||' ' || CASE WHEN sda.segundonombre=''THEN sda.primernombre ELSE sda.primernombre || ' ' || sda.segundonombre END END    AS nombre, ptr.nombre || ' '|| ptr.apellidos AS nombepadres, cp.descripcion AS relacion,case when ptr.cattrabaja_pid is null then 'No' else trabaja.descripcion end as trabaja, ptr.empresatrabaja AS empleador, ptr.puesto AS titulo, ptr.correoelectronico AS correo, ptr.calle ||' #' || ptr.numeroexterior || ' '|| ptr.colonia ||', '||ce.descripcion || ' ' || ptr.ciudad || ' ' || ptr.codigopostal direccion, ptr.telefono, sda.estatussolicitud  AS codigodedecision, ptr.calle ||' #' || ptr.numeroexterior AS calle, ptr.colonia, ptr.delegacionmunicipio, ptr.ciudad, ce.descripcion  AS estado, ptr.codigopostal AS cp, cpa.descripcion AS pais, to_char(to_date(sda.fechaultimamodificacion, 'YYYY-MM-DD\"T\"HH24:MI:SS'),'DD-MM-YYYY')   ultimamod, ''   resultadoad, CASE WHEN ptr.vive_pid IS NULL THEN 'Sí' ELSE vv.descripcion END  as vive, ptr.vive_pid as tutor FROM solicituddeadmision AS sda INNER JOIN DETALLESOLICITUD cda ON cda.caseid::bigint=sda.caseid AND cda.idbanner != '' AND sda.estatussolicitud != 'Solicitud vencida' AND sda.estatussolicitud not like '%Período vencido en:%'  LEFT JOIN padrestutorRespaldo as ptr ON ptr.caseid=cda.caseid::bigint INNER JOIN CatParentesco cp ON cp.persistenceid=ptr.catparentezco_pid LEFT JOIN catestados ce ON ce.persistenceid=ptr.catestado_pid LEFT JOIN sesionaspirante sa ON sa.username=sda.correoelectronico LEFT JOIN pruebas p ON sa.sesiones_pid=p.sesion_pid AND p.cattipoprueba_pid=4 LEFT JOIN sesiones s ON s.persistenceid=sa.sesiones_pid LEFT JOIN catpais cpa ON ptr.catpais_pid=cpa.persistenceid LEFT JOIN catVive as vv ON vv.persistenceid = ptr.vive_pid LEFT JOIN catpadretrabaja trabaja on trabaja.persistenceid=ptr.cattrabaja_pid INNER JOIN catcampus campus on campus.persistenceid=sda.catcampus_pid  WHERE ptr.persistenceid IN (${ids}) ORDER BY  ptr.countIntento, ptr.persistenceid DESC ";
				//errorLog += "|consultaFamiliares:" + consultaFamiliares + "|"
				
				pstm2 = con.prepareStatement(consultaFamiliares)
				rs2 = pstm2.executeQuery()
				ResultSetMetaData metaData2 = rs2.getMetaData();
				int columnCount2 = metaData2.getColumnCount();
				while(rs2.next()) {
					columns = new LinkedHashMap < String, Object > ();
					columns.put('id', rs.getString("id"));
					for (int j = 1; j <= columnCount2; j++) {
						//entro = true;
						if(metaData2.getColumnLabel(j).toLowerCase().equals("nombepadres") && (rs2.getString("tutor").equals("null") || rs2.getString("tutor").equals(null) ) ) {
							columns.put(metaData2.getColumnLabel(j).toLowerCase(), rs2.getString(j)+" (TUTOR)");
						}else {
							columns.put(metaData2.getColumnLabel(j).toLowerCase(), rs2.getString(j));
						}
					}
					rows.add(columns);
				}
				

                //rows.add(columns);
            }
			//errorLog +=rows.toString()
            //errorLog += "|Consulta:" + consulta + "|"
            dataResult.setData(rows)
            dataResult.setSuccess(true)
            if (dataResult.success) {
                lstParams = dataResult.getData();
                errorLog += "|Parametros:" + lstParams.toString() + "|"
            } else {
                throw new Exception("No encontro datos");
            }
            FileOutputStream fw = new FileOutputStream("out.txt");



            def titulos = ["ID", "Nombre Alumno", "Nombre de los relativos", "Relación","Intento","Vive","Trabaja", "Empleador", "Titulo", "Correo", "Teléfono","Calle número y cruzamientos","Colonia","Delegación","Ciudad","Estado","CP","País", "Estatus","Última modificación", "Resultado de Admisión"]
            if (object.encabezado) {
                fw.write((titulos.join(",") + "\r\n").getBytes());
                Row headersRow = sheet.createRow(rowCount);
                ++rowCount;
                List < Cell > header = new ArrayList < Cell > ();
                for (int i = 0; i < titulos.size(); ++i) {
                    header.add(headersRow.createCell(i))
                    header[i].setCellValue(titulos.get(i))
                    header[i].setCellStyle(style)
                }
            }
            CellStyle bodyStyle = workbook.createCellStyle();
            bodyStyle.setWrapText(true);
            bodyStyle.setAlignment(HorizontalAlignment.LEFT);

            def info = ["id", "nombre", "nombepadres", "relacion","countintento","vive","trabaja", "empleador", "titulo", "correo","telefono", "calle","colonia","delegacionmunicipio","ciudad","estado","cp","pais",  "codigodedecision", "ultimamod", "resultadoad"]
            List < Cell > body;
            String line = "" ;
            for (int i = 0; i < lstParams.size(); ++i) {
                line = ""
                Row row = sheet.createRow(rowCount);
                ++rowCount;
                body = new ArrayList < Cell > ()
                for (int j = 0; j < info.size(); ++j) {
                    body.add(row.createCell(j))
                    try {
                        body[j].setCellValue(lstParams[i][info.get(j)])
                        line += (lstParams[i][info.get(j)]) + ((info.get(j).equals("observaciones")) ? "\r\n" : ",")
						
                    } catch (Exception e) {
                        body[j].setCellValue(lstParams[i][info.get(j)])
                        line += (lstParams[i][info.get(j)]) + ((info.get(j).equals("observaciones")) ? "\r\n" : ",")
                    }

                    body[j].setCellStyle(bodyStyle);
					

                }
                fw.write(line.getBytes());
            }

            if (lstParams.size() > 0) {
                for (int i = 0; i <= 25; ++i) {
                    sheet.autoSizeColumn(i);
                }
                String fecha = lstParams[0]["fecharegistro"].toString() + "";
                String nameFile = "Reporte-" + fecha + ".xls";
                FileOutputStream outputStream = new FileOutputStream(nameFile);
                workbook.write(outputStream);
                List < Object > lstResultado = new ArrayList < Object > ();
                lstResultado.add(encodeFileToBase64Binary(nameFile));
                lstResultado.add(encodeFileToBase64Binary("out.txt"))
                resultado.setData(lstResultado)
                outputStream.close();
                fw.close();

            } else {
                throw new Exception("No encontro datos:" + errorLog + dataResult.getError());
            }




            //List<Object> lstResultado = new ArrayList<Object>();
            //lstResultado.add(encodeFileToBase64Binary(nameFile));

            resultado.setSuccess(true);
            //resultado.setData(lstResultado);
            resultado.setError_info(errorLog);

        } catch (Exception e) {
            e.printStackTrace();
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            resultado.setError_info(errorLog);
            e.printStackTrace();
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }

        return resultado;
    }
    public Result getPeriodos() {
        Boolean closeCon = false
        String consulta = "SELECT persistenceid, activo, clave, clave||' - '||descripcion descripcion, fechacreacion, fechafin, fechaimportacion, fechainicio, id, isanual, iscuatrimestral, iseliminado, isenabled, ispropedeutico, issemestral, nombrecampus, persistenceversion, usuariobanner FROM catperiodo where iseliminado!=true and activo=true"
        List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
        Result resultado = new Result();
        try {
            closeCon = validarConexion();
            pstm = con.prepareStatement(consulta)
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
            resultado.setData(rows)
            resultado.setSuccess(true)
        } catch (Exception e) {
            e.printStackTrace()
            resultado.setError("500")
            resultado.setError_info(e.getMessage())
            resultado.setSuccess(false)
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }
	//SELECT cge.* from catgestionescolar cge inner join catcampus campus on campus.grupobonita=cge.campus where campus.clave in ([IN-CLAUSE])
	public Result getCatGestionEscolarMultiple(String campus) {
		Boolean closeCon = false
		String condicion=""
		for (int i=0;i< campus.split(",").size();i++) {
			condicion+="'"+campus.split(",")[i]+"'"+((i==campus.split(",").size()-1)?"":",")
		}
		String consulta = "SELECT cge.* from catgestionescolar cge inner join catcampus campus on campus.grupobonita=cge.campus where campus.clave in ([IN-CLAUSE]) AND cge.iseliminado =false".replace("[IN-CLAUSE]",condicion)
		List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
		Result resultado = new Result();
		try {
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta)
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
			resultado.setData(rows)
			resultado.setSuccess(true)
		} catch (Exception e) {
			e.printStackTrace()
			resultado.setError("500")
			resultado.setError_info(e.getMessage() + consulta)
			resultado.setSuccess(false)
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	public Result generarReporteRelacionAspirantes(String jsonData) {
		Result resultado = new Result();
		String errorLog = "", where = "", where2 = "";;
		Boolean closeCon = false;
		try {
			Result dataResult = new Result();
			int rowCount = 0;
			List < Object > lstParams;
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
			XSSFColor color = new XSSFColor(new java.awt.Color(191, 220, 249), colorMap);

			style.setFillForegroundColor(color)
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			String condicion=""
			for (int i=0;i< object.multicampus.split(",").size();i++) {
				condicion+="'"+object.multicampus.split(",")[i]+"'"+((i==object.multicampus.split(",").size()-1)?"":",")
			}
            //where += " WHERE sda.catcampus_pid=" + object.campus
			where += " WHERE (campus.clave in (${condicion }) OR campus2.clave in (${condicion })) AND (ap.asistencia IS TRUE OR (PL.asistencia IS NULL AND ap.acreditado IS NULL) OR (cda.cbcoincide IS TRUE AND ap.acreditado IS TRUE))"
			where += (object.periodo == null || object.periodo.equals("")) ? "" : " AND (sda.catperiodo_pid in (${object.periodo}) OR sda2.catperiodo_pid in (${object.periodo}) )"
			where += (object.carrera == null || object.carrera.equals("")) ? "" : " AND (sda.catgestionescolar_pid in (${object.carrera}) OR sda2.catgestionescolar_pid in (${object.carrera}) ) "
			where += (object.preparatoria == null || object.preparatoria.equals("")) ? "" : " AND (sda.catbachilleratos_pid in (${object.preparatoria}) OR sda2.catbachilleratos_pid in (${object.preparatoria}) )"
			where += (object.idbanner == null || object.idbanner.equals("")) ? "" : " AND cda.idbanner = '${object.idbanner }' "
			where += (object.sesion == null || object.sesion.equals("")) ? "" : " AND sesion.persistenceid in (" + object.sesion + ")";
			
			// se carga el where para las subconsultas
			where2 += " campus.clave in (" + condicion +") AND (ap.asistencia IS TRUE OR (PL.asistencia IS NULL AND ap.acreditado IS NULL) OR (cda.cbcoincide IS TRUE AND ap.acreditado IS TRUE))"
			where2 += (object.periodo == null || object.periodo.equals("")) ? "" : " AND sda.catperiodo_pid in (" + object.periodo + ")"
			where2 += (object.carrera == null || object.carrera.equals("")) ? "" : " AND sda.catgestionescolar_pid in (" + object.carrera + ")"
			where2 += (object.preparatoria == null || object.preparatoria.equals("")) ? "" : " AND sda.catbachilleratos_pid in (" + object.preparatoria + ")"
			where2 += (object.idbanner == null || object.idbanner.equals("")) ? "" : " AND cda.idbanner = '" + object.idbanner + "'"
			where2 += (object.sesion == null || object.sesion.equals("")) ? "" : " AND sesion.persistenceid in (" + object.sesion + ")  "
			
			int numero = 1;

			//String consulta = "SELECT *, row_number() over (ORDER BY nomatricula) nosolicitantes FROM (SELECT DISTINCT entrevista.aplicacion fechaentrevista, invp.aplicacion AS fechainvp, eac.aplicacion AS fechaeac, periodo.clave AS periodo, cda.idbanner AS nomatricula, CASE WHEN cr.apellidomaterno=''THEN cr.apellidopaterno ELSE cr.apellidopaterno||' '||cr.apellidomaterno END AS apellidos, CASE WHEN cr.segundonombre=''THEN cr.primernombre ELSE cr.primernombre || ' ' || cr.segundonombre END AS nombres, carrera.nombre AS carrera, tipoadmision.descripcion AS tipodeadmision, preparatoria.descripcion AS preparatoria, sda.promediogeneral AS promedio, sesion.nombre AS sesion, '20-may-21' AS fechaaplicacionexamenpaan, 'RE-Rechazado' AS resultado, '29-may-21' AS fechadeadmision, sda.fechanacimiento, sda.correoelectronico email, sda.telefono, sda.telefonocelular celular FROM catregistro cr INNER JOIN DETALLESOLICITUD cda ON cda.caseid::bigint=cr.caseid LEFT JOIN solicituddeadmision sda ON sda.caseid=cda.caseid::bigint LEFT JOIN aspirantespruebas sa ON sa.username=sda.correoelectronico AND sa.asistencia=true LEFT JOIN sesiones sesion ON sa.sesiones_pid=sesion.persistenceid LEFT JOIN catcampus campus ON campus.persistenceid=sda.catcampus_pid INNER JOIN catperiodo periodo ON sda.catPeriodo_pid=periodo.persistenceid LEFT JOIN catgestionescolar gestionescolar ON sda.catgestionescolar_pid=gestionescolar.persistenceid LEFT JOIN catbachilleratos preparatoria ON sda.catbachilleratos_pid=preparatoria.persistenceid LEFT JOIN autodescripcion autodescripcion ON autodescripcion.caseid=sda.caseid LEFT JOIN catreligion religion ON religion.persistenceid=autodescripcion.catreligion_pid LEFT JOIN catsexo sexo ON sexo.persistenceid=sda.catsexo_pid LEFT JOIN cattipoadmision tipoadmision ON cattipoadmision_pid=tipoadmision.persistenceid LEFT JOIN catcampus campusingreso ON campusingreso.persistenceid=sda.catcampusestudio_pid LEFT JOIN cattipoalumno tipoalumno ON tipoalumno.persistenceid=cda.cattipoalumno_pid LEFT JOIN catgestionescolar carrera ON carrera.persistenceid=sda.catgestionescolar_pid LEFT JOIN pruebas eac ON eac.persistenceid= (SELECT prueba_pid FROM aspirantespruebas WHERE username=sda.correoelectronico AND cattipoprueba_pid=4 AND (asistencia=true OR acreditado = true) ORDER BY persistenceid ASC limit 1) LEFT JOIN pruebas invp ON invp.persistenceid= (SELECT prueba_pid FROM aspirantespruebas WHERE username=sda.correoelectronico AND cattipoprueba_pid=2 AND (asistencia=true OR acreditado = true) ORDER BY persistenceid ASC limit 1) LEFT JOIN pruebas entrevista ON entrevista.persistenceid= (SELECT prueba_pid FROM aspirantespruebas WHERE username=sda.correoelectronico AND cattipoprueba_pid=1 AND (asistencia=true OR acreditado = true) ORDER BY persistenceid ASC limit 1) "+ where+" )datos "
			String consulta = "SELECT DISTINCT sda.persistenceid,sda.caseid FROM catregistro cr INNER JOIN DETALLESOLICITUD cda ON cda.caseid::bigint=cr.caseid  LEFT JOIN solicituddeadmision sda ON sda.caseid=cda.caseid::bigint LEFT JOIN solicituddeadmisionRespaldo as sda2 ON sda2.caseid=cda.caseid::bigint LEFT JOIN aspirantespruebas ap ON ap.username=sda.correoelectronico LEFT JOIN paseLista AS PL ON PL.username = ap.username AND pl.prueba_pid = ap.prueba_pid  LEFT JOIN sesiones sesion ON ap.sesiones_pid=sesion.persistenceid LEFT JOIN catcampus campus ON campus.persistenceid=sda.catcampus_pid LEFT JOIN catcampus campus2 ON campus2.persistenceid=sda2.catcampus_pid  INNER JOIN catperiodo periodo ON sda.catPeriodo_pid=periodo.persistenceid LEFT JOIN catgestionescolar gestionescolar ON sda.catgestionescolar_pid=gestionescolar.persistenceid LEFT JOIN catbachilleratos preparatoria ON sda.catbachilleratos_pid=preparatoria.persistenceid LEFT JOIN autodescripcion autodescripcion ON autodescripcion.caseid=sda.caseid LEFT JOIN catreligion religion ON religion.persistenceid=autodescripcion.catreligion_pid LEFT JOIN catsexo sexo ON sexo.persistenceid=sda.catsexo_pid LEFT JOIN cattipoadmision tipoadmision ON cda.cattipoadmision_pid=tipoadmision.persistenceid LEFT JOIN catcampus campusingreso ON campusingreso.persistenceid=sda.catcampusestudio_pid LEFT JOIN cattipoalumno tipoalumno ON tipoalumno.persistenceid=cda.cattipoalumno_pid LEFT JOIN catgestionescolar carrera ON carrera.persistenceid=sda.catgestionescolar_pid LEFT JOIN pruebas eac ON eac.persistenceid= (SELECT prueba_pid FROM aspirantespruebas WHERE username=sda.correoelectronico AND cattipoprueba_pid=4 AND (asistencia=true OR acreditado = true) ORDER BY persistenceid ASC limit 1) LEFT JOIN pruebas invp ON invp.persistenceid= (SELECT prueba_pid FROM aspirantespruebas WHERE username=sda.correoelectronico AND cattipoprueba_pid=2 AND (asistencia=true OR acreditado = true) ORDER BY persistenceid ASC limit 1) LEFT JOIN pruebas entrevista ON entrevista.persistenceid= (SELECT prueba_pid FROM aspirantespruebas WHERE username=sda.correoelectronico AND cattipoprueba_pid=1 AND (asistencia=true OR acreditado = true) ORDER BY persistenceid ASC limit 1) "+ where+"  order by sda.persistenceid "
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			pstm = con.prepareStatement(consulta);
			rs = pstm.executeQuery();
			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();
				
				String consultaAspirante ="";
				
				
				
				consultaAspirante = "SELECT DISTINCT entrevista.aplicacion fechaentrevista, invp.aplicacion AS fechainvp, eac.aplicacion AS fechaeac, periodo.clave AS periodo, cda.idbanner AS nomatricula, CASE WHEN cr.apellidomaterno=''THEN cr.apellidopaterno ELSE cr.apellidopaterno||' '||cr.apellidomaterno END AS apellidos, CASE WHEN cr.segundonombre=''THEN cr.primernombre ELSE cr.primernombre || ' ' || cr.segundonombre END AS nombres, carrera.nombre AS carrera, tipoadmision.descripcion AS tipodeadmision, preparatoria.descripcion AS preparatoria, sda.promediogeneral AS promedio, sesion.nombre AS sesion, '20-may-21' AS fechaaplicacionexamenpaan, 'N/A' AS resultado, 'N/A' AS fechadeadmision, sda.fechanacimiento, sda.correoelectronico email, sda.telefono, sda.telefonocelular celular,  CASE WHEN sda.countRechazos IS NULL THEN 1 ELSE sda.countRechazos+1 END countrechazos FROM catregistro cr INNER JOIN DETALLESOLICITUDRespaldo cda ON cda.caseid::bigint=cr.caseid LEFT JOIN solicitudDeAdmisionRespaldo sda ON sda.caseid=cda.caseid::bigint LEFT JOIN aspirantespruebas ap ON ap.username=sda.correoelectronico LEFT JOIN paseLista AS PL ON PL.username = ap.username AND pl.prueba_pid = ap.prueba_pid  LEFT JOIN sesiones sesion ON ap.sesiones_pid=sesion.persistenceid LEFT JOIN catcampus campus ON campus.persistenceid=sda.catcampus_pid INNER JOIN catperiodo periodo ON sda.catPeriodo_pid=periodo.persistenceid LEFT JOIN catgestionescolar gestionescolar ON sda.catgestionescolar_pid=gestionescolar.persistenceid LEFT JOIN catbachilleratos preparatoria ON sda.catbachilleratos_pid=preparatoria.persistenceid LEFT JOIN autodescripcionRespaldo autodescripcion ON autodescripcion.caseid=sda.caseid LEFT JOIN catreligion religion ON religion.persistenceid=autodescripcion.catreligion_pid LEFT JOIN catsexo sexo ON sexo.persistenceid=sda.catsexo_pid LEFT JOIN cattipoadmision tipoadmision ON cattipoadmision_pid=tipoadmision.persistenceid LEFT JOIN catcampus campusingreso ON campusingreso.persistenceid=sda.catcampusestudio_pid LEFT JOIN cattipoalumno tipoalumno ON tipoalumno.persistenceid=cda.cattipoalumno_pid LEFT JOIN catgestionescolar carrera ON carrera.persistenceid=sda.catgestionescolar_pid LEFT JOIN pruebas eac ON eac.persistenceid= (SELECT prueba_pid FROM aspirantespruebas WHERE username=sda.correoelectronico AND cattipoprueba_pid=4 AND (asistencia=true OR acreditado = true) ORDER BY persistenceid ASC limit 1) LEFT JOIN pruebas invp ON invp.persistenceid= (SELECT prueba_pid FROM aspirantespruebas WHERE username=sda.correoelectronico AND cattipoprueba_pid=2 AND (asistencia=true OR acreditado = true) ORDER BY persistenceid ASC limit 1) LEFT JOIN pruebas entrevista ON entrevista.persistenceid= (SELECT prueba_pid FROM aspirantespruebas WHERE username=sda.correoelectronico AND cattipoprueba_pid=1 AND (asistencia=true OR acreditado = true) ORDER BY persistenceid ASC limit 1) WHERE ${where2} AND SDA.caseid ="+rs.getString("caseid");
				errorLog += "|Consulta Respaldo: ${consultaAspirante} |"
				pstm2 = con.prepareStatement(consultaAspirante)
				rs2 = pstm2.executeQuery()
				ResultSetMetaData metaData2 = rs2.getMetaData();
				int columnCount2 = metaData2.getColumnCount();
				while(rs2.next()) {
					columns = new LinkedHashMap < String, Object > ();
					for (int j = 1; j <= columnCount2; j++) {
						if(metaData2.getColumnLabel(j).toLowerCase().equals("nomatricula") ) {
							columns.put(metaData2.getColumnLabel(j).toLowerCase(), rs2.getString(j));
							columns.put("nosolicitantes", numero.toString());
							numero++;
						}else {
							columns.put(metaData2.getColumnLabel(j).toLowerCase(), rs2.getString(j));
						}
					}
					rows.add(columns);
				}
				
				
				consultaAspirante = "SELECT DISTINCT entrevista.aplicacion fechaentrevista, invp.aplicacion AS fechainvp, eac.aplicacion AS fechaeac, periodo.clave AS periodo, cda.idbanner AS nomatricula, CASE WHEN cr.apellidomaterno=''THEN cr.apellidopaterno ELSE cr.apellidopaterno||' '||cr.apellidomaterno END AS apellidos, CASE WHEN cr.segundonombre=''THEN cr.primernombre ELSE cr.primernombre || ' ' || cr.segundonombre END AS nombres, carrera.nombre AS carrera, tipoadmision.descripcion AS tipodeadmision, preparatoria.descripcion AS preparatoria, sda.promediogeneral AS promedio, sesion.nombre AS sesion, '20-may-21' AS fechaaplicacionexamenpaan, 'N/A' AS resultado, 'N/A' AS fechadeadmision, sda.fechanacimiento, sda.correoelectronico email, sda.telefono, sda.telefonocelular celular, CASE WHEN sda.countrechazos IS NULL THEN 1 ELSE sda.countrechazos+1 END countrechazos FROM catregistro cr INNER JOIN DETALLESOLICITUD cda ON cda.caseid::bigint=cr.caseid LEFT JOIN solicituddeadmision sda ON sda.caseid=cda.caseid::bigint LEFT JOIN aspirantespruebas ap ON ap.username=sda.correoelectronico LEFT JOIN paseLista AS PL ON PL.username = ap.username AND pl.prueba_pid = ap.prueba_pid  LEFT JOIN sesiones sesion ON ap.sesiones_pid=sesion.persistenceid  LEFT JOIN catcampus campus ON campus.persistenceid=sda.catcampus_pid INNER JOIN catperiodo periodo ON sda.catPeriodo_pid=periodo.persistenceid LEFT JOIN catgestionescolar gestionescolar ON sda.catgestionescolar_pid=gestionescolar.persistenceid LEFT JOIN catbachilleratos preparatoria ON sda.catbachilleratos_pid=preparatoria.persistenceid LEFT JOIN autodescripcion autodescripcion ON autodescripcion.caseid=sda.caseid LEFT JOIN catreligion religion ON religion.persistenceid=autodescripcion.catreligion_pid LEFT JOIN catsexo sexo ON sexo.persistenceid=sda.catsexo_pid LEFT JOIN cattipoadmision tipoadmision ON cattipoadmision_pid=tipoadmision.persistenceid LEFT JOIN catcampus campusingreso ON campusingreso.persistenceid=sda.catcampusestudio_pid LEFT JOIN cattipoalumno tipoalumno ON tipoalumno.persistenceid=cda.cattipoalumno_pid LEFT JOIN catgestionescolar carrera ON carrera.persistenceid=sda.catgestionescolar_pid LEFT JOIN pruebas eac ON eac.persistenceid= (SELECT prueba_pid FROM aspirantespruebas WHERE username=sda.correoelectronico AND cattipoprueba_pid=4 AND (asistencia=true OR acreditado = true) ORDER BY persistenceid ASC limit 1) LEFT JOIN pruebas invp ON invp.persistenceid= (SELECT prueba_pid FROM aspirantespruebas WHERE username=sda.correoelectronico AND cattipoprueba_pid=2 AND (asistencia=true OR acreditado = true) ORDER BY persistenceid ASC limit 1) LEFT JOIN pruebas entrevista ON entrevista.persistenceid= (SELECT prueba_pid FROM aspirantespruebas WHERE username=sda.correoelectronico AND cattipoprueba_pid=1 AND (asistencia=true OR acreditado = true) ORDER BY persistenceid ASC limit 1) WHERE ${where2} AND SDA.caseid ="+rs.getString("caseid");	
				pstm2 = con.prepareStatement(consultaAspirante)
				rs2 = pstm2.executeQuery()
				metaData2 = rs2.getMetaData();
				columnCount2 = metaData2.getColumnCount();
				while(rs2.next()) {
					columns = new LinkedHashMap < String, Object > ();
					for (int j = 1; j <= columnCount2; j++) {
						
						if(metaData2.getColumnLabel(j).toLowerCase().equals("nomatricula") ) {
							columns.put(metaData2.getColumnLabel(j).toLowerCase(), rs2.getString(j));
							columns.put("nosolicitantes", numero.toString());
							numero++;
						}else {
							columns.put(metaData2.getColumnLabel(j).toLowerCase(), rs2.getString(j));
						}
					}
					rows.add(columns);
				}
				
				

				//rows.add(columns);
			}
			errorLog += "|Consulta:" + consulta + "|"
			dataResult.setData(rows)
			dataResult.setSuccess(true)
			if (dataResult.success) {
				lstParams = dataResult.getData();
				errorLog += "|Parametros:" + lstParams.toString() + "|"
			} else {
				throw new Exception("No encontro datos");
			}
			FileOutputStream fw = new FileOutputStream("out.txt");



			def titulos = ["No. Solicitante","Periodo","Id Aspirante","Apellidos","Nombres","Intento","Carrera","Tipo de admisión","Preparatoria de procedencia", "Promedio","Sesión", "Fecha aplicación examen EAC","Fecha aplicación INVP","Fecha Entrevista","Resultado", "Fecha de admisión", "Correo personal","Teléfono permanente","Teléfono celular"]
			if (object.encabezado) {
				fw.write((titulos.join(",") + "\r\n").getBytes());
				Row headersRow = sheet.createRow(rowCount);
				++rowCount;
				List < Cell > header = new ArrayList < Cell > ();
				for (int i = 0; i < titulos.size(); ++i) {
					header.add(headersRow.createCell(i))
					header[i].setCellValue(titulos.get(i))
					header[i].setCellStyle(style)
				}
			}
			CellStyle bodyStyle = workbook.createCellStyle();
			bodyStyle.setWrapText(true);
			bodyStyle.setAlignment(HorizontalAlignment.LEFT);

			def info = ["nosolicitantes","periodo","nomatricula","apellidos","nombres","countrechazos","carrera","tipodeadmision","preparatoria","promedio","sesion","fechaeac","fechainvp","fechaentrevista","resultado", "fechadeadmision","email","telefono","celular"]
			List < Cell > body;
			String line = ""
			for (int i = 0; i < lstParams.size(); ++i) {
				line = ""
				Row row = sheet.createRow(rowCount);
				++rowCount;
				body = new ArrayList < Cell > ()
				for (int j = 0; j < info.size(); ++j) {
					body.add(row.createCell(j))
					try {
						body[j].setCellValue( lstParams[i][info.get(j)])
						line += (lstParams[i][info.get(j)]) + ((info.get(j).equals("observaciones")) ? "\r\n" : ",")
					} catch (Exception e) {
						body[j].setCellValue( lstParams[i][info.get(j)])
						line += (lstParams[i][info.get(j)]) + ((info.get(j).equals("observaciones")) ? "\r\n" : ",")
					}

					body[j].setCellStyle(bodyStyle);

				}
				fw.write(line.getBytes());
			}

			if (lstParams.size() > 0) {
				for (int i = 0; i <= 138; ++i) {
					sheet.autoSizeColumn(i);
				}
				String fecha = lstParams[0]["fecharegistro"].toString() + "";
				String nameFile = "Reporte-" + fecha + ".xls";
				FileOutputStream outputStream = new FileOutputStream(nameFile);
				workbook.write(outputStream);
				List < Object > lstResultado = new ArrayList < Object > ();
				lstResultado.add(encodeFileToBase64Binary(nameFile));
				lstResultado.add(encodeFileToBase64Binary("out.txt"))
				resultado.setData(lstResultado)
				outputStream.close();
				fw.close();

			} else {
				throw new Exception("No encontro datos:" + errorLog + dataResult.getError());
			}




			//List<Object> lstResultado = new ArrayList<Object>();
			//lstResultado.add(encodeFileToBase64Binary(nameFile));

			resultado.setSuccess(true);
			//resultado.setData(lstResultado);
			resultado.setError_info(errorLog);

		} catch (Exception e) {
			e.printStackTrace();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog);
			e.printStackTrace();
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}

		return resultado;
	}
	
	
	

}