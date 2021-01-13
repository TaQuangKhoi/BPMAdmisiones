function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {

  'use strict';

  var vm = this;

    $scope.generatePDF = function(){
      //blockUI.start();
	var element = document.getElementById('canvas');
	var opt = {
		margin:       [0, 0, 0, 0],
		filename:     'CredencialAnahuac.pdf',
		image:        { type: 'jpg', quality: 0.98 },
		html2canvas:  { dpi:300, letterRendering: true,  useCORS: true },
		jsPDF:        { unit: 'mm', format: 'a4', orientation: 'portrait' }
	};
	html2pdf().from(element).set(opt).save();
    //blockUI.stop()
    btnDescargar
    document.getElementById('btnDescargar').disabled= 'disabled'; 
    document.getElementById('btnDescargar2').disabled= 'disabled'; 
    document.getElementById('btnFinalizarTarea').disabled = false; 
    document.getElementById('btnFinalizarTarea2').disabled = false;
    }

}

