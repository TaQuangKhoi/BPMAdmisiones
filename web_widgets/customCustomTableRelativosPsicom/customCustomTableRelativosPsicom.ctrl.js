function PbTableCtrl($scope) {

    $scope.listaTemporal = [];
    $scope.isRelativosBDM = false;
	$scope.tutorBDM = false;
	$scope.padreBDM = false;
	$scope.madreBDM = false;
    
    this.isArray = Array.isArray;
    
    this.isJubilado = function(id){
        for(let i=0; i<$scope.properties.content.length;i++){
            if($scope.properties.content[i].persistenceId == id){
                //$scope.properties.content[i].jubilado = !$scope.properties.content[i].jubilado;
                console.log($scope.properties.content[i].jubilado);
            }
        }
    }
    
    this.isClickable = function () {
        return $scope.properties.isBound('selectedRow');
    };
    
    this.desconoceSusDatos = function(dato){
        if(dato === undefined || dato === null || dato.toString().trim().length <= 0 ){
            return "desconoce sus datos";
        }
        return dato;
    }
    
    
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
        $scope.valorJubilado();
    } else{
        value=[];
    }
  });
  
    function initWatcherRelativos(){
        
       /* $scope.$watchCollection("[properties.relativosBDM, properties.datosTutor, properties.datosMadre, properties.datosPadre]", function(){
			
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
        }); */
    }
    
    $scope.$watch("properties.datosPadre", function (newValue, oldValue) {
        if (newValue !== undefined) {
            let existe = $scope.properties.content.find(element => element.persistenceId === $scope.properties.datosPadre.persistenceId)
            if(existe == null){
               let padre = $scope.properties.datosPadre;
				padre.jubilado = false;
				let isJubilado = $scope.properties.content.find(element => element.persistenceId === $scope.properties.datosPadre.persistenceId)
                $scope.properties.content.push(padre);
                $scope.valorJubilado()
            }
        }
    });
    
    $scope.$watch("properties.datosMadre", function (newValue, oldValue) {
        if (newValue !== undefined) {
            let existe = $scope.properties.content.find(element => element.persistenceId === $scope.properties.datosMadre.persistenceId)
            if(existe == null){
               let madre = $scope.properties.datosMadre;
			   madre.jubilado = false;
               $scope.properties.content.push(madre);
               $scope.valorJubilado()
           }
        }
    });
    
    $scope.$watch("properties.datosTutor", function (newValue, oldValue) {
        if (newValue !== undefined) {
            let existe = $scope.properties.content.find(element => element.persistenceId === $scope.properties.datosTutor.persistenceId)
            if(existe == null){
               let tutor = $scope.properties.datosTutor;
			   tutor.jubilado = false;
			   tutor.vive = {"descripcion":"Sí"};
			   tutor.vives = null;
               $scope.properties.content.push(tutor);
               $scope.valorJubilado()
           }
        }
    });
    
    $scope.valorJubilado = function(){
        
        if($scope.properties.relativosBDM.length === 3 && $scope.properties.content.length === 3 ){
            let valorindex = [];
            debugger;
            $scope.properties.relativosBDM.forEach(element => {
                for(let i=0; i<$scope.properties.content.length;i++){
                    let valor = valorindex.find(data => data == i);
                    if( valor == null ){
                        if($scope.properties.content[i].apellidos == element.apellidos && $scope.properties.content[i].nombre == element.nombre){
                            if($scope.properties.content[i].isTutor && $scope.properties.content[i].vives === null && element.vive === null){
                                $scope.properties.content[i].jubilado = element.jubilado;
                                valorindex.push(i);
                            }else if($scope.properties.content[i].vives === undefined){
                                $scope.properties.content[i].jubilado = element.jubilado;
                                valorindex.push(i);
                            }
                        }
                    }
                    
                }
            });
            
           // console.log($scope.properties.content);
        }
    }
    
     
    // initWatcherTutor();
    initWatcherRelativos();
}

    