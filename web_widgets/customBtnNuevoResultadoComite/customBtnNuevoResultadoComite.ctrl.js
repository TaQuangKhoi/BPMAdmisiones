function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      $scope.properties.value = $scope.properties.texto;
      $scope.properties.view = false;
      $scope.properties.strAlumno = {
            "decision": "",
            "observaciones": "",
            "PDP_1": "No",
            "PDU_1": "No",
            "SSE_1": "No",
            "PCDA_1": "No",
            "PCA_1": "No",
            "IDBANNER":  $scope.properties.idBanner,
            "Periodo": $scope.properties.periodo,
            "isPropedeutico":false
        };
      
    };
    
    


}
