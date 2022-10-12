function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    $scope.setVal = function() {
        $scope.properties.objCatGenerico = angular.copy($scope.properties.objCatGenericoNuevo);
        $scope.properties.accion = 'agregar';
    };

    $scope.openCloseModal = function() {
            if ($scope.properties.OpenModal) {
                modalService.open($scope.properties.modalId);
            } else {
                modalService.close();
            } 
        }
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