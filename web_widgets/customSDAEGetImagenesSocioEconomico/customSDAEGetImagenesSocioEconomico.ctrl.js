function ($scope, $http) {
    
    var urlCargada = false;
    var objetoCargado = false;

    function getImagenesSocioeconomico() {
        debugger;
        $http.post($scope.properties.url, $scope.properties.dataToSend).success((data) => {
            debugger;
            $scope.properties.imagenesSocioEconomico = data;
        }).error((err) => {
            swal("Error", "Error al obtener el model. " + err, "error");
        });
    }

    $scope.$watch("properties.url", ()=>{
        if($scope.properties.url){
            urlCargada = true;

            if(urlCargada && objetoCargado){
                debugger;
                getImagenesSocioeconomico();
            }
        }
    });

    $scope.$watch("properties.dataToSend", ()=>{
        if($scope.properties.dataToSend){
            objetoCargado = true;

            if(urlCargada && objetoCargado){
                debugger;
                getImagenesSocioeconomico();
            }
        }
    });
}