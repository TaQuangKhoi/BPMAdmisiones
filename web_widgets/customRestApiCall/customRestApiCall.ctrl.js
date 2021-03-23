function WidgetrestApiCallController($scope,$http) {
    
    this.initData= function(){
     $scope.postPayload='{\n"param1":"abc",\n"param2":123,\n"param3":true\n}';
     $http({method:  'GET',url: '/bonita/API/system/session/1'})
            .success(function (data, status, headers, config) {
                $scope.apiToken= headers('X-Bonita-API-Token');
            });
    }
    
    this.callApi = function() {
         $scope.response={};
         
         var apiUrl= '/bonita/API/extension/' + $scope.properties.pathTemplate;
         if ($scope.properties.queryString){
             apiUrl+= "?"+ $scope.properties.queryString ;
         }
         
         $http({method:  $scope.properties.method, url: apiUrl, data: $scope.postPayload, headers:{'X-Bonita-API-Token':$scope.apiToken} })
            .success(function (data, status, headers, config) {
                $scope.response.respData = data;
                $scope.response.respStatus = status;
                $scope.response.respHeaders = headers('awesome header');
            })
            .error(function (data, status, headers, config) {
                $scope.response.respData = data;
                $scope.response.respStatus = status;
            });
        
    };
    
     this.clear = function() {
                $scope.response = null;
                
    };
        
    
    
}