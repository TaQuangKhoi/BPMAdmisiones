package com.anahuac.rest.api.Entity

class CatCodigoPostalCustomFiltro {
	private Long persistenceId;
	private String persistenceId_string;
	private Long persistenceVersion;
	private String persistenceVersion_string;
	private String codigoPostal;
	private String asentamiento;
	private String tipoAsentamiento;
	private String claveEstado;
	private String estado;
	private String claveMunicipio;
	private String municipio;
	private String clavePais;
	private String fechaCreacion;
	private String usuarioCreacion;
	private Boolean isEliminado;
	private Boolean isAnahuac;
	private Long orden;
	private String ciudad;
	
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public String getPersistenceId_string() {
		return persistenceId_string;
	}
	public void setPersistenceId_string(String persistenceId_string) {
		this.persistenceId_string = persistenceId_string;
	}
	public Long getPersistenceVersion() {
		return persistenceVersion;
	}
	public void setPersistenceVersion(Long persistenceVersion) {
		this.persistenceVersion = persistenceVersion;
	}
	public String getPersistenceVersion_string() {
		return persistenceVersion_string;
	}
	public void setPersistenceVersion_string(String persistenceVersion_string) {
		this.persistenceVersion_string = persistenceVersion_string;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(String asentamiento) {
		this.asentamiento = asentamiento;
	}
	public String getTipoAsentamiento() {
		return tipoAsentamiento;
	}
	public void setTipoAsentamiento(String tipoAsentamiento) {
		this.tipoAsentamiento = tipoAsentamiento;
	}
	public String getClaveEstado() {
		return claveEstado;
	}
	public void setClaveEstado(String claveEstado) {
		this.claveEstado = claveEstado;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getClaveMunicipio() {
		return claveMunicipio;
	}
	public void setClaveMunicipio(String claveMunicipio) {
		this.claveMunicipio = claveMunicipio;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getClavePais() {
		return clavePais;
	}
	public void setClavePais(String clavePais) {
		this.clavePais = clavePais;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
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
	public Boolean getIsAnahuac() {
		return isAnahuac;
	}
	public void setIsAnahuac(Boolean isAnahuac) {
		this.isAnahuac = isAnahuac;
	}
	public Long getOrden() {
		return orden;
	}
	public void setOrden(Long orden) {
		this.orden = orden;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	
	@Override
	public String toString() {
		return "CatCodigoPostalCustomFiltro [persistenceId=" + persistenceId + ", persistenceId_string="
				+ persistenceId_string + ", persistenceVersion=" + persistenceVersion + ", persistenceVersion_string="
				+ persistenceVersion_string + ", codigoPostal=" + codigoPostal + ", asentamiento=" + asentamiento
				+ ", tipoAsentamiento=" + tipoAsentamiento + ", claveEstado=" + claveEstado + ", estado=" + estado
				+ ", claveMunicipio=" + claveMunicipio + ", municipio=" + municipio + ", clavePais=" + clavePais
				+ ", fechaCreacion=" + fechaCreacion + ", usuarioCreacion=" + usuarioCreacion + ", isEliminado="
				+ isEliminado + ", isAnahuac=" + isAnahuac + ", orden=" + orden + ", ciudad=" + ciudad + "]";
	}
	
	
}
