function PbTableCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
  

  this.isArray = Array.isArray;
  var vm = this;
  
  this.isClickable = function() {
      return $scope.properties.isBound('selectedRow');
  };

  this.selectRow = function(row) {
      if (this.isClickable()) {
          $scope.properties.selectedRow = row;
      }
  };

  this.isSelected = function(row) {
      return angular.equals(row, $scope.properties.selectedRow);
  }

  this.showdatos = function(row) {
      $scope.properties.datomodificar = angular.copy(row);
      $scope.properties.ver = true;
      $scope.properties.datosEditar = row;
      openModal($scope.properties.modalidContactoEmergencia);
      openModal($scope.properties.modalid);

  }

  this.editdatos = function(row) {
      $scope.properties.datomodificar = angular.copy(row);
      console.log($scope.properties.datomodificar);
      $scope.properties.ocultar = true;
      $scope.properties.ver = false;
      $scope.properties.datosEditar = row;
      openModalEditar($scope.properties.modalid);
      
  }


  $scope.deleteData = function(row, index) {
      
          $scope.properties.eliminar.splice(index, 1);
          console.log($scope.properties.eliminar + index);
  }
  
  
  function openModal(modalid,modalidContactoEmergencia) {

      modalService.open(modalid);
      $scope.properties.MostrarBotones = true;
      $scope.properties.BanderaInformacionEmergencia = true;
  }
  
  function openModalEditar(modalid,modalidContactoEmergencia) {

      modalService.open(modalid);
      $scope.properties.MostrarBotones = false;
      $scope.properties.BanderaInformacionEmergencia = false;
  }

  function closeModal(shouldClose) {
      if (shouldClose)
          modalService.close();
  }

  $scope.$watch('properties.casosDeEmergencia', function (item) {
    debugger
    if ($scope.properties.casosDeEmergencia.length > 0) {
      let params = new URLSearchParams(location.search);
        $scope.caseidvar = params.get('caseId');
        $scope.Caseid = parseInt(caseidvar);

        $scope.getContactosEmergencia();
    }

});

$scope.getContactosEmergencia = function  doRequest(method, url, params) {
  debugger
  vm.busy = true;
  var req = {
    method: method,
    url: "../API/extension/AnahuacRestGet?url=getLstContactosEmergencia&caseid="+ $scope.caseidvar,
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
  
function notifyParentFrame(additionalProperties) {
  if ($window.parent !== $window.self) {
    var dataToSend = angular.extend({}, $scope.properties, additionalProperties);
    $window.parent.postMessage(JSON.stringify(dataToSend), '*');
  }
}

}