function PbTableCtrl($scope, $http, modalService) {

    this.isArray = Array.isArray;
    $scope.loading = false;

    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row) {
        if (this.isClickable()) {
            //$scope.properties.selectedRow = row;
            console.log($scope.properties.selectedRow);
        }
    };

    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }
    
    
    $scope.modificarData = function(row, index) {
            closeModal();
            openModal("modalInputs");
            $scope.properties.selectedToModificate = [{"nombre" : "", "descripcion" : "", "enlace" : "", "propedeutico" : false, "programaparcial" : false, "matematicas" : false, "inscripcionenero" : "", "inscripcionagosto" : "", "periododisponible" : "", "isEliminado" : false, "tipocentroestudios":"", "persistenceId_string" : "","usuarioCreacion":"", "FechaCreacion":"" }];
            $scope.properties.selectedToModificate[0].nombre = $scope.properties.contenido[index].nombre;
            $scope.properties.selectedToModificate[0].descripcion = $scope.properties.contenido[index].descripcion;
            $scope.properties.selectedToModificate[0].enlace = $scope.properties.contenido[index].enlace;
            $scope.properties.selectedToModificate[0].propedeutico = $scope.properties.contenido[index].propedeutico;
            $scope.properties.selectedToModificate[0].programaparcial = $scope.properties.contenido[index].programaparcial;
            $scope.properties.selectedToModificate[0].matematicas = $scope.properties.contenido[index].matematicas;
            $scope.properties.selectedToModificate[0].inscripcionenero = parseInt($scope.properties.contenido[index].inscripcionenero);
            $scope.properties.selectedToModificate[0].inscripcionagosto = parseInt($scope.properties.contenido[index].inscripcionagosto);
            $scope.properties.selectedToModificate[0].periododisponible = $scope.properties.contenido[index].periododisponible;
            $scope.properties.selectedToModificate[0].tipocentroestudios = $scope.properties.contenido[index].tipocentroestudios;
            $scope.properties.selectedToModificate[0].usuarioCreacion = $scope.properties.contenido[index].usuarioCreacion;
            $scope.properties.selectedToModificate[0].FechaCreacion = $scope.properties.contenido[index].FechaCreacion;
            $scope.properties.selectedToModificate[0].orden = $scope.properties.contenido[index].orden;
            $scope.properties.isModificacion = true;
            $scope.properties.index = index;
            console.log($scope.properties.selectedToModificate[0]);
    }

    $scope.deleteData = function(row, index) {

        if ($scope.loading == false) {
            $("#loading").modal("show");
            $scope.loading = true;
            $scope.properties.contenido[index].isEliminado = true
            console.log(row);
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
  

    $scope.sendData = function(row, index) {

        if ($scope.loading == false) {
            $("#loading").modal("show");
            console.log(index)
            $scope.loading = true;
            $scope.properties.contenido[index].isEnabled = !$scope.properties.contenido[index].isEnabled
            console.log(row);
            $scope.asignarTarea()
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
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
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
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    $scope.submitTask = function() {
        console.log($scope.properties.dataToSend)
        var req = {
            method: "POST",
            url: "/bonita/API/bpm/userTask/" + $scope.properties.taskId + "/execution?assign=false",
            data: angular.copy($scope.properties.dataToSend)
        };

        return $http(req)
            .success(function(data, status) {
                $scope.getConsulta();
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
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
                $scope.properties.contenido = data;
                $scope.getObjTaskInformation();
            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                $scope.loading = false;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
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
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }
}