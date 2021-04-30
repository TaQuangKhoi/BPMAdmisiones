function PbTableCtrl($scope, $http) {

    this.isArray = Array.isArray;

    this.selectRow = function(row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
        }
    };

    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    $scope.loadData = function(){
        var req = {
            method: "GET",
            url: $scope.properties.urlUsuario.replace("[USUARIO]", $scope.properties.usuario)
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.contenido=data;
                $scope.loadMembership();
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            }).finally(function() {

                blockUI.stop();
            });
    }

    $scope.loadMembership = function(){
        console.log($scope.properties.urlMembresia.replace("[USERID]", $scope.properties.contenido[0].id))
        var req = {
            method: "GET",
            url: $scope.properties.urlMembresia.replace("[USERID]", $scope.properties.contenido[0].id)
        };
        return $http(req)
            .success(function(data, status) {
                $scope.properties.contenido[0].lstMembership = data;
            })
            .error(function(data, status) {
                console.log(data)
                console.log(status)
            }).finally(function() {

            });
    }

    $scope.borrarUsuario = function(row){
        var req = {
            method: "DELETE",
            url: "../API/identity/user/[USERID]".replace("[USERID]", row.id)
        };
        return $http(req)
            .success(function(data, status) {
                $scope.properties.usuario = "";
                $scope.properties.contenido=[];
            })
            .error(function(data, status) {
                console.log(data)
                console.log(status)
            }).finally(function() {

            });
    }
    
    $scope.$watch("properties.usuario", function() {
        if($scope.properties.usuario !== undefined && $scope.properties.usuario !== ""){
            $scope.loadData();
        }
    })
}