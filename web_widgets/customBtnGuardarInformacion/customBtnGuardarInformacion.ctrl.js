function PbButtonCtrl($scope, $http, modalService) {
    'use strict';
    var vm = this;

    this.isArray = Array.isArray;
    $scope.loading = false;
    $scope.lstContenidoRespaldo = [];

    $scope.generateObjContrato = function() {
        var obj = {};
        var resultado = [];
        
        if($scope.properties.isModificacion){
            $scope.properties.contenido[$scope.properties.index] = angular.copy($scope.properties.nuevosValores);
        }
        else{
            $scope.properties.contenido.push($scope.properties.nuevosValores);   
        }
        $scope.asignarTarea();
    }

    $scope.sendData = function() {
        if ($scope.loading == false) {
            $("#loading").modal("show");
            $scope.loading = true;
            if ($scope.properties.isModificacion === false) {
                $scope.properties.nuevosValores.forEach(element => {
                    $scope.properties.contenido.push(element);
                })
                $scope.properties.nuevosValores = [];
            } else {
                $scope.properties.contenido[$scope.properties.index].clave = $scope.properties.nuevosValores[0].clave;
                $scope.properties.contenido[$scope.properties.index].descripcion = $scope.properties.nuevosValores[0].descripcion;
                $scope.properties.contenido[$scope.properties.index].fechaCreacion = $scope.properties.nuevosValores[0].fechaCreacion;
                $scope.properties.contenido[$scope.properties.index].usuarioCreacion = $scope.properties.nuevosValores[0].usuarioCreacion;
                $scope.properties.nuevosValores = [];
            }

            $scope.asignarTarea()
        } else {
            console.log("click doble");
        }
    }

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal() {
        modalService.close();
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
                //console.log("$scope.properties.dataToSend");
                //console.log($scope.properties.dataToSend);
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
                closeModal();
                $("#loading").modal("hide");
                $scope.properties.nuevosValores = angular.copy($scope.properties.dataToSet);
                $scope.properties.ocultarTable = false;
                $scope.properties.isModificacion = false;
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    function uuidv4() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = Math.random() * 16 | 0,
                v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }
}