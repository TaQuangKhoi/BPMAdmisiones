function ($scope, $http) {
    
    $scope.$watch("properties.valoresSolicitante", function(){
        if($scope.properties.valoresSolicitante !== undefined){
            debugger;
            for(var x=0;x<$scope.properties.catCampus.length;x++){
                if($scope.properties.valoresSolicitante.campus === $scope.properties.catCampus[x].descripcion){
                    $scope.properties.valoresSolicitante.catCampus = $scope.properties.catCampus[x];
                }
            }
            for(var l=0;l<$scope.properties.radioOptionsTestPlace.length;l++){
                if($scope.properties.valoresSolicitante.lugarexamen.toUpperCase() === $scope.properties.radioOptionsTestPlace[l][0].displayName.toUpperCase()){
                    $scope.properties.testOptionsRadio = $scope.properties.radioOptionsTestPlace[l][0].value;
                    if($scope.properties.valoresSolicitante.lugarexamen.toUpperCase() === "EN UN ESTADO"){
                        for(var e=0;e<$scope.properties.estadosRUA.length;e++){
                            if($scope.properties.valoresSolicitante.estadoexamen === $scope.properties.estadosRUA[e]){
                                $scope.properties.estadoSeleccionado = $scope.properties.estadosRUA[e];    
                            }
                        }
                    }else if($scope.properties.valoresSolicitante.lugarexamen.toUpperCase() === "EN EL EXTRANJERO (SOLO SI VIVES FUERA DE MÉXICO)"){
                        for(var e=0;e<$scope.properties.paisesRUA.length;e++){
                            if($scope.properties.valoresSolicitante.ciudadpais === $scope.properties.paisesRUA[e]){
                                $scope.properties.paisSeleccionado = $scope.properties.paisesRUA[e];    
                            }
                        }
                    }
                }
            }
        }
    });
    
    $scope.$watch("properties.catLicenciatura", function(){
        if($scope.properties.catLicenciatura !== undefined){
            debugger;
            for(var x=0;x<$scope.properties.catLicenciatura.length;x++){
                if($scope.properties.valoresSolicitante.licenciatura === $scope.properties.catLicenciatura[x].nombre){
                    $scope.properties.valoresSolicitante.catLicenciatura = $scope.properties.catLicenciatura[x];
                    for(var y=0;y<$scope.properties.valoresSolicitante.catLicenciatura.periodoDisponible.length;y++){
                        if($scope.properties.valoresSolicitante.ingreso === $scope.properties.valoresSolicitante.catLicenciatura.periodoDisponible[y].descripcion){
                            $scope.properties.valoresSolicitante.periodo = $scope.properties.valoresSolicitante.catLicenciatura.periodoDisponible[y];
                        }
                        
                    }
                }
            }
        }
    });
    
    $scope.$watch("properties.catCiudadExamen", function(){
        if($scope.properties.catCiudadExamen !== undefined){
            for(var x=0;x<$scope.properties.catCiudadExamen.length;x++){
                if($scope.properties.valoresSolicitante.ciudadestado === $scope.properties.catCiudadExamen[x].descripcion){
                    $scope.properties.valoresSolicitante.ciudadExamen = $scope.properties.catCiudadExamen[x];
                }   
            }
        }
    });
    
    $scope.$watch("properties.catPaisExamen", function(){
        if($scope.properties.catPaisExamen !== undefined){
            for(var x=0;x<$scope.properties.catCiudadPais.length;x++){
                if($scope.properties.valoresSolicitante.ciudadpais === $scope.properties.catCiudadPais[x].descripcion){
                    $scope.properties.valoresSolicitante.paisExamen = $scope.properties.catCiudadPais[x];
                }   
            }
        }
    });
}