function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

  'use strict';

 
  
  var vm = this;
  $scope.filebase64=null;

    $scope.lstFormatosSolicitud=[
        {
          "nombre":"Registro",
          "url":""
        },
        {
          "nombre":"Solicitud de admisión",
          "url":""
        },
        {
          "nombre":"Madre",
          "url":""
        },
        {
          "nombre":"Padre",
          "url":""
        },
        {
          "nombre":"Tutor",
          "url":""
        },
        {
          "nombre":"Contacto de emergencia",
          "url":""
        },
        {
          "nombre":"Pago",
          "url":""
        }
    ];

    $scope.lstFormatosAutodescripcion=[
        {
          "nombre":"Autodescripción",
          "url":""
        },
        {
          "nombre":"Hermanos",
          "url":""
        },
        {
          "nombre":"Madre",
          "url":""
        },
        {
          "nombre":"Grupos sociales",
          "url":""
        },
        {
          "nombre":"Idiomas",
          "url":""
        },
        {
          "nombre":"Terapia",
          "url":""
        },
        {
          "nombre":"Información escolar",
          "url":""
        },
        {
          "nombre":"Universidades",
          "url":""
        },
        {
          "nombre":"Pariente egresado",
          "url":""
        }
    ]

  $scope.downloadFile=function(value){
    alert(value);

  }
  
  //============FILE UPLOAD
  // Check for the File API support.
    if (window.File && window.FileReader && window.FileList && window.Blob) {
      document.getElementById('files').addEventListener('change', handleFileSelect, false);
    } else {
      alert('The File APIs are not fully supported in this browser.');
    }

    function handleFileSelect(evt) {
      var f = evt.target.files[0]; // FileList object
      var reader = new FileReader();
      // Closure to capture the file information.
      reader.onload = (function(theFile) {
        return function(e) {
          var binaryData = e.target.result;
          //Converting Binary Data to base 64
          var base64String = window.btoa(binaryData);
          //showing file converted to base64
          //document.getElementById('base64').value = base64String;
          $scope.filebase64=base64String
          $scope.uploadFile();
          alert(base64String);
        };
      })(f);
      // Read in the image file as a data URL.
      reader.readAsBinaryString(f);
     
    }
    
  //============END
  
   $scope.uploadFile=function() {
    
    vm.busy = true;
    $scope.filebase64={"b64":"data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64,"+$scope.filebase64};
    var req = {
      method: 'POST',
      url: "https://anahuac-preproduction.bonitacloud.com/bonita/API/extension/AnahuacAzureRest?url=uploadFile&p=0&c=10",
      data: angular.copy($scope.filebase64)
    };

    return $http(req)
      .success(function(data, status) {
        alert("SUCCESS"+data)
      })
      .error(function(data, status) {
        alert("ERROR"+data)
      })
      .finally(function() {
        vm.busy = false;
      });
  }
  

}
