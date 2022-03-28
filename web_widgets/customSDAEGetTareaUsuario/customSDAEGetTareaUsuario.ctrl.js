function ($scope, $http) {

    function getCurrentTaskId() {
        $http.get($scope.properties.url).success((data) => {
            if (data.length) {
                $scope.properties.taskId = data[0].id;
                $scope.properties.caseId = data[0].caseId;
                getCurrentContext();
            }
        }).error((err) => {
            // swal("Error", "Error al obtener las tareas asignadas al usuario. " + err, "error");
            console.log("Error al obtener las tareas asignadas al usuario" + JSON.stringify(err));
        });
    }

    function getCurrentContext() {
        $http.get($scope.properties.urlContext).success((data) => {
            if (data.solicitudApoyoEducativo_ref) {
                getModelSolicitudApoyoEducativo("../" + data.solicitudApoyoEducativo_ref.link);
                getModelHermanos("../" + data.hermanos_ref.link);
                getModelAutos("../" + data.autos_ref.link);
                getModelBienesRaices("../" + data.bienesRaices_ref.link);
                getModelDocumentos("../" + data.documentosSolicitante_ref.link);
                getModelImagenesSocioEco("../" + data.imagenesSocEcoSolicitante_ref.link);

                $scope.properties.fotoCalleCasa = addDataToDocuments(data.fotoCalleCasa_ref);
                $scope.properties.fotoComedorCasa = addDataToDocuments(data.fotoComedorCasa_ref);
                $scope.properties.fotoFachadaCasa = addDataToDocuments(data.fotoFachadaCasa_ref);
                $scope.properties.fotoSalaCasa = addDataToDocuments(data.fotoSalaCasa_ref);

                $scope.properties.lstDocumentos = [];
                $scope.properties.autos = [];
                let lstDoc = [];
                let newValue = {
                    "filename": null,
                    "tempPath": null,
                    "contentType": null,
                    "id": null
                }

                if (data.lstDocumentos_ref) {
                    for (documento of data.lstDocumentos_ref) {
                        newValue.id = documento["id"] + "";
                        lstDoc.push(angular.copy(newValue));
                    }

                    $scope.properties.lstDocumentos = lstDoc;
                }
            }
        }).error((err) => {
            // swal("Error", "Error al obtener el context. " + err, "error");
            console.log("Error al obtener el context")
        });
    }

    function addDataToDocuments(_document) {

        let newValue = {
            "filename": _document["fileName"],
            "tempPath": null,
            "contentType": _document["contentMimeType"],
            "id": _document["id"] + ""
        }

        return newValue;
    }

    function getModelSolicitudApoyoEducativo(url) {
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.solicitudApoyoEducativo = [];
                $scope.properties.solicitudApoyoEducativo = data;
                let links = $scope.properties.solicitudApoyoEducativo.links;

                for (let link of links) {
                    getLazyRefModel(".." + link.href, link.rel);
                }
            }
        }).error((err) => {
            swal("Error", "Error al obtener el model. " + err, "error");
        });
    }

    function getModelHermanos(url) {
        $scope.properties.hermanos = [];
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.hermanos = data;
            }
        }).error((err) => {
            $scope.properties.hermanos = [];
            // swal("Error","Error al obtener el model. " + err,"error");
            console.log("hermanos vacío")
        });
    }

    function getModelAutos(url) {
        $scope.properties.autos = [];
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.autos = data;
            }
        }).error((err) => {
            $scope.properties.autos = [];
            // swal("Error","Error al obtener el model. " + err,"error");
            console.log("autos vacío")
        });
    }

    function getModelBienesRaices(url) {
        $scope.properties.bienesRaices = [];
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.bienesRaices = data;
            }
        }).error((err) => {
            $scope.properties.bienesRaices = [];
            // swal("Error","Error al obtener el model. " + err,"error");
            console.log("bienesRaices vacío")
        });
    }

    function getModelDocumentos(url) {
        $scope.properties.documentos = [];
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.documentos = data;
            }
        }).error((err) => {
            $scope.properties.documentos = buildDocumentList();

        });
    }

    function buildDocumentList(){
        let list = [];
        for(let documentObject of $scope.properties.lstDocumentosByTipoApoyo){
            let templateObject = {
                "persistenceId_string":"",
                "catManejoDocumentos": documentObject,
                "urlDocumento":"",
                "caseId": $scope.properties.caseId
            }   

            list.push(templateObject);

        }
        
        return list;
    }

    function getModelImagenesSocioEco(url) {
        $scope.properties.imagenesSocioEco = [];
        $http.get(url).success((data) => {
            if (data) {
                $scope.properties.imagenesSocioEco = data;
            }
        }).error((err) => {
            $scope.properties.imagenesSocioEco = buildImagesList();
        });
    }

    function buildImagesList(){
        let list = [];
        for(let imageObject of $scope.properties.lstImagenesByTipoApoyo){
            let templateObject = {
                "persistenceId_string":"",
                "catManejoDocumentos": imageObject,
                "urlDocumento":"",
                "caseId": $scope.properties.caseId
            }   

            list.push(templateObject);

        }

        return list;
   }

    function getLazyRefModel(_url, _bdmFieldName) {
        $http.get(_url).success((data) => {
            $scope.properties.solicitudApoyoEducativo[_bdmFieldName] = data;
        }).error((err) => {
            // swal("Error", "Error al obtener el model. " + err, "error");
            console.log(" vacío")
        });
    }

    $scope.$watch("properties.taskId", (oldValue, newValue) => {
        if ($scope.properties.taskId && oldValue !== newValue) {
            getCurrentContext();
        }
    });

    $scope.$watch("properties.reloadTask", () => {
        if ($scope.properties.reloadTask) {
            getCurrentTaskId();
        }
    });

    $scope.$watch("properties.url", () => {
        getCurrentTaskId();
    });
}