function PbButtonCtrl($scope, $http, $window, blockUI) {

    'use strict';

    var vm = this;

    $scope.$watch("properties.value", function(newValue, oldValue) {

        if (newValue !== undefined) {
            //if($scope.properties.lstContenido.length >1){return }
            doRequest();
        }
    });


    function doRequest() {

        blockUI.start();
        var req = {
            method: "GET",
            url: `/bonita/API/extension/AnahuacRestGet?url=${$scope.properties.url}&p=0&c=100${$scope.properties.urlInfo}`,
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.returnValue = data.data[0].isVencido;
                if (data.data[0].isVencido == true) {
                    sw2Action()
                }
                console.log("datos is vencido" + data);
            })
            .error(function(data, status) {
                $scope.properties.returnValue = false;
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            }).finally(function() {

                blockUI.stop();
            });
    }


    async function sw2Action() {

        let options = {};
        $scope.properties.value.forEach(element => {
            options[element.persistenceId] = element.descripcion;
        });
        //cancelButtonText:'continuar en otra ocasión',
        const { value } = await Swal.fire({
            title: 'El periodo de ingreso que seleccionaste ya ha vencido, para continuar debes seleccionar uno vigente',
            input: 'select',
            allowOutsideClick: false,
            inputOptions: options,
            inputPlaceholder: 'Seleccionar un periodo activo',
            showCancelButton: false,
            confirmButtonText: 'Guardar periodo',
            confirmButtonColor: "#FF5900",
            inputValidator: (value) => {
                return new Promise((resolve) => {
                    if (value !== "") {
                        resolve()
                    } else {
                        resolve('Se tiene que seleccionar un periodo')
                    }
                })
            }
        })
        if (value) {
            doRequest2(value)
                //Swal.fire(`El periodo seleccionado es ${options[value]}`)
        } else {
            Swal.fire(`No se selecciono algun periodo`)
        }

    }

    function doRequest2(persistenceid) {
        let info = angular.copy({
            "persistenceid": persistenceid + "",
            "caseid": $scope.properties.caseId + "",
        })
        blockUI.start();
        var req = {
            method: "POST",
            url: `/bonita/API/extension/AnahuacRest?url=${$scope.properties.urlPost}&p=0&c=100`,
            data: info
        };

        return $http(req)
            .success(function(data, status) {
                Swal.fire(`sea actualizado el periodo correctamente`)
                setTimeout(() => {
                    location.reload();
                }, 1000);

            })
            .error(function(data, status) {
                Swal.fire(`sea sucitado un error al actualizar el periodo`)

            }).finally(function() {

                blockUI.stop();
            });
    }

}