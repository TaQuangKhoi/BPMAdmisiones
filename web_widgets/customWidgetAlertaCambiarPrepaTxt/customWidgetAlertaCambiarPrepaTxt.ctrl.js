function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {

    $scope.$watchCollection('properties.CheckCambiarPrepa', function (items) {
        if ($scope.properties.CheckCambiarPrepa != undefined && $scope.properties.CheckCambiarPrepa===true) {
            Swal.fire("¡Alerta!", "Está a punto de modificar un dato proporcionado por el aspirante, ¿Está seguro de hacerlo?.", "warning");
        }
    });
    $scope.$watchCollection('properties.catTipoAdmision', function (items) {
            if($scope.properties.catTipoAdmision==="Admisión Anáhuac" && !$scope.properties.CheckAA){
                Swal.fire("¡Aviso!", "¿Está seguro de validar la Admisión Anáhuac sin revisar la carta?", "warning");
            }
    });
       


}