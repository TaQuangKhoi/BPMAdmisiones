function PbButtonCtrl($scope, $http, $window) {

  'use strict';

  var vm = this;

    $scope.myFunc = function() {
        $scope.blockPaseLista().then(function() {
            //|| $scope.properties.datosUsuario.aplicacion != $scope.properties.getFechas
            if(!$scope.properties.habilitado  ){
                swal(`¡No es el día (${$scope.properties.datosUsuario.aplicacion}) para pasar lista!`,"","warning")
            }else{
                var asis = angular.copy($scope.properties.dataToSend.asistencia)
                $scope.properties.dataToSend.asistencia = $scope.properties.seleccion;
                var req = {
                    method: "POST",
                    url: asis === null?"/bonita/API/extension/AnahuacRest?url=insertPaseLista&p=0&c=10":"/bonita/API/extension/AnahuacRest?url=updatePaseLista&p=0&c=10",
                    data: angular.copy($scope.properties.dataToSend)
                };
                return $http(req)
                    .success(function (data, status) {
                        if($scope.properties.seleccion === true){
                            swal("¡Asistencia capturada correctamente!","","success")
                        }else{
                            swal("¡Asistencia cancelada correctamente!","","success")    
                        }
                        doRequestCaseValue($scope.properties.seleccion);
                        $scope.properties.regresarTabla = "tabla";
                    })
                    .error(function (data, status) {
                        notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
                    });
                }
        }).catch(err => {
            swal("¡Se ha producido un error!", `Al momento de obtener la fecha del servidor`,"warning", {
                closeOnClickOutside: false,
                buttons: {
                    catch: {
                        text: "OK",
                        value: "OK",
                    }
                },
            })
        });
            
        
            
    };
    
     function doRequestCaseValue (asistencia){
        var caseId = $scope.properties.datosUsuario.caseid;
        var variableNombre = "asistencia"+( $scope.properties.datosUsuario.tipoprueba_pid == 1?"Entrevista": $scope.properties.datosUsuario.tipo_prueba == "Examen Psicométrico" ? "Psicometrico" : "CollegeBoard") 
        var req = {
            method: "PUT",
            url: `/API/bpm/caseVariable/${caseId}/${variableNombre}`,
            data: `{ "type": "java.lang.Boolean","value": "${asistencia}"}`
        };
        return $http(req)
            .success(function (data, status) {
                
        })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        });
    }
    
    
    $scope.blockPaseLista = function(){
        //var d = new Date();
        //var n = moment( (d.getHours() < 10? "0"+d.getHours() : d.getHours()) +":"+ (d.getMinutes() < 10 ? "0"+d.getMinutes() : d.getMinutes() ) , 'HH:mm'  );
        //var fecha = moment(d.getFullYear()+"-"+((d.getMonth()+1) < 10 ?"0"+(d.getMonth()+1):(d.getMonth()+1) )+"-"+(d.getDate() < 10 ? "0"+d.getDate() : d.getDate() ))
        
        //var n = moment("09:00", 'HH:mm');
        //var fecha = moment("2021-01-31", 'YYYY-MM-DD ')
        //console.log( moment(fecha).isSame(row.fecha));
        // && moment("2021-01-31").isSame(row.fecha)
        
       /* var ini = angular.copy($scope.properties.datosUsuario.horario.slice(0,5));
        var last =angular.copy($scope.properties.datosUsuario.horario.slice(8,13));
        
        var inicio = moment(ini, 'HH:mm');
        var fin = moment(last, 'HH:mm');*/
        
        var req = {
            method: "GET",
            url: `/bonita/API/extension/AnahuacRestGet?url=getFechaServidor&p=0&c=100`,
        };
        return $http(req)
            .success(function (data, status) {
                
                let fecha =moment(angular.copy(data.data[0].fecha));
                if(fecha.isSameOrAfter($scope.properties.datosUsuario.aplicacion)  ){
                    $scope.properties.habilitado = true;
                    
                }else{
                    $scope.properties.habilitado = false;
                }
                
                //let fecha = moment(angular.copy(data.data[0].fecha))
                //let fecha2 =moment(angular.copy(data.data[0].fecha)).subtract(1, 'day');
                //for(let i = 0;i<3;i++){
                    
                //}
                //$scope.properties.datosUsuario.fecha
                //|| fecha2.isSame($scope.properties.datosUsuario.aplicacion)
                /*if(fecha.isSame($scope.properties.datosUsuario.aplicacion)  ){
                    $scope.properties.habilitado = true;
                }else{
                    $scope.properties.habilitado = false;
                }*/
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            });
        /*
        if(fecha.isSameOrAfter($scope.properties.datosUsuario.fecha) ){
                $scope.properties.habilitado = true;
        }else{
                $scope.properties.habilitado = false;
        }*/
        
        /*if(row.tipoprueba_PID == "1"){
        
            if(  n.isSameOrAfter(inicio) && n.isSameOrBefore(fin) && fecha.isSame($scope.properties.datosUsuario.fecha)){
                $scope.properties.habilitado = true;
            }else{
                $scope.properties.habilitado = false;
            }
            
        }else{
            
            
        }*/
    }

}
