function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService,blockUI) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      //$scope.properties.value = $scope.properties.texto;
      let paso = validacion();
      if(paso){
          //$scope.properties.value.fechaExamen = formatDate($scope.properties.value.fechaExamen)
          let info = []
          info.push($scope.properties.value)
          doRequest("POST",$scope.properties.urlPost,info)
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
        if($scope.properties.value.decision == "Aceptado"){
            
            if(isNullOrUndefined($scope.properties.value.pdp_1) ){
                swal(`¡debe ingresar el valor del PDP!`,"","warning")
                return false;
            }
            if(isNullOrUndefined($scope.properties.value.pdu_1)){
                swal(`¡debe ingresar el valor del PDU!`,"","warning")
                return false;
            }
            if(isNullOrUndefined($scope.properties.value.sse_1)){
               swal('¡debe ingresar el valor del SSE!',"","warning")
                return false; 
            }
            if(isNullOrUndefined($scope.properties.value.pcda_1)){
               swal('¡debe ingresar el valor del PCDA!',"","warning")
                return false; 
            }
            if(isNullOrUndefined($scope.properties.value.pca_1)){
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
    
    

}
