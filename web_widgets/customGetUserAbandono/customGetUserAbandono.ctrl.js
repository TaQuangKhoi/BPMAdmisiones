function getUserAbandono($scope, $http, $window) {
    $scope.$watch("properties.datosUsuario", function() {
        if ($scope.properties.datosUsuario !== undefined) {
            var req = {
                method: "GET",
                url: "../API/bdm/businessData/com.anahuac.catalogos.CatRegistro?q=findByCorreoelectronico&f=correoelectronico=" + $scope.properties.datosUsuario.user_name + "&p=0&c=500"
            };
            return $http(req)
                .success(function(data, status) {
                    if(data.length === 0){
                       $scope.getSolicitudAbandonada();
                    }else{
                        $scope.getCurrentTask(data[0].caseId);
                    }
                    
                })
                .error(function(data, status) {
                    console.error(data);
                });
        }

    });

    $scope.getSolicitudAbandonada = function(){
         var req = {
            method: "GET",
            url: "../API/bdm/businessData/com.anahuac.model.SolicitudDeAdmision?q=findByCorreoElectronico&f=correoElectronico=" + $scope.properties.datosUsuario.user_name + "(ABANDONADO)&p=0&c=10"
        };
        return $http(req)
            .success(function(data, status) {
                $scope.getTaskAbandono(data[0].caseId);
            })
            .error(function(data, status) {
                console.error(data);
            });
    }

    $scope.getCurrentTask = function(caseid) {
        var req = {
            method: "GET",
            url: "../API/bpm/humanTask?p=0&c=10&f=caseId=" + caseid + "&fstate=ready"
        };
        return $http(req)
            .success(function(data, status) {
                if(data[0].name === "Modificar información"){
                    $scope.ipBonita = window.location.protocol + "//" + window.location.host + "/bonita";
                    $scope.url = ipBonita + "/portal/resource/app/aspirante/modificacion_iniciada/content/?app=aspirante";
                }else {
                    $scope.url = ipBonita + "/portal/resource/app/aspirante/solicitud_iniciada/content/?app=aspirante";
                }
                
                if($data.currentTask[0].name !== "Llenar solicitud"){
                    window.location.href = $scope.url;
                }
            })
            .error(function(data, status) {
                console.error(data);
            });
    }

    $scope.getTaskAbandono = function(caseid) {
        var req = {
            method: "GET",
            url: "../API/bpm/humanTask?p=0&c=10&f=caseId=" + caseid + "&fstate=ready"
        };
        return $http(req)
            .success(function(data, status) {
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