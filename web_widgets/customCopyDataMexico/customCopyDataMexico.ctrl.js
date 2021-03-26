function ($scope) {
    $scope.$watch('properties.objEntrada', function () {
        if($scope.properties.objEntrada!= undefined){
            if($scope.properties.objEntrada == "Estado"){    
                $scope.properties.objSalida = "México";
                console.log("lleno");
            }
            else {
                $scope.properties.objSalida = "";
                console.log("vacio");
            }
        }
    });
}