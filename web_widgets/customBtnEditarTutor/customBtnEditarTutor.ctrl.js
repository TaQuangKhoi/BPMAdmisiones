        function PbButtonCtrl($scope, modalService, $http, blockUI, $q, $filter) {
        var vm = this;

          this.action = function action() {
            if($scope.properties.banderaEdicion == true){
                $scope.properties.banderaEdicion = false;
                
            }else if($scope.properties.banderaEdicion == false){
                $scope.properties.banderaEdicion = true
            }
              
          };

        }
