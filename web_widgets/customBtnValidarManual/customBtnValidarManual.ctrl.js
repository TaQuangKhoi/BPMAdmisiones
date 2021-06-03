function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService,blockUI) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      //$scope.properties.value = $scope.properties.texto;
      let paso = validacion();
      if(paso){
          $scope.properties.value.fechaExamen = formatDate($scope.properties.value.fechaExamen)
          doRequest("POST",$scope.properties.urlPost,$scope.properties.value)
      }
    };
    
    function formatDate(date) {
        return date.slice(8,10)+"-"+date.slice(5,7)+"-"+date.slice(0,4);
    }
        
    
    function validacion(){
        if(isNullOrUndefined($scope.properties.value.tipoExamen)){
           swal('¡debe ingresar el tipo de examen!',"","warning")
            return false; 
        }
        if( isNullOrUndefined($scope.properties.value.IDBANNER) || $scope.properties.value.IDBANNER.length < 8){
            swal(`¡debe agregar el id banner!`,"","warning")
            return false;
        }
        if( isNullOrUndefined($scope.properties.value.Nombre) ){
            swal(`¡debe ingresar un idbanner valido!`,"","warning")
            return false;
        }
        if(isNullOrUndefined($scope.properties.value.Total) ){
            swal(`¡debe ingresar el valor de Puntuación Total!`,"","warning")
            return false;
        }
        if(isNullOrUndefined($scope.properties.value.fechaExamen) ){
            swal(`¡debe ingresar el valor de la fecha de examen!`,"","warning")
            return false;
        }
        if(isNullOrUndefined($scope.properties.value.PAAN)){
            swal(`¡debe ingresar el valor de PAAN!`,"","warning")
            return false;
        }
        
        if(isNullOrUndefined($scope.properties.value.PAAV)){
           swal('¡debe ingresar el valor de PAAV!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.PARA)){
           swal('¡debe ingresar el valor de PARA!',"","warning")
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
        blockUI.start();
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
                else {
                    swal('¡Se han guardado los datos correctamente!',"","success")
                    $scope.properties.tabla = "tabla";
                }
            })
            .error(function (data, status) {
                
            })
            .finally(function () {
                
                blockUI.stop();
            });
    }
    
    

}
