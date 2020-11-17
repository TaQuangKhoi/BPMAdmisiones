function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';
    $scope.visible = false;
    var vm = this;

    $scope.setVal = function() {
        //$scope.properties.variableDestino = angular.copy($scope.properties.variableAcopiar);
        $scope.visible = true;
        doRequest("POST","/bonita/API/extension/AnahuacRest?url=generateHtml&p=0&c=10",{},$scope.properties.dataToSend,function(data){
                
                
                document.getElementById('iframe').contentWindow.document.write(data);
                //$scope.openCloseModal();
            })
        
    };

    /**
   * Execute a get/post request to an URL
   * It also bind custom data from success|error to a data
   * @return {void}
   */
  function doRequest(method, url, params, dataToSend, callback) {
    vm.busy = true;
    var req = {
      method: method,
      url: url,
      data: dataToSend,
      params: params
    };

    return $http(req)
      .success(function(data, status) {
          debugger;
        callback(data.data[0]);
      })
      .error(function(data, status) {
        console.error("error al llamar" + url);
          
      })
      .finally(function() {
        vm.busy = false;
      });
  }
}