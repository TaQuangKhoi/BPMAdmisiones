function PbTableCtrl($scope, $http, $location, $log, $window, localStorageService, modalService,blockUI) {
    this.isArray = Array.isArray;
    $scope.dynamicInput={};
    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row) {
        $scope.properties.selectedRow = row;
        $scope.properties.isSelected = 'editar';
    };

    this.selectRowDelete = function(row) {
        swal("¿Está seguro que desea eliminar?", {
                buttons: {
                    cancel: "No",
                    catch: {
                        text: "Si",
                        value: "Si",
                    }
                },
            })
            .then((value) => {
                switch (value) {
                    case "Si":
                        debugger
                        $scope.properties.selectedRow = row;
                        row.isEliminado = true
                        $scope.properties.selectedRow["todelete"] = false;
                        
                        $scope.$apply();
                        startProcess();
                        break;
                    default:

                }
            });
        /*
        
        $scope.properties.isSelected = 'editar';*/
    };

    this.isSelected = function(row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }

    function startProcess() {
        if ($scope.properties.processId) {
            var prom = doRequest('POST', '../API/bpm/process/' + $scope.properties.processId + '/instantiation', $scope.properties.userId).then(function() {
                localStorageService.delete($window.location.href);
            });

        } else {
            $log.log('Impossible to retrieve the process definition id value from the URL');
        }
    }

    function doRequest(method, url, params) {
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                doRequestGet();
                swal("Eliminado correctamente!", "", "success");
            })
            .error(function(data, status) {

            }).finally(function () {
                blockUI.stop();
            });
    }
    
    function doRequestGet() {
        blockUI.start();
        var req = {
            method: "GET",
            url: `${$scope.properties.urlGet}&jsonData=${encodeURIComponent(JSON.stringify($scope.properties.filtroToSend))}`
        };

        return $http(req)
            .success(function(data, status) {
                $scope.properties.contenido = data.data;
                $scope.value = data.totalRegistros;
			    $scope.loadPaginado();
            })
            .error(function(data, status) {

            }).finally(function () {
                blockUI.stop();
            });
	}
	$scope.$watch("properties.filtroToSend", function (newValue, oldValue) {
        if (newValue !== undefined) {
            doRequestGet();
        }
        console.log($scope.properties.filtroToSend);
    });
        $scope.setOrderBy= function(order){
        if($scope.properties.filtroToSend.orderby == order){
            $scope.properties.filtroToSend.orientation = ($scope.properties.filtroToSend.orientation=="ASC")?"DESC":"ASC";
        }else{
            $scope.properties.filtroToSend.orderby = order;
            $scope.properties.filtroToSend.orientation = "ASC";
        }
        doRequestGet();
    }
    $scope.filterKeyPress= function(columna,press){
        var aplicado = true;
        for (let index = 0; index < $scope.properties.filtroToSend.lstFiltro.length; index++) {
            const element = $scope.properties.filtroToSend.lstFiltro[index];
            if(element.columna==columna){
                $scope.properties.filtroToSend.lstFiltro[index].valor=press;
                $scope.properties.filtroToSend.lstFiltro[index].operador="Que contenga";
                aplicado=false;
            }
            
        }
        if(aplicado){
            var obj = 	{ "columna":columna, "operador":"Que contenga", "valor":press }
            $scope.properties.filtroToSend.lstFiltro.push(obj);
        }
        
        doRequestGet();
    }
    $scope.lstPaginado = [];
    $scope.valorSeleccionado = 1;
    $scope.iniciarP = 1;
    $scope.finalP = 10;
    $scope.valorTotal = 10;
    
    $scope.loadPaginado = function(){
        $scope.valorTotal = Math.ceil($scope.value/$scope.properties.filtroToSend.limit);
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
    
    $scope.siguiente = function(){
        var objSelected = {};
        for(var i in $scope.lstPaginado){
            if($scope.lstPaginado[i].seleccionado){
                objSelected = $scope.lstPaginado[i];
                $scope.valorSeleccionado=$scope.lstPaginado[i].numero;
            }
        }
        $scope.valorSeleccionado=$scope.valorSeleccionado+1;
        if($scope.valorSeleccionado>Math.ceil($scope.value/$scope.properties.filtroToSend.limit)){
            $scope.valorSeleccionado = Math.ceil($scope.value/$scope.properties.filtroToSend.limit);
        }
        $scope.seleccionarPagina($scope.valorSeleccionado);
    }

    $scope.anterior = function(){
        var objSelected = {};
        for(var i in $scope.lstPaginado){
            if($scope.lstPaginado[i].seleccionado){
                objSelected = $scope.lstPaginado[i];
                $scope.valorSeleccionado=$scope.lstPaginado[i].numero;
            }
        }
        $scope.valorSeleccionado=$scope.valorSeleccionado-1;
        if($scope.valorSeleccionado == 0){
            $scope.valorSeleccionado = 1;
        }
        $scope.seleccionarPagina($scope.valorSeleccionado);
    }

    $scope.seleccionarPagina = function(valorSeleccionado){
        var objSelected = {};
        for(var i in $scope.lstPaginado){
            if($scope.lstPaginado[i].numero == valorSeleccionado){
                $scope.inicio = ($scope.lstPaginado[i].numero-1);
                $scope.fin = $scope.lstPaginado[i].fin;
                $scope.valorSeleccionado=$scope.lstPaginado[i].numero;
                $scope.properties.filtroToSend.offset=(($scope.lstPaginado[i].numero - 1) * $scope.properties.filtroToSend.limit)
            }
        }

        doRequestGet();
	}
	$scope.filtroCampus = ""
    $scope.addFilter = function () {
        var filter = {
            "columna": "CAMPUS",
            "operador": "Igual a",
            "valor": $scope.filtroCampus
        }
        if ($scope.properties.filtroToSend.lstFiltro.length > 0) {
            var encontrado = false;
            for (let index = 0; index < $scope.properties.filtroToSend.lstFiltro.length; index++) {
                const element = $scope.properties.filtroToSend.lstFiltro[index];
                if (element.columna == "CAMPUS") {
                    $scope.properties.filtroToSend.lstFiltro[index].columna = filter.columna;
                    $scope.properties.filtroToSend.lstFiltro[index].operador = filter.operador;
                    $scope.properties.filtroToSend.lstFiltro[index].valor = $scope.filtroCampus;
                    encontrado = true
                }
                if (!encontrado) {
                    $scope.properties.filtroToSend.lstFiltro.push(filter);
                }

            }
        } else {
            $scope.properties.filtroToSend.lstFiltro.push(filter);
        }
    }
    $scope.sizing=function(){
        $scope.lstPaginado = [];
        $scope.valorSeleccionado = 1;
        $scope.iniciarP = 1;
        $scope.finalP = 10;
        try{
            $scope.properties.filtroToSend.limit=parseInt($scope.properties.filtroToSend.limit);
        }catch(exception){
            
        }
        
        doRequestGet();
    }
}