package com.anahuac.rest.api.Entity.db

class CatTypoApoyo {
	private Long persistenceId; 
	private Long persistenceVersion; 
	private String clave; 
	private String descripcion; 
	private String fechaCreacion; 
	private String usuarioCreacion; 
	private Boolean isEliminado;
	private Boolean requiereVideo;
	private String condicionesVideo;
	private boolean esSocioEconomico;
	
	public boolean isEsSocioEconomico() {
		return esSocioEconomico;
	}
	public void setEsSocioEconomico(boolean esSocioEconomico) {
		this.esSocioEconomico = esSocioEconomico;
	}
	public Boolean getRequiereVideo() {
		return requiereVideo;
	}
	public void setRequiereVideo(Boolean requiereVideo) {
		this.requiereVideo = requiereVideo;
	}
	public String getCondicionesVideo() {
		return condicionesVideo;
	}
	public void setCondicionesVideo(String condicionesVideo) {
		this.condicionesVideo = condicionesVideo;
	}
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
	
}
