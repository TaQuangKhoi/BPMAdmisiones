function PbButtonCtrl($scope, modalService, blockUI, $q,$filter) {
    
      
    $scope.generatePDF = function() {
        //blockUI.start();
        //$scope.properties.idsDivGrafica.forEach(data => {
            //document.getElementById(data.id).classList.remove('chart-container')
            //document.getElementById(data.id).style.height = "509px";
            //document.getElementById(data.id).style.width = "960px";
        //})
       
       
        var doc = new jspdf.jsPDF('p', 'mm', 'a4');
        getTexto(doc);
        var canvas = document.getElementById("chartjs1")
        var imgData = canvas.toDataURL('image/png'); 
        doc.addImage(imgData,'PNG',2,5,200,145,'chart1')
        //doc.addPage();
        var canvas2 = document.getElementById("chartjs2")
        var imgData2 = canvas2.toDataURL('image/png'); 
        doc.addImage(imgData2,'PNG',2,150,200,145,'chart2')

        doc.save($scope.properties.idBanner+"_"+$scope.properties.fileName + "V1.pdf");
        
        doc = new jspdf.jsPDF('l', 'mm', 'a4');
        getTexto(doc);
        canvas = document.getElementById("chartjs1")
        imgData = canvas.toDataURL('image/png'); 
        doc.addImage(imgData,'PNG',15,15,280,150,'chart1')
        doc.addPage();
        canvas2 = document.getElementById("chartjs2")
        imgData2 = canvas2.toDataURL('image/png'); 
        doc.addImage(imgData2,'PNG',15,15,280,150,'chart2')
        
        doc.save($scope.properties.idBanner+"_"+$scope.properties.fileName + "V2.pdf");
        
        /*
        var element = document.querySelector($scope.properties.elementSelector);
        
        var opt = {
            margin: [5,5,1,5],
            filename: $scope.properties.idBanner+"_"+$scope.properties.fileName + ".pdf",
            image: { type: 'jpeg',quality: 0.98},
            html2canvas: { dpi: 192, letterRendering: true, useCORS: true },
            jsPDF: { unit: 'mm', format: 'a4', orientation: 'landscape' },
            pagebreak: { mode: [ 'legacy'], before:[".break-before"]},
            // pagebreak: { avoid: '.avoid' }
        };

        var promise;
        primise = getPDF(element, opt);
        setTimeout(function(){ 
            $scope.properties.idsDivGrafica.forEach(data => {
                //document.getElementById(data.id).style.height = "40vh";
                document.getElementById(data.id).removeAttribute("style")
                //document.getElementById(data.id).classList.add('chart-container')
            })
        }, 1000);
        
        promise.then(function(resultado) {
            alert("Fin de la promesa");
            blockUI.stop();
        }, function(error) {
            $scope.mensaje="Se ha producido un error al obtener el dato:"+error;
        });*/
    }
    
    // html2pdf(element, opt); 
    function getPDF(element, opt) {
        var defered=$q.defer();
        var promise=defered.promise;
        html2pdf(element, opt); 
        return promise;
    }
    
    function getTexto(doc){
        var width = doc.internal.pageSize.getWidth();
        var height = doc.internal.pageSize.getHeight();
        var yvalue = 30;
        var fontText = 10;
        var fontparam = undefined;
        var margenPrimeraFila = 15;
        var margenSegundaFila = 90;
        var respuestasPrimeraFila = 60;
        var respuestasSegundaFila = 135;
        let date = new Date();
        let fechaActual = ($filter('date')(Date.parse(date), "dd/MMM/yyyy")).toString();
        
        doc.setFontSize(fontText);
        doc.text(margenSegundaFila, (height / 2) - 140, 'Fecha:');
        doc.text(fechaActual, 155, (height / 2) - 140);
        
        doc.text(margenSegundaFila, (height / 2) - 135, 'Usuario:');
        doc.text($scope.properties.userName, 155, (height / 2) - 135);
        
        //doc.setFontSize(fontText);
        doc.setFont(fontparam, 'bold')
        doc.text(margenPrimeraFila, (height / 2) - 93, 'ID:');
        doc.text(margenPrimeraFila, (height / 2) - 88, 'Sexo:');
        doc.text(margenPrimeraFila, (height / 2) - 83, 'Nacionalidad:');
        doc.text(margenPrimeraFila, (height / 2) - 78, 'Promedio:');
        doc.text(margenPrimeraFila, (height / 2) - 73, 'Fecha Examen:');
        
        doc.text(margenSegundaFila, (height / 2) - 93, 'Nombre:');
        doc.text(margenSegundaFila, (height / 2) - 88, 'Fecha de nacimiento:');
        doc.text(margenSegundaFila, (height / 2) - 83, 'R. Permanente:');
        doc.text(margenSegundaFila, (height / 2) - 78, 'Escuela Procedencia:');
        doc.text(margenSegundaFila, (height / 2) - 73, 'Carrera:');
        
        doc.setFont(fontparam, 'normal');
        doc.text(respuestasPrimeraFila, (height / 2) - 93, $scope.properties.datosUsuario.id_siu);
        doc.text(respuestasPrimeraFila, (height / 2) - 88, $scope.properties.datosUsuario.sexo);
        doc.text(respuestasPrimeraFila, (height / 2) - 83, $scope.properties.datosUsuario.nacionalidad);
        doc.text(respuestasPrimeraFila, (height / 2) - 78, $scope.properties.datosUsuario.promediogeneral);
        doc.text(respuestasPrimeraFila, (height / 2) - 73, $scope.properties.datosUsuario.fecharegistro);
        
        doc.text(respuestasSegundaFila, (height / 2) - 93, `${$scope.properties.datosUsuario.nombres} ${$scope.properties.datosUsuario.apellidop} ${$scope.properties.datosUsuario.apellidom}`);
        doc.text(respuestasSegundaFila, (height / 2) - 88, $scope.properties.datosUsuario.fechanacimiento);
        doc.text(respuestasSegundaFila, (height / 2) - 83, $scope.properties.datosUsuario.pais);
        doc.text(respuestasSegundaFila, (height / 2) - 78, $scope.properties.datosUsuario.preparatoria);
        doc.text(respuestasSegundaFila, (height / 2) - 73, $scope.properties.datosUsuario.licenciatura);
        
        aplicarFactor();
        doc.setFontSize(fontText);
        doc.setFont(fontparam, 'bold')
        doc.text(margenPrimeraFila, (height / 2) - 50, "Escalas de Validez" );
        doc.text(margenPrimeraFila, (height / 2) - 20, "Escalas Básicas" );
        doc.text(margenPrimeraFila, (height / 2) + 10, "Escalas de contenido" );
        
        doc.setFont(fontparam, 'normal');
        let registro = [0,0,0];
        let tipo = 0;
        $scope.respuestaProcesadas.forEach(element =>{
            if(element.tipo == 1){
               doc.text(margenPrimeraFila + (registro[0] * 10), (height / 2) - 40, element.escala );
               doc.text(margenPrimeraFila + (registro[0] * 10), (height / 2) - 30, element.puntuacion.toString() );
               registro[0]+=1;
            }
            if(element.tipo == 2){
               doc.text(margenPrimeraFila + (registro[1] * 10), (height / 2) - 10, element.escala );
               doc.text(margenPrimeraFila + (registro[1] * 10), (height / 2), element.puntuacion.toString() );
               registro[1]+=1;
            }
            if(element.tipo == 3){
               doc.text(margenPrimeraFila + (registro[2] * 12), (height / 2) + 20, element.escala );
               doc.text(margenPrimeraFila + (registro[2] * 12), (height / 2) + 30, element.puntuacion.toString() );
               registro[2]+=1;
            }
        });
        
        
        
        
        
        doc.addPage();
        
        
    }
    
    
    
    $scope.factorK = 
  [{"k":30,".5":15,".4":12,".2":6},
  {"k":29,".5":15,".4":12,".2":6},
  {"k":28,".5":14,".4":11,".2":6},
  {"k":27,".5":14,".4":11,".2":5},
  {"k":26,".5":13,".4":10,".2":5},
  {"k":25,".5":13,".4":10,".2":5},
  {"k":24,".5":12,".4":10,".2":5},
  {"k":23,".5":12,".4":9,".2":5},
  {"k":22,".5":11,".4":9,".2":4},
  {"k":21,".5":11,".4":8,".2":4},
  {"k":20,".5":10,".4":8,".2":4},
  {"k":19,".5":10,".4":8,".2":4},
  {"k":18,".5":9,".4":7,".2":4},
  {"k":17,".5":9,".4":7,".2":3},
  {"k":16,".5":8,".4":6,".2":3},
  {"k":15,".5":8,".4":6,".2":3},
  {"k":14,".5":7,".4":6,".2":3},
  {"k":13,".5":7,".4":5,".2":3},
  {"k":12,".5":6,".4":5,".2":2},
  {"k":11,".5":6,".4":4,".2":2},
  {"k":10,".5":5,".4":4,".2":2},
  {"k":9,".5":5,".4":4,".2":2},
  {"k":8,".5":4,".4":3,".2":2},
  {"k":7,".5":4,".4":3,".2":1},
  {"k":6,".5":3,".4":2,".2":1},
  {"k":5,".5":3,".4":2,".2":1},
  {"k":4,".5":2,".4":2,".2":1},
  {"k":3,".5":2,".4":1,".2":1},
  {"k":2,".5":1,".4":1,".2":0},
  {"k":1,".5":1,".4":0,".2":0},
  {"k":0,".5":0,".4":0,".2":0}];
  
  $scope.respuestaProcesadas = [];
  function aplicarFactor(){
      $scope.respuestaProcesadas = [];
      var valorK=0;
      $scope.properties.respuesta.forEach(element =>{
          if (element.escala=='K') {
              valorK=element.puntuacion;
          }
      });
      
      var jsonk={};
      $scope.factorK.forEach(element =>{
          if(element.k==valorK){
              jsonk=element;
          }
      });
      
      let mfExtra = {};
      let indexmf=0;
      $scope.properties.respuesta.forEach((element,index) =>{
      $scope.respuestaProcesadas.push(angular.copy(element));
      if (element.escala=='Hs') {
          $scope.respuestaProcesadas[index].puntuacion = parseInt(element.puntuacion)+jsonk['.5'];
      }
      if (element.escala=='Dp') {
          $scope.respuestaProcesadas[index].puntuacion = parseInt(element.puntuacion)+jsonk['.4'];
      }
      if (element.escala=='Pt') {
          $scope.respuestaProcesadas[index].puntuacion = parseInt(element.puntuacion)+jsonk['k'];
      }
      if (element.escala=='Es') {
          $scope.respuestaProcesadas[index].puntuacion = parseInt(element.puntuacion)+jsonk['k'];
      }
      if (element.escala=='Ma') {
          $scope.respuestaProcesadas[index].puntuacion = parseInt(element.puntuacion)+jsonk['.2'];
      }
      if(element.escala == 'Mf (femenino)'){
          mfExtra = angular.copy($scope.respuestaProcesadas[index]);
          mfExtra.escala = 'Mfm';
          mfExtra.puntuacion = 0;
          $scope.respuestaProcesadas[index].escala = 'Mff'
          indexmf = index;
      }
      if(element.escala == 'Mf (masculino)' ){
          mfExtra = angular.copy($scope.respuestaProcesadas[index]);
          mfExtra.escala = 'Mff';
          mfExtra.puntuacion = 0;
          $scope.respuestaProcesadas[index].escala = 'Mfm'
          indexmf = index;
      }
      
      });
      $scope.respuestaProcesadas.splice((indexmf+1),0,angular.copy(mfExtra));
      
      console.log($scope.respuestaProcesadas)
  }
    
    
    
     
    
}