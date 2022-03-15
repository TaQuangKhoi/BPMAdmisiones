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
    } else if ($scope.properties.action) {
      debugger

      if($scope.properties.jsonTutor[0].catParentezco.persistenceId != 0){
        $scope.catParentezco_pid = $scope.properties.jsonTutor[0].catParentezco.persistenceId;
      }else{
        $scope.catParentezco_pid = parseInt($scope.properties.jsonTutor[0].catParentezco.persistenceId_string);
      } 
 
    $scope.ObjetoInformacionTutor = {
      "titulo_pid": $scope.properties.jsonTutor[0].catTitulo.persistenceId,
      "nombre": $scope.properties.jsonTutor[0].nombre,
      "apellidos":$scope.properties.jsonTutor[0].apellidos,
      "parentesco_pid": $scope.catParentezco_pid,
      "otroParentesco": "",
      "correoElectronico": $scope.properties.jsonTutor[0].correoElectronico,
      "escolaridad_pid": $scope.properties.jsonTutor[0].catEscolaridad.persistenceId,
      "egresoAnahuac_pid": $scope.properties.jsonTutor[0].catEgresoAnahuac.persistenceId,
      "trabaja_pid": $scope.properties.jsonTutor[0].catTrabaja.persistenceId,
      "empresaTrabaja": $scope.properties.jsonTutor[0].empresaTrabaja,
      "giro": $scope.properties.jsonTutor[0].giroEmpresa,
      "puesto": $scope.properties.jsonTutor[0].puesto,
      "pais_pid": $scope.properties.jsonTutor[0].catPais.persistenceId,
      "codigoPostal": $scope.properties.jsonTutor[0].codigoPostal,
      "estadoExtranjero": $scope.properties.jsonTutor[0].estadoExtranjero,
      "estado_pid": $scope.properties.jsonTutor[0].catEstado.persistenceId,
      "ciudad": $scope.properties.jsonTutor[0].ciudad,
      "delegacionMunicipio": $scope.properties.jsonTutor[0].delegacionMunicipio,
      "colonia": $scope.properties.jsonTutor[0].colonia,
      "calle": $scope.properties.jsonTutor[0].calle,
      "numExterior": $scope.properties.jsonTutor[0].numeroExterior,
      "numInterior": $scope.properties.jsonTutor[0].numeroInterior,       
      "telefono": $scope.properties.jsonTutor[0].telefono,
      "caseid": $scope.properties.jsonTutor[0].caseId,
      "persistenceid": $scope.properties.jsonTutor[0].persistenceId
      
      }
      
      doRequest($scope.properties.action);
    }
  };

  function openModal(modalId) {
    modalService.open(modalId);
  }

  function closeModal(shouldClose) {
    if(shouldClose)
    {modalService.close();}
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

  function doRequest(method, url, params) {
    debugger
    vm.busy = true;
    var req = {
      method: method,
      url: "../API/extension/AnahuacRest?url=updateViewDownloadSolicitud&p=0&c=100&&key=IT&intento=null&tipoTabla=true",
        
      data: angular.copy($scope.ObjetoInformacionTutor),
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
