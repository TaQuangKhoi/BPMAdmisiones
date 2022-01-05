function PbTableCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    this.isArray = Array.isArray;
    this.selectRow = function(row) {
        debugger
        $scope.properties.objCatGenerico = row;
        $scope.properties.accion = 'editar';
    };
       function doRequest(method, url, params) {
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToFilter),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.contenido = data.data;
                $scope.value = data.totalRegistros;
                $scope.loadPaginado();
                console.log(data.data)
            })
            .error(function(data, status) {
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }
        $scope.$watch("properties.dataToFilter", function(newValue, oldValue) {
        if (newValue !== undefined) {
            doRequest("POST", $scope.properties.urlPost);
        }
        console.log($scope.properties.dataToFilter);
    });
    
}