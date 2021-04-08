function PbButtonCtrl($scope, $http, $location,  $window) {

    'use strict';

    var vm = this;
    $scope.psicologo={}
    $scope.sesiones = [];
    this.action = function action() {
        doRequest("GET","/bonita/API/extension/AnahuacRestGet?url=getPsicologoSesiones&p=0&c=9999&jsonData="+$scope.psicologo.id,null,null,function(datos){
            $scope.sesiones = datos;
        })
    };

    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params, dataToSend, retorno) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                retorno(data);
            })
            .error(function(data, status) {

                console.error(data)
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

    $scope.$watch('properties.campusSelected', function(value) {
        if (angular.isDefined(value) && value !== null) {
            var filtro = [{ "columna": "ROL", "operador": "Igual a", "valor": "PSICOLOGO" }, { "columna": "ROL", "operador": "Igual a", "valor": "PSICOLOGO SUPERVISOR" }];
            filtro.push({
                "columna": "GRUPO",
                "operador": "Igual a",
                "valor": $scope.properties.campusSelected.grupoBonita
            })
            doRequest("GET", `/bonita/API/extension/AnahuacRestGet?url=getUserBonita&p=0&c=9999&jsonData=${encodeURIComponent(JSON.stringify({ "estatusSolicitud": "Cat campus", "tarea": "Cat Campus", "lstFiltro": filtro, "type": "solicitudes_progreso", "usuario": "Administrador", "orderby": "", "orientation": "DESC", "limit": 999, "offset": 0 }))}`,  null, null, function(datos, extra) {
                $scope.lstResponsables = datos.data;
            });
        }
    });

}