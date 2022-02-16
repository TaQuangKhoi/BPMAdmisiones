function($scope, $http, $location) {

    function getCurrentTask() {
        let url = "../API/extension/RegistroRest?url=humanTask&p=0&c=10&caseid=" + $scope.properties.caseId + "&fstate=ready";

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
                if (data.length > 0) {
                    $scope.properties.taskId = data[0].id;
                } else {

                }
            })
            .error(function(data, status) {
                $scope.loadCaseId();
            })
            .finally(function() {
                // vm.busy = false;
            });
    }

    $scope.$watch("properties.caseId", function() {
        if ($scope.properties.caseId !== undefined) {
            //getCurrentTask();
        }
    });

    $scope.$watch("properties.reloadTask", function() {
        if ($scope.properties.reloadTask === true || $scope.properties.reloadTask === "true") {
            $scope.properties.reloadTask = false;
            //getCurrentTask();
        }
    });
    $scope.lstArchivedCase = [];
    $scope.caseId = 0;
    $scope.taskId = 0;
    $scope.lstArchivedCase = {};
    $scope.loadCaseId = function() {
        doRequest("GET", "/bonita/API/system/session/unusedid", null, function(data, status) {
                doRequest("GET", "../API/bdm/businessData/com.anahuac.catalogos.CatRegistro?q=findByCorreoelectronico&f=correoelectronico=" + data.user_name + "&p=0&c=500", null, function(data, status) {
                    if (data.length > 0) {
                        $scope.caseId = data[0].caseId;

                    } else {
                        $scope.caseId = getUrlParam("caseId");
                    }
                    //  ../API/extension/RegistroRest?url=humanTask&p=0&c=10&caseid={{caseList[0].caseId}}&fstate=ready
                    doRequest("GET", "../API/extension/RegistroRest?url=humanTask&p=0&c=10&caseid=" + $scope.caseId + "&fstate=ready", null, function(data, status) {

                        if (data.length < 1) {
                            doRequest("GET", "../API/extension/RegistroRest?url=archivedCase&caseid=" + $scope.caseId, {},
                                function(data, status) { //SUCCESS
                                    $scope.lstArchivedCase = data[0];
                                    if (data.length > 0) {
                                        doRequest("GET", "../API/bpm/archivedCase/" + $scope.lstArchivedCase.id + "/context", {},
                                            function(data, status) { //SUCCESS
                                                $scope.properties.context = data;
                                            },
                                            function(data, status) { //ERROR

                                            })
                                    }
                                },
                                function(data, status) { //ERROR

                                })
                        } else {
                            $scope.taskId = data[0].id;
                            $scope.properties.taskId = $scope.taskId;

                            doRequest("GET", "../API/bpm/userTask/" + $scope.taskId + "/context", null, function(context, status) {
                                $scope.properties.context = context;

                            }, function(data, status) {
                                doRequest("GET", "../API/extension/RegistroRest?url=archivedCase&caseid=" + $scope.caseId, {},
                                    function(data, status) { //SUCCESS
                                        $scope.lstArchivedCase = data[0];
                                        if (data.length > 0) {
                                            doRequest("GET", "../API/bpm/archivedCase/" + $scope.lstArchivedCase.id + "/context", {},
                                                function(data, status) { //SUCCESS
                                                    $scope.properties.context = data;
                                                },
                                                function(data, status) { //ERROR

                                                })
                                        }
                                    },
                                    function(data, status) { //ERROR
                                    })


                            })
                        }

                    }, function(data, status) {

                    })
                }, function(data, status) {

                })
            }, function(data, status) {

            })
            //../API/bdm/businessData/com.anahuac.catalogos.CatRegistro?q=findByCorreoelectronico&f=correoelectronico={{session.user_name}}&p=0&c=500
    }
    $scope.loadContextTask = function(taskId) {
        //doRequest("GET", "../API/bpm/userTask/"+taskId+"/context", {},
        let task;
        doRequest("GET", "../API/bpm/archivedManualTask?p=0&c=10&f=caseId=" + $scope.caseId, {},
            function(data, status) { //SUCCESS
                task = data;

                doRequest("GET", `../API/bpm/archivedUserTask/${task[task.length - 1].id}/context`, {},
                    function(data, status) { //SUCCESS
                        $scope.properties.context = data;
                    },
                    function(data, status) { //ERROR
                    })
            },
            function(data, status) { //ERROR

            });


    }

    $scope.loadArchivedCase = function(caseId) {
        if ($scope.taskId === undefined || $scope.taskId === "") {
            doRequest("GET", "../API/extension/RegistroRest?url=archivedCase&caseid=" + caseId, {},
                function(data, status) { //SUCCESS
                    $scope.lstArchivedCase = data;
                    if (data.length > 0) {
                        $scope.loadContextCaseId(data[0]);
                    }
                },
                function(data, status) { //ERROR

                })
        }
    }

    $scope.loadContextCaseId = function(archivedCase) {
        if ($scope.taskId === undefined || $scope.taskId === "") {
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

    /*$scope.$watchCollection("taskId", function(newValue, oldValue) {
        console.log("loadcontext")
        if ($scope.taskId !== undefined && $scope.taskId !== "") {
            $scope.loadContextTask($scope.taskId);
        }
    });*/

    /*$scope.$watchCollection("caseId", function(newValue, oldValue) {
        if ($scope.caseId !== undefined) {
            if ($scope.taskId === undefined || $scope.taskId === "") {
                $scope.loadArchivedCase($scope.caseId);
            }
        }
    });*/

    function getUrlParam(param) {
        var paramValue = $location.absUrl().match('[//?&]' + param + '=([^&#]*)($|[&#])');
        if (paramValue) {
            return paramValue[1];
        }
        return '';
    }
    $scope.loadCaseId();
}