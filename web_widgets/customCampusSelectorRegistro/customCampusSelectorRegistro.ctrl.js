function ($scope) {
    $scope.selected = "";
    $scope.setSelected = function(_campus){
        for(var index in $scope.properties.campusList){
            if(_campus == $scope.properties.campusList[index].persistenceId){
                $scope.properties.campusSelected = $scope.properties.campusList[index];
            }
        }
        
        $scope.selected = _campus;
    }
}