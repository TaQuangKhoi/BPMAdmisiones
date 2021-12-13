function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {
        $scope.properties.value = $scope.properties.cleanObject;
        closeModal($scope.properties.closeOnSuccess);
        openModal($scope.properties.modalId);
    };

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal(shouldClose) {
        if(shouldClose)
            modalService.close();
    }
}