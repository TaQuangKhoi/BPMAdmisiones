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
        
        if($scope.properties.tipoVariable2 === "json"){
          $scope.properties.value2 =  {};  
        } else if($scope.properties.tipoVariable2 === "array"){
          $scope.properties.value2 =  [];  
        } else if($scope.properties.tipoVariable2 === "text"){
          $scope.properties.value2 =  "";  
        }
        $scope.properties.variable = $scope.properties.variableDato
      
    };

}
