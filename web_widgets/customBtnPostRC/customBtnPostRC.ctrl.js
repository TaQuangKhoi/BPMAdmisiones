function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService,blockUI) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      doRequest("POST",$scope.properties.url,"")
    };
    
    
    function doRequest(method, url, params) {
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                actualizacion_de_datos();
            })
            .error(function (data, status) {
            })
            .finally(function () {
                blockUI.stop();
            });
    }
    
    function actualizacion_de_datos(){
        if($scope.properties.tipoValor2 === "json"){
          $scope.properties.valor2 =  {};  
        } else if($scope.properties.tipoValor2 === "array"){
          $scope.properties.valor2 =  [];  
        } else if($scope.properties.tipoValor2 === "text"){
          $scope.properties.valor2 =  "";  
        }
        
        if($scope.properties.tipoValor3 === "json"){
          $scope.properties.valor3 =  {};  
        } else if($scope.properties.tipoValor3 === "array"){
          $scope.properties.valor3 =  [];  
        } else if($scope.properties.tipoValor3 === "text"){
          $scope.properties.valor3 =  "";  
        }
        
        $scope.properties.datos = $scope.properties.valor;
    }

}
