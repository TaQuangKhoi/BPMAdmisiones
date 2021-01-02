function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    $scope.action = function() {
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        var localS = localStorage;
        console.log("boton de siguiente");
        console.log($scope.properties.collageBoard);
        if ($scope.properties.selectedIndex === 0) {
            console.log("validar 0");
            if ($scope.properties.catSolicitudDeAdmision.catCampus.persistenceId_string === "") {
                swal("Campus!", "Debe seleccionar un campus donde cursara tus estudios!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar === null) {
                swal("Licenciatura!", "Debe seleccionar una licenciatura!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar.propedeutico) {
                if ($scope.properties.catSolicitudDeAdmision.catPropedeutico === null) {
                    swal("Examen propedéutico!", "Debe seleccionar un periodo donde cursara sus estudios!", "warning");
                } else {
                    if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                        swal("Periodo!", "Debe seleccionar un periodo donde cursara sus estudios!", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                        swal("Lugar de examen!", "Debe seleccionar un lugar donde cursara sus estudios!", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                        if ($scope.properties.lugarexamen === "En un estado") {
                            if ($scope.properties.catSolicitudDeAdmision.catEstadoExamen === null) {
                                swal("Lugar de examen!", "Debe seleccionar un estado donde realizara el examen!", "warning");
                            } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                                swal("Lugar de examen!", "Debe seleccionar una ciudad donde realizara el examen!", "warning");
                            } else if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                $scope.properties.selectedIndex++;
                            }
                        } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                            if ($scope.properties.catSolicitudDeAdmision.catPaisExamen === null) {
                                swal("Lugar de examen!", "Debe seleccionar un país donde realizara el examen!", "warning");
                            } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                                swal("Lugar de examen!", "Debe seleccionar una ciudad donde realizara el examen!", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                    localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                    $scope.properties.selectedIndex++;
                                }
                            }
                        } else {
                            $scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                            $scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                $scope.properties.selectedIndex++;
                            }
                        }

                    } else {
                        swal("Lugar de examen!", "Debe seleccionar un lugar donde realizara el examen!", "warning");
                    }
                }
            } else if ($scope.properties.catSolicitudDeAdmision.catPeriodo === null) {
                swal("Periodo!", "Debe seleccionar un periodo donde cursara sus estudios!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                swal("Lugar de examen!", "Debe seleccionar un lugar donde cursara sus estudios!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.catSolicitudDeAdmision.catEstadoExamen === null) {
                        swal("Lugar de examen!", "Debe seleccionar un estado donde realizara el examen!", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                        swal("Lugar de examen!", "Debe seleccionar una ciudad donde realizara el examen!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                            $scope.properties.selectedIndex++;
                        }
                    }
                } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                    if ($scope.properties.catSolicitudDeAdmision.catPaisExamen === null) {
                        swal("Lugar de examen!", "Debe seleccionar un país donde realizara el examen!", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                        swal("Lugar de examen!", "Debe seleccionar una ciudad donde realizara el examen!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                            $scope.properties.selectedIndex++;
                        }
                    }
                } else {
                    $scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                    $scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                        localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                        $scope.properties.selectedIndex++;
                    }
                }

            } else {
                swal("Lugar de examen!", "Debe seleccionar un lugar donde realizara el examen!", "warning");
            }
        } else if ($scope.properties.selectedIndex === 1) {

            console.log("validar 1");
            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.selectedIndex++;
            }*/
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            }else if ($scope.properties.catSolicitudDeAdmision.primerNombre === "") {
                swal("Nombre!", "Debe ingresar su primer nombre!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.apellidoPaterno === "") {
                swal("Apellido paterno!", "Debe ingresar su apellido paterno!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.apellidoMaterno === "") {
                swal("Apellido materno!", "Debe ingresar su apellido materno!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.correoElectronico === "") {
                swal("Correo electrónico!", "Debe ingresar su correo electrónico!", "warning");
            } else if(!re.test(String($scope.properties.catSolicitudDeAdmision.correoElectronico.trim()).toLowerCase())){
                swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar === null) {
                swal("Licenciatura!", "Debe seleccionar una licenciatura!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catGestionEscolar.propedeutico) {
                if ($scope.properties.catSolicitudDeAdmision.catPropedeutico === null) {
                    swal("Examen propedéutico!", "Debe seleccionar un periodo donde cursara sus estudios!", "warning");
                } else {
                    if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                        swal("Lugar de examen!", "Debe seleccionar un lugar donde cursara sus estudios!", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.avisoPrivacidad === false) {
                        swal("Aviso de privacidad!", "Debe aceptar el aviso de privacidad!", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                        if ($scope.properties.lugarexamen === "En un estado") {
                            if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                                swal("Lugar de examen!", "Debe seleccionar un estado y una ciudad donde realizara el examen!", "warning");
                            } else {
                                /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.selectedIndex++;
                                }*/
                                localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                openModal($scope.properties.modalid);
                            }
                        } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                            if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                                swal("Lugar de examen!", "Debe seleccionar un pais y una ciudad donde realizara el examen!", "warning");
                            } else {
                                /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    $scope.properties.selectedIndex++;
                                }*/
                                localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                openModal($scope.properties.modalid);
                            }
                        } else {
                            $scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                            $scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                $scope.properties.selectedIndex++;
                            }*/
                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                            openModal($scope.properties.modalid);
                        }
                    }
                }
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen === null) {
                swal("Lugar de examen!", "Debe seleccionar un lugar donde cursara sus estudios!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.avisoPrivacidad === false) {
                swal("Aviso de privacidad!", "Debe aceptar el aviso de privacidad!", "warning");
            } else if ($scope.properties.catSolicitudDeAdmision.catLugarExamen.persistenceId_string !== "") {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.catSolicitudDeAdmision.ciudadExamen === null) {
                        swal("Lugar de examen!", "Debe seleccionar un estado y una ciudad donde realizara el examen!", "warning");
                    } else {
                        /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.selectedIndex++;
                        }*/
                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                        localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                        openModal($scope.properties.modalid);
                    }
                } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                    if ($scope.properties.catSolicitudDeAdmision.ciudadExamenPais === null) {
                        swal("Lugar de examen!", "Debe seleccionar un pais y una ciudad donde realizara el examen!", "warning");
                    } else {
                        /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.selectedIndex++;
                        }*/
                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                        localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                        openModal($scope.properties.modalid);
                    }
                } else {
                    $scope.properties.catSolicitudDeAdmision.catPaisExamen = null;
                    $scope.properties.catSolicitudDeAdmision.catEstadoExamen = null;
                    /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.selectedIndex++;
                    }*/
                    localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                    localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                    openModal($scope.properties.modalid);
                }
            }
        } else if ($scope.properties.selectedIndex === 2) {
            $scope.faltacampo = false;
            console.log("validar 2");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
                $scope.faltacampo = true;
            }else if ($scope.properties.catSolicitudDeAdmision.fechaNacimiento === undefined) {
                swal("Fecha de nacimiento!", "Debe agergar su fecha de nacimiento!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catNacionalidad === null) {
                swal("Nacionalidad!", "Debe seleccionar su nacionalidad!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catReligion === null) {
                swal("Religión!", "Debe seleccionar su religón!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.curp === "" && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                swal("CURP!", "Debe agregar su CURP!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.curp.length < 18 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana") {
                swal("CURP!", "Su CURP debe tener 18 caracteres!", "warning");
                $scope.faltacampo = true;
            } else if($scope.properties.catSolicitudDeAdmision.telefonoCelular === undefined){
                swal("Teléfono celular!", "Debe agregar su numero celular!", "warning");
                $scope.faltacampo = true;
            }else if($scope.properties.catSolicitudDeAdmision.telefonoCelular.length !== 10 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion === "Mexicana"){
                swal("Teléfono celular!", "Su teléfono celular debe ser de 10 digitos!", "warning");
                $scope.faltacampo = true;
            } else if($scope.properties.catSolicitudDeAdmision.telefonoCelular.length !== 14 && $scope.properties.catSolicitudDeAdmision.catNacionalidad.descripcion !== "Mexicana"){
                swal("Teléfono celular!", "Su teléfono celular debe ser de 14 digitos!", "warning");
                $scope.faltacampo = true;
            }else if($scope.properties.catSolicitudDeAdmision.telefonoCelular === ""){
                swal("Teléfono celular!", "Debe agregar su numero celular!", "warning");
                $scope.faltacampo = true;
            }else if ($scope.properties.catSolicitudDeAdmision.catEstadoCivil === null) {
                swal("Estado civil!", "Debe seleccionar su estado civil!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catSexo.persistenceId_string === "") {
                swal("Sexo!", "Debe seleccionar su sexo!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus === null) {
                swal("Presento examen en otro campus!", "Debe seleccionar si ha realizado la solicitud en otro campus!", "warning");
                $scope.faltacampo = true;
            } else if ($scope.properties.catSolicitudDeAdmision.catPresentasteEnOtroCampus.descripcion === "Si") {
                if ($scope.properties.catSolicitudDeAdmision.catCampusPresentadoSolicitud.length === 0) {
                    swal("Campus presentado!", "Debe seleccionar el/los campus donde ha presentado su solicitud!", "warning");
                    $scope.faltacampo = true;
                }
            }
            if (!$scope.faltacampo) {
                /*if ($scope.properties.fotopasaporte === undefined || JSON.stringify($scope.properties.fotopasaporte) == '{}') {
                    swal("Fotografía!", "Debe agregar una fotografía!", "warning");
                } else if ($scope.properties.actanacimiento === undefined || JSON.stringify($scope.properties.actanacimiento) == '{}') {
                    swal("Acta de nacimiento!", "Debe agregar su acta de nacimiento!", "warning");
                } else*/ if ($scope.properties.catSolicitudDeAdmision.calle === "") {
                    swal("Calle!", "Debe agregar la calle!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.codigoPostal === "") {
                    swal("Código postal!", "Debe agregar el código postal!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catPais === null) {
                    swal("País!", "Debe seleccionar el país!", "warning");
                } /*else if ($scope.properties.catSolicitudDeAdmision.catEstado === null) {
                    swal("Estado!", "Debe seleccionar el estado!", "warning");
                } */else if ($scope.properties.catSolicitudDeAdmision.ciudad === "") {
                    swal("Ciudad!", "Debe agregar una ciudad!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.calle2 === "") {
                    swal("Entre calles!", "Debe agregar entre que calles se encuentra su domicilio!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.numExterior === "") {
                    swal("Número!", "Debe agregar el número de su domicilio!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.colonia === "") {
                    swal("Colonia!", "Debe agregar la colonia!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.telefono === "") {
                    swal("Teléfono!", "Debe el agregar el teléfono!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catBachilleratos === null) {
                    swal("Preparatoria!", "Debe seleccionar una preparatoria en caso de no encontrar la suya seleccionar la opción otro!", "warning");
                } else if ($scope.properties.catSolicitudDeAdmision.catBachilleratos.descripcion === "Otro") {
                    if ($scope.properties.datosPreparatoria.nombreBachillerato === "") {
                        swal("Preparatoria!", "Debe agregar el nombre de su preparatoria!", "warning");
                    } else if ($scope.properties.datosPreparatoria.paisBachillerato === undefined) {
                        swal("País preparatoria!", "Debe agregar el país de su preparatoria!", "warning");
                    } else if ($scope.properties.datosPreparatoria.estadoBachillerato === undefined) {
                        swal("Estado preparatoria!", "Debe agregar el estado de su preparatoria!", "warning");
                    } else if ($scope.properties.datosPreparatoria.ciudadBachillerato === undefined) {
                        swal("Ciudad preparatoria!", "Debe agregar la ciudad de su preparatoria!", "warning");
                    } else if ($scope.properties.catSolicitudDeAdmision.promedioGeneral === "") {
                        swal("Promedio!", "Debe agregar el promedio que obtuvo en su preparatoria!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.catSolicitudDeAdmision.promedioGeneral = $scope.properties.catSolicitudDeAdmision.promedioGeneral + "";
                            //$scope.properties.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                            //$scope.properties.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                            if($scope.properties.kardex !== undefined){
								$scope.properties.kardexarchivo["newValue"] = $scope.properties.kardex;
                            }

                            

                            if($scope.properties.catSolicitudDeAdmision.catBachilleratos.perteneceRed){
                                if($scope.properties.catSolicitudDeAdmision.resultadoPAA === 0 || $scope.properties.catSolicitudDeAdmision.resultadoPAA === "" || $scope.properties.catSolicitudDeAdmision.resultadoPAA === null || $scope.properties.catSolicitudDeAdmision.resultadoPAA === undefined){
                                    swal("Resultado (PAA) del Examen Collage Board!", "La calificacion debe ser mayor a cero en caso de no haber realizado esta prueba marcarlo con el número 1!", "warning");
                                }else if($scope.properties.catSolicitudDeAdmision.resultadoPAA > 1){
                                    if($scope.properties.collageBoard !== undefined){
                                        $scope.properties.resultadoCBDocumentInput= [];
                                        $scope.properties.resultadoCBDocumentInput.push($scope.properties.collageBoard);
                                        if ($scope.properties.tieneDescuento === true) {
                                            if ($scope.properties.descuento !== undefined) {
                                                $scope.properties.descuentoDocumentInput = [];
                                                $scope.properties.descuentoDocumentInput.push($scope.properties.descuento);
                                                localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                localS.setItem("actanacimiento",JSON.stringify($scope.properties.actanacimiento));
                                                localS.setItem("fotopasaporte",JSON.stringify($scope.properties.fotopasaporte));
                                                localS.setItem("kardex",JSON.stringify($scope.properties.kardex));
                                                localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                                localS.setItem("descuento",JSON.stringify($scope.properties.descuento));
                                                localS.setItem("collageBoard",JSON.stringify($scope.properties.collageBoard));
                                                if($scope.properties.idExtranjero !== undefined){
                                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                }
                                                $scope.properties.selectedIndex++;
                                            } else {
                                                swal("Documento de descuento!", "Debe agregar el documento que acredita tu descuento!", "warning");
                                            }
                                        } else {
                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                            localS.setItem("actanacimiento",JSON.stringify($scope.properties.actanacimiento));
                                            localS.setItem("fotopasaporte",JSON.stringify($scope.properties.fotopasaporte));
                                            localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                            localS.setItem("kardex",JSON.stringify($scope.properties.kardex));
                                            if($scope.properties.idExtranjero !== undefined){
                                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                }
                                            $scope.properties.selectedIndex++;
                                        }
                                        
                                    }else{
                                        swal("Constancia Collage Board!", "Debe agregar la constancia del resultado PAA como viene emitida por el Collage Board. En caso de no haber realizado esta prueba marcarlo con el número 1!", "warning");
                                    }
                                } 
                            } else {
                                if ($scope.properties.tieneDescuento === true) {
                                    if($scope.properties.descuentoarchivo.length === 0){
                                        if($scope.properties.descuento !== undefined){
                                            $scope.properties.descuentoarchivo.push($scope.properties.descuento);
                                        }else{
                                            swal("Documento de descuento!", "Debe agregar el documento que acredita tu descuento!", "warning");
                                        }
                                    }else{
                                        if($scope.properties.descuento !== undefined){
                                            $scope.properties.descuentoarchivo["newValue"] = $scope.properties.descuento;
                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                            localS.setItem("kardex",JSON.stringify($scope.properties.kardex));
                                            localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                            localS.setItem("descuento",JSON.stringify($scope.properties.descuento));
                                            if($scope.properties.idExtranjero !== undefined){
                                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            }
                                            $scope.properties.selectedIndex++;
                                        }
                                        else{
                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                            localS.setItem("kardex",JSON.stringify($scope.properties.kardex));
                                            localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                            localS.setItem("descuento",JSON.stringify($scope.properties.descuento));
                                            if($scope.properties.idExtranjero !== undefined){
                                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            }
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                } else {
                                    localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                    localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                    localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                    localS.setItem("kardex",JSON.stringify($scope.properties.kardex));
                                    if($scope.properties.idExtranjero !== undefined){
                                            $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                        }
                                    $scope.properties.selectedIndex++;
                                }
                            }

                        }
                    }
                } else if ($scope.properties.catSolicitudDeAdmision.promedioGeneral === "") {
                    swal("Promedio!", "Debe agregar el promedio que obtuvo en su preparatoria!", "warning");
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            //$scope.properties.catSolicitudDeAdmision.promedioGeneral = $scope.properties.catSolicitudDeAdmision.promedioGeneral + "";
                            //$scope.properties.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                            //$scope.properties.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                            if($scope.properties.kardex !== undefined){
								$scope.properties.kardexarchivo["newValue"] = $scope.properties.kardex;
                            }

                            if($scope.properties.catSolicitudDeAdmision.catBachilleratos.perteneceRed){
                                if($scope.properties.catSolicitudDeAdmision.resultadoPAA === 0 || $scope.properties.catSolicitudDeAdmision.resultadoPAA === "" || $scope.properties.catSolicitudDeAdmision.resultadoPAA === null || $scope.properties.catSolicitudDeAdmision.resultadoPAA === undefined){
                                    swal("Resultado (PAA) del Examen Collage Board!", "La calificacion debe ser mayor a cero en caso de no haber realizado esta prueba marcarlo con el número 1!", "warning");
                                }else if($scope.properties.catSolicitudDeAdmision.resultadoPAA > 1){
                                    if($scope.properties.collageBoardarchivo.length === 0){
                                        if($scope.properties.collageBoard !== undefined){
                                            $scope.properties.collageBoardarchivo.push($scope.properties.collageBoard);
                                        }else{
                                            swal("Constancia Collage Board!", "Debe agregar la consntancia del resultado PAA como viene emitida por el Collage Board. En caso de no haber realizado esta prueba marcarlo con el número 1!", "warning");
                                        }
                                    }else{
                                        if($scope.properties.collageBoard !== undefined){
                                            $scope.properties.collageBoardarchivo["newValue"] = $scope.properties.collageBoard;
                                        }
                                    }

                                    if ($scope.properties.tieneDescuento === true) {
                                        if($scope.properties.descuentoarchivo.length === 0){
                                            if($scope.properties.descuento !== undefined){
                                                $scope.properties.descuentoarchivo.push($scope.properties.descuento);
                                            }else{
                                                swal("Documento de descuento!", "Debe agregar el documento que acredita tu descuento!", "warning");
                                            }
                                        }else{
                                            if($scope.properties.descuento !== undefined){
                                                $scope.properties.descuentoarchivo["newValue"] = $scope.properties.descuento;
                                                localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                localS.setItem("kardex",JSON.stringify($scope.properties.kardex));
                                                localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                                localS.setItem("descuento",JSON.stringify($scope.properties.descuento));
                                                if($scope.properties.idExtranjero !== undefined){
                                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                }
                                                $scope.properties.selectedIndex++;
                                            }
                                            else{
                                                localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                localS.setItem("kardex",JSON.stringify($scope.properties.kardex));
                                                localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                                localS.setItem("descuento",JSON.stringify($scope.properties.descuento));
                                                if($scope.properties.idExtranjero !== undefined){
                                                    $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                                }
                                                $scope.properties.selectedIndex++;
                                            }
                                        }
                                    } else {
                                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                        localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                        localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                        localS.setItem("kardex",JSON.stringify($scope.properties.kardex));
                                        if($scope.properties.idExtranjero !== undefined){
                                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            }
                                        $scope.properties.selectedIndex++;
                                    }

                                } else if ($scope.properties.tieneDescuento === true) {
                                    if($scope.properties.descuentoarchivo.length === 0){
                                        if($scope.properties.descuento !== undefined){
                                            $scope.properties.descuentoarchivo.push($scope.properties.descuento);
                                        }else{
                                            swal("Documento de descuento!", "Debe agregar el documento que acredita tu descuento!", "warning");
                                        }
                                    }else{
                                        if($scope.properties.descuento !== undefined){
                                            $scope.properties.descuentoarchivo["newValue"] = $scope.properties.descuento;
                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                            localS.setItem("kardex",JSON.stringify($scope.properties.kardex));
                                            localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                            localS.setItem("descuento",JSON.stringify($scope.properties.descuento));
                                            if($scope.properties.idExtranjero !== undefined){
                                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            }
                                            $scope.properties.selectedIndex++;
                                        }
                                        else{
                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                            localS.setItem("kardex",JSON.stringify($scope.properties.kardex));
                                            localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                            localS.setItem("descuento",JSON.stringify($scope.properties.descuento));
                                            if($scope.properties.idExtranjero !== undefined){
                                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            }
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                } else {
                                    localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                    localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                    localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                    localS.setItem("kardex",JSON.stringify($scope.properties.kardex));
                                    if($scope.properties.idExtranjero !== undefined){
                                            $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                        }
                                    $scope.properties.selectedIndex++;
                                }
                            } else {
                                if ($scope.properties.tieneDescuento === true) {
                                    if($scope.properties.descuentoarchivo.length === 0){
                                        if($scope.properties.descuento !== undefined){
                                            $scope.properties.descuentoarchivo.push($scope.properties.descuento);
                                        }else{
                                            swal("Documento de descuento!", "Debe agregar el documento que acredita tu descuento!", "warning");
                                        }
                                    }else{
                                        if($scope.properties.descuento !== undefined){
                                            $scope.properties.descuentoarchivo["newValue"] = $scope.properties.descuento;
                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                            localS.setItem("kardex",JSON.stringify($scope.properties.kardex));
                                            localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                            localS.setItem("descuento",JSON.stringify($scope.properties.descuento));
                                            if($scope.properties.idExtranjero !== undefined){
                                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            }
                                            $scope.properties.selectedIndex++;
                                        }
                                        else{
                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                            localS.setItem("kardex",JSON.stringify($scope.properties.kardex));
                                            localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                            localS.setItem("descuento",JSON.stringify($scope.properties.descuento));
                                            if($scope.properties.idExtranjero !== undefined){
                                                $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                            }
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                } else {
                                    localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                    localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                    localS.setItem("preparatoriaSeleccionada",$scope.properties.preparatoriaSeleccionada);
                                    localS.setItem("kardex",JSON.stringify($scope.properties.kardex));
                                    if($scope.properties.idExtranjero !== undefined){
                                            $scope.properties.catSolicitudDeAdmision.curp = $scope.properties.idExtranjero;
                                        }
                                    $scope.properties.selectedIndex++;
                                }
                            }

                        }
                }
            }
            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.selectedIndex++;
            }
            //$scope.properties.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                //$scope.properties.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                $scope.properties.constanciaDocumentInput.push($scope.properties.kardex);*/
            /* if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                 $scope.properties.selectedIndex--;
             } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                  //$scope.properties.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                     //$scope.properties.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                     $scope.properties.constanciaDocumentInput.push($scope.properties.kardex);
                 $scope.properties.selectedIndex++;
             }*/
        } else if ($scope.properties.selectedIndex === 3) {
            console.log("validar 3");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            }else if ($scope.properties.tutorInput.length === 0) {
                swal("Tutor!", "Debe agregar al menos un tutor!", "warning");
            } else if ($scope.properties.padreInput.desconozcoDatosPadres) {
                //validar madre
                if ($scope.properties.madreInput.desconozcoDatosPadres) {
                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                            $scope.properties.selectedIndex++;
                        }
                    }
                } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                    swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                    swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                    swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                    swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                } else if ($scope.properties.datosPadres.madrevive) {
                    if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                        swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                        if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                            swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                        } else {
                            if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                    swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                    swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                } else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                    swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                } /*else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                } */else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                } else {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                }
                            } else {
                                if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                } else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                    swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                }else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                }*/ else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                } else {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                            swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                        } else if ($scope.properties.datosPadres.madretrabaja) {
                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                            } else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                            }else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                            } /*else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                            } */else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                            } else {
                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                        localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                            } else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                            }else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                            }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                            } */else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                            } else {
                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                        localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                        localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                        $scope.properties.selectedIndex++;
                    }
                }




            } else if ($scope.properties.padreInput.catTitulo === 0 || $scope.properties.padreInput.catTitulo === null) {
                swal("Título!", "Debe seleccionar el título para identificar al padre!", "warning");
            } else if ($scope.properties.padreInput.nombre === "" || $scope.properties.padreInput.nombre === undefined) {
                swal("Nombre del padre!", "Debe agregar nombre del padre!", "warning");
            } else if ($scope.properties.padreInput.apellidos === "" || $scope.properties.padreInput.apellidos === undefined) {
                swal("Apellidos del padre!", "Debe agregar los apellidos del padre!", "warning");
            } else if ($scope.properties.padreInput.vive === 0 || $scope.properties.padreInput.vive === null) {
                swal("Padre vive!", "Debe seleccionar si el padre vive!", "warning");
            } else if ($scope.properties.datosPadres.padrevive) {
                if ($scope.properties.padreInput.catEgresoAnahuac === 0 || $scope.properties.padreInput.catEgresoAnahuac === null) {
                    swal("Egreso Anahuac!", "Debe seleccionar si su padre egreso de la universidad Anahuac!", "warning");
                } else if ($scope.properties.datosPadres.padreegresoanahuac) {
                    if ($scope.properties.padreInput.catCampusEgreso === 0 || $scope.properties.padreInput.catCampusEgreso === null) {
                        swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su padre!", "warning");
                    } else {
                        if ($scope.properties.padreInput.catTrabaja === 0 || $scope.properties.padreInput.catTrabaja === null) {
                            swal("Trabaja!", "Debe seleccionar si su padre trabaja!", "warning");
                        } else if ($scope.properties.datosPadres.padretrabaja) {
                            if ($scope.properties.padreInput.empresaTrabaja === "" || $scope.properties.padreInput.empresaTrabaja === undefined) {
                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su padre trabaja!", "warning");
                            } else if ($scope.properties.padreInput.puesto === "" || $scope.properties.padreInput.puesto === undefined) {
                                swal("Puesto!", "Debe agregar el puesto de trabajo del padre!", "warning");
                            } else if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                            } else if(!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())){
                                swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                            } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad del padre!", "warning");
                            } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio del padre!", "warning");
                            }/* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio del padre!", "warning");
                            } */else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio del padre!", "warning");
                            } else if ($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                            } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio del padre!", "warning");
                            } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio del padre!", "warning");
                            } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio del padre!", "warning");
                            } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono del padre!", "warning");
                            } else {
                                ///Validar madre 1
                                if ($scope.properties.madreInput.desconozcoDatosPadres) {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                                    swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                                } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                                    swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                                } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                                    swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                                } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                                    swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                                } else if ($scope.properties.datosPadres.madrevive) {
                                    if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                        swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                        if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                            swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                                        } else {
                                            if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                    swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                                } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                    swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                                }  else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                                    swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                                }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                                } */else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                                } else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                                    swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                                }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                                }*/ else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                            swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                            } else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                                swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                            }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                            }*/ else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                        localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                        $scope.properties.selectedIndex++;
                                                    }
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                            } else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                                swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                            }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                            } */else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                        localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                        $scope.properties.selectedIndex++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                        localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                            } else if(!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())){
                                swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                            } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad del padre!", "warning");
                            } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio del padre!", "warning");
                            }/* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio del padre!", "warning");
                            }*/ else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio del padre!", "warning");
                            } else if ($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                            } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio del padre!", "warning");
                            } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio del padre!", "warning");
                            } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio del padre!", "warning");
                            } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono del padre!", "warning");
                            } else {
                                //VALIDAR MADRE 2
                                if ($scope.properties.madreInput.desconozcoDatosPadres) {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                                    swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                                } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                                    swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                                } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                                    swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                                } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                                    swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                                } else if ($scope.properties.datosPadres.madrevive) {
                                    if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                        swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                        if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                            swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                                        } else {
                                            if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                                swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                    swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                                } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                    swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                                } else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                                    swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                                }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                                } */else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                                } else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                                    swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                                }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                                } */else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                                } else {
                                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                            swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                                swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                            }  else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                                swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                            }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                            }*/ else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                        localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                        $scope.properties.selectedIndex++;
                                                    }
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                            }  else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                                swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                            }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                            }*/ else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                            } else {
                                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                        localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                        $scope.properties.selectedIndex++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                        localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        }
                    }
                } else if ($scope.properties.padreInput.catTrabaja === 0 || $scope.properties.padreInput.catTrabaja === null) {
                    swal("Trabaja!", "Debe seleccionar si su padre trabaja!", "warning");
                } else if ($scope.properties.datosPadres.padretrabaja) {
                    if ($scope.properties.padreInput.empresaTrabaja === "" || $scope.properties.padreInput.empresaTrabaja === undefined) {
                        swal("Empresa!", "Debe agregar el nombre de la empresa donde su padre trabaja!", "warning");
                    } else if ($scope.properties.padreInput.puesto === "" || $scope.properties.padreInput.puesto === undefined) {
                        swal("Puesto!", "Debe agregar el puesto de trabajo del padre!", "warning");
                    } else if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                        swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                    } else if(!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())){
                        swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                    } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                        swal("Escolaridad!", "Debe seleccionar la escolaridad del padre!", "warning");
                    } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                        swal("País!", "Debe agregar el país del domicilio del padre!", "warning");
                    }/* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                        swal("Estado!", "Debe agregar el estado del domicilio del padre!", "warning");
                    }*/ else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                        swal("Calle!", "Debe agregar la calle del domicilio del padre!", "warning");
                    } else if ($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) {
                        swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                    } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                        swal("Número exterior!", "Debe agregar el número exterior del domicilio del padre!", "warning");
                    } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                        swal("Ciudad!", "Debe agregar la calle del domicilio del padre!", "warning");
                    } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                        swal("Colonia!", "Debe agregar la colonia del domicilio del padre!", "warning");
                    } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                        swal("Teléfono!", "Debe agregar el teléfono del padre!", "warning");
                    } else {
                        ///Validar madre  3
                        if ($scope.properties.madreInput.desconozcoDatosPadres) {
                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                    localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                    $scope.properties.selectedIndex++;
                                }
                            }
                        } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                            swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                        } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                            swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                        } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                            swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                        } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                            swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                        } else if ($scope.properties.datosPadres.madrevive) {
                            if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                            } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                    swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                                } else {
                                    if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                        swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                        if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                            swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                        } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                            swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                            swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                        } else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                            swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                            swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                            swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                        }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                            swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                        } */else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                            swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                            swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                            swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                            swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                            swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                            swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                    localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                    localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                            swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                        } else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                            swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                            swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                            swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                        }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                            swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                        }*/ else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                            swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                            swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                            swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                            swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                            swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                            swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                    localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                    localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                    swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                    if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                        swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                    } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                        swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                        swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                    } else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                        swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                        swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                        swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                    }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                        swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                    }*/ else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                        swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                        swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                        swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                        swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                        swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                        swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                            swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                $scope.properties.selectedIndex++;
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                        swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                    } else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                        swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                        swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                        swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                    }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                        swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                    }*/ else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                        swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                        swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                        swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                        swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                        swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                        swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                            swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                $scope.properties.selectedIndex++;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                $scope.properties.selectedIndex++;
                            }
                        }
                    }
                } else {
                    if ($scope.properties.padreInput.correoElectronico === "" || $scope.properties.padreInput.correoElectronico === undefined) {
                        swal("Correo electrónico!", "Debe agregar el correo electrónico del padre!", "warning");
                    } else if(!re.test(String($scope.properties.padreInput.correoElectronico.trim()).toLowerCase())){
                        swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                    } else if ($scope.properties.padreInput.catEscolaridad === 0 || $scope.properties.padreInput.catEscolaridad === null) {
                        swal("Escolaridad!", "Debe seleccionar la escolaridad del padre!", "warning");
                    } else if ($scope.properties.padreInput.catPais === 0 || $scope.properties.padreInput.catPais === null) {
                        swal("País!", "Debe agregar el país del domicilio del padre!", "warning");
                    }/* else if ($scope.properties.padreInput.catEstado === 0 || $scope.properties.padreInput.catEstado === null) {
                        swal("Estado!", "Debe agregar el estado del domicilio del padre!", "warning");
                    }*/ else if ($scope.properties.padreInput.calle === "" || $scope.properties.padreInput.calle === undefined) {
                        swal("Calle!", "Debe agregar la calle del domicilio del padre!", "warning");
                    } else if ($scope.properties.padreInput.codigoPostal === "" || $scope.properties.padreInput.codigoPostal === undefined) {
                        swal("Código postal!", "Debe agregar el código postal del domicilio del padre!", "warning");
                    } else if ($scope.properties.padreInput.numeroExterior === "" || $scope.properties.padreInput.numeroExterior === undefined) {
                        swal("Número exterior!", "Debe agregar el número exterior del domicilio del padre!", "warning");
                    } else if ($scope.properties.padreInput.ciudad === "" || $scope.properties.padreInput.ciudad === undefined) {
                        swal("Ciudad!", "Debe agregar la calle del domicilio del padre!", "warning");
                    } else if ($scope.properties.padreInput.colonia === "" || $scope.properties.padreInput.colonia === undefined) {
                        swal("Colonia!", "Debe agregar la colonia del domicilio del padre!", "warning");
                    } else if ($scope.properties.padreInput.telefono === "" || $scope.properties.padreInput.telefono === undefined) {
                        swal("Teléfono!", "Debe agregar el teléfono del padre!", "warning");
                    } else {
                        //VALIDAR MADRE 4
                        if ($scope.properties.madreInput.desconozcoDatosPadres) {
                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                            } else {
                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                    $scope.properties.selectedIndex--;
                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                    localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                    localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                    $scope.properties.selectedIndex++;
                                }
                            }
                        } else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                            swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                        } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                            swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                        } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                            swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                        } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                            swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                        } else if ($scope.properties.datosPadres.madrevive) {
                            if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                                swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                            } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                                    swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                                } else {
                                    if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                        swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                        if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                            swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                        } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                            swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                            swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                        }  else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                            swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                            swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                            swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                        } /*else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                            swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                        } */else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                            swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                            swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                            swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                            swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                            swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                            swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                    localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                    localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                            swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                        } else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                            swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                        } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                            swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                            swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                        }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                            swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                        }*/ else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                            swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                            swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                            swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                            swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                            swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                        } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                            swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                        } else {
                                            if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                                swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                    localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                    localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                    swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                    if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                        swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                    } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                        swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                        swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                    }  else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                        swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                        swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                        swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                    }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                        swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                    }*/ else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                        swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                        swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                        swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                        swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                        swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                        swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                            swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                $scope.properties.selectedIndex++;
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                        swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                    } else if(!re.test(String($scope.properties.madreInput.correoElectronico.trim()).toLowerCase())){
                                        swal("Correo electrónico!", "Su correo electrónico no es valido!", "warning");
                                    } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                        swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                        swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                    }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                        swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                    }*/ else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                        swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                        swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                        swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                        swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                        swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                    } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                        swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                    } else {
                                        if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                            swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                                localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                                $scope.properties.selectedIndex++;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                $scope.properties.selectedIndex--;
                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                $scope.properties.selectedIndex++;
                            }
                        }
                    }
                }

            } else {
                console.log("esta validando el la linea 373")
                if ($scope.properties.madreInput.desconozcoDatosPadres) {
                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                            $scope.properties.selectedIndex++;
                        }
                    }
                }else if ($scope.properties.madreInput.catTitulo === 0 || $scope.properties.madreInput.catTitulo === null) {
                    swal("Título!", "Debe seleccionar el título para identificar a la madre!", "warning");
                } else if ($scope.properties.madreInput.nombre === "" || $scope.properties.madreInput.nombre === undefined) {
                    swal("Nombre de la madre!", "Debe agregar nombre de la madre!", "warning");
                } else if ($scope.properties.madreInput.apellidos === "" || $scope.properties.madreInput.apellidos === undefined) {
                    swal("Apellidos de la madre!", "Debe agregar los apellidos de la madre!", "warning");
                } else if ($scope.properties.madreInput.vive === 0 || $scope.properties.madreInput.vive === null) {
                    swal("Madre vive!", "Debe seleccionar si la madre vive!", "warning");
                } else if ($scope.properties.datosPadres.madrevive) {
                    if ($scope.properties.madreInput.catEgresoAnahuac === 0 || $scope.properties.madreInput.catEgresoAnahuac === null) {
                        swal("Egreso Anahuac!", "Debe seleccionar si su madre egreso de la universidad Anahuac!", "warning");
                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                        if ($scope.properties.madreInput.catCampusEgreso === 0 || $scope.properties.madreInput.catCampusEgreso === null) {
                            swal("Campus egresado!", "Debe seleccionar de que campus Anahuac egresó su madre!", "warning");
                        } else {
                            if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                                swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                    swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                                } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                    swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                                } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                }*/ else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                } else {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                }
                            } else {
                                if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                    swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                                } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                    swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                                } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                    swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                                }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                    swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                                }*/ else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                    swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                    swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                    swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                    swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                    swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                                } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                    swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                                } else {
                                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.madreInput.catTrabaja === 0 || $scope.properties.madreInput.catTrabaja === null) {
                            swal("Trabaja!", "Debe seleccionar si su madre trabaja!", "warning");
                        } else if ($scope.properties.datosPadres.madretrabaja) {
                            if ($scope.properties.madreInput.empresaTrabaja === "" || $scope.properties.madreInput.empresaTrabaja === undefined) {
                                swal("Empresa!", "Debe agregar el nombre de la empresa donde su madre trabaja!", "warning");
                            } else if ($scope.properties.madreInput.puesto === "" || $scope.properties.madreInput.puesto === undefined) {
                                swal("Puesto!", "Debe agregar el puesto de trabajo de la madre!", "warning");
                            } else if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                            }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                            }*/ else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                            } else {
                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                        localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.madreInput.correoElectronico === "" || $scope.properties.madreInput.correoElectronico === undefined) {
                                swal("Correo electrónico!", "Debe agregar el correo electrónico de la madre!", "warning");
                            } else if ($scope.properties.madreInput.catEscolaridad === 0 || $scope.properties.madreInput.catEscolaridad === null) {
                                swal("Escolaridad!", "Debe seleccionar la escolaridad de la madre!", "warning");
                            } else if ($scope.properties.madreInput.catPais === 0 || $scope.properties.madreInput.catPais === null) {
                                swal("País!", "Debe agregar el país del domicilio de la madre!", "warning");
                            }/* else if ($scope.properties.madreInput.catEstado === 0 || $scope.properties.madreInput.catEstado === null) {
                                swal("Estado!", "Debe agregar el estado del domicilio de la madre!", "warning");
                            }*/ else if ($scope.properties.madreInput.calle === "" || $scope.properties.madreInput.calle === undefined) {
                                swal("Calle!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.codigoPostal === "" || $scope.properties.madreInput.codigoPostal === undefined) {
                                swal("Código postal!", "Debe agregar el código postal del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.numeroExterior === "" || $scope.properties.madreInput.numeroExterior === undefined) {
                                swal("Número exterior!", "Debe agregar el número exterior del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.ciudad === "" || $scope.properties.madreInput.ciudad === undefined) {
                                swal("Ciudad!", "Debe agregar la calle del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.colonia === "" || $scope.properties.madreInput.colonia === undefined) {
                                swal("Colonia!", "Debe agregar la colonia del domicilio de la madre!", "warning");
                            } else if ($scope.properties.madreInput.telefono === "" || $scope.properties.madreInput.telefono === undefined) {
                                swal("Teléfono!", "Debe agregar el teléfono de la madre!", "warning");
                            } else {
                                if ($scope.properties.contactoEmergenciaInput.length === 0) {
                                    swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                                        localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if ($scope.properties.contactoEmergenciaInput.length === 0) {
                        swal("Contacto de emergencia!", "Debe agregar al menos un contacto de emergencia!", "warning");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            localS.setItem("catSolicitudDeAdmisionInput",JSON.stringify($scope.properties.formInput));
                            localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                            $scope.properties.selectedIndex++;
                        }
                    }
                }
            }
            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.selectedIndex++;
            }*/



        } else if ($scope.properties.selectedIndex === 4) {
            console.log("validar 4");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                $scope.properties.selectedIndex++;
            }
        } else if ($scope.properties.selectedIndex === 5) {
            console.log("validar 4");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                localS.setItem("selectedIndex",$scope.properties.selectedIndex);
                $scope.properties.selectedIndex++;
            }
        }

    }

    function openModal(modalid) {

        modalService.open(modalid);
    }

    function closeModal(shouldClose) {
        if (shouldClose)
            modalService.close();
    }
}