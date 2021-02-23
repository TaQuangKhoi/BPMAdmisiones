function PbButtonCtrl($scope) {
    $scope.action = function() {
        $scope.properties.selectedIndex = $scope.properties.valueToSet;
    }
}