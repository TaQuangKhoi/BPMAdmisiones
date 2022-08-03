function ($scope, $http) {

    var loaded = false;
    $scope.setCardObject = function (_token) {
        $scope.properties.tokenObject["card"] = _token;
    }

    function getConektaTokenbObject() {
        var req = {
            method: "POST",
            url: "https://api.conekta.io/tokens",
            data: {
                "checkout": {
                    "returns_control_on": "Token"
                }
            },
            headers: {
                "Authorization": "Basic " + $scope.properties.cryptedKey,
                "Accept": "application/vnd.conekta-v2.0.0+json",
                "Accept-Language": "es"
            }
        };

        return $http(req).success(function (data, status) {
            $scope.properties.tokenObject = data;
        }).error(function (data, status) {
            // $scope.properties.dataFromError = data;
        }).finally(function () {
            if ($scope.properties.tokenObject) {
                setIframe($scope);
            }
        });
    }

    $scope.$watch("properties.publicKey", () => {
        if ($scope.properties.publicKey && $scope.properties.cryptedKey) {
            if (!loaded) {
                loaded = true;
                getConektaTokenbObject();
            }

        }
    });

    $scope.$watch("properties.cryptedKey", () => {
        if ($scope.properties.publicKey && $scope.properties.cryptedKey) {
            if (!loaded) {
                loaded = true;
                getConektaTokenbObject();
            }
        }
    });

    function setIframe() {
        window.ConektaCheckoutComponents.Card({
            targetIFrame: "#conektaIframeContainer",
            allowTokenization: true,
            checkoutRequestId: $scope.properties.tokenObject.checkout.id,
            publicKey: $scope.properties.publicKey,
            options: {
                styles: {
                    inputType: 'basic', //'basic' | 'rounded' | 'line'
                    buttonType: 'basic', //'basic' | 'rounded' | 'sharp'
                    //Elemento que personaliza el borde de color de los elementos            
                    states: {
                        empty: {
                            borderColor: '#FFAA00'
                        },
                        invalid: {
                            borderColor: '#FF00E0'
                        },
                        valid: {
                            borderColor: '#FF5900'
                        }
                    }
                },
                languaje: 'es', // 'es' Español | 'en' Ingles
                button: {
                    colorText: '#ffffff',
                    text: 'Alta de tarjeta',
                    backgroundColor: '#FF5900'
                },
                //Elemento que personaliza el diseño del iframe
                iframe: {
                    colorText: '#65A39B',
                    backgroundColor: '#FFFFFF'
                }
            },
            onCreateTokenSucceeded: function (_token) {
                var scope = angular.element($("custom-conekta-iframe")).scope();
                scope.$apply(function(){
                    // scope.properties.tokenObject["card"] = _token;
                    scope.buildCardObject();
                })
            },
            onCreateTokenError: function (error) {
                swal("Error", error.data.message_to_purchaser, "error");
            }
        });
    }
    
    $scope.buildCardObject = function(){
        debugger;
        $scope.properties.objectCard = {
            "name": $scope.properties.objSolicitudAdmision.primerNombre + " " + $scope.properties.objSolicitudAdmision.apellidoPaterno,
            "email": $scope.properties.objSolicitudAdmision.correoElectronico,
            "phone": $scope.properties.objSolicitudAdmision.telefono,
            "campus_id": $scope.properties.objSolicitudAdmision.catCampus.persistenceId,
            "unit_price": "",
            "idToken": $scope.properties.tokenObject.id,
            "caseId" : $scope.properties.objSolicitudAdmision.caseId,
            "last4": "",
            "nombrePago": "",
            "campus": $scope.properties.objSolicitudAdmision.catCampus.descripcion,
            "idbanner": ""
        };
    }
}