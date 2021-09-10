/**
 * The controller is a JavaScript function that augments the AngularJS scope and exposes functions that can be used in the custom widget template
 * 
 * Custom widget properties defined on the right can be used as variables in a controller with $scope.properties
 * To use AngularJS standard services, you must declare them in the main function arguments.
 * 
 * You can leave the controller empty if you do not need it.
 */
function($scope, $http) {
    var el = null;
    var vanilla = null;
    $scope.$watch('properties.id', function(value) {
        if (angular.isDefined(value) && value !== null) {

            setTimeout(function() {
                el = document.getElementById('vanilla-demo' + $scope.properties.id);
                vanilla = new Croppie(el, {
                    viewport: { width: 725, height: 81 },
                    boundary: { width: 800, height: 200 },
                    showZoomer: false,
                    enableOrientation: true
                });
            }, 500);
        }
    });



    $scope.$watch('properties.url', function(value) {
        if (angular.isDefined(value) && value !== null) {
            vanilla.bind({
                url: value,
                orientation: 1
            });
        }
    });
    //https://i.ibb.co/Q8P9G1X/123678658-10223026328784811-4515884765791087189-n.jpg
    //https://integrasoa.blob.core.windows.net/publico/licenciatura.jpg

    //on button click


    $scope.rotate = function(left) {
        if (left) {
            vanilla.rotate(90)
        } else {
            vanilla.rotate(-90)
        }
    }
    $scope.recortar = function() {
        vanilla.result('blob').then(function(blob) {
            console.log(blob)
            var reader = new FileReader();
            reader.readAsDataURL(blob);
            reader.onloadend = function() {
                var base64data = reader.result;
                console.log(base64data);
                var jsonData = {
                    "b64": base64data,
                    "filename": $scope.properties.filename,
                    "filetype": $scope.properties.filetype,
                    "contenedor": "publico"
                }
                return $http.post('/bonita/API/extension/AnahuacAzureRest?url=uploadFile&p=0&c=10', jsonData, {
                    headers: { 'Content-Type': "application/json" }
                }).then(function(results) {
                    $scope.properties.urlretorno = results.data.data[0];
                    $scope.$apply();
                });
            }
        });
    }



    var hidden = document.getElementsByClassName("oculto");
    for (var i = 0; i < hidden.length; i++) {
        hidden[i].classList.add("invisible")
    }
}