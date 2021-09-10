function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    $scope.setVal = function() {
        $scope.properties.variableDestino = angular.copy($scope.properties.variableAcopiar);
        $scope.openCloseModal()

    };

    $scope.openCloseModal = function() {
            if ($scope.properties.OpenModal) {
                modalService.open($scope.properties.modalId);
            } else {
                $scope.properties.modalId[0] = {
                    "campus": null,
                    "nombreCampus": "",
                    "descripcion": "",
                    "isEliminado": false,
                    "usuarioBanner": "",
                    "fechaImportacion": null,
                    "clave": "",
                    "fechaCreacion": null,
                    "isEnabled": true,
                    "isCuatrimestral": false,
                    "fechaInicio": null,
                    "fechaFin": null,
                    "isSemestral": false,
                    "isAnual": false,
                    "year": "",
                    "codigo": "",
                    "activo": false,
                    "id": ""
                }
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