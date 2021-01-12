function PbTableCtrl($scope, $http,blockUI) {
    var vm = this;
    $scope.isInsert = false;
    $scope.content = [];
    $scope.firma = { "persistenceId": null, "cargo": "", "correo": "", "grupo": "", "nombreCompleto": "", "persistenceVersion": null, "showCargo": false, "showCorreo": false, "showGrupo": false, "showTelefono": false, "showTitulo": false, "telefono": "", "titulo": "" }
    this.isArray = Array.isArray;

    this.isClickable = function () {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function (row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
        }
    };

    this.isSelected = function (row) {
        return angular.equals(row, $scope.properties.selectedRow);
    }
    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params, dataToSend, callback) {
        blockUI.start();
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                callback(data)
            })
            .error(function (data, status) {
                console.error(data)
            })
            .finally(function () {
                vm.busy = false;
                blockUI.stop();
            });
    }
    function initialize() {
        $scope.firma = { "persistenceId": null, "cargo": "", "correo": "", "grupo": "", "nombreCompleto": "", "persistenceVersion": null, "showCargo": false, "showCorreo": false, "showGrupo": false, "showTelefono": false, "showTitulo": false, "telefono": "", "titulo": "" }
    }
    $scope.openModal = function (kind, data) {
        $scope.isInsert = kind;
        if ($scope.isInsert) {
            initialize();
        } else {
            $scope.firma = angular.copy(data)
        }
        $(`#insFirma`).modal('show')
    }
    $scope.previewModal = function (data) {

        $scope.firma = angular.copy(data)
        var value = setFirma();
        var element = document.getElementById("firma");
        element.innerHTML = value
        $(`#previewFirma`).modal('show')
    }
    $scope.insertData = function () {
        doRequest("POST", `/bonita/API/extension/AnahuacRest?url=${($scope.isInsert) ? 'insertFirma' : 'updateFirma'}&p=0&c=10`, {}, $scope.firma, function (dataCallBack) {
            $(`#insFirma`).modal(`hide`)
            swal("Correcto", "Firma guardada", "success");
            $scope.loadCatalog();
        })
    }
    function setFirma() {

        var nombretitulo = "<p><b>" + $scope.firma.titulo + " " + $scope.firma.nombreCompleto + "</b> <br>";
        var cargo =     $scope.firma.cargo + "</p>";
        var telefono = "<p><img style='width: 10px;'src='https://iconsplace.com/wp-content/uploads/_icons/ffa500/256/png/phone-icon-11-256.png'>&nbsp;" + $scope.firma.telefono + "<br> ";
        var pagina = "<img style='width: 10px;'src='https://e7.pngegg.com/pngimages/102/938/png-clipart-universidad-anahuac-mexico-sur-universidad-anahuac-queretaro-anahuac-university-network-lions-angle-text.png'>&nbsp;" + $scope.firma.grupo + "<br>";
        var correo = "<img style='width: 10px;'src='https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRz4uRldQmURlsu0KPNiQ0FcXNM0G8No6IOcg&usqp=CAU'>&nbsp;" + $scope.firma.correo + "<br> </p>"
        return nombretitulo + cargo + telefono + pagina + correo;

    }
     $scope.$watch("properties.dataToSend", function(newValue, oldValue) {
         console.log("WATCHER")
        if (newValue !== undefined) {
			$scope.loadCatalog();
        }

    });
    $scope.setOrderBy= function(order){
        if($scope.properties.dataToSend.orderby == order){
            $scope.properties.dataToSend.orientation = ($scope.properties.dataToSend.orientation=="ASC")?"DESC":"ASC";
        }else{
            $scope.properties.dataToSend.orderby = order;
            $scope.properties.dataToSend.orientation = "ASC";
        }
		$scope.loadCatalog();
    }
    
    $scope.lstPaginado = [];
    $scope.valorSeleccionado = 1;
    $scope.iniciarP = 1;
    $scope.finalP = 10;
    $scope.valorTotal = 10;
    
    $scope.loadPaginado = function(){
        $scope.valorTotal = Math.ceil($scope.value/$scope.properties.dataToSend.limit);
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
        if($scope.valorSeleccionado>Math.ceil($scope.value/$scope.properties.dataToSend.limit)){
            $scope.valorSeleccionado = Math.ceil($scope.value/$scope.properties.dataToSend.limit);
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
                $scope.properties.dataToSend.offset=(($scope.lstPaginado[i].numero - 1) * $scope.properties.dataToSend.limit)
            }
        }
		$scope.loadCatalog();
    }
        $scope.getCampusByGrupo = function (campus) {
        var retorno = "";
        for (var i = 0; i < $scope.lstCampus.length; i++) {
            if (campus == $scope.lstCampus[i].valor) {
                retorno = $scope.lstCampus[i].descripcion
            }
            
        }
        return retorno;
    }
    $scope.lstMembership = [];
    $scope.$watch("properties.userId", function (newValue, oldValue) {
        if (newValue !== undefined) {
            var req = {
                method: "GET",
                url: `/API/identity/membership?p=0&c=10&f=user_id%3d${$scope.properties.userId}&d=role_id&d=group_id`
            };

            return $http(req)
                .success(function (data, status) {
                    $scope.lstMembership = data;
                })
                .error(function (data, status) {
                    console.error(data);
                })
                .finally(function () { });
        }
    });
        $scope.filtroCampus = ""
    $scope.addFilter = function () {
        var filter = {
            "columna": "CAMPUS",
            "operador": "Igual a",
            "valor": $scope.filtroCampus
        }
        if ($scope.properties.dataToSend.lstFiltro.length > 0) {
            var encontrado = false;
            for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
                const element = $scope.properties.dataToSend.lstFiltro[index];
                if (element.columna == "CAMPUS") {
                    $scope.properties.dataToSend.lstFiltro[index].columna = filter.columna;
                    $scope.properties.dataToSend.lstFiltro[index].operador = filter.operador;
                    $scope.properties.dataToSend.lstFiltro[index].valor = $scope.filtroCampus;
                    encontrado = true
                }
                if (!encontrado) {
                    $scope.properties.dataToSend.lstFiltro.push(filter);
                }

            }
        } else {
            $scope.properties.dataToSend.lstFiltro.push(filter);
        }
    }
    $scope.sizing=function(){
        $scope.lstPaginado = [];
        $scope.valorSeleccionado = 1;
        $scope.iniciarP = 1;
        $scope.finalP = 10;
        try{
            $scope.properties.dataToSend.limit=parseInt($scope.properties.dataToSend.limit);
        }catch(exception){
            
        }
        
		$scope.loadCatalog();
    }
	$scope.loadCatalog=function(){
		debugger;
        doRequest("GET", "/bonita/API/extension/AnahuacRestGet?url=getCatNotificacionesFirma&p=0&c=10&jsonData="+encodeURIComponent(JSON.stringify($scope.properties.dataToSend)), {}, null, function (dataCallBack) {
			$scope.content = dataCallBack.data;
			$scope.value = dataCallBack.totalRegistros;
			$scope.loadPaginado();
        })
	}
    $(function() {
        $scope.loadCatalog();
    })
}
