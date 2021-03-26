function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {
        if ($scope.properties.action === 'Remove from collection') {
            removeFromCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Add to collection') {
            addToCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Start process') {
            startProcess();
        } else if ($scope.properties.action === 'Submit task') {
            submitTask();
        } else if ($scope.properties.action === 'Open modal') {
            closeModal($scope.properties.closeOnSuccess);
            openModal($scope.properties.modalId);
        } else if ($scope.properties.action === 'Close modal') {
            closeModal(true);
        } else if ($scope.properties.url) {
            doRequest($scope.properties.action, $scope.properties.url);
        }
    };

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal(shouldClose) {
        if (shouldClose)
            modalService.close();
    }

    function removeFromCollection() {
        if ($scope.properties.collectionToModify) {
            if (!Array.isArray($scope.properties.collectionToModify)) {
                throw 'Collection property for widget button should be an array, but was ' + $scope.properties.collectionToModify;
            }
            var index = -1;
            if ($scope.properties.collectionPosition === 'First') {
                index = 0;
            } else if ($scope.properties.collectionPosition === 'Last') {
                index = $scope.properties.collectionToModify.length - 1;
            } else if ($scope.properties.collectionPosition === 'Item') {
                index = $scope.properties.collectionToModify.indexOf($scope.properties.removeItem);
            }

            // Only remove element for valid index
            if (index !== -1) {
                $scope.properties.collectionToModify.splice(index, 1);
            }
        }
    }

    function addToCollection() {
        if (!$scope.properties.collectionToModify) {
            $scope.properties.collectionToModify = [];
        }
        if (!Array.isArray($scope.properties.collectionToModify)) {
            throw 'Collection property for widget button should be an array, but was ' + $scope.properties.collectionToModify;
        }
        var item = angular.copy($scope.properties.valueToAdd);

        if ($scope.properties.collectionPosition === 'First') {
            $scope.properties.collectionToModify.unshift(item);
        } else {
            $scope.properties.collectionToModify.push(item);
        }
    }





function startProcess() {
    debugger;
    var count = 0;
        if ($scope.properties.dataToChange2.clave || $scope.properties.dataToChange2.clave === "") {
            count= ($scope.properties.dataToChange2.isCuatrimestral ? 1:0)+($scope.properties.dataToChange2.isSemestral?1:0)+($scope.properties.dataToChange2.isAnual?1:0)
            let inicio = typeof $scope.properties.dataToChange2.fechaInicio === 'string' ? (new Date(angular.copy($scope.properties.dataToChange2.fechaInicio.slice(0,10)) + "Z")):angular.copy($scope.properties.dataToChange2.fechaInicio) ;
            let fin = typeof $scope.properties.dataToChange2.fechaFin === 'string' ? (new Date(angular.copy($scope.properties.dataToChange2.fechaFin.slice(0,10)) + "Z")):angular.copy($scope.properties.dataToChange2.fechaFin) ;
            $scope.properties.dataToChange2.fechaInicio = inicio;
            $scope.properties.dataToChange2.fechaFin = fin;
            //$scope.properties.dataToChange2.campus.persistenceId_string=$scope.properties.dataToChange2.campus.persistenceId;
            if ( $scope.properties.dataToChange2.year && $scope.properties.dataToChange2.codigo  && $scope.properties.dataToChange2.descripcion  && count >= 1 && ( inicio < fin) && $scope.properties.dataToChange2.year && $scope.properties.dataToChange2.codigo && inicio && fin ) {
                $scope.properties.dataToChange2.clave = angular.copy($scope.properties.dataToChange2.year)+""+angular.copy($scope.properties.dataToChange2.codigo)
                var tipo = ($scope.properties.dataToChange2.isCuatrimestral ?"ISCUATRIMESTRAL":"" ) +($scope.properties.dataToChange2.isSemestral?"ISSEMESTRAL":"");
                if ($scope.properties.processId) {
                    var req = {
                        method: 'GET',
                        url: `/API/extension/AnahuacRestGet?url=getValidarIdBanner&p=0&c=10&tabla=CATPERIODO&idBanner=${$scope.properties.dataToChange2.id}&id=${$scope.properties.dataToChange2.persistenceId}`,
                    };
                    return $http(req).success(function(data, status) {
                        if(data.data[0]){
                            req = {
                                method: 'GET',
                                url: `/API/extension/AnahuacRestGet?url=getValidarClavePeriodo&p=0&c=10&clave=${$scope.properties.dataToChange2.clave}&tipo=${tipo}&id=${$scope.properties.dataToChange2.persistenceId}`,
                            };
                            return $http(req).success(function(datos, status) {
                                if(datos.totalRegistros<1){
                                    var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function () {
                                        doRequest("GET", $scope.properties.url).then(function () {
                                            $scope.properties.dataToChange = $scope.properties.dataToSet;
                                            $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                                        });
                                        localStorageService.delete($window.location.href);
                                    }); 
                                }else{
                                     swal("¡Aviso!", "la combinación de año y codigo capturado ya existe, por favor ingrese uno diferente.", "warning");
                                }
                            }).error(function(data, status) {})
                             
                        }else{
                            swal("¡Aviso!", "el id capturado ya existe, por favor ingrese uno diferente.", "warning");
                        }
                        
                    }).error(function(data, status) {})
                } else {
                    $log.log('Impossible to retrieve the process definition id value from the URL');
                }
            } else {
                if(inicio >= fin){
                    swal("¡Aviso!", "La fecha de fin captación no puede ser menor o iguarl a la fecha de inicio captación" , "warning");
                }
                if (!$scope.properties.dataToChange2.fechaInicio) {
                    swal("¡Aviso!", "Faltó capturar información en: fecha de inicio captación" , "warning");
                }
                if (!$scope.properties.dataToChange2.fechaFin) {
                    swal("¡Aviso!", "Faltó capturar información en: fecha de fin captación " , "warning");
                }
                if(count < 1){
                    swal("¡Aviso!", "Debe seleccionar uno de los tres: cuatrimestral, semestral, anual" , "warning");
                }
                if (!$scope.properties.dataToChange2.descripcion) {
                    swal("¡Aviso!", "Faltó capturar información en: Descripción" , "warning");
                }
                if (!$scope.properties.dataToChange2.codigo) {
                    swal("¡Aviso!", "Faltó capturar información en: Código" , "warning");
                }
                if (!$scope.properties.dataToChange2.year) {
                    swal("¡Aviso!", "Faltó capturar información en: Año" , "warning");
                }
                if (!$scope.properties.dataToChange2.id) {
                    swal("¡Aviso!", "Faltó capturar información en: Id" , "warning");
                }
            }

        } else {
            count= ($scope.properties.dataToChange2.lstCatPeriodoInput[0].isCuatrimestral ? 1:0)+($scope.properties.dataToChange2.lstCatPeriodoInput[0].isSemestral?1:0)+($scope.properties.dataToChange2.lstCatPeriodoInput[0].isAnual?1:0)
            let fin = typeof $scope.properties.dataToChange2.lstCatPeriodoInput[0].fechaFin === 'string' ? (new Date(angular.copy($scope.properties.dataToChange2.lstCatPeriodoInput[0].fechaFin) + "Z")):angular.copy($scope.properties.dataToChange2.lstCatPeriodoInput[0].fechaFin) ;
            $scope.properties.dataToChange2.lstCatPeriodoInput[0].fechaFin = fin;
            if ( $scope.properties.dataToChange2.lstCatPeriodoInput[0].year && $scope.properties.dataToChange2.lstCatPeriodoInput[0].codigo && $scope.properties.dataToChange2.lstCatPeriodoInput[0].descripcion && count >= 1 &&($scope.properties.dataToChange2.lstCatPeriodoInput[0].fechaInicio < $scope.properties.dataToChange2.lstCatPeriodoInput[0].fechaFin) && $scope.properties.dataToChange2.lstCatPeriodoInput[0].fechaInicio && $scope.properties.dataToChange2.lstCatPeriodoInput[0].fechaFin) {
                if ($scope.properties.processId) {
                    $scope.properties.dataToChange2.lstCatPeriodoInput[0].clave = angular.copy($scope.properties.dataToChange2.lstCatPeriodoInput[0].year)+""+angular.copy($scope.properties.dataToChange2.lstCatPeriodoInput[0].codigo)
                    var tipo = ($scope.properties.dataToChange2.lstCatPeriodoInput[0].isCuatrimestral ?"ISCUATRIMESTRAL":"" ) +($scope.properties.dataToChange2.lstCatPeriodoInput[0].isSemestral?"ISSEMESTRAL":"");
                    var req = {
                        method: 'GET',
                        url: `/API/extension/AnahuacRestGet?url=getValidarIdBanner&p=0&c=10&tabla=CATPERIODO&idBanner=${$scope.properties.dataToChange2.lstCatPeriodoInput[0].id}&id=`,
                    };
                    return $http(req).success(function(data, status) {
                        if(data.data[0]){
                           req = {
                                method: 'GET',
                                url: `/API/extension/AnahuacRestGet?url=getValidarClavePeriodo&p=0&c=10&clave=${$scope.properties.dataToChange2.lstCatPeriodoInput[0].clave}&tipo=${tipo}&id=`,
                            };
                            return $http(req).success(function(datos, status) {
                                if(datos.totalRegistros<1){
                                    var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function () {
                                        doRequest("GET", $scope.properties.url).then(function () {
                                            $scope.properties.dataToChange = $scope.properties.dataToSet;
                                            $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                                        });
                                        localStorageService.delete($window.location.href);
                                    });
                                }else{
                                    swal("¡Aviso!", "la combinación de año y codigo capturado ya existe, por favor ingrese uno diferente.", "warning");
                                }
                            }).error(function(data, status) {})
                        }else {
                            swal("¡Aviso!", "el id capturado ya existe, por favor ingrese uno diferente.", "warning");
                        }
                        
                    }).error(function(data, status) {})
                } else {
                    $log.log('Impossible to retrieve the process definition id value from the URL');
                }
            } else {
                if($scope.properties.dataToChange2.lstCatPeriodoInput[0].fechaInicio >= $scope.properties.dataToChange2.lstCatPeriodoInput[0].fechaFin){
                    swal("¡Aviso!", "La fecha de fin captación no puede ser menor o iguarl a la fecha de inicio captación" , "warning");
                }
                if (!$scope.properties.dataToChange2.lstCatPeriodoInput[0].fechaInicio) {
                    swal("¡Aviso!", "Faltó capturar información en: fecha de inicio captación" , "warning");
                }
                if (!$scope.properties.dataToChange2.lstCatPeriodoInput[0].fechaFin) {
                    swal("¡Aviso!", "Faltó capturar información en: fecha de fin captación " , "warning");
                }
                if(count < 1){
                    swal("¡Aviso!", "Debe seleccionar uno de los tres: cuatrimestral, semestral, anual" , "warning");
                }
                if (!$scope.properties.dataToChange2.lstCatPeriodoInput[0].descripcion) {
                    swal("¡Aviso!", "Faltó capturar información en: Descripción" , "warning");
                }
                if (!$scope.properties.dataToChange2.lstCatPeriodoInput[0].codigo) {
                    swal("¡Aviso!", "Faltó capturar información en: Código" , "warning");
                }
                if (!$scope.properties.dataToChange2.lstCatPeriodoInput[0].year) {
                    swal("¡Aviso!", "Faltó capturar información en: Año" , "warning");
                }
                if (!$scope.properties.dataToChange2.lstCatPeriodoInput[0].id) {
                    swal("¡Aviso!", "Faltó capturar información en: Id" , "warning");
                }
               
            }
        }
}


    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.dataFromSuccess = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromError = undefined;
                notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status });
                if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                    redirectIfNeeded();
                }
                closeModal($scope.properties.closeOnSuccess);
            })
            .error(function(data, status) {
                $scope.properties.dataFromError = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromSuccess = undefined;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                vm.busy = false;
            });
    }

    function redirectIfNeeded() {
        var iframeId = $window.frameElement ? $window.frameElement.id : null;
        //Redirect only if we are not in the portal or a living app
        if (!iframeId || iframeId && iframeId.indexOf('bonitaframe') !== 0) {
            $window.location.assign($scope.properties.targetUrlOnSuccess);
        }
    }

    function notifyParentFrame(additionalProperties) {
        if ($window.parent !== $window.self) {
            var dataToSend = angular.extend({}, $scope.properties, additionalProperties);
            $window.parent.postMessage(JSON.stringify(dataToSend), '*');
        }
    }

    function getUserParam() {
        var userId = getUrlParam('user');
        if (userId) {
            return { 'user': userId };
        }
        return {};
    }

    /**
     * Extract the param value from a URL query
     * e.g. if param = "id", it extracts the id value in the following cases:
     *  1. http://localhost/bonita/portal/resource/process/ProcName/1.0/content/?id=8880000
     *  2. http://localhost/bonita/portal/resource/process/ProcName/1.0/content/?param=value&id=8880000&locale=en
     *  3. http://localhost/bonita/portal/resource/process/ProcName/1.0/content/?param=value&id=8880000&locale=en#hash=value
     * @returns {id}
     */
    function getUrlParam(param) {
        var paramValue = $location.absUrl().match('[//?&]' + param + '=([^&#]*)($|[&#])');
        if (paramValue) {
            return paramValue[1];
        }
        return '';
    }

    function submitTask() {
        var id;
        id = getUrlParam('id');
        if (id) {
            var params = $scope.properties.userId;
            params.assign = $scope.properties.assign;
            doRequest('POST', '../API/bpm/userTask/' + getUrlParam('id') + '/execution', params).then(function() {
                localStorageService.delete($window.location.href);
            });
        } else {
            $log.log('Impossible to retrieve the task id value from the URL');
        }
    }

}