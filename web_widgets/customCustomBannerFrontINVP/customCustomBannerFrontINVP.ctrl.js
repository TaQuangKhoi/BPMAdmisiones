function ($scope, modalService, $http) {
    
    $scope.showModalLogout = function(){
        openModal($scope.properties.idModalLogout);
    }
    
    function openModal(modalId) {
        modalService.open(modalId);
    }
    
    $scope.action = function(){
        $scope.properties.idioma = localStorage.getItem("idioma");
        if($scope.properties.pageToken === "examen"){
            $scope.properties.accionModal = "logout";
            modalService.open($scope.properties.idModalLogout);
        } else {
            $scope.logout();
        }
    };
    
    $scope.logout = function(){
        let url = "	/bonita/logoutservice?redirect=false";
        var req = {
            method: "POST",
            url: url,
        };
  
        return $http(req).success(function(data){
            if($scope.properties.pageToken === "termino"){
                window.top.location.href = "/bonita/apps/login/admisiones/";
            }else{
                let href = window.location.protocol + "//" + window.location.host + $scope.properties.urlToRedirect;
                window.top.location = href;
            }

        }).error(function(error){
            
        });
    }
    $.sessionTimeout({
        title: "Tu sesión ha expirado",
        message: "Su sesión está apunto de cerrarse.",
        keepAliveUrl: "/bonita/API/system/session/unusedid",
        redirUrl: (window.location.href.includes("aspirante"))?"/bonita/apps/login/admisiones/":"/bonita/apps/login/administrativo/",
        logoutUrl: (window.location.href.includes("aspirante"))?"/bonita/apps/login/admisiones/":"/bonita/apps/login/administrativo/",
        warnAfter: 7.08e+6,
        redirAfter: 7.14e+6,
        ignoreUserActivity: !0,
        countdownMessage: "Redireccionando en {timer} segundos.",
        ajaxType: "GET",
        keepAliveButton: "Mantenerse conectado",
        logoutButton: "Desconectarse",
        countdownBar: !0
    })
    
    $scope.$watch("properties.cerrarSesion", function(){
        
        if($scope.properties.pageToken === "examen" || $scope.properties.pageToken === "presentar"){
            //window.top.location.href = "/bonita/apps/invplogin/login/";    
            updateterminado();
        }else if($scope.properties.pageToken === "termino"){
            $scope.properties.cerrarSesion = false;
            modalService.close();
            $scope.logout();
        }else if($scope.properties.cerrarSesion === true){
            $scope.properties.cerrarSesion = false;
            modalService.close();
            $scope.logout();
        }
    })
    
    $scope.msj = "";
    $scope.quitar = false;
    
    $scope.$watch("properties.pageToken", function(){
        $scope.properties.idioma = localStorage.getItem("idioma");
        if($scope.properties.pageToken === "presentar"){
            $scope.msj = "Terminar / Finish";
            $scope.quitar = true;
        }else if($scope.properties.pageToken === "examen"){
            if($scope.properties.idioma === "ESP"){
                $scope.msj = "Terminar";    
            }else if($scope.properties.idioma === "ENG"){
                $scope.msj = "Finish";  
            }
            $scope.quitar = false;
        }else if($scope.properties.pageToken === "termino"){
            $scope.msj = "Ir a Admisiones / Go to Admisions";
            $scope.quitar = true;
        }
    })
    
 function updateterminado() {
        debugger;

        var data = {
            "terminado": true,
            "username": $scope.properties.userData.user_name
        }

        var req = {
            method: "POST",
            url: "../API/extension/AnahuacINVPRestAPI?url=updateterminado&p=0&c=10",
            data: data
        };

        return $http(req)
            .success(function(data, status) {
               window.top.location.href = "/bonita/apps/aspiranteinvp/termino/";
            })
            .error(function(data, status) {
               
            })
            .finally(function() {

            });
    }
}