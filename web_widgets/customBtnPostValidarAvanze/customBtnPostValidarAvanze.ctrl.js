function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
         
        if(validar()){
         doRequestEstatus("GET","../API/extension/AnahuacRestGet?url=getEstatusDelAspirante&p=0&c=9999&username="+$scope.properties.username,"");  
        }
    };
    
    
    function doRequest(method, url, params) {
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                console.log(data.data)
                swal("¡El aspirante a sido avanzado!", {buttons: false,timer: 1500,});
                setTimeout(function(){ location.reload(true); }, 1500);
                
            })
            .error(function (data, status) {
            })
            
    }
    
    function doRequestEstatus(method, url, params) {
        var req = {
            method: method,
            url: url,
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                doRequest("POST",$scope.properties.url,"")
                //if(data[0].estatussolicitud === "Autodescripción concluida" ||data[0].estatussolicitud === "Elección de pruebas calendarizado" || data[0].estatussolicitud === "Ya se imprimió su credencial"){
                    
                //}else{
                  //   swal("¡El no esta en los estatus correspondientes para avanzar!","","info")
                //}
            })
            .error(function (data, status) {
            })
            
    }
    
    function validar(){
        if($scope.properties.strValidar.length < 1){
            swal("¡No tiene una sesion selecionada!","","info")
            return false;
        }
        return true;
    }
    
    
    
    

}
