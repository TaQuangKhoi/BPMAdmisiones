function PbTableCtrl($scope, modalService) {

    this.isArray = Array.isArray;

    this.isClickable = function () {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function (row, index, action) {
        if (this.isClickable()) {
            row.index = index;
            row.escuelaString = row.escuela === null ? "" : row.escuela.descripcion;
            
            if(row.anoFin !== null && row.anoFin !== undefined && row.anoFin !== ""){
                row.anoFin = parseInt(row.anoFin);
            }
            
            if(row.anoInicio !== null && row.anoInicio !== undefined && row.anoInicio !== ""){
                row.anoInicio = parseInt(row.anoInicio);
            }
            
            if(row.promedio !== null && row.promedio !== undefined && row.promedio !== ""){
                row.promedio = parseInt(row.promedio);
            }
            // row.anoInicio = parseInt(row.anoInicio);
            
            // row.promedio = parseFloat(row.promedio);
            
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
