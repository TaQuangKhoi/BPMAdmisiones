function PbInputCtrl($scope, $log, widgetNameFactory, $http, $window) {

    'use strict';
    var vm = this;
    this.name = widgetNameFactory.getName('pbInput');
    //console.log($scope.properties.idInput)
    if (!$scope.properties.isBound('value')) {
        $log.error('the pbInput property named "value" need to be bound to a variable');
    }

    $scope.forceKeyPressUppercase = function(e) {

        var charInput = e.keyCode;
        var limite = $scope.properties.maxLength === 1 ? 250 : $scope.properties.maxLength;
        if ((e.target.value.length) < limite) {} else {
            var start = e.target.selectionStart;
            var end = e.target.selectionEnd;
            e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
            e.target.setSelectionRange(start + 1, start + 1);
            e.preventDefault();
        }




    }
    $scope.nuevoCorreo = function() {
            swal({
                text: 'Capturar nuevo el correo electrónico.',
                icon: 'info',
                content: "input",
                buttons: {
                    confirm: { text: 'Cambiar', className: 'sweet-warning' },
                    cancel: 'Cancelar'
                },
                confirmButtonColor: "#ff5900",
            }).then(name => {
                var re= /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                if (!name) throw null;
                if (!re.test(name)) throw "Correo invalido";
                doRequest("POST", "/bonita/API/extension/AnahuacRest?url=updateCorreoElectronico&p=0&c=10", {}, { "correoNuevo": name, "correoAnterior": $scope.properties.value }, function(response) {
                    $scope.createOrUpdateUsuariosRegistrados(name)
                });
            }).catch(err => {
                if (err) {
                    swal("Error", "¡Favor de asignar correo válido!", "error");
                } else {
                    swal.stopLoading();
                    swal.close();
                }
            });
        }
        /**
         * Execute a get/post request to an URL
         * It also bind custom data from success|error to a data
         * @return {void}
         */
    function doRequest(method, url, params, dataToSend, callback) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                callback(data);
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {
                vm.busy = false;
            });
    }

	$scope.createOrUpdateUsuariosRegistrados = function(correoElectronico){
    	var req = {
            method: "POST",
            url: "../API/extension/AnahuacRest?url=createOrUpdateUsuariosRegistrados&p=0&c=100",
            data: angular.copy({"email":correoElectronico})
        };
        $http(req).success(function(data, status) {
        	console.log(data);
        	swal.stopLoading();
            swal.close();
            $window.location.assign("/portal/resource/app/administrativo/UsuariosRegistrados/content/?app=administrativo");
    	}).error(function(data, status) {
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status});
        }).finally(function() {
            
            vm.busy = false;
            blockUI.stop();
        });
    }

    //document.getElementById(""+$scope.properties.idInput.toString()).addEventListener("keypress", forceKeyPressUppercase, false);
}