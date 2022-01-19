/**
 * The controller is a JavaScript function that augments the AngularJS scope and exposes functions that can be used in the custom widget template
 * 
 * Custom widget properties defined on the right can be used as variables in a controller with $scope.properties
 * To use AngularJS standard services, you must declare them in the main function arguments.
 * 
 * You can leave the controller empty if you do not need it.
 */
function ($scope) {
    
      $scope.$watch('properties.solicitudDeAdmision', function(value) {
        if (angular.isDefined(value) && value !== null) {
             $scope.validarPeriodo(value.catPeriodo.fechaFin)
        }
  });
  $scope.validarPeriodo=function(periodofin){
      console.log("isPeriodoVencido?")
      var fecha = new Date(periodofin.slice(0,10));
      if (fecha<new Date()){
          swal("¡Periodo vencido!", "El periodo de ingreso que seleccionaste ya ha vencido, ponte en contacto con el área de admisiones por medio del chat", "warning");
      }
  }
}