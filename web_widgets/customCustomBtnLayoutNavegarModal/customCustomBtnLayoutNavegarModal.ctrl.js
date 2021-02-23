function PbButtonCtrl($scope, modalService) {
    $scope.changeVariable = function(){
        $scope.properties.navigationVar = $scope.properties.newValue;
    }
}
