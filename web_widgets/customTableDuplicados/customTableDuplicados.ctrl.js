function PbTableCtrl($scope, $http, $window) {

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
  $scope.asignarTarea = function (rowData) {
    var req = {
        method: "GET",
        url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseId}&f=isFailed%3dfalse`
    };

    return $http(req)
        .success(function (data, status) {
            //var url = "/bonita/apps/administrativo/verSolicitudAdmision/?id=[TASKID]&displayConfirmation=false";
            var url = "/bonita/portal/resource/app/administrativo/verSolicitudAdmision/content/?id=[TASKID]&displayConfirmation=false";
            url = url.replace("[TASKID]", data[0].id);
            //window.top.location.href = url;
            window.open(url,'_blank');
        })
        .error(function (data, status) {
            console.error(data);
        })
        .finally(function () { });
}
  
}
