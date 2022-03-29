function PbButtonCtrl($scope, $filter, $http, modalService, blockUI, $q) {
    $scope.datosUsuario = {};
    $scope.datosPadres = [];
    $scope.datosHermanos = [];
    $scope.datosFuentesInfluyeron = [];
    $scope.datosRasgos = [];
    $scope.datosCapacidad = [];
    $scope.datosSaludPSeccion = [];
    $scope.datosSaludSSeccion = [];
    $scope.datosBitacoraComentarios = [];
    $scope.salud;
    $scope.comentarios = [];
    $scope.conclusiones_recomendaciones;
    $scope.interpretacion;
    $scope.cursos = {
        "PDU": false,
        "SSE": false,
        "PDP": false,
        "PCA": false
    }

    $scope.cargarDatos = function () {
        doRequest("GET", "../API/extension/AnahuacRestGet?url=getInfoReportes&p=0&c=9999&usuario=" + $scope.properties.usuario + "&intentos=" + $scope.properties.intentos, 1);
    }

    function doRequest(method, url, numero) {
        blockUI.start();
        //data: angular.copy($scope.properties.dataToSend),
        var req = {
            method: method,
            url: url
        };

        return $http(req)
            .success(function (data, status) {
                switch (numero) {
                    case 1:
                        if (data.length > 0) {
                            $scope.datosUsuario = data[0];
                        }
                        doRequest("GET", "../API/extension/AnahuacRestGet?url=getInfoRelativos&p=0&c=9999&caseid=" + $scope.properties.caseId, 2);
                        break;
                    case 2:
                        $scope.datosPadres = data;
                        doRequest("GET", "../API/extension/AnahuacRestGet?url=getInfoRelativosHermanos&p=0&c=9999&caseid=" + $scope.properties.caseId, 3);
                        break;
                    case 3:
                        if (data.length > 0) {
                            $scope.datosHermanos = data;
                        }
                        doRequest("GET", "../API/extension/AnahuacRestGet?url=getInfoFuentesInfluyeron&p=0&c=9999&caseid=" + $scope.properties.caseId + "&intentos=" + $scope.properties.intentos, 4);
                        break;
                    case 4:
                        if (data.length > 0) {
                            $scope.getInfoFuentesInfluyeron = data;
                        }
                        doRequest("GET", "../API/extension/AnahuacRestGet?url=getInfoRasgos&p=0&c=9999&caseid=" + $scope.properties.caseId + "&intentos=" + $scope.properties.intentos, 5);
                        break;
                    case 5:
                        if (data.length > 0) {
                            $scope.datosRasgos = data;
                        }
                        doRequest("GET", "../API/extension/AnahuacRestGet?url=getInfoCapacidadAdaptacion&p=0&c=9999&caseid=" + $scope.properties.caseId + "&intentos=" + $scope.properties.intentos, 6);
                        break;
                    case 6:
                        if (data.length > 0) {
                            $scope.datosCapacidad = data;
                        }
                        doRequest("GET", "../API/extension/AnahuacRestGet?url=getInfoSaludPSeccion&p=0&c=9999&caseid=" + $scope.properties.caseId, 7);
                        break;
                    case 7:
                        if (data.length > 0) {
                            $scope.datosSaludPSeccion = data;
                        }
                        doRequest("GET", "../API/extension/AnahuacRestGet?url=getInfoSaludSSeccion&p=0&c=9999&caseid=" + $scope.properties.caseId + "&intentos=" + $scope.properties.intentos, 8);
                        break;
                    case 8:
                        if (data.length > 0) {
                            $scope.datosSaludSSeccion = data;
                        }
                        doRequest("GET", "../API/extension/AnahuacRestGet?url=postGetCatBitacoraComentariosPsicometrico&p=0&c=9999&usuario=" + $scope.properties.usuario + "&intentos=" + $scope.properties.intentos, 9);
                        break;
                    case 9:
                        if (data.length > 0) {

                            $scope.datosBitacoraComentarios = data;
                        }
                        $scope.generatePDF();
                        break;
                }

            })
            .error(function (data, status) {
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () {
                blockUI.stop();
            });
    }


    $scope.generatePDF = function () {
        var doc = new jspdf.jsPDF('p', 'mm', 'a4');
        var width = doc.internal.pageSize.getWidth();
        var height = doc.internal.pageSize.getHeight();
        var i = 0;
        var recSize = 0;
        var textSize;
        var textCount;
        var textConvert;
        var yvalue = 30;
        var fontText = 10;
        var fontTitle = 14;
        var fontSubTitle = 13;
        var fontparam = undefined;
        var margenPrimeraFila = 15;
        var margenFilaIntermedia = 60;
        var margenSegundaFila = 115;
        var margenSegundaFilaRasgos = 115;
        var respuestasPrimeraFila = 60;
        var respuestasSegundaFila = 167;
        var respuestasSegundaFilaInfoAdm = 147;
        var yValorRagos = 77;

        doc.addImage("widgets/customBtnPDFPsicometrico/assets/img/LogoRUA.png", "PNG", ((width / 2) + 35), ((height / 2) - 128), 60, 20);

        doc.setFontSize(fontSubTitle);
        doc.setFont(fontparam, 'bold');
        doc.text('DEPARTAMENTO DE ORIENTACIÓN VOCACIONAL', margenPrimeraFila, (height / 2) - 118)

        doc.setFontSize(fontTitle);
        doc.text('REPORTE PSICOLÓGICO', margenPrimeraFila, (height / 2) - 108)


        doc.setFillColor(228, 212, 200)
        doc.rect(10, (height / 2) - 103, 190, 75, 'F');


        doc.setFontSize(fontSubTitle);
        doc.setFont(fontparam, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 93, 'Información personal');

        doc.setFontSize(fontText);
        doc.setFont(fontparam, 'bold')
        doc.text(margenPrimeraFila, (height / 2) - 83, 'Id banner:');
        doc.text(margenPrimeraFila, (height / 2) - 78, 'Nombre del Aspirante:');
        doc.text(margenPrimeraFila, (height / 2) - 73, 'Fecha de nacimiento:');
        doc.text(margenPrimeraFila, (height / 2) - 68, 'Preparatoria:');
        doc.text(margenPrimeraFila, (height / 2) - 63, 'Ciudad de la preparatoria:');
        doc.text(margenPrimeraFila, (height / 2) - 58, 'País:');
        doc.text(margenPrimeraFila, (height / 2) - 53, 'Carrera que desea');
        doc.text(margenPrimeraFila, (height / 2) - 48, 'estudiar:');
        doc.text(margenPrimeraFila, (height / 2) - 43, 'Edad:');
        doc.text(margenPrimeraFila, (height / 2) - 38, 'Promedio:');
        doc.text(margenPrimeraFila, (height / 2) - 33, 'Fecha:');

        doc.setDrawColor(115, 66, 34)
        doc.rect(157.5, ((height / 2) - 84), 35, 45)
        doc.addImage($scope.datosUsuario.fotografiab64, "JPG", 160, ((height / 2) - 82), 30, 40);

        doc.setFontSize(fontSubTitle);
        doc.setFont(fontparam, 'bold')
        doc.text(margenPrimeraFila, (height / 2) - 18, 'Información de la admisión')

        doc.setFontSize(10);
        doc.setFont(fontparam, 'bold')
        doc.text(margenPrimeraFila, (height / 2) - 8, 'PAAV:');
        doc.text(margenPrimeraFila, (height / 2) - 3, 'PAAN:');
        doc.text(margenPrimeraFila, (height / 2) + 3, 'PARA:');
        doc.text(margenPrimeraFila, (height / 2) + 8, 'PAAT: ');
        doc.text(margenPrimeraFila, (height / 2) + 13, 'INVP:');

        doc.text(margenSegundaFila, (height / 2) - 8, 'Tipo de Admisión:');
        doc.text(margenSegundaFila, (height / 2) - 3, 'Periodo Ingreso:');
        doc.text(margenSegundaFila, (height / 2) + 3, 'Entrevisto:');
        doc.text(margenSegundaFila, (height / 2) + 8, 'Integro:');

        doc.setFont(fontparam, 'normal');
        doc.text(respuestasPrimeraFila, (height / 2) - 83, $scope.datosUsuario.idbanner);
        doc.text(respuestasPrimeraFila, (height / 2) - 78, $scope.datosUsuario.nombre);
        doc.text(respuestasPrimeraFila, (height / 2) - 73, $scope.datosUsuario.fechanacimiento);
        doc.text(respuestasPrimeraFila, (height / 2) - 68, $scope.datosUsuario.preparatoria);
        doc.text(respuestasPrimeraFila, (height / 2) - 63, $scope.datosUsuario.ciudad);
        doc.text(respuestasPrimeraFila, (height / 2) - 58, $scope.datosUsuario.pais);
        doc.text(respuestasPrimeraFila, (height / 2) - 51.5, $scope.datosUsuario.carrera);
        doc.text(respuestasPrimeraFila, (height / 2) - 43, (Math.trunc(parseInt($scope.datosUsuario.edad))).toString());

        doc.text(respuestasPrimeraFila, (height / 2) - 38, $scope.datosUsuario.promedio);
        doc.text(respuestasPrimeraFila, (height / 2) - 33, ($scope.datosUsuario.fechafinalizacion == null ? "N/A" : $scope.datosUsuario.fechafinalizacion = ($filter('date')(Date.parse($scope.datosUsuario.fechafinalizacion), "dd/MMM/yyyy")).toString()));

        doc.text(respuestasPrimeraFila, (height / 2) - 8, $scope.datosUsuario.paav);
        doc.text(respuestasPrimeraFila, (height / 2) - 3, $scope.datosUsuario.paan);
        doc.text(respuestasPrimeraFila, (height / 2) + 3, $scope.datosUsuario.para);
        doc.text(respuestasPrimeraFila, (height / 2) + 8, ($scope.datosUsuario.resultadopaa == 0 ? (parseInt($scope.datosUsuario.paav) + parseInt($scope.datosUsuario.paan) + parseInt($scope.datosUsuario.para) + "") : $scope.datosUsuario.resultadopaa));
        doc.text(respuestasPrimeraFila, (height / 2) + 13, $scope.datosUsuario.invp);

        doc.text(respuestasSegundaFilaInfoAdm, (height / 2) - 8, $scope.datosUsuario.tipoadmision);
        doc.text(respuestasSegundaFilaInfoAdm, (height / 2) - 3, $scope.datosUsuario.periodo);
        doc.text(138, (height / 2) + 3, $scope.datosUsuario.quienrealizoentrevista);
        doc.text(138, (height / 2) + 8, $scope.datosUsuario.quienintegro);

        doc.setFillColor(228, 212, 200)
        doc.rect(10, 170, 190, 100, 'F');

        doc.setFontSize(fontSubTitle);
        doc.setFont(fontparam, 'bold')
        doc.text(margenPrimeraFila, 180, 'Información familiar');

        doc.setFontSize(10);
        doc.setFont(fontparam, 'bold')
        doc.text(margenPrimeraFila, 190, 'Nombre del Padre:');
        doc.text(margenPrimeraFila, 195, 'Ocupación del Padre:');
        doc.text(margenPrimeraFila, 200, 'Empresa:');
        doc.text(margenPrimeraFila, 205, 'Universidad Anahuac:');

        doc.text(margenPrimeraFila, 215, 'Nombre de la Madre:');
        doc.text(margenPrimeraFila, 220, 'Ocupación de la Madre:');
        doc.text(margenPrimeraFila, 225, 'Empresa:');
        doc.text(margenPrimeraFila, 230, 'Universidad Anahuac:');

        doc.text(margenPrimeraFila, 240, 'Nombre del tutor:');
        doc.text(margenPrimeraFila, 245, 'Ocupación del Tutor:');
        doc.text(margenPrimeraFila, 250, 'Empresa:');
        doc.text(margenPrimeraFila, 255, 'Universidad Anahuac:');

        doc.setFont(fontparam, 'normal')
        $scope.datosPadres.forEach(element => {
            if (element.parentesco == "Padre") {
                doc.text(respuestasPrimeraFila, 190, element.nombre == "" || element.nombre == " " || element.nombre == null ? "Se desconoce" : element.nombre);
                if (element.vive == "No") {
                    doc.text(respuestasPrimeraFila, 195, "Finado");
                    doc.text(respuestasPrimeraFila, 200, "Finado");
                    doc.text(respuestasPrimeraFila, 205, "Finado");
                } else if (element.desconozcodatospadres == "t" && element.vive == null) {
                    doc.text(respuestasPrimeraFila, 195, "Se desconoce");
                    doc.text(respuestasPrimeraFila, 200, "Se desconoce");
                    doc.text(respuestasPrimeraFila, 205, "Se desconoce");
                } else {
                    doc.text(respuestasPrimeraFila, 195, element.puesto == "" ? "No trabaja" : element.puesto);
                    doc.text(respuestasPrimeraFila, 200, element.empresatrabaja == "" ? "No trabaja" : element.empresatrabaja);
                    doc.text(respuestasPrimeraFila, 205, element.campusanahuac == null ? "No" : element.campusanahuac);
                }
            } else if (element.parentesco == "Madre") {
                doc.text(respuestasPrimeraFila, 215, element.nombre == "" || element.nombre == " " || element.nombre == null ? "Se desconoce" : element.nombre);
                if (element.vive == "No") {
                    doc.text(respuestasPrimeraFila, 220, "Finado");
                    doc.text(respuestasPrimeraFila, 225, "Finado");
                    doc.text(respuestasPrimeraFila, 230, "Finado");
                } else if (element.desconozcodatospadres == "t" && element.vive == null) {
                    doc.text(respuestasPrimeraFila, 220, "Se desconoce");
                    doc.text(respuestasPrimeraFila, 225, "Se desconoce");
                    doc.text(respuestasPrimeraFila, 230, "Se desconoce");
                } else {
                    doc.text(respuestasPrimeraFila, 220, element.puesto == "" ? "No trabaja" : element.puesto);
                    doc.text(respuestasPrimeraFila, 225, element.empresatrabaja == "" ? "No trabaja" : element.empresatrabaja);
                    doc.text(respuestasPrimeraFila, 230, element.campusanahuac == null ? "No" : element.campusanahuac);
                }
            }
            if (element.istutor == "t") {
                doc.text(respuestasPrimeraFila, 240, element.nombre == "" || element.nombre == " " || element.nombre == null ? "Se desconoce" : element.nombre);
                doc.text(respuestasPrimeraFila, 245, element.puesto == "" ? "No trabaja" : element.puesto);
                doc.text(respuestasPrimeraFila, 250, element.empresatrabaja == "" ? "No trabaja" : element.empresatrabaja);
                doc.text(respuestasPrimeraFila, 255, element.campusanahuac == null ? "No" : element.campusanahuac);
            }
        });

        doc.addPage();
        /*----------------------------------------------------------------- FIN PRIMERA HOJA-----------------------------------------------------------------*/

        doc.setFontSize(fontSubTitle);
        doc.setFont(fontparam, 'bold')
        //doc.internal.write(0, "Tw")
        doc.text(margenPrimeraFila, 35, 'Fuentes que influyeron en su decisíon:');
        doc.setFontSize(fontText);
        doc.setFont(fontparam, 'normal')

        if ($scope.getInfoFuentesInfluyeron.length > 0) {
            yvalue += 10;
            if ($scope.getInfoFuentesInfluyeron[0].autodescripcion == true) {
                $scope.getInfoFuentesInfluyeron.forEach(element => {
                    //doc.internal.write(-2, "Tw")
                    doc.text(margenPrimeraFila, yvalue, element.fuentes);
                    yvalue += 5;
                    if (yvalue >= 275) {
                        doc.addPage();
                        yvalue = 30;
                    }
                });
            } else {
                //doc.internal.write(-2, "Tw")
                //doc.text($scope.getInfoFuentesInfluyeron[0].fuentes, margenPrimeraFila, yvalue, { maxWidth: 180, align: "justify" });
                doc.text($scope.getInfoFuentesInfluyeron[0].fuentes, margenPrimeraFila, 40, { maxWidth: 180, align: "justify" });
                let count = Math.ceil(($scope.getInfoFuentesInfluyeron[0].fuentes.length / 180))
                yvalue += (count * 7) + 3;
                if (yvalue >= 275) {
                    doc.addPage();
                    yvalue = 30;
                }
            }
        } else {
            yvalue += 10;
            //doc.internal.write(0, "Tw")
            doc.text(margenPrimeraFila, yvalue, "No se capturo ninguna fuente.");
            //yvalue += 7;
            yvalue += 20;
        }

        let yValor = yvalue;
        if ((295 - yvalue) < 143) {
            doc.addPage();
            yValor = 30;
        }

        doc.setFillColor(228, 212, 200)
        doc.rect(10, yValor, 190, 100, 'F');

        doc.setFontSize(fontSubTitle);
        doc.setFont(fontparam, 'bold')
        //doc.internal.write(0, "Tw") //CAMBIA LOS ESPACIOS ENTRE PALABRAS (0) LOS DEJA DEL TAMAÑO NORMAL
        doc.text(margenPrimeraFila, yValor += 7, "Rasgos observados durante la entrevista (Deficiente, Regular, Bien, Excelente)");

        yValor += 10;
        respuestasPrimeraFila += 5;
        doc.setFontSize(fontText);
        $scope.datosRasgos.forEach(element => {
            if (i > 10) {
                doc.setFont(fontparam, 'bold')
                doc.text(margenSegundaFilaRasgos, yValorRagos, element.rasgo);
                //doc.text(margenSegundaFilaRasgos, yValorRagos, element.rasgo);
                doc.setFont(fontparam, 'normal')
                doc.text(respuestasSegundaFila, yValorRagos, element.calificacion);
                //doc.text(respuestasSegundaFila, yValorRagos, element.calificacion);
                yValorRagos += 7;
            } else {
                doc.setFont(fontparam, 'bold')
                doc.text(margenPrimeraFila, yValor, element.rasgo);
                doc.setFont(fontparam, 'normal')
                doc.text(respuestasPrimeraFila, yValor, element.calificacion);
                yValor += 7;
            }
            i += 1;
        });

        yValor += 7;
        if (yValor >= 230) {
            doc.addPage();
            yValor = 15;
        }
        debugger
        doc.setFontSize(fontSubTitle);

        doc.setFont(fontparam, 'bold');
        doc.text(margenPrimeraFila, yValor += 10, "Salud");
        yValor += 10;


        doc.setFontSize(fontText);
        doc.setFont(fontparam, 'normal');
        $scope.salud = convertToPlain($scope.datosSaludSSeccion[0].salud);
        doc.text($scope.salud, margenPrimeraFila, yValor, { maxWidth: 180, align: "justify" });
        let count = 0;
        $scope.salud.forEach(element =>{
            count = Math.ceil((element.length / 180))
        })
        yValor += (count * 7) + 3;

        yValor += 10;
        if (yValor >= 275) {
            doc.addPage();
            yValor = 15;
        }

        doc.setFont(fontparam, 'bold')
        doc.text(margenPrimeraFila, yValor, "¿Vives en situación de discapacidad?");
        yValor += 7;

        doc.setFont(fontparam, 'normal');
        doc.text($scope.datosSaludPSeccion[0].cat_situacion_discapacidad_descripcion, margenPrimeraFila, yValor);
        yValor += (1 * 7);

        if (yValor >= 275) {
            doc.addPage();
            yValor = 15;
        }

        count = Math.ceil(($scope.datosSaludPSeccion[0].situacion_discapacidad.length / 150))
        let yTempdisc = (yValor + (count * 7) + 3);
        yTempdisc=nuevaHoja(yTempdisc,doc,true);
        if(yTempdisc == 15){
            yValor = yTempdisc;
        }
        doc.setFont(fontparam, 'bold')
        doc.text(margenPrimeraFila, yValor, "¿Tienes algún problema de salud que necesite atención médica continua?");
        yValor += 7;
        doc.setFont(fontparam, 'normal');
        doc.text(($scope.datosSaludPSeccion[0].situacion_discapacidad != '' ? $scope.datosSaludPSeccion[0].situacion_discapacidad : "N/A"), margenPrimeraFila, yValor,{ maxWidth: 150, align: "justify" });
        yValor += (count * 7)+3;


        doc.setFont(fontparam, 'bold')
        doc.text(margenPrimeraFila, yValor, "¿Te consideras una persona saludable?");
        yValor += 7;

        doc.setFont(fontparam, 'normal');
        doc.text($scope.datosSaludPSeccion[0].cat_persona_saludable_descripcion, margenPrimeraFila, yValor);
        yValor += (1 * 7);

        if (yValor >= 275) {
            doc.addPage();
            yValor = 15;
        }


        doc.setFont(fontparam, 'bold')
        doc.text(margenPrimeraFila, yValor, "¿Has recibido alguna terapia?");
        yValor += 7;

        doc.setFont(fontparam, 'normal');
        doc.text($scope.datosSaludPSeccion[0].cat_terapia_descripcion, margenPrimeraFila, yValor);
        yValor += (1 * 7);

        if (yValor >= 275) {
            doc.addPage();
            yValor = 15;
        }

        count = Math.ceil(($scope.datosSaludPSeccion[0].tipo_terapia.length / 150))
        let yTempTipo = (yValor + (count * 7) + 3);
        yTempTipo=nuevaHoja(yTempTipo,doc,true);
        if(yTempTipo == 15){
            yValor = yTempTipo;
        }
        doc.setFont(fontparam, 'bold')
        doc.text(margenPrimeraFila, yValor, "¿Qué tipo de terapia?");
        yValor += 7;
        doc.setFont(fontparam, 'normal');
        doc.text(($scope.datosSaludPSeccion[0].tipo_terapia != '' ? $scope.datosSaludPSeccion[0].tipo_terapia : "N/A"), margenPrimeraFila, yValor,{ maxWidth: 150, align: "justify" });
        yValor += (count * 7)+3;

        /*----------------------------------------------------------------- FIN SEGUNDA HOJA -----------------------------------------------------------------*/

        //yValor = 30
        var capacidadesTitulo = ["Ajuste al medio familiar", "Ajuste escolar previo", "Ajuste al medio social", "Ajuste afectivo (filiación)", "Ajuste religioso", "Ajuste existencial"]
        var capacidades = [{ nombre: 'ajustemediofamiliar', calificacion: 'califajustemediofamiliar' }, { nombre: 'ajusteescolarprevio', calificacion: 'califajusteescolarprevio' }, { nombre: 'ajustemediosocial', calificacion: 'califajustemediosocial' }, { nombre: 'ajusteefectivo', calificacion: 'califajusteafectivo' }, { nombre: 'ajustereligioso', calificacion: 'califajustereligioso' }, { nombre: 'ajusteexistencial', calificacion: 'califajusteexistencial' }]

        doc.setFontSize(fontSubTitle);
        doc.setFont(fontparam, 'bold')
        doc.text(margenPrimeraFila, yValor += 7, "Capacidad de adaptación");
        doc.setFontSize(fontText);
        yValor += 7
        capacidades.forEach((data, index) =>{
            $scope.datosCapacidad[0][data.nombre] = convertToPlain($scope.datosCapacidad[0][data.nombre]);
            $scope.datosCapacidad[0][data.nombre].forEach(element =>{
                count = Math.ceil((element.length / 180))
            })

            let yTemporal = (yValor + (count * 7) + 3);
            yTemporal=nuevaHoja(yTemporal,doc,true);
            if(yTemporal == 15){
                yValor = yTemporal;
            }

            doc.setFont(fontparam, 'bold')
            doc.text(margenPrimeraFila, yValor, capacidadesTitulo[index]);
            yValor=nuevaHoja(yValor,doc,true);

            /*lines = doc.setFont(fontparam, 'normal')
            .setFontSize(fontText)
            .splitTextToSize($scope.datosCapacidad[0][data.nombre], 180)*/
            
            
            doc.setFont(fontparam, 'normal');
            doc.text( $scope.datosCapacidad[0][data.nombre], margenPrimeraFila, yValor+=8,{ maxWidth: 180,align:'justify'} );

            yValor += (count * 7) + 3;

            doc.text(165, yValor, "Puntiación:");
            doc.text(185, yValor, $scope.datosCapacidad[0][data.calificacion]);
            yValor+=8

        });
        


        /******************Capacidad de adaptación segunda parte************************ */
        doc.setFontSize(fontSubTitle);
        doc.setFont(fontparam, 'bold')
        //doc.internal.write(0, "Tw")
        doc.text(margenPrimeraFila, yValor, "Conclusiones y recomendaciones");
        yValor += 10;

        doc.setFontSize(fontText);
        doc.setFont(fontparam, 'normal');
        $scope.conclusiones_recomendaciones = convertToPlain($scope.datosSaludSSeccion[0].conclusiones_recomendaciones);
        //doc.internal.write(-2, "Tw")
        doc.text($scope.conclusiones_recomendaciones, margenPrimeraFila, yValor, { maxWidth: 177, align: "justify" });
        $scope.conclusiones_recomendaciones.forEach(element =>{
            count = Math.ceil((element.length / 180))
        })
        //count = Math.ceil(($scope.conclusiones_recomendaciones.length / 180))
        yValor += (count * 7) + 3;
        yValor += 10;

        yValor=nuevaHoja(yValor,doc,true);

        yValor +10;

         /***********************  Calcular la interpretacion ***********************/

       /* let regtangulo = calcularRegtangulo(convertToPlain($scope.datosSaludSSeccion[0].interpretacion),yValor);
        if(regtangulo.newPage){
            doc.addPage();
        }
        doc.setFillColor(228, 212, 200);
        doc.rect(10, regtangulo.yInicio, 190, regtangulo.longitud+10, 'F');
        yValor = regtangulo.yInicio + 8;*/

        yValor += 8,

        $scope.interpretacion = convertToPlain($scope.datosSaludSSeccion[0].interpretacion);
        $scope.interpretacion.forEach(element =>{
            count = Math.ceil((element.length / 180))
        })
        //count = Math.ceil(($scope.interpretacion.length / 180))
        let yTempInter = (yValor + (count * 7) + 3)+10;
        yTempInter=nuevaHoja(yTempInter,doc,true);
        if(yTempInter == 15){
            yValor = yTempInter;
        }

        doc.setFontSize(fontSubTitle);
        doc.setFont(fontparam, 'bold')
        doc.text(margenPrimeraFila, yValor, "Interpretación");
        yValor += 10;

        doc.setFontSize(fontText);
        doc.setFont(fontparam, 'normal');
        
        
        doc.text($scope.interpretacion, margenPrimeraFila, yValor, { maxWidth: 180, align: "justify" });
        yValor += (count * 7) + 3;

        yValor = nuevaHoja(yValor,doc,false);
        yValor+=10
        /***************** CURSOS RECOMENDADOS ***********/

        doc.setFontSize(fontSubTitle);
        doc.setFont(fontparam, 'bold')
        //doc.text(margenPrimeraFila, 238, "Cursos Recomendados")
        doc.text(margenFilaIntermedia += 10, yValor, "Cursos Recomendados")
        yValor += 10;

        doc.setFontSize(fontText);
        doc.setFont(fontparam, 'normal');
        for (let i = 0; i < $scope.datosSaludSSeccion.length; i++) {
            switch ($scope.datosSaludSSeccion[i].cursos_recomendados) {
                case "PDU":
                    $scope.cursos.PDU = true;
                    break;
                case "SSE":
                    $scope.cursos.SSE = true;
                    break;
                case "PDP":
                    $scope.cursos.PDP = true;
                    break;
                case "PCA":
                    $scope.cursos.PCA = true;
                    break;
                default:
                    break;
            }
        }
        
        doc.text("PDU: " + ($scope.cursos.PDU ? "Si" : "No"), 60, yValor, { maxWidth: 180, align: "justify" });
        doc.text("SSE: " + ($scope.cursos.SSE ? "Si" : "No"), 80, yValor, { maxWidth: 180, align: "justify" });
        doc.text("PDP: " + ($scope.cursos.PDP ? "Si" : "No"), 100, yValor, { maxWidth: 180, align: "justify" });
        doc.text("PCA: " + ($scope.cursos.PCA ? "Si" : "No"), 120, yValor, { maxWidth: 180, align: "justify" });
        yValor += 17;

        yValor = nuevaHoja(yValor,doc,true);

        
        /****  Bitacora *****/
       
       // doc.addPage();

        //COMENTARIOS 
         doc.addPage();
         yValor = 15;
         doc.setFontSize(fontSubTitle);
         doc.setFont(fontparam, 'bold')
         doc.text(margenPrimeraFila, yValor, "Comentarios");

         yValor += 10;
 
         doc.setFontSize(fontText);
         doc.setFont(fontparam, 'normal');

         $scope.comentarios = [];
         for (let i = 0; i < $scope.datosBitacoraComentarios.length; i++) {
 
             $scope.comentarios[i] = convertToPlain( $scope.datosBitacoraComentarios[i].comentario);
             $scope.comentarios[i].forEach(element =>{
                count = Math.ceil((element.length / 180))
            })
             //count = Math.ceil(($scope.comentarios[i].length / 180))
             let yTemp = (yValor + (count * 7) + 3);
             yTemp=nuevaHoja(yTemp,doc,true);
             if(yTemp == 15){
                yValor = yTemp;
             }

             doc.text($scope.comentarios[i], margenPrimeraFila, yValor, { maxWidth: 180, align: "justify" });
    
             
             yValor += (count * 7) + 3; 
         }

        doc.save(`${$scope.properties.fileName}_ReporteOV.pdf`);
    }

    function convertToPlain(html) {
        html = html.replaceAll(' style="color: rgb(44, 62, 80);background-color: rgb(255, 255, 255);"', "");
        html = html.replaceAll('float: none;', "");
        html = html.replaceAll("&#34;", '"');
        html = html.replace(/<style([\s\S]*?)<\/style>/gi, '');
        html = html.replace(/<script([\s\S]*?)<\/script>/gi, '');
        html = html.replace(/<\/div>/ig, '');
        html = html.replace(/<\/li>/ig, '');
        html = html.replace(/<li>/ig, '  *  ');
        html = html.replace(/<\/ul>/ig, '');
        html = html.replace(/<\/p>/ig, '');
        html = html.replace(/<br\s*[\/]?>/gi, "\n");
        html = html.replace(/<[^>]+>/ig, '');
        html = html.replace(/(<([^>]+)>)/ig, '');
        html = html.replace(/\\par[d]?/g, "");
        html = html.replace(/\{\*?\\[^{}]+}|[{}]|\\\n?[A-Za-z]+\n?(?:-?\d+)?[ ]?/g, "").trim();
        let texto = html.split("\n");
        html = [];
        texto.forEach( element =>{
            if(element.toString().trim().length >= 0){
                html.push(element.toString().trim());
            }
        })
        return html

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

    function nuevaHoja( yValor, doc, white = false){
      if ( ((yValor + 30) >= 275 && white) || (yValor + 30) >= 230 && (!white) ) {
            doc.addPage();
            yValor = 15;
        }
      return  yValor;
    }

    function calcularCapacidadDeAdaptacion(medioFamiliar, escolarPrevio, medioSosial,efectivo, religion, existencial,yValor){

      var str = [];
      var strIndividual = [];
      let cantidad = [];
      let nuevaLongitud = yValor+22;
      let yValue = yValor+22;

      let count = Math.ceil((medioFamiliar.length / 180));
      nuevaLongitud += (count * 10) + 5;
      strIndividual.push({yValue: yValue, yValueFinal:nuevaLongitud})
      cantidad.push(0)
      if ((nuevaLongitud + 30) >= 230) {
            str.push({longitud:nuevaLongitud,newPage:true,cantidad:cantidad,individual:strIndividual})
            nuevaLongitud = 40;
            cantidad = []
      }

      yValue = nuevaLongitud;
      count = Math.ceil((escolarPrevio.length / 180));
      nuevaLongitud += (count * 10) + 5;
      strIndividual.push({yValue:yValue, yValueFinal:nuevaLongitud})
      cantidad.push(1)
      if ((nuevaLongitud + 30) >= 230) {
            str.push({longitud:nuevaLongitud,newPage:true,cantidad:cantidad,individual:strIndividual})
            nuevaLongitud = 40;
            cantidad = []
      }

      yValue = nuevaLongitud;
      count = Math.ceil((medioSosial.length / 180));
      nuevaLongitud += (count * 10) + 5;
      strIndividual.push({yValue:yValue, yValueFinal:nuevaLongitud})
      cantidad.push(2)
      if ((nuevaLongitud + 30) >= 230) {
            str.push({longitud:nuevaLongitud,newPage:true,cantidad:cantidad,individual:strIndividual})
            nuevaLongitud = 40;
            cantidad = []
      }

      yValue = nuevaLongitud;
      count = Math.ceil((efectivo.length / 180));
      nuevaLongitud += (count * 10) + 5;
      strIndividual.push({yValue:yValue, yValueFinal:nuevaLongitud})
      cantidad.push(3)
      if ((nuevaLongitud + 30) >= 230) {
            str.push({longitud:nuevaLongitud,newPage:true,cantidad:cantidad,individual:strIndividual})
            nuevaLongitud = 40;
            cantidad = []
      }

      yValue = nuevaLongitud;
      count = Math.ceil((religion.length / 180));
      nuevaLongitud += (count * 10) + 5;
      strIndividual.push({yValue:yValue, yValueFinal:nuevaLongitud})
      cantidad.push(4)
      if ((nuevaLongitud + 30) >= 230) {
            str.push({longitud:nuevaLongitud,newPage:true,cantidad:cantidad,individual:strIndividual})
            nuevaLongitud = 40;
            cantidad = []
      }
      
      yValue = nuevaLongitud;
      count = Math.ceil((existencial.length / 180));
      nuevaLongitud += (count * 10) + 5;
      strIndividual.push({yValue:yValue, yValueFinal:nuevaLongitud})
      cantidad.push(5)
      if ((nuevaLongitud ) >= 230) {
            str.push({longitud:nuevaLongitud,newPage:true,cantidad:cantidad,individual:strIndividual})
      }else{
        str.push({longitud:nuevaLongitud,newPage:false,cantidad:cantidad,individual:strIndividual})
      }
      return str;
    }

    function calcularRegtangulo(valor,yValor){

        var str = {};
        let nuevaLongitud = yValor;
        let count = Math.ceil((valor.length / 180));
        nuevaLongitud += (count * 7) + 5;
        nuevaLongitud -= yValor;
        if ((nuevaLongitud + 30) >= 230) {
              str = {longitud:nuevaLongitud,newPage:true,yInicio:40};
        }else{
            str = {longitud:nuevaLongitud,newPage:false, yInicio:yValor};
        }
        return str;
      }

}