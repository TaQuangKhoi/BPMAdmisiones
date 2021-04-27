function getUserAbandono($scope, $http, $window) {
    $scope.$watch("properties.datosTutor.viveContigo", function() {
       debugger;
        if ($scope.properties.datosTutor.viveContigo) {
            $scope.properties.datosTutor.calle = $scope.properties.SolicitudDeAdmision.calle;
            $scope.properties.datosTutor.codigoPostal = $scope.properties.SolicitudDeAdmision.codigoPostal;
            $scope.properties.datosTutor.catPais = $scope.properties.SolicitudDeAdmision.catPais;
            $scope.properties.datosTutor.catEstado = $scope.properties.SolicitudDeAdmision.catEstado;
            $scope.properties.datosTutor.ciudad = $scope.properties.SolicitudDeAdmision.ciudad;
            $scope.properties.datosTutor.numeroExterior = $scope.properties.SolicitudDeAdmision.numExterior;
            $scope.properties.datosTutor.numeroInterior = $scope.properties.SolicitudDeAdmision.numInterior;
            $scope.properties.datosTutor.colonia = $scope.properties.SolicitudDeAdmision.colonia;
            $scope.properties.datosTutor.telefono = angular.copy($scope.properties.SolicitudDeAdmision.telefono);
            $scope.properties.datosTutor.delegacionMunicipio = $scope.properties.SolicitudDeAdmision.delegacionMunicipio;
            $scope.properties.datosTutor.estadoExtranjero = $scope.properties.SolicitudDeAdmision.estadoExtranjero;
            $scope.properties.llenardatostutor = true;
        } else {
            if ($scope.properties.llenardatostutor) {
                $scope.properties.datosTutor.calle = "";
                $scope.properties.datosTutor.codigoPostal = "";
                $scope.properties.datosTutor.catPais = null;
                $scope.properties.datosTutor.catEstado = null;
                $scope.properties.datosTutor.ciudad = "";
                $scope.properties.datosTutor.numeroExterior = "";
                $scope.properties.datosTutor.numeroInterior = "";
                $scope.properties.datosTutor.colonia = "";
                $scope.properties.datosTutor.telefono = "";
                $scope.properties.datosTutor.delegacionMunicipio = "";
                $scope.properties.datosTutor.estadoExtranjero = "";
                $scope.properties.llenardatostutor = false;
            }
        }

    });

    $scope.$watch("properties.jsonModificarTutor.viveContigo", function() {
        debugger;
        if ($scope.properties.jsonModificarTutor.viveContigo) {
            $scope.properties.jsonModificarTutor.calle = $scope.properties.SolicitudDeAdmision.calle;
            $scope.properties.jsonModificarTutor.codigoPostal = $scope.properties.SolicitudDeAdmision.codigoPostal;
            $scope.properties.jsonModificarTutor.catPais = $scope.properties.SolicitudDeAdmision.catPais;
            $scope.properties.jsonModificarTutor.catEstado = $scope.properties.SolicitudDeAdmision.catEstado;
            $scope.properties.jsonModificarTutor.ciudad = $scope.properties.SolicitudDeAdmision.ciudad;
            $scope.properties.jsonModificarTutor.numeroExterior = $scope.properties.SolicitudDeAdmision.numExterior;
            $scope.properties.jsonModificarTutor.numeroInterior = $scope.properties.SolicitudDeAdmision.numInterior;
            $scope.properties.jsonModificarTutor.colonia = $scope.properties.SolicitudDeAdmision.colonia;
            $scope.properties.jsonModificarTutor.telefono = angular.copy($scope.properties.SolicitudDeAdmision.telefono);
            $scope.properties.jsonModificarTutor.delegacionMunicipio = $scope.properties.SolicitudDeAdmision.delegacionMunicipio;
            $scope.properties.jsonModificarTutor.estadoExtranjero = $scope.properties.SolicitudDeAdmision.estadoExtranjero;
            $scope.properties.llenarjsonModificarTutor = true;
        } else {
            if ($scope.properties.llenarjsonModificarTutor) {
                $scope.properties.jsonModificarTutor.calle = "";
                $scope.properties.jsonModificarTutor.codigoPostal = "";
                $scope.properties.jsonModificarTutor.catPais = null;
                $scope.properties.jsonModificarTutor.catEstado = null;
                $scope.properties.jsonModificarTutor.ciudad = "";
                $scope.properties.jsonModificarTutor.numeroExterior = "";
                $scope.properties.jsonModificarTutor.numeroInterior = "";
                $scope.properties.jsonModificarTutor.colonia = "";
                $scope.properties.jsonModificarTutor.telefono = "";
                $scope.properties.jsonModificarTutor.delegacionMunicipio = "";
                $scope.properties.jsonModificarTutor.estadoExtranjero = "";
                $scope.properties.llenarjsonModificarTutor = false;
            }
        }

    });

    $scope.$watch("properties.padre.viveContigo", function() {
        debugger;
        if ($scope.properties.padre.viveContigo) {
            $scope.properties.padre.calle = $scope.properties.SolicitudDeAdmision.calle;
            $scope.properties.padre.codigoPostal = $scope.properties.SolicitudDeAdmision.codigoPostal;
            $scope.properties.padre.catPais = $scope.properties.SolicitudDeAdmision.catPais;
            $scope.properties.padre.catEstado = $scope.properties.SolicitudDeAdmision.catEstado;
            $scope.properties.padre.ciudad = $scope.properties.SolicitudDeAdmision.ciudad;
            $scope.properties.padre.numeroExterior = $scope.properties.SolicitudDeAdmision.numExterior;
            $scope.properties.padre.numeroInterior = $scope.properties.SolicitudDeAdmision.numInterior;
            $scope.properties.padre.colonia = $scope.properties.SolicitudDeAdmision.colonia;
            $scope.properties.padre.telefono = $scope.properties.SolicitudDeAdmision.telefono;
            $scope.properties.padre.delegacionMunicipio = $scope.properties.SolicitudDeAdmision.delegacionMunicipio;
            $scope.properties.padre.estadoExtranjero = $scope.properties.SolicitudDeAdmision.estadoExtranjero;
            $scope.properties.llenardatospadre = true;
        } else {
            if ($scope.properties.llenardatospadre) {
                $scope.properties.padre.calle = "";
                $scope.properties.padre.codigoPostal = "";
                $scope.properties.padre.catPais = null;
                $scope.properties.padre.catEstado = null;
                $scope.properties.padre.ciudad = "";
                $scope.properties.padre.numeroExterior = "";
                $scope.properties.padre.numeroInterior = "";
                $scope.properties.padre.colonia = "";
                $scope.properties.padre.telefono = "";
                $scope.properties.padre.delegacionMunicipio = "";
                $scope.properties.padre.estadoExtranjero = "";
                $scope.properties.llenardatospadre = false;
            }

        }
    });

    $scope.$watch("properties.madre.viveContigo", function() {
        debugger;
        if ($scope.properties.madre.viveContigo) {
            $scope.properties.madre.calle = $scope.properties.SolicitudDeAdmision.calle;
            $scope.properties.madre.codigoPostal = $scope.properties.SolicitudDeAdmision.codigoPostal;
            $scope.properties.madre.catPais = $scope.properties.SolicitudDeAdmision.catPais;
            $scope.properties.madre.catEstado = $scope.properties.SolicitudDeAdmision.catEstado;
            $scope.properties.madre.ciudad = $scope.properties.SolicitudDeAdmision.ciudad;
            $scope.properties.madre.numeroExterior = $scope.properties.SolicitudDeAdmision.numExterior;
            $scope.properties.madre.numeroInterior = $scope.properties.SolicitudDeAdmision.numInterior;
            $scope.properties.madre.colonia = $scope.properties.SolicitudDeAdmision.colonia;
            $scope.properties.madre.telefono = $scope.properties.SolicitudDeAdmision.telefono;
            $scope.properties.madre.delegacionMunicipio = $scope.properties.SolicitudDeAdmision.delegacionMunicipio;
            $scope.properties.madre.estadoExtranjero = $scope.properties.SolicitudDeAdmision.estadoExtranjero;
            $scope.properties.llenardatosmadre = true;
        } else {
            if ($scope.properties.llenardatosmadre) {
                $scope.properties.madre.calle = "";
                $scope.properties.madre.codigoPostal = "";
                $scope.properties.madre.catPais = null;
                $scope.properties.madre.catEstado = null;
                $scope.properties.madre.ciudad = "";
                $scope.properties.madre.numeroExterior = "";
                $scope.properties.madre.numeroInterior = "";
                $scope.properties.madre.colonia = "";
                $scope.properties.madre.telefono = "";
                $scope.properties.madre.delegacionMunicipio = "";
                $scope.properties.madre.estadoExtranjero = "";
                $scope.properties.llenardatosmadre = false;
            }

        }

    });
}