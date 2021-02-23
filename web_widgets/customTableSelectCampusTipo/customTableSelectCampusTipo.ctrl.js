function PbTableCtrl($scope) {

    $scope.lstTipoPeriodo =[
        {"descripcion":"", "valor":"Seleccionar..."},
        {"descripcion":"Anual", "valor":"Anual"},
        {"descripcion":"Cuatrimestral", "valor":"Cuatrimestral"},
        {"descripcion":"Semestral", "valor":"Semestral"}
    ];

    this.isArray = Array.isArray;

    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
        }
    };

    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    $scope.seleccionarInformacion = function(row) {
        if (!row.selected) {
            row.tipoPeriodoSelected = "";
        }
        var objetoRelacion = {
            "catCampus": null,
            "tipoPeriodo":""
        };
        $scope.properties.dataResultado = [];
        for (var index in $scope.properties.content) {
            objetoRelacion = {
                "catCampus": null,
                "tipoPeriodo":""
            };
            if($scope.properties.content[index].selected){
                objetoRelacion.catCampus = $scope.properties.content[index];
                objetoRelacion.tipoPeriodo = $scope.properties.content[index].tipoPeriodoSelected;
                $scope.properties.dataResultado.push(objetoRelacion);
            }
        }
    }

    $scope.$watch('properties.cleanData', function() {
    	if ($scope.properties.cleanData !== undefined) {
	    	for(var indexCampus in $scope.properties.content){
	        	$scope.properties.content[indexCampus].selected=false;
				$scope.properties.content[indexCampus].tipoPeriodoSelected="";
	        }
	    }
    })

}