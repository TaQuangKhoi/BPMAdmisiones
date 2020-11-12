function ($scope, $http) {
    
    $scope.formInput = { 
        "conIsPagoValidado" : false, 
        "isPagoTarjeta" : false ,
        "detalleSolicitudInput" : {
            "ordenPago":""
        }
    }

    function executeTask(){
        let ipBonita = window.location.protocol + "//" + window.location.host + "/bonita";
        let url = ipBonita + "/API/bpm/userTask/" + $scope.properties.taskId + "/execution";
        
        $http.post(url, $scope.formInput).success(function(data){
            alert("se ejecuto la tarea");
        }).error(function(error){
            alert("Fallo la tarea" + JSON.stringify(error));
        })
    }
    
    $scope.$watch("properties.bankInformationSPEI", function(){
        if($scope.properties.bankInformationSPEI !== undefined){
            if($scope.properties.bankInformationSPEI.success){
                $scope.formInput.isPagoTarjeta = false;
                $scope.formInput.detalleSolicitudInput.ordenPago = $scope.properties.bankInformationSPEI.data[0].id;
                executeTask();
            }
        }
    });
    
    $scope.$watch("properties.paymentInformationOXXO", function(){
        if($scope.properties.paymentInformationOXXO !== undefined){
            if($scope.properties.paymentInformationOXXO.success){
                $scope.formInput.isPagoTarjeta = false;
                $scope.formInput.detalleSolicitudInput.ordenPago = $scope.properties.paymentInformationOXXO.data[0].id;
                executeTask();
            }
        }
    });
    
    $scope.$watch("properties.paymentInfoCard", function(){
        if($scope.properties.paymentInfoCard !== undefined){
            if($scope.properties.paymentInfoCard.success){
                $scope.formInput.isPagoTarjeta = true;
                $scope.formInput.detalleSolicitudInput.ordenPago = $scope.properties.paymentInfoCard.data[0].id;
                executeTask();
            }
        }
    });
}