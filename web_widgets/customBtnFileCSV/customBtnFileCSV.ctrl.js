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
    $scope.objDataToSend=[];
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
            blockUI.start();
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
                            "isEliminado": data[8],
                            "ayuda": data[9],
                            "catCampus": $scope.persistenceIdCampus == null ? null : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "catGestionEscolar": $scope.persistenceIdGestionEscolar == null ? null : {
                                "persistenceId_string": $scope.persistenceIdGestionEscolar
                            }
                        });
                    }
                }
                blockUI.stop();
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
                            if ($scope.properties.catEstadoCivil[k].descripcion == data[4]) {
                                $scope.persistenceIdEstadoCivil = $scope.properties.catEstadoCivil[k].persistenceId_string;
                            }
                        }

                        //For Nacionalidad
                        for (var k = 0; k < $scope.properties.catNacionalidad.length; k++) {
                            if ($scope.properties.catNacionalidad[k].descripcion == data[5]) {
                                $scope.persistenceIdNacionalidad = $scope.properties.catNacionalidad[k].persistenceId_string;
                            }
                        }
                        
                        //For PresentarseOtroCampus
                        for (var k = 0; k < $scope.properties.catPresentasteEnOtroCampus.length; k++) {
                            if ($scope.properties.catPresentasteEnOtroCampus[k].descripcion == data[6]) {
                                $scope.persistenceIdPresentarseOtroCampus = $scope.properties.catPresentasteEnOtroCampus[k].persistenceId_string;
                            }
                        }
                        
                        //For Religion
                        for (var k = 0; k < $scope.properties.catReligion.length; k++) {
                            if ($scope.properties.catReligion[k].descripcion == data[7]) {
                                $scope.persistenceIdReligion = $scope.properties.catReligion[k].persistenceId_string;
                            }
                        }

                        //For PaisExamen
                        for (var h = 0; h < $scope.properties.catPaisExamen.length; h++) {
                            if ($scope.properties.catPaisExamen[h].descripcion == data[8]) {
                                $scope.persistenceIdPaisExamen = $scope.properties.catPaisExamen[h].persistenceId_string;
                            }
                        }
                        
                        //For EstadoExamen
                        for (var l = 0; l < $scope.properties.catEstadoExamen.length; l++) {
                            if ($scope.properties.catEstadoExamen[l].descripcion == data[9]) {
                                $scope.persistenceIdEstadoExamen = $scope.properties.catEstadoExamen[l].persistenceId_string;
                            }
                        }
                        
                        //For Bachillerato
                        for (var m = 0; m < $scope.properties.catBachilleratos.length; m++) {
                            if ($scope.properties.catBachilleratos[m].descripcion == data[10]) {
                                $scope.persistenceIdBachillerato = $scope.properties.catBachilleratos[m].persistenceId_string;
                            }
                        }
                        
                        //For Propedeutico
                        for (var n = 0; n < $scope.properties.catPropedeutico.length; n++) {
                            if ($scope.properties.catPropedeutico[n].descripcion == data[11]) {
                                $scope.persistenceIdPropedeutico = $scope.properties.catPropedeutico[n].persistenceId_string;
                            }
                        }

                        //For Campus
                        for (var k = 0; k < $scope.properties.catCampus.length; k++) {
                            if ($scope.properties.catCampus[k].descripcion == data[12]) {
                                $scope.persistenceIdCampus = $scope.properties.catCampus[k].persistenceId_string;
                            }
                        }

                        //Setea datos del csv a la lista $scope.properties.formOutput.solicitudDeAdmisionInput
                        $scope.properties.formOutput.solicitudDeAdmisionInput.push({
                            "catLugarExamen": $scope.persistenceIdLugarExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdLugarExamen
                            },
                            "catCampus": $scope.persistenceIdCampus == null ? null : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "catPeriodo": $scope.persistenceIdPeriodo == null ? null : {
                                "persistenceId_string": $scope.persistenceIdPeriodo
                            },
                            "primerNombre": data[13],
                            "segundoNombre": data[14],
                            "apellidoPaterno": data[15],
                            "apellidoMaterno": data[16],
                            "correoElectronico": data[17],
                            "catSexo": $scope.persistenceIdSexo == null ? null : {
                                "persistenceId_string": $scope.persistenceIdSexo
                            },
                            "catEstadoCivil": $scope.persistenceIdEstadoCivil == null ? null : {
                                "persistenceId_string": $scope.persistenceIdEstadoCivil
                            },
                            "catNacionalidad": $scope.persistenceIdNacionalidad == null ? null : {
                                "persistenceId_string": $scope.persistenceIdNacionalidad
                            },
                            "catPresentasteEnOtroCampus": $scope.persistenceIdPresentarseOtroCampus == null ? null : {
                                "persistenceId_string": $scope.persistenceIdPresentarseOtroCampus
                            },
                            "catCampusPresentadoSolicitud": [],
                            "catReligion": $scope.persistenceIdReligion == null ? null : {
                                "persistenceId_string": $scope.persistenceIdReligion
                            },
                            "curp": data[18],
                            "usuarioFacebook": data[19],
                            "usiarioTwitter": data[20],
                            "usuarioInstagram": data[21],
                            "telefonoCelular": data[22],
                            "foto": data[23],
                            "actaNacimiento": data[24],
                            "calle": data[25],
                            "codigoPostal": data[26],
                            "catPais": $scope.persistenceIdPaisExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdPaisExamen
                            },
                            "catEstado": $scope.persistenceIdEstadoExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdEstadoExamen
                            },
                            "ciudad": data[27],
                            "calle2": data[28],
                            "numExterior": data[29],
                            "numInterior": data[30],
                            "colonia": data[31],
                            "telefono": data[32],
                            "otroTelefonoContacto": data[33],
                            "promedioGeneral": data[33],
                            "comprobanteCalificaciones": data[35],
                            "catPaisExamen": $scope.persistenceIdPaisExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdPaisExamen
                            },
                            "catEstadoExamen": $scope.persistenceIdEstadoExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdEstadoExamen
                            },
                            "ciudadExamen": $scope.persistenceIdCiudadExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdCiudadExamen
                            },
                            "avisoPrivacidad": data[37],
                            "datosVeridicos": data[38],
                            "aceptoAvisoPrivacidad": data[39],
                            "confirmarAutorDatos": data[40],
                            "caseId": data[0],
                            "catBachilleratos": $scope.persistenceIdBachillerato == null ? null : {
                                "persistenceId_string": $scope.persistenceIdBachillerato
                            },
                            "paisBachillerato": data[41],
                            "estadoBachillerato": data[42],
                            "ciudadBachillerato": data[43],
                            "bachillerato": data[44],
                            "ciudadExamenPais": $scope.persistenceIdCiudadExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdCiudadExamen
                            },
                            "catGestionEscolar": $scope.persistenceIdGestionEscolar == null ? null : {
                                "persistenceId_string": $scope.persistenceIdGestionEscolar
                            },
                            "catPropedeutico": $scope.persistenceIdPropedeutico == null ? null : {
                                "persistenceId_string": $scope.persistenceIdPropedeutico
                            },
                            "estatusSolicitud": data[46],
                            "delegacionMunicipio": data[47],
                            "estadoExtranjero": data[48],
                            "resultadoPAA": data[49],
                            "selectedIndex": 0,
                            "selectedIndexPersonal": 0,
                            "selectedIndexFamiliar": 0,
                            "selectedIndexRevision": 0,
                            "catCampusEstudio": $scope.persistenceIdCampus == null ? null : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "tienePAA": data[50],
                            "tieneDescuento": data[51],
                            "admisionAnahuac": data[52]
                        });

                        //====================LECTURA SOLICITUD DE CAT SOLICITUD DE ADMISION====================//

                        //Setea datos del csv a la lista $scope.properties.formInputSolicitud.catSolicitudDeAdmisionInput
                        $scope.properties.formInputSolicitud.catSolicitudDeAdmisionInput.push({
                            "catLugarExamen": $scope.persistenceIdLugarExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdLugarExamen
                            },
                            "catCampus": $scope.persistenceIdCampus == null ? null : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "catPeriodo": $scope.persistenceIdPeriodo == null ? null : {
                                "persistenceId_string": $scope.persistenceIdPeriodo
                            },
                            "primerNombre": data[13],
                            "segundoNombre": data[14],
                            "apellidoPaterno": data[15],
                            "apellidoMaterno": data[16],
                            "correoElectronico": data[17],
                            "catSexo": $scope.persistenceIdSexo == null ? null : {
                                "persistenceId_string": $scope.persistenceIdSexo
                            },
                            "catEstadoCivil": $scope.persistenceIdEstadoCivil == null ? null : {
                                "persistenceId_string": $scope.persistenceIdEstadoCivil
                            },
                            "catNacionalidad": $scope.persistenceIdNacionalidad == null ? null : {
                                "persistenceId_string": $scope.persistenceIdNacionalidad
                            },
                            "catPresentasteEnOtroCampus": $scope.persistenceIdPresentarseOtroCampus == null ? null : {
                                "persistenceId_string": $scope.persistenceIdPresentarseOtroCampus
                            },
                            "catCampusPresentadoSolicitud": [],
                            "catReligion": $scope.persistenceIdReligion == null ? null : {
                                "persistenceId_string": $scope.persistenceIdReligion
                            },
                            "curp": data[18],
                            "usuarioFacebook": data[19],
                            "usiarioTwitter": data[20],
                            "usuarioInstagram": data[21],
                            "telefonoCelular": data[22],
                            "foto": data[23],
                            "actaNacimiento": data[24],
                            "calle": data[25],
                            "codigoPostal": data[26],
                            "catPais": $scope.persistenceIdPaisExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdPaisExamen
                            },
                            "catEstado": $scope.persistenceIdEstadoExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdEstadoExamen
                            },
                            "ciudad": data[27],
                            "calle2": data[28],
                            "numExterior": data[29],
                            "numInterior": data[30],
                            "colonia": data[31],
                            "telefono": data[32],
                            "otroTelefonoContacto": data[33],
                            "promedioGeneral": data[33],
                            "comprobanteCalificaciones": data[35],
                            "catPaisExamen": $scope.persistenceIdPaisExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdPaisExamen
                            },
                            "catEstadoExamen": $scope.persistenceIdEstadoExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdEstadoExamen
                            },
                            "ciudadExamen": $scope.persistenceIdCiudadExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdCiudadExamen
                            },
                            "avisoPrivacidad": data[37],
                            "datosVeridicos": data[38],
                            "aceptoAvisoPrivacidad": data[39],
                            "confirmarAutorDatos": data[40],
                            "caseId": data[0],
                            "catBachilleratos": $scope.persistenceIdBachillerato == null ? null : {
                                "persistenceId_string": $scope.persistenceIdBachillerato
                            },
                            "paisBachillerato": data[41],
                            "estadoBachillerato": data[42],
                            "ciudadBachillerato": data[43],
                            "bachillerato": data[44],
                            "ciudadExamenPais": $scope.persistenceIdCiudadExamen == null ? null : {
                                "persistenceId_string": $scope.persistenceIdCiudadExamen
                            },
                            "catGestionEscolar": $scope.persistenceIdGestionEscolar == null ? null : {
                                "persistenceId_string": $scope.persistenceIdGestionEscolar
                            },
                            "catPropedeutico": $scope.persistenceIdPropedeutico == null ? null : {
                                "persistenceId_string": $scope.persistenceIdPropedeutico
                            },
                            "estatusSolicitud": data[46],
                            "delegacionMunicipio": data[47],
                            "estadoExtranjero": data[48],
                            "resultadoPAA": data[49],
                            "selectedIndex": 0,
                            "selectedIndexPersonal": 0,
                            "selectedIndexFamiliar": 0,
                            "selectedIndexRevision": 0,
                            "catCampusEstudio": $scope.persistenceIdCampus == null ? null : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "tienePAA": data[50],
                            "tieneDescuento": data[51],
                            "admisionAnahuac": data[52]
                        });

                        //====================FIN LECTURA DE CAT SOLICITUD DE ADMISION====================//
                    }
                }

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
                        
                        //For Escolaridad
                        for (var o = 0; o < $scope.properties.catEscolaridad.length; o++) {
                            if ($scope.properties.catEscolaridad[o].descripcion == data[5]) {
                                $scope.persistenceIdEscolaridad = $scope.properties.catEscolaridad[o].persistenceId_string;
                            }
                        }
                        
                        //For Egresado
                        for (var p = 0; p < $scope.properties.catEgresado.length; p++) {
                            if ($scope.properties.catEgresado[p].descripcion == data[6]) {
                                $scope.persistenceIdEgresadoAnahuac = $scope.properties.catEgresado[p].persistenceId_string;
                            }
                        }
                        
                        //For Trabaja
                        for (var q = 0; q < $scope.properties.catTrabaja.length; q++) {
                            if ($scope.properties.catTrabaja[q].descripcion == data[7]) {
                                $scope.persistenceIdTrabaja = $scope.properties.catTrabaja[q].persistenceId_string;
                            }
                        }
                        
                        //For Vive
                        for (var r = 0; r < $scope.properties.catVive.length; r++) {
                            if ($scope.properties.catVive[r].descripcion == data[12]) {
                                $scope.persistenceIdVive = $scope.properties.catVive[r].persistenceId_string;
                            }
                        }
                        
                        //For Pais
                        for (var s = 0; s < $scope.properties.catPaisExamen.length; s++) {
                            if ($scope.properties.catPaisExamen[s].descripcion == data[14]) {
                                $scope.persistenceIdPaisMadre = $scope.properties.catPaisExamen[s].persistenceId_string;
                            }
                        }
                        
                        //For Estado
                        for (var t = 0; t < $scope.properties.catEstadoExamen.length; t++) {
                            if ($scope.properties.catEstadoExamen[t].descripcion == data[17]) {
                                $scope.persistenceIdEstadoMadre = $scope.properties.catEstadoExamen[t].persistenceId_string;
                            }
                        }

                        $scope.properties.formInputSolicitud.madreInput.push({
                            "caseId": data[0],
                            "catTitulo": $scope.persistenceIdTitulo == null ? null : {
                                "persistenceId_string": $scope.persistenceIdTitulo
                            },
                            "nombre": data[2],
                            "apellidos": data[3],
                            "correoElectronico": data[4],
                            "catEscolaridad": $scope.persistenceIdEscolaridad == null ? null : {
                                "persistenceId_string": $scope.persistenceIdEscolaridad
                            },
                            "catEgresoAnahuac": $scope.persistenceIdEgresadoAnahuac == null ? null : {
                                "persistenceId_string": $scope.persistenceIdEgresadoAnahuac
                            },
                            "catCampusEgreso": $scope.persistenceIdCampus == null ? null : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "catTrabaja": $scope.persistenceIdTrabaja == null ? null : {
                                "persistenceId_string": $scope.persistenceIdTrabaja
                            },
                            "empresaTrabaja": data[8],
                            "giroEmpresa": data[9],
                            "puesto": data[10],
                            "isTutor": data[11],
                            "vive": $scope.persistenceIdVive == null ? null : {
                                "persistenceId_string": $scope.persistenceIdVive
                            },
                            "calle": data[13],
                            "catPais": $scope.persistenceIdPaisMadre == null ? null : {
                                "persistenceId_string": $scope.persistenceIdPaisMadre
                            },
                            "numeroExterior": data[15],
                            "numeroInterior": data[16],
                            "catEstado": $scope.persistenceIdEstadoMadre == null ? null : {
                                "persistenceId_string": $scope.persistenceIdEstadoMadre
                            },
                            "ciudad": data[18],
                            "colonia": data[19],
                            "telefono": data[20],
                            "codigoPostal": data[21],
                            "viveContigo": data[22],
                            "desconozcoDatosPadres": data[23],
                            "delegacionMunicipio": data[24],
                            "estadoExtranjero": data[25]
                        });

                    }
                }

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
                        
                        //For Escolaridad
                        for (var o = 0; o < $scope.properties.catEscolaridad.length; o++) {
                            if ($scope.properties.catEscolaridad[o].descripcion == data[5]) {
                                $scope.persistenceIdEscolaridad = $scope.properties.catEscolaridad[o].persistenceId_string;
                            }
                        }
                        
                        //For Egresado
                        for (var p = 0; p < $scope.properties.catEgresado.length; p++) {
                            if ($scope.properties.catEgresado[p].descripcion == data[6]) {
                                $scope.persistenceIdEgresadoAnahuac = $scope.properties.catEgresado[p].persistenceId_string;
                            }
                        }
                        
                        //For Trabaja
                        for (var q = 0; q < $scope.properties.catTrabaja.length; q++) {
                            if ($scope.properties.catTrabaja[q].descripcion == data[7]) {
                                $scope.persistenceIdTrabaja = $scope.properties.catTrabaja[q].persistenceId_string;
                            }
                        }
                        
                        //For Vive
                        for (var r = 0; r < $scope.properties.catVive.length; r++) {
                            if ($scope.properties.catVive[r].descripcion == data[12]) {
                                $scope.persistenceIdVive = $scope.properties.catVive[r].persistenceId_string;
                            }
                        }
                        
                        //For Pais
                        for (var s = 0; s < $scope.properties.catPaisExamen.length; s++) {
                            if ($scope.properties.catPaisExamen[s].descripcion == data[14]) {
                                $scope.persistenceIdPaisPadre = $scope.properties.catPaisExamen[s].persistenceId_string;
                            }
                        }
                        
                        //For Estado
                        for (var t = 0; t < $scope.properties.catEstadoExamen.length; t++) {
                            if ($scope.properties.catEstadoExamen[t].descripcion == data[17]) {
                                $scope.persistenceIdEstadoPadre = $scope.properties.catEstadoExamen[t].persistenceId_string;
                            }
                        }

                        $scope.properties.formInputSolicitud.padreInput.push({
                            "caseId": data[0],
                            "catTitulo": $scope.persistenceIdTitulo == null ? null : {
                                "persistenceId_string": $scope.persistenceIdTitulo
                            },
                            "nombre": data[2],
                            "apellidos": data[3],
                            "correoElectronico": data[4],
                            "catEscolaridad": $scope.persistenceIdEscolaridad == null ? null : {
                                "persistenceId_string": $scope.persistenceIdEscolaridad
                            },
                            "catEgresoAnahuac": $scope.persistenceIdEgresadoAnahuac == null ? null : {
                                "persistenceId_string": $scope.persistenceIdEgresadoAnahuac
                            },
                            "catCampusEgreso": $scope.persistenceIdCampus == null ? null : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "catTrabaja": $scope.persistenceIdTrabaja == null ? null : {
                                "persistenceId_string": $scope.persistenceIdTrabaja
                            },
                            "empresaTrabaja": data[8],
                            "giroEmpresa": data[9],
                            "puesto": data[10],
                            "isTutor": data[11],
                            "vive": $scope.persistenceIdVive == null ? null : {
                                "persistenceId_string": $scope.persistenceIdVive
                            },
                            "calle": data[13],
                            "catPais": $scope.persistenceIdPaisPadre == null ? null : {
                                "persistenceId_string": $scope.persistenceIdPaisPadre
                            },
                            "numeroExterior": data[15],
                            "numeroInterior": data[16],
                            "catEstado": $scope.persistenceIdEstadoPadre == null ? null : {
                                "persistenceId_string": $scope.persistenceIdEstadoPadre
                            },
                            "ciudad": data[18],
                            "colonia": data[19],
                            "telefono": data[20],
                            "codigoPostal": data[21],
                            "viveContigo": data[22],
                            "desconozcoDatosPadres": data[23],
                            "delegacionMunicipio": data[24],
                            "estadoExtranjero": data[25]
                        });

                    }
                }

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
                        
                        //For Escolaridad
                        for (var o = 0; o < $scope.properties.catEscolaridad.length; o++) {
                            if ($scope.properties.catEscolaridad[o].descripcion == data[5]) {
                                $scope.persistenceIdEscolaridad = $scope.properties.catEscolaridad[o].persistenceId_string;
                            }
                        }
                        
                        //For Egresado
                        for (var p = 0; p < $scope.properties.catEgresado.length; p++) {
                            if ($scope.properties.catEgresado[p].descripcion == data[6]) {
                                $scope.persistenceIdEgresadoAnahuac = $scope.properties.catEgresado[p].persistenceId_string;
                            }
                        }
                        
                        //For Trabaja
                        for (var q = 0; q < $scope.properties.catTrabaja.length; q++) {
                            if ($scope.properties.catTrabaja[q].descripcion == data[7]) {
                                $scope.persistenceIdTrabaja = $scope.properties.catTrabaja[q].persistenceId_string;
                            }
                        }
                        
                        //For Vive
                        for (var r = 0; r < $scope.properties.catVive.length; r++) {
                            if ($scope.properties.catVive[r].descripcion == data[12]) {
                                $scope.persistenceIdVive = $scope.properties.catVive[r].persistenceId_string;
                            }
                        }
                        
                        //For Pais
                        for (var s = 0; s < $scope.properties.catPaisExamen.length; s++) {
                            if ($scope.properties.catPaisExamen[s].descripcion == data[14]) {
                                $scope.persistenceIdPaisTutor = $scope.properties.catPaisExamen[s].persistenceId_string;
                            }
                        }
                        
                        //For Estado
                        for (var t = 0; t < $scope.properties.catEstadoExamen.length; t++) {
                            if ($scope.properties.catEstadoExamen[t].descripcion == data[17]) {
                                $scope.persistenceIdEstadoTutor = $scope.properties.catEstadoExamen[t].persistenceId_string;
                            }
                        }

                        $scope.properties.formInputSolicitud.tutorInput.push({
                            "caseId": data[0],
                            "catTitulo": $scope.persistenceIdTitulo == null ? null : {
                                "persistenceId_string": $scope.persistenceIdTitulo
                            },
                            "nombre": data[2],
                            "apellidos": data[3],
                            "correoElectronico": data[4],
                            "catEscolaridad": $scope.persistenceIdEscolaridad == null ? null : {
                                "persistenceId_string": $scope.persistenceIdEscolaridad
                            },
                            "catEgresoAnahuac": $scope.persistenceIdEgresadoAnahuac == null ? null : {
                                "persistenceId_string": $scope.persistenceIdEgresadoAnahuac
                            },
                            "catCampusEgreso": $scope.persistenceIdCampus == null ? null : {
                                "persistenceId_string": $scope.persistenceIdCampus
                            },
                            "catTrabaja": $scope.persistenceIdTrabaja == null ? null : {
                                "persistenceId_string": $scope.persistenceIdTrabaja
                            },
                            "empresaTrabaja": data[8],
                            "giroEmpresa": data[9],
                            "puesto": data[10],
                            "isTutor": data[11],
                            "vive": $scope.persistenceIdVive == null ? null : {
                                "persistenceId_string": $scope.persistenceIdVive
                            },
                            "calle": data[13],
                            "catPais": $scope.persistenceIdPaisTutor == null ? null : {
                                "persistenceId_string": $scope.persistenceIdPaisTutor
                            },
                            "numeroExterior": data[15],
                            "numeroInterior": data[16],
                            "catEstado": $scope.persistenceIdEstadoTutor == null ? null : {
                                "persistenceId_string": $scope.persistenceIdEstadoTutor
                            },
                            "ciudad": data[18],
                            "colonia": data[19],
                            "telefono": data[20],
                            "codigoPostal": data[21],
                            "viveContigo": data[22],
                            "desconozcoDatosPadres": data[23],
                            "delegacionMunicipio": data[24],
                            "estadoExtranjero": data[25]
                        });

                    }
                }

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
                            "persistenceId_string": null,
                            "caseId": data[0],
                            "nombre": data[1],
                            "telefono": data[2],
                            "catCasoDeEmergencia": $scope.persistenceIdCasoEmergencia == null ? null : {
                                "persistenceId_string": $scope.persistenceIdCasoEmergencia
                            },
                            "telefonoCelular": data[4],
                            "parentesco": data[5],
                            "catParentesco": $scope.persistenceIdParentesco == null ? null : {
                                "persistenceId_string": $scope.persistenceIdParentesco
                            }
                        });
                    }
                }



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
                            if ($scope.properties.catPagoDeExamenDeAdmision[j].descripcion == data[1]) {
                                $scope.persistenceIdPagoExamenAdmision = $scope.properties.catPagoDeExamenDeAdmision[j].persistenceId_string;
                            }
                        }

                        //For TipoAlumno
                        for (var j = 0; j < $scope.properties.catTipoAlumno.length; j++) {
                            if ($scope.properties.catTipoAlumno[j].descripcion == data[13]) {
                                $scope.persistenceIdTipoAlumno = $scope.properties.catTipoAlumno[j].persistenceId_string;
                            }
                        }

                        //For Residencia
                        for (var j = 0; j < $scope.properties.catResidencia.length; j++) {
                            if ($scope.properties.catResidencia[j].descripcion == data[14]) {
                                $scope.persistenceIdResidencia = $scope.properties.catResidencia[j].persistenceId_string;
                            }
                        }

                        //For TipoAdmision
                        for (var j = 0; j < $scope.properties.catTipoAdmision.length; j++) {
                            if ($scope.properties.catTipoAdmision[j].descripcion == data[15]) {
                                $scope.persistenceIdTipoAdmision = $scope.properties.catTipoAdmision[j].persistenceId_string;
                            }
                        }

                        //Setea datos del csv a la lista $scope.properties.formInputValidar.detalleSolicitudInput
                        $scope.properties.formInputValidar.detalleSolicitudInput.push({
                            "catDescuentos":  {
                                "persistenceId_string": null
                            },
                            "idBanner": data[0],
                            "catPagoDeExamenDeAdmision": $scope.persistenceIdPagoExamenAdmision == null ? null : {
                                "persistenceId_string": $scope.persistenceIdPagoExamenAdmision
                            },
                            "isCurpValidado":data[2],
                            "promedioCoincide":data[3],
                            "revisado":data[4],
                            "tipoAlumno":data[5],
                            "descuento":data[6],
                            "observacionesDescuento":data[7],
                            "observacionesCambio":data[8],
                            "observacionesRechazo":data[9],
                            "observacionesListaRoja":data[10],
                            "cbCoincide":data[11],
                            "admisionAnahuac":data[12],
                            "catTipoAlumno":$scope.persistenceIdTipoAlumno == null ? null : {
                                "persistenceId_string": $scope.persistenceIdTipoAlumno
                            },
                            "catResidencia":$scope.persistenceIdResidencia == null ? null : {
                                "persistenceId_string": $scope.persistenceIdResidencia
                            },
                            "catTipoAdmision":$scope.persistenceIdTipoAdmision == null ? null : {
                                "persistenceId_string": $scope.persistenceIdTipoAdmision
                            }

                        });
                    }
                }

                debugger
                $scope.objDataToSend.push({"catRegistroInput":$scope.properties.formOutput.catRegistroInput});
                $scope.objDataToSend.push({"solicitudDeAdmisionInput":$scope.properties.formOutput.solicitudDeAdmisionInput});
                $scope.objDataToSend.push({"catSolicitudDeAdmisionInput":$scope.properties.formInputSolicitud.catSolicitudDeAdmisionInput});
                $scope.objDataToSend.push({"contactoEmergenciaInput":$scope.properties.formInputSolicitud.contactoEmergenciaInput});
                $scope.objDataToSend.push({"madreInput":$scope.properties.formInputSolicitud.madreInput});
                $scope.objDataToSend.push({"padreInput":$scope.properties.formInputSolicitud.padreInput});
                $scope.objDataToSend.push({"tutorInput":$scope.properties.formInputSolicitud.tutorInput});
                $scope.objDataToSend.push({"detalleSolicitudInput":$scope.properties.formInputValidar.detalleSolicitudInput});

                  $scope.startProcess();

                $scope.$apply();
            };
            readerPago.readAsBinaryString(inputPago.files[0]);
        };
    inputPago.addEventListener('change', readFile);

    //====================FIN LECTURA PAGO====================//

    

      //INICIO ----------Instanciacion de proceso
      $scope.startProcess =function() {
        debugger
        var id = $scope.properties.processJson[0].id;
        if (id) {
          var prom = $scope.doRequest('POST', '../API/bpm/process/' + id + '/instantiation', { 'user': $scope.properties.userData.user_id } );
    
        } else {
          $log.log('Impossible to retrieve the process definition id value from the URL');
        }
      }          

      $scope.getUserParam=function () {
        var userId = $scope.properties.userData.user_id;
        if (userId) {
          return { 'user': userId };
        }
        return {};
      }


      $scope.doRequest=function (method, url, params) {
        debugger
        vm.busy = true;
        var req = {
          method: method,
          url: url,
          data: angular.copy($scope.properties.dataToSend),
          params: params
        };
    
        return $http(req)
          .success(function(data, status) {
            debugger
            $scope.properties.dataFromSuccess = data;
            $scope.objDataToSend=[];
            /*$scope.properties.responseStatusCode = status;
            $scope.properties.dataFromError = undefined;
            notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status});
            if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
            
            }*/
          })
          .error(function(data, status) {
            debugger
            $scope.properties.dataFromError = data;
            $scope.objDataToSend=[];
            /*$scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status});*/
          })
          .finally(function() {
            vm.busy = false;
          });
      }

      //FIN----------------- Instanciacion de proceso

}