package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement

import org.bonitasoft.engine.api.APIClient
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.document.DocumentValue
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException
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
import com.anahuac.catalogos.CatCiudad
import com.anahuac.catalogos.CatCiudadDAO
import com.anahuac.catalogos.CatEstados
import com.anahuac.catalogos.CatEstadosDAO
import com.anahuac.catalogos.CatGestionEscolar
import com.anahuac.catalogos.CatGestionEscolarDAO
import com.anahuac.catalogos.CatLugarExamen
import com.anahuac.catalogos.CatLugarExamenDAO
import com.anahuac.catalogos.CatPais
import com.anahuac.catalogos.CatPaisDAO
import com.anahuac.catalogos.CatPeriodo
import com.anahuac.catalogos.CatPeriodoDAO
import com.anahuac.catalogos.CatPropedeutico
import com.anahuac.catalogos.CatPropedeuticoDAO
import com.anahuac.catalogos.CatRegistro
import com.anahuac.catalogos.CatRegistroDAO
import com.anahuac.model.ContactoEmergencias
import com.anahuac.model.ContactoEmergenciasDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.model.PadresTutor
import com.anahuac.model.PadresTutorDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.PropertiesEntity
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Utilities.LoadParametros
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class ReactivacionDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransferenciasDAO.class);
	Connection con;
	Statement stm;
	ResultSet rs;
	ResultSet rs2;
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
		Boolean autoCommit = false;
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
			int intento = Integer.valueOf(object.countrechazos);
			
			for (HumanTaskInstance objHumanTaskInstance: lstHumanTaskInstanceSearch) {
				if (objHumanTaskInstance.getName().equals("Reactivar usuario rechazado")) {
					Map < String, Serializable > inputs = new HashMap < String, Serializable > ()
					inputs.put("activoUsuario", false);
					inputs.put("countReactivacion",((intento == 1)?0:intento-1));
					processAPI.assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
					processAPI.executeUserTask(objHumanTaskInstance.getId(), inputs);
				}
			}
			
			Result nuevaSolicitud = nuevoCasoSolicitud(jsonData,context);
			errorLog += ", nuevaSolicitud:"+nuevaSolicitud+", data:"+nuevaSolicitud.getData();

			if(nuevaSolicitud.isSuccess()) {
				autoCommit = true;
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
				pstm.setInt(6, intento);
				pstm.setLong(7, Long.valueOf(nuevaSolicitud.getData().get(0)));
				pstm.executeUpdate();
				
				pstm = con.prepareStatement(Statements.UPDATE_DATOS_REACTIVARUSUARIO_AUTODESCRIPCION)
				pstm.setLong(1,  Long.valueOf(nuevaSolicitud.getData().get(0)));
				pstm.executeUpdate();
	
				con.commit();
			}
			
			
			//Result formateo = new Result();
			//formateo = formateoVariablesPaseListaProceso(Long.valueOf(object.caseid),context);
			//errorLog += "Formateo: "+formateo.isSuccess().toString()+" Errores: "+formateo.getError()+" Error_info: "+formateo.getError_info();
			
			resultado.setSuccess(true)
			resultado.setError_info(errorLog);
		} catch (Exception ex) {
			resultado.setError_info(errorLog);
			resultado.setSuccess(false);
			resultado.setError(ex.getMessage());
			if(autoCommit) {
				con.rollback();
			}
			
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
			pstm = con.prepareStatement("INSERT INTO SolicitudDeAdmisionRespaldo (persistenceid,aceptado,aceptoavisoprivacidad,actanacimiento,admisionanahuac,apellidomaterno,apellidopaterno,avisoprivacidad,bachillerato,calle,calle2,caseid,ciudad,ciudadbachillerato,codigopostal,colonia,comprobantecalificaciones,confirmarautordatos,correoelectronico,curp,datosveridicos,delegacionmunicipio,estadobachillerato,estadoextranjero,estatussolicitud,fechanacimiento,fecharegistro,fechasolicitudenviada,fechaultimamodificacion,foto,isaspirantemigrado,necesitoayuda,numexterior,numinterior,otrotelefonocontacto,paisbachillerato,persistenceversion,primernombre,promediogeneral,resultadopaa,segundonombre,selectedindex,selectedindexfamiliar,selectedindexpersonal,selectedindexrevision,telefono,telefonocelular,tienedescuento,tienepaa,usiariotwitter,usuariofacebook,usuarioinstagram,catbachilleratos_pid,catcampus_pid,catcampusestudio_pid,catestado_pid,catestadocivil_pid,catestadoexamen_pid,catgestionescolar_pid,catlugarexamen_pid,catnacionalidad_pid,catpais_pid,catpaisexamen_pid,catperiodo_pid,catpresentasteenotrocampus_pid,catpropedeutico_pid,catreligion_pid,catsexo_pid,ciudadexamen_pid,ciudadexamenpais_pid,urlactanacimiento,urlcartaaa,urlconstancia,urldescuentos,urlfoto,urlresultadopaa,catconcluisteproceso_pid,catresultadoadmision_pid,countRechazos)  SELECT  (case when (SELECT max(persistenceId)+1 from SolicitudDeAdmisionRespaldo ) is null then 1 else (SELECT max(persistenceId)+1 from SolicitudDeAdmisionRespaldo) end) AS persistenceid,aceptado,aceptoavisoprivacidad,actanacimiento,admisionanahuac,apellidomaterno,apellidopaterno,avisoprivacidad,bachillerato,calle,calle2,caseid,ciudad,ciudadbachillerato,codigopostal,colonia,comprobantecalificaciones,confirmarautordatos,correoelectronico,curp,datosveridicos,delegacionmunicipio,estadobachillerato,estadoextranjero,'Rechazado por comité' as estatussolicitud,fechanacimiento,fecharegistro,fechasolicitudenviada,fechaultimamodificacion,foto,isaspirantemigrado,necesitoayuda,numexterior,numinterior,otrotelefonocontacto,paisbachillerato,persistenceversion,primernombre,promediogeneral,resultadopaa,segundonombre,selectedindex,selectedindexfamiliar,selectedindexpersonal,selectedindexrevision,telefono,telefonocelular,tienedescuento,tienepaa,usiariotwitter,usuariofacebook,usuarioinstagram,catbachilleratos_pid,catcampus_pid,catcampusestudio_pid,catestado_pid,catestadocivil_pid,catestadoexamen_pid,catgestionescolar_pid,catlugarexamen_pid,catnacionalidad_pid,catpais_pid,catpaisexamen_pid,catperiodo_pid,catpresentasteenotrocampus_pid,catpropedeutico_pid,catreligion_pid,catsexo_pid,ciudadexamen_pid,ciudadexamenpais_pid,urlactanacimiento,urlcartaaa,urlconstancia,urldescuentos,urlfoto,urlresultadopaa,catconcluisteproceso_pid,catresultadoadmision_pid,countRechazos FROM SolicitudDeAdmision WHERE caseid = "+object.caseid);
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
			
			//pstm = con.prepareStatement("INSERT INTO PadresTutorRespaldo ( persistenceid,persistenceversion,apellidos,calle,caseid,ciudad,codigopostal,colonia,correoelectronico,delegacionmunicipio,desconozcodatospadres,empresatrabaja,estadoextranjero,giroempresa,istutor,nombre,numeroexterior,numerointerior,otroparentesco, puesto,telefono,vencido,vivecontigo,catcampusegreso_pid,categresoanahuac_pid,catescolaridad_pid,catestado_pid,catpais_pid,catparentezco_pid,cattitulo_pid,cattrabaja_pid,vive_pid) SELECT DISTINCT ON (nombre) (case when (SELECT max(persistenceId)+1 from PadresTutorRespaldo ) is null then 0+ROW_NUMBER () OVER (ORDER BY persistenceid) else (SELECT max(persistenceId)+1 from PadresTutorRespaldo)+ROW_NUMBER () OVER (ORDER BY persistenceid) end) AS persistenceid,persistenceversion,apellidos,calle,caseid,ciudad,codigopostal,colonia,correoelectronico,delegacionmunicipio,desconozcodatospadres,empresatrabaja,estadoextranjero,giroempresa,istutor,nombre,numeroexterior,numerointerior,otroparentesco, puesto,telefono,vencido,vivecontigo,catcampusegreso_pid,categresoanahuac_pid,catescolaridad_pid,catestado_pid,catpais_pid,catparentezco_pid,cattitulo_pid,cattrabaja_pid,vive_pid FROM PadresTutor WHERE caseid = "+object.caseid);
			//pstm.executeUpdate();
			//errorLog+=", padresTutor"
			
			pstm = con.prepareStatement("INSERT INTO ParienteEARespaldo (persistenceid,persistenceversion,apellidos,carrera,caseid,correo,nombre,vencido,catcampus_pid,catdiploma_pid,catparentesco_pid)  SELECT (case when (SELECT max(persistenceId)+1 from ParienteEARespaldo ) is null then 0+ROW_NUMBER () OVER (ORDER BY persistenceid) else (SELECT max(persistenceId)+1 from ParienteEARespaldo)+ROW_NUMBER () OVER (ORDER BY persistenceid) end) AS persistenceid,persistenceversion,apellidos,carrera,caseid,correo,nombre,vencido,catcampus_pid,catdiploma_pid,catparentesco_pid FROM ParienteEARespaldo WHERE caseid = "+object.caseid);
			pstm.executeUpdate();
			errorLog+=", parienteEA"
			
			//pstm = con.prepareStatement("INSERT INTO PlantillaHermanosRespaldo (persistenceid,persistenceversion,apellidohermano,apellidomaterno,apellidopaterno,campus,empresahermano,estudiohermano,expediente,fechanacimientohermano,fecharegistro,idbanner,idreg,institucionhermano,nombrehermano,primernombre,segundonombre,trabajohermano) SELECT (case when (SELECT max(persistenceId)+1 from PlantillaHermanosRespaldo ) is null then 0+ROW_NUMBER () OVER (ORDER BY persistenceid) else (SELECT max(persistenceId)+1 from PlantillaHermanosRespaldo)+ROW_NUMBER () OVER (ORDER BY persistenceid) end) AS persistenceid,persistenceversion,apellidohermano,apellidomaterno,apellidopaterno,campus,empresahermano,estudiohermano,expediente,fechanacimientohermano,fecharegistro,idbanner,idreg,institucionhermano,nombrehermano,primernombre,segundonombre,trabajohermano FROM PlantillaHermanos WHERE idreg::INTEGER = "+object.caseid);			pstm.executeUpdate();
			//errorLog+=", plantillaHermanos"
			
			//pstm = con.prepareStatement("INSERT INTO PlantillaRegistroRespaldo (persistenceid,persistenceversion,algundeporte,apellidomaterno,apellidopaterno,asociaciondeportiva,asociacionescolar,asociacionpolitica,asociacionreligiosa,asociacionsocial,aspectoreligionnogustar,atencionespiritual,atencionmedica,ayudasocial,campus,casacalle,celular,colonia,conquienvives,conoceregnum,contrasena,correo,cp,cualasociacion,curp,discapacidad,emergencia,emergenciacelular,emergencianombre,emergenciatelefono,estado,estadocivil,estadopadres,expediente,familiarenanahuac,fechanacimiento,fecharegistro,fecuenciadeleer,foto,jefeasociacion,kardex,licenciatura,madreapellido,madrecp,madrecalle,madrecel,madrecolonia,madrecorreo,madreegresada,madreescolaridad,madreestado,madremunicipio,madrenombre,madrenumext,madrenumint,madrepais,madretitulo,madreuniegresada,madrevive,municipio,nregistro,nacionalidad,numext,numint,otrauniversidad,otrotelefono,padreapellido,padrecp,padrecalle,padrecel,padrecolonia,padrecorreo,padredireccion,padreegresado,padreempresa,padreescolaridad,padreestado,padregiroempresa,padremunicipio,padrenombre,padrenumext,padrenumint,padrepais,padrepuesto,padretitulo,padretrabaja,padretutor,padreuniegresado,padrevivo,padresdireccion,pais,periodoingreso,personasaludable,practicasreligion,prepaciudad,prepaestado,prepapais,prepapromedio,preparatoria,primernombre,puntajepaa,religion,segundonombre,sexo,tegustaleer,tiempolibre,tienereligion,tipolectura,tutorapellido,tutorcp,tutorcalle,tutorcelular,tutorcolonia,tutordireccion,tutoregresado,tutorempresa,tutorescolaridad,tutorestado,tutorgiroempresa,tutormunicipio,tutornombre,tutornumext,tutornumint,tutorpais,tutorparentesco,tutorpuesto,tutortitulo,tutortrabaja,tutoruniegresado,universidad,valorreligion) SELECT  (case when (SELECT max(persistenceId)+1 from PlantillaRegistroRespaldo ) is null then 0 else (SELECT max(persistenceId)+1 from PlantillaRegistroRespaldo) end) AS persistenceid,persistenceversion,algundeporte,apellidomaterno,apellidopaterno,asociaciondeportiva,asociacionescolar,asociacionpolitica,asociacionreligiosa,asociacionsocial,aspectoreligionnogustar,atencionespiritual,atencionmedica,ayudasocial,campus,casacalle,celular,colonia,conquienvives,conoceregnum,contrasena,correo,cp,cualasociacion,curp,discapacidad,emergencia,emergenciacelular,emergencianombre,emergenciatelefono,estado,estadocivil,estadopadres,expediente,familiarenanahuac,fechanacimiento,fecharegistro,fecuenciadeleer,foto,jefeasociacion,kardex,licenciatura,madreapellido,madrecp,madrecalle,madrecel,madrecolonia,madrecorreo,madreegresada,madreescolaridad,madreestado,madremunicipio,madrenombre,madrenumext,madrenumint,madrepais,madretitulo,madreuniegresada,madrevive,municipio,nregistro,nacionalidad,numext,numint,otrauniversidad,otrotelefono,padreapellido,padrecp,padrecalle,padrecel,padrecolonia,padrecorreo,padredireccion,padreegresado,padreempresa,padreescolaridad,padreestado,padregiroempresa,padremunicipio,padrenombre,padrenumext,padrenumint,padrepais,padrepuesto,padretitulo,padretrabaja,padretutor,padreuniegresado,padrevivo,padresdireccion,pais,periodoingreso,personasaludable,practicasreligion,prepaciudad,prepaestado,prepapais,prepapromedio,preparatoria,primernombre,puntajepaa,religion,segundonombre,sexo,tegustaleer,tiempolibre,tienereligion,tipolectura,tutorapellido,tutorcp,tutorcalle,tutorcelular,tutorcolonia,tutordireccion,tutoregresado,tutorempresa,tutorescolaridad,tutorestado,tutorgiroempresa,tutormunicipio,tutornombre,tutornumext,tutornumint,tutorpais,tutorparentesco,tutorpuesto,tutortitulo,tutortrabaja,tutoruniegresado,universidad,valorreligion FROM PlantillaRegistro WHERE expediente = '"+object.idbanner+"' ORDER BY persistenceId DESC LIMIT 1");
			//pstm.executeUpdate();
			//errorLog+=", plantillaRegistro"
			
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
		}finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	} 
	
	
	public Result RealizarRespaldo(String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			Result resultado2 = new Result();
			//resultado2 = guardarTutorIntento(jsonData, context);
			//errorLog += "TutorIntento: "+resultado2.isSuccess().toString()+" Errores: "+resultado2.getError()+" Error_info: "+resultado2.getError_info();
			
			resultado2 = new Result();
			resultado2 = respaldoUsuario(jsonData, context);
			errorLog += " Respaldo: "+resultado2.isSuccess().toString()+" Errores: "+resultado2.getError()+" Error_info: "+resultado2.getError_info();
			
			resultado.setSuccess(true)
			resultado.setError_info(errorLog)
			
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError(e.getMessage())
			resultado.setError_info(errorLog)
		}finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	
	public Result guardarTutorIntento(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		boolean autocomit = false;
		try {
			errorLog+="1";
			
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			

			closeCon = validarConexion();
			con.setAutoCommit(false);
			
			String consultaNumeroIntento = "SELECT CASE WHEN countRechazos IS NULL THEN 0 ELSE countRechazos END AS countRechazos FROM solicituddeadmision  WHERE caseid = "+object.caseid
			pstm = con.prepareStatement(consultaNumeroIntento)
			rs = pstm.executeQuery()
			errorLog+="5"
			String numeroIntento = "0";
			while(rs.next()) {
				numeroIntento = rs.getString(1);
			}
			errorLog+="numero:"+numeroIntento
			
			String consulta = "SELECT  DISTINCT ON (pt.catparentezco_pid) pt.persistenceid FROM solicituddeadmision sda  INNER JOIN padrestutor pt ON pt.caseid=sda.caseid AND sda.estatussolicitud != 'Solicitud vencida' AND sda.estatussolicitud not like '%Período vencido en:%' INNER JOIN CatParentesco cp ON cp.persistenceid=pt.catparentezco_pid  WHERE sda.caseid = "+object.caseid+" AND pt.istutor IS true  ORDER BY   pt.catparentezco_pid ASC,pt.persistenceid DESC "
			pstm = con.prepareStatement(consulta)
			rs = pstm.executeQuery()
			while(rs.next()) {
				errorLog+="7"
				String update = "UPDATE PadresTutor SET countIntentos = "+numeroIntento+" WHERE persistenceid = "+rs.getString(1)
				pstm = con.prepareStatement(update);
				pstm.executeUpdate();
			}
			
			consulta = "SELECT  DISTINCT ON (pt.catparentezco_pid) pt.persistenceid FROM solicituddeadmision sda  INNER JOIN padrestutor pt ON pt.caseid=sda.caseid AND sda.estatussolicitud != 'Solicitud vencida' AND sda.estatussolicitud not like '%Período vencido en:%' INNER JOIN CatParentesco cp ON cp.persistenceid=pt.catparentezco_pid  WHERE sda.caseid = "+object.caseid+" AND (cp.clave = 'P' OR cp.clave = 'M') ORDER BY   pt.catparentezco_pid ASC,pt.persistenceid DESC  LIMIT 2"
			pstm = con.prepareStatement(consulta)
			rs = pstm.executeQuery()
			while(rs.next()) {
				errorLog+="8"
				String update = "UPDATE PadresTutor SET countIntentos = "+numeroIntento+" WHERE persistenceid = "+rs.getString(1)
				pstm = con.prepareStatement(update);
				pstm.executeUpdate();
			}

			errorLog+="9"
			con.commit();
			resultado.setSuccess(true)
			resultado.setError_info(errorLog)
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError(e.getMessage())
			resultado.setError_info(errorLog)
			con.rollback();
			
		}finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result selectAspirantesRechazadosRespaldo(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "", campus = "",  orderby = "ORDER BY ", errorlog = "";
		List < String > lstGrupo = new ArrayList < String > ();

		Boolean primeraCondicion = false;
		String consultaCountAux = "";
		Long totalRegistrosAux = 0;
		String consultaInicial = "";

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
			//(sda.ESTATUSSOLICITUD != 'Solicitud vencida') AND (sda.ESTATUSSOLICITUD != 'Periodo vencido') AND (sda.ESTATUSSOLICITUD != 'Solicitud caduca') AND (sda.ESTATUSSOLICITUD not like '%Solicitud vencida en:%') AND (sda.ESTATUSSOLICITUD not like '%Período vencido en:%')
			where += " WHERE sda.iseliminado IS NOT TRUE and (sda.isAspiranteMigrado is null  or sda.isAspiranteMigrado = false ) "
			
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

			errorlog += "campus" + campus;
			errorlog += "object.lstFiltro" + object.lstFiltro
			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();

			String SSA = "";
			pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
			rs = pstm.executeQuery();
			if (rs.next()) {
				SSA = rs.getString("valor")
			}

			String consulta = Statements.GET_ASPIRANTES_RESPALDO

			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				errorlog = consulta + " 1";
				switch (filtro.get("columna")) {
				
				case "IDBANNER,NOMBRE,EMAIL":
					errorlog+="IDBANNER,NOMBRE,EMAIL"
					if(where.contains("WHERE")) {
						where+= " AND "
					}else {
						where+= " WHERE "
					}
					primeraCondicion = true;
					where +=" ( LOWER(concat(sda.apellidopaterno,' ', sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(da.idbanner) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
					break;
					case "NOMBRE,EMAIL,CURP":
						errorlog += "NOMBRE,EMAIL,CURP"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
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
					case "ULTIMA MODIFICACION":
						errorlog += "FECHAULTIMAMODIFICACION"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " (LOWER(fechaultimamodificacion) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where += " OR to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') ";
						where += "LIKE LOWER('%[valor]%'))";

						where = where.replace("[valor]", filtro.get("valor"))
						break;

						//filtrado utilizado en lista roja y rechazado
					case "NOMBRE,EMAIL,CURP":
						errorlog += "NOMBRE,EMAIL,CURP"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(sda.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						

					case "CAMPUS,PROGRAMA,PERÍODO":
						errorlog += "PROGRAMA,INGRESO,PERÍODO"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))

						where += " OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
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
					case "NÚMERO DE SOLICITUD":
						errorlog += "SOLICITUD"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(CAST(sda.caseid AS varchar)) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PROMEDIO":
						errorlog+="PROMEDIO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +="  LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"));
						break;

					case "ID BANNER":
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(da.idbanner) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;

					case "MOTIVO DE LISTA RECHAZO":
						errorlog += "RECHAZO"
						if (where.contains("WHERE")) {
							where += " AND "
						} else {
							where += " WHERE "
						}
						where += " LOWER(da.observacionesRechazo) ";
						if (filtro.get("operador").equals("Igual a")) {
							where += "=LOWER('[valor]')"
						} else {
							where += "LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						/*====================================================================*/
					default:

						//consulta=consulta.replace("[BACHILLERATO]", bachillerato)
						//consulta=consulta.replace("[WHERE]", where);

						break;
				}

			}
			errorlog = consulta + " 2";
			switch (object.orderby) {
				case "FECHAULTIMAMODIFICACION":
					orderby += "sda.fechaultimamodificacion";
					break;
				case "NOMBRE":
					orderby += "sda.apellidopaterno";
					break;
				case "EMAIL":
					orderby += "sda.correoelectronico";
					break;
				case "CURP":
					orderby += "sda.curp";
					break;
				case "CAMPUS":
					orderby += "campusEstudio.DESCRIPCION"
					break;
				case "PREPARATORIA":
					orderby += "prepa.DESCRIPCION"
					break;
				case "PROGRAMA":
					orderby += "gestionescolar.NOMBRE"
					break;
				case "PERÍODO":
					orderby += "periodo.DESCRIPCION"
					break;
				case "PROCEDENCIA":
					orderby += "CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END";
					break;
				case "PROMEDIO":
					orderby += "sda.PROMEDIOGENERAL";
					break;
				case "IDBANNER":
					orderby += "da.idbanner";
					break;
				case "SOLICITUD":
					orderby += "sda.caseid::INTEGER";
					break;
				case "RECHAZO":
					orderby += "da.observacionesRechazo";
					break;
				default:
					orderby += "sda.persistenceid"
					break;
			}
			errorlog = consulta + " 3";
			
			orderby += " " + object.orientation;
			consulta = consulta.replace("[CAMPUS]", campus)
			where += " " + campus;
			
			consulta = consulta.replace("[WHERE]", where);
			errorlog = consulta + " 5";
			
			String consultaCount = Statements.GET_ASPIRANTES_RESPALDO_COUNT
			consultaCount = consultaCount.replace("[WHERE]", where);
			consultaCount = consultaCount.replace("[CAMPUS]", campus)
			consultaCountAux = consultaCount;
			pstm = con.prepareStatement(consultaCount)
			
			errorlog = consulta + " 6";

			rs = pstm.executeQuery()
			if (rs.next()) {
			 	resultado.setTotalRegistros(rs.getInt("registros"))
			 	totalRegistrosAux =	rs.getInt("registros");
			}
			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorlog = consulta + " 7";

			consultaInicial = consulta;			
			if(primeraCondicion == true && totalRegistrosAux == 0) {
				errorlog = consulta + " if totalRegistrosAux "+consulta;
				consulta = consultaInicial;
				consulta = consulta.replace(" ( LOWER(concat(sda.apellidopaterno,' ', sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) ", " ( LOWER(concat(sda.apellidopaterno,' ',sda.primernombre,' ',sda.segundonombre)) ")

				consultaCountAux = consultaCountAux.replace("  ( LOWER(concat(sda.apellidopaterno,' ', sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) ", "  ( LOWER(concat(sda.apellidopaterno,' ',sda.primernombre,' ',sda.segundonombre)) ")
				pstm = con.prepareStatement(consultaCountAux)
			
				errorlog = consulta + " 6";
				rs = pstm.executeQuery()
				if (rs.next()) {
				 	resultado.setTotalRegistros(rs.getInt("registros"))
				}
			}

			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)
			rs = pstm.executeQuery()

			rows = new ArrayList < Map < String, Object >> ();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			errorlog = consulta + " 8";
			while (rs.next()) {
				Map < String, Object > columns = new LinkedHashMap < String, Object > ();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
					if (metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
						String encoded = "";
						boolean noAzure = false;
						try {
							String urlFoto = rs.getString("urlfoto");
							if (urlFoto != null && !urlFoto.isEmpty()) {
								columns.put("fotografiab64", rs.getString("urlfoto") + SSA);
							} else {
								noAzure = true;
								List < Document > doc1 = context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)
								for (Document doc: doc1) {
									encoded = "../API/formsDocumentImage?document=" + doc.getId();
									columns.put("fotografiab64", encoded);
								}
							}

							for (Document doc: context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
								encoded = "../API/formsDocumentImage?document=" + doc.getId();
								columns.put("fotografiabpm", encoded);
							}
						} catch (Exception e) {
							LOGGER.error "[ERROR] " + e.getMessage();
							columns.put("fotografiabpm", "");
							if(noAzure){
								columns.put("fotografiab64", "");
							}
							errorlog += "" + e.getMessage();
						}
					}
				}

				rows.add(columns);
			}
			errorlog = consulta + " 9" + consultaCount;
			resultado.setSuccess(true)

			resultado.setError_info(errorlog);
			resultado.setData(rows)

		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
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

		public Result getInformacionReporteSolicitudRespaldo(String jsonData) {
	    Result resultado = new Result();
	    Boolean closeCon = false;

	    String errorlog = "";
	    List < Map < String, Object >> lstResultado = new ArrayList < Map < String, Object >> ();
	    Map < String, Object > objResultado = new HashMap < String, Object > ();
	    List < Map < String, Object >> objTutor = new ArrayList < Map < String, Object >> ();
	    List < Map < String, Object >> objEmergencia = new ArrayList < Map < String, Object >> ();

	    try {
	        def jsonSlurper = new JsonSlurper();
	        def object = jsonSlurper.parseText(jsonData);
	        closeCon = validarConexion();

	        ResultSetMetaData metaData = rs.getMetaData();
	        int columnCount = metaData.getColumnCount();

	        errorlog += " | consulta: " + Statements.GET_INFORMACION_REPORTE_SOLICITUD_RESPALDO;

	        pstm = con.prepareStatement(Statements.GET_INFORMACION_REPORTE_SOLICITUD_RESPALDO);
	        pstm.setString(1, object.caseid);
	        rs = pstm.executeQuery();

	        errorlog += " | pstm: " + pstm;

	        while (rs.next()) {
	            objResultado = new HashMap < String, Object > ();
	            objTutor = new HashMap < String, Object > ();
	            objEmergencia = new HashMap < String, Object > ();
	            
	            objResultado.put("campuscursar1", rs.getString("campuscursar1"));
	            objResultado.put("periodo", rs.getString("periodo"));
	            objResultado.put("presentasteenotrocampus", rs.getString("presentasteenotrocampus"));
	            objResultado.put("lugarexamen", rs.getString("lugarexamen"));
	            objResultado.put("caseid", rs.getString("caseid"));
	            objResultado.put("primernombre", rs.getString("primernombre"));
	            objResultado.put("segundonombre", rs.getString("segundonombre"));
	            objResultado.put("apellidopaterno", rs.getString("apellidopaterno"));
	            objResultado.put("apellidomaterno", rs.getString("apellidomaterno"));
	            objResultado.put("correoelectronico", rs.getString("correoelectronico"));
	            objResultado.put("fechanacimiento", rs.getString("fechanacimiento"));
	            objResultado.put("sexo", rs.getString("sexo"));
	            objResultado.put("nacionalidad", rs.getString("nacionalidad"));
	            objResultado.put("religion", rs.getString("religion"));
	            objResultado.put("curp", rs.getString("curp"));
	            objResultado.put("estadocivil", rs.getString("estadocivil"));
	            objResultado.put("telefonocelular", rs.getString("telefonocelular"));
	            objResultado.put("pais", rs.getString("pais"));
	            objResultado.put("codigopostal", rs.getString("codigopostal"));
	            objResultado.put("estado", rs.getString("estado"));
	            objResultado.put("ciudad", rs.getString("ciudad"));
	            objResultado.put("delegacionmunicipio", rs.getString("delegacionmunicipio"));
	            objResultado.put("colonia", rs.getString("colonia"));
	            objResultado.put("calle", rs.getString("calle"));
	            objResultado.put("calle2", rs.getString("calle2"));
	            objResultado.put("numexterior", rs.getString("numexterior"));
	            objResultado.put("numinterior", rs.getString("numinterior"));
	            objResultado.put("telefono", rs.getString("telefono"));
	            objResultado.put("otrotelefonocontacto", rs.getString("otrotelefonocontacto"));
	            objResultado.put("nombrebachillerato", rs.getString("nombrebachillerato"));
	            objResultado.put("paisbachillerato", rs.getString("paisbachillerato"));
	            objResultado.put("estadobachillerato", rs.getString("estadobachillerato"));
	            objResultado.put("ciudadbachillerato", rs.getString("ciudadbachillerato"));
	            objResultado.put("promediogeneral", rs.getString("promediogeneral"));
	            objResultado.put("resultadopaa", rs.getString("resultadopaa"));
	            objResultado.put("bachillerato", rs.getString("bachillerato"));

	            errorlog += " | Se termino la informacion del alumno: ";

	            if (rs.getString("istutor").equals("t")) {
	                for (int i = 1; i <= columnCount; i++) {
	                	errorlog += " | Itero al tutor: ";
	                    objResultado.put(objTutor.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i)));
	                    errorlog += " | Termino la iteracion del tutor: ";
	                }
	            } else if (rs.getString("parentezcopadrestutor").equals("Padre")) {
	            	errorlog += " | Itero al padre: ";
	            	objResultado.put("vive", rs.getString("vive"));
		            objResultado.put("desconozcodatoPadre", rs.getString("desconozcodatoPadre"));
		            objResultado.put("trabajaPadre", rs.getString("trabajaPadre"));
		            objResultado.put("tituloPadre", rs.getString("tituloPadre"));
		            objResultado.put("nombrePadre", rs.getString("nombrePadre"));
		            objResultado.put("apellidoPadre", rs.getString("apellidoPadre"));
		            objResultado.put("correoPadre", rs.getString("correoPadre"));
		            objResultado.put("escolaridadPadre", rs.getString("escolaridadPadre"));
		            objResultado.put("ocupacionPadre", rs.getString("ocupacionPadre"));
		            objResultado.put("empresatrabajaPadre", rs.getString("empresatrabajaPadre"));
		            objResultado.put("egresouniversidadanahuacPadre", rs.getString("egresouniversidadanahuacPadre"));
		            objResultado.put("paisPadre", rs.getString("paisPadre"));
		            objResultado.put("codigopostalPadre", rs.getString("codigopostalPadre"));
		            objResultado.put("estadoPadre", rs.getString("estadoPadre"));
		            objResultado.put("ciudadPadre", rs.getString("ciudadPadre"));
		            objResultado.put("delegacionPadre", rs.getString("delegacionPadre"));
		            objResultado.put("coloniaPadre", rs.getString("coloniaPadre"));
		            objResultado.put("callePadre", rs.getString("callePadre"));
		            objResultado.put("numexteriorPadre", rs.getString("numexteriorPadre"));
		            objResultado.put("numinteriorPadre", rs.getString("numinteriorPadre"));
		            objResultado.put("telefonoPadre", rs.getString("telefonoPadre"));
		            errorlog += " | Termino la iteracion del padre: ";

	            } else if (rs.getString("parentezcopadrestutor").equals("Madre")) {
	            	errorlog += " | Itero a la madre: ";
					objResultado.put("vive", rs.getString("vive"));
		            objResultado.put("desconozcodatoMadre", rs.getString("desconozcodatoMadre"));
		            objResultado.put("trabajaMadre", rs.getString("trabajaMadre"));
		            objResultado.put("tituloMadre", rs.getString("tituloMadre"));
		            objResultado.put("nombreMadre", rs.getString("nombreMadre"));
		            objResultado.put("apellidoMadre", rs.getString("apellidoMadre"));
		            objResultado.put("correoMadre", rs.getString("correoMadre"));
		            objResultado.put("escolaridadMadre", rs.getString("escolaridadMadre"));
		            objResultado.put("ocupacionMadre", rs.getString("ocupacionMadre"));
		            objResultado.put("empresatrabajaMadre", rs.getString("empresatrabajaMadre"));
		            objResultado.put("egresouniversidadanahuacMadre", rs.getString("egresouniversidadanahuacMadre"));
		            objResultado.put("paisMadre", rs.getString("paisMadre"));
		            objResultado.put("codigopostalMadre", rs.getString("codigopostalMadre"));
		            objResultado.put("estadoMadre", rs.getString("estadoMadre"));
		            objResultado.put("ciudadMadre", rs.getString("ciudadMadre"));
		            objResultado.put("delegacionMadre", rs.getString("delegacionMadre"));
		            objResultado.put("coloniaMadre", rs.getString("coloniaMadre"));
		            objResultado.put("calleMadre", rs.getString("calleMadre"));
		            objResultado.put("numexteriorMadre", rs.getString("numexteriorMadre"));
		            objResultado.put("numinteriorMadre", rs.getString("numinteriorMadre"));
		            objResultado.put("telefonoMadre", rs.getString("telefonoMadre"));
		            errorlog += " | Termino la iteracion de la madre: ";
	            }

	            for (int i = 1; i <= columnCount; i++) {
	            		errorlog += " | Itero a los numeros de emergencia: ";
	                    objResultado.put(objEmergencia.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i)));
	                    errorlog += " | Termino la iteracion de  los numeros de emergencia: ";
	            }

	            lstResultado.add(objResultado);
	        }

	        resultado.setError_info(errorlog);
	        resultado.setSuccess(true);
	        resultado.setData(lstResultado);
	    } catch (Exception e) {
	    	errorlog += " | Exception: "+e;
	        resultado.setSuccess(false);
	        resultado.setError(e.getMessage());
	        resultado.setError_info(errorlog)
	    } finally {
	        if (closeCon) {
	            new DBConnect().closeObj(con, stm, rs, pstm)
	        }
	    }
	    return resultado
	}
	
	public Result nuevoCasoSolicitud2(String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		Long caseId = 0L;
		String errorLog = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			assert object instanceof Map
			
			Map<String, Serializable> contract = new HashMap<String, Serializable>();
			Map<String, Serializable> catRegistroInput = new HashMap<String, Serializable>();
			
			catRegistroInput.put("apellidomaterno",object.catRegistroInput.apellidomaterno);
			catRegistroInput.put("apellidopaterno",object.catRegistroInput.apellidopaterno);
			catRegistroInput.put("ayuda",object.catRegistroInput.ayuda);
			catRegistroInput.put("catCampus",object.catRegistroInput.catCampus);
			catRegistroInput.put("catGestionEscolar",object.catRegistroInput.catGestionEscolar);
			catRegistroInput.put("correoelectronico",object.catRegistroInput.correoelectronico);
			catRegistroInput.put("isEliminado",object.catRegistroInput.isEliminado);
			catRegistroInput.put("nombreusuario",object.catRegistroInput.nombreusuario);
			catRegistroInput.put("password",object.catRegistroInput.password);
			catRegistroInput.put("primernombre",object.catRegistroInput.primernombre);
			catRegistroInput.put("segundonombre",object.catRegistroInput.segundonombre);
			
			contract.put("catRegistroInput",catRegistroInput)
			
			Map<String, Serializable> catSolicitudDeAdmisionInput = new HashMap<String, Serializable>();
			
			catSolicitudDeAdmisionInput.put("apellidoMaterno",object.catSolicitudDeAdmisionInput.apellidoMaterno);
			catSolicitudDeAdmisionInput.put("apellidoPaterno",object.catSolicitudDeAdmisionInput.apellidoPaterno);
			catSolicitudDeAdmisionInput.put("avisoPrivacidad",object.catSolicitudDeAdmisionInput.avisoPrivacidad);
			catSolicitudDeAdmisionInput.put("catCampus",object.catSolicitudDeAdmisionInput.catCampus);
			catSolicitudDeAdmisionInput.put("catCampusEstudio",object.catSolicitudDeAdmisionInput.catCampusEstudio);
			catSolicitudDeAdmisionInput.put("catEstadoExamen",object.catSolicitudDeAdmisionInput.catEstadoExamen);
			catSolicitudDeAdmisionInput.put("catGestionEscolar",object.catSolicitudDeAdmisionInput.catGestionEscolar);
			catSolicitudDeAdmisionInput.put("catLugarExamen",object.catSolicitudDeAdmisionInput.catLugarExamen);
			catSolicitudDeAdmisionInput.put("catPaisExamen",object.catSolicitudDeAdmisionInput.catPaisExamen);
			
			assert object.catSolicitudDeAdmisionInput.catPeriodo instanceof Map
			def map = [:]
			for ( prop in object.catSolicitudDeAdmisionInput.catPeriodo ) {
				map[prop.key] = prop.value
			}
			
			catSolicitudDeAdmisionInput.put("catPeriodo",map);
			catSolicitudDeAdmisionInput.put("catPropedeutico",object.catSolicitudDeAdmisionInput.catPropedeutico);
			catSolicitudDeAdmisionInput.put("ciudadExamen",object.catSolicitudDeAdmisionInput.ciudadExamen);
			catSolicitudDeAdmisionInput.put("ciudadExamenPais",object.catSolicitudDeAdmisionInput.ciudadExamenPais);
			catSolicitudDeAdmisionInput.put("correoElectronico",object.catSolicitudDeAdmisionInput.correoElectronico);
			catSolicitudDeAdmisionInput.put("isEliminado",object.catSolicitudDeAdmisionInput.isEliminado);
			catSolicitudDeAdmisionInput.put("necesitoAyuda",object.catSolicitudDeAdmisionInput.necesitoAyuda);
			catSolicitudDeAdmisionInput.put("primerNombre",object.catSolicitudDeAdmisionInput.primerNombre);
			catSolicitudDeAdmisionInput.put("segundoNombre",object.catSolicitudDeAdmisionInput.segundoNombre);
			//catSolicitudDeAdmisionInput.put("segundonombre",object.segundonombre);
			
			contract.put("catSolicitudDeAdmisionInput",catSolicitudDeAdmisionInput)
			contract.put("nuevaoportunidad",object.nuevaoportunidad)
			errorLog="4";
			
			
			
			Long processId = context.getApiClient().getProcessAPI().getLatestProcessDefinitionId("Proceso admisiones");
			ProcessInstance processInstance = context.getApiClient().getProcessAPI().startProcessWithInputs(processId, contract);
			caseId = processInstance.getRootProcessInstanceId();
			resultado.setSuccess(true);
		} catch (Exception loginApi) {
			LOGGER.error("[EXCEPTION]" + loginApi.getMessage());
			resultado.setSuccess(false);
			resultado.setError(loginApi.getMessage())
			resultado.setError_info(errorLog)
		}
		return resultado;
	}
	
	public Result nuevoCasoSolicitud(String jsonData,RestAPIContext context) {
		Result resultado = new Result();
		Long caseId = 0L;
		String errorLog = "";
		Boolean closeCon = false;
		try {
			def jsonSlurper = new JsonSlurper();
			def JsonOutput = new JsonOutput();
			def object = jsonSlurper.parseText(jsonData);
			assert object instanceof Map
			
			closeCon = validarConexion();
			
			def valorString = ["apellidomaterno","apellidopaterno","correoelectronico","nombreusuario","numeroContacto","password","persistenceversion","primernombre","segundonombre","catCampus","catGestionEscolar" ]
			def valorBoolean = ["isEliminado","ayuda"];
			String consulta = "SELECT apellidomaterno,apellidopaterno,ayuda,correoelectronico,isEliminado,nombreusuario,numeroContacto,password,persistenceversion,primernombre,segundonombre, null as catCampus,null as catGestionEscolar FROM CatRegistro WHERE caseid = "+object.caseid;
			pstm = con.prepareStatement(consulta)
			rs = pstm.executeQuery();
			
			Map<String, Serializable> contract = new HashMap<String, Serializable>();
			contract.put("nuevaoportunidad",false)
			
			Map<String, Serializable> registro = new HashMap<String, Serializable>();
			
			if(rs.next()) {
				for(int i=0; i<valorString.size(); i++) {
					registro.put(valorString[i],rs.getString(valorString[i]))
				}
				
				for(int i=0; i<valorBoolean.size(); i++) {
					registro.put(valorBoolean[i],rs.getBoolean(valorBoolean [i]))
				}
			}
			
			contract.put("catRegistroInput",registro);
			
			consulta = "SELECT apellidoMaterno,apellidoPaterno,avisoPrivacidad,correoelectronico,isEliminado,necesitoAyuda, primerNombre,segundoNombre,ciudadExamen_pid,ciudadExamenPais_pid,catCampus_pid,catCampusEstudio_pid, catEstadoExamen_pid, catGestionEscolar_pid, catLugarExamen_pid, catPaisExamen_pid,catPeriodo_pid,catPropedeutico_pid FROM SolicitudDeAdmision WHERE caseid::Integer = "+object.caseid;
			pstm = con.prepareStatement(consulta)
			rs = pstm.executeQuery();
			Map<String, Serializable> catSolicitudDeAdmisionInput = new HashMap<String, Serializable>();
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			def objCatCiudadDAO = context.apiClient.getDAO(CatCiudadDAO.class);
			def objCatPeriodoDAO = context.apiClient.getDAO(CatPeriodoDAO.class);
			def objCatEstadoDAO = context.apiClient.getDAO(CatEstadosDAO.class);
			def objCatGestionEscolarDAO = context.apiClient.getDAO(CatGestionEscolarDAO.class);
			def objCatLugarExamenDAO = context.apiClient.getDAO(CatLugarExamenDAO.class);
			def objCatPaisDAO = context.apiClient.getDAO(CatPaisDAO.class);
			def objCatPropedeuticoDAO = context.apiClient.getDAO(CatPropedeuticoDAO.class);
			def objContactoEmergenciaDAO = context.apiClient.getDAO(ContactoEmergenciasDAO.class);
			def objPadresTutorDAO = context.apiClient.getDAO(PadresTutorDAO.class);
			def map = [:]
			String correo = "";
			if(rs.next()) {
				catSolicitudDeAdmisionInput.put("apellidoMaterno",rs.getString("apellidoMaterno"));
				catSolicitudDeAdmisionInput.put("apellidoPaterno",rs.getString("apellidoPaterno"));
				catSolicitudDeAdmisionInput.put("avisoPrivacidad",rs.getBoolean("avisoPrivacidad"));
				catSolicitudDeAdmisionInput.put("correoElectronico",rs.getString("correoElectronico"));
				catSolicitudDeAdmisionInput.put("isEliminado",rs.getBoolean("isEliminado"));
				catSolicitudDeAdmisionInput.put("necesitoAyuda",rs.getBoolean("necesitoAyuda"));
				catSolicitudDeAdmisionInput.put("primerNombre",rs.getString("primerNombre"));
				catSolicitudDeAdmisionInput.put("segundoNombre",rs.getString("segundoNombre"));

				correo = rs.getString("correoElectronico");
				
				if(rs.getString("ciudadExamen_pid") != null) {
					CatCiudad ciudadExamen = objCatCiudadDAO.findByPersistenceId(rs.getLong("ciudadExamen_pid"))				
					def obj = jsonSlurper.parseText(JsonOutput.toJson(ciudadExamen));
					errorLog+="  obj:"+ obj
					map = [:]
					for ( prop in obj ) {
						if(prop.key == "persistenceId") {
							map["persistenceId_string"] = prop.value+"";
						}
						map[prop.key] = prop.value;
					}
					
					catSolicitudDeAdmisionInput.put("ciudadExamen",map);
					
				}else {
					catSolicitudDeAdmisionInput.put("ciudadExamen",null);
				}
				
				if(rs.getString("ciudadExamenPais_pid") != null) {
					CatCiudad ciudadExamenPais = objCatCiudadDAO.findByPersistenceId(rs.getLong("ciudadExamenPais_pid"))
					def obj = jsonSlurper.parseText(JsonOutput.toJson(ciudadExamenPais));
					errorLog+="  obj:"+ obj
					map = [:]
					for ( prop in obj ) {
						if(prop.key == "persistenceId") {
							map["persistenceId_string"] = prop.value+"";
						}
						map[prop.key] = prop.value;
					}
					
					catSolicitudDeAdmisionInput.put("ciudadExamenPais",map);
					
				}else {
					catSolicitudDeAdmisionInput.put("ciudadExamenPais",null);
				}
				
				if(rs.getString("catPeriodo_pid") != null) {
					CatPeriodo CatPeriodo = objCatPeriodoDAO.findByPersistenceId(rs.getLong("catPeriodo_pid"))
					def obj = jsonSlurper.parseText(JsonOutput.toJson(CatPeriodo)); 
					map = [:]
					for ( prop in obj ) {
						if(prop.key == "persistenceId") {
							map["persistenceId_string"] = prop.value+"";
						}
						map[prop.key] = prop.value;
					}
				
					catSolicitudDeAdmisionInput.put("catPeriodo",map);
					
				}else {
					catSolicitudDeAdmisionInput.put("catPeriodo",null);
				}
				
				
				if(rs.getString("catCampus_pid") != null) {
					CatCampus CatCampus = objCatCampusDAO.findByPersistenceId(rs.getLong("catCampus_pid"))
					def obj = jsonSlurper.parseText(JsonOutput.toJson(CatCampus)); 
					map = [:]
					for ( prop in obj ) {
						if(prop.key == "persistenceId") {
							map["persistenceId_string"] = prop.value+"";
						}
						map[prop.key] = prop.value;
					}
				
					catSolicitudDeAdmisionInput.put("catCampus",map);
					
				}else {
					catSolicitudDeAdmisionInput.put("catCampus",null);
				}
				
				if(rs.getString("catCampusEstudio_pid") != null) {
					CatCampus CatCampusEstudio = objCatCampusDAO.findByPersistenceId(rs.getLong("catCampusEstudio_pid"))
					def obj = jsonSlurper.parseText(JsonOutput.toJson(CatCampusEstudio));
					map = [:]
					for ( prop in obj ) {
						if(prop.key == "persistenceId") {
							map["persistenceId_string"] = prop.value+"";
						}
						map[prop.key] = prop.value;
					}
				
					catSolicitudDeAdmisionInput.put("catCampusEstudio",map);
					
				}else {
					catSolicitudDeAdmisionInput.put("catCampusEstudio",null);
				}
				
				
				if(rs.getString("catEstadoExamen_pid") != null) {
					CatEstados CatEstadoExamen = objCatEstadoDAO.findByPersistenceId(rs.getLong("catEstadoExamen_pid"))
					def obj = jsonSlurper.parseText(JsonOutput.toJson(CatEstadoExamen));
					map = [:]
					for ( prop in obj ) {
						if(prop.key == "persistenceId") {
							map["persistenceId_string"] = prop.value+"";
						}
						
						map[prop.key] = prop.value;
						
						
						
					}
				
					catSolicitudDeAdmisionInput.put("catEstadoExamen",map);
					
				}else {
					catSolicitudDeAdmisionInput.put("catEstadoExamen",null);
				}
				
				if(rs.getString("catGestionEscolar_pid") != null) {
					CatGestionEscolar catGestionEscolar = objCatGestionEscolarDAO.findByPersistenceId(rs.getLong("catGestionEscolar_pid"))
					def obj = jsonSlurper.parseText(JsonOutput.toJson(catGestionEscolar));
					map = [:]
					for ( prop in obj ) {
						if(prop.key == "persistenceId") {
							map["persistenceId_string"] = prop.value+"";
						}
						map[prop.key] = prop.value;
					}
				
					catSolicitudDeAdmisionInput.put("catGestionEscolar",map);
					
				}else {
					catSolicitudDeAdmisionInput.put("catGestionEscolar",null);
				}
				
				
				if(rs.getString("catLugarExamen_pid") != null) {
					CatLugarExamen catLugarExamen = objCatLugarExamenDAO.findByPersistenceId(rs.getLong("catLugarExamen_pid"))
					def obj = jsonSlurper.parseText(JsonOutput.toJson(catLugarExamen));
					map = [:]
					for ( prop in obj ) {
						if(prop.key == "persistenceId") {
							map["persistenceId_string"] = prop.value+"";
						}
						map[prop.key] = prop.value;
					}
				
					catSolicitudDeAdmisionInput.put("catLugarExamen",map);
					
				}else {
					catSolicitudDeAdmisionInput.put("catLugarExamen",null);
				}
				
				if(rs.getString("catPaisExamen_pid") != null) {
					CatPais catPaisExamen = objCatPaisDAO.findByPersistenceId(rs.getLong("catPaisExamen_pid"))
					def obj = jsonSlurper.parseText(JsonOutput.toJson(catPaisExamen));
					map = [:]
					for ( prop in obj ) {
						if(prop.key == "persistenceId") {
							map["persistenceId_string"] = prop.value+"";
						}
						map[prop.key] = prop.value;
					}
				
					catSolicitudDeAdmisionInput.put("catPaisExamen",map);
					
				}else {
					catSolicitudDeAdmisionInput.put("catPaisExamen",null);
				}
				
				if(rs.getString("catPropedeutico_pid") != null) {
					CatPropedeutico catPropedeutico = objCatPropedeuticoDAO.findByPersistenceId(rs.getLong("catPropedeutico_pid"))
					def obj = jsonSlurper.parseText(JsonOutput.toJson(catPropedeutico));
					map = [:]
					for ( prop in obj ) {
						if(prop.key == "persistenceId") {
							map["persistenceId_string"] = prop.value+"";
						}
						map[prop.key] = prop.value;
					}
				
					catSolicitudDeAdmisionInput.put("catPropedeutico",map);
					
				}else {
					catSolicitudDeAdmisionInput.put("catPropedeutico",null);
				}
				
				
			}
			
			contract.put("catSolicitudDeAdmisionInput",catSolicitudDeAdmisionInput);
			
			
			ContactoEmergencias contactoEmergencia = objContactoEmergenciaDAO.findByPersistenceId(1)
			def obj = jsonSlurper.parseText(JsonOutput.toJson(contactoEmergencia));
			map = [:]
			for ( prop in obj ) {
				if(prop.key == "persistenceId") {
					
				}else {
					map[prop.key] = prop.value;
				}
				
			}
		
			//errorLog+= contract
			Long processId = context.getApiClient().getProcessAPI().getLatestProcessDefinitionId("Proceso admisiones");
			ProcessInstance processInstance = context.getApiClient().getProcessAPI().startProcessWithInputs(processId, contract);
			caseId = processInstance.getRootProcessInstanceId();
			
			
			catSolicitudDeAdmisionInput.put("catSexo",null);
			catSolicitudDeAdmisionInput.put("fechaNacimiento",null);
			catSolicitudDeAdmisionInput.put("catEstadoCivil",null);
			catSolicitudDeAdmisionInput.put("catNacionalidad",null);
			catSolicitudDeAdmisionInput.put("catPresentasteEnOtroCampus",null);
			catSolicitudDeAdmisionInput.put("catCampusPresentadoSolicitud",null);
			catSolicitudDeAdmisionInput.put("catReligion",null);
			catSolicitudDeAdmisionInput.put("curp",null);
			catSolicitudDeAdmisionInput.put("usuarioFacebook",null);
			catSolicitudDeAdmisionInput.put("usiarioTwitter",null);
			catSolicitudDeAdmisionInput.put("usuarioInstagram",null);
			catSolicitudDeAdmisionInput.put("telefonoCelular",null);
			catSolicitudDeAdmisionInput.put("foto",null);
			catSolicitudDeAdmisionInput.put("actaNacimiento",null);
			catSolicitudDeAdmisionInput.put("calle",null);
			catSolicitudDeAdmisionInput.put("codigoPostal",null);
			catSolicitudDeAdmisionInput.put("catPais",null);
			catSolicitudDeAdmisionInput.put("catEstado",null);
			catSolicitudDeAdmisionInput.put("ciudad",null);
			catSolicitudDeAdmisionInput.put("calle2",null);
			catSolicitudDeAdmisionInput.put("numExterior",null);
			catSolicitudDeAdmisionInput.put("numInterior",null);
			catSolicitudDeAdmisionInput.put("colonia",null);
			catSolicitudDeAdmisionInput.put("telefono",null);
			catSolicitudDeAdmisionInput.put("otroTelefonoContacto",null);
			catSolicitudDeAdmisionInput.put("promedioGeneral",null);
			catSolicitudDeAdmisionInput.put("comprobanteCalificaciones",null);
			catSolicitudDeAdmisionInput.put("datosVeridicos",null);
			catSolicitudDeAdmisionInput.put("aceptoAvisoPrivacidad",null);
			catSolicitudDeAdmisionInput.put("confirmarAutorDatos",null);
			catSolicitudDeAdmisionInput.put("catBachilleratos",null);
			catSolicitudDeAdmisionInput.put("paisBachillerato",null);
			catSolicitudDeAdmisionInput.put("estadoBachillerato",null);
			catSolicitudDeAdmisionInput.put("ciudadBachillerato",null);
			catSolicitudDeAdmisionInput.put("bachillerato",null);
			catSolicitudDeAdmisionInput.put("delegacionMunicipio",null);
			catSolicitudDeAdmisionInput.put("estadoExtranjero",null);
			catSolicitudDeAdmisionInput.put("resultadoPAA",0);
			catSolicitudDeAdmisionInput.put("selectedIndex",0);
			catSolicitudDeAdmisionInput.put("selectedIndexPersonal",null);
			catSolicitudDeAdmisionInput.put("selectedIndexFamiliar",null);
			catSolicitudDeAdmisionInput.put("selectedIndexRevision",null);
			catSolicitudDeAdmisionInput.put("tienePAA",null);
			catSolicitudDeAdmisionInput.put("tieneDescuento",null);
			catSolicitudDeAdmisionInput.put("admisionAnahuac",null);
			catSolicitudDeAdmisionInput.put("urlFoto",null);
			catSolicitudDeAdmisionInput.put("urlConstancia",null);
			catSolicitudDeAdmisionInput.put("urlCartaAA",null);
			catSolicitudDeAdmisionInput.put("urlResultadoPAA",null);
			catSolicitudDeAdmisionInput.put("urlActaNacimiento",null);
			catSolicitudDeAdmisionInput.put("urlDescuentos",null);
			catSolicitudDeAdmisionInput.put("catConcluisteProceso",null);
			catSolicitudDeAdmisionInput.put("catResultadoAdmision",null);
			
			
			contract.put("catSolicitudDeAdmisionInput",catSolicitudDeAdmisionInput);
			
			contract.put("isEnviarSolicitudCont",false);
			map = [];
			
			contract.put("actaNacimientoDocumentInput",map);
			contract.put("cartaAADocumentInput",map);
			contract.put("constanciaDocumentInput",map);
			contract.put("fotoPasaporteDocumentInput",map);
			contract.put("descuentoDocumentInput",map);
			contract.put("resultadoCBDocumentInput",map);
			map=[:];
			map["apellidos"] = "";
			map["calle"] = "";
			map["catCampusEgreso"] = null;
			map["catEgresoAnahuac"] = null;
			map["catEscolaridad"] = null;
			map["catEstado"] = null;
			map["catPais"] = null;
			map["catTitulo"] = null;
			map["catTrabaja"] = null;
			map["ciudad"] = "";
			map["codigoPostal"] = "";
			map["colonia"] = "";
			map["correoElectronico"] = "";
			map["countIntento"] = null;
			map["delegacionMunicipio"] = "";
			map["desconozcoDatosPadres"] = false;
			map["empresaTrabaja"] = "";
			map["estadoExtranjero"] = "";
			map["giroEmpresa"] = "";
			map["isTutor"] = false;
			map["nombre"] = "";
			map["numeroExterior"] = "";
			map["numeroInterior"] = "";
			map["puesto"] = "";
			map["telefono"] = "";
			map["vive"] = null;
			map["viveContigo"] = false;
			
			contract.put("madreInput",map);
			contract.put("padreInput",map);
			
			Result respuesta = new Result();
			
			map["catParentezco"] = null;
			map["persistenceId_string"] = '';
			map["otroParentesco"] = '';
			
			def tutores = [];
			def contexto = jsonSlurper.parseText(JsonOutput.toJson(getUserContext(Long.parseLong(object.caseid), context).getData().get(0)));
			//errorLog += contexto
			for(int i = 0; i<contexto?.tutor_ref?.storageIds?.size(); i++) {
				tutores.add(map)
			}
			contract.put("tutorInput",tutores);
			
			map=[:];
			map["telefono"] = "";
			map["nombre"] = "";
			map["catCasoDeEmergencia"] = null;
			map["persistenceId_string"] = '';
			map["telefonoCelular"] = '';
			map["parentesco"] = '';
			map["catParentesco"] = null;
			def contacto = [];
			
			contexto = jsonSlurper.parseText(JsonOutput.toJson(getUserContext(Long.parseLong(object.caseid), context).getData().get(0)));
			//errorLog += contexto
			for(int i = 0; i<contexto?.contactoEmergencia_ref?.storageIds?.size(); i++) {
				contacto.add(map)
			}
			contract.put("contactoEmergenciaInput",contacto);
			
			sleep(5000);
			
			respuesta = validarAspirante(caseId, context,contract);
			errorLog +=", tarea:"+ respuesta;
			sleep(5000);
			respuesta = updateDatosSolicitud(object.caseid,caseId.toString(),context);
			errorLog ="Update:"+ respuesta;
			
			List < String> rows = new ArrayList <String> ();
			rows.add("${caseId}")
			
			resultado.setSuccess(true);
			resultado.setError_info(errorLog);
			resultado.setData(rows);
		} catch (Exception loginApi) {
			LOGGER.error("[EXCEPTION]" + loginApi.getMessage());
			resultado.setSuccess(false);
			resultado.setError(loginApi.getMessage())
			resultado.setError_info(errorLog)
		}finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result validarAspirante(Long caseid,RestAPIContext context,Map<String, Serializable> contract) {
		Result resultado = new Result();
		List<CatRegistro> lstCatRegistro = new ArrayList<CatRegistro>();
		CatRegistro objCatRegistro = new CatRegistro();
		String errorLog ="";
		Boolean closeCon = false;
		try {
			String username = "";
			String password = "";
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)
			
			SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(0, 99999);
			searchBuilder.filter(HumanTaskInstanceSearchDescriptor.NAME, "Validar Cuenta");
			
			final SearchOptions searchOptions = searchBuilder.done();
			SearchResult<HumanTaskInstance>  SearchHumanTaskInstanceSearch = apiClient.getProcessAPI().searchHumanTaskInstances(searchOptions);
			List<HumanTaskInstance> lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
			def catRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			for(HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstanceSearch) {
				lstCatRegistro = catRegistroDAO.findByCaseId(objHumanTaskInstance.getRootContainerId(), 0, 1)
				if(lstCatRegistro != null) {
					if(lstCatRegistro.size() > 0) {
						objCatRegistro = new CatRegistro();
						objCatRegistro = lstCatRegistro.get(0);
						if(objCatRegistro.getCaseId().equals(caseid)) {
							apiClient.getProcessAPI().assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
							apiClient.getProcessAPI().executeFlowNode(objHumanTaskInstance.getId());
						}
					}
				}
			}
			
			sleep(5000);
			
			searchBuilder = new SearchOptionsBuilder(0, 99999);
			searchBuilder.filter(HumanTaskInstanceSearchDescriptor.NAME, "Llenar solicitud");
			final SearchOptions searchOptions2 = searchBuilder.done();
			SearchHumanTaskInstanceSearch = apiClient.getProcessAPI().searchHumanTaskInstances(searchOptions2);
			lstHumanTaskInstanceSearch = SearchHumanTaskInstanceSearch.getResult();
			catRegistroDAO = context.apiClient.getDAO(CatRegistroDAO.class);
			for(HumanTaskInstance objHumanTaskInstance : lstHumanTaskInstanceSearch) {
				lstCatRegistro = catRegistroDAO.findByCaseId(objHumanTaskInstance.getRootContainerId(), 0, 1)
				if(lstCatRegistro != null) {
					if(lstCatRegistro.size() > 0) {
						objCatRegistro = new CatRegistro();
						objCatRegistro = lstCatRegistro.get(0);
						if(objCatRegistro.getCaseId().equals(caseid)) {
							apiClient.getProcessAPI().assignUserTask(objHumanTaskInstance.getId(), context.getApiSession().getUserId());
							apiClient.getProcessAPI().executeUserTask(objHumanTaskInstance.getId(), contract)
						}
					}
				}
			}
			
			resultado.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			e.printStackTrace();
		}
		return resultado;
	}
	
	
	public Result updateDatosSolicitud(String caseIdOrigen,caseIdDestino,RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		try {
			closeCon = validarConexion();
			def jsonSlurper = new JsonSlurper();
			def JsonOutput = new JsonOutput();
			
			con.setAutoCommit(false)
			// INSERTAR ISELIMINADO
			pstm = con.prepareStatement("UPDATE solicitudDeAdmision SET catSexo_pid = sda.catSexo_pid, fechaNacimiento = sda.fechaNacimiento, catEstadoCivil_pid = sda.catEstadoCivil_pid, catNacionalidad_pid = sda.catNacionalidad_pid, catPresentasteEnOtroCampus_pid = sda.catPresentasteEnOtroCampus_pid, catConcluisteProceso_pid = sda.catConcluisteProceso_pid, catReligion_pid = sda.catReligion_pid, curp = sda.curp, telefonoCelular = sda.telefonoCelular, foto = sda.foto, actaNacimiento = sda.actaNacimiento, calle = sda.calle, codigoPostal = sda.codigoPostal, catPais_pid = sda.catPais_pid, catEstado_pid = sda.catEstado_pid, ciudad = sda.ciudad, calle2 = sda.calle2, numExterior = sda.numExterior, numInterior = sda.numInterior, colonia = sda.colonia, telefono = sda.telefono, otroTelefonoContacto = sda.otroTelefonoContacto, promedioGeneral = sda.promedioGeneral, comprobanteCalificaciones = sda.comprobanteCalificaciones, datosVeridicos = sda.datosVeridicos, aceptoAvisoPrivacidad = sda.aceptoAvisoPrivacidad, confirmarAutorDatos = sda.confirmarAutorDatos, catBachilleratos_pid = sda.catBachilleratos_pid, paisBachillerato = sda.paisBachillerato, estadoBachillerato = sda.estadoBachillerato, ciudadBachillerato = sda.ciudadBachillerato, bachillerato = sda.bachillerato, delegacionMunicipio = sda.delegacionMunicipio, estadoExtranjero = sda.estadoExtranjero, resultadoPAA = sda.resultadoPAA, tienePAA = sda.tienePAA, tieneDescuento = sda.tieneDescuento, admisionAnahuac = sda.admisionAnahuac, necesitoAyuda = sda.necesitoAyuda, countRechazos = sda.countRechazos, urlFoto = sda.urlFoto, urlConstancia = sda.urlConstancia, urlCartaAA = sda.urlCartaAA, urlResultadoPAA = sda.urlResultadoPAA, urlActaNacimiento = sda.urlActaNacimiento, urldescuentos = sda.urldescuentos, isEliminado = false  FROM (SELECT catSexo_pid,fechaNacimiento,catEstadoCivil_pid,catNacionalidad_pid,catPresentasteEnOtroCampus_pid,catConcluisteProceso_pid,catReligion_pid,curp,telefonoCelular,foto,actaNacimiento,calle,codigoPostal,catPais_pid,catEstado_pid,ciudad,calle2,numExterior,numInterior,colonia,telefono,otroTelefonoContacto,promedioGeneral,comprobanteCalificaciones,datosVeridicos,aceptoAvisoPrivacidad,confirmarAutorDatos,catBachilleratos_pid,paisBachillerato,estadoBachillerato,ciudadBachillerato,bachillerato,delegacionMunicipio,estadoExtranjero,resultadoPAA,tienePAA,tieneDescuento,admisionAnahuac, necesitoAyuda,countRechazos, urlFoto,urlConstancia,urlCartaAA,urlResultadoPAA,urlActaNacimiento,urldescuentos FROM solicitudDeAdmision WHERE caseid::integer = ${caseIdOrigen} ) as sda WHERE solicitudDeAdmision.caseid::integer = "+caseIdDestino);
			pstm.executeUpdate();
			errorLog+="solicitud";
			
			
			pstm = con.prepareStatement("SELECT persistenceid, correoelectronico FROM solicitudDeAdmision WHERE caseid::integer = "+caseIdOrigen);
			rs = pstm.executeQuery();
			errorLog+=", persistenceid1";
			Long persistenceIdOrigen = 0L;
			String correo = "";
			if(rs.next()) {
				persistenceIdOrigen = rs.getLong("persistenceid");
				correo = rs.getString("correoelectronico");
			}
			
			pstm = con.prepareStatement("SELECT persistenceid FROM solicitudDeAdmision WHERE caseid::integer = "+caseIdDestino);
			rs = pstm.executeQuery();
			errorLog+=", persistenceid2";
			if(rs.next()) {
				try {
					pstm = con.prepareStatement("INSERT INTO SOLICITUDDEADM_CATCAMPUSPRESE (SOLICITUDDEADMISION_PID,CATCAMPUS_PID,CATCAMPUSPRESENTADOSOLICITUD_ORDER) SELECT ${rs.getLong("persistenceid")} AS SOLICITUDDEADMISION_PID,CATCAMPUS_PID,CATCAMPUSPRESENTADOSOLICITUD_ORDER FROM SOLICITUDDEADM_CATCAMPUSPRESE WHERE SOLICITUDDEADMISION_PID = "+persistenceIdOrigen);
					pstm.executeUpdate();
					errorLog+=", insert campusPresente";
				}catch(Exception a) {
					errorLog+=", error campusPresente:"+a
				}
				
			}
			
			
			pstm = con.prepareStatement("UPDATE solicitudDeAdmision SET correoelectronico = '${correo} (rechazado)' where caseid = '${caseIdOrigen}' ")
			pstm.executeUpdate();
			errorLog+=", cambio correo"+caseIdOrigen;
			
			pstm = con.prepareStatement("UPDATE catRegistro SET correoelectronico = '${correo} (rechazado)' where caseid::INTEGER = ${caseIdOrigen} ")
			pstm.executeUpdate();
			errorLog+=", cambio correo registro"+caseIdOrigen;
			Long autodescripcionId=0L
			pstm = con.prepareStatement("INSERT INTO AutodescripcionV2 (persistenceid,admiraspersonalidadmadre,admiraspersonalidadpadre,asprctosnogustanreligion,caracteristicasexitocarrera,caseid,comodescribesrelacionhermanos,comodescribestufamilia,comoestaconformadafamilia,comoresolvisteproblema,comotedescribentusamigos,conquienplaticasproblemas,cualexamenextrapresentaste,defectosobservasmadre,defectosobservaspadre,detallespersonalidad,empresatrabajas,empresatrabajaste,expectativascarrera,familiarmejorrelacion,fuentesinfluyerondesicion,hasrecibidoalgunaterapia,materiascalifaltas,materiascalifbajas,materiasnotegustan,materiastegustan,mayorproblemaenfrentado,metascortoplazo,metaslargoplazo,metasmedianoplazo,motivoaspectosnogustanreligion,motivoelegistecarrera,motivoexamenextraordinario,motivopadresnoacuerdo,motivoreprobaste,organizacionhassidojefe,organizacionparticipas,organizacionesperteneces,pageindex,periodoreprobaste,persistenceversion,personasinfluyerondesicion,principalesdefectos,principalesvirtudes,problemassaludatencioncontinua,profesionalcomoteves,quecambiariasdeti,quecambiariasdetufamilia,quedeportepracticas,quehacesentutiempolibre,quelecturaprefieres,tipodiscapacidad,catactualnentetrabajas_pid,catareabachillerato_pid,cataspectodesagradareligio_pid,catestudiadoextranjero_pid,catexperienciaayudacarrera_pid,cathaspresentadoexamenextr_pid,cathasreprobado_pid,cathastenidotrabajo_pid,catinscritootrauniversidad_pid,catjefeorganizacionsocial_pid,catorientacionvocacional_pid,catpadresdeacuerdo_pid,catparticipasgruposocial_pid,catpersonasaludable_pid,catpracticasdeporte_pid,catpracticasreligion_pid,catproblemassaludatencion_pid,catrecibidoterapia_pid,cattegustaleer_pid,catvivesestadodiscapacidad_pid,catyaresolvisteelproblema_pid,paisestudiasteextranjero_pid,pertenecesorganizacion_pid,tiempoestudiasteextranjero_pid) SELECT (case when (SELECT max(persistenceId)+1 from AutodescripcionV2 ) is null then 1 else (SELECT max(persistenceId)+1 from AutodescripcionV2) end) AS persistenceid,admiraspersonalidadmadre,admiraspersonalidadpadre,asprctosnogustanreligion,caracteristicasexitocarrera, ${caseIdDestino} AS caseid,comodescribesrelacionhermanos,comodescribestufamilia,comoestaconformadafamilia,comoresolvisteproblema,comotedescribentusamigos,conquienplaticasproblemas,cualexamenextrapresentaste,defectosobservasmadre,defectosobservaspadre,detallespersonalidad,empresatrabajas,empresatrabajaste,expectativascarrera,familiarmejorrelacion,fuentesinfluyerondesicion,hasrecibidoalgunaterapia,materiascalifaltas,materiascalifbajas,materiasnotegustan,materiastegustan,mayorproblemaenfrentado,metascortoplazo,metaslargoplazo,metasmedianoplazo,motivoaspectosnogustanreligion,motivoelegistecarrera,motivoexamenextraordinario,motivopadresnoacuerdo,motivoreprobaste,organizacionhassidojefe,organizacionparticipas,organizacionesperteneces,pageindex,periodoreprobaste,persistenceversion,personasinfluyerondesicion,principalesdefectos,principalesvirtudes,problemassaludatencioncontinua,profesionalcomoteves,quecambiariasdeti,quecambiariasdetufamilia,quedeportepracticas,quehacesentutiempolibre,quelecturaprefieres,tipodiscapacidad,catactualnentetrabajas_pid,catareabachillerato_pid,cataspectodesagradareligio_pid,catestudiadoextranjero_pid,catexperienciaayudacarrera_pid,cathaspresentadoexamenextr_pid,cathasreprobado_pid,cathastenidotrabajo_pid,catinscritootrauniversidad_pid,catjefeorganizacionsocial_pid,catorientacionvocacional_pid,catpadresdeacuerdo_pid,catparticipasgruposocial_pid,catpersonasaludable_pid,catpracticasdeporte_pid,catpracticasreligion_pid,catproblemassaludatencion_pid,catrecibidoterapia_pid,cattegustaleer_pid,catvivesestadodiscapacidad_pid,catyaresolvisteelproblema_pid,paisestudiasteextranjero_pid,pertenecesorganizacion_pid,tiempoestudiasteextranjero_pid FROM AutodescripcionV2 WHERE caseid ="+caseIdOrigen + " RETURNING persistenceid")
			rs = pstm.executeQuery();
			if(rs.next()) {
				autodescripcionId = rs.getLong("persistenceid")
			}
			errorLog+=", insertAutoDescripcion";
			
			Long detalleSolicitudId=0L
			pstm = con.prepareStatement("INSERT INTO DetalleSolicitud (persistenceid,caseid,idbanner) SELECT (case when (SELECT max(persistenceId)+1 from DetalleSolicitud ) is null then 1 else (SELECT max(persistenceId)+1 from DetalleSolicitud) end) AS persistenceid, '${caseIdDestino}' as caseid,idbanner FROM DetalleSolicitud WHERE caseid = '"+caseIdOrigen+"' RETURNING persistenceid");
			rs = pstm.executeQuery();
			errorLog+=", detalleSolicitud";
			
			if(rs.next()) {
				detalleSolicitudId = rs.getLong("persistenceid")
			}
			
			
			
			pstm = con.prepareStatement("UPDATE DetalleSolicitud SET persistenceversion = '0' WHERE persistenceversion is null ")
			pstm.executeUpdate();
			
			def contexto = jsonSlurper.parseText(JsonOutput.toJson(getUserContext(Long.parseLong(caseIdOrigen), context)?.getData()?.get(0)));
			def contexto2 = jsonSlurper.parseText(JsonOutput.toJson(getUserContext(Long.parseLong(caseIdDestino), context)?.getData()?.get(0)));
			
			pstm = con.prepareStatement("UPDATE padresTutor SET catTitulo_pid = pt.catTitulo_pid, catParentezco_pid = pt.catParentezco_pid, nombre = pt.nombre, apellidos = pt.apellidos, correoElectronico = pt.correoElectronico, catEscolaridad_pid = pt.catEscolaridad_pid, catEgresoAnahuac_pid = pt.catEgresoAnahuac_pid, catCampusEgreso_pid = pt.catCampusEgreso_pid, catTrabaja_pid = pt.catTrabaja_pid, empresaTrabaja = pt.empresaTrabaja, giroEmpresa = pt.giroEmpresa, puesto = pt.puesto, isTutor = pt.isTutor, vive_pid = pt.vive_pid, calle = pt.calle, catPais_pid = pt.catPais_pid, numeroExterior = pt.numeroExterior, numeroInterior = pt.numeroInterior, catEstado_pid = pt.catEstado_pid, ciudad = pt.ciudad, colonia = pt.colonia, telefono = pt.telefono, codigoPostal = pt.codigoPostal, viveContigo = pt.viveContigo, otroParentesco = pt.otroParentesco, desconozcoDatosPadres = pt.desconozcoDatosPadres, delegacionMunicipio = pt.delegacionMunicipio, estadoExtranjero = pt.estadoExtranjero FROM (SELECT catTitulo_pid,catParentezco_pid,nombre,apellidos,correoElectronico,catEscolaridad_pid,catEgresoAnahuac_pid,catCampusEgreso_pid,catTrabaja_pid,empresaTrabaja,giroEmpresa,puesto,isTutor,vive_pid,calle,catPais_pid,numeroExterior,numeroInterior,catEstado_pid,ciudad,colonia,telefono,codigoPostal,viveContigo,otroParentesco,desconozcoDatosPadres,delegacionMunicipio,estadoExtranjero FROM padresTutor WHERE persistenceId = ${contexto?.madre_ref?.storageId} ) as pt WHERE padresTutor.persistenceId = "+contexto2?.madre_ref?.storageId);
			pstm.executeUpdate();
			errorLog+=", padresTutor_Madre";
			
			pstm = con.prepareStatement("UPDATE padresTutor SET catTitulo_pid = pt.catTitulo_pid, catParentezco_pid = pt.catParentezco_pid, nombre = pt.nombre, apellidos = pt.apellidos, correoElectronico = pt.correoElectronico, catEscolaridad_pid = pt.catEscolaridad_pid, catEgresoAnahuac_pid = pt.catEgresoAnahuac_pid, catCampusEgreso_pid = pt.catCampusEgreso_pid, catTrabaja_pid = pt.catTrabaja_pid, empresaTrabaja = pt.empresaTrabaja, giroEmpresa = pt.giroEmpresa, puesto = pt.puesto, isTutor = pt.isTutor, vive_pid = pt.vive_pid, calle = pt.calle, catPais_pid = pt.catPais_pid, numeroExterior = pt.numeroExterior, numeroInterior = pt.numeroInterior, catEstado_pid = pt.catEstado_pid, ciudad = pt.ciudad, colonia = pt.colonia, telefono = pt.telefono, codigoPostal = pt.codigoPostal, viveContigo = pt.viveContigo, otroParentesco = pt.otroParentesco, desconozcoDatosPadres = pt.desconozcoDatosPadres, delegacionMunicipio = pt.delegacionMunicipio, estadoExtranjero = pt.estadoExtranjero FROM (SELECT catTitulo_pid,catParentezco_pid,nombre,apellidos,correoElectronico,catEscolaridad_pid,catEgresoAnahuac_pid,catCampusEgreso_pid,catTrabaja_pid,empresaTrabaja,giroEmpresa,puesto,isTutor,vive_pid,calle,catPais_pid,numeroExterior,numeroInterior,catEstado_pid,ciudad,colonia,telefono,codigoPostal,viveContigo,otroParentesco,desconozcoDatosPadres,delegacionMunicipio,estadoExtranjero FROM padresTutor WHERE persistenceId = ${contexto?.padre_ref?.storageId} ) as pt WHERE padresTutor.persistenceId = "+contexto2?.padre_ref?.storageId);
			pstm.executeUpdate();
			errorLog+=", padresTutor_Padre";
			
			
			
			for(int i = 0; i<contexto?.tutor_ref?.storageIds?.size(); i++) {
				pstm = con.prepareStatement("UPDATE padresTutor SET catTitulo_pid = pt.catTitulo_pid, catParentezco_pid = pt.catParentezco_pid, nombre = pt.nombre, apellidos = pt.apellidos, correoElectronico = pt.correoElectronico, catEscolaridad_pid = pt.catEscolaridad_pid, catEgresoAnahuac_pid = pt.catEgresoAnahuac_pid, catCampusEgreso_pid = pt.catCampusEgreso_pid, catTrabaja_pid = pt.catTrabaja_pid, empresaTrabaja = pt.empresaTrabaja, giroEmpresa = pt.giroEmpresa, puesto = pt.puesto, isTutor = pt.isTutor, vive_pid = pt.vive_pid, calle = pt.calle, catPais_pid = pt.catPais_pid, numeroExterior = pt.numeroExterior, numeroInterior = pt.numeroInterior, catEstado_pid = pt.catEstado_pid, ciudad = pt.ciudad, colonia = pt.colonia, telefono = pt.telefono, codigoPostal = pt.codigoPostal, viveContigo = pt.viveContigo, otroParentesco = pt.otroParentesco, desconozcoDatosPadres = pt.desconozcoDatosPadres, delegacionMunicipio = pt.delegacionMunicipio, estadoExtranjero = pt.estadoExtranjero FROM (SELECT catTitulo_pid,catParentezco_pid,nombre,apellidos,correoElectronico,catEscolaridad_pid,catEgresoAnahuac_pid,catCampusEgreso_pid,catTrabaja_pid,empresaTrabaja,giroEmpresa,puesto,isTutor,vive_pid,calle,catPais_pid,numeroExterior,numeroInterior,catEstado_pid,ciudad,colonia,telefono,codigoPostal,viveContigo,otroParentesco,desconozcoDatosPadres,delegacionMunicipio,estadoExtranjero FROM padresTutor WHERE persistenceId = ${contexto?.tutor_ref?.storageIds[i]} ) as pt WHERE padresTutor.persistenceId = "+contexto2?.tutor_ref?.storageIds[i]);
				pstm.executeUpdate();
				errorLog+=", padresTutor_Emergencia_"+i;
			}
			
			for(int i = 0; i<contexto?.contactoEmergencia_ref?.storageIds?.size(); i++) {
				pstm = con.prepareStatement("UPDATE ContactoEmergencias SET nombre= ce.nombre, telefono = ce.telefono, catCasoDeEmergencia_pid = ce.catCasoDeEmergencia_pid, telefonoCelular = ce.telefonoCelular, parentesco = ce.parentesco, catParentesco_pid = ce.catParentesco_pid FROM (SELECT nombre,telefono,catCasoDeEmergencia_pid,telefonoCelular,parentesco,catParentesco_pid FROM ContactoEmergencias WHERE persistenceId = ${contexto?.contactoEmergencia_ref?.storageIds[i]} ) as ce WHERE ContactoEmergencias.persistenceId =  "+contexto2?.contactoEmergencia_ref?.storageIds[i]);
				pstm.executeUpdate();
				errorLog+=", ContactoEmergencias_"+i;
			}
			
			/*for(int i = 0; i<contexto?.lstInformacionEscolar_ref?.storageIds?.size(); i++) {
				pstm = con.prepareStatement("UPDATE EscuelasHasEstado SET anofin = pt.anofin, anoinicio = pt.anoinicio, caseid = ${caseIdDestino}, ciudad = pt.ciudad, estadostring = pt.estadostring, otraescuela = pt.otraescuela, persistenceversion = pt.persistenceversion, promedio = pt.promedio, escuela_pid = pt.escuela_pid, estado_pid = pt.estado_pid, grado_pid = pt.grado_pid, pais_pid = pt.pais_pid, tipo_pid = pt.tipo_pid, vencido = pt.vencido FROM (SELECT anofin, anoinicio, caseid, ciudad estadostring, otraescuela, persistenceversion, promedio, escuela_pid, estado_pid, grado_pid, pais_pid, tipo_pid, vencido FROM EscuelasHasEstado WHERE persistenceId = ${contexto?.lstInformacionEscolar_ref?.storageIds[i]} ) as pt WHERE EscuelasHasEstado.persistenceId = "+contexto2?.lstInformacionEscolar_ref?.storageIds[i]);
				pstm.executeUpdate();
				errorLog+=", lstInformacionEscolar_ref";
			}*/
			
			
			
			
			con.commit();
			
			con.close();
			
			validarConexionBonita()
			con.setAutoCommit(false)
			errorLog+=",INICIO lstInformacionEscolar_ref";
			List<Long> lstEscuelasHasEstadoids = new ArrayList()
			pstm = con prepareStatement("SELECT  md.data_id FROM ref_biz_data_inst data  INNER JOIN multi_biz_data md on md.id=data.id where proc_inst_id=${caseIdOrigen} AND data.name='lstInformacionEscolar'")
			rs = pstm.executeQuery()
			while(rs.next()) {
				lstEscuelasHasEstadoids.add(rs.getLong("data_id"))
			}
			
			pstm = con.prepareStatement("UPDATE REF_BIZ_DATA_INST SET data_id=? where proc_inst_id=${caseIdDestino} and name='autodescripcionV2' ")
			pstm.setLong(1,autodescripcionId)

			pstm.executeUpdate();
			
			pstm = con.prepareStatement("UPDATE REF_BIZ_DATA_INST SET data_id=? where proc_inst_id=${caseIdDestino} and name='detalleSolicitud' ")
			pstm.setLong(1,detalleSolicitudId)

			pstm.executeUpdate();
			
			con.commit();
			
			con.close()
			
			validarConexion()
			con.setAutoCommit(false)
			List<Long> lstEscuelasHasEstadoDestinoids = new ArrayList()
			for(Long escuelasHasEstadoId: lstEscuelasHasEstadoids) {
				
				pstm = con.prepareStatement("INSERT INTO EscuelasHasEstado (persistenceid,anofin, anoinicio, ciudad estadostring, otraescuela, persistenceversion, promedio, escuela_pid, estado_pid, grado_pid, pais_pid, tipo_pid, vencido,caseid) SELECT (case when (SELECT max(persistenceId)+1 from EscuelasHasEstado ) is null then 1 else (SELECT max(persistenceId)+1 from EscuelasHasEstado) end) AS persistenceid,anofin, anoinicio, ciudad estadostring, otraescuela, persistenceversion, promedio, escuela_pid, estado_pid, grado_pid, pais_pid, tipo_pid, vencido, '${caseIdDestino}' as caseid FROM EscuelasHasEstado WHERE persistenceid = '"+escuelasHasEstadoId+"' RETURNING persistenceid")
				rs = pstm.executeQuery()
				if(rs.next()) {
					lstEscuelasHasEstadoDestinoids.add(rs.getLong("persistenceid"))
				}
			}
			errorLog+=",FIN lstInformacionEscolar_ref";
			con.commit();
			
			con.close()
			validarConexionBonita()
			con.setAutoCommit(false)
			for(Long pid:lstEscuelasHasEstadoDestinoids) {
				pstm = con.prepareStatement("INSERT INTO multi_biz_data (tenantid,id,idx,data_id) VALUES (1,(SELECT id FROM ref_biz_data_inst WHERE proc_inst_id=${caseIdDestino} AND name='lstInformacionEscolar' ),0, ${pid})")
				pstm.executeUpdate();
			}
			con.commit();
			
			con.close()
			
			validarConexion()
			con.setAutoCommit(false)
			try {
				String username = "";
				String password = "";
				
				/*-------------------------------------------------------------*/
				LoadParametros objLoad = new LoadParametros();
				PropertiesEntity objProperties = objLoad.getParametros();
				username = objProperties.getUsuario();
				password = objProperties.getPassword();
				/*-------------------------------------------------------------*/
				
				org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
				apiClient.login(username, password)
				String consulta_bonita = "[\"SELECT id,processinstanceid FROM arch_document_mapping WHERE processinstanceid = ${caseIdOrigen} \"]"
				def bonita = new NotificacionDAO().simpleSelectBonita(0, 0, consulta_bonita, context)?.getData()?.get(0);
				Document doc = apiClient.getProcessAPI().getArchivedProcessDocument(Long.parseLong(bonita?.id));
				//apiClient.getProcessAPI().updateDocument(101, new DocumentValue(apiClient.getProcessAPI().getDocumentContent(doc.getContentStorageId()), doc.getContentMimeType(), "Fot_47010.jpg"))
				apiClient.getProcessAPI().addDocument(Long.parseLong(caseIdDestino),"FotoPasaporte","FotoPasaporte", new DocumentValue(apiClient.getProcessAPI().getDocumentContent(doc.getContentStorageId()), doc.getContentMimeType(), "Fot_${caseIdDestino}.jpg") )
				
			}catch(Exception a) {
				errorLog+="error:"+a
			}
			
			resultado.setSuccess(true)
			resultado.setError_info(errorLog)
		} catch (Exception e) {
			resultado.setSuccess(false)
			resultado.setError(e.getMessage())
			resultado.setError_info(errorLog)
			con.rollback();
		}finally {
			if (closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado;
	}
	
	public Result getUserContext(Long caseid,RestAPIContext context) {
		Result resultado = new Result();
		String errorLog ="";
		try {
			String username = "";
			String password = "";
			
			List < Map < String, Serializable >> rows = new ArrayList < Map < String, Serializable >> ();
			Map<String, Serializable> contexto;
			
			/*-------------------------------------------------------------*/
			LoadParametros objLoad = new LoadParametros();
			PropertiesEntity objProperties = objLoad.getParametros();
			username = objProperties.getUsuario();
			password = objProperties.getPassword();
			/*-------------------------------------------------------------*/
			
			org.bonitasoft.engine.api.APIClient apiClient = new APIClient()//context.getApiClient();
			apiClient.login(username, password)
			
			try {
				contexto = apiClient.getProcessAPI().getProcessInstanceExecutionContext(caseid);
			}catch(ProcessInstanceNotFoundException ex) {
				String consulta = "[\"SELECT * FROM ARCH_PROCESS_INSTANCE WHERE sourceobjectid = ${caseid} \"]"
				def bonita = new NotificacionDAO().simpleSelectBonita(0, 0, consulta, context)?.getData()?.get(0);
				errorLog+=bonita;
				contexto = apiClient.getProcessAPI().getArchivedProcessInstanceExecutionContext( Long.parseLong(bonita?.id));
			}
			
			/**/
			rows.add(contexto);
			
			resultado.setSuccess(true);
			resultado.setData(rows)
			resultado.setError_info(errorLog);
			} catch (Exception e) {
			LOGGER.error "[ERROR] " + e.getMessage();
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog)
			e.printStackTrace();
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
	public Boolean validarConexionBonita() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno = true
		}
		return retorno
	}
}
