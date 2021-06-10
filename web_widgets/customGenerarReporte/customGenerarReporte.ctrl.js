function PbImageButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService,blockUI) {

  'use strict';

  var vm = this;
    this.action = function action() {
        var url="";
        if($scope.properties.reporte=='Lexium'){
            url="/bonita/API/extension/AnahuacRest?url=generarReporte&p=0&c=9999";
        }else if($scope.properties.reporte=='Admitidos al propedéutico'){
            url="/bonita/API/extension/AnahuacRest?url=generarReporteAdmitidosPropedeutico&p=0&c=9999"
        }else{
            url="/bonita/API/extension/AnahuacRest?url=generarReporteResultadosExamenes&p=0&c=9999"
        }
        doRequest("POST",url,null,$scope.properties.dataToSend,function(data){
          if($scope.properties.fileExtension === "xls"){
          const blob = b64toBlob(data.data[0]);
            const blobUrl = URL.createObjectURL(blob);
            window.open(blobUrl, '_blank');
            // window.location = blobUrl;
          }else{
              fakeLink(data.data[1])
          }
        })
    }
    function b64toBlob(dataURI) {

      var byteString = atob(dataURI);
      var ab = new ArrayBuffer(byteString.length);
      var ia = new Uint8Array(ab);
      let contentType = "text/plain";
      if($scope.properties.fileExtension === "xls"){
        contentType = "application/vnd.ms-excel";
      }
      for (var i = 0; i < byteString.length; i++) {
          ia[i] = byteString.charCodeAt(i);
      }
      return new Blob([ab], { type: contentType });
  }
  function fakeLink(rua){
    const linkSource = `data:text/plain;base64,${rua}`;
    const downloadLink = document.createElement("a");
    const fileName = "kwafile.rua";

    downloadLink.href = linkSource;
    downloadLink.download = fileName;
    downloadLink.click();
  }
  /**
   * Execute a get/post request to an URL
   * It also bind custom data from success|error to a data
   * @return {void}
   */
  function doRequest(method, url, params,dataToSend,callback) {
    vm.busy = true;
    var req = {
      method: method,
      url: url,
      data: angular.copy(dataToSend),
      params: params
    };

    return $http(req)
      .success(function(data, status) {
          callback(data);
      })
      .error(function(data, status) {console.errror(data)})
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


}
