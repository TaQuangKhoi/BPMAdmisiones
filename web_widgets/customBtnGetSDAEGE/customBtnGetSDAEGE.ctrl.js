function PbButtonCtrl($scope, $http, $window, blockUI) {

    'use strict';

    var vm = this;

   /* $scope.$watch("properties.urlInfo", function(newValue, oldValue) {
        if (newValue !== undefined) {
            //if($scope.properties.lstContenido.length >1){return }
            doRequest();
        }
    });*/
    
    $scope.$watch("properties.urlParameter", function(newValue, oldValue) {
        if (newValue !== undefined) {
            //if($scope.properties.lstContenido.length >1){return }
            $scope.properties.url.forEach( (info,index) =>{
                doRequest(info,$scope.properties.urlParameter[index]);    
            })
            
        }
    });


    function doRequest(url,parameter,index) {

        blockUI.start();
        var req = {
            method: "GET",
            url: `/bonita/API/extension/${url}&p=0&c=100${parameter}`,
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.value[index] = (data.data == null ? data: data.data);
                console.log(data);
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            }).finally(function() {

                blockUI.stop();
            });
    }


}