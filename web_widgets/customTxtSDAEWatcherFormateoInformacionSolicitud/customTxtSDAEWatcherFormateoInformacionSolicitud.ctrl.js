function fnctnCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    $scope.$watch("properties.objPadresTutor", function(newValue, oldValue) {

        if (newValue !== undefined) {
             $scope.loadContextCaseId();
             console.log($scope.properties.objPadresTutor);
        }
    });

   
    $scope.loadContextCaseId = function() {
        for (var i = 0; i < $scope.properties.objPadresTutor.length; i++) {
         if($scope.properties.objPadresTutor[i].isTutor){
             $scope.properties.objTutor = $scope.properties.objPadresTutor[i];
         }
         if($scope.properties.objPadresTutor[i].catParentezco.descripcion == 'Padre'){
             $scope.properties.objPadre = $scope.properties.objPadresTutor[i];
         }else if($scope.properties.objPadresTutor[i].catParentezco.descripcion == 'Madre'){
             $scope.properties.objMadre = $scope.properties.objPadresTutor[i];
         }
         
        }
          
    }
}