function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {

    'use strict';

    var vm = this;

    $scope.generatePDF = function() {
            //blockUI.start();
debugger
            Swal.fire({
                title: `Reagendar`,
                text: "¿Está seguro que desea reagendar la citas?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: '#5cb85c',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Continuar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    //Confirmo Reasignar
                    $scope.ReagendarCitas();
                }
            })

        }
        ///FIN GENERAR PDF  
        $scope.ReagendarCitas = function() {
                // URL POST: https://anahuac-preproduction.bonitacloud.com/API/extension/AnahuacRest?url=reagendarExamen&p=0&c=100
            var req = {
                method: "POST",
                url: "/API/extension/AnahuacRest?url=reagendarExamen&p=0&c=100",
                data: angular.copy({"caseid":$scope.properties.caseId})            
            };
            return $http(req)
                .success(function(data, status) {
                     let ipBonita = window.location.protocol + "//" + window.location.host + "";
                     let url = "";
                    url = ipBonita + "/bonita/apps/aspirante/nueva_solicitud/";
                    top.location.href= url;
                })
                .error(function(data, status) {
                    notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
                })
                .finally(function() {});
        }

}