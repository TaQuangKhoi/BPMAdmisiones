function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {

    $scope.$watch('properties.caseid', function(value) {
        if ($scope.properties.caseid !== undefined) {
            let url = "/bonita/API/extension/AnahuacRest?url=getPadresTutorVencido&p=0&c=100";
            var req = {
                method: "POST",
                url: url,
                data: {
                    "caseid": $scope.properties.caseid
                }
            };

            return $http(req).success(function(data, status) {
                    $scope.properties.lstTutor = data.data;
                    $scope.getDatosPadres();
                })
                .error(function(data, status) {
                    swal("Error", data.message, "error");
                })
                .finally(function() {

                });
        }
    });

    $scope.getDatosPadres = function() {
        let url = "/bonita/API/extension/AnahuacRest?url=getPadreVencido&p=0&c=100";
        var req = {
            method: "POST",
            url: url,
            data: {
                "caseid": $scope.properties.caseid
            }
        };

        return $http(req).success(function(data, status) {
                $scope.properties.Padreobj = data.data[0];
                $scope.getDatosMadres();
            })
            .error(function(data, status) {
                swal("Error", data.message, "error");
            })
            .finally(function() {

            });
    }

    $scope.getDatosMadres = function() {
        let url = "/bonita/API/extension/AnahuacRest?url=getMadreVencido&p=0&c=100";
        var req = {
            method: "POST",
            url: url,
            data: {
                "caseid": $scope.properties.caseid
            }
        };

        return $http(req).success(function(data, status) {
                $scope.properties.madreObj = data.data[0];
                $scope.getDatosContacto();
            })
            .error(function(data, status) {
                swal("Error", data.message, "error");
            })
            .finally(function() {

            });
    }

    $scope.getDatosContacto = function() {
        let url = "/bonita/API/extension/AnahuacRest?url=getContactoEmergencia&p=0&c=100";
        var req = {
            method: "POST",
            url: url,
            data: {
                "caseid": $scope.properties.caseid
            }
        };

        return $http(req).success(function(data, status) {
                $scope.properties.contactoEmergenciaObj = data.data;
                $scope.getHermanosVencidos();
            })
            .error(function(data, status) {
                swal("Error", data.message, "error");
            })
            .finally(function() {

            });
    }

    $scope.getHermanosVencidos = function() {
        let url = "/bonita/API/extension/AnahuacRest?url=getHermanosVencidos&p=0&c=100";
        var req = {
            method: "POST",
            url: url,
            data: {
                "caseid": $scope.properties.caseid
            }
        };

        return $http(req).success(function(data, status) {

                if (data.data.length > 0) {
                    $scope.properties.hermanosObj = data.data;
                    $scope.getInformacionEscolarVencidos();
                } else {
                    $scope.getInformacionEscolarVencidos();
                }
            })
            .error(function(data, status) {
                swal("Error", data.message, "error");
            })
            .finally(function() {

            });
    }

    $scope.getInformacionEscolarVencidos = function() {
        let url = "/bonita/API/extension/AnahuacRest?url=getInformacionEscolarVencidos&p=0&c=100";
        var req = {
            method: "POST",
            url: url,
            data: {
                "caseid": $scope.properties.caseid
            }
        };

        return $http(req).success(function(data, status) {
                if (data.data.length > 0) {
                    $scope.properties.infoEscolarObj = data.data;
                    $scope.getUniviersidadesVencidos();
                } else {
                    $scope.getUniviersidadesVencidos();
                }

            })
            .error(function(data, status) {
                swal("Error", data.message, "error");
            })
            .finally(function() {

            });
    }

    $scope.getUniviersidadesVencidos = function() {
        let url = "/bonita/API/extension/AnahuacRest?url=getUniviersidadesVencidos&p=0&c=100";
        var req = {
            method: "POST",
            url: url,
            data: {
                "caseid": $scope.properties.caseid
            }
        };

        return $http(req).success(function(data, status) {
                if (data.data.length > 0) {
                    $scope.properties.universidadesObj = data.data;
                    $scope.getIdiomaVencidos();
                } else {
                    $scope.getIdiomaVencidos();
                }


            })
            .error(function(data, status) {
                swal("Error", data.message, "error");
            })
            .finally(function() {

            });
    }

    $scope.getIdiomaVencidos = function() {
        let url = "/bonita/API/extension/AnahuacRest?url=getIdiomaVencidos&p=0&c=100";
        var req = {
            method: "POST",
            url: url,
            data: {
                "caseid": $scope.properties.caseid
            }
        };

        return $http(req).success(function(data, status) {
                if (data.data.length > 0) {
                    $scope.properties.IdiomasObj = data.data;
                    $scope.getTerapiaVencidos();
                } else {
                    $scope.getTerapiaVencidos();
                }

            })
            .error(function(data, status) {
                swal("Error", data.message, "error");
            })
            .finally(function() {

            });
    }

    $scope.getTerapiaVencidos = function() {
        let url = "/bonita/API/extension/AnahuacRest?url=getTerapiaVencidos&p=0&c=100";
        var req = {
            method: "POST",
            url: url,
            data: {
                "caseid": $scope.properties.caseid
            }
        };

        return $http(req).success(function(data, status) {
                if (data.data.length > 0) {
                    $scope.properties.terapiasObj = data.data;
                    $scope.getGrupoSocialVencidos();
                } else {
                    $scope.getGrupoSocialVencidos();
                }

            })
            .error(function(data, status) {
                swal("Error", data.message, "error");
            })
            .finally(function() {

            });
    }

    $scope.getGrupoSocialVencidos = function() {
        let url = "/bonita/API/extension/AnahuacRest?url=getGrupoSocialVencidos&p=0&c=100";
        var req = {
            method: "POST",
            url: url,
            data: {
                "caseid": $scope.properties.caseid
            }
        };

        return $http(req).success(function(data, status) {
                if (data.data.length > 0) {
                    $scope.properties.gruposSocialesObj = data.data;
                    $scope.getParienteEgresadoVencidos();
                } else {
                    $scope.getParienteEgresadoVencidos();
                }

            })
            .error(function(data, status) {
                swal("Error", data.message, "error");
            })
            .finally(function() {

            });
    }

    $scope.getParienteEgresadoVencidos = function() {
        let url = "/bonita/API/extension/AnahuacRest?url=getParienteEgresadoVencidos&p=0&c=100";
        var req = {
            method: "POST",
            url: url,
            data: {
                "caseid": $scope.properties.caseid
            }
        };

        return $http(req).success(function(data, status) {
                $scope.properties.parienteEgresadoAnahuacObj = data.data;
            })
            .error(function(data, status) {
                swal("Error", data.message, "error");
            })
            .finally(function() {

            });
    }
}