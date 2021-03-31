function PbButtonCtrl($scope, $http, $sce) {

    'use strict';
    
    $scope.content
  
    var vm = this;

    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend)
        };
  
        return $http(req).success(function(data, status) {
            debugger;
            
            $scope.content = $sce.trustAsHtml(data.data[0]);
            $scope.properties.dataFromSuccess = data;
        })
        .error(function(data, status) {
            debugger;
            $scope.properties.dataFromError = data;
        })
        .finally(function() {
            vm.busy = false;
        });
    }

    $scope.$watch("properties.dataToSend", function(){
        doRequest('POST', $scope.properties.urlPost);
    });
}