function ($scope, $http) {
    
    function getCurrentTask(){
        //let url = "../API/bpm/humanTask?p=0&c=10&f=caseId=" + $scope.properties.caseId +"&fstate=ready";
        let url = "../API/bpm/archivedManualTask?p=0&c=10&f=caseId=" + $scope.properties.caseId;
        
        var req = {
            method: "GET",
            url: url
        };

        return $http(req)
        .success(function(data, status) {
            // let message = "SE CAMBIO EL ID DE LA TAREA, EL VIEJO ES " 
            // + $scope.properties.taskId 
            // + " EL NUEVO ES "
            // + data[0].id;
            // swal("OK", message, "success");
            //$scope.properties.taskId = data[0].id;
            $scope.properties.taskId = data[data.length - 1].id;
        })
        .error(function(data, status) {
            // swal("¡Error!","NO se ha podido obtener los datos de la tarea.","error");
        })
        .finally(function() {
            // vm.busy = false;
        });
    }
    
    $scope.$watch("properties.caseId", function(){
        if($scope.properties.caseId !== undefined){
            getCurrentTask();
        }
    });
    
    $scope.$watch("properties.reloadTask", function(){
        if($scope.properties.reloadTask === true || $scope.properties.reloadTask === "true" ){
            $scope.properties.reloadTask = false;
            getCurrentTask();
        }
    });
}