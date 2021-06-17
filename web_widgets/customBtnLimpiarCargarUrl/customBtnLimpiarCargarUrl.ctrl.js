function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
        if($scope.properties.tipoVariable === "json"){
          $scope.properties.value =  {};  
        } else if($scope.properties.tipoVariable === "array"){
          $scope.properties.value =  [];  
        } else if($scope.properties.tipoVariable === "text"){
          $scope.properties.value =  "";  
        }
        $scope.properties.variable = $scope.properties.variableDato
        doRequest("GET",$scope.properties.url,"");
    };
    
    function doRequest(method, url,datos) {
        var req = {
            method: method,
            url: url,
            data: angular.copy(datos)
        };
        return $http(req)
            .success(function (data, status) {
                debugger;
                $scope.properties.value = data.data;
            })
            .error(function (data, status) {
            })
    }
}
