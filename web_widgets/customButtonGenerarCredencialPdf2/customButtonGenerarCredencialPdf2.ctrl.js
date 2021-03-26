function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {

    'use strict';
    $scope.contadorVerificarTask = 0;
    var vm = this;

    $scope.generatePDF = function () {
        //blockUI.start();
        Swal.fire({
            title: `Reagendar`,
            text: "¿Estás seguro que deseas reagendar cita?",
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
    $scope.ReagendarCitas = function () {
        // URL POST: https://anahuac-preproduction.bonitacloud.com/API/extension/AnahuacRest?url=reagendarExamen&p=0&c=100
        var req = {
            method: "POST",
            url: "/API/extension/AnahuacRest?url=reagendarExamen&p=0&c=100",
            data: angular.copy({ "caseid": $scope.properties.caseId })
        };
        return $http(req)
            .success(function (data, status) {
                //  let ipBonita = window.location.protocol + "//" + window.location.host + "";
                //  let url = "";
                // url = ipBonita + "/bonita/apps/aspirante/nueva_solicitud/";
                // top.location.href= url;
                $scope.VerificarTask();
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () { });
    }

   
    $scope.VerificarTask = function () {
        doRequest("GET", "/API/bpm/humanTask?p=0&c=10&f=caseId=" + $scope.properties.caseId + "&fstate=ready", null, null, null, function (datos, extra) {
            console.log(datos);
            if ($scope.contadorVerificarTask <= 100) {
                if (datos[0].name !== undefined && datos[0].name !== "Generar credencial") {
                    let ipBonita = window.location.protocol + "//" + window.location.host + "";
                    let url = "";
                    url = ipBonita + "/bonita/apps/aspirante/nueva_solicitud/";
                    top.location.href= url;
                }else{
                    $scope.VerificarTask();
                    $scope.contadorVerificarTask = $scope.contadorVerificarTask + 1;
                }
                
            }
        })


        // URL POST: https://anahuac-preproduction.bonitacloud.com/API/extension/AnahuacRest?url=reagendarExamen&p=0&c=100
        // var req = {
        //     method: "GET",
        //     url: "/API/bpm/humanTask?p=0&c=10&f=caseId={{caseId}}&fstate=ready",
        //     data: angular.copy({"caseid":$scope.properties.caseId})            
        // };
        // return $http(req)
        //     .success(function(data, status) {
        //        if($scope.contadorVerificarTask<=100){
        //            if(data!==){

        //            }
        //         $scope.VerificarTask(); 
        //         $scope.contadorVerificarTask=$scope.contadorVerificarTask+1;
        //        }

        //     })
        //     .error(function(data, status) {
        //         notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        //     })
        //     .finally(function() {});
    }

    function doRequest(method, url, params, dataToSend, extra, callback) {
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                callback(data, extra)
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () {

                blockUI.stop();
            });
    }

}