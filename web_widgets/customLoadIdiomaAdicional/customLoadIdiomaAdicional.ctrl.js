function loadContextCtrl($scope, $http) {

    $scope.$watchCollection("properties.lstIdiomasV2", function(newValue, oldValue) {
        if ($scope.properties.lstIdiomasV2 !== undefined) {
            $scope.properties.hablasIdiomas = $scope.properties.lstIdiomasV2.length > 0;
        }
    });
}