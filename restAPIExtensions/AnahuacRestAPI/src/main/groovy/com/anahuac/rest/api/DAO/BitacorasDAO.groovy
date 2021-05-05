package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.RowId
import java.sql.Statement
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.anahuac.rest.api.Entity.Result
import com.bonitasoft.web.extension.rest.RestAPIContext
import com.anahuac.catalogos.CatCampus 
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements

import groovy.json.JsonSlurper

class BitacorasDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ListadoDAO.class);
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
	
	
	
	public Result getBitacorasComentarios(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		String where ="", orderby="ORDER BY ", role="", campus=" ", group="";
		List<String> lstGrupo = new ArrayList<String>();
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
			
			
			
			String consulta = Statements.GET_BITACORACOMENTARIOS
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
					
					
				case "CAMPUS":
					campus +=" AND LOWER(campus.DESCRIPCION) ";
					where +=" AND LOWER(campus.DESCRIPCION)  "
					if(filtro.get("operador").equals("Igual a")) {
						campus+="=LOWER('[valor]')"
						where +="=LOWER('[valor]')"
					}else {
						campus+="LIKE LOWER('%[valor]%')"
						where+="LIKE LOWER('%[valor]%')"
					}
					campus = campus.replace("[valor]", filtro.get("valor"))
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "Comentario":
					where +=" AND LOWER(cbc.comentario)  "
					if(filtro.get("operador").equals("Igual a")) {
						where +="=LOWER('[valor]')"
					}else {
						where+="LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "Fecha creación":
					where +=" AND LOWER(cbc.fechacreacion)  "
					if(filtro.get("operador").equals("Igual a")) {
						where +="=LOWER('[valor]')"
					}else {
						where+="LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "Modulo":
					where +=" AND LOWER(cbc.modulo)  "
					if(filtro.get("operador").equals("Igual a")) {
						where +="=LOWER('[valor]')"
					}else {
						where+="LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "Usuario":
					where +=" AND LOWER(cbc.usuario)  "
					if(filtro.get("operador").equals("Igual a")) {
						where +="=LOWER('[valor]')"
					}else {
						where+="LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "Aspirante":
					where +=" AND LOWER(cbc.usuariocomentario)  "
					if(filtro.get("operador").equals("Igual a")) {
						where +="=LOWER('[valor]')"
					}else {
						where+="LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				}
				
				
			}
			
			switch(object.orderby) {
				
				case "Comentario":
				orderby+="cbc.comentario";
				break;
				case "Fecha creación":
				orderby+="cbc.fechacreacion";
				break;
				case "Modulo":
				orderby+="cbc.modulo";
				break;
				case "Usuario":
				orderby+="cbc.usuario"
				break;
				case "Aspirante":
				orderby+="cbc.usuariocomentario"
				break;
				
				default:
				orderby+="cbc.fechacreacion"
				break;
				
			}
			
			closeCon = validarConexion();	
			String count = Statements.COUNT_BITACORACOMENTARIOS
			count=count.replace("[CAMPUS]", campus)
			count=count.replace("[WHERE]", where);
			pstm = con.prepareStatement(count)
			
			rs= pstm.executeQuery()
			if(rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			
			consulta=consulta.replace("[CAMPUS]", campus)
			consulta=consulta.replace("[WHERE]", where);
			consulta=consulta.replace("[ORDERBY]", orderby);
			consulta=consulta.replace("[LIMITOFFSET]", "LIMIT ? OFFSET ?");
			
			errorLog+= consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)
			
			rs = pstm.executeQuery();
			
			List<String> rows = new ArrayList<String>();
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
			
			resultado.setError_info(" errorLog = "+errorLog)
			resultado.setData(rows)
			resultado.setSuccess(true)
		}catch(Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog+" "+e.getMessage())
		}
		finally{
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
	public Result getComentariosValidacion(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String errorLog = "";
		Long userLogged = 0L;
		Long caseId = 0L;
		Long total = 0L;
		String where ="", orderby="ORDER BY ", role="", campus=" ", group="";
		List<String> lstGrupo = new ArrayList<String>();
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
			
			String variable = "",consulta = "";
			if(object.bitacora.equals("Bitácora de lista roja")) {
				consulta = Statements.GET_LISTAROJA
				variable = "ds.observacioneslistaroja"
			}else if(object.bitacora.equals("Bitácora de cambios")) {
				consulta = Statements.GET_LISTACAMBIO
				variable = "ds.observacionescambio"
			}else if(object.bitacora.equals("bitácora de rechazados")) {
				consulta = Statements.GET_LISTRECHAZO
				variable = "ds.observacionesrechazo"
			}
			
			for(Map<String, Object> filtro:(List<Map<String, Object>>) object.lstFiltro) {
				switch(filtro.get("columna")) {
					
					
				case "CAMPUS":
					campus +=" AND LOWER(campus.DESCRIPCION) ";
					where +=" AND LOWER(campus.DESCRIPCION)  "
					if(filtro.get("operador").equals("Igual a")) {
						campus+="=LOWER('[valor]')"
						where +="=LOWER('[valor]')"
					}else {
						campus+="LIKE LOWER('%[valor]%')"
						where+="LIKE LOWER('%[valor]%')"
					}
					campus = campus.replace("[valor]", filtro.get("valor"))
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "Comentario":					
					where +=" AND LOWER("+variable+") "
					if(filtro.get("operador").equals("Igual a")) {
						where +="=LOWER('[valor]')"
					}else {
						where+="LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				case "Aspirante":
					where +=" AND LOWER(sda.correoelectronico)  "
					if(filtro.get("operador").equals("Igual a")) {
						where +="=LOWER('[valor]')"
					}else {
						where+="LIKE LOWER('%[valor]%')"
					}
					where = where.replace("[valor]", filtro.get("valor"))
					break;
					
				}
				
				
			}
			
			switch(object.orderby) {
				
				case "Comentario":
				orderby+=variable;
				break;
				/*case "Modulo":
				orderby+="cbc.modulo";
				break;*/
				case "Aspirante":
				orderby+="sda.correoelectronico"
				break;
				
				default:
				orderby+="ds.caseId"
				break;
				
			}
			
			closeCon = validarConexion();
			consulta=consulta.replace("[CAMPUS]", campus)
			consulta=consulta.replace("[WHERE]", where);
			pstm = con.prepareStatement(consulta.replace("ds.observacionescambio,ds.observacioneslistaroja,ds.observacionesrechazo, sda.correoelectronico as aspirante, campus.descripcion","count("+variable+") as registros").replace("[ORDERBY]"," ").replace("[LIMITOFFSET]", " ") )
			
			rs= pstm.executeQuery()
			if(rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}
			
			
			consulta=consulta.replace("[ORDERBY]", orderby);
			consulta=consulta.replace("[LIMITOFFSET]", "LIMIT ? OFFSET ?");
			
			errorLog+= consulta
			pstm = con.prepareStatement(consulta)
			pstm.setInt(1, object.limit)
			pstm.setInt(2, object.offset)
			
			rs = pstm.executeQuery();
			
			List<String> rows = new ArrayList<String>();
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
			
			resultado.setError_info(" errorLog = "+errorLog)
			resultado.setData(rows)
			resultado.setSuccess(true)
		}catch(Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
			resultado.setError_info(errorLog+" "+e.getMessage())
		}
		finally{
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
	
}
