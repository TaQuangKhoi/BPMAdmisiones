function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
 

 $scope.asignarTarea = function () {
    var req = {
        method: "PUT",
        url: "/bonita/API/bpm/humanTask/" + $scope.properties.taskId,
        data: angular.copy({ "assigned_id": "" })
    };

    return $http(req)
        .success(function (data, status) {
            redireccionarTarea();
        })
        .error(function (data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function () { });
}

function redireccionarTarea() {
    var req = {
        method: "PUT",
        url: "/bonita/API/bpm/humanTask/" + $scope.properties.taskId,
        data: angular.copy({ "assigned_id": $scope.properties.userId })
    };

    return $http(req)
        .success(function (data, status) {
            $scope.submitTask();
        })
        .error(function (data, status) {

            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function () { });
}

$scope.submitTask = function () {

    var req = {
        method: "POST",
        url: "/bonita/API/bpm/userTask/" + $scope.properties.taskId + "/execution?assign=false",
        data: angular.copy($scope.properties.dataToSend)
    };

    return $http(req)
        .success(function (data, status) {
            $scope.getConsulta();
        })
        .error(function (data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function () { });
}
}
