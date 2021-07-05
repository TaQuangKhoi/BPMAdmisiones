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
      
      $scope.properties.selectedRow = data;
      $scope.properties.table = "infodatos";
      $scope.properties.view = true;
      //$scope.$apply();
  }
  
    $scope.eliminarRegostro = function(data){
        let info = {idBanner:data.IDBANNER,desactivado:(data.desactivado =="t"?true:false),persistenceid:data.persistenceid};
        
        Swal.fire({
          title: '¡Advertencia!',
          text: `${info.desactivado?"¡Se eliminara este registro!":"¡El resultado anterior a este se convertira en el resultado oficial!"}`,
          showCancelButton: true,
          confirmButtonText: `Eliminar`,
        }).then((result) => {
          if (result.isConfirmed) {
            doReques(info);
          }
        });
        
        
    }
    
    function doReques(info){
        
        var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=postEliminarResultado&p=0&c=100",
            data: angular.copy(info)
        };
        return $http(req)
            .success(function (data, status) {
                
            })
            .error(function (data, status) {
            });
    }
  
  
}
