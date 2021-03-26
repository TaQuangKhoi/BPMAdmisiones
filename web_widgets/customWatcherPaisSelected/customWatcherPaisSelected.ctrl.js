function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {
        var vm = this;
    $scope.$watchCollection('properties.PrepaSeleccionada', function (items) {
        if ($scope.properties.PrepaSeleccionada != undefined) {
           
            if($scope.properties.PrepaSeleccionada!== "México"){
            $scope.getcampusbypais();  
            }

        }
    });

        $scope.$watchCollection('properties.EstadoSeleccionado', function (items) {
        if ($scope.properties.EstadoSeleccionado != undefined) {
            $scope.getcampusbyEstado();
        }
    });

    $scope.getcampusbypais = function() {
        doRequest("GET", "../API/bdm/businessData/com.anahuac.catalogos.CatCiudad?q=getCiudadesByPais&f=pais="+$scope.properties.PrepaSeleccionada+"&p=0&c=1000&f=caseId=00000", null, null, null, function(datos, extra) {
            $scope.properties.listaCiudades = datos;

        })
    }
    
     $scope.getcampusbyEstado = function() {
        doRequest("GET", "../API/bdm/businessData/com.anahuac.catalogos.CatCiudad?q=getCatCiudadByEstado&p=0&c=999&f=estado="+$scope.properties.EstadoSeleccionado, null, null, null, function(datos, extra) {
            $scope.properties.listaCiudades = datos;
        })
    }
    
        function doRequest(method, url, params, dataToSend, extra, callback) {
        vm.busy = true;
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                callback(data, extra)
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {
                vm.busy = false;
                blockUI.stop();
            });
    }
    
}