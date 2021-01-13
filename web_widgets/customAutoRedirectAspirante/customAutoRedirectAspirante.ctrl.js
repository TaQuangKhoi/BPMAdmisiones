function ($scope) {
    $scope.$watch("properties.isAspirante", function(){
        if($scope.properties.isAspirante !== undefined){
            if($scope.properties.isAspirante){
                window.location.href = "/bonita/apps/aspirante/";
            }
        }
    });
}