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
  
  $scope.testblur = function(){
      debugger;
      var otro = true;
      for(var x=0;x<$scope.properties.availableValues.length;x++){
          if($scope.properties.value === $scope.properties.availableValues[x].descripcion){
              otro = false;
              break;
          }
      }
      if(otro){
          $scope.properties.datosPreparatoria.nombreBachillerato = $scope.properties.value;
        $scope.properties.datosPreparatoria.paisBachillerato = "";
        $scope.properties.datosPreparatoria.estadoBachillerato = "";
            $scope.properties.datosPreparatoria.ciudadBachillerato = "";
          $scope.properties.value = "Otro";
      }
  }
  
  $scope.$watch("properties.catSolicitudDeAdmision.CatPreparatoria.descripcion", function(){
        if(!$scope.llenoEntero){
            if($scope.properties.catSolicitudDeAdmision.CatPreparatoria.descripcion !== null || $scope.properties.catSolicitudDeAdmision.CatPreparatoria.descripcion !== undefined){
                $scope.properties.value = $scope.properties.catSolicitudDeAdmision.CatPreparatoria;
                $scope.llenoEntero = true;
            }
            //$scope.properties.gestionEscolar = $scope.properties.catSolicitudDeAdmision.catGestionEscolar;
        }
    });
}
