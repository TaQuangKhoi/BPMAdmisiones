function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

  this.action = function action() {
    // $scope.properties.valor = !$scope.properties.valor
    $scope.properties.mensaje=""
     modalService.close();
  };


}
