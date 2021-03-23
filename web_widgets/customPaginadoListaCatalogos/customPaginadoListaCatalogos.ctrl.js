function ($scope) {
    $scope.lstPaginado = [];
    $scope.valorSeleccionado = 1;
    $scope.iniciarP = 1;
    $scope.finalP = 20;
    $scope.valorTotal = 20;


    
    $scope.$watch('properties.value', function () {
        if($scope.properties.value != undefined){
            $scope.loadPaginado();
        }
    });
    
    $scope.loadPaginado = function(){
        
        $scope.valorTotal = Math.ceil($scope.properties.value/20);
        $scope.lstPaginado=[]
        if($scope.valorSeleccionado <= 20) {
            $scope.iniciarP = 1;
            $scope.finalP = $scope.valorTotal>20 ? 20 : $scope.valorTotal;
        }
        else {
            $scope.iniciarP = $scope.valorSeleccionado - 20;
            $scope.finalP = $scope.valorTotal>($scope.valorSeleccionado + 8) ? ($scope.valorSeleccionado + 8) : $scope.valorTotal;
        }
        for(var i=$scope.iniciarP; i<=$scope.finalP; i++){

            var obj = {
                "numero":i,
                "inicio":((i*20)-19),
                "fin":(i*20),
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
        if($scope.valorSeleccionado>Math.ceil($scope.properties.value/20)){
            $scope.valorSeleccionado = Math.ceil($scope.properties.value/20);
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
        
                $scope.properties.inicio = ($scope.lstPaginado[i].inicio);
                $scope.properties.fin = $scope.lstPaginado[i].fin;
                $scope.valorSeleccionado=$scope.lstPaginado[i].numero;
            }
        }

        $scope.loadPaginado();
    }
}