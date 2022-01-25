function PbTableCtrl($scope, $http, $window, blockUI) {

    this.isArray = Array.isArray;

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
    $scope.functionFechaExmenes = function(row, op) {
        let fechas;
        let NoCollegeBoard = -1;
        let NoPsicometrico = -1;
        let NoEntrevista = -1;


        if (row.fechasexamenes == null || row.fechasexamenes == "" || row.fechasexamenes == " ") {
            fechas = "Sin programar                                                 "

        } else {

            var arrayDeCadenas = row.fechasexamenes.split(",");
            for (var i = 0; i < arrayDeCadenas.length; i++) {
                if (arrayDeCadenas[i].includes("Examen de aptitudes y conocimientos")) {
                    NoCollegeBoard = i;
                } else if (arrayDeCadenas[i].includes("Entrevista")) {
                    NoEntrevista = i;
                } else if (arrayDeCadenas[i].includes("Examen Psicométrico")) {
                    NoPsicometrico = i;
                }
            }
            if (op == 1) {
                if ($scope.functionAsistenciaExmenes(row, op) || row.cbcoincide === "t") {
                    fechas = (row.cbcoincide == "t" ? " Examen de aptitudes y conocimientos (validado)" : " Examen de aptitudes y conocimientos")
                        //fechas="Asistió Examen de aptitudes y conocimientos"
                } else {
                    fechas = " " + arrayDeCadenas[NoCollegeBoard]
                }

            }
            if (op == 2) {
                if ($scope.functionAsistenciaExmenes(row, op)) {
                    fechas = " Entrevista"
                        //fechas="Asistió Entrevista"
                } else {
                    fechas = " " + arrayDeCadenas[NoEntrevista]
                }
            }
            if (op == 3) {
                if ($scope.functionAsistenciaExmenes(row, op)) {
                    fechas = " Examen Psicométrico"
                        // fechas="Asistió Examen Psicométrico"
                } else {
                    fechas = " " + arrayDeCadenas[NoPsicometrico]
                }
            }

        }
        return fechas;

    }

    $scope.functionAsistenciaExmenes = function(row, op) {
        let resultado;
        let NoCollegeBoard = -1;
        let NoPsicometrico = -1;
        let NoEntrevista = -1;

        var arrayDeCadenas = row.asistencia.split(",");
        for (var i = 0; i < arrayDeCadenas.length; i++) {
            if (arrayDeCadenas[i].includes("Examen de aptitudes y conocimientos")) {
                NoCollegeBoard = i;
            } else if (arrayDeCadenas[i].includes("Entrevista")) {
                NoEntrevista = i;
            } else if (arrayDeCadenas[i].includes("Examen Psicométrico")) {
                NoPsicometrico = i;
            }
        }

        if (row.asistencia !== null || row.asistencia !== "" || row.asistencia !== " ") {
            if (op === 1) {
                if ((NoCollegeBoard == -1 ? false : arrayDeCadenas[NoCollegeBoard].includes("1")) || row.cbcoincide === "t") {
                    resultado = true;
                } else {
                    resultado = false
                }
            } else if (op === 2) {
                if (NoEntrevista == -1 ? false : arrayDeCadenas[NoEntrevista].includes("1")) {
                    resultado = true;
                } else {
                    resultado = false
                }
            } else if (op === 3) {
                if (NoPsicometrico == -1 ? false : arrayDeCadenas[NoPsicometrico].includes("1")) {
                    resultado = true;
                } else {
                    resultado = false
                }
            }
        }
        return resultado;

    }

    $scope.asignarTarea = function(rowData) {
        if ($scope.isPeriodoVencido(rowData.periodofin)) {
            Swal.fire("¡Periodo vencido!", "El periodo del aspirante ha vencido, se debe actualizar para poder continuar con el proceso", "warning").then((value) => {




                var page = "verSolicitudAdmisionADV2";

                var req = {
                    method: "GET",
                    url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
                };

                return $http(req)
                    .success(function(data, status) {

                        blockUI.start();
                        var req2 = {
                            method: "GET",
                            url: `/API/bpm/humanTask?p=0&c=10&f=caseId=${rowData.caseid}&f=state=ready&d=processId`
                        };

                        $http(req2)
                            .success(function(data2, status) {

                                ///API/bpm/humanTask?p=0&c=10&f=caseId=30197&f=state=ready&d=processId

                                var url = "/bonita/portal/resource/app/administrativo/[PAGE]/content/?id=[TASKID]&caseId=[CASEID]&displayConfirmation=false";
                                if (data.length > 0) {
                                    if (parseFloat(data2[0].processId.version) < 1.51) {
                                        page = "verSolicitudAdmision";
                                    }
                                    url = url.replace("[PAGE]", page);
                                    url = url.replace("[TASKID]", data[0].id);
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


            });
        } else {


            var page = "verSolicitudAdmisionADV2";

            var req = {
                method: "GET",
                url: `/API/bpm/task?p=0&c=10&f=caseId%3d${rowData.caseid}&f=isFailed%3dfalse`
            };

            return $http(req)
                .success(function(data, status) {

                    blockUI.start();
                    var req2 = {
                        method: "GET",
                        url: `/API/bpm/humanTask?p=0&c=10&f=caseId=${rowData.caseid}&f=state=ready&d=processId`
                    };

                    $http(req2)
                        .success(function(data2, status) {

                            ///API/bpm/humanTask?p=0&c=10&f=caseId=30197&f=state=ready&d=processId

                            var url = "/bonita/portal/resource/app/administrativo/[PAGE]/content/?id=[TASKID]&caseId=[CASEID]&displayConfirmation=false";
                            if (data.length > 0) {
                                if (parseFloat(data2[0].processId.version) < 1.51) {
                                    page = "verSolicitudAdmision";
                                }
                                url = url.replace("[PAGE]", page);
                                url = url.replace("[TASKID]", data[0].id);
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
                "correo": row.correoelectronico,
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
                if ($scope.lstCampusByUser.length === 2) {
                    $scope.properties.campusSeleccionado = $scope.properties.lstCampus[i].grupoBonita
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
        // var isSerua = true;
        resultado.push("Todos los campus")
        for (var x in $scope.lstMembership) {
            if ($scope.lstMembership[x].group_id.name.indexOf("CAMPUS") != -1) {
                let i = 0;
                resultado.forEach(value => {
                    if (value == $scope.lstMembership[x].group_id.name) {
                        i++;
                    }
                });
                if (i === 0) {
                    resultado.push($scope.lstMembership[x].group_id.name);
                }
            }
        }
        $scope.lstCampusByUser = resultado;
    }
    $scope.filtroCampus = ""
    $scope.caseId = null;
    $scope.recursiveCount = 0;

    $scope.sendExamen = function(row) {
        Swal.fire({
            title: "¿Está seguro que desea terminar el pase de lista?",
            text: "",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Sí',
            cancelButtonText: 'No'
        }).then((result) => {
            if (result.isConfirmed) {
                $scope.caseId = row.caseid;
                var req = {
                    method: "GET",
                    url: "../API/bpm/timerEventTrigger?caseId=" + row.caseid + "&p=0&c=9999&"
                };
                return $http(req)
                    .success(function(data, status) {
                        $scope.readDataTimmer(data);
                    })
                    .error(function(data, status) {
                        console.error(data);
                    });
            }
        })
    }

    $scope.readDataTimmer = function(data) {
        var idTimer = null;
        for (var indexD in data) {
            if (data[indexD].eventInstanceName === "Timmer pase de lista") {
                idTimer = data[indexD].id;
            }
        }
        if (idTimer == null) {
            Swal.fire("Error", "Esta tarea no se puede avanzar", "warning");
        } else {
            var req = {
                method: "PUT",
                url: "../API/bpm/timerEventTrigger/" + idTimer,
                data: { "executionDate": 1 }
            };
            return $http(req)
                .success(function(dataResponse, status) {
                    blockUI.start();
                    $scope.findGenerarCredencial();
                })
                .error(function(dataResponse, status) {
                    console.error(dataResponse);
                });
        }
    }

    $scope.findGenerarCredencial = function() {
        console.log("findGenerarCredencial")
        var req = {
            method: "GET",
            url: "../API/bpm/task?p=0&c=10&f=caseId%3d[CASEID]&f=isFailed%3dfalse&f=name=Generar%20credencial".replace("[CASEID]", $scope.caseId)
        };
        return $http(req)
            .success(function(data, status) {
                $scope.lstTarea = data;
                if ($scope.lstTarea.length > 0) {
                    $scope.desasignarTarea($scope.lstTarea[0].id);
                } else {
                    $scope.recursiveGet();
                }
            })
            .error(function(data, status) {
                console.error(data);
            });
    }

    $scope.desasignarTarea = function(taskId) {
        console.log("desasignarTarea")
        var req = {
            method: "GET",
            url: "../API/bpm/humanTask/[TASKID]".replace("[TASKID]", taskId),
            data: { "assigned_id": "" }
        };
        return $http(req)
            .success(function(data, status) {
                $scope.asignarTareaCorreo(taskId);
            })
            .error(function(data, status) {
                console.error(data);
            });
        //https://procesos.bpm.anahuac.mx/API/bpm/humanTask/580535
        //{"assigned_id":""}
    }

    $scope.asignarTareaCorreo = function(taskId) {
        console.log("asignarTareaCorreo")
        var req = {
            method: "GET",
            url: "../API/bpm/humanTask/[TASKID]".replace("[TASKID]", taskId),
            data: { "assigned_id": $scope.properties.userId }
        };
        return $http(req)
            .success(function(data, status) {
                $scope.ejecutarTareaPL(taskId);
            })
            .error(function(data, status) {
                console.error(data);
            });
    }

    $scope.ejecutarTareaPL = function(taskId) {
        console.log("ejecutarTareaPL");
        var req = {
            method: "POST",
            url: "../API/bpm/userTask/[TASKID]/execution?assign=false".replace("[TASKID]", taskId),
            data: {}
        };
        return $http(req)
            .success(function(data, status) {
                $scope.recursiveGet();
            })
            .error(function(data, status) {
                console.error(data);
            });
    }

    $scope.recursiveGet = function() {
        console.log("recursiveGet")
        $scope.foundTask = false;
        var req = {
            method: "GET",
            url: "../API/bpm/task?p=0&c=10&f=caseId=[CASEID]&f=isFailed=false".replace("[CASEID]", $scope.caseId)
        };
        return $http(req)
            .success(function(data, status) {
                for (var indexData in data) {
                    if (data[indexData].name == "Seleccionar cita" || data[indexData].name == "Carga y consulta de resultados") {
                        $scope.foundTask = true;
                    }
                }

                if ($scope.foundTask) {
                    $scope.recursiveCount = 0;
                    doRequest("POST", $scope.properties.urlPost);
                    blockUI.stop();
                    Swal.fire("", "Aspirante enviado exitosamente", "success");
                } else {
                    if ($scope.recursiveCount <= 50) {
                        setTimeout(function() {
                            $scope.recursiveCount++;
                            $scope.$apply();
                            $scope.recursiveGet();
                        }, 1000);
                    } else {
                        blockUI.stop();
                        $scope.recursiveCount = 0;
                        doRequest("POST", $scope.properties.urlPost);
                    }
                }
            })
            .error(function(data, status) {
                console.error(data);
            });


    }

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

    $scope.getCatCampus = function() {
        var req = {
            method: "GET",
            url: "../API/bdm/businessData/com.anahuac.catalogos.CatCampus?q=find&p=0&c=100"
        };
        return $http(req)
            .success(function(data, status) {
                $scope.lstCampus = [];
                for (var index in data) {
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

    $scope.Transferencia = function(row) {
        let sedeExamen;
        if (row.transferencia == row.campus) {
            sedeExamen = "MISMO CAMPUS"
        } else {
            sedeExamen = "TRANSFERENCIA"
        }
        return sedeExamen;
    }
    $scope.isPeriodoVencido = function(periodofin) {
        var fecha = new Date(periodofin.slice(0, 10))
        return fecha < new Date();
    }


    $scope.getCatCampus();
}