function PbButtonCtrl($scope, modalService, blockUI, $q) {

  'use strict';
  
    var vm = this;
    //GENERAR PDF - AR
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

        var countFinSeccionTutor;

        console.log("W "+width)
        console.log("H "+height)

        doc.addImage("widgets/customBtnADPDF/assets/img/LogoRUA.png", "PNG", ((width / 2) + 35), ((height / 2) - 128), 60, 20);
        
        doc.setFontSize(fontTitle);
        doc.setFont(undefined, 'bold');
        doc.text('REPORTE DE SOLICITUD', margenPrimeraFila, (height / 2) - 116);

        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 103, 190, 57, 'F');

        //  ----------------------------------------- PRIMERA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 93, 'Información de la solicitud');
        
        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 83, 'Campus donde cursará sus estudios:');
        doc.text(margenSegundaFila-5, (height / 2) - 83, 'Licenciatura:');
        doc.text(margenPrimeraFila, (height / 2) - 71, 'Período de ingreso:');
        doc.text(margenSegundaFila-5, (height / 2) - 71, 'Realizo proceso de admisión en otra');
        doc.text(margenSegundaFila-5, (height / 2) - 66, 'universidad de la red Anáhuac:');
        doc.text(margenPrimeraFila, (height / 2) - 59, 'Lugar donde realizarás tu examen:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catCampusEstudio.descripcion, respuestasPrimeraFila, (height / 2) - 78);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catGestionEscolar.nombre, respuestasSegundaFila-5, (height / 2) - 78);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catPeriodo.descripcion, respuestasPrimeraFila, (height / 2) - 66);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion, respuestasSegundaFila-5, (height / 2) - 61);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catCampus.descripcion, respuestasPrimeraFila, (height / 2) - 54);
    
        //  ----------------------------------------- SEGUNDA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 38, 'Información personal');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 28, 'Nombre (s):');
        doc.text(margenFilaIntermedia, (height / 2) - 28, 'Apellido paterno:');
        doc.text(margenSegundaFila, (height / 2) - 28, 'Apellido materno:');

        doc.text(margenPrimeraFila, (height / 2) - 16, 'Correo eletrónico:');
        doc.text(margenFilaIntermedia, (height / 2) - 16, 'Fecha de nacimiento:');
        doc.text(margenSegundaFila, (height / 2) - 16, 'Sexo:');

        doc.text(margenPrimeraFila, (height / 2) - 4, 'Nacionalidad:');
        doc.text(margenFilaIntermedia, (height / 2) - 4, 'Religión:');
        doc.text(margenSegundaFila, (height / 2) - 4, 'CURP:');

        doc.text(margenPrimeraFila, (height / 2) + 8, 'Número de pasaporte:');
        doc.text(margenFilaIntermedia, (height / 2) + 8, 'Estado civil:');
        doc.text(margenSegundaFila, (height / 2) + 8, 'Teléfono celular:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.primerNombre+" "+$scope.properties.PDFobjSolicitudDeAdmision.segundoNombre, respuestasPrimeraFila, (height / 2) - 23);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.apellidoPaterno, respuestasFilaIntermedia, (height / 2) - 23);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.apellidoMaterno, respuestasSegundaFila, (height / 2) - 23);

        doc.text($scope.properties.PDFobjSolicitudDeAdmision.correoElectronico, respuestasPrimeraFila, (height / 2) - 11);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.fechaNacimiento, respuestasFilaIntermedia, (height / 2) - 11);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catSexo.descripcion, respuestasSegundaFila, (height / 2) - 11);

        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catNacionalidad.descripcion, respuestasPrimeraFila, (height / 2) + 1);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catReligion.descripcion, respuestasFilaIntermedia, (height / 2) + 1);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.curp, respuestasSegundaFila, (height / 2) + 1);

        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.curp == "" ? "N/A" : $scope.properties.PDFobjSolicitudDeAdmision.curp), respuestasPrimeraFila, (height / 2) + 13);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catEstadoCivil.descripcion, respuestasFilaIntermedia, (height / 2) + 13);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.telefonoCelular, respuestasSegundaFila, (height / 2) + 13);

        //  ----------------------------------------- TERCERA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) + 18, 190, 75, 'F');

        doc.setFontSize(fontTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 28, 'Domicilio permanente');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 38, 'País:');
        doc.text(margenFilaIntermedia, (height / 2) + 38, 'Código postal:');
        doc.text(margenSegundaFila, (height / 2) + 38, 'Estado:');

        doc.text(margenPrimeraFila, (height / 2) + 52.5, 'Ciudad:');
        doc.text(margenFilaIntermedia, (height / 2) + 50, 'Ciudad/Municipio/');
        doc.text(margenFilaIntermedia, (height / 2) + 55, 'Delegación/Poblado:');
        doc.text(margenSegundaFila, (height / 2) + 52.5, 'Colonia:');

        doc.text(margenPrimeraFila, (height / 2) + 67, 'Calle:');
        doc.text(margenFilaIntermedia, (height / 2) + 67, 'Entre calles:');
        doc.text(margenSegundaFila, (height / 2) + 67, 'Núm. Exterior');

        doc.text(margenPrimeraFila, (height / 2) + 79, 'Núm. Interior:');
        doc.text(margenFilaIntermedia, (height / 2) + 79, 'Teléfono:');
        doc.text(margenSegundaFila, (height / 2) + 79, 'Otro teléfono de contacto:');

         //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catPais.descripcion, respuestasPrimeraFila, (height / 2) + 43);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.codigoPostal, respuestasFilaIntermedia, (height / 2) + 43);
        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.catEstado.descripcion == ""  ? $scope.properties.PDFobjSolicitudDeAdmision.estadoExtranjero : $scope.properties.PDFobjSolicitudDeAdmision.catEstado.descripcion), respuestasSegundaFila, (height / 2) + 43);

        doc.text($scope.properties.PDFobjSolicitudDeAdmision.ciudad, respuestasPrimeraFila, (height / 2) + 56.5);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.delegacionMunicipio, respuestasFilaIntermedia, (height / 2) + 60);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.colonia, respuestasSegundaFila, (height / 2) + 56.5);

        doc.text($scope.properties.PDFobjSolicitudDeAdmision.calle, respuestasPrimeraFila, (height / 2) + 72);
        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.calle2 ==  null || $scope.properties.PDFobjSolicitudDeAdmision.calle2 ==  "" ? "N/A" : $scope.properties.PDFobjSolicitudDeAdmision.calle2), respuestasFilaIntermedia, (height / 2) + 72);
        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.numExterior == "" ? "N/A" : $scope.properties.PDFobjSolicitudDeAdmision.numExterior), respuestasSegundaFila, (height / 2) + 72);

        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.numInterior == "" ? "N/A" : $scope.properties.PDFobjSolicitudDeAdmision.numInterior), respuestasPrimeraFila, (height / 2) + 84);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.telefono, respuestasFilaIntermedia, (height / 2) + 84);
        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.otroTelefonoContacto == "" ? "N/A" : $scope.properties.PDFobjSolicitudDeAdmision.otroTelefonoContacto), respuestasSegundaFila, (height / 2) + 84);

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

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catBachilleratos.descripcion, respuestasPrimeraFila, (height / 2) - 101);
        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.catBachilleratos.pais == null ? $scope.properties.PDFobjSolicitudDeAdmision.paisBachillerato : $scope.properties.PDFobjSolicitudDeAdmision.catBachilleratos.pais), respuestasFilaIntermedia, (height / 2) - 101);
        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.catBachilleratos.estado == null ? $scope.properties.PDFobjSolicitudDeAdmision.estadoBachillerato : $scope.properties.PDFobjSolicitudDeAdmision.catBachilleratos.estado), respuestasSegundaFila, (height / 2) - 101);

        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.catBachilleratos.ciudad == null ? $scope.properties.PDFobjSolicitudDeAdmision.ciudad : $scope.properties.PDFobjSolicitudDeAdmision.catBachilleratos.ciudad), respuestasPrimeraFila, (height / 2) - 89);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.promedioGeneral, respuestasFilaIntermedia, (height / 2) - 89);
        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.resultadoPAA <= 0 ? "N/A" : $scope.properties.PDFobjSolicitudDeAdmision.resultadoPAA), respuestasSegundaFila, (height / 2) - 89);
    
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

                //Respuestas
                doc.setFont(undefined, 'normal');
                doc.text($scope.properties.PDFobjTutor[0].catTitulo.descripcion, respuestasPrimeraFila, (height / 2) - 59);
                doc.text($scope.properties.PDFobjTutor[0].nombre, respuestasFilaIntermedia, (height / 2) - 59);
                doc.text($scope.properties.PDFobjTutor[0].apellidos, respuestasSegundaFila, (height / 2) - 59);

                doc.text($scope.properties.PDFobjTutor[0].catParentezco.descripcion, respuestasPrimeraFila, (height / 2) - 47);
                doc.text($scope.properties.PDFobjTutor[0].correoElectronico, respuestasFilaIntermedia, (height / 2) - 47);
                doc.text($scope.properties.PDFobjTutor[0].catEscolaridad.descripcion, respuestasSegundaFila, (height / 2) - 47);

                doc.text(($scope.properties.PDFobjTutor[0].puesto == "" ? "No trabaja" : $scope.properties.PDFobjTutor[0].puesto), respuestasPrimeraFila, (height / 2) - 35);
                doc.text(($scope.properties.PDFobjTutor[0].empresaTrabaja == "" ? "No trabaja" : $scope.properties.PDFobjTutor[0].empresaTrabaja), respuestasFilaIntermedia, (height / 2) - 35);
                doc.text(($scope.properties.PDFobjTutor[0].catCampusEgreso == null ? "No" : $scope.properties.PDFobjTutor[0].catCampusEgreso.descripcion), respuestasSegundaFila, (height / 2) - 35);

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

                //Respuestas
                doc.setFont(undefined, 'normal');
                doc.text($scope.properties.PDFobjTutor[0].catPais.descripcion, respuestasPrimeraFila, (height / 2) - 5);
                doc.text($scope.properties.PDFobjTutor[0].codigoPostal, respuestasFilaIntermedia, (height / 2) - 5);
                doc.text($scope.properties.PDFobjTutor[0].catEstado.descripcion, respuestasSegundaFila, (height / 2) - 5);

                doc.text($scope.properties.PDFobjTutor[0].ciudad, respuestasPrimeraFila, (height / 2) + 7.5);
                doc.text($scope.properties.PDFobjTutor[0].delegacionMunicipio, respuestasFilaIntermedia, (height / 2) + 12);
                doc.text($scope.properties.PDFobjTutor[0].colonia, respuestasSegundaFila, (height / 2) + 7.5);

                doc.text($scope.properties.PDFobjTutor[0].calle, respuestasPrimeraFila, (height / 2) + 24);
                doc.text(($scope.properties.PDFobjTutor[0].numeroExterior == "" ? "N/A" : $scope.properties.PDFobjTutor[0].numeroExterior), respuestasFilaIntermedia, (height / 2) + 24);
                doc.text(($scope.properties.PDFobjTutor[0].numeroInterior == "" ? "N/A" : $scope.properties.PDFobjTutor[0].numeroInterior), respuestasSegundaFila, (height / 2) + 24);
                doc.text($scope.properties.PDFobjTutor[0].telefono, respuestasPrimeraFila, countFinSeccionTutor = ((height / 2) + 36));

                console.log(countFinSeccionTutor)

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

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjPadreCatTitulo.descripcion, respuestasPrimeraFila, (height / 2) + 70);
        doc.text($scope.properties.PDFobjPadre.nombre, respuestasFilaIntermedia, (height / 2) + 70);
        doc.text($scope.properties.PDFobjPadre.apellidos, respuestasSegundaFila, (height / 2) + 70);

        if($scope.properties.PDFobjPadre.vive.descripcion == "Sí") {
        doc.text($scope.properties.PDFobjPadre.correoElectronico, respuestasPrimeraFila, (height / 2) + 82);
        doc.text($scope.properties.PDFobjPadreCatEscolaridad.descripcion, respuestasFilaIntermedia, (height / 2) + 82);
        doc.text(($scope.properties.PDFobjPadreCatCampusEgreso == null ? "No" : $scope.properties.PDFobjPadreCatCampusEgreso.descripcion), respuestasSegundaFila, (height / 2) + 82);

        doc.text(($scope.properties.PDFobjPadre.puesto == null || $scope.properties.PDFobjPadre.puesto == "" ? "No trabaja" : $scope.properties.PDFobjPadre.puesto), respuestasPrimeraFila, (height / 2) + 94);
        doc.text(($scope.properties.PDFobjPadre.empresaTrabaja == null || $scope.properties.PDFobjPadre.empresaTrabaja == "" ? "No trabaja" : $scope.properties.PDFobjPadre.empresaTrabaja), respuestasFilaIntermedia, (height / 2) + 94);
        } else {
            doc.setFont(undefined, 'normal');
            doc.text("Finado", respuestasPrimeraFila, (height / 2) + 82);
            doc.text("Finado", respuestasFilaIntermedia, (height / 2) + 82);
            doc.text("Finado", respuestasSegundaFila, (height / 2) + 82);

            doc.text("Finado", respuestasPrimeraFila, (height / 2) + 94);
            doc.text("Finado", respuestasFilaIntermedia, (height / 2) + 94);
        }

        doc.addPage();

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

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjPadre.catPais.descripcion, respuestasPrimeraFila, (height / 2) - 101);
        doc.text($scope.properties.PDFobjPadre.codigoPostal, respuestasFilaIntermedia, (height / 2) - 101);
        doc.text($scope.properties.PDFobjPadre.catEstado.descripcion, respuestasSegundaFila, (height / 2) - 101);

        doc.text($scope.properties.PDFobjPadre.ciudad, respuestasPrimeraFila, (height / 2) - 86.5);
        doc.text($scope.properties.PDFobjPadre.delegacionMunicipio, respuestasFilaIntermedia, (height / 2) - 83);
        doc.text($scope.properties.PDFobjPadre.colonia, respuestasSegundaFila, (height / 2) - 86.5);

        doc.text($scope.properties.PDFobjPadre.calle, respuestasPrimeraFila, (height / 2) - 71);
        doc.text(($scope.properties.PDFobjPadre.numeroExterior == "" ? "N/A" : $scope.properties.PDFobjPadre.numeroExterior), respuestasFilaIntermedia, (height / 2) - 71);
        doc.text(($scope.properties.PDFobjPadre.numeroInterior == "" ? "N/A" : $scope.properties.PDFobjPadre.numeroInterior), respuestasSegundaFila, (height / 2) - 71);
        doc.text($scope.properties.PDFobjPadre.telefono, respuestasPrimeraFila, (height / 2) - 59);

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

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjMadreCatTitulo.descripcion, respuestasPrimeraFila, (height / 2) - 26);
        doc.text($scope.properties.PDFobjMadre.nombre, respuestasFilaIntermedia, (height / 2) - 26);
        doc.text($scope.properties.PDFobjMadre.apellidos, respuestasSegundaFila, (height / 2) - 26);

        if($scope.properties.PDFobjMadre.vive.descripcion == "Sí") {
        doc.text($scope.properties.PDFobjMadre.correoElectronico, respuestasPrimeraFila, (height / 2) - 14);
        doc.text($scope.properties.PDFobjMadreCatEscolaridad.descripcion, respuestasFilaIntermedia, (height / 2) - 14);
        doc.text(($scope.properties.PDFobjMadreCatCampusEgreso == null ? "No" : $scope.properties.PDFobjMadreCatCampusEgreso.descripcion), respuestasSegundaFila, (height / 2) - 14);

        doc.text(($scope.properties.PDFobjMadre.puesto == null || $scope.properties.PDFobjMadre.puesto == "" ? "No trabaja" : $scope.properties.PDFobjMadre.puesto), respuestasPrimeraFila, (height / 2) - 2);
        doc.text(($scope.properties.PDFobjMadre.empresaTrabaja == null || $scope.properties.PDFobjMadre.empresaTrabaja == "" ? "No trabaja" : $scope.properties.PDFobjMadre.empresaTrabaja), respuestasFilaIntermedia, (height / 2) - 2);
        } else {
            doc.setFont(undefined, 'normal');
            doc.text("Finado", respuestasPrimeraFila, (height / 2) - 14);
            doc.text("Finado", respuestasFilaIntermedia, (height / 2) - 14);
            doc.text("Finado", respuestasSegundaFila, (height / 2) - 14);

            doc.text("Finado", respuestasPrimeraFila, (height / 2) - 2);
            doc.text("Finado", respuestasSegundaFila, (height / 2) - 2);
        }

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

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjMadre.catPais.descripcion, respuestasPrimeraFila, (height / 2) + 23);
        doc.text($scope.properties.PDFobjMadre.codigoPostal, respuestasFilaIntermedia, (height / 2) + 23);
        doc.text($scope.properties.PDFobjMadre.catEstado.descripcion, respuestasSegundaFila, (height / 2) + 23);

        doc.text($scope.properties.PDFobjMadre.ciudad, respuestasPrimeraFila, (height / 2) + 35.5);
        doc.text($scope.properties.PDFobjMadre.delegacionMunicipio, respuestasFilaIntermedia, (height / 2) + 40);
        doc.text($scope.properties.PDFobjMadre.colonia, respuestasSegundaFila, (height / 2) + 35.5);

        doc.text($scope.properties.PDFobjMadre.calle, respuestasPrimeraFila, (height / 2) + 52);
        doc.text(($scope.properties.PDFobjMadre.numeroExterior == "" ? "N/A" : $scope.properties.PDFobjMadre.numeroExterior), respuestasFilaIntermedia, (height / 2) + 52);
        doc.text(($scope.properties.PDFobjMadre.numeroInterior == "" ? "N/A" : $scope.properties.PDFobjMadre.numeroInterior), respuestasSegundaFila, (height / 2) + 52);
        doc.text($scope.properties.PDFobjMadre.telefono, respuestasPrimeraFila, (height / 2) + 64);

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

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjcasosDeEmergencia[0].nombre, respuestasPrimeraFila, (height / 2) + 94);
        doc.text($scope.properties.PDFobjcasosDeEmergencia[0].parentesco, respuestasFilaIntermedia, (height / 2) + 94);
        doc.text($scope.properties.PDFobjcasosDeEmergencia[0].telefono, respuestasSegundaFila, (height / 2) + 94);

        doc.text($scope.properties.PDFobjcasosDeEmergencia[0].telefonoCelular, respuestasPrimeraFila, (height / 2) + 106);

        doc.save(`CuestionarioSolicitud.pdf`);
    }
}
