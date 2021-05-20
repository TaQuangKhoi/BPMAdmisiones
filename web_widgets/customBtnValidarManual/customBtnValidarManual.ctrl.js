function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService,blockUI) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      //$scope.properties.value = $scope.properties.texto;
      let paso = validacion();
      if(paso){
          doRequest("POST",$scope.properties.urlPost,$scope.properties.value)
      }
    };
    
        
    
    function validacion(){
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
        
        if(isNullOrUndefined($scope.properties.value.LA1)){
           swal('¡debe ingresar el valor de LA1!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.LA2)){
           swal('¡debe ingresar el valor de LA2!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.LA3)){
           swal('¡debe ingresar el valor de LA3!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.PG1)){
           swal('¡debe ingresar el valor de PG1!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.PG2)){
           swal('¡debe ingresar el valor de PG2!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.PG3)){
           swal('¡debe ingresar el valor de PG3!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.PG4)){
           swal('¡debe ingresar el valor de PG4!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.PV1)){
           swal('¡debe ingresar el valor de PV1!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.PV2)){
           swal('¡debe ingresar el valor de PV2!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.PV3)){
           swal('¡debe ingresar el valor de PV3!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.PE1)){
           swal('¡debe ingresar el valor de PE1!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.PE2)){
           swal('¡debe ingresar el valor de PE2!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.PE3)){
           swal('¡debe ingresar el valor de PE3!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.PE4)){
           swal('¡debe ingresar el valor de PE4!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.LEO1)){
           swal('¡debe ingresar el valor de LEO1!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.LEO2)){
           swal('¡debe ingresar el valor de LEO2!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.LEO3)){
           swal('¡debe ingresar el valor de LEO3!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.LEO4)){
           swal('¡debe ingresar el valor de LEO4!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.LEO5)){
           swal('¡debe ingresar el valor de LEO5!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.CIT1)){
           swal('¡debe ingresar el valor de CIT1!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.CIT2)){
           swal('¡debe ingresar el valor de CIT2!',"","warning")
            return false; 
        }
        
        if(isNullOrUndefined($scope.properties.value.HI1)){
           swal('¡debe ingresar el valor de HI1!',"","warning")
            return false; 
        }
        if(isNullOrUndefined($scope.properties.value.HI2)){
           swal('¡debe ingresar el valor de HI2!',"","warning")
            return false; 
        }
        if(isNullOrUndefined($scope.properties.value.HI3)){
           swal('¡debe ingresar el valor de HI3!',"","warning")
            return false; 
        }
        if(isNullOrUndefined($scope.properties.value.HI4)){
           swal('¡debe ingresar el valor de HI4!',"","warning")
            return false; 
        }
        if(isNullOrUndefined($scope.properties.value.HI5)){
           swal('¡debe ingresar el valor de HI5!',"","warning")
            return false; 
        }
        if(isNullOrUndefined($scope.properties.value.HI6)){
           swal('¡debe ingresar el valor de HI6!',"","warning")
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
