function PbButtonCtrl($scope, modalService) {
    
    $scope.getPdf = function(){
        let width = $($scope.properties.elementSelector).width();
        let height = $($scope.properties.elementSelector).height();
        let millimeters = {};
		millimeters.width = Math.floor(width * 0.264583);
		millimeters.height = Math.floor(height * 0.264583);
        var doc = new jsPDF('p', 'mm', [millimeters.width, millimeters.height]);
        let id = "#" + $scope.properties.idEditor;
        var specialElementHandlers = {};
        specialElementHandlers[id] = function (element, renderer) {
            return true;
        };
        
        doc.addHTML($($scope.properties.elementSelector), 0, 0, function (){
            doc.save($scope.properties.fileName + '.pdf');
        });
        
        // NO BORRAR, IMPORTANTE PARA UN AJUSTE 
        
        // html2canvas($($scope.properties.elementSelector)).then(function (canvas) {
        //     var img = canvas.toDataURL("image/png", 1.0);
        //     console.log("IMAGEN" + img);
        //     // var doc = new jsPDF();
        //     // doc = new jsPDF('p', 'mm', [millimeters.width, millimeters.height]);
        //     let options = {
        //         scale: 300
        //     }
        //     doc = new jsPDF('p', 'px', [canvas.width, canvas.height]);
        //     doc.addImage(img, 'JPEG', 10, 10);
        //     doc.save('test.pdf');        
        // });
    }
}
