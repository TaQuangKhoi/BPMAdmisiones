function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';
    
    var vm = this;
    
    this.action = function action() {
        $scope.properties.objNuevo = {
            "idCampus":"",
            "idTipoApoyo": $scope.properties.idTipoApoyo,
            "isDelete": false
        }
        modalService.open($scope.properties.modalId);
    };

}
