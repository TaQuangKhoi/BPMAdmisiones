function ($scope, $http) {
    function doRequest(method, url, params) {
        var req = {
            method: "GET",
            url: $scope.properties.url,
            data: {}
        };

        return $http(req).success(function(data, status) {
            $scope.properties.successResponseValue = data;
        })
        .error(function(data, status) {
            $scope.properties.errorResponseValue = data;
            swal("Error", data.error, "error");
        })
        .finally(function() {
            vm.busy = false;
            $scope.hideLoading();
        });
    }
    
    $scope.$watch("properties.url", function(){
        if($scope.properties.url !== undefined && $scope.properties.url !== ""){
            doRequest();
        } 
    });
}