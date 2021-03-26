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
            if($scope.properties.dataToChange2.lstCatNacionalidadInput !== undefined){
                validarNuevaNacionalidad($scope.properties.dataToChange2);
            } else {
                validarEditarNacionalidad($scope.properties.dataToChange2);
            }
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
        if ($scope.properties.processId) {
            var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function() {
                doRequest("GET", $scope.properties.url).then(function() {
                    $scope.properties.dataToChange = $scope.properties.dataToSet;
                    $scope.properties.dataToChange2 = $scope.properties.dataToSet2;
                });
                localStorageService.delete($window.location.href);
            });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
        }
    }

    function validarEditarNacionalidad(_valor){
        let isValid = true;
        let errorMessage = "";

        if(_valor.id === "" || _valor.id === null || _valor.id === undefined){
            isValid = false;
            errorMessage = "Faltó capturar información en: ID banner";
         } else if(_valor.orden === null || _valor.orden === undefined){
            isValid = false;
            errorMessage = "Faltó capturar información en: Orden";
         } else if(_valor.clave === "" || _valor.clave === null || _valor.clave === undefined){
           isValid = false;
           errorMessage = "Faltó capturar información en: Clave";
        } else if(_valor.descripcion === "" || _valor.descripcion === null || _valor.descripcion === undefined){
           isValid = false;
           errorMessage = "Faltó capturar información en: Descripción";
        } 
        // else if(_valor.usuarioBanner === "" || _valor.usuarioBanner === null || _valor.usuarioBanner === undefined){
        //    isValid = false;
        //    errorMessage = "Faltó capturar información en: Usuario banner";
        // } 

       if(!isValid){
           swal("¡Aviso!", errorMessage, "warning");
       } else {
           checkclave("editar");
       }
    }

    function validarNuevaNacionalidad(_valor){
        
        let isValid = true;
        let errorMessage = "";

        if(_valor.lstCatNacionalidadInput[0].id === "" || _valor.lstCatNacionalidadInput[0].id === null || _valor.lstCatNacionalidadInput[0].id === undefined){
            isValid = false;
            errorMessage = "Faltó capturar información en: ID banner";
        } else if(_valor.lstCatNacionalidadInput[0].orden === null || _valor.lstCatNacionalidadInput[0].orden === undefined){
            isValid = false;
            errorMessage = "Faltó capturar información en: Orden";
        } else if(_valor.lstCatNacionalidadInput[0].clave === "" || _valor.lstCatNacionalidadInput[0].clave === null || _valor.lstCatNacionalidadInput[0].clave === undefined){
            isValid = false;
            errorMessage = "Faltó capturar información en: Clave";
        } else if(_valor.lstCatNacionalidadInput[0].descripcion === "" || _valor.lstCatNacionalidadInput[0].descripcion === null || _valor.lstCatNacionalidadInput[0].descripcion === undefined){
            isValid = false;
            errorMessage = "Faltó capturar información en: Descripción";
        } 
        // else if(_valor.lstCatNacionalidadInput[0].usuarioBanner === "" || _valor.lstCatNacionalidadInput[0].usuarioBanner === null || _valor.lstCatNacionalidadInput[0].usuarioBanner === undefined){
        //     isValid = false;
        //     errorMessage = "Faltó capturar información en: Usuario banner";
        // } 

        if(!isValid){
            swal("¡Aviso!", errorMessage, "warning");
        } else {
            checkclave("agregar");
        }
    }

    function checkclave(funcion){
        if(funcion === 'agregar'){
            var req = {
                method: 'GET',
                url: `/API/extension/AnahuacRestGet?url=getValidarClave&p=0&c=10&tabla=CATNACIONALIDAD&clave=${$scope.properties.dataToChange2.lstCatNacionalidadInput[0].clave}&id=`
            };
        }else{
            var req = {
                method: 'GET',
                url: `/API/extension/AnahuacRestGet?url=getValidarClave&p=0&c=10&tabla=CATNACIONALIDAD&clave=${$scope.properties.dataToChange2.clave}&id=${$scope.properties.dataToChange2.persistenceId}`,
            };
        }
        
        return $http(req).success(function(data, status) {
            if (data.data[0]) {
                ckeckOrden(funcion);
            } else {
                swal("¡Aviso!", "La clave capturada ya existe, por favor ingrese una diferente.", "warning");
            }
        })
        .error(function(data, status) {
            console.log(data);
            console.log(status);
            swal("¡Error!", "Error al realizar la validación de los datos. Por favor intente de nuevo mas tarde.", "error");
        })
    }

    function ckeckOrden(funcion){
        if(funcion === 'agregar'){
            var req = {
                method: 'GET',
                url: `/API/extension/AnahuacRestGet?url=getValidarOrden&p=0&c=10&tabla=CATNACIONALIDAD&orden=${$scope.properties.dataToChange2.lstCatNacionalidadInput[0].orden}&id=`
            };
        }else{
            var req = {
                method: 'GET',
                url: `/API/extension/AnahuacRestGet?url=getValidarOrden&p=0&c=10&tabla=CATNACIONALIDAD&orden=${$scope.properties.dataToChange2.orden}&id=${$scope.properties.dataToChange2.persistenceId}`,
            };
        }
        
        return $http(req).success(function(data, status) {
            if (data.data[0]) {
                // ckeckIdBanner(funcion);
                startProcess();
            } else {
                swal("¡Aviso!", "El orden capturado ya existe, por favor ingrese una diferente.", "warning");
            }
        })
        .error(function(data, status) {
            console.log(data);
            console.log(status);
            swal("¡Error!", "Error al realizar la validación de los datos. Por favor intente de nuevo mas tarde.", "error");
        })
    }

    function ckeckIdBanner(funcion){
        if(funcion === 'agregar'){
            var req = {
                method: 'GET',
                url: `/API/extension/AnahuacRestGet?url=getValidarIdBanner&p=0&c=10&tabla=CATNACIONALIDAD&idBanner=${$scope.properties.dataToChange2.lstCatNacionalidadInput[0].id}&id=`
            };
        }else{
            var req = {
                method: 'GET',
                url: `/API/extension/AnahuacRestGet?url=getValidarIdBanner&p=0&c=10&tabla=CATNACIONALIDAD&idBanner=${$scope.properties.dataToChange2.id}&id=${$scope.properties.dataToChange2.persistenceId}`,
            };
        }
        
        return $http(req).success(function(data, status) {
            if (data.data[0]) {
                startProcess();
            } else {
                swal("¡Aviso!", "El ID banner capturado ya existe, por favor ingrese una diferente.", "warning");
            }
        })
        .error(function(data, status) {
            console.log(data);
            console.log(status);
            swal("¡Error!", "Error al realizar la validación de los datos. Por favor intente de nuevo mas tarde.", "error");
        })
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

        return $http(req).success(function(data, status) {
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