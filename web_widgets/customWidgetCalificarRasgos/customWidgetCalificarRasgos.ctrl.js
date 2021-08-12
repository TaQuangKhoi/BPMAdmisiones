function($scope) {

    $scope.$watchCollection("[properties.rasgosBDM, properties.catRasgosObservados]", function() {
        if ($scope.properties.rasgosBDM !== undefined) {
            // $scope.properties.content.splice(0, $scope.properties.content.length);
            $scope.properties.content = [];
            $scope.properties.content = $scope.properties.rasgosBDM;

            for (let rasgo in $scope.properties.rasgosBDM) {
                // $scope.properties.content.push($scope.properties.rasgosBDM[rasgo]);
                let rasgoId = $scope.properties.rasgosBDM[rasgo].rasgo.persistenceId_string;
                let califId = $scope.properties.rasgosBDM[rasgo].calificacion.persistenceId_string;
                let classToClick = ".calif-" + rasgoId + "-" + califId;

                setTimeout(() => {
                    $(classToClick).trigger("click");
                }, 1000);
            }


        } else if ($scope.properties.catRasgosObservados !== undefined) {
            // $scope.properties.content.splice(0, $scope.properties.content.length);
            $scope.properties.content = [];

            for (let rasgo in $scope.properties.catRasgosObservados) {
                let rasgoCompleto = {
                    "rasgo": $scope.properties.catRasgosObservados[rasgo],
                    "calificacion": null
                }

                $scope.properties.content.push(rasgoCompleto);
            }
        }
    });
}