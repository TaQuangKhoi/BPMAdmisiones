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
      if($scope.properties.objcasosDeEmergencia[0].catParentesco.persistenceId == 145996){
            //Padre
            $scope.catCasodeEmergencia = 56; 
        
        }else if ($scope.properties.objcasosDeEmergencia[0].catParentesco.persistenceId == 145999){
            //Padre
            $scope.catCasodeEmergencia =  55;  
            
        }else if($scope.properties.objcasosDeEmergencia[0].catParentesco.persistenceId == 146002){
            //tutor
            $scope.catCasodeEmergencia =  54;
        }else{
            $scope.catCasodeEmergencia= 57;
        }
      $scope.properties.objcasosDeEmergencia;
    $scope.ObjetoDomicilioPermanente = {

        "nombre":$scope.properties.objcasosDeEmergencia[0].nombre,
        "parentesco":"",
        //"parentesco":$scope.properties.objcasosDeEmergencia[0].catParentesco.descripcion,
        "telefono":$scope.properties.objcasosDeEmergencia[0].telefono,
        "telefonoCelular": $scope.properties.objcasosDeEmergencia[0].telefonoCelular,
        "parentesco_pid":$scope.properties.objcasosDeEmergencia[0].catParentesco.persistenceId,
        "caseid":$scope.properties.objcasosDeEmergencia[0].caseId,
        "persistenceid": $scope.properties.objcasosDeEmergencia[0].persistenceId,
        "catCasoDeEmergencia_pid": $scope.catCasodeEmergencia
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
      url: "../API/extension/AnahuacRest?url=updateViewDownloadSolicitud&p=0&c=100&&key=CE&intento=null&tipoTabla=true",
        
      data: angular.copy($scope.ObjetoDomicilioPermanente),
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
