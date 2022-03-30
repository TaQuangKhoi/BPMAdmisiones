function PbButtonCtrl($scope, modalService, blockUI, $q) {
    
      
    $scope.generatePDF = function() {
        //blockUI.start();
        $scope.properties.idsDivGrafica.forEach(data => {
            //document.getElementById(data.id).classList.remove('chart-container')
            document.getElementById(data.id).style.height = "209px";
            document.getElementById(data.id).style.width = "360px";
        })
       
        var doc = new jspdf.jsPDF('l', 'mm', 'a4'); ;
        //var element = document.querySelector($scope.properties.elementSelector);
        //element.style.width = "720px";
        var canvas = document.getElementById("chartjs1")
        var imgData = canvas.toDataURL('image/png'); 
        doc.addImage(imgData,'PNG',15,15,135,150,'chart1')
        //doc.addPage();
        var canvas2 = document.getElementById("chartjs2")
        var imgData2 = canvas2.toDataURL('image/png'); 
        doc.addImage(imgData2,'PNG',155,15,135,150,'chart2')

        doc.save('Generated.pdf');
        
        /*
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
    
     
    
}