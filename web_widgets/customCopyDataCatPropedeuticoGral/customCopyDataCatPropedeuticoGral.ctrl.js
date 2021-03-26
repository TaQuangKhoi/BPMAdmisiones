function($scope) {

    $scope.$watch('properties.getCatPropedeuticoGralByPersistenceId', function() {
        if ($scope.properties.getCatPropedeuticoGralByPersistenceId != undefined) {
            $scope.$watch('properties.getCatRelacionCampusTipo', function() {
                if ($scope.properties.getCatRelacionCampusTipo != undefined) {
                    $scope.properties.returnData={
                        "catPropedeuticoGralInput":{
                            "catRelacionCampusTipo": []
                        }
                    }

                    $scope.properties.returnData.catPropedeuticoGralInput["persistenceId"]=$scope.properties.getCatPropedeuticoGralByPersistenceId[0].persistenceId;
                    $scope.properties.returnData.catPropedeuticoGralInput["persistenceId_string"]=$scope.properties.getCatPropedeuticoGralByPersistenceId[0].persistenceId_string;
                    $scope.properties.returnData.catPropedeuticoGralInput["persistenceVersion"]=$scope.properties.getCatPropedeuticoGralByPersistenceId[0].persistenceVersion;
                    $scope.properties.returnData.catPropedeuticoGralInput["persistenceVersion_string"]=$scope.properties.getCatPropedeuticoGralByPersistenceId[0].persistenceVersion_string;
                    $scope.properties.returnData.catPropedeuticoGralInput["id"]=$scope.properties.getCatPropedeuticoGralByPersistenceId[0].id;
                    $scope.properties.returnData.catPropedeuticoGralInput["descripcion"]=$scope.properties.getCatPropedeuticoGralByPersistenceId[0].descripcion;
                    $scope.properties.returnData.catPropedeuticoGralInput["isEliminado"]=$scope.properties.getCatPropedeuticoGralByPersistenceId[0].isEliminado;
                    $scope.properties.returnData.catPropedeuticoGralInput["catRelacionCampusTipo"]=$scope.properties.getCatRelacionCampusTipo;
                    $scope.properties.returnData.catPropedeuticoGralInput["clave"]=$scope.properties.getCatPropedeuticoGralByPersistenceId[0].clave;
                    $scope.properties.returnData.catPropedeuticoGralInput["claveYear"]=parseInt($scope.properties.getCatPropedeuticoGralByPersistenceId[0].clave.substring(0, 4));
                    $scope.properties.returnData.catPropedeuticoGralInput["claveRestante"]=$scope.properties.getCatPropedeuticoGralByPersistenceId[0].clave.substring(4);
                    $scope.properties.catRelacionCampusTipoCopy = angular.copy($scope.properties.getCatRelacionCampusTipo);
                }
            });
        }
    });
}