function loadUsuariosDuplicadosCtrl($scope, $http) {

    $scope.loadUsuariosDuplicados = function(parametros) {
        var url = "../API/extension/AnahuacRestGet?url=getDuplicado&idbanner=[IDBANNER]&curp=[CURP]&primerNombre=[PRIMERNOMBRE]&segundoNombre=[SEGUNDONOMBRE]&apellidoPaterno=[APELLIDOPATERNO]&apellidoMaterno=[APELLIDOMATERNO]&sexo=[SEXO]&p=0&c=9999";
        //url = url.replace("[CORREOELECTRONICO]", $scope.properties.parametros["correoElectronico"]);
        //url = url.replace("[FECHANACIMIENTO]", $scope.properties.parametros["fechaNacimiento"]);
        url = url.replace("[CURP]", $scope.properties.parametros["curp"]);
        //url = url.replace("[NOMBRE]", $scope.properties.parametros["nombre"]);
        //url = url.replace("[CASEID]", $scope.properties.parametros["caseid"]);
        url = url.replace("[IDBANNER]", $scope.properties.parametros["idbanner"]);
        url = url.replace("[PRIMERNOMBRE]", $scope.properties.parametros["primerNombre"]);
        url = url.replace("[SEGUNDONOMBRE]", $scope.properties.parametros["segundoNombre"]);
        url = url.replace("[APELLIDOPATERNO]", $scope.properties.parametros["apellidoPaterno"]);
        url = url.replace("[APELLIDOMATERNO]", $scope.properties.parametros["apellidoMaterno"]);
        url = url.replace("[SEXO]", $scope.properties.parametros["sexo"]);
        console.log(url);
        doRequest("GET", url, {},
        function(data, status){//SUCCESS
            $scope.properties.getDuplicado = data;
            $scope.unregister();
        },
        function(data, status){//ERROR

        })
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

    $scope.unregister = $scope.$watch("properties.parametros", function() {
        console.log("paramtros usuarios duplicados");
        if ($scope.properties.parametros !== undefined && $scope.properties.parametros !== "") {
            $scope.loadUsuariosDuplicados($scope.properties.parametros); 
        }
    });
}