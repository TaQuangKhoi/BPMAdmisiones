function ($scope, $http) {

    function getCurrentTaskId(){
        $http.get($scope.properties.url).success((data)=>{
            if(data.length){
                $scope.properties.taskId = data[0].id;
                $scope.properties.caseId = data[0].caseId;
                getCurrentContext();
            }
        }).error((err)=>{
            swal("Error","Error al obtener las tareas asignadas al usuario. " + err,"error");
        });
    }
    
    function getCurrentContext(){
        $http.get($scope.properties.urlContext).success((data)=>{
            if(data.solicitudApoyoEducativo_ref){
                getModelSolicitudApoyoEducativo("../" + data.solicitudApoyoEducativo_ref.link);
                getModelHermanos("../" + data.hermanos_ref.link);
                $scope.properties.lstDocumentos = [];
                $scope.properties.autos = [];
                let lstDoc = [];
                let newValue = {
                    "filename": null,
                    "tempPath": null,
                    "contentType": null,
                    "id": null
                }
                
                for(documento of data.lstDocumentos_ref){
                    newValue.id = documento["id"] + "";
                    lstDoc.push(angular.copy(newValue));
                }
                
                $scope.properties.lstDocumentos = lstDoc;
                
            } 
        }).error((err)=>{
            swal("Error","Error al obtener el context. " + err,"error");
        });
    }
    
    function getModelSolicitudApoyoEducativo(url){
        $http.get(url).success((data)=>{
            if(data){
                $scope.properties.solicitudApoyoEducativo = [];
                $scope.properties.solicitudApoyoEducativo = data;
                let links = $scope.properties.solicitudApoyoEducativo.links;

                for(let link of links){
                    getLazyRefModel(".." + link.href, link.rel);
                }
            }
        }).error((err)=>{
            swal("Error","Error al obtener el model. " + err,"error");
        });
    }

    function getModelHermanos(url){
        $scope.properties.hermanos = [];
        $http.get(url).success((data)=>{
            if(data){
                $scope.properties.hermanos = data;
            }
        }).error((err)=>{
            // swal("Error","Error al obtener el model. " + err,"error");
            console.log("hermanos vacío")
        });
    } 

    function getLazyRefModel(_url, _bdmFieldName){
        $http.get(_url).success((data)=>{
            $scope.properties.solicitudApoyoEducativo[_bdmFieldName] = data;
        }).error((err)=>{
            swal("Error","Error al obtener el model. " + err,"error");
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