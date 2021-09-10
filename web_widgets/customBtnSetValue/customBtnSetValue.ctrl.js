function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    $scope.confirmacion = function() {
        swal({
                title: `¿Ésta seguro que desea quitar la firma?`,
                text: "Ya no se mostrará en la notificación",
                icon: "warning",
                buttons: true,
                dangerMode: true,
                buttons: ["Cancelar", "Continuar"]
            })
            .then((willDelete) => {
                if (willDelete) {

                    $scope.properties.contenido = ""
                    $scope.properties.usuarioFirmaSeleccionado = undefined;
                    $scope.$apply();
                } else {

                }
            });
    }
}