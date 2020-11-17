/**
 * The controller is a JavaScript function that augments the AngularJS scope and exposes functions that can be used in the custom widget template
 * 
 * Custom widget properties defined on the right can be used as variables in a controller with $scope.properties
 * To use AngularJS standard services, you must declare them in the main function arguments.
 * 
 * You can leave the controller empty if you do not need it.
 */
function ($scope) {
    var white = 'white';
    
    // add a new variable in AngularJS scope. It'll be usable in the template directly with {{ backgroudColor }} 
    $scope.backgroudColor = white;
    
    // define a function to be used in template with ctrl.toggleBackgroundColor()
    this.toggleBackgroundColor = function() {
        if ($scope.backgroudColor === white) {
           // use the custom widget property backgroudColor with $scope.properties.backgroudColor
            $scope.backgroudColor = $scope.properties.background;
        } else {
            $scope.backgroudColor = white;
        }
    };
    $scope.$watch(function(){
        debugger;
        if($scope.properties.getCatRegistro !== undefined){
            if($scope.properties.getCatRegistro.length > 0){
                if($scope.properties.isDataRegistro === undefined || $scope.properties.isDataRegistro === null || $scope.properties.isDataRegistro === ""){
                    $scope.properties.isDataRegistro = "Lleno";
                     $scope.properties.formInput.catSolicitudDeAdmisionInput.primerNombre = $scope.properties.getCatRegistro[0].primernombre;
                        $scope.properties.formInput.catSolicitudDeAdmisionInput.segundoNombre = $scope.properties.getCatRegistro[0].segundonombre; 
                        $scope.properties.formInput.catSolicitudDeAdmisionInput.apellidoPaterno = $scope.properties.getCatRegistro[0].apellidopaterno; 
                        $scope.properties.formInput.catSolicitudDeAdmisionInput.apellidoMaterno = $scope.properties.getCatRegistro[0].apellidomaterno;
                }
            }
        }
        
    });
    
    /*
    $data.isDataRegistro === "Lleno";
        $data.formInput.catSolicitudDeAdmisionInput.primerNombre = $data.getCatRegistro[0].primernombre;
        $data.formInput.catSolicitudDeAdmisionInput.segundoNombre = $data.getCatRegistro[0].segundonombre; 
        $data.formInput.catSolicitudDeAdmisionInput.apellidoPaterno = $data.getCatRegistro[0].apellidopaterno; 
        $data.formInput.catSolicitudDeAdmisionInput.apellidoMaterno = $data.getCatRegistro[0].apellidomaterno;
    */
    
}