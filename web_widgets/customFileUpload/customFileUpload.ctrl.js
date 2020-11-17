function CustomFileUploadCtrl($scope, $sce, $element, $timeout, $log, widgetNameFactory) {
  var ctrl = this;
  this.name = widgetNameFactory.getName("fileUpload");
//  this.name = "fileUpload";
  this.filename = '';
  this.filemodel = '';
  this.editMode = false;
  this.existingFile = false;
  
  // console.log("widget_fileupload="+$scope) ;

  


  this.downloadUrl = '';
  this.newFile=true;
  this.isFile=true;


  // console.log("FileUpload=["+$scope.properties.label+"] urlUpload=["+$scope.properties.urlUpload+"]");

  var input = $element.find('input');
  var form = $element.find('form');

  this.preventFocus = function($event) {
    $event.target.blur();
  };

  input.on('change', forceSubmit);
  $scope.$on('$destroy', function() {
    input.off('change', forceSubmit);
  });

  $scope.$watch('properties.urlUpload', function(newUrl, oldUrl){
    ctrl.urlUpload = $sce.trustAsResourceUrl(newUrl);
    // console.log("widgetFileUpload["+$scope.properties.label+"] :urlUpload=["+ctrl.urlUpload+"]");
    if (newUrl === undefined) {
      $log.warn('you need to define a url for pbUpload');
    }
  });

  $scope.$watch('properties.currentFile', function(newCurrentFile, oldCurrentFile){
    if (angular.isObject(newCurrentFile) && newCurrentFile.fileName) {
      ctrl.downloadUrl = $scope.properties.contextRoot +  "/portal/" + newCurrentFile.url;
      ctrl.filename = newCurrentFile.fileName;
      ctrl.newFile = false;
    }
  });

  $scope.$watch('properties.deleteFile', function(newDeleteFile){
    if (!newDeleteFile) {
      $scope.properties.deleteFile = false;
    }
  });

  $scope.$watch('properties.newFile', function(newFile){
    if (!newFile) {
      $scope.properties.newFile = null;
    }
  });

  //the filename displayed is not bound to the value as a bidirectionnal
  //bond, thus, in case the value is updated, it is not reflected
  //to the filename (example with the BS-14498)
  //we watch the value to update the filename and the upload widget state
  $scope.$watch(function(){return $scope.properties.newFile;}, function(newValue){
    if (newValue && newValue.filename) {
      ctrl.filemodel = true;
      ctrl.filename = newValue.filename;
    } else if (!angular.isDefined(newValue)) {
      delete ctrl.filemodel;
      delete ctrl.filename;
    }
  });


  if (!$scope.properties.isBound('newFile')) {
    $log.error('the pbUpload property named "newFile" need to be bound to a variable');
  } 

  if ($scope.properties.isBound('existingFile') && angular.isObject($scope.properties.existingFile)) {
      this.editMode = false;
      this.existingFile = true;
  } 

  this.clear = function () {
    ctrl.filename = '';
    ctrl.filemodel = '';
    ctrl.newFile=true;
    ctrl.isFile=true;

    $scope.properties.newFile = null;
  }

  this.deleteCurrentFile = function() {
    ctrl.newFile=true;
    ctrl.filename = '';
    ctrl.filemodel = '';
    $scope.properties.currentFile = null;
    $scope.properties.deleteFile = true;
  }

  this.edit = function() {
    // console.log("FileUploadWidget: edit");
    ctrl.newFile=true;
    ctrl.editMode=true;
    input.required = true;
    ctrl.filemodel = '';
    ctrl.isFile = true;

  }

this.showUrlDoc = function () {
	// console.log("FileUploadWidget: showUrl");
    ctrl.isFile = false;
	$scope.properties.deleteFile=false;
  }
  
  this.showUpload = function() {
	// console.log("FileUploadWidget: showUpload");
    ctrl.isFile = true;
    ctrl.filename = $scope.properties.currentFile.fileName
  }
  this.cancelEdit = function() {
    ctrl.newFile=false;
    ctrl.editMode=false;
    ctrl.filemodel = '';
    $scope.properties.newFile = null;
    ctrl.filename = $scope.properties.currentFile.fileName;
  }

  this.uploadError = function (error) {
    $log.warn('upload fails too', error);
    ctrl.filemodel = '';
    ctrl.filename = "Upload failed";
  }

  this.startUploading = function() {
    console.log("FileUploadWidget: startUploading");
    ctrl.filemodel = '';
    ctrl.filename  = 'Uploading...';
  }

  this.uploadComplete = function(response) {
    console.log("FileUploadWidget: uploadComplete response="+angular.toJson(response));
  
    //when the upload widget return a String, it means an error has occurred (with a html document as a response)
    //if it's not a string, we test if it contains some error message
    if(angular.isString(response) || (response && response.type && response.message)){
      $log.warn('upload fails');
      ctrl.filemodel = '';
      ctrl.filename = 'Upload failed';
      $scope.properties.errorContent = angular.isString(response) ? response : response.message;
      return;
    }
    $scope.properties.deleteFile = false;

    $scope.properties.newFile = response;
    
  }
  
  this.getClassIsFile = function () {
	if (ctrl.isFile)
		return "btn btn-success";
	else
		return "btn btn-primary";
  }
  this.getClassIsUrl = function () {
	if (ctrl.isFile)
		return "btn btn-primary";
	else
		return "btn btn-success";
  }
  

  function forceSubmit(event) {
    if(!event.target.value) {
      return;
    }

    form.triggerHandler('submit');
    form[0].submit();
    event.target.value = null;
  }
}