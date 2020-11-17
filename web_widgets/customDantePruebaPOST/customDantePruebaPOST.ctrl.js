/**
 * The controller is a JavaScript function that augments the AngularJS scope and exposes functions that can be used in the custom widget template
 * 
 * Custom widget properties defined on the right can be used as variables in a controller with $scope.properties
 * To use AngularJS standard services, you must declare them in the main function arguments.
 * 
 * You can leave the controller empty if you do not need it.
 */
function ($scope, $http) {
   
    $scope.pruebaPOST = function(){
       $http.get('../API/identity/user?p=0&c=10&o=lastname%20ASC&f=enabled%3dtrue').then(successCallback, errorCallback);
       
    }
   
    function successCallback(variable){
        console.log(variable)
       
    }
   
    function errorCallback(){
       
    }
}