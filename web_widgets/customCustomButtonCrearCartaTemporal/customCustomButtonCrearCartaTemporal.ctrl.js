function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';
  
    var vm = this;
  
    this.action = function action() {
        clearCartaTemporal();
    };

    function clearCartaTemporal() {
        let url = "/bonita/API/extension/AnahuacRest?url=clearInfoCartaTemporal&p=0&c=100";
        var req = {
            method: "POST",
            url: url,
            data: {}
        };

        $http(req).success(function(){
            startProcess();
        }).error(function(){
            swal("Error", "No es posible procesar la acción. Intente de nuevo mas tarde","error");
        }).finally(function(){
            vm.busy = false;
        });
    }

    function startProcess() {
        let url = "/bonita/API/bpm/process/" + $scope.properties.processId + "/instantiation";
        var req = {
            method: "POST",
            url: url,
            data: angular.copy($scope.properties.dataToSend)
        };

        $http(req).success(function(){
            window.top.location = "/bonita/apps/administrativo/envio_cartas";
        }).error(function(){
            swal("Error", "No es posible procesar la acción. Intente de nuevo mas tarde","error");
        }).finally(function(){
            vm.busy = false;
        });
    }
}