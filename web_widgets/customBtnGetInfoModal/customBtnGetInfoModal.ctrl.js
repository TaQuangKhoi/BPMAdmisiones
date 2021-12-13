function PbButtonCtrl($scope, $http, $window, blockUI) {

    'use strict';

    var vm = this;

    $scope.$watch("properties.value", function(newValue, oldValue) {
        
        if (newValue !== undefined) {
            //if($scope.properties.lstContenido.length >1){return }
            doRequest();
        }
    });


    function doRequest() {

        blockUI.start();
        var req = {
            method: "GET",
            url: `/bonita/API/extension/AnahuacRestGet?url=${$scope.properties.url}&p=0&c=100${$scope.properties.urlInfo}`,
        };

        return $http(req)
            .success(function(data, status) {
                sw2Action()
                console.log(data);
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            }).finally(function() {

                blockUI.stop();
            });
    }
    
    
    async function sw2Action(){
        
        let options = {};
        $scope.properties.value.forEach( element =>{
           options[element.clave] = element.descripcion; 
        });
        
        const { value } = await Swal.fire({
            title: 'Se tiene que seleccionar un periodo nuevo ',
            input: 'select',
            allowOutsideClick:false,
            inputOptions: options,
            inputPlaceholder: 'Seleccionar un periodo activo',
            showCancelButton: false,
            confirmButtonText: 'Guardar periodo',
            confirmButtonColor: "FF5900",
            inputValidator: (value) => {
                return new Promise((resolve) => {
                    if (value !== "") {
                        resolve()
                    } else {
                        resolve('Se tiene que seleccionar un periodo')
                    }
                })
            }
        })
        if (value) {
            
            Swal.fire(`El periodo seleccionado es ${options[value]}`)
        }
        
    }

}