package com.anahuac.rest.api.Entity.custom

class AspiranteSesionCustom {
	private Long idBpm;
	private String idBanner;
	private String nombre;
	private String uni;
	private String correoElectronico;
	private String telefono;
	private String celular;
	private Integer preguntas;
	private Integer contestadas;
	private String inicio;
	private String termino; 
	private String tiempo;
	private String estatus;
	private Boolean bloqueado;
	private String idioma;
	private Boolean terminado;
	
	public Boolean getTerminado() {
		return terminado;
	}
	public void setTerminado(Boolean terminado) {
		this.terminado = terminado;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public Long getIdBpm() {
		return idBpm;
	}
	public void setIdBpm(Long idBpm) {
		this.idBpm = idBpm;
	}
	public String getIdBanner() {
		return idBanner;
	}
	public void setIdBanner(String idBanner) {
		this.idBanner = idBanner;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUni() {
		return uni;
	}
	public void setUni(String uni) {
		this.uni = uni;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public Integer getPreguntas() {
		return preguntas;
	}
	public void setPreguntas(Integer preguntas) {
		this.preguntas = preguntas;
	}
	public Integer getContestadas() {
		return contestadas;
	}
	public void setContestadas(Integer contestadas) {
		this.contestadas = contestadas;
	}
	public String getInicio() {
		return inicio;
	}
	public void setInicio(String inicio) {
		this.inicio = inicio;
	}
	public String getTermino() {
		return termino;
	}
	public void setTermino(String termino) {
		this.termino = termino;
	}
	public String getTiempo() {
		return tiempo;
	}
	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public Boolean getBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	public String getIdioma() {
		return idioma;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
}
