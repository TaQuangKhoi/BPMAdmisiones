function PbTableCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    this.isArray = Array.isArray;

    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row) {
        $scope.properties.selectedRow = row;
        $scope.properties.isSelected = 'editar';
    };

    this.selectRowDelete = function(row) {
        swal("Esta seguro que desea eliminar?", {
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
                        $scope.properties.selectedRow = row;
                        row.isEliminado = true
                        $scope.properties.selectedRow["todelete"] = false;
                        
                        $scope.$apply();
                        startProcess();
                        break;
                    default:

                }
            });
        /*
        
        $scope.properties.isSelected = 'editar';*/
    };

    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    function startProcess() {
        if ($scope.properties.processId) {
            var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function() {
                localStorageService.delete($window.location.href);
            });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
        }
    }

    function doRequest(method, url, params) {
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                doRequestGet();
                swal("Eliminado correctamente!", "", "success");
            })
            .error(function(data, status) {

            });
    }
    
    function doRequestGet() {
        var req = {
            method: "GET",
            url: $scope.properties.urlGet,
            data: angular.copy($scope.properties.dataToSend)
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.contenido = data;
            })
            .error(function(data, status) {

            });
    }
    function redireccionarTarea(rowData) {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/"+rowData.taskId,
            data: angular.copy({"assigned_id": $scope.properties.userId})
        };

        return $http(req)
            .success(function(data, status) {
                var url="/bonita/portal/resource/taskInstance/[NOMBREPROCESO]/[VERSIONPROCESO]/[NOMBRETAREA]/content/?id=[TASKID]&displayConfirmation=false";
                url = url.replace("[NOMBREPROCESO]", rowData.processName);
                url = url.replace("[VERSIONPROCESO]", rowData.processVersion);
                url = url.replace("[NOMBRETAREA]", rowData.taskName);
                url = url.replace("[TASKID]", rowData.taskId);
                $window.location.assign(url);
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    $(function() {
        doRequest("POST", $scope.properties.urlPost);
    })
    
    
    $scope.$watch("properties.dataToSend", function(newValue, oldValue) {
        if (newValue !== undefined) {
            doRequest("POST", $scope.properties.urlPost);
        }
        console.log($scope.properties.dataToSend);
    });
}