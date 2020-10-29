function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {
        debugger;
        $scope.agregootro = false;
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
            $scope.properties.tutor.isTutor = true;
            if ($scope.properties.tutor.idCatTitulo === 0) {
                console.log("falta titulo");
            } else if ($scope.properties.tutor.idCatParentezco === 0) {
                console.log("falta parentesco");
            } else if ($scope.properties.otroparentesco) {
                if ($scope.properties.tutor.otroParentesco === undefined || $scope.properties.tutor.otroParentesco === "") {
                    console.log("falta el otro parentesco");
                } else {
                    $scope.agregootro = true;
                }
            }
            if ($scope.agregootro) {
                if ($scope.properties.tutor.nombre === "") {
                    console.log("falta nombre");
                } else if ($scope.properties.tutor.apellidos === "") {
                    console.log("falta apellido");
                } else if ($scope.properties.tutor.correoElectronico === "") {
                    console.log("falta correo");
                } else if ($scope.properties.tutor.idCatEscolaridad === 0) {
                    console.log("falta escolaridad");
                } else if ($scope.properties.tutor.idCatEgresoAnahuac === 0) {
                    console.log("falta egresado");
                } else if ($scope.properties.tutor.idCatCampusEgreso === 0) {
                    console.log("falta campus");
                } else if ($scope.properties.tutor.idCatTrabaja === 0) {
                    console.log("falta trabajo");
                } else if (!$scope.properties.tutor.isTutor) {
                    console.log("falta tutor");
                } else if ($scope.properties.tutor.calle === "") {
                    console.log("falta calle");
                } else if ($scope.properties.tutor.idCatPais === 0) {
                    console.log("falta pais");
                } else if ($scope.properties.tutor.numeroExterior === "") {
                    console.log("falta numero");
                } else if ($scope.properties.tutor.idCatEstado === 0) {
                    console.log("falta estado");
                } else if ($scope.properties.tutor.ciudad === "") {
                    console.log("falta ciudad");
                } else if ($scope.properties.tutor.colonia === "") {
                    console.log("falta colonia");
                } else if ($scope.properties.tutor.telefono === "") {
                    console.log("falta telefono");
                } else if ($scope.properties.tutor.codigoPostal === "") {
                    console.log("falta cp");
                } else if ($scope.properties.tutor.empresaTrabaja === "") {
                    console.log("falta empresa");
                } else if ($scope.properties.tutor.puesto === "") {
                    console.log("falta puesto");
                } else {
                    $scope.properties.formInput.tutorInput.push($scope.properties.tutor);
                    $scope.properties.tutor = {
                        "catTitulo": {
                            "persistenceId_string": ""
                        },
                        "catParentezco": {
                            "persistenceId_string": ""
                        },
                        "nombre": "",
                        "apellidos": "",
                        "correoElectronico": "",
                        "catEscolaridad": {
                            "persistenceId_string": ""
                        },
                        "catEgresoAnahuac": {
                            "persistenceId_string": ""
                        },
                        "catCampusEgreso": {
                            "persistenceId_string": ""
                        },
                        "catTrabaja": {
                            "persistenceId_string": ""
                        },
                        "empresaTrabaja": "",
                        "giroEmpresa": "",
                        "puesto": "",
                        "isTutor": false,
                        "vive": {
                            "persistenceId_string": ""
                        },
                        "calle": "",
                        "catPais": {
                            "persistenceId_string": ""
                        },
                        "numeroExterior": "",
                        "numeroInterior": "",
                        "catEstado": {
                            "persistenceId_string": ""
                        },
                        "ciudad": "",
                        "colonia": "",
                        "telefono": "",
                        "codigoPostal": "",
                        "viveContigo": false,
                        "otroParentesco": ""
                    };
                    closeModal(true);
                }
            } else {
                if ($scope.properties.tutor.nombre === "") {
                    console.log("falta nombre");
                } else if ($scope.properties.tutor.apellidos === "") {
                    console.log("falta apellido");
                } else if ($scope.properties.tutor.correoElectronico === "") {
                    console.log("falta correo");
                } else if ($scope.properties.tutor.idCatEscolaridad === 0) {
                    console.log("falta escolaridad");
                } else if ($scope.properties.tutor.idCatEgresoAnahuac === 0) {
                    console.log("falta egresado");
                } else if ($scope.properties.tutor.idCatCampusEgreso === 0) {
                    console.log("falta campus");
                } else if ($scope.properties.tutor.idCatTrabaja === 0) {
                    console.log("falta trabajo");
                } else if (!$scope.properties.tutor.isTutor) {
                    console.log("falta tutor");
                } else if ($scope.properties.tutor.calle === "") {
                    console.log("falta calle");
                } else if ($scope.properties.tutor.idCatPais === 0) {
                    console.log("falta pais");
                } else if ($scope.properties.tutor.numeroExterior === "") {
                    console.log("falta numero");
                } else if ($scope.properties.tutor.idCatEstado === 0) {
                    console.log("falta estado");
                } else if ($scope.properties.tutor.ciudad === "") {
                    console.log("falta ciudad");
                } else if ($scope.properties.tutor.colonia === "") {
                    console.log("falta colonia");
                } else if ($scope.properties.tutor.telefono === "") {
                    console.log("falta telefono");
                } else if ($scope.properties.tutor.codigoPostal === "") {
                    console.log("falta cp");
                } else if ($scope.properties.tutor.empresaTrabaja === "") {
                    console.log("falta empresa");
                } else if ($scope.properties.tutor.puesto === "") {
                    console.log("falta puesto");
                } else {
                    $scope.properties.formInput.tutorInput.push($scope.properties.tutor);
                    $scope.properties.tutor = {
                        "catTitulo": {
                            "persistenceId_string": ""
                        },
                        "catParentezco": {
                            "persistenceId_string": ""
                        },
                        "nombre": "",
                        "apellidos": "",
                        "correoElectronico": "",
                        "catEscolaridad": {
                            "persistenceId_string": ""
                        },
                        "catEgresoAnahuac": {
                            "persistenceId_string": ""
                        },
                        "catCampusEgreso": {
                            "persistenceId_string": ""
                        },
                        "catTrabaja": {
                            "persistenceId_string": ""
                        },
                        "empresaTrabaja": "",
                        "giroEmpresa": "",
                        "puesto": "",
                        "isTutor": false,
                        "vive": {
                            "persistenceId_string": ""
                        },
                        "calle": "",
                        "catPais": {
                            "persistenceId_string": ""
                        },
                        "numeroExterior": "",
                        "numeroInterior": "",
                        "catEstado": {
                            "persistenceId_string": ""
                        },
                        "ciudad": "",
                        "colonia": "",
                        "telefono": "",
                        "codigoPostal": "",
                        "viveContigo": false,
                        "otroParentesco": ""
                    };
                    closeModal(true);
                }
            }
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
        var id = getUrlParam('id');
        if (id) {
            var prom = doRequest('POST', '../API/bpm/process/' + id + '/instantiation', getUserParam()).then(function() {
                localStorageService.delete($window.location.href);
            });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
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
                notifyParentFrame({
                    message: 'success',
                    status: status,
                    dataFromSuccess: data,
                    dataFromError: undefined,
                    responseStatusCode: status
                });
                if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                    redirectIfNeeded();
                }
                closeModal($scope.properties.closeOnSuccess);
            })
            .error(function(data, status) {
                $scope.properties.dataFromError = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromSuccess = undefined;
                notifyParentFrame({
                    message: 'error',
                    status: status,
                    dataFromError: data,
                    dataFromSuccess: undefined,
                    responseStatusCode: status
                });
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
            return {
                'user': userId
            };
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
            var params = getUserParam();
            params.assign = $scope.properties.assign;
            doRequest('POST', '../API/bpm/userTask/' + getUrlParam('id') + '/execution', params).then(function() {
                localStorageService.delete($window.location.href);
            });
        } else {
            $log.log('Impossible to retrieve the task id value from the URL');
        }
    }

}