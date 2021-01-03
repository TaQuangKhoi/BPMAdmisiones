function ($scope, $http) {
    $scope.$watch("properties.CatLugarExamen",function(){
        if($scope.properties.CatLugarExamen === 2){
            $scope.properties.ciudadPaisExamen = null;
            $scope.properties.catPaisExamen = null;
            $scope.properties.paisSeleccionado = "";
        }else if($scope.properties.CatLugarExamen === 3){
            $scope.properties.ciudadEstadoExamen = null
            $scope.properties.catEstadoExamen = null;
            $scope.properties.estadoSeleccionado = "";
        }
    });
    
    
}