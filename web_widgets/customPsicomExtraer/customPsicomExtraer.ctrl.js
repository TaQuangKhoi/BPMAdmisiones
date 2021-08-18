function ($scope, $http) {
    
    function getPsicom(){
        debugger;
        let url =  window.location.protocol + "//" + window.location.hostname + "/bonita/API/extension/AnahuacRestGet?url=getPsicometricoCompleto&p=0&c=10&caseId=" + $scope.properties.caseId;
        
        $http.get(url).success((success)=>{
            console.log(success);
            if(success.data.length > 0){
                $scope.properties.testPsicom = success.data[0];   
            }
        }).error((err)=>{
            alert("no se pudo obtener le psicom" +  JSON.stringify(err));
        });
    }
    
    $scope.$watch("properties.caseId", ()=>{
        if($scope.properties.caseId){
           getPsicom(); 
        }
    });
}