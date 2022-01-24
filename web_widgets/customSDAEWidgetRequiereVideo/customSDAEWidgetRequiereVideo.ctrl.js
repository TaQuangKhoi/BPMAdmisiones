function PbCheckboxCtrl($scope, $log, widgetNameFactory, modalService, $http) {

    $scope.$watch('properties.value', function(value) {
        if (value === 'true' || value === true) {
          $scope.properties.value = true;
        } else {
          $scope.properties.value = false;
        }
    });
  
    $scope.showModalCondiciones = function(){
        modalService.open("modalDetalleVideo");
    }
    
    $scope.updateTipoApoyoVideocase = function(){
        debugger;
        var req = {
            method: "POST",
            url: $scope.properties.url,
            data: angular.copy($scope.properties.tipoApoyoSeleccionado)
        };

        return $http(req)
            .success(function(data, status) {
                 alert("si")
                 console.log(data.data)
            })
            .error(function(data, status) {
                debugger;
                console.log("ERROR" +  data.toString());
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                debugger;
                console.log("Final ");
            });
    }
    
    if (!$scope.properties.isBound('value')) {
        $log.error('the pbCheckbox property named "value" need to be bound to a variable');
    }
}
