function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      $scope.properties.value = $scope.properties.texto;
      $scope.properties.view = false;
      $scope.properties.strAlumno = {
            "decision": "",
            "observaciones": "",
            "pdp_1": "",
            "pdu_1": "",
            "sse_1": "",
            "pcda_1": "",
            "pca_1": "",
            "IDBANNER":  $scope.properties.idBanner,
            "periodo": $scope.properties.periodo
        };
      
    };
    
    


}
