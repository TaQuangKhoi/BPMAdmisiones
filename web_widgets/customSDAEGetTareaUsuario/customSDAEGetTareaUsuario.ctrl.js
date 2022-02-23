function ($scope, $http) {
    
    function getCurrentTaskId(){
        $http.get($scope.properties.url).success((data)=>{
            if(data.length){
                $scope.properties.taskId = data[0].id;
                $scope.properties.caseId = data[0].caseId;
            }
        }).error((err)=>{
            swal("Error","Error al obtener las tareas asignadas al usuario. " + err,"error");
        });
    }
    
    $scope.$watch("properties.reloadTask", ()=>{
        if($scope.properties.reloadTask){
            getCurrentTaskId();
        } 
    });
    
     $scope.$watch("properties.url", ()=>{
            getCurrentTaskId();
    });
}