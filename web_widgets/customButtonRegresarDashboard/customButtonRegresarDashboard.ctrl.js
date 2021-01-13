function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    $scope.redirecc = function(){
      let ipBonita = window.location.protocol + "//" + window.location.host + "";
     let url = "";
	 url = ipBonita + "/portal/resource/app/administrativo/dashboard/content/";
     console.log(url)
    window.location.replace(url); 
    }

}
