function customGetPropedeuticosNoFecha($scope, $http, modalService) {
    
    $scope.getList = function(){
        let url = "../API/extension/AnahuacRestGet?url=getPropedeuticosNoFecha&p=0&c=10";
        var req = {
            method: "GET",
            url: url
        };
        
        return $http(req).success(function(data, status) {
            $scope.properties.propedeuticosFaltantes = data;
            if($scope.properties.propedeuticosFaltantes.data.length > 0){
                modalService.open($scope.properties.modalid);
            }
        })
        .error(function(data, status) {
             swal("Error.", data.message, "error");
           // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {
            $scope.properties.ejecuto = true;
        });
    }
    
    $scope.getList();
}