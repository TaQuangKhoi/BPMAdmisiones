function PbUploadCtrl($scope, $sce, $element, widgetNameFactory, $timeout, $log, gettextCatalog) {
    var ctrl = this;
    this.name = widgetNameFactory.getName('pbInput');
    this.filename = '';
    this.filemodel = '';

    this.clear = clear;
    this.startUploading = startUploading;
    this.uploadError = uploadError;
    this.uploadComplete = uploadComplete;

    this.name = widgetNameFactory.getName('pbUpload');

    this.preventFocus = function($event) {
        $event.target.blur();
    };

    this.submitForm = function() {
        var form = $element.find('form');
        form.triggerHandler('submit');
        form[0].submit();
    };

    this.forceSubmit = function(event) {
        $scope.procesar = false;

        //$scope.properties.urlretorno = window.btoa(event.target.files[0]);
        if (event.target.files[0].type === "image/jpeg") {
            $scope.properties.isImagen = true;
            $scope.properties.isPDF = false;
            $scope.properties.tipoArchivo = angular.copy(event.target.files[0].type);
            $scope.procesar = true;
        } else if (event.target.files[0].type === "image/png") {
            $scope.properties.isImagen = true;
            $scope.properties.isPDF = false;
            $scope.properties.tipoArchivo = angular.copy(event.target.files[0].type);
            $scope.procesar = true;
        } else if (event.target.files[0].type === "application/pdf") {
            $scope.properties.isPDF = true;
            $scope.properties.isImagen = false;
            $scope.properties.tipoArchivo = angular.copy(event.target.files[0].type);
            $scope.procesar = true;
        }
        /*else if(event.target.files[0].type === "image/jfif"){
            $scope.properties.isImagen = true;
            $scope.properties.isPDF = false;
            $scope.properties.tipoArchivo = angular.copy(event.target.files[0].type);
            $scope.procesar = true;
        }*/
        else {
            swal("!Formato no valido!", "Solo puede agregar archivos PDF o imagenes JPG y PNG", "warning");
            $scope.properties.isPDF = true;
            $scope.properties.isImagen = true;
            $scope.properties.urlretorno = "";
            $scope.properties.tipoArchivo = "";
            $scope.properties.archivo64 = "";
        }

        if ($scope.procesar === true) {

            $scope.properties.urlretorno = URL.createObjectURL(event.target.files[0]);
            $scope.properties.documentId = $scope.properties.originalDocumentId;

            var file = event.target.files[0],
                reader = new FileReader();

            reader.onloadend = function() {
                // Since it contains the Data URI, we should remove the prefix and keep only Base64 string
                var b64 = reader.result.replace(/^data:.+;base64,/, '');
                //onsole.log(b64); //-> "R0lGODdhAQABAPAAAP8AAAAAACwAAAAAAQABAAACAkQBADs="
                $scope.properties.archivo64 = angular.copy(b64);
            };
            reader.readAsDataURL(file);
            /* var reader = new FileReader();
             reader.readAsDataURL(event.target.files[0]); 
             reader.onloadend = function() {
                 
                 $scope.properties.urlretorno  = reader.result;                
                 //console.log(base64data);
                 acept
             }*/
            if (!event.target.value) {
                return;
            }
            ctrl.submitForm();
            event.target.value = null;
        }

    };

    var input = $element.find('input');
    input.on('change', ctrl.forceSubmit);
    $scope.$on('$destroy', function() {
        input.off('change', ctrl.forceSubmit);
    });

    $scope.$watch('properties.url', function(newUrl, oldUrl) {
        ctrl.url = $sce.trustAsResourceUrl(newUrl);
        if (newUrl === undefined) {
            $log.warn('you need to define a url for pbUpload');
        }
    });

    //the filename displayed is not bound to the value as a bidirectionnal
    //bond, thus, in case the value is updated, it is not reflected
    //to the filename (example with the BS-14498)
    //we watch the value to update the filename and the upload widget state
    $scope.$watch(function() { return $scope.properties.value; }, function(newValue) {
        if (newValue && newValue.filename) {
            var nombrearchivo = newValue.filename;
            var extension = nombrearchivo.split(".");
            for (var i = 0; i < extension.length; i++) {
                if (extension[i].toString().toLowerCase() === "jpg" || extension[i].toString().toLowerCase() === "png" || extension[i].toString().toLowerCase() === "jpeg" || extension[i].toString().toLowerCase() === "jfif" || extension[i].toString().toLowerCase() === "pdf") {
                    newValue.filename = $scope.properties.rename + $scope.properties.caseId + "." + extension[i]
                }
            }
            console.log(newValue)
            ctrl.filemodel = true;
            ctrl.filename = newValue.filename;
        } else if (!angular.isDefined(newValue)) {
            delete ctrl.filemodel;
            delete ctrl.filename;
        } else {
            $scope.properties.urlretorno = "";
            $scope.properties.isPDF = true;
            $scope.properties.isImagen = true;
        }
    });

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbUpload property named "value" need to be bound to a variable');
    }

    function clear() {
        ctrl.filename = '';
        ctrl.filemodel = '';
        $scope.properties.value = {};
        $scope.properties.isPDF = true;
        $scope.properties.isImagen = true;
        $scope.properties.urlretorno = "";
        $scope.properties.tipoArchivo = "";
        $scope.properties.archivo64 = "";
    }

    function uploadError(error) {
        $log.warn('upload fails too', error);
        ctrl.filemodel = '';
        ctrl.filename = gettextCatalog.getString('Upload failed');
    }

    function startUploading() {
        ctrl.filemodel = '';
        ctrl.filename = gettextCatalog.getString('Uploading...');
    }

    function uploadComplete(response) {
        //when the upload widget return a String, it means an error has occurred (with a html document as a response)
        //if it's not a string, we test if it contains some error message
        if (angular.isString(response) || (response && response.type && response.message)) {
            $log.warn('upload failed');
            ctrl.filemodel = '';
            ctrl.filename = gettextCatalog.getString('Upload failed');
            $scope.properties.errorContent = angular.isString(response) ? response : response.message;
            return;
        }
        $scope.properties.value = response;
    }
}