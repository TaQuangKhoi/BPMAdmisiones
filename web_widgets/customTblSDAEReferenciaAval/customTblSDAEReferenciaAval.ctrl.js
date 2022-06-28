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
  
    $scope.removeElement = function(_row){
        $scope.properties.content.splice($scope.properties.content.indexOf($scope.properties.content), 1);
    }
    // ng-keypress="forceKeyPressUppercase($event)"
    
    $scope.forceKeyPressUppercase = function(e,limit) {
    var charInput = e.keyCode;
    var limite = limit;
    if((charInput >=48) && (charInput <=57)&&(e.target.value.length) <limite){
        if($scope.properties.value >= $scope.properties.max){
            $scope.properties.value = null;
            $scope.properties.value = $scope.properties.max+"";
        }
    }else{
            $scope.properties.value = null;
            $scope.properties.value = $scope.properties.max+"";
            var start = e.target.selectionStart;
            var end = e.target.selectionEnd;
            e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
            e.target.setSelectionRange(start+1, start+1);
            e.preventDefault();
    }
     
    
    

  }
  
    $scope.$watch("properties.value",function(){
        if($scope.properties.value >= $scope.properties.max){
            $scope.properties.value = null;
            $scope.properties.value = "";
        }
        if($scope.properties.value % 1 != 0){
            $scope.properties.value = (parseFloat($scope.properties.value).toFixed(2))+"";
            $scope.properties.value = parseFloat($scope.properties.value)+"";
            $scope.$apply();
        }
    });
    
    
}
