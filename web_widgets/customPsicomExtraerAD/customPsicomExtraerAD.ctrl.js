function ($scope, $http) {
    function initExtraction(){
        let links = $scope.properties.bdmAutodescripcion.links;

        for(let i = 0; i < links.length; i++){
            if(links[i].rel === "catAreaBachillerato"){
                getBDM(links[i].href, links[i].rel);
            } else if(links[i].rel === "catVivesEstadoDiscapacidad"){
                getBDM(links[i].href, links[i].rel);
            } else if(links[i].rel === "catProblemaSaludAtencionContinua"){
                getBDM(links[i].href, links[i].rel);
            } else if(links[i].rel === "discapacidades"){
                getBDM(links[i].href, links[i].rel);
            } else if(links[i].rel === "problemasSalud"){
                getBDM(links[i].href, links[i].rel);
            } else if(links[i].rel === "catReligion"){
                getBDM(links[i].href, links[i].rel);
            } else if(links[i].rel === "catAreaLaboralDeInteres"){
                getBDM(links[i].href, links[i].rel);
            } else if(links[i].rel === "informacionCarrera"){
                getBDM(links[i].href, links[i].rel);
            } else if(links[i].rel === "sacramentos"){
                getBDM(links[i].href, links[i].rel);
            } else if(links[i].rel === "catRequieresAsistencia"){
                getBDM(links[i].href, links[i].rel);
            } else if(links[i].rel === "tipoAsistencia"){
                getBDM(links[i].href, links[i].rel);
            }
        }
    }

    function getBDM(_href, _rel){
        $http.get(_href).success(function(data){
            $scope.properties.bdmAutodescripcionExtraido[_rel] = data;
        }).error(function(){

        });
    }

    $scope.$watch("properties.bdmAutodescripcion", function(){
        if($scope.properties.bdmAutodescripcion !== undefined){
            initExtraction();
        }
    })
}