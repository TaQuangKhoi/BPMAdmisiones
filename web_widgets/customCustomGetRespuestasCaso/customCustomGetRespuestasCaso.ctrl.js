function ($scope, $http) {
    
    $scope.getCommentList = function(){
        
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
            $scope.properties.commentList = data.data[0].caseid;
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