/**
 * The controller is a JavaScript function that augments the AngularJS scope and exposes functions that can be used in the custom widget template
 * 
 * Custom widget properties defined on the right can be used as variables in a controller with $scope.properties
 * To use AngularJS standard services, you must declare them in the main function arguments.
 * 
 * You can leave the controller empty if you do not need it.
 */
function ($scope, $http) {
      'use strict';

  var vm = this;
     /**
   * Execute a get/post request to an URL
   * It also bind custom data from success|error to a data
   * @return {void}
   */
  function doRequest(method, url, params, dataToSend,funcion) {
    vm.busy = true;
    var req = {
      method: method,
      url: url,
      data: dataToSend
    };

    return $http(req)
      .success(function(data, status) {
          funcion(data);
        })
      .error(function(data, status) {
          console.error(data)})
      .finally(function() {
        vm.busy = false;
      });
  }
  $scope.tablaDatos=[];
  doRequest("GET","../API/bpm/flowNode?p=0&c=999&o=displayName%20ASC&f=state%3dfailed&d=rootContainerId&d=assigned_id",null,null, function(valor){
      $scope.tablaDatos=valor;
  });
  
  $scope.execute=function(){
      for (var i = 0; i < $scope.tablaDatos.length; i++) {
          var id=0;
          id=$scope.tablaDatos[i].id;
	 doRequest("PUT","/API/bpm/flowNode/"+ $scope.tablaDatos[i].id,null,{"state": "replay"}, function(valor){
        console.log("Success " + id);
    });
    }

  }
}