function PbInputCtrl($scope, $log, widgetNameFactory, $sce) {

    'use strict';

    this.name = widgetNameFactory.getName('pbInput');

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbInput property named "value" need to be bound to a variable');
    }
  
    $scope.currentProjectUrl = "";
  
    $scope.cargarVideo = function () {
       $scope.currentProjectUrl = $sce.trustAsResourceUrl($scope.properties.url.replace("watch?v=","embed/"));
    }
    
    $scope.$watch("properties.url", ()=>{
       $scope.cargarVideo(); 
    });
}
