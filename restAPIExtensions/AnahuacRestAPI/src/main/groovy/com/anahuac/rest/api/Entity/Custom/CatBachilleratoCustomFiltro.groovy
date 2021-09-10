package com.anahuac.rest.api.Entity.Custom

import com.anahuac.catalogos.CatCampus
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class CatBachilleratoCustomFiltro {
	@XmlElement
	private Long persistenceId;
	@XmlElement
	private Long persistenceVersion;
	@XmlElement
	private String clave;
	@XmlElement
	private String pais;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String fechaCreacion;
	@XmlElement
	private String fechaImportacion;
	@XmlElement
	private String usuarioCreacion;
	@XmlElement
	private Boolean isEliminado;
	@XmlElement
	private String caseId;
	@XmlElement
	private String estado;
	@XmlElement
	private String region;
	@XmlElement
	private String ciudad;
	@XmlElement
	private Boolean isEnabled;
	@XmlElement
	private Boolean perteneceRed;
	@XmlElement
	private String usuarioBanner;
	@XmlElement
	private String id;
	@XmlElement
	private String idDireccion;
	@XmlElement
	private String countyCode;
	@XmlElement
	private String nationCode;
	@XmlElement
	private String stateCode;
	@XmlElement
	private String streetLine1;
	@XmlElement
	private String streetLine2;
	@XmlElement
	private String streetLine3;
	@XmlElement
	private String typeInd;
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public Long getPersistenceVersion() {
		return persistenceVersion;
	}
	public void setPersistenceVersion(Long persistenceVersion) {
		this.persistenceVersion = persistenceVersion;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getFechaImportacion() {
		return fechaImportacion;
	}
	public void setFechaImportacion(String fechaImportacion) {
		this.fechaImportacion = fechaImportacion;
	}
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public Boolean getIsEliminado() {
		return isEliminado;
	}
	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public Boolean getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public Boolean getPerteneceRed() {
		return perteneceRed;
	}
	public void setPerteneceRed(Boolean perteneceRed) {
		this.perteneceRed = perteneceRed;
	}
	public String getUsuarioBanner() {
		return usuarioBanner;
	}
	public void setUsuarioBanner(String usuarioBanner) {
		this.usuarioBanner = usuarioBanner;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdDireccion() {
		return idDireccion;
	}
	public void setIdDireccion(String idDireccion) {
		this.idDireccion = idDireccion;
	}
	public String getCountyCode() {
		return countyCode;
	}
	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}
	public String getNationCode() {
		return nationCode;
	}
	public void setNationCode(String nationCode) {
		this.nationCode = nationCode;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getStreetLine1() {
		return streetLine1;
	}
	public void setStreetLine1(String streetLine1) {
		this.streetLine1 = streetLine1;
	}
	public String getStreetLine2() {
		return streetLine2;
	}
	public void setStreetLine2(String streetLine2) {
		this.streetLine2 = streetLine2;
	}
	public String getStreetLine3() {
		return streetLine3;
	}
	public void setStreetLine3(String streetLine3) {
		this.streetLine3 = streetLine3;
	}
	public String getTypeInd() {
		return typeInd;
	}
	public void setTypeInd(String typeInd) {
		this.typeInd = typeInd;
	}
	
	@Override
	public String toString() {
		return "CatBachilleratoCustomFiltro [persistenceId=" + persistenceId + ", persistenceVersion="
				+ persistenceVersion + ", clave=" + clave + ", pais=" + pais + ", descripcion=" + descripcion
				+ ", fechaCreacion=" + fechaCreacion + ", fechaImportacion=" + fechaImportacion + ", usuarioCreacion="
				+ usuarioCreacion + ", isEliminado=" + isEliminado + ", caseId=" + caseId + ", estado=" + estado
				+ ", region=" + region + ", ciudad=" + ciudad + ", isEnabled=" + isEnabled + ", perteneceRed="
				+ perteneceRed + ", usuarioBanner=" + usuarioBanner + ", id=" + id + ", idDireccion=" + idDireccion
				+ ", countyCode=" + countyCode + ", nationCode=" + nationCode + ", stateCode=" + stateCode
				+ ", streetLine1=" + streetLine1 + ", streetLine2=" + streetLine2 + ", streetLine3=" + streetLine3
				+ ", typeInd=" + typeInd + "]";
	}
	

	
	
}
