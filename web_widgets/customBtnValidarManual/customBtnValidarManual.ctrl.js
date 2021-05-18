function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      //$scope.properties.value = $scope.properties.texto;
      let paso = validacion();
      if(paso){
          
      }
      
    };
    
        
    
    function validacion(){
        if( isNullOrUndefined($scope.properties.value.IDBANNER) || $scope.properties.value.IDBANNER.length < 8){
            swal(`¡Debe agregar el idBanner!`,"","warning")
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

        //default
        /*if(isNullOrUndefined($scope.properties.value)){
           swal('¡debe ingresar el valor de !',"","warning")
            return false; 
        }*/
        
        
    }
    
    function isNullOrUndefined(dato){
        if(dato === undefined || dato === null || dato.length <= 0 ){
            return true;
        }
    }
    
    

}
