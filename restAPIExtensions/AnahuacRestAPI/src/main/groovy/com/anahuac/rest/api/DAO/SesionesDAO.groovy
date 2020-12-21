package com.anahuac.rest.api.DAO

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.anahuac.rest.api.Entity.db.CatPsicologo
import com.anahuac.rest.api.Entity.db.CatTipoPrueba
import com.bonitasoft.web.extension.rest.RestAPIContext

import groovy.json.JsonSlurper
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion

class SesionesDAO {
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
	}
	public Boolean validarConexionBonita() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnectionBonita();
			retorno=true
		}
	}
	public Result getCatTipoPrueba(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", orderby="ORDER BY ", errorlog=""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatTipoPrueba row = new CatTipoPrueba()
			List<CatTipoPrueba> rows = new ArrayList<CatTipoPrueba>();
			closeCon = validarConexion();
			
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
					case "DESCRIPCION":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(DESCRIPCION) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
				}
			}
			orderby+=object.orderby
			if(orderby.equals("ORDER BY ")) {
				orderby+="PERSISTENCEID";
			}
			orderby+=" "+object.orientation;
			String consulta = Statements.GET_CATTIPOPRUEBA;
			consulta=consulta.replace("[WHERE]", where);
			errorlog+="consulta:"
			errorlog+=consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "")
				pstm = con.prepareStatement(consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				rs = pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog+="consulta:"
				errorlog+=consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				rs = pstm.executeQuery()
				while(rs.next()) {
					row = new CatTipoPrueba()
					
					row.setDescripcion(rs.getString("descripcion"))
					row.setPersistenceId(rs.getLong("persistenceId"))
					row.setPersistenceVersion(rs.getLong("persistenceVersion"))
					rows.add(row)
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	public Result getCatPsicologo(String jsonData) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where ="", orderby="ORDER BY ", errorlog=""
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);
			CatPsicologo row = new CatPsicologo()
			List<CatPsicologo> rows = new ArrayList<CatPsicologo>();
			closeCon = validarConexion();
			
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
					case "NOMBRE":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(DESCRIPCION) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
					break;
				}
			}
			orderby+=object.orderby
			if(orderby.equals("ORDER BY ")) {
				orderby+="PERSISTENCEID";
			}
			orderby+=" "+object.orientation;
			String consulta = Statements.GET_CATPSICOLOGO;
			consulta=consulta.replace("[WHERE]", where);
			errorlog+="consulta:"
			errorlog+=consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", "")
				pstm = con.prepareStatement(consulta.replace("*", "COUNT(PERSISTENCEID) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				rs = pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog+="consulta:"
				errorlog+=consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				rs = pstm.executeQuery()
				while(rs.next()) {
					row = new CatTipoPrueba()
					
					row.setNombre(rs.getString("nombre"))
					row.setPersistenceId(rs.getLong("persistenceId"))
					row.setPersistenceVersion(rs.getLong("persistenceVersion"))
					rows.add(row)
				}
				resultado.setSuccess(true)
				
				resultado.setData(rows)
				
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	public Result getUserBonita(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		
		List<String> lstGrupo = new ArrayList<String>();
		List<Map<String, String>> lstGrupoCampus = new ArrayList<Map<String, String>>();
		
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		Map<String, String> objGrupoCampus = new HashMap<String, String>();
		String where ="", orderby="ORDER BY ", errorlog="", role="", group="";
		try {
				def jsonSlurper = new JsonSlurper();
				def object = jsonSlurper.parseText(jsonData);
				
				//assert object instanceof List;
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Cancún");
				objGrupoCampus.put("valor","CAMPUS-CANCUN");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Mayab");
				objGrupoCampus.put("valor","CAMPUS-MAYAB");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac México Norte");
				objGrupoCampus.put("valor","CAMPUS-MNORTE");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac México Sur");
				objGrupoCampus.put("valor","CAMPUS-MSUR");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Oaxaca");
				objGrupoCampus.put("valor","CAMPUS-OAXACA");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Puebla");
				objGrupoCampus.put("valor","CAMPUS-PUEBLA");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Querétaro");
				objGrupoCampus.put("valor","CAMPUS-QUERETARO");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Xalapa");
				objGrupoCampus.put("valor","CAMPUS-XALAPA");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Querétaro");
				objGrupoCampus.put("valor","CAMPUS-QUERETARO");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Juan Pablo II");
				objGrupoCampus.put("valor","CAMPUS-JP2");
				lstGrupoCampus.add(objGrupoCampus);
				
				objGrupoCampus = new HashMap<String, String>();
				objGrupoCampus.put("descripcion","Anáhuac Cordoba");
				objGrupoCampus.put("valor","CAMPUS-CORDOBA");
				lstGrupoCampus.add(objGrupoCampus);
						
				userLogged = context.getApiSession().getUserId();
				
				List<UserMembership> lstUserMembership = context.getApiClient().getIdentityAPI().getUserMemberships(userLogged, 0, 99999, UserMembershipCriterion.GROUP_NAME_ASC)
				for(UserMembership objUserMembership : lstUserMembership) {
					for(Map<String, String> rowGrupo : lstGrupoCampus) {
						if(objUserMembership.getGroupName().equals(rowGrupo.get("valor"))) {
							lstGrupo.add(rowGrupo.get("valor"));
							break;
						}
					}
				}
				group+=" AND ("
				for(Integer i=0; i<lstGrupo.size(); i++) {
					String campusMiembro=lstGrupo.get(i);
					group+="g.name='"+campusMiembro+"'"
					if(i==(lstGrupo.size()-1)) {
						group+=") "
					}
					else {
						group+=" OR "
					}
				}
				
				for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
					switch(filtro.get("columna")) {
						case "NOMBRE":
						if(where.contains("WHERE")) {
							where+= " AND "
						}else {
							where+= " WHERE "
						}
						where +=" LOWER(firstname) ";
						if(filtro.get("operador").equals("Igual a")) {
							where+="=LOWER('[valor]')"
						}else {
							where+="LIKE LOWER('%[valor]%')"
						}
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						case "APELLIDO":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(lastname) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 break;
						case "GRUPO":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(g.name) ";
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
								  group+="AND LOWER(g.name)=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
								  group+="AND LOWER(g.name) LIKE LOWER('[valor]')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 group = group.replace("[valor]", filtro.get("valor"))
							 break;
						case "ROL":
						if(where.contains("WHERE")) {
								  where+= " AND "
							 }else {
								  where+= " WHERE "
							 }
							 where +=" LOWER(r.name) ";
							 role +="AND LOWER(r.name) "
							 if(filtro.get("operador").equals("Igual a")) {
								  where+="=LOWER('[valor]')"
								  role+="=LOWER('[valor]')"
							 }else {
								  where+="LIKE LOWER('%[valor]%')"
								  role+="LIKE LOWER('%[valor]%')"
							 }
							 where = where.replace("[valor]", filtro.get("valor"))
							 role = role.replace("[valor]", filtro.get("valor"))
							 break;
					}
				}
				switch(object.orderby) {
					case "NOMBRE":
					orderby+="firstname";
					break;
					case "APELLIDO":
					orderby+="lastname";
					break;
					case "GRUPO":
					orderby+="g.name";
					break;
					case "ROL":
					orderby+="r.name";
					break;
					default:
					orderby+="u.id"
					break;
				}
				orderby+=" "+object.orientation;
				String consulta = Statements.GET_USER_BONITA;
				consulta=consulta.replace("[ROLE]", role);
				consulta=consulta.replace("[GROUP]", group);
				consulta=consulta.replace("[WHERE]", where);
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
				closeCon = validarConexionBonita();
				pstm = con.prepareStatement(consulta.replace("u.id, u.firstname, u.lastname, g.name as grupo, r.name as rol", "COUNT(u.id) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
				rs= pstm.executeQuery()
				if(rs.next()) {
					resultado.setTotalRegistros(rs.getInt("registros"))
				}
				consulta=consulta.replace("[ORDERBY]", orderby)
				consulta=consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
				errorlog+="consulta:"
				errorlog+=consulta
				pstm = con.prepareStatement(consulta)
				pstm.setInt(1, object.limit)
				pstm.setInt(2, object.offset)
				
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
				resultado.setSuccess(true)
				
				resultado.setData(rows)
			
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorlog)
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
}
