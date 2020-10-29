function PbTableCtrl($scope) {

  this.isArray = Array.isArray;

  this.isClickable = function () {
    return $scope.properties.isBound('selectedRow');
  };

  this.selectRow = function (row) {
    if (this.isClickable()) {
      $scope.properties.selectedRow = row;
      console.log($scope.properties.selectedRow);
    }
  };

  this.isSelected = function(row) {
    return angular.equals(row, $scope.properties.selectedRow);
  }
  
  this.isEnabled = function(row) {
      if(row.isEnabled == true){
          row.isEnabled = false;
      }else{
            if(row.isEnabled == false){
            row.isEnabled = true;
        }
          
      }
   console.log(row);
  }
}
