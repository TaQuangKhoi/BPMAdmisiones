function PbTableCtrl($scope) {

  this.isArray = Array.isArray;

  this.isClickable = function () {
    return $scope.properties.isBound('selectedRow');
  };

  this.selectRow = function (row) {
    if (this.isClickable()) {
      $scope.properties.selectedRow = row;
    }
  };

  this.isSelected = function(row) {
    return angular.equals(row, $scope.properties.selectedRow);
  }
    $scope.$watch('properties.objetoExamenes', function(value) {
    if (angular.isDefined(value) && value !== null) {
        if(value.invp!=""){
            $scope.properties.puntuacionINVP=parseInt(value.invp);
        }
     
    }
  });
  
}
