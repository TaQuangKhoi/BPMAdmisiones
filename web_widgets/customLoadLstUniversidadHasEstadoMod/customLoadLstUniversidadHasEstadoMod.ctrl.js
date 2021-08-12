function loadlstUniversidadesHasEstadoModCtrl($scope, $http) {

    $scope.loadListData = function() {
        $scope.properties.lstUniversidadesHasEstadoMod = [];
        console.log("loadlstUniversidadesHasEstadoModCtrl---------------------------------------------------------------------------------------");
        for (var index in $scope.properties.lstUniversidadesHasEstado) {
            $scope.properties.lstUniversidadesHasEstadoMod.push(angular.copy($scope.properties.lstUniversidadesHasEstado[index]));
        }

    }

    function doRequest(method, url, dataToSend, callback, errorCallback) {

        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend)
        };

        return $http(req)
            .success(callback)
            .error(errorCallback);
    }

    $scope.$watchCollection("properties.lstUniversidadesHasEstado", function(newValue, oldValue) {
        $scope.loadListData();
    });
}