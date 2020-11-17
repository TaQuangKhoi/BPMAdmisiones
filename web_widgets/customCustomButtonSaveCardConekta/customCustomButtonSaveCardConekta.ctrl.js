function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';
    
    var vm = this;

    this.action = function action() {
        $scope.execute();
    };
    $scope.tokenParams = {};
    $scope.tokenId = "";
    
    $scope.execute = function () {
        $scope.tokenParams = {
            "card": {
              "number": $scope.properties.dataToSend.cardNumber,
              "name":  $scope.properties.dataToSend.cardholder,
              "exp_year": "20" + $scope.properties.dataToSend.expireYear,
              "exp_month": $scope.properties.dataToSend.expireMonth,
              "cvc": $scope.properties.dataToSend.cvv,
              "address": $scope.properties.dataToSend.address
            }
        };
        Conekta.setPublicKey("key_BKn3nrrQJGw1qybfcirDprg");
        Conekta.Token.create($scope.tokenParams, successResponseHandler, errorResponseHandler);
    }
    
    var successResponseHandler = function (token) {
        $scope.properties.dataFromSuccess = token.id;
        $scope.properties.cardToken = token.id;
        let data = angular.copy($scope.properties.objectCard);
        data.idToken = token.id;
        
        doRequest(data, null);
    };
    
    var errorResponseHandler = function (error) {
        console.error(error)
    };
    
    /**
   * Execute a get/post request to an URL
   * It also bind custom data from success|error to a data
   * @return {void}
   */
    function doRequest(data, params) {
        vm.busy = true;
        var req = {
            method: "POST",
            url: $scope.properties.url,
            data: data,
            params: params
        };

        return $http(req)
        .success(function(data, status) {
            $scope.properties.dataFromSuccess = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromError = undefined;
            $scope.properties.paymentInfoCard = data
            notifyParentFrame({ message: 'success', status: status, dataFromSuccess: data, dataFromError: undefined, responseStatusCode: status});
            if ($scope.properties.targetUrlOnSuccess && method !== 'GET') {
                redirectIfNeeded();
            }
            closeModal($scope.properties.closeOnSuccess);
        })
        .error(function(data, status) {
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status});
        })
        .finally(function() {
            vm.busy = false;
        });
    }
}
