function PbTableCtrl($scope,blockUI,modalService,$http) {

  this.isArray = Array.isArray;

  this.isClickable = function () {
    return $scope.properties.isBound('selectedRow');
  };

  this.selectRow = function (row) {
    if (this.isClickable()) {
      $scope.properties.selectedRow = row;
    }
  };

  this.isSelected = function(row) {
    return angular.equals(row, $scope.properties.selectedRow);
  }
        $scope.preview = function(codigo) {
       blockUI.start()   
      var req = {
          method: "POST",
          url: "/bonita/API/extension/AnahuacRest?url=generateHtml&p=0&c=10",
          data: angular.copy({
                                "campus": $scope.properties.campusSelected,
                                "correo": "eventosgiley@gmail.com",
                                "codigo": codigo,
                                "isEnviar": false
                              })
      };

      return $http(req)
          .success(function(data, status) {
              $scope.properties.previewHtml = data.data[0]
              modalService.open("previewerid");
              $scope.$apply();


          })
          .error(function(data, status) {
              $("#loading").modal("hide");
              console.error(data)
          })
          .finally(function() {
              blockUI.stop()
          });
  }
}
