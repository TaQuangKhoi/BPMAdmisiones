package com.anahuac.rest.api.Entity.Custom

import com.anahuac.rest.api.Entity.db.BusinessAppMenu
import com.anahuac.rest.api.Entity.db.Role

class AppMenuRole extends BusinessAppMenu{
	public static String GET="SELECT b.tenantid, b.id, b.displayname, b.applicationid, b.applicationpageid, b.parentid, b.index_, r.id as roleid, r.name as rolename FROM business_app_menu b LEFT JOIN app_menu_role amr on amr.business_app_menuid=b.id LEFT JOIN business_app ba on b.applicationid=ba.id  LEFT JOIN role r on r.id=amr.roleid WHERE ba.displayname='Administrativo'   order by index_"
	public static String INSERT="INSERT INTO app_menu_role (business_app_menuid, roleid ) VALUES (?, ? )";
	public static String DELETE ="DELETE FROM app_menu_role WHERE business_app_menuid=? AND roleid=? "
	private List<Role> roles;

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
