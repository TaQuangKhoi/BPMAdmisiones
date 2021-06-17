function actualizarInformacionCtrl($scope, $http) {
    $scope.showAlert = function(){
        Swal.fire({
            title: '¡Alerta!',
            text: 'Está a punto de modificar un dato proporcionado por el aspirante, ¿Está seguro de hacerlo?.',
            showDenyButton: true,
            showCancelButton: false,
            confirmButtonText: 'OK',
            denyButtonText: 'Cancelar',
            allowOutsideClick: false
        }).then((result) => {
            if (result.isConfirmed) {
                $scope.properties.isActualizarInformacion = true;
                $scope.properties.objSolicitudDeAdmisionRespaldo = angular.copy($scope.properties.objSolicitudDeAdmision);
                $scope.properties.objTutorRespaldo = angular.copy($scope.properties.objTutor);
            } else if (result.isDenied) {
                $scope.properties.isActualizarInformacion = false;
            }
            $scope.$apply();
        })
    }

    $scope.restoreData = function(){
    	if($scope.properties.objSolicitudDeAdmisionRespaldo.persistenceId !== undefined){
	    	$scope.properties.objSolicitudDeAdmision = angular.copy($scope.properties.objSolicitudDeAdmisionRespaldo);
	        $scope.properties.objTutor = angular.copy($scope.properties.objTutorRespaldo);
	    }
	    else {
	    	console.log("entro a la alerta")
	    }
    }
    
    $scope.$watchCollection("properties.isActualizarInformacion", function(newValue, oldValue) {
        if ($scope.properties.isActualizarInformacion !== undefined) {
            if ($scope.properties.isActualizarInformacion) {
                $scope.showAlert();
            }
            else {
            	$scope.restoreData();
            }
        }
    });
}