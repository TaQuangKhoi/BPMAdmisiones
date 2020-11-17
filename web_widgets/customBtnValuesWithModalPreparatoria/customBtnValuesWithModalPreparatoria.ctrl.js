function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      $scope.properties.dataReset = [{"clave":"","descripcion":"","pais":"","estado":"","ciudad":"","isEliminado":false,"isEnabled":true,"perteneceRed":"","persistenceId_string":""}];
      closeModal();
      openModal($scope.properties.modalName);
      $scope.properties.value =  $scope.properties.seleccion;
    };
    
    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal() {
        modalService.close();
    }

}
