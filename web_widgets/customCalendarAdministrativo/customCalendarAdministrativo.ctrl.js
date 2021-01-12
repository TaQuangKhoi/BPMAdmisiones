function($scope, $http, blockUI) {
    window.mobileCheck = function() {
        let check = false;
        (function(a) { if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4))) check = true; })(navigator.userAgent || navigator.vendor || window.opera);
        return check;
    };
    $scope.pantalla = "principal";
    var vm = this;
    var caseid = 0;
    $scope.resultado;
    $scope.lstDisponibilidad;
    $scope.inicio;
    $scope.finalizacion;
    $scope.editarS = false;
    $scope.editarP = false;
    var idFolio;
    $scope.disabledHasta = true;
    $scope.disabledDesde = true;
    $scope.hideHoraDesde = true;
    $scope.hideHoraHasta = true;
    $scope.disableCancelacionCita = true;
    $scope.color = "";
    $scope.sesion = {};
    scheduler.init('scheduler_here', new Date(), window.mobileCheck() ? "day" : "month");
    scheduler.config.drag_resize = false;
    scheduler.config.drag_create = false;
    scheduler.config.drag_highlight = false;
    scheduler.config.drag_lightbox = false;
    scheduler.config.drag_move = false;
    scheduler.config.drag_resize = false;
    scheduler.config.edit_on_create = false;
    scheduler.config.dblclick_create = false;
    scheduler.config.select = false;
    document.getElementById("dhx_minical_icon").innerHTML = "<i class='glyphicon glyphicon-calendar'></i>";
    //scheduler.parse([], "json"); //t

    /*Evento que se ejecuta al dar click!*/
    scheduler.attachEvent("onClick", function(id, e) {
        $scope.getSesion(id);
    })

    $scope.horaInicio = new Date();
    $scope.horaFin = new Date();
    $scope.fechaCalendario = "";
    $scope.resultadoMostrarAudiencia = {};
    $scope.resultadoMostrarAudiencia.idSala = 0;
    $scope.resultadoMostrarAudiencia.idTipo_Audiencia = 0;
    $scope.resultadoMostrarAudiencia.text = "";
    $scope.resultadoMostrarAudiencia.id = 0;
    $scope.eliminado = "false";
    $scope.prueba = {
        "campus": {
            "fechaImplementacion": null,
            "persistenceId": null,
            "orden": null,
            "isEliminado": null,
            "isEnabled": null,
            "urlAutorDatos": null,
            "descripcion": null,
            "id": null,
            "urlDatosVeridicos": null,
            "urlAvisoPrivacidad": null,
            "usuarioBanner": null,
            "fechaCreacion": null,
            "clave": null,
            "persistenceVersion": null,
            "grupoBonita": null
        },
        "codigo_postal": null,
        "sesion_pid": null,
        "ultimo_dia_inscripcion": null,
        "psicologos": [],
        "lugar": null,
        "cupo": null,
        "estado_pid": null,
        "estado": {
            "persistenceId": null,
            "orden": null,
            "isEliminado": null,
            "descripcion": null,
            "fechaCreacion": null,
            "caseId": null,
            "clave": null,
            "persistenceVersion": null,
            "usuarioCreacion": null,
            "pais": null
        },
        "pais": {
            "persistenceId": null,
            "orden": null,
            "isEliminado": null,
            "descripcion": null,
            "fechaCreacion": null,
            "caseId": null,
            "clave": null,
            "persistenceVersion": null,
            "usuarioCreacion": null
        },
        "tipo": {
            "iseliminado": null,
            "persistenceId": null,
            "descripcion": null,
            "persistenceVersion": null
        },
        "calle": null,
        "campus_pid": null,
        "aplicacion": null,
        "iseliminado": false,
        "registrados": 0,
        "salida": null,
        "colonia": null,
        "entrada": null,
        "presistenceId": null,
        "nombre": null,
        "numero_int": null,
        "persistenceVersion": null,
        "municipio": null,
        "pais_pid": null,
        "numero_ext": null,
        "duracion": null,
        "cambioDuracion": false

    }

    $scope.show_minical = function() {
        if (scheduler.isCalendarVisible()) {
            scheduler.destroyCalendar();
        } else {
            scheduler.renderCalendar({
                position: "dhx_minical_icon",
                date: scheduler._date,
                navigation: true,
                handler: function(date, calendar) {
                    scheduler.setCurrentView(date);
                    scheduler.destroyCalendar()
                }
            });
        }
    }


    var selectorFecha = $("div.dhx_cal_date").text();
    var fechaReporte = convertidorFechaCalendario(selectorFecha);
    console.log(fechaReporte);
    //Fución que se ejecuta cada que se cambia el DOM  en la clase dhx_cal_date
    $('div.dhx_cal_date').bind("DOMSubtreeModified", function() {
        selectorFecha = $("div.dhx_cal_date").text();
        fechaReporte = convertidorFechaCalendario(selectorFecha);
        console.log(fechaReporte);
        $scope.getCampus();
    });

    function convertidorFechaCalendario(selectorFecha) {
        var fecha = "";
        var year = selectorFecha.substring(selectorFecha.length - 4);
        if (selectorFecha.includes("Ene")) {
            fecha = year + "-01-01";
        }
        if (selectorFecha.includes("Feb")) {
            fecha = year + "-02-01";
        }
        if (selectorFecha.includes("Mar")) {
            fecha = year + "-03-01";
        }
        if (selectorFecha.includes("Abr")) {
            fecha = year + "-04-01";
        }
        if (selectorFecha.includes("May")) {
            fecha = year + "-05-01";
        }
        if (selectorFecha.includes("Jun")) {
            fecha = year + "-06-01";
        }
        if (selectorFecha.includes("Jul")) {
            fecha = year + "-07-01";
        }
        if (selectorFecha.includes("Ago")) {
            fecha = year + "-08-01";
        }
        if (selectorFecha.includes("Sep")) {
            fecha = year + "-09-01";
        }
        if (selectorFecha.includes("Oct")) {
            fecha = year + "-10-01";
        }
        if (selectorFecha.includes("Nov")) {
            fecha = year + "-11-01";
        }
        if (selectorFecha.includes("Dic")) {
            fecha = year + "-12-01";
        }

        return fecha;
    }

    $scope.modal = function(id, e) {
        $scope.pantallaAgregar = !$scope.pantallaAgregar;
    }
    $scope.tipos = ['Locales', 'Foráneos', 'Extranjeros'];
    $scope.preparatorias = [];
    $scope.estados = [];
    $scope.paises = [];
    $scope.ciudades = [];
    $scope.selectedCar = [];
    $scope.tipoPrueba = [];
    $scope.catcampus = [];
    $scope.psicologos = [];
    $scope.lstResponsables = [];
    $scope.pantallaCambiar = function(pantalla) {
        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //January is 0!
        var yyyy = today.getFullYear();
        if (dd < 10) {
            dd = '0' + dd
        }
        if (mm < 10) {
            mm = '0' + mm
        }

        today = yyyy + '-' + mm + '-' + dd;
        document.getElementById("fecha_inicio").setAttribute("min", today);
        document.getElementById("aplicacion").setAttribute("min", today);
        document.getElementById("ultimo").setAttribute("min", today);
        document.getElementById("inicio").setAttribute("min", today);
        document.getElementById("fin").setAttribute("min", today);

        $scope.pantalla = pantalla;
        try {
            $scope.editarS = ($scope.sesion.persistenceId == undefined) ? false : $scope.sesion.persistenceId > 0;
            $scope.editarP = ($scope.prueba.persistenceId == undefined) ? false : $scope.prueba.persistenceId > 0;
        } catch (error) {
            $scope.editarS = false;
            $scope.editarP = false;
        }

        if (pantalla == "principal") {
            $scope.loadCatalogs();
            $scope.getCampus();
            $("#aplicacion").val("");
            $("#ultimo").val("");
            $("#inicio").val("");
            $("#fin").val("");
            $("#fecha_inicio").val("");
            $scope.tipoPruebaSelected = {};
        }
        if (pantalla == "prueba") {
            ///bonita/API/extension/AnahuacRestGet?url=getCatTipoPrueba&p=0&c=10
            for (let index = 0; index < $scope.sesion.pruebas.length; index++) {
                const element = $scope.sesion.pruebas[index];
                if (element.tipo.descripcion == "Examen Psicométrico") {
                    for (let i = 0; i < $scope.tipoPrueba.length; i++) {
                        const ele = $scope.tipoPrueba[i];
                        if (ele.descripcion == "Examen Psicométrico" && $scope.prueba.tipo.descripcion != "Examen Psicométrico") {
                            $scope.tipoPrueba.splice(i, 1);
                        }
                    }
                } else if (element.tipo.descripcion == "College Board") {
                    for (let i = 0; i < $scope.tipoPrueba.length; i++) {
                        const ele = $scope.tipoPrueba[i];
                        if (ele.descripcion == "College Board" && $scope.prueba.tipo.descripcion != "College Board") {
                            $scope.tipoPrueba.splice(i, 1);
                        }
                    }
                } else if (element.tipo.descripcion == "Sesión Internacionalización") {
                    for (let i = 0; i < $scope.tipoPrueba.length; i++) {
                        const ele = $scope.tipoPrueba[i];
                        if (ele.descripcion == "Sesión Internacionalización" && $scope.prueba.tipo.descripcion != "Sesión Internacionalización") {
                            $scope.tipoPrueba.splice(i, 1);
                        }
                    }
                }

            }


            ///bonita/API/extension/AnahuacRestGet?url=getCatTipoPrueba&p=0&c=10

        }
    }
    $scope.agregarPruebaNueva = function(pantalla) {
        $scope.prueba = {
            "campus": {
                "fechaImplementacion": null,
                "persistenceId": null,
                "orden": null,
                "isEliminado": null,
                "isEnabled": null,
                "urlAutorDatos": null,
                "descripcion": null,
                "id": null,
                "urlDatosVeridicos": null,
                "urlAvisoPrivacidad": null,
                "usuarioBanner": null,
                "fechaCreacion": null,
                "clave": null,
                "persistenceVersion": null,
                "grupoBonita": null
            },
            "codigo_postal": null,
            "sesion_pid": null,
            "ultimo_dia_inscripcion": null,
            "psicologos": [],
            "lugar": null,
            "cupo": null,
            "estado_pid": null,
            "estado": {
                "persistenceId": null,
                "orden": null,
                "isEliminado": null,
                "descripcion": null,
                "fechaCreacion": null,
                "caseId": null,
                "clave": null,
                "persistenceVersion": null,
                "usuarioCreacion": null,
                "pais": null
            },
            "pais": {
                "persistenceId": null,
                "orden": null,
                "isEliminado": null,
                "descripcion": null,
                "fechaCreacion": null,
                "caseId": null,
                "clave": null,
                "persistenceVersion": null,
                "usuarioCreacion": null
            },
            "tipo": {
                "iseliminado": null,
                "persistenceId": null,
                "descripcion": null,
                "persistenceVersion": null
            },
            "calle": null,
            "campus_pid": null,
            "aplicacion": null,
            "iseliminado": false,
            "registrados": 0,
            "salida": null,
            "colonia": null,
            "entrada": null,
            "presistenceId": null,
            "nombre": null,
            "numero_int": null,
            "persistenceVersion": null,
            "municipio": null,
            "pais_pid": null,
            "numero_ext": null,
            "duracion": null,
            "cambioDuracion": false

        }
        $scope.displayEstado = "";
        $scope.displayPais = "";
        $scope.dblCP = false;
        $scope.dblCalle = false;
        $scope.dblCiudad = false;
        $scope.dblNext = false;
        $scope.dblNint = false;
        $scope.dblColonia = false;
        $("#aplicacion").val("");
        $("#ultimo").val("");
        $("#inicio").val("");
        $("#fin").val("");
        $scope.tipoPruebaSelected = {};
        $scope.pantallaCambiar(pantalla);
    }
    $scope.getLabel = function(desc) {
        return (desc == "Anáhuac Mayab") ? "Anáhuac Mérida" : (desc == "Anáhuac Xalapa") ? "Anáhuac Veracruz" : desc;
    }

    $scope.getResponsables = function() {
        $scope.setTipoPrueba();
        //&jsonData=${encodeURIComponent(JSON.stringify($scope.properties.filtroToSend))}

        var filtro = ($scope.prueba.cattipoprueba_pid == 1) ? [{ "columna": "ROL", "operador": "Igual a", "valor": "PSICOLOGO" }] : [{ "columna": "ROL", "operador": "Igual a", "valor": "ADMISIONES" }, { "columna": "ROL", "operador": "Igual a", "valor": "PASE DE LISTA" }]
        filtro.push({
            "columna": "GRUPO",
            "operador": "Igual a",
            "valor": $scope.properties.campusSelected.grupoBonita
        })
        doRequest("GET", `/bonita/API/extension/AnahuacRestGet?url=getUserBonita&p=0&c=9999&jsonData=${encodeURIComponent(JSON.stringify({ "estatusSolicitud": "Cat campus", "tarea": "Cat Campus", "lstFiltro": filtro, "type": "solicitudes_progreso", "usuario": "Administrador", "orderby": "", "orientation": "DESC", "limit": 999, "offset": 0 }))}`, null, null, null, function(datos, extra) {
            $scope.lstResponsables = datos.data;
        });
    }
    $scope.agregarResponsable = function() {
        if ($scope.psicologo != null) {
            $scope.psicologo.lstFechasDisponibles = $scope.lstFechasDisponibles;
            $scope.prueba.psicologos.push(angular.copy({...$scope.psicologo, licenciaturas: "" }));

            for (let index = 0; index < $scope.lstResponsables.length; index++) {
                const element = $scope.lstResponsables[index];
                if (element.id == $scope.psicologo.id) {
                    $scope.lstResponsables.splice(index, 1)
                }

            }
            $scope.psicologo = null;
            $scope.setCupoValue();
        }

    }
    $scope.eliminarLicenciatura = function(indice) {
        var a = $scope.pselected.licenciaturas.split(',');
        a.splice(indice, 1);
        $scope.pselected.licenciaturas = a.join();
    }
    $scope.setCupoValue = function() {
        $scope.prueba.cupo = 0;
        for (let index = 0; index < $scope.prueba.psicologos.length; index++) {
            const element = $scope.prueba.psicologos[index];
            for (let j = 0; j < element.lstFechasDisponibles.length; j++) {
                const el2 = element.lstFechasDisponibles[j];
                if (el2.disponible) {
                    $scope.prueba.cupo++;
                }
            }
        }
    }


    $scope.eliminarResponsable = function(responsable) {
            Swal.fire({
                title: `¿Está seguro que desea eliminar responsable ${responsable.firstname + ' ' + responsable.lastname}?`,
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Continuar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    responsable.iseliminado = true;
                    var a = angular.copy(responsable)
                    a.iseliminado = false;
                    $scope.lstResponsables.push(a)
                    $scope.$apply();
                    Swal.fire({
                        icon: 'success',
                        title: 'Correcto',
                        text: `Responsable ${responsable.firstname + ' ' + responsable.lastname + ' ' } eliminado correctamente`,
                    })
                }
            })
        }
        /**
         * Execute a get/post request to an URL
         * It also bind custom data from success|error to a data
         * @return {void}
         */
    function doRequest(method, url, params, dataToSend, extra, callback) {
        vm.busy = true;
        blockUI.start();
        var req = {
            method: method,
            url: url,
            data: angular.copy(dataToSend),
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                callback(data, extra)
            })
            .error(function(data, status) {
                console.error(data);
            })
            .finally(function() {
                vm.busy = false;
                blockUI.stop();
            });
    }
    $scope.isMedicina = function(valor) {
        $scope.sesion.ismedicina = valor;
    }
    $scope.psicologo = null;

    $scope.confirmarEliminarPruebas = function(prueba) {
        $scope.prueba = prueba;
        Swal.fire({
            title: `¿Está seguro que desea eliminar prueba ${prueba.nombre}?`,
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Continuar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                $scope.prueba.iseliminado = true;
                $scope.$apply();
                Swal.fire({
                    icon: 'success',
                    title: 'Correcto',
                    text: `Prueba ${$scope.prueba.nombre} eliminada correctamente`,
                })
            }
        })
    }
    $scope.tipoPruebaSelected = {};
    $scope.setTipoPrueba = function() {
        console.log("tipopruebaselected");
        $scope.prueba.cattipoprueba_pid = $scope.tipoPruebaSelected.persistenceId;
        $scope.prueba.tipo = angular.copy($scope.tipoPruebaSelected);
    }
    $scope.agregarPrueba = function() {
        if (!$scope.validarPrueba()) {
            if ($scope.prueba.persistenceId > 0) {
                for (let index = 0; index < $scope.sesion.pruebas.length; index++) {
                    const element = $scope.sesion.pruebas[index];
                    if (element.persistenceId == $scope.prueba.persistenceId) {
                        $scope.sesion.pruebas[index] = angular.copy($scope.prueba);
                    }

                }
            } else {
                var push = true;
                for (let index = 0; index < $scope.sesion.pruebas.length; index++) {
                    const element = $scope.sesion.pruebas[index];
                    if (element.persistenceId == $scope.prueba.persistenceId) {
                        $scope.sesion.pruebas[index] = angular.copy($scope.prueba);
                        push = false
                    }

                }
                if (push) {
                    $scope.sesion.pruebas.push(angular.copy($scope.prueba));
                }

            }
            //$scope.pantalla = 'sesion';
            console.log(JSON.stringify($scope.sesion));
            $scope.insertSesion($scope.sesion.borrador);
        }

    }
    $scope.setHora = function(entrada) {
        if (entrada) {
            $scope.prueba.entrada = $("#inicio").val();
            $("#fin").val("");
            $scope.prueba.salida = null;
        } else {
            //convert both time into timestamp
            var stt = new Date($scope.sesion.fecha_inicio + " " + $("#inicio").val());
            stt = stt.getTime();

            var endt = new Date($scope.sesion.fecha_inicio + " " + $("#fin").val());
            endt = endt.getTime();

            if (endt > stt) {
                $scope.prueba.salida = $("#fin").val();
            } else {
                Swal.fire(
                    'Incorrecto',
                    'La hora inicio no puede ser igual o mayor a la hora de finalización',
                    'info'
                )
                $("#fin").val("");
            }

        }
    }
    $scope.setFecha = function() {

        $scope.prueba.aplicacion = $("#aplicacion").val();
        var now = new Date(new Date($scope.prueba.aplicacion).setDate(new Date($scope.prueba.aplicacion).getDate() - 4))

        var day = ("0" + now.getDate()).slice(-2);
        var month = ("0" + (now.getMonth() + 1)).slice(-2);

        var today = now.getFullYear() + "-" + (month) + "-" + (day);
        $("#ultimo").val(today);
        $scope.prueba.ultimo_dia_inscripcion = $("#ultimo").val()
    }
    $scope.setFechaUltimo = function() {
        $scope.prueba.ultimo_dia_inscripcion = $("#ultimo").val();
    }
    $scope.setFechaInicio = function() {
        $scope.sesion.fecha_inicio = $("#fecha_inicio").val();
    }
    $scope.cantidadEntrevistas = 0;
    $scope.lstFechasDisponibles = [];
    $scope.lstGestionEscolar = [];
    $scope.setEntrevistas = function() {

        $scope.prueba.cambioDuracion = true;

        $scope.lstFechasDisponibles = [];
        var inicio = new Date(`${$scope.prueba.aplicacion+" "+$scope.prueba.entrada}`)
        var fin = new Date(`${$scope.prueba.aplicacion+" "+$scope.prueba.salida}`)
        var minutes = Math.floor((fin - inicio) / 60000);
        $scope.cantidadEntrevistas = (minutes / $scope.prueba.duracion)
        for (let index = 0; index < $scope.cantidadEntrevistas; index++) {
            var now = new Date(new Date($scope.prueba.aplicacion + " " + $scope.prueba.entrada + ":00").getTime() + (((index + 1) * parseInt($scope.prueba.duracion)) * 60000))
            var day = ("0" + now.getDate()).slice(-2);
            var month = ("0" + (now.getMonth() + 1)).slice(-2);
            var m = ("0" + now.getMinutes()).slice(-2);
            var hour = ("0" + now.getHours()).slice(-2);
            var today = (hour) + ":" + (m);

            var horario = ((index == 0) ? $scope.prueba.entrada : $scope.lstFechasDisponibles[index - 1].horario.split(" - ")[1]) + " - " + (($scope.lstFechasDisponibles.length == (index + 1)) ? prueba.salida : today)
            var objFecha = { "horario": `${horario}`, "disponible": true }
            $scope.lstFechasDisponibles.push(objFecha);
        }
        for (let index = 0; index < $scope.prueba.psicologos.length; index++) {
            const element = $scope.prueba.psicologos[index];
            element.lstFechasDisponibles = angular.copy($scope.lstFechasDisponibles)

        }
        $scope.setCupoValue();
        console.log($scope.lstFechasDisponibles);
    }
    $scope.pselected = {};
    $scope.configurarResponsable = function(responsable) {
        $scope.pselected = responsable;
        $scope.getCatGestionEscolar($scope.pselected.grupo);
        $('#modal-sesiones-psicologo').modal('show');
    }
    $scope.getCatGestionEscolar = function(campus) {
        //&jsonData=${encodeURIComponent(JSON.stringify($scope.properties.filtroToSend))}
        var filtro = [{ "columna": "CAMPUS", "operador": "Igual a", "valor": campus }];
        doRequest("GET", `/bonita/API/extension/AnahuacRestGet?url=getCatGestionEscolar&p=0&c=9999&jsonData=${encodeURIComponent(JSON.stringify({ "estatusSolicitud": "Cat campus", "tarea": "Cat Campus", "lstFiltro": filtro, "type": "solicitudes_progreso", "usuario": "Administrador", "orderby": "", "orientation": "DESC", "limit": 999, "offset": 0 }))}`, null, null, null, function(datos, extra) {
            $scope.lstGestionEscolar = datos.data;
        });
    }
    $scope.insertSesion = function(borrador) {
        if (!$scope.validarSesion()) {
            $scope.sesion.borrador = borrador;
            Swal.fire({
                title: `¿Está seguro que desea gurardar la sesión ${$scope.sesion.nombre}?`,
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Continuar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    doRequest("POST", "/bonita/API/extension/AnahuacRest?url=insertSesion&p=0&c=10", null, $scope.sesion, null, function(datos, extra) {
                        $scope.sesion = datos.data[0];

                        $scope.getSesion($scope.sesion.persistenceId);
                        $scope.loadCatalogs();
                        $scope.pantalla = "sesion"
                        Swal.fire({
                            icon: 'success',
                            title: 'Correcto',
                            text: `Sesion ${$scope.sesion.nombre} guardada correctamente`,
                        })
                    })

                } else {

                }
            })
        }

    }
    $scope.$watch('properties.campusSelected', function(value) {
        if (angular.isDefined(value) && value !== null) {
            var filtro = {
                "columna": "CAMPUS",
                "operador": "Igual a",
                "valor": value.persistenceId
            }
            $scope.dataToSend = {
                "lstFiltro": []
            }
            $scope.dataToSend.lstFiltro.push(filtro);
            $scope.sesion.campus_pid = value.persistenceId;
            $scope.getCampus()
        }
    });
    $scope.getCampus = function() {
        doRequest("POST", "/bonita/API/extension/AnahuacRest?url=getSesionesCalendario&p=0&c=10&fecha=" + fechaReporte, null, $scope.dataToSend, null, function(datos, extra) {
            scheduler.clearAll();
            scheduler.parse(datos.data, "json");
        })
    }
    $scope.getSesion = function(sesionid) {
        doRequest("GET", "/bonita/API/extension/AnahuacRestGet?url=getSesion&p=0&c=10&sessionid=" + sesionid, null, $scope.dataToSend, null, function(datos, extra) {
            $scope.sesion = datos.data[0]
            $("#fecha_inicio").val($scope.sesion.fecha_inicio);
            $scope.pantallaCambiar('sesion');

        })
    }
    $scope.editarPrueba = function(pruebaSelected) {
        $scope.prueba = angular.copy(pruebaSelected);
        $("#aplicacion").val($scope.prueba.aplicacion);
        $("#ultimo").val($scope.prueba.ultimo_dia_inscripcion);
        $("#inicio").val($scope.prueba.entrada);
        $("#fin").val($scope.prueba.salida);
        for (var i = 0; i < $scope.tipoPrueba.length; i++) {
            var tipos = $scope.tipoPrueba[i];
            if (tipos.persistenceId == $scope.prueba.cattipoprueba_pid) {
                $scope.tipoPruebaSelected = tipos
            }
        }
        for (let index = 0; index < $scope.catcampus.length; index++) {
            const element = $scope.catcampus[index];
            if (element.persistenceId == $scope.prueba.campus_pid) {
                $scope.campusDirSelected = element;
                $scope.setDirCampus()
            }
        }
        $scope.getResponsables();
        $scope.pantallaCambiar('prueba');
    }
    $scope.loadCatalogs = function() {
        doRequest("GET", "/bonita/API/extension/AnahuacRestGet?url=getCatBachilleratos&p=0&c=9999", null, null, null, function(datos, extra) {
            $scope.preparatorias = datos;
            doRequest("GET", "/bonita/API/bdm/businessData/com.anahuac.catalogos.CatEstados?q=find&p=0&c=999", null, null, null, function(datos, extra) {
                $scope.estados = datos;
                doRequest("POST", `/bonita/API/extension/AnahuacRest?url=getCatPais&p=0&c=100`, null, { "estatusSolicitud": "Solicitud en progreso", "tarea": "Llenar solicitud", "lstFiltro": [], "type": "solicitudes_progreso", "usuario": "Administrador", "orderby": "CLAVE", "orientation": "ASC", "limit": 999, "offset": 0 }, null, function(datos, extra) {
                    $scope.paises = datos.data;
                    doRequest("GET", `/bonita/API/extension/AnahuacRestGet?url=getCatTipoPrueba&p=0&c=9999&jsonData=%7B"estatusSolicitud"%3A"Solicitud%20en%20progreso"%2C"tarea"%3A"Llenar%20solicitud"%2C"lstFiltro"%3A%5B%5D%2C"type"%3A"solicitudes_progreso"%2C"usuario"%3A0%2C"orderby"%3A""%2C"orientation"%3A"DESC"%2C"limit"%3A999%2C"offset"%3A0%7D`, null, null, null, function(datos, extra) {
                        $scope.tipoPrueba = datos.data;
                        doRequest("POST", `/bonita/API/extension/AnahuacRest?url=getCatCampus&p=0&c=100`, null, { "estatusSolicitud": "Cat campus", "tarea": "Cat Campus", "lstFiltro": [], "type": "solicitudes_progreso", "usuario": "Administrador", "orderby": "", "orientation": "DESC", "limit": 999, "offset": 0 }, null, function(datos, extra) {
                            $scope.catcampus = datos.data;
                            doRequest("GET", `/bonita/API/extension/AnahuacRestGet?url=getCatPsicologo&p=0&c=9999&jsonData=%7B"estatusSolicitud"%3A"Solicitud%20en%20progreso"%2C"tarea"%3A"Llenar%20solicitud"%2C"lstFiltro"%3A%5B%5D%2C"type"%3A"solicitudes_progreso"%2C"usuario"%3A0%2C"orderby"%3A""%2C"orientation"%3A"DESC"%2C"limit"%3A999%2C"offset"%3A0%7D`, null, null, null, function(datos, extra) {
                                $scope.psicologos = datos.data;
                                doRequest("GET", `/bonita/API/bpm/case?c=25&d=processDefinitionId&d=started_by&d=startedBySubstitute&f=name=CatCiudad&n=activeFlowNodes&n=failedFlowNodes&o=id+DESC&p=0&`, null, null, null, function(datos, extra) {
                                    caseid = datos[0].id;
                                    ///API/bdm/businessData/com.anahuac.catalogos.CatCiudad?q=getCatCiudadPaisByCaso&p=0&c=1000&f=caseId={{getCaseIdCatCiudad[0].id}}
                                    doRequest("GET", `/bonita/API/bdm/businessData/com.anahuac.catalogos.CatCiudad?q=getCatCiudadPaisByCaso&p=0&c=1000&f=caseId=${caseid}`, null, null, null, function(datos, extra) {
                                        var aux = []
                                        for (let index = 0; index < $scope.paises.length; index++) {
                                            const element = $scope.paises[index];
                                            for (let j = 0; j < datos.length; j++) {
                                                const element2 = datos[j];
                                                if (element.descripcion == element2.pais) {
                                                    var contains = true;
                                                    for (let k = 0; k < aux.length; k++) {
                                                        const element3 = aux[k];
                                                        if (element3.descripcion == element.descripcion) {
                                                            contains = false
                                                        }

                                                    }
                                                    if (contains) {
                                                        aux.push(element);
                                                    }
                                                }
                                            }
                                        }
                                        $scope.paises = aux;
                                        doRequest("GET", `/bonita/API/bdm/businessData/com.anahuac.catalogos.CatCiudad?q=getCatCiudadEstadoByCaso&p=0&c=1000&f=caseId=${caseid}`, null, null, null, function(datos, extra) {
                                            var aux = []
                                            for (let index = 0; index < $scope.estados.length; index++) {
                                                const element = $scope.estados[index];
                                                for (let j = 0; j < datos.length; j++) {
                                                    const element2 = datos[j];
                                                    if (element.descripcion == element2.estado) {
                                                        var contains = true;
                                                        for (let k = 0; k < aux.length; k++) {
                                                            const element3 = aux[k];
                                                            if (element3.descripcion == element.descripcion) {
                                                                contains = false
                                                            }

                                                        }
                                                        if (contains) {
                                                            aux.push(element);
                                                        }

                                                    }
                                                }
                                            }
                                            $scope.estados = aux;
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });

        });
    }
    $scope.agregarNuevaSesion = function(pantalla) {
        $scope.sesion = {
            "bachillerato_pid": null,
            "ciudad_pid": null,
            "persistenceId": null,
            "ismedicina": null,
            "fecha_inicio": null,
            "tipo": null,
            "preparatoria": {
                "persistenceId": null,
                "fechaImportacion": null,
                "isEliminado": null,
                "isEnabled": null,
                "descripcion": null,
                "perteneceRed": null,
                "usuarioBanner": null,
                "fechaCreacion": null,
                "clave": null,
                "persistenceVersion": null,
                "estado": null,
                "ciudad": null,
                "pais": null
            },
            "descripcion": null,
            "nombre": null,
            "estado_pid": null,
            "pruebas": [],
            "persistenceVersion": null,
            "borrador": null,
            "pais_pid": null,
            "estado": {
                "persistenceId": null,
                "orden": null,
                "isEliminado": null,
                "descripcion": null,
                "fechaCreacion": null,
                "caseId": null,
                "clave": null,
                "persistenceVersion": null,
                "usuarioCreacion": null,
                "pais": null
            },
            "pais": {
                "persistenceId": null,
                "orden": null,
                "isEliminado": null,
                "descripcion": null,
                "fechaCreacion": null,
                "caseId": null,
                "clave": null,
                "persistenceVersion": null,
                "usuarioCreacion": null
            },
            "campus_pid": $scope.sesion.campus_pid
        }
        $scope.pantallaCambiar(pantalla);
    }
    $scope.$watch('sesion.pais_pid', function(value) {
        if (angular.isDefined(value) && value !== null) {
            var pais = "";
            for (let index = 0; index < $scope.paises.length; index++) {
                const element = $scope.paises[index];
                if (element.persistenceId == value) {
                    pais = element.descripcion
                }

            }
            doRequest("GET", `/API/bdm/businessData/com.anahuac.catalogos.CatCiudad?q=getCiudadesByPais&f=pais=${pais}&p=0&c=1000&f=caseId=${caseid}`, null, null, null, function(datos) {
                $scope.ciudades = datos;
            })
        } else {
            $scope.sesion.ciudad_pid = 0;
            $scope.ciudades = [];

        }
    });
    $scope.$watch('sesion.estado_pid', function(value) {
        if (angular.isDefined(value) && value !== null) {
            var estado = "";
            for (let index = 0; index < $scope.estados.length; index++) {
                const element = $scope.estados[index];
                if (element.persistenceId == value) {
                    estado = element.descripcion
                }

            }
            doRequest("GET", `/API/bdm/businessData/com.anahuac.catalogos.CatCiudad?q=getCatCiudadByEstado&f=estado=${estado}&p=0&c=1000&f=caseId=${caseid}`, null, null, null, function(datos) {

                $scope.ciudades = datos;
            })
        } else {
            $scope.sesion.ciudad_pid = null;
            $scope.ciudades = [];

        }
    });
    $scope.codigoPostalBtn = false;
    $scope.$watch('prueba.pais_pid', function(value) {
        if (angular.isDefined(value) && value !== null) {
            var pais = "";
            for (let index = 0; index < $scope.paises.length; index++) {
                const element = $scope.paises[index];
                if (element.persistenceId == value) {
                    pais = element.descripcion
                }

            }
            if (pais == "México") {
                $scope.codigoPostalBtn = true;
            } else {
                $scope.codigoPostalBtn = false;
            }
            doRequest("GET", `/API/bdm/businessData/com.anahuac.catalogos.CatCiudad?q=getCiudadesByPais&f=pais=${pais}&p=0&c=1000&f=caseId=${caseid}`, null, null, null, function(datos) {
                $scope.ciudades = datos;
            })
        } else {
            $scope.codigoPostalBtn = false;
        }
    });
    $scope.$watch('prueba.estado_pid', function(value) {
        if (angular.isDefined(value) && value !== null) {
            var estado = "";
            for (let index = 0; index < $scope.estados.length; index++) {
                const element = $scope.estados[index];
                if (element.persistenceId == value) {
                    estado = element.descripcion
                }

            }
            doRequest("GET", `/API/bdm/businessData/com.anahuac.catalogos.CatCiudad?q=getCatCiudadByEstado&f=estado=${estado}&p=0&c=1000&f=caseId=${caseid}`, null, null, null, function(datos) {

                $scope.ciudades = datos;
            })
        }
    });
    $scope.lstCodigos = []
    $scope.buscarCodigo = function(codigo_postal) {
        doRequest("GET", `/API/bdm/businessData/com.anahuac.catalogos.CatCodigoPostal?q=findByCodigoPostal&p=0&c=1000&f=codigoPostal=${codigo_postal}`, null, null, null, function(datos, extra) {
            console.log(datos)
            $scope.lstCodigos = datos;
            if ($scope.lstCodigos.length > 0) {
                $("#modal-codigo-postal").modal("show");
            }
        })
    }
    $scope.setCodigoSelected = function(codigo) {
        $scope.displayEstado = "";
        $scope.displayPais = "";
        $scope.dblCP = false;
        $scope.dblCalle = false;
        $scope.dblCiudad = false;
        $scope.dblNext = false;
        $scope.dblNint = false;
        $scope.dblColonia = false;
        for (let index = 0; index < $scope.estados.length; index++) {
            const element = $scope.estados[index];
            if (element.descripcion == codigo.estado) {
                $scope.displayEstado = element.descripcion;
                $scope.prueba.estado_pid = element.persistenceId;
            }

        }
        for (let index = 0; index < $scope.paises.length; index++) {
            const element = $scope.paises[index];
            if (element.persistenceId == $scope.prueba.pais_pid) {
                $scope.displayPais = element.descripcion;
            }

        }
        if (codigo.ciudad != null) {
            $scope.dblCiudad = true;
            $scope.prueba.municipio = codigo.ciudad;
        }
        if (codigo.municipio != null) {
            $scope.dblCiudad = true;
            $scope.prueba.municipio = codigo.municipio;
        }
        if (codigo.asentamiento != null) {
            $scope.dblColonia = true;
        }
        $scope.prueba.ciudad = codigo.ciudad
        $scope.prueba.municipio = codigo.municipio;
        $scope.prueba.colonia = codigo.asentamiento;
        $("#modal-codigo-postal").modal("hide");
    }
    $scope.validarPrueba = function() {
        var error = false
        var sweet = { "titulo": "", "texto": "" }
        if ($scope.prueba == undefined) {
            error = true;
            sweet.titulo = "Incorrecto";
            sweet.texto = 'No es posible agregar prueba sin capturar en el formulario'
        } else if ($scope.prueba.nombre == null || $scope.prueba.nombre == undefined || $scope.prueba.nombre.trim() == "") {
            error = true;
            sweet.titulo = "Título de la prueba";
            sweet.texto = 'Favor de capturar "Título de la prueba"'
        } else if ($scope.prueba.cattipoprueba_pid == null || $scope.prueba.cattipoprueba_pid == undefined || $scope.prueba.cattipoprueba_pid == 0) {
            error = true;
            sweet.titulo = "Tipo de prueba";
            sweet.texto = 'Favor de capturar "Tipo de prueba"'
        } else if ($scope.prueba.descripcion == null || $scope.prueba.descripcion == undefined || $scope.prueba.descripcion.trim() == "") {
            error = true;
            sweet.titulo = "Descripción de la prueba";
            sweet.texto = 'Favor de capturar "Descripción de la prueba"'
        } else if ($scope.prueba.aplicacion == null || $scope.prueba.aplicacion == undefined || $scope.prueba.aplicacion.trim() == "") {
            error = true;
            sweet.titulo = "Fecha de aplicación";
            sweet.texto = 'Favor de capturar "Fecha de aplicación"'
        } else if ($scope.prueba.entrada == null || $scope.prueba.entrada == undefined || $scope.prueba.entrada.trim() == "") {
            error = true;
            sweet.titulo = "Hora de inicio";
            sweet.texto = 'Favor de capturar "Hora de inicio"'
        } else if ($scope.prueba.salida == null || $scope.prueba.salida == undefined || $scope.prueba.salida.trim() == "") {
            error = true;
            sweet.titulo = "Hora de finalización";
            sweet.texto = 'Favor de capturar "Hora de finalización"'
        } else if ($scope.prueba.ultimo_dia_inscripcion == null || $scope.prueba.ultimo_dia_inscripcion == undefined || $scope.prueba.ultimo_dia_inscripcion.trim() == "") {
            error = true;
            sweet.titulo = "Último día para inscripción";
            sweet.texto = 'Favor de capturar "Último día para inscripción"'
        } else if ($scope.prueba.lugar == null || $scope.prueba.lugar == undefined || $scope.prueba.lugar.trim() == "") {
            error = true;
            sweet.titulo = "Lugar";
            sweet.texto = 'Favor de capturar "Lugar"'
        } else if ($scope.prueba.cupo == null || $scope.prueba.cupo == undefined || $scope.prueba.cupo == 0) {
            error = true;
            sweet.titulo = "Cupo";
            sweet.texto = 'Cupo debe de ser mayor a 0'
        } else if ($scope.prueba.colonia == null || $scope.prueba.colonia == undefined || $scope.prueba.colonia.trim() == "") {
            error = true;
            sweet.titulo = "Colonia";
            sweet.texto = 'Favor de capturar "Colonia"'
        } else if ($scope.prueba.cattipoprueba_pid == 1) {
            if ($scope.prueba.duracion == null || $scope.prueba.duracion == undefined || $scope.prueba.duracion == "") {
                error = true;
                sweet.titulo = "Duración de las entrevistas(min)";
                sweet.texto = 'Favor de capturar "Duración de las entrevistas(min)"'
            }

        }

        if (error) {
            Swal.fire(
                sweet.titulo,
                sweet.texto,
                'info'
            )
        }
        return error;
    }
    $scope.validarSesion = function() {
        var error = false
        var sweet = { "titulo": "", "texto": "" }

        if ($scope.sesion.nombre == undefined || $scope.sesion.nombre == null || $scope.sesion.nombre.trim() == "") {
            error = true;
            sweet.titulo = "Nombre de la sesión";
            sweet.texto = 'Favor de capturar "Nombre de la sesión"'
        } else if ($scope.sesion.descripcion == undefined || $scope.sesion.descripcion == null || $scope.sesion.descripcion.trim() == "") {
            error = true;
            sweet.titulo = "Descripción de la sesión";
            sweet.texto = 'Favor de capturar "Descripción de la sesión"'
        } else if ($scope.sesion.fecha_inicio == undefined || $scope.sesion.fecha_inicio == null || $scope.sesion.fecha_inicio.trim() == "") {
            error = true;
            sweet.titulo = "Fecha de inicio de la sesión";
            sweet.texto = 'Favor de capturar "Fecha de inicio de la sesión"'
        } else if ($scope.sesion.tipo == undefined || $scope.sesion.tipo == null || $scope.sesion.tipo.trim() == "") {
            error = true;
            sweet.titulo = "Residencia";
            sweet.texto = 'Favor de capturar "Residencia"'
        }


        if (error) {
            Swal.fire(
                sweet.titulo,
                sweet.texto,
                'info'
            )
        }
        return error;
    }
    $scope.validarBtnGuardarPublicar = function() {
        var entrevista = false;
        var college = false;
        var psicometrico = false;
        try {
            for (let index = 0; index < $scope.sesion.pruebas.length; index++) {
                const element = $scope.sesion.pruebas[index];
                if (element.tipo.descripcion == "Examen Psicométrico") {
                    psicometrico = true;
                }
                if (element.tipo.descripcion == "College Board") {
                    college = true;
                }
                if (element.tipo.descripcion == "Entrevista") {
                    entrevista = true;
                }

            }
        } catch (error) {
            entrevista = false;
            college = false;
            psicometrico = false;
        }

        return entrevista && college & psicometrico;
    }
    $scope.setDisponibleOcupado = function(disponibles) {
        if (!disponibles.ocupado) {
            disponibles.disponible = !disponibles.disponible;
            setCupoValue();
        } else {
            Swal.fire(
                "Imposible",
                "No es posible modificar la disponibilidad ya que actualmente hay un alumno registrado en el horario seleccionado",
                'info'
            )
        }

    }
    $scope.checkRegistrado = function(psi) {
        var registrados = false;
        for (let index = 0; index < psi.lstFechasDisponibles.length; index++) {
            const element = psi.lstFechasDisponibles[index];
            if (element.ocupado) {
                registrados = true;
            }

        }
        return registrados;
    }
    $scope.campusDirSelected = {}
    $scope.displayEstado = "";
    $scope.displayPais = "";
    $scope.dblCP = false;
    $scope.dblCalle = false;
    $scope.dblCiudad = false;
    $scope.dblNext = false;
    $scope.dblNint = false;
    $scope.dblColonia = false;
    $scope.setDirCampus = function() {
        try {
            if ($scope.campusDirSelected.persistenceId > 0) {
                $scope.dblCP = true;
                $scope.dblCalle = true;
                $scope.dblCiudad = true;
                $scope.dblNext = true;
                $scope.dblNint = true;
                $scope.dblColonia = true;

                $scope.prueba.campus_pid = $scope.campusDirSelected.persistenceId
                $scope.prueba.calle = $scope.campusDirSelected.calle
                $scope.prueba.colonia = $scope.campusDirSelected.colonia
                $scope.prueba.numero_ext = $scope.campusDirSelected.numeroExterior
                $scope.prueba.numero_int = $scope.campusDirSelected.numeroInterior
                $scope.prueba.codigo_postal = $scope.campusDirSelected.codigoPostal

                $scope.prueba.municipio = $scope.campusDirSelected.municipio
                for (let index = 0; index < $scope.estados.length; index++) {
                    const element = $scope.estados[index];
                    if (element.persistenceId == $scope.campusDirSelected.estado_pid) {
                        $scope.displayEstado = element.descripcion;
                    }
                }
                for (let index = 0; index < $scope.paises.length; index++) {
                    const element = $scope.paises[index];
                    if (element.persistenceId == $scope.campusDirSelected.pais_pid) {
                        $scope.displayPais = element.descripcion;
                    }
                }
            } else {
                $scope.displayEstado = "";
                $scope.displayPais = "";
                $scope.dblCP = false;
                $scope.dblCalle = false;
                $scope.dblCiudad = false;
                $scope.dblNext = false;
                $scope.dblNint = false;
                $scope.dblColonia = false;
            }
        } catch (error) {
            $scope.displayEstado = "";
            $scope.displayPais = "";
            $scope.dblCP = false;
            $scope.dblCalle = false;
            $scope.dblCiudad = false;
            $scope.dblNext = false;
            $scope.dblNint = false;
            $scope.dblColonia = false;
        }




    }
    $scope.loadCatalogs();
    var hidden = document.getElementsByClassName("oculto");
    hidden[0].classList.add("invisible")
}