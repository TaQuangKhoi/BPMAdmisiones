function PbButtonCtrl($scope, $http, $window, blockUI) {

    'use strict';

    var vm = this;

    $scope.$watch("properties.value", function(newValue, oldValue) {
        console.log("actualizacion")
        if (newValue !== undefined) {
            doRequest();
        }
    });


    function doRequest() {

        blockUI.start();
        var req = {
            method: "GET",
            url: `/bonita/API/extension/AnahuacRestGet?url=${$scope.properties.urlGet}&p=0&c=100${$scope.properties.urlParametro}`,
        };

        return $http(req)
            .success(function(data, status) {
                if ($scope.properties.resultadoGet == "array") {
                    if (data.data[0][$scope.properties.datoNombre] == true) {
                        $scope.properties.returnValue = data.data[0][$scope.properties.datoNombre]
                        sw2Action()
                    }
                } else {
                    if (data[$scope.properties.dataNombre] == true) {
                        $scope.properties.returnValue = data[$scope.properties.dataNombre];
                        sw2Action()
                    }
                }

            })
            .error(function(data, status) {}).finally(function() {

                blockUI.stop();
            });
    }

    function sw2Action() {

        Swal.fire({
            icon: 'warning',
            title: '' + $scope.properties.textMensaje,
            confirmButtonColor: "#FF5900",
        })

    }



}