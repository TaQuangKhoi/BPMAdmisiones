function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

 
$scope.sendMail = function (row) {
if($scope.properties.mensaje==="" || $scope.properties.mensaje===" " || $scope.properties.mensaje==="  " || $scope.properties.mensaje===null || $scope.properties.mensaje===undefined){
//if(false){
  
     Swal.fire("¡Aviso!", "El correo no debe ir vacío", "warning");
}else{

    var req = {
        method: "POST",
        url: "/bonita/API/extension/AnahuacRest?url=generateHtml&p=0&c=10",
        data: angular.copy({
            "campus": row.catCampus.grupoBonita,
            "correo": row.correoElectronico,
            "codigo": "recordatorio",
            "isEnviar": true,
            "mensaje": $scope.properties.mensaje
        })
    };

    return $http(req)
        .success(function (data, status) {
            Swal.fire("¡Correo enviado correctamente!","","success")
            $scope.properties.mensaje=""
            modalService.close();
           // $scope.envelopeCancel();
        })
        .error(function (data, status) {
            console.error(data)
        })
        .finally(function () { });
}      
}
}
