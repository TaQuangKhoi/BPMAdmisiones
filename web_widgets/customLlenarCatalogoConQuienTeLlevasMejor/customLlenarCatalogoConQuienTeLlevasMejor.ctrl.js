function ($scope) {
    
    function startValidation(viveTuPadre, viveTuMadre, catConQuienTeLlevasMejor){
        let output = catConQuienTeLlevasMejor;
        
        for(let i = 0; i < output.length; i++){
            if(viveTuPadre === null && output[i].descripcion.toLowerCase().includes("papá")){
                output.splice(i, 1);
                i--;
            } else if(viveTuPadre.descripcion === "No" && output[i].descripcion.toLowerCase().includes("papá")){
                output.splice(i, 1);
                i--;
            } 
            
            if(viveTuMadre === null && output[i].descripcion.toLowerCase().includes("mamá")){
                output.splice(i, 1);
                i--;
            } else if(viveTuMadre.descripcion === "No" && output[i].descripcion.toLowerCase().includes("mamá")){
                output.splice(i, 1);
                i--;
            }
        }
        
        $scope.properties.returnCatConQuienVives = output;
    }
    
    $scope.$watch("properties.viveTuPadre", function(){
         if($scope.properties.viveTuPadre !== undefined){
             startWatcherViveTuMadre();
         }
    });
    
    function startWatcherViveTuMadre(){
        $scope.$watch("properties.viveTuMadre", function(){
            if($scope.properties.viveTuMadre !== undefined){
                startWatcherCatConQuienTeLlevasMejor();
            } 
        });
    }
    
    function startWatcherCatConQuienTeLlevasMejor(){
        $scope.$watch("properties.catConQuienTeLlevasMejor", function(){
            if($scope.properties.catConQuienTeLlevasMejor !== undefined){
                startValidation($scope.properties.viveTuPadre, $scope.properties.viveTuMadre, $scope.properties.catConQuienTeLlevasMejor);
            } 
        });
    }
}