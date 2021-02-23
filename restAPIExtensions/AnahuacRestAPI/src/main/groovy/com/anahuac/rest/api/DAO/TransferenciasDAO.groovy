package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import java.text.SimpleDateFormat

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.search.Order
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.Transferencias
import com.anahuac.rest.api.Entity.db.CatBitacoraCorreo
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class TransferenciasDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferenciasDAO.class);
    Connection con;
    Statement stm;
    ResultSet rs;
    PreparedStatement pstm;

    public Result getUsuariosTransferencia(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = "", bachillerato = "", campus = "", programa = "", ingreso = "", estado = "", tipoalumno = "", orderby = "ORDER BY ", errorlog = ""
        List < String > lstGrupo = new ArrayList < String > ();
        List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();
        List < DetalleSolicitud > lstDetalleSolicitud = new ArrayList < DetalleSolicitud > ();

        Long userLogged = 0L;
        Long caseId = 0L;
        Long total = 0L;
        Map < String, String > objGrupoCampus = new HashMap < String, String > ();
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);

            List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)

            userLogged = context.getApiSession().getUserId();

            List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for (UserMembership objUserMembership: lstUserMembership) {
                for (CatCampus rowGrupo: lstCatCampus) {
                    if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
                        lstGrupo.add(rowGrupo.getDescripcion());
                        break;
                    }
                }
            }

            assert object instanceof Map;
            where += " WHERE sda.iseliminado=false "
            where += " AND (sda.ESTATUSSOLICITUD <> 'estatus1' AND sda.ESTATUSSOLICITUD <> 'estatus2' AND sda.ESTATUSSOLICITUD <> 'estatus3')"
            //				AND (sda.ESTATUSSOLICITUD <> 'Solicitud lista roja' AND sda.ESTATUSSOLICITUD <> 'Solicitud rechazada' AND sda.ESTATUSSOLICITUD <> 'Aspirantes registrados sin validación de cuenta' AND sda.ESTATUSSOLICITUD <> 'Aspirantes registrados con validación de cuenta' AND sda.ESTATUSSOLICITUD <> 'Solicitud en progreso' AND sda.ESTATUSSOLICITUD <> 'Aspirante migrado'
            //if(object.estatusSolicitud !=null) {

            //					where+="AND (sda.ESTATUSSOLICITUD <> 'Solicitud lista roja' AND sda.ESTATUSSOLICITUD <> 'Solicitud rechazada' AND sda.ESTATUSSOLICITUD <> 'Aspirantes registrados sin validación de cuenta' AND sda.ESTATUSSOLICITUD <> 'Aspirantes registrados con validación de cuenta')"

            /*if(object.estatusSolicitud.equals("Solicitud lista roja")) {
            	where+=" AND sda.ESTATUSSOLICITUD='Solicitud lista roja'"
            }
            else if(object.estatusSolicitud.equals("Solicitud rechazada")) {
            	where+=" AND sda.ESTATUSSOLICITUD='Solicitud rechazada'"
            } else if(object.estatusSolicitud.equals("Aspirantes registrados sin validación de cuenta")) {
            	where+=" AND (sda.ESTATUSSOLICITUD='Aspirantes registrados sin validación de cuenta')"
            } else if(object.estatusSolicitud.equals("Aspirantes registrados con validación de cuenta")) {
            	where+=" AND (sda.ESTATUSSOLICITUD='Aspirantes registrados con validación de cuenta')"
            }else if(object.estatusSolicitud.equals("Solicitud en espera de pago")) {
            	where+=" AND (sda.ESTATUSSOLICITUD='Solicitud en espera de pago')"
            }
            else if(object.estatusSolicitud.equals("Solicitud con pago aceptado")) {
            	where+=" AND (sda.ESTATUSSOLICITUD='Solicitud con pago aceptado')"
            }
            else if(object.estatusSolicitud.equals("Autodescripción en proceso")) {
            	where+=" AND (sda.ESTATUSSOLICITUD='Autodescripción en proceso')"
            }
            else if(object.estatusSolicitud.equals("Elección de pruebas no calendarizado")) {
            	where+=" AND (sda.ESTATUSSOLICITUD='Elección de pruebas no calendarizado')"
            }
            else if(object.estatusSolicitud.equals("No se ha impreso credencial")) {
            	where+=" AND (sda.ESTATUSSOLICITUD='No se ha impreso credencial')"
            }
            else if(object.estatusSolicitud.equals("Ya se imprimió su credencial")) {
            	where+=" AND (sda.ESTATUSSOLICITUD='Ya se imprimió su credencial')"
            }*/
            //			}
            if (lstGrupo.size() > 0) {
                campus += " AND ("
            }
            for (Integer i = 0; i < lstGrupo.size(); i++) {
                String campusMiembro = lstGrupo.get(i);
                campus += "campus.descripcion='" + campusMiembro + "'"
                if (i == (lstGrupo.size() - 1)) {
                    campus += ") "
                } else {
                    campus += " OR "
                }
            }

            //errorlog+="campus" + campus;
            errorlog += "object.lstFiltro" + object.lstFiltro
            List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
            closeCon = validarConexion();
            String consulta = Statements.GET_SOLICITUDES_TRANSFERENCIA
            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                errorlog += ", columna " + filtro.get("columna")
                switch (filtro.get("columna")) {

                    case "NOMBRE":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(concat(sda.primernombre,' ', sda.segundonombre,' ',sda.apellidopaterno,' ',sda.apellidomaterno)) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "EMAIL":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.correoelectronico) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CURP":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.curp) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CAMPUS":
                        campus += " AND LOWER(campus.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            campus += "=LOWER('[valor]')"
                        } else {
                            campus += "LIKE LOWER('%[valor]%')"
                        }
                        campus = campus.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PREPARATORIA":
                        bachillerato += " AND LOWER(prepa.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            bachillerato += "=LOWER('[valor]')"
                        } else {
                            bachillerato += "LIKE LOWER('%[valor]%')"
                        }
                        bachillerato = bachillerato.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROGRAMA":
                        programa += " AND LOWER(gestionescolar.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            programa += "=LOWER('[valor]')"
                        } else {
                            programa += "LIKE LOWER('%[valor]%')"
                        }
                        programa = programa.replace("[valor]", filtro.get("valor"))
                        break;
                    case "INGRESO":
                        ingreso += " AND LOWER(periodo.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            ingreso += "=LOWER('[valor]')"
                        } else {
                            ingreso += "LIKE LOWER('%[valor]%')"
                        }
                        ingreso = ingreso.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ESTADO":
                        estado += " AND LOWER(estado.DESCRIPCION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            estado += "=LOWER('[valor]')"
                        } else {
                            estado += "LIKE LOWER('%[valor]%')"
                        }
                        estado = estado.replace("[valor]", filtro.get("valor"))
                        break;
                    case "PROMEDIO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.PROMEDIOGENERAL) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "ESTATUS":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.ESTATUSSOLICITUD) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "TELEFONO":
                        if (where.contains("WHERE")) {
                            where += " AND "
                        } else {
                            where += " WHERE "
                        }
                        where += " LOWER(sda.telefonocelular) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')"
                        } else {
                            where += "LIKE LOWER('%[valor]%')"
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "TIPO":
                        tipoalumno += " AND LOWER(da.TIPOALUMNO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "IDBANNER":
                        tipoalumno += " AND LOWER(da.idbanner) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "LISTAROJA":
                        tipoalumno += " AND LOWER(da.observacionesListaRoja) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    case "RECHAZO":
                        tipoalumno += " AND LOWER(da.observacionesRechazo) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            tipoalumno += "=LOWER('[valor]')"
                        } else {
                            tipoalumno += "LIKE LOWER('%[valor]%')"
                        }
                        tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
                        break;
                    default:
                        //consulta=consulta.replace("[BACHILLERATO]", bachillerato)
                        //consulta=consulta.replace("[WHERE]", where);

                        break;
                }
            }
            switch (object.orderby) {
                case "NOMBRE":
                    orderby += "sda.primernombre";
                    break;
                case "EMAIL":
                    orderby += "sda.correoelectronico";
                    break;
                case "CURP":
                    orderby += "sda.curp";
                    break;
                case "CAMPUS":
                    orderby += "campus.DESCRIPCION"
                    break;
                case "PREPARATORIA":
                    orderby += "prepa.DESCRIPCION"
                    break;
                case "PROGRAMA":
                    orderby += "gestionescolar.DESCRIPCION"
                    break;
                case "INGRESO":
                    orderby += "periodo.DESCRIPCION"
                    break;
                case "ESTADO":
                    orderby += "estado.DESCRIPCION";
                    break;
                case "PROMEDIO":
                    orderby += "sda.PROMEDIOGENERAL";
                    break;
                case "ESTATUS":
                    orderby += "sda.ESTATUSSOLICITUD";
                    break;
                case "TIPO":
                    orderby += "da.TIPOALUMNO";
                    break;
                case "TELEFONO":
                    orderby += "sda.telefonocelular";
                    break;
                case "IDBANNER":
                    orderby += "da.idbanner";
                    break;
                case "LISTAROJA":
                    orderby += "da.observacionesListaRoja";
                    break;
                case "RECHAZO":
                    orderby += "da.observacionesRechazo";
                    break;
                default:
                    orderby += "sda.persistenceid"
                    break;
            }
            orderby += " " + object.orientation;
            consulta = consulta.replace("[CAMPUS]", campus)
            consulta = consulta.replace("[PROGRAMA]", programa)
            consulta = consulta.replace("[INGRESO]", ingreso)
            consulta = consulta.replace("[ESTADO]", estado)
            consulta = consulta.replace("[BACHILLERATO]", bachillerato)
            consulta = consulta.replace("[TIPOALUMNO]", tipoalumno)
            where += " " + campus + " " + programa + " " + ingreso + " " + estado + " " + bachillerato + " " + tipoalumno
            consulta = consulta.replace("[WHERE]", where);
            //pstm = con.prepareStatement(consulta.replace("SELECT sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.DESCRIPCION AS licenciatura, periodo.DESCRIPCION AS ingreso, estado.DESCRIPCION AS estado, prepa.DESCRIPCION AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, le.descripcion, ciudadestado.descripcion, ciudadpais.descripcion, estadoexamen.descripcion, pais.descripcion FROM SOLICITUDDEADMISION sda ", "SELECT COUNT(sda.persistenceid) as registros FROM SOLICITUDDEADMISION sda ").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("GROUP BY sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion, campus.descripcion, gestionescolar.DESCRIPCION, periodo.DESCRIPCION, estado.DESCRIPCION, prepa.DESCRIPCION, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, le.descripcion, ciudadestado.descripcion, ciudadpais.descripcion, estadoexamen.descripcion, pais.descripcion", "GROUP BY sda.persistenceid"))
            //errorlog+=consulta
            String consultaCount = Statements.GET_COUNT_SOLICITUDES_TRASNFERENCIA;
            consultaCount = consultaCount.replace("[CAMPUS]", campus)
            consultaCount = consultaCount.replace("[PROGRAMA]", programa)
            consultaCount = consultaCount.replace("[INGRESO]", ingreso)
            consultaCount = consultaCount.replace("[ESTADO]", estado)
            consultaCount = consultaCount.replace("[BACHILLERATO]", bachillerato)
            consultaCount = consultaCount.replace("[TIPOALUMNO]", tipoalumno)
            consultaCount = consultaCount.replace("[WHERE]", where)
            consultaCount = consultaCount.replace("[LIMITOFFSET]", "");
            consultaCount = consultaCount.replace("[ORDERBY]", "");
            //				errorlog+=" AQUI ES LA CONSULTA PARA CONTAR "+consultaCount
            pstm = con.prepareStatement(consultaCount);
            rs = pstm.executeQuery()
            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"))
            }
            consulta = consulta.replace("[ORDERBY]", orderby)
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
            errorlog += " ///////*/*/*/*/*/ la consulta es: " + consulta
            pstm = con.prepareStatement(consulta)
            pstm.setInt(1, object.limit)
            pstm.setInt(2, object.offset)

            rs = pstm.executeQuery()
            rows = new ArrayList < Map < String, Object >> ();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                Map < String, Object > columns = new LinkedHashMap < String, Object > ();

                for (int i = 1; i <= columnCount; i++) {
                    columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
                    if (metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
                        String encoded = "";
                        try {
                            for (Document doc: context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
                                encoded = "../API/formsDocumentImage?document=" + doc.getId();
                                columns.put("fotografiab64", encoded);
                            }
                        } catch (Exception e) {
                            columns.put("fotografiab64", "");
                            errorlog += "" + e.getMessage();
                        }
                    }
                }

                rows.add(columns);
            }
            resultado.setSuccess(true)

            resultado.setError_info(errorlog);
            //resultado.setError(consulta);
            resultado.setData(rows)

        } catch (Exception e) {
            resultado.setError_info(errorlog)
            //resultado.setError_info(consulta)
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result transferirAspirante(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        String errorLog = "";
        Boolean closeCon = false;
        try {
            Transferencias transferencia = new Transferencias();
            ProcessAPI processAPI = context.getApiClient().getProcessAPI()
            Boolean avanzartarea = false;
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
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            assert object instanceof Map;

            org.bonitasoft.engine.api.APIClient apiClient = new APIClient() //context.getApiClient();
            apiClient.login(username, password)

            SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
            searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID, object.caseid);
            searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);
            final SearchOptions searchOptions = searchBuilder.done();
            SearchResult < HumanTaskInstance > SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
            List < HumanTaskInstance > lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();

            if (!object.campusAnterior.equals(object.campusNuevo)) {
                for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {
                    if (objHumanTaskInstance.getName().equals("Generar credencial") || objHumanTaskInstance.getName().equals("Pase de lista Prueba 1") || objHumanTaskInstance.getName().equals("Pase de lista Prueba 2") || objHumanTaskInstance.getName().equals("Pase de lista Prueba 3") || objHumanTaskInstance.getName().equals("Carga y consulta de resultados") || objHumanTaskInstance.getName().equals("Resultado final de comité")) {
                        Map < String, Serializable > inputs = new HashMap < String, Serializable > ();
                        if (objHumanTaskInstance.getName().equals("Carga y consulta de resultados")) {
                            inputs.put("isTransferencia", true);
                        } else if (objHumanTaskInstance.getName().equals("Resultado final de comité")) {
                            inputs.put("isTransferencia", true);
                        }

                        processAPI.assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
                        processAPI.executeUserTask(objHumanTaskInstance.getId(), inputs);
                    }
                }
            }

            errorLog += " Antes del update "
            closeCon = validarConexion();
            errorLog += " closeCon " + closeCon
            con.setAutoCommit(false)
            pstm = con.prepareStatement(Statements.UPDATE_DATOS_TRASNFERENCIA)
            pstm.setLong(1, object.campus);
            pstm.setLong(2, object.licenciatura);
            if (object.propedeutico == null) {
                pstm.setNull(3, java.sql.Types.BIGINT);
            } else {
                pstm.setLong(3, object.propedeutico);
            }
            pstm.setLong(4, object.periodo);
//            pstm.setLong(5, object.lugarexamen);
//            if (object.estadoexamen == null) {
//                pstm.setNull(6, java.sql.Types.BIGINT);
//            } else {
//                pstm.setLong(6, object.estadoexamen);
//            }
//            if (object.ciudadexamen == null) {
//                pstm.setNull(7, java.sql.Types.BIGINT);
//            } else {
//                pstm.setLong(7, object.ciudadexamen);
//            }
//            if (object.paisexamen == null) {
//                pstm.setNull(8, java.sql.Types.BIGINT);
//            } else {
//                pstm.setLong(8, object.paisexamen);
//            }
//            if (object.ciudadpaisexamen == null) {
//                pstm.setNull(9, java.sql.Types.BIGINT);
//            } else {
//                pstm.setLong(9, object.ciudadpaisexamen);
//            }
            pstm.setLong(5, object.campusestudio);
            pstm.setLong(6, Long.valueOf(object.caseid));
            pstm.executeUpdate();

            con.commit();
            errorLog += " DESPUES del update "
            errorLog += " insertare en la bitacora "
            transferencia.setAspirante(object.aspirante);
            transferencia.setCorreoAspirante(object.correoaspirante);
            transferencia.setValorOriginal(object.valororginal.toString());
            transferencia.setValorCambio(object.valorcambio.toString());
            transferencia.setUsuarioCreacion(object.usuario);
            transferencia.setCampusAnterior(object.campusAnterior);
            transferencia.setCampusNuevo(object.campusNuevo);
			transferencia.setCaseid(Long.valueOf(object.caseid));
			transferencia.setEstatus(object.estatus);
			transferencia.setLicenciatura(object.licenciatura);
			transferencia.setPeriodo(object.periodo);
            //errorLog += " datos bitacora aspirante " + object.aspirante + " correo " + object.correoaspirante + " valor original " + object.valororginal + " valor cambio " + object.valorcambio + " usuario " + object.usuario;
            Result resultadoinsert = insertCatBitacoraTransferencia(transferencia);
            errorLog += " el error en el insert es: " + resultadoinsert.getError_info();
            if (resultadoinsert.isSuccess()) {
                errorLog += " inserte en la bitacora " + resultadoinsert.isSuccess().toString();
            } else {
                errorLog += " NO inserte en la bitacora "
                resultadoinsert.isSuccess().toString();
            }


            resultado.setSuccess(true)
            resultado.setError_info(errorLog);
        } catch (Exception ex) {
            resultado.setError_info(errorLog);
            resultado.setSuccess(false);
            resultado.setError(ex.getMessage());
            con.rollback();
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }

        return resultado;
    }

    public Boolean validarConexion() {
        Boolean retorno = false
        if (con == null || con.isClosed()) {
            con = new DBConnect().getConnection();
            retorno = true
        }
		return retorno
    }

    public Result insertCatBitacoraTransferencia(Transferencias transferencia) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String errorlog = "";
        List < Transferencias > data = new ArrayList < Transferencias > ()
        try {
            errorlog += " antes de revisar la conexion "
            closeCon = validarConexion();
            errorlog += " antes del preparent " + " CONEXION " + closeCon
            pstm = con.prepareStatement(Statements.INSERT_BITACORA_TRANSFERENCIA)
            pstm.setString(1, transferencia.getAspirante())
            pstm.setString(2, transferencia.getCorreoAspirante())
            pstm.setString(3, transferencia.getValorOriginal())
            pstm.setString(4, transferencia.getValorCambio())
            pstm.setString(5, transferencia.getUsuarioCreacion())
            pstm.setString(6, transferencia.getCampusAnterior())
            pstm.setString(7, transferencia.getCampusNuevo())
            errorlog += " CONSULTA " + Statements.INSERT_BITACORA_TRANSFERENCIA;
            errorlog += " ASPIRANTE " + transferencia.getAspirante()
            errorlog += " CORREO " + transferencia.getCorreoAspirante()
            errorlog += " ORIGINAL " + transferencia.getValorOriginal()
            errorlog += " CAMBIO " + transferencia.getValorCambio()
            errorlog += " USUARIO " + transferencia.getUsuarioCreacion()
            errorlog += " CAMPUSANTERIOR " + transferencia.getCampusAnterior()
            errorlog += " CAMPUSNUEVO " + transferencia.getCampusNuevo()

            errorlog += " antes de hacer le execute update "
            pstm.executeUpdate();
            errorlog += " ya hice el insert "
            /*rs = pstm.getGeneratedKeys()
            errorlog += " voy por el key "
            if(rs.next()) {
            	errorlog += " asignare el pid "
            	transferencia.setPersistenceId(rs.getLong("persistenceId"))
            	errorlog += " asigne el pid "
            }
            errorlog += " data "
            data.add(transferencia)*/
            resultado.setSuccess(true);
            resultado.setData(data)
            resultado.setError_info(errorlog);
        } catch (Exception e) {
            errorlog += " falle " + e.getMessage()
            resultado.setError_info(errorlog);
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result selectBitacoraTransferencias(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        Boolean closeCon = false;
        String where = " ", orderby = "ORDER BY ", errorlog = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Long userLogged = 0L;
        List < String > lstGrupo = new ArrayList < String > ();
        List < Map < String, String >> lstGrupoCampus = new ArrayList < Map < String, String >> ();
        String campus = "";
        try {
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            Transferencias row = new Transferencias();
            List < Transferencias > rows = new ArrayList < Transferencias > ();

            def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);

            List < CatCampus > lstCatCampus = objCatCampusDAO.find(0, 9999)

            userLogged = context.getApiSession().getUserId();

            List < UserMembership > lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
            for (UserMembership objUserMembership: lstUserMembership) {
                for (CatCampus rowGrupo: lstCatCampus) {
                    if (objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
                        lstGrupo.add(rowGrupo.getDescripcion());
                        break;
                    }
                }
            }



            //			if(lstGrupo.size()>0) {
            //				where+=" OR campusNuevo IN ("
            //			}
            //			for(Integer i=0; i<lstGrupo.size(); i++) {
            //				String campusMiembro=lstGrupo.get(i);
            //				where+="'"+campusMiembro+"'"
            //				if(i==(lstGrupo.size()-1)) {
            //					where+=")) "
            //				}
            //				else {
            //					where+=", "
            //				}
            //			}

            closeCon = validarConexion();

            for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
                switch (filtro.get("columna")) {

                    case "ASPIRANTE":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(ASPIRANTE) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CORREO ASPIRANTE":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(CORREOASPIRANTE) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "FECHA":
                        //fechacreacion
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(FECHACREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "USUARIO":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(USUARIOCREACION) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "CAMPUS":
                        if (object.orden == "<") {
                            if (where.contains("WHERE")) {
                                campus += " AND ";
                            } else {
                                campus += " WHERE ";
                            }
                            campus += " LOWER(campusnuevo) ";
                            if (filtro.get("operador").equals("Igual a")) {
                                campus += "=LOWER('[valor]')"
                            } else {
                                campus += "LIKE LOWER('%[valor]%')"
                            }
							campus = campus.replace("[valor]", filtro.get("valor"))
                        } else {
							if (where.contains("WHERE")) {
								campus += " AND ";
							} else {
								campus += " WHERE ";
							}
                            campus += "LOWER(campusanterior) ";
                            if (filtro.get("operador").equals("Igual a")) {
                                campus += "=LOWER('[valor]')"
                            } else {
                                campus += "LIKE LOWER('%[valor]%')"
                            }
                            campus = campus.replace("[valor]", filtro.get("valor"))
                        }
                        break;
                    case "VPD ANTERIOR":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(CAMPUSANTERIOR) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    case "VPD ACTUAL":
                        if (where.contains("WHERE")) {
                            where += " AND ";
                        } else {
                            where += " WHERE ";
                        }
                        where += " LOWER(CAMPUSNUEVO) ";
                        if (filtro.get("operador").equals("Igual a")) {
                            where += "=LOWER('[valor]')";
                        } else {
                            where += "LIKE LOWER('%[valor]%')";
                        }
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
                        //					case "TRANSFERENCIAS ENVIADAS":
                        //					//(campusAnterior IN (
                        //						if(where.contains("WHERE")) {
                        //							where+= " AND ";
                        //						} else {
                        //							where+= " WHERE ";
                        //						}
                        //						where +=" LOWER(CAMPUSNUEVO) ";
                        //						if(filtro.get("operador").equals("Igual a")) {
                        //							where+="=LOWER('[valor]')";
                        //						}else {
                        //							where+="LIKE LOWER('%[valor]%')";
                        //						}
                        //						where = where.replace("[valor]", filtro.get("valor"))
                        //					break;
                        //					case "TRANSFERENCIAS RECIBIDAS":
                        //						if(where.contains("WHERE")) {
                        //							where+= " AND ";
                        //						} else {
                        //							where+= " WHERE ";
                        //						}
                        //						where +=" LOWER(CAMPUSNUEVO) ";
                        //						if(filtro.get("operador").equals("Igual a")) {
                        //							where+="=LOWER('[valor]')";
                        //						}else {
                        //							where+="LIKE LOWER('%[valor]%')";
                        //						}
                        //						where = where.replace("[valor]", filtro.get("valor"))
                        break;
                    default:
                        if (object.orden == "<") {
                            if (lstGrupo.size() > 0) {
                                where += " WHERE (campusAnterior IN ("
                            }
                            for (Integer i = 0; i < lstGrupo.size(); i++) {
                                String campusMiembro = lstGrupo.get(i);
                                where += "'" + campusMiembro + "'"
                                if (i == (lstGrupo.size() - 1)) {
                                    where += ")) "
                                } else {
                                    where += ", "
                                }
                            }
                        } else {
                            if (lstGrupo.size() > 0) {
                                where += " WHERE (campusNuevo IN ("
                            }
                            for (Integer i = 0; i < lstGrupo.size(); i++) {
                                String campusMiembro = lstGrupo.get(i);
                                where += "'" + campusMiembro + "'"
                                if (i == (lstGrupo.size() - 1)) {
                                    where += ")) "
                                } else {
                                    where += ", "
                                }
                            }
                        }
                        break;
                }
            }

            switch (object.orderby) {
                case "ASPIRANTE":
                    orderby += "aspirante";
                    break;
                case "CORREO ASPIRANTE":
                    orderby += "correoaspirante";
                    break;
                case "FECHA":
                    orderby += "fechacreacion";
                    break;
                case "USUARIO":
                    orderby += "usuariocreacion";
                    break;
                case "VPD ANTERIOR":
                    orderby += "campusanterior";
                    break;
                case "VPD ACTUAL":
                    orderby += "campusnuevo";
                    break;
                    /*case "CAMPUS":
                    	orderby+="campus.DESCRIPCION"
                    break;*/
            }

            if (orderby.equals("ORDER BY ")) {
                orderby += "PERSISTENCEID";
            }

            orderby += " " + object.orientation;



            String consulta = Statements.GET_BITACORA_TRANSFERENCIA;
            consulta = consulta.replace("[WHERE]", where);
            consulta = consulta.replace("[campus]", campus);

            String consultaCount = Statements.GET_COUNT_BITACORA_TRANSFERENCIA;
            consultaCount = consultaCount.replace("[WHERE]", where);
            consultaCount = consultaCount.replace("[campus]", campus);

            pstm = con.prepareStatement(consultaCount.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]", "").replace("[ORDERBY]", ""))
            rs = pstm.executeQuery();

            if (rs.next()) {
                resultado.setTotalRegistros(rs.getInt("registros"));
            }

            consulta = consulta.replace("[ORDERBY]", orderby);
            consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?");
            errorlog += " consulta " + consulta;
            pstm = con.prepareStatement(consulta);
            pstm.setInt(1, object.limit);
            pstm.setInt(2, object.offset);
            rs = pstm.executeQuery();

            while (rs.next()) {
                row = new Transferencias();
                row.setAspirante(rs.getString("aspirante"));
                row.setCorreoAspirante(rs.getString("correoaspirante"));
                row.setFechaCreacion(rs.getString("fechacreacion"));
                row.setUsuarioCreacion(rs.getString("usuariocreacion"));
                row.setValorCambio(rs.getString("valorcambio"));
                row.setValorOriginal(rs.getString("valororiginal"));
                row.setCampusAnterior(rs.getString("campusanterior"));
                row.setCampusNuevo(rs.getString("campusnuevo"));

                rows.add(row);
            }
            resultado.setSuccess(true);
            resultado.setError_info(errorlog);
            resultado.setData(rows);

        } catch (Exception e) {
            resultado.setSuccess(false);
            resultado.setError(e.getMessage());
            errorlog += " ERROR "
            e.getMessage();
            resultado.setError_info(errorlog)
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }
        return resultado
    }

    public Result reagendarExamen(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
        Result resultado = new Result();
        String errorLog = "";
        Boolean closeCon = false;
        try {
            ProcessAPI processAPI = context.getApiClient().getProcessAPI()
            Boolean avanzartarea = false;
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
            def jsonSlurper = new JsonSlurper();
            def object = jsonSlurper.parseText(jsonData);
            assert object instanceof Map;

            org.bonitasoft.engine.api.APIClient apiClient = new APIClient() //context.getApiClient();
            apiClient.login(username, password)
            errorLog = errorLog + " | object.caseid: " + object.caseid;
            context.getApiClient().getProcessAPI().updateProcessDataInstance("isReagendar", Long.valueOf(object.caseid.toString()), true);

            SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
            searchBuilder.filter(HumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID, object.caseid);
            searchBuilder.sort(HumanTaskInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID, Order.ASC);
            final SearchOptions searchOptions = searchBuilder.done();
            SearchResult < HumanTaskInstance > SearchHumanTaskInstanceSearch = context.getApiClient().getProcessAPI().searchHumanTaskInstances(searchOptions)
            List < HumanTaskInstance > lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();

            for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {
                errorLog = errorLog + " | objHumanTaskInstance.getName(): " + objHumanTaskInstance.getName()
                if (objHumanTaskInstance.getName().equals("Pase de lista Prueba 1") || objHumanTaskInstance.getName().equals("Pase de lista Prueba 2") || objHumanTaskInstance.getName().equals("Pase de lista Prueba 3") || objHumanTaskInstance.getName().equals("Generar credencial")) {
                    Map < String, Serializable > inputs = new HashMap < String, Serializable > ();
                    processAPI.assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
                    processAPI.executeUserTask(objHumanTaskInstance.getId(), inputs);
                    errorLog = errorLog + " | FINAL"
                }
            }
            resultado.setSuccess(true)
            resultado.setError_info(errorLog);
        } catch (Exception ex) {
            resultado.setError_info(errorLog);
            resultado.setSuccess(false);
            resultado.setError(ex.getMessage());
            con.rollback();
        } finally {
            if (closeCon) {
                new DBConnect().closeObj(con, stm, rs, pstm)
            }
        }

        return resultado;
    }
}