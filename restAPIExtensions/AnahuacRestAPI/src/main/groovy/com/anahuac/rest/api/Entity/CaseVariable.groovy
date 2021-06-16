package com.anahuac.rest.api.Entity

class CaseVariable {
	private String case_id;
	private String name;
	private String description;
	private String type;
	private String value;
	
	public String getCase_id() {
		return case_id;
	}
	public void setCase_id(String case_id) {
		this.case_id = case_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "CaseVariable [case_id=" + case_id + ", name=" + name + ", description=" + description + ", type=" + type
				+ ", value=" + value + "]";
	}
	
	
}
