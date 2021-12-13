function PbTableCtrl($scope, $http, modalService, blockUI) {

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
        $scope.properties.selectedToModificate = row;
        $scope.properties.selectedIndex = index;
        localStorage.setItem("index",index);
        /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
            $scope.properties.selectedIndex--;
        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
            if ($scope.properties.selectedIndex === 0) {
                console.log("validar 0");
            } else if ($scope.properties.selectedIndex === 1) {
                console.log("validar 1");
            } else if ($scope.properties.selectedIndex === 2) {
                console.log("validar 2");
            } else if ($scope.properties.selectedIndex === 3) {
                console.log("validar 3");
            } else if ($scope.properties.selectedIndex === 4) {
                console.log("validar 4");
            }
            $scope.properties.selectedIndex++;
        }
        closeModal();
        openModal("modalInputs");
        $scope.properties.selectedToModificate = [{ "persistenceId_string": "", "tipoCorreo": "", "descripcion": "", "codigo": "", "isEliminado": false, "tipoDocumento": "", "nombreImagenHeader": "", "anguloImagenHeader": "", "nombreImagenFooter": "", "anguloImagenFooter": "" }];
        $scope.properties.selectedToModificate[0].tipoCorreo = $scope.properties.contenido[index].tipoCorreo;
        $scope.properties.selectedToModificate[0].descripcion = $scope.properties.contenido[index].descripcion;
        $scope.properties.selectedToModificate[0].tipoDocumento = $scope.properties.contenido[index].tipoDocumento;
        $scope.properties.selectedToModificate[0].isEliminado = $scope.properties.contenido[index].isEliminado;
        $scope.properties.selectedToModificate[0].codigo = $scope.properties.contenido[index].codigo;
        $scope.properties.selectedToModificate[0].nombreImagenHeader = $scope.properties.contenido[index].nombreImagenHeader;
        $scope.properties.selectedToModificate[0].anguloImagenHeader = $scope.properties.contenido[index].anguloImagenHeader;
        $scope.properties.selectedToModificate[0].nombreImagenFooter = $scope.properties.contenido[index].nombreImagenFooter;
        $scope.properties.selectedToModificate[0].anguloImagenFooter = $scope.properties.contenido[index].anguloImagenFooter;
        $scope.properties.isModificacion = true;
        $scope.properties.index = index;
        $scope.properties.mostrarPantallaEditar = true
        console.log($scope.properties.selectedToModificate);*/
    }


    $scope.deleteData = function(row, index) {

        if ($scope.loading === false) {
            $("#loading").modal("show");
            $scope.loading = true;
            $scope.properties.contenido[index].isEliminado = true;
            console.log(row);
            $scope.asignarTarea()
        } else {
            console.log("click doble");
        }
    }

    $scope.sendData = function(row, index) {

        if ($scope.loading === false) {
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
                $scope.properties.contenido = [];
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
    
        $scope.preview = function(codigo) {
         blockUI.start()   
        var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=generateHtml&p=0&c=10",
            data: angular.copy({
                                  "campus": $scope.properties.campusSelected,
                                  "correo": "braul5497@gmail.com",
                                  "codigo": codigo,
                                  "isEnviar": false
                                })
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.previewHtml = data.data[0]
                modalService.open("previewerid");
                $scope.$apply();


            })
            .error(function(data, status) {
                $("#loading").modal("hide");
                console.error(data)
            })
            .finally(function() {
                blockUI.stop()
            });
    }
}