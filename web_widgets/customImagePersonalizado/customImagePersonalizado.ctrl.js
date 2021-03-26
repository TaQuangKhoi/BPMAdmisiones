function ImagePersonalizado($scope, $http, $location, $log, $window, localStorageService, modalService) {
    this.test = function test(){
        console.log("di clic en la imagen");
        $scope.properties.imgMostrar = $scope.properties.url;
        modalService.open($scope.properties.modalimgpreview);
    }
}


