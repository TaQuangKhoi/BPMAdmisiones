function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    $scope.action = function(){
        if($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0){
            $scope.properties.selectedIndex --; 
        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)){

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
                    $scope.properties.selectedIndex ++; 
                }
            })
            .error(function(data, status) {
                swal("¡Error!", data.message, "error");
            })
            .finally(function() {
            
            });       
        }
    }
}
