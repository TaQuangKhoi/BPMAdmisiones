function ($scope) {
    
    $scope.loaded = false;
    
    function buildGruposSociales(){
        // if(!$scope.loaded){
        
            if($scope.properties.content === undefined || $scope.properties.content === null){
                $scope.properties.content = [];
            }
            for(let i = 0; i < $scope.properties.catGrupoSocial.length; i++){
                let objGrupoSocial = {
                    "catTipoOrganizacion": $scope.properties.catGrupoSocial[i],
                    "catPertenecesOrganizacion": null,
                    "nombre": "",
                    "catAfiliacion": null,
                    "catCuantoTiempo": null,
                    "persistenceId_string": null
                };
                
                $scope.properties.content.push(objGrupoSocial);
            }
        // }
    }
    
    function startWatcherOrg(){
        $scope.$watch("properties.organizaciones", function(){
            if($scope.properties.organizaciones !== undefined){
                if($scope.properties.organizaciones.length > 0){
                    $scope.properties.content = $scope.properties.organizaciones;
                } else {
                    buildGruposSociales();
                }
            } else {
                if($scope.properties.content !== undefined){
                    if($scope.properties.content.length === 0){
                        buildGruposSociales();
                    }
                } else {
                    buildGruposSociales();
                }
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
    
    $scope.$watch("properties.content", function(oldVal, newVal){
       console.log(oldVal, newVal);
       if($scope.properties.content === undefined){
          buildGruposSociales(); 
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