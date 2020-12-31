function ($scope){
    // var solicitudCargada = false;
    var solicitudCargada = true;//EN TRUE PARA PRURBAS EN LOCAL
    var autodescripcionCargada = false;
    var catGradoEscolar = false;
    var llenadoIniciado = false;
    
    function llenarPaso2(){
        debugger;
        if($scope.properties.bdmAutodescripcion !== undefined){
            if($scope.properties.bdmAutodescripcion.length > 0){
                $scope.properties.formInput.informacionEscolar = $scope.properties.bdmAutodescripcion;
            } else {
                $scope.properties.formInput.informacionEscolar = ordenarGradoEscolar($scope.properties.catGradoEscolar);
            }
        } else {
            $scope.properties.formInput.informacionEscolar = ordenarGradoEscolar($scope.properties.catGradoEscolar);
        }
    }

    function ordenarGradoEscolar(_gradosEscolares){
        let output = [];

        for(let i = 0; i < _gradosEscolares.length; i++){
            if(_gradosEscolares[i].descripcion === "Primaria"){
                let infoEscolar = {
                    "grado": _gradosEscolares[i],
                    "tipo": null,
                    "escuela": null,
                    "pais": null,
                    "estado": null, 
                    "ciudad": "",
                    "anoInicio": "",
                    "anoFin": "",
                    "promedio": "",
                    "catBachillerato": null,
                    "otraEscuela":"",
                    "persistenceId_string": null
                }
                
                output.push(infoEscolar);
            }   
        }

        for(let i = 0; i < _gradosEscolares.length; i++){
            if(_gradosEscolares[i].descripcion === "Secundaria"){
                let infoEscolar = {
                    "grado": _gradosEscolares[i],
                    "tipo": null,
                    "escuela": null,
                    "pais": null,
                    "estado": null, 
                    "ciudad": "",
                    "anoInicio": "",
                    "anoFin": "",
                    "promedio": "",
                    "catBachillerato": null,
                    "otraEscuela":"",
                    "persistenceId_string": null
                }

                output.push(infoEscolar);
            }   
        }

        for(let i = 0; i < _gradosEscolares.length; i++){
            if(_gradosEscolares[i].descripcion === "Preparatoria/Bachillerato"){
                let infoEscolar = {
                    "grado": _gradosEscolares[i],
                    "tipo": null,
                    "escuela": null,
                    "pais": null,
                    "estado": null, 
                    "ciudad": "",
                    "anoInicio": "",
                    "anoFin": "",
                    "promedio": "",
                    "catBachillerato": null,
                    "otraEscuela":"",
                    "persistenceId_string": null
                }
                
                output.push(infoEscolar);
            }   
        }

        return output;
    }
    
    $scope.$watch("properties.bdmSolicitud", function(){
        if($scope.properties.bdmSolicitud !== undefined){
            solicitudCargada = true;
            startWatcherGradoEscolar();
        }
    });

    function startWatcherGradoEscolar(){
        $scope.$watch("properties.catGradoEscolar", function(){
            if($scope.properties.catGradoEscolar !== undefined){
                catGradoEscolar = true;
                startWatcherAD();      
            }
        });
    }

    function startWatcherAD(){
        $scope.$watch("properties.bdmAutodescripcion", function(oldValue, newValue){
            autodescripcionCargada = true;
            if(solicitudCargada && autodescripcionCargada && catGradoEscolar && !llenadoIniciado){
                llenadoIniciado = true;
                llenarPaso2();
            }
        });
    }
    
    
    // $scope.$watch("properties.catGradoEscolar", function(){
    //     if($scope.properties.catGradoEscolar !== undefined){
    //         catGradoEscolar = true;
    //         if(solicitudCargada && autodescripcionCargada && catGradoEscolar && !llenadoIniciado){
    //             llenadoIniciado = true;
    //             llenarPaso2();
    //         }
    //     }
    // });
    
    // $scope.$watch("properties.bdmAutodescripcion", function(oldValue, newValue){
    //     // if($scope.properties.bdmAutodescripcion !== undefined){
    //     //     autodescripcionCargada = true;
    //     //     if(solicitudCargada && autodescripcionCargada && catGradoEscolar && !llenadoIniciado){
    //     //         llenadoIniciado = true;
    //     //         llenarPaso2();
    //     //         //$scope.properties.formInput.informacionEscolar = $scope.properties.bdmAutodescripcion.informacionEscolar
    //     //     }
    //     // }
    //     autodescripcionCargada = true;
    //     if(solicitudCargada && autodescripcionCargada && catGradoEscolar && !llenadoIniciado){
    //         llenadoIniciado = true;
    //         llenarPaso2();
    //         //$scope.properties.formInput.informacionEscolar = $scope.properties.bdmAutodescripcion.informacionEscolar
    //     }
    // });
    
    // $scope.$watch("properties.bdmSolicitud", function(){
    //     if($scope.properties.bdmSolicitud !== undefined){
    //         solicitudCargada = true;
    //         if(solicitudCargada && autodescripcionCargada && catGradoEscolar && !llenadoIniciado){
    //             llenadoIniciado = true;
    //             llenarPaso2();
    //         }
    //     }
    // });
}