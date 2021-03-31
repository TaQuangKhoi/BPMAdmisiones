function PbButtonCtrl($scope, $http, $window,blockUI) {

  'use strict';

  var vm = this;
    $scope.sesion = [];
    $scope.jsonEntrevista = {};
    $scope.jsonPsicometrico = {};
    $scope.jsonCollage = {};
    
    $scope.$watch("properties.urlInfo", function (newValue, oldValue) {
        if (newValue !== undefined) {
            //if($scope.properties.lstContenido.length >1){return }
            doRequest();
        }
    });
    
    
    function doRequest() {
        debugger;
        blockUI.start();
        var req = {
            method: "GET",
            url: `/bonita/API/extension/AnahuacRestGet?url=${$scope.properties.url}&p=0&c=100${$scope.properties.urlInfo}`,
        };
    
        return $http(req)
            .success(function (data, status) {
               $scope.properties.value = data.data;
               $scope.cargarDatos(data.data);
            })
            .error(function (data, status) {
                    notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            }).finally(function () {
                
                blockUI.stop();
            });
        }
    
    $scope.cargarDatos = function(datos){
        debugger;
        for (let index = 0; index < datos.length; index++) {
                const element = datos[index];
                $scope.sesion.nombre = element.snombre;
                $scope.sesion.descripcion = element.sdescripcion;
                if (element.descripcion == "Examen de aptitudes y conocimientos") {
                    $scope.jsonCollage.nombre = element.pnombre;
                    $scope.jsonCollage.descripcion = element.pdescripcion;
                    $scope.jsonCollage.aplicacion = element.aplicacion;
                    $scope.jsonCollage.horario = element.horario;
                    $scope.jsonCollage.lugar = element.lugar;
                    $scope.jsonCollage.municipio = element.municipio;
                    $scope.jsonCollage.colonia = element.colonia;
                    $scope.jsonCollage.codigo_postal = element.codigo_postal;
                    $scope.jsonCollage.calle = element.calle;
                    $scope.jsonCollage.numero_int = element.numero_int;
                    $scope.jsonCollage.online = element.online;
                }
                if (element.descripcion == "Examen Psicométrico") {
                    $scope.jsonPsicometrico.nombre = element.pnombre;
                    $scope.jsonPsicometrico.descripcion = element.pdescripcion;
                    $scope.jsonPsicometrico.aplicacion = element.aplicacion;
                    $scope.jsonPsicometrico.horario = element.horario;
                    $scope.jsonPsicometrico.lugar = element.lugar;
                    $scope.jsonPsicometrico.municipio = element.municipio;
                    $scope.jsonPsicometrico.colonia = element.colonia;
                    $scope.jsonPsicometrico.codigo_postal = element.codigo_postal;
                    $scope.jsonPsicometrico.calle = element.calle;
                    $scope.jsonPsicometrico.numero_int = element.numero_int;
                    $scope.jsonPsicometrico.online = element.online;
                }
                if (element.descripcion == "Entrevista") {
                    $scope.jsonEntrevista.nombre = element.pnombre;
                    $scope.jsonEntrevista.descripcion = element.pdescripcion;
                    $scope.jsonEntrevista.aplicacion = element.aplicacion;
                    $scope.jsonEntrevista.horario = element.horario;
                    $scope.jsonEntrevista.lugar = element.lugar;
                    $scope.jsonEntrevista.municipio = element.municipio;
                    $scope.jsonEntrevista.colonia = element.colonia;
                    $scope.jsonEntrevista.codigo_postal = element.codigo_postal;
                    $scope.jsonEntrevista.calle = element.calle;
                    $scope.jsonEntrevista.numero_int = element.numero_int;
                    $scope.jsonEntrevista.online = element.online;

                }
            }
    }
}
