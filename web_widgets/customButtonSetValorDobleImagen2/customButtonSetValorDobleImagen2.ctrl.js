function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    $scope.setVal = function() {
        $scope.properties.variableDestino = angular.copy($scope.properties.variableAcopiar);
        $scope.properties.variableDestino2 = angular.copy($scope.properties.variableAcopiar2);
        $scope.properties.variableDestino3 = angular.copy($scope.properties.variableAcopiar3);
        $scope.properties.variableDestino4 = angular.copy($scope.properties.variableAcopiar2);
        $scope.properties.catUsuariosSelectedNR = "";
        $scope.openCloseModal()

    };

    $scope.openCloseModal = function() {
            window.scrollTo(0, 0);
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