function PbTableCtrl($scope, $window) {

  this.isArray = Array.isArray;
  'use strict';

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
  
   $scope.visualizarInfo = function(data){
      $scope.$apply();
      $scope.properties.selectedRow = data;
      $scope.properties.table = "fragmento";
      //$scope.$apply();
  }
  
  
}
