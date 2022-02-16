function loadContextCtrl($scope, $http) {
    $scope.lstArchivedCase = [];

    $scope.loadContextTask = function(taskId) {
        console.log("LOAD TASK");
        doRequest("GET", "../API/extension/RegistroRest?url=context&caseid=" + taskId, {},
            function(data, status) { //SUCCESS
                $scope.properties.context = data;
            },
            function(data, status) { //ERROR
                if ($scope.properties.caseId !== undefined) {
                    $scope.loadArchivedCase($scope.properties.caseId, true);
                }
            })
    }

    $scope.loadArchivedCase = function(caseId, revivida) {
        console.log("LOAD ARCHIVED TASK");
        if ($scope.properties.taskId === undefined || $scope.properties.taskId === "" || revivida) {
            doRequest("GET", "../API/extension/RegistroRest?url=archivedCase&caseid=" + caseId, {},
                function(data, status) { //SUCCESS
                    $scope.lstArchivedCase = data;
                    if (data.length > 0) {
                        $scope.loadContextCaseId(data[0], revivida);
                    }
                },
                function(data, status) { //ERROR

                })
        }
    }

    $scope.loadContextCaseId = function(archivedCase, revivida) {
        if ($scope.properties.taskId === undefined || $scope.properties.taskId === "" || revivida) {
            doRequest("GET", "../API/bpm/archivedCase/" + archivedCase.id + "/context", {},
                function(data, status) { //SUCCESS
                    $scope.properties.context = data;
                },
                function(data, status) { //ERROR

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
                $scope.loadArchivedCase($scope.properties.caseId, false);
            }
        }
    });
}