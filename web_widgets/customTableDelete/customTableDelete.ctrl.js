function PbTableCtrl($scope) {

    this.isArray = Array.isArray;
    $scope.contenedor=[];
    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
        }
    };

    $scope.deleteContent = function(objContent) {
        console.log(objContent);
        var index = $scope.contenedor.indexOf(objContent);
        console.log(index);
        if(index != -1){
            $scope.contenedor.splice(index, 1);
            $scope.properties.retorno=$scope.contenedor.join(';');
        }
    }
    $scope.$watch('properties.retorno', function(value) {
        if (angular.isDefined(value) && value !== null) {
            $scope.contenedor=value.split(';');
        }
      });
    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }
}