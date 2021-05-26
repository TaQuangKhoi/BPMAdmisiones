function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {
  $scope.action = function(){
      if($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0){
          $scope.properties.selectedIndex --; 
      } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)){
          if($scope.properties.isValidStep){
              
              blockUI.start();
              //revisar si el usuario del caso es el mismo que tiene la sesion iniciada
              var req = {
                method: "GET",
                url: "../API/system/session/unusedid",
              };
              return $http(req).success(function(data, status) {
                if($scope.properties.catSolicitudDeAdmision.correoElectronico != data.user_name){
                  swal("¡Error!", "Su sesion ha expirado", "warning");   
                  setTimeout(function(){ window.top.location.href = $scope.properties.urlDireccion }, 3000);
                }else{
                  $scope.properties.selectedIndex ++; 
                  submitTask();
                }
              })
              .error(function(data, status) {
                  swal("¡Error!", data.message, "error");
              })
              .finally(function() {
          
              }); 
              
          } else {
              swal($scope.properties.messageTitle, $scope.properties.errorMessage, "warning");
          }
      }
  }
  
  function submitTask() {
    var id;
    id = $scope.properties.taskId;
    if (id) {
      doRequest('POST', '../API/bpm/userTask/' + id + '/execution').then(function() {
        localStorageService.delete($window.location.href);
      });
    } else {
        $log.log('Impossible to retrieve the task id value from the URL');
    }
      
  }
  
  function doRequest(method, url) {
      let dataToSend = angular.copy($scope.properties.formOutput);
      dataToSend.autodescripcionInput.pageIndex = $scope.properties.selectedIndex;
      
      var req = {
          method: method,
          url: url,
          // data: angular.copy($scope.properties.formOutput)
          data: dataToSend
      };
      
      return $http(req)
      .success(function(data, status) {
          getCurrentTask();
          console.log("Task done")
      })
      .error(function(data, status) {
          console.log("task failed")
      });
  }
  
  function getCurrentTask(){
      let contador = 0;
      let limite = 99
      
      let url = "../API/bpm/humanTask?p=0&c=10&f=caseId=" + $scope.properties.caseId +"&fstate=ready";
      
      var req = {
          method: "GET",
          url: url
      };

      return $http(req)
      .success(function(data, status) {
          if (data.length === 0){
              console.log("retry, no task found");
              getCurrentTask();
          } else if(data[0].id === $scope.properties.taskId){
              console.log("retry, same id");
              getCurrentTask();
          } else {
              $scope.properties.taskId = data[0].id;
              console.log("Nueva tarea", $scope.properties.taskId);
              blockUI.stop();
          }
      })
      .error(function(data, status) {
          getCurrentTask();
          if(contador <= limite){
              contador ++;
              getCurrentTask();
          } else {
              blockUI.stop();
          }
      });
  }
}