function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    $scope.setVal = function() {

        $scope.properties.variableDestino = angular.copy($scope.properties.variableAcopiar);
        $scope.properties.variableDestino2 = angular.copy($scope.properties.variableAcopiar2);
        $scope.properties.fotopasaporte = undefined;
        $scope.properties.cartaAA = undefined;
        $scope.properties.actanacimiento = undefined;
        $scope.properties.kardex = undefined;
        $scope.properties.descuento = undefined;
        $scope.properties.collageBoard = undefined;

        /*
        $scope.properties.catSolicitudDeAdmision.fotoPasaporteDocumentInput[0].filename = null;
        $scope.properties.catSolicitudDeAdmision.fotoPasaporteDocumentInput[0].tempPath = null;
        $scope.properties.catSolicitudDeAdmision.fotoPasaporteDocumentInput[0].contentType = null;
        $scope.properties.fotopasaporte = undefined;
        $scope.properties.catSolicitudDeAdmision.actaNacimientoDocumentInput[0].filename = null;
        $scope.properties.catSolicitudDeAdmision.actaNacimientoDocumentInput[0].tempPath = null;
        $scope.properties.catSolicitudDeAdmision.actaNacimientoDocumentInput[0].contentType = null;
        $scope.properties.actanacimiento = undefined;
        $scope.properties.catSolicitudDeAdmision.constanciaDocumentInput[0].filename = null;
        $scope.properties.catSolicitudDeAdmision.constanciaDocumentInput[0].tempPath = null;
        $scope.properties.catSolicitudDeAdmision.constanciaDocumentInput[0].contentType = null;
        $scope.properties.kardex = undefined;
        if($scope.properties.catSolicitudDeAdmision.descuentoDocumentInput.length > 0){
            $scope.properties.catSolicitudDeAdmision.descuentoDocumentInput[0].filename = null;
            $scope.properties.catSolicitudDeAdmision.descuentoDocumentInput[0].tempPath = null;
            $scope.properties.catSolicitudDeAdmision.descuentoDocumentInput[0].contentType = null;
            $scope.properties.descuento = undefined;
        }
        
        if($scope.properties.catSolicitudDeAdmision.resultadoCBDocumentInput.length > 0){
            $scope.properties.catSolicitudDeAdmision.resultadoCBDocumentInput[0].filename = null;
            $scope.properties.catSolicitudDeAdmision.resultadoCBDocumentInput[0].tempPath = null;
            $scope.properties.catSolicitudDeAdmision.resultadoCBDocumentInput[0].contentType = null;
            $scope.properties.collageBoard = undefined;
        }
        
        if($scope.properties.catSolicitudDeAdmision.cartaAADocumentInput.length > 0){
            $scope.properties.catSolicitudDeAdmision.cartaAADocumentInput[0].filename = null;
            $scope.properties.catSolicitudDeAdmision.cartaAADocumentInput[0].tempPath = null;
            $scope.properties.catSolicitudDeAdmision.cartaAADocumentInput[0].contentType = null;
            $scope.properties.cartaAA = undefined;
        }
        */

        $scope.openCloseModal();

    };

    $scope.openCloseModal = function() {
            if ($scope.properties.OpenModal) {
                modalService.open($scope.properties.modalId);
            } else {
                modalService.close();
            }



        }
        /**
         * Execute a get/post request to an URL
         * It also bind custom data from success|error to a data
         * @return {void}
         */
    function doRequest(method, url, params, dataToSend, callback) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: dataToSend,
            params: params
        };

        return $http(req)
            .success(function(data, status) {

                callback(data.data[0]);
            })
            .error(function(data, status) {
                console.error("error al llamar" + url);

            })
            .finally(function() {
                vm.busy = false;
            });
    }
}