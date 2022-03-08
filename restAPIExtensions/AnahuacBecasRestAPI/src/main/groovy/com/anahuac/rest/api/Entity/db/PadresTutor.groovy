package com.anahuac.rest.api.Entity.db

class PadresTutor {
	
	private String nombre;
	private String apellidos;
	private String vive;
	private String sexo;
	private String parentesco;
	private Boolean mismoDomicilio;
	private String telefonoCasa;
	private String telefonoCelular;
	private String correoElectronico;
	private String maximoNivelEstudios;
	private String ocupacion;
	private String empresa;
	private String puesto;
	private String giroEmpresa
	private Boolean isTutor;
	
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getParentesco() {
		return parentesco;
	}
	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}
	public Boolean getMismoDomicilio() {
		return mismoDomicilio;
	}
	public void setMismoDomicilio(Boolean mismoDomicilio) {
		this.mismoDomicilio = mismoDomicilio;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getVive() {
		return vive;
	}
	public void setVive(String vive) {
		this.vive = vive;
	}
	public String getTelefonoCasa() {
		return telefonoCasa;
	}
	public void setTelefonoCasa(String telefonoCasa) {
		this.telefonoCasa = telefonoCasa;
	}
	public String getTelefonoCelular() {
		return telefonoCelular;
	}
	public void setTelefonoCelular(String telefonoCelular) {
		this.telefonoCelular = telefonoCelular;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public String getMaximoNivelEstudios() {
		return maximoNivelEstudios;
	}
	public void setMaximoNivelEstudios(String maximoNivelEstudios) {
		this.maximoNivelEstudios = maximoNivelEstudios;
	}
	public String getOcupacion() {
		return ocupacion;
	}
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getPuesto() {
		return puesto;
	}
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	public String getGiroEmpresa() {
		return giroEmpresa;
	}
	public void setGiroEmpresa(String giroEmpresa) {
		this.giroEmpresa = giroEmpresa;
	}
	public Boolean getIsTutor() {
		return isTutor;
	}
	public void setIsTutor(Boolean isTutor) {
		this.isTutor = isTutor;
	}
}
