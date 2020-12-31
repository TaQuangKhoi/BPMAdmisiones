function ($scope) {
    
    $scope.loaded = false;
    
    function buildGruposSociales(){
        if(!$scope.loaded){
            for(let i = 0; i < $scope.properties.catGrupoSocial.length; i++){
                let objGrupoSocial = {
                    "catTipoOrganizacion": $scope.properties.catGrupoSocial[i],
                    "catPertenecesOrganizacion": null,
                    "nombre": "",
                    "catAfiliacion": null,
                    "catCuantoTiempo": null,
                    "persistenceId_string": null
                };
    
                $scope.properties.organizaciones.push(objGrupoSocial);
            }
        }
    }
    
    function startWatcherOrg(){
        $scope.$watch("properties.organizaciones", function(){
            if($scope.properties.catGrupoSocial !== undefined && $scope.properties.organizaciones !== undefined){
                $scope.loaded = false;
               buildGruposSociales();
           } 
        });
    }
    
    $scope.$watch("properties.catGrupoSocial", function(){
       if($scope.properties.catGrupoSocial !== undefined){
        //   $scope.loaded = false;
        //   buildGruposSociales();
            startWatcherOrg();
       } 
    });
    // $scope.$watch("properties.organizaciones", function(){
    //     if($scope.properties.catGrupoSocial !== undefined && $scope.properties.organizaciones !== undefined){
    //         $scope.loaded = false;
    //       buildGruposSociales();
    //   } 
    // });
    
    // $scope.$watch("properties.catGrupoSocial", function(){
    //   if($scope.properties.catGrupoSocial !== undefined && $scope.properties.organizaciones !== undefined){
    //       $scope.loaded = false;
    //       buildGruposSociales();
    //   } 
    // });
}