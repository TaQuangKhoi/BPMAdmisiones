function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;
    var validarPeriodoDisponible;
    var validarInscripcionagosto;
    var validarInscripcionenero;
    var validarInscripcionmayo;
    var validarInscripcionseptiembre;
    var validarPropedeuticos;
    var validarClave;
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
        if ($scope.properties.dataToChange2[0]) {
            let prom = doRequest("POST","../API/extension/AnahuacBecasRest?url=updateSDAEGestionEscolar&p=0&c=10",1,2).then(function() {
                if($scope.properties.dataToChange2[2]){
                    doRequest("POST","../API/extension/AnahuacBecasRest?url=updateSDAECreditoGE&p=0&c=10",3,2).then(function() {
                        console.log("termino update")
                    });
                }else{
                    doRequest("POST","../API/extension/AnahuacBecasRest?url=insertSDAECreditoGE&p=0&c=10",3,2).then(function() {
                        console.log("termino")
                    });
                }
                
            });
        }else {
            let prom = doRequest("POST","../API/extension/AnahuacBecasRest?url=insertSDAEGestionEscolar&p=0&c=10",1,1).then(function() {
                doRequest("POST","../API/extension/AnahuacBecasRest?url=insertSDAECreditoGE&p=0&c=10",3,2).then(function() {
                    console.log("termino")
                });
            });
            
        }
    }

    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, index,tipo) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToChange2[index]),
        };
       // params: params
        return $http(req).success(function(data, status) {
            if(tipo == 1 && index == 1){
                $scope.properties.dataToChange2[3][0].sdaecatgestionescolar_pid = parseInt(data.data[0]);
            }
            console.log(data);
            })
            .error(function(data, status) {
            
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

    function ValidateEmail(mail) {
        let res = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        return res.test(mail);
    }

    function validateURL(textval) {
        var urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/;
        return urlregex.test(textval);
    }

    function doRequestValidar(clave, id) {
        vm.busy = true;
        var req = {
            method: 'GET',
            url: `/API/extension/AnahuacRestGet?url=getValidarClave&p=0&c=10&tabla=CATGESTIONESCOLAR&clave=${clave}&id=${id}`,
        };

        return $http(req).success(function(data, status) {

                validarClave = data.data[0];
            })
            .error(function(data, status) {})
            .finally(function() {
                vm.busy = false;
            });
    }
}