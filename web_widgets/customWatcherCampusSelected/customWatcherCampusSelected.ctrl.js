function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {
        var vm = this;
        
    $scope.$watchCollection('properties.campusseleccionado', function (items) {
        if($scope.properties.campusseleccionado !==undefined){
            
            if($scope.properties.campusseleccionado=== "Juan Pablo II México" || $scope.properties.campusseleccionado=== "Juan Pablo II Guadalajara"){
                $scope.properties.valorRadio=1;
                $scope.properties.deshabilitar=true;
            }else{
                $scope.properties.valorRadio=0;
                $scope.properties.deshabilitar=false;
            }
            
        }
    });
    
  



    
}