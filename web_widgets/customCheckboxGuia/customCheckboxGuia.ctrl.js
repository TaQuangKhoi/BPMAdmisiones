function PbCheckboxCtrl($scope, $log, widgetNameFactory) {

$scope.clicked=function(){
    if($scope.properties.guiaDeExamen==''){
        $scope.properties.guiaDeExamen="Contiene guia";
    }else{
        $scope.properties.guiaDeExamen='';
    }
}
  $scope.$watch('properties.value', function(value) {
    if (value === 'true' || value === true) {
      $scope.properties.value = true;
    } else {
      $scope.properties.value = false;
    }
  });

  this.name = widgetNameFactory.getName('pbCheckbox');

  if (!$scope.properties.isBound('value')) {
    $log.error('the pbCheckbox property named "value" need to be bound to a variable');
  }
}
