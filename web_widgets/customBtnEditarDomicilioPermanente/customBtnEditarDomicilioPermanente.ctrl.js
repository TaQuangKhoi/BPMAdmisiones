


function PbButtonCtrl($scope, modalService, $http, blockUI, $q, $filter) {
var vm = this;

  this.action = function action() {
    debugger
    if($scope.properties.banderaEdicion == "" || $scope.properties.banderaEdicion == undefined ){
        
        $scope.properties.banderaEdicion = true;
        console.log($scope.properties.banderaEdicion);    
    }else if($scope.properties.banderaEdicion == true ){
        $scope.properties.banderaEdicion = false
    }
     

      
  };

}

