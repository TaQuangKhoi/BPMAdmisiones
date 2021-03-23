function PbAutocompleteCtrl($scope, $parse, $log, widgetNameFactory) {

    'use strict';
    
    function createGetter(accessor) {
        return accessor && $parse(accessor);
    }
    
    this.getLabel = createGetter($scope.properties.displayedKey) || function (item) {
        return typeof item === 'string' ? item : JSON.stringify(item);
    };
    
    this.name = widgetNameFactory.getName('pbAutocomplete');
    
    if (!$scope.properties.isBound('value')) {
        $log.error('the pbAutocomplete property named "value" need to be bound to a variable');
    }
    
    $scope.testblur = function() {
        var otro = true;
        for (var x = 0; x < $scope.properties.availableValues.length; x++) {
            if ($scope.properties.value === $scope.properties.availableValues[x].descripcion) {
                otro = false;
                $scope.properties.completeObject = $scope.properties.availableValues[x];
                $scope.properties.claveBachilleratoSeleccionado = $scope.properties.availableValues[x].clave;
                break;
            }
        }
        if (otro) {
            $scope.properties.value = "Otro";
        }
    }
}