function PbButtonCtrl($scope, $http, modalService, blockUI, $q) {
    $scope.datosUsuario = {};
    $scope.datosPadres = [];
    $scope.datosHermanos = [];
    $scope.datosFuentesInfluyeron = [];
    $scope.datosRasgos = [];
    
    $scope.cargarDatos = function(){
        doRequest("GET","../API/extension/AnahuacRestGet?url=getInfoReportes&p=0&c=9999&usuario="+$scope.properties.usuario);
    }
    
    function doRequest(method, url) {
        //blockUI.start();
        //data: angular.copy($scope.properties.dataToSend),
        var req = {
            method: method,
            url: url
        };

        return $http(req)
            .success(function(data, status) {
                if(data.length > 0){
                    $scope.datosUsuario = data[0];
                    doRequest2("GET","../API/extension/AnahuacRestGet?url=getInfoRelativos&p=0&c=9999&caseid="+$scope.properties.caseId);
                }
            })
            .error(function(data, status) {
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                //blockUI.stop();
            });
    }
    
    function doRequest2(method, url) {
        //blockUI.start();
        var req = {
            method: method,
            url: url
        };
        return $http(req)
            .success(function(data, status) {
                if(data.length > 0){
                    $scope.datosPadres = data;
                    doRequest3("GET","../API/extension/AnahuacRestGet?url=getInfoRelativosHermanos&p=0&c=9999&caseid="+$scope.properties.caseId);
                }
            })
            .error(function(data, status) {
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                //blockUI.stop();
            });
    }
    
    function doRequest3(method, url) {
        //blockUI.start();
        var req = {
            method: method,
            url: url
        };
        return $http(req)
            .success(function(data, status) {
                if(data.length > 0){
                    $scope.datosHermanos= data;
                }
                doRequest4("GET","../API/extension/AnahuacRestGet?url=getInfoFuentesInfluyeron&p=0&c=9999&caseid="+$scope.properties.caseId);
                
            })
            .error(function(data, status) {
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                //blockUI.stop();
            });
    }

    function doRequest4(method, url) {
        //blockUI.start();
        var req = {
            method: method,
            url: url
        };
        return $http(req)
            .success(function(data, status) {
                if(data.length > 0){
                    $scope.getInfoFuentesInfluyeron = data;
                }
                doRequest5("GET","../API/extension/AnahuacRestGet?url=getInfoRasgos&p=0&c=9999&caseid="+$scope.properties.caseId);
            })
            .error(function(data, status) {
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                //blockUI.stop();
            });
    }

    function doRequest5(method, url) {
        //blockUI.start();
        var req = {
            method: method,
            url: url
        };
        return $http(req)
            .success(function(data, status) {
                if(data.length > 0){
                    $scope.datosRasgos = data;
                }
                $scope.generatePDF();
            })
            .error(function(data, status) {
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {
                //blockUI.stop();
            });
    }
    
    $scope.generatePDF = function() {
        var doc = new jspdf.jsPDF('p', 'mm', 'a4');
        var width = doc.internal.pageSize.getWidth();
        var height = doc.internal.pageSize.getHeight();
        
        doc.text('DEPARTAMENTO DE ORIENTACIÓN VOCACIONAL', width/2, (height/2), { align: 'center' })
        doc.text('REPORTE PSICOLÓGICO', width/2, (height/2)+20, { align: 'center' })
        
        //Rectangulo
        doc.rect(10, (height/2)+30, 190, 80, 'S');
        
        doc.setFontSize(8);
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
        doc.text(15, (height/2)+93, 'INVP:');
        //segunda fila
        doc.text(120, (height/2)+45, 'Edad:');
        doc.text(120, (height/2)+50, 'Promedio:');
        doc.text(120, (height/2)+73, 'Tipo de Admisión:');
        doc.text(120, (height/2)+78, 'Periodo Ingreso:');
        doc.text(120, (height/2)+83, 'Entrevisto:');
        doc.text(120, (height/2)+88, 'Integro:');
        
        
        
        doc.setFont(undefined, 'normal');
        doc.text(57, (height/2)+40, `${$scope.datosUsuario.idbanner}-${$scope.datosUsuario.nombre}`);
        doc.text(57, (height/2)+45, $scope.datosUsuario.fechanacimiento);
        doc.text(57, (height/2)+50, $scope.datosUsuario.preparatoria);
        doc.text(57, (height/2)+55, $scope.datosUsuario.ciudad);
        doc.text(57, (height/2)+60, $scope.datosUsuario.pais);
        doc.text(57, (height/2)+66.5, $scope.datosUsuario.carrera);
        doc.text(57, (height/2)+73, 'N/A');
        doc.text(57, (height/2)+78, $scope.datosUsuario.paav);
        doc.text(57, (height/2)+83, $scope.datosUsuario.paan);
        doc.text(57, (height/2)+88, $scope.datosUsuario.para);
        doc.text(57, (height/2)+93, $scope.datosUsuario.invp);
        //segunda fila
        doc.text(148, (height/2)+45, $scope.datosUsuario.edad);
        doc.text(148, (height/2)+50, $scope.datosUsuario.promedio);
        doc.text(148, (height/2)+73, $scope.datosUsuario.tipoadmision);
        doc.text(148, (height/2)+78, $scope.datosUsuario.periodo);
        doc.text(148, (height/2)+83, $scope.datosUsuario.quienrealizoentrevista);
        doc.text(148, (height/2)+88, $scope.datosUsuario.quienintegro);
        
        
        doc.addPage();
        
        doc.setFontSize(8);
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
        var yvalue = 0;

        if($scope.datosHermanos.length > 0){
            let yHermanos = 85;
            $scope.datosHermanos.forEach((element,index)=>{
                doc.text(15, yHermanos, `Ocupación del Hermano ${index+1}:`);
                if(element.istrabaja == 't'){
                    yHermanos+=5;
                    doc.text(15, yHermanos, 'Empresa:');
                }
                if(element.isestudia == 't'){
                    yHermanos+=5;
                    doc.text(15, yHermanos, 'Escuela:');
                }
                yHermanos+=10;
            });
            yvalue = yHermanos;
        }
        doc.text(15, yvalue, 'Fuentes que influyeron en su decisíon:');
        
        doc.setFont(undefined, 'normal')
        $scope.datosPadres.forEach(element => {
            if(element.parentesco == "Padre"){
                doc.text(55, 25, element.puesto == ""?"Se desconoce":element.puesto);
                doc.text(55, 30, element.empresatrabaja);
                doc.text(55, 35, element.campusanahuac == null?"No":element.campusanahuac);
            }else if (element.parentesco == "Madre"){
                doc.text(55, 45, element.puesto == ""?"Se desconoce":element.puesto);
                doc.text(55, 50, element.empresatrabaja);
                doc.text(55, 55, element.campusanahuac == null?"No":element.campusanahuac);
            }
            if(element.istutor == "t"){
                doc.text(55, 65, element.puesto == ""?"Se desconoce":element.puesto);
                doc.text(55, 70, element.empresatrabaja);
                doc.text(55, 75, element.campusanahuac == null?"No":element.campusanahuac);
            }
        });

        if($scope.datosHermanos.length > 0){
            let yHermanos = 85;
            $scope.datosHermanos.forEach(element=>{
                doc.text(55, yHermanos,  element.isestudia == "t"?"Estudiante":(element.istrabaja=="t"?"trabajador(a)":"Se desconoce"));
                if(element.istrabaja == 't'){
                    yHermanos+=5;
                    doc.text(55, yHermanos, element.empresatrabaja);
                }
                if(element.isestudia == 't'){
                    yHermanos+=5;
                    doc.text(55, yHermanos, element.escuelaestudia);
                }
                yHermanos+=10;
            });
        }

        if($scope.datosFuentesInfluyeron.length > 0){
            yvalue+= 10;
            if($scope.datosFuentesInfluyeron[0].autodescripcion == true){
                
                $scope.datosFuentesInfluyeron.forEach( element =>{
                    doc.text(15, yvalue, element.fuentes);
                    yvalue+=5;
                });
            }else{
                
                doc.text(15, yvalue, element.fuentes.match(/.{1,100}(\s|$)/g));
                
            }
        }
        
        
        doc.addPage();
        doc.setFontSize(9);
        doc.setFont(undefined, 'bold')
        doc.text(15, 20, "Rasgos observados durante la entrevista");

        doc.setFontSize(8);
        let inicio=30;
        $scope.datosRasgos.forEach( element =>{
            doc.text(15, inicio, element.rasgo);
            inicio+=5;
        });
        
        doc.setFont(undefined, 'normal')
        inicio=30;
        $scope.datosRasgos.forEach( element =>{
            doc.text(65, inicio, element.calificacion);
            inicio+=5;
        });
        inicio+=5;
        doc.text(15, inicio, "CAPACIDAD DE ADAPTACIÓN");
        inicio+=5;
        doc.line(15, inicio, 150, inicio);
        inicio+=5;
        var capacidades = ["Ajuste al medio familiar","Ajuste escolar previo","Ajuste al medio social","Ajuste afectivo (filiación)","Ajuste religioso","Ajuste existencial"]
        for(let i=0;i<6;i++){
            doc.text(15, inicio, capacidades[i]);
            inicio+=5;
            pageHeight= doc.internal.pageSize.height;
            if (inicio>=pageHeight)
            {
                doc.addPage();
            }
            
        }
        
        
        // Save the PDF
        doc.save(`${$scope.properties.fileName}.pdf`);
    }
    
}