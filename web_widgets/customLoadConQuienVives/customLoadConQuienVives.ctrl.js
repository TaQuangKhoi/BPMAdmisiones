function($scope) {

    function getConQuienVives(){
        if ($scope.properties.objetoTutor != undefined && $scope.properties.objetoTutor != null && $scope.properties.objetoTutor != "") {
            var stringResultado = "";
            var data = {};

            if($scope.properties.objetoPadre){
                data[$scope.properties.objetoPadre.catParentezco.descripcion] = $scope.properties.objetoPadre.viveContigo;
            }
            
            if($scope.properties.objetoMadre){
                data[$scope.properties.objetoMadre.catParentezco.descripcion] = $scope.properties.objetoMadre.viveContigo;
            }
            
            for (var x in $scope.properties.objetoTutor) {
                data[$scope.properties.objetoTutor[x].catParentezco.descripcion] = $scope.properties.objetoTutor[x].viveContigo
            }
            
            if (data["Padre"] === true && data["Madre"] === true) {
                stringResultado = "Con mis padres";
            } else {
                if (data["Padre"]) {
                    stringResultado = "Con mi papá";
                } else if (data["Madre"]) {
                    stringResultado = "Con mi mamá";
                } else if (data["Hermano (a)"]) {
                    stringResultado = "Con mi familia";
                } else if (data["Esposo(a)"]) {
                    stringResultado = "Con mi familia";
                } else if (data["Aval Crédito Educativo"]) {
                    stringResultado = "Otro";
                } else if (data["Sobrino (a)"]) {
                    stringResultado = "Con mi familia";
                } else if (data["Tutor"]) {
                    stringResultado = "Con mi familia";
                } else if (data["Abuelo (a)"]) {
                    stringResultado = "Con mi familia";
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
    }

    $scope.$watch('properties.objetoTutor', function(nuevoValor, antiguoValor) {
        if($scope.properties.objetoTutor !== undefined){
            getConQuienVives();
        }
    });

    $scope.$watch('properties.objetoMadre', function(nuevoValor, antiguoValor) {
        if(nuevoValor !== undefined){
            getConQuienVives();
        }
    });

    $scope.$watch('properties.objetoPadre', function(nuevoValor, antiguoValor) {
        if(nuevoValor !== undefined){
            getConQuienVives();
        }
    });
}