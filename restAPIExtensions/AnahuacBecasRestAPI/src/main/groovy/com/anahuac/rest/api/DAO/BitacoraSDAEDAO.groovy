package com.anahuac.rest.api.DAO

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData

import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.SearchOptionsBuilder
import org.bonitasoft.engine.search.SearchResult
import org.bonitasoft.web.extension.rest.RestAPIContext

import com.anahuac.catalogos.CatCampus
import com.anahuac.catalogos.CatCampusDAO
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result

import groovy.json.JsonSlurper

class BitacoraSDAEDAO {
	Connection con;
	Statements stm;
	ResultSet rs;
	PreparedStatement pstm;

	public Boolean validarConexion() {
		Boolean retorno = false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno = true
		}
		return retorno
	}

	public Result cargarBitacora(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		ProcessInstance processInstance = null;
		ProcessAPI processAPI = null;
		Map < String, Serializable > contracto = new HashMap < String, Serializable > ();
		Map < String, Serializable > objBitacoraSDAEInput = new HashMap < String, Serializable > ();
		List < Map < String, Serializable >> lstBitacoraSDAEInput = new ArrayList < Map < String, Serializable >> ();

		String info = "";
		try {
			def jsonSlurper = new JsonSlurper();
			def object = jsonSlurper.parseText(jsonData);

			SearchOptionsBuilder searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			List < ProcessDeploymentInfo > lstProcessDeploymentInfo = new ArrayList < ProcessDeploymentInfo > ();
			SearchOptions searchOptionsProccess = null;
			Long processId = null;

			searchBuilderProccess = new SearchOptionsBuilder(0, 99999);
			searchBuilderProccess.filter(ProcessDeploymentInfoSearchDescriptor.NAME, "Agregar BitacoraSDAE");
			searchOptionsProccess = searchBuilderProccess.done();
			SearchResult < ProcessDeploymentInfo > SearchProcessDeploymentInfo = context.getApiClient().getProcessAPI().searchProcessDeploymentInfos(searchOptionsProccess);
			lstProcessDeploymentInfo = SearchProcessDeploymentInfo.getResult();
			for (ProcessDeploymentInfo objProcessDeploymentInfo: lstProcessDeploymentInfo) {
				if (objProcessDeploymentInfo.getActivationState().toString().equals("ENABLED")) {
					processId = objProcessDeploymentInfo.getProcessId();
					info+="entro";
				}
			}


			objBitacoraSDAEInput.put("idBanner", object?.idBanner)
			objBitacoraSDAEInput.put("estatus", object?.estatus);
			objBitacoraSDAEInput.put("correo", object?.correo)
			objBitacoraSDAEInput.put("usuario", context.getApiSession().getUserName())
			objBitacoraSDAEInput.put("comentario", object?.comentario)
			objBitacoraSDAEInput.put("beca", object?.beca)
			objBitacoraSDAEInput.put("financiamiento", object?.financiamiento);
			lstBitacoraSDAEInput.add(objBitacoraSDAEInput);
			contracto.put("lstBitacoraSDAEInput", lstBitacoraSDAEInput);

			processAPI = context.getApiClient().getProcessAPI();

			processInstance = processAPI.startProcessWithInputs(processId, contracto);
			resultado.setSuccess(true);
		}catch(Exception e) {
			resultado.setError(e.getMessage())
			resultado.setSuccess(false);
			resultado.setError_info(info)
		}

		return resultado;
	}

	public Result getBitacoraSDAE(String jsonData, RestAPIContext context) {
		Result resultado = new Result();
		Boolean closeCon = false;
		String where = "",campus = "", orderby = "ORDER BY ", errorlog = "";
		List < String > lstGrupo = new ArrayList < String > ();

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
			if (lstGrupo.size() > 0) {
				where += " AND ("
				for (Integer i = 0; i < lstGrupo.size(); i++) {
					String campusMiembro = lstGrupo.get(i);
					where += "campus.descripcion='" + campusMiembro + "'"
					if (i == (lstGrupo.size() - 1)) {
						where += ") "
					} else {
						where += " OR "
					}
				}
			}

			List < Map < String, Object >> rows = new ArrayList < Map < String, Object >> ();
			closeCon = validarConexion();

			String consulta = Statements.GET_BITACORA_SDAE
			
			for (Map < String, Object > filtro: (List < Map < String, Object >> ) object.lstFiltro) {
				errorlog = consulta + " 1";
				switch (filtro.get("columna")) {

					case "ID BANNER":
						where += " AND (LOWER(IDBANNER) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;

					case "CORREO":
						where += " AND (LOWER(CORREO) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "ESTATUS":
						where += " AND (LOWER(ESTATUS) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						
					case "COMENTARIO":
						where += " AND (LOWER(COMENTARIO) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "BECA":
						where += " AND (LOWER(BECA) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "FINANCIAMIENTO":
						where += " AND (LOWER(FINANCIAMIENTO) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "USUARIO":
						where += " AND (LOWER(USUARIO) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
					case "FECHA":
						where += " AND (LOWER(FECHA) LIKE LOWER('%[valor]%')) ";
						where = where.replace("[valor]", filtro.get("valor"))
						break;
						

					default:
						break;
				}

			}
			errorlog = consulta + " 2";
			switch (object.orderby) {
				case "Id Banner":
					orderby += "IDBANNER";
					break;
				case "Correo":
					orderby += "CORREO";
					break;
				case "Estatus":
					orderby += "ESTATUS";
					break;
				case "Comentario":
					orderby += "Comentario";
					break;
				case "Beca":
					orderby += "Beca"
					break;
				case "Financiamiento":
					orderby += "Financiamiento"
					break;
				case "Usuario":
					orderby += "Usuario"
					break;
				case "Fecha":
					orderby += "Fecha"
					break;
				default:
					orderby += "IDBANNER"
					break;
			}
			orderby += " " + object.orientation;
			consulta = consulta.replace("[WHERE]", where);

			pstm = con.prepareStatement(consulta.replace("idBanner,correo,estatus,comentario,beca,financiamiento,usuaria,fecha", "COUNT(persistenceid) as registros").replace("[LIMITOFFSET]","").replace("[ORDERBY]", ""))
			//errorlog = consultaCount + " 6";
			rs = pstm.executeQuery()
			if (rs.next()) {
				resultado.setTotalRegistros(rs.getInt("registros"))
			}

			consulta = consulta.replace("[ORDERBY]", orderby)
			consulta = consulta.replace("[LIMITOFFSET]", " LIMIT ? OFFSET ?")
			errorlog = consulta + " 7";

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
				}
				rows.add(columns);
			}

			errorlog = consulta + " 9";
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

}
