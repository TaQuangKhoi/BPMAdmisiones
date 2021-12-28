function PbTableCtrl($scope, $http, $window, blockUI, modalService, $filter) {
    this.isArray = Array.isArray;

    $scope.date = new Date();
    $scope.fechaActual = ($filter('date')(Date.parse($scope.date), "dd/MMM/yyyy")).toString();
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
    ///API/bpm/process/4774666324165829920?d=deployedBy&n=openCases&n=failedCases
    $scope.preAsignarTarea = function(rowData) {

        var req = {
            method: "GET",
            url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
        };

        return $http(req)
            .success(function(data, status) {
                rowData.taskId = data[0].id;
                rowData.taskName = data[0].name;
                rowData.processId = data[0].processId;
                //rowData.taskName=
                $scope.preProcesoAsignarTarea(rowData);
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {});
    }
    $scope.preProcesoAsignarTarea = function(rowData) {

        var req = {
            method: "GET",
            url: `/API/bpm/process/${rowData.processId}?d=deployedBy&n=openCases&n=failedCases`
        };

        return $http(req)
            .success(function(data, status) {
                rowData.processName = data.name;
                rowData.processVersion = data.version;
                $scope.asignarTarea(rowData);
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {});
    }

    /*$scope.asignarTarea=function(rowData) {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/"+rowData.taskId,
            data: angular.copy({"assigned_id":""})
        };

        return $http(req)
            .success(function(data, status) {
                redireccionarTarea(rowData);
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }*/

    $scope.asignarTarea = function(rowData) {
        var page = "verSolicitudAdmisionADV2";
        doRequest2("GET", "/API/bpm/archivedHumanTask?p=0&c=10&f=caseId=" + rowData.caseid + "&f=state=aborted&d=processId", null, null, function(dataAborted) {
            if (dataAborted.length > 0) {
                doRequest2("POST", "/bonita/API/extension/AnahuacRest?url=recoveryData&p=0&c=100", null, { "caseId": parseInt(rowData.caseid), "processDefinitionId": dataAborted[0].processId.id }, function(recoveryData) {
                    var req = {
                        method: "GET",
                        url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
                    };

                    return $http(req)
                        .success(function(data, status) {

                            blockUI.start();
                            var req2 = {
                                method: "GET",
                                url: `/API/bpm/${(data.length>0)?"humanTask":"archivedHumanTask"}?p=0&c=10&f=caseId=${rowData.caseid}&f=state=${(data.length>0)?"ready":"completed"}&d=processId`
                            };

                            $http(req2)
                                .success(function(data2, status) {

                                    ///API/bpm/humanTask?p=0&c=10&f=caseId=30197&f=state=ready&d=processId

                                    var url = "/bonita/portal/resource/app/administrativo/[PAGE]/content/?id=[TASKID]&caseId=[CASEID]&displayConfirmation=false";
                                    if (data2.length > 0) {
                                        if (parseFloat(data2[0].processId.version) < 1.51) {
                                            page = "verSolicitudAdmision";
                                        }
                                        url = url.replace("[PAGE]", page);
                                        url = url.replace("[TASKID]", data2[0].id);
                                    } else {
                                        url = url.replace("[TASKID]", "");
                                    }
                                    url = url.replace("[CASEID]", rowData.caseid);
                                    //window.top.location.href = url;
                                    window.open(url, '_blank');
                                })
                                .error(function(data, status) {
                                    notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
                                })
                                .finally(function() {
                                    blockUI.stop();
                                });
                        })
                        .error(function(data, status) {
                            console.error(data);
                        })
                        .finally(function() {});

                })
            } else {
                var req = {
                    method: "GET",
                    url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
                };

                return $http(req)
                    .success(function(data, status) {

                        blockUI.start();
                        var req2 = {
                            method: "GET",
                            url: `/API/bpm/${(data.length>0)?"humanTask":"archivedHumanTask"}?p=0&c=10&f=caseId=${rowData.caseid}&f=state=${(data.length>0)?"ready":"completed"}&d=processId`
                        };

                        $http(req2)
                            .success(function(data2, status) {

                                ///API/bpm/humanTask?p=0&c=10&f=caseId=30197&f=state=ready&d=processId

                                var url = "/bonita/portal/resource/app/administrativo/[PAGE]/content/?id=[TASKID]&caseId=[CASEID]&displayConfirmation=false";
                                if (data2.length > 0) {
                                    if (parseFloat(data2[0].processId.version) < 1.51) {
                                        page = "verSolicitudAdmision";
                                    }
                                    url = url.replace("[PAGE]", page);
                                    url = url.replace("[TASKID]", data2[0].id);
                                } else {
                                    url = url.replace("[TASKID]", "");
                                }
                                url = url.replace("[CASEID]", rowData.caseid);
                                //window.top.location.href = url;
                                window.open(url, '_blank');
                            })
                            .error(function(data, status) {
                                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
                            })
                            .finally(function() {

                                blockUI.stop();
                            });
                    })
                    .error(function(data, status) {
                        console.error(data);
                    })
                    .finally(function() {});
            }

        })
    }

    function redireccionarTarea(rowData) {
        var req = {
            method: "PUT",
            url: "/bonita/API/bpm/humanTask/" + rowData.taskId,
            data: angular.copy({ "assigned_id": $scope.properties.userId })
        };

        return $http(req)
            .success(function(data, status) {
                var url = "/bonita/portal/resource/taskInstance/[NOMBREPROCESO]/[VERSIONPROCESO]/[NOMBRETAREA]/content/?id=[TASKID]&displayConfirmation=false";
                url = url.replace("[NOMBREPROCESO]", rowData.processName);
                url = url.replace("[VERSIONPROCESO]", rowData.processVersion);
                url = url.replace("[NOMBRETAREA]", rowData.taskName);
                url = url.replace("[TASKID]", rowData.taskId);
                $window.location.assign(url);
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            })
            .finally(function() {});
    }

    $scope.isenvelope = false;
    $scope.selectedrow = {};
    $scope.mensaje = "";
    $scope.envelope = function(row) {
        $scope.isenvelope = true;
        $scope.mensaje = "";
        $scope.selectedrow = row;
    }
    $scope.envelopeCancel = function() {
        $scope.isenvelope = false;
        $scope.selectedrow = {};
    }
    $scope.sendMail = function(row, mensaje) {
        if (row.catCampus.grupoBonita == undefined) {
            for (var i = 0; i < $scope.lstCampus.length; i++) {
                if ($scope.lstCampus[i].descripcion == row.catCampus.descripcion) {
                    row.catCampus.grupoBonita = $scope.lstCampus[i].valor;
                }
            }
        }
        var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=generateHtml&p=0&c=10",
            data: angular.copy({
                "campus": row.catCampus.grupoBonita,
                "correo": row.correoElectronico,
                "codigo": "recordatorio",
                "isEnviar": true,
                "mensaje": mensaje
            })
        };

        return $http(req)
            .success(function(data, status) {

                $scope.envelopeCancel();
            })
            .error(function(data, status) {
                console.error(data)
            })
            .finally(function() {});
    }
    $scope.lstCampus = [];
    $(function() {
        doRequest("POST", $scope.properties.urlPost);
    })


    $scope.$watch("properties.dataToSend", function(newValue, oldValue) {
        if (newValue !== undefined) {
            doRequest("POST", $scope.properties.urlPost);
        }
        console.log($scope.properties.dataToSend);
    });
    $scope.setOrderBy = function(order) {
        if ($scope.properties.dataToSend.orderby == order) {
            $scope.properties.dataToSend.orientation = ($scope.properties.dataToSend.orientation == "ASC") ? "DESC" : "ASC";
        } else {
            $scope.properties.dataToSend.orderby = order;
            $scope.properties.dataToSend.orientation = "ASC";
        }
        doRequest("POST", $scope.properties.urlPost);
    }

    $scope.filterKeyPress = function(columna, press) {
        var aplicado = true;
        for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
            const element = $scope.properties.dataToSend.lstFiltro[index];
            if (element.columna == columna) {
                $scope.properties.dataToSend.lstFiltro[index].valor = press;
                $scope.properties.dataToSend.lstFiltro[index].operador = "Que contengan";
                aplicado = false;
            }

        }
        if (aplicado) {
            var obj = { "columna": columna, "operador": "Que contengan", "valor": press }
            $scope.properties.dataToSend.lstFiltro.push(obj);
        }

        doRequest("POST", $scope.properties.urlPost);
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

        doRequest("POST", $scope.properties.urlPost);
    }

    $scope.getCampusByGrupo = function(campus) {
        var retorno = "";
        for (var i = 0; i < $scope.properties.lstCampus.length; i++) {
            if (campus == $scope.properties.lstCampus[i].grupoBonita) {
                retorno = $scope.properties.lstCampus[i].descripcion
                if ($scope.lstMembership.length == 1) {
                    $scope.properties.campusSeleccionado = $scope.lstCampus[i].valor
                }
            } else if (campus == "Todos los campus") {
                retorno = campus
            }
        }
        return retorno;
    }

    $scope.lstMembership = [];
    $scope.$watch("properties.userId", function(newValue, oldValue) {
        if (newValue !== undefined) {
            var req = {
                method: "GET",
                url: `/API/identity/membership?p=0&c=100&f=user_id%3d${$scope.properties.userId}&d=role_id&d=group_id`
            };

            return $http(req)
                .success(function(data, status) {
                    $scope.lstMembership = data;
                    $scope.campusByUser();
                })
                .error(function(data, status) {
                    console.error(data);
                })
                .finally(function() {});
        }
    });

    $scope.lstCampusByUser = [];
    $scope.campusByUser = function() {
        var resultado = [];
        resultado.push("Todos los campus")
        for (var x in $scope.lstMembership) {
            if ($scope.lstMembership[x].group_id.name.indexOf("CAMPUS") != -1) {
                let i = 0;
                resultado.forEach(value =>{
                    if(value == $scope.lstMembership[x].group_id.name){
                       i++;
                    }
                });
                if(i === 0){
                   resultado.push($scope.lstMembership[x].group_id.name);  
                }
                //resultado.push($scope.lstMembership[x].group_id.name);
            }
        }
        $scope.lstCampusByUser = resultado;
    }
    $scope.filtroCampus = ""
    $scope.addFilter = function() {
        if ($scope.filtroCampus != "Todos los campus") {
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
                        for (let index2 = 0; index2 < $scope.lstCampus.length; index2++) {
                            if ($scope.lstCampus[index2].descripcion === $scope.filtroCampus) {
                                $scope.properties.campusSeleccionado = $scope.lstCampus[index2].valor;
                            }
                        }
                        encontrado = true
                    }
                }

                if (!encontrado) {
                    $scope.properties.dataToSend.lstFiltro.push(filter);
                    for (let index2 = 0; index2 < $scope.lstCampus.length; index2++) {
                        if ($scope.lstCampus[index2].descripcion === $scope.filtroCampus) {
                            $scope.properties.campusSeleccionado = $scope.lstCampus[index2].valor;
                        }
                    }
                }
            } else {
                $scope.properties.dataToSend.lstFiltro.push(filter);
                for (let index2 = 0; index2 < $scope.lstCampus.length; index2++) {
                    if ($scope.lstCampus[index2].descripcion === $scope.filtroCampus) {
                        $scope.properties.campusSeleccionado = $scope.lstCampus[index2].valor;
                    }
                }
            }
        } else {

            if ($scope.properties.dataToSend.lstFiltro.length > 0) {
                var encontrado = false;
                for (let index = 0; index < $scope.properties.dataToSend.lstFiltro.length; index++) {
                    const element = $scope.properties.dataToSend.lstFiltro[index];
                    if (element.columna == "CAMPUS") {
                        $scope.properties.dataToSend.lstFiltro.splice(index, 1);
                        $scope.properties.campusSeleccionado = null;
                    }
                }
            }

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

        doRequest("POST", $scope.properties.urlPost);
    }
    
    function doRequest2(method, url, params, dataToSend, callback) {
        var req = {
            method: method,
            url: url,
            data: dataToSend,
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                callback(data);
            })
            .error(function(data, status) {
                console.error(data);

            })
            .finally(function() {
               
            });
    }

    $scope.getCatCampus = function() {
        var req = {
            method: "GET",
            url: "../API/bdm/businessData/com.anahuac.catalogos.CatCampus?q=find&p=0&c=100"
        };
        debugger
        return $http(req)
            .success(function(data, status) {
                $scope.lstDatosAlumno = data;
            })
            .error(function(data, status) {
                console.error(data);
            });
    }

    $scope.getCatCampus();

    this.showdatos = function(row) {
        $scope.properties.datosTransferencia = angular.copy(row);
        $scope.properties.jsonOriginal = JSON.parse(row.valorOriginal);
        $scope.properties.jsonCambio = JSON.parse(row.valorCambio);
        openModal($scope.properties.modalid);
    }

    function openModal(modalid) {

        modalService.open(modalid);
    }


    $scope.openModal = function(row) {

        if (parseInt(row.countrechazos) >= 1) {
            Swal.fire({
                title: `Aviso`,
                text: "El aspirante ya fue reactivado anteriormente, ¿Desea continuar?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: '#5cb85c',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Continuar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    $scope.properties.datosAspirante = angular.copy(row);
                    $scope.properties.jsonOriginal = angular.copy(row);
                    modalService.open($scope.properties.modalid);
                }
            })
            $scope.$apply();
        } else {
            $scope.properties.datosAspirante = angular.copy(row);
            $scope.properties.jsonOriginal = angular.copy(row);
            modalService.open($scope.properties.modalid);
        }

    }
    
    /*              GENERACIÓN PDF              */
    $scope.getReporteSolicitudAdmisionRespaldo = function(row) {
        debugger
        var req = {
            method: "POST",
            url: "../API/extension/AnahuacRest?url=getInformacionReporteSolicitudRespaldo&p=0&c=10&caseid="+row.caseid
        };

        return $http(req)
            .success(function(data, status) {
                $scope.fotobase64 = "";
                $scope.fotobase64 = row.fotografiab64;
                $scope.lstInReporteAdmision = [];
                $scope.lstInReporteAdmision = data.data[0];
                $scope.generatePDF($scope.lstInReporteAdmision);
            })
            .error(function(data, status) {
                console.error(data);
            });
    }

    
    $scope.generatePDF = function(row) {
        debugger
        var doc = new jspdf.jsPDF('p', 'mm', 'a4');
        var width = doc.internal.pageSize.getWidth();
        var height = doc.internal.pageSize.getHeight();
        var yValor = 0;
        var yvalor = 0;

        var fontText = 10;
        var fontTitle = 14;
        var fontSubTitle = 13;

        var margenPrimeraFila = 15;
        var margenSegundaFila =  135;
        var margenFilaIntermedia =  80;

        var respuestasPrimeraFila = 15;
        var respuestasSegundaFila = 135;
        var respuestasFilaIntermedia = 80;



        console.log("W "+width)
        console.log("H "+height)
        //console.log($scope.properties.data)
        
        doc.addImage("widgets/customTablePostRespaldoUsuarios/assets/img/LogoRUA.png", "PNG", ((width / 2) + 35), ((height / 2) - 128), 60, 20);

        doc.setFontSize(fontText);
        doc.text(margenSegundaFila, (height / 2) - 135, 'Usuario:');
        doc.text($scope.properties.data.user_name, 160, (height / 2) - 135);
        doc.setFontSize(fontText);
        doc.text(margenPrimeraFila, (height / 2) - 135, 'Fecha:');  
        doc.text($scope.fechaActual, 37, (height / 2) - 135);
        

        

        doc.setFontSize(fontTitle);
        doc.setFont(undefined, 'bold');
        doc.text('SOLICITUD DE ADMISIÓN', margenPrimeraFila, (height / 2) - 116);

        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 103, 190, 85, 'F');

        //  ----------------------------------------- PRIMERA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 93, 'Información de la solicitud');
        
        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 83, 'Campus donde cursará sus estudios:');
        doc.text(margenPrimeraFila, (height / 2) - 71, 'Licenciatura:');
        doc.text(margenPrimeraFila, (height / 2) - 59, 'Período de ingreso:');
        doc.text(margenPrimeraFila, (height / 2) - 47, 'Realizo proceso de admisión en otra');
        doc.text(margenPrimeraFila, (height / 2) - 42, 'universidad de la red Anáhuac:');
        doc.text(margenPrimeraFila, (height / 2) - 30, 'Lugar donde realizarás tu examen:');

        doc.setDrawColor(115, 66, 34)
        //doc.rect(margenSegundaFila,((height / 2)-84),35,45)
        doc.rect(157.5,((height / 2)-84),35,45)
        
        doc.addImage($scope.fotobase64,"JPG", 160, ((height / 2)-82), 30, 40);
        

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.lstInReporteAdmision.campuscursar1, respuestasPrimeraFila, (height / 2) - 78);
        doc.text("N/A", respuestasPrimeraFila, (height / 2) - 66);
        doc.text($scope.lstInReporteAdmision.periodo, respuestasPrimeraFila, (height / 2) - 54);
        doc.text($scope.lstInReporteAdmision.presentasteenotrocampus, respuestasPrimeraFila, (height / 2) - 37);
        doc.text($scope.lstInReporteAdmision.lugarexamen, respuestasPrimeraFila, (height / 2) - 25);
    
        //  ----------------------------------------- SEGUNDA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 12, 'Información personal');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 2, 'Nombre (s):');
        doc.text(margenFilaIntermedia, (height / 2) - 2, 'Apellido paterno:');
        doc.text(margenSegundaFila, (height / 2) - 2, 'Apellido materno:');

        doc.text(margenPrimeraFila, (height / 2) + 10, 'Correo eletrónico:');
        doc.text(margenFilaIntermedia, (height / 2) + 10, 'Fecha de nacimiento:');
        doc.text(margenSegundaFila, (height / 2) + 10, 'Sexo:');

        doc.text(margenPrimeraFila, (height / 2) + 22, 'Nacionalidad:');
        doc.text(margenFilaIntermedia, (height / 2) + 22, 'Religión:');
        doc.text(margenSegundaFila, (height / 2) + 22, 'CURP:');

        doc.text(margenPrimeraFila, (height / 2) + 34, 'Número de pasaporte:');
        doc.text(margenFilaIntermedia, (height / 2) + 34, 'Estado civil:');
        doc.text(margenSegundaFila, (height / 2) + 34, 'Teléfono celular:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.lstInReporteAdmision.primernombre+" "+$scope.lstInReporteAdmision.segundonombre, respuestasPrimeraFila, (height / 2) + 3);
        doc.text($scope.lstInReporteAdmision.apellidopaterno, respuestasFilaIntermedia, (height / 2) + 3);
        doc.text($scope.lstInReporteAdmision.apellidomaterno, respuestasSegundaFila, (height / 2) + 3);

        doc.text($scope.lstInReporteAdmision.correoelectronico, respuestasPrimeraFila, (height / 2) + 15);
        doc.text($scope.lstInReporteAdmision.fechanacimiento = ($filter('date')(Date.parse($scope.lstInReporteAdmision.fechanacimiento), "dd/MMM/yyyy")).toString(), respuestasFilaIntermedia, (height / 2) + 15);
        doc.text($scope.lstInReporteAdmision.sexo, respuestasSegundaFila, (height / 2) + 15);

        doc.text($scope.lstInReporteAdmision.nacionalidad, respuestasPrimeraFila, (height / 2) + 27);
        doc.text($scope.lstInReporteAdmision.religion, respuestasFilaIntermedia, (height / 2) + 27);
        if($scope.lstInReporteAdmision.curp !=null){
        doc.text($scope.lstInReporteAdmision.curp, respuestasSegundaFila, (height / 2) + 27);  
        doc.text(($scope.lstInReporteAdmision.curp == "" ? "N/A" : $scope.lstInReporteAdmision.curp), respuestasPrimeraFila, (height / 2) + 39);
        }
        doc.text($scope.lstInReporteAdmision.estadocivil, respuestasFilaIntermedia, (height / 2) + 39);
        doc.text($scope.lstInReporteAdmision.telefonocelular, respuestasSegundaFila, (height / 2) + 39);

        //  ----------------------------------------- TERCERA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) + 48, 190, 75, 'F');

        doc.setFontSize(fontTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 58, 'Domicilio permanente');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 70, 'País:');
        doc.text(margenFilaIntermedia, (height / 2) + 70, 'Código postal:');
        doc.text(margenSegundaFila, (height / 2) + 70, 'Estado:');

        doc.text(margenPrimeraFila, (height / 2) + 84.5, 'Ciudad:');
        doc.text(margenFilaIntermedia, (height / 2) + 82, 'Ciudad/Municipio/');
        doc.text(margenFilaIntermedia, (height / 2) + 87, 'Delegación/Poblado:');
        doc.text(margenSegundaFila, (height / 2) + 84.5, 'Colonia:');

        doc.text(margenPrimeraFila, (height / 2) + 99, 'Calle:');
        doc.text(margenFilaIntermedia, (height / 2) + 99, 'Entre calles:');
        doc.text(margenSegundaFila, (height / 2) + 99, 'Núm. Exterior');

        doc.text(margenPrimeraFila, (height / 2) + 111, 'Núm. Interior:');
        doc.text(margenFilaIntermedia, (height / 2) + 111, 'Teléfono:');
        doc.text(margenSegundaFila, (height / 2) + 111, 'Otro teléfono de contacto:');

         //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.lstInReporteAdmision.pais, respuestasPrimeraFila, (height / 2) + 75);
        doc.text($scope.lstInReporteAdmision.codigopostal, respuestasFilaIntermedia, (height / 2) + 75);
        doc.text(($scope.lstInReporteAdmision.estado == ""  ? $scope.lstInReporteAdmision.estadoExtranjero : $scope.lstInReporteAdmision.estado), respuestasSegundaFila, (height / 2) + 75);

        doc.text($scope.lstInReporteAdmision.ciudad, respuestasPrimeraFila, (height / 2) + 88.5);
        doc.text($scope.lstInReporteAdmision.delegacionmunicipio, respuestasFilaIntermedia, (height / 2) + 92);
        doc.text($scope.lstInReporteAdmision.colonia, respuestasSegundaFila, (height / 2) + 88.5);

        doc.text($scope.lstInReporteAdmision.calle, respuestasPrimeraFila, (height / 2) + 104);
        doc.text(($scope.lstInReporteAdmision.calle2 ==  null || $scope.lstInReporteAdmision.calle2 ==  "" ? "N/A" : $scope.lstInReporteAdmision.calle2), respuestasFilaIntermedia, (height / 2) + 104);
        doc.text(($scope.lstInReporteAdmision.numexterior == "" ? "N/A" : $scope.lstInReporteAdmision.numexterior), respuestasSegundaFila, (height / 2) + 104);

        doc.text(($scope.lstInReporteAdmision.numinterior == "" ? "N/A" : $scope.lstInReporteAdmision.numinterior), respuestasPrimeraFila, (height / 2) + 116);
        doc.text($scope.lstInReporteAdmision.telefono, respuestasFilaIntermedia, (height / 2) + 116);
        doc.text(($scope.lstInReporteAdmision.otrotelefonocontacto == "" ? "N/A" : $scope.lstInReporteAdmision.otrotelefonocontacto), respuestasSegundaFila, (height / 2) + 116);

        doc.addPage();
        //  ----------------------------------- NUEVA HOJA Y CUARTA SECCIÓN  ----------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 116, 'Información del bachillerato');
        
        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 106, 'Nombre bachillerato:');
        doc.text(margenFilaIntermedia, (height / 2) - 106, 'País de tu bachillerato:');
        doc.text(margenSegundaFila, (height / 2) - 106, 'Estado de tu bachillerato:');

        doc.text(margenPrimeraFila, (height / 2) - 94, 'Ciudad de tu bachillerato');
        doc.text(margenFilaIntermedia, (height / 2) - 94, 'Promedio general:');
        doc.text(margenSegundaFila, (height / 2) - 94, 'Resultado PAA:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.lstInReporteAdmision.lstPreparatoria.descripcion, respuestasPrimeraFila, (height / 2) - 101);
        doc.text(($scope.lstInReporteAdmision.lstPreparatoria.pais == null ? $scope.lstInReporteAdmision.paisbachillerato : $scope.lstInReporteAdmision.lstPreparatoria.pais), respuestasFilaIntermedia, (height / 2) - 101);
        doc.text(($scope.lstInReporteAdmision.lstPreparatoria.estado == null ? $scope.lstInReporteAdmision.estadobachillerato : $scope.lstInReporteAdmision.lstPreparatoria.estado), respuestasSegundaFila, (height / 2) - 101);

        doc.text(($scope.lstInReporteAdmision.lstPreparatoria.ciudad == null ? $scope.lstInReporteAdmision.ciudad : $scope.lstInReporteAdmision.lstPreparatoria.ciudad), respuestasPrimeraFila, (height / 2) - 89);
        doc.text($scope.lstInReporteAdmision.promediogeneral, respuestasFilaIntermedia, (height / 2) - 89);
        doc.text(($scope.lstInReporteAdmision.resultadopaa.toString() <= 0 ? "N/A" : $scope.lstInReporteAdmision.resultadopaa.toString()), respuestasSegundaFila, (height / 2) - 89);
    
        //  ----------------------------------------- QUINTA SECCIÓN  ----------------------------------------- 
        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 84, 190, 130, 'F');

        //$scope.properties.PDFobjTutor.forEach(function(PDFitem, i, objeto) {


                doc.setFontSize(fontSubTitle);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 74, 'Información del tutor');

                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 64, 'Título:');
                doc.text(margenFilaIntermedia, (height / 2) - 64, 'Nombre (s):');
                doc.text(margenSegundaFila, (height / 2) - 64, 'Apellido (s):');

              

                doc.text(margenPrimeraFila, (height / 2) - 52, 'Parentesco:');
                doc.text(margenFilaIntermedia, (height / 2) - 52, 'Correo eletrónico:');
                doc.text(margenSegundaFila, (height / 2) - 52, 'Escolaridad del tutor:');

                doc.text(margenPrimeraFila, (height / 2) - 40, 'Ocupación del tutor:');
                doc.text(margenFilaIntermedia, (height / 2) - 40, 'Empresa:');
                doc.text(margenSegundaFila, (height / 2) - 40, 'Universidad Anáhuac:');

                //Respuestas
                doc.setFont(undefined, 'normal');
                doc.text($scope.lstInReporteAdmision.objTutor.tituloTutor, respuestasPrimeraFila, (height / 2) - 59);
                doc.text($scope.lstInReporteAdmision.objTutor.nombreTutor, respuestasFilaIntermedia, (height / 2) - 59);
                doc.text($scope.lstInReporteAdmision.objTutor.apellidoTutor, respuestasSegundaFila, (height / 2) - 59);

                doc.text($scope.lstInReporteAdmision.objTutor.parentezcoTutor, respuestasPrimeraFila, (height / 2) - 47);
                doc.text($scope.lstInReporteAdmision.objTutor.correoTutor, respuestasFilaIntermedia, (height / 2) - 47);
                doc.text($scope.lstInReporteAdmision.objTutor.escolaridadTutore, respuestasSegundaFila, (height / 2) - 47);

                doc.text(($scope.lstInReporteAdmision.objTutor.ocupacionTutor == "" ? "No trabaja" : $scope.lstInReporteAdmision.objTutor.ocupacionTutor), respuestasPrimeraFila, (height / 2) - 35);
                doc.text(($scope.lstInReporteAdmision.objTutor.empresatrabajaTutor == "" ? "No trabaja" : $scope.lstInReporteAdmision.objTutor.empresatrabajaTutor), respuestasFilaIntermedia, (height / 2) - 35);
                doc.text(($scope.lstInReporteAdmision.objTutor.egresouniversidadanahuacTutor == null ? "No" : $scope.lstInReporteAdmision.objTutor.egresouniversidadanahuacTutor), respuestasSegundaFila, (height / 2) - 35);

                doc.setFontSize(fontSubTitle);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 20, 'Domicilio permanente del tutor');

                //Encabezados
                doc.setFontSize(fontText);
                doc.setFont(undefined, 'bold');
                doc.text(margenPrimeraFila, (height / 2) - 10, 'País:');
                doc.text(margenFilaIntermedia, (height / 2) - 10, 'Código postal:');
                doc.text(margenSegundaFila, (height / 2) - 10, 'Estado:');

                doc.text(margenPrimeraFila, (height / 2) + 3.5, 'Ciudad:');
                doc.text(margenFilaIntermedia, (height / 2) + 2, 'Ciudad/Municipio/');
                doc.text(margenFilaIntermedia, (height / 2) + 7, 'Delegación/Poblado:');
                doc.text(margenSegundaFila, (height / 2) + 3.5, 'Colonia:');

                doc.text(margenPrimeraFila, (height / 2) + 19, 'Calle:');
                doc.text(margenFilaIntermedia, (height / 2) + 19, 'Núm. Exterior:');
                doc.text(margenSegundaFila, (height / 2) + 19, 'Núm. Interior:');
                doc.text(margenPrimeraFila, (height / 2) + 31, 'Teléfono:');

                //Respuestas
                doc.setFont(undefined, 'normal');
                doc.text($scope.lstInReporteAdmision.objTutor.paisTutor, respuestasPrimeraFila, (height / 2) - 5);
                doc.text($scope.lstInReporteAdmision.objTutor.codigopostalTutor, respuestasFilaIntermedia, (height / 2) - 5);
                doc.text($scope.lstInReporteAdmision.objTutor.estadoTutor, respuestasSegundaFila, (height / 2) - 5);

                doc.text($scope.lstInReporteAdmision.objTutor.ciudadTutor, respuestasPrimeraFila, (height / 2) + 7.5);
                doc.text($scope.lstInReporteAdmision.objTutor.delegacionTutor, respuestasFilaIntermedia, (height / 2) + 12);
                doc.text($scope.lstInReporteAdmision.objTutor.coloniaTutor, respuestasSegundaFila, (height / 2) + 7.5);

                doc.text($scope.lstInReporteAdmision.objTutor.calleTutor, respuestasPrimeraFila, (height / 2) + 24);
                doc.text(($scope.lstInReporteAdmision.objTutor.numexteriorTutor == "" ? "N/A" : $scope.lstInReporteAdmision.objTutor.numexteriorTutor), respuestasFilaIntermedia, (height / 2) + 24);
                doc.text(($scope.lstInReporteAdmision.objTutor.numinteriorTutor == "" ? "N/A" : $scope.lstInReporteAdmision.objTutor.numinteriorTutor), respuestasSegundaFila, (height / 2) + 24);
                doc.text($scope.lstInReporteAdmision.objTutor.telefonoTutor, respuestasPrimeraFila, countFinSeccionTutor = ((height / 2) + 36));

                //console.log(countFinSeccionTutor)

        //})

        //  ----------------------------------------- SEXTA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 55, 'Información del padre');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 65, 'Título:');
        doc.text(margenFilaIntermedia, (height / 2) + 65, 'Nombre (s):');
        doc.text(margenSegundaFila, (height / 2) + 65, 'Apellido (s):');

        doc.text(margenPrimeraFila, (height / 2) + 77, 'Correo eletrónico:');
        doc.text(margenFilaIntermedia, (height / 2) + 77, 'Escolaridad del padre:');
        doc.text(margenSegundaFila, (height / 2) + 77, 'Universidad Anáhuac:');

        doc.text(margenPrimeraFila, (height / 2) + 89, 'Ocupación del padre:');
        doc.text(margenFilaIntermedia, (height / 2) + 89, 'Empresa:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        
        if($scope.lstInReporteAdmision.objPadre.tituloPadre !=null){
        doc.text($scope.lstInReporteAdmision.objPadre.tituloPadre, respuestasPrimeraFila, (height / 2) + 70);
        doc.text($scope.lstInReporteAdmision.objPadre.nombrePadre, respuestasFilaIntermedia, (height / 2) + 70);
        doc.text($scope.lstInReporteAdmision.objPadre.apellidoPadre, respuestasSegundaFila, (height / 2) + 70);  
        }else{
        doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) + 70);
        doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) + 70);
        doc.text("Se desconoce", respuestasSegundaFila, (height / 2) + 70);            
        }
        
        if($scope.lstInReporteAdmision.objPadre.vive != null){
        if($scope.lstInReporteAdmision.objPadre.vive == "Sí") {
        doc.text($scope.lstInReporteAdmision.objPadre.correoPadre, respuestasPrimeraFila, (height / 2) + 82);
        doc.text($scope.lstInReporteAdmision.objPadre.escolaridadPadre, respuestasFilaIntermedia, (height / 2) + 82);
        doc.text(($scope.lstInReporteAdmision.objPadre.egresouniversidadanahuacPadre == null ? "No" : $scope.lstInReporteAdmision.objPadre.egresouniversidadanahuacPadre), respuestasSegundaFila, (height / 2) + 82);

        doc.text(($scope.lstInReporteAdmision.objPadre.ocupacionPadre == null || $scope.lstInReporteAdmision.objPadre.ocupacionPadre == "" ? "No trabaja" : $scope.lstInReporteAdmision.objPadre.ocupacionPadre), respuestasPrimeraFila, (height / 2) + 94);
        doc.text(($scope.lstInReporteAdmision.objPadre.empresatrabajaPadre == null || $scope.lstInReporteAdmision.objPadre.empresatrabajaPadre == "" ? "No trabaja" : $scope.lstInReporteAdmision.objPadre.empresatrabajaPadre), respuestasFilaIntermedia, (height / 2) + 94);
        } else {
            doc.setFont(undefined, 'normal');
            doc.text("Finado", respuestasPrimeraFila, (height / 2) + 82);
            doc.text("Finado", respuestasFilaIntermedia, (height / 2) + 82);
            doc.text("Finado", respuestasSegundaFila, (height / 2) + 82);

            doc.text("Finado", respuestasPrimeraFila, (height / 2) + 94);
            doc.text("Finado", respuestasFilaIntermedia, (height / 2) + 94);
          }  
        }else{
           doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) + 82);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) + 82);
            doc.text("Se desconoce", respuestasSegundaFila, (height / 2) + 82);

            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) + 94);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) + 94);
        }
        
        doc.addPage();

        //  ----------------------------------- NUEVA HOJA Y SEPTIMA SECCIÓN  ----------------------------------- 

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 116, 'Domicilio permanente del padre');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 106, 'País:');
        doc.text(margenFilaIntermedia, (height / 2) - 106, 'Código postal:');
        doc.text(margenSegundaFila, (height / 2) - 106, 'Estado:');

        doc.text(margenPrimeraFila, (height / 2) - 90.5, 'Ciudad:');
        doc.text(margenFilaIntermedia, (height / 2) - 93, 'Ciudad/Municipio/');
        doc.text(margenFilaIntermedia, (height / 2) - 88, 'Delegación/Poblado:');
        doc.text(margenSegundaFila, (height / 2) - 90.5, 'Colonia:');

        doc.text(margenPrimeraFila, (height / 2) - 76, 'Calle:');
        doc.text(margenFilaIntermedia, (height / 2) - 76, 'Núm. Exterior:');
        doc.text(margenSegundaFila, (height / 2) - 76, 'Núm. Interior:');
        doc.text(margenPrimeraFila, (height / 2) - 64, 'Teléfono:');

        //Respuestas
        if($scope.lstInReporteAdmision.objPadre.paisPadre != null){
        doc.setFont(undefined, 'normal');
        doc.text($scope.lstInReporteAdmision.objPadre.paisPadre, respuestasPrimeraFila, (height / 2) - 101);
        doc.text($scope.lstInReporteAdmision.objPadre.codigopostalPadre, respuestasFilaIntermedia, (height / 2) - 101);
        doc.text($scope.lstInReporteAdmision.objPadre.estadoPadre, respuestasSegundaFila, (height / 2) - 101);

        doc.text($scope.lstInReporteAdmision.objPadre.ciudadPadre, respuestasPrimeraFila, (height / 2) - 86.5);
        doc.text($scope.lstInReporteAdmision.objPadre.delegacionPadre, respuestasFilaIntermedia, (height / 2) - 83);
        doc.text($scope.lstInReporteAdmision.objPadre.coloniaPadre, respuestasSegundaFila, (height / 2) - 86.5);

        doc.text($scope.lstInReporteAdmision.objPadre.callePadre, respuestasPrimeraFila, (height / 2) - 71);
        doc.text(($scope.lstInReporteAdmision.objPadre.numexteriorPadre == "" ? "N/A" : $scope.lstInReporteAdmision.objPadre.numexteriorPadre), respuestasFilaIntermedia, (height / 2) - 71);
        doc.text(($scope.lstInReporteAdmision.objPadre.numinteriorPadre == "" ? "N/A" : $scope.lstInReporteAdmision.objPadre.numinteriorPadre), respuestasSegundaFila, (height / 2) - 71);
        doc.text($scope.lstInReporteAdmision.objPadre.telefonoPadre, respuestasPrimeraFila, (height / 2) - 59);
  
      } else{

        doc.setFont(undefined, 'normal');
        doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) - 101);
        doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) - 101);
        doc.text("Se desconoce", respuestasSegundaFila, (height / 2) - 101);

        doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) - 86.5);
        doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) - 83);
        doc.text("Se desconoce", respuestasSegundaFila,(height / 2) - 86.5);

        doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) - 71);
        doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) - 71);
        doc.text("Se desconoce", respuestasSegundaFila, (height / 2) - 71);
        doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) - 59);
      }
        
        //  ----------------------------------------- OCTAVA SECCIÓN  ----------------------------------------- 

        doc.setFillColor(228, 212, 200);
        doc.rect(10, (height / 2) - 51, 190, 120, 'F');

        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 41, 'Información de la madre');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) - 31, 'Título:');
        doc.text(margenFilaIntermedia, (height / 2) - 31, 'Nombre (s):');
        doc.text(margenSegundaFila, (height / 2) - 31, 'Apellido (s):');

        doc.text(margenPrimeraFila, (height / 2) - 19, 'Correo eletrónico:');
        doc.text(margenFilaIntermedia, (height / 2) - 19, 'Escolaridad del tutor:');
        doc.text(margenSegundaFila, (height / 2) - 19, 'Universidad Anáhuac:');

        doc.text(margenPrimeraFila, (height / 2) - 7, 'Ocupación del tutor:');
        doc.text(margenFilaIntermedia, (height / 2) - 7, 'Empresa:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        if($scope.lstInReporteAdmision.objMadre.tituloMadre != null){
        doc.text($scope.lstInReporteAdmision.objMadre.tituloMadre,  respuestasPrimeraFila, (height / 2) - 26);
        doc.text($scope.lstInReporteAdmision.objMadre.nombreMadre, respuestasFilaIntermedia, (height / 2) - 26);
        doc.text($scope.lstInReporteAdmision.objMadre.apellidoMadre, respuestasSegundaFila, (height / 2) - 26);

        }else{
        doc.text("Se desconoce",  respuestasPrimeraFila, (height / 2) - 26);
        doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) - 26);
        doc.text("Se desconoce", respuestasSegundaFila, (height / 2) - 26);          
        }

        if($scope.lstInReporteAdmision.objMadre.vive != null){
        if($scope.lstInReporteAdmision.objMadre.vive == "Sí") {
        doc.text($scope.lstInReporteAdmision.objMadre.correoMadre, respuestasPrimeraFila, (height / 2) - 14);
        doc.text($scope.lstInReporteAdmision.objMadre.escolaridadMadre, respuestasFilaIntermedia, (height / 2) - 14);
        doc.text(($scope.lstInReporteAdmision.objMadre.egresouniversidadanahuacMadre == null ? "No" : $scope.lstInReporteAdmision.objMadre.egresouniversidadanahuacMadre), respuestasSegundaFila, (height / 2) - 14);

        doc.text(($scope.lstInReporteAdmision.objMadre.ocupacionMadre == null || $scope.lstInReporteAdmision.objMadre.ocupacionMadre == "" ? "No trabaja" : $scope.lstInReporteAdmision.objMadre.ocupacionMadre), respuestasPrimeraFila, (height / 2) - 2);
        doc.text(($scope.lstInReporteAdmision.objMadre.empresatrabajaMadre == null || $scope.lstInReporteAdmision.objMadre.empresatrabajaMadre == "" ? "No trabaja" : $scope.lstInReporteAdmision.objMadre.empresatrabajaMadre), respuestasFilaIntermedia, (height / 2) - 2);
        } else {
            doc.setFont(undefined, 'normal');
            doc.text("Finado", respuestasPrimeraFila, (height / 2) - 14);
            doc.text("Finado", respuestasFilaIntermedia, (height / 2) - 14);
            doc.text("Finado", respuestasSegundaFila, (height / 2) - 14);

            doc.text("Finado", respuestasPrimeraFila, (height / 2) - 2);
            doc.text("Finado", respuestasSegundaFila, (height / 2) - 2);
        }

        }else{
            doc.setFont(undefined, 'normal');
            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) - 14);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) - 14);
            doc.text("Se desconoce", respuestasSegundaFila, (height / 2) - 14);

            doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) - 2);
            doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) - 2);

        }
        
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 8, 'Domicilio permanente de la madre');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 18, 'País:');
        doc.text(margenFilaIntermedia, (height / 2) + 18, 'Código postal:');
        doc.text(margenSegundaFila, (height / 2) + 18, 'Estado:');

        doc.text(margenPrimeraFila, (height / 2) + 31.5, 'Ciudad:');
        doc.text(margenFilaIntermedia, (height / 2) + 30, 'Ciudad/Municipio/');
        doc.text(margenFilaIntermedia, (height / 2) + 35, 'Delegación/Poblado:');
        doc.text(margenSegundaFila, (height / 2) + 31.5, 'Colonia:');

        doc.text(margenPrimeraFila, (height / 2) + 47, 'Calle:');
        doc.text(margenFilaIntermedia, (height / 2) + 47, 'Núm. Exterior:');
        doc.text(margenSegundaFila, (height / 2) + 47, 'Núm. Interior:');
        doc.text(margenPrimeraFila, (height / 2) + 59, 'Teléfono:');

        //Respuestas
        if($scope.lstInReporteAdmision.objMadre.paisMadre != null){
        doc.setFont(undefined, 'normal');
        doc.text($scope.lstInReporteAdmision.objMadre.paisMadre, respuestasPrimeraFila, (height / 2) + 23);
        doc.text($scope.lstInReporteAdmision.objMadre.codigopostalMadre, respuestasFilaIntermedia, (height / 2) + 23);
        doc.text($scope.lstInReporteAdmision.objMadre.estadoMadre, respuestasSegundaFila, (height / 2) + 23);

        doc.text($scope.lstInReporteAdmision.objMadre.ciudadMadre, respuestasPrimeraFila, (height / 2) + 35.5);
        doc.text($scope.lstInReporteAdmision.objMadre.delegacionMadre, respuestasFilaIntermedia, (height / 2) + 40);
        doc.text($scope.lstInReporteAdmision.objMadre.coloniaMadre, respuestasSegundaFila, (height / 2) + 35.5);

        doc.text($scope.lstInReporteAdmision.objMadre.calleMadre, respuestasPrimeraFila, (height / 2) + 52);
        doc.text(($scope.lstInReporteAdmision.objMadre.numexteriorMadre == "" ? "N/A" : $scope.lstInReporteAdmision.objMadre.numexteriorMadre), respuestasFilaIntermedia, (height / 2) + 52);
        doc.text(($scope.lstInReporteAdmision.objMadre.numinteriorMadre == "" ? "N/A" : $scope.lstInReporteAdmision.objMadre.numinteriorMadre), respuestasSegundaFila, (height / 2) + 52);
        doc.text($scope.lstInReporteAdmision.objMadre.telefonoMadre, respuestasPrimeraFila, (height / 2) + 64);  
        }else{

        doc.setFont(undefined, 'normal');
        doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) + 23);
        doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) + 23);
        doc.text("Se desconoce", respuestasSegundaFila, (height / 2) + 23);

        doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) + 35.5);
        doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) + 40);
        doc.text("Se desconoce", respuestasSegundaFila, (height / 2) + 35.5);

        doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) + 52);
        doc.text("Se desconoce", respuestasFilaIntermedia, (height / 2) + 52);
        doc.text("Se desconoce", respuestasSegundaFila, (height / 2) + 52);
        doc.text("Se desconoce", respuestasPrimeraFila, (height / 2) + 64);  

        }
        

        //  ----------------------------------------- NOVENA SECCIÓN  ----------------------------------------- 
        doc.setFontSize(fontSubTitle);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 79, 'Contacto(s) en caso de emergencia');

        //Encabezados
        doc.setFontSize(fontText);
        doc.setFont(undefined, 'bold');
        doc.text(margenPrimeraFila, (height / 2) + 89, 'Contacto de emergencia:');
        doc.text(margenFilaIntermedia, (height / 2) + 89, 'Parentesco:');
        doc.text(margenSegundaFila, (height / 2) + 89, 'Teléfono de emergencia:');

        doc.text(margenPrimeraFila, (height / 2) + 101, 'Celular de emergencia:');

        //Respuestas
        doc.setFont(undefined, 'normal');
        doc.text($scope.lstInReporteAdmision.lstEmergencia.nombreEmergencia, respuestasPrimeraFila, (height / 2) + 94);
        doc.text("N/A", respuestasFilaIntermedia, (height / 2) + 94);
        doc.text($scope.lstInReporteAdmision.lstEmergencia.telefonoEmergencia, respuestasSegundaFila, (height / 2) + 94);

        doc.text($scope.lstInReporteAdmision.lstEmergencia.telefonoCelularEmergencia, respuestasPrimeraFila, (height / 2) + 106);

        doc.save(`CuestionarioSolicitud.pdf`);
    }
}