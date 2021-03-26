function PbTableCtrl($scope, $http, blockUI, $window) {
    var vm = this;
    $scope.watcherCont = 0;
    $scope.watcherCont2 = 0;
    $scope.fecha1;
    $scope.fecha2;
    $scope.fecha3;
    $scope.asistenciaCollegeBoard = false;
    $scope.asistenciaPsicometrico = false;
    $scope.asistenciaEntrevista = false;



    this.isArray = Array.isArray;

    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
        }
    };

    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    $scope.loadAsistenciaCollegeBoard = function() {
        
        doRequest("GET", "../API/bpm/caseVariable/" + $scope.properties.caseId + "/asistenciaCollegeBoard", null, null, null, function(datos, extra) {
            
            
            $scope.asistenciaCollegeBoard = (datos.value === "true");

        })
    }
    $scope.loadAsistenciaPsicometrico = function() {
        
        doRequest("GET", "../API/bpm/caseVariable/" + $scope.properties.caseId + "/asistenciaPsicometrico", null, null, null, function(datos, extra) {
            $scope.asistenciaPsicometrico = (datos.value === "true");

        })
    }
    $scope.loadAsistenciaEntrevista = function() {
        
        doRequest("GET", "../API/bpm/caseVariable/" + $scope.properties.caseId + "/asistenciaEntrevista", null, null, null, function(datos, extra) {
            $scope.asistenciaEntrevista = (datos.value === "true");
        })
    }

    function doRequest(method, url, params, dataToSend, extra, callback) {
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
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {

                blockUI.stop();
            });
    }

    $scope.$watchCollection('properties.fechasExamenes', function() {
        $scope.loadAsistenciaCollegeBoard();
            $scope.loadAsistenciaPsicometrico();
            $scope.loadAsistenciaEntrevista();
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

   $scope.$watchCollection('asistenciaCollegeBoard', function() {
       
        if ($scope.asistenciaCollegeBoard != undefined && $scope.asistenciaCollegeBoard=== true) {
            //$scope.fecha1;
            $scope.fecha1= "Realizado";
            //$scope.fecha3;
        }
    });
       $scope.$watchCollection('asistenciaPsicometrico', function() {
       
        if ($scope.asistenciaPsicometrico != undefined && $scope.asistenciaPsicometrico=== true) {
            //$scope.fecha1;
            $scope.fecha2= "Realizado";
            //$scope.fecha3;
        }
    });
       $scope.$watchCollection('asistenciaEntrevista', function() {
       
        if ($scope.asistenciaEntrevista != undefined && $scope.asistenciaEntrevista=== true) {
            //$scope.fecha1;
            $scope.fecha3= "Realizado";
            //$scope.fecha3;
        }
    });
    


    $scope.EjecutarFechas = function() {
        
        if ($scope.properties.datosUsuarioId != undefined && $scope.properties.fechasExamenes != undefined) {
            $scope.fecha1 = $scope.getFechas(1);
            $scope.fecha2 = $scope.getFechas(2);
            $scope.fecha3 = $scope.getFechas(3);
        }


    }

    $scope.getFechas = function(op) {
        
        let resultado = "";

        let fechaEnOrden = "";
        let fechaAsignada = "";




        if ($scope.properties.datosUsuarioId != undefined && $scope.properties.fechasExamenes != undefined) {
            
            $scope.asistenciaCollegeBoard = false;
            $scope.asistenciaPsicometrico = false;
            $scope.asistenciaEntrevista = false;

            $scope.NoCollegeBoard = 0;
            $scope.NoPsicometrico = 0;
            $scope.NoEntrevista = 0;

            for(var x = 0; x < $scope.properties.fechasExamenes.data.length; x++){
                if($scope.properties.fechasExamenes.data[x].descripcion==="Examen de aptitudes y conocimientos"){
                    $scope.NoCollegeBoard = x;
                    
                }else if($scope.properties.fechasExamenes.data[x].descripcion==="Examen Psicométrico"){

                    $scope.NoPsicometrico = x;

                }else{
                    $scope.NoEntrevista = x;
                }

            }
            if (op === 1) {
                //ES COLLEGGE BOARD
                if ($scope.properties.datosUsuarioId.cbCoincide || $scope.asistenciaCollegeBoard) { //obtener si tiene college board(si esta excento)
                    resultado = "Validado";
                    if($scope.asistenciaCollegeBoard){
                        resultado = "Realizado";
                    }
                } else if ($scope.properties.datosUsuarioId.cbCoincide === false || $scope.properties.datosUsuarioId.cbCoincide === "" || $scope.properties.datosUsuarioId.cbCoincide === " " || $scope.properties.datosUsuarioId.cbCoincide === null || $scope.properties.datosUsuarioId.cbCoincide === "null") { //Si no esta excento (tiene fecha asignada)
                    fechaAsignada = $scope.properties.fechasExamenes.data[$scope.NoCollegeBoard].aplicacion;
                    fechaEnOrden = $scope.FechasAcomodado(fechaAsignada);
                    resultado = fechaEnOrden + " " + $scope.properties.fechasExamenes.data[$scope.NoCollegeBoard].horario + "    " + $scope.properties.fechasExamenes.data[$scope.NoCollegeBoard].lugar;
                }


            } else if (op === 2) {
                if ($scope.asistenciaPsicometrico) {
                    resultado = "Realizado";
                } else {
                    fechaAsignada = $scope.properties.fechasExamenes.data[$scope.NoPsicometrico].aplicacion;
                    fechaEnOrden = $scope.FechasAcomodado(fechaAsignada);
                    resultado = fechaEnOrden + " " + $scope.properties.fechasExamenes.data[$scope.NoPsicometrico].horario + "    " + $scope.properties.fechasExamenes.data[$scope.NoPsicometrico].lugar;
                }


            } else if (op === 3) {
                if ($scope.asistenciaEntrevista) {
                    resultado = "Realizado";
                } else {
                    fechaAsignada = $scope.properties.fechasExamenes.data[$scope.NoEntrevista].aplicacion;
                    fechaEnOrden = $scope.FechasAcomodado(fechaAsignada);
                    resultado = fechaEnOrden + " " + $scope.properties.fechasExamenes.data[$scope.NoEntrevista].horario + "    " + $scope.properties.fechasExamenes.data[$scope.NoEntrevista].lugar;
                }
            }
        }
        return resultado;
    }

    $scope.FechasAcomodado = function(fecha) {
        let resultado = "";
        var arrayDeCadenas = fecha.split("-");
        resultado = arrayDeCadenas[2] + "-" + arrayDeCadenas[1] + "-" + arrayDeCadenas[0];
        console.log(resultado)
        return resultado;


    }
}