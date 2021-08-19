function PbTableCtrl($scope) {

    $scope.listaTemporal = [];
    $scope.isRelativosBDM = false;
	$scope.tutorBDM = false;
	$scope.padreBDM = false;
	$scope.madreBDM = false;
    
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
    
    // function initWatcherPadre(){
    //     $scope.$watch("properties.datosPadre", function(){
    //         if($scope.properties.datosPadre !== undefined && $scope.isRelativosBDM === false){
    //             let datosPadre = angular.copy($scope.properties.datosPadre);
    //             datosPadre.jubilado = false;
    //             $scope.properties.content.push(datosPadre);
    //             initWatcherRelativos();
    //         }
    //     });
    // }
    
    // function initWatcherMadre(){
    //     $scope.$watch("properties.datosMadre", function(){
    //         if($scope.properties.datosMadre !== undefined && $scope.isRelativosBDM === false){
    //             let datosMadre = angular.copy($scope.properties.datosMadre);
    //             datosMadre.jubilado = false;
    //             $scope.listaTemporal.push(datosMadre);
    //             $scope.properties.content.push(datosMadre);
    //             // initWatcherTutor();
    //         }
    //     }); 
    // }
    
    // function initWatcherTutor(){
    //     $scope.$watch("properties.datosTutor", function(){
    //         if($scope.properties.datosTutor !== undefined && $scope.isRelativosBDM === false){
    //             let datosTutor = angular.copy($scope.properties.datosTutor);
    //             datosTutor.jubilado = false;
    //             $scope.listaTemporal.push($scope.properties.datosTutor);
    //             $scope.properties.content = $scope.listaTemporal;
    //             initWatcherPadre();
    //             initWatcherMadre();
    //             initWatcherRelativos();
    //         }
    //     }); 
    // }
  $scope.$watch('properties.relativosBDM', function(value) {
    if (angular.isDefined(value) && value !== null) {
        
    } else{
        value=[];
    }
  });
    function initWatcherRelativos(){
        $scope.$watchCollection("[properties.relativosBDM, properties.datosTutor, properties.datosMadre, properties.datosPadre]", function(){
			
			if($scope.properties.relativosBDM.length==0 && $scope.properties.datosTutor !== undefined && $scope.tutorBDM === false){
				$scope.tutorBDM = true;
				let tutor = $scope.properties.datosTutor;
				tutor.jubilado = false;
				$scope.properties.content.push(tutor);
			} else if($scope.properties.relativosBDM.length==0 && $scope.properties.datosPadre !== undefined && $scope.padreBDM === false){
				$scope.padreBDM = true;
				let padre = $scope.properties.datosPadre;
				padre.jubilado = false;
				$scope.properties.content.push(padre);
			} else if($scope.properties.relativosBDM.length==0 && $scope.properties.datosMadre !== undefined && $scope.madreBDM === false){
				$scope.madreBDM = true;
				let madre = $scope.properties.datosMadre;
				madre.jubilado = false;
				$scope.properties.content.push(madre);
			}
            
            console.log("RELATIVOS", $scope.properties.content);
        }); 
    }
    
    // initWatcherTutor();
    initWatcherRelativos();
}