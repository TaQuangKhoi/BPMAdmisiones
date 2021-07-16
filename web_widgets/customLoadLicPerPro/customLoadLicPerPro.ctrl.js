function ($scope) {
    $scope.$watch('properties.estatusSelected',function(nuevoValor,antiguoValor){
        if($scope.properties.estatusSelected != undefined && $scope.properties.estatusSelected != null && $scope.properties.estatusSelected != ""){
            $scope.properties.lstFiltros = [{
                "columna": "ESTATUS",
                "operador": "Igual a",
                "valor": $scope.properties.estatusSelected
            }]
        }
    })
}