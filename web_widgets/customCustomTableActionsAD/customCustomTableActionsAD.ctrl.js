function PbTableCtrl($scope, modalService) {

    this.isArray = Array.isArray;

    this.isClickable = function () {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function (row, index, action) {
        // if (this.isClickable()) {
        //     // $scope.properties.selectedRow = row;
        //     row.index = index;
        //     $scope.properties.selectedRow = angular.copy(row);
        // }
        if (this.isClickable()) {
            row.index = index;
            $scope.properties.selectedRow = angular.copy(row);
            
            if(action === "ver" ){
                this.showModalEditar(false);
            } else if(action === "editar" ){
                this.showModalEditar(true);
            } else if(action === "eliminar" ){
                this.showModalEliminar();
            }
        }
    };

    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }
    
    this.showModalEliminar = function(){
        openModal($scope.properties.modalEliminar);
    }
    
    this.showModalEditar = function(_isEditar){
        if(_isEditar){
            $scope.properties.accionModal = "editar";
        } else {
            $scope.properties.accionModal = "ver";
        }
        
        openModal($scope.properties.modalEditar);
    }
    
    function openModal(modal) {
        modalService.open(modal);
    }
}
