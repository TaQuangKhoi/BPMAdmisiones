/**
 * The controller is a JavaScript function that augments the AngularJS scope and exposes functions that can be used in the custom widget template
 * 
 * Custom widget properties defined on the right can be used as variables in a controller with $scope.properties
 * To use AngularJS standard services, you must declare them in the main function arguments.
 * 
 * You can leave the controller empty if you do not need it.
 */
function ($scope) {
    $scope.llenoGestion = false;
    $scope.llenoSeleccion = false;
    $scope.llenoEntero = false;
    $scope.llenoPrepa = false;
    $scope.gestionEscolarselected;
    
    var unregister = $scope.$watch("properties.catSolicitudDeAdmision.catGestionEscolar", function(){
        if($scope.properties.catSolicitudDeAdmision.catGestionEscolar !== null && $scope.properties.catSolicitudDeAdmision.catGestionEscolar !== undefined){
            if(!$scope.llenoGestion){
                $scope.gestionEscolarselected = $scope.properties.catSolicitudDeAdmision.catGestionEscolar;
                $scope.llenoGestion = true;
                unregister();
            }
            
        }
    });
    
    $scope.$watch("properties.getGestionEscolarByCampus", function(){
        if($scope.properties.getGestionEscolarByCampus.length > 0){
            console.log($scope.properties.catSolicitudDeAdmision.catGestionEscolar);
            for(var x=0;x<$scope.properties.getGestionEscolarByCampus.length; x++){
                if($scope.gestionEscolarselected.descripcion === $scope.properties.getGestionEscolarByCampus[x].descripcion){
                    if(!$scope.llenoSeleccion){
                        $scope.properties.catSolicitudDeAdmision.catGestionEscolar = $scope.properties.getGestionEscolarByCampus[x];
                        $scope.llenoSeleccion = true;
                    }
                }
            }
        }
    });
    
    $scope.$watch("properties.catSolicitudDeAdmision.promedioGeneral", function(){
        if(!$scope.llenoEntero){
            if($scope.properties.catSolicitudDeAdmision.promedioGeneral !== ""){
                $scope.properties.catSolicitudDeAdmision.promedioGeneral = parseFloat($scope.properties.catSolicitudDeAdmision.promedioGeneral)
                $scope.llenoEntero = true;
            }
        }
    });
    
    $scope.$watch("properties.catSolicitudDeAdmision.catBachilleratos.descripcion", function(){
        if(!$scope.llenoPrepa){
            debugger;
            if($scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion !== null || $scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion !== undefined){
                $scope.properties.preparatoriaSeleccionada = $scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion;
                $scope.llenoPrepa = true;
            }
        }
    });
    
   /* $scope.$watch("properties.catSolicitudDeAdmision.catBachilleratos.descripcion", function(){
        if(!$scope.llenoPrepa){
            if($scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion !== null || $scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion !== undefined){
                $scope.properties.preparatoriaSeleccionada = $scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion;
                $scope.llenoPrepa = true;
            }
        }
    });*/
}