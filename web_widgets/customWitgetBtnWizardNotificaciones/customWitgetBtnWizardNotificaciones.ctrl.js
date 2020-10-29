function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
   
    $scope.action = function(){
        if($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0){
            $scope.properties.selectedIndex --; 
        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)){
            if($scope.properties.selectedIndex === 0){
                console.log("validar 0");
            }else if($scope.properties.selectedIndex === 1){
                console.log("validar 1");
            }else if($scope.properties.selectedIndex === 2){
                console.log("validar 2");
            }else if($scope.properties.selectedIndex === 3){
                console.log("validar 3");
            }else if($scope.properties.selectedIndex === 4){
                console.log("validar 4");
            }
            $scope.properties.selectedIndex ++; 
        }
    }
}