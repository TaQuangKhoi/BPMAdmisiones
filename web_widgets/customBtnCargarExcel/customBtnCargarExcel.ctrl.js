function PbButtonCtrl($scope, $http, modalService) {
    'use strict';
    var vm = this;

    this.isArray = Array.isArray;
    $scope.loading = false;


    function doRequest(method, url, data) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy(`{"fecha":"${data}"}`),
            //params: params
        };

        return $http(req)
            .success(function (data, status) {

            })
            .error(function (data, status) {

            })
            .finally(function () {
                vm.busy = false;
            });
    }



    this.cargarExcel =  async function cargarExcel() {
        const inputValue = `${$scope.properties.getDate}`;
        const { value: fecha } = await Swal.fire({
            title: 'Ingrese la fecha para general los excel',
            input: 'text',
            inputLabel: 'fecha con el formato yyyy-mm-dd',
            inputValue: inputValue,
            showCancelButton: true,
            inputValidator: (value) => {
                if (!value) {
                    return '¡Necesitas escribir algo!'
                }else{
                    var pattern =/^([0-9]{4})\-([0-9]{2})\-([0-9]{2})$/;

                    if(pattern.test(value)){
                        
                    }else{
                        return  '¡Formato incorrecto!'
                    }
                }
            }
        })

        if (fecha) {
            doRequest('POST', $scope.properties.urlConsulta, fecha).then( function () {
              Swal.fire(`Se creara el archvio ${fecha}`)  
            })
            
        }
    };

}
