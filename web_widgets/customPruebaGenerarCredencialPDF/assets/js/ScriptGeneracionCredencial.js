var regNombre;
		regNombre = "Jose";
		var regSegNombre;
		regSegNombre = "Carlos";
		var regApePaterno;
		regApePaterno = "Felix";
		var regApeMaterno;
		regApeMaterno = "Ibarra";
		var regExpediente;
		regExpediente = "aaa";
		var regNombreFull;
		regNombreFull = "Jose Carlos";
		var regApesFull;
		regApesFull = "Felix Ibarra";
		var Campus;
		Campus = "UAMN";
		var Carrera;
		Carrera = "title-25";
		var fotoUsuario;
		fotoUsuario = "img2.jpg";
		var fecha;
		fecha = "fecha";
		var Hora;
		Hora = "Hora";
		var Lugar;
		Lugar = "Lugar";

 	    function generatePDF(){
        
	var element = document.getElementById('canvas');
	var opt = {
		margin:       [0, 0, 0, 0],
		filename:     'document.pdf',
		image:        { type: 'jpg', quality: 0.98 },
		html2canvas:  { dpi:300, letterRendering: true, useCORS: true },
		jsPDF:        { unit: 'mm', format: 'a4', orientation: 'portrait' }
	};
	html2pdf().from(element).set(opt).save();

 	    }