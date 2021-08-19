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
    $scope.$watch('properties.objetoExamenes', function(value) {
    if (angular.isDefined(value) && value !== null) {
        if(value.invp!=""){
            $scope.properties.puntuacionINVP=parseInt(value.invp);
        }
        
     
    }
  });
  $scope.getTotal=function(){
    try{
      return parseInt(($scope.properties.objetoExamenes.paan==""||$scope.properties.objetoExamenes.paan==null||$scope.properties.objetoExamenes.paan==undefined)?"0":$scope.properties.objetoExamenes.paan) + parseInt(($scope.properties.objetoExamenes.paav==""||$scope.properties.objetoExamenes.paav==null||$scope.properties.objetoExamenes.paav==undefined)?"0":$scope.properties.objetoExamenes.paav) + parseInt(($scope.properties.objetoExamenes.para==""||$scope.properties.objetoExamenes.para==null||$scope.properties.objetoExamenes.para==undefined)?"0":$scope.properties.objetoExamenes.para) + parseInt(($scope.properties.puntuacionINVP==""||$scope.properties.puntuacionINVP==null||$scope.properties.puntuacionINVP==undefined)?"0":$scope.properties.puntuacionINVP)
    }catch(e){
      return 0
    }
  }
  
}
