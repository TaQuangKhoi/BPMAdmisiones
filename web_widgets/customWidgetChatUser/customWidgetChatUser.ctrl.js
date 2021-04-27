function($scope, $http) {
    $(function() {
        var req = {
            method: "POST",
            url: '../API/extension/AnahuacRest?url=getApiCrispChat&p=0&c=100',
            data: {}
        };

        return $http(req)
            .success(function(data, status) {
                
                window.$crisp = [];
                window.CRISP_WEBSITE_ID = data.data[0];
                (
                    function() {
                        d = document;
                        s = d.createElement("script");
                        s.src = "https://client.crisp.chat/l.js";
                        s.async = 1;
                        d.getElementsByTagName("head")[0].appendChild(s);
                    }
                )();

            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            }).finally(function() {

                blockUI.stop();
            });
    });

    $scope.$watch("properties.datosUsuario", function(newValue, oldValue) {
        if (newValue !== undefined) {

            $crisp.push(["set", "user:email", [`${$scope.properties.datosUsuario.correoelectronico}`]]);
            //var info=$scope.properties.datosUsuario.primernombre+" "+$scope.properties.datosUsuario.segundonombre+" - "+$scope.properties.datosUsuario.claveCampus+" - "+$scope.properties.datosUsuario.claveLicenciatura
            var info = $scope.properties.datosUsuario.primernombre + " " + $scope.properties.datosUsuario.apellidopaterno + " - " + $scope.properties.datosUsuario.campus + " - " + $scope.properties.datosUsuario.licenciatura
            $crisp.push(["set", "user:nickname", [info + ""]]);
            $crisp.push(["set", "session:data", [
                [
                    ["universidad", `${$scope.properties.datosUsuario.campus}`],
                    ["nombre_completo", `${$scope.properties.datosUsuario.primernombre+" "+$scope.properties.datosUsuario.segundonombre+" "+$scope.properties.datosUsuario.apellidopaterno+" "+$scope.properties.datosUsuario.apellidomaterno}`],
                    ["licenciatura", `${$scope.properties.datosUsuario.licenciatura}`],
                    ["estatus_solicitud", `${$scope.properties.datosUsuario.estatussolicitud}`]
                ]
            ]]);
        }
    });
}