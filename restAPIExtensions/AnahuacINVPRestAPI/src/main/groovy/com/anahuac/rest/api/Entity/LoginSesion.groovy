package com.anahuac.rest.api.Entity

import javax.xml.bind.annotation.XmlElement

class LoginSesion {
	@XmlElement
	private Long persistenceId;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String username;
	@XmlElement
	private String aplicacion;
	@XmlElement
	private String entrada;
	public Long getPersistenceId() {
		return persistenceId;
	}
	public void setPersistenceId(Long persistenceId) {
		this.persistenceId = persistenceId;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}
	public String getEntrada() {
		return entrada;
	}
	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}

}
