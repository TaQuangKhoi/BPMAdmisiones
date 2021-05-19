function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
        debugger;
        if($scope.properties.tipoVariable === "json"){
          $scope.properties.value =  {};  
        } else if($scope.properties.tipoVariable === "array"){
          $scope.properties.value =  [];  
        } else if($scope.properties.tipoVariable === "text"){
          $scope.properties.value =  "";  
        }
      
    };

}
