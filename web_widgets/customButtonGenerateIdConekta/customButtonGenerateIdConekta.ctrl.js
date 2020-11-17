function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';
    
    var vm = this;

    this.action = function action() {
        $scope.execute();
    };

    $scope.tokenParams = {
        "card": {
          "number": "4242424242424242",
          "name": "Fulanito Pérez",
          "exp_year": "2020",
          "exp_month": "12",
          "cvc": "123",
          "address": {
            "street1": "Calle 123 Int 404",
            "street2": "Col. Condesa",
            "city": "Ciudad de Mexico",
            "state": "Distrito Federal",
            "zip": "12345",
            "country": "Mexico"
          }
        }
    };
    $scope.execute = function () {
        debugger;
        Conekta.setPublicKey("key_BKn3nrrQJGw1qybfcirDprg");
        Conekta.Token.create($scope.tokenParams, successResponseHandler, errorResponseHandler);
    }
  
    var successResponseHandler = function (token) {
        console.log(token);
        $scope.properties.dataFromSuccess = token.id;
    };
    
    var errorResponseHandler = function (error) {
        console.error(error)
    };
}
