function ($scope) {
    $scope.$watch('properties.valor',function(nuevoValor,antiguoValor){
        if($scope.properties.valor != undefined && $scope.properties.valor != null && $scope.properties.valor != ""){
            $scope.properties.variableDestino = $scope.properties.valor
        }
    })
}