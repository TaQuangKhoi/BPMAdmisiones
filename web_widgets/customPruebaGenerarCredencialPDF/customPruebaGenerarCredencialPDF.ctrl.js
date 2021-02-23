function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {
    $scope.watcherCont = 0;
    $scope.watcherCont2 = 0;

    $scope.fecha11;
    $scope.fecha12;
    $scope.fecha13;

    $scope.fecha21;
    $scope.fecha22;
    $scope.fecha23;

    $scope.fecha31;
    $scope.fecha32;
    $scope.fecha33;

    $scope.asistenciaCollegeBoard = false;
    $scope.asistenciaPsicometrico = false;
    $scope.asistenciaEntrevista = false;

    $scope.$watchCollection('properties.datosUsuarioId', function(items) {

        if ($scope.properties.datosUsuarioId != undefined) {
            var qrcode = new QRCode(document.getElementById("qrcode"))
            var JSONs = '{"nombre":"' + $scope.properties.usuario.primerNombre + " " + $scope.properties.usuario.segundoNombre + " " + $scope.properties.usuario.apellidoPaterno + " " + $scope.properties.usuario.apellidoMaterno + '","idbanner":"' + $scope.properties.datosUsuarioId.idBanner + '"}';
            console.log(JSONs)
            qrcode.makeCode(JSONs);

        }
    });

    var vm = this;
    $scope.lstDatos = null;

    $scope.$watchCollection('properties.correoUsuario', function(items) {
        if ($scope.properties.correoUsuario != undefined) {
            $scope.getDataImagenUsuario();
        }
    });



    function doRequest(method, url, params, dataToSend, extra, callback) {
        vm.busy = true;
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                callback(data, extra)
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {
                vm.busy = false;
                blockUI.stop();
            });
    }

    $scope.getDataImagenUsuario = function() {
        $scope.dataToSend = {
            "tarea": "Validar Información",
            "lstFiltro": [{ "columna": "EMAIL", "operador": "Igual a", "valor": $scope.properties.correoUsuario.userName }],
            "type": "aspirantes_proceso",
            "orderby": "",
            "orientation": "DESC",
            "limit": 20,
            "offset": 0,
            "usuario": "Administrador"
        }

        doRequest("POST", "/bonita/API/extension/AnahuacRest?url=selectAspirantesEnproceso&p=0&c=100", null, $scope.dataToSend, null, function(datos, extra) {
            $scope.lstDatos = datos.data;
        })
    }



    $scope.loadAsistenciaCollegeBoard = function() {
        debugger
        doRequest("GET", "../API/bpm/caseVariable/" + $scope.properties.caseId + "/asistenciaCollegeBoard", null, null, null, function(datos, extra) {
            debugger
            $scope.asistenciaCollegeBoard = (datos.value === "true");

        })
    }
    $scope.loadAsistenciaPsicometrico = function() {
        debugger
        doRequest("GET", "../API/bpm/caseVariable/" + $scope.properties.caseId + "/asistenciaPsicometrico", null, null, null, function(datos, extra) {
            debugger
            $scope.asistenciaPsicometrico = (datos.value === "true");

        })
    }
    $scope.loadAsistenciaEntrevista = function() {
        debugger
        doRequest("GET", "../API/bpm/caseVariable/" + $scope.properties.caseId + "/asistenciaEntrevista", null, null, null, function(datos, extra) {
            debugger
            $scope.asistenciaEntrevista = (datos.value === "true");
        })
    }


    $scope.$watchCollection('properties.fechasExamenes', function() {

        if ($scope.properties.fechasExamenes != undefined && $scope.watcherCont === 0) {
            $scope.EjecutarFechas();
            $scope.watcherCont = 1;
        }
    });

    $scope.$watchCollection('properties.datosUsuarioId', function() {
        if ($scope.properties.datosUsuarioId != undefined && $scope.watcherCont2 === 0) {
            $scope.EjecutarFechas();
            $scope.watcherCont2 = 1;
        }
    });
    $scope.$watchCollection('properties.caseId', function() {
        if ($scope.properties.caseId != undefined) {
            $scope.loadAsistenciaCollegeBoard();
            $scope.loadAsistenciaPsicometrico();
            $scope.loadAsistenciaEntrevista();
        }
    });



    $scope.$watchCollection('asistenciaCollegeBoard', function() {
        debugger
        if ($scope.asistenciaCollegeBoard != undefined && $scope.asistenciaCollegeBoard === true) {
            //$scope.fecha1;
            $scope.fecha11 = "Realizado";
            $scope.fecha12 = " ";
            $scope.fecha13 = " ";
            //$scope.fecha3;
        }
    });
    $scope.$watchCollection('asistenciaPsicometrico', function() {
        debugger
        if ($scope.asistenciaPsicometrico != undefined && $scope.asistenciaPsicometrico === true) {
            $scope.fecha21 = "Realizado";
            $scope.fecha22 = " ";
            $scope.fecha23 = " ";

        }
    });
    $scope.$watchCollection('asistenciaEntrevista', function() {
        debugger
        if ($scope.asistenciaEntrevista != undefined && $scope.asistenciaEntrevista === true) {
            $scope.fecha31 = "Realizado";
            $scope.fecha32 = " ";
            $scope.fecha33 = " ";

        }
    });


    $scope.EjecutarFechas = function() {
        if ($scope.properties.datosUsuarioId != undefined && $scope.properties.fechasExamenes != undefined) {
            $scope.fecha11 = $scope.getFechas(1, 1);
            $scope.fecha12 = $scope.getFechas(1, 2);
            $scope.fecha13 = $scope.getFechas(1, 3);

            $scope.fecha21 = $scope.getFechas(2, 1);
            $scope.fecha22 = $scope.getFechas(2, 2);
            $scope.fecha23 = $scope.getFechas(2, 3);

            $scope.fecha31 = $scope.getFechas(3, 1);
            $scope.fecha32 = $scope.getFechas(3, 2);
            $scope.fecha33 = $scope.getFechas(3, 3);
        }
    }








    //op es la opcion de la fila (tipo del examen) y dato es que tipo de dato va en la columna(fecha, lugar, Horario)
    $scope.getFechas = function(op, dato) {
        let resultado = "";
        if ($scope.properties.datosUsuarioId != undefined && $scope.properties.fechasExamenes != undefined) {
            if (op === 1) {
                //ES COLLEGGE BOARD
                if ($scope.properties.datosUsuarioId.cbCoincide) { //obtener si tiene college board(si esta excento)
                    if (dato === 1) {
                        //ES APLICACION
                        resultado = "Validado"
                    } else if (dato === 2) {
                        //ES LUGAR
                        resultado = " "
                    } else if (dato === 3) {
                        //ES HORARIO
                        resultado = " "
                    }
                } else if ($scope.asistenciaCollegeBoard != undefined && $scope.asistenciaCollegeBoard === true) {
                    //$scope.fecha1;
                    $scope.fecha11 = "Realizado";
                    $scope.fecha12 = " ";
                    $scope.fecha13 = " ";
                    //$scope.fecha3;
                } else if ($scope.properties.datosUsuarioId.cbCoincide === false || $scope.properties.datosUsuarioId.cbCoincide === "" || $scope.properties.datosUsuarioId.cbCoincide === " " || $scope.properties.datosUsuarioId.cbCoincide === null || $scope.properties.datosUsuarioId.cbCoincide === "null") { //Si no esta excento (tiene fecha asignada)
                    if (dato === 1) {
                        //ES APLICACION
                        let fechaAuxiliar = $scope.properties.fechasExamenes.data[0].aplicacion;
                        resultado = $scope.FechasAcomodado(fechaAuxiliar);
                    } else if (dato === 2) {
                        //ES LUGAR
                        resultado = $scope.properties.fechasExamenes.data[0].lugar
                    } else if (dato === 3) {
                        //ES HORARIO
                        resultado = $scope.properties.fechasExamenes.data[0].horario
                    }
                }

            } else if (op === 2) {
                if ($scope.asistenciaPsicometrico != undefined && $scope.asistenciaPsicometrico === true) {
                    $scope.fecha21 = "Realizado";
                    $scope.fecha22 = " ";
                    $scope.fecha23 = " ";

                } else {
                    //ES EXAMEN PSICOMETRICO

                    if (dato === 1) {
                        //ES APLICACION
                        let fechaAuxiliar = $scope.properties.fechasExamenes.data[1].aplicacion;
                        resultado = $scope.FechasAcomodado(fechaAuxiliar);
                    } else if (dato === 2) {
                        //ES LUGAR
                        resultado = $scope.properties.fechasExamenes.data[1].lugar
                    } else if (dato === 3) {
                        //ES HORARIO
                        resultado = $scope.properties.fechasExamenes.data[1].horario
                    }
                }


            } else if (op === 3) {
                if ($scope.asistenciaEntrevista != undefined && $scope.asistenciaEntrevista === true) {
                    $scope.fecha31 = "Realizado";
                    $scope.fecha32 = " ";
                    $scope.fecha33 = " ";

                } else {
                    //ES ENTREVISTA 
                    if (dato === 1) {
                        //ES APLICACION
                        let fechaAuxiliar = $scope.properties.fechasExamenes.data[2].aplicacion;
                        resultado = $scope.FechasAcomodado(fechaAuxiliar);
                    } else if (dato === 2) {
                        //ES LUGAR
                        resultado = $scope.properties.fechasExamenes.data[2].lugar
                    } else if (dato === 3) {
                        //ES HORARIO
                        resultado = $scope.properties.fechasExamenes.data[2].horario
                    }

                }

            }
        }
        return resultado;

    }

    $scope.FechasAcomodado = function(fecha) {
        let resultado = "";
        var arrayDeCadenas = fecha.split("-");
        resultado = arrayDeCadenas[2] + "-" + arrayDeCadenas[1] + "-" + arrayDeCadenas[0];
        return resultado;
    }
}