package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.model.DetalleSolicitud
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.DB.Statements

import com.bonitasoft.web.extension.rest.RestAPIContext
import groovy.json.JsonSlurper

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.bpm.document.Document


class ImportacionPAADAO {
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
	
	
	public Result postGuardarUsuario(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				closeCon = validarConexion();
				
				//revisar si el aspirante ya esta registrado en la importacion PAA

				String paa = "", sda = "", ds= ""
				pstm = con.prepareStatement(Statements.GET_EXITE_Y_DATOS_DUPLICADOS)
				pstm.setString(1,object.IDBANNER);
				rs= pstm.executeQuery();
				if(rs.next()) {
					
					paa = (rs.getString("idbanner"));
					sda = (rs.getString("primernombre"));
					ds = (rs.getString("dsbanner"));
				}
				
				List<Map<String, Object>> estatus = new ArrayList<Map<String, Object>>();
				
				if(isNullOrEmpty(paa) && !isNullOrEmpty(sda) && !isNullOrEmpty(ds)) {
					con.setAutoCommit(false)
					pstm = con.prepareStatement(Statements.GET_IMPORTACIONPAA, Statement.RETURN_GENERATED_KEYS)
					
					pstm.setString(1,object.IDBANNER);
					pstm.setString(2,object.PAAN);
					pstm.setString(3,object.LA1);
					pstm.setString(4,object.LA2);
					pstm.setString(5,object.LA3);
					pstm.setString(6,object.PG1);
					pstm.setString(7,object.PG2);
					pstm.setString(8,object.PG3);
					pstm.setString(9,object.PG4);
					pstm.setString(10,object.PV1);
					pstm.setString(11,object.PV2);
					pstm.setString(12,object.PV3);
					pstm.setString(13,object.PE1);
					pstm.setString(14,object.PE2);
					pstm.setString(15,object.PE3);
					pstm.setString(16,object.PE4);
					pstm.setString(17,object.PAAV);
					pstm.setString(18,object.LEO1);
					pstm.setString(19,object.LEO2);
					pstm.setString(20,object.LEO3);
					pstm.setString(21,object.LEO4);
					pstm.setString(22,object.LEO5);
					pstm.setString(23,object.CIT1);
					pstm.setString(24,object.CIT2);
					pstm.setString(25,object.PARA);
					pstm.setString(26,object.HI1);
					pstm.setString(27,object.HI2);
					pstm.setString(28,object.HI3);
					pstm.setString(29,object.HI4);
					pstm.setString(30,object.HI5);
					pstm.setString(31,object.HI6);
					pstm.setString(32,object.Total);
					pstm.setString(33,object.fechaExamen);
					
					pstm.executeUpdate();
					
					con.commit();
				}else {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
					columns.put("noRegistrado",isNullOrEmpty(paa))
					columns.put("noExiste",isNullOrEmpty(ds))
					columns.put("noEstaEnCarga",isNullOrEmpty(sda))
					
					estatus.add(columns)
				}
				
				resultado.setSuccess(true)
				resultado.setData(estatus)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			con.rollback();
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	} 
	
	

	public Boolean isNullOrEmpty(String text) {
		
		if(text?.equals("null") || text?.equals("") || text?.equals(" ") || text?.length() < 1) {
			return true
		}
		return false
	}
	
	//todos los aspirantes que tengan el status de carga y consulta de resultados

	public Result getAspirantesSinPAA(Integer parameterP, Integer parameterC, String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", bachillerato="", campus="", programa="", ingreso="", estado ="", tipoalumno ="", orderby="ORDER BY ", errorlog=""
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		List<DetalleSolicitud> lstDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			def objCatCampusDAO = context.apiClient.getDAO(CatCampusDAO.class);
			
			List<CatCampus> lstCatCampus = objCatCampusDAO.find(0, 9999)
			
			userLogged = context.getApiSession().getUserId();
			
			List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
			for(UserMembership objUserMembership : lstUserMembership) {
				for(CatCampus rowGrupo : lstCatCampus) {
					if(objUserMembership.getGroupName().equals(rowGrupo.getGrupoBonita())) {
						lstGrupo.add(rowGrupo.getDescripcion());
						break;
					}
				}
			}
			
			assert object instanceof Map;
			where+=" WHERE sda.iseliminado=false and (sda.isAspiranteMigrado is null  or sda.isAspiranteMigrado = false ) "
			if(object.campus != null){
				where+=" AND LOWER(campus.grupoBonita) = LOWER('"+object.campus+"') "
			}			

			where+=" AND (sda.ESTATUSSOLICITUD = 'Carga y consulta de resultados')"				
				
			
			if(lstGrupo.size()>0) {
				campus+=" AND ("
			}
			for(Integer i=0; i<lstGrupo.size(); i++) {
				String campusMiembro=lstGrupo.get(i);
				campus+="campus.descripcion='"+campusMiembro+"'"
				if(i==(lstGrupo.size()-1)) {
					campus+=") "	
				}
				else {
					campus+=" OR "
				}
			}
			
			errorlog+="campus" + campus;
				errorlog+="object.lstFiltro" +object.lstFiltro
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexion();
				
				String SSA = "";
				pstm = con.prepareStatement(Statements.CONFIGURACIONESSSA)
				rs= pstm.executeQuery();
				if(rs.next()) {
					SSA = rs.getString("valor")
				}
				
				String consulta = Statements.GET_ASPIRANTES_EN_PROCESO
				
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
                    errorlog=consulta+" 1";
					switch(filtro.get("columna")) {
					
					case "NOMBRE,EMAIL,CURP":
						errorlog+="NOMBRE,EMAIL,CURP"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "PROGRAMA,PERÍODO DE INGRESO,CAMPUS INGRESO":
						errorlog+="PROGRAMA, PERÍODO DE INGRESO, CAMPUS INGRESO"
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
						
					case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
						errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						/*where +=" ( LOWER(estado.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						*/
						where +="( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +="  OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ULTIMA MODIFICACION":
						errorlog+="FECHAULTIMAMODIFICACION"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" (LOWER(fechaultimamodificacion) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where +=" OR to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') ";
						where+="LIKE LOWER('%[valor]%'))";

						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
				//filtrado utilizado en lista roja y rechazado
					case "NOMBRE,EMAIL,CURP":
						errorlog+="NOMBRE,EMAIL,CURP"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(concat(sda.apellidopaterno,' ',sda.apellidomaterno,' ',sda.primernombre,' ',sda.segundonombre)) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.correoelectronico) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(sda.curp) like lower('%[valor]%') ) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "CAMPUS,PROGRAMA,INGRESO":
						errorlog+="PROGRAMA,INGRESO,CAMPUS"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(campusEstudio.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(gestionescolar.NOMBRE) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(periodo.DESCRIPCION) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						
						break;
						
					case "PROCEDENCIA,PREPARATORIA,PROMEDIO":
						errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +="( LOWER(CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(prepa.DESCRIPCION) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						/*
						where +=" OR LOWER(sda.estadoextranjero) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						*/
						where +=" OR LOWER(sda.PROMEDIOGENERAL) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ESTATUS,TIPO":
						errorlog+="PREPARATORIA,ESTADO,PROMEDIO"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" ( LOWER(sda.ESTATUSSOLICITUD) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(R.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "INDICADORES":
						errorlog+="INDICADORES"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						
						where +=" ( LOWER(R.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(TA.descripcion) like lower('%[valor]%') ";
						where = where.replace("[valor]", filtro.get("valor"))
						
						where +=" OR LOWER(TAL.descripcion) like lower('%[valor]%') )";
						where = where.replace("[valor]", filtro.get("valor"))
						
						break;
						
					// filtrados normales	
					case "NÚMERO DE SOLICITUD":
                        errorlog+="SOLICITUD"
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(CAST(sda.caseid AS varchar)) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
                        where = where.replace("[valor]", filtro.get("valor"))
                        break;
						
					case "IDBANNER":
						errorlog+="IDBANNER"
						tipoalumno +=" AND LOWER(da.idbanner) ";
						if(filtro.get("operador").equals("Igual a")) {
							tipoalumno+="=LOWER('[valor]')"
						}else {
							tipoalumno+="LIKE LOWER('%[valor]%')"
						}
						tipoalumno = tipoalumno.replace("[valor]", filtro.get("valor"))
						break;
						
					case "ID BANNER":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(da.idbanner) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					
					} 
					
				}
				errorlog=consulta+" 2";
				switch(object.orderby) {
					case "RESIDEICA":
					orderby+="residensia";
					break;
					case "TIPODEADMISION":
					orderby+="tipoadmision";
					break;
					case "TIPODEALUMNO":
					orderby+="tipoDeAlumno";
					break;
					case "FECHAULTIMAMODIFICACION":
					orderby+="sda.fechaultimamodificacion";
					break;
					case "NOMBRE":
					orderby+="sda.apellidopaterno";
					break;
					case "EMAIL":
					orderby+="sda.correoelectronico";
					break;
					case "CURP":
					orderby+="sda.curp";
					break;
					case "CAMPUS":
					orderby+="campus.DESCRIPCION"
					break;
					case "PREPARATORIA":
					orderby+="prepa.DESCRIPCION"
					break;
					case "PROGRAMA":
					orderby+="gestionescolar.NOMBRE"
					break;
					case "INGRESO":
					orderby+="periodo.DESCRIPCION"
					break;
					case "PROCEDENCIA":
					orderby +="CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END";
					break;
					case "PROMEDIO":
					orderby+="sda.PROMEDIOGENERAL";
					break;
					case "ESTATUS":
					orderby+="sda.ESTATUSSOLICITUD";
					break;
					case "TIPO":
					orderby+="da.TIPOALUMNO";
					break;
					case "TELEFONO":
					orderby+="sda.telefonocelular";
					break;
					case "IDBANNER":
					orderby+="da.idbanner";
					break;
					default:
					orderby+="sda.persistenceid"
					break;
				}
				orderby+=" "+object.orientation;
				consulta=consulta.replace("[CAMPUS]", campus)
				consulta=consulta.replace("[PROGRAMA]", programa)
				consulta=consulta.replace("[INGRESO]", ingreso)
				consulta=consulta.replace("[ESTADO]", estado)
				consulta=consulta.replace("[BACHILLERATO]", bachillerato)
				consulta=consulta.replace("[TIPOALUMNO]", tipoalumno)
				where+=" "+campus +" "+programa +" " + ingreso + " " + estado +" "+bachillerato +" "+tipoalumno
				
				consulta=consulta.replace("[WHERE]", where);
				errorlog=consulta+" 5";
				pstm = con.prepareStatement(consulta.replace("CASE WHEN prepa.descripcion = 'Otro' THEN sda.estadobachillerato ELSE prepa.estado END AS procedencia, to_char(CURRENT_TIMESTAMP - TO_TIMESTAMP(sda.fechaultimamodificacion, 'YYYY-MM-DDTHH:MI'), 'DD \"días\" HH24 \"horas\" MI \"minutos\"') AS tiempoultimamodificacion, sda.fechasolicitudenviada, sda.fechaultimamodificacion, sda.urlfoto, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusEstudio.descripcion AS campus, campus.descripcion AS campussede, gestionescolar.NOMBRE AS licenciatura, periodo.DESCRIPCION AS ingreso, CASE WHEN estado.DESCRIPCION ISNULL THEN sda.estadoextranjero ELSE estado.DESCRIPCION END AS estado, CASE WHEN prepa.DESCRIPCION = 'Otro' THEN sda.bachillerato ELSE prepa.DESCRIPCION END AS preparatoria, sda.PROMEDIOGENERAL, sda.ESTATUSSOLICITUD, da.TIPOALUMNO, sda.caseid, sda.telefonocelular, da.observacionesListaRoja, da.observacionesRechazo, da.idbanner, campus.grupoBonita, TA.descripcion as tipoadmision , R.descripcion as residensia, TAL.descripcion as tipoDeAlumno, catcampus.descripcion as transferencia, campusEstudio.clave as claveCampus, gestionescolar.clave as claveLicenciatura", "COUNT(sda.persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "").replace("GROUP BY prepa.descripcion,sda.estadobachillerato, prepa.estado, sda.fechaultimamodificacion, sda.fechasolicitudenviada, sda.apellidopaterno, sda.apellidomaterno, sda.primernombre, sda.segundonombre, sda.correoelectronico, sda.curp, campusestudio.descripcion,campus.descripcion, gestionescolar.nombre, periodo.descripcion, estado.descripcion, sda.estadoextranjero,sda.bachillerato,sda.promediogeneral,sda.estatussolicitud,da.tipoalumno,sda.caseid,sda.telefonocelular,da.observacioneslistaroja,da.observacionesrechazo,da.idbanner,campus.grupobonita,ta.descripcion,r.descripcion,tal.descripcion,catcampus.descripcion,campusestudio.clave,gestionescolar.clave, sda.persistenceid",""))
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				rs = pstm.executeQuery()
				rows = new ArrayList<Map<String, Object>>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();
				errorlog=consulta+" 8";
				while(rs.next()) {
					Map<String, Object> columns = new LinkedHashMap<String, Object>();
	
					for (int i = 1; i <= columnCount; i++) {
						columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
						if(metaData.getColumnLabel(i).toLowerCase().equals("caseid")) {
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
								
								for(Document doc : context.getApiClient().getProcessAPI().getDocumentList(Long.parseLong(rs.getString(i)), "fotoPasaporte", 0, 10)) {
									encoded = "../API/formsDocumentImage?document=" + doc.getId();
									columns.put("fotografiabpm", encoded);
								}
								
							}catch(Exception e) {
								columns.put("fotografiab64", "");
								errorlog+= ""+e.getMessage();
							}
						}
					}
	
					rows.add(columns);
				}
				errorlog=consulta+" 9";
				resultado.setSuccess(true)
				
				resultado.setError_info(errorlog);
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setError_info(errorlog)	
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getAspirantePAA(String idBanner, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String  errorlog="";
		try {
			
			closeCon = validarConexion();
			
			
			pstm = con.prepareStatement(Statements.GET_PAA_BY_IDBANNER);
			pstm.setString(1, idBanner)
			
			rs= pstm.executeQuery();
			
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
			
			while(rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i).toLowerCase(), rs.getString(i));
				}
				info.add(columns)
			}
			
			resultado.setSuccess(true);
			resultado.setData(info);
			resultado.setError_info(errorlog);
		} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog);
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}

	
}
