package com.anahuac.rest.api.Entity.db

class CatManejoDocumentos {
	private Long persistenceId;
	private Long idCampus;
	private Long idTipoApoyo;
	private Boolean isObligatorio;
	private String nombreDocumento;
	private String urlDocumentoAzure;
	private Boolean isEliminado;
	private String fechaCreacion;
	private String usuarioCreacion;
	private String descripcionDocumento;
	
	public String getDescripcionDocumento() {
		return descripcionDocumento;
	}
	public void setDescripcionDocumento(String descripcionDocumento) {
		this.descripcionDocumento = descripcionDocumento;
	}
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public Long getIdCampus() {
		return idCampus;
	}
	public void setIdCampus(Long idCampus) {
		this.idCampus = idCampus;
	}
	public Long getIdTipoApoyo() {
		return idTipoApoyo;
	}
	public void setIdTipoApoyo(Long idTipoApoyo) {
		this.idTipoApoyo = idTipoApoyo;
	}
	public Boolean getIsObligatorio() {
		return isObligatorio;
	}
	public void setIsObligatorio(Boolean isObligatorio) {
		this.isObligatorio = isObligatorio;
	}
	public String getNombreDocumento() {
		return nombreDocumento;
	}
	public void setNombreDocumento(String nombreDocumento) {
		this.nombreDocumento = nombreDocumento;
	}
	public String getUrlDocumentoAzure() {
		return urlDocumentoAzure;
	}
	public void setUrlDocumentoAzure(String urlDocumentoAzure) {
		this.urlDocumentoAzure = urlDocumentoAzure;
	}
	public Boolean getIsEliminado() {
		return isEliminado;
	}
	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
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
	
	
}
