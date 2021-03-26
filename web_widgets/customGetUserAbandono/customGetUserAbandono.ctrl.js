function getUserAbandono($scope, $http, $window) {
    $scope.$watch("properties.datosUsuario", function() {
        debugger;
        if ($scope.properties.datosUsuario !== undefined) {
            var req = {
                method: "GET",
                url: "../API/bdm/businessData/com.anahuac.model.SolicitudDeAdmision?q=findByCorreoElectronico&f=correoElectronico=" + $scope.properties.datosUsuario.user_name + "(ABANDONADO)&p=0&c=10"
            };
            return $http(req)
                .success(function(data, status) {
                    debugger;
                    $scope.getTask(data[0].caseId);
                })
                .error(function(data, status) {
                    console.error(data);
                });
        }

    });

    $scope.getTask = function(caseid) {
        debugger;
        var req = {
            method: "GET",
            url: "../API/bpm/humanTask?p=0&c=10&f=caseId=" + caseid + "&fstate=ready"
        };
        return $http(req)
            .success(function(data, status) {
                debugger;
                if (data.length === 0) {
                    $scope.ipBonita = window.location.protocol + "//" + window.location.host + "/bonita";
                    $scope.url = $scope.ipBonita + "/portal/resource/app/aspirante/solicitud_caducada/content/?app=aspirante";
                    window.location.href = $scope.url;
                }
            })
            .error(function(data, status) {
                console.error(data);
            });
    }
}