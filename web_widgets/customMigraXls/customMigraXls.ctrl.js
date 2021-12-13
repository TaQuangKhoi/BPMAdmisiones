function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;
    $scope.jsonCsv = null;

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
    $scope.persistenceIdCiudadExamenPais = null;
    $scope.persistenceIdPagoExamenAdmision = null;
    $scope.persistenceIdTipoAlumno = null;
    $scope.persistenceIdResidencia = null;
    $scope.persistenceIdTipoAdmision = null;

    /*================LECTURA DE XLS REGISTRO============================*/
    var fileInput = document.getElementById("csv"),
        readFile = function() {

            $scope.jsonCsv = null;
            var reader = new FileReader();
            reader.onload = function() {

                document.getElementById('out').innerHTML = reader.result;

                var data = reader.result;
                var workbook = XLSX.read(data, {
                    type: 'binary'
                });

                workbook.SheetNames.forEach(function(sheetName) {
                    // Here is your object

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[sheetName]);
                    var json_object = JSON.stringify(XL_row_object);
                    $scope.jsonCsv = JSON.parse(json_object);

                })
                for (var i = 0; i < $scope.jsonCsv.length; i++) {

                    //For Campus
                    for (var k = 0; k < $scope.properties.catCampus.length; k++) {
                        if ($scope.properties.catCampus[k].descripcion == $scope.jsonCsv[i].catCampus) {
                            $scope.persistenceIdCampus = $scope.properties.catCampus[k].persistenceId_string;
                        }
                    }
                    //For GestionEscolar
                    for (var k = 0; k < $scope.properties.catGestionEscolar.length; k++) {
                        if ($scope.properties.catGestionEscolar[k].descripcion == $scope.jsonCsv[i].catGestionEscolar) {
                            $scope.persistenceIdGestionEscolar = $scope.properties.catGestionEscolar[k].persistenceId_string;
                        }
                    }

                    //Setea datos del xlsx a la lista $scope.properties.formOutput.catRegistroInput
                    $scope.properties.formOutput.catRegistroInput.push({
                        "caseId": $scope.jsonCsv[i].idAspirante,
                        "nombreusuario": $scope.jsonCsv[i].nombreUsuario,
                        "primernombre": $scope.jsonCsv[i].primernombre,
                        "segundonombre": $scope.jsonCsv[i].segundonombre == undefined ? "" : $scope.jsonCsv[i].segundonombre,
                        "apellidopaterno": $scope.jsonCsv[i].apellidopaterno,
                        "apellidomaterno": $scope.jsonCsv[i].apellidomaterno,
                        "correoelectronico": $scope.jsonCsv[i].correoelectronico,
                        "password": $scope.jsonCsv[i].password,
                        "isEliminado": $scope.jsonCsv[i].isEliminado,
                        "ayuda": $scope.jsonCsv[i].ayuda,
                        "catCampus": $scope.persistenceIdCampus == null ? null : {
                            "persistenceId_string": $scope.persistenceIdCampus
                        },
                        "catGestionEscolar": $scope.persistenceIdGestionEscolar == null ? null : {
                            "persistenceId_string": $scope.persistenceIdGestionEscolar
                        }
                    });

                }

                $scope.startProcess();
                $scope.properties.selectedIndex = $scope.properties.selectedIndex + 1;
                $scope.$apply();


            };
            // start reading the file. When it is done, calls the onload event defined above.
            reader.readAsBinaryString(fileInput.files[0]);
        };

    fileInput.addEventListener('change', readFile);

    /*================FIN LECTURA DE XLS REGISTRO============================*/

    /*================LECTURA DE XLS SOLICITUD ADMISION============================*/
    var fileInputSolicitud = document.getElementById("csvSolicitud"),
        readFileSolicitud = function() {

            $scope.jsonCsv = null;
            var readerSolicitud = new FileReader();
            readerSolicitud.onload = function() {

                document.getElementById('outSolicitud').innerHTML = readerSolicitud.result;

                var dataSolicitud = readerSolicitud.result;
                var workbookSolicitud = XLSX.read(dataSolicitud, {
                    type: 'binary'
                });

                workbookSolicitud.SheetNames.forEach(function(sheetName) {
                    // Here is your object

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbookSolicitud.Sheets[sheetName]);
                    var json_object = JSON.stringify(XL_row_object);
                    $scope.jsonCsv = JSON.parse(json_object);

                })

                for (var i = 0; i < $scope.jsonCsv.length; i++) {
                    //For LugarExamen
                    for (var k = 0; k < $scope.properties.catLugarExamen.length; k++) {
                        if ($scope.properties.catLugarExamen[k].descripcion == $scope.jsonCsv[i].catLugarExamen) {
                            $scope.persistenceIdLugarExamen = $scope.properties.catLugarExamen[k].persistenceId_string;
                        }
                    }

                    //For Periodo
                    for (var k = 0; k < $scope.properties.catPeriodo.length; k++) {
                        if ($scope.properties.catPeriodo[k].descripcion == $scope.jsonCsv[i].catPeriodo) {
                            $scope.persistenceIdPeriodo = $scope.properties.catPeriodo[k].persistenceId_string;
                        }
                    }

                    //For Sexo
                    for (var k = 0; k < $scope.properties.catSexo.length; k++) {
                        if ($scope.properties.catSexo[k].descripcion == $scope.jsonCsv[i].catSexo) {
                            $scope.persistenceIdSexo = $scope.properties.catSexo[k].persistenceId_string;
                        }
                    }

                    //For EstadoCivil
                    for (var k = 0; k < $scope.properties.catEstadoCivil.length; k++) {
                        if ($scope.properties.catEstadoCivil[k].descripcion == $scope.jsonCsv[i].catEstadoCivil) {
                            $scope.persistenceIdEstadoCivil = $scope.properties.catEstadoCivil[k].persistenceId_string;
                        }
                    }

                    //For Nacionalidad
                    for (var k = 0; k < $scope.properties.catNacionalidad.length; k++) {
                        if ($scope.properties.catNacionalidad[k].descripcion == $scope.jsonCsv[i].catNacionalidad) {
                            $scope.persistenceIdNacionalidad = $scope.properties.catNacionalidad[k].persistenceId_string;
                        }
                    }

                    //For PresentarseOtroCampus
                    for (var k = 0; k < $scope.properties.catPresentasteEnOtroCampus.length; k++) {
                        if ($scope.properties.catPresentasteEnOtroCampus[k].descripcion == $scope.jsonCsv[i].catPresentasteEnOtroCampus) {
                            $scope.persistenceIdPresentarseOtroCampus = $scope.properties.catPresentasteEnOtroCampus[k].persistenceId_string;
                        }
                    }

                    //For Religion
                    for (var k = 0; k < $scope.properties.catReligion.length; k++) {
                        if ($scope.properties.catReligion[k].descripcion == $scope.jsonCsv[i].catReligion) {
                            $scope.persistenceIdReligion = $scope.properties.catReligion[k].persistenceId_string;
                        }
                    }

                    //For PaisExamen
                    for (var h = 0; h < $scope.properties.catPaisExamen.length; h++) {
                        if ($scope.properties.catPaisExamen[h].descripcion == $scope.jsonCsv[i].catPaisExamen) {
                            $scope.persistenceIdPaisExamen = $scope.properties.catPaisExamen[h].persistenceId_string;
                        }
                    }

                    //For EstadoExamen
                    for (var l = 0; l < $scope.properties.catEstadoExamen.length; l++) {
                        if ($scope.properties.catEstadoExamen[l].descripcion == $scope.jsonCsv[i].catEstadoExamen) {
                            $scope.persistenceIdEstadoExamen = $scope.properties.catEstadoExamen[l].persistenceId_string;
                        }
                    }

                    //For Bachillerato
                    for (var m = 0; m < $scope.properties.catBachilleratos.length; m++) {
                        if ($scope.properties.catBachilleratos[m].descripcion == $scope.jsonCsv[i].catBachilleratos) {
                            $scope.persistenceIdBachillerato = $scope.properties.catBachilleratos[m].persistenceId_string;
                        }
                    }

                    //For Propedeutico
                    for (var n = 0; n < $scope.properties.catPropedeutico.length; n++) {
                        if ($scope.properties.catPropedeutico[n].descripcion == $scope.jsonCsv[i].catPropedeutico) {
                            $scope.persistenceIdPropedeutico = $scope.properties.catPropedeutico[n].persistenceId_string;
                        }
                    }

                    //For Campus
                    for (var k = 0; k < $scope.properties.catCampus.length; k++) {
                        if ($scope.properties.catCampus[k].descripcion == $scope.jsonCsv[i].catCampusEstudio) {
                            $scope.persistenceIdCampus = $scope.properties.catCampus[k].persistenceId_string;
                        }
                    }

                    //For Cuidad
                    for (var k = 0; k < $scope.properties.catCiudad.length; k++) {
                        if ($scope.properties.catCiudad[k].descripcion == $scope.jsonCsv[i].ciudadExamenPais) {
                            $scope.persistenceIdCiudadExamenPais = $scope.properties.catCiudad[k].persistenceId_string;
                        }
                        if ($scope.properties.catCiudad[k].descripcion == $scope.jsonCsv[i].ciudadBachillerato) {
                            $scope.persistenceIdCiudadExamen = $scope.properties.catCiudad[k].persistenceId_string;
                        }
                    }
                    //Parseo de date a timeStamp

                    $scope.fechaNacimientoParse = null;
                    let fechaTemp = $scope.jsonCsv[i].fechaNacimiento;
                    fechaTemp = fechaTemp.split("/");
                    $scope.fechaNacimientoParse = new Date(fechaTemp[2], fechaTemp[1] - 1, fechaTemp[0]);
                    //Fin parseo

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
                        "primerNombre": $scope.jsonCsv[i].primernombreSA,
                        "segundoNombre": $scope.jsonCsv[i].segundonombreSA,
                        "apellidoPaterno": $scope.jsonCsv[i].apellidoPaternoSA,
                        "apellidoMaterno": $scope.jsonCsv[i].apellidoMaternoSA,
                        "correoElectronico": $scope.jsonCsv[i].correoElectronicoSA,
                        "catSexo": $scope.persistenceIdSexo == null ? null : {
                            "persistenceId_string": $scope.persistenceIdSexo
                        },
                        "fechaNacimiento": $scope.fechaNacimientoParse[i],
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
                        "curp": $scope.jsonCsv[i].curpSA,
                        "usuarioFacebook": $scope.jsonCsv[i].usuarioFacebook,
                        "usiarioTwitter": $scope.jsonCsv[i].usiarioTwitter,
                        "usuarioInstagram": $scope.jsonCsv[i].usuarioInstagram,
                        "telefonoCelular": $scope.jsonCsv[i].telefonoCelular,
                        "foto": null,
                        "actaNacimiento": $scope.jsonCsv[i].actaNacimiento,
                        "calle": $scope.jsonCsv[i].calle,
                        "codigoPostal": $scope.jsonCsv[i].codigoPostal,
                        "catPais": $scope.persistenceIdPaisExamen == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPaisExamen
                        },
                        "catEstado": $scope.persistenceIdEstadoExamen == null ? null : {
                            "persistenceId_string": $scope.persistenceIdEstadoExamen
                        },
                        "ciudad": $scope.jsonCsv[i].ciudad,
                        "calle2": $scope.jsonCsv[i].calle2,
                        "numExterior": $scope.jsonCsv[i].numExterior,
                        "numInterior": $scope.jsonCsv[i].numInterior,
                        "colonia": $scope.jsonCsv[i].colonia,
                        "telefono": $scope.jsonCsv[i].telefono,
                        "otroTelefonoContacto": $scope.jsonCsv[i].otroTelefonoContacto,
                        "promedioGeneral": $scope.jsonCsv[i].promedioGeneral,
                        "comprobanteCalificaciones": $scope.jsonCsv[i].comprobanteCalificaciones,
                        "catPaisExamen": $scope.persistenceIdPaisExamen == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPaisExamen
                        },
                        "catEstadoExamen": $scope.persistenceIdEstadoExamen == null ? null : {
                            "persistenceId_string": $scope.persistenceIdEstadoExamen
                        },
                        "ciudadExamen": $scope.persistenceIdCiudadExamen == null ? null : {
                            "persistenceId_string": $scope.persistenceIdCiudadExamen
                        },
                        "avisoPrivacidad": $scope.jsonCsv[i].avisoPrivacidad == 'true' ? true : false,
                        "datosVeridicos": $scope.jsonCsv[i].datosVeridicos == 'true' ? true : false,
                        "aceptoAvisoPrivacidad": $scope.jsonCsv[i].aceptoAvisoPrivacidad == 'true' ? true : false,
                        "confirmarAutorDatos": $scope.jsonCsv[i].confirmarAutorDatos == 'true' ? true : false,
                        "caseId": $scope.jsonCsv[i].idAspirante,
                        "catBachilleratos": $scope.persistenceIdBachillerato == null ? null : {
                            "persistenceId_string": $scope.persistenceIdBachillerato
                        },
                        "paisBachillerato": $scope.jsonCsv[i].paisBachillerato,
                        "estadoBachillerato": $scope.jsonCsv[i].estadoBachillerato,
                        "ciudadBachillerato": $scope.jsonCsv[i].ciudadBachillerato,
                        "bachillerato": $scope.jsonCsv[i].bachillerato,
                        "ciudadExamenPais": $scope.persistenceIdCiudadExamenPais == null ? null : {
                            "persistenceId_string": $scope.persistenceIdCiudadExamenPais
                        },
                        "catGestionEscolar": $scope.persistenceIdGestionEscolar == null ? null : {
                            "persistenceId_string": $scope.persistenceIdGestionEscolar
                        },
                        "catPropedeutico": $scope.persistenceIdPropedeutico == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPropedeutico
                        },
                        "estatusSolicitud": $scope.jsonCsv[i].estatusSolicitud,
                        "delegacionMunicipio": $scope.jsonCsv[i].delegacionMunicipio,
                        "estadoExtranjero": $scope.jsonCsv[i].estadoExtranjero,
                        "resultadoPAA": $scope.jsonCsv[i].resultadoPAA,
                        "selectedIndex": 0,
                        "selectedIndexPersonal": 0,
                        "selectedIndexFamiliar": 0,
                        "selectedIndexRevision": 0,
                        "catCampusEstudio": $scope.persistenceIdCampus == null ? null : {
                            "persistenceId_string": $scope.persistenceIdCampus
                        },
                        "tienePAA": $scope.jsonCsv[i].tienePAA == 'true' ? true : false,
                        "tieneDescuento": $scope.jsonCsv[i].tieneDescuento == 'true' ? true : false,
                        "admisionAnahuac": $scope.jsonCsv[i].admisionAnahuac == 'true' ? true : false
                    });

                }

                $scope.startProcess();
                $scope.properties.selectedIndex = $scope.properties.selectedIndex + 1;
                $scope.$apply();


            };
            // start reading the file. When it is done, calls the onload event defined above.
            readerSolicitud.readAsBinaryString(fileInputSolicitud.files[0]);
        };

    fileInputSolicitud.addEventListener('change', readFileSolicitud);

    /*================FIN LECTURA DE XLS SOLICITUD ADMISION============================*/

    //====================LECTURA MADRE====================//
    var inputMadre = document.getElementById("csvMadre"),
        readFileMadre = function() {

            $scope.jsonCsv = null;
            var readerMadre = new FileReader();
            readerMadre.onload = function() {
                document.getElementById('outMadre').innerHTML = readerMadre.result;

                var dataMadre = readerMadre.result;
                var workbookMadre = XLSX.read(dataMadre, {
                    type: 'binary'
                });

                workbookMadre.SheetNames.forEach(function(sheetName) {
                    // Here is your object

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbookMadre.Sheets[sheetName]);
                    var json_object = JSON.stringify(XL_row_object);
                    $scope.jsonCsv = JSON.parse(json_object);

                })

                for (var i = 0; i < $scope.jsonCsv.length; i++) {
                    //For Titulo
                    for (var m = 0; m < $scope.properties.catTitulo.length; m++) {
                        if ($scope.properties.catTitulo[m].descripcion == $scope.jsonCsv[i].catTituloMadre) {
                            $scope.persistenceIdTitulo = $scope.properties.catTitulo[m].persistenceId_string;
                        }
                    }
                    //For Parentesco
                    for (var k = 0; k < $scope.properties.catParentesco.length; k++) {
                        if ($scope.properties.catParentesco[k].descripcion == $scope.jsonCsv[i].catParentesco) {
                            $scope.persistenceIdParentesco = $scope.properties.catParentesco[k].persistenceId_string;
                        }
                    }
                    //For Escolaridad
                    for (var o = 0; o < $scope.properties.catEscolaridad.length; o++) {
                        if ($scope.properties.catEscolaridad[o].descripcion == $scope.jsonCsv[i].catEscolaridad) {
                            $scope.persistenceIdEscolaridad = $scope.properties.catEscolaridad[o].persistenceId_string;
                        }
                    }
                    //For Egresado
                    for (var p = 0; p < $scope.properties.catEgresado.length; p++) {
                        if ($scope.properties.catEgresado[p].descripcion == $scope.jsonCsv[i].catEgresado) {
                            $scope.persistenceIdEgresadoAnahuac = $scope.properties.catEgresado[p].persistenceId_string;
                        }
                    }
                    //For Campus
                    for (var k = 0; k < $scope.properties.catCampus.length; k++) {
                        if ($scope.properties.catCampus[k].descripcion == $scope.jsonCsv[i].catCampusEgreso) {
                            $scope.persistenceIdCampus = $scope.properties.catCampus[k].persistenceId_string;
                        }
                    }
                    //For Trabaja
                    for (var q = 0; q < $scope.properties.catTrabaja.length; q++) {
                        if ($scope.properties.catTrabaja[q].descripcion == $scope.jsonCsv[i].catTrabaja) {
                            $scope.persistenceIdTrabaja = $scope.properties.catTrabaja[q].persistenceId_string;
                        }
                    }
                    //For Vive
                    for (var r = 0; r < $scope.properties.catVive.length; r++) {
                        if ($scope.properties.catVive[r].descripcion == $scope.jsonCsv[i].catVive) {
                            $scope.persistenceIdVive = $scope.properties.catVive[r].persistenceId_string;
                        }
                    }
                    //For Pais
                    for (var s = 0; s < $scope.properties.catPaisExamen.length; s++) {
                        if ($scope.properties.catPaisExamen[s].descripcion == $scope.jsonCsv[i].catPais) {
                            $scope.persistenceIdPaisMadre = $scope.properties.catPaisExamen[s].persistenceId_string;
                        }
                    }
                    //For Estado
                    for (var t = 0; t < $scope.properties.catEstadoExamen.length; t++) {
                        if ($scope.properties.catEstadoExamen[t].descripcion == $scope.jsonCsv[i].catEstado) {
                            $scope.persistenceIdEstadoMadre = $scope.properties.catEstadoExamen[t].persistenceId_string;
                        }
                    }

                    $scope.properties.formOutput.madreInput.push({
                        "caseId": $scope.jsonCsv[i].idAspirante,
                        "catTitulo": $scope.persistenceIdTitulo == null ? null : {
                            "persistenceId_string": $scope.persistenceIdTitulo
                        },
                        "catParentezco": $scope.persistenceIdParentesco == null ? null : {
                            "persistenceId_string": $scope.persistenceIdParentesco
                        },
                        "nombre": $scope.jsonCsv[i].nombre,
                        "apellidos": $scope.jsonCsv[i].apellidos,
                        "correoElectronico": $scope.jsonCsv[i].correoElectronico,
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
                        "empresaTrabaja": $scope.jsonCsv[i].empresaTrabaja,
                        "giroEmpresa": $scope.jsonCsv[i].giroEmpresa,
                        "puesto": $scope.jsonCsv[i].puesto,
                        "isTutor": $scope.jsonCsv[i].isTutor == 'true' ? true : false,
                        "vive": $scope.persistenceIdVive == null ? null : {
                            "persistenceId_string": $scope.persistenceIdVive
                        },
                        "calle": $scope.jsonCsv[i].calle,
                        "catPais": $scope.persistenceIdPaisMadre == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPaisMadre
                        },
                        "numeroExterior": $scope.jsonCsv[i].numeroExterior,
                        "numeroInterior": $scope.jsonCsv[i].numeroInterior,
                        "catEstado": $scope.persistenceIdEstadoMadre == null ? null : {
                            "persistenceId_string": $scope.persistenceIdEstadoMadre
                        },
                        "ciudad": $scope.jsonCsv[i].ciudad,
                        "colonia": $scope.jsonCsv[i].colonia,
                        "telefono": $scope.jsonCsv[i].telefono,
                        "codigoPostal": $scope.jsonCsv[i].codigoPostal,
                        "viveContigo": $scope.jsonCsv[i].viveContigo == 'true' ? true : false,
                        "otroParentesco": $scope.jsonCsv[i].otroParentesco,
                        "desconozcoDatosPadres": $scope.jsonCsv[i].desconozcoDatosPadres == 'true' ? true : false,
                        "delegacionMunicipio": $scope.jsonCsv[i].delegacionMunicipio,
                        "estadoExtranjero": $scope.jsonCsv[i].estadoExtranjero
                    });
                }
                $scope.startProcess();
                $scope.properties.selectedIndex = $scope.properties.selectedIndex + 1;

                $scope.$apply();
            };
            readerMadre.readAsBinaryString(inputMadre.files[0]);
        };
    inputMadre.addEventListener('change', readFileMadre);

    //====================FIN LECTURA MADRE====================//

    //====================LECTURA PADRE====================//

    var inputPadre = document.getElementById("csvPadre"),
        readFile = function() {

            $scope.jsonCsv = null;
            var readerPadre = new FileReader();
            readerPadre.onload = function() {
                document.getElementById('outPadre').innerHTML = readerPadre.result;
                var dataPadre = readerPadre.result;
                var workbookPadre = XLSX.read(dataPadre, {
                    type: 'binary'
                });

                workbookPadre.SheetNames.forEach(function(sheetName) {
                    // Here is your object

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbookPadre.Sheets[sheetName]);
                    var json_object = JSON.stringify(XL_row_object);
                    $scope.jsonCsv = JSON.parse(json_object);

                })

                for (var i = 0; i < $scope.jsonCsv.length; i++) {

                    //For Titulo
                    for (var m = 0; m < $scope.properties.catTitulo.length; m++) {
                        if ($scope.properties.catTitulo[m].descripcion == $scope.jsonCsv[i].catTituloPadre) {
                            $scope.persistenceIdTitulo = $scope.properties.catTitulo[m].persistenceId_string;
                        }
                    }

                    //For Parentesco
                    for (var k = 0; k < $scope.properties.catParentesco.length; k++) {
                        if ($scope.properties.catParentesco[k].descripcion == $scope.jsonCsv[i].catParentesco) {
                            $scope.persistenceIdParentesco = $scope.properties.catParentesco[k].persistenceId_string;
                        }
                    }

                    //For Escolaridad
                    for (var o = 0; o < $scope.properties.catEscolaridad.length; o++) {
                        if ($scope.properties.catEscolaridad[o].descripcion == $scope.jsonCsv[i].catEscolaridad) {
                            $scope.persistenceIdEscolaridad = $scope.properties.catEscolaridad[o].persistenceId_string;
                        }
                    }

                    //For Egresado
                    for (var p = 0; p < $scope.properties.catEgresado.length; p++) {
                        if ($scope.properties.catEgresado[p].descripcion == $scope.jsonCsv[i].catEgresado) {
                            $scope.persistenceIdEgresadoAnahuac = $scope.properties.catEgresado[p].persistenceId_string;
                        }
                    }

                    //For Campus
                    for (var k = 0; k < $scope.properties.catCampus.length; k++) {
                        if ($scope.properties.catCampus[k].descripcion == $scope.jsonCsv[i].catCampusEgreso) {
                            $scope.persistenceIdCampus = $scope.properties.catCampus[k].persistenceId_string;
                        }
                    }

                    //For Trabaja
                    for (var q = 0; q < $scope.properties.catTrabaja.length; q++) {
                        if ($scope.properties.catTrabaja[q].descripcion == $scope.jsonCsv[i].catTrabaja) {
                            $scope.persistenceIdTrabaja = $scope.properties.catTrabaja[q].persistenceId_string;
                        }
                    }

                    //For Vive
                    for (var r = 0; r < $scope.properties.catVive.length; r++) {
                        if ($scope.properties.catVive[r].descripcion == $scope.jsonCsv[i].catVive) {
                            $scope.persistenceIdVive = $scope.properties.catVive[r].persistenceId_string;
                        }
                    }

                    //For Pais
                    for (var s = 0; s < $scope.properties.catPaisExamen.length; s++) {
                        if ($scope.properties.catPaisExamen[s].descripcion == $scope.jsonCsv[i].catPais) {
                            $scope.persistenceIdPaisMadre = $scope.properties.catPaisExamen[s].persistenceId_string;
                        }
                    }

                    //For Estado
                    for (var t = 0; t < $scope.properties.catEstadoExamen.length; t++) {
                        if ($scope.properties.catEstadoExamen[t].descripcion == $scope.jsonCsv[i].catEstado) {
                            $scope.persistenceIdEstadoMadre = $scope.properties.catEstadoExamen[t].persistenceId_string;
                        }
                    }

                    $scope.properties.formOutput.padreInput.push({
                        "caseId": $scope.jsonCsv[i].idAspirante,
                        "catTitulo": $scope.persistenceIdTitulo == null ? null : {
                            "persistenceId_string": $scope.persistenceIdTitulo
                        },
                        "catParentezco": $scope.persistenceIdParentesco == null ? null : {
                            "persistenceId_string": $scope.persistenceIdParentesco
                        },
                        "nombre": $scope.jsonCsv[i].nombre,
                        "apellidos": $scope.jsonCsv[i].apellidos,
                        "correoElectronico": $scope.jsonCsv[i].correoElectronico,
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
                        "empresaTrabaja": $scope.jsonCsv[i].empresaTrabaja,
                        "giroEmpresa": $scope.jsonCsv[i].giroEmpresa,
                        "puesto": $scope.jsonCsv[i].puesto,
                        "isTutor": $scope.jsonCsv[i].isTutor == 'true' ? true : false,
                        "vive": $scope.persistenceIdVive == null ? null : {
                            "persistenceId_string": $scope.persistenceIdVive
                        },
                        "calle": $scope.jsonCsv[i].calle,
                        "catPais": $scope.persistenceIdPaisMadre == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPaisMadre
                        },
                        "numeroExterior": $scope.jsonCsv[i].numeroExterior,
                        "numeroInterior": $scope.jsonCsv[i].numeroInterior,
                        "catEstado": $scope.persistenceIdEstadoMadre == null ? null : {
                            "persistenceId_string": $scope.persistenceIdEstadoMadre
                        },
                        "ciudad": $scope.jsonCsv[i].ciudad,
                        "colonia": $scope.jsonCsv[i].colonia,
                        "telefono": $scope.jsonCsv[i].telefono,
                        "codigoPostal": $scope.jsonCsv[i].codigoPostal,
                        "viveContigo": $scope.jsonCsv[i].viveContigo == 'true' ? true : false,
                        "otroParentesco": $scope.jsonCsv[i].otroParentesco,
                        "desconozcoDatosPadres": $scope.jsonCsv[i].desconozcoDatosPadres == 'true' ? true : false,
                        "delegacionMunicipio": $scope.jsonCsv[i].delegacionMunicipio,
                        "estadoExtranjero": $scope.jsonCsv[i].estadoExtranjero
                    });
                }
                $scope.startProcess();
                $scope.properties.selectedIndex = $scope.properties.selectedIndex + 1;

                $scope.$apply();
            };
            readerPadre.readAsBinaryString(inputPadre.files[0]);
        };
    inputPadre.addEventListener('change', readFile);

    //====================FIN LECTURA PADRE====================//

    //====================LECTURA TUTOR====================//

    var inputTutor = document.getElementById("csvTutor"),
        readFile = function() {

            $scope.jsonCsv = null;
            var readerTutor = new FileReader();
            readerTutor.onload = function() {
                document.getElementById('outTutor').innerHTML = readerTutor.result;
                var dataTutor = readerTutor.result;
                var workbookTutor = XLSX.read(dataTutor, {
                    type: 'binary'
                });

                workbookTutor.SheetNames.forEach(function(sheetName) {
                    // Here is your object

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbookTutor.Sheets[sheetName]);
                    var json_object = JSON.stringify(XL_row_object);
                    $scope.jsonCsv = JSON.parse(json_object);

                })

                for (var i = 0; i < $scope.jsonCsv.length; i++) {
                    //For Titulo
                    for (var m = 0; m < $scope.properties.catTitulo.length; m++) {
                        if ($scope.properties.catTitulo[m].descripcion == $scope.jsonCsv[i].catTituloTutor) {
                            $scope.persistenceIdTitulo = $scope.properties.catTitulo[m].persistenceId_string;
                        }
                    }
                    //For Parentesco
                    for (var k = 0; k < $scope.properties.catParentesco.length; k++) {
                        if ($scope.properties.catParentesco[k].descripcion == $scope.jsonCsv[i].catParentesco) {
                            $scope.persistenceIdParentesco = $scope.properties.catParentesco[k].persistenceId_string;
                        }
                    }
                    //For Escolaridad
                    for (var o = 0; o < $scope.properties.catEscolaridad.length; o++) {
                        if ($scope.properties.catEscolaridad[o].descripcion == $scope.jsonCsv[i].catEscolaridad) {
                            $scope.persistenceIdEscolaridad = $scope.properties.catEscolaridad[o].persistenceId_string;
                        }
                    }
                    //For Egresado
                    for (var p = 0; p < $scope.properties.catEgresado.length; p++) {
                        if ($scope.properties.catEgresado[p].descripcion == $scope.jsonCsv[i].catEgresado) {
                            $scope.persistenceIdEgresadoAnahuac = $scope.properties.catEgresado[p].persistenceId_string;
                        }
                    }
                    //For Campus
                    for (var k = 0; k < $scope.properties.catCampus.length; k++) {
                        if ($scope.properties.catCampus[k].descripcion == $scope.jsonCsv[i].catCampusEgreso) {
                            $scope.persistenceIdCampus = $scope.properties.catCampus[k].persistenceId_string;
                        }
                    }
                    //For Trabaja
                    for (var q = 0; q < $scope.properties.catTrabaja.length; q++) {
                        if ($scope.properties.catTrabaja[q].descripcion == $scope.jsonCsv[i].catTrabaja) {
                            $scope.persistenceIdTrabaja = $scope.properties.catTrabaja[q].persistenceId_string;
                        }
                    }

                    //For Vive
                    for (var r = 0; r < $scope.properties.catVive.length; r++) {
                        if ($scope.properties.catVive[r].descripcion == $scope.jsonCsv[i].catVive) {
                            $scope.persistenceIdVive = $scope.properties.catVive[r].persistenceId_string;
                        }
                    }

                    //For Pais
                    for (var s = 0; s < $scope.properties.catPaisExamen.length; s++) {
                        if ($scope.properties.catPaisExamen[s].descripcion == $scope.jsonCsv[i].catPais) {
                            $scope.persistenceIdPaisMadre = $scope.properties.catPaisExamen[s].persistenceId_string;
                        }
                    }
                    //For Estado
                    for (var t = 0; t < $scope.properties.catEstadoExamen.length; t++) {
                        if ($scope.properties.catEstadoExamen[t].descripcion == $scope.jsonCsv[i].catEstado) {
                            $scope.persistenceIdEstadoMadre = $scope.properties.catEstadoExamen[t].persistenceId_string;
                        }
                    }

                    $scope.properties.formOutput.tutorInput.push({
                        "caseId": $scope.jsonCsv[i].idAspirante,
                        "catTitulo": $scope.persistenceIdTitulo == null ? null : {
                            "persistenceId_string": $scope.persistenceIdTitulo
                        },
                        "catParentezco": $scope.persistenceIdParentesco == null ? null : {
                            "persistenceId_string": $scope.persistenceIdParentesco
                        },
                        "nombre": $scope.jsonCsv[i].nombre,
                        "apellidos": $scope.jsonCsv[i].apellidos,
                        "correoElectronico": $scope.jsonCsv[i].correoElectronico,
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
                        "empresaTrabaja": $scope.jsonCsv[i].empresaTrabaja,
                        "giroEmpresa": $scope.jsonCsv[i].giroEmpresa,
                        "puesto": $scope.jsonCsv[i].puesto,
                        "isTutor": $scope.jsonCsv[i].isTutor == 'true' ? true : false,
                        "vive": $scope.persistenceIdVive == null ? null : {
                            "persistenceId_string": $scope.persistenceIdVive
                        },
                        "calle": $scope.jsonCsv[i].calle,
                        "catPais": $scope.persistenceIdPaisMadre == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPaisMadre
                        },
                        "numeroExterior": $scope.jsonCsv[i].numeroExterior,
                        "numeroInterior": $scope.jsonCsv[i].numeroInterior,
                        "catEstado": $scope.persistenceIdEstadoMadre == null ? null : {
                            "persistenceId_string": $scope.persistenceIdEstadoMadre
                        },
                        "ciudad": $scope.jsonCsv[i].ciudad,
                        "colonia": $scope.jsonCsv[i].colonia,
                        "telefono": $scope.jsonCsv[i].telefono,
                        "codigoPostal": $scope.jsonCsv[i].codigoPostal,
                        "viveContigo": $scope.jsonCsv[i].viveContigo == 'true' ? true : false,
                        "otroParentesco": $scope.jsonCsv[i].otroParentesco,
                        "desconozcoDatosPadres": $scope.jsonCsv[i].desconozcoDatosPadres == 'true' ? true : false,
                        "delegacionMunicipio": $scope.jsonCsv[i].delegacionMunicipio,
                        "estadoExtranjero": $scope.jsonCsv[i].estadoExtranjero
                    });
                }
                $scope.startProcess();
                $scope.properties.selectedIndex = $scope.properties.selectedIndex + 1;

                $scope.$apply();
            };
            readerTutor.readAsBinaryString(inputTutor.files[0]);
        };
    inputTutor.addEventListener('change', readFile);

    //====================FIN LECTURA TUTOR====================//

    //====================LECTURA CASO EMERGENCIA====================//

    var inputCaso = document.getElementById("csvCaso"),
        readFile = function() {

            $scope.jsonCsv = null;
            var readerCaso = new FileReader();
            readerCaso.onload = function() {
                document.getElementById('outCaso').innerHTML = readerCaso.result;

                var dataCaso = readerCaso.result;
                var workbookCaso = XLSX.read(dataCaso, {
                    type: 'binary'
                });

                workbookCaso.SheetNames.forEach(function(sheetName) {
                    // Here is your object

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbookCaso.Sheets[sheetName]);
                    var json_object = JSON.stringify(XL_row_object);
                    $scope.jsonCsv = JSON.parse(json_object);

                })


                for (var i = 0; i < $scope.jsonCsv.length; i++) {

                    //For CasoEmergencia
                    for (var s = 0; s < $scope.properties.catCasoEmergencia.length; s++) {
                        if ($scope.properties.catCasoEmergencia[s].descripcion == $scope.jsonCsv[i].catCasoDeEmergencia) {
                            $scope.persistenceIdCasoEmergencia = $scope.properties.catCasoEmergencia[s].persistenceId_string;
                        }
                    }

                    //For Parentesco
                    for (var t = 0; t < $scope.properties.catParentesco.length; t++) {
                        if ($scope.properties.catParentesco[t].descripcion == $scope.jsonCsv[i].catParentesco) {
                            $scope.persistenceIdParentesco = $scope.properties.catParentesco[t].persistenceId_string;
                        }
                    }

                    ////Setea datos del csv a la lista $scope.properties.formInputSolicitud.contactoEmergenciaInput
                    $scope.properties.formOutput.contactoEmergenciaInput.push({
                        "caseId": $scope.jsonCsv[i].idAspirante,
                        "nombre": $scope.jsonCsv[i].nombre,
                        "telefono": $scope.jsonCsv[i].telefono,
                        "catCasoDeEmergencia": $scope.persistenceIdCasoEmergencia == null ? null : {
                            "persistenceId_string": $scope.persistenceIdCasoEmergencia
                        },
                        "telefonoCelular": $scope.jsonCsv[i].telefonoCelular,
                        "parentesco": $scope.jsonCsv[i].parentesco,
                        "catParentesco": $scope.persistenceIdParentesco == null ? null : {
                            "persistenceId_string": $scope.persistenceIdParentesco
                        }
                    });
                }
                $scope.startProcess();
                $scope.properties.selectedIndex = $scope.properties.selectedIndex + 1;

                $scope.$apply();
            };
            readerCaso.readAsBinaryString(inputCaso.files[0]);
        };
    inputCaso.addEventListener('change', readFile);

    //====================FIN LECTURA CASO EMERGENCIA====================//

    //====================LECTURA PAGO====================//

    var inputPago = document.getElementById("csvPago"),
        readFile = function() {

            $scope.jsonCsv = null;
            var readerPago = new FileReader();
            readerPago.onload = function() {
                document.getElementById('outPago').innerHTML = readerPago.result;

                var dataPago = readerPago.result;
                var workbookPago = XLSX.read(dataPago, {
                    type: 'binary'
                });

                workbookPago.SheetNames.forEach(function(sheetName) {
                    // Here is your object

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbookPago.Sheets[sheetName]);
                    var json_object = JSON.stringify(XL_row_object);
                    $scope.jsonCsv = JSON.parse(json_object);

                })


                for (var i = 0; i < $scope.jsonCsv.length; i++) {

                    //For PagoExamenAdmision
                    for (var j = 0; j < $scope.properties.catPagoDeExamenDeAdmision.length; j++) {
                        if ($scope.properties.catPagoDeExamenDeAdmision[j].descripcion == $scope.jsonCsv[i].catPagoDeExamenDeAdmision) {
                            $scope.persistenceIdPagoExamenAdmision = $scope.properties.catPagoDeExamenDeAdmision[j].persistenceId_string;
                        }
                    }

                    //For TipoAlumno
                    for (var j = 0; j < $scope.properties.catTipoAlumno.length; j++) {
                        if ($scope.properties.catTipoAlumno[j].descripcion == $scope.jsonCsv[i].catTipoAlumno) {
                            $scope.persistenceIdTipoAlumno = $scope.properties.catTipoAlumno[j].persistenceId_string;
                        }
                    }

                    //For Residencia
                    for (var j = 0; j < $scope.properties.catResidencia.length; j++) {
                        if ($scope.properties.catResidencia[j].descripcion == $scope.jsonCsv[i].catResidencia) {
                            $scope.persistenceIdResidencia = $scope.properties.catResidencia[j].persistenceId_string;
                        }
                    }

                    //For TipoAdmision
                    for (var j = 0; j < $scope.properties.catTipoAdmision.length; j++) {
                        if ($scope.properties.catTipoAdmision[j].descripcion == $scope.jsonCsv[i].catTipoAdmision) {
                            $scope.persistenceIdTipoAdmision = $scope.properties.catTipoAdmision[j].persistenceId_string;
                        }
                    }

                    //Setea datos del csv a la lista $scope.properties.formInputValidar.detalleSolicitudInput
                    $scope.properties.formOutput.detalleSolicitudInput.push({
                        "catDescuentos": null,
                        "idBanner": $scope.jsonCsv[i].idBanner,
                        "catPagoDeExamenDeAdmision": $scope.persistenceIdPagoExamenAdmision == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPagoExamenAdmision
                        },
                        "isCurpValidado": $scope.jsonCsv[i].isCurpValidado == 'true' ? true : false,
                        "promedioCoincide": $scope.jsonCsv[i].promedioCoincide == 'true' ? true : false,
                        "revisado": $scope.jsonCsv[i].revisado == 'true' ? true : false,
                        "tipoAlumno": $scope.jsonCsv[i].tipoAlumno,
                        "descuento": $scope.jsonCsv[i].descuento,
                        "observacionesDescuento": $scope.jsonCsv[i].observacionesDescuento,
                        "observacionesCambio": $scope.jsonCsv[i].observacionesCambio,
                        "observacionesRechazo": $scope.jsonCsv[i].observacionesRechazo,
                        "observacionesListaRoja": $scope.jsonCsv[i].observacionesListaRoja,
                        "ordenPago": $scope.jsonCsv[i].ordenPago,
                        "caseId": $scope.jsonCsv[i].idAspirante,
                        "cbCoincide": $scope.jsonCsv[i].cbCoincide == 'true' ? true : false,
                        "admisionAnahuac": $scope.jsonCsv[i].admisionAnahuac == 'true' ? true : false,
                        "catTipoAlumno": $scope.persistenceIdTipoAlumno == null ? null : {
                            "persistenceId_string": $scope.persistenceIdTipoAlumno
                        },
                        "catResidencia": $scope.persistenceIdResidencia == null ? null : {
                            "persistenceId_string": $scope.persistenceIdResidencia
                        },
                        "catTipoAdmision": $scope.persistenceIdTipoAdmision == null ? null : {
                            "persistenceId_string": $scope.persistenceIdTipoAdmision
                        }

                    });
                }

                $scope.startProcess();
                $scope.$apply();
            };
            readerPago.readAsBinaryString(inputPago.files[0]);
        };
    inputPago.addEventListener('change', readFile);

    //====================FIN LECTURA PAGO====================//


    //INICIO ----------Instanciacion de proceso
    $scope.startProcess = function() {
        var id = 0;

        //Modulo Registro
        if ($scope.properties.selectedIndex == 0) {
            id = $scope.properties.procesoRegistroId[0].id;
            $scope.objDataToSend = {
                "catRegistroInput": $scope.properties.formOutput.catRegistroInput
            }
        }
        //Modulo Solicitud de Admision
        else if ($scope.properties.selectedIndex == 1) {
            id = $scope.properties.procesoSolicitudAdmisionId[0].id;
            $scope.objDataToSend = {
                "solicitudDeAdmisionInput": $scope.properties.formOutput.solicitudDeAdmisionInput
            }

        }
        //Modulo Madre
        else if ($scope.properties.selectedIndex == 2) {
            id = $scope.properties.procesoMadreId[0].id;
            $scope.objDataToSend = {
                "madreInput": $scope.properties.formOutput.madreInput
            }

        }
        //Modulo Padre
        else if ($scope.properties.selectedIndex == 3) {
            id = $scope.properties.procesoPadreId[0].id;
            $scope.objDataToSend = {
                "padreInput": $scope.properties.formOutput.padreInput
            }

        }
        //Modulo Tutor
        else if ($scope.properties.selectedIndex == 4) {
            id = $scope.properties.procesoTutorId[0].id;
            $scope.objDataToSend = {
                "tutorInput": $scope.properties.formOutput.tutorInput
            }

        }
        //Modulo Contacto de Emergencia
        else if ($scope.properties.selectedIndex == 5) {
            id = $scope.properties.procesoContactoEmergenciaId[0].id;
            $scope.objDataToSend = {
                "contactoEmergenciaInput": $scope.properties.formOutput.contactoEmergenciaInput
            }

        }
        //Modulo Pago
        else if ($scope.properties.selectedIndex == 6) {
            id = $scope.properties.procesoPagoId[0].id;
            $scope.objDataToSend = {
                "detalleSolicitudInput": $scope.properties.formOutput.detalleSolicitudInput
            }

        }
        if (id) {
            var prom = $scope.doRequest('POST', '../API/bpm/process/' + id + '/instantiation', { 'user': $scope.properties.userData.user_id });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
        }
    }

    $scope.doRequest = function(method, url, params) {

        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.objDataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {

                $scope.objDataToSend = {};
                Swal.fire(
                    'Operación Exitosa!',
                    'Los aspirantes han sido agregados correctamente con el caseId:! ' + data.caseId,
                    'success'
                )
            })
            .error(function(data, status) {

                $scope.objDataToSend = {};
                Swal.fire({
                    icon: 'error',
                    title: 'Error...',
                    text: 'Ha ocurrido un error!' + data,
                    footer: '<a href>Why do I have this issue?</a>'
                })
            })
            .finally(function() {
                vm.busy = false;
            });
    }

    //FIN----------------- Instanciacion de proceso

}