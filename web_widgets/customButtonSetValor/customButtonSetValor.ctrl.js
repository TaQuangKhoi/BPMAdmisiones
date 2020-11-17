function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    $scope.setVal = function() {
        $scope.properties.variableDestino = angular.copy($scope.properties.variableAcopiar);
        $scope.openCloseModal();
    };

    $scope.openCloseModal= function () {
        if($scope.properties.OpenModal){
            modalService.open($scope.properties.modalId);
        }
        else {
            modalService.close();
        }
        
        
        
    }
}