function PbTableCtrl($scope, modalService) {

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
  
 this.showdatos = function(row){
     $scope.properties.ver = true;
     $scope.properties.datosEditar= row;
     console.log(this.datosEditar);
     openModal($scope.properties.modalid);
 }
 
  this.editdatos = function(row){
     $scope.properties.ver = false;
     $scope.properties.datosEditar= row;
     console.log(this.datosEditar);
     openModal($scope.properties.modalid);
 }
 
   function openModal(modalid) {
      
    modalService.open(modalid);
  }

  function closeModal(shouldClose) {
    if(shouldClose)
      modalService.close();
  }

}
