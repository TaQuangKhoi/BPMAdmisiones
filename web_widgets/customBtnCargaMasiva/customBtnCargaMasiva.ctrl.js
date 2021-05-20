function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService,blockUI) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      //$scope.properties.value = $scope.properties.texto;
      blockUI.start();
      if(!isNullOrUndefined($scope.properties.value) ){
          
          $scope.properties.value.forEach(datos =>{
            let paso = validacion(datos);
            if(paso){
                doRequest("POST",$scope.properties.urlPost,datos)
            }
          })
          
          setTimeout(function(){ 
              $scope.properties.tabla = "tabla"; 
              $scope.properties.value = []; 
              swal('¡Se han terminado la carga masiva!',"","success")
          }, 1000);
      }
      
      blockUI.stop();
      
    };
    
    function validacion(dato){
        let error = "";
        if( isNullOrUndefined(dato['IDBANNER']) || dato['IDBANNER'].length < 8){
            error+=(error.length>0?",":"")+"falta el dato id banner"
            
        }
        if(isNullOrUndefined(dato['Total']) ){
            error+=(error.length>0?",":"")+"falta el dato Puntuación Total"
            
        }
        if(isNullOrUndefined(dato['Fecha de examen']) ){
            error+=(error.length>0?",":"")+"falta el dato de la fecha de examen"
            
        }
        if(isNullOrUndefined(dato['PAAN']) ){
            error+=(error.length>0?",":"")+"falta el dato PAAN"
            
        }
        
        if(isNullOrUndefined(dato['PAAV']) ){
           error+=(error.length>0?",":"")+"falta el dato PAAV"
             
        }
        
        if(isNullOrUndefined(dato['PARA']) ){
           error+=(error.length>0?",":"")+"falta el dato PARA"
             
        }
        
        if(isNullOrUndefined(dato['LA1']) ){
           error+=(error.length>0?",":"")+"falta el dato LA1"
             
        }
        
        if(isNullOrUndefined(dato['LA2']) ){
           error+=(error.length>0?",":"")+"falta el dato LA2"
             
        }
        
        if(isNullOrUndefined(dato['LA3']) ){
           error+=(error.length>0?",":"")+"falta el dato LA3"
             
        }
        
        if(isNullOrUndefined(dato['PG1']) ){
           error+=(error.length>0?",":"")+"falta el dato PG1"
             
        }
        
        if(isNullOrUndefined(dato['PG2']) ){
           error+=(error.length>0?",":"")+"falta el dato PG2"
             
        }
        
        if(isNullOrUndefined(dato['PG3']) ){
           error+=(error.length>0?",":"")+"falta el dato PG3"
             
        }
        
        if(isNullOrUndefined(dato['PG4']) ){
           error+=(error.length>0?",":"")+"falta el dato PG4"
             
        }
        
        if(isNullOrUndefined(dato['PV1']) ){
           error+=(error.length>0?",":"")+"falta el dato PV1"
             
        }
        
        if(isNullOrUndefined(dato['PV2']) ){
           error+=(error.length>0?",":"")+"falta el dato PV2"
             
        }
        
        if(isNullOrUndefined(dato['PV3']) ){
           error+=(error.length>0?",":"")+"falta el dato PV3"
             
        }
        
        if(isNullOrUndefined(dato['PE1']) ){
           error+=(error.length>0?",":"")+"falta el dato PE1"
             
        }
        
        if(isNullOrUndefined(dato['PE2']) ){
           error+=(error.length>0?",":"")+"falta el dato PE2"
             
        }
        
        if(isNullOrUndefined(dato['PE3']) ){
           error+=(error.length>0?",":"")+"falta el dato PE3"
             
        }
        
        if(isNullOrUndefined(dato['PE4']) ){
           error+=(error.length>0?",":"")+"falta el dato PE4"
             
        }
        
        if(isNullOrUndefined(dato['LEO1']) ){
           error+=(error.length>0?",":"")+"falta el dato LEO1"
             
        }
        
        if(isNullOrUndefined(dato['LEO2']) ){
           error+=(error.length>0?",":"")+"falta el dato LEO2"
             
        }
        
        if(isNullOrUndefined(dato['LEO3']) ){
           error+=(error.length>0?",":"")+"falta el dato LEO3"
             
        }
        
        if(isNullOrUndefined(dato['LEO4']) ){
           error+=(error.length>0?",":"")+"falta el dato LEO4"
             
        }
        
        if(isNullOrUndefined(dato['LEO5']) ){
           error+=(error.length>0?",":"")+"falta el dato LEO5"
             
        }
        
        if(isNullOrUndefined(dato['CIT1']) ){
           error+=(error.length>0?",":"")+"falta el dato CIT1"
             
        }
        
        if(isNullOrUndefined(dato['CIT2']) ){
           error+=(error.length>0?",":"")+"falta el dato CIT2"
             
        }
        
        if(isNullOrUndefined(dato['HI1']) ){
           error+=(error.length>0?",":"")+"falta el dato HI1"
             
        }
        if(isNullOrUndefined(dato['HI2']) ){
           error+=(error.length>0?",":"")+"falta el dato HI2"
             
        }
        if(isNullOrUndefined(dato['HI3']) ){
           error+=(error.length>0?",":"")+"falta el dato HI3"
             
        }
        if(isNullOrUndefined(dato['HI4']) ){
           error+=(error.length>0?",":"")+"falta el dato HI4"
             
        }
        if(isNullOrUndefined(dato['HI5']) ){
           error+=(error.length>0?",":"")+"falta el dato HI5"
             
        }
        if(isNullOrUndefined(dato['HI6']) ){
           error+=(error.length>0?",":"")+"falta el dato HI6"
             
        }
        if(error.length > 0){
          $scope.properties.lstErrores.push({idBanner:dato['IDBANNER'],nombre:dato['Nombre'],Error:error})
          return false;
        }
        return true;
    }
    
    function isNullOrUndefined(dato){
        if(dato === undefined || dato === null || dato.length <= 0 ){
            return true;
        }
        return false
    }
    
    
    function doRequest(method, url,datos) {
        var req = {
            method: method,
            url: url,
            data: angular.copy(datos)
        };
        return $http(req)
            .success(function (data, status) {
                if(!data.data[0].noRegistrado){
                    $scope.properties.lstErrores.push({idBanner:datos['IDBANNER'],nombre:datos['Nombre'],Error:"el aspirante ya tiene puntuacion"})
                }
                else if(data.data[0].noEstaEnCarga){
                    $scope.properties.lstErrores.push({idBanner:datos['IDBANNER'],nombre:datos['Nombre'],Error:"el aspirante no se encuantra en carga y consulta de resultados"})
                }
                else if(data.data[0].noExiste){
                    $scope.properties.lstErrores.push({idBanner:datos['IDBANNER'],nombre:datos['Nombre'],Error:"no hay aspirante con ese idBanner"})
                }
            })
            .error(function (data, status) {
                
            })
            .finally(function () {
                
                
            });
    }
    
    

}
