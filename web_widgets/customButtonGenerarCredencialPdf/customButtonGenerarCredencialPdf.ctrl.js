function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {

    'use strict';

    var vm = this;
    //GENERAR PDF
    $scope.generatePDF = function() {
            //blockUI.start();
            var element = document.getElementById('canvas');
            var opt = {
                margin: [0, 0, 0, 0],
                filename: 'CredencialAnahuac.pdf',
                image: { type: 'jpg', quality:  0.98 },
                html2canvas: { dpi: 150, letterRendering: true, useCORS: true },
                jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
            };
            html2pdf().from(element).set(opt).save();
            Swal.fire("¡Descarga!", "La descarga comenzara en breve, no cierres ni actualices la página.", "success");
            //blockUI.stop()
            if ($scope.properties.isCorrectTask) {
                // document.getElementById('btnDescargar').disabled= 'disabled'; 
                // document.getElementById('btnDescargar2').disabled= 'disabled'; 
                // document.getElementById('btnFinalizarTarea').disabled = false; 
                // document.getElementById('btnFinalizarTarea2').disabled = false; 
               
               console.log("Cambio de tarea")
                $scope.asignarTarea();
                $scope.properties.isCorrectTask = false;
            }else{
                console.log("Solo descarga")
            }
        }
        ///FIN GENERAR PDF  

    ////ASIGNAR TAREA
    $scope.asignarTarea = function() {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + $scope.properties.taskId,
            data: angular.copy({ "assigned_id": "" })
        };

        return $http(req)
            .success(function(data, status) {
                redireccionarTarea();
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    function redireccionarTarea() {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + $scope.properties.taskId,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };

        return $http(req)
            .success(function(data, status) {
                $scope.submitTask();
            })
            .error(function(data, status) {

                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    $scope.submitTask = function() {

        var req = {
            method: "POST",
            url: "/bonita/API/bpm/userTask/" + $scope.properties.taskId + "/execution?assign=false",
            data: angular.copy($scope.properties.dataToSend)
        };

        return $http(req)
            .success(function(data, status) {
                // $scope.getConsulta();
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }
}