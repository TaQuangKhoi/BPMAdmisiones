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
    
    $scope.removeItem = function(_row){
        $scope.properties.content.splice($scope.properties.content.indexOf(_row), 1);
    }

    $scope.disableEscolar = function(_row, _index){
        if(!_row.isEstudia){
            $scope.properties.content[_index].institucion = "";
            $scope.properties.content[_index].isTieneBeca = false;
            $scope.properties.content[_index].porcentajeBecaAsignado = 0;
            $scope.properties.content[_index].colegiaturaMensual = 0;
        }
    }

    $scope.disableBeca = function(_row, _index){
        if(!_row.isTieneBeca){
            $scope.properties.content[_index].porcentajeBecaAsignado = 0;
        }
    }

    $scope.disableTrabaja = function(_row, _index){
        if(!_row.isTrabaja){
            $scope.properties.content[_index].empresa = "";
            $scope.properties.content[_index].ingresoMensual = 0;
        }
    }
}