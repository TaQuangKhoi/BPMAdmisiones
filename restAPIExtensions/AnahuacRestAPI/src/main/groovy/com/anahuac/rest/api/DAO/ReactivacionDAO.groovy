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
					inputs.put("countReactivacion",object.countrechazos);
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
}
