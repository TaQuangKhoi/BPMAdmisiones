function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
      if($scope.properties.arregloOServicio){
            doRequest($scope.properties.tipoDeUrl, $scope.properties.url);
        }else{
            $scope.searchArray();
        }
    };
    
    
    $scope.searchArray = function(){
        let str = {
            "valor":"",
            "index":null
        }
        $scope.properties.arregloDatos.forEach((datos, index) =>{
            if($scope.properties.datoActual == datos[$scope.properties.campoDato]){
                
                if(index>0 && !$scope.properties.accionNextOrReturn){
                    str.valor == datos[index-1][$scope.properties.campoDato];
                    str.index = index-1;
                }else if(index < ($scope.properties.arregloDatos.length - 1)  && $scope.properties.accionNextOrReturn){
                    str.valor == datos[index+1][$scope.properties.campoDato];
                    str.index = index+1;
                }else{
                    swal(`¡El aspirante es el ${$scope.properties.accionNextOrReturn?"ultimo de la lista ":"primero de la lista "}, no se puede seleccionar otro!`,"","")
                }
            } 
        });
        
        $scope.properties.valor = str.valor;
        $scope.properties.posicionDato = str.index;
    }
    
    
    function doRequest(method, url, params) {
        var datos = "";
        if(method == "POST"){
            datos = angular.copy($scope.properties.datosPost);
            datos.accion = $scope.properties.accionNextOrReturn;
        }
        

        var req = {
            method: method,
            url: url,
            data: datos,
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                if(!data.data[0].accion){
                    swal(`¡El aspirante es el ${$scope.properties.accionNextOrReturn?"ultimo de la lista, ":"primero de la lista, "}no se puede seleccionar otro!`,"","")
                }else{
                    
                    $scope.properties.value = data.data[0].idbanner
                    //var url = "/portal/resource/app/administrativo/ResultadoINVP/content/?idbanner="+data.data[0].idbanner+"&idsesion="+$scope.properties.sesiones;
                    //window.location.href= url
                }
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () {
            });
    }

    
    

}
