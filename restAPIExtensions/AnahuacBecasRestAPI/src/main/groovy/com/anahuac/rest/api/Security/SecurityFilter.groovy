package com.anahuac.rest.api.Security


import org.bonitasoft.engine.identity.UserMembership
import org.bonitasoft.engine.identity.UserMembershipCriterion

import org.bonitasoft.web.extension.rest.RestAPIContext

class SecurityFilter {
	public Boolean bonitaRolFilter(RestAPIContext context,String roleName) {
		Boolean valid = false;
		List<UserMembership> uMemberships=context.apiClient.identityAPI.getUserMemberships(context.apiSession.userId, 0, 100, UserMembershipCriterion.ROLE_NAME_ASC);
		uMemberships.each{
			it ->
			if(it.roleName.toLowerCase().equals(roleName.toLowerCase()) || it.roleName.equals("ADMINISTRADOR") || it.roleName.equals("TI SERUA") || it.roleName.equals("SERUA") || it.roleName.equals("TI CAMPUS") || it.roleName.equals("PSICOLOGO") || it.roleName.equals("PSICOLOGO SUPERVISOR") || it.roleName.equals("PASE DE LISTA")) {
				valid=true
			}
		}
		
		return valid;
	} 
	
	public Boolean allowedUrl(RestAPIContext context, String url) {
		
			Boolean allow= false;
			switch(url){
				case "getCatalogosGenericos":
					allow=bonitaRolFilter(context,"BECAS")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Artistica")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Deportiva")
					if(allow){break;}
				break;
				case "getUserProcessApoyoEducativo":
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Artistica")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Deportiva")
					if(allow){break;}
				break;
				case "getCatTipoAoyoByCampus":
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Artistica")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Deportiva")
					if(allow){break;}
				break;
				case "getB64FileByUrlAzure":
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Artistica")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Deportiva")
					if(allow){break;}
				break;
				case "getSolicitudDeAdmision":
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Artistica")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Deportiva")
					if(allow){break;}
				break;
				case "getConfiguracionCampus":
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Artistica")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Deportiva")
					if(allow){break;}
				break;
				case "getPRomedioMinimoByCampus":
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Artistica")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Deportiva")
					if(allow){break;}
				break;
				case "getDocumentosByTipoApoyo":
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Artistica")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Deportiva")
					if(allow){break;}
				break;
				case "getDocumentosByCaseId":
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Artistica")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Deportiva")
					if(allow){break;}
				break;
				case "getImagenesByCaseId":
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Artistica")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Deportiva")
					if(allow){break;}
				break;
				case "getCatTienesHijos":
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Artistica")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Deportiva")
					if(allow){break;}
				break;
				case "getPromedioMinimoByCampus":
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Artistica")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Deportiva")
					if(allow){break;}
				break;
				case "getSolicitudByCaseid":
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Artistica")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Deportiva")
					if(allow){break;}
				break;
				case "getInfrmacionEscolar":
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Artistica")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Area Deportiva")
					if(allow){break;}
				break;
				case "getConfiguracionPagoEstudioSocEcoAspirante":
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
				break;
				case "getSDAEGestionEscolar":
					allow=bonitaRolFilter(context,"TI SERUA")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ADMINISTRADOR")
					if(allow){break;}
					allow=bonitaRolFilter(context,"PreAutorizacion")
					if(allow){break;}
					allow=bonitaRolFilter(context,"Becas")
					if(allow){break;}
					allow=bonitaRolFilter(context,"ASPIRANTE")
				break;
				
				
			}
			return allow;
	}
	public Boolean allowedUrlPost(RestAPIContext context, String url) {
		Boolean allow = false;
	
		switch (url) {
			case "getExcelBachilleratos":
				allow=bonitaRolFilter(context,"TI SERUA")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ADMINISTRADOR")
				if(allow){break;}
			break;
			case "simpleSelect":
				allow=bonitaRolFilter(context,"TI SERUA")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ADMINISTRADOR")
				if(allow){break;}
			break;
			case "insertBitacoraSDAE":
				allow=bonitaRolFilter(context,"TI SERUA")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ADMINISTRADOR")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Area Artistica")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Area Deportiva")
				if(allow){break;}
			break;
			case "getCatTipoAoyo":
				allow=bonitaRolFilter(context,"TI SERUA")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ADMINISTRADOR")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Area Artistica")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Area Deportiva")
				if(allow){break;}
			break;
			case "getCampusByTipoApoyo":
				allow=bonitaRolFilter(context,"TI SERUA")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ADMINISTRADOR")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
			break;
			case "insertUpdateCatTipoApoyo":
				allow=bonitaRolFilter(context,"TI SERUA")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ADMINISTRADOR")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
			break;
			case "switchCampusTipoApoyo":
				allow=bonitaRolFilter(context,"TI SERUA")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ADMINISTRADOR")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
			break;
			case "updateTipoApoyoVideo":
				allow=bonitaRolFilter(context,"TI SERUA")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ADMINISTRADOR")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
			break;
			case "insertManejoDocumento":
				allow=bonitaRolFilter(context,"TI SERUA")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ADMINISTRADOR")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
			break;
			case "getImagenesByTipoApoyo":
				allow=bonitaRolFilter(context,"TI SERUA")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ADMINISTRADOR")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ASPIRANTE")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Area Artistica")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Area Deportiva")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Becas")
				if(allow){break;}
			break;
			case "selectSolicitudesApoyo":
				allow=bonitaRolFilter(context,"TI SERUA")
				if(allow){break;}
				allow=bonitaRolFilter(context,"ADMINISTRADOR")
				if(allow){break;}
				allow=bonitaRolFilter(context,"PreAutorizacion")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Becas")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Area Artistica")
				if(allow){break;}
				allow=bonitaRolFilter(context,"Area Deportiva")
				if(allow){break;}
			break;
			
		}
		return allow;
	}
}
