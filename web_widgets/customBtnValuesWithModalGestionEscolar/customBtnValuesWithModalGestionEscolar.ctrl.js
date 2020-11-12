function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      $scope.properties.dataReset = [{"nombre" : "", "descripcion" : "", "enlace" : "", "propedeutico" : false, "programaparcial" : false, "matematicas" : false, "inscripcionenero" : "", "inscripcionagosto" : "", "periododisponible" : "", "isEliminado" : false, "tipocentroestudios":"", "persistenceId_string" : "","usuarioCreacion":"", "fechaCreacion":"" }];
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
