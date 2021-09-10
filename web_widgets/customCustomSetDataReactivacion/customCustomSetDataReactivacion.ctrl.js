function customSetDataTransferencia($scope, $http) {

    $scope.$watch("properties.valoresSolicitante", function() {

        $scope.properties.estadoSeleccionado = "EJEMPLO";
        if ($scope.properties.valoresSolicitante !== undefined) {
            for (var x = 0; x < $scope.properties.catCampus.length; x++) {
                if ($scope.properties.valoresSolicitante.campussede === $scope.properties.catCampus[x].descripcion) {
                    $scope.properties.valoresSolicitante.catCampusEstudio = $scope.properties.catCampus[x];
                }
            }

            for (var l = 0; l < $scope.properties.radioOptionsTestPlace.length; l++) {
                if ($scope.properties.valoresSolicitante.lugarexamen.toUpperCase() === $scope.properties.radioOptionsTestPlace[l][0].displayName.toUpperCase()) {
                    $scope.properties.testOptionsRadio = $scope.properties.radioOptionsTestPlace[l][0].value;
                    if ($scope.properties.valoresSolicitante.lugarexamen.toUpperCase() === "EN UN ESTADO") {
                        for (var e = 0; e < $scope.properties.estadosRUA.length; e++) {
                            if ($scope.properties.valoresSolicitante.estadoexamen === $scope.properties.estadosRUA[e]) {
                                $scope.properties.estadoSeleccionado = $scope.properties.estadosRUA[e];
                                $scope.properties.estadoPeticion = $scope.properties.estadosRUA[e];
                                $scope.properties.valoresSolicitante.catPaisExamen = null;
                                $scope.properties.valoresSolicitante.ciudadExamenPais = null;
                                for (var edo = 0; edo < $scope.properties.catEstado.length; edo++) {
                                    if ($scope.properties.valoresSolicitante.estadoexamen === $scope.properties.catEstado[edo].descripcion) {
                                        $scope.properties.valoresSolicitante.catEstadoExamen = $scope.properties.catEstado[edo];
                                    }
                                }
                            }
                        }
                    } else if ($scope.properties.valoresSolicitante.lugarexamen.toUpperCase() === "EN EL EXTRANJERO (SOLO SI VIVES FUERA DE MÉXICO)") {
                        for (var ex = 0; ex < $scope.properties.paisesRUA.length; ex++) {
                            if ($scope.properties.valoresSolicitante.paisexamen === $scope.properties.paisesRUA[ex]) {
                                $scope.properties.paisSeleccionado = $scope.properties.paisesRUA[ex];
                                $scope.properties.valoresSolicitante.catEstadoExamen = null;
                                $scope.properties.valoresSolicitante.ciudadExamen = null;
                                for (var pais = 0; pais < $scope.properties.catPais.length; pais++) {
                                    if ($scope.properties.valoresSolicitante.paisexamen === $scope.properties.catPais[pais].descripcion) {
                                        $scope.properties.valoresSolicitante.catPaisExamen = $scope.properties.catPais[pais];
                                    }
                                }
                            }
                        }
                    } else {
                        $scope.properties.valoresSolicitante.catEstadoExamen = null;
                        $scope.properties.valoresSolicitante.catPaisExamen = null;
                        $scope.properties.valoresSolicitante.ciudadExamen = null;
                        $scope.properties.valoresSolicitante.ciudadExamenPais = null;
                    }
                }
                for (var le = 0; le < $scope.properties.CatLugarExamen.length; le++) {
                    if ($scope.properties.valoresSolicitante.lugarexamen.toUpperCase() === $scope.properties.CatLugarExamen[le].descripcion.toUpperCase()) {
                        $scope.properties.valoresSolicitante.catLugarExamen = $scope.properties.CatLugarExamen[le];
                        for (var ce = 0; ce < $scope.properties.catCampus.length; ce++) {
                            if ($scope.properties.valoresSolicitante.campus === $scope.properties.catCampus[ce].descripcion) {
                                $scope.properties.valoresSolicitante.catCampus = $scope.properties.catCampus[ce];
                            }
                        }
                    }
                }
            }
        }
    });

    /* $scope.$watch("properties.catLicenciatura", function(){
         
         var findLic = false;
         var findperiodo = false;
         var findprope = false;
         if($scope.properties.catLicenciatura !== undefined){
             for(var x=0;x<$scope.properties.catLicenciatura.length;x++){
                 if($scope.properties.valoresSolicitante.licenciatura === $scope.properties.catLicenciatura[x].nombre){
                     $scope.properties.entro = true;
                     $scope.properties.valoresSolicitante.catLicenciatura = $scope.properties.catLicenciatura[x];
                     findLic = true;
                     for(var y=0;y<$scope.properties.valoresSolicitante.catLicenciatura.periodoDisponible.length;y++){
                         if($scope.properties.valoresSolicitante.ingreso === $scope.properties.valoresSolicitante.catLicenciatura.periodoDisponible[y].descripcion){
                             $scope.properties.valoresSolicitante.periodo = $scope.properties.valoresSolicitante.catLicenciatura.periodoDisponible[y];
                             findperiodo = true;
                             break;
                         }
                         
                     }
                     if($scope.properties.valoresSolicitante.catLicenciatura.propedeutico){
                         for(var z=0;z<$scope.properties.valoresSolicitante.catLicenciatura.propedeuticos.length;z++){
                             if($scope.properties.valoresSolicitante.propedeutico === $scope.properties.valoresSolicitante.catLicenciatura.propedeuticos[z].descripcion){
                                 $scope.properties.valoresSolicitante.catPropedeutico = $scope.properties.valoresSolicitante.catLicenciatura.propedeuticos[z];
                                 findprope = true;
                                 break;
                             }
                         }
                     }else{
                         $scope.properties.valoresSolicitante.catPropedeutico = null;
                         findprope = true;
                     }
                     break;
                 }
             }
             if($scope.properties.catLicenciatura.length>0){
                 if(!findLic){
                     swal("¡Aviso!", "La licenciatura seleccionada anteriormente no se encuentra en este campus.", "warning");
                 }else if(!findperiodo){
                     swal("¡Aviso!", "El periodo actual no se encuentra establecido para la licenciatura seleccionada.", "warning");
                 }else if(!findprope){
                     swal("¡Aviso!", "No existe el propedéutico seleccionado anteriormente.", "warning");
                 }
             }
             
             
             
             
         }
     });*/
    $scope.$watch("properties.catLicenciatura", function() {

        var findLic = false;
        var findperiodo = false;
        var findprope = false;
        if ($scope.properties.catLicenciatura !== undefined) {
            for (var x = 0; x < $scope.properties.catLicenciatura.length; x++) {
                if ($scope.properties.valoresSolicitante.licenciatura === $scope.properties.catLicenciatura[x].nombre) {
                    $scope.properties.entro = true;
                    $scope.properties.valoresSolicitante.catLicenciatura = $scope.properties.catLicenciatura[x];
                    findLic = true;

                    if (!$scope.properties.valoresSolicitante.catLicenciatura.propedeutico) {
                        $scope.properties.valoresSolicitante.catPropedeutico = null;
                    }
                    break;
                }
            }
            if ($scope.properties.catLicenciatura.length > 0) {
                if (!findLic) {
                    swal("¡Aviso!", "La licenciatura seleccionada anteriormente no se encuentra en este campus.", "warning");
                }
            }
        }
    });

    $scope.$watch("properties.catCiudadExamen", function() {
        if ($scope.properties.catCiudadExamen !== undefined) {
            for (var x = 0; x < $scope.properties.catCiudadExamen.length; x++) {
                if ($scope.properties.valoresSolicitante.ciudadestado === $scope.properties.catCiudadExamen[x].descripcion) {
                    $scope.properties.valoresSolicitante.ciudadExamen = $scope.properties.catCiudadExamen[x];
                    $scope.properties.estadoPeticion = "VACIO";
                    break;
                }
            }
        }
    });

    $scope.$watch("properties.catCiudadPais", function() {
        if ($scope.properties.catCiudadPais !== undefined) {
            for (var x = 0; x < $scope.properties.catCiudadPais.length; x++) {
                if ($scope.properties.valoresSolicitante.ciudadpais === $scope.properties.catCiudadPais[x].descripcion) {
                    $scope.properties.valoresSolicitante.ciudadExamenPais = $scope.properties.catCiudadPais[x];
                    break;
                }
            }
        }
    });

    $scope.$watch("properties.testOptionsRadio", function() {
        var letext = "";
        if ($scope.properties.testOptionsRadio === 1) {
            letext = "En el mismo campus en donde realizaré mi licenciatura";
        } else if ($scope.properties.testOptionsRadio === 2) {
            letext = "En un estado";
        } else if ($scope.properties.testOptionsRadio === 3) {
            letext = "En el extranjero (solo si vives fuera de México)";
        }

        for (var le = 0; le < $scope.properties.CatLugarExamen.length; le++) {
            if (letext.toUpperCase() === $scope.properties.CatLugarExamen[le].descripcion.toUpperCase()) {
                $scope.properties.valoresSolicitante.catLugarExamen = $scope.properties.CatLugarExamen[le];
                for (var ce = 0; ce < $scope.properties.catCampus.length; ce++) {
                    if ($scope.properties.valoresSolicitante.campus === $scope.properties.catCampus[ce].descripcion) {
                        $scope.properties.valoresSolicitante.catCampus = $scope.properties.catCampus[ce];
                    }
                }
            }
        }
    });

    /*   $scope.$watch("properties.catPeriodo", function(){
        
        if($scope.properties.catPeriodo !== undefined){
            for(var x=0;x<$scope.properties.catPeriodo.length;x++){
                if($scope.properties.valoresSolicitante.ingreso === $scope.properties.catPeriodo[x].descripcion){
                    $scope.properties.valoresSolicitante.periodo = $scope.properties.catPeriodo[x];
                    $scope.properties.catGestionEscolarTipoLicenciatura = "1";
                    break;
                }   
            }
        }
    });
    
    $scope.$watch("properties.catPropedeutico", function(){
        
        if($scope.properties.catPropedeutico !== undefined){
            for(var x=0;x<$scope.properties.catPropedeutico.length;x++){
                if($scope.properties.valoresSolicitante.propedeutico === $scope.properties.catPropedeutico[x].descripcion){
                    $scope.properties.valoresSolicitante.catPropedeutico = $scope.properties.catPropedeutico[x];
                    break;
                }   
            }
        }
    });*/

    /*$scope.$watch("properties.paisSeleccionado", function(){
        if($scope.properties.paisSeleccionado !== undefined){
            for(var x=0;x<$scope.properties.catPais.length;x++){
                if($scope.properties.paisSeleccionado === $scope.properties.catPais[x].descripcion){
                    $scope.properties.valoresSolicitante.catPaisExamen = $scope.properties.catPais[x];
                }   
            }
        }
    });*/

    /*$scope.$watch("properties.estadoSeleccionado", function(){
        if($scope.properties.estadoSeleccionado !== undefined){
            for(var x=0;x<$scope.properties.catEstado.length;x++){
                if($scope.properties.estadoSeleccionado === $scope.properties.catEstado[x].descripcion){
                    $scope.properties.valoresSolicitante.catEstadoExamen = $scope.properties.catEstado[x];
                }   
            }
        }
    });*/
}