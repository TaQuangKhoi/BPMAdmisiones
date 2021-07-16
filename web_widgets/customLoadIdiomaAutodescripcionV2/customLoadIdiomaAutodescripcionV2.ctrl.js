function loadContextCtrl($scope, $http) {

    $scope.objDataToPut = {
        "persistenceId_string": "",
        "catIdioma": null,
        "catTraduccion": null,
        "otroIdioma": "",
        "linkIdioma": "",
        "linkNivel": ""
    };

    $scope.loadLstIdiomaV2 = function(url) {

        doRequest("GET", url, {},
            function(data, status) { //SUCCESS
                $scope.generateLstIdiomasV2R(data);
            },
            function(data, status) { //ERROR

            })

    }

    $scope.generateLstIdiomasV2R = function(lstIdiomasV2) {
        $scope.properties.lstIdiomasV2 = [];
        for (var index in lstIdiomasV2) {
            $scope.objDataToPut["persistenceId_string"] = "";
            $scope.objDataToPut["catIdioma"] = null;
            $scope.objDataToPut["catTraduccion"] = null;
            $scope.objDataToPut["otroIdioma"] = "";
            $scope.objDataToPut["linkIdioma"] = "";
            $scope.objDataToPut["linkNivel"] = "";
            
            
            $scope.objDataToPut["persistenceId_string"] = lstIdiomasV2[index].persistenceId_string == undefined ? "" : lstIdiomasV2[index].persistenceId_string;
            $scope.objDataToPut["otroIdioma"] = lstIdiomasV2[index].otroIdioma == undefined ? "" : lstIdiomasV2[index].otroIdioma;
            
            for (var indexL in lstIdiomasV2[index].links) {
                if (lstIdiomasV2[index].links[indexL].rel === "idioma") {
                    $scope.objDataToPut["linkIdioma"] = ".."+lstIdiomasV2[index].links[indexL].href;
                } else if (lstIdiomasV2[index].links[indexL].rel === "nivel") {
                    $scope.objDataToPut["linkNivel"] = ".."+lstIdiomasV2[index].links[indexL].href;
                }
            }
            $scope.properties.lstIdiomasV2.push(angular.copy($scope.objDataToPut));
        }

        for (var indexPL in $scope.properties.lstIdiomasV2) {
            console.log("INICIO-----------------------------------------------------------------------------------------------------")

            $scope.loadInformacion($scope.properties.lstIdiomasV2[indexPL].linkIdioma, angular.copy(indexPL), "catIdioma");
            $scope.loadInformacion($scope.properties.lstIdiomasV2[indexPL].linkNivel, angular.copy(indexPL), "catTraduccion");

            console.log("FIN--------------------------------------------------------------------------------------------------------")
        }
    }

    $scope.loadInformacion = function(url, indexPL, campo) {
        doRequest("GET", url, {},
            function(data, status) { //SUCCESS
                console.log("catIdioma");
                console.log($scope.properties.lstIdiomasV2);
                $scope.properties.lstIdiomasV2[indexPL][campo] = data;
            },
            function(data, status) { //ERROR

            })
    }

    function doRequest(method, url, dataToSend, callback, errorCallback) {
        debugger;
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend)
        };

        return $http(req)
            .success(callback)
            .error(errorCallback);
    }

    $scope.$watchCollection("properties.urlGetIdioma", function(newValue, oldValue) {
        if ($scope.properties.urlGetIdioma !== undefined) {
            $scope.loadLstIdiomaV2($scope.properties.urlGetIdioma);
        }
    });
}