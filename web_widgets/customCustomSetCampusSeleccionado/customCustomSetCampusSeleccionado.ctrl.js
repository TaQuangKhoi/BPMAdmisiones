function($scope, $http) {
    $scope.llenoestado = false;
    $scope.llenovalororiginal = false;
    $scope.ciudadExamenSelected;
    $scope.estadoExamenSelected;

    var unregister = $scope.$watch("$properties.campusSelected", function() {

        if ($scope.properties.campusSelected !== null && $scope.properties.campusSelected !== undefined) {
            for (var x = 0; x < $scope.properties.getCatCampus.length; x++) {
                if ($scope.properties.getCatCampus[x].persistenceId === $scope.properties.campusSelected) {
                    $scope.properties.catSolicitudDeAdmision.catCampus = $scope.properties.getCatCampus[x];
                    unregister();
                }
            }

            /*if($scope.properties.catSolicitudDeAdmision.ciudadExamenPais !== null && $scope.properties.catSolicitudDeAdmision.ciudadExamenPais !== undefined){
                if(!$scope.llenovalororiginal){
                    $scope.ciudadExamenSelected = $scope.properties.catSolicitudDeAdmision.ciudadExamenPais;
                    $scope.llenovalororiginal = true;
                    $scope.properties.paisSeleccionado = $scope.properties.catSolicitudDeAdmision.catPaisExamen.descripcion;
                    unregister();
                }
            }  */
        }
    });

    /* var unregister2 = $scope.$watch("properties.getCiudadesByPais", function(){
         if($scope.properties.getCiudadesByPais !== null && $scope.properties.getCiudadesByPais !== undefined){
             if($scope.properties.getCiudadesByPais.length > 0){
                 if($scope.llenovalororiginal){
                     $scope.properties.catSolicitudDeAdmision.ciudadExamen = $scope.ciudadExamenSelected;
                     unregister2();
                 }
             }   
         }
     });*/
}