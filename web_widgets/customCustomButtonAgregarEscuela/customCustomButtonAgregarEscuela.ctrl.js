function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    this.action = function action() {
        if ($scope.properties.action === 'Remove from collection') {
            removeFromCollection();
            closeModal($scope.properties.closeOnSuccess);
        } else if ($scope.properties.action === 'Add to collection') {
            if($scope.validarEscuela()){
                addToCollection();
                closeModal($scope.properties.closeOnSuccess);
            } 
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

    
    $scope.validarEscuela = function(){
        let isValid = true;
        let errorMessage = "";
        let titleMessage = "";
        let topYear = new Date().getFullYear() + 1;
        let currentYear = new Date().getFullYear();
        let promedioRegex = /(?<![\d.])(\d{1,2}|\d{0,2}\.\d{1})?(?![\d.])/;

        if($scope.properties.infoEscuela.grado === null){
            isValid = false;
            titleMessage = "¡Grado escolar!";
            errorMessage = "Selecciona un valor.";
        } else if($scope.properties.infoEscuela.grado.descripcion === "Preparatoria/Bachillerato" && ($scope.properties.infoEscuela.escuela === null && $scope.properties.infoEscuela.otraEscuela === "")){
            isValid = false;
            titleMessage = "¡Escuela!";
            errorMessage = "Captura el nombre de la escuela.";
        } else if($scope.properties.infoEscuela.grado.descripcion !== "Preparatoria/Bachillerato" && ($scope.properties.infoEscuela.otraEscuela === undefined || $scope.properties.infoEscuela.otraEscuela === "")){
            isValid = false;
            titleMessage = "¡Escuela!";
            errorMessage = "Captura el nombre de la escuela.";
        } else if ($scope.properties.infoEscuela.pais === null){
            isValid = false;
            titleMessage = "¡País!";
            errorMessage = "Captura el País de tu escuela.";
        } else if ($scope.properties.infoEscuela.pais.descripcion === "México" && $scope.properties.infoEscuela.estado === null){
            isValid = false;
            titleMessage = "¡Estado!";
            errorMessage = "Captura el Estado de tu escuela.";
        } else if ($scope.properties.infoEscuela.pais.descripcion !== "México" && $scope.properties.infoEscuela.estado === ""){
            isValid = false;
            titleMessage = "¡Estado!";
            errorMessage = "Captura el Estado de tu escuela.";
        } else if ($scope.properties.infoEscuela.ciudad === ""){
            isValid = false;
            titleMessage = "¡Ciudad/Municipio/Delegación/Poblado!";
            errorMessage = "Captura la Ciudad de tu escuela.";
        } else if($scope.properties.infoEscuela.anoInicio === undefined || $scope.properties.infoEscuela.anoInicio === ""){
            isValid = false;
            titleMessage = "¡Año inicio!";
            errorMessage = "Captura el año inicio de tu escuela.";
        } else if($scope.properties.infoEscuela.anoInicio > currentYear){
            isValid = false;
            titleMessage = "¡Año inicio!";
            errorMessage = "El año inicio no puede ser posterior al año actual.";
        } else if($scope.properties.infoEscuela.anoFin === undefined || $scope.properties.infoEscuela.anoFin === ""){
            isValid = false;
            titleMessage = "¡Año Fin!";
            errorMessage = "Captura el año en que finalizaste tu escuela.";
        } else if (parseInt($scope.properties.infoEscuela.anoFin) < parseInt($scope.properties.infoEscuela.anoInicio)){
            isValid = false;
            titleMessage = "¡Año fin!";
            errorMessage = "El año de fin no puede ser anterior al año de inicio.";
        } else if($scope.properties.infoEscuela.anoFin > topYear && $scope.properties.infoEscuela.grado.descripcion === "Preparatoria/Bachillerato"){
            isValid = false;
            titleMessage = "¡Año Fin!";
            errorMessage = "El año fin no puede superar al año actual.";
        } else if($scope.properties.infoEscuela.anoFin > currentYear && $scope.properties.infoEscuela.grado.descripcion !== "Preparatoria/Bachillerato"){
            isValid = false;
            titleMessage = "¡Año Fin!";
            errorMessage = "El año fin no puede superar por mas de un año al año actual.";
        } else if($scope.properties.infoEscuela.promedio === undefined || $scope.properties.infoEscuela.promedio === ""){
            isValid = false;
            titleMessage = "¡Promedio!";
            errorMessage = "El campo Promedio no puede ir vacío";
        } else if($scope.properties.infoEscuela.promedio > 10 || !promedioRegex.test($scope.properties.infoEscuela.promedio)){
            isValid = false;
            titleMessage = "¡Promedio!";
            errorMessage = "El promedio no es válido, por favor captura un número del 1 al 10. Puedes incluir un decimal.";
        } 
    
        if(!isValid){
            swal(titleMessage, errorMessage, "warning");
        }
        
        return isValid;
    }

    function openModal(modalId) {
        modalService.open(modalId);
    }

    function closeModal(shouldClose) {
        if(shouldClose)
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
        
        var item = angular.copy($scope.properties.infoEscuela);

        if ($scope.properties.collectionPosition === 'First') {
            $scope.properties.collectionToModify.unshift(item);
        } else {
            if($scope.properties.saveAction === "Agregar"){
                let maxIndex = 0;
                if($scope.properties.collectionToModify.length > 0){
                    if(item.grado.descripcion === "Primaria"){
                        for(let i = 0; i < $scope.properties.collectionToModify.length; i++){
                            if($scope.properties.collectionToModify[i].grado.descripcion === "Primaria"){
                                maxIndex = i;
                            }
                        }
                        $scope.properties.collectionToModify.splice(maxIndex, 0, item);
                    } else if(item.grado.descripcion === "Secundaria"){
                        for(let i = 0; i < $scope.properties.collectionToModify.length; i++){
                            if($scope.properties.collectionToModify[i].grado.descripcion === "Secundaria" || $scope.properties.collectionToModify[i].grado.descripcion === "Primaria"){
                                maxIndex = i;
                            }
                        }
                        $scope.properties.collectionToModify.splice(maxIndex, 0, item);
                    } else if(item.grado.descripcion === "Preparatoria/Bachillerato"){
                        $scope.properties.collectionToModify.push(item);
                        // $scope.properties.collectionToModify.splice(maxIndex, 0, item);
                    }
                } else {
                    $scope.properties.collectionToModify.push(item);
                }
                
                // $scope.properties.collectionToModify.push(item);
            } else {
                $scope.properties.collectionToModify.splice(item.index, 1, item);
            }

            $scope.properties.infoEscuela = {
                "grado": null,
                "tipo": null,
                "escuela": "",
                "pais": null,
                "estado": null, 
                "ciudad": "",
                "anoInicio": "",
                "anoFin": "",
                "promedio": "",
                "catBachillerato": null,
                "otraEscuela": ""
            };

            closeModal(true);
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
            notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status});
            if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                redirectIfNeeded();
            }
            closeModal($scope.properties.closeOnSuccess);
        })
        .error(function(data, status) {
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status});
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