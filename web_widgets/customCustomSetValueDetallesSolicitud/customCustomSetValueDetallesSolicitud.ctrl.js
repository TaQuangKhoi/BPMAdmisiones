function customSetValueDetallesSolicitud($scope, $http) {
    $scope.$watch("properties.formOutput", function() {
        if ($scope.properties.formOutput !== undefined && $scope.properties.objDetalleSolicitud !== undefined && $scope.properties.urlTipoAlumno !== undefined && $scope.properties.urlResidencia !== undefined && $scope.properties.urlTipoAdmision !== undefined) {
            debugger;
            $scope.properties.isCurpValidado = $scope.properties.objDetalleSolicitud.isCurpValidado;
            $scope.properties.descuento = $scope.properties.objDetalleSolicitud.descuento;
            if($scope.properties.objDetalleSolicitud.promedioCoincide !== null){
                $scope.properties.formOutput.detalleSolicitudInput.promedioCoincide = $scope.properties.objDetalleSolicitud.promedioCoincide;    
            }
            if($scope.properties.objDetalleSolicitud.cbCoincide !== null){
                $scope.properties.formOutput.detalleSolicitudInput.cbCoincide = $scope.properties.objDetalleSolicitud.cbCoincide;    
            }
            if($scope.properties.objDetalleSolicitud.revisado !== null){
                $scope.properties.formOutput.detalleSolicitudInput.revisado = $scope.properties.objDetalleSolicitud.revisado;    
            }
            $scope.properties.formOutput.detalleSolicitudInput.idBanner = $scope.properties.objDetalleSolicitud.idBanner;
            
            
            $scope.gettipoAlumno();
        }
    });

    $scope.gettipoAlumno = function() {
        var req = {
            method: "GET",
            url: ".." + $scope.properties.urlTipoAlumno
        };
        return $http(req)
            .success(function(data, status) {
                debugger;
                $scope.properties.formOutput.detalleSolicitudInput.catTipoAlumno = data;
                $scope.getResidencia();
            })
            .error(function(data, status) {
                console.error(data);
            });
    }

    $scope.getResidencia = function() {
        var req = {
            method: "GET",
            url: ".." + $scope.properties.urlResidencia
        };
        return $http(req)
            .success(function(data, status) {
                debugger;
                $scope.properties.formOutput.detalleSolicitudInput.catResidencia = data;
                $scope.gettipoAdmision();
            })
            .error(function(data, status) {
                console.error(data);
            });
    }

    $scope.gettipoAdmision = function() {
        var req = {
            method: "GET",
            url: ".." + $scope.properties.urlTipoAdmision
        };
        return $http(req)
            .success(function(data, status) {
                debugger;
                $scope.properties.formOutput.detalleSolicitudInput.catTipoAdmision = data;
                $scope.properties.tieneDatos = true;
            })
            .error(function(data, status) {
                console.error(data);
            });
    }

}