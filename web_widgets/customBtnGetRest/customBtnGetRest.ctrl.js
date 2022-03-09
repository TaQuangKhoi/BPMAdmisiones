function PbButtonCtrl($scope, $http, $window, blockUI) {

    'use strict';

    var vm = this;
    
    $scope.executeGet = function(){
        doRequest();
    }

    /*$scope.$watch("properties.urlInfo", function(newValue, oldValue) {
        if (newValue !== undefined) {
            //if($scope.properties.lstContenido.length >1){return }
            
        }
    });*/


    function doRequest() {

        blockUI.start();
        var req = {
            method: "GET",
            url: `/bonita/API/extension/AnahuacRestGet?url=${$scope.properties.url}&p=0&c=100`,
        };

        return $http(req)
            .success(function(data, status) {
                swal("Reintentar todo", "Ha terminado el reintento de todo", "success");
                console.log(data);
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            }).finally(function() {

                blockUI.stop();
            });
    }


}