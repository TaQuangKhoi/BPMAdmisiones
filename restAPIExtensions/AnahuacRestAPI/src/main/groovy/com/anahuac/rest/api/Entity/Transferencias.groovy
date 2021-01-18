package com.anahuac.rest.api.Entity

class Transferencias {
	
	private Long persistenceId;
	private Long persistenceVersion;
	private String aspirante; 
	private String correoAspirante;
	private String valorOriginal;
	private String valorCambio;
	private String fechaCreacion;
	private String usuarioCreacion;
	private String campusAnterior;
	private String campusNuevo;
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
	public String getAspirante() {
		return aspirante;
	}
	public void setAspirante(String aspirante) {
		this.aspirante = aspirante;
	}
	public String getCorreoAspirante() {
		return correoAspirante;
	}
	public void setCorreoAspirante(String correoAspirante) {
		this.correoAspirante = correoAspirante;
	}
	public String getValorOriginal() {
		return valorOriginal;
	}
	public void setValorOriginal(String valorOriginal) {
		this.valorOriginal = valorOriginal;
	}
	public String getValorCambio() {
		return valorCambio;
	}
	public void setValorCambio(String valorCambio) {
		this.valorCambio = valorCambio;
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
	public String getCampusAnterior() {
		return campusAnterior;
	}
	public void setCampusAnterior(String campusAnterior) {
		this.campusAnterior = campusAnterior;
	}
	public String getCampusNuevo() {
		return campusNuevo;
	}
	public void setCampusNuevo(String campusNuevo) {
		this.campusNuevo = campusNuevo;
	}
	
}
