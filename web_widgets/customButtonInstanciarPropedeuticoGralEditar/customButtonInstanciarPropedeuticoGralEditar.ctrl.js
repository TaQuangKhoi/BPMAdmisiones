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

        var isId = ($scope.properties.dataToSend.catPropedeuticoGralInput.id === null ? false : ($scope.properties.dataToSend.catPropedeuticoGralInput.id === "" ? false : true));
        var isDescripcion = ($scope.properties.dataToSend.catPropedeuticoGralInput.descripcion === null ? false : ($scope.properties.dataToSend.catPropedeuticoGralInput.descripcion === "" ? false : true));
        var isCatRelacionCampusTipo = ($scope.properties.dataToSend.catPropedeuticoGralInput.catRelacionCampusTipo === null ? false : ($scope.properties.dataToSend.catPropedeuticoGralInput.catRelacionCampusTipo.length > 0 ? true : false));
        var isClave = ($scope.properties.dataToSend.catPropedeuticoGralInput.clave === null ? false : ($scope.properties.dataToSend.catPropedeuticoGralInput.clave.length != 6 ? false : true));

        var isClaveRestante = $scope.properties.dataToSend.catPropedeuticoGralInput.claveRestante === null ? false : ($scope.properties.dataToSend.catPropedeuticoGralInput.claveRestante === "" ? false : true)
        var isLstYear = $scope.properties.dataToSend.catPropedeuticoGralInput.claveYear === null ? false : ($scope.properties.dataToSend.catPropedeuticoGralInput.claveYear === "" ? false : true)

        if (isId && isDescripcion && isCatRelacionCampusTipo && isClaveRestante && isLstYear) {
            var isValidList = true;
            for (var indexRelacion in $scope.properties.dataToSend.catPropedeuticoGralInput.catRelacionCampusTipo) {
                if ($scope.properties.dataToSend.catPropedeuticoGralInput.catRelacionCampusTipo[indexRelacion].tipoPeriodo == null || $scope.properties.dataToSend.catPropedeuticoGralInput.catRelacionCampusTipo[indexRelacion].tipoPeriodo == "") {
                    isValidList = false;
                }
            }
            if (isValidList) {
                if ($scope.properties.processId) {
                    var idBanner = $scope.properties.dataToSend.catPropedeuticoGralInput.id;
                    var persistenceId = $scope.properties.dataToSend.catPropedeuticoGralInput.persistenceId;
                    vm.busy = true;
                    var req = {
                        method: "GET",
                        url: "../API/extension/AnahuacRestGet?url=getValidarIdBanner&p=0&c=10&tabla=CatPropedeuticoGral&idBanner=" + idBanner + "&id=" + persistenceId
                    };
                    return $http(req)
                        .success(function(data, status) {
                            if (data.data[0]) {
                            	console.log("TEST DATA")
                                var clave = $scope.properties.dataToSend.catPropedeuticoGralInput.claveYear+""+$scope.properties.dataToSend.catPropedeuticoGralInput.claveRestante;
                                vm.busy = true;
                                var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function() {
                                    doRequest("GET", $scope.properties.url).then(function() {
                                        $scope.properties.dataToChange = $scope.properties.dataToSet;
                                        $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                                    });
                                    localStorageService.delete($window.location.href);
                                });
                               /* var req = {
                                    method: "GET",
                                    url: "../API/extension/AnahuacRestGet?url=getValidarClave&p=0&c=10&tabla=CatPropedeuticoGral&clave=" + clave + "&id=" + persistenceId
                                };
                                return $http(req)
                                    .success(function(data, status) {
                                        if (data.data[0]) {*/
                                            /*=============================================*/
                                            /*var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function() {
                                                doRequest("GET", $scope.properties.url).then(function() {
                                                    $scope.properties.dataToChange = $scope.properties.dataToSet;
                                                    $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                                                });
                                                localStorageService.delete($window.location.href);
                                            });*/
                                            /*=============================================*/
                                        /*} else {
                                            swal("¡Aviso!", "La clave " + clave + " ya se encuentra registrada", "warning");
                                        }
                                    })
                                    .error(function(data, status) {

                                    }).finally(function() {
                                        vm.busy = false;
                                    });*/
                            } else {
                                swal("¡Aviso!", "El id " + idBanner + " ya se encuentra registrado", "warning");
                            }
                        })
                        .error(function(data, status) {

                        }).finally(function() {
                            vm.busy = false;
                        });
                } else {
                    $log.log('Impossible to retrieve the process definition id value from the URL');
                }
            } else {
                swal("¡Aviso!", "Faltó capturar información en: Tipo de período en el listado de campus", "warning");
            }
        } else {
            if (!isId) {
                swal("¡Aviso!", "Faltó capturar información en: Id", "warning");
            }
            if (!isDescripcion) {
                swal("¡Aviso!", "Faltó capturar información en: Descripción", "warning");
            }
            if (!isClaveRestante || !isLstYear) {
                swal("¡Aviso!", "Faltó capturar información en: Clave", "warning");
            }
            if (!isCatRelacionCampusTipo) {
                swal("¡Aviso!", "Faltó capturar información en: Listado de campus", "warning");
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