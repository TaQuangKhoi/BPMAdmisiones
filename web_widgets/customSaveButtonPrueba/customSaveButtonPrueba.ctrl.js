function PbButtonCtrl($scope, $http,  modalService) {
  
    $scope.intento = "";

    $scope.Objeto = {
    "primerNombre" : datosUsuario.primerNombre,
    "segundoNombre" : objSolicitudDeAdmision.segundoNombre,
    "apellidoPaterno" :objSolicitudDeAdmision.apellidoPaterno,
    "apellidoMaterno" : objSolicitudDeAdmision.apellidoMaterno,
    "correoElectronico": objSolicitudDeAdmision.correoElectronico,
    "fechaNacimiento" : objSolicitudDeAdmision.fechaNacimiento,
    "sexo_pid" : objSolicitudDeAdmision.catSexo.descripcion,
    "nacionalidad_pid" : objSolicitudDeAdmision.catNacionalidad.descripcion,
    "religion_pid" : objSolicitudDeAdmision.catReligion.descripcion,
    "curp" : objSolicitudDeAdmision.curp,
    "estadoCivil_pid" : objSolicitudDeAdmision.catEstadoCivil.descripcion,
    "telefonoCelular" : objSolicitudDeAdmision.telefonoCelular,
    "caseid" : $scope.caseId,
    }


    var GET_parameters = {};
    debugger
    if (location.search) {
        var splitts = location.search.substring(1).split('&');
    for (var i = 0; i < splitts.length; i++) {
        var key_value_pair = splitts[i].split('=');
    if (!key_value_pair[0]) continue;
        GET_parameters[key_value_pair[0]] = key_value_pair[1] || true;
        }
    }

    $scope.intento = GET_parameters.intento;


    $scope.postEditar = function() {
            
        debugger
          
          var req = {
              method: "POST",
               url: "/bonita/API/extension/AnahuacRest?url=updateViewDownloadSolicitud&p=0&c=100&intento=null&key='IP'&resultado=true",
              data: $scope.Objeto,
          };
            
            //url = url.replace("[INTENTO]", $scope.intento);

  
          return $http(req)
              .success(function (data, status) {
                  actualizacion_de_datos();
              })
              .error(function (data, status) {
              })
              .finally(function () {
                  blockUI.stop();
              });
      }
}
