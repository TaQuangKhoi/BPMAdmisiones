function ($scope, modalService, $http) {
    
    $scope.showModalLogout = function(){
        openModal($scope.properties.idModalLogout);
    }
    
    function openModal(modalId) {
        modalService.open(modalId);
    }
    
    $scope.logout = function(){
        let url = "	/bonita/logoutservice?redirect=false";
        var req = {
            method: "POST",
            url: url,
        };
  
        return $http(req).success(function(data){
            let href = window.location.protocol + "//" + window.location.host + $scope.properties.urlToRedirect;
            window.top.location = href;
        }).error(function(error){
            
        });
    }
    $.sessionTimeout({
    title: "Tu sesión ha expirado",
    message: "Su sesión está apunto de cerrarse.",
    keepAliveUrl: "/bonita/API/system/session/unusedid",
    redirUrl: "/bonita/apps/adminLogin/login/",
    logoutUrl: "/bonita/apps/adminLogin/login/",
    warnAfter: 1.44e+6,
    redirAfter: 1.5e+6,
    ignoreUserActivity: !0,
    countdownMessage: "Redireccionando en {timer} segundos.",
    ajaxType: "GET",
    keepAliveButton: "Mantenerse conectado",
    logoutButton: "Desconectarse",
    countdownBar: !0
})
}