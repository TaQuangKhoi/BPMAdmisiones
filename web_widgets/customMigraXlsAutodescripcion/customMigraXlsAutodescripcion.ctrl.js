function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;
    $scope.jsonCsv = null;
    $scope.lstMatGustan = [];
    $scope.lstMatNoGustan = [];
    $scope.lstMatCalifAlt = [];
    $scope.lstMatCalifBaj = [];
    $scope.lstDiscapacidades = [];
    $scope.lstProblemasSalud = [];
    $scope.lstDeportes = [];
    $scope.lstLectura = [];
    $scope.lstInfoCarrera = [];
    $scope.lstCualidades = [];
    $scope.lstDefectos = [];
    $scope.lstPasatiempos = [];

    /*================MODULO AUTODESCRIPCION ============================*/
    var fileInput = document.getElementById("autoDescripcion"),
        readFile = function() {
            
            $scope.jsonCsv = null;
            var reader = new FileReader();
            reader.onload = function() {

                document.getElementById('outautoDescripcion').innerHTML = reader.result;
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
                    $scope.persistenceIdVivePadre = null;
                    $scope.persistenceIdViveMadre = null;
                    $scope.persistenceIdConQuienVives = null;
                    $scope.persistenceIdEstadoCivilPadres = null;
                    $scope.persistenceIdMateriaTeGusta = null;
                    $scope.persistenceIdMateriaNoTeGusta = null;
                    $scope.persistenceIdMateriaCalifAlta = null;
                    $scope.persistenceIdMateriaCalifBaja = null;
                    $scope.persistenceIdEstudiadoExtranjero = null;
                    $scope.persistenceIdPaisEstudiasteExtranjero = null;
                    $scope.persistenceIdtiempoEstudiasteExtranjero = null;
                    $scope.persistenceIdAreaBachillerato = null;
                    $scope.persistenceIdHasPresentadoExamenExtraordinario = null;
                    $scope.persistenceIdHasReprobado = null;
                    $scope.persistenceIdInscritoOtraUniversidad = null;
                    $scope.persistenceIdHasTenidoTrabajo = null;
                    $scope.persistenceIdExperienciaAyudaCarrera = null;
                    $scope.persistenceIdActualnenteTrabajas = null;
                    $scope.persistenceIdMotivoTrabajas = null;
                    $scope.persistenceIdGiroEmpresa = null;
                    $scope.persistenceIdHaceCuantoTiempoTrabajas = null;
                    $scope.persistenceIdHorarioTrabajo = null;
                    $scope.persistenceIdVivesEstadoDiscapacidad = null;
                    $scope.persistenceIdProblemaSaludAtencionContinua = null;
                    $scope.persistenceIdPersonaSaludable = null;
                    $scope.persistenceIdFamiliarTeLlevasMejor = null;
                    $scope.persistenceIdRelacionHermanos = null;
                    $scope.persistenceIdPracticasDeporte = null;
                    $scope.persistenceIdParticipasGrupoSocial = null;
                    $scope.persistenceIdTeGustaLeer = null;
                    $scope.persistenceIdJefeOrganizacionSocial = null;
                    $scope.persistenceIddiscapacidades = null;
                    $scope.persistenceIdproblemasSalud = null;
                    $scope.persistenceIddeportes = null;
                    $scope.persistenceIdlecturas = null;
                    $scope.persistenceIdpasatiempos = null;
                    $scope.persistenceIdPlaticasProblemasPadre = null;
                    $scope.persistenceIdPlaticasProblemasMadre = null;
                    $scope.persistenceIdReligion = null;
                    $scope.persistenceIdRecibirAtencionEspiritual = null;
                    $scope.persistenceIdRegnumChris = null;
                    $scope.persistenceIdPracticasReligion = null;
                    $scope.persistenceIdAspectoDesagradaReligion = null;
                    $scope.persistenceIdAreaLaboralDeInteres = null;
                    $scope.persistenceIdPadresDeAcuerdo = null;
                    $scope.persistenceIdinformacionCarrera = null;
                    $scope.persistenceIdOrientacionVocacional = null;
                    $scope.persistenceIdPersonaIntervinoEleccion = null;
                    $scope.persistenceIdYaResolvisteElProblema = null;
                    $scope.persistenceIdcualidades = null;
                    $scope.persistenceIddefectos = null;
                    $scope.persistenceIdFamiliarEgresadoAnahuac = null;


                    //For catVive
                    for (var k = 0; k < $scope.properties.catVive.length; k++) {
                        if ($scope.properties.catVive[k].descripcion == $scope.jsonCsv[i].catPadreVive) {
                            $scope.persistenceIdVivePadre = $scope.properties.catVive[k].persistenceId_string;
                        } else if ($scope.properties.catVive[k].descripcion == $scope.jsonCsv[i].catMadreVive) {
                            $scope.persistenceIdViveMadre = $scope.properties.catVive[k].persistenceId_string;
                        }
                    }
                    //For catConQuienVives
                    for (var k = 0; k < $scope.properties.catConQuienVives.length; k++) {
                        if ($scope.properties.catConQuienVives[k].descripcion == $scope.jsonCsv[i].catConQuienVives) {
                            $scope.persistenceIdConQuienVives = $scope.properties.catConQuienVives[k].persistenceId_string;
                        }
                    }
                    //For catEstadoCivilPadres
                    for (var k = 0; k < $scope.properties.catEstadoCivilPadres.length; k++) {
                        if ($scope.properties.catEstadoCivilPadres[k].descripcion == $scope.jsonCsv[i].catEstadoCivilPadres) {
                            $scope.persistenceIdEstadoCivilPadres = $scope.properties.catEstadoCivilPadres[k].persistenceId_string;
                        }
                    }
                    //For catMaterias
                    for (var k = 0; k < $scope.properties.catMaterias.length; k++) {
                        if ($scope.properties.catMaterias[k].descripcion == $scope.jsonCsv[i].materiasTeGustan) {
                            $scope.lstMatGustan.push({
                                "persistenceId_string": $scope.properties.catMaterias[k].persistenceId_string
                            })
                        }
                        if ($scope.properties.catMaterias[k].descripcion == $scope.jsonCsv[i].materiasNoTeGustan) {
                            $scope.lstMatNoGustan.push({
                                "persistenceId_string": $scope.properties.catMaterias[k].persistenceId_string
                            })
                        }
                        if ($scope.properties.catMaterias[k].descripcion == $scope.jsonCsv[i].materiasCalifAltas) {
                            $scope.lstMatCalifAlt.push({
                                "persistenceId_string": $scope.properties.catMaterias[k].persistenceId_string
                            })
                        }
                        if ($scope.properties.catMaterias[k].descripcion == $scope.jsonCsv[i].materiasCalifBajas) {
                            $scope.lstMatCalifBaj.push({
                                "persistenceId_string": $scope.properties.catMaterias[k].persistenceId_string
                            })
                        }
                    }

                    //For catEstudiadoExtranjero
                    for (var k = 0; k < $scope.properties.catEstudiadoExtranjero.length; k++) {
                        if ($scope.properties.catEstudiadoExtranjero[k].descripcion == $scope.jsonCsv[i].catEstudiadoExtranjero) {
                            $scope.persistenceIdEstudiadoExtranjero = $scope.properties.catEstudiadoExtranjero[k].persistenceId_string;
                        }
                    }

                    //For paisEstudiasteExtranjero
                    for (var k = 0; k < $scope.properties.paisEstudiasteExtranjero.length; k++) {
                        if ($scope.properties.paisEstudiasteExtranjero[k].descripcion == $scope.jsonCsv[i].paisEstudiasteExtranjero) {
                            $scope.persistenceIdPaisEstudiasteExtranjero = $scope.properties.paisEstudiasteExtranjero[k].persistenceId_string;
                        }
                    }

                    //For tiempoEstudiasteExtranjero
                    for (var k = 0; k < $scope.properties.tiempoEstudiasteExtranjero.length; k++) {
                        if ($scope.properties.tiempoEstudiasteExtranjero[k].descripcion == $scope.jsonCsv[i].tiempoEstudiasteExtranjero) {
                            $scope.persistenceIdtiempoEstudiasteExtranjero = $scope.properties.tiempoEstudiasteExtranjero[k].persistenceId_string;
                        }
                    }

                    //For catAreaBachillerato
                    for (var k = 0; k < $scope.properties.catAreaBachillerato.length; k++) {
                        if ($scope.properties.catAreaBachillerato[k].descripcion == $scope.jsonCsv[i].catAreaBachillerato) {
                            $scope.persistenceIdAreaBachillerato = $scope.properties.catAreaBachillerato[k].persistenceId_string;
                        }
                    }

                    //For catHasPresentadoExamenExtraordinario
                    for (var k = 0; k < $scope.properties.catHasPresentadoExamenExtraordinario.length; k++) {
                        if ($scope.properties.catHasPresentadoExamenExtraordinario[k].descripcion == $scope.jsonCsv[i].catHasPresentadoExamenExtraordinario) {
                            $scope.persistenceIdHasPresentadoExamenExtraordinario = $scope.properties.catHasPresentadoExamenExtraordinario[k].persistenceId_string;
                        }
                    }

                    //For catHasReprobado
                    for (var k = 0; k < $scope.properties.catHasReprobado.length; k++) {
                        if ($scope.properties.catHasReprobado[k].descripcion == $scope.jsonCsv[i].catHasReprobado) {
                            $scope.persistenceIdHasReprobado = $scope.properties.catHasReprobado[k].persistenceId_string;
                        }
                    }

                    //For catInscritoOtraUniversidad
                    for (var k = 0; k < $scope.properties.catInscritoOtraUniversidad.length; k++) {
                        if ($scope.properties.catInscritoOtraUniversidad[k].descripcion == $scope.jsonCsv[i].catInscritoOtraUniversidad) {
                            $scope.persistenceIdInscritoOtraUniversidad = $scope.properties.catInscritoOtraUniversidad[k].persistenceId_string;
                        }
                    }

                    //For catHasTenidoTrabajo
                    for (var k = 0; k < $scope.properties.catHasTenidoTrabajo.length; k++) {
                        if ($scope.properties.catHasTenidoTrabajo[k].descripcion == $scope.jsonCsv[i].catHasTenidoTrabajo) {
                            $scope.persistenceIdHasTenidoTrabajo = $scope.properties.catHasTenidoTrabajo[k].persistenceId_string;
                        }
                    }

                    //For catExperienciaAyudaCarrera
                    for (var k = 0; k < $scope.properties.catExperienciaAyudaCarrera.length; k++) {
                        if ($scope.properties.catExperienciaAyudaCarrera[k].descripcion == $scope.jsonCsv[i].catExperienciaAyudaCarrera) {
                            $scope.persistenceIdExperienciaAyudaCarrera = $scope.properties.catExperienciaAyudaCarrera[k].persistenceId_string;
                        }
                    }

                    //For catActualnenteTrabajas
                    for (var k = 0; k < $scope.properties.catActualnenteTrabajas.length; k++) {
                        if ($scope.properties.catActualnenteTrabajas[k].descripcion == $scope.jsonCsv[i].catActualnenteTrabajas) {
                            $scope.persistenceIdActualnenteTrabajas = $scope.properties.catActualnenteTrabajas[k].persistenceId_string;
                        }
                    }

                    //For catMotivoTrabajas
                    for (var k = 0; k < $scope.properties.catMotivoTrabajas.length; k++) {
                        if ($scope.properties.catMotivoTrabajas[k].descripcion == $scope.jsonCsv[i].catMotivoTrabajas) {
                            $scope.persistenceIdMotivoTrabajas = $scope.properties.catMotivoTrabajas[k].persistenceId_string;
                        }
                    }

                    //For catGiroEmpresa
                    for (var k = 0; k < $scope.properties.catGiroEmpresa.length; k++) {
                        if ($scope.properties.catGiroEmpresa[k].descripcion == $scope.jsonCsv[i].catGiroEmpresa) {
                            $scope.persistenceIdGiroEmpresa = $scope.properties.catGiroEmpresa[k].persistenceId_string;
                        }
                    }

                    //For catHaceCuantoTiempoTrabajas
                    for (var k = 0; k < $scope.properties.catHaceCuantoTiempoTrabajas.length; k++) {
                        if ($scope.properties.catHaceCuantoTiempoTrabajas[k].descripcion == $scope.jsonCsv[i].catHaceCuantoTiempoTrabajas) {
                            $scope.persistenceIdHaceCuantoTiempoTrabajas = $scope.properties.catHaceCuantoTiempoTrabajas[k].persistenceId_string;
                        }
                    }

                    //For catHorarioTrabajo
                    for (var k = 0; k < $scope.properties.catHorarioTrabajo.length; k++) {
                        if ($scope.properties.catHorarioTrabajo[k].descripcion == $scope.jsonCsv[i].catHorarioTrabajo) {
                            $scope.persistenceIdHorarioTrabajo = $scope.properties.catHorarioTrabajo[k].persistenceId_string;
                        }
                    }

                    //For catVivesEstadoDiscapacidad
                    for (var k = 0; k < $scope.properties.catVivesEstadoDiscapacidad.length; k++) {
                        if ($scope.properties.catVivesEstadoDiscapacidad[k].descripcion == $scope.jsonCsv[i].catVivesEstadoDiscapacidad) {
                            $scope.persistenceIdVivesEstadoDiscapacidad = $scope.properties.catVivesEstadoDiscapacidad[k].persistenceId_string;
                        }
                    }

                    //For catProblemaSaludAtencionContinua
                    for (var k = 0; k < $scope.properties.catProblemaSaludAtencionContinua.length; k++) {
                        if ($scope.properties.catProblemaSaludAtencionContinua[k].descripcion == $scope.jsonCsv[i].catProblemaSaludAtencionContinua) {
                            $scope.persistenceIdProblemaSaludAtencionContinua = $scope.properties.catProblemaSaludAtencionContinua[k].persistenceId_string;
                        }
                    }

                    //For catPersonaSaludable
                    for (var k = 0; k < $scope.properties.catPersonaSaludable.length; k++) {
                        if ($scope.properties.catPersonaSaludable[k].descripcion == $scope.jsonCsv[i].catPersonaSaludable) {
                            $scope.persistenceIdPersonaSaludable = $scope.properties.catPersonaSaludable[k].persistenceId_string;
                        }
                    }

                    //For catFamiliarTeLlevasMejor
                    for (var k = 0; k < $scope.properties.catFamiliarTeLlevasMejor.length; k++) {
                        if ($scope.properties.catFamiliarTeLlevasMejor[k].descripcion == $scope.jsonCsv[i].catFamiliarTeLlevasMejor) {
                            $scope.persistenceIdFamiliarTeLlevasMejor = $scope.properties.catFamiliarTeLlevasMejor[k].persistenceId_string;
                        }
                    }

                    //For catRelacionHermanos
                    for (var k = 0; k < $scope.properties.catRelacionHermanos.length; k++) {
                        if ($scope.properties.catRelacionHermanos[k].descripcion == $scope.jsonCsv[i].catRelacionHermanos) {
                            $scope.persistenceIdRelacionHermanos = $scope.properties.catRelacionHermanos[k].persistenceId_string;
                        }
                    }
                    //For catPracticasDeporte
                    for (var k = 0; k < $scope.properties.catPracticasDeporte.length; k++) {
                        if ($scope.properties.catPracticasDeporte[k].descripcion == $scope.jsonCsv[i].catPracticasDeporte) {
                            $scope.persistenceIdPracticasDeporte = $scope.properties.catPracticasDeporte[k].persistenceId_string;
                        }
                    }
                    //For catParticipasGrupoSocial
                    for (var k = 0; k < $scope.properties.catParticipasGrupoSocial.length; k++) {
                        if ($scope.properties.catParticipasGrupoSocial[k].descripcion == $scope.jsonCsv[i].catParticipasGrupoSocial) {
                            $scope.persistenceIdParticipasGrupoSocial = $scope.properties.catParticipasGrupoSocial[k].persistenceId_string;
                        }
                    }
                    //For catTeGustaLeer
                    for (var k = 0; k < $scope.properties.catTeGustaLeer.length; k++) {
                        if ($scope.properties.catTeGustaLeer[k].descripcion == $scope.jsonCsv[i].catTeGustaLeer) {
                            $scope.persistenceIdTeGustaLeer = $scope.properties.catTeGustaLeer[k].persistenceId_string;
                        }
                    }
                    //For catJefeOrganizacionSocial
                    for (var k = 0; k < $scope.properties.catJefeOrganizacionSocial.length; k++) {
                        if ($scope.properties.catJefeOrganizacionSocial[k].descripcion == $scope.jsonCsv[i].catParticipasGrupoSocial) {
                            $scope.persistenceIdJefeOrganizacionSocial = $scope.properties.catJefeOrganizacionSocial[k].persistenceId_string;
                        }
                    }
                    //For discapacidades
                    for (var k = 0; k < $scope.properties.discapacidades.length; k++) {
                        
                        if ($scope.properties.discapacidades[k].descripcion == $scope.jsonCsv[i].discapacidades) {
                            $scope.lstDiscapacidades.push({
                                "persistenceId_string": $scope.properties.discapacidades[k].persistenceId_string
                            })
                        }
                    }
                    //For problemasSalud
                    for (var k = 0; k < $scope.properties.problemasSalud.length; k++) {
                        if ($scope.properties.problemasSalud[k].descripcion == $scope.jsonCsv[i].problemasSalud) {
                            $scope.lstProblemasSalud.push({
                                "persistenceId_string": $scope.properties.problemasSalud[k].persistenceId_string
                            })
                        }
                    }
                    //For deportes
                    for (var k = 0; k < $scope.properties.deportes.length; k++) {
                        if ($scope.properties.deportes[k].descripcion == $scope.jsonCsv[i].deportes) {
                            $scope.lstDeportes.push({
                                "persistenceId_string": $scope.properties.deportes[k].persistenceId_string
                            })
                        }
                    }
                    //For lecturas
                    for (var k = 0; k < $scope.properties.lecturas.length; k++) {
                        if ($scope.properties.lecturas[k].descripcion == $scope.jsonCsv[i].lecturas) {
                            $scope.lstLectura.push({
                                "persistenceId_string": $scope.properties.lecturas[k].persistenceId_string
                            })
                        }
                    }
                    //For pasatiempos
                    for (var k = 0; k < $scope.properties.pasatiempos.length; k++) {
                        if ($scope.properties.pasatiempos[k].descripcion == $scope.jsonCsv[i].pasatiempos) {
                            $scope.persistenceIdpasatiempos = $scope.properties.pasatiempos[k].persistenceId_string;
                            $scope.lstPasatiempos.push({
                                "persistenceId_string": $scope.properties.pasatiempos[k].persistenceId_string
                            })
                        }
                    }
                    //For catPlaticasProblemasPadre
                    for (var k = 0; k < $scope.properties.catPlaticasProblemasPadre.length; k++) {
                        if ($scope.properties.catPlaticasProblemasPadre[k].descripcion == $scope.jsonCsv[i].catPlaticasProblemasPadre) {
                            $scope.persistenceIdPlaticasProblemasPadre = $scope.properties.catPlaticasProblemasPadre[k].persistenceId_string;
                        }
                    }
                    //For catPlaticasProblemasMadre
                    for (var k = 0; k < $scope.properties.catPlaticasProblemasMadre.length; k++) {
                        if ($scope.properties.catPlaticasProblemasMadre[k].descripcion == $scope.jsonCsv[i].catPlaticasProblemasMadre) {
                            $scope.persistenceIdPlaticasProblemasMadre = $scope.properties.catPlaticasProblemasMadre[k].persistenceId_string;
                        }
                    }
                    //For catReligion
                    for (var k = 0; k < $scope.properties.catReligion.length; k++) {
                        if ($scope.properties.catReligion[k].descripcion == $scope.jsonCsv[i].catReligion) {
                            $scope.persistenceIdReligion = $scope.properties.catReligion[k].persistenceId_string;
                        }
                    }
                    //For catRecibirAtencionEspiritual
                    for (var k = 0; k < $scope.properties.catRecibirAtencionEspiritual.length; k++) {
                        if ($scope.properties.catRecibirAtencionEspiritual[k].descripcion == $scope.jsonCsv[i].catRecibirAtencionEspiritual) {
                            $scope.persistenceIdRecibirAtencionEspiritual = $scope.properties.catRecibirAtencionEspiritual[k].persistenceId_string;
                        }
                    }
                    //For catRegnumChris
                    for (var k = 0; k < $scope.properties.catRegnumChris.length; k++) {
                        if ($scope.properties.catRegnumChris[k].descripcion == $scope.jsonCsv[i].catRegnumChris) {
                            $scope.persistenceIdRegnumChris = $scope.properties.catRegnumChris[k].persistenceId_string;
                        }
                    }
                    //For catPracticasReligion
                    for (var k = 0; k < $scope.properties.catPracticasReligion.length; k++) {
                        if ($scope.properties.catPracticasReligion[k].descripcion == $scope.jsonCsv[i].catPracticasReligion) {
                            $scope.persistenceIdPracticasReligion = $scope.properties.catPracticasReligion[k].persistenceId_string;
                        }
                    }
                    //For catAspectoDesagradaReligion
                    for (var k = 0; k < $scope.properties.catAspectoDesagradaReligion.length; k++) {
                        if ($scope.properties.catAspectoDesagradaReligion[k].descripcion == $scope.jsonCsv[i].catAspectoDesagradaReligion) {
                            $scope.persistenceIdAspectoDesagradaReligion = $scope.properties.catAspectoDesagradaReligion[k].persistenceId_string;
                        }
                    }
                    //For catAreaLaboralDeInteres
                    for (var k = 0; k < $scope.properties.catAreaLaboralDeInteres.length; k++) {
                        if ($scope.properties.catAreaLaboralDeInteres[k].descripcion == $scope.jsonCsv[i].catAreaLaboralDeInteres) {
                            $scope.persistenceIdAreaLaboralDeInteres = $scope.properties.catAreaLaboralDeInteres[k].persistenceId_string;
                        }
                    }
                    //For catPadresDeAcuerdo
                    for (var k = 0; k < $scope.properties.catPadresDeAcuerdo.length; k++) {
                        if ($scope.properties.catPadresDeAcuerdo[k].descripcion == $scope.jsonCsv[i].catPadresDeAcuerdo) {
                            $scope.persistenceIdPadresDeAcuerdo = $scope.properties.catPadresDeAcuerdo[k].persistenceId_string;
                        }
                    }
                    //For informacionCarrera
                    for (var k = 0; k < $scope.properties.informacionCarrera.length; k++) {
                        if ($scope.properties.informacionCarrera[k].descripcion == $scope.jsonCsv[i].informacionCarrera) {
                            $scope.lstInfoCarrera.push({
                                "persistenceId_string": $scope.properties.informacionCarrera[k].persistenceId_string
                            })
                        }
                    }
                    //For catOrientacionVocacional
                    for (var k = 0; k < $scope.properties.catOrientacionVocacional.length; k++) {
                        if ($scope.properties.catOrientacionVocacional[k].descripcion == $scope.jsonCsv[i].catOrientacionVocacional) {
                            $scope.persistenceIdOrientacionVocacional = $scope.properties.catOrientacionVocacional[k].persistenceId_string;
                        }
                    }
                    //For catPersonaIntervinoEleccion
                    for (var k = 0; k < $scope.properties.catPersonaIntervinoEleccion.length; k++) {
                        if ($scope.properties.catPersonaIntervinoEleccion[k].descripcion == $scope.jsonCsv[i].catPersonaIntervinoEleccion) {
                            $scope.persistenceIdPersonaIntervinoEleccion = $scope.properties.catPersonaIntervinoEleccion[k].persistenceId_string;
                        }
                    }
                    //For catYaResolvisteElProblema
                    for (var k = 0; k < $scope.properties.catYaResolvisteElProblema.length; k++) {
                        if ($scope.properties.catYaResolvisteElProblema[k].descripcion == $scope.jsonCsv[i].catYaResolvisteElProblema) {
                            $scope.persistenceIdYaResolvisteElProblema = $scope.properties.catYaResolvisteElProblema[k].persistenceId_string;
                        }
                    }
                    //For cualidades
                    for (var k = 0; k < $scope.properties.cualidades.length; k++) {
                        if ($scope.properties.cualidades[k].descripcion == $scope.jsonCsv[i].cualidades) {
                            $scope.persistenceIdcualidades = $scope.properties.cualidades[k].persistenceId_string;
                            $scope.lstCualidades.push({
                                "persistenceId_string": $scope.properties.cualidades[k].persistenceId_string
                            })
                        }


                    }
                    //For defectos
                    for (var k = 0; k < $scope.properties.defectos.length; k++) {
                        if ($scope.properties.defectos[k].descripcion == $scope.jsonCsv[i].defectos) {
                            $scope.persistenceIddefectos = $scope.properties.defectos[k].persistenceId_string;
                        }
                        $scope.lstDefectos.push({
                            "persistenceId_string": $scope.properties.defectos[k].persistenceId_string
                        })
                    }
                    //For catFamiliarEgresadoAnahuac
                    for (var k = 0; k < $scope.properties.catFamiliarEgresadoAnahuac.length; k++) {
                        if ($scope.properties.catFamiliarEgresadoAnahuac[k].descripcion == $scope.jsonCsv[i].catFamiliarEgresadoAnahuac) {
                            $scope.persistenceIdFamiliarEgresadoAnahuac = $scope.properties.catFamiliarEgresadoAnahuac[k].persistenceId_string;
                        }
                    }

                    //Setea datos del csv a la lista $scope.properties.formOutput.autodescripcionInput
                    $scope.properties.formOutputAutoDesc.autodescripcionInput.push({

                        "caseId": $scope.jsonCsv[i].idAspirante,
                        "catPadreVive": $scope.persistenceIdVivePadre == null ? null : {
                            "persistenceId_string": $scope.persistenceIdVivePadre
                        },
                        "anoMuertePadre": $scope.jsonCsv[i].anoMuertePadre,
                        "catMadreVive": $scope.persistenceIdViveMadre == null ? null : {
                            "persistenceId_string": $scope.persistenceIdViveMadre
                        },
                        "anoMuerteMadre": $scope.jsonCsv[i].anoMuerteMadre,
                        "catConQuienVives": $scope.persistenceIdConQuienVives == null ? null : {
                            "persistenceId_string": $scope.persistenceIdConQuienVives
                        },
                        "catEstadoCivilPadres": $scope.persistenceIdEstadoCivilPadres == null ? null : {
                            "persistenceId_string": $scope.persistenceIdEstadoCivilPadres
                        },
                        "otroEstadoCivilPadres": $scope.jsonCsv[i].otroEstadoCivilPadres,
                        "materiasTeGustan": $scope.lstMatGustan,
                        "materiasNoTeGustan": $scope.lstMatNoGustan,
                        "materiasCalifAltas": $scope.lstMatCalifAlt,
                        "materiasCalifBajas": $scope.lstMatCalifBaj,
                        "catEstudiadoExtranjero": $scope.persistenceIdEstudiadoExtranjero == null ? null : {
                            "persistenceId_string": $scope.persistenceIdEstudiadoExtranjero
                        },
                        "paisEstudiasteExtranjero": $scope.persistenceIdPaisEstudiasteExtranjero == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPaisEstudiasteExtranjero
                        },
                        "tiempoEstudiasteExtranjero": $scope.persistenceIdtiempoEstudiasteExtranjero == null ? null : {
                            "persistenceId_string": $scope.persistenceIdtiempoEstudiasteExtranjero
                        },
                        "catAreaBachillerato": $scope.persistenceIdAreaBachillerato == null ? null : {
                            "persistenceId_string": $scope.persistenceIdAreaBachillerato
                        },
                        "catHasPresentadoExamenExtraordinario": $scope.persistenceIdHasPresentadoExamenExtraordinario == null ? null : {
                            "persistenceId_string": $scope.persistenceIdHasPresentadoExamenExtraordinario
                        },
                        "cualExamenExtraPresentaste": $scope.jsonCsv[i].cualExamenExtraPresentaste,
                        "catHasReprobado": $scope.persistenceIdHasReprobado == null ? null : {
                            "persistenceId_string": $scope.persistenceIdHasReprobado
                        },
                        "motivoExamenExtraordinario": $scope.jsonCsv[i].motivoExamenExtraordinario,
                        "periodoReprobaste": $scope.jsonCsv[i].periodoReprobaste,
                        "motivoReprobaste": $scope.jsonCsv[i].motivoReprobaste,
                        "catInscritoOtraUniversidad": $scope.persistenceIdInscritoOtraUniversidad == null ? null : {
                            "persistenceId_string": $scope.persistenceIdInscritoOtraUniversidad
                        },
                        "catHasTenidoTrabajo": $scope.persistenceIdHasTenidoTrabajo == null ? null : {
                            "persistenceId_string": $scope.persistenceIdHasTenidoTrabajo
                        },
                        "empresaTrabajaste": $scope.jsonCsv[i].empresaTrabajaste,
                        "organizacionHasSidoJefe": $scope.jsonCsv[i].organizacionHasSidoJefe,
                        "puestoTrabajo": $scope.jsonCsv[i].puestoTrabajo,
                        "catExperienciaAyudaCarrera": $scope.persistenceIdExperienciaAyudaCarrera == null ? null : {
                            "persistenceId_string": $scope.persistenceIdExperienciaAyudaCarrera
                        },
                        "motivoTrabajoAyudaElegirCarrera": $scope.jsonCsv[i].motivoTrabajoAyudaElegirCarrera,
                        "catActualnenteTrabajas": $scope.persistenceIdActualnenteTrabajas == null ? null : {
                            "persistenceId_string": $scope.persistenceIdActualnenteTrabajas
                        },
                        "catMotivoTrabajas": $scope.persistenceIdMotivoTrabajas == null ? null : {
                            "persistenceId_string": $scope.persistenceIdMotivoTrabajas
                        },
                        "empresaTrabajas": $scope.jsonCsv[i].empresaTrabajas,
                        "catGiroEmpresa": $scope.persistenceIdGiroEmpresa == null ? null : {
                            "persistenceId_string": $scope.persistenceIdGiroEmpresa
                        },
                        "catHaceCuantoTiempoTrabajas": $scope.persistenceIdHaceCuantoTiempoTrabajas == null ? null : {
                            "persistenceId_string": $scope.persistenceIdHaceCuantoTiempoTrabajas
                        },
                        "catHorarioTrabajo": $scope.persistenceIdHorarioTrabajo == null ? null : {
                            "persistenceId_string": $scope.persistenceIdHorarioTrabajo
                        },
                        "trabajo": $scope.jsonCsv[i].trabajo,
                        "queCambiariasDeTuFamilia": $scope.jsonCsv[i].queCambiariasDeTuFamilia,
                        "comoDescribesTuFamilia": $scope.jsonCsv[i].comoDescribesTuFamilia,
                        "admirasPersonalidadPadre": $scope.jsonCsv[i].admirasPersonalidadPadre,
                        "defectosObservasPadre": $scope.jsonCsv[i].defectosObservasPadre,
                        "padreAyudaProblemas": $scope.jsonCsv[i].padreAyudaProblemas,
                        "admirasPersonalidadMadre": $scope.jsonCsv[i].admirasPersonalidadMadre,
                        "defectosObservasMadre": $scope.jsonCsv[i].defectosObservasMadre,
                        "madreAyudaProblemas": $scope.jsonCsv[i].madreAyudaProblemas,
                        "organizacionParticipas": $scope.jsonCsv[i].organizacionParticipas,
                        "catVivesEstadoDiscapacidad": $scope.persistenceIdVivesEstadoDiscapacidad == null ? null : {
                            "persistenceId_string": $scope.persistenceIdVivesEstadoDiscapacidad
                        },
                        "catProblemaSaludAtencionContinua": $scope.persistenceIdProblemaSaludAtencionContinua == null ? null : {
                            "persistenceId_string": $scope.persistenceIdProblemaSaludAtencionContinua
                        },
                        "catPersonaSaludable": $scope.persistenceIdPersonaSaludable == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPersonaSaludable
                        },
                        "catFamiliarTeLlevasMejor": $scope.persistenceIdFamiliarTeLlevasMejor == null ? null : {
                            "persistenceId_string": $scope.persistenceIdFamiliarTeLlevasMejor
                        },
                        "catRelacionHermanos": $scope.persistenceIdRelacionHermanos == null ? null : {
                            "persistenceId_string": $scope.persistenceIdRelacionHermanos
                        },
                        "catPracticasDeporte": $scope.persistenceIdPracticasDeporte == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPracticasDeporte
                        },
                        "catParticipasGrupoSocial": $scope.persistenceIdParticipasGrupoSocial == null ? null : {
                            "persistenceId_string": $scope.persistenceIdParticipasGrupoSocial
                        },
                        "catTeGustaLeer": $scope.persistenceIdTeGustaLeer == null ? null : {
                            "persistenceId_string": $scope.persistenceIdTeGustaLeer
                        },
                        "catJefeOrganizacionSocial": $scope.persistenceIdJefeOrganizacionSocial == null ? null : {
                            "persistenceId_string": $scope.persistenceIdJefeOrganizacionSocial
                        },
                        "discapacidades": $scope.lstDiscapacidades,
                        "problemasSalud": $scope.lstProblemasSalud,
                        "deportes": $scope.lstDeportes,
                        "lecturas": $scope.lstLectura,
                        "pasatiempos": $scope.lstPasatiempos,
                        "catPlaticasProblemasPadre": $scope.persistenceIdPlaticasProblemasPadre == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPlaticasProblemasPadre
                        },
                        "catPlaticasProblemasMadre": $scope.persistenceIdPlaticasProblemasMadre == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPlaticasProblemasMadre
                        },
                        "valoresApordatoReligion": $scope.jsonCsv[i].valoresApordatoReligion,
                        "asprctosNoGustanReligion": $scope.jsonCsv[i].asprctosNoGustanReligion,
                        "motivoAspectosNoGustanReligion": $scope.jsonCsv[i].motivoAspectosNoGustanReligion,
                        "caracteristicasExitoCarrera": $scope.jsonCsv[i].caracteristicasExitoCarrera,
                        "profesionalComoTeVes": $scope.jsonCsv[i].profesionalComoTeVes,
                        "motivoElegisteCarrera": $scope.jsonCsv[i].motivoElegisteCarrera,
                        "expectativasCarrera": $scope.jsonCsv[i].expectativasCarrera,
                        "motivoPadresNoAcuerdo": $scope.jsonCsv[i].motivoPadresNoAcuerdo,
                        "catReligion": $scope.persistenceIdReligion == null ? null : {
                            "persistenceId_string": $scope.persistenceIdReligion
                        },
                        "catRecibirAtencionEspiritual": $scope.persistenceIdRecibirAtencionEspiritual == null ? null : {
                            "persistenceId_string": $scope.persistenceIdRecibirAtencionEspiritual
                        },
                        "catRegnumChris": $scope.persistenceIdRegnumChris == null ? null : {
                            "persistenceId_string": $scope.persistenceIdRegnumChris
                        },
                        "catPracticasReligion": $scope.persistenceIdPracticasReligion == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPracticasReligion
                        },
                        "catAspectoDesagradaReligion": $scope.persistenceIdAspectoDesagradaReligion == null ? null : {
                            "persistenceId_string": $scope.persistenceIdAspectoDesagradaReligion
                        },
                        "catAreaLaboralDeInteres": $scope.persistenceIdAreaLaboralDeInteres == null ? null : {
                            "persistenceId_string": $scope.persistenceIdAreaLaboralDeInteres
                        },
                        "catPadresDeAcuerdo": $scope.persistenceIdPadresDeAcuerdo == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPadresDeAcuerdo
                        },
                        "informacionCarrera": $scope.lstInfoCarrera,
                        "lugarYTipoOrientacion": $scope.jsonCsv[i].lugarYTipoOrientacion,
                        "personaIntervinoEleccion": $scope.jsonCsv[i].personaIntervinoEleccion,
                        "catOrientacionVocacional": $scope.persistenceIdOrientacionVocacional == null ? null : {
                            "persistenceId_string": $scope.persistenceIdOrientacionVocacional
                        },
                        "catPersonaIntervinoEleccion": $scope.persistenceIdPersonaIntervinoEleccion == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPersonaIntervinoEleccion
                        },
                        "mayorProblemaEnfrentado": $scope.jsonCsv[i].mayorProblemaEnfrentado,
                        "comoResolvisteProblema": $scope.jsonCsv[i].comoResolvisteProblema,
                        "metasCortoPlazo": $scope.jsonCsv[i].metasCortoPlazo,
                        "metasMedianoPlazo": $scope.jsonCsv[i].metasMedianoPlazo,
                        "metasLargoPlazo": $scope.jsonCsv[i].metasLargoPlazo,
                        "comoTeDescribenTusAmigos": $scope.jsonCsv[i].comoTeDescribenTusAmigos,
                        "queCambiariasDeTi": $scope.jsonCsv[i].queCambiariasDeTi,
                        "detallesPersonalidad": $scope.jsonCsv[i].detallesPersonalidad,
                        "catYaResolvisteElProblema": $scope.persistenceIdYaResolvisteElProblema == null ? null : {
                            "persistenceId_string": $scope.persistenceIdYaResolvisteElProblema
                        },
                        "cualidades": $scope.lstCualidades,
                        "defectos": $scope.lstDefectos,
                        "pageIndex": "",
                        "catFamiliarEgresadoAnahuac": $scope.persistenceIdFamiliarEgresadoAnahuac == null ? null : {
                            "persistenceId_string": $scope.persistenceIdFamiliarEgresadoAnahuac
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

    /*================FIN MODULO AUTODESCRIPCION============================*/


    /*=======MODULO HERMANOS=========*/
    var inputHermanos = document.getElementById("csvHermanos"),
        readFile = function() {
            $scope.jsonCsv = null;
            
            var readerHermanos = new FileReader();
            readerHermanos.onload = function() {
                document.getElementById('outHermanos').innerHTML = readerHermanos.result;

                var dataHermano = readerHermanos.result;
                var workbookHermano = XLSX.read(dataHermano, {
                    type: 'binary'
                });

                workbookHermano.SheetNames.forEach(function(sheetName) {
                    // Here is your object

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbookHermano.Sheets[sheetName]);
                    var json_object = JSON.stringify(XL_row_object);
                    $scope.jsonCsv = JSON.parse(json_object);

                })

                for (var i = 0; i < $scope.jsonCsv.length; i++) {

                    $scope.properties.formOutputAutoDesc.lstHermanosInput.push({
                        "persistenceId_string": "",
                        "caseId": $scope.jsonCsv[i].idAspirante,
                        "nombres": $scope.jsonCsv[i].nombres,
                        "apellidos": $scope.jsonCsv[i].apellidos,
                        "fechaNacimiento": $scope.jsonCsv[i].fechaNacimiento,
                        "isEstudia": $scope.jsonCsv[i].isEstudia == "true" ? true : false,
                        "escuelaEstudia": $scope.jsonCsv[i].escuelaEstudia,
                        "isTrabaja": $scope.jsonCsv[i].isTrabaja == "true" ? true : false,
                        "empresaTrabaja": $scope.jsonCsv[i].empresaTrabaja
                    });
                }
                $scope.startProcess();
                $scope.properties.selectedIndex = $scope.properties.selectedIndex + 1;

                $scope.$apply();
            };
            readerHermanos.readAsBinaryString(inputHermanos.files[0]);
        };
    inputHermanos.addEventListener('change', readFile);
    /*=======FIN MODULO HERMANOS=========*/


    /*=======MODULO GRUPOS SOCIALES=========*/
    var inputGrupo = document.getElementById("csvGrupo"),
        readFile = function() {
            
            $scope.jsonCsv = null;
            var readerGrupo = new FileReader();
            readerGrupo.onload = function() {
                document.getElementById('outGrupo').innerHTML = readerGrupo.result;
                var dataGrupo = readerGrupo.result;
                var workbookGrupo = XLSX.read(dataGrupo, {
                    type: 'binary'
                });

                workbookGrupo.SheetNames.forEach(function(sheetName) {
                    // Here is your object

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbookGrupo.Sheets[sheetName]);
                    var json_object = JSON.stringify(XL_row_object);
                    $scope.jsonCsv = JSON.parse(json_object);

                })

                for (var i = 0; i < $scope.jsonCsv.length; i++) {
                    $scope.persistenceIdTipoOrganizacion = null;
                    $scope.persistenceIdPertenecesOrganizacion = null;
                    $scope.persistenceIdAfiliacion = null;
                    $scope.persistenceIdCuantoTiempo = null;

                    //For catTipoOrganizacion
                    for (var k = 0; k < $scope.properties.catTipoOrganizacion.length; k++) {
                        if ($scope.properties.catTipoOrganizacion[k].descripcion == $scope.jsonCsv[i].catTipoOrganizacion) {
                            $scope.persistenceIdTipoOrganizacion = $scope.properties.catTipoOrganizacion[k].persistenceId_string;
                        }
                    }

                    //For catPertenecesOrganizacion
                    for (var k = 0; k < $scope.properties.catPertenecesOrganizacion.length; k++) {
                        if ($scope.properties.catPertenecesOrganizacion[k].descripcion == $scope.jsonCsv[i].catPertenecesOrganizacion) {
                            $scope.persistenceIdPertenecesOrganizacion = $scope.properties.catPertenecesOrganizacion[k].persistenceId_string;
                        }
                    }

                    //For catAfiliacion
                    for (var k = 0; k < $scope.properties.catAfiliacion.length; k++) {
                        if ($scope.properties.catAfiliacion[k].descripcion == $scope.jsonCsv[i].catAfiliacion) {
                            $scope.persistenceIdAfiliacion = $scope.properties.catAfiliacion[k].persistenceId_string;
                        }
                    }

                    //For catCuantoTiempo
                    for (var k = 0; k < $scope.properties.catHaceCuantoTiempoTrabajas.length; k++) {
                        if ($scope.properties.catHaceCuantoTiempoTrabajas[k].descripcion == $scope.jsonCsv[i].catCuantoTiempo) {
                            $scope.persistenceIdCuantoTiempo = $scope.properties.catHaceCuantoTiempoTrabajas[k].persistenceId_string;
                        }
                    }

                    $scope.properties.formOutputAutoDesc.lstGruposSocialesInput.push({
                        "persistenceId_string": "",
                        "caseId": $scope.jsonCsv[i].idAspirante,
                        "catTipoOrganizacion": $scope.persistenceIdTipoOrganizacion == null ? null : {
                            "persistenceId_string": $scope.persistenceIdTipoOrganizacion
                        },
                        "catPertenecesOrganizacion": $scope.persistenceIdPertenecesOrganizacion == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPertenecesOrganizacion
                        },
                        "nombre": $scope.jsonCsv[i].nombre,
                        "catAfiliacion": $scope.persistenceIdAfiliacion == null ? null : {
                            "persistenceId_string": $scope.persistenceIdAfiliacion
                        },
                        "catCuantoTiempo": $scope.persistenceIdCuantoTiempo == null ? null : {
                            "persistenceId_string": $scope.persistenceIdCuantoTiempo
                        }
                    });
                }
                $scope.startProcess();
                $scope.properties.selectedIndex = $scope.properties.selectedIndex + 1;

                $scope.$apply();
            };
            readerGrupo.readAsBinaryString(inputGrupo.files[0]);
        };
    inputGrupo.addEventListener('change', readFile);
    /*=======FIN MODULO GRUPOS SOCIALES=========*/

    /*=======MODULO IDIOMAS=========*/
    var inputIdioma = document.getElementById("csvIdioma"),
        readFile = function() {
            
            $scope.jsonCsv = null;
            var readerIdioma = new FileReader();
            readerIdioma.onload = function() {
                document.getElementById('outIdioma').innerHTML = readerIdioma.result;
                var dataIdioma = readerIdioma.result;
                var workbookIdioma = XLSX.read(dataIdioma, {
                    type: 'binary'
                });

                workbookIdioma.SheetNames.forEach(function(sheetName) {
                    // Here is your object

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbookIdioma.Sheets[sheetName]);
                    var json_object = JSON.stringify(XL_row_object);
                    $scope.jsonCsv = JSON.parse(json_object);

                })

                for (var i = 0; i < $scope.jsonCsv.length; i++) {
                    $scope.persistenceIdIdioma = null;
                    $scope.persistenceIdConversacion = null;
                    $scope.persistenceIdEscritura = null;
                    $scope.persistenceIdTraduccion = null;
                    $scope.persistenceIdLectura = null;

                    //For catIdioma
                    for (var k = 0; k < $scope.properties.catIdioma.length; k++) {
                        if ($scope.properties.catIdioma[k].descripcion == $scope.jsonCsv[i].catIdioma) {
                            $scope.persistenceIdIdioma = $scope.properties.catIdioma[k].persistenceId_string;
                        }
                    }

                    //For catConversacion
                    for (var k = 0; k < $scope.properties.catConversacion.length; k++) {
                        if ($scope.properties.catConversacion[k].descripcion == $scope.jsonCsv[i].catConversacion) {
                            $scope.persistenceIdConversacion = $scope.properties.catConversacion[k].persistenceId_string;
                        }
                    }

                    //For catEscritura
                    for (var k = 0; k < $scope.properties.catEscritura.length; k++) {
                        if ($scope.properties.catEscritura[k].descripcion == $scope.jsonCsv[i].catEscritura) {
                            $scope.persistenceIdEscritura = $scope.properties.catEscritura[k].persistenceId_string;
                        }
                    }

                    //For catTraduccion
                    for (var k = 0; k < $scope.properties.catTraduccion.length; k++) {
                        if ($scope.properties.catTraduccion[k].descripcion == $scope.jsonCsv[i].catTraduccion) {
                            $scope.persistenceIdTraduccion = $scope.properties.catTraduccion[k].persistenceId_string;
                        }
                    }

                    //For catLectura
                    for (var k = 0; k < $scope.properties.catLectura.length; k++) {
                        if ($scope.properties.catLectura[k].descripcion == $scope.jsonCsv[i].catLectura) {
                            $scope.persistenceIdLectura = $scope.properties.catLectura[k].persistenceId_string;
                        }
                    }

                    $scope.properties.formOutputAutoDesc.lstIdiomasInput.push({
                        "persistenceId_string": "",
                        "caseId": $scope.jsonCsv[i].idAspirante,
                        "catIdioma": $scope.persistenceIdIdioma == null ? null : {
                            "persistenceId_string": $scope.persistenceIdIdioma
                        },
                        "catConversacion": $scope.persistenceIdConversacion == null ? null : {
                            "persistenceId_string": $scope.persistenceIdConversacion
                        },
                        "catEscritura": $scope.persistenceIdEscritura == null ? null : {
                            "persistenceId_string": $scope.persistenceIdEscritura
                        },
                        "catTraduccion": $scope.persistenceIdTraduccion == null ? null : {
                            "persistenceId_string": $scope.persistenceIdTraduccion
                        },
                        "catLectura": $scope.persistenceIdLectura == null ? null : {
                            "persistenceId_string": $scope.persistenceIdLectura
                        },
                        "otroIdioma": $scope.jsonCsv[i].otroIdioma
                    });
                }
                $scope.startProcess();
                $scope.properties.selectedIndex = $scope.properties.selectedIndex + 1;

                $scope.$apply();
            };
            readerIdioma.readAsBinaryString(inputIdioma.files[0]);
        };
    inputIdioma.addEventListener('change', readFile);
    /*=======FIN MODULO IDIOMAS=========*/


    /*=======MODULO TERAPIA=========*/
    var inputTerapia = document.getElementById("csvTerapia"),
        readFile = function() {
            $scope.jsonCsv = null;
            
            var readerTerapia = new FileReader();
            readerTerapia.onload = function() {
                document.getElementById('outTerapia').innerHTML = readerTerapia.result;
                var dataTerapia = readerTerapia.result;
                var workbookTerapia = XLSX.read(dataTerapia, {
                    type: 'binary'
                });

                workbookTerapia.SheetNames.forEach(function(sheetName) {
                    // Here is your object

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbookTerapia.Sheets[sheetName]);
                    var json_object = JSON.stringify(XL_row_object);
                    $scope.jsonCsv = JSON.parse(json_object);

                })

                for (var i = 0; i < $scope.jsonCsv.length; i++) {
                    $scope.persistenceIdTipoTerapia = null;
                    $scope.persistenceIdRecibidoTerapia = null;

                    //For catTipoTerapia
                    for (var k = 0; k < $scope.properties.catTipoTerapia.length; k++) {
                        if ($scope.properties.catTipoTerapia[k].descripcion == $scope.jsonCsv[i].catTipoTerapia) {
                            $scope.persistenceIdTipoTerapia = $scope.properties.catTipoTerapia[k].persistenceId_string;
                        }
                    }

                    //For catRecibidoTerapia
                    for (var k = 0; k < $scope.properties.catRecibidoTerapia.length; k++) {
                        if ($scope.properties.catRecibidoTerapia[k].descripcion == $scope.jsonCsv[i].catRecibidoTerapia) {
                            $scope.persistenceIdRecibidoTerapia = $scope.properties.catRecibidoTerapia[k].persistenceId_string;
                        }
                    }

                    $scope.properties.formOutputAutoDesc.lstTerapiaInput.push({
                        "persistenceId_string": "",
                        "caseId": $scope.jsonCsv[i].idAspirante,
                        "catTipoTerapia": $scope.persistenceIdTipoTerapia == null ? null : {
                            "persistenceId_string": $scope.persistenceIdTipoTerapia
                        },
                        "catRecibidoTerapia": $scope.persistenceIdRecibidoTerapia == null ? null : {
                            "persistenceId_string": $scope.persistenceIdRecibidoTerapia
                        },
                        "tipoTerapia": $scope.jsonCsv[i].tipoTerapia,
                        "cuantoTiempo": $scope.jsonCsv[i].cuantoTiempo,
                        "recibidoTerapiaString": $scope.jsonCsv[i].recibidoTerapiaString,
                        "otraTerapia": $scope.jsonCsv[i].otraTerapia
                    });
                }
                $scope.startProcess();
                $scope.properties.selectedIndex = $scope.properties.selectedIndex + 1;

                $scope.$apply();
            };
            readerTerapia.readAsBinaryString(inputTerapia.files[0]);
        };
    inputTerapia.addEventListener('change', readFile);
    /*=======FIN MODULO TERAPIA=========*/

    /*=======MODULO INFORMACION ESCOLAR=========*/
    var inputInfoEscolar = document.getElementById("csvInfoEscolar"),
        readFile = function() {
            
            $scope.jsonCsv = null;
            var readerInfoEscolar = new FileReader();
            readerInfoEscolar.onload = function() {
                document.getElementById('outInfoEscolar').innerHTML = readerInfoEscolar.result;
                var dataInfoEscolar = readerInfoEscolar.result;
                var workbookInfoEscolar = XLSX.read(dataInfoEscolar, {
                    type: 'binary'
                });

                workbookInfoEscolar.SheetNames.forEach(function(sheetName) {
                    // Here is your object

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbookInfoEscolar.Sheets[sheetName]);
                    var json_object = JSON.stringify(XL_row_object);
                    $scope.jsonCsv = JSON.parse(json_object);

                })

                for (var i = 0; i < $scope.jsonCsv.length; i++) {
                    $scope.persistenceIdgrado = null;
                    $scope.persistenceIdtipo = null;

                    //For grado
                    for (var k = 0; k < $scope.properties.catGradoAcademico.length; k++) {
                        if ($scope.properties.catGradoAcademico[k].descripcion == $scope.jsonCsv[i].grado) {
                            $scope.persistenceIdgradoAcademico = $scope.properties.catGradoAcademico[k].persistenceId_string;
                        }
                    }

                    //For escuela
                    for (var k = 0; k < $scope.properties.catBachilleratos.length; k++) {
                        if ($scope.properties.catBachilleratos[k].descripcion == $scope.jsonCsv[i].escuela) {
                            $scope.persistenceIdBachilleratos = $scope.properties.catBachilleratos[k].persistenceId_string;
                        }
                    }

                    //For catPais
                    for (var k = 0; k < $scope.properties.catPais.length; k++) {
                        if ($scope.properties.catPais[k].descripcion == $scope.jsonCsv[i].pais) {
                            $scope.persistenceIdPais = $scope.properties.catPais[k].persistenceId_string;
                        }
                    }

                    //For catEstado
                    for (var k = 0; k < $scope.properties.catEstado.length; k++) {
                        if ($scope.properties.catEstado[k].descripcion == $scope.jsonCsv[i].estado) {
                            $scope.persistenceIdEstado = $scope.properties.catEstado[k].persistenceId_string;
                        }
                    }

                    $scope.properties.formOutputAutoDesc.lstInformacionEscolarInput.push({
                        "caseId": $scope.jsonCsv[i].idAspirante,
                        "grado": $scope.persistenceIdgradoAcademico == null ? null : {
                            "persistenceId_string": $scope.persistenceIdgradoAcademico
                        },
                        "tipo": $scope.persistenceIdgradoAcademico == null ? null : {
                            "persistenceId_string": $scope.persistenceIdgradoAcademico
                        },
                        "escuela": $scope.persistenceIdBachilleratos == null ? null : {
                            "persistenceId_string": $scope.persistenceIdBachilleratos
                        },
                        "otraEscuela": $scope.jsonCsv[i].otraEscuela,
                        "pais": $scope.persistenceIdPais == null ? null : {
                            "persistenceId_string": $scope.persistenceIdPais
                        },
                        "estado": $scope.persistenceIdEstado == null ? null : {
                            "persistenceId_string": $scope.persistenceIdEstado
                        },
                        "ciudad": $scope.jsonCsv[i].ciudad,
                        "anoInicio": $scope.jsonCsv[i].anoInicio,
                        "anoFin": $scope.jsonCsv[i].anoFin,
                        "promedio": $scope.jsonCsv[i].promedio,
                        "estadoString": $scope.jsonCsv[i].estadoString
                    });
                }
                $scope.startProcess();
                $scope.properties.selectedIndex = $scope.properties.selectedIndex + 1;

                $scope.$apply();
            };
            readerInfoEscolar.readAsBinaryString(inputInfoEscolar.files[0]);
        };
    inputInfoEscolar.addEventListener('change', readFile);
    /*=======FIN INFORMACION ESCOLAR=========*/


    /*=======MODULO UNIVERSIDAD=========*/
    var inputUniversidad = document.getElementById("csvUniversidad"),
        readFile = function() {
            
            $scope.jsonCsv = null;
            var readerUniversidad = new FileReader();
            readerUniversidad.onload = function() {
                document.getElementById('outUniversidad').innerHTML = readerUniversidad.result;
                var dataUniversidad = readerUniversidad.result;
                var workbookUniversidad = XLSX.read(dataUniversidad, {
                    type: 'binary'
                });

                workbookUniversidad.SheetNames.forEach(function(sheetName) {
                    // Here is your object

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbookUniversidad.Sheets[sheetName]);
                    var json_object = JSON.stringify(XL_row_object);
                    $scope.jsonCsv = JSON.parse(json_object);

                })

                for (var i = 0; i < $scope.jsonCsv.length; i++) {

                    $scope.properties.formOutputAutoDesc.lstUniversidadesHasEstadoInput.push({
                        "persistenceId_string": "",
                        "caseId": $scope.jsonCsv[i].idAspirante,
                        "nombre": $scope.jsonCsv[i].nombre,
                        "anoInicio": $scope.jsonCsv[i].anoInicio,
                        "anoFin": $scope.jsonCsv[i].anoFin,
                        "carrera": $scope.jsonCsv[i].carrera,
                        "motivoSuspension": $scope.jsonCsv[i].motivoSuspension
                    });
                }
                $scope.startProcess();
                $scope.properties.selectedIndex = $scope.properties.selectedIndex + 1;

                $scope.$apply();
            };
            readerUniversidad.readAsBinaryString(inputUniversidad.files[0]);
        };
    inputUniversidad.addEventListener('change', readFile);
    /*=======FIN UNIVERSIDAD=========*/

    /*=======MODULO PARIENTE=========*/
    var inputPariente = document.getElementById("csvPariente"),
        readFile = function() {
            
            $scope.jsonCsv = null;
            var readerPariente = new FileReader();
            readerPariente.onload = function() {
                document.getElementById('outPariente').innerHTML = readerPariente.result;
                var dataPariente = readerPariente.result;
                var workbookPariente = XLSX.read(dataPariente, {
                    type: 'binary'
                });

                workbookPariente.SheetNames.forEach(function(sheetName) {
                    // Here is your object

                    var XL_row_object = XLSX.utils.sheet_to_row_object_array(workbookPariente.Sheets[sheetName]);
                    var json_object = JSON.stringify(XL_row_object);
                    $scope.jsonCsv = JSON.parse(json_object);

                })

                for (var i = 0; i < $scope.jsonCsv.length; i++) {
                    $scope.persistenceIdParentesco = null;
                    $scope.persistenceIdCampus = null;
                    $scope.persistenceIdDiploma = null;

                    //For catParentesco
                    for (var k = 0; k < $scope.properties.catParentesco.length; k++) {
                        if ($scope.properties.catParentesco[k].descripcion == $scope.jsonCsv[i].catParentesco) {
                            $scope.persistenceIdParentesco = $scope.properties.catParentesco[k].persistenceId_string;
                        }
                    }

                    //For catCampus
                    for (var k = 0; k < $scope.properties.catCampus.length; k++) {
                        if ($scope.properties.catCampus[k].descripcion == $scope.jsonCsv[i].catCampus) {
                            $scope.persistenceIdCampus = $scope.properties.catCampus[k].persistenceId_string;
                        }
                    }

                    //For catDiploma
                    for (var k = 0; k < $scope.properties.catDiploma.length; k++) {
                        if ($scope.properties.catDiploma[k].descripcion == $scope.jsonCsv[i].catDiploma) {
                            $scope.persistenceIdDiploma = $scope.properties.catDiploma[k].persistenceId_string;
                        }
                    }

                    $scope.properties.formOutputAutoDesc.lstParienteEgresadoAnahuacInput.push({
                        "persistenceId_string": "",
                        "caseId": $scope.jsonCsv[i].idAspirante,
                        "catParentesco": $scope.persistenceIdParentesco == null ? null : {
                            "persistenceId_string": $scope.persistenceIdParentesco
                        },
                        "nombre": $scope.jsonCsv[i].nombre,
                        "apellidos": $scope.jsonCsv[i].apellidos,
                        "correo": $scope.jsonCsv[i].correo,
                        "catCampus": $scope.persistenceIdCampus == null ? null : {
                            "persistenceId_string": $scope.persistenceIdCampus
                        },
                        "carrera": $scope.jsonCsv[i].carrera,
                        "catDiploma": $scope.persistenceIdDiploma == null ? null : {
                            "persistenceId_string": $scope.persistenceIdDiploma
                        }
                    });
                }
                $scope.startProcess();
                $scope.properties.selectedIndex = $scope.properties.selectedIndex + 1;

                $scope.$apply();
            };
            readerPariente.readAsBinaryString(inputPariente.files[0]);
        };
    inputPariente.addEventListener('change', readFile);
    /*=======FIN PARIENTE=========*/

    //INICIO ----------Instanciacion de proceso
    $scope.startProcess = function() {
        var id = 0;
        
        //Modulo Autodescricpion
        if ($scope.properties.selectedIndex == 0) {
            id = $scope.properties.procesoAutodescId[0].id;
            $scope.objDataToSend = {
                "autodescripcionInput": $scope.properties.formOutputAutoDesc.autodescripcionInput
            }
        }
        //Modulo Hermanos
        else if ($scope.properties.selectedIndex == 1) {
            id = $scope.properties.procesoHermanoId[0].id;
            $scope.objDataToSend = {
                "lstHermanosInput": $scope.properties.formOutputAutoDesc.lstHermanosInput
            }

        }
        //Modulo Grupos
        else if ($scope.properties.selectedIndex == 2) {
            id = $scope.properties.procesoGrupoId[0].id;
            $scope.objDataToSend = {
                "lstGruposSocialesInput": $scope.properties.formOutputAutoDesc.lstGruposSocialesInput
            }

        }
        //Modulo Idiomas
        else if ($scope.properties.selectedIndex == 3) {
            id = $scope.properties.procesoIdiomaId[0].id;
            $scope.objDataToSend = {
                "lstIdiomasInput": $scope.properties.formOutputAutoDesc.lstIdiomasInput
            }

        }
        //Modulo Terapia
        else if ($scope.properties.selectedIndex == 4) {
            id = $scope.properties.procesoTerapiaId[0].id;
            $scope.objDataToSend = {
                "lstTerapiaInput": $scope.properties.formOutputAutoDesc.lstTerapiaInput
            }

        }
        //Modulo Informacion Escolar
        else if ($scope.properties.selectedIndex == 5) {
            id = $scope.properties.procesoInfoEscolarId[0].id;
            $scope.objDataToSend = {
                "lstInformacionEscolarInput": $scope.properties.formOutputAutoDesc.lstInformacionEscolarInput
            }

        }
        //Modulo Universidad
        else if ($scope.properties.selectedIndex == 6) {
            id = $scope.properties.procesoUniversidadId[0].id;
            $scope.objDataToSend = {
                "lstUniversidadesHasEstadoInput": $scope.properties.formOutputAutoDesc.lstUniversidadesHasEstadoInput
            }

        }
        //Modulo Pariente
        else if ($scope.properties.selectedIndex == 7) {
            id = $scope.properties.procesoParienteId[0].id;
            $scope.objDataToSend = {
                "lstParienteEgresadoAnahuacInput": $scope.properties.formOutputAutoDesc.lstParienteEgresadoAnahuacInput
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