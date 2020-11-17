package com.anahuac.rest.api.Entity.Custom

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement
class CatBachilleratosCustom {
	@XmlElement
	private Long persistenceId;
	@XmlElement
	private Long persistenceVersion;
	@XmlElement
	private String clave;
	@XmlElement
	private String descripcion;
	@XmlElement
	private String pais;
	@XmlElement
	private String estado;
	@XmlElement
	private String ciudad;
	@XmlElement
	private Boolean convenio;
	@XmlElement
	private String tipoconvenio;
	@XmlElement
	private String descuento;
	@XmlElement
	private String documento;
	@XmlElement
	private Boolean isEliminado;
	
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
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public Boolean getConvenio() {
		return convenio;
	}
	public void setConvenio(Boolean convenio) {
		this.convenio = convenio;
	}
	public String getTipoconvenio() {
		return tipoconvenio;
	}
	public void setTipoconvenio(String tipoconvenio) {
		this.tipoconvenio = tipoconvenio;
	}
	public String getDescuento() {
		return descuento;
	}
	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public Boolean getIsEliminado() {
		return isEliminado;
	}
	public void setIsEliminado(Boolean isEliminado) {
		this.isEliminado = isEliminado;
	}
	
	@Override
	public String toString() {
		return "CatBachilleratosCustom [persistenceId=" + persistenceId + ", persistenceVersion=" + persistenceVersion
				+ ", clave=" + clave + ", descripcion=" + descripcion + ", pais=" + pais + ", estado=" + estado
				+ ", ciudad=" + ciudad + ", convenio=" + convenio + ", tipoconvenio=" + tipoconvenio + ", descuento="
				+ descuento + ", documento=" + documento + ", isEliminado=" + isEliminado + "]";
	}
	
}
