function PbInputCtrl($scope, $log, widgetNameFactory,$http) {

  'use strict';

  this.name = widgetNameFactory.getName('pbInput');

  if (!$scope.properties.isBound('value')) {
    $log.error('the pbInput property named "value" need to be bound to a variable');
  }
  
    $scope.forceKeyPressUppercase = function(e)
  {
    var charInput = e.keyCode;
    var limite = $scope.properties.maxLength === 0 ?250:$scope.properties.maxLength;
    if((charInput >=48) && (charInput <=57)&&(e.target.value.length) <limite){
        if($scope.properties.value >= $scope.properties.max){
            $scope.properties.value = null;
            $scope.properties.value = $scope.properties.max+"";
        }else{
            doRequest($scope.properties.value);
        }
    }else{
            $scope.properties.value = null;
            $scope.properties.value = $scope.properties.max+"";
            var start = e.target.selectionStart;
            var end = e.target.selectionEnd;
            e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
            e.target.setSelectionRange(start+1, start+1);
            e.preventDefault();
    }
     
    
    

  }
  
    $scope.$watch("properties.value",function(){
        if($scope.properties.value >= $scope.properties.max){
            $scope.properties.value = null;
            $scope.properties.value = "";
        }
        if($scope.properties.value % 1 != 0){
            $scope.properties.value = (parseFloat($scope.properties.value).toFixed(2))+"";
            $scope.properties.value = parseFloat($scope.properties.value)+"";
            $scope.$apply();
        }
    });
    
    //API/bdm/businessData/com.anahuac.catalogos.CatCampus?q=getCatCampus&p=0&c=999
    
    function doRequest(cantidad) {
      if(isNumeric(cantidad)){
        var req = {
          method: "GET",
          url: `../API/bdm/businessData/com.anahuac.catalogos.CatEscalaEAC?q=findByEscala&f=escala=${cantidad}&p=0&c=999`,
        };

        return $http(req)
          .success(function(data, status) {
              if(data.length > 0){
                  return data[0].equivalenteKP;
              }
              return 0
          })
          .error(function(data, status) {
              return 0;
          })
      }
      
   }
   
   function isNumeric(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
   }
    
}
