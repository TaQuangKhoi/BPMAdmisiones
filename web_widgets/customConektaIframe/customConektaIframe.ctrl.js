/**
 * The controller is a JavaScript function that augments the AngularJS scope and exposes functions that can be used in the custom widget template
 * 
 * Custom widget properties defined on the right can be used as variables in a controller with $scope.properties
 * To use AngularJS standard services, you must declare them in the main function arguments.
 * 
 * You can leave the controller empty if you do not need it.
 */
function ($scope, $http) {
    let obj = {
        method: 'POST',
        url: 'https://api.conekta.io/tokens',
        data:{
            'checkout': {
                'returns_control_on': 'Token'
            }   
        }, 
        headers:{
            'Accept': 'application/vnd.conekta-v2.0.0+json',
            'Content-Type': 'application/json',
            'Authorization': 'Basic a2V5X3NESEtxZ3lyaDl4R3h2ZnQzcmhnNUE='
        }
        
    };
    
    $http(obj).success(function(success){
        debugger;
    }).error(function(err){
        debugger;
    });
}