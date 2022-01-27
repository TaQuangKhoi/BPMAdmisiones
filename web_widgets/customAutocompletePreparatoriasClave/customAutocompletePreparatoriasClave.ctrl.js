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
        debugger
        $scope.properties.urlCollageBoard = "";
        $scope.properties.isPDFCollageBoard = "true";
        $scope.properties.isImagenCollage = "true";
        $scope.properties.collageBoard = "";

        var otro = true;
        for (var x = 0; x < $scope.properties.availableValues.length; x++) {
            if ($scope.properties.value === $scope.properties.availableValues[x].clave) {
                otro = false;
                if($scope.properties.availableValues[x].descripcion === "Otro"){
                    $scope.properties.datosPreparatoria.nombreBachillerato = "";
                    $scope.properties.datosPreparatoria.paisBachillerato = "";
                    $scope.properties.datosPreparatoria.estadoBachillerato = "";
                    $scope.properties.datosPreparatoria.ciudadBachillerato = "";
                    $scope.properties.datosPreparatoria.clave = "";
                    $scope.properties.value = "Otro";
                }
                break;
            }
        }
        if (otro) {
            $scope.properties.datosPreparatoria.clave = $scope.properties.value;
            $scope.properties.datosPreparatoria.paisBachillerato = "";
            $scope.properties.datosPreparatoria.estadoBachillerato = "";
            $scope.properties.datosPreparatoria.ciudadBachillerato = "";
            $scope.properties.datosPreparatoria.nombreBachillerato = "";
            $scope.properties.value = "Otro";
        }
    }
    
    $scope.$watch("properties.catSolicitudDeAdmision.catBachilleratos.clave", function(){
        debugger
        if(!$scope.llenoEntero){
            if($scope.properties.catSolicitudDeAdmision.catBachilleratos.clave !== null || $scope.properties.catSolicitudDeAdmision.catBachilleratos.clave !== undefined){
                $scope.properties.value = $scope.properties.catSolicitudDeAdmision.catBachilleratos.clave;
                if($scope.properties.catSolicitudDeAdmision.catBachilleratos.clave === "Otro"){
                    /*$scope.properties.datosPreparatoria.nombreBachillerato = $scope.properties.value;*/
                    $scope.properties.datosPreparatoria.nombreBachillerato = $scope.properties.catSolicitudDeAdmision.bachillerato;
                    $scope.properties.datosPreparatoria.paisBachillerato = $scope.properties.catSolicitudDeAdmision.paisbachillerato;
                    $scope.properties.datosPreparatoria.estadoBachillerato = $scope.properties.catSolicitudDeAdmision.estadobachillerato;
                    $scope.properties.datosPreparatoria.ciudadBachillerato = $scope.properties.catSolicitudDeAdmision.ciudadbachillerato;
                    $scope.properties.datosPreparatoria.clave = $scope.properties.catSolicitudDeAdmision.clave;
                    $scope.llenoEntero = true;
                }else{
                    $scope.llenoEntero = true;
                    //$scope.properties.datosPreparatoria.nombreBachillerato = $scope.properties.value;
                    $scope.properties.datosPreparatoria.paisBachillerato = $scope.properties.catSolicitudDeAdmision.catBachilleratos.pais;
                    $scope.properties.datosPreparatoria.estadoBachillerato = $scope.properties.catSolicitudDeAdmision.catBachilleratos.estado;
                    $scope.properties.datosPreparatoria.ciudadBachillerato = $scope.properties.catSolicitudDeAdmision.catBachilleratos.ciudad;   
                    //$scope.properties.datosPreparatoria.nombreBachillerato = $scope.properties.catSolicitudDeAdmision.bachillerato;
                }
            }
            //$scope.properties.gestionEscolar = $scope.properties.catSolicitudDeAdmision.catGestionEscolar;
        }
    });
}