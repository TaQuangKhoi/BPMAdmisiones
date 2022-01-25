function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

  this.action = function action() {
    let nuevoItem= {
    "marca": "",
    "modelo": "",
    "ano": 0,
    "costo_aprox": 0,
    "situacion_auto": 0
    };
   $scope.properties.lst.push(nuevoItem);
  };

 

}
