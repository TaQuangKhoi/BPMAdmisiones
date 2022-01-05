function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

  this.action = function action() {
    let nuevoHermano= {
    "nombre": "",
    "apellidos": "",
    "edad": "",
    "estudia": false,
    "institucion": "",
    "tiene_beca": false,
    "porcentaje_beca_asignado": "",
    "colegiatura_mensual": "",
    "trabaja": false,
    "empresa": "",
    "ingreso_mensual": ""
    };
   $scope.properties.lstHermanos.push(nuevoHermano);
  };

 

}
