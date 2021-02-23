function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService,blockUI) {

    'use strict';

    var vm = this;
    $scope.jsonCsv = null;
    $scope.jsonCsvCaso = null;
    $scope.jsonCsvSolicitud = null;
    $scope.jsonCsvMadre = null;
    $scope.jsonCsvPadre = null;
    $scope.jsonCsvTutor = null;
    $scope.jsonCsvPago=null;
    $scope.objDataToSend={};
    $scope.lstDatos = [];
    $scope.persistenceIdCampus = null;
    $scope.persistenceIdGestionEscolar = null;
    $scope.persistenceIdLugarExamen = null;
    $scope.persistenceIdPeriodo = null;
    $scope.persistenceIdSexo = null;
    $scope.persistenceIdEstadoCivil = null;
    $scope.persistenceIdNacionalidad = null;
    $scope.persistenceIdPresentarseOtroCampus = null;
    $scope.persistenceIdReligion = null;
    $scope.persistenceIdPaisExamen = null;
    $scope.persistenceIdEstadoExamen = null;
    $scope.persistenceIdBachillerato = null;
    $scope.persistenceIdPropedeutico = null;
    $scope.persistenceIdTitulo = null;
    $scope.persistenceIdEscolaridad = null;
    $scope.persistenceIdEgresadoAnahuac = null;
    $scope.persistenceIdTrabaja = null;
    $scope.persistenceIdVive = null;
    $scope.persistenceIdPaisMadre = null;
    $scope.persistenceIdEstadoMadre = null;
    $scope.persistenceIdCasoEmergencia = null;
    $scope.persistenceIdParentesco = null;
    $scope.persistenceIdCiudadExamen = null;
    $scope.persistenceIdPagoExamenAdmision = null;
    $scope.persistenceIdTipoAlumno = null;
    $scope.persistenceIdResidencia = null;
    $scope.persistenceIdTipoAdmision = null;


    /*================LECTURA DE CSV============================*/
    var fileInput = document.getElementById("csv"),
        readFile = function() {
            debugger
            var reader = new FileReader();
            reader.onload = function() {
                debugger;
                document.getElementById('out').innerHTML = reader.result;
                $scope.jsonCsv = reader.result;
                //var jsonDecode = decodeURIComponent(escape($scope.jsonCsv));

                var allTextLines = $scope.jsonCsv.split(/\r\n|\n/);
                var headers = allTextLines[0].split(',');
                var lines = [];

                for (var i = 1; i < allTextLines.length; i++) {
                    var data = allTextLines[i].split(',');
                    if (data.length == headers.length) {
                        $scope.persistenceIdCampus = null;
                        $scope.persistenceIdGestionEscolar = null;
                        /*$scope.persistenceIdLugarExamen = null;
                        $scope.persistenceIdPeriodo = null;
                        $scope.persistenceIdSexo = null;
                        $scope.persistenceIdEstadoCivil = null;
                        $scope.persistenceIdNacionalidad = null;
                        $scope.persistenceIdPresentarseOtroCampus = null;
                        $scope.persistenceIdReligion = null;
                        $scope.persistenceIdPaisExamen = null;
                        $scope.persistenceIdEstadoExamen = null;
                        $scope.persistenceIdBachillerato = null;
                        $scope.persistenceIdPropedeutico = null;
                        $scope.persistenceIdTitulo = null;
                        $scope.persistenceIdEscolaridad = null;
                        $scope.persistenceIdEgresadoAnahuac = null;
                        $scope.persistenceIdTrabaja = null;
                        $scope.persistenceIdVive = null;
                        $scope.persistenceIdPaisMadre = null;
                        $scope.persistenceIdEstadoMadre = null;
                        $scope.persistenceIdCasoEmergencia = null;
                        $scope.persistenceIdParentesco = null;
                        $scope.persistenceIdCiudadExamen = null;
                        $scope.persistenceIdPagoExamenAdmision = null;
                        $scope.persistenceIdTipoAlumno = null;
                        $scope.persistenceIdResidencia = null;
                        $scope.persistenceIdTipoAdmision = null;*/

                        //For Campus
                        for (var k = 0; k < $scope.properties.catCampus.length; k++) {
                            if ($scope.properties.catCampus[k].descripcion == data[10]) {
                                $scope.persistenceIdCampus = $scope.properties.catCampus[k].persistenceId_string;
                            }
                        }
                        //For GestionEscolar
                        for (var k = 0; k < $scope.properties.catGestionEscolar.length; k++) {
                            if ($scope.properties.catGestionEscolar[k].descripcion == data[11]) {
                                $scope.persistenceIdGestionEscolar = $scope.properties.catGestionEscolar[k].persistenceId_string;
                            }
                        }

                        //Setea datos del csv a la lista $scope.properties.formOutput.catRegistroInput
                        $scope.properties.formOutput.catRegistroInput.push({
                            "caseId": data[0],
                            "nombreusuario": data[1],
                            "primernombre": data[2],
                            "segundonombre": data[3],
                            "apellidopaterno": data[4],
                            "apellidomaterno": data[5],
                            "correoelectronico": data[6],
                            "password": data[7],
                            "isEliminado": data[8]=='true'?true:false,
                            "ayuda": data[9]=='true'?true:false,
                            "catCampus": $scope.persistenceIdCampus == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "catGestionEscolar": $scope.persistenceIdGestionEscolar == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdGestionEscolar
                            }
                        });
                    }
                }
                $scope.startProcess();

                $scope.properties.selectedIndex=$scope.properties.selectedIndex+1;

                $scope.$apply();
                

            };
            // start reading the file. When it is done, calls the onload event defined above.
            reader.readAsBinaryString(fileInput.files[0]);
        };

    fileInput.addEventListener('change', readFile);

    //====================LECTURA SOLICITUD DE ADMISION====================//

    var inputSolicitud = document.getElementById("csvSolicitud"),
        readFile = function() {
            debugger
            var readerSolicitud = new FileReader();
            readerSolicitud.onload = function() {
                document.getElementById('outSolicitud').innerHTML = readerSolicitud.result;
                debugger
                $scope.jsonCsvSolicitud = readerSolicitud.result;
                //var jsonDecode = decodeURIComponent(escape($scope.jsonCsvSolicitud));

                var allTextLines = $scope.jsonCsvSolicitud.split(/\r\n|\n/);
                var headers = allTextLines[0].split(',');
                var lines = [];

                for (var i = 1; i < allTextLines.length; i++) {
                    var data = allTextLines[i].split(',');
                    if (data.length == headers.length) {

                        //For LugarExamen
                        for (var k = 0; k < $scope.properties.catLugarExamen.length; k++) {
                            if ($scope.properties.catLugarExamen[k].descripcion == data[1]) {
                                $scope.persistenceIdLugarExamen = $scope.properties.catLugarExamen[k].persistenceId_string;
                            }
                        }

                        //For Periodo
                        for (var k = 0; k < $scope.properties.catPeriodo.length; k++) {
                            if ($scope.properties.catPeriodo[k].descripcion == data[2]) {
                                $scope.persistenceIdPeriodo = $scope.properties.catPeriodo[k].persistenceId_string;
                            }
                        }

                        //For Sexo
                        for (var k = 0; k < $scope.properties.catSexo.length; k++) {
                            if ($scope.properties.catSexo[k].descripcion == data[3]) {
                                $scope.persistenceIdSexo = $scope.properties.catSexo[k].persistenceId_string;
                            }
                        }

                        //For EstadoCivil
                        for (var k = 0; k < $scope.properties.catEstadoCivil.length; k++) {
                            if ($scope.properties.catEstadoCivil[k].descripcion == data[5]) {
                                $scope.persistenceIdEstadoCivil = $scope.properties.catEstadoCivil[k].persistenceId_string;
                            }
                        }

                        //For Nacionalidad
                        for (var k = 0; k < $scope.properties.catNacionalidad.length; k++) {
                            if ($scope.properties.catNacionalidad[k].descripcion == data[6]) {
                                $scope.persistenceIdNacionalidad = $scope.properties.catNacionalidad[k].persistenceId_string;
                            }
                        }
                        
                        //For PresentarseOtroCampus
                        for (var k = 0; k < $scope.properties.catPresentasteEnOtroCampus.length; k++) {
                            if ($scope.properties.catPresentasteEnOtroCampus[k].descripcion == data[7]) {
                                $scope.persistenceIdPresentarseOtroCampus = $scope.properties.catPresentasteEnOtroCampus[k].persistenceId_string;
                            }
                        }
                        
                        //For Religion
                        for (var k = 0; k < $scope.properties.catReligion.length; k++) {
                            if ($scope.properties.catReligion[k].descripcion == data[8]) {
                                $scope.persistenceIdReligion = $scope.properties.catReligion[k].persistenceId_string;
                            }
                        }

                        //For PaisExamen
                        for (var h = 0; h < $scope.properties.catPaisExamen.length; h++) {
                            if ($scope.properties.catPaisExamen[h].descripcion == data[9]) {
                                $scope.persistenceIdPaisExamen = $scope.properties.catPaisExamen[h].persistenceId_string;
                            }
                        }
                        
                        //For EstadoExamen
                        for (var l = 0; l < $scope.properties.catEstadoExamen.length; l++) {
                            if ($scope.properties.catEstadoExamen[l].descripcion == data[10]) {
                                $scope.persistenceIdEstadoExamen = $scope.properties.catEstadoExamen[l].persistenceId_string;
                            }
                        }
                        
                        //For Bachillerato
                        for (var m = 0; m < $scope.properties.catBachilleratos.length; m++) {
                            if ($scope.properties.catBachilleratos[m].descripcion == data[11]) {
                                $scope.persistenceIdBachillerato = $scope.properties.catBachilleratos[m].persistenceId_string;
                            }
                        }
                        
                        //For Propedeutico
                        for (var n = 0; n < $scope.properties.catPropedeutico.length; n++) {
                            if ($scope.properties.catPropedeutico[n].descripcion == data[12]) {
                                $scope.persistenceIdPropedeutico = $scope.properties.catPropedeutico[n].persistenceId_string;
                            }
                        }

                        //For Campus
                        for (var k = 0; k < $scope.properties.catCampus.length; k++) {
                            if ($scope.properties.catCampus[k].descripcion == data[13]) {
                                $scope.persistenceIdCampus = $scope.properties.catCampus[k].persistenceId_string;
                            }
                        }
                        
                        //For Cuidad
                        for (var k = 0; k < $scope.properties.catCiudad.length; k++) {
                            if ($scope.properties.catCiudad[k].descripcion == data[47]) {
                                $scope.persistenceIdCiudadExamen = $scope.properties.catCiudad[k].persistenceId_string;
                            }
                        }

                        //Setea datos del csv a la lista $scope.properties.formOutput.solicitudDeAdmisionInput
                        $scope.properties.formOutput.solicitudDeAdmisionInput.push({
                            "catLugarExamen": $scope.persistenceIdLugarExamen == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdLugarExamen
                            },
                            "catCampus": $scope.persistenceIdCampus == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "catPeriodo": $scope.persistenceIdPeriodo == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdPeriodo
                            },
                            "primerNombre": data[14],
                            "segundoNombre": data[15],
                            "apellidoPaterno": data[16],
                            "apellidoMaterno": data[17],
                            "correoElectronico": data[18],
                            "catSexo": $scope.persistenceIdSexo == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdSexo
                            },
                            "fechaNacimiento":data[4],
                            "catEstadoCivil": $scope.persistenceIdEstadoCivil == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdEstadoCivil
                            },
                            "catNacionalidad": $scope.persistenceIdNacionalidad == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdNacionalidad
                            },
                            "catPresentasteEnOtroCampus": $scope.persistenceIdPresentarseOtroCampus == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdPresentarseOtroCampus
                            },
                            "catCampusPresentadoSolicitud": [],
                            "catReligion": $scope.persistenceIdReligion == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdReligion
                            },
                            "curp": data[19],
                            "usuarioFacebook": data[20],
                            "usiarioTwitter": data[21],
                            "usuarioInstagram": data[22],
                            "telefonoCelular": data[23],
                            "foto": data[24],
                            "actaNacimiento": data[25],
                            "calle": data[26],
                            "codigoPostal": data[27],
                            "catPais": $scope.persistenceIdPaisExamen == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdPaisExamen
                            },
                            "catEstado": $scope.persistenceIdEstadoExamen == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdEstadoExamen
                            },
                            "ciudad": data[28],
                            "calle2": data[29],
                            "numExterior": data[30],
                            "numInterior": data[31],
                            "colonia": data[32],
                            "telefono": data[33],
                            "otroTelefonoContacto": data[34],
                            "promedioGeneral": data[35],
                            "comprobanteCalificaciones": data[36],
                            "catPaisExamen": $scope.persistenceIdPaisExamen == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdPaisExamen
                            },
                            "catEstadoExamen": $scope.persistenceIdEstadoExamen == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdEstadoExamen
                            },
                            "ciudadExamen": $scope.persistenceIdCiudadExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdCiudadExamen
                            },
                            "avisoPrivacidad": data[38]=='true'?true:false,
                            "datosVeridicos": data[39]=='true'?true:false,
                            "aceptoAvisoPrivacidad": data[40]=='true'?true:false,
                            "confirmarAutorDatos": data[41]=='true'?true:false,
                            "caseId": data[0],
                            "catBachilleratos": $scope.persistenceIdBachillerato == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdBachillerato
                            },
                            "paisBachillerato": data[42],
                            "estadoBachillerato": data[43],
                            "ciudadBachillerato": data[44],
                            "bachillerato": data[45],
                            "ciudadExamenPais": $scope.persistenceIdCiudadExamen == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdCiudadExamen
                            },
                            "catGestionEscolar": $scope.persistenceIdGestionEscolar == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdGestionEscolar
                            },
                            "catPropedeutico": $scope.persistenceIdPropedeutico == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdPropedeutico
                            },
                            "estatusSolicitud": data[47],
                            "delegacionMunicipio": data[48],
                            "estadoExtranjero": data[49],
                            "resultadoPAA": data[50],
                            "selectedIndex": 0,
                            "selectedIndexPersonal": 0,
                            "selectedIndexFamiliar": 0,
                            "selectedIndexRevision": 0,
                            "catCampusEstudio": $scope.persistenceIdCampus == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "tienePAA": data[51]=='true'?true:false,
                            "tieneDescuento": data[52]=='true'?true:false,
                            "admisionAnahuac": data[53]=='true'?true:false
                        });

                        //====================LECTURA SOLICITUD DE CAT SOLICITUD DE ADMISION====================//

                        //Setea datos del csv a la lista $scope.properties.formInputSolicitud.catSolicitudDeAdmisionInput
                        /*$scope.properties.formInputSolicitud.catSolicitudDeAdmisionInput.push({
                            "catLugarExamen": $scope.persistenceIdLugarExamen == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdLugarExamen
                            },
                            "catCampus": $scope.persistenceIdCampus == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "catPeriodo": $scope.persistenceIdPeriodo == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdPeriodo
                            },
                            "primerNombre": data[14],
                            "segundoNombre": data[15],
                            "apellidoPaterno": data[16],
                            "apellidoMaterno": data[17],
                            "correoElectronico": data[18],
                            "catSexo": $scope.persistenceIdSexo == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdSexo
                            },
                            "fechaNacimiento":data[4],
                            "catEstadoCivil": $scope.persistenceIdEstadoCivil == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdEstadoCivil
                            },
                            "catNacionalidad": $scope.persistenceIdNacionalidad == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdNacionalidad
                            },
                            "catPresentasteEnOtroCampus": $scope.persistenceIdPresentarseOtroCampus == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdPresentarseOtroCampus
                            },
                            "catCampusPresentadoSolicitud": [],
                            "catReligion": $scope.persistenceIdReligion == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdReligion
                            },
                            "curp": data[19],
                            "usuarioFacebook": data[20],
                            "usiarioTwitter": data[21],
                            "usuarioInstagram": data[22],
                            "telefonoCelular": data[23],
                            "foto": data[24],
                            "actaNacimiento": data[25],
                            "calle": data[26],
                            "codigoPostal": data[27],
                            "catPais": $scope.persistenceIdPaisExamen == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdPaisExamen
                            },
                            "catEstado": $scope.persistenceIdEstadoExamen == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdEstadoExamen
                            },
                            "ciudad": data[28],
                            "calle2": data[29],
                            "numExterior": data[30],
                            "numInterior": data[31],
                            "colonia": data[32],
                            "telefono": data[33],
                            "otroTelefonoContacto": data[34],
                            "promedioGeneral": data[35],
                            "comprobanteCalificaciones": data[36],
                            "catPaisExamen": $scope.persistenceIdPaisExamen == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdPaisExamen
                            },
                            "catEstadoExamen": $scope.persistenceIdEstadoExamen == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdEstadoExamen
                            },
                            "ciudadExamen": $scope.persistenceIdCiudadExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdCiudadExamen
                            },
                            "avisoPrivacidad": data[38]=='true'?true:false,
                            "datosVeridicos": data[39]=='true'?true:false,
                            "aceptoAvisoPrivacidad": data[40]=='true'?true:false,
                            "confirmarAutorDatos": data[41]=='true'?true:false,
                            "caseId": data[0],
                            "catBachilleratos": $scope.persistenceIdBachillerato == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdBachillerato
                            },
                            "paisBachillerato": data[42],
                            "estadoBachillerato": data[43],
                            "ciudadBachillerato": data[44],
                            "bachillerato": data[45],
                            "ciudadExamenPais": $scope.persistenceIdCiudadExamen == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdCiudadExamen
                            },
                            "catGestionEscolar": $scope.persistenceIdGestionEscolar == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdGestionEscolar
                            },
                            "catPropedeutico": $scope.persistenceIdPropedeutico == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdPropedeutico
                            },
                            "estatusSolicitud": data[47],
                            "delegacionMunicipio": data[48],
                            "estadoExtranjero": data[49],
                            "resultadoPAA": data[50],
                            "selectedIndex": 0,
                            "selectedIndexPersonal": 0,
                            "selectedIndexFamiliar": 0,
                            "selectedIndexRevision": 0,
                            "catCampusEstudio": $scope.persistenceIdCampus == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "tienePAA": data[51]=='true'?true:false,
                            "tieneDescuento": data[52]=='true'?true:false,
                            "admisionAnahuac": data[53]=='true'?true:false
                        });*/

                        //====================FIN LECTURA DE CAT SOLICITUD DE ADMISION====================//
                    }
                }
                $scope.startProcess();
                $scope.properties.selectedIndex=$scope.properties.selectedIndex+1;

                $scope.$apply();
            };
            readerSolicitud.readAsBinaryString(inputSolicitud.files[0]);
        };
    inputSolicitud.addEventListener('change', readFile);

    //====================FIN LECTURA SOLICITUD DE ADMISION====================//

    //====================LECTURA MADRE====================//
    var inputMadre = document.getElementById("csvMadre"),
        readFile = function() {
            debugger
            var readerMadre = new FileReader();
            readerMadre.onload = function() {
                document.getElementById('outMadre').innerHTML = readerMadre.result;
                debugger
                $scope.jsonCsvMadre = readerMadre.result;
                //var jsonDecode = decodeURIComponent(escape($scope.jsonCsvMadre));

                var allTextLines = $scope.jsonCsvMadre.split(/\r\n|\n/);
                var headers = allTextLines[0].split(',');
                var lines = [];

                for (var i = 1; i < allTextLines.length; i++) {
                    var data = allTextLines[i].split(',');
                    if (data.length == headers.length) {

                        //For Titulo
                        for (var m = 0; m < $scope.properties.catTitulo.length; m++) {
                            if ($scope.properties.catTitulo[m].descripcion == data[1]) {
                                $scope.persistenceIdTitulo = $scope.properties.catTitulo[m].persistenceId_string;
                            }
                        }

                        //For Parentesco
                        for (var k = 0; k < $scope.properties.catParentesco.length; k++) {
                            if ($scope.properties.catParentesco[k].descripcion == data[2]) {
                                $scope.persistenceIdParentesco = $scope.properties.catParentesco[k].persistenceId_string;
                            }
                        }
                        
                        //For Escolaridad
                        for (var o = 0; o < $scope.properties.catEscolaridad.length; o++) {
                            if ($scope.properties.catEscolaridad[o].descripcion == data[6]) {
                                $scope.persistenceIdEscolaridad = $scope.properties.catEscolaridad[o].persistenceId_string;
                            }
                        }
                        
                        //For Egresado
                        for (var p = 0; p < $scope.properties.catEgresado.length; p++) {
                            if ($scope.properties.catEgresado[p].descripcion == data[7]) {
                                $scope.persistenceIdEgresadoAnahuac = $scope.properties.catEgresado[p].persistenceId_string;
                            }
                        }

                        //For Campus
                        for (var k = 0; k < $scope.properties.catCampus.length; k++) {
                            if ($scope.properties.catCampus[k].descripcion == data[8]) {
                                $scope.persistenceIdCampus = $scope.properties.catCampus[k].persistenceId_string;
                            }
                        }
                        
                        //For Trabaja
                        for (var q = 0; q < $scope.properties.catTrabaja.length; q++) {
                            if ($scope.properties.catTrabaja[q].descripcion == data[9]) {
                                $scope.persistenceIdTrabaja = $scope.properties.catTrabaja[q].persistenceId_string;
                            }
                        }
                        
                        //For Vive
                        for (var r = 0; r < $scope.properties.catVive.length; r++) {
                            if ($scope.properties.catVive[r].descripcion == data[14]) {
                                $scope.persistenceIdVive = $scope.properties.catVive[r].persistenceId_string;
                            }
                        }
                        
                        //For Pais
                        for (var s = 0; s < $scope.properties.catPaisExamen.length; s++) {
                            if ($scope.properties.catPaisExamen[s].descripcion == data[16]) {
                                $scope.persistenceIdPaisMadre = $scope.properties.catPaisExamen[s].persistenceId_string;
                            }
                        }
                        
                        //For Estado
                        for (var t = 0; t < $scope.properties.catEstadoExamen.length; t++) {
                            if ($scope.properties.catEstadoExamen[t].descripcion == data[19]) {
                                $scope.persistenceIdEstadoMadre = $scope.properties.catEstadoExamen[t].persistenceId_string;
                            }
                        }

                         

                        $scope.properties.formInputSolicitud.madreInput.push({
                            "caseId": data[0],
                            "catTitulo": $scope.persistenceIdTitulo == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdTitulo
                            },
                            "catParentezco": $scope.persistenceIdParentesco == null ?null : {
                                "persistenceId_string": $scope.persistenceIdParentesco
                            },
                            "nombre": data[3],
                            "apellidos": data[4],
                            "correoElectronico": data[5],
                            "catEscolaridad": $scope.persistenceIdEscolaridad == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdEscolaridad
                            },
                            "catEgresoAnahuac": $scope.persistenceIdEgresadoAnahuac == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdEgresadoAnahuac
                            },
                            "catCampusEgreso": $scope.persistenceIdCampus == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "catTrabaja": $scope.persistenceIdTrabaja == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdTrabaja
                            },
                            "empresaTrabaja": data[10],
                            "giroEmpresa": data[11],
                            "puesto": data[12],
                            "isTutor": data[13]=='true'?true:false,
                            "vive": $scope.persistenceIdVive == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdVive
                            },
                            "calle": data[15],
                            "catPais": $scope.persistenceIdPaisMadre == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdPaisMadre
                            },
                            "numeroExterior": data[17],
                            "numeroInterior": data[18],
                            "catEstado": $scope.persistenceIdEstadoMadre == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdEstadoMadre
                            },
                            "ciudad": data[20],
                            "colonia": data[21],
                            "telefono": data[22],
                            "codigoPostal": data[23],
                            "viveContigo": data[24]=='true'?true:false,
                            "otroParentesco":data[25],
                            "desconozcoDatosPadres": data[26]=='true'?true:false,
                            "delegacionMunicipio": data[27],
                            "estadoExtranjero": data[28]
                        });

                    }
                }
                $scope.startProcess();
                $scope.properties.selectedIndex=$scope.properties.selectedIndex+1;

                $scope.$apply();
            };
            readerMadre.readAsBinaryString(inputMadre.files[0]);
        };
    inputMadre.addEventListener('change', readFile);

    //====================FIN LECTURA MADRE====================//

    //====================LECTURA PADRE====================//

    var inputPadre = document.getElementById("csvPadre"),
        readFile = function() {
            debugger
            var readerPadre = new FileReader();
            readerPadre.onload = function() {
                document.getElementById('outPadre').innerHTML = readerPadre.result;
                debugger
                $scope.jsonCsvPadre = readerPadre.result;
                //var jsonDecode = decodeURIComponent(escape($scope.jsonCsvPadre));

                var allTextLines = $scope.jsonCsvPadre.split(/\r\n|\n/);
                var headers = allTextLines[0].split(',');
                var lines = [];

                for (var i = 1; i < allTextLines.length; i++) {
                    var data = allTextLines[i].split(',');
                    if (data.length == headers.length) {

                        //For Titulo
                        for (var m = 0; m < $scope.properties.catTitulo.length; m++) {
                            if ($scope.properties.catTitulo[m].descripcion == data[1]) {
                                $scope.persistenceIdTitulo = $scope.properties.catTitulo[m].persistenceId_string;
                            }
                        }

                        //For Parentesco
                        for (var k = 0; k < $scope.properties.catParentesco.length; k++) {
                            if ($scope.properties.catParentesco[k].descripcion == data[2]) {
                                $scope.persistenceIdParentesco = $scope.properties.catParentesco[k].persistenceId_string;
                            }
                        }
                        
                        //For Escolaridad
                        for (var o = 0; o < $scope.properties.catEscolaridad.length; o++) {
                            if ($scope.properties.catEscolaridad[o].descripcion == data[6]) {
                                $scope.persistenceIdEscolaridad = $scope.properties.catEscolaridad[o].persistenceId_string;
                            }
                        }
                        
                        //For Egresado
                        for (var p = 0; p < $scope.properties.catEgresado.length; p++) {
                            if ($scope.properties.catEgresado[p].descripcion == data[7]) {
                                $scope.persistenceIdEgresadoAnahuac = $scope.properties.catEgresado[p].persistenceId_string;
                            }
                        }

                        //For Campus
                        for (var k = 0; k < $scope.properties.catCampus.length; k++) {
                            if ($scope.properties.catCampus[k].descripcion == data[8]) {
                                $scope.persistenceIdCampus = $scope.properties.catCampus[k].persistenceId_string;
                            }
                        }
                        
                        //For Trabaja
                        for (var q = 0; q < $scope.properties.catTrabaja.length; q++) {
                            if ($scope.properties.catTrabaja[q].descripcion == data[9]) {
                                $scope.persistenceIdTrabaja = $scope.properties.catTrabaja[q].persistenceId_string;
                            }
                        }
                        
                        //For Vive
                        for (var r = 0; r < $scope.properties.catVive.length; r++) {
                            if ($scope.properties.catVive[r].descripcion == data[14]) {
                                $scope.persistenceIdVive = $scope.properties.catVive[r].persistenceId_string;
                            }
                        }
                        
                        //For Pais
                        for (var s = 0; s < $scope.properties.catPaisExamen.length; s++) {
                            if ($scope.properties.catPaisExamen[s].descripcion == data[16]) {
                                $scope.persistenceIdPaisMadre = $scope.properties.catPaisExamen[s].persistenceId_string;
                            }
                        }
                        
                        //For Estado
                        for (var t = 0; t < $scope.properties.catEstadoExamen.length; t++) {
                            if ($scope.properties.catEstadoExamen[t].descripcion == data[19]) {
                                $scope.persistenceIdEstadoMadre = $scope.properties.catEstadoExamen[t].persistenceId_string;
                            }
                        }

                        $scope.properties.formInputSolicitud.padreInput.push({
                            "caseId": data[0],
                            "catTitulo": $scope.persistenceIdTitulo == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdTitulo
                            },
                            "catParentezco": $scope.persistenceIdParentesco == null ?null : {
                                "persistenceId_string": $scope.persistenceIdParentesco
                            },
                            "nombre": data[3],
                            "apellidos": data[4],
                            "correoElectronico": data[5],
                            "catEscolaridad": $scope.persistenceIdEscolaridad == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdEscolaridad
                            },
                            "catEgresoAnahuac": $scope.persistenceIdEgresadoAnahuac == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdEgresadoAnahuac
                            },
                            "catCampusEgreso": $scope.persistenceIdCampus == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "catTrabaja": $scope.persistenceIdTrabaja == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdTrabaja
                            },
                            "empresaTrabaja": data[10],
                            "giroEmpresa": data[11],
                            "puesto": data[12],
                            "isTutor": data[13]=='true'?true:false,
                            "vive": $scope.persistenceIdVive == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdVive
                            },
                            "calle": data[15],
                            "catPais": $scope.persistenceIdPaisMadre == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdPaisMadre
                            },
                            "numeroExterior": data[17],
                            "numeroInterior": data[18],
                            "catEstado": $scope.persistenceIdEstadoMadre == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdEstadoMadre
                            },
                            "ciudad": data[20],
                            "colonia": data[21],
                            "telefono": data[22],
                            "codigoPostal": data[23],
                            "viveContigo": data[24]=='true'?true:false,
                            "otroParentesco":data[25],
                            "desconozcoDatosPadres": data[26]=='true'?true:false,
                            "delegacionMunicipio": data[27],
                            "estadoExtranjero": data[28]
                        });

                    }
                }
                $scope.startProcess();
                $scope.properties.selectedIndex=$scope.properties.selectedIndex+1;

                $scope.$apply();
            };
            readerPadre.readAsBinaryString(inputPadre.files[0]);
        };
    inputPadre.addEventListener('change', readFile);

    //====================FIN LECTURA PADRE====================//

    //====================LECTURA TUTOR====================//

    var inputTutor = document.getElementById("csvTutor"),
        readFile = function() {
            debugger
            var readerTutor = new FileReader();
            readerTutor.onload = function() {
                document.getElementById('outTutor').innerHTML = readerTutor.result;
                debugger
                $scope.jsonCsvTutor = readerTutor.result;
                //var jsonDecode = decodeURIComponent(escape($scope.jsonCsvTutor));

                var allTextLines = $scope.jsonCsvTutor.split(/\r\n|\n/);
                var headers = allTextLines[0].split(',');
                var lines = [];

                for (var i = 1; i < allTextLines.length; i++) {
                    var data = allTextLines[i].split(',');
                    if (data.length == headers.length) {

                         //For Titulo
                        for (var m = 0; m < $scope.properties.catTitulo.length; m++) {
                            if ($scope.properties.catTitulo[m].descripcion == data[1]) {
                                $scope.persistenceIdTitulo = $scope.properties.catTitulo[m].persistenceId_string;
                            }
                        }

                        //For Parentesco
                        for (var k = 0; k < $scope.properties.catParentesco.length; k++) {
                            if ($scope.properties.catParentesco[k].descripcion == data[2]) {
                                $scope.persistenceIdParentesco = $scope.properties.catParentesco[k].persistenceId_string;
                            }
                        }
                        
                        //For Escolaridad
                        for (var o = 0; o < $scope.properties.catEscolaridad.length; o++) {
                            if ($scope.properties.catEscolaridad[o].descripcion == data[6]) {
                                $scope.persistenceIdEscolaridad = $scope.properties.catEscolaridad[o].persistenceId_string;
                            }
                        }
                        
                        //For Egresado
                        for (var p = 0; p < $scope.properties.catEgresado.length; p++) {
                            if ($scope.properties.catEgresado[p].descripcion == data[7]) {
                                $scope.persistenceIdEgresadoAnahuac = $scope.properties.catEgresado[p].persistenceId_string;
                            }
                        }

                        //For Campus
                        for (var k = 0; k < $scope.properties.catCampus.length; k++) {
                            if ($scope.properties.catCampus[k].descripcion == data[8]) {
                                $scope.persistenceIdCampus = $scope.properties.catCampus[k].persistenceId_string;
                            }
                        }
                        
                        //For Trabaja
                        for (var q = 0; q < $scope.properties.catTrabaja.length; q++) {
                            if ($scope.properties.catTrabaja[q].descripcion == data[9]) {
                                $scope.persistenceIdTrabaja = $scope.properties.catTrabaja[q].persistenceId_string;
                            }
                        }
                        
                        //For Vive
                        for (var r = 0; r < $scope.properties.catVive.length; r++) {
                            if ($scope.properties.catVive[r].descripcion == data[14]) {
                                $scope.persistenceIdVive = $scope.properties.catVive[r].persistenceId_string;
                            }
                        }
                        
                        //For Pais
                        for (var s = 0; s < $scope.properties.catPaisExamen.length; s++) {
                            if ($scope.properties.catPaisExamen[s].descripcion == data[16]) {
                                $scope.persistenceIdPaisMadre = $scope.properties.catPaisExamen[s].persistenceId_string;
                            }
                        }
                        
                        //For Estado
                        for (var t = 0; t < $scope.properties.catEstadoExamen.length; t++) {
                            if ($scope.properties.catEstadoExamen[t].descripcion == data[19]) {
                                $scope.persistenceIdEstadoMadre = $scope.properties.catEstadoExamen[t].persistenceId_string;
                            }
                        }

                        $scope.properties.formInputSolicitud.tutorInput.push({
                            "caseId": data[0],
                            "catTitulo": $scope.persistenceIdTitulo == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdTitulo
                            },
                            "catParentezco": $scope.persistenceIdParentesco == null ?null : {
                                "persistenceId_string": $scope.persistenceIdParentesco
                            },
                            "nombre": data[3],
                            "apellidos": data[4],
                            "correoElectronico": data[5],
                            "catEscolaridad": $scope.persistenceIdEscolaridad == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdEscolaridad
                            },
                            "catEgresoAnahuac": $scope.persistenceIdEgresadoAnahuac == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdEgresadoAnahuac
                            },
                            "catCampusEgreso": $scope.persistenceIdCampus == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "catTrabaja": $scope.persistenceIdTrabaja == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdTrabaja
                            },
                            "empresaTrabaja": data[10],
                            "giroEmpresa": data[11],
                            "puesto": data[12],
                            "isTutor": data[13]=='true'?true:false,
                            "vive": $scope.persistenceIdVive == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdVive
                            },
                            "calle": data[15],
                            "catPais": $scope.persistenceIdPaisMadre == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdPaisMadre
                            },
                            "numeroExterior": data[17],
                            "numeroInterior": data[18],
                            "catEstado": $scope.persistenceIdEstadoMadre == null ? null  : {
                                "persistenceId_string": $scope.persistenceIdEstadoMadre
                            },
                            "ciudad": data[20],
                            "colonia": data[21],
                            "telefono": data[22],
                            "codigoPostal": data[23],
                            "viveContigo": data[24]=='true'?true:false,
                            "otroParentesco":data[25],
                            "desconozcoDatosPadres": data[26]=='true'?true:false,
                            "delegacionMunicipio": data[27],
                            "estadoExtranjero": data[28]
                        });

                    }
                }
                $scope.startProcess();
                $scope.properties.selectedIndex=$scope.properties.selectedIndex+1;

                $scope.$apply();
            };
            readerTutor.readAsBinaryString(inputTutor.files[0]);
        };
    inputTutor.addEventListener('change', readFile);

    //====================FIN LECTURA TUTOR====================//

    //====================LECTURA CASO EMERGENCIA====================//

    var inputCaso = document.getElementById("csvCaso"),
        readFile = function() {
            debugger
            var readerCaso = new FileReader();
            readerCaso.onload = function() {
                document.getElementById('outCaso').innerHTML = readerCaso.result;
                $scope.jsonCsvCaso = readerCaso.result;
                debugger
                //var jsonDecode = decodeURIComponent(escape($scope.jsonCsvSolicitud));

                var allTextLines = $scope.jsonCsvCaso.split(/\r\n|\n/);
                var headers = allTextLines[0].split(',');
                var lines = [];

                for (var i = 1; i < allTextLines.length; i++) {
                    var data = allTextLines[i].split(',');
                    if (data.length == headers.length) {

                        //For CasoEmergencia
                        for (var s = 0; s < $scope.properties.catCasoEmergencia.length; s++) {
                            if ($scope.properties.catCasoEmergencia[s].descripcion == data[3]) {
                                $scope.persistenceIdCasoEmergencia = $scope.properties.catCasoEmergencia[s].persistenceId_string;
                            }
                        }
                        
                        //For Parentesco
                        for (var t = 0; t < $scope.properties.catParentesco.length; t++) {
                            if ($scope.properties.catParentesco[t].descripcion == data[6]) {
                                $scope.persistenceIdParentesco = $scope.properties.catParentesco[t].persistenceId_string;
                            }
                        }

                        ////Setea datos del csv a la lista $scope.properties.formInputSolicitud.contactoEmergenciaInput
                        $scope.properties.formInputSolicitud.contactoEmergenciaInput.push({
                            "caseId": data[0],
                            "nombre": data[1],
                            "telefono": data[2],
                            "catCasoDeEmergencia": $scope.persistenceIdCasoEmergencia == null ?null : {
                                "persistenceId_string": $scope.persistenceIdCasoEmergencia
                            },
                            "telefonoCelular": data[4],
                            "parentesco": data[5],
                            "catParentesco": $scope.persistenceIdParentesco == null ?null : {
                                "persistenceId_string": $scope.persistenceIdParentesco
                            }
                        });
                    }
                }
                $scope.startProcess();
                $scope.properties.selectedIndex=$scope.properties.selectedIndex+1;

                $scope.$apply();
            };
            readerCaso.readAsBinaryString(inputCaso.files[0]);
        };
    inputCaso.addEventListener('change', readFile);

    //====================FIN LECTURA CASO EMERGENCIA====================//


    //====================LECTURA PAGO====================//

    var inputPago = document.getElementById("csvPago"),
        readFile = function() {
            debugger
            var readerPago = new FileReader();
            readerPago.onload = function() {
                document.getElementById('outPago').innerHTML = readerPago.result;
                $scope.jsonCsvPago = readerPago.result;
                debugger
                //var jsonDecode = decodeURIComponent(escape($scope.jsonCsvSolicitud));

                var allTextLines = $scope.jsonCsvPago.split(/\r\n|\n/);
                var headers = allTextLines[0].split(',');
                var lines = [];

                for (var i = 1; i < allTextLines.length; i++) {
                    var data = allTextLines[i].split(',');
                    if (data.length == headers.length) {

                        //For PagoExamenAdmision
                        for (var j = 0; j < $scope.properties.catPagoDeExamenDeAdmision.length; j++) {
                            if ($scope.properties.catPagoDeExamenDeAdmision[j].descripcion == data[2]) {
                                $scope.persistenceIdPagoExamenAdmision = $scope.properties.catPagoDeExamenDeAdmision[j].persistenceId_string;
                            }
                        }

                        //For TipoAlumno
                        for (var j = 0; j < $scope.properties.catTipoAlumno.length; j++) {
                            if ($scope.properties.catTipoAlumno[j].descripcion == data[15]) {
                                $scope.persistenceIdTipoAlumno = $scope.properties.catTipoAlumno[j].persistenceId_string;
                            }
                        }

                        //For Residencia
                        for (var j = 0; j < $scope.properties.catResidencia.length; j++) {
                            if ($scope.properties.catResidencia[j].descripcion == data[16]) {
                                $scope.persistenceIdResidencia = $scope.properties.catResidencia[j].persistenceId_string;
                            }
                        }

                        //For TipoAdmision
                        for (var j = 0; j < $scope.properties.catTipoAdmision.length; j++) {
                            if ($scope.properties.catTipoAdmision[j].descripcion == data[17]) {
                                $scope.persistenceIdTipoAdmision = $scope.properties.catTipoAdmision[j].persistenceId_string;
                            }
                        }

                        //Setea datos del csv a la lista $scope.properties.formInputValidar.detalleSolicitudInput
                        $scope.properties.formInputValidar.detalleSolicitudInput.push({
                            "catDescuentos":  null,
                            "idBanner": data[1],
                            "catPagoDeExamenDeAdmision": $scope.persistenceIdPagoExamenAdmision == null ?null : {
                                "persistenceId_string": $scope.persistenceIdPagoExamenAdmision
                            },
                            "isCurpValidado":data[3]=='true'?true:false,
                            "promedioCoincide":data[4]=='true'?true:false,
                            "revisado":data[5]=='true'?true:false,
                            "tipoAlumno":data[6],
                            "descuento":data[7],
                            "observacionesDescuento":data[8],
                            "observacionesCambio":data[9],
                            "observacionesRechazo":data[10],
                            "observacionesListaRoja":data[11],
                            "ordenPago":data[12],
                            "caseId":data[0],
                            "cbCoincide":data[13]=='true'?true:false,
                            "admisionAnahuac":data[14]=='true'?true:false,
                            "catTipoAlumno":$scope.persistenceIdTipoAlumno == null ?null : {
                                "persistenceId_string": $scope.persistenceIdTipoAlumno
                            },
                            "catResidencia":$scope.persistenceIdResidencia == null ?null : {
                                "persistenceId_string": $scope.persistenceIdResidencia
                            },
                            "catTipoAdmision":$scope.persistenceIdTipoAdmision == null ?null : {
                                "persistenceId_string": $scope.persistenceIdTipoAdmision
                            }

                        });
                    }
                }
               
                $scope.startProcess();
                $scope.$apply();
            };
            readerPago.readAsBinaryString(inputPago.files[0]);
        };
    inputPago.addEventListener('change', readFile);

    //====================FIN LECTURA PAGO====================//

    

      //INICIO ----------Instanciacion de proceso
      $scope.startProcess =function() {
        var id = 0;
        debugger
        //Modulo Registro
        if($scope.properties.selectedIndex==0){
            id=$scope.properties.procesoRegistroId[0].id;
            $scope.objDataToSend={
                "catRegistroInput" : $scope.properties.formOutput.catRegistroInput
            }
        }
        //Modulo Solicitud de Admision
        else if($scope.properties.selectedIndex==1){
            id=$scope.properties.procesoSolicitudAdmisionId[0].id;
            $scope.objDataToSend={
                "solicitudDeAdmisionInput" : $scope.properties.formOutput.solicitudDeAdmisionInput
            }

        }
        //Modulo Madre
        else if($scope.properties.selectedIndex==2){
            id=$scope.properties.procesoMadreId[0].id;
            $scope.objDataToSend={
                "madreInput" : $scope.properties.formInputSolicitud.madreInput
            }

        }
        //Modulo Padre
        else if($scope.properties.selectedIndex==3){
            id=$scope.properties.procesoPadreId[0].id;
            $scope.objDataToSend={
                "padreInput" :  $scope.properties.formInputSolicitud.padreInput
            }

        }
        //Modulo Tutor
        else if($scope.properties.selectedIndex==4){
            id=$scope.properties.procesoTutorId[0].id;
            $scope.objDataToSend={
                "tutorInput" : $scope.properties.formInputSolicitud.tutorInput
            }

        }
        //Modulo Contacto de Emergencia
        else if($scope.properties.selectedIndex==5){
            id=$scope.properties.procesoContactoEmergenciaId[0].id;
            $scope.objDataToSend={
                "contactoEmergenciaInput" : $scope.properties.formInputSolicitud.contactoEmergenciaInput
            }

        }
        //Modulo Pago
        else if($scope.properties.selectedIndex==6){
            id=$scope.properties.procesoPagoId[0].id;
            $scope.objDataToSend={
                "detalleSolicitudInput" : $scope.properties.formInputValidar.detalleSolicitudInput
            }

        }
        if (id) {
          var prom = $scope.doRequest('POST', '../API/bpm/process/' + id + '/instantiation', { 'user': $scope.properties.userData.user_id } );
    
        } else {
          $log.log('Impossible to retrieve the process definition id value from the URL');
        }
      }

      $scope.doRequest=function (method, url, params) {
        debugger
        vm.busy = true;
        var req = {
          method: method,
          url: url,
          data: angular.copy($scope.objDataToSend),
          params: params
        };
    
        return $http(req)
          .success(function(data, status) {
            debugger
            $scope.objDataToSend={};
            Swal.fire(
                  'Operación Exitosa!',
                  'Los aspirantes han sido agregados correctamente con el caseId:!'+data.caseId,
                  'success'
                )
          })
          .error(function(data, status) {
            debugger
            $scope.objDataToSend={};
            Swal.fire({
              icon: 'error',
              title: 'Error...',
              text: 'Ha ocurrido un error!'+data,
              footer: '<a href>Why do I have this issue?</a>'
            })
          })
          .finally(function() {
            vm.busy = false;
          });
      }

      //FIN----------------- Instanciacion de proceso

}