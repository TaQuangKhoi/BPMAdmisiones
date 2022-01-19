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
                    $scope.validarPeriodo($scope.properties.catSolicitudDeAdmision.catPeriodo.fechaFin);
                }
            })
            .error(function(data, status) {
                swal("¡Error!", data.message, "error");
            })
            .finally(function() {
            
            });   
    }
    $scope.validarPeriodo=function(periodofin){
      console.log("isPeriodoVencido?")
      try{
      var fecha = new Date(periodofin.slice(0,10));
      if (fecha<new Date()){
          swal("¡Periodo vencido!", "Recuerda que el periodo de ingreso que seleccionaste ha vencido y debes actualizarlo, contacta a tu asesor o a través del chat", "warning");
      }}catch(e){
          
      }
  }
}
