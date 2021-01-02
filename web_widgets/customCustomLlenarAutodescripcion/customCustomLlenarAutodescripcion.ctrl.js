function ($scope, blockUI) {
    
    blockUI.start();
    // function llenarObjeto(_objeto){
    //     debugger;
    //     $scope.properties.formInput = $scope.properties.objBDM.autodescripcion;
    //     $scope.properties.formInput.idiomas = getValue($scope.properties.objBDM.idiomas, "list");
    //     $scope.properties.formInput.hermanos = getValue($scope.properties.objBDM.idiomas, "list");
    //     $scope.properties.formInput.gruposSociales = getValue($scope.properties.objBDM.gruposSociales, "list");
    //     $scope.properties.formInput.informacionEscolar = getValue($scope.properties.objBDM.informacionEscolar, "list");
    //     $scope.properties.formInput.universidadesHasEstado = getValue($scope.properties.objBDM.universidadesHasEstado, "list");
    //     $scope.properties.formInput.terapias = getValue($scope.properties.objBDM.terapias, "list");
    // }
    
    // function getValue(_value, _type){
    //     let output = null;
    //     if(_type === "string"){
    //         _value === undefined ? "" : _value;
    //     } else if(_type === "list"){
    //         _value === undefined ? [] : _value;
    //     } else if(_type === "object"){
    //         _value === undefined ? null : _value;
    //     }
        
    //     return output;
    // }
    
    $scope.$watch("properties.objetoAutodescripcion",function(){
        if($scope.properties.objetoAutodescripcion !== undefined){
            $scope.properties.formInput = $scope.properties.objetoAutodescripcion;
            $scope.properties.selectedIndex = $scope.properties.formInput.pageIndex === undefined ? 0 : $scope.properties.formInput.pageIndex;
            $scope.properties.formInput.hermanos = [];
            $scope.properties.formInput.gruposSociales = [];
            $scope.properties.formInput.terapias = [];
            $scope.properties.formInput.informacionEscolar = [];
            $scope.properties.formInput.universidadesHasEstado = [];
            $scope.properties.formInput.idiomas = [];
            $scope.properties.formInput.parienteEgresadoAnahuac = [];
            
             $scope.$watch("properties.objetoHermanos",function(){
                if($scope.properties.objetoHermanos !== undefined){
                    $scope.properties.formInput.hermanos = $scope.properties.objetoHermanos;
                }
            });
            
            $scope.$watch("properties.objetoTerapias",function(){
                if($scope.properties.objetoTerapias !== undefined){
                    // let terapias = [];
                    // for(let i = 0; i < $scope.properties.objetoTerapias.length; i++){
                    //     let terapia = $scope.properties.objetoTerapias[i];
                    //     terapia.catTipoTerapia = JSON.parse(terapia.catTipoTerapia);
                    //     terapias.push(terapia);
                    // }
                    
                    // $scope.properties.formInput.terapias = terapias;
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
                }
            });
            
            $scope.$watch("properties.objetoFamiliarEgresadoAnahuac",function(){
                if($scope.properties.objetoFamiliarEgresadoAnahuac !== undefined){
                    $scope.properties.formInput.parienteEgresadoAnahuac = $scope.properties.objetoFamiliarEgresadoAnahuac;
                }
            });
            
            blockUI.stop();
        }
    });
    
    // $scope.$watch("properties.objetoHermanos",function(){
    //     debugger;
    //     if($scope.properties.objetoHermanos !== undefined){
    //         $scope.properties.formInput.hermanos = $scope.properties.objetoHermanos;
    //     }
    // });
    
    // $scope.$watch("properties.objetoTerapias",function(){
    //     if($scope.properties.objetoTerapias !== undefined){
    //         $scope.properties.formInput.terapias = $scope.properties.objetoTerapias;
    //     }
    // });
    
    // $scope.$watch("properties.objetoGruposSociales",function(){
    //     if($scope.properties.objetoAutodescripcion !== undefined){
    //         $scope.properties.formInput.gruposSociales = $scope.properties.objetoGruposSociales;
    //     }
    // });
    
    // $scope.$watch("properties.objInformacionEscolar",function(){
    //     if($scope.properties.objInformacionEscolar !== undefined){
    //         $scope.properties.formInput.informacionEscolar = $scope.properties.objInformacionEscolar;
    //     }
    // });
    
    // $scope.$watch("properties.objetoUninversidadesHasEstado",function(){
    //     if($scope.properties.objetoUninversidadesHasEstado !== undefined){
    //         $scope.properties.formInput.universidadesHasEstado = $scope.properties.objetoUninversidadesHasEstado;
    //     }
    // });
    
    // $scope.$watch("properties.objetoIdiomas",function(){
    //     if($scope.properties.objetoIdiomas !== undefined){
    //         $scope.properties.formInput.idiomas = $scope.properties.objetoIdiomas;
    //     }
    // });
}