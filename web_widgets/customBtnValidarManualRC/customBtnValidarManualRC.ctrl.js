function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService,blockUI) {

  'use strict';

  var vm = this;
  $scope.validaciones = {};

    $scope.myFunc = function() {
      //$scope.properties.value = $scope.properties.texto;
      let paso = validacion();
      if(paso){
          //$scope.properties.value.fechaExamen = formatDate($scope.properties.value.fechaExamen)
          let info = [];
          info.push($scope.properties.value);
          info[0].usuariocreacion = angular.copy($scope.properties.usuario);
          let info2 = {"IDBANNER":info[0].IDBANNER}
          doRequestValidar("POST",$scope.properties.urlValidar,info2).then(function() {
              if($scope.validaciones.cantidadIntentos <=2){
                doRequest("POST",$scope.properties.urlPost,info)
              }else{
                swal(`¡Este aspirante a sobrepasado la cantidad de intentos permitidos!`,"","warning")
              }
            });
      }
    };
    
    function formatDate(date) {
        //return date.slice(8,10)+"-"+date.slice(5,7)+"-"+date.slice(0,4);
        let current_datetime = date;
        let formatted_date = appendLeadingZeroes(current_datetime.getDate())+ "-" + appendLeadingZeroes(current_datetime.getMonth() + 1) + "-" +  current_datetime.getFullYear() + " "
    }
    
    function appendLeadingZeroes(n){
        if(n <= 9){
            return "0" + n;
        }
        return n
    }

        
        
        
    
    function validacion(){

        if( isNullOrUndefined($scope.properties.value.IDBANNER) || $scope.properties.value.IDBANNER.length < 8){
            swal(`¡debe agregar el id banner!`,"","warning")
            return false;
        }
        if( isNullOrUndefined($scope.properties.value.nombre) ){
            swal(`¡debe ingresar un idbanner valido!`,"","warning")
            return false;
        }
        if(isNullOrUndefined($scope.properties.value.decision) ){
            swal(`¡debe ingresar el valor de la decisión de admisión!`,"","warning")
            return false;
        }
        if($scope.properties.value.isAdmitido){
            
            if(isNullOrUndefined($scope.properties.value.PDP_1) ){
                swal(`¡debe ingresar el valor del PDP!`,"","warning")
                return false;
            }
            if(isNullOrUndefined($scope.properties.value.PDU_1)){
                swal(`¡debe ingresar el valor del PDU!`,"","warning")
                return false;
            }
            if(isNullOrUndefined($scope.properties.value.SSE_1)){
               swal('¡debe ingresar el valor del SSE!',"","warning")
                return false; 
            }
            if(isNullOrUndefined($scope.properties.value.PCDA_1)){
               swal('¡debe ingresar el valor del PCDA!',"","warning")
                return false; 
            }
            if(isNullOrUndefined($scope.properties.value.PCA_1)){
                swal('¡debe ingresar el valor del PCA!',"","warning")
                 return false; 
            }
        }else{
            return true;
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
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy(datos)
        };
        return $http(req)
            .success(function (data, status) {

                swal('¡Se han guardado los datos correctamente!',"","success")
                $scope.properties.tabla = "tabla";
                
            })
            .error(function (data, status) {
                
            })
            .finally(function () {
                
                blockUI.stop();
            });
    }
    
    
    function doRequestValidar(method, url,datos) {
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy(datos)
        };
        return $http(req)
            .success(function (data, status) {
                $scope.validaciones = data.data[0];
            })
            .error(function (data, status) {
                
            })
            .finally(function () {
                
                blockUI.stop();
            });
    }
    

}
