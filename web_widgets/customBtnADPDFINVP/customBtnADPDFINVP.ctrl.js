function PbButtonCtrl($scope, modalService, blockUI, $q) {

    $scope.generatePDF = function() {
        //blockUI.start();
        $scope.properties.idsDivGrafica.forEach(data => {
            document.getElementById(data.id).style.height = "30vh";
            document.getElementById(data.id).style.width = "40vw";
        })
        
       
        
        var element = document.querySelector($scope.properties.elementSelector);
    
        var opt = {
            margin: [15, 5, 15, 5],
            filename: $scope.properties.fileName + ".pdf",
            image: { type: 'jpeg', quality: 0.98},
            html2canvas: { dpi: 100, letterRendering: true, useCORS: true },
            jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' },
            pagebreak: { mode: ['css']}
            // pagebreak: { avoid: '.avoid' }
        };

        var promise
        setTimeout(function(){ 
            primise = getPDF(element, opt);
            $scope.properties.idsDivGrafica.forEach(data => {
                document.getElementById(data.id).style.height = "40vh";
                document.getElementById(data.id).style.width = "80vw";
            })
        }, 1000);
        
        promise.then(function(resultado) {
            alert("Fin de la promesa");
            blockUI.stop();
        }, function(error) {
            $scope.mensaje="Se ha producido un error al obtener el dato:"+error;
        });
    }
    
    // html2pdf(element, opt); 
    function getPDF(element, opt) {
        var defered=$q.defer();
        var promise=defered.promise;
        html2pdf(element, opt); 
        return promise;
    }
    
     
    
}