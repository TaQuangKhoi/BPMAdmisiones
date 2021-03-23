function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {
        var vm = this;
        
    $scope.$watchCollection('properties.prepaSeleccionada', function (items) {
        debugger;
        if($scope.properties.prepaSeleccionada !==undefined && $scope.properties.prepaSeleccionada !==" " && $scope.properties.prepaSeleccionada !==""){
                
                
                for (var x = 0; x < $scope.properties.bachilleratos.length; x++) {
                    if ($scope.properties.prepaSeleccionada === $scope.properties.bachilleratos[x].descripcion) {
                        if($scope.properties.clavePrepaSeleccionada !==$scope.properties.bachilleratos[x].clave){
                            $scope.properties.completeObject.nombreBachillerato = $scope.properties.bachilleratos[x].descripcion;
                            $scope.properties.completeObject.paisBachillerato   = $scope.properties.bachilleratos[x].pais;
                            $scope.properties.completeObject.estadoBachillerato = $scope.properties.bachilleratos[x].estado;
                            $scope.properties.completeObject.ciudadBachillerato = $scope.properties.bachilleratos[x].ciudad;
                            $scope.properties.objFinal = $scope.properties.bachilleratos[x];
                            $scope.properties.clavePrepaSeleccionada = $scope.properties.bachilleratos[x].clave;
                        }
                        break;
                    }
                }
            
        }
    });
    
    $scope.$watchCollection('properties.clavePrepaSeleccionada', function (items) {
        debugger;
        if($scope.properties.clavePrepaSeleccionada !==undefined && $scope.properties.clavePrepaSeleccionada !==" " && $scope.properties.clavePrepaSeleccionada !==""){
            for (var x = 0; x < $scope.properties.bachilleratos.length; x++) {
                if ($scope.properties.clavePrepaSeleccionada === $scope.properties.bachilleratos[x].clave) {
                    if($scope.properties.prepaSeleccionada!==$scope.properties.bachilleratos[x].descripcion){
                        $scope.properties.completeObject.nombreBachillerato = $scope.properties.bachilleratos[x].descripcion;
                        $scope.properties.completeObject.paisBachillerato   = $scope.properties.bachilleratos[x].pais;
                        $scope.properties.completeObject.estadoBachillerato = $scope.properties.bachilleratos[x].estado;
                        $scope.properties.completeObject.ciudadBachillerato = $scope.properties.bachilleratos[x].ciudad;
                        $scope.properties.objFinal = $scope.properties.bachilleratos[x];
                        $scope.properties.prepaSeleccionada = $scope.properties.bachilleratos[x].descripcion;
                    }  
                    break;
                }
            }
            
        }
    });




    
}