function PbTableCtrl($scope) {

    this.isArray = Array.isArray;
    $scope.loaded = false;
    $scope.objLoaded = false;

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
    
    $scope.setValue = function(value, index){
        let newValue = null;
        for(let i = 0; i < $scope.properties.catTerapia.length; i++){
            if($scope.properties.catTerapia[i].descripcion === value){
                newValue = $scope.properties.catTerapia[i];
            }
        }
        $scope.properties.content[index].catRecibidoTerapia = newValue;
    };
    
    function initTable(){
        if(!$scope.loaded){
            if($scope.properties.catTipoTerapia !== undefined && $scope.properties.catTerapia !== undefined){
                if($scope.properties.catTipoTerapia.length > 0 && $scope.properties.catTerapia.length > 0){
                    $scope.properties.content = [];
                    let no = {};
                    for(let i = 0; i < $scope.properties.catTerapia.length; i++){
                        if($scope.properties.catTerapia[i].descripcion === "No"){
                            no = $scope.properties.catTerapia[i];
                        }
                    }
            
                    for(let i = 0; i < $scope.properties.catTipoTerapia.length; i++){
                        let objTerapia = {
                            "catTipoTerapia": $scope.properties.catTipoTerapia[i],
                            "catRecibidoTerapia": no,
                            "tipoTerapia": "",
                            "cuantoTiempo": "",
                            "recibidoTerapiaString":"No",
                            "persistenceId_string": null
                        };
                        
                        $scope.properties.content.push(objTerapia);
                    }
                }
            }
        }
    }
    
    $scope.$watch("properties.catTerapia", function(){
       if($scope.properties.catTerapia !== undefined && $scope.properties.catTipoTerapia !== undefined && $scope.properties.content.length === 0){
           initTable();
       } 
    });
    
    $scope.$watch("properties.catTipoTerapia", function(){
       if($scope.properties.catTerapia !== undefined && $scope.properties.catTipoTerapia !== undefined && $scope.properties.content.length === 0){
           initTable();
       } 
    });
    
    $scope.$watch("properties.objetoTerapias", function(){
        if($scope.properties.objetoTerapias !== undefined ){
            if($scope.properties.objetoTerapias.length  > 0){
                if($scope.loaded){
                    $scope.loaded = true;
                    $scope.properties.content = $scope.properties.objetoTerapias;    
                } else {
                    $scope.objLoaded = true;
                    initTable();
                }
               
            }
        } 
    });
    
}
