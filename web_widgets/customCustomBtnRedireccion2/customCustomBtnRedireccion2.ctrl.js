function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';

    var vm = this;


    $scope.redirect = function() {

        if ($scope.properties.Direccion == "window.history.back()") {
            window.history.back();
        } else {
            var url = "/portal/resource/app/administrativo/" + $scope.properties.Direccion + "/content/";
            window.location.replace(url);
        }
        //var url = "/apps/administrativo/"+$scope.properties.Direccion;
        //window.top.location.href = url;
    }
}