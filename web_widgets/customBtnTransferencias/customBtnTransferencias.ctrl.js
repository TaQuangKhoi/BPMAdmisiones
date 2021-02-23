function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {

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
            debugger;
            if ($scope.properties.paisSeleccionado !== undefined) {
                for (var x = 0; x < $scope.properties.catPais.length; x++) {
                    if ($scope.properties.paisSeleccionado === $scope.properties.catPais[x].descripcion) {
                        $scope.properties.valoresSolicitante.catPaisExamen = $scope.properties.catPais[x];
                        break;
                    }
                }
            }

            if ($scope.properties.estadoSeleccionado !== undefined) {
                for (var x = 0; x < $scope.properties.catEstado.length; x++) {
                    if ($scope.properties.estadoSeleccionado === $scope.properties.catEstado[x].descripcion) {
                        $scope.properties.valoresSolicitante.catEstadoExamen = $scope.properties.catEstado[x];
                        break;
                    }
                }
            }

            var existecambio = false;
            var isTransferencia = false;
            if ($scope.properties.valoresSolicitante.catCampus === null) {
                swal("¡Aviso!", "Debes seleccionar un campus", "warning");
            } else if ($scope.properties.valoresSolicitante.catLicenciatura === null) {
                swal("¡Aviso!", "Debes seleccionar una licenciatura", "warning");
            } else if ($scope.properties.valoresSolicitante.catLicenciatura.propedeutico && $scope.properties.valoresSolicitante.catPropedeutico === null) {
                swal("¡Aviso!", "Debes seleccionar un curso propedéutico", "warning");
            } else if ($scope.properties.valoresSolicitante.periodo === null) {
                swal("¡Aviso!", "Debes seleccionar un período", "warning");
            } else if ($scope.properties.valoresSolicitante.catLugarExamen === null) {
                swal("¡Aviso!", "Debes seleccionar un lugar de examen", "warning");
            } else if ($scope.properties.valoresSolicitante.catLugarExamen.descripcion === "En un estado" && $scope.properties.valoresSolicitante.catEstadoExamen === null) {
                swal("¡Aviso!", "Debes seleccionar un estado donde realizará  el examen", "warning");
            } else if ($scope.properties.valoresSolicitante.catLugarExamen.descripcion === "En un estado" && $scope.properties.valoresSolicitante.ciudadExamen === null) {
                swal("¡Aviso!", "Debes seleccionar una ciudad donde se realizará  el examen", "warning");
            } else if ($scope.properties.valoresSolicitante.catLugarExamen.descripcion === "En el extranjero (solo si vives fuera de México)" && $scope.properties.valoresSolicitante.catPaisExamen === null) {
                swal("¡Aviso!", "Debes seleccionar un pais donde se realizará  el examen", "warning");
            } else if ($scope.properties.valoresSolicitante.catLugarExamen.descripcion === "En el extranjero (solo si vives fuera de México)" && $scope.properties.valoresSolicitante.ciudadExamenPais === null) {
                swal("¡Aviso!", "Debes seleccionar una ciudad donde se realizará  el examen", "warning");
            } else {
                $scope.properties.JSONTransferencia.caseid = $scope.properties.valoresSolicitante.caseid;
                $scope.properties.JSONTransferencia.licenciatura = $scope.properties.valoresSolicitante.catLicenciatura.persistenceId;
                if(!$scope.properties.valoresSolicitante.catLicenciatura.propedeutico){
                    $scope.properties.valoresSolicitante.catPropedeutico = null
                }
                if ($scope.properties.valoresSolicitante.catPropedeutico === null) {
                    $scope.properties.JSONTransferencia.propedeutico = $scope.properties.valoresSolicitante.catPropedeutico
                } else {
                    $scope.properties.JSONTransferencia.propedeutico = $scope.properties.valoresSolicitante.catPropedeutico.persistenceId;
                }
                $scope.properties.JSONTransferencia.periodo = $scope.properties.valoresSolicitante.periodo.persistenceId;
                $scope.properties.JSONTransferencia.lugarexamen = $scope.properties.valoresSolicitante.catLugarExamen.persistenceId;
                if ($scope.properties.valoresSolicitante.catLugarExamen.descripcion === "En el mismo campus en donde realizaré mi licenciatura") {
                    $scope.properties.valoresSolicitante.catCampus = $scope.properties.valoresSolicitante.catCampusEstudio;
                    $scope.properties.JSONTransferencia.estadoexamen = null;
                    $scope.properties.JSONTransferencia.ciudadexamen = null;
                    $scope.properties.JSONTransferencia.paisexamen = null;
                    $scope.properties.JSONTransferencia.ciudadpaisexamen = null;
                } else if ($scope.properties.valoresSolicitante.catLugarExamen.descripcion === "En un estado") {
                    $scope.properties.valoresSolicitante.catPaisExamen = null;
                    $scope.properties.valoresSolicitante.ciudadExamenPais = null;
                    if ($scope.properties.valoresSolicitante.ciudadExamen.descripcion === "Tampico") {
                        $scope.properties.valoresSolicitante.catCampus = $scope.properties.valoresSolicitante.catCampusEstudio;
                    } else {
                        $scope.properties.valoresSolicitante.catCampus = $scope.properties.valoresSolicitante.ciudadExamen.campus;
                    }
                    if ($scope.properties.valoresSolicitante.catEstadoExamen === null) {
                        $scope.properties.JSONTransferencia.estadoexamen = $scope.properties.valoresSolicitante.catEstadoExamen;
                    } else {
                        $scope.properties.JSONTransferencia.estadoexamen = $scope.properties.valoresSolicitante.catEstadoExamen.persistenceId;
                    }
                    if ($scope.properties.valoresSolicitante.ciudadExamen === null) {
                        $scope.properties.JSONTransferencia.ciudadexamen = $scope.properties.valoresSolicitante.ciudadExamen;
                    } else {
                        $scope.properties.JSONTransferencia.ciudadexamen = $scope.properties.valoresSolicitante.ciudadExamen.persistenceId;
                        $scope.properties.JSONTransferencia.paisexamen = null;
                        $scope.properties.JSONTransferencia.ciudadpaisexamen = null;
                    }
                } else if ($scope.properties.valoresSolicitante.catLugarExamen.descripcion === "En el extranjero (solo si vives fuera de México)") {
                    $scope.properties.valoresSolicitante.catCampus = $scope.properties.valoresSolicitante.ciudadExamenPais.campus;
                    $scope.properties.valoresSolicitante.ciudadExamen = null;
                    $scope.properties.valoresSolicitante.catEstadoExamen = null;
                    if ($scope.properties.valoresSolicitante.catPaisExamen === null) {
                        $scope.properties.JSONTransferencia.paisexamen = $scope.properties.valoresSolicitante.catPaisExamen;
                    } else {
                        $scope.properties.JSONTransferencia.paisexamen = $scope.properties.valoresSolicitante.catPaisExamen.persistenceId;
                    }
                    if ($scope.properties.valoresSolicitante.ciudadExamenPais === null) {
                        $scope.properties.JSONTransferencia.ciudadpaisexamen = $scope.properties.valoresSolicitante.ciudadExamenPais;
                    } else {
                        $scope.properties.JSONTransferencia.ciudadpaisexamen = $scope.properties.valoresSolicitante.ciudadExamenPais.persistenceId;
                        $scope.properties.JSONTransferencia.estadoexamen = null;
                        $scope.properties.JSONTransferencia.ciudadexamen = null;
                    }
                }
                $scope.properties.JSONTransferencia.campusestudio = $scope.properties.valoresSolicitante.catCampusEstudio.persistenceId;
                $scope.properties.JSONTransferencia.campus = $scope.properties.valoresSolicitante.catCampus.persistenceId;
                if ($scope.properties.valoresSolicitante.catCampusEstudio.descripcion !== $scope.properties.jsonOriginal.campussede) {
                    existecambio = true;
                    isTransferencia = true;
                }
                if ($scope.properties.valoresSolicitante.catLicenciatura.nombre !== $scope.properties.jsonOriginal.licenciatura) {
                    existecambio = true;
                }
                if ($scope.properties.valoresSolicitante.catPropedeutico === null) {
                    if ($scope.properties.valoresSolicitante.propedeutico !== $scope.properties.jsonOriginal.propedeutico) {
                        existecambio = true;
                    }
                } else {
                    if ($scope.properties.valoresSolicitante.catPropedeutico.descripcion !== $scope.properties.jsonOriginal.propedeutico) {
                        existecambio = true;
                    }
                }
                if ($scope.properties.valoresSolicitante.periodo.descripcion !== $scope.properties.jsonOriginal.ingreso) {
                    existecambio = true;
                }
                if ($scope.properties.valoresSolicitante.catLugarExamen.descripcion !== $scope.properties.jsonOriginal.lugarexamen) {
                    existecambio = true;
                }
                if ($scope.properties.valoresSolicitante.catEstadoExamen === null) {
                    if ($scope.properties.valoresSolicitante.estadoexamen !== $scope.properties.jsonOriginal.estadoexamen) {
                        existecambio = true;
                    }
                } else {
                    if ($scope.properties.valoresSolicitante.catEstadoExamen.descripcion !== $scope.properties.jsonOriginal.estadoexamen) {
                        existecambio = true;
                    }
                }
                if ($scope.properties.valoresSolicitante.ciudadExamen === null) {
                    if ($scope.properties.valoresSolicitante.ciudadestado !== $scope.properties.jsonOriginal.ciudadestado) {
                        existecambio = true;
                    }
                } else {
                    if ($scope.properties.valoresSolicitante.ciudadExamen.descripcion !== $scope.properties.jsonOriginal.ciudadestado) {
                        existecambio = true;
                    }
                }

                if ($scope.properties.valoresSolicitante.catPaisExamen === null) {
                    if ($scope.properties.valoresSolicitante.paisexamen !== $scope.properties.jsonOriginal.paisexamen) {
                        existecambio = true;
                    }
                } else {
                    if ($scope.properties.valoresSolicitante.catPaisExamen.descripcion !== $scope.properties.jsonOriginal.paisexamen) {
                        existecambio = true;
                    }
                }
                if ($scope.properties.valoresSolicitante.ciudadExamenPais === null) {
                    if ($scope.properties.valoresSolicitante.ciudadpais !== $scope.properties.jsonOriginal.ciudadpais) {
                        existecambio = true;
                    }
                } else {
                    if ($scope.properties.valoresSolicitante.ciudadExamenPais.descripcion !== $scope.properties.jsonOriginal.paisexamen) {
                        existecambio = true;
                    }
                }
                $scope.properties.JSONTransferencia.campusestudio = $scope.properties.valoresSolicitante.catCampusEstudio.persistenceId;
                if ($scope.properties.valoresSolicitante.catCampus.descripcion !== $scope.properties.jsonOriginal.campus) {
                    existecambio = true;
                    isTransferencia = true;
                }
                if(!isTransferencia){
                  swal("¡Aviso!", "No se realizó ninguna transferencia, para realizar cambios a los datos del aspirante debe ir al modulo de usuarios registrados.", "warning");
                }else{
                  if (!existecambio) {
                    swal("¡Aviso!", "No se realizó ninguna modificación, por tanto, no se realizará la transferencia", "warning");
                } else {
                    var jsonAnterior = {};
                    var jsonNuevo = {};
                    jsonAnterior.campus = $scope.properties.jsonOriginal.campussede;
                    jsonAnterior.licenciatura = $scope.properties.jsonOriginal.licenciatura;
                    jsonAnterior.propedeutico = $scope.properties.jsonOriginal.propedeutico;
                    jsonAnterior.periodo = $scope.properties.jsonOriginal.ingreso;
                    jsonAnterior.lugarexamen = $scope.properties.jsonOriginal.lugarexamen;
                    jsonAnterior.estadoexamen = $scope.properties.jsonOriginal.estadoexamen;
                    jsonAnterior.ciudadexamen = $scope.properties.jsonOriginal.ciudadestado;
                    jsonAnterior.paisexamen = $scope.properties.jsonOriginal.paisexamen;
                    jsonAnterior.ciudadpais = $scope.properties.jsonOriginal.ciudadpais;
                    jsonAnterior.campusestudio = $scope.properties.jsonOriginal.campus;
                    jsonNuevo.campus = $scope.properties.valoresSolicitante.catCampus.descripcion;
                    jsonNuevo.licenciatura = $scope.properties.valoresSolicitante.catLicenciatura.nombre;
                    if ($scope.properties.valoresSolicitante.catPropedeutico === null) {
                        jsonNuevo.propedeutico = $scope.properties.valoresSolicitante.catPropedeutico
                    } else {
                        jsonNuevo.propedeutico = $scope.properties.valoresSolicitante.catPropedeutico.descripcion
                    }
                    jsonNuevo.periodo = $scope.properties.valoresSolicitante.periodo.descripcion;
                    jsonNuevo.lugarexamen = $scope.properties.valoresSolicitante.catLugarExamen.descripcion;
                    if ($scope.properties.valoresSolicitante.catEstadoExamen === null) {
                        jsonNuevo.estadoexamen = $scope.properties.valoresSolicitante.catEstadoExamen
                    } else {
                        jsonNuevo.estadoexamen = $scope.properties.valoresSolicitante.catEstadoExamen.descripcion
                    }
                    if ($scope.properties.valoresSolicitante.ciudadExamen === null) {
                        jsonNuevo.ciudadexamen = $scope.properties.valoresSolicitante.ciudadExamen
                    } else {
                        jsonNuevo.ciudadexamen = $scope.properties.valoresSolicitante.ciudadExamen.descripcion
                    }
                    if ($scope.properties.valoresSolicitante.catPaisExamen === null) {
                        jsonNuevo.paisexamen = $scope.properties.valoresSolicitante.catPaisExamen
                    } else {
                        jsonNuevo.paisexamen = $scope.properties.valoresSolicitante.catPaisExamen.descripcion
                    }
                    if ($scope.properties.valoresSolicitante.ciudadExamenPais === null) {
                        jsonNuevo.ciudadpais = $scope.properties.valoresSolicitante.ciudadExamenPais
                    } else {
                        jsonNuevo.ciudadpais = $scope.properties.valoresSolicitante.ciudadExamenPais.descripcion
                    }
                    jsonNuevo.campusestudio = $scope.properties.valoresSolicitante.catCampusEstudio.descripcion;
                    
                    $scope.properties.JSONTransferencia.valororginal = JSON.stringify(jsonAnterior);
                    $scope.properties.JSONTransferencia.valorcambio = JSON.stringify(jsonNuevo);
                    $scope.properties.JSONTransferencia.usuario = $scope.properties.usuario.user_name;
                    var nombreaspirante = "";
                    if($scope.properties.valoresSolicitante.segundonombre === ""){
                        nombreaspirante = $scope.properties.valoresSolicitante.primernombre + " " + $scope.properties.valoresSolicitante.apellidopaterno + " " + $scope.properties.valoresSolicitante.apellidomaterno;
                    }else{
                        nombreaspirante = $scope.properties.valoresSolicitante.primernombre + " " + $scope.properties.valoresSolicitante.segundonombre + " " + $scope.properties.valoresSolicitante.apellidopaterno + " " + $scope.properties.valoresSolicitante.apellidomaterno;
                    }
                    $scope.properties.JSONTransferencia.campusAnterior = $scope.properties.jsonOriginal.campus
                    $scope.properties.JSONTransferencia.campusNuevo = $scope.properties.valoresSolicitante.catCampus.descripcion
                    $scope.properties.JSONTransferencia.aspirante = nombreaspirante;
                    $scope.properties.JSONTransferencia.correoaspirante = $scope.properties.valoresSolicitante.correoelectronico;
                    console.log($scope.properties.JSONTransferencia);
                    $scope.properties.estadoSeleccionado = undefined;
                    doRequest($scope.properties.action, $scope.properties.url);
                }

                }
                

            }
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
        blockUI.start();
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
                getLstAspirantesTransferencia("POST", $scope.properties.urlPost);

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

    function getLstAspirantesTransferencia(method, url, params) {
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.jsonenviar),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.lstContenido = data.data;
                swal("¡Aspirante transferido correctamente!", "Se ha transferido al aspirante correctamente", "success");
            })
            .error(function(data, status) {
                notifyParentFrame({
                    message: 'error',
                    status: status,
                    dataFromError: data,
                    dataFromSuccess: undefined,
                    responseStatusCode: status
                });
            })
            .finally(function() {
                blockUI.stop();
                closeModal($scope.properties.closeOnSuccess);
            });
    }

}