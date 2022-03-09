function PbTableCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {
    this.isArray = Array.isArray;

    $scope.lstPaginado = [];
    $scope.valorSeleccionado = 1;
    $scope.iniciarP = 1;
    $scope.finalP = 10;
    $scope.valorTotal = 10;
    
    $scope.loadPaginado = function(){
        $scope.valorTotal = Math.ceil($scope.value/$scope.properties.dataToFilter.limit);
        $scope.lstPaginado=[]
        if($scope.valorSeleccionado <= 5) {
            $scope.iniciarP = 1;
            $scope.finalP = $scope.valorTotal>10 ? 10 : $scope.valorTotal;
        }
        else {
            $scope.iniciarP = $scope.valorSeleccionado - 5;
            $scope.finalP = $scope.valorTotal>($scope.valorSeleccionado + 4) ? ($scope.valorSeleccionado + 4) : $scope.valorTotal;
        }
        for(var i=$scope.iniciarP; i<=$scope.finalP; i++){

            var obj = {
                "numero":i,
                "inicio":((i*10)-9),
                "fin":(i*10),
                "seleccionado": (i == $scope.valorSeleccionado)
            };
            $scope.lstPaginado.push(obj);
        }
    }

    this.selectRow = function(row) {
        debugger
        $scope.properties.objCatGenerico.objCatGenerico = row;
        $scope.properties.accion = 'editar';
    };
    
    $scope.setOrderBy= function(order){
        debugger
        if($scope.properties.dataToFilter.orderby == order){
            $scope.properties.dataToFilter.orientation = ($scope.properties.dataToFilter.orientation=="ASC")?"DESC":"ASC";
        }else{
            $scope.properties.dataToFilter.orderby = order;
            $scope.properties.dataToFilter.orientation = "ASC";
        }
        getRegistrosCatalogo();
    }

     this.selectRowDelete = function(row) {
        swal("¿Esta seguro que desea eliminar?", {
            icon: "warning",
            buttons: {
                    cancel: "No", 
                    catch: {
                        text: "Si",
                        value: true,
                    }
                },
            })
            .then((value) => {
                if(value){
        
                    $scope.properties.objCatGenerico.objCatGenerico = angular.copy(row);
                    $scope.properties.objCatGenerico.objCatGenerico.isEliminado= true;
                    doRequest('POST', '../API/extension/AnahuacBecasRest?url=insertUpdateCatGenerico&p=0&c=0', $scope.properties.objCatGenerico )
                    $scope.properties.accion = 'tabla';
                }
            }); 
    };
       function doRequest(method, url, datos, params) {
        var req = {
            method: method,
            url: url,
            data: datos,
            params: params
        };
        return $http(req)
            .success(function(data, status) {
                getRegistrosCatalogo();
                swal("!Correcto!", "Se actualizaron los datos correctamente.", "success");
            })
            .error(function(data, status) {
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

        $scope.$watch("properties.dataToFilter", function(newValue, oldValue) {

        if (newValue !== undefined) {
            getRegistrosCatalogo();
        }
        console.log($scope.properties.dataToFilter);
    });

    
    function getRegistrosCatalogo(params) {
        var req = {
            method: "POST",
            url: $scope.properties.urlPost,
            data: angular.copy($scope.properties.dataToFilter),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                 $scope.properties.contenido = data.data;
                 $scope.value = data.totalRegistros;
                 $scope.loadPaginado();
                 console.log(data.data)
            })
            .error(function(data, status) {
                //notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
        }

}