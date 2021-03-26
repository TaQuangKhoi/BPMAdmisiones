function PbTableCtrl($scope, $http, blockUI, $sce, $element, widgetNameFactory, $timeout, $log, gettextCatalog) {
    var vm = this;
    $scope.isInsert = false;
    $scope.content = [];
    $scope.firma = { "persistenceId": null, "cargo": "", "correo": "", "grupo": "", "nombreCompleto": "", "persistenceVersion": null, "showCargo": false, "showCorreo": false, "showGrupo": false, "showTelefono": false, "showTitulo": false, "telefono": "", "titulo": "", "facebook": "", "twitter": "", "apellido": "", "banner": "" }
    this.isArray = Array.isArray;
    $scope.agregarEditar = false;
    $scope.group_id = "";
    $scope.file = null;
    this.isClickable = function() {
        return $scope.properties.isBound('selectedRow');
    };

    this.selectRow = function(row) {
        if (this.isClickable()) {
            $scope.properties.selectedRow = row;
        }
    };

    this.isSelected = function(row) {
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
            .success(function(data, status) {
                callback(data)
            })
            .error(function(data, status) {
                console.error(data)
            })
            .finally(function() {
                vm.busy = false;
                blockUI.stop();
            });
    }

    function initialize() {

        $scope.firma = { "persistenceId": null, "cargo": "", "correo": "", "grupo": "", "nombreCompleto": "", "persistenceVersion": null, "showCargo": false, "showCorreo": false, "showGrupo": false, "showTelefono": false, "showTitulo": false, "telefono": "", "titulo": "", "facebook": "", "twitter": "", "apellido": "", "banner": "" }
    }
    $scope.openModal = function(kind, data) {
        $scope.isInsert = kind;
        ctrl.filename = '';
        if ($scope.isInsert) {
            initialize();
        } else {
            for (let index = 0; index < $scope.properties.lstCampus.length; index++) {
                const element = $scope.properties.lstCampus[index];
                if (element.valor == data.campus) {
                    $scope.properties.campusSelected = element;
                }
            }

            $scope.firma = angular.copy(data)
        }
        $scope.agregarEditar = true;
        //$(`#insFirma`).modal('show')
    }
    $scope.previewModal = function(data) {

        $scope.firma = angular.copy(data)
        var value = setFirma();
        /*var element = document.getElementById("firma");
        element.innerHTML = value
        $(`#previewFirma`).modal('show')*/
    }
    $scope.insertData = function() {

        var error = false;
        var texto = "";
        var info = "";
        if ($scope.firma.nombreCompleto == "") {
            error = true;
            texto = "Faltó capturar: Nombre";
            info = "¡Aviso!"
        } else if (!$scope.allLetter($scope.firma.nombreCompleto)) {
            error = true;
            texto = "Solo caracteres válidos en: Nombre";
            info = "¡Aviso!"
        } else if (!$scope.capitalLetter($scope.firma.nombreCompleto)) {
            error = true;
            texto = "Primera letra debe ser mayúscula en: Nombre";
            info = "¡Aviso!"
        } else if ($scope.firma.apellido == "") {
            error = true;
            texto = "Faltó capturar: Apellido";
            info = "¡Aviso!"
        } else if (!$scope.allLetter($scope.firma.apellido)) {
            error = true;
            texto = "Solo caracteres válidos en: Apellido";
            info = "¡Aviso!"
        } else if (!$scope.capitalLetter($scope.firma.apellido)) {
            error = true;
            texto = "Primera letra para debe ser mayúscula en: Apellido";
            info = "¡Aviso!"
        } else if ($scope.firma.cargo == "") {
            error = true;
            texto = "Faltó capturar: Cargo";
            info = "¡Aviso!"
        } else if (!$scope.allLetter($scope.firma.cargo)) {
            error = true;
            texto = "Solo caracteres válidos en: Cargo";
            info = "¡Aviso!"
        } else if ($scope.firma.telefono == "") {
            error = true;
            texto = "Faltó capturar: Teléfono";
            info = "¡Aviso!"
        } else if ($scope.firma.telefono.length !== 10) {
            error = true;
            texto = "Favor de ingresar un teléfono válido";
            info = "¡Aviso!"
        } else if (!$scope.validUrl($scope.firma.grupo) && $scope.firma.grupo !== "") {
            error = true;
            texto = "Favor de ingresar url válida en: Url campus";
            info = "¡Aviso!"
        } else if ($scope.firma.correo == "") {
            error = true;
            texto = "Faltó capturar: Correo";
            info = "¡Aviso!"
        } else if (!$scope.validateEmail($scope.firma.correo)) {
            error = true;
            texto = "Favor de capturar correo válido en: Correo";
            info = "¡Aviso!"
        }

        if (error) {
            swal(info, texto, "warning");
        } else {
            $scope.firma.campus = $scope.group_id;
            if($scope.file!=null){
                Main()
            }else{
                doRequest("POST", `/bonita/API/extension/AnahuacRest?url=${($scope.isInsert) ? 'insertFirma' : 'updateFirma'}&p=0&c=10`, {}, $scope.firma, function(dataCallBack) {
                    $(`#insFirma`).modal(`hide`)
                    swal("Correcto", "Firma guardada", "success");
                    $scope.agregarEditar = false;
                    $scope.loadCatalog();
                })
            }
            
        }

    }
    $scope.deleteFirma = function(firma) {
        swal({
                title: `¿Está seguro que desea eliminar la firma de ${firma.nombreCompleto + " " + firma.apellido }?`,
                text: "",
                icon: "warning",
                buttons: ["No", "Sí"],
                dangerMode: true,
            })
            .then((willDelete) => {
                if (willDelete) {
                    doRequest("POST", `/bonita/API/extension/AnahuacRest?url=deleteFirma&p=0&c=10`, {}, firma, function(dataCallBack) {
                        $(`#insFirma`).modal(`hide`)
                        swal("Correcto", "Firma eliminada", "success");
                        $scope.agregarEditar = false;
                        $scope.file = null;
                        $scope.loadCatalog();
                    })
                } else {}
            });


        /*swal({
             title: `¿Está seguro que desea eliminar la firma de ${firma.nombreCompleto + " " + firma.apellido }?`,
             icon: "warning",
             showCancelButton: true,
             confirmButtonColor: '#FF5900',
             cancelButtonColor: '#231F20',
             confirmButtonText: 'Continuar',
             cancelButtonText: 'Cancelar'
             }).then((result) => {
             if (result.isConfirmed) {
                 
             
             } else {
             
             }
             })*/

    }
    $scope.quitarBanner = function() {
        swal({
                title: `¿Está seguro que desea quitar el Banner de firma?`,
                text: "",
                icon: "warning",
                buttons: ["No", "Sí"],
                dangerMode: true,
            })
            .then((willDelete) => {
                if (willDelete) {
                    $scope.firma.banner="";
                    $scope.file = null;
                    $scope.$apply();
                }
            });
    }
    $scope.setFirma = "";

    function setFirma() {
        doRequest("GET", "/bonita/API/extension/AnahuacRestGet?url=generarFirma&p=0&c=10&persistenceId=" + $scope.firma.persistenceId, null, null, function(dataCallBack) {
            $scope.setFirma = dataCallBack;
            var element = document.getElementById("firma");
            element.innerHTML = $scope.setFirma;
            $(`#previewFirma`).modal('show')
        });

        /*var nombretitulo = "<p><b>" + $scope.firma.titulo + " " + $scope.firma.nombreCompleto + "</b> <br>";
        var cargo = ($scope.firma.cargo == null || $scope.firma.cargo.equals(""))?"":$scope.firma.cargo + "</p>";
        var telefono = ($scope.firma.telefono == null || $scope.firma.telefono.equals(""))?"":"<p><img style='width: 10px;'src='https://i.ibb.co/vsCB2MF/icono-UA-04.png'>&nbsp;" + $scope.firma.telefono + "<br> ";
        var pagina = ($scope.firma.grupo == null || $scope.firma.grupo.equals(""))?"":"<img style='width: 10px;'src='https://i.ibb.co/172hPYQ/icono-UA-Mesa-de-trabajo-1.png'>&nbsp;" + $scope.firma.grupo + "<br>";
        var facebook = ($scope.firma.facebook == null || $scope.firma.facebook.equals(""))?"":"<img style='width: 10px;'src='https://i.ibb.co/B49GzyB/icono-UA-03.png'>&nbsp;"+$scope.firma.facebook+"<br>";
		var twitter = ($scope.firma.twitter == null || $scope.firma.twitter.equals(""))?"":"<img style='width: 10px;'src='https://i.ibb.co/7Wd817H/icono-UA-02.png'>&nbsp;"+$scope.firma.twitter+"<br>";
        var correo = ($scope.firma.correo == null || $scope.firma.correo.equals(""))?"":"<img style='width: 10px;'src='https://i.ibb.co/Yc0TXX9/Iconos-UA-05.png'>&nbsp;" + $scope.firma.correo + "<br> </p>"
        return nombretitulo + cargo + telefono + pagina + facebook + twitter + correo;*/

    }
    $scope.$watch("properties.dataToSend", function(newValue, oldValue) {
        console.log("WATCHER")
        if (newValue !== undefined) {
            //$scope.loadCatalog();
        }

    });
    $scope.setOrderBy = function(order) {
        if ($scope.properties.dataToSend.orderby == order) {
            $scope.properties.dataToSend.orientation = ($scope.properties.dataToSend.orientation == "ASC") ? "DESC" : "ASC";
        } else {
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

    $scope.loadPaginado = function() {
        $scope.valorTotal = Math.ceil($scope.value / $scope.properties.dataToSend.limit);
        $scope.lstPaginado = []
        if ($scope.valorSeleccionado <= 5) {
            $scope.iniciarP = 1;
            $scope.finalP = $scope.valorTotal > 10 ? 10 : $scope.valorTotal;
        } else {
            $scope.iniciarP = $scope.valorSeleccionado - 5;
            $scope.finalP = $scope.valorTotal > ($scope.valorSeleccionado + 4) ? ($scope.valorSeleccionado + 4) : $scope.valorTotal;
        }
        for (var i = $scope.iniciarP; i <= $scope.finalP; i++) {

            var obj = {
                "numero": i,
                "inicio": ((i * 10) - 9),
                "fin": (i * 10),
                "seleccionado": (i == $scope.valorSeleccionado)
            };
            $scope.lstPaginado.push(obj);
        }
    }

    $scope.siguiente = function() {
        var objSelected = {};
        for (var i in $scope.lstPaginado) {
            if ($scope.lstPaginado[i].seleccionado) {
                objSelected = $scope.lstPaginado[i];
                $scope.valorSeleccionado = $scope.lstPaginado[i].numero;
            }
        }
        $scope.valorSeleccionado = $scope.valorSeleccionado + 1;
        if ($scope.valorSeleccionado > Math.ceil($scope.value / $scope.properties.dataToSend.limit)) {
            $scope.valorSeleccionado = Math.ceil($scope.value / $scope.properties.dataToSend.limit);
        }
        $scope.seleccionarPagina($scope.valorSeleccionado);
    }

    $scope.anterior = function() {
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

    $scope.seleccionarPagina = function(valorSeleccionado) {
        var objSelected = {};
        for (var i in $scope.lstPaginado) {
            if ($scope.lstPaginado[i].numero == valorSeleccionado) {
                $scope.inicio = ($scope.lstPaginado[i].numero - 1);
                $scope.fin = $scope.lstPaginado[i].fin;
                $scope.valorSeleccionado = $scope.lstPaginado[i].numero;
                $scope.properties.dataToSend.offset = (($scope.lstPaginado[i].numero - 1) * $scope.properties.dataToSend.limit)
            }
        }
        $scope.loadCatalog();
    }
    $scope.getCampusByGrupo = function(campus) {
        var retorno = "";
        for (var i = 0; i < $scope.lstCampus.length; i++) {
            if (campus == $scope.lstCampus[i].valor) {
                retorno = $scope.lstCampus[i].descripcion
            }

        }
        return retorno;
    }
    $scope.lstMembership = [];
    $scope.lstCampus = [];
    $scope.lstCampusRole = [];
    $scope.campusDescripcion = function(campus) {
        var retorno = ""
        for (let index = 0; index < $scope.lstCampusRole.length; index++) {
            const element = $scope.lstCampusRole[index];
            if (element.valor == campus) {
                retorno = element.descripcion
            }
        }
        return retorno;
    }
    $scope.$watch("properties.userId", function(newValue, oldValue) {
        if (newValue !== undefined) {
            var req = {
                method: "GET",
                url: `/API/identity/membership?p=0&c=10&f=user_id%3d${$scope.properties.userId}&d=role_id&d=group_id`
            };

            return $http(req)
                .success(function(data, status) {
                    $scope.lstMembership = data;
                    $scope.lstCampusRole = [{
                            "descripcion": "Anáhuac Cancún",
                            "valor": "CAMPUS-CANCUN"
                        },
                        {
                            "descripcion": "Anáhuac Mérida",
                            "valor": "CAMPUS-MAYAB"
                        },
                        {
                            "descripcion": "Anáhuac México Norte",
                            "valor": "CAMPUS-MNORTE"
                        },
                        {
                            "descripcion": "Anáhuac México Sur",
                            "valor": "CAMPUS-MSUR"
                        },
                        {
                            "descripcion": "Anáhuac Oaxaca",
                            "valor": "CAMPUS-OAXACA"
                        },
                        {
                            "descripcion": "Anáhuac Puebla",
                            "valor": "CAMPUS-PUEBLA"
                        },
                        {
                            "descripcion": "Anáhuac Querétaro",
                            "valor": "CAMPUS-QUERETARO"
                        },
                        {
                            "descripcion": "Anáhuac Xalapa",
                            "valor": "CAMPUS-XALAPA"
                        },
                        {
                            "descripcion": "Juan Pablo II",
                            "valor": "CAMPUS-JP2"
                        },
                        {
                            "descripcion": "Anáhuac Cordoba",
                            "valor": "CAMPUS-CORDOBA"
                        }
                    ];
                    var lstCampusByUser = [];

                    for (var x in $scope.lstMembership) {
                        if ($scope.lstMembership[x].group_id.name.indexOf("CAMPUS") != -1) {
                            lstCampusByUser.push($scope.lstMembership[x].group_id.name);
                        }
                    }

                    $scope.lstCampus = [];

                    for (var x in lstCampusByUser) {
                        for (var index in $scope.lstCampusRole) {
                            if (lstCampusByUser[x] === $scope.lstCampusRole[index].valor) {
                                $scope.lstCampus.push($scope.lstCampusRole[index]);
                            }
                        }
                    }


                })
                .error(function(data, status) {
                    console.error(data);
                })
                .finally(function() {});
        }
    });
    $scope.filtroCampus = ""
    $scope.addFilter = function() {
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
    $scope.sizing = function() {
        $scope.lstPaginado = [];
        $scope.valorSeleccionado = 1;
        $scope.iniciarP = 1;
        $scope.finalP = 10;
        try {
            $scope.properties.dataToSend.limit = parseInt($scope.properties.dataToSend.limit);
        } catch (exception) {

        }

        $scope.loadCatalog();
    }
    $scope.loadCatalog = function() {
        var dataToSend = angular.copy($scope.properties.dataToSend)
        if (dataToSend.lstFiltro.length > 0 && angular.isDefined($scope.filter)) {
            var encontrado = false;
            for (let index = 0; index < dataToSend.lstFiltro.length; index++) {
                const element = dataToSend.lstFiltro[index];
                if (element.columna == "CAMPUS") {
                    dataToSend.lstFiltro[index].columna = $scope.filter.columna;
                    dataToSend.lstFiltro[index].operador = $scope.filter.operador;
                    dataToSend.lstFiltro[index].valor = $scope.group_id;
                    encontrado = true
                }
                if (!encontrado) {
                    dataToSend.lstFiltro.push($scope.filter);
                }

            }
        } else if (angular.isDefined($scope.filter.valor)) {
            dataToSend.lstFiltro.push($scope.filter);
        }
        doRequest("GET", "/bonita/API/extension/AnahuacRestGet?url=getCatNotificacionesFirma&p=0&c=10&jsonData=" + encodeURIComponent(JSON.stringify(dataToSend)), {}, null, function(dataCallBack) {
            $scope.content = dataCallBack.data;
            $scope.value = dataCallBack.totalRegistros;
            $scope.loadPaginado();
        })
    }
    $scope.$watch('properties.campusSelected', function(value) {
        if (angular.isDefined(value) && value !== null) {
            ///bonita/API/identity/group/18?n=number_of_users
            $scope.pagina = 0;
            $scope.group_id = value.valor;
            $scope.filter = {
                "columna": "CAMPUS",
                "operador": "Igual a",
                "valor": $scope.group_id
            }

            $scope.loadCatalog();

        }

    });
    $scope.validateEmail = function(email) {
        var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
    }
    $scope.allLetter = function(inputtxt) {
        //var letters = /^[a-zA-Z\s]*$/;
        var letters = /^[a-zA-ZÀ-ÿ\u00f1\u00d1]+(\s*[a-zA-ZÀ-ÿ\u00f1\u00d1]*)*[a-zA-ZÀ-ÿ\u00f1\u00d1]+$/;
        return letters.test(inputtxt);
    }
    $scope.capitalLetter = function(txt) {
        var re = /^[A-ZÀ-ÿ\u00f1\u00d1]+(\s*[a-zA-ZÀ-ÿ\u00f1\u00d1]*)*[a-zA-ZÀ-ÿ\u00f1\u00d1]\w*$/g;
        return re.test(txt);
    }
    $scope.validUrl = function(txt) {
        var re = /^(^http[s]?:\/{2})|(^www)|(^\/{1,2})/igm;
        return re.test(txt);
    }
    $scope.requisitosMinimos = function() {
        return $scope.validateEmail($scope.firma.correo) && $scope.allLetter($scope.firma.cargo) && $scope.validUrl($scope.firma.grupo) && $scope.allLetter($scope.firma.nombreCompleto) && $scope.capitalLetter($scope.firma.nombreCompleto) && $scope.allLetter($scope.firma.apellido) && $scope.capitalLetter($scope.firma.apellido);
    }

    var ctrl = this;
    this.name = widgetNameFactory.getName('pbInput');
    this.filename = '';
    this.filemodel = '';

    this.clear = clear;
    this.startUploading = startUploading;
    this.uploadError = uploadError;
    this.uploadComplete = uploadComplete;

    this.name = widgetNameFactory.getName('pbUpload');
    this.preventFocus = function($event) {
        $event.target.blur();
    };

    function clear() {
        ctrl.filename = '';
        ctrl.filemodel = '';
        $scope.properties.value = {};
    }
    this.submitForm = function() {
        /*var form = $element.find('form');
        form.triggerHandler('submit');
        form[0].submit();*/
    };

    this.forceSubmit = function(event) {
        $scope.procesar = false;
        //$scope.properties.urlretorno = window.btoa(event.target.files[0]);
        if (event.target.files[0].type === "image/jpeg") {

            $scope.procesar = true;
        } else if (event.target.files[0].type === "image/png") {

            $scope.procesar = true;
        } else {
            swal("¡Formato no válido!", "Solo puede agregar archivos PDF o imágenes JPG y PNG.", "warning");
            $scope.properties.urlretorno = "";
        }

        if ($scope.procesar === true) {

            $scope.firma.banner=URL.createObjectURL(event.target.files[0]);
            $scope.$apply();
            if (!event.target.value) {
                return;
            }
            $scope.upload();
            event.target.value = null;
        }

    };
    var input = $element.find('input');
    input.on('change', ctrl.forceSubmit);
    $scope.$on('$destroy', function() {
        input.off('change', ctrl.forceSubmit);
    });
    $scope.upload = function() {
        $scope.file = $('input[name="' + ctrl.name + '"]').get(0).files[0];
        ctrl.filename = $scope.file.name;
        $scope.$apply();
        /*Main()
        var jsonData = { "b64": toBase64(file) }
        var formData = new FormData();
        formData.append('file', file);*/



    }

    function uploadError(error) {
        $log.warn('upload fails too', error);
        ctrl.filemodel = '';
        ctrl.filename = gettextCatalog.getString('Upload failed');
    }

    function startUploading() {
        ctrl.filemodel = '';
        ctrl.filename = gettextCatalog.getString('Uploading...');
    }

    function uploadComplete(response) {
        //when the upload widget return a String, it means an error has occurred (with a html document as a response)
        //if it's not a string, we test if it contains some error message
        if (angular.isString(response) || (response && response.type && response.message)) {
            $log.warn('upload failed');
            ctrl.filemodel = '';
            ctrl.filename = gettextCatalog.getString('Upload failed');
            $scope.properties.errorContent = angular.isString(response) ? response : response.message;
            return;
        }
        $scope.properties.value = response;

    }
    const toBase64 = file => new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
    });

    async function Main() {
        //const file = $('input[name="' + ctrl.name + '"]').get(0).files[0];

        var b64 = await toBase64($scope.file);
        var jsonData = {
            "b64": b64,
            "filename": $scope.file.name,
            "filetype": $scope.file.type,
            "contenedor": "publico"
        }
        return $http.post('/bonita/API/extension/AnahuacAzureRest?url=uploadFile&p=0&c=10', jsonData, {
            headers: { 'Content-Type': "application/json" }
        }).then(function(results) {
            $scope.firma.banner = results.data.data[0];
            doRequest("POST", `/bonita/API/extension/AnahuacRest?url=${($scope.isInsert) ? 'insertFirma' : 'updateFirma'}&p=0&c=10`, {}, $scope.firma, function(dataCallBack) {
                $(`#insFirma`).modal(`hide`)
                swal("Correcto", "Firma guardada", "success");
                $scope.agregarEditar = false;
                $scope.file = null;
                $scope.loadCatalog();
            })
        });
    }

    $scope.forceKeyPressUppercase = function(e){
        var charInput = e.keyCode;
        var limite = 10;
        if((charInput >=48) && (charInput <=57)&&(e.target.value.length) <limite){
        }else{
            var start = e.target.selectionStart;
            var end = e.target.selectionEnd;
            e.target.value = e.target.value.substring(0, start) + e.target.value.substring(end);
            e.target.setSelectionRange(start+1, start+1);
             e.preventDefault();
        }
      }


}