function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
 
 this.mostrarModalMensaje = function mostrarModalMensaje() {
     
      actionMensaje();
    };

function actionMensaje() {
       
    closeModal($scope.properties.closeOnSuccess);
    openModal($scope.properties.modalIdMensaje);
      
    }

  function openModal(modalId) {
        
        modalService.open(modalId);
    }

    function closeModal(shouldClose) {
        if (shouldClose)
            modalService.close();
    }
}
