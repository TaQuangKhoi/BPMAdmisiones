function ($scope) {
    
    $scope.$watchCollection("[properties.lstCatalogoDocumentos, properties.bdmDocumentos]",()=>{
        debugger;
        if(!$scope.properties.bdmDocumentos && $scope.properties.lstCatalogoDocumentos){
            $scope.properties.bdmDocumentos = $scope.properties.lstCatalogoDocumentos;
            $scope.properties.lstDocumentosBDM = $scope.properties.lstCatalogoDocumentos
        } else {
            $scope.properties.lstDocumentosBDM = $scope.properties.bdmDocumentos;
        }
    });
}