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
        
        for (var x = 0; x < $scope.properties.availableValues.length; x++) {
            if ($scope.properties.value === $scope.properties.availableValues[x].clave) {
                
                $scope.properties.completeObject = $scope.properties.availableValues[x];
                $scope.properties.bachilleratoSeleccionado =$scope.properties.availableValues[x].descripcion;
                break;
            }
        }
    }
}