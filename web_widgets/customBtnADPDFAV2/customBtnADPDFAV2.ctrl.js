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
        doc.text(margenPrimeraFila, (height / 2) - 47, 'Residencia');
        doc.text(margenPrimeraFila, (height / 2) - 42, 'Periodo de ingreso:');

        doc.setDrawColor(115, 66, 34)
        //doc.rect(margenSegundaFila,((height / 2)-84),35,45)
        doc.rect(157.5,((height / 2)-84),35,45)
    
        //  ----------------------------------------- SEGUNDA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 12, 'Información familiar');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 2, '¿Quiénes conforman tu familia?:');
        



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
        


        doc.addPage();
        //  ----------------------------------- NUEVA HOJA Y CUARTA SECCIÓN  ----------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 116, 'Información del bachillerato');
        
        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 106, 'Nombre bachillerato:');
        doc.text(margenFilaIntermedia, (height / 2) - 106, 'País de tu bachillerato:');
        doc.text(margenSegundaFila, (height / 2) - 106, 'Estado de tu bachillerato:');

        doc.text(margenPrimeraFila, (height / 2) - 94, 'Ciudad de tu bachillerato');
        doc.text(margenFilaIntermedia, (height / 2) - 94, 'Promedio general:');
        doc.text(margenSegundaFila, (height / 2) - 94, 'Resultado PAA:');

        
        //  ----------------------------------------- QUINTA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 84, 190, 130, 'F');

        //$scope.properties.PDFobjTutor.forEach(function(PDFitem, i, objeto) {


                doc.setFontSize(fontSubTitle);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 74, 'Información del tutor');

                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 64, 'Título:');
                doc.text(margenFilaIntermedia, (height / 2) - 64, 'Nombre (s):');
                doc.text(margenSegundaFila, (height / 2) - 64, 'Apellido (s):');

              

                doc.text(margenPrimeraFila, (height / 2) - 52, 'Parentesco:');
                doc.text(margenFilaIntermedia, (height / 2) - 52, 'Correo eletrónico:');
                doc.text(margenSegundaFila, (height / 2) - 52, 'Escolaridad del tutor:');

                doc.text(margenPrimeraFila, (height / 2) - 40, 'Ocupación del tutor:');
                doc.text(margenFilaIntermedia, (height / 2) - 40, 'Empresa:');
                doc.text(margenSegundaFila, (height / 2) - 40, 'Universidad Anáhuac:');

               

                doc.setFontSize(fontSubTitle);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 20, 'Domicilio permanente del tutor');

                //Encabezados
                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 10, 'País:');
                doc.text(margenFilaIntermedia, (height / 2) - 10, 'Código postal:');
                doc.text(margenSegundaFila, (height / 2) - 10, 'Estado:');

                doc.text(margenPrimeraFila, (height / 2) + 3.5, 'Ciudad:');
                doc.text(margenFilaIntermedia, (height / 2) + 2, 'Ciudad/Municipio/');
                doc.text(margenFilaIntermedia, (height / 2) + 7, 'Delegación/Poblado:');
                doc.text(margenSegundaFila, (height / 2) + 3.5, 'Colonia:');

                doc.text(margenPrimeraFila, (height / 2) + 19, 'Calle:');
                doc.text(margenFilaIntermedia, (height / 2) + 19, 'Núm. Exterior:');
                doc.text(margenSegundaFila, (height / 2) + 19, 'Núm. Interior:');
                doc.text(margenPrimeraFila, (height / 2) + 31, 'Teléfono:');

                

        //})

        //  ----------------------------------------- SEXTA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 55, 'Información del padre');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 65, 'Título:');
        doc.text(margenFilaIntermedia, (height / 2) + 65, 'Nombre (s):');
        doc.text(margenSegundaFila, (height / 2) + 65, 'Apellido (s):');

        doc.text(margenPrimeraFila, (height / 2) + 77, 'Correo eletrónico:');
        doc.text(margenFilaIntermedia, (height / 2) + 77, 'Escolaridad del padre:');
        doc.text(margenSegundaFila, (height / 2) + 77, 'Universidad Anáhuac:');

        doc.text(margenPrimeraFila, (height / 2) + 89, 'Ocupación del padre:');
        doc.text(margenFilaIntermedia, (height / 2) + 89, 'Empresa:');

       

        //  ----------------------------------- NUEVA HOJA Y SEPTIMA SECCIÓN  ----------------------------------- 

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 116, 'Domicilio permanente del padre');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 106, 'País:');
        doc.text(margenFilaIntermedia, (height / 2) - 106, 'Código postal:');
        doc.text(margenSegundaFila, (height / 2) - 106, 'Estado:');

        doc.text(margenPrimeraFila, (height / 2) - 90.5, 'Ciudad:');
        doc.text(margenFilaIntermedia, (height / 2) - 93, 'Ciudad/Municipio/');
        doc.text(margenFilaIntermedia, (height / 2) - 88, 'Delegación/Poblado:');
        doc.text(margenSegundaFila, (height / 2) - 90.5, 'Colonia:');

        doc.text(margenPrimeraFila, (height / 2) - 76, 'Calle:');
        doc.text(margenFilaIntermedia, (height / 2) - 76, 'Núm. Exterior:');
        doc.text(margenSegundaFila, (height / 2) - 76, 'Núm. Interior:');
        doc.text(margenPrimeraFila, (height / 2) - 64, 'Teléfono:');

        
        //  ----------------------------------------- OCTAVA SECCIÓN  ----------------------------------------- 

        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 51, 190, 120, 'F');

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 41, 'Información de la madre');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 31, 'Título:');
        doc.text(margenFilaIntermedia, (height / 2) - 31, 'Nombre (s):');
        doc.text(margenSegundaFila, (height / 2) - 31, 'Apellido (s):');

        doc.text(margenPrimeraFila, (height / 2) - 19, 'Correo eletrónico:');
        doc.text(margenFilaIntermedia, (height / 2) - 19, 'Escolaridad del tutor:');
        doc.text(margenSegundaFila, (height / 2) - 19, 'Universidad Anáhuac:');

        doc.text(margenPrimeraFila, (height / 2) - 7, 'Ocupación del tutor:');
        doc.text(margenFilaIntermedia, (height / 2) - 7, 'Empresa:');

       
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 8, 'Domicilio permanente de la madre');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 18, 'País:');
        doc.text(margenFilaIntermedia, (height / 2) + 18, 'Código postal:');
        doc.text(margenSegundaFila, (height / 2) + 18, 'Estado:');

        doc.text(margenPrimeraFila, (height / 2) + 31.5, 'Ciudad:');
        doc.text(margenFilaIntermedia, (height / 2) + 30, 'Ciudad/Municipio/');
        doc.text(margenFilaIntermedia, (height / 2) + 35, 'Delegación/Poblado:');
        doc.text(margenSegundaFila, (height / 2) + 31.5, 'Colonia:');

        doc.text(margenPrimeraFila, (height / 2) + 47, 'Calle:');
        doc.text(margenFilaIntermedia, (height / 2) + 47, 'Núm. Exterior:');
        doc.text(margenSegundaFila, (height / 2) + 47, 'Núm. Interior:');
        doc.text(margenPrimeraFila, (height / 2) + 59, 'Teléfono:');

       
        

        //  ----------------------------------------- NOVENA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 79, 'Contacto(s) en caso de emergencia');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 89, 'Contacto de emergencia:');
        doc.text(margenFilaIntermedia, (height / 2) + 89, 'Parentesco:');
        doc.text(margenSegundaFila, (height / 2) + 89, 'Teléfono de emergencia:');

        doc.text(margenPrimeraFila, (height / 2) + 101, 'Celular de emergencia:');


        doc.save(`CuestionarioSolicitud.pdf`);
    }
}
