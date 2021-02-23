function ($scope){
    // var solicitudCargada = false;
    var solicitudCargada = true;//EN TRUE PARA PRURBAS EN LOCAL
    var autodescripcionCargada = false;
    var catGradoEscolar = false;
    var llenadoIniciado = false;
    
    function llenarPaso2(){
        if($scope.properties.bdmAutodescripcion !== undefined){
            if($scope.properties.bdmAutodescripcion.length > 0){
                $scope.properties.formInput.informacionEscolar = $scope.properties.bdmAutodescripcion;
            } else {
                // $scope.properties.formInput.informacionEscolar = ordenarGradoEscolar($scope.properties.catGradoEscolar);
                if($scope.properties.formInput.informacionEscolar === null || $scope.properties.formInput.informacionEscolar === undefined){
                    $scope.properties.formInput.informacionEscolar = ordenarGradoEscolar($scope.properties.catGradoEscolar);
                } else if ($scope.properties.formInput.informacionEscolar.length == 0){
                    $scope.properties.formInput.informacionEscolar = ordenarGradoEscolar($scope.properties.catGradoEscolar);
                }
            }
        } else {
            if($scope.properties.formInput.informacionEscolar === null || $scope.properties.formInput.informacionEscolar === undefined){
                $scope.properties.formInput.informacionEscolar = ordenarGradoEscolar($scope.properties.catGradoEscolar);
            } else if ($scope.properties.formInput.informacionEscolar.length == 0){
                $scope.properties.formInput.informacionEscolar = ordenarGradoEscolar($scope.properties.catGradoEscolar);
            }
            
            // $scope.properties.formInput.informacionEscolar = ordenarGradoEscolar($scope.properties.catGradoEscolar);
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
                    "estadoString":"",
                    "persistenceId_string": null,
                    "isDefault": true
                }
                
                output.push(angular.copy(infoEscolar));
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
                    "estadoString":"",
                    "persistenceId_string": null,
                    "isDefault": true
                }

                output.push(angular.copy(infoEscolar));
            }   
        }

        for(let i = 0; i < _gradosEscolares.length; i++){
            if(_gradosEscolares[i].descripcion === "Preparatoria/Bachillerato"){
                let infoEscolar = {
                    "grado": _gradosEscolares[i],
                    "tipo": null,
                    "escuela": $scope.properties.bdmSolicitud.catBachilleratos,
                    "escuelaString": $scope.properties.bdmSolicitud.catBachilleratos.descripcion !== "Otro" ? "" : $scope.properties.bdmSolicitud.bachillerato,
                    // "pais": null,
                    "pais": $scope.properties.bdmSolicitud.catBachilleratos.descripcion !== "Otro" ? getPais($scope.properties.bdmSolicitud.catBachilleratos.pais) : getPais($scope.properties.bdmSolicitud.paisBachillerato) ,
                    "estado": $scope.properties.bdmSolicitud.catBachilleratos.pais === "México" ? getEstado($scope.properties.bdmSolicitud.catBachilleratos.estado) : null, 
                    "ciudad": $scope.properties.bdmSolicitud.catBachilleratos.descripcion !== "Otro" ? $scope.properties.bdmSolicitud.catBachilleratos.ciudad : $scope.properties.bdmSolicitud.ciudadBachillerato,
                    "anoInicio": "",
                    "anoFin": "",
                    "promedio": $scope.properties.bdmSolicitud.promedioGeneral,
                    "catBachillerato": null,
                    "otraEscuela": "",
                    "estadoString": $scope.properties.bdmSolicitud.estadoBachillerato,
                    "persistenceId_string": null,
                    "isDefault": true
                }
                
                output.push(angular.copy(infoEscolar));
            }   
        }

        return output;
    }

    function getPais(_descripcion){
        let output = null;
        for(let i = 0; i < $scope.properties.catPais.length; i++){
            if($scope.properties.catPais[i].descripcion === _descripcion){
                output = $scope.properties.catPais[i];
                break;
            }
        }

        return output;
    }

    function getEstado(_descripcion){
        let output = null;
        for(let i = 0; i < $scope.properties.catEstado.length; i++){
            if($scope.properties.catEstado[i].descripcion === _descripcion){
                output = $scope.properties.catEstado[i];
                break;
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
            llenarPaso2();
        });
    }
}