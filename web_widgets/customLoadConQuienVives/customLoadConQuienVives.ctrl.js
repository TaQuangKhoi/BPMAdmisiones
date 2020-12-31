function($scope) {
    $scope.$watch('properties.objetoTutor', function(nuevoValor, antiguoValor) {
        if ($scope.properties.objetoTutor != undefined && $scope.properties.objetoTutor != null && $scope.properties.objetoTutor != "") {
            console.log("loadConQuienVives")
            var stringResultado = "";
            var data = {};
            for (var x in $scope.properties.objetoTutor) {
                data[$scope.properties.objetoTutor[x].catParentezco.descripcion] = $scope.properties.objetoTutor[x].viveContigo
            }
            if (data["Padre"] == true && data["Madre"] == true) {
                stringResultado = "Con mis padres";
            } else {
                if (data["Padre"]) {
                    stringResultado = "Con mi papá";
                } else if (data["Madre"]) {
                    stringResultado = "con mi mamá";
                } else if (data["Hermano (a)"]) {
                    stringResultado = "con un familiar";
                } else if (data["Esposo(a)"]) {
                    stringResultado = "con un familiar";
                } else if (data["Aval Crédito Educativo"]) {
                    stringResultado = "Otro";
                } else if (data["Sobrino (a)"]) {
                    stringResultado = "con un familiar";
                } else if (data["Tutor"]) {
                    stringResultado = "con un familiar";
                } else if (data["Abuelo (a)"]) {
                    stringResultado = "con un familiar";
                } else if (data["Otro"]) {
                    stringResultado = "Otro";
                } else {
					stringResultado = "Solo";
                }
            }

            for(var indexCQV in $scope.properties.catConQuienVives){
				if($scope.properties.catConQuienVives[indexCQV].descripcion == stringResultado){
					$scope.properties.variableDestino = $scope.properties.catConQuienVives[indexCQV];
				}
            }
        }
    })
}