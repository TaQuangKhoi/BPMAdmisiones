function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    $scope.changeVariable = function(){
        var req = {
                method: "GET",
                url: "../API/system/session/unusedid",
            };
            return $http(req).success(function(data, status) {
                let url = "../API/bpm/userTask/" + $scope.properties.taskId;
                if($scope.properties.catSolicitudDeAdmision.correoElectronico != data.user_name){
                    swal("¡Error!", "Su sesion ha expirado", "warning");   
                    setTimeout(function(){ window.top.location.href = $scope.properties.urlDireccion }, 3000);
                }else{
                    $scope.properties.navigationVar = $scope.properties.newValue;
                }
            })
            .error(function(data, status) {
                swal("¡Error!", data.message, "error");
            })
            .finally(function() {
            
            });   
    }
}
