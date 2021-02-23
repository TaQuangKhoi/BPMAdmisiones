function PbTableCtrl($scope, $http, $window, blockUI) {
    var vm = this;
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

    function doRequest(method, url, params) {
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                $scope.properties.lstContenido = data.data;
                $scope.value = data.totalRegistros;
                $scope.loadPaginado();
                console.log(data.data)
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () {
                
                blockUI.stop();
            });
    }

    $scope.asignarTarea = function (rowData) {
       var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
        };

        return $http(req)
            .success(function(data, status) {
                debugger
               // var url="/bonita/apps/administrativo/verSolicitudAdmision/?id=[TASKID]&displayConfirmation=false";
                var url="/bonita/portal/resource/app/administrativo/verSolicitudAdmision/content/?id=[TASKID]&displayConfirmation=false";
                url = url.replace("[TASKID]", data[0].id);
                //window.top.location.href=url;
                window.open(url,'_blank');
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {});

    }

    function redireccionarTarea(rowData) {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + rowData.taskId,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };

        return $http(req)
            .success(function (data, status) {
                var url = "/bonita/portal/resource/taskInstance/[NOMBREPROCESO]/[VERSIONPROCESO]/[NOMBRETAREA]/content/?id=[TASKID]&displayConfirmation=false";
                url = url.replace("[NOMBREPROCESO]", rowData.processName);
                url = url.replace("[VERSIONPROCESO]", rowData.processVersion);
                url = url.replace("[NOMBRETAREA]", rowData.taskName);
                url = url.replace("[TASKID]", rowData.taskId);
                $window.location.assign(url);
            })
            .error(function (data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function () { });
    }

    $(function () {
        doRequest("POST", $scope.properties.urlPost);
    })


    $scope.$watch("properties.dataToSend", function (newValue, oldValue) {
        if (newValue !== undefined) {
            doRequest("POST", $scope.properties.urlPost);
        }
        console.log($scope.properties.dataToSend);
    });
        $scope.setOrderBy= function(order){
        if($scope.properties.dataToSend.orderby == order){
            $scope.properties.dataToSend.orientation = ($scope.properties.dataToSend.orientation=="ASC")?"DESC":"ASC";
        }else{
            $scope.properties.dataToSend.orderby = order;
            $scope.properties.dataToSend.orientation = "ASC";
        }
        doRequest("POST", $scope.properties.urlPost);
    }
    $scope.filterKeyPress= function(columna,press){
        var aplicado = true;
        for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
            const element = $scope.properties.dataToSend.lstFiltro[index];
            if(element.columna==columna){
                $scope.properties.dataToSend.lstFiltro[index].valor=press;
                $scope.properties.dataToSend.lstFiltro[index].operador="Que contengan";
                aplicado=false;
            }
            
        }
        if(aplicado){
            var obj = 	{ "columna":columna, "operador":"Que contengan", "valor":press }
            $scope.properties.dataToSend.lstFiltro.push(obj);
        }
        
        doRequest("POST", $scope.properties.urlPost);
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

        doRequest("POST", $scope.properties.urlPost);
    }
    $scope.preAsignarTarea=function(rowData) {
        
        var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
        };

        return $http(req)
            .success(function(data, status) {
                rowData.taskId=data[0].id;
                rowData.taskName=data[0].name;
                rowData.processId=data[0].processId;
                //rowData.taskName=
                $scope.preProcesoAsignarTarea(rowData);
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {});
    }
    $scope.preProcesoAsignarTarea=function(rowData) {
        
        var req = {
            method: "GET",
            url: `/API/bpm/process/${rowData.processId}?d=deployedBy&n=openCases&n=failedCases`
        };

        return $http(req)
            .success(function(data, status) {
                rowData.processName=data.name;
                rowData.processVersion=data.version;
                $scope.reciclar(rowData);
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {});
    }
    $scope.reciclar = function (rowData) {
        swal({
            title: `¿Está seguro que desea reactivar la solicitud del aspirante ${rowData.primernombre + " " + rowData.segundonombre + " " + rowData.apellidopaterno + " " + rowData.apellidomaterno}?`,
            text: "La solicitud tendrá que ser validada",
            icon: "warning",
            buttons: true,
            dangerMode: true,
            buttons: ["Cancelar", "Continuar"]
        })
            .then((willDelete) => {
                if (willDelete) {
                    doRequestCallback("PUT", `/bonita/API/bpm/humanTask/${rowData.taskId}`,{},{ "assigned_id": $scope.properties.userId },function(success,rowData){
                        doRequestCallback("POST", `/bonita/API/bpm/userTask/${rowData.taskId}/execution?assign=false`,{},{},function(success,rowData){
                            window.top.location.href=`/bonita/apps/administrativo/AppListaRoja/`
                            
                        },rowData); 
                    },rowData);
                    
                } else {

                }
            });
        
    }
    /**
   * Execute a get/post request to an URL
   * It also bind custom data from success|error to a data
   * @return {void}
   */
    function doRequestCallback(method, url, params, payload, callback,extra) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: angular.copy(payload),
            params: params
        };

        return $http(req)
            .success(function (data, status) {
                callback(data,extra);
            })
            .error(function (data, status) {
                console.error(data);
            })
            .finally(function () {
                vm.busy = false;
            });
    }
        $scope.lstCampus = [];
        $scope.getCampusByGrupo = function (campus) {
        var retorno = "";
        for (var i = 0; i < $scope.properties.lstCampus.length; i++) {
            if (campus == $scope.properties.lstCampus[i].grupoBonita) {
                retorno = $scope.properties.lstCampus[i].descripcion
                if($scope.lstMembership.length == 1){
                    $scope.properties.campusSeleccionado = $scope.lstCampus[i].valor    
                }
            }
            
        }
        return retorno;
    }
    $scope.lstMembership = [];
    $scope.$watch("properties.userId", function (newValue, oldValue) {
        if (newValue !== undefined) {
            var req = {
                method: "GET",
                url: `/API/identity/membership?p=0&c=100&f=user_id%3d${$scope.properties.userId}&d=role_id&d=group_id`
            };

            return $http(req)
                .success(function (data, status) {
                    $scope.lstMembership = data;
                    $scope.campusByUser();
                })
                .error(function (data, status) {
                    console.error(data);
                })
                .finally(function () { });
        }
    });
    
    $scope.lstCampusByUser = [];
	$scope.campusByUser = function(){
		var resultado=[];
		
		for(var x in $scope.lstMembership){
			if($scope.lstMembership[x].group_id.name.indexOf("CAMPUS") != -1){
				resultado.push($scope.lstMembership[x].group_id.name);
			}
		}
		$scope.lstCampusByUser = resultado;
	}
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
                    for(let index2 = 0; index2 < $scope.lstCampus.length; index2++){
                        if($scope.lstCampus[index2].descripcion === $scope.filtroCampus){ 
                        $scope.properties.campusSeleccionado = $scope.lstCampus[index2].valor;    
                        }
                    }
                    encontrado = true
                }

            }
            if (!encontrado) {
                $scope.properties.dataToSend.lstFiltro.push(filter);
                for(let index2 = 0; index2 < $scope.lstCampus.length; index2++){
                    if($scope.lstCampus[index2].descripcion === $scope.filtroCampus){ 
                    $scope.properties.campusSeleccionado = $scope.lstCampus[index2].valor;    
                    }
                }
            }
        } else {
            $scope.properties.dataToSend.lstFiltro.push(filter);
            for(let index2 = 0; index2 < $scope.lstCampus.length; index2++){
                if($scope.lstCampus[index2].descripcion === $scope.filtroCampus){ 
                $scope.properties.campusSeleccionado = $scope.lstCampus[index2].valor;    
                }
            }
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
        
        doRequest("POST", $scope.properties.urlPost);
    }
    $scope.getCatCampus = function() {
        var req = {
            method: "GET",
            url: "../API/bdm/businessData/com.anahuac.catalogos.CatCampus?q=find&p=0&c=100"
        };

        return $http(req)
            .success(function(data, status) {
                $scope.lstCampus = [];
                for(var index in data){
                    $scope.lstCampus.push({
                        "descripcion": data[index].descripcion,
                        "valor": data[index].grupoBonita
                    })                    
                }
            })
            .error(function(data, status) {
                console.error(data);
            });
    }

    $scope.getCatCampus();
}