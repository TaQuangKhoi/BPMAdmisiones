function PbInputCtrl($scope, $log, widgetNameFactory) {

  'use strict';

  this.name = widgetNameFactory.getName('pbInput');

  if (!$scope.properties.isBound('value')) {
    $log.error('the pbInput property named "value" need to be bound to a variable');
  }
  
   $scope.forceKeyPressUppercase = function(e) {
        
        var charInput = e.keyCode;
            var limite = $scope.properties.maxLength === 0 ? 250 : $scope.properties.maxLength;
            if ((charInput >= 48) && (charInput <= 57) && (e.target.value.length) < limite) {
                if ($scope.properties.value >= $scope.properties.max) {
                    $scope.properties.value = null;
                    $scope.properties.value = $scope.properties.max;
                }
            } else {
                if ($scope.properties.value >= $scope.properties.max) {
                    $scope.properties.value = null;
                    $scope.properties.value = $scope.properties.max;
                }
            }
    }
    
     $scope.$watch("properties.value", function() {
        
       if($scope.properties.value > 100){
               $scope.properties.value = null;
            }else if($scope.properties.value < 0){
                $scope.properties.value = null;
            }
            if ($scope.properties.value % 1 != 0) {
                       $scope.properties.value = null;
                    } 
    });
}
