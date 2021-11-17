function PbAutocompleteCtrl($scope, $parse, $log, widgetNameFactory) {

    'use strict';

    function createGetter(accessor) {
        return accessor && $parse(accessor);
    }

    this.getLabel = createGetter($scope.properties.displayedKey) || function(item) {
        return typeof item === 'string' ? item : JSON.stringify(item);
    };

    this.name = widgetNameFactory.getName('pbAutocomplete');

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbAutocomplete property named "value" need to be bound to a variable');
    }
    
    $scope.selectTypeAhead = function(item){
        console.log("SELECTTYPEAHEAD")
        $scope.properties.datosPreparatoria=angular.copy(item);

        $scope.properties.objNuevoInformacionEscolar.estadoString = $scope.properties.datosPreparatoria.estado.trim();
        $scope.properties.objNuevoInformacionEscolar.ciudad = $scope.properties.datosPreparatoria.ciudad.trim();
        $scope.properties.objNuevoInformacionEscolar.pais = null;
        $scope.properties.objNuevoInformacionEscolar.estado = null;
        for(var indexP in $scope.properties.catPais){
            if($scope.properties.catPais[indexP].descripcion == item.pais.trim()){
                $scope.properties.objNuevoInformacionEscolar.pais = $scope.properties.catPais[indexP];
                break;
            }
        }

        for(var indexE in $scope.properties.catEstados){
            if($scope.properties.catEstados[indexE].descripcion == item.estado.trim()){
                $scope.properties.objNuevoInformacionEscolar.estado = $scope.properties.catEstados[indexE];
                break;
            }
        }
    }

    $scope.testblur = function() {
        
    }
}