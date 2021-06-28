function PbInputCtrl($scope, $log, widgetNameFactory) {

    'use strict';
    $scope.otroPais={"pais":""};
    this.name = widgetNameFactory.getName('pbInput');

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbInput property named "value" need to be bound to a variable');
    }

    $scope.isValid = false;
    $scope.forceKeyPressUppercase = function(e) {
        debugger;
        var re=/^[a-zA-Z0-9.]*$/g;
        if($scope.properties.preparatoriaSeleccionada!='México'){
        $scope.properties.value=re.test($scope.otroPais.pais)?$scope.otroPais.pais.toUpperCase():"";
        }else{
        var charInput = e.keyCode;
        if (charInput === 69) {
            $scope.isValid = true;
        }
        if (charInput === 82) {
            $scope.isValid = true;
        }
        if (charInput === 86) {
            $scope.isValid = true;
        }
        if (charInput === 46) {
            $scope.isValid = true;
        }
        if ($scope.isValid) {
            
        } else {
            var limite = $scope.properties.maxLength === 0 ? 250 : $scope.properties.maxLength;
            if ((charInput >= 48) && (charInput <= 57) && (e.target.value.length) < limite) {
                if ($scope.properties.value >= $scope.properties.max) {
                    $scope.properties.value = null;
                    $scope.properties.value = $scope.properties.max;
                }
            } else {
                if ($scope.properties.value >= $scope.properties.max) {
                    $scope.properties.value = null;
                    $scope.properties.value = $scope.properties.max;
                }
            }
        }
            
        }
    }
      $scope.$watch('otroPais.pais', function(value) {
    if (angular.isDefined(value) && value !== null) {
        var re=/^[a-zA-Z0-9.]*$/g;
        if(re.test(value)){
            $scope.otroPais.pais=value.toUpperCase();
            $scope.properties.value=value.toUpperCase();
        }else{
            $scope.otroPais.pais = $scope.properties.value;
        }
    }
  });
    
    $scope.$watch("properties.value", function() {
        
        debugger;
        if($scope.properties.preparatoriaSeleccionada=='México'){
        if($scope.properties.value === "E"){
            if($scope.properties.value.length > 1){
                $scope.properties.value = null;
            }
        } else if($scope.properties.value === "R"){
            if($scope.properties.value.length > 1){
                $scope.properties.value = null;
            }
        } else if($scope.properties.value === "RV"){
            if($scope.properties.value.length > 2){
                $scope.properties.value = null;
            }
        }else if($scope.properties.value.indexOf('.') != -1){
            if($scope.properties.value === "10"){
               $scope.properties.value = null;
                $scope.properties.value = $scope.properties.max;
            }else{
                if($scope.properties.value.length > 2){
                    if (parseFloat($scope.properties.value) >= $scope.properties.max) {
                        $scope.properties.value = null;
                        $scope.properties.value = $scope.properties.max;
                    }
                    if (parseFloat($scope.properties.value) % 1 != 0) {
                        $scope.properties.value = parseFloat($scope.properties.value).toFixed(1);
                        $scope.properties.value = parseFloat($scope.properties.value);
                    } else {
                        $scope.properties.value = parseFloat($scope.properties.value);
                    }
                }
            }
        }else{
            if (parseFloat($scope.properties.value) > $scope.properties.max) {
                $scope.properties.value = null;
                //$scope.properties.value = $scope.properties.max;
            }
            if (parseFloat($scope.properties.value) % 1 != 0) {
                $scope.properties.value = parseFloat($scope.properties.value).toFixed(1);
                $scope.properties.value = parseFloat($scope.properties.value);
            } else {
                $scope.properties.value = parseFloat($scope.properties.value);
            }
        }
            
        }
        
    });

}