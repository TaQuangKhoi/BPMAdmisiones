function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;

    $scope.validarObservacion = function(){
        let output = true;
        let errorMessage = "";
        let messageTitle = "";
        let regex = new RegExp('^[0-9]*$');
        if($scope.properties.nuevaObservacion.orden === "" || $scope.properties.nuevaObservacion.orden === null || $scope.properties.nuevaObservacion.orden === undefined){
            errorMessage = "El campo 'Orden' no debe ir vacío y debe contener un valor numérico.";
            messageTitle = "¡Orden!";
            output = false;
        } else if(!regex.test($scope.properties.nuevaObservacion.orden)){
            errorMessage = "El campo 'Orden' no debe ir vacío y debe contener un valor numérico.";
            messageTitle = "¡Orden!";
            output = false;
        } else if ($scope.properties.nuevaObservacion.universidad === ""){
            errorMessage = "El campo 'Universidad' no debe ir vacío.";
            messageTitle = "Universidad";
            output = false;
        } else if ($scope.properties.nuevaObservacion.porcentajeBeca === ""){
            errorMessage = "El campo '% Beca' no debe ir vacío y debe contener un valor numérico.";
            messageTitle = "% Beca";
            output = false;
        } else if(!regex.test($scope.properties.nuevaObservacion.porcentajeBeca)){
            errorMessage = "El campo '% Beca' no debe ir vacío y debe contener un valor numérico.";
            messageTitle = "% Beca!";
            output = false;
        } else if ($scope.properties.nuevaObservacion.porcentajeCredito === ""){
            errorMessage = "El campo '% Crédito' no debe ir vacío y debe contener un valor numérico.";
            messageTitle = "% Crédito";
            output = false;
        } else if(!regex.test($scope.properties.nuevaObservacion.porcentajeCredito)){
            errorMessage = "El campo '% Crédito' no debe ir vacío y debe contener un valor numérico.";
            messageTitle = "% Crédito!";
            output = false;
        } 

        if(output === false){
            swal(messageTitle, errorMessage, "warning");
        }

        return output;
    }

    $scope.agregarOvservacion = function(){
        if($scope.validarObservacion()){
           $scope.properties.observaciones.push(angular.copy($scope.properties.nuevaObservacion));
           modalService.close();
        }
    }

}  