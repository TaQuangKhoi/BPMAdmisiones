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
        $scope.properties.content.splice($scope.properties.content.indexOf(_row), 1);
    }
    
    $scope.forceKeyPressUppercase = function(e,limit) {
        var charInput = e.keyCode;
        var limite = limit;
        if((charInput >=48) && (charInput <=57)&&(e.target.value.length) <limite){
            if($scope.properties.value >= $scope.properties.max){
                $scope.properties.value = null;
                $scope.properties.value = $scope.properties.max+"";
            }
        } else {
            $scope.properties.value = null;
            $scope.properties.value = $scope.properties.max+"";
            var start = e.target.selectionStart;
            var end = e.target.selectionEnd;
            e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
            e.target.setSelectionRange(start+1, start+1);
            e.preventDefault();
        }
    }
    
    // saldoPromedio
    // forceKeyPressUppercaseSaldo

	$scope.forceKeyPressUppercaseSaldo = function (e, value) {
		debugger;
        var charInput = e.keyCode;
        var content = e.key;
        var limite = 9;
        let valid = true;
        let isNumber = false;
        let fullNumber = 0;
        
        if((e.keyCode > 47 && e.keyCode < 59)){
            isNumber = true;
            valid = true;
            fullNumber = parseInt(e.target.value + content);
        } else{
            valid = false;
        }
        
        let valueSize = 0;
        if(value){
            let auxValue = $scope.properties.value + "";
            valueSize = auxValue.length;
        }
        
        if(!isNumber){
            e.preventDefault();
        } else if ((valueSize) >= limite || !valid || (fullNumber > 100000000)) {
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