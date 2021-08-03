function PbTableCtrl($scope, modalService) {

    $scope.objUniversidadHasEstado = {
        "persistenceId_string": "",
        "nombre": "",
        "anoInicio": "",
        "anoFin": "",
        "carrera": "",
        "motivoSuspension": "",
        "vencido": false
    };
    this.isArray = Array.isArray;

    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row, index, action) {
        if (this.isClickable()) {
            row.index = index;
            if (row.anoFin !== null && row.anoFin !== undefined && row.anoFin !== "") {
                row.anoFin = parseInt(row.anoFin);
            }

            if (row.anoInicio !== null && row.anoInicio !== undefined && row.anoInicio !== "") {
                row.anoInicio = parseInt(row.anoInicio);
            }

            $scope.properties.selectedRow = angular.copy(row);

            if (action === "ver") {
                this.showModalEditar(false);
            } else if (action === "editar") {
                this.showModalEditar(true);
            } else if (action === "eliminar") {
                swal({
                        title: "",
                        text: "¿Seguro que deseas continuar?",
                        icon: "warning",
                        buttons: true
                    })
                    .then((willDelete) => {
                        if (willDelete) {
                            $scope.properties.lstInformacionEscolarMod.splice(index, 1);
                            $scope.$apply();
                        }
                    });
            }
        }
    };

    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    this.showModalEliminar = function() {
        openModal($scope.properties.modalEliminar);
    }

    this.showModalEditar = function(_isEditar) {
        if (_isEditar) {
            $scope.properties.accionModal = "editar";
        } else {
            $scope.properties.accionModal = "ver";
        }

        openModal($scope.properties.modalEditar);
    }

    this.showModalAgregar = function() {
        $scope.properties.accionModal = "agregar";
        $scope.properties.selectedRow = angular.copy($scope.objUniversidadHasEstado);
        openModal($scope.properties.modalEditar);
    }

    function openModal(modal) {
        modalService.open(modal);
    }
}