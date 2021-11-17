function PbButtonCtrl($scope, modalService, blockUI, $q) {

  'use strict';
  
    var vm = this;
    //GENERAR PDF
    $scope.generatePDF = function() {
        var doc = new jspdf.jsPDF('p', 'mm', 'a4');
        var width = doc.internal.pageSize.getWidth();
        var height = doc.internal.pageSize.getHeight();

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
        doc.text(margenSegundaFila, (height / 2) - 83, 'Licenciatura:');
        doc.text(margenPrimeraFila, (height / 2) - 71, 'Período de ingreso:');
        doc.text(margenSegundaFila, (height / 2) - 71, 'Realizo proceso de admisión en otra');
        doc.text(margenSegundaFila, (height / 2) - 66, 'universidad de la red Anáhuac:');
        doc.text(margenPrimeraFila, (height / 2) - 59, 'Lugar donde realizarás tu examen:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catCampusEstudio.descripcion, respuestasPrimeraFila, (height / 2) - 78);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catGestionEscolar.nombre, respuestasSegundaFila, (height / 2) - 78);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catPeriodo.descripcion, respuestasPrimeraFila, (height / 2) - 66);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion, respuestasSegundaFila, (height / 2) - 61);
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
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.paisBachillerato, respuestasFilaIntermedia, (height / 2) - 101);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.estadoBachillerato, respuestasSegundaFila, (height / 2) - 101);

        doc.text($scope.properties.PDFobjSolicitudDeAdmision.ciudadBachillerato, respuestasPrimeraFila, (height / 2) - 89);
        doc.text($scope.properties.PDFobjSolicitudDeAdmision.promedioGeneral, respuestasFilaIntermedia, (height / 2) - 89);
        doc.text(($scope.properties.PDFobjSolicitudDeAdmision.resultadoPAA <= 0 ? "N/A" : $scope.properties.PDFobjSolicitudDeAdmision.resultadoPAA), respuestasSegundaFila, (height / 2) - 89);
    
        //  ----------------------------------------- QUINTA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 84, 190, 75, 'F');

        $scope.properties.PDFobjTutor.forEach(function(PDFitem, i, objeto) {
            doc.setFontSize(fontSubTitle);
            doc.setFont(undefined, 'bold');
            doc.text(margenPrimeraFila, (height / 2) - 74, 'Información del tutor');

            //Encabezados
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
            doc.text($scope.properties.PDFitem[i].catTitulo.descripcion, respuestasPrimeraFila, (height / 2) - 59);
            doc.text($scope.properties.PDFitem[i].nombre, respuestasFilaIntermedia, (height / 2) - 59);
            doc.text($scope.properties.PDFitem[i].apellidos, respuestasSegundaFila, (height / 2) - 59);

            doc.text($scope.properties.PDFitem[i].catParentezco.descripcion, respuestasPrimeraFila, (height / 2) - 47);
            doc.text($scope.properties.PDFitem[i].correoElectronico, respuestasFilaIntermedia, (height / 2) - 47);
            doc.text($scope.properties.PDFitem[i].catEscolaridad.descripcion, respuestasSegundaFila, (height / 2) - 47);

            doc.text($scope.properties.PDFitem[i].puesto, respuestasPrimeraFila, (height / 2) - 35);
            doc.text($scope.properties.PDFitem[i].empresaTrabaja, respuestasFilaIntermedia, (height / 2) - 35);
            doc.text($scope.properties.PDFitem[i].catCampusEgreso.descripcion, respuestasSegundaFila, (height / 2) - 35);

            doc.setFontSize(fontSubTitle);
            doc.setFont(undefined, 'bold');
            doc.text(margenPrimeraFila, (height / 2) - 20, 'Domicilio permanente del tutor');

            //Encabezados
            doc.setFontSize(fontText);
            doc.setFont(undefined, 'bold');
            doc.text(margenPrimeraFila, (height / 2) - 10, 'País:');
            doc.text(margenFilaIntermedia, (height / 2) - 10, 'Código postal:');
            doc.text(margenSegundaFila, (height / 2) - 10, 'Estado:');

            doc.text(margenPrimeraFila, (height / 2) + 2, 'Ciudad:');
            doc.text(margenFilaIntermedia, (height / 2) + 2, 'Ciudad/Municipio/');
            doc.text(margenFilaIntermedia, (height / 2) + 7, 'Delegación/Poblado:');
            doc.text(margenPrimeraFila, (height / 2) + 2, 'Colonia:');

            doc.text(margenPrimeraFila, (height / 2) + 10, 'Calle:');
            doc.text(margenFilaIntermedia, (height / 2) + 10, 'Núm. Exterior:');
            doc.text(margenSegundaFila, (height / 2) + 10, 'Núm. Interior:');
            doc.text(margenPrimeraFila, (height / 2) + 22, 'Teléfono:');

            //Respuestas
            doc.setFont(undefined, 'normal');
            doc.text($scope.properties.PDFitem[i].catPais.descripcion, respuestasPrimeraFila, (height / 2) - 5);
            doc.text($scope.properties.PDFitem[i].codigoPostal, respuestasFilaIntermedia, (height / 2) - 5);
            doc.text($scope.properties.PDFitem[i].catEstado.descripcion, respuestasSegundaFila, (height / 2) - 5);

            doc.text($scope.properties.PDFitem[i].ciudad, respuestasPrimeraFila, (height / 2) + 7);
            doc.text($scope.properties.PDFitem[i].delegacionMunicipio, respuestasFilaIntermedia, (height / 2) + 11);
            doc.text($scope.properties.PDFitem[i].colonia, respuestasSegundaFila, (height / 2) + 7);

            doc.text($scope.properties.PDFitem[i].calle, respuestasPrimeraFila, (height / 2) + 15);
            doc.text($scope.properties.PDFitem[i].numExterior.toString(), respuestasFilaIntermedia, (height / 2) + 15);
            doc.text($scope.properties.PDFitem[i].numInterior.toString(), respuestasSegundaFila, (height / 2) + 15);
            doc.text($scope.properties.PDFitem[i].telefono, respuestasPrimeraFila, (height / 2) + 27);

        })
        /*doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFitem.catTitulo.descripcion, respuestasPrimeraFila, (height / 2) - 59);
        doc.text($scope.properties.PDFitem.nombre, respuestasFilaIntermedia, (height / 2) - 59);
        doc.text($scope.properties.PDFitem.apellidos, respuestasSegundaFila, (height / 2) - 59);

        doc.text($scope.properties.PDFitem.catParentezco.descripcion, respuestasPrimeraFila, (height / 2) - 47);
        doc.text($scope.properties.PDFitem.correoElectronico, respuestasFilaIntermedia, (height / 2) - 47);
        doc.text($scope.properties.PDFitem.catEscolaridad.descripcion, respuestasSegundaFila, (height / 2) - 47);

        doc.text($scope.properties.PDFitem.puesto, respuestasPrimeraFila, (height / 2) - 35);
        doc.text($scope.properties.PDFitem.empresaTrabaja, respuestasFilaIntermedia, (height / 2) - 35);
        doc.text($scope.properties.PDFitem.catCampusEgreso.descripcion, respuestasSegundaFila, (height / 2) - 35);

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 20, 'Domicilio permanente del tutor');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 10, 'País:');
        doc.text(margenFilaIntermedia, (height / 2) - 10, 'Código postal:');
        doc.text(margenSegundaFila, (height / 2) - 10, 'Estado:');

        doc.text(margenPrimeraFila, (height / 2) + 2, 'Ciudad:');
        doc.text(margenFilaIntermedia, (height / 2) + 2, 'Ciudad/Municipio/');
        doc.text(margenFilaIntermedia, (height / 2) + 7, 'Delegación/Poblado:');
        doc.text(margenPrimeraFila, (height / 2) + 2, 'Colonia:');

        doc.text(margenPrimeraFila, (height / 2) + 10, 'Calle:');
        doc.text(margenFilaIntermedia, (height / 2) + 10, 'Núm. Exterior:');
        doc.text(margenSegundaFila, (height / 2) + 10, 'Núm. Interior:');
        doc.text(margenPrimeraFila, (height / 2) + 10, 'Teléfono:');*/

        //  ----------------------------------------- QUINTA SECCIÓN  ----------------------------------------- 
        /*doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 74, 'Información del padre');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 64, 'Título:');
        doc.text(margenFilaIntermedia, (height / 2) - 64, 'Nombre (s):');
        doc.text(margenSegundaFila, (height / 2) - 64, 'Apellido (s):');

        doc.text(margenFilaIntermedia, (height / 2) - 52, 'Correo eletrónico:');
        doc.text(margenSegundaFila, (height / 2) - 52, 'Escolaridad del tutor:');
        doc.text(margenSegundaFila, (height / 2) - 40, 'Universidad Anáhuac:');

        doc.text(margenPrimeraFila, (height / 2) - 40, 'Ocupación del tutor:');
        doc.text(margenFilaIntermedia, (height / 2) - 40, 'Empresa:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.properties.PDFitem.catTitulo.descripcion, respuestasPrimeraFila, (height / 2) - 59);
        doc.text($scope.properties.PDFitem.nombre, respuestasFilaIntermedia, (height / 2) - 59);
        doc.text($scope.properties.PDFitem.apellidos, respuestasSegundaFila, (height / 2) - 59);

        doc.text($scope.properties.PDFitem.catParentezco.descripcion, respuestasPrimeraFila, (height / 2) - 47);
        doc.text($scope.properties.PDFitem.correoElectronico, respuestasFilaIntermedia, (height / 2) - 47);
        doc.text($scope.properties.PDFitem.catEscolaridad.descripcion, respuestasSegundaFila, (height / 2) - 47);

        doc.text($scope.properties.PDFitem.puesto, respuestasPrimeraFila, (height / 2) - 35);
        doc.text($scope.properties.PDFitem.empresaTrabaja, respuestasFilaIntermedia, (height / 2) - 35);
        doc.text($scope.properties.PDFitem.catCampusEgreso.descripcion, respuestasSegundaFila, (height / 2) - 35);*/
        doc.save(`CuestionarioSolicitud.pdf`);
    }
}