function PbButtonCtrl($scope, modalService, $http, blockUI, $q, $filter) {

  'use strict';
  
    var vm = this;
    
    $scope.date = new Date();
    $scope.fechaActual = ($filter('date')(Date.parse($scope.date), "dd/MMM/yyyy")).toString();
    
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

        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 103, 190, 85, 'F');

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
        doc.text($scope.properties.catResidencia, respuestasPrimeraFila, (height / 2) - 45);
        doc.text($scope.properties.objectSolicitud.catPeriodo.descripcion, respuestasPrimeraFila, (height / 2) - 35);
        
        
    
        //  ----------------------------------------- SEGUNDA SECCIÓN  ----------------------------------------- 
        doc.setDrawColor(115, 66, 34)
        //doc.rect(margenSegundaFila,((height / 2)-84),35,45)
        doc.rect(157.5,((height / 2)-84),35,45)

        doc.setFontSize(fontTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 12, 'Información familiar');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 2, '¿Quiénes conforman tu familia?:');
        
        //Respuestas
        doc.setFont(undefined, 'normal');
        debugger
        doc.text($scope.properties.autodescripcionV2.comoEstaConformadaFamilia , respuestasPrimeraFila, (height / 2) + 5);


        //  ----------------------------------------- TERCERA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) + 48, 190, 75, 'F');

        doc.setFontSize(fontTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 58, 'Información escolar');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 70, 'Nombre:');
        doc.text(margenFilaIntermedia, (height / 2) + 70, 'Año inicio:');
        doc.text(margenSegundaFila, (height / 2) + 70, 'Año fin:');

        doc.text(margenPrimeraFila, (height / 2) + 84.5, 'Promedio:');
        doc.text(margenFilaIntermedia, (height / 2) + 84.5,  '¿Has estudiado en el extranjero?');


        doc.addPage();
        //  ----------------------------------- NUEVA HOJA Y CUARTA SECCIÓN  ----------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 130, 190, 165, 'F');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 114, '¿Has estado inscrito en otras universidades?')
        doc.text(margenPrimeraFila, (height / 2) - 98, '¿Qué área cursas o cursaste en el último año de bachillerato?');
        
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 85,  '¿En qué materia has obtenido calificaciones más altas?');
        doc.text(margenPrimeraFila, (height / 2) - 60,  '¿Qué materias te gustan más?');
        doc.text(margenPrimeraFila, (height / 2) - 35,  '¿En qué materia has obtenido calificaciones más bajas?');

        doc.text(margenPrimeraFila, (height / 2) - 10,  '¿Qué materias te gustan menos?');
        doc.text(margenPrimeraFila, (height / 2) + 10,  '¿Has presentado exámenes extraordinarios?');
        doc.text(margenPrimeraFila, (height / 2) + 25 ,  '¿Has reprobado algún año o semestre?');


        //Respuestas
        doc.setFont(undefined, 'normal');
        debugger
        //doc.text($scope.properties.autodescripcionV2.catInscritoOtraUniversidad.descripcion, respuestasPrimeraFila, (height / 2) - 18);
       // doc.text($scope.properties.autodescripcionV2.catAreaBachillerato.descripcion, respuestasPrimeraFila, (height / 2) - 90);
       // doc.text($scope.properties.autodescripcionV2.materiasTeGustan, respuestasPrimeraFila, (height / 2) - 110);
        





        //  ----------------------------------------- QUINTA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 43, 'Información laboral');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 50, '¿Has tenido algún trabajo?');
        doc.text(margenFilaIntermedia, (height / 2) + 50, '¿Actualmente trabajas?');
        
        //  ----------------------------------- NUEVA HOJA Y SEPTIMA SECCIÓN  ----------------------------------- 

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 65, 'Idiomas');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 75, 'Adicional a tu lengua materna, ¿hablas o estudias algún otro idioma?');
        
        doc.addPage();
        //  ----------------------------------------- OCTAVA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 140, 190, 60, 'F');

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 130, 'Salud');

       //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 120, '¿Te consideras una persona saludable?');
        doc.text(margenSegundaFila, (height / 2) - 120, '¿Vives en situación de discapacidad?');
        doc.text(margenPrimeraFila, (height / 2) - 100, '¿Tienes algún problema de salud que necesite atención médica continua?');

        doc.text(margenPrimeraFila, (height / 2) - 90, '¿Has recibido alguna terapia?');
        
        //  ----------------------------------------- NOVENA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 75, 'Entorno familiar');
        
        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 70, '¿Cómo describirías a tu familia?');
        doc.text(margenPrimeraFila, (height / 2) - 50, '¿Si pudieras cambiar algo de tu familia, qué sería?');
        doc.text(margenPrimeraFila, (height / 2) - 30, '¿Con quién de tu familia tienes una mejor relación?');
        doc.text(margenPrimeraFila, (height / 2) - 5, 'Cuándo tienes un problema, ¿con quién lo platicas?');

        //  ----------------------------------------- DECÍMA SECCIÓN  ----------------------------------------- 
        doc.text(margenPrimeraFila, (height / 2) + 20, '¿Qué características de personalidad admiras de tu padre?');
        doc.text(margenPrimeraFila, (height / 2) + 40, '¿Qué defectos observas en él?');
        doc.text(margenPrimeraFila, (height / 2) + 60, '¿Qué características de personalidad admiras de tu madre?');
        doc.text(margenPrimeraFila, (height / 2) + 80, '¿Qué defectos observas en ella?');
        doc.text(margenPrimeraFila, (height / 2) + 100, '¿Cómo describirías la relación con tus hermanos?');

        doc.addPage();

        //  ----------------------------------------- ONCEAVA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 140, 190, 90, 'F');

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 130, 'Entorno social');
        
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 120, '¿Practicas algún deporte?');
        doc.text(margenPrimeraFila, (height / 2) - 110, '¿Te gusta leer?');
        doc.text(margenPrimeraFila, (height / 2) - 100, '¿Participas o asistes a alguna organización, club o grupo?');
        doc.text(margenPrimeraFila, (height / 2) - 90, '¿Has sido jefe o directivo de algún grupo o asociación?');
        doc.text(margenPrimeraFila, (height / 2) - 80, '¿Qué haces en tu tiempo libre?');
        doc.text(margenPrimeraFila, (height / 2) - 60, '¿Perteneces o has pertenecido a alguna organización?');

        //  ----------------------------------------- DOCEAVA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 43, 'Valoración personal');
        
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 37, '¿Cuáles crees que son tus principales cualidades o virtudes?');
        doc.text(margenPrimeraFila, (height / 2) - 10, '¿Cuál ha sido el mayor problema que has enfrentado?');

        doc.text(margenPrimeraFila, (height / 2) + 20, '¿Ya resolviste ese problema?');
        doc.text(margenPrimeraFila, (height / 2) + 50, '¿Cómo crees que te describirían tus amigos?');
        doc.text(margenPrimeraFila, (height / 2) + 70, '¿Si pudieras, qué cambiarías de ti?');
        doc.text(margenPrimeraFila, (height / 2) + 90, '¿Cuáles crees que son tus principales defectos o puntos débiles?');
      
        doc.addPage()
      //  ----------------------------------------- DOCEAVA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 140, 190, 85, 'F');

        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 130, '¿Cuáles son tus principales metas a corto plazo?');
        doc.text(margenPrimeraFila, (height / 2) - 110, '¿Cuáles son tus principales metas a mediano plazo?');

        doc.text(margenPrimeraFila, (height / 2) - 90, '¿Cuáles son tus principales metas a largo plazo?');
        doc.text(margenPrimeraFila, (height / 2) - 70, 'Describe detalladamente tus características de personalidad:');

      //  ----------------------------------------- DOCEAVA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 48, 'Religión');
        
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 40, '¿A qué religión perteneces?');


  //  ----------------------------------------- DOCEAVA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 30, 190, 180, 'F');
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 25, 'Información vocacional');

        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 20, '¿A cuál de las Universidades de la Red Anáhuac deseas ingresar?');
        doc.text(margenSegundaFila, (height / 2) - 20, 'Licenciatura que deseas estudiar:');

        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold')
        doc.text(margenPrimeraFila, (height / 2) - 5, '¿Por qué decidiste ingresar a esta carrera?');
        doc.text(margenPrimeraFila, (height / 2) + 15, '¿Qué expectativas tienes de la carrera?');
        doc.text(margenPrimeraFila, (height / 2) + 30, '¿Has recibido Orientación Vocacional?');
        doc.text(margenPrimeraFila, (height / 2) + 50, '¿Qué características y habilidades consideras tener para lograr éxito en la carrera que has elegido?');

        doc.text(margenPrimeraFila, (height / 2) + 70, 'Profesionalmente, ¿Cómo te ves en 6 años?');
        doc.text(margenPrimeraFila, (height / 2) + 90, '¿Tus padres están de acuerdo con tu elección?');
        doc.text(margenPrimeraFila, (height / 2) + 110, '¿Por qué tus padres piensan eso?');
        doc.text(margenPrimeraFila, (height / 2) + 130, '¿Qué o quiénes influyeron en la elección de tu carrera?');

        doc.save(`CuestionarioSolicitud.pdf`);
    }
}
