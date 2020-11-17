function ($scope, modalService) {
    
    $scope.showModalLogout = function(){
        openModal($scope.properties.idModalLogout);
    }
    
    function openModal(modalId) {
        modalService.open(modalId);
    }
}