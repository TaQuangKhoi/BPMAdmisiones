function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService, blockUI) {
    var cont = 0;
    $scope.action = function() {
        $scope.properties.disabled = true;
        if ($scope.properties.selectedIndex === 0) {} else if ($scope.properties.selectedIndex === 1) {
            console.log("Modal continuar");

            if ($scope.properties.catSolicitudDeAdmision.catCampus.persistenceId_string === "") {
                swal("¡Campus!", "Debes seleccionar un campus donde cursarás tus estudios", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar === null) {
                swal("¡Licenciatura!", "Debes seleccionar una licenciatura", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar.propedeutico) {
                if ($scope.properties.catSolicitudDeAdmision.catPropedeutico === null) {
                    swal("¡Curso propedéutico!", "Favor de seleccionar un curso propedéutico", "warning");
                } else {
                    if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                        swal("¡Período!", "Debes seleccionar un período donde cursarás sus estudios", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                        swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás tu examen", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                        if ($scope.properties.lugarexamen === "En un estado") {
                            if ($scope.properties.catSolicitudDeAdmision.catEstadoExamen === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar un estado donde realizarás el examen", "warning");
                            } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar una ciudad donde realizarás el examen", "warning");
                            } else if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                //$scope.properties.selectedIndex++;
                                $scope.assignTask();
                            }
                        } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                            if ($scope.properties.catSolicitudDeAdmision.catPaisExamen === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar un país donde realizarás el examen", "warning");
                            } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                                swal("¡Lugar de examen!", "Debes seleccionar una ciudad donde realizarás el examen", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    //$scope.properties.selectedIndex++;
                                    $scope.assignTask();
                                }
                            }
                        } else {
                            //$scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                            //$scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                //$scope.properties.selectedIndex++;
                                $scope.assignTask();
                            }
                        }

                    } else {
                        swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás el examen", "warning");
                    }
                    /* if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                 $scope.properties.selectedIndex--;
                             } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                 //$scope.properties.selectedIndex++;
                                 $scope.assignTask();
                             }*/
                }
            } else if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                swal("¡Periodo!", "Debes seleccionar un periodo donde cursarás tus estudios", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                swal("¡Lugar de examen!", "Debes seleccionar un lugar donde cursarás tus estudios", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.catSolicitudDeAdmision.catEstadoExamen === null || $scope.properties.catSolicitudDeAdmision.ciudadExamen === "") {
                        swal("¡Lugar de examen!", "Debes seleccionar un estado y una ciudad donde realizarás el examen", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            //closeModal($scope.properties.modalid);
                            //$scope.properties.selectedIndex++;
                            $scope.assignTask();
                        }
                    }
                } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                    if ($scope.properties.catSolicitudDeAdmision.catPaisExamen === null || $scope.properties.catSolicitudDeAdmision.ciudadExamen === "") {
                        swal("¡Lugar de examen!", "Debes seleccionar un país y una ciudad donde realizarás el examen", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.assignTask();
                        }
                    }
                } else {
                    //$scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                    //$scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.assignTask();
                    }
                }

            } else {
                swal("¡Lugar de examen!", "Debes seleccionar un lugar donde realizarás el examen", "warning");
            }
            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                $scope.assignTask();
                            }*/

        } else if ($scope.properties.selectedIndex === 2) {

        } else if ($scope.properties.selectedIndex === 3) {

        } else if ($scope.properties.selectedIndex === 4) {

        }

    }

    function openModal(modalid) {

        modalService.open(modalid);
    }

    function closeModal(shouldClose) {
        if (shouldClose)
            modalService.close();
    }

    $scope.assignTask = function() {
        //$scope.showModal();
        blockUI.start();
        var req = {
            method: "GET",
            url: "../API/system/session/unusedid",
        };
        return $http(req).success(function(data, status) {
                let url = "../API/extension/RegistroPut?url=userTask&task_id=" + $scope.properties.taskId;
                //let url = "../API/bpm/userTask/" + $scope.properties.taskId;
                if ($scope.properties.catSolicitudDeAdmision.correoElectronico != data.user_name) {
                    swal("¡Error!", "Su sesion ha expirado", "warning");
                    setTimeout(function() { window.top.location.href = $scope.properties.urlDireccion }, 3000);
                } else {
                    var req = {
                        method: "PUT",
                        url: url,
                        data: {
                            "assigned_id": $scope.properties.userId
                        }
                    };

                    return $http(req).success(function(data, status) {
                            //$scope.executeTask();
                            submitTask();
                        })
                        .error(function(data, status) {
                            swal("¡Error!", data.message, "error");
                        })
                        .finally(function() {});
                }
            })
            .error(function(data, status) {
                swal("¡Error!", data.message, "error");
            })
            .finally(function() {

            });

    }

    function submitTask() {
        var id;
        //id = getUrlParam('id');
        id = $scope.properties.taskId;
        if (id) {
            var params = getUserParam();
            //params.assign = $scope.properties.assign;
            doRequest('POST', '../API/bpm/userTask/' + id + '/execution', params).then(function() {
                localStorageService.delete($window.location.href);
            });
        } else {
            $log.log('Impossible to retrieve the task id value from the URL');
        }
    }

    function getUserParam() {
        var userId = getUrlParam('user');
        if (userId) {
            return {
                'user': userId
            };
        }
        return {};
    }

    function getUrlParam(param) {
        var paramValue = $location.absUrl().match('[//?&]' + param + '=([^&#]*)($|[&#])');
        if (paramValue) {
            return paramValue[1];
        }
        return '';
    }

    function doRequest(method, url, params) {
        //vm.busy = true;
        $scope.properties.dataToSend.catSolicitudDeAdmisionInput.selectedIndex = $scope.properties.selectedIndex + 1;
        console.log($scope.properties.dataToSend.catSolicitudDeAdmisionInput);
        var req = {
            method: method,
            url: url,
            data: angular.copy($scope.properties.dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                gettimmer("fechavencimientoperiodo");
                //getTask();
                // closeModal($scope.properties.modalid);
                // $scope.properties.selectedIndex++;
            })
            .error(function(data, status) {
                blockUI.stop();
                swal("¡Error!", data.message, "error");
                console.log("Error al avanzar tarea")
                console.log(data);
                console.log(status);
            })
            .finally(function() {
                //vm.busy = false;
            });
    }

    // function getTask(){
    //     setTimeout(function(){ 
    //         var req = {
    //         method: 'GET',
    //         url: $scope.properties.urlCurrentTask
    //     };

    //     return $http(req)
    //         .success(function(data, status) {
    //             console.log("SUCCSES")
    //             console.log(data);
    //             $scope.properties.currentTask = data;
    //         })
    //         .error(function(data, status) {
    //             console.log("Error al avanzar tarea")
    //             console.log(data);
    //             console.log(status);
    //         })
    //         .finally(function() {
    //             //vm.busy = false;
    //         });

    //     }, 1000);

    // }

    function getTask() {

        var req = {
            method: 'GET',
            url: $scope.properties.urlCurrentTask
        };

        return $http(req)
            .success(function(data, status) {
                console.log("SUCCSES")
                console.log(data);
                if (data.length > 0) {
                    if (data[0].id === $scope.properties.taskId) {
                        if (cont === 100) {
                            location.reload();
                        } else {
                            getTask();
                            cont++;
                        }
                    } else {
                        $scope.properties.currentTask = data;
                        topFunction();
                        closeModal($scope.properties.modalid);
                        $scope.properties.disabled = false;
                        blockUI.stop();
                        $scope.properties.selectedIndex++;
                    }
                } else {
                    if (cont === 100) {
                        location.reload();
                    } else {
                        getTask();
                        cont++;
                    }
                }
            })
            .error(function(data, status) {
                console.log("Error al avanzar tarea")
                console.log(data);
                console.log(status);
            })
            .finally(function() {
                //vm.busy = false;
            });
    }

    function topFunction() {
        document.body.scrollTop = 0;
        document.documentElement.scrollTop = 0;
    }

    function gettimmer(variable) {

        blockUI.start();
        var req = {
            method: "GET",
            url: "../API/extension/RegistroRest?url=caseVariable&caseid=" + $scope.properties.catSolicitudDeAdmision.caseId + "&name=" + variable,
        };
        return $http(req).success(function(data, status) {
                var date = new Date();
                var datetosend = null;
                var datestr = "";
                if (variable === "fechaAbandonosolicitud") {
                    datetosend = new Date($scope.properties.catSolicitudDeAdmision.catPeriodo.fechaFin)
                    datetosend = new Date(datetosend.getTime() + ((((3600 * 1000) * 24) * 30) * 18))
                } else {
                    datetosend = new Date($scope.properties.catSolicitudDeAdmision.catPeriodo.fechaFin);
                }
                if (datetosend.getDay() == 0) {
                    datestr = "Sun";
                } else if (datetosend.getDay() == 1) {
                    datestr = "Mon";
                } else if (datetosend.getDay() == 2) {
                    datestr = "Tue";
                } else if (datetosend.getDay() == 3) {
                    datestr = "Wed";
                } else if (datetosend.getDay() == 4) {
                    datestr = "Thu";
                } else if (datetosend.getDay() == 5) {
                    datestr = "Fri";
                } else if (datetosend.getDay() == 6) {
                    datestr = "Sat";
                }

                if (datetosend.getMonth() == 0) {
                    datestr = datestr + " Jan";
                } else if (datetosend.getMonth() == 1) {
                    datestr = datestr + " Feb";
                } else if (datetosend.getMonth() == 2) {
                    datestr = datestr + " Mar";
                } else if (datetosend.getMonth() == 3) {
                    datestr = datestr + " Apr";
                } else if (datetosend.getMonth() == 4) {
                    datestr = datestr + " May";
                } else if (datetosend.getMonth() == 5) {
                    datestr = datestr + " Jun";
                } else if (datetosend.getMonth() == 6) {
                    datestr = datestr + " Jul";
                } else if (datetosend.getMonth() == 7) {
                    datestr = datestr + " Aug";
                } else if (datetosend.getMonth() == 8) {
                    datestr = datestr + " Sep";
                } else if (datetosend.getMonth() == 9) {
                    datestr = datestr + " Oct";
                } else if (datetosend.getMonth() == 10) {
                    datestr = datestr + " Nov";
                } else if (datetosend.getMonth() == 11) {
                    datestr = datestr + " Dec";
                }

                datestr = datestr + " " + datetosend.getDate().toString() + " 00:00:00 CST " + datetosend.getFullYear().toString();
                updateVariable(data.type, datestr, variable);
            })
            .error(function(data, status) {
                swal("¡Error!", data.message, "error");
            })
            .finally(function() {

            });

    }

    function updateVariable(type, fechatimmer, variable) {
        //$scope.showModal();
        var callUrl = "/API/extension/RegistroPut?url=caseVariable&caseVariable=" + $scope.properties.catSolicitudDeAdmision.caseId + "&name=" + variable
        blockUI.start();
        var req = {
            method: "PUT",
            url: callUrl, //"../API/bpm/caseVariable/"+$scope.properties.catSolicitudDeAdmision.caseId+"/"+variable,
            data: {
                "type": type,
                "value": fechatimmer
            }
        };
        return $http(req).success(function(data, status) {
                if (variable === "fechaAbandonosolicitud") {
                    getTask();
                } else {
                    gettimmer("fechaAbandonosolicitud");
                }
            })
            .error(function(data, status) {
                swal("¡Error!", data.message, "error");
            })
            .finally(function() {

            });
    }
}