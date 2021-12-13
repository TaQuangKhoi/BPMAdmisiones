package com.anahuac.rest.api.Entity

class EntitySesionAspirante {
	private String username;
	private String responsable;
	private String horario;
	private Integer sesiones_pid;
	private Integer responsabledisponible_pid;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	public String getHorario() {
		return horario;
	}
	public void setHorario(String horario) {
		this.horario = horario;
	}
	public Integer getSesiones_pid() {
		return sesiones_pid;
	}
	public void setSesiones_pid(Integer sesiones_pid) {
		this.sesiones_pid = sesiones_pid;
	}
	public Integer getResponsabledisponible_pid() {
		return responsabledisponible_pid;
	}
	public void setResponsabledisponible_pid(Integer responsabledisponible_pid) {
		this.responsabledisponible_pid = responsabledisponible_pid;
	}
	
	@Override
	public String toString() {
		return "EntitySesionAspirante [username=" + username + ", responsable=" + responsable + ", horario=" + horario
				+ ", sesiones_pid=" + sesiones_pid + ", responsabledisponible_pid=" + responsabledisponible_pid + "]";
	}
		
}