package com.anahuac.rest.api.Entity.Custom

class MenuParent extends Menu{
	public static String GET="SELECT DISTINCT * FROM(SELECT bam.id,bam.parentid,case when parent.displayname is null then true else false end isparent, '../apps/'||ba.token||'/'||bap.token AS url, ba.token parenttoken, bam.displayname menu, bap.token, ba.displayname, parent.displayname parent FROM business_app ba LEFT JOIN public.business_app_menu bam ON bam.applicationid=ba.id LEFT JOIN public.business_app_page bap ON bap.id=bam.applicationpageid LEFT JOIN public.business_app_menu parent on bam.parentid=parent.id INNER JOIN app_menu_role apr on apr.business_app_menuid=bam.id  WHERE ba.displayname='Administrativo' and (apr.roleid in (SELECT um.roleid from user_membership um where userid=?)) ORDER BY parent.displayname desc,bam.index_ ) datos order by parent desc;"
	private List<Menu> child;

	public List<Menu> getChild() {
		return child;
	}

	public void setChild(List<Menu> child) {
		this.child = child;
	}
	@Override
	public boolean equals(Object arg0) {
		Boolean part1=arg0 != null;
		Boolean part3=((MenuParent) arg0).getId().equals(this.parentid) || ((MenuParent) arg0).getParentid().equals(this.id);
		return (part1 && part3);
	}
}
