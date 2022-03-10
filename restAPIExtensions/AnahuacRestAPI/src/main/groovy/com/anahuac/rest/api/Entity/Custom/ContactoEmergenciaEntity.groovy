package com.anahuac.rest.api.Entity.Custom

class ContactoEmergenciaEntity {
	private Long persistenceId;
	private Long persistenceVersion;
	private String nombre;
	private String parentesco;
	private String telefono;
	private String telefonocelular;
	private String casoemergencia;
	private String otroparentesco;
	private Long caseid;
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
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getParentesco() {
		return parentesco;
	}
	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getTelefonocelular() {
		return telefonocelular;
	}
	public void setTelefonocelular(String telefonocelular) {
		this.telefonocelular = telefonocelular;
	}
	public String getCasoemergencia() {
		return casoemergencia;
	}
	public void setCasoemergencia(String casoemergencia) {
		this.casoemergencia = casoemergencia;
	}
	public String getOtroparentesco() {
		return otroparentesco;
	}
	public void setOtroparentesco(String otroparentesco) {
		this.otroparentesco = otroparentesco;
	}
	public Long getCaseid() {
		return caseid;
	}
	public void setCaseid(Long caseid) {
		this.caseid = caseid;
	}
	
	
	
	
}
