function PbTableCtrl($scope) {

  this.isArray = Array.isArray;

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
  
  $scope.redirecGrafica = function(row){
      var url = "/portal/resource/app/administrativo/ResultadoINVP/content/?idbanner="+row.idbanner+"&idsesion="+row.sesion_id
        window.location.href= url;
  }
  
  
}
