function ($scope, $http) {
    
    var jsonVacioSolicitudApoyoEducativo = 
    {
    	"solicitudApoyoEducativoInput":{
    		"caseId":"",
    		"isEliminado":[],
    		"pageIndex":"",
    		"costoMensualColegiatura":"",
    		"catTienesHijos":null,
    		"calificacionOficialPreparatoria":"",
    		"nombreTutor":"",
    		"telefonoCasaTutor":"",
    		"telefonoOficinaTutor":"",
    		"telefonoCelularTutor":"",
    		"parentescoTutor":"",
    		"correoElectronicoTutor":"",
    		"viveMismoDocimicilioAlumnoTutor":"",
    		"maximoNivelEstudiosTutor":"",
    		"ocupacionTutor":"",
    		"parentescoTutor":"",
    		"correoElectronicoTutor":"",
    		"viveMismoDocimicilioAlumnoTutor":"",
    		"maximoNivelEstudiosTutor":"",
    		"ocupacionTutor":"",
    		"catProvienenIngresos":null,
    		"ingresoMensualNetoTutor":"",
    		"empresaTutor":"",
    		"puestoTutor":"",
    		"bienesRaices":[],
    		"tieneHermanos":false,
    		"hermanos":[],
    		"motivoBeca":"",
    		"catTipoApoyo":null,
    		"catPorcentajeBeca":null,
    		"catPorcentajeFinanciamiento":null,
    		"cantMensualPagarUni":0,
    		"catCasaDondeVives":null,
    		"contruccionM2Casa":0,
    		"terrenoM2Casa":0,
    		"valorAproxCasa":0,
    		"calle":"",
    		"delegacionCiudad":"",
    		"numExterior":"",
    		"numInterior":"",
    		"pais":"",
    		"colonia":"",
    		"estado":"",
    		"codigoPostal":"",
    		"autos":[],
    		"ingresoPadre":0,
    		"ingresoMadre":0,
    		"ingresoHermano":0,
    		"ingresoTio":0,
    		"ingresoAbuelo":0,
    		"ingresoAspirante":0,
    		"ingresoTotal":0,
    		"egresoRenta":0,
    		"egresoServicios":0,
    		"egresoEducacion":0,
    		"egresoGastosMedicos":0,
    		"egresoAlimentacion":0,
    		"egresoVestido":0,
    		"egresoSeguro":0,
    		"egresoDiversion":0,
    		"egresoAhorro":0,
    		"egresoCreditos":0,
    		"egresoOtros":0,
    		"egresoTotal":0,
    		"urlVideoYouTube":"",
    		"isSolDeportivaAprobada":false,
    		"motivoRechazoDeportiva":"",
    		"motivoAprobadaDeportiva":"",
    		"isSolArtisticaAprobada":false,
    		"motivoRechazoArtistica":"",
    		"motivoAprobadaArtistica":""
    	},
    	"isFinalizadaInput":false
    };
    
    function getCurrentTaskId(){
        $http.get($scope.properties.url).success((data)=>{
            if(data.length){
                $scope.properties.taskId = data[0].id;
                $scope.properties.caseId = data[0].caseId;
                getCurrentContext();
            }
        }).error((err)=>{
            swal("Error","Error al obtener las tareas asignadas al usuario. " + err,"error");
        });
    }
    
    function getCurrentContext(){
        $http.get($scope.properties.urlContext).success((data)=>{
            if(data.length){
               getModelSolicitudApoyoEducativo(data.solicitudApoyoEducativo_ref.link);
            }else{
                $scope.properties.solicitudApoyoEducativo = [];
                $scope.properties.solicitudApoyoEducativo = jsonVacioSolicitudApoyoEducativo;
            }
        }).error((err)=>{
            swal("Error","Error al obtener el context. " + err,"error");
        });
    }
    
    function getModelSolicitudApoyoEducativo(url){
        $http.get(url).success((data)=>{
            if(data.length){
                $scope.properties.solicitudApoyoEducativo = [];
                $scope.properties.solicitudApoyoEducativo = data;
            }else{
                $scope.properties.solicitudApoyoEducativo = [];
                $scope.properties.solicitudApoyoEducativo = jsonVacioSolicitudApoyoEducativo;
            }
        }).error((err)=>{
            swal("Error","Error al obtener el model. " + err,"error");
        });
    }
    
    $scope.$watch("properties.reloadTask", ()=>{
        if($scope.properties.reloadTask){
            getCurrentTaskId();
        } 
    });
    
     $scope.$watch("properties.url", ()=>{
            getCurrentTaskId();
    });
}