function ($scope, $http) {
    
    $scope.formInput = { 
        "conIsPagoValidado" : false, 
        "isPagoTarjeta" : false ,
        "detalleSolicitudInput" : {
            "ordenPago":""
        }
    }
    
    $scope.newNavValue = "";

    function executeTask(){
        let ipBonita = window.location.protocol + "//" + window.location.host + "/bonita";
        let url = ipBonita + "/API/bpm/userTask/" + $scope.properties.taskId + "/execution";
        
        $http.post(url, $scope.formInput).success(function(data){
            $scope.properties.navigationVar = $scope.newNavValue;
            if($scope.reloadPage){
                window.location.reload();   
            }
        }).error(function(error){
            console.log("Fallo la tarea" + JSON.stringify(error));
        })
    }
    
    $scope.$watch("properties.bankInformationSPEI", function(){
        if($scope.properties.bankInformationSPEI !== undefined){
            if($scope.properties.bankInformationSPEI.success){
                $scope.formInput.isPagoTarjeta = false;
                $scope.formInput.detalleSolicitudInput.ordenPago = $scope.properties.bankInformationSPEI.data[0].id;
                if($scope.properties.isCorrectTask){
                    $scope.newNavValue = "paymentWaiting";
                    $scope.reloadPage = false;
                    executeTask();  
                }
            }
        }
    });
    
    $scope.$watch("properties.paymentInformationOXXO", function(){
        if($scope.properties.paymentInformationOXXO !== undefined){
            if($scope.properties.paymentInformationOXXO.success){
                $scope.formInput.isPagoTarjeta = false;
                $scope.formInput.detalleSolicitudInput.ordenPago = $scope.properties.paymentInformationOXXO.data[0].id;
                if($scope.properties.isCorrectTask){
                    $scope.newNavValue = "paymentWaiting";
                    $scope.reloadPage = false;
                    executeTask();    
                }
            }
        }
    });
    
    $scope.$watch("properties.paymentInfoCard", function(){
        if($scope.properties.paymentInfoCard !== undefined){
            if($scope.properties.paymentInfoCard.success){
                $scope.formInput.isPagoTarjeta = true;
                $scope.formInput.detalleSolicitudInput.ordenPago = $scope.properties.paymentInfoCard.data[0].id;
                if($scope.properties.isCorrectTask){
                    $scope.newNavValue = "cardPaid";
                    $scope.reloadPage = true;
                    executeTask();
                }
            }
        }
    });
    
    $scope.getOrderInformation = function(){
        let url = "/bonita/API/extension/AnahuacRest?url=getOrderDetails&p=0&c=10";
        $http.post(url, $scope.properties.orderObject).success(function(success){
            $scope.properties.orderOutput = success.data[0];
        }).error(function(error){
            swal("Error", JSOM.stringify(error), "error");
        }).finally(function(){
          
        })  
    };
    
    $scope.$watch("properties.orderObject", function(){
        if($scope.properties.orderObject !== undefined && $scope.properties.orderObject !== null){
            $scope.getOrderInformation();
        }
    })
}