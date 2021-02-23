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
                "tipoPeriodo":"",
                "isEliminado":false
            };
            if($scope.properties.content[index].selected){
                console.log("data")
                for(var indexID in $scope.properties.initialData){
                    if($scope.properties.initialData[indexID].catCampus.descripcion === $scope.properties.content[index].descripcion){
                        objetoRelacion.persistenceId = $scope.properties.initialData[indexID].persistenceId;
                        objetoRelacion.persistenceId_string = $scope.properties.initialData[indexID].persistenceId_string;
                        objetoRelacion.persistenceVersion = $scope.properties.initialData[indexID].persistenceVersion;
                        objetoRelacion.persistenceVersion_string = $scope.properties.initialData[indexID].persistenceVersion_string;
                    }
                }
                objetoRelacion.catCampus = $scope.properties.content[index];
                objetoRelacion.tipoPeriodo = $scope.properties.content[index].tipoPeriodoSelected;
                objetoRelacion.isEliminado = false;
                $scope.properties.dataResultado.push(objetoRelacion);
            }
        }
    }

    $scope.$watch('properties.initialData', function() {
        if ($scope.properties.initialData !== undefined) {
            for(var indexCampus in $scope.properties.content){
            	$scope.properties.content[indexCampus].selected=false;
				$scope.properties.content[indexCampus].tipoPeriodoSelected="";
            }
            for(var index in $scope.properties.initialData){
	            for(var indexCampusCa in $scope.properties.content){
					console.log($scope.properties.initialData[index].catCampus.descripcion)
					console.log($scope.properties.content[indexCampusCa].descripcion)

	            	if($scope.properties.initialData[index].catCampus.descripcion == $scope.properties.content[indexCampusCa].descripcion){
						$scope.properties.content[indexCampusCa].selected=true;
						$scope.properties.content[indexCampusCa].tipoPeriodoSelected=$scope.properties.initialData[index].tipoPeriodo;
	            	}
	            }
            }
        }
    });

}