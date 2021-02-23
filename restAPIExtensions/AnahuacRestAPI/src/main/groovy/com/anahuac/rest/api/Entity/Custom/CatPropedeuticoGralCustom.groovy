package com.anahuac.rest.api.Entity.Custom

class CatPropedeuticoGralCustom {
	
	private Long persistenceId;
	private String clave;
	private String descripcion;
	private String id;
	private String persistenceVersion;
	private String campus;
	
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPersistenceVersion() {
		return persistenceVersion;
	}
	public void setPersistenceVersion(String persistenceVersion) {
		this.persistenceVersion = persistenceVersion;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	
	@Override
	public String toString() {
		return "CatPropedeuticoGralCustom [persistenceId=" + persistenceId + ", clave=" + clave + ", descripcion="
				+ descripcion + ", id=" + id + ", persistenceVersion=" + persistenceVersion + ", campus=" + campus
				+ "]";
	}
	
	
}
