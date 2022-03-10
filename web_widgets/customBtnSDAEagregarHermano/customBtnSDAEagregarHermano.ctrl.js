function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {
        // let nuevoHermano = {
        //     "nombre": "",
        //     "apellidos": "",
        //     "edad": "",
        //     "estudia": false,
        //     "institucion": "",
        //     "tiene_beca": false,
        //     "porcentaje_beca_asignado": "",
        //     "colegiatura_mensual": "",
        //     "trabaja": false,
        //     "empresa": "",
        //     "ingreso_mensual": ""
        // };
        let nuevoHermano = {
            "persistenceId_string":"",
            "caseId":$scope.properties.caseId,
            "nombres":"",
            "apellidos":"",
            "edad": 0,
            "isEstudia": false,
            "institucion":"",
            "isTieneBeca":false,
            "porcentajeBecaAsignado":0,
            "colegiaturaMensual": 0,
            "isTrabaja": false,
            "empresa":"",
            "ingresoMensual": 0,
        }
        
        $scope.properties.lstHermanos.push(nuevoHermano);
    };
}