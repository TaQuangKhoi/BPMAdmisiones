function ($scope) {
   $scope.switchValue = function(){
       if($scope.properties.menuClosed){
           $scope.properties.menuClosed = false;
       } else {
           $scope.properties.menuClosed = true;
       }
   }
}