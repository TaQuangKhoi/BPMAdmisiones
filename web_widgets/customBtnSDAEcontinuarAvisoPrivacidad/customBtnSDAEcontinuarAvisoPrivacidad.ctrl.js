function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

  this.action = function action() {
      debugger
    if ($scope.properties.aceptoAvisoPrivacidad == true) {
        var url= "/portal/resource/app/sdae/solicitudApoyoEducativo/content/"
        window.location.replace(url); 
    }else{
         swal("¡Aviso!", "Para continuar debe aceptar el aviso de privacidad.", "warning"); 
    }
  };


}
