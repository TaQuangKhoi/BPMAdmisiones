function PbButtonCtrl($scope, $http) {
    'use strict';
    var vm = this;
    this.isArray = Array.isArray;
    $scope.loading = false;
    
    $scope.sendData = function() {
        if ($scope.loading == false) {
            if($scope.properties.isModificacion === false){
                 
            }else{
                $("#loading").modal("show");
                $scope.loading = true;
                $scope.properties.contenido[$scope.properties.index].tipoCorreo = $scope.properties.nuevosValores[0].tipoCorreo;
                $scope.properties.contenido[$scope.properties.index].codigo = $scope.properties.nuevosValores[0].codigo;
                $scope.properties.contenido[$scope.properties.index].descripcion = $scope.properties.nuevosValores[0].descripcion;
                $scope.properties.contenido[$scope.properties.index].tipoDocumento = $scope.properties.nuevosValores[0].tipoDocumento;
                $scope.properties.contenido[$scope.properties.index].nombreImagenHeader = $scope.properties.nuevosValores[0].nombreImagenHeader;
                $scope.properties.contenido[$scope.properties.index].anguloImagenHeader = $scope.properties.nuevosValores[0].anguloImagenHeader;
                $scope.properties.contenido[$scope.properties.index].nombreImagenFooter = $scope.properties.nuevosValores[0].nombreImagenFooter;
                $scope.properties.contenido[$scope.properties.index].montoAspiranteForaneoDolares = $scope.properties.nuevosValores[0].montoAspiranteForaneoDolares;
                $scope.properties.contenido[$scope.properties.index].anguloImagenFooter = $scope.properties.nuevosValores[0].anguloImagenFooter;
                $scope.properties.nuevosValores = [];
                $scope.asignarTarea()
            }
        
        } else {
            console.log("click doble");
        }
    }

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
                $("#loading").modal("hide");
                $scope.loading = false;
               // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
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
                $("#loading").modal("hide");
                $scope.loading = false;
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
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
                console.log("$scope.properties.dataToSend");
                console.log($scope.properties.dataToSend);
                $scope.getConsulta();
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
               // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    $scope.getConsulta = function() {
        var req = {
            method: "GET",
            url: $scope.properties.urlConsulta
        };

        return $http(req)
            .success(function(data, status) {
                //console.log("data");
                //console.log(data);
                $scope.properties.contenido = data;
                $scope.getObjTaskInformation();
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    $scope.getObjTaskInformation = function() {
        var req = {
            method: "GET",
            url: $scope.properties.urlTaskInformation
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.objTaskInformation = data;
                $scope.loading = false;
                $("#loading").modal("hide");
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
               // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }
}
