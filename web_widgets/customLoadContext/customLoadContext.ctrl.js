function loadContextCtrl($scope, $http) {
    $scope.lstArchivedCase = [];

    $scope.loadContextTask = function(taskId) {
        //doRequest("GET", "../API/bpm/userTask/"+taskId+"/context", {},
        let task;
        doRequest("GET", "../API/bpm/archivedManualTask?p=0&c=10&f=caseId="+$scope.properties.caseId, {},
        function(data, status){//SUCCESS
            task = data;
            
            doRequest("GET", `../API/bpm/archivedUserTask/${task[task.length-1].id}/context`, {},
                function(data, status){//SUCCESS
                    $scope.properties.context = data;
                },
                function(data, status){//ERROR
                })
        },
        function(data, status){//ERROR

        });
        
        
    }

    $scope.loadArchivedCase = function(caseId) {
        if ($scope.properties.taskId === undefined || $scope.properties.taskId === "") {
            doRequest("GET", "../API/bpm/archivedCase?c=1&p=0&f=sourceObjectId="+caseId, {},
            function(data, status){//SUCCESS
                $scope.lstArchivedCase = data;
                if(data.length>0){
                    $scope.loadContextCaseId(data[0]);
                }
            },
            function(data, status){//ERROR

            })
        }
    }

    $scope.loadContextCaseId = function(archivedCase) {
        if ($scope.properties.taskId === undefined || $scope.properties.taskId === "") {
            doRequest("GET", "../API/bpm/archivedCase/"+archivedCase.id+"/context", {},
            function(data, status){//SUCCESS
                $scope.properties.context = data;
            },
            function(data, status){//ERROR

            })
        }
    }

    function doRequest(method, url, dataToSend, callback, errorCallback) {
        
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend)
        };

        return $http(req)
            .success(callback)
            .error(errorCallback);
    }

    $scope.$watchCollection("properties.taskId", function(newValue, oldValue) {
        console.log("loadcontext")
        if ($scope.properties.taskId !== undefined && $scope.properties.taskId !== "") {
            $scope.loadContextTask($scope.properties.taskId);
        }
    });

    $scope.$watchCollection("properties.caseId", function(newValue, oldValue) {
        if ($scope.properties.caseId !== undefined) {
            if ($scope.properties.taskId === undefined || $scope.properties.taskId === "") {
                $scope.loadArchivedCase($scope.properties.caseId);
            }
        }
    });
}