package com.anahuac.rest.api.DAO

import com.anahuac.catalogos.CatDocumentosTextos
import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.Entity.Result

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class DocumentosTextosDAO {
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	public static final String GET="SELECT * FROM CATDOCUMENTOSTEXTOS WHERE campus_pid=?"
	public static final String INSERT="INSERT INTO CATDOCUMENTOSTEXTOS  (NOSABES ,TIPSCB ,URLGUIAEXAMENCB , URLTESTVOCACIONAL ,persistenceid,persistenceversion, campus_pid) values (?,?,?,?,case when (SELECT max(persistenceId)+1 from CATDOCUMENTOSTEXTOS  ) is null then 1 else (SELECT max(persistenceId)+1 from CATDOCUMENTOSTEXTOS) end,0,?);"
	public static final String UPDATE="UPDATE CATDOCUMENTOSTEXTOS SET NOSABES=?, TIPSCB=?, URLGUIAEXAMENCB=?, URLTESTVOCACIONAL=? where campus_pid=?;"
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
		return retorno
	}
	public Result getDocumentosTextos(Long campus_pid) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {

			CatDocumentosTextos row = new CatDocumentosTextos()
			List<CatDocumentosTextos> rows = new ArrayList<CatDocumentosTextos>();
			closeCon = validarConexion();
			String consulta = GET
			pstm = con.prepareStatement(consulta)
			pstm.setLong(1, campus_pid)
			rs = pstm.executeQuery()
				while(rs.next()) {
					row = new CatDocumentosTextos()
					row.setNoSabes(rs.getString("noSabes"))
					row.setTipsCB(rs.getString("tipsCB"))
					row.setUrlGuiaExamenCB(rs.getString("urlGuiaExamenCB"))
					row.setUrlTestVocacional(rs.getString("urlTestVocacional"))
					row.setCampus_pid(rs.getLong("campus_pid"))
					rows.add(row)
				}
				resultado.setSuccess(true)
				resultado.setData(rows)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	public Result insertDocumentosTextos(CatDocumentosTextos row) {
		Result resultado = new Result();
		Boolean closeCon = false;
		try {
			List<CatDocumentosTextos> rows = new ArrayList<CatDocumentosTextos>();
			closeCon = validarConexion();
			String consulta = GET
			pstm = con.prepareStatement(consulta)
			pstm.setLong(1, row.getCampus_pid())
			rs = pstm.executeQuery()
					pstm = con.prepareStatement(rs.next()?UPDATE:INSERT)
					pstm.setString(1, row.getNoSabes())
					pstm.setString(2, row.getTipsCB())
					pstm.setString(3, row.getUrlGuiaExamenCB())
					pstm.setString(4, row.getUrlTestVocacional())
					pstm.setLong(5, row.getCampus_pid())
					pstm.execute()
				resultado.setSuccess(true)
				resultado.setData(rows)
			} catch (Exception e) {
			resultado.setSuccess(false);
			resultado.setError(e.getMessage());
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return resultado
	}
	
}

