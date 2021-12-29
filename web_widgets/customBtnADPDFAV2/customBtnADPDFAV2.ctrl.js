        function PbButtonCtrl($scope, modalService, $http, blockUI, $q, $filter) {

          'use strict';
          
            var vm = this;
            
            $scope.date = new Date();
            $scope.fechaActual = ($filter('date')(Date.parse($scope.date), "dd/MMM/yyyy")).toString();
            $scope.FinEscuela = "";
            
            //GENERAR PDF - AdataR
            $scope.generatePDF = function() {
                var doc = new jspdf.jsPDF('p', 'mm', 'a4');
                var width = doc.internal.pageSize.getWidth();
                var height = doc.internal.pageSize.getHeight();
                var yValor = 0;
                var yvalor = 0;

                var fontText = 10;
                var fontTitle = 14;
                var fontSubTitle = 13;

                var margenPrimeraFila = 15;
                var margenSegundaFila =  135;
                var margenFilaIntermedia =  80;

                var respuestasPrimeraFila = 15;
                var respuestasSegundaFila = 135;
                var respuestasFilaIntermedia = 80;

                console.log("W "+width)
                console.log("H "+height)
                

                
                doc.addImage("widgets/customBtnADPDFAV2/assets/img/LogoRUA.png", "PNG", ((width / 2) + 35), ((height / 2) - 128), 60, 20);

                doc.setFontSize(fontTitle);
                doc.setFont(undefined, 'bold');
                doc.text('Cuestionario de autodescripción', margenPrimeraFila, (height / 2) - 116);
             
                //IMAGEN DE 
                doc.setFillColor(228, 212, 200);
                doc.rect(10, (height / 2) - 103, 190, 85, 'F');
                doc.addImage($scope.properties.urlFoto,"JPG", 160, ((height / 2)-82), 30, 40);


                //  ----------------------------------------- PRIMERA SECCIÓN  ----------------------------------------- 
                doc.setFontSize(fontSubTitle);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 93, 'Información de la solicitud');
                
                //Encabezados
                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 83, 'Nombre:');
                doc.text(margenPrimeraFila, (height / 2) - 71, 'ID:');
                doc.text(margenPrimeraFila, (height / 2) - 59, 'Correo electrónico:');
                doc.text(margenPrimeraFila, (height / 2) - 49, 'Residencia');
                doc.text(margenPrimeraFila, (height / 2) - 39, 'Periodo de ingreso:');

                //Respuestas
                doc.setFont(undefined, 'normal');
                doc.text($scope.properties.objectSolicitud.primerNombre + " " + $scope.properties.objectSolicitud.segundoNombre + " " + $scope.properties.objectSolicitud.apellidoPaterno + " " + $scope.properties.objectSolicitud.apellidoMaterno, respuestasPrimeraFila, (height / 2) - 76);
                doc.text($scope.properties.idBanner, respuestasPrimeraFila, (height / 2) - 66);
                doc.text($scope.properties.objectSolicitud.correoElectronico, respuestasPrimeraFila, (height / 2) - 55);
                doc.text($scope.properties.catResidencia.descripcion, respuestasPrimeraFila, (height / 2) - 45);
                doc.text($scope.properties.objectSolicitud.catPeriodo.descripcion, respuestasPrimeraFila, (height / 2) - 35);
            
                //  ----------------------------------------- SEGUNDA SECCIÓN  ----------------------------------------- 
              

                doc.setFontSize(fontTitle);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 12, 'Información familiar');

                //Encabezados
                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 2, '¿Quiénes conforman tu familia?:');
                
                //Respuestas
                doc.setFont(undefined, 'normal');
                doc.text($scope.properties.autodescripcionV2.comoEstaConformadaFamilia , respuestasPrimeraFila, (height / 2) + 5);


                //  ----------------------------------------- TERCERA SECCIÓN  ----------------------------------------- 
                doc.setFillColor(228, 212, 200);
                doc.rect(10, (height / 2) + 30, 190, 100, 'F');

                doc.setFontSize(fontTitle);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) + 35, 'Información escolar');

                //Encabezados
                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) + 45, 'Nombre:');
                doc.text(margenFilaIntermedia, (height / 2) + 45, 'Año inicio:');
                doc.text(margenSegundaFila, (height / 2) + 45, 'Año fin:');

                doc.text(margenPrimeraFila, (height / 2) + 75, 'Promedio:');
                doc.text(margenFilaIntermedia, (height / 2) + 75,  '¿Has estudiado en el extranjero?:');

                //Respuestas
                doc.setFont(undefined, 'normal');
                for (let i = 0; i < $scope.properties.lstInformacionEscolarMod.length; i++) {
                
                doc.text($scope.properties.lstInformacionEscolarMod[i].escuela.descripcion.toString(), margenPrimeraFila, (height / 2) + 49);            
                doc.text($scope.properties.lstInformacionEscolarMod[i].anoInicio.toString(), margenFilaIntermedia, (height / 2) + 49);            
                doc.text($scope.properties.lstInformacionEscolarMod[i].anoFin.toString(), margenSegundaFila, (height / 2) + 49);            
                doc.text($scope.properties.lstInformacionEscolarMod[i].promedio.toString(), margenPrimeraFila, (height / 2) + 79);            
                
                }

                doc.text($scope.properties.autodescripcionV2.catEstudiadoExtranjero.descripcion, margenFilaIntermedia, (height / 2) + 79);        
                
                
                
            



                
           
                
                doc.addPage();
                //  ----------------------------------- NUEVA HOJA Y CUARTA SECCIÓN  ----------------------------------- 
                
                //Encabezados
                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 135, '¿Has estado inscrito en otras universidades?')
                doc.text(margenPrimeraFila, (height / 2) - 125, '¿Qué área cursas o cursaste en el último año de bachillerato?');
            
                doc.text(margenPrimeraFila, (height / 2) - 115,  '¿En qué materia has obtenido calificaciones más altas?');
                doc.text(margenPrimeraFila, (height / 2) - 95,  '¿Qué materias te gustan más?');
                doc.text(margenPrimeraFila, (height / 2) - 75,  '¿En qué materia has obtenido calificaciones más bajas?');

                doc.text(margenPrimeraFila, (height / 2) - 55,  '¿Qué materias te gustan menos?');
                doc.text(margenPrimeraFila, (height / 2) - 35,  '¿Has presentado exámenes extraordinarios?');
                doc.text(margenPrimeraFila, (height / 2) - 25,  '¿Cúal(es) exámenes has presentado?');
                doc.text(margenPrimeraFila, (height / 2) - 6 ,  '¿Por qué tuviste que hacerlos?');

                doc.text(margenPrimeraFila, (height / 2) + 12 ,  '¿Has reprobado algún año o semestre?');
                doc.text(margenPrimeraFila, (height / 2) + 20 ,  '¿Qué período reprobaste?');
                doc.text(margenPrimeraFila, (height / 2) + 40 ,  '¿Por qué reprobaste?');


                //Respuestas
                doc.setFont(undefined, 'normal');
                doc.text($scope.properties.autodescripcionV2.catInscritoOtraUniversidad.descripcion, respuestasPrimeraFila, (height / 2) - 130);
                doc.text($scope.properties.autodescripcionV2.catAreaBachillerato.descripcion, respuestasPrimeraFila, (height / 2) - 120);
                doc.text($scope.properties.autodescripcionV2.materiasCalifAltas, respuestasPrimeraFila, (height / 2) - 110);
                doc.text($scope.properties.autodescripcionV2.materiasTeGustan, respuestasPrimeraFila, (height / 2) - 91);

                doc.text($scope.properties.autodescripcionV2.materiasNoTeGustan, respuestasPrimeraFila, (height / 2) - 51);
                doc.text($scope.properties.autodescripcionV2.materiasCalifBajas, respuestasPrimeraFila, (height / 2) - 71);
                doc.text($scope.properties.autodescripcionV2.catHasPresentadoExamenExtraordinario.descripcion, respuestasPrimeraFila, (height / 2) - 31);
                doc.text(($scope.properties.autodescripcionV2.cualExamenExtraPresentaste == null || $scope.properties.autodescripcionV2.cualExamenExtraPresentaste == "" ? "N/A" : $scope.properties.autodescripcionV2.cualExamenExtraPresentaste), respuestasPrimeraFila, (height / 2) - 21);
                doc.text($scope.properties.autodescripcionV2.catHasReprobado.descripcion, respuestasPrimeraFila, (height / 2) + 17);
                doc.text(($scope.properties.autodescripcionV2.motivoExamenExtraordinario == null || $scope.properties.autodescripcionV2.motivoExamenExtraordinario == "" ? "N/A" : $scope.properties.autodescripcionV2.motivoExamenExtraordinario), respuestasPrimeraFila, (height / 2) - 2);
                doc.text(($scope.properties.autodescripcionV2.periodoReprobaste == null || $scope.properties.autodescripcionV2.periodoReprobaste == "" ? "N/A" : $scope.properties.autodescripcionV2.periodoReprobaste), respuestasPrimeraFila, (height / 2) + 24);
                doc.text(($scope.properties.autodescripcionV2.motivoReprobaste == null || $scope.properties.autodescripcionV2.motivoReprobaste == "" ? "N/A" : $scope.properties.autodescripcionV2.motivoReprobaste), respuestasPrimeraFila, (height / 2) + 44);
                
                
                



                //  ----------------------------------------- QUINTA SECCIÓN  ----------------------------------------- 
                doc.setFillColor(228, 212, 200);
                doc.rect(10, (height / 2) + 55, 194, 78, 'F');

                doc.setFontSize(fontSubTitle);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) + 61, 'Información laboral');

                //Encabezados
                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) + 70, '¿Has tenido algún trabajo?');
                doc.text(margenFilaIntermedia, (height / 2) + 70, '¿Actualmente trabajas?');
                doc.text(margenPrimeraFila, (height / 2) + 80, 'Nombre de la organización o empresa donde trabajaste:');
                doc.text(margenPrimeraFila, (height / 2) + 100, 'Nombre de la organización o empresa donde trabajas actualmente:');
                doc.text(margenPrimeraFila, (height / 2) +120 , '¿Sientes que tu experiencia de trabajo te ayudó a elegir tu carrera?:');
                
                //Respuestas
                doc.setFont(undefined, 'normal');
                doc.text($scope.properties.autodescripcionV2.catHasTenidoTrabajo.descripcion, respuestasPrimeraFila, (height / 2) + 74);
                doc.text($scope.properties.autodescripcionV2.catActualnenteTrabajas.descripcion, margenFilaIntermedia, (height / 2) + 74);
                if($scope.properties.autodescripcionV2.catExperienciaAyudaCarrera == null){
                doc.text("N/A", margenPrimeraFila, (height / 2) + 84);            
                }else{
                doc.text(($scope.properties.autodescripcionV2.catExperienciaAyudaCarrera.descripcion == null ? "N/A" : $scope.properties.autodescripcionV2.catExperienciaAyudaCarrera.descripcion), margenPrimeraFila, (height / 2) + 84);            
                }
            
                doc.text(($scope.properties.autodescripcionV2.empresaTrabajas == null || $scope.properties.autodescripcionV2.empresaTrabajas == "" ? "N/A" :$scope.properties.autodescripcionV2.empresaTrabajas), margenPrimeraFila, (height / 2) + 105);
                doc.text(($scope.properties.autodescripcionV2.empresaTrabajaste == null || $scope.properties.autodescripcionV2.empresaTrabajaste == "" ? "N/A" :$scope.properties.autodescripcionV2.empresaTrabajaste), margenPrimeraFila, (height / 2) + 125);
                
                doc.addPage();
                //  ----------------------------------- NUEVA HOJA Y SEPTIMA SECCIÓN  ----------------------------------- 

                doc.setFontSize(fontSubTitle);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 135, 'Idiomas');

                //Encabezados
                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 130, 'Adicional a tu lengua materna, ¿hablas o estudias algún otro idioma?');

                doc.text(margenPrimeraFila, (height / 2) - 120, 'Idiomas');
                doc.text(margenFilaIntermedia, (height / 2) - 120, 'Nivel');
                
                //Respuestas
                doc.setFont(undefined, 'normal');
                if($scope.properties.lstIdiomasV2.length>0){
                doc.text("Si", respuestasPrimeraFila, (height / 2) - 127);    
                }else{
                doc.text("No", respuestasPrimeraFila, (height / 2) - 127);    

                doc.text("N/A", respuestasPrimeraFila, (height / 2) -116);
                doc.text("N/A", margenFilaIntermedia, (height / 2) -116);

                }
                
                //doc.text($scope.properties.autodescripcionV2.catActualnenteTrabajas.descripcion, margenFilaIntermedia, (height / 2) + 74);

              
                //  ----------------------------------------- OCTAVA SECCIÓN  ----------------------------------------- 
                doc.setFillColor(228, 212, 200);
                doc.rect(10, (height / 2) - 105, 194, 130, 'F');

                doc.setFontSize(fontSubTitle);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 97, 'Salud');

               //Encabezados
                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 90, '¿Te consideras una persona saludable?:');
                doc.text(margenPrimeraFila, (height / 2) - 80, '¿Vives en situación de discapacidad?:');
                doc.text(margenPrimeraFila, (height / 2) - 70, '¿Qué tipo de discapacidad?:');
                doc.text(margenPrimeraFila, (height / 2) - 50, '¿Tienes algún problema de salud que necesite atención médica continua?:');
                doc.text(margenPrimeraFila, (height / 2) - 30, 'Describe brevemente:');
                doc.text(margenPrimeraFila, (height / 2) - 20, '¿Has recibido alguna terapia?:');
                doc.text(margenPrimeraFila, (height / 2) - 5, '¿Qué tipo de terapia?:');

              //Respuestas
                doc.setFont(undefined, 'normal');
                doc.text($scope.properties.autodescripcionV2.catPersonaSaludable.descripcion, respuestasPrimeraFila, (height / 2) - 84);
                doc.text($scope.properties.autodescripcionV2.catVivesEstadoDiscapacidad.descripcion, respuestasPrimeraFila, (height / 2) - 74);

                doc.text(($scope.properties.autodescripcionV2.tipoDiscapacidad == null || $scope.properties.autodescripcionV2.tipoDiscapacidad == "" ? "N/A" : $scope.properties.autodescripcionV2.tipoDiscapacidad), respuestasPrimeraFila, (height / 2) - 64);
                doc.text($scope.properties.autodescripcionV2.catProblemasSaludAtencion.descripcion, respuestasPrimeraFila, (height / 2) - 44);
                doc.text(($scope.properties.autodescripcionV2.problemasSaludAtencionContinua == null || $scope.properties.autodescripcionV2.problemasSaludAtencionContinua == "" ? "N/A" : $scope.properties.autodescripcionV2.problemasSaludAtencionContinua ), margenPrimeraFila, (height / 2) - 24);

                doc.text(( ($scope.properties.autodescripcionV2.catRecibidoTerapia.descripcion == null || $scope.properties.autodescripcionV2.catRecibidoTerapia.descripcion == "" ) ? "N/A" : $scope.properties.autodescripcionV2.catRecibidoTerapia.descripcion), margenPrimeraFila, (height / 2) - 14);
                doc.text(($scope.properties.autodescripcionV2.hasRecibidoAlgunaTerapia == null || $scope.properties.autodescripcionV2.hasRecibidoAlgunaTerapia == "" ? "N/A" : $scope.properties.autodescripcionV2.autodescripcionV2.hasRecibidoAlgunaTerapia), margenPrimeraFila, (height / 2) - 1);

                //  ----------------------------------------- NOVENA SECCIÓN  ----------------------------------------- 
                doc.setFontSize(fontSubTitle);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) + 35, 'Entorno familiar');
                
                //Encabezados
                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) + 40, '¿Cómo describirías a tu familia?');
                doc.text(margenPrimeraFila, (height / 2) + 55, '¿Si pudieras cambiar algo de tu familia, qué sería?');
                doc.text(margenPrimeraFila, (height / 2) + 75, '¿Con quién de tu familia tienes una mejor relación?');
                doc.text(margenPrimeraFila, (height / 2) + 95, 'Cuándo tienes un problema, ¿con quién lo platicas?');
                doc.text(margenPrimeraFila, (height / 2) + 115, '¿Qué características de personalidad admiras de tu padre?');

                //Respuestas
                doc.setFont(undefined, 'normal');
                doc.text($scope.properties.autodescripcionV2.comoDescribesTuFamilia, respuestasPrimeraFila, (height / 2) + 44);
                doc.text($scope.properties.autodescripcionV2.queCambiariasDeTuFamilia, respuestasPrimeraFila, (height / 2) + 60);
                doc.text($scope.properties.autodescripcionV2.familiarMejorRelacion, respuestasPrimeraFila, (height / 2) + 80);

                doc.text($scope.properties.autodescripcionV2.conQuienPlaticasProblemas, respuestasPrimeraFila, (height / 2) + 100);
                doc.text($scope.properties.autodescripcionV2.admirasPersonalidadPadre, respuestasPrimeraFila, (height / 2) + 120);



                doc.addPage();
                //  ----------------------------------------- DECÍMA SECCIÓN  ----------------------------------------- 
                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');

                doc.text(margenPrimeraFila, (height / 2) - 125, '¿Qué características de personalidad admiras de tu padre?');
                doc.text(margenPrimeraFila, (height / 2) - 110, '¿Qué defectos observas en él?');
                doc.text(margenPrimeraFila, (height / 2) - 95, '¿Qué características de personalidad admiras de tu madre?');
                doc.text(margenPrimeraFila, (height / 2) - 80, '¿Qué defectos observas en ella?');
                doc.text(margenPrimeraFila, (height / 2) - 65, '¿Cómo describirías la relación con tus hermanos?');

                //Respuestas
                doc.setFont(undefined, 'normal');                
                doc.text($scope.properties.autodescripcionV2.admirasPersonalidadPadre, respuestasPrimeraFila, (height / 2) - 120);
                doc.text($scope.properties.autodescripcionV2.admirasPersonalidadMadre, respuestasPrimeraFila, (height / 2) - 91);
                doc.text($scope.properties.autodescripcionV2.defectosObservasMadre, respuestasPrimeraFila, (height / 2) - 75);

                doc.text($scope.properties.autodescripcionV2.defectosObservasPadre, respuestasPrimeraFila, (height / 2) - 105);
                doc.text($scope.properties.autodescripcionV2.comoDescribesRelacionHermanos, respuestasPrimeraFila, (height / 2) - 60);


                //  ----------------------------------------- ONCEAVA SECCIÓN  ----------------------------------------- 
                doc.setFillColor(228, 212, 200);
                doc.rect(10, (height / 2) - 55, 190, 185, 'F');

                doc.setFontSize(fontSubTitle);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 48, 'Entorno social');
                
                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 40, '¿Practicas algún deporte?');
                doc.text(margenPrimeraFila, (height / 2) - 25, '¿Qué deportes practicas?');
                doc.text(margenPrimeraFila, (height / 2) - 10, '¿Participas o asistes a alguna organización, club o grupo?');
                doc.text(margenPrimeraFila, (height / 2) + 5, '¿En cuál(es)?');
                doc.text(margenPrimeraFila, (height / 2) + 20, '¿Qué haces en tu tiempo libre?');
                doc.text(margenPrimeraFila, (height / 2) + 40, '¿Te gusta leer?');
                doc.text(margenPrimeraFila, (height / 2) + 50, '¿Qué tipo de lectura prefieres?');
                doc.text(margenPrimeraFila, (height / 2) + 60, '¿En cuál(es)?');
                doc.text(margenPrimeraFila, (height / 2) + 85, '¿Has sido jefe o directivo de algún grupo o asociación?');
                doc.text(margenPrimeraFila, (height / 2) + 95, '¿Perteneces o has pertenecido a alguna organización?');
                doc.text(margenPrimeraFila, (height / 2) + 110, '¿En cuál(es)?');



                //Respuestas
                doc.setFont(undefined, 'normal');
                doc.text($scope.properties.autodescripcionV2.catPracticasDeporte.descripcion, respuestasPrimeraFila, (height / 2) - 36);
                doc.text(($scope.properties.autodescripcionV2.queDeportePracticas == null || $scope.properties.autodescripcionV2.queDeportePracticas == "" ? "N/A" : $scope.properties.autodescripcionV2.queDeportePracticas ), respuestasPrimeraFila, (height / 2) - 20);
                doc.text($scope.properties.autodescripcionV2.catParticipasGrupoSocial.descripcion, respuestasPrimeraFila, (height / 2) - 5);
                doc.text(($scope.properties.autodescripcionV2.organizacionParticipas == null || $scope.properties.autodescripcionV2.organizacionParticipas == "" ? "N/A" : $scope.properties.autodescripcionV2.organizacionParticipas), respuestasPrimeraFila, (height / 2) + 9);
                doc.text($scope.properties.autodescripcionV2.queHacesEnTuTiempoLibre, respuestasPrimeraFila, (height / 2) + 25);

                doc.text($scope.properties.autodescripcionV2.catTeGustaLeer.descripcion, respuestasPrimeraFila, (height / 2) + 45);
                doc.text(($scope.properties.autodescripcionV2.queLecturaPrefieres == null || $scope.properties.autodescripcionV2.queLecturaPrefieres == "" ? "N/A" : $scope.properties.autodescripcionV2.queLecturaPrefieres), respuestasPrimeraFila, (height / 2) + 55);
                doc.text(($scope.properties.autodescripcionV2.organizacionHasSidoJefe == null || $scope.properties.autodescripcionV2.organizacionHasSidoJefe == "" ? "N/A" : $scope.properties.autodescripcionV2.organizacionHasSidoJefe), respuestasPrimeraFila, (height / 2) + 65);
                doc.text($scope.properties.autodescripcionV2.catJefeOrganizacionSocial.descripcion, respuestasPrimeraFila, (height / 2) + 89);
                doc.text($scope.properties.autodescripcionV2.pertenecesOrganizacion.descripcion, respuestasPrimeraFila, (height / 2) + 99);
                doc.text(($scope.properties.autodescripcionV2.organizacionesPerteneces == null || $scope.properties.autodescripcionV2.organizacionesPerteneces == "" ? "N/A" : $scope.properties.autodescripcionV2.organizacionesPerteneces), respuestasPrimeraFila, (height / 2) + 115);

                doc.addPage()
              //  ----------------------------------------- DOCEAVA SECCIÓN  ----------------------------------------- 
                
                doc.setFillColor(228, 212, 200);
                doc.rect(10, (height / 2) - 125, 190, 245, 'F');

                doc.setFontSize(fontSubTitle);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 110, 'Valoración personal');
                
                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                
                doc.text(margenPrimeraFila, (height / 2) - 98, '¿Cuáles crees que son tus principales cualidades o virtudes?');
                doc.text(margenPrimeraFila, (height / 2) - 80, '¿Cuál ha sido el mayor problema que has enfrentado?');
                doc.text(margenPrimeraFila, (height / 2) - 65, '¿Ya resolviste ese problema?');
                doc.text(margenPrimeraFila, (height / 2) - 50, '¿Cómo lo resolviste?');

                doc.text(margenPrimeraFila, (height / 2) - 35, '¿Cómo crees que te describirían tus amigos?');
                doc.text(margenPrimeraFila, (height / 2) - 10, '¿Si pudieras, qué cambiarías de ti?');
                doc.text(margenPrimeraFila, (height / 2) + 20, '¿Cuáles crees que son tus principales defectos o puntos débiles?');

                doc.text(margenPrimeraFila, (height / 2) + 35, '¿Cuáles son tus principales metas a corto plazo?');
                doc.text(margenPrimeraFila, (height / 2) + 55, '¿Cuáles son tus principales metas a mediano plazo?');

                doc.text(margenPrimeraFila, (height / 2) + 75, '¿Cuáles son tus principales metas a largo plazo?');
                doc.text(margenPrimeraFila, (height / 2) + 100, 'Describe detalladamente tus características de personalidad:');


                //Respuestas
                doc.setFont(undefined, 'normal');
                doc.text($scope.properties.autodescripcionV2.principalesVirtudes, respuestasPrimeraFila, (height / 2) - 95);
                doc.text($scope.properties.autodescripcionV2.mayorProblemaEnfrentado, respuestasPrimeraFila, (height / 2) - 75);
                doc.text($scope.properties.autodescripcionV2.catYaResolvisteElProblema.descripcion, respuestasPrimeraFila, (height / 2) - 61);
                doc.text($scope.properties.autodescripcionV2.comoResolvisteProblema, respuestasPrimeraFila, (height / 2) - 46);

                doc.text($scope.properties.autodescripcionV2.comoTeDescribenTusAmigos, respuestasPrimeraFila, (height / 2) - 30);
                doc.text($scope.properties.autodescripcionV2.queCambiariasDeTi, respuestasPrimeraFila, (height / 2) - 5);
                doc.text($scope.properties.autodescripcionV2.principalesDefectos, respuestasPrimeraFila, (height / 2) + 24);

                doc.text($scope.properties.autodescripcionV2.metasCortoPlazo, respuestasPrimeraFila, (height / 2) + 31);                
                doc.text($scope.properties.autodescripcionV2.metasMedianoPlazo, respuestasPrimeraFila, (height / 2) + 58);
                doc.text($scope.properties.autodescripcionV2.metasLargoPlazo, respuestasPrimeraFila, (height / 2) + 78);
                doc.text($scope.properties.autodescripcionV2.detallesPersonalidad, respuestasPrimeraFila, (height / 2) + 105);            

                doc.addPage()

              //  ----------------------------------------- DOCEAVA SECCIÓN  ----------------------------------------- 
                doc.setFontSize(fontSubTitle);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 110, 'Religión');
                
                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 100, '¿A qué religión perteneces?:');
                doc.text(margenFilaIntermedia, (height / 2) - 100, '¿Practicas tu religión?:');
                doc.text(margenSegundaFila, (height / 2) - 100, '¿Qué aspectos no te gustan de tu religión?:');
                doc.text(margenPrimeraFila, (height / 2) - 85, '¿Existe algún aspecto de tu religión que no te guste?:');
                doc.text(margenPrimeraFila, (height / 2) - 75, '¿Por qué no te gustan estos aspectos de tu religión?:');

               //Respuestas
               doc.setFont(undefined, 'normal');
               doc.text($scope.properties.objectSolicitud.catReligion.descripcion, respuestasPrimeraFila, (height / 2) - 96);
               doc.text($scope.properties.autodescripcionV2.catPracticasReligion.descripcion, margenFilaIntermedia, (height / 2) - 96);
               doc.text(($scope.properties.autodescripcionV2.asprctosNoGustanReligion == null || $scope.properties.autodescripcionV2.asprctosNoGustanReligion == "" ? "N/A" : $scope.properties.autodescripcionV2.asprctosNoGustanReligion), margenSegundaFila, (height / 2) - 96);

               doc.text($scope.properties.autodescripcionV2.catAspectoDesagradaReligion.descripcion, margenPrimeraFila, (height / 2) - 80);
               doc.text(($scope.properties.autodescripcionV2.motivoAspectosNoGustanReligion == null || $scope.properties.autodescripcionV2.motivoAspectosNoGustanReligion == "" ? "N/A" : $scope.properties.autodescripcionV2.motivoAspectosNoGustanReligion), margenPrimeraFila, (height / 2) - 70);

            // ----------------------------------------- DOCEAVA SECCIÓN  ----------------------------------------- 
                doc.setFillColor(228, 212, 200);
                doc.rect(10, (height / 2) - 37, 190, 180, 'F');
                doc.setFontSize(fontSubTitle);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 30, 'Información vocacional');

                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 20, '¿A cuál de las Universidades de la Red Anáhuac deseas ingresar?');
                doc.text(margenSegundaFila, (height / 2) - 20, 'Licenciatura que deseas estudiar:');

                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold')
                doc.text(margenPrimeraFila, (height / 2) - 5, '¿Por qué decidiste ingresar a esta carrera?');
                doc.text(margenPrimeraFila, (height / 2) + 15, '¿Qué expectativas tienes de la carrera?');

                doc.text(margenPrimeraFila, (height / 2) + 30, '¿Has recibido Orientación Vocacional?');
                doc.text(margenPrimeraFila, (height / 2) + 40, '¿Qué características y habilidades consideras tener para lograr éxito en la carrera que has elegido?');

                doc.text(margenPrimeraFila, (height / 2) + 55, 'Profesionalmente, ¿Cómo te ves en 6 años?');
                doc.text(margenPrimeraFila, (height / 2) + 70, '¿Tus padres están de acuerdo con tu elección?');
                doc.text(margenPrimeraFila, (height / 2) + 90, '¿Por qué tus padres piensan eso?');
                doc.text(margenPrimeraFila, (height / 2) + 100, '¿Qué o quiénes influyeron en la elección de tu carrera?');
                doc.text(margenPrimeraFila, (height / 2) + 120, 'Fuentes que influyeron en tu decisión:');

                

                //Respuestas
                doc.setFont(undefined, 'normal');
                doc.text($scope.properties.objectSolicitud.catCampus.descripcion, respuestasPrimeraFila, (height / 2) - 16);
                doc.text($scope.properties.objectSolicitud.catGestionEscolar.nombre, margenSegundaFila, (height / 2) - 16);

                doc.text($scope.properties.autodescripcionV2.motivoElegisteCarrera, respuestasPrimeraFila, (height / 2) - 1);
                doc.text($scope.properties.autodescripcionV2.expectativasCarrera, respuestasPrimeraFila, (height / 2) + 18);

                doc.text($scope.properties.autodescripcionV2.catOrientacionVocacional.descripcion, respuestasPrimeraFila, (height / 2) + 33);
                doc.text($scope.properties.autodescripcionV2.caracteristicasExitoCarrera , respuestasPrimeraFila, (height / 2) + 45);
                doc.text($scope.properties.autodescripcionV2.profesionalComoTeVes, respuestasPrimeraFila, (height / 2) + 58);

                doc.text($scope.properties.autodescripcionV2.catPadresDeAcuerdo.descripcion, respuestasPrimeraFila, (height / 2) + 74);
                doc.text(($scope.properties.autodescripcionV2.motivoPadresNoAcuerdo == null || $scope.properties.autodescripcionV2.motivoPadresNoAcuerdo == "" ? "N/A" : $scope.properties.autodescripcionV2.motivoPadresNoAcuerdo), respuestasPrimeraFila, (height / 2) + 96);
                doc.text($scope.properties.autodescripcionV2.personasInfluyeronDesicion, respuestasPrimeraFila, (height / 2) + 105);
                doc.text(($scope.properties.autodescripcionV2.fuentesInfluyeronDesicion == null || $scope.properties.autodescripcionV2.fuentesInfluyeronDesicion == "" ? "N/A" : $scope.properties.autodescripcionV2.fuentesInfluyeronDesicion), respuestasPrimeraFila, (height / 2) + 124);




                doc.save(`CuestionarioSolicitud.pdf`);
            }
        }
