function PbCheckboxCtrl($scope, $http, $log, widgetNameFactory, blockUI) {

    $scope.$watch('properties.value', function(value) {
        if (value === 'true' || value === true) {
            $scope.properties.value = true;
        } else {
            $scope.properties.value = false;
        }
    });

    this.name = widgetNameFactory.getName('pbCheckbox');

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbCheckbox property named "value" need to be bound to a variable');
    }




    $scope.doRequest = function(accion) {

        blockUI.start();
        var datos = { "deshabilitado": null };
        datos.deshabilitado = angular.copy(accion);

        var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=activarDesactivarLugarExamen&p=0&c=10",
            data: datos,
        };

        return $http(req)
            .success(function(data, status) {
                if (accion) {
                    swal("¡Se han deshabilitado los VPD!", "", "success")
                } else {
                    swal("¡Se han habilitado los VPD!", "", "success")
                }
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {

                blockUI.stop();
            });
    }
}