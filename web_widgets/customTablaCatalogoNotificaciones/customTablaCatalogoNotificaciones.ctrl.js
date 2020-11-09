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
        if($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0){
            $scope.properties.selectedIndex --; 
        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)){
            if($scope.properties.selectedIndex === 0){
                console.log("validar 0");
            }else if($scope.properties.selectedIndex === 1){
                console.log("validar 1");
            }else if($scope.properties.selectedIndex === 2){
                console.log("validar 2");
            }else if($scope.properties.selectedIndex === 3){
                console.log("validar 3");
            }else if($scope.properties.selectedIndex === 4){
                console.log("validar 4");
            }
            $scope.properties.selectedIndex ++; 
        }
    }
    
    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal() {
        modalService.close();
    }
  
    $scope.deleteData = function(row, index) {

        if ($scope.loading == false) {
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
}