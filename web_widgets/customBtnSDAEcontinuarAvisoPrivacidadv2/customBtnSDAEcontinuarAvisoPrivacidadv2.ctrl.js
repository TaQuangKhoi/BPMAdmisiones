function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

  this.action = function action() {
      debugger
    if ($scope.properties.aceptoAvisoPrivacidad == true) {
        startProcess();
        //var url= "/portal/resource/app/sdae/solicitudApoyoEducativo/content/"
        //window.location.replace(url); 
    }else{
         swal("¡Aviso!", "Para continuar debe aceptar el aviso de privacidad.", "warning"); 
    }
  };
  
  function startProcess() {
    //var id = getUrlParam('id');
    if ($scope.properties.idProceso) {
      var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.idProceso + '/instantiation', getUserParam()).then(function() {
        localStorageService.delete($window.location.href);
      });

    } else {
      $log.log('Impossible to retrieve the process definition id value from the URL');
    }
  }
  
  function getUserParam() {
    var userId = getUrlParam('user');
    if (userId) {
      return { 'user': userId };
    }
    return {};
  }
  
  function getUserParam() {
    var userId = getUrlParam('user');
    if (userId) {
      return { 'user': userId };
    }
    return {};
  }


}
