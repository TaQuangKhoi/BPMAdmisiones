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

    $scope.testblur = function() {
    
        $scope.properties.urlCollageBoard = "";
        $scope.properties.isPDFCollageBoard = "true";
        $scope.properties.isImagenCollage = "true";
        $scope.properties.collageBoard = "";

        var otro = true;
        for (var x = 0; x < $scope.properties.availableValues.length; x++) {
            if ($scope.properties.value === $scope.properties.availableValues[x].descripcion) {
                otro = false;
                if($scope.properties.availableValues[x].descripcion === "Otro"){
                    $scope.properties.datosPreparatoria.nombreBachillerato = "";
                    $scope.properties.datosPreparatoria.paisBachillerato = "";
                    $scope.properties.datosPreparatoria.estadoBachillerato = "";
                    $scope.properties.datosPreparatoria.ciudadBachillerato = "";
                    $scope.properties.datosPreparatoria.clave = "";
                    $scope.properties.value = "Otro";
                }else{
                    
                    $scope.properties.datosPreparatoria.nombreBachillerato =  $scope.properties.availableValues[x].descripcion;
                    $scope.properties.datosPreparatoria.paisBachillerato =  $scope.properties.availableValues[x].paisBachillerato;
                    $scope.properties.datosPreparatoria.estadoBachillerato =  $scope.properties.availableValues[x].estadoBachillerato;
                    $scope.properties.datosPreparatoria.ciudadBachillerato =  $scope.properties.availableValues[x].ciudadBachillerato;
                    $scope.properties.datosPreparatoria.persistenceId =  $scope.properties.availableValues[x].persistenceId;
                    $scope.properties.datosPreparatoria.clave =  $scope.properties.availableValues[x].clave;
                    $scope.properties.clavePrepaSeleccionada = $scope.properties.availableValues[x].clave;
                    $scope.$apply();
                }  
                break;
            }
        }
        $scope.properties.filtrarPorClave=false;
        $scope.properties.modificarPreparatoria=true;
    }
    
    $scope.$watch("properties.catSolicitudDeAdmision.catBachilleratos.descripcion", function(){
    debugger
        if(!$scope.llenoEntero){
            if($scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion !== null || $scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion !== undefined){
                $scope.properties.value = $scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion;
                if($scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion === "Otro"){

                    $scope.properties.datosPreparatoria.nombreBachillerato = $scope.properties.catSolicitudDeAdmision.bachillerato;
                    $scope.properties.datosPreparatoria.paisBachillerato = $scope.properties.catSolicitudDeAdmision.paisBachillerato;
                    $scope.properties.datosPreparatoria.estadoBachillerato = $scope.properties.catSolicitudDeAdmision.estadoBachillerato;
                    $scope.properties.datosPreparatoria.ciudadBachillerato = $scope.properties.catSolicitudDeAdmision.ciudadBachillerato;
                    $scope.properties.datosPreparatoria.clave = " ";
                    $scope.llenoEntero = true;
                }else{
                    $scope.llenoEntero = true;

                    $scope.properties.datosPreparatoria.paisBachillerato = $scope.properties.catSolicitudDeAdmision.catBachilleratos.pais;
                    $scope.properties.datosPreparatoria.estadoBachillerato = $scope.properties.catSolicitudDeAdmision.catBachilleratos.estado;
                    $scope.properties.datosPreparatoria.ciudadBachillerato = $scope.properties.catSolicitudDeAdmision.catBachilleratos.ciudad;
                    $scope.properties.datosPreparatoria.persistenceId = $scope.properties.catSolicitudDeAdmision.catBachilleratos.persistenceId;
                    $scope.properties.datosPreparatoria.clave = $scope.properties.catSolicitudDeAdmision.catBachilleratos.clave;
                }
            }
             $scope.properties.modificarPreparatoria=true;
          $scope.$apply();
        }
    });
}