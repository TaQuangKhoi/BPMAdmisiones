package com.anahuac.rest.api.Entity.Custom

class CatPeriodoFinal {
	
	private Long persistenceId
	private String persistenceId_string;
	private Long persistenceVersion
	private String persistenceVersion_string;
	private String descripcion;
	private String fechaCreacion;
	private Boolean isEliminado;
	private String usuarioBanner;
	private String fechaImportacion;
	private String clave;
	private Boolean isEnabled;
	private String nombreCampus;
	private Boolean isCuatrimestral;
	private String fechaInicio;
	private String fechaFin;
	private Boolean isSemestral;
	private Boolean isAnual;
	private String isPropedeutico;
	private String id;
	private Boolean activo;
	
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
	public Boolean getIsEliminado() {
		return isEliminado;
	}
	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
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
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public Boolean getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getNombreCampus() {
		return nombreCampus;
	}
	public void setNombreCampus(String nombreCampus) {
		this.nombreCampus = nombreCampus;
	}
	public Boolean getIsCuatrimestral() {
		return isCuatrimestral;
	}
	public void setIsCuatrimestral(Boolean isCuatrimestral) {
		this.isCuatrimestral = isCuatrimestral;
	}
	public String getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	public Boolean getIsSemestral() {
		return isSemestral;
	}
	public void setIsSemestral(Boolean isSemestral) {
		this.isSemestral = isSemestral;
	}
	public Boolean getIsAnual() {
		return isAnual;
	}
	public void setIsAnual(Boolean isAnual) {
		this.isAnual = isAnual;
	}
	public String getIsPropedeutico() {
		return isPropedeutico;
	}
	public void setIsPropedeutico(String isPropedeutico) {
		this.isPropedeutico = isPropedeutico;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Boolean getActivo() {
		return activo;
	}
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	
	@Override
	public String toString() {
		return "CatPeriodoFinal [persistenceId=" + persistenceId + ", persistenceId_string=" + persistenceId_string
				+ ", persistenceVersion=" + persistenceVersion + ", persistenceVersion_string="
				+ persistenceVersion_string + ", descripcion=" + descripcion + ", fechaCreacion=" + fechaCreacion
				+ ", isEliminado=" + isEliminado + ", usuarioBanner=" + usuarioBanner + ", fechaImportacion="
				+ fechaImportacion + ", clave=" + clave + ", isEnabled=" + isEnabled + ", nombreCampus=" + nombreCampus
				+ ", isCuatrimestral=" + isCuatrimestral + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin
				+ ", isSemestral=" + isSemestral + ", isAnual=" + isAnual + ", isPropedeutico=" + isPropedeutico
				+ ", id=" + id + ", activo=" + activo + "]";
	}
	
}
