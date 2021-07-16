package com.anahuac.rest.api.Entity.Custom

import javax.xml.bind.annotation.XmlElement

class CatGenericoFiltro {
	private Long persistenceId;

	private Long persistenceVersion;
	
	private String clave;
	
	private String descripcion;
	
	private String fechaCreacion;
	
	private String usuarioCreacion;
	
	private Boolean isEliminado;
	
	private String orden;
	
	private String fechaImportacion;
	
	private Boolean isAdmitido;
	
	
	public Boolean getIsAdmitido() {
		return isAdmitido;
	}
	public void setIsAdmitido(Boolean isAdmitido) {
		this.isAdmitido = isAdmitido;
	}
	
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
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
	public String getFechaImportacion() {
		return fechaImportacion;
	}
	public void setFechaImportacion(String fechaImportacion) {
		this.fechaImportacion = fechaImportacion;
	}
	
	
	
	
}
