function ($scope, blockUI, $http) {
    
    blockUI.start();

    function getBdmObject(_object){
        let url = _object.href;
        
        var req = {
            method: "GET",
            url: url
        };

        return $http(req)
        .success(function(data, status) {
            if( Object.keys(data).length === 0 && data.constructor === Object){
                $scope.properties.formInput[_object.rel] = null;
            } else {
                $scope.properties.formInput[_object.rel] = data;
            }
        })
        .error(function(data, status) {
            // swal("¡Error!","NO se ha podido obtener los datos de la tarea.","error");
        });
    }
    
    $scope.$watch("properties.objetoAutodescripcion",function(){
        if($scope.properties.objetoAutodescripcion !== undefined){
            $scope.properties.formInput = $scope.properties.objetoAutodescripcion;
            $scope.properties.selectedIndex = $scope.properties.formInput.pageIndex === undefined ? -1 : $scope.properties.formInput.pageIndex;
            $scope.properties.formInput.hermanos = [];
            $scope.properties.formInput.gruposSociales = [];
            $scope.properties.formInput.terapias = [];
            // $scope.properties.formInput.informacionEscolar = [];
            if($scope.properties.formInput.informacionEscolar === undefined ){
                $scope.properties.formInput.informacionEscolar = [];
            } else if ($scope.properties.formInput.informacionEscolar.length === 0){
                $scope.properties.formInput.informacionEscolar = []
            }
            
            $scope.properties.formInput.universidadesHasEstado = [];
            $scope.properties.formInput.idiomas = [];
            $scope.properties.formInput.parienteEgresadoAnahuac = [];
            
            if($scope.properties.formInput.anoMuertePadre !== null && $scope.properties.formInput.anoMuertePadre !== undefined){
                $scope.properties.formInput.anoMuertePadre = parseInt($scope.properties.formInput.anoMuertePadre); 
            } 
            
            if($scope.properties.formInput.anoMuerteMadre !== null && $scope.properties.formInput.anoMuerteMadre !== undefined){
                $scope.properties.formInput.anoMuerteMadre = parseInt($scope.properties.formInput.anoMuerteMadre); 
            }
            
             $scope.$watch("properties.objetoHermanos",function(){
                if($scope.properties.objetoHermanos !== undefined){
                    $scope.properties.formInput.hermanos = $scope.properties.objetoHermanos;
                    $scope.properties.formInput.tienesHermanos = true;   
                }
            });
            
            $scope.$watch("properties.objetoTerapias",function(){
                if($scope.properties.objetoTerapias !== undefined){
                    $scope.properties.formInput.terapias = $scope.properties.objetoTerapias;
                }
            });
            
            $scope.$watch("properties.objetoGruposSociales",function(){
                if($scope.properties.objetoAutodescripcion !== undefined){
                    $scope.properties.formInput.gruposSociales = $scope.properties.objetoGruposSociales;
                }
            });
            
            $scope.$watch("properties.objInformacionEscolar",function(){
                if($scope.properties.objInformacionEscolar !== undefined){
                    if($scope.properties.objInformacionEscolar.length > 0){
                        $scope.properties.formInput.informacionEscolar = $scope.properties.objInformacionEscolar;    
                    }
                    
                }
            });
            
            $scope.$watch("properties.objetoUninversidadesHasEstado",function(){
                if($scope.properties.objetoUninversidadesHasEstado !== undefined){
                    $scope.properties.formInput.universidadesHasEstado = $scope.properties.objetoUninversidadesHasEstado;
                }
            });
            
            $scope.$watch("properties.objetoIdiomas",function(){
                if($scope.properties.objetoIdiomas !== undefined){
                    $scope.properties.formInput.idiomas = $scope.properties.objetoIdiomas;
                    $scope.properties.idiomasHablas = true;
                }
            });
            
            $scope.$watch("properties.objetoFamiliarEgresadoAnahuac",function(){
                if($scope.properties.objetoFamiliarEgresadoAnahuac !== undefined){
                    $scope.properties.formInput.parienteEgresadoAnahuac = $scope.properties.objetoFamiliarEgresadoAnahuac;
                }
            });
            
            for(let i = 0; i < $scope.properties.objetoAutodescripcion.links.length; i++){
                getBdmObject($scope.properties.objetoAutodescripcion.links[i]);
            }

            blockUI.stop();
        } else {
            blockUI.stop();
        }
    });
}