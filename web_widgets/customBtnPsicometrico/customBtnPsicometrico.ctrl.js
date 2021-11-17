function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

  this.action = function action() {
      doRequest("POST", $scope.properties.url);
  };



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
          doRequest2("GET","../API/extension/AnahuacRestGet?url=getPsicometricoFinalizado&p=0&c=10&usuario="+$scope.properties.urlParametro);
        notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status});
      })
      .error(function(data, status) {
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


  
  function doRequest2(method, url) {
    vm.busy = true;
    var req = {
      method: method,
      url: url
    };

    return $http(req)
      .success(function(data, status) {
        $scope.properties.returnValues = data;
      })
      .error(function(data, status) {
        console.log("Error: "+data);
        
      })
      .finally(function() {
        vm.busy = false;
      });
  }

}
