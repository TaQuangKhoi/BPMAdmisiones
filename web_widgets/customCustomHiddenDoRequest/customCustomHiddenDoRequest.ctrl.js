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
            if(data.error === "fallo por Cannot invoke method size() on null object"){
                //swal("Activado", "El usuario ya se encuentra activado", "success");
                
                swal({
                    title: "Activado!",
                    text: "¡El usuario ya se encuentra activado!",
                    type: "success",
                    timer: 13000,
                    closeOnClickOutside: false,
                    buttons:false
                    });
                    setTimeout(fun, 2000);  
                    
                
            }else{
                swal("Error", data.error, "error");
            }
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
    
    
    function fun() {  
        //window.location = "http://www.youtube.com";
       window.location.assign("https://"+ location.host+"/apps/login"  )
    }  
}