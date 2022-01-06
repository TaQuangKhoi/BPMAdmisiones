function PbButtonCtrl($scope, $http,  modalService) {
    
    function postEditar() {
          blockUI.start();
          var req = {
              method: "POST",
              url: "/bonita/API/extension/AnahuacRest?url=updateViewDownloadSolicitud&p=0&c=100",
              data: angular.copy($scope.properties.datosAspirante),
          };
  
          return $http(req)
              .success(function (data, status) {
                  actualizacion_de_datos();
              })
              .error(function (data, status) {
              })
              .finally(function () {
                  blockUI.stop();
              });
      }
}
