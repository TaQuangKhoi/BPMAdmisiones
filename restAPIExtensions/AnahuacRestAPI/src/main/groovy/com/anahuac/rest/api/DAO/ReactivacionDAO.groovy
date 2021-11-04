package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement

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
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utilities.LoadParametros
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper

class ReactivacionDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransferenciasDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	public Result getUsuariosRechazadosComite(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
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
			where += " AND (sda.ESTATUSSOLICITUD = 'Rechazado por comité')"
			
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

			errorlog += "object.lstFiltro" + object.lstFiltro
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs= pstm.executeQuery();
			if(rs.next()) {
				SSA = rs.getString("valor")
			}
			String consulta = Statements.GET_USUARIOS_RECHAZADOS_COMITE
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				errorlog += ", columna " + filtro.get("columna")
				switch (filtro.get("columna")) {

					case "IDBANNER,NOMBRE,EMAIL":
					errorlog+="IDBANNER,NOMBRE,EMAIL"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(concat(sda.primernombre,' ', sda.segundonombre,' ',sda.apellidopaterno,' ',sda.apellidomaterno)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(da.idbanner) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
				break;
				case "CAMPUS,PROGRAMA,PERÍODO":
					errorlog+="CAMPUS,PROGRAMA,PERÍODO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(campusEstudio.descripcion) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				
				break;
				case "PREPARATORIA,PROCEDENCIA,PROMEDIO":
					errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					/*where +="  OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))*/
					
					where +="  OR LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				break;
				case "ESTATUS":
					errorlog+="ESTATUS,TIPO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				break;
				case "RESIDENCIA,TIPO ALUMNO,TIPO ADMISION":
					errorlog+="ESTATUS,TIPO"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					where +=" ( LOWER(tipoalumno.descripcion) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +="  OR LOWER(tipoadmision.DESCRIPCION) like lower('%[valor]%') ";
					where = where.replace("[valor]", filtro.get("valor"))
					
					where +=" OR LOWER(residencia.descripcion) like lower('%[valor]%') )";
					where = where.replace("[valor]", filtro.get("valor"))
				break;
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
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(campus.descripcion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
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
						tipoalumno += " AND LOWER(residencia.descripcion) ";
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
					orderby += "campusEstudio.descripcion"
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
				case "PROCEDENCIA":
                    orderby += "procedencia";
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
			
			String consultaCount = Statements.GET_COUNT_USUARIOS_RECHAZADOS_COMITE;
			consultaCount = consultaCount.replace("[CAMPUS]", campus)
			consultaCount = consultaCount.replace("[PROGRAMA]", programa)
			consultaCount = consultaCount.replace("[INGRESO]", ingreso)
			consultaCount = consultaCount.replace("[ESTADO]", estado)
			consultaCount = consultaCount.replace("[BACHILLERATO]", bachillerato)
			consultaCount = consultaCount.replace("[TIPOALUMNO]", tipoalumno)
			consultaCount = consultaCount.replace("[WHERE]", where)
			consultaCount = consultaCount.replace("[LIMITOFFSET]", "");
			consultaCount = consultaCount.replace("[ORDERBY]", "");
			
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
							String urlFoto = rs.getString("urlfoto");
							if(urlFoto != null && !urlFoto.isEmpty()) {
								columns.put("fotografiab64", rs.getString("urlfoto") +SSA);
							}else {
								List<Document>doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
								for(Document doc : doc1) {
									encoded = "../API/formsDocumentImage?document="+doc.getId();
									columns.put("fotografiab64", encoded);
								}
							}
							/*for (Document doc: context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
								encoded = "../API/formsDocumentImage?document=" + doc.getId();
								columns.put("fotografiab64", encoded);
							}*/
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
			resultado.setData(rows)

		} catch (Exception e) {
			resultado.setError_info(errorlog)
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		} finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	public Result reactivarAspirante(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		String errorLog = "";
		Boolean closeCon = false;
		try {
			ProcessAPI processAPI = context.getApiClient().getProcessAPI()
			Boolean avanzartarea = false;
			String username = "";
			String password = "";
			
			closeCon = validarConexion();
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
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
			
			for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {
				if (objHumanTaskInstance.getName().equals("Reactivar usuario rechazado")) {
					Map < String, Serializable > inputs = new HashMap < String, Serializable > ()
					inputs.put("activoUsuario", true);
					inputs.put("countReactivacion",(object.countrechazos as Integer));
					processAPI.assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
					processAPI.executeUserTask(objHumanTaskInstance.getId(), inputs);
				}
			}
			
			con.setAutoCommit(false)
			pstm = con.prepareStatement(Statements.UPDATE_DATOS_REACTIVARUSUARIO)
			pstm.setLong(1, object.campus);
			pstm.setLong(2, object.licenciatura);
			if (object.propedeutico == null) {
				pstm.setNull(3, java.sql.Types.BIGINT);
			} else {
				pstm.setLong(3, object.propedeutico);
			}
			pstm.setLong(4, object.periodo);
			pstm.setLong(5, object.campusestudio);
			pstm.setInt(6, Integer.valueOf(object.countrechazos));
			pstm.setLong(7, Long.valueOf(object.caseid));
			pstm.executeUpdate();

			con.commit();
			
			Result formateo = new Result();
			formateo = formateoVariablesPaseListaProceso(Long.valueOf(object.caseid),context);
			errorLog += "Formateo: "+formateo.isSuccess().toString()+" Errores: "+formateo.getError()+" Error_info: "+formateo.getError_info();
			Result resultado2 = new Result();
			resultado2 = respaldoUsuario(jsonData, context);
			errorLog += " Respaldo: "+resultado2.isSuccess().toString()+" Errores: "+formateo.getError()+" Error_info: "+resultado2.getError_info();
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
	
	public Result formateoVariablesPaseListaProceso(Long caseid, RestAPIContext context) {
		Result resultado = new Result()
		try {
			ProcessAPI processAPI = context.getApiClient().getProcessAPI();
			Map<String, Serializable> rows = new HashMap<String, Serializable>();
			
			rows.put("asistenciaCollegeBoard", false);
			rows.put("asistenciaPsicometrico", false);
			rows.put("asistenciaEntrevista", false);
			
			processAPI.updateProcessDataInstances(caseid, rows)
			
		resultado.setSuccess(true)
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError("500 Internal Server Error")
			resultado.setError_info(e.getMessage())
		} 
		return resultado
	}
	
	
	
	
	public Result respaldoUsuario(String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();
			
			con.setAutoCommit(false)
			// INSERTAR ISELIMINADO
			pstm = con.prepareStatement("INSERT INTO SolicitudDeAdmisionRespaldo (persistenceid,aceptado,aceptoavisoprivacidad,actanacimiento,admisionanahuac,apellidomaterno,apellidopaterno,avisoprivacidad,bachillerato,calle,calle2,caseid,ciudad,ciudadbachillerato,codigopostal,colonia,comprobantecalificaciones,confirmarautordatos,correoelectronico,curp,datosveridicos,delegacionmunicipio,estadobachillerato,estadoextranjero,estatussolicitud,fechanacimiento,fecharegistro,fechasolicitudenviada,fechaultimamodificacion,foto,isaspirantemigrado,necesitoayuda,numexterior,numinterior,otrotelefonocontacto,paisbachillerato,persistenceversion,primernombre,promediogeneral,resultadopaa,segundonombre,selectedindex,selectedindexfamiliar,selectedindexpersonal,selectedindexrevision,telefono,telefonocelular,tienedescuento,tienepaa,usiariotwitter,usuariofacebook,usuarioinstagram,catbachilleratos_pid,catcampus_pid,catcampusestudio_pid,catestado_pid,catestadocivil_pid,catestadoexamen_pid,catgestionescolar_pid,catlugarexamen_pid,catnacionalidad_pid,catpais_pid,catpaisexamen_pid,catperiodo_pid,catpresentasteenotrocampus_pid,catpropedeutico_pid,catreligion_pid,catsexo_pid,ciudadexamen_pid,ciudadexamenpais_pid,urlactanacimiento,urlcartaaa,urlconstancia,urldescuentos,urlfoto,urlresultadopaa,catconcluisteproceso_pid,catresultadoadmision_pid,countRechazos)  SELECT  (case when (SELECT max(persistenceId)+1 from SolicitudDeAdmisionRespaldo ) is null then 1 else (SELECT max(persistenceId)+1 from SolicitudDeAdmisionRespaldo) end) AS persistenceid,aceptado,aceptoavisoprivacidad,actanacimiento,admisionanahuac,apellidomaterno,apellidopaterno,avisoprivacidad,bachillerato,calle,calle2,caseid,ciudad,ciudadbachillerato,codigopostal,colonia,comprobantecalificaciones,confirmarautordatos,correoelectronico,curp,datosveridicos,delegacionmunicipio,estadobachillerato,estadoextranjero,estatussolicitud,fechanacimiento,fecharegistro,fechasolicitudenviada,fechaultimamodificacion,foto,isaspirantemigrado,necesitoayuda,numexterior,numinterior,otrotelefonocontacto,paisbachillerato,persistenceversion,primernombre,promediogeneral,resultadopaa,segundonombre,selectedindex,selectedindexfamiliar,selectedindexpersonal,selectedindexrevision,telefono,telefonocelular,tienedescuento,tienepaa,usiariotwitter,usuariofacebook,usuarioinstagram,catbachilleratos_pid,catcampus_pid,catcampusestudio_pid,catestado_pid,catestadocivil_pid,catestadoexamen_pid,catgestionescolar_pid,catlugarexamen_pid,catnacionalidad_pid,catpais_pid,catpaisexamen_pid,catperiodo_pid,catpresentasteenotrocampus_pid,catpropedeutico_pid,catreligion_pid,catsexo_pid,ciudadexamen_pid,ciudadexamenpais_pid,urlactanacimiento,urlcartaaa,urlconstancia,urldescuentos,urlfoto,urlresultadopaa,catconcluisteproceso_pid,catresultadoadmision_pid,countRechazos FROM SolicitudDeAdmision WHERE caseid = "+object.caseid);
			pstm.executeUpdate();
			errorLog+="solicituddeadmision"
			pstm = con.prepareStatement("INSERT INTO AutodescripcionRespaldo (persistenceid,admiraspersonalidadmadre,admiraspersonalidadpadre,anomuertemadre,anomuertepadre,asprctosnogustanreligion,caracteristicasexitocarrera,caseid,comodescribestufamilia,comoresolvisteproblema,comotedescribentusamigos,cualexamenextrapresentaste,defectosobservasmadre,defectosobservaspadre,detallespersonalidad,empresatrabajas,empresatrabajaste,expectativascarrera,iseliminado,lugarytipoorientacion,madreayudaproblemas,mayorproblemaenfrentado,metascortoplazo,metaslargoplazo,metasmedianoplazo,motivoaspectosnogustanreligion,motivoelegistecarrera,motivoexamenextraordinario,motivopadresnoacuerdo,motivoreprobaste,motivotrabajoayudaelegircarrera,organizacionhassidojefe,organizacionparticipas,otroestadocivilpadres,otrotipoasistencia,padreayudaproblemas,pageindex,periodoreprobaste,persistenceversion,personaintervinoeleccion,profesionalcomoteves,puestotrabajo,quecambiariasdeti,quecambiariasdetufamilia,trabajo,valoresapordatoreligion,vencido,catactualnentetrabajas_pid,catareabachillerato_pid,catarealaboraldeinteres_pid,cataspectodesagradareligio_pid,catconquienvives_pid,catestadocivilpadres_pid,catestudiadoextranjero_pid,catexperienciaayudacarrera_pid,catfamiliaregresadoanahuac_pid,catfamiliartellevasmejor_pid,catgiroempresa_pid,cathacecuantotiempotrabaja_pid,cathaspresentadoexamenextr_pid,cathasreprobado_pid,cathastenidotrabajo_pid,cathorariotrabajo_pid,catinscritootrauniversidad_pid,catjefeorganizacionsocial_pid,catmadrevive_pid,catmotivotrabajas_pid,catorientacionvocacional_pid,catpadrevive_pid,catpadresdeacuerdo_pid,catparticipasgruposocial_pid,catpersonaintervinoeleccio_pid,catpersonasaludable_pid,catplaticasproblemasmadre_pid,catplaticasproblemaspadre_pid,catpracticasdeporte_pid,catpracticasreligion_pid,catproblemasaludatencionco_pid,catrecibiratencionespiritu_pid,catregnumchris_pid,catrelacionhermanos_pid,catreligion_pid,catrequieresasistencia_pid,cattegustaleer_pid,catvivesestadodiscapacidad_pid,catyaresolvisteelproblema_pid,paisestudiasteextranjero_pid,tiempoestudiasteextranjero_pid)  SELECT  (case when (SELECT max(persistenceId)+1 from AutodescripcionRespaldo ) is null then 1 else (SELECT max(persistenceId)+1 from AutodescripcionRespaldo) end) AS persistenceid,admiraspersonalidadmadre,admiraspersonalidadpadre,anomuertemadre,anomuertepadre,asprctosnogustanreligion,caracteristicasexitocarrera,caseid,comodescribestufamilia,comoresolvisteproblema,comotedescribentusamigos,cualexamenextrapresentaste,defectosobservasmadre,defectosobservaspadre,detallespersonalidad,empresatrabajas,empresatrabajaste,expectativascarrera,iseliminado,lugarytipoorientacion,madreayudaproblemas,mayorproblemaenfrentado,metascortoplazo,metaslargoplazo,metasmedianoplazo,motivoaspectosnogustanreligion,motivoelegistecarrera,motivoexamenextraordinario,motivopadresnoacuerdo,motivoreprobaste,motivotrabajoayudaelegircarrera,organizacionhassidojefe,organizacionparticipas,otroestadocivilpadres,otrotipoasistencia,padreayudaproblemas,pageindex,periodoreprobaste,persistenceversion,personaintervinoeleccion,profesionalcomoteves,puestotrabajo,quecambiariasdeti,quecambiariasdetufamilia,trabajo,valoresapordatoreligion,vencido,catactualnentetrabajas_pid,catareabachillerato_pid,catarealaboraldeinteres_pid,cataspectodesagradareligio_pid,catconquienvives_pid,catestadocivilpadres_pid,catestudiadoextranjero_pid,catexperienciaayudacarrera_pid,catfamiliaregresadoanahuac_pid,catfamiliartellevasmejor_pid,catgiroempresa_pid,cathacecuantotiempotrabaja_pid,cathaspresentadoexamenextr_pid,cathasreprobado_pid,cathastenidotrabajo_pid,cathorariotrabajo_pid,catinscritootrauniversidad_pid,catjefeorganizacionsocial_pid,catmadrevive_pid,catmotivotrabajas_pid,catorientacionvocacional_pid,catpadrevive_pid,catpadresdeacuerdo_pid,catparticipasgruposocial_pid,catpersonaintervinoeleccio_pid,catpersonasaludable_pid,catplaticasproblemasmadre_pid,catplaticasproblemaspadre_pid,catpracticasdeporte_pid,catpracticasreligion_pid,catproblemasaludatencionco_pid,catrecibiratencionespiritu_pid,catregnumchris_pid,catrelacionhermanos_pid,catreligion_pid,catrequieresasistencia_pid,cattegustaleer_pid,catvivesestadodiscapacidad_pid,catyaresolvisteelproblema_pid,paisestudiasteextranjero_pid,tiempoestudiasteextranjero_pid FROM Autodescripcion WHERE caseid = "+object.caseid);
			pstm.executeUpdate();
			errorLog+=", autodescripcion"
			pstm = con.prepareStatement("INSERT INTO AutodescripcionV2Respaldo (persistenceid,admiraspersonalidadmadre,admiraspersonalidadpadre,asprctosnogustanreligion,caracteristicasexitocarrera,caseid,comodescribesrelacionhermanos,comodescribestufamilia,comoestaconformadafamilia,comoresolvisteproblema,comotedescribentusamigos,conquienplaticasproblemas,cualexamenextrapresentaste,defectosobservasmadre,defectosobservaspadre,detallespersonalidad,empresatrabajas,empresatrabajaste,expectativascarrera,familiarmejorrelacion,fuentesinfluyerondesicion,hasrecibidoalgunaterapia,materiascalifaltas,materiascalifbajas,materiasnotegustan,materiastegustan,mayorproblemaenfrentado,metascortoplazo,metaslargoplazo,metasmedianoplazo,motivoaspectosnogustanreligion,motivoelegistecarrera,motivoexamenextraordinario,motivopadresnoacuerdo,motivoreprobaste,organizacionhassidojefe,organizacionparticipas,organizacionesperteneces,pageindex,periodoreprobaste,persistenceversion,personasinfluyerondesicion,principalesdefectos,principalesvirtudes,problemassaludatencioncontinua,profesionalcomoteves,quecambiariasdeti,quecambiariasdetufamilia,quedeportepracticas,quehacesentutiempolibre,quelecturaprefieres,tipodiscapacidad,catactualnentetrabajas_pid,catareabachillerato_pid,cataspectodesagradareligio_pid,catestudiadoextranjero_pid,catexperienciaayudacarrera_pid,cathaspresentadoexamenextr_pid,cathasreprobado_pid,cathastenidotrabajo_pid,catinscritootrauniversidad_pid,catjefeorganizacionsocial_pid,catorientacionvocacional_pid,catpadresdeacuerdo_pid,catparticipasgruposocial_pid,catpersonasaludable_pid,catpracticasdeporte_pid,catpracticasreligion_pid,catproblemassaludatencion_pid,catrecibidoterapia_pid,cattegustaleer_pid,catvivesestadodiscapacidad_pid,catyaresolvisteelproblema_pid,paisestudiasteextranjero_pid,pertenecesorganizacion_pid,tiempoestudiasteextranjero_pid) SELECT (case when (SELECT max(persistenceId)+1 from AutodescripcionV2Respaldo ) is null then 1 else (SELECT max(persistenceId)+1 from AutodescripcionV2Respaldo) end) AS persistenceid,admiraspersonalidadmadre,admiraspersonalidadpadre,asprctosnogustanreligion,caracteristicasexitocarrera,caseid,comodescribesrelacionhermanos,comodescribestufamilia,comoestaconformadafamilia,comoresolvisteproblema,comotedescribentusamigos,conquienplaticasproblemas,cualexamenextrapresentaste,defectosobservasmadre,defectosobservaspadre,detallespersonalidad,empresatrabajas,empresatrabajaste,expectativascarrera,familiarmejorrelacion,fuentesinfluyerondesicion,hasrecibidoalgunaterapia,materiascalifaltas,materiascalifbajas,materiasnotegustan,materiastegustan,mayorproblemaenfrentado,metascortoplazo,metaslargoplazo,metasmedianoplazo,motivoaspectosnogustanreligion,motivoelegistecarrera,motivoexamenextraordinario,motivopadresnoacuerdo,motivoreprobaste,organizacionhassidojefe,organizacionparticipas,organizacionesperteneces,pageindex,periodoreprobaste,persistenceversion,personasinfluyerondesicion,principalesdefectos,principalesvirtudes,problemassaludatencioncontinua,profesionalcomoteves,quecambiariasdeti,quecambiariasdetufamilia,quedeportepracticas,quehacesentutiempolibre,quelecturaprefieres,tipodiscapacidad,catactualnentetrabajas_pid,catareabachillerato_pid,cataspectodesagradareligio_pid,catestudiadoextranjero_pid,catexperienciaayudacarrera_pid,cathaspresentadoexamenextr_pid,cathasreprobado_pid,cathastenidotrabajo_pid,catinscritootrauniversidad_pid,catjefeorganizacionsocial_pid,catorientacionvocacional_pid,catpadresdeacuerdo_pid,catparticipasgruposocial_pid,catpersonasaludable_pid,catpracticasdeporte_pid,catpracticasreligion_pid,catproblemassaludatencion_pid,catrecibidoterapia_pid,cattegustaleer_pid,catvivesestadodiscapacidad_pid,catyaresolvisteelproblema_pid,paisestudiasteextranjero_pid,pertenecesorganizacion_pid,tiempoestudiasteextranjero_pid FROM AutodescripcionV2 WHERE caseid ="+object.caseid);
			pstm.executeUpdate();
			errorLog+=", autodescripcionv2"
			pstm = con.prepareStatement("INSERT INTO CatRegistroRespaldo (persistenceid,apellidomaterno,apellidopaterno,ayuda,caseid,correoelectronico,iseliminado,nombreusuario,numerocontacto,password,persistenceversion,primernombre,segundonombre,catcampus_pid,catgestionescolar_pid) SELECT (case when (SELECT max(persistenceId)+1 from CatRegistroRespaldo ) is null then 1 else (SELECT max(persistenceId)+1 from CatRegistroRespaldo) end) AS persistenceid,apellidomaterno,apellidopaterno,ayuda,caseid,correoelectronico,iseliminado,nombreusuario,numerocontacto,password,persistenceversion,primernombre,segundonombre,catcampus_pid,catgestionescolar_pid FROM CatRegistro WHERE caseid = "+object.caseid);
			pstm.executeUpdate();
			errorLog+=", catRegistroRespaldo"
			pstm = con.prepareStatement("INSERT INTO ContactoEmergenciasRespaldo (persistenceid,caseid,nombre,parentesco,persistenceversion,telefono,telefonocelular,catcasodeemergencia_pid,catparentesco_pid,vencido) SELECT  DISTINCT ON (nombre) (case when (SELECT max(persistenceId)+1 from ContactoEmergenciasRespaldo ) is null then 0+ROW_NUMBER () OVER (ORDER BY persistenceid) else (SELECT max(persistenceId)+1 from ContactoEmergenciasRespaldo)+ROW_NUMBER () OVER (ORDER BY persistenceid) end) AS persistenceid,caseid,nombre,parentesco,persistenceversion,telefono,telefonocelular,catcasodeemergencia_pid,catparentesco_pid,vencido FROM ContactoEmergencias WHERE caseid =  "+object.caseid);
			pstm.executeUpdate();
			errorLog+=", contactoEmergencias"
			pstm = con.prepareStatement("INSERT INTO DetalleSolicitudRespaldo SELECT (case when (SELECT max(persistenceId)+1 from DetalleSolicitudRespaldo ) is null then 1 else (SELECT max(persistenceId)+1 from DetalleSolicitudRespaldo) end) AS persistenceid,admisionanahuac,caseid,cbcoincide,descuento,idbanner,iscurpvalidado,observacionescambio,observacionesdescuento,observacioneslistaroja,observacionesrechazo,ordenpago,persistenceversion,promediocoincide,revisado,tipoalumno,vencido,catdescuentos_pid,catpagodeexamendeadmision_pid,catresidencia_pid,cattipoadmision_pid,cattipoalumno_pid FROM DetalleSolicitud WHERE caseid = '"+object.caseid+"'");
			pstm.executeUpdate();
			errorLog+=", detallesolicitud"
			pstm = con.prepareStatement("INSERT INTO EscuelaHasEstadoRespaldo SELECT DISTINCT ON (escuela_pid) (case when (SELECT max(persistenceId)+1 from EscuelaHasEstadoRespaldo ) is null then 0+ROW_NUMBER () OVER (ORDER BY persistenceid) else (SELECT max(persistenceId)+1 from EscuelaHasEstadoRespaldo)+ROW_NUMBER () OVER (ORDER BY persistenceid) end) AS persistenceid,anofin,anoinicio,caseid,ciudad,estadostring,otraescuela,persistenceversion,promedio,vencido,escuela_pid,estado_pid,grado_pid,pais_pid,tipo_pid FROM EscuelasHasEstado WHERE caseid = "+object.caseid+" and anofin != '' ");
			pstm.executeUpdate();
			errorLog+=", EscuelaHasEstado"
			pstm = con.prepareStatement("INSERT INTO GrupoSocialRespaldo SELECT (case when (SELECT max(persistenceId)+1 from GrupoSocialRespaldo ) is null then 0+ROW_NUMBER () OVER (ORDER BY persistenceid) else (SELECT max(persistenceId)+1 from GrupoSocialRespaldo)+ROW_NUMBER () OVER (ORDER BY persistenceid) end) AS persistenceid,caseid,nombre,persistenceversion,vencido,catafiliacion_pid,catcuantotiempo_pid,catpertenecesorganizacion_pid,cattipoorganizacion_pid FROM GrupoSocial WHERE caseid = "+object.caseid);
			pstm.executeUpdate();
			errorLog+=", grupoSocial"
			pstm = con.prepareStatement("INSERT INTO HermanoRespaldo SELECT (case when (SELECT max(persistenceId)+1 from HermanoRespaldo) is null then 0+ROW_NUMBER () OVER (ORDER BY persistenceid) else (SELECT max(persistenceId)+1 from HermanoRespaldo)+ROW_NUMBER () OVER (ORDER BY persistenceid) end) AS persistenceid,apellidos,caseid,empresatrabaja,escuelaestudia,fechanacimiento,isestudia,istrabaja,nombres,persistenceversion,vencido FROM HermanoRespaldo WHERE caseid = "+object.caseid);
			pstm.executeUpdate();
			errorLog+=", Hermano"
			pstm = con.prepareStatement("INSERT INTO IdiomasHablasRespaldo SELECT (case when (SELECT max(persistenceId)+1 from IdiomasHablasRespaldo ) is null then 0+ROW_NUMBER () OVER (ORDER BY persistenceid) else (SELECT max(persistenceId)+1 from IdiomasHablasRespaldo)+ROW_NUMBER () OVER (ORDER BY persistenceid) end) AS  persistenceid,caseid,otroidioma,persistenceversion,vencido,catconversacion_pid,catescritura_pid,catidioma_pid,catlectura_pid,cattraduccion_pid FROM IdiomasHablas WHERE caseid = "+object.caseid);
			pstm.executeUpdate();
			errorLog+=", IdiomaHablas"
			
			//Revisarlo como conectar los datos
			//pstm = con.prepareStatement("INSERT INTO IdiomasHablasV2Respaldo SELECT * FROM IdiomasHablasV2 WHERE caseid = "+object.caseid);
			//pstm.executeUpdate();
			
			pstm = con.prepareStatement("INSERT INTO InfoCartaRespaldo SELECT (case when (SELECT max(persistenceId)+1 from InfoCartaRespaldo) is null then 0+ROW_NUMBER () OVER (ORDER BY persistenceid) else (SELECT max(persistenceId)+1 from InfoCartaRespaldo)+ROW_NUMBER () OVER (ORDER BY persistenceid) end) AS persistenceid,carta,curp,descuentoprontopagomes1,descuentoprontopagomes1fecha,descuentoprontopagomes2,descuentoprontopagomes2fecha,espanol,fechaenvio,fechalimitedepago,fechalimiteentregadocumentacion,nombre,notaslistaroja,notastransferencia,numerodematricula,pca,pcda,pdp,pdu,persistenceversion,pia,sihaceonomatematicas,sse,statuspdu,universidad FROM InfoCarta WHERE numerodematricula = '"+object.idbanner+"'");
			pstm.executeUpdate();
			errorLog+=", infoCarta"
			
			pstm = con.prepareStatement("INSERT INTO PadresTutorRespaldo ( persistenceid,persistenceversion,apellidos,calle,caseid,ciudad,codigopostal,colonia,correoelectronico,delegacionmunicipio,desconozcodatospadres,empresatrabaja,estadoextranjero,giroempresa,istutor,nombre,numeroexterior,numerointerior,otroparentesco, puesto,telefono,vencido,vivecontigo,catcampusegreso_pid,categresoanahuac_pid,catescolaridad_pid,catestado_pid,catpais_pid,catparentezco_pid,cattitulo_pid,cattrabaja_pid,vive_pid) SELECT DISTINCT ON (nombre) (case when (SELECT max(persistenceId)+1 from PadresTutorRespaldo ) is null then 0+ROW_NUMBER () OVER (ORDER BY persistenceid) else (SELECT max(persistenceId)+1 from PadresTutorRespaldo)+ROW_NUMBER () OVER (ORDER BY persistenceid) end) AS persistenceid,persistenceversion,apellidos,calle,caseid,ciudad,codigopostal,colonia,correoelectronico,delegacionmunicipio,desconozcodatospadres,empresatrabaja,estadoextranjero,giroempresa,istutor,nombre,numeroexterior,numerointerior,otroparentesco, puesto,telefono,vencido,vivecontigo,catcampusegreso_pid,categresoanahuac_pid,catescolaridad_pid,catestado_pid,catpais_pid,catparentezco_pid,cattitulo_pid,cattrabaja_pid,vive_pid FROM PadresTutor WHERE caseid = "+object.caseid);
			pstm.executeUpdate();
			errorLog+=", padresTutor"
			
			pstm = con.prepareStatement("INSERT INTO ParienteEARespaldo (persistenceid,persistenceversion,apellidos,carrera,caseid,correo,nombre,vencido,catcampus_pid,catdiploma_pid,catparentesco_pid)  SELECT (case when (SELECT max(persistenceId)+1 from ParienteEARespaldo ) is null then 0+ROW_NUMBER () OVER (ORDER BY persistenceid) else (SELECT max(persistenceId)+1 from ParienteEARespaldo)+ROW_NUMBER () OVER (ORDER BY persistenceid) end) AS persistenceid,persistenceversion,apellidos,carrera,caseid,correo,nombre,vencido,catcampus_pid,catdiploma_pid,catparentesco_pid FROM ParienteEARespaldo WHERE caseid = "+object.caseid);
			pstm.executeUpdate();
			errorLog+=", parienteEA"
			
			pstm = con.prepareStatement("INSERT INTO PlantillaHermanosRespaldo (persistenceid,persistenceversion,apellidohermano,apellidomaterno,apellidopaterno,campus,empresahermano,estudiohermano,expediente,fechanacimientohermano,fecharegistro,idbanner,idreg,institucionhermano,nombrehermano,primernombre,segundonombre,trabajohermano) SELECT (case when (SELECT max(persistenceId)+1 from PlantillaHermanosRespaldo ) is null then 0+ROW_NUMBER () OVER (ORDER BY persistenceid) else (SELECT max(persistenceId)+1 from PlantillaHermanosRespaldo)+ROW_NUMBER () OVER (ORDER BY persistenceid) end) AS persistenceid,persistenceversion,apellidohermano,apellidomaterno,apellidopaterno,campus,empresahermano,estudiohermano,expediente,fechanacimientohermano,fecharegistro,idbanner,idreg,institucionhermano,nombrehermano,primernombre,segundonombre,trabajohermano FROM PlantillaHermanos WHERE idreg::INTEGER = "+object.caseid);			pstm.executeUpdate();
			errorLog+=", plantillaHermanos"
			
			pstm = con.prepareStatement("INSERT INTO PlantillaRegistroRespaldo (persistenceid,persistenceversion,algundeporte,apellidomaterno,apellidopaterno,asociaciondeportiva,asociacionescolar,asociacionpolitica,asociacionreligiosa,asociacionsocial,aspectoreligionnogustar,atencionespiritual,atencionmedica,ayudasocial,campus,casacalle,celular,colonia,conquienvives,conoceregnum,contrasena,correo,cp,cualasociacion,curp,discapacidad,emergencia,emergenciacelular,emergencianombre,emergenciatelefono,estado,estadocivil,estadopadres,expediente,familiarenanahuac,fechanacimiento,fecharegistro,fecuenciadeleer,foto,jefeasociacion,kardex,licenciatura,madreapellido,madrecp,madrecalle,madrecel,madrecolonia,madrecorreo,madreegresada,madreescolaridad,madreestado,madremunicipio,madrenombre,madrenumext,madrenumint,madrepais,madretitulo,madreuniegresada,madrevive,municipio,nregistro,nacionalidad,numext,numint,otrauniversidad,otrotelefono,padreapellido,padrecp,padrecalle,padrecel,padrecolonia,padrecorreo,padredireccion,padreegresado,padreempresa,padreescolaridad,padreestado,padregiroempresa,padremunicipio,padrenombre,padrenumext,padrenumint,padrepais,padrepuesto,padretitulo,padretrabaja,padretutor,padreuniegresado,padrevivo,padresdireccion,pais,periodoingreso,personasaludable,practicasreligion,prepaciudad,prepaestado,prepapais,prepapromedio,preparatoria,primernombre,puntajepaa,religion,segundonombre,sexo,tegustaleer,tiempolibre,tienereligion,tipolectura,tutorapellido,tutorcp,tutorcalle,tutorcelular,tutorcolonia,tutordireccion,tutoregresado,tutorempresa,tutorescolaridad,tutorestado,tutorgiroempresa,tutormunicipio,tutornombre,tutornumext,tutornumint,tutorpais,tutorparentesco,tutorpuesto,tutortitulo,tutortrabaja,tutoruniegresado,universidad,valorreligion) SELECT  (case when (SELECT max(persistenceId)+1 from PlantillaRegistroRespaldo ) is null then 0 else (SELECT max(persistenceId)+1 from PlantillaRegistroRespaldo) end) AS persistenceid,persistenceversion,algundeporte,apellidomaterno,apellidopaterno,asociaciondeportiva,asociacionescolar,asociacionpolitica,asociacionreligiosa,asociacionsocial,aspectoreligionnogustar,atencionespiritual,atencionmedica,ayudasocial,campus,casacalle,celular,colonia,conquienvives,conoceregnum,contrasena,correo,cp,cualasociacion,curp,discapacidad,emergencia,emergenciacelular,emergencianombre,emergenciatelefono,estado,estadocivil,estadopadres,expediente,familiarenanahuac,fechanacimiento,fecharegistro,fecuenciadeleer,foto,jefeasociacion,kardex,licenciatura,madreapellido,madrecp,madrecalle,madrecel,madrecolonia,madrecorreo,madreegresada,madreescolaridad,madreestado,madremunicipio,madrenombre,madrenumext,madrenumint,madrepais,madretitulo,madreuniegresada,madrevive,municipio,nregistro,nacionalidad,numext,numint,otrauniversidad,otrotelefono,padreapellido,padrecp,padrecalle,padrecel,padrecolonia,padrecorreo,padredireccion,padreegresado,padreempresa,padreescolaridad,padreestado,padregiroempresa,padremunicipio,padrenombre,padrenumext,padrenumint,padrepais,padrepuesto,padretitulo,padretrabaja,padretutor,padreuniegresado,padrevivo,padresdireccion,pais,periodoingreso,personasaludable,practicasreligion,prepaciudad,prepaestado,prepapais,prepapromedio,preparatoria,primernombre,puntajepaa,religion,segundonombre,sexo,tegustaleer,tiempolibre,tienereligion,tipolectura,tutorapellido,tutorcp,tutorcalle,tutorcelular,tutorcolonia,tutordireccion,tutoregresado,tutorempresa,tutorescolaridad,tutorestado,tutorgiroempresa,tutormunicipio,tutornombre,tutornumext,tutornumint,tutorpais,tutorparentesco,tutorpuesto,tutortitulo,tutortrabaja,tutoruniegresado,universidad,valorreligion FROM PlantillaRegistro WHERE expediente = '"+object.idbanner+"' ORDER BY persistenceId DESC LIMIT 1");
			pstm.executeUpdate();
			errorLog+=", plantillaRegistro"
			
			pstm = con.prepareStatement("INSERT INTO TerapiaRespaldo (persistenceid,persistenceversion,caseid,cuantotiempo,otraterapia,recibidoterapiastring,tipoterapia,vencido,catrecibidoterapia_pid,cattipoterapia_pid) SELECT (case when (SELECT max(persistenceId)+1 from TerapiaRespaldo ) is null then 0+ROW_NUMBER () OVER (ORDER BY persistenceid) else (SELECT max(persistenceId)+1 from TerapiaRespaldo)+ROW_NUMBER () OVER (ORDER BY persistenceid) end) AS persistenceid,persistenceversion,caseid,cuantotiempo,otraterapia,recibidoterapiastring,tipoterapia,vencido,catrecibidoterapia_pid,cattipoterapia_pid FROM Terapia WHERE caseid = "+object.caseid);
			pstm.executeUpdate();
			errorLog+=", terapia"
			
			pstm = con.prepareStatement("INSERT INTO UniversidadesRespaldo (persistenceid,persistenceversion,anofin,anoinicio,carrera,caseid,motivosuspension,nombre,vencido) SELECT (case when (SELECT max(persistenceId)+1 from UniversidadesRespaldo ) is null then 0+ROW_NUMBER () OVER (ORDER BY persistenceid) else (SELECT max(persistenceId)+1 from UniversidadesRespaldo)+ROW_NUMBER () OVER (ORDER BY persistenceid) end) AS persistenceid,persistenceversion,anofin,anoinicio,carrera,caseid,motivosuspension,nombre,vencido FROM Universidades WHERE caseid = "+object.caseid);
			pstm.executeUpdate();
			errorLog+=", univerisdades"
			
			con.commit();
			resultado.setSuccess(true)
			resultado.setError_info(errorLog)
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError(e.getMessage())
			resultado.setError_info(errorLog)
			con.rollback();
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
}
