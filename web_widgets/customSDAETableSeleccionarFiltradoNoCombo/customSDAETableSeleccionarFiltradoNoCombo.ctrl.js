function PbTableCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    this.isArray = Array.isArray;
    this.selectRow = function(row) {
        debugger
        $scope.properties.objCatGenerico = row;
        $scope.properties.accion = 'editar';
    };
    
     this.selectRowDelete = function(row) {
        swal("¿Esta seguro que desea eliminar?", {
                buttons: {
                    cancel: "No",
                    catch: {
                        text: "Si",
                        value: "Si",
                    }
                },
            })
            .then((value) => {
                switch (value) {
                    case "Si":
                        debugger
                         $scope.properties.objCatGenerico.objCatGenerico = row;
                         $scope.properties.objCatGenerico.objCatGenerico.isEliminado= true;
                         doRequest('POST', '../API/extension/AnahuacBecasRest?url=insertUpdateCatGenerico&p=0&c=0', $scope.properties.objCatGenerico )
                        break;
                    default:

                }
            }); 
    };
       function doRequest(method, url, datos, params) {
        var req = {
            method: method,
            url: url,
            data: datos,
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
            doRequest("POST", $scope.properties.urlPost, angular.copy($scope.properties.dataToFilter));
        }
        console.log($scope.properties.dataToFilter);
    });
    
}