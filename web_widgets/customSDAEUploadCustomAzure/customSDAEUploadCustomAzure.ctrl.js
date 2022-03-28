function PbUploadCtrl($scope, $sce, $element, widgetNameFactory, $timeout, $log, gettextCatalog, $http) {
    var ctrl = this;
    this.name = widgetNameFactory.getName('pbInput');
    this.filename = '';
    this.filemodel = '';

    $scope.documetObject = {
        "b64": "",
        "filename": "",
        "filetype": "",
        "contenedor": ""
    }

    this.clear = clear;
    this.startUploading = startUploading;
    this.uploadError = uploadError;
    this.uploadComplete = uploadComplete;

    this.name = widgetNameFactory.getName('pbUpload');

    this.preventFocus = function ($event) {
        $event.target.blur();
    };

    this.submitForm = function () {
        // var form = $element.find('form');
        // form.triggerHandler('submit');
        // form[0].submit();
    };

    function handleFileSelect(evt) {
        var f = evt.target.files[0]; // FileList object
        var reader = new FileReader();
        // Closure to capture the file information.
        reader.onload = (function (theFile) {
            return function (e) {
                var binaryData = e.target.result;
                //Converting Binary Data to base 64
                var base64String = window.btoa(binaryData);
                $scope.documetObject["b64"] = $scope.documetObject["filetype"] +  "," +  base64String;
                
                doRequest("POST", "../API/extension/AnahuacAzureRest?url=uploadFile&p=0&c=0", null);
            };
        })(f);
        // Read in the image file as a data URL.
        reader.readAsBinaryString(f);
    }


    /**
   * Execute a get/post request to an URL
   * It also bind custom data from success|error to a data
   * @return {void}
   */
    function doRequest(method, url, params) {
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.documetObject),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                $scope.properties.urlAzure = data[0];
                $scope.properties.dataFromSuccess = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromError = undefined;
                notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status });
                if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                    redirectIfNeeded();
                }
                closeModal($scope.properties.closeOnSuccess);
            })
            .error(function (data, status) {
                $scope.properties.dataFromError = data;
                $scope.properties.responseStatusCode = status;
                $scope.properties.dataFromSuccess = undefined;
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () {
                
            });
    }
    this.forceSubmit = function (event) {
        $scope.procesar = false;
        //$scope.properties.urlretorno = window.btoa(event.target.files[0]);

        // console.log(event.target.files[0]);
        handleFileSelect(event);
        // $scope.documetObject["b64"] = window.btoa(event.target.files[0]);
        $scope.documetObject["filename"] = event.target.files[0].name;
        $scope.documetObject["filetype"] = event.target.files[0].type;
        $scope.documetObject["contenedor"] = "privado";

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
            swal("!Formato no valido!", "Solo puede agregar archivos PDF o imagenes JPG y PNG", "warning");
            $scope.properties.isPDF = "true";
            $scope.properties.isImagen = "true";
            $scope.properties.urlretorno = "";
        }

        if ($scope.procesar === true) {
            $scope.properties.urlretorno = URL.createObjectURL(event.target.files[0]);
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
    $scope.$on('$destroy', function () {
        input.off('change', ctrl.forceSubmit);
    });

    $scope.$watch('properties.url', function (newUrl, oldUrl) {
        ctrl.url = $sce.trustAsResourceUrl(newUrl);
        if (newUrl === undefined) {
            $log.warn('you need to define a url for pbUpload');
        }
    });

    //the filename displayed is not bound to the value as a bidirectionnal
    //bond, thus, in case the value is updated, it is not reflected
    //to the filename (example with the BS-14498)
    //we watch the value to update the filename and the upload widget state
    $scope.$watch(function () { return $scope.properties.value; }, function (newValue) {
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
}