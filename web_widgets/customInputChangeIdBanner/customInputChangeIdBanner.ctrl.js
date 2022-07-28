function PbInputCtrl($scope,$http, $log, widgetNameFactory,blockUI) {

  'use strict';

  this.name = widgetNameFactory.getName('pbInput');

  if (!$scope.properties.isBound('value')) {
    $log.error('the pbInput property named "value" need to be bound to a variable');
  }
  
    $scope.$watch("properties.value", function (newValue, oldValue) {
        if (newValue !== undefined) {
            if(newValue.length === 8  ){
                doRequest("GET", $scope.properties.urlGet);
            }
        }
    });
    
    
    
    function doRequest(method, url) {
        blockUI.start();
        let urlGet = angular.copy(url)+($scope.properties.value)
        var req = {
            method: method,
            url: urlGet
        };
        return $http(req)
            .success(function (data, status) {
                if(data.data.length < 1){
                    swal(`¡No se ha encontra un aspirante con ese id banner!`,"","warning")
                }else{
                    $scope.properties.caseid = data.data[0].caseid;
                    $scope.properties.sesiones = data.data[0].sesiones_pid;
                    $scope.properties.idbanner = data.data[0].idbanner;
                    //var url = "/portal/resource/app/administrativo/ResultadoINVP/content/?idbanner="+data.data[0].idbanner+"&idsesion="+data.data[0].sesiones_pid+"&individual=true"
                    //window.location.href= url
                }
            })
            .error(function (data, status) {
                
            })
            .finally(function () {
                
                blockUI.stop();
            });
    }
    
    
    
    $scope.forceKeyPressUppercase=function(e) {
        var charInput = e.keyCode;
        var limite = $scope.properties.maxLength === 1?250:$scope.properties.maxLength;
        if((e.target.value.length) <limite){
        }else{
            var start = e.target.selectionStart;
            var end = e.target.selectionEnd;
            e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
            e.target.setSelectionRange(start+1, start+1);
            e.preventDefault();
        }
    }
    
    
}
