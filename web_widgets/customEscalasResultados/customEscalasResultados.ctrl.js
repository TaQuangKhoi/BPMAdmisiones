/**
 * The controller is a JavaScript function that augments the AngularJS scope and exposes functions that can be used in the custom widget template
 * 
 * Custom widget properties defined on the right can be used as variables in a controller with $scope.properties
 * To use AngularJS standard services, you must declare them in the main function arguments.
 * 
 * You can leave the controller empty if you do not need it.
 */
function ($scope, $http) {
      /**
   * Execute a get/post request to an URL
   * It also bind custom data from success|error to a data
   * @return {void}
   */
   $scope.$watch("properties.respuesta", function (newValue, oldValue) {
        if (newValue !== undefined) {
            //doRequest(method, url, params,dataToSend)
        }
    });
    
    $scope.factorK = 
    [{"k":30,".5":15,".4":12,".2":6},
    {"k":29,".5":15,".4":12,".2":6},
    {"k":28,".5":14,".4":11,".2":6},
    {"k":27,".5":14,".4":11,".2":5},
    {"k":26,".5":13,".4":10,".2":5},
    {"k":25,".5":13,".4":10,".2":5},
    {"k":24,".5":12,".4":10,".2":5},
    {"k":23,".5":12,".4":9,".2":5},
    {"k":22,".5":11,".4":9,".2":4},
    {"k":21,".5":11,".4":8,".2":4},
    {"k":20,".5":10,".4":8,".2":4},
    {"k":19,".5":10,".4":8,".2":4},
    {"k":18,".5":9,".4":7,".2":4},
    {"k":17,".5":9,".4":7,".2":3},
    {"k":16,".5":8,".4":6,".2":3},
    {"k":15,".5":8,".4":6,".2":3},
    {"k":14,".5":7,".4":6,".2":3},
    {"k":13,".5":7,".4":5,".2":3},
    {"k":12,".5":6,".4":5,".2":2},
    {"k":11,".5":6,".4":4,".2":2},
    {"k":10,".5":5,".4":4,".2":2},
    {"k":9,".5":5,".4":4,".2":2},
    {"k":8,".5":4,".4":3,".2":2},
    {"k":7,".5":4,".4":3,".2":1},
    {"k":6,".5":3,".4":2,".2":1},
    {"k":5,".5":3,".4":2,".2":1},
    {"k":4,".5":2,".4":2,".2":1},
    {"k":3,".5":2,".4":1,".2":1},
    {"k":2,".5":1,".4":1,".2":0},
    {"k":1,".5":1,".4":0,".2":0},
    {"k":0,".5":0,".4":0,".2":0}];
    
    $scope.respuestaProcesadas = [];
    function aplicarFactor(){
        var valorK=0;
        $scope.properties.respuesta.foerEach(element =>{
            if (element.escala=='K') {
                valorK=element.puntuacion;
            }
        });
        
        var jsonk={};
        $scope.factorK.forEach(element =>{
            if(element.k==valorK){
                jsonk=element;
            }
        });
        
        $scope.properties.respuesta.forEach(element,index =>{
        $scope.respuestaProcesadas.push(angular.copy(element));
        if (element.escala=='Hs') {
            $scope.respuestaProcesadas[index].puntuacion =(element.puntuacion)+jsonk['.5'];
        }
        if (element.escala=='Dp') {
            $scope.respuestaProcesadas[index].puntuacion =(element.puntuacion)+jsonk['.5'];
        }
        if (element.escala=='Pt') {
            $scope.respuestaProcesadas[index].puntuacion =(element.puntuacion)+jsonk['.5'];
        }
        if (element.escala=='Es') {
            $scope.respuestaProcesadas[index].puntuacion =(element.puntuacion)+jsonk['.5'];
        }
        if (element.escala=='Ma') {
            $scope.respuestaProcesadas[index].puntuacion =(element.puntuacion)+jsonk['.5'];
        }
        
        });
    }
   
  function doRequest(method, url, params,dataToSend) {
    vm.busy = true;
    var req = {
      method: method,
      url: url,
      data: angular.copy(dataToSend),
      params: params
    };

    return $http(req)
      .success(function(data, status) {
        $scope.properties.dataFromSuccess = data;
        $scope.properties.responseStatusCode = status;
        $scope.properties.dataFromError = undefined;
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