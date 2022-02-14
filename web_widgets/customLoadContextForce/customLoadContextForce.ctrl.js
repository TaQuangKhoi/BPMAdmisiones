function loadContextCtrl($scope, $http) {
    $scope.lstArchivedCase = [];
    $scope.caseId=0;
    $scope.taskId=0;
    $scope.loadCaseId= function(){
        doRequest("GET","/bonita/API/system/session/unusedid", null, function(data,status){
            doRequest("GET","../API/bdm/businessData/com.anahuac.catalogos.CatRegistro?q=findByCorreoelectronico&f=correoelectronico="+data.user_name+"&p=0&c=500", null, function(data,status){
                $scope.caseId=data[0].caseId;
                //	../API/extension/RegistroRest?url=humanTask&p=0&c=10&caseid={{caseList[0].caseId}}&fstate=ready
                doRequest("GET","../API/extension/RegistroRest?url=humanTask&p=0&c=10&caseid="+$scope.caseId+"&fstate=ready", null, function(data,status){
                    $scope.taskId=data[0].id;
                    doRequest("GET","../API/bpm/userTask/"+$scope.taskId+"/context", null, function(context,status){
                        $scope.properties.context = context;
                        
                    },function(data,status){
            
                    })
                },function(data,status){
        
                })
            },function(data,status){
    
            })
        },function(data,status){

        })
        //../API/bdm/businessData/com.anahuac.catalogos.CatRegistro?q=findByCorreoelectronico&f=correoelectronico={{session.user_name}}&p=0&c=500
    }
    $scope.loadContextTask = function(taskId) {
        //doRequest("GET", "../API/bpm/userTask/"+taskId+"/context", {},
        let task;
        doRequest("GET", "../API/bpm/archivedManualTask?p=0&c=10&f=caseId="+$scope.caseId, {},
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
        if ($scope.taskId === undefined || $scope.taskId === "") {
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
        if ($scope.taskId === undefined || $scope.taskId === "") {
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
    $scope.loadCaseId();
}