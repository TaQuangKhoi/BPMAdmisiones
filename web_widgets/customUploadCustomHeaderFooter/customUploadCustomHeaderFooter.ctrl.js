function PbUploadCtrl($scope, $sce, $element, $http, widgetNameFactory, $timeout, $log, gettextCatalog) {
    var ctrl = this;
    this.name = widgetNameFactory.getName('pbInput');
    this.filename = '';
    this.filemodel = '';

    this.clear = clear;
    this.startUploading = startUploading;
    this.uploadError = uploadError;
    this.uploadComplete = uploadComplete;

    this.name = widgetNameFactory.getName('pbUpload');
    $scope.progress = 0;
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
            $scope.properties.isImagen = "true";
            $scope.properties.isPDF = "false";
            $scope.procesar = true;
        } else if (event.target.files[0].type === "image/png") {
            $scope.properties.isImagen = "true";
            $scope.properties.isPDF = "false";
            $scope.procesar = true;
        } else if (event.target.files[0].type === "application/pdf") {
            $scope.properties.isPDF = "true";
            $scope.properties.isImagen = "false";
            $scope.procesar = true;
        } else {
            swal("Formato no valido!", "Solo puede agregar archivos PDF o imagenes JPG y PNG!", "warning");
            $scope.properties.isPDF = "true";
            $scope.properties.isImagen = "true";
            $scope.properties.urlretorno = "";
        }

        if ($scope.procesar === true) {

            $scope.properties.urlretorno = URL.createObjectURL(event.target.files[0]);
            $scope.properties.filename = event.target.files[0].name;
            $scope.properties.filetype = event.target.files[0].type
            $scope.$apply();
            /* var reader = new FileReader();
             reader.readAsDataURL(event.target.files[0]); 
             reader.onloadend = function() {
                 
                 $scope.properties.urlretorno  = reader.result;                
                 //console.log(base64data);
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

    $scope.upload = function() {
        var file = $('input[name="pbUpload0"]').get(0).files[0];
        Main()
        var jsonData = { "b64": toBase64(file) }
        var formData = new FormData();
        formData.append('file', file);



    }

    function progress(e) {
        if (e.lengthComputable) {
            $('#progress_percent').text(Math.floor((e.loaded * 100) / e.total));
            $('progress').attr({ value: e.loaded, max: e.total });
            $scope.progress = Math.floor((e.loaded * 100) / e.total);
        }
    }
    //the filename displayed is not bound to the value as a bidirectionnal
    //bond, thus, in case the value is updated, it is not reflected
    //to the filename (example with the BS-14498)
    //we watch the value to update the filename and the upload widget state
    $scope.$watch(function() { return $scope.properties.value; }, function(newValue) {
        if (newValue && newValue.filename) {
            ctrl.filemodel = true;
            ctrl.filename = newValue.filename;
        } else if (!angular.isDefined(newValue)) {
            delete ctrl.filemodel;
            delete ctrl.filename;
        } else {
            $scope.properties.urlretorno = "";
            $scope.properties.isPDF = "true";
            $scope.properties.isImagen = "true";
        }
    });

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbUpload property named "value" need to be bound to a variable');
    }

    function clear() {
        ctrl.filename = '';
        ctrl.filemodel = '';
        $scope.properties.value = {};
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
    const toBase64 = file => new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
    });

    async function Main() {
        const file = $('input[name="pbUpload0"]').get(0).files[0];

        var b64 = await toBase64(file);
        var jsonData = {
            "b64": b64,
            "filename": file.name,
            "filetype": file.type,
            "contenedor": "publico"
        }
        return $http.post('/bonita/API/extension/AnahuacAzureRest?url=uploadFile&p=0&c=10', jsonData, {
            headers: { 'Content-Type': "application/json" }
        }).then(function(results) {
            $scope.properties.urlretorno = results.data.data[0]
        });
    }



}