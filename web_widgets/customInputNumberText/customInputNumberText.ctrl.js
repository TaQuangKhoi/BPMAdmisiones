function PbInputCtrl($scope, $log, widgetNameFactory) {

  'use strict';

  this.name = widgetNameFactory.getName('pbInput');

  if (!$scope.properties.isBound('value')) {
    $log.error('the pbInput property named "value" need to be bound to a variable');
  }
  
    $scope.checkBlur=function(){
        
        var rex= /^[0-9]{8}$/g;
        if(!rex.test($scope.properties.value)){
            $scope.properties.value="";
        }
    }
}
