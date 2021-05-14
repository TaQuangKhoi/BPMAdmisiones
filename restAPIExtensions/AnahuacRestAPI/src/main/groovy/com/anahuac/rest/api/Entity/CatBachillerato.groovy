package com.anahuac.rest.api.Entity

class CatBachillerato {
	private String descripcion;
	private String usuarioBanner;
	private String fechaImportacion;
	private String fechaCreacion;
	private String pais;
	private String estado;
	private String ciudad;
	private Boolean isEliminado;
	private Boolean isEnabled;
	private Boolean perteneceRed;
	private String clave;
	private String idDireccion;
	private String idBachillerato;
	private String operation;
	private String streetLine1;
	private String streetLine2;
	private String streetLine3;
	private String nationCode;
	private String stateCode;
	private String countyCode;
	
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

	public String getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUsuarioBanner() {
		return usuarioBanner;
	}

	public void setUsuarioBanner(String usuarioBanner) {
		this.usuarioBanner = usuarioBanner;
	}

	public String getFechaImportacion() {
		return fechaImportacion;
	}

	public void setFechaImportacion(String fechaImportacion) {
		this.fechaImportacion = fechaImportacion;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public Boolean getIsEliminado() {
		return isEliminado;
	}

	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
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

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getIdDireccion() {
		return idDireccion;
	}

	public void setIdDireccion(String idDireccion) {
		this.idDireccion = idDireccion;
	}

	public String getIdBachillerato() {
		return idBachillerato;
	}

	public void setIdBachillerato(String idBachillerato) {
		this.idBachillerato = idBachillerato;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public String toString() {
		return "CatBachillerato [descripcion=" + descripcion + ", usuarioBanner=" + usuarioBanner
				+ ", fechaImportacion=" + fechaImportacion + ", fechaCreacion=" + fechaCreacion + ", pais=" + pais
				+ ", estado=" + estado + ", ciudad=" + ciudad + ", isEliminado=" + isEliminado + ", isEnabled="
				+ isEnabled + ", perteneceRed=" + perteneceRed + ", clave=" + clave + ", idDireccion=" + idDireccion
				+ ", idBachillerato=" + idBachillerato + ", operation=" + operation + ", streetLine1=" + streetLine1
				+ ", streetLine2=" + streetLine2 + ", streetLine3=" + streetLine3 + ", nationCode=" + nationCode
				+ ", stateCode=" + stateCode + ", countyCode=" + countyCode + "]";
	}

	@Override
	public boolean equals(Object obj) {
		Boolean part1=obj != null;
		Boolean part2=obj instanceof CatBachillerato;
		Boolean part3 = ((CatBachillerato) obj).getIdDireccion().equals(this.getIdDireccion());
		return part1 && part2 && part3;
	}
}
