function ($scope) {
    var unregister = $scope.$watch('properties.objEntrada', function () {
        if($scope.properties.objEntrada!= undefined){
            $scope.properties.objSalida = angular.copy($scope.properties.objEntrada);
            console.log("data periodoooooooooooooooo")
            unregister();
        }
    });
}