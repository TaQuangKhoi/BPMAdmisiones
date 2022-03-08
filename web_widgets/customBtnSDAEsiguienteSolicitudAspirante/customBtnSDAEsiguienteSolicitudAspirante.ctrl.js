function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

//   this.action = function action() {
//     if ($scope.properties.action === 'Remove from collection') {
//       removeFromCollection();
//       closeModal($scope.properties.closeOnSuccess);
//     } else if ($scope.properties.action === 'Add to collection') {
//       addToCollection();
//       closeModal($scope.properties.closeOnSuccess);
//     } else if ($scope.properties.action === 'Start process') {
//       startProcess();
//     } else if ($scope.properties.action === 'Submit task') {
//       submitTask();
//     } else if ($scope.properties.action === 'Open modal') {
//       closeModal($scope.properties.closeOnSuccess);
//       openModal($scope.properties.modalId);
//     } else if ($scope.properties.action === 'Close modal') {
//       closeModal(true);
//     } else if ($scope.properties.url) {
//       doRequest($scope.properties.action, $scope.properties.url);
//     }
//   };
  this.accion = function action() {
      debugger
    if ($scope.properties.accion == "siguiente") {
      $scope.properties.selectedIndex = $scope.properties.selectedIndex +1;
    } else {
      $scope.properties.selectedIndex = $scope.properties.selectedIndex -1;
    } 
  };

}
