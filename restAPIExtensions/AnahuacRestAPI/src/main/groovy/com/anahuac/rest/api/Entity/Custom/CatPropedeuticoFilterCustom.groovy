package com.anahuac.rest.api.Entity.Custom

class CatPropedeuticoFilterCustom {
	private String clave;
	private String descripcion;
	private String usuarioCreacion;
	private Long persistenceIdCatRecibirAtencionEspiritual;
	private Long persistenceId;
	private String fechaInicio;
	private String fechaFinal;
	private String campus;
	private String tipoPeriodo;
	
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
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public Long getPersistenceIdCatRecibirAtencionEspiritual() {
		return persistenceIdCatRecibirAtencionEspiritual;
	}
	public void setPersistenceIdCatRecibirAtencionEspiritual(Long persistenceIdCatRecibirAtencionEspiritual) {
		this.persistenceIdCatRecibirAtencionEspiritual = persistenceIdCatRecibirAtencionEspiritual;
	}
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public String getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public String getFechaFinal() {
		return fechaFinal;
	}
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getTipoPeriodo() {
		return tipoPeriodo;
	}
	public void setTipoPeriodo(String tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}
	
	@Override
	public String toString() {
		return "CatPropedeuticoFilterCustom [clave=" + clave + ", descripcion=" + descripcion + ", usuarioCreacion="
				+ usuarioCreacion + ", persistenceIdCatRecibirAtencionEspiritual="
				+ persistenceIdCatRecibirAtencionEspiritual + ", persistenceId=" + persistenceId + ", fechaInicio="
				+ fechaInicio + ", fechaFinal=" + fechaFinal + ", campus=" + campus + ", tipoPeriodo=" + tipoPeriodo
				+ "]";
	}
	
	
	
}
