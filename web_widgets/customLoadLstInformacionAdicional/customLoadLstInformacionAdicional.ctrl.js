function loadLstInformacionAdicionalCtrl($scope, $http) {

    $scope.objInformacionEscolarInput = {
        "persistenceId_string": "",
        "grado": null,
        "tipo": null,
        "escuela": null,
        "otraEscuela": "",
        "pais": null,
        "estado": null,
        "ciudad": "",
        "anoInicio": "",
        "anoFin": "",
        "promedio": "",
        "estadoString": ""
    };

    $scope.loadListData = function() {
        console.log("loadListData---------------------------------------------------------------------------------------")
        var existe = false;
        if ($scope.properties.catBachillerato !== undefined) {
            $scope.properties.lstInformacionEscolarMod = [];
            for (var index in $scope.properties.lstInformacionEscolarInput) {
                if ($scope.properties.lstInformacionEscolarInput[index].escuela.descripcion == $scope.properties.catBachillerato.descripcion) {
                    existe = true;
                }
            }

            if (existe) {
                $scope.properties.lstInformacionEscolarMod = angular.copy($scope.properties.lstInformacionEscolarInput);
            } else {
                $scope.objInformacionEscolarInput = {
                    "persistenceId_string": "",
                    "grado": null,
                    "tipo": null,
                    "escuela": null,
                    "otraEscuela": "",
                    "pais": null,
                    "estado": null,
                    "ciudad": "",
                    "anoInicio": "",
                    "anoFin": "",
                    "promedio": "",
                    "estadoString": ""
                };

                $scope.objInformacionEscolarInput.escuela = angular.copy($scope.properties.catBachillerato);
                $scope.objInformacionEscolarInput.promedio = angular.copy($scope.properties.promedioGeneral);
                $scope.objInformacionEscolarInput.grado = angular.copy($scope.properties.catGradoAcademico);
                $scope.objInformacionEscolarInput.tipo = angular.copy($scope.properties.catGradoAcademico);

                $scope.properties.lstInformacionEscolarMod.push($scope.objInformacionEscolarInput)
            }
        }
    }

    function doRequest(method, url, dataToSend, callback, errorCallback) {

        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend)
        };

        return $http(req)
            .success(callback)
            .error(errorCallback);
    }

    $scope.$watchCollection("properties.lstInformacionEscolarInput", function(newValue, oldValue) {
        $scope.loadListData();
    });

    $scope.$watchCollection("properties.catBachillerato", function(newValue, oldValue) {
        if ($scope.properties.catBachillerato !== undefined) {
            $scope.loadListData();
        }
    });
}