function PbButtonCtrl($scope, $http, modalService, blockUI, $q) {
    $scope.datosUsuario = {};
    $scope.datosPadres = [];
    $scope.datosHermanos = [];
    $scope.datosFuentesInfluyeron = [];
    $scope.datosRasgos = [];
    $scope.datosCapacidad = [];
    $scope.datosSalud = [];
    
    $scope.cargarDatos = function(){
        doRequest("GET","../API/extension/AnahuacRestGet?url=getInfoReportes&p=0&c=9999&usuario="+$scope.properties.usuario,1);
    }
    
    function doRequest(method, url,numero) {
        blockUI.start();
        //data: angular.copy($scope.properties.dataToSend),
        var req = {
            method: method,
            url: url
        };

        return $http(req)
            .success(function(data, status) {
                switch (numero) {
                    case 1:
                        if(data.length > 0){
                            $scope.datosUsuario = data[0];
                        }
                        doRequest("GET","../API/extension/AnahuacRestGet?url=getInfoRelativos&p=0&c=9999&caseid="+$scope.properties.caseId,2);
                      break;
                    case 2:
                        $scope.datosPadres = data;
                        doRequest("GET","../API/extension/AnahuacRestGet?url=getInfoRelativosHermanos&p=0&c=9999&caseid="+$scope.properties.caseId,3);
                      break;
                    case 3:
                        if(data.length > 0){
                            $scope.datosHermanos= data;
                        }
                        doRequest("GET","../API/extension/AnahuacRestGet?url=getInfoFuentesInfluyeron&p=0&c=9999&caseid="+$scope.properties.caseId,4);
                      break;
                    case 4:
                        if(data.length > 0){
                            $scope.getInfoFuentesInfluyeron = data;
                        }
                        doRequest("GET","../API/extension/AnahuacRestGet?url=getInfoRasgos&p=0&c=9999&caseid="+$scope.properties.caseId,5);
                      break;
                    case 5:
                        if(data.length > 0){
                            $scope.datosRasgos = data;
                        }
                        doRequest("GET","../API/extension/AnahuacRestGet?url=getInfoCapacidadAdaptacion&p=0&c=9999&caseid="+$scope.properties.caseId,6);
                      break;
                    case  6:
                        if(data.length > 0){
                            $scope.datosCapacidad = data;
                        }
                        $scope.generatePDF();
                      break;
                  }
               
            })
            .error(function(data, status) {
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                blockUI.stop();
            });
    }

    
    $scope.generatePDF = function() {
        var doc = new jspdf.jsPDF('p', 'mm', 'a4');
        var width = doc.internal.pageSize.getWidth();
        var height = doc.internal.pageSize.getHeight();
        
        doc.addImage("widgets/customBtnPDFPsicometrico/assets/img/LogoUniversidadAnahuac.png","PNG",((width/2)-30),((height/2)-90),70,70 );
        doc.addImage($scope.datosUsuario.fotografiab64,"JPG",((width/2)+50),((height/2)-80),30,40 );
        doc.setFontSize(12);
        doc.text('DEPARTAMENTO DE ORIENTACIÓN VOCACIONAL', width/2, (height/2), { align: 'center' })
        doc.text('REPORTE PSICOLÓGICO', width/2, (height/2)+10, { align: 'center' })
        
        //Rectangulo
        doc.rect(10, (height/2)+30, 195, 80, 'S');
        
        doc.setFontSize(10);
        doc.setFont(undefined, 'bold')
        doc.text(15, (height/2)+40, 'Nombre del Aspirante:');
        doc.text(15, (height/2)+45, 'Fecha de nacimiento:');
        doc.text(15, (height/2)+50, 'Preparatoria:');
        doc.text(15, (height/2)+55, 'Ciudad de la preparatoria:');
        doc.text(15, (height/2)+60, 'País:');
        // Un solo titulo
        doc.text(15, (height/2)+65, 'Carrera que desea');
        doc.text(15, (height/2)+68, 'estudiar:');
        
        doc.text(15, (height/2)+73, 'Fecha:');
        doc.text(15, (height/2)+78, 'PAAV:');
        doc.text(15, (height/2)+83, 'PAAN:');
        doc.text(15, (height/2)+88, 'PARA:');
        doc.text(15, (height/2)+93, 'PAA: ');
        doc.text(15, (height/2)+98, 'INVP:');
        //segunda fila
        doc.text(135, (height/2)+45, 'Edad:');
        doc.text(135, (height/2)+50, 'Promedio:');
        doc.text(135, (height/2)+73, 'Tipo de Admisión:');
        doc.text(135, (height/2)+78, 'Periodo Ingreso:');
        doc.text(135, (height/2)+83, 'Entrevisto:');
        doc.text(135, (height/2)+88, 'Integro:');
        
        
        
        doc.setFont(undefined, 'normal');
        doc.text(60, (height/2)+40, `${$scope.datosUsuario.idbanner}-${$scope.datosUsuario.nombre}`);
        doc.text(60, (height/2)+45, $scope.datosUsuario.fechanacimiento);
        doc.text(60, (height/2)+50, $scope.datosUsuario.preparatoria);
        doc.text(60, (height/2)+55, $scope.datosUsuario.ciudad);
        doc.text(60, (height/2)+60, $scope.datosUsuario.pais);
        doc.text(60, (height/2)+66.5, $scope.datosUsuario.carrera);
        doc.text(60, (height/2)+73, 'N/A');
        doc.text(60, (height/2)+78, $scope.datosUsuario.paav);
        doc.text(60, (height/2)+83, $scope.datosUsuario.paan);
        doc.text(60, (height/2)+88, $scope.datosUsuario.para);
        doc.text(60, (height/2)+93, ($scope.datosUsuario.resultadopaa == 0? (parseInt($scope.datosUsuario.paav) + parseInt($scope.datosUsuario.paan)+parseInt($scope.datosUsuario.para)+""): '0'));
        doc.text(60, (height/2)+98, $scope.datosUsuario.invp);
        //segunda fila
        doc.text(167, (height/2)+45, $scope.datosUsuario.edad);
        doc.text(167, (height/2)+50, $scope.datosUsuario.promedio);
        doc.text(167, (height/2)+73, $scope.datosUsuario.tipoadmision);
        doc.text(167, (height/2)+78, $scope.datosUsuario.periodo);
        doc.text(167, (height/2)+83, $scope.datosUsuario.quienrealizoentrevista);
        doc.text(167, (height/2)+88, $scope.datosUsuario.quienintegro);
        
        
        doc.addPage();
        
        doc.setFontSize(10);
        doc.setFont(undefined, 'bold')

        doc.text(15, 20, 'Información familiar');
        
        doc.text(15, 25, 'Ocupación del Padre:');
        doc.text(15, 30, 'Empresa:');
        doc.text(15, 35, 'Universidad Anahuac:');
        
        doc.text(15, 45, 'Ocupación de la Madre:');
        doc.text(15, 50, 'Empresa:');
        doc.text(15, 55, 'Universidad Anahuac:');
        
        doc.text(15, 65, 'Ocupación del Tutor:');
        doc.text(15, 70, 'Empresa:');
        doc.text(15, 75, 'Universidad Anahuac:');

        
        doc.setFont(undefined, 'normal')
        $scope.datosPadres.forEach(element => {
            if(element.parentesco == "Padre"){
                doc.text(60, 25, element.puesto == ""?"Se desconoce":element.puesto);
                doc.text(60, 30, element.empresatrabaja);
                doc.text(60, 35, element.campusanahuac == null?"No":element.campusanahuac);
            }else if (element.parentesco == "Madre"){
                doc.text(60, 45, element.puesto == ""?"Se desconoce":element.puesto);
                doc.text(60, 50, element.empresatrabaja);
                doc.text(60, 55, element.campusanahuac == null?"No":element.campusanahuac);
            }
            if(element.istutor == "t"){
                doc.text(60, 65, element.puesto == ""?"Se desconoce":element.puesto);
                doc.text(60, 70, element.empresatrabaja);
                doc.text(60, 75, element.campusanahuac == null?"No":element.campusanahuac);
            }
        });
        var yvalue = 85;
        if($scope.datosHermanos.length > 0){
            let yHermanos = 85;
            $scope.datosHermanos.forEach(element=>{
                doc.text(15, yHermanos, `Ocupación del Hermano ${index+1}:`);
                doc.text(55, yHermanos,  element.isestudia == "t"?"Estudiante":(element.istrabaja=="t"?"trabajador(a)":"Se desconoce"));
                if(element.istrabaja == 't'){
                    yHermanos+=5;
                    doc.text(15, yHermanos, `Ocupación del Hermano ${index+1}:`);
                    doc.text(60, yHermanos, element.empresatrabaja);
                }
                if(element.isestudia == 't'){
                    yHermanos+=5;
                    doc.text(15, yHermanos, 'Escuela:');
                    doc.text(60, yHermanos, element.escuelaestudia);
                }
                yHermanos+=10;
                if (yHermanos>=275)
                {
                    doc.addPage();
                    yHermanos=15;
                }
                yvalue = yHermanos;
            });
        }
        
        doc.setFontSize(10);
        doc.setFont(undefined, 'bold')
        doc.text(15, yvalue, 'Fuentes que influyeron en su decisíon:');
        doc.setFont(undefined, 'normal')
        if($scope.datosFuentesInfluyeron.length > 0){
            yvalue+= 10;
            if($scope.datosFuentesInfluyeron[0].autodescripcion == true){
                
                $scope.datosFuentesInfluyeron.forEach( element =>{
                    doc.text(15, yvalue, element.fuentes);
                    yvalue+=5;
                    if (yvalue>=275)
                    {
                        doc.addPage();
                        yvalue=15;
                    }
                });
            }else{
                doc.text($scope.datosCapacidad[0][capacidades[i].nombre],15, yvalue,{maxWidth: 180});
                //doc.text(15, yvalue, element.fuentes.match(/.{1,100}(\s|$)/g));
                let count = Math.ceil((element.fuentes.length / 180))
                yvalue+= (count*7)+3;
                if (yvalue>=275)
                {
                    doc.addPage();
                    yvalue=15;
                }
                
            }
        }else{
            yvalue+=7;
            doc.text(15, yvalue, "N/A");
            yvalue+=7;
        }

        let yValor=yvalue;
        if( (295 - yvalue) < 143 ){
            doc.addPage();
            yValor=15;
        }
        doc.setFontSize(12);
        doc.setFont(undefined, 'bold')
        doc.text(15, yValor, "Rasgos observados durante la entrevista");
        yValor+=7;
        doc.setFontSize(10);
        $scope.datosRasgos.forEach( element =>{
            doc.setFont(undefined, 'bold')
            doc.text(15, yValor, element.rasgo);
            doc.setFont(undefined, 'normal')
            doc.text(65, yValor, element.calificacion);
            yValor+=7;
        });
        yValor+=7;
        console.log("valor de yValor"+yValor)
        if (yValor>=230)
        {
            doc.addPage();
            yValor = 15;
        }
        doc.setFont(undefined, 'bold')
        doc.text(15, yValor, "CAPACIDAD DE ADAPTACIÓN");
        yValor+=7;
        doc.line(15, yValor, 195, yValor);
        yValor+=7;
        var capacidadesTitulo = ["Ajuste al medio familiar","Ajuste escolar previo","Ajuste al medio social","Ajuste afectivo (filiación)","Ajuste religioso","Ajuste existencial"]
        var capacidades = [{nombre:'ajustemediofamiliar',calificacion:'califajustemediofamiliar'},{nombre:'ajusteescolarprevio',calificacion:'califajusteescolarprevio'},{nombre:'ajustemediosocial',calificacion:'califajustemediosocial'},{nombre:'ajusteefectivo',calificacion:'califajusteafectivo'},{nombre:'ajustereligioso',calificacion:'califajustereligioso'},{nombre:'ajusteexistencial',calificacion:'califajusteexistencial'}]
        let newPage,reseteo = false;
        for(let i=0;i<6;i++){    
            if (yValor>=275)
            {
                doc.addPage();
                newPage = true;
                reseteo = false;
            }
            if(!reseteo && newPage){
                reseteo = true;
                yValor = 30;
            }
            doc.setFont(undefined, 'bold')
            doc.text(15, yValor, capacidadesTitulo[i]);
            yValor+=7;
            doc.setFont(undefined, 'normal');
            $scope.datosCapacidad[0][capacidades[i].nombre] = convertToPlain($scope.datosCapacidad[0][capacidades[i].nombre])
            doc.text($scope.datosCapacidad[0][capacidades[i].nombre],15, yValor,{maxWidth: 180, align: "justify"});
            let count = Math.ceil(($scope.datosCapacidad[0][capacidades[i].nombre].length / 180))
            yValor+= (count*7)+3;
            doc.text(165, yValor, "Puntiación:");
            doc.text(185, yValor, $scope.datosCapacidad[0][capacidades[i].calificacion]);
            yValor+=7;
        }
        
        if (yValor>=150)
        {
            doc.addPage();
            yValor=15;
        }

        doc.setFontSize(12);
        doc.setFont(undefined, 'bold')
        doc.text(15, yValor, "Salud");
        yValor+=7;

        doc.setFontSize(10);
        doc.text(15, yValor, "Salud");
        yValor+=7;
        doc.setFont(undefined, 'normal');
        doc.text("N/A",15, yValor);
        yValor+= (1*7);

        if (yValor>=275)
        {
            doc.addPage();
            yValor=15;
        }

        doc.setFont(undefined, 'bold')
        doc.text(15, yValor, "¿Vives en situación de discapacidad?");
        doc.setFont(undefined, 'normal');
        doc.text(85, yValor, "N/A");
        yValor+=7;

        if (yValor>=275)
        {
            doc.addPage();
            yValor=15;
        }
        
        doc.setFont(undefined, 'bold')
        doc.text(15, yValor, "¿Tienes algún problema de salud que necesite atención médica continua?");
        yValor+=7;
        doc.setFont(undefined, 'normal');
        doc.text('N/A',15, yValor);
        yValor+= (1*7);

        if (yValor>=275)
        {
            doc.addPage();
            yValor=15;
        }

        doc.setFont(undefined, 'bold')
        doc.text(15, yValor, "¿Te consideras una persona saludable?");
        doc.setFont(undefined, 'normal');
        doc.text(85, yValor, "N/A");
        yValor+= 5;

        if (yValor>=275)
        {
            doc.addPage();
            yValor=15;
        }


        doc.setFont(undefined, 'bold')
        doc.text(15, yValor, "¿Has recibido alguna terapia?");
        doc.setFont(undefined, 'normal');
        doc.text(85, yValor, "N/A");
        yValor+= 5;

        if (yValor>=275)
        {
            doc.addPage();
            yValor=15;
        }

        doc.setFont(undefined, 'bold')
        doc.text(15, yValor, "¿Qué tipo de terapia?");
        yValor+=7;
        doc.setFont(undefined, 'normal');
        doc.text('N/A',15, yValor);
        yValor+=7;

        if (yValor>=275)
        {
            doc.addPage();
            yValor=15;
        }

        
        doc.setFont(undefined, 'bold')
        //doc.text(100, yValor, "Cursos Recomendados");
        doc.text("Cursos Recomendados",width/2, yValor, { align: 'center' });
        yValor+=7;
       
        doc.setFont(undefined, 'normal');
        //doc.text(85, yValor, "PDU: N/A, SSE: N/A, PDP: N/A, PCA: N/A");
        doc.text("PDU: N/A, SSE: N/A, PDP: N/A, PCA: N/A",width/2, yValor, { align: 'center' });
        yValor+=7;

        if (yValor>=275)
        {
            doc.addPage();
            yValor=15;
        }

        
        doc.setFont(undefined, 'bold')
        doc.text(15, yValor, "Conclusiones y recomendaciones");
        yValor+=7;
       
        doc.setFont(undefined, 'normal');
        doc.text('N/A',15, yValor);
        yValor+= (1*7);

        if (yValor>=275)
        {
            doc.addPage();
            yValor=15;
        }

        
        doc.setFont(undefined, 'bold')
        doc.text(15, yValor, "Interpretación");
        yValor+=7;
       
        doc.setFont(undefined, 'normal');
        doc.text('N/A',15, yValor);
        yValor+= (1*7);

        if (yValor>=275)
        {
            doc.addPage();
            yValor=15;
        }

        
        doc.setFont(undefined, 'bold')
        doc.text(15, yValor, "Comentarios");
        yValor+=7;
       
        doc.setFont(undefined, 'normal');
        doc.text('N/A',15, yValor);
        
        yValor+= (1*7);

        if (yValor>=275)
        {
            doc.addPage();
            yValor=15;
        }

        console.log(height)


        // Save the PDF
        doc.save(`${$scope.properties.fileName}_ReporteOV.pdf`);
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
        return html;
    }
    
}
