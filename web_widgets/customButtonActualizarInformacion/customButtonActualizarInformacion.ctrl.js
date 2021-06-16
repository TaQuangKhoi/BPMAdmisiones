function PbButtonActualizarInformacionCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    $scope.action = function() {
        var isValid = true;
        console.log("PbButtonActualizarInformacionCtrl");
        if($scope.properties.updateInformacion.objSolicitudDeAdmisionPrimerNombre === undefined || $scope.properties.updateInformacion.objSolicitudDeAdmisionPrimerNombre === "" || $scope.properties.updateInformacion.objSolicitudDeAdmisionPrimerNombre === null){
            isValid = false;
            Swal.fire('¡Aviso!','Debe agregar el primer nombre','warning');
        }
        else if($scope.properties.updateInformacion.objSolicitudDeAdmisionApellidoPaterno === undefined || $scope.properties.updateInformacion.objSolicitudDeAdmisionApellidoPaterno === "" || $scope.properties.updateInformacion.objSolicitudDeAdmisionApellidoPaterno === null){
            isValid = false;
            Swal.fire('¡Aviso!','Debe agregar el apellido paterno','warning');
        }
        else if($scope.properties.updateInformacion.objSolicitudDeAdmisionCurp === undefined || $scope.properties.updateInformacion.objSolicitudDeAdmisionCurp === "" || $scope.properties.updateInformacion.objSolicitudDeAdmisionCurp === null){
            isValid = false;
            Swal.fire('¡Aviso!','Debe agregar la curp','warning');
        }
        else if($scope.properties.updateInformacion.objSolicitudDeAdmisionPromedioGeneral === undefined || $scope.properties.updateInformacion.objSolicitudDeAdmisionPromedioGeneral === "" || $scope.properties.updateInformacion.objSolicitudDeAdmisionPromedioGeneral === null){
            isValid = false;
            Swal.fire('¡Aviso!','Debe agregar el promedio','warning');
        }
        else if($scope.properties.updateInformacion.objSolicitudDeAdmisionFechaNacimiento === undefined || $scope.properties.updateInformacion.objSolicitudDeAdmisionFechaNacimiento === "" || $scope.properties.updateInformacion.objSolicitudDeAdmisionFechaNacimiento === null){
            isValid = false;
            Swal.fire('¡Aviso!','Debe agregar la fecha de nacimiento','warning');
        }
        else if($scope.properties.updateInformacion.objSolicitudDeAdmisionCatSexoPersistenceId === undefined || $scope.properties.updateInformacion.objSolicitudDeAdmisionCatSexoPersistenceId === "" || $scope.properties.updateInformacion.objSolicitudDeAdmisionCatSexoPersistenceId === null){
            isValid = false;
            Swal.fire('¡Aviso!','Debe selecionar el sexo','warning');
        }
        else if($scope.properties.updateInformacion.objSolicitudDeAdmisionCatNacionalidadPersistenceId === undefined || $scope.properties.updateInformacion.objSolicitudDeAdmisionCatNacionalidadPersistenceId === "" || $scope.properties.updateInformacion.objSolicitudDeAdmisionCatNacionalidadPersistenceId === null){
            isValid = false;
            Swal.fire('¡Aviso!','Debe selecionar la nacionalidad','warning');
        }
        else if($scope.properties.updateInformacion.objTutorNombre === undefined || $scope.properties.updateInformacion.objTutorNombre === "" || $scope.properties.updateInformacion.objTutorNombre === null){
            isValid = false;
            Swal.fire('¡Aviso!','Debe agregar el nombre del tutor','warning');
        }
        else if($scope.properties.updateInformacion.objTutorApellidos === undefined || $scope.properties.updateInformacion.objTutorApellidos === "" || $scope.properties.updateInformacion.objTutorApellidos === null){
            isValid = false;
            Swal.fire('¡Aviso!','Debe agregar los apellidos del tutor','warning');
        }
        if(isValid){
            doRequest("POST", "../API/extension/AnahuacRest?url=updateInformacionAspirante&p=0&c=100", $scope.properties.updateInformacion,
            function(data, status){//SUCCESS
                $scope.properties.objSolicitudDeAdmisionRespaldo = {};
                $scope.properties.objTutorRespaldo = {};
                $scope.properties.isActualizarInformacion = false;
                $scope.loadUsuariosDuplicados();
            },
            function(data, status){//ERROR
                Swal.fire('¡Aviso!','Se Produjo Un Error Inesperado al intentar actualizar la información','error');
            })
        }
    }
    
    $scope.loadUsuariosDuplicados = function() {
        var url = "../API/extension/AnahuacRestGet?url=getDuplicado&correoElectronico=[CORREOELECTRONICO]&fechaNacimiento=[FECHANACIMIENTO]&curp=[CURP]&nombre=[NOMBRE]&caseid=[CASEID]&primerNombre=[PRIMERNOMBRE]&segundoNombre=[SEGUNDONOMBRE]&apellidoPaterno=[APELLIDOPATERNO]&apellidoMaterno=[APELLIDOMATERNO]&p=0&c=9999";
        url = url.replace("[CORREOELECTRONICO]", $scope.properties.parametros["correoElectronico"]);
        url = url.replace("[FECHANACIMIENTO]", $scope.properties.parametros["fechaNacimiento"]);
        url = url.replace("[CURP]", $scope.properties.parametros["curp"]);
        url = url.replace("[NOMBRE]", $scope.properties.parametros["nombre"]);
        url = url.replace("[CASEID]", $scope.properties.parametros["caseid"]);
        url = url.replace("[PRIMERNOMBRE]", $scope.properties.parametros["primerNombre"]);
        url = url.replace("[SEGUNDONOMBRE]", $scope.properties.parametros["segundoNombre"]);
        url = url.replace("[APELLIDOPATERNO]", $scope.properties.parametros["apellidoPaterno"]);
        url = url.replace("[APELLIDOMATERNO]", $scope.properties.parametros["apellidoMaterno"]);
        console.log(url);
        doRequest("GET", url, {},
        function(data, status){//SUCCESS
            $scope.properties.getDuplicado = data;
            Swal.fire('¡Aviso!','Guardado correctamente','success');
        },
        function(data, status){//ERROR

        })
    }
    
    function doRequest(method, url, dataToSend, callback, errorCallback) {
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend)
        };
    
        return $http(req)
            .success(callback)
            .error(errorCallback);
    }
}
