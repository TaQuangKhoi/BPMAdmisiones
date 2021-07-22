function PbTableCtrl($scope, $window,$http,blockUI) {

  this.isArray = Array.isArray;
  'use strict';

  this.isClickable = function () {
    return $scope.properties.isBound('selectedRow');
  };

  this.selectRow = function (row) {
    if (this.isClickable()) {
      $scope.properties.selectedRow = row;
    }
  };

  this.isSelected = function(row) {
    return angular.equals(row, $scope.properties.selectedRow);
  }
  
   $scope.visualizarInfo = function(data){
      
      doReques(data.IDBANNER,data.persistenceid);
      $scope.properties.strInfo = data.fechasolicitudenviada;
      //$scope.$apply();
  }
  
  
    function doReques(idbanner,persistenceid){
        var req = {
            method: "GET",
            url: `/API/extension/AnahuacRestGet?url=getAspiranteRC_Expecifico&p=0&c=10&idbanner=${idbanner}&persistenceid=${persistenceid}`
        };
        return $http(req)
            .success(function (data, status) {
                cargaDeDatos(data.data);
                $scope.properties.table = "infodatos";
                $scope.properties.view = true;
            })
            .error(function (data, status) {
            });
    }
    
    
    function cargaDeDatos(datos){
        
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
                $scope.properties.returnValue =  angular.copy(json);
            });
            
        }
    }
  
  
}
