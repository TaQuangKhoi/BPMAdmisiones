function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService,blockUI) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      //$scope.properties.value = $scope.properties.texto;
      var count = 0;
      blockUI.start();
      try{
          if(!isNullOrUndefined($scope.properties.value) ){
            doRequest("POST",$scope.properties.urlPost,$scope.properties.value).then(function() {
                if($scope.properties.lstErrores.length > 0){
                    doRequest("POST",$scope.properties.urlErrores,$scope.properties.lstErrores).then(function(){
                        $scope.properties.tabla = "tabla"; 
                        $scope.properties.value = []; 
                        swal('¡Se han terminado la carga masiva!',"","success")
                        doRequest("GET",$scope.properties.urlLimpiar,"")
                    });
                }else{
                    $scope.properties.tabla = "tabla"; 
                    $scope.properties.value = []; 
                    swal('¡Se han terminado la carga masiva!',"","success")
                    doRequest("GET",$scope.properties.urlLimpiar,"")
                }
                
            });    
        /*$scope.properties.value.forEach(datos =>{
        count++;
          var info = angular.copy(datos);
              doRequest("POST",$scope.properties.urlPost,info).then(function() {
                  if(count == $scope.properties.value.length){
                      if($scope.properties.lstErrores.length > 0){
                          doRequest("POST",$scope.properties.urlErrores,$scope.properties.lstErrores).then(function(){
                              $scope.properties.tabla = "tabla"; 
                              $scope.properties.value = []; 
                              swal('¡Se han terminado la carga masiva!',"","success")
                              doRequest("GET",$scope.properties.urlLimpiar,"")
                          });
                      }else{
                          $scope.properties.tabla = "tabla"; 
                          $scope.properties.value = []; 
                          swal('¡Se han terminado la carga masiva!',"","success")
                          doRequest("GET",$scope.properties.urlLimpiar,"")
                      }
                      
                      
                  }    
              });       
        }); */
            
      }
      }catch(error){}
      finally{
          blockUI.stop();
      }
    };
    
    
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
                console.log(status)
                console.log(data)
            })
            .error(function (data, status) {
                
            })
            .finally(function () {
                
                
            });
    }
    
    

}
