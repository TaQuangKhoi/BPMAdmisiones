function ($scope, $http) {
    
    $scope.$watch('properties.value', function(value) {
    if (angular.isDefined(value) && value !== null){  if($scope.properties.value){
            recoverydata();
        }  
    }
  });
  
   $scope.$watch('properties.processDefinitionId', function(value) {
    if (angular.isDefined(value) && value !== null){  if($scope.properties.value){
            recoverydata();
        }  
    }
  });
  
   $scope.$watch('properties.caseid', function(value) {
    if (angular.isDefined(value) && value !== null){  if($scope.properties.value){
            recoverydata();
        }  
    }
  });

    
function recoverydata() {
        var req = {
            method: "POST",
            url: "../API/extension/AnahuacRest?url=recoveryData&p=0&c=100",
            data: {"caseId":$scope.properties.caseid, "processDefinitionId":$scope.properties.processDefinitionId}
        };
        return $http(req).success(function(data, status) {
               window.location.reload(false);
            })
            .error(function(data, status) {
                console.error(data)
            })
            .finally(function() {

            });
    }
}