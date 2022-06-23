function PbInputCtrl($scope, $log, widgetNameFactory) {

    'use strict';

    this.name = widgetNameFactory.getName('pbInput');

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbInput property named "value" need to be bound to a variable');
    }
    
    
    $scope.forceKeyPressUppercase = function (e) {
        var charInput = e.keyCode;
        var content = e.key;
        var limite = $scope.properties.maxLength === 1 ? 10 : $scope.properties.maxLength;
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
        
        if(!isNumber){
            e.preventDefault();
        } else if ((e.target.value.length) >= limite || !valid || (fullNumber > $scope.properties.max)) {
            // var start = e.target.selectionStart;
            // var end = e.target.selectionEnd;
            // e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
            // e.target.setSelectionRange(start + 1, start + 1);
            e.preventDefault();
        } 
    }
}
