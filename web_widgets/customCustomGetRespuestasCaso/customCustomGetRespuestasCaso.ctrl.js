function ($scope, $http) {
    
    $scope.getCommentList = function(){
        debugger
        let data = {
            "idusuario":$scope.properties.reload
        }
        let url = "../API/extension/AnahuacINVPRestAPI?url=getCaseIdbyuserid&p=0&c=10";
        var req = {
            method: "POST",
            url: url,
            data:data
        };
        
        return $http(req).success(function(data, status) {
            debugger
            $scope.properties.commentList = data.data[0].caseid;
            var termino = localStorage.getItem("terminado");
            if(termino === "true"){
                window.top.location.href = "/bonita/apps/login/testinvp/";
            }
        })
        .error(function(data, status) {
             swal("Error.", data.message, "error");
           // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {
            
        });
    }
    
   // $scope.getCommentList();
    
    $scope.$watch("properties.reload", function(){
        if($scope.properties.reload !== undefined){
             $scope.getCommentList();
        }
        /*if(($scope.properties.reload === undefined || $scope.properties.reload.length === 0) && $scope.properties.campusSeleccionado !== undefined){
            $("#loading").modal("show");
        }else{
            $("#loading").modal("hide");
        }*/
    });
}