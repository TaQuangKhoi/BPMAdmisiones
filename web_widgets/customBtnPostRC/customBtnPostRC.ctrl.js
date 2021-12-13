function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService,blockUI) {

    'use strict';
  
    var vm = this;
  
      $scope.myFunc = function() {
          swal({
            title: "¡Aviso!",
            text: "Se agregará un resultado nuevo con los datos actualizados. ¿Desea continuar?",
            icon: "warning",
            buttons: ["No","Sí"],
            dangerMode: true,
          })
          .then((willDelete) => {
            if (willDelete) {
                doRequest("POST",$scope.properties.url,"")
            } else {
              
            }
          });
        
      };
      
      
      function doRequest(method, url, params) {
          blockUI.start();
          var req = {
              method: method,
              url: url,
              data: angular.copy($scope.properties.dataToSend),
              params: params
          };
  
          return $http(req)
              .success(function (data, status) {
                  postGuardarUsuario();
              })
              .error(function (data, status) {
              })
              .finally(function () {
                  blockUI.stop();
              });
      }
      
      function postGuardarUsuario() {
          blockUI.start();
          var req = {
              method: "POST",
              url: "/bonita/API/extension/AnahuacRest?url=postGuardarUsuarioRC&p=0&c=100",
              data: angular.copy($scope.properties.datosAspirante),
          };
  
          return $http(req)
              .success(function (data, status) {
                  actualizacion_de_datos();
              })
              .error(function (data, status) {
              })
              .finally(function () {
                  blockUI.stop();
              });
      }
      function actualizacion_de_datos(){
          if($scope.properties.tipoValor2 === "json"){
            $scope.properties.valor2 =  {};  
          } else if($scope.properties.tipoValor2 === "array"){
            $scope.properties.valor2 =  [];  
          } else if($scope.properties.tipoValor2 === "text"){
            $scope.properties.valor2 =  "";  
          }
          
          if($scope.properties.tipoValor3 === "json"){
            $scope.properties.valor3 =  {};  
          } else if($scope.properties.tipoValor3 === "array"){
            $scope.properties.valor3 =  [];  
          } else if($scope.properties.tipoValor3 === "text"){
            $scope.properties.valor3 =  "";  
          }
          
          $scope.properties.datos = $scope.properties.valor;
          $scope.cargaManual();
      }
       $scope.cargaManual = function(){
          
          $scope.properties.datosAspirante = [];
          var req = {
              method: "GET",
              url: `/API/extension/AnahuacRestGet?url=getAspiranteRC&p=0&c=10&idbanner=${$scope.properties.idbanner}`
          };
          return $http(req)
              .success(function (data, status) {
                  //$scope.properties.datosAspirante = data.data;
                  cargaDeDatos($scope.properties.datosAspirante,data.data);
                  
                  
              })
              .error(function (data, status) {
                  console.error(data);
              })
              .finally(function () { });
          
          
      }
      function cargaDeDatos(final,datos){
        
        if(datos !== null && datos !== undefined){
            datos.forEach(element =>{
                let json = {
                    "decision": "",
                    "observaciones": "",
                    "IDBANNER": "",
                    "persistenceid":"",
                    "desactivado":""
                };
                let columna = angular.copy(element);
                for(var key in columna){
                    if(!$scope.properties.variablesCambio.includes(key)){
                        json[ (key=="idbanner"?"IDBANNER":key)] = element[key];
                    }else{
                         json[(key.toUpperCase()+"_1")] = element[key];
                    }
                }
                //json[count].observaciones = json.observacione;
                json.Periodo = json.periodo;
                json.update = true;
                final.push(json);
            });
            
        }
    }
  
  }
  