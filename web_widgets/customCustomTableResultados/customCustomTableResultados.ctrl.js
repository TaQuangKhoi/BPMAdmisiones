function PbTableCtrl($scope, $http, $window,blockUI) {

    this.isArray = Array.isArray;
    
    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
        }
    };

    $scope.seleccionarTodos = false;
    
    $scope.seleccionarTodasCartas = function(){
        $scope.seleccionarCarta(true, $scope.seleccionarTodos, "");
    }
    
    $scope.seleccionarCarta = function(_todos, _seleccionado, _persistenceid){
        blockUI.start();
        let dataToSend = {
            "todos": _todos,
            "seleccionado": _seleccionado,
            "persistenceid": _persistenceid
        }

        $http.post("/bonita/API/extension/AnahuacRest?url=seleccionarCarta&p=0&c=100", dataToSend).success(function(success){
            doRequest("POST", $scope.properties.urlPost);
        }).error(function(error){
            swal("Error", error.error, "error")
        }).finally(function(){
            blockUI.stop();
        })
    }

    this.isSelected = function(row) {
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

        return $http(req).success(function(data, status) {
            $scope.properties.lstContenido = data.data;
            $scope.value = data.totalRegistros;
            $scope.loadPaginado();
            console.log(data.data)
        })
        .error(function(data, status) {
            notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {
            blockUI.stop();
        });
    }

    $scope.isenvelope = false;
    $scope.selectedrow = {};
    $scope.mensaje = "";

    $scope.lstCampus = [];

    $(function() {
        doRequest("POST", $scope.properties.urlPost);
    })
    
    
 
    $scope.$watch("properties.dataToSend", function(newValue, oldValue) {
        if (newValue !== undefined) {
            doRequest("POST", $scope.properties.urlPost);
        }
       
    });

    $scope.$watch("properties.campusSeleccionado", function (newValue, oldValue) {
        
        if (newValue !== undefined) {
            debugger
            $scope.properties.dataToSend.campus=$scope.properties.campusSeleccionado.descripcion;
            doRequest("POST", $scope.properties.urlPost);
        }
       

    });

    $scope.setOrderBy = function(order){
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
    
    $scope.asignarTarea = function (rowData) {

        var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
        };

        return $http(req)
            .success(function (data, status) {
                //var url = "/bonita/apps/administrativo/verSolicitudAdmision/?id=[TASKID]&displayConfirmation=false";
                var url = "/bonita/portal/resource/app/administrativo/verSolicitudAdmision/content/?id=[TASKID]&displayConfirmation=false";
                url = url.replace("[TASKID]", data[0].id);
                //window.top.location.href = url;
                window.open(url,'_blank');
            })
            .error(function (data, status) {
                console.error(data);
            })
            .finally(function () { });
    }
    $scope.isenvelope = false;
    $scope.selectedrow = {};
    $scope.mensaje = "";
    $scope.envelope = function (row) {
        $scope.isenvelope = true;
        $scope.mensaje = "";
        $("#"+$scope.properties.idtext).text("")
        $scope.selectedrow = row;
    }
    $scope.envelopeCancel = function () {
        $scope.isenvelope = false;
        $scope.selectedrow = {};
    }
    $scope.sendMail = function (row, mensaje) {
        if (row.grupobonita == undefined) {
            for (var i = 0; i < $scope.lstCampus.length; i++) {
                if ($scope.lstCampus[i].descripcion == row.campus) {
                    row.grupobonita = $scope.lstCampus[i].valor;
                }
            }
        }
        var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=generateHtml&p=0&c=10",
            data: angular.copy({
                "campus": row.grupobonita,
                "correo": row.email,
                "codigo": "recordatorio",
                "isEnviar": true,
                "mensaje": mensaje
            })
        };

        return $http(req)
            .success(function (data, status) {

                $scope.envelopeCancel();
            })
            .error(function (data, status) {
                console.error(data)
            })
            .finally(function () { });
    }
    
    $scope.functionFechaExmenes = function (row, op) {
    let fechas;
    let NoCollegeBoard;
    let NoPsicometrico;
    let NoEntrevista;
    
    
    if(row.fechasexamenes==null || row.fechasexamenes== "" || row.fechasexamenes==" "){
         fechas="Sin programar                                                 "
         
    }else{
        
        var arrayDeCadenas = row.fechasexamenes.split(",");
        for (var i = 0; i <arrayDeCadenas.length; i++) {
            if (arrayDeCadenas[i].includes("Examen de aptitudes y conocimientos")) {
                NoCollegeBoard=i;
            }else if (arrayDeCadenas[i].includes("Entrevista")) {
                NoEntrevista=i;
            }else if (arrayDeCadenas[i].includes("Examen Psicométrico")) {
                NoPsicometrico=i;
            }
        }
        if(op==1){
             fechas=" "+arrayDeCadenas[NoCollegeBoard]
         }
        if(op==2){

            fechas=" "+arrayDeCadenas[NoEntrevista]
        }
         if(op==3){
           fechas=" "+arrayDeCadenas[NoPsicometrico]
         }

    }
     return fechas;
    
}

}