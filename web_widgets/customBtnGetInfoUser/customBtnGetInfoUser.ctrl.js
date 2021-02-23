function PbButtonCtrl($scope, $http, $window,blockUI) {

  'use strict';

  var vm = this;

    $scope.$watch("properties.userData", function (newValue, oldValue) {
        if (newValue !== undefined) {
            //if($scope.properties.lstContenido.length >1){return }
            doRequest();
        }
    });
    
    
    function doRequest() {
        blockUI.start();
        let datos = {
            "lstFiltro": [{columna: "EMAIL", operador: "Igual a", valor: $scope.properties.userData.user_name}], 
            "orderby":"",
            "orientation":"DESC",
            "limit":1,
            "offset":0
        }
        var req = {
            method: "POST",
            url: `/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100`,
            data: datos
        };
    
        return $http(req)
            .success(function (data, status) {
               $scope.properties.value = data.data;

            })
            .error(function (data, status) {
                    notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            }).finally(function () {
                
                blockUI.stop();
            });
        }
    
    
}
