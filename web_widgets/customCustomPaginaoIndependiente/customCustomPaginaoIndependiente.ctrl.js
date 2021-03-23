function PbTableCtrl($scope, blockUI) {

    $scope.lstPaginado = [];
    $scope.valorSeleccionado = 1;
    $scope.iniciarP = 1;
    $scope.finalP = 10;
    $scope.valorTotal = 10;
    $scope.inicio = 0;
    $scope.fin = 0;
    $scope.ultimaPagina = 0;
    $scope.primeraCarga = false;

    $scope.loadPaginado = function () {
        $scope.valorTotal = Math.ceil($scope.properties.totalRows);
        $scope.ultimaPagina = Math.ceil($scope.properties.totalRows / $scope.properties.pageSize);
        $scope.lstPaginado = [];
        
        if ($scope.valorSeleccionado <= 5) {
            $scope.iniciarP = 1;
            $scope.finalP = $scope.ultimaPagina > 10 ? 10 :$scope.ultimaPagina;
        }
        else {
            $scope.iniciarP = $scope.valorSeleccionado - 5;
            $scope.finalP = $scope.ultimaPagina > ($scope.valorSeleccionado + 4) ? ($scope.valorSeleccionado + 4) : $scope.ultimaPagina;
        }
        for (var i = $scope.iniciarP; i <= $scope.finalP; i++) {

            var obj = {
                "numero": i,
                "inicio": ((i * $scope.properties.pageSize) - 9),
                "fin": (i * $scope.properties.pageSize),
                "seleccionado": (i == $scope.valorSeleccionado)
            };
            
            $scope.lstPaginado.push(obj);
        }

        if(!$scope.primeraCarga){
            $scope.primeraCarga = true;
            $scope.fillPage();
        }
    }

    $scope.siguiente = function () {
        var objSelected = {};
        for (var i in $scope.lstPaginado) {
            if ($scope.lstPaginado[i].seleccionado) {
                objSelected = $scope.lstPaginado[i];
                $scope.valorSeleccionado = $scope.lstPaginado[i].numero;
            }
        }

        $scope.valorSeleccionado = $scope.valorSeleccionado + 1;
        if($scope.ultimaPagina < $scope.valorSeleccionado){
            $scope.valorSeleccionado = $scope.valorSeleccionado - 1;
        }
        
        $scope.seleccionarPagina($scope.valorSeleccionado);
    }

    $scope.anterior = function () {
        var objSelected = {};
        for (var i in $scope.lstPaginado) {
            if ($scope.lstPaginado[i].seleccionado) {
                objSelected = $scope.lstPaginado[i];
                $scope.valorSeleccionado = $scope.lstPaginado[i].numero;
            }
        }
        $scope.valorSeleccionado = $scope.valorSeleccionado - 1;
        if ($scope.valorSeleccionado == 0) {
            $scope.valorSeleccionado = 1;
        }
        $scope.seleccionarPagina($scope.valorSeleccionado);
    }

    $scope.seleccionarPagina = function (valorSeleccionado) {
        var objSelected = {};
        for (var i in $scope.lstPaginado) {
            if ($scope.lstPaginado[i].numero == valorSeleccionado) {
                $scope.lstPaginado[i].seleccionado = true;
                $scope.valorSeleccionado = $scope.lstPaginado[i].numero;
            } else {
                $scope.lstPaginado[i].seleccionado = false;
            }
        }

        $scope.fillPage();
    }

    $scope.sizing = function(){
        $scope.lstPaginado = [];
        $scope.valorSeleccionado = 1;
        $scope.iniciarP = 1;
        $scope.finalP = 10;

        try{
            $scope.properties.dataToSend.limit = parseInt($scope.properties.dataToSend.limit);
        }catch(exception){
            
        }

        $scope.fillPage();
    }

    $scope.fillPage = function(){
        blockUI.start();

        $scope.inicio = ($scope.valorSeleccionado - 1) * $scope.properties.pageSize;
        $scope.fin = (($scope.valorSeleccionado) * $scope.properties.pageSize) > $scope.properties.lstContenido.length ? $scope.properties.lstContenido.length : (($scope.valorSeleccionado) * $scope.properties.pageSize);
        $scope.properties.rowsShown = [];
        for(let i = $scope.inicio; i < $scope.fin; i++){
            if($scope.properties.rowsShown.length < i || $scope.inicio === 0){
                $scope.properties.rowsShown.push($scope.properties.lstContenido[i]);
            } else {
                break;
            }
        }

        blockUI.stop();
        $scope.loadPaginado();
    }
    
    $scope.$watch("properties.totalRows", function(){
        if($scope.properties.totalRows !== undefined){
            $scope.valorTotal = $scope.properties.totalRows;
            initWatcherPageSize();
        }
    });

    function initWatcherPageSize(){
        $scope.$watch("properties.pageSize", function(){
            $scope.primeraCarga = false;
            $scope.loadPaginado();
        });
    }
}