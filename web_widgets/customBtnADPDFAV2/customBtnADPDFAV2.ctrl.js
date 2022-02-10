function PbButtonCtrl($scope, modalService, $http, blockUI, $q, $filter) {

    'use strict';

    var vm = this;

    $scope.date = new Date();
    $scope.fechaActual = ($filter('date')(Date.parse($scope.date), "dd/MMM/yyyy")).toString();
    $scope.FinEscuela = "";

    //GENERAR PDF - AdataR
    $scope.generatePDF = function () {
        var doc = new jspdf.jsPDF('p', 'mm', 'a4');
        var width = doc.internal.pageSize.getWidth();
        var height = doc.internal.pageSize.getHeight();
        var yValor = 0;
        var yvalor = 0;

        var fontText = 10;
        var fontTitle = 14;
        var fontSubTitle = 13;

        var margenPrimeraFila = 15;
        var margenSegundaFila = 135;
        var margenFilaIntermedia = 80;

        var respuestasPrimeraFila = 15;
        var respuestasSegundaFila = 135;
        var respuestasFilaIntermedia = 80;

        console.log("W " + width)
        console.log("H " + height)



        doc.addImage("widgets/customBtnADPDFAV2/assets/img/LogoRUA.png", "PNG", ((width / 2) + 35), ((height / 2) - 128), 60, 20);

        doc.setFontSize(fontTitle);
        doc.setFont(undefined, 'bold');
        doc.text('Cuestionario de autodescripción', margenPrimeraFila, (height / 2) - 116);

        //IMAGEN DE 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 103, 190, 85, 'F');
        doc.addImage($scope.properties.urlFoto, "JPG", 160, ((height / 2) - 82), 30, 40);

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
        doc.text($scope.properties.autodescripcionV2.comoEstaConformadaFamilia, respuestasPrimeraFila, (height / 2) + 5, { maxWidth: 180, align: "left" });


        //  ----------------------------------------- TERCERA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) + 30, 190, 45, 'F');

        doc.setFontSize(fontTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 35, 'Información escolar');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 45, 'Nombre:');
        doc.text(margenFilaIntermedia, (height / 2) + 45, 'Año inicio:');
        doc.text(margenSegundaFila, (height / 2) + 45, 'Año fin:');
        doc.text(margenSegundaFila +30, +193, 'Promedio:');
        
        doc.text(margenPrimeraFila, +230, '¿Has estudiado en el extranjero?:');
        doc.text(margenPrimeraFila, + 242, '¿Has estado inscrito en otras universidades?')
        doc.text(margenPrimeraFila, + 253, '¿Qué área cursas o cursaste en el último año de bachillerato?');

      
        //Respuestas
        doc.setFont(undefined, 'normal');
        for (let i = 0; i < $scope.properties.lstInformacionEscolarMod.length; i++) {

            doc.text($scope.properties.lstInformacionEscolarMod[i].escuela.descripcion.toString(), margenPrimeraFila, (height / 2) + 49);
            doc.text($scope.properties.lstInformacionEscolarMod[i].anoInicio.toString(), margenFilaIntermedia, (height / 2) + 49);
            doc.text($scope.properties.lstInformacionEscolarMod[i].anoFin.toString(), margenSegundaFila, (height / 2) + 49);
            doc.text($scope.properties.lstInformacionEscolarMod[i].promedio.toString(), margenSegundaFila +30, (height / 2) + 49);

        }

        doc.text($scope.properties.autodescripcionV2.catEstudiadoExtranjero.descripcion, margenPrimeraFila,+235);
        doc.text($scope.properties.autodescripcionV2.catInscritoOtraUniversidad.descripcion, respuestasPrimeraFila, +246);
        doc.text($scope.properties.autodescripcionV2.catAreaBachillerato.descripcion, respuestasPrimeraFila, + 260);

        doc.addPage();
        //  ----------------------------------- NUEVA HOJA Y CUARTA SECCIÓN  ----------------------------------- 

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
      
        doc.text(margenPrimeraFila, + 50, '¿En qué materia has obtenido calificaciones más altas?');
        doc.text(margenPrimeraFila, + 75, '¿Qué materias te gustan más?');
        doc.text(margenPrimeraFila, + 99, '¿En qué materia has obtenido calificaciones más bajas?');

        doc.text(margenPrimeraFila, + 125, '¿Qué materias te gustan menos?');
        doc.text(margenPrimeraFila, + 151, '¿Has presentado exámenes extraordinarios?');
        doc.text(margenPrimeraFila, + 164, '¿Cúal(es) exámenes has presentado?');
        doc.text(margenPrimeraFila, + 177, '¿Por qué tuviste que hacerlos?');

        doc.text(margenPrimeraFila, + 190, '¿Has reprobado algún año o semestre?');
        doc.text(margenPrimeraFila, + 205, '¿Qué período reprobaste?');
        doc.text(margenPrimeraFila, + 220, '¿Por qué reprobaste?');

        //Respuestas
        doc.setFont(undefined, 'normal');
        
        doc.text($scope.properties.autodescripcionV2.materiasCalifAltas, respuestasPrimeraFila, + 55, { maxWidth: 180, align: "left" });

        doc.text($scope.properties.autodescripcionV2.materiasTeGustan, respuestasPrimeraFila, + 81, { maxWidth: 180, align: "left" });
        doc.text($scope.properties.autodescripcionV2.materiasCalifBajas, respuestasPrimeraFila, + 105, { maxWidth: 180, align: "left" });
        doc.text($scope.properties.autodescripcionV2.materiasNoTeGustan, respuestasPrimeraFila, + 130, { maxWidth: 180, align: "left" });
        doc.text($scope.properties.autodescripcionV2.catHasPresentadoExamenExtraordinario.descripcion, respuestasPrimeraFila, + 155);
        doc.text(($scope.properties.autodescripcionV2.cualExamenExtraPresentaste == null || $scope.properties.autodescripcionV2.cualExamenExtraPresentaste == "" ? "N/A" : $scope.properties.autodescripcionV2.cualExamenExtraPresentaste), respuestasPrimeraFila, + 168);
        doc.text(($scope.properties.autodescripcionV2.motivoExamenExtraordinario == null || $scope.properties.autodescripcionV2.motivoExamenExtraordinario == "" ? "N/A" : $scope.properties.autodescripcionV2.motivoExamenExtraordinario), respuestasPrimeraFila, + 181);
        doc.text($scope.properties.autodescripcionV2.catHasReprobado.descripcion, respuestasPrimeraFila, + 196);
        doc.text(($scope.properties.autodescripcionV2.periodoReprobaste == null || $scope.properties.autodescripcionV2.periodoReprobaste == "" ? "N/A" : $scope.properties.autodescripcionV2.periodoReprobaste), respuestasPrimeraFila, + 210);
        doc.text(($scope.properties.autodescripcionV2.motivoReprobaste == null || $scope.properties.autodescripcionV2.motivoReprobaste == "" ? "N/A" : $scope.properties.autodescripcionV2.motivoReprobaste), respuestasPrimeraFila, + 225);

        //  ----------------------------------------- QUINTA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, +237, 190, 53, 'F');

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 93, 'Información laboral');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 103, '¿Has tenido algún trabajo?');
        doc.text(margenFilaIntermedia, (height / 2) + 103, '¿Actualmente trabajas?');
        doc.text(margenPrimeraFila, (height / 2) + 115, 'Nombre de la organización o empresa donde trabajaste:');
        doc.text(margenPrimeraFila, (height / 2) + 125, 'Nombre de la organización o empresa donde trabajas actualmente:');
        doc.text(margenPrimeraFila, (height / 2) + 135, '¿Sientes que tu experiencia de trabajo te ayudó a elegir tu carrera?:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.autodescripcionV2.catHasTenidoTrabajo.descripcion, respuestasPrimeraFila, (height / 2) + 110);
        doc.text($scope.properties.autodescripcionV2.catActualnenteTrabajas.descripcion, margenFilaIntermedia, (height / 2) + 110);

        doc.text(($scope.properties.autodescripcionV2.empresaTrabajaste == null || $scope.properties.autodescripcionV2.empresaTrabajaste == "" ? "N/A" : $scope.properties.autodescripcionV2.empresaTrabajaste), margenPrimeraFila, (height / 2) + 119);
        doc.text(($scope.properties.autodescripcionV2.empresaTrabajas == null || $scope.properties.autodescripcionV2.empresaTrabajas == "" ? "N/A" : $scope.properties.autodescripcionV2.empresaTrabajas), margenPrimeraFila, (height / 2) + 129);

        if ($scope.properties.autodescripcionV2.catExperienciaAyudaCarrera == null) {
            doc.text("N/A", margenPrimeraFila, (height / 2) + 139);
        } else {
            doc.text(($scope.properties.autodescripcionV2.catExperienciaAyudaCarrera.descripcion == null ? "N/A" : $scope.properties.autodescripcionV2.catExperienciaAyudaCarrera.descripcion), margenPrimeraFila, (height / 2) + 139);
        }

        doc.addPage();
        //  ----------------------------------- NUEVA HOJA Y SEPTIMA SECCIÓN  ----------------------------------- 

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, +25, 'Idiomas');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila,+30, 'Adicional a tu lengua materna, ¿hablas o estudias algún otro idioma?');

        doc.text(margenPrimeraFila, +40, 'Idiomas');
        doc.text(margenFilaIntermedia, +40, 'Nivel');

        //Respuestas
        doc.setFont(undefined, 'normal');
        if ($scope.properties.lstIdiomasV2.length > 0) {
            doc.text("Si", respuestasPrimeraFila, +35);
        } else {
            doc.text("No", respuestasPrimeraFila, +35);

            doc.text("N/A", respuestasPrimeraFila, +35);
            doc.text("N/A", margenFilaIntermedia, +35);

        }
        debugger
        for (let i = 0; i < $scope.properties.lstIdiomasV2.length; i++) {
            $scope.idiomas = [];
            $scope.idiomas[i] =$scope.properties.lstIdiomasV2[i].catIdioma.descripcion;
    
            doc.text($scope.idiomas[i], margenPrimeraFila, +45, { maxWidth: 180, align: "left" });
    
        }      

        for (let i = 0; i < $scope.properties.lstIdiomasV2.length; i++) {
            $scope.idiomas = [];
            $scope.idiomas[i] =$scope.properties.lstIdiomasV2[i].catTraduccion.descripcion;
    
            doc.text($scope.idiomas[i], margenFilaIntermedia, +45, { maxWidth: 180, align: "left" });
    
        }      

        //doc.text($scope.properties.autodescripcionV2.catActualnenteTrabajas.descripcion, margenFilaIntermedia, (height / 2) + 74);
        //  ----------------------------------------- OCTAVA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 95, 150, 105, 'F');

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, +60, 'Salud');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, +70, '¿Te consideras una persona saludable?:');
        doc.text(margenPrimeraFila, +80, '¿Vives en situación de discapacidad?:');
        doc.text(margenPrimeraFila, +90, '¿Qué tipo de discapacidad?:');
        doc.text(margenPrimeraFila, +105, '¿Tienes algún problema de salud que necesite atención médica continua?:');
        doc.text(margenPrimeraFila, +120, 'Describe brevemente:');
        doc.text(margenPrimeraFila, +140, '¿Has recibido alguna terapia?:');
        doc.text(margenPrimeraFila, +150, '¿Qué tipo de terapia?:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.autodescripcionV2.catPersonaSaludable.descripcion, respuestasPrimeraFila, +75);
        doc.text($scope.properties.autodescripcionV2.catVivesEstadoDiscapacidad.descripcion, respuestasPrimeraFila, +85);

        doc.text(($scope.properties.autodescripcionV2.tipoDiscapacidad == null || $scope.properties.autodescripcionV2.tipoDiscapacidad == "" ? "N/A" : $scope.properties.autodescripcionV2.tipoDiscapacidad), respuestasPrimeraFila, +95);
        doc.text($scope.properties.autodescripcionV2.catProblemasSaludAtencion.descripcion, respuestasPrimeraFila, +110);
        doc.text(($scope.properties.autodescripcionV2.problemasSaludAtencionContinua == null || $scope.properties.autodescripcionV2.problemasSaludAtencionContinua == "" ? "N/A" : $scope.properties.autodescripcionV2.problemasSaludAtencionContinua), margenPrimeraFila, +125);

        doc.text((($scope.properties.autodescripcionV2.catRecibidoTerapia.descripcion == null || $scope.properties.autodescripcionV2.catRecibidoTerapia.descripcion == "") ? "N/A" : $scope.properties.autodescripcionV2.catRecibidoTerapia.descripcion), margenPrimeraFila, +145);
        doc.text(($scope.properties.autodescripcionV2.hasRecibidoAlgunaTerapia == null || $scope.properties.autodescripcionV2.hasRecibidoAlgunaTerapia == "" ? "N/A" : $scope.properties.autodescripcionV2.autodescripcionV2.hasRecibidoAlgunaTerapia), margenPrimeraFila, +155);

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, + 165, 'Entorno familiar');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, +170, '¿Cómo describirías a tu familia?');
        doc.text(margenPrimeraFila, +195 , '¿Si pudieras cambiar algo de tu familia, qué sería?');
        doc.text(margenPrimeraFila, +220, '¿Con quién de tu familia tienes una mejor relación?');
        doc.text(margenPrimeraFila, +245, 'Cuándo tienes un problema, ¿con quién lo platicas?');
        doc.text(margenPrimeraFila, +270, '¿Qué características de personalidad admiras de tu madre?');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.autodescripcionV2.comoDescribesTuFamilia, respuestasPrimeraFila,  + 175, { maxWidth: 180, align: "left" });
        doc.text($scope.properties.autodescripcionV2.queCambiariasDeTuFamilia, respuestasPrimeraFila,  +200 , { maxWidth: 180, align: "left" });
        doc.text($scope.properties.autodescripcionV2.familiarMejorRelacion, respuestasPrimeraFila,+225 , { maxWidth: 180, align: "left" });

        doc.text($scope.properties.autodescripcionV2.conQuienPlaticasProblemas, respuestasPrimeraFila, +250, { maxWidth: 180, align: "left" });
        doc.text($scope.properties.autodescripcionV2.admirasPersonalidadMadre, respuestasPrimeraFila, +275, { maxWidth: 180, align: "left" });
        doc.addPage();
        //  ----------------------------------------- DECÍMA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, +30, '¿Qué defectos observas en ella?');
        doc.text(margenPrimeraFila, +54, '¿Qué características de personalidad admiras de tu padre?');
        doc.text(margenPrimeraFila, +78, '¿Qué defectos observas en él?');
        doc.text(margenPrimeraFila, +100, '¿Cómo describirías la relación con tus hermanos?');

        //Respuestas
        doc.setFont(undefined, 'normal');

        doc.text($scope.properties.autodescripcionV2.defectosObservasMadre, respuestasPrimeraFila, +35, { maxWidth: 180, align: "left" });
        doc.text($scope.properties.autodescripcionV2.admirasPersonalidadPadre, respuestasPrimeraFila, +59, { maxWidth: 180, align: "left" });

        doc.text($scope.properties.autodescripcionV2.defectosObservasPadre, respuestasPrimeraFila, +82, { maxWidth: 180, align: "left" });
        doc.text($scope.properties.autodescripcionV2.comoDescribesRelacionHermanos, respuestasPrimeraFila, +108, { maxWidth: 180, align: "left" });

        //  ----------------------------------------- ONCEAVA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, +123, 190, 159, 'F');

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, +129, 'Entorno social');

        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, +134, '¿Practicas algún deporte?');
        doc.text(margenPrimeraFila, +145, '¿Qué deportes practicas?');
        doc.text(margenPrimeraFila, +162, '¿Participas o asistes a alguna organización, club o grupo?');
        doc.text(margenPrimeraFila, +172, '¿En cuál(es)?');
        doc.text(margenPrimeraFila, +183, '¿Qué haces en tu tiempo libre?');
        doc.text(margenPrimeraFila, +200, '¿Te gusta leer?');
        doc.text(margenPrimeraFila, +212, '¿Qué tipo de lectura prefieres?');
        doc.text(margenPrimeraFila, +230, '¿Has sido jefe o directivo de algún grupo o asociación?');
        doc.text(margenPrimeraFila, +240, '¿En cuál(es)?');
        doc.text(margenPrimeraFila, +260, '¿Perteneces o has pertenecido a alguna organización?');
        doc.text(margenPrimeraFila, +270, '¿En cuál(es)?');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.autodescripcionV2.catPracticasDeporte.descripcion, respuestasPrimeraFila, +139);
        doc.text(($scope.properties.autodescripcionV2.queDeportePracticas == null || $scope.properties.autodescripcionV2.queDeportePracticas == "" ? "N/A" : $scope.properties.autodescripcionV2.queDeportePracticas), respuestasPrimeraFila, +149);
        doc.text(($scope.properties.autodescripcionV2.organizacionParticipas == null || $scope.properties.autodescripcionV2.organizacionParticipas == "" ? "N/A" : $scope.properties.autodescripcionV2.organizacionParticipas), respuestasPrimeraFila, +166);
        doc.text($scope.properties.autodescripcionV2.catParticipasGrupoSocial.descripcion, respuestasPrimeraFila, +176);
        doc.text($scope.properties.autodescripcionV2.queHacesEnTuTiempoLibre, respuestasPrimeraFila, +188, { maxWidth: 180, align: "left" });

        doc.text($scope.properties.autodescripcionV2.catTeGustaLeer.descripcion, respuestasPrimeraFila, + 204);
        doc.text(($scope.properties.autodescripcionV2.queLecturaPrefieres == null || $scope.properties.autodescripcionV2.queLecturaPrefieres == "" ? "N/A" : $scope.properties.autodescripcionV2.queLecturaPrefieres), respuestasPrimeraFila, + 216);
        doc.text($scope.properties.autodescripcionV2.catJefeOrganizacionSocial.descripcion, respuestasPrimeraFila, +235);
        doc.text(($scope.properties.autodescripcionV2.organizacionHasSidoJefe == null || $scope.properties.autodescripcionV2.organizacionHasSidoJefe == "" ? "N/A" : $scope.properties.autodescripcionV2.organizacionHasSidoJefe), respuestasPrimeraFila, + 245);
        doc.text($scope.properties.autodescripcionV2.pertenecesOrganizacion.descripcion, respuestasPrimeraFila, +265);
        doc.text(($scope.properties.autodescripcionV2.organizacionesPerteneces == null || $scope.properties.autodescripcionV2.organizacionesPerteneces == "" ? "N/A" : $scope.properties.autodescripcionV2.organizacionesPerteneces), respuestasPrimeraFila, +275);

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
        
        doc.text($scope.properties.autodescripcionV2.principalesVirtudes, respuestasPrimeraFila, (height / 2)-94, { maxWidth: 180, align: "left" });
        doc.text($scope.properties.autodescripcionV2.mayorProblemaEnfrentado, respuestasPrimeraFila, (height / 2) - 75, { maxWidth: 180, align: "left" });
        doc.text($scope.properties.autodescripcionV2.catYaResolvisteElProblema.descripcion, respuestasPrimeraFila, (height / 2) - 61);
        doc.text($scope.properties.autodescripcionV2.comoResolvisteProblema, respuestasPrimeraFila, (height / 2) - 46);

        doc.text($scope.properties.autodescripcionV2.comoTeDescribenTusAmigos, respuestasPrimeraFila, (height / 2) - 30, { maxWidth: 180, align: "left" });
        doc.text($scope.properties.autodescripcionV2.queCambiariasDeTi, respuestasPrimeraFila, (height / 2) - 5, { maxWidth: 180, align: "left" });
        doc.text($scope.properties.autodescripcionV2.principalesDefectos, respuestasPrimeraFila, (height / 2) + 24, { maxWidth: 180, align: "left" });

        doc.text($scope.properties.autodescripcionV2.metasCortoPlazo, respuestasPrimeraFila, (height / 2) + 38, { maxWidth: 180, align: "left" });
        doc.text($scope.properties.autodescripcionV2.metasMedianoPlazo, respuestasPrimeraFila, (height / 2) + 58, { maxWidth: 180, align: "left" });
        doc.text($scope.properties.autodescripcionV2.metasLargoPlazo, respuestasPrimeraFila, (height / 2) + 78, { maxWidth: 180, align: "left" });
        doc.text($scope.properties.autodescripcionV2.detallesPersonalidad, respuestasPrimeraFila, (height / 2) + 105, { maxWidth: 180, align: "left" });

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
        doc.text(margenPrimeraFila, (height / 2) - 80, '¿Existe algún aspecto de tu religión que no te guste?:');
        doc.text(118, (height / 2) - 80, '¿Por qué no te gustan estos aspectos de tu religión?:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.objectSolicitud.catReligion.descripcion, respuestasPrimeraFila, (height / 2) - 96);
        doc.text($scope.properties.autodescripcionV2.catPracticasReligion.descripcion, margenFilaIntermedia, (height / 2) - 96);
        doc.text(($scope.properties.autodescripcionV2.asprctosNoGustanReligion == null || $scope.properties.autodescripcionV2.asprctosNoGustanReligion == "" ? "N/A" : $scope.properties.autodescripcionV2.asprctosNoGustanReligion), margenSegundaFila, (height / 2) - 96);

        doc.text($scope.properties.autodescripcionV2.catAspectoDesagradaReligion.descripcion, margenPrimeraFila, (height / 2) - 75);
        doc.text(($scope.properties.autodescripcionV2.motivoAspectosNoGustanReligion == null || $scope.properties.autodescripcionV2.motivoAspectosNoGustanReligion == "" ? "N/A" : $scope.properties.autodescripcionV2.motivoAspectosNoGustanReligion), 118, (height / 2) - 75);

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
        doc.text($scope.properties.autodescripcionV2.expectativasCarrera, respuestasPrimeraFila, (height / 2) + 20);

        doc.text($scope.properties.autodescripcionV2.catOrientacionVocacional.descripcion, respuestasPrimeraFila, (height / 2) + 35);
        doc.text($scope.properties.autodescripcionV2.caracteristicasExitoCarrera, respuestasPrimeraFila, (height / 2) + 45);
        doc.text($scope.properties.autodescripcionV2.profesionalComoTeVes, respuestasPrimeraFila, (height / 2) + 60);

        doc.text($scope.properties.autodescripcionV2.catPadresDeAcuerdo.descripcion, respuestasPrimeraFila, (height / 2) + 74);
        doc.text(($scope.properties.autodescripcionV2.motivoPadresNoAcuerdo == null || $scope.properties.autodescripcionV2.motivoPadresNoAcuerdo == "" ? "N/A" : $scope.properties.autodescripcionV2.motivoPadresNoAcuerdo), respuestasPrimeraFila, (height / 2) + 94);
        doc.text($scope.properties.autodescripcionV2.personasInfluyeronDesicion, respuestasPrimeraFila, (height / 2) + 105);
        doc.text(($scope.properties.autodescripcionV2.fuentesInfluyeronDesicion == null || $scope.properties.autodescripcionV2.fuentesInfluyeronDesicion == "" ? "N/A" : $scope.properties.autodescripcionV2.fuentesInfluyeronDesicion), respuestasPrimeraFila, (height / 2) + 124);

        doc.save(`CuestionarioSolicitud.pdf`);
    }

    function convertToPlain(html) {
        html = html.replace(/<style([\s\S]*?)<\/style>/gi, '');
        html = html.replace(/<script([\s\S]*?)<\/script>/gi, '');
        html = html.replace(/<\/div>/ig, '\n');
        html = html.replace(/<\/li>/ig, '\n');
        html = html.replace(/<li>/ig, '  *  ');
        html = html.replace(/<\/ul>/ig, '\n');
        html = html.replace(/<\/p>/ig, '\n');
        html = html.replace(/<br\s*[\/]?>/gi, "\n");
        html = html.replace(/<[^>]+>/ig, '');
        html = html.replaceAll("&#34;", '"');
        html = html.replace(/(<([^>]+)>)/ig, '');

        return html;
    }

    function recResize(texto) {

        if (texto != undefined) {
            textConvert = convertToPlain(texto);
            textCount = Math.ceil((textConvert.length / 180));
            textSize = (textCount * 7) + 3;
        } else {
            textSize = 35;
        }
        return textSize;
    }
}
