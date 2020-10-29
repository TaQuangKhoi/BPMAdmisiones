function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    $scope.action = function() {
        debugger;
        if ($scope.properties.selectedIndex === 0) {
            console.log("validar 0");
            //swal("Good job!", "You clicked the button!", "success")
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.selectedIndex++;
            }
            /* if ($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatCampus === undefined) {
                 // alert("Debe seleccionar un campus");
                 swal("Good job!", "You clicked the button!", "success")
             } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatLicenciatura === null) {
                 alert("Debe seleccionar una licenciatura");
             } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatPeriodo === null) {
                 alert("Debe seleccionar un periodo");
             } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatLugarExamen === 0) {
                 alert("Debe seleccionar un lugar para realizar el examen");
             } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatLugarExamen !== 0) {
                 if ($scope.properties.lugarexamen === "En un estado") {
                     if ($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatEstadoExamen === null || $scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamen === "") {
                         alert("Debe seleccionar un estado y agregar una ciudad para realizar el examen");
                     } else {
                         if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                             $scope.properties.selectedIndex--;
                         } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                             $scope.properties.selectedIndex++;
                         }
                     }
                 } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                     if ($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatPaisExamen === null || $scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamen === "") {
                         alert("Debe seleccionar un pais y agregar una ciudad para realizar el examen");
                     } else {
                         if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                             $scope.properties.selectedIndex--;
                         } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                             $scope.properties.selectedIndex++;
                         }
                     }
                 } else {
                     if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                         $scope.properties.selectedIndex--;
                     } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                         $scope.properties.selectedIndex++;
                     }
                 }

             } else {
                 if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                     $scope.properties.selectedIndex--;
                 } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                     $scope.properties.selectedIndex++;
                 }
             }*/
        } else if ($scope.properties.selectedIndex === 1) {
            console.log("validar 1");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.selectedIndex++;
            }
            /*if ($scope.properties.formInput.catSolicitudDeAdmisionInput.primerNombre === "") {
                alert("Debe agregar su primer nombre");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.apellidoPaterno === "") {
                alert("Debe agregar su apellido paterno");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.apellidoMaterno === "") {
                alert("Debe agregar su apellido materno");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.correoElectronico === "") {
                alert("Debe agregar su correo electronico");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatLicenciatura === null) {
                alert("Debe seleccionar una licenciatura");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatLugarExamen === 0) {
                alert("Debe seleccionar un lugar para realizar el examen");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.avisoPrivacidad === false) {
                alert("Debe aceptar el aviso de privacidad");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatLugarExamen !== 0) {
                if ($scope.properties.lugarexamen === "En un estado") {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatEstadoExamen === null || $scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamen === "") {
                        alert("Debe seleccionar un estado y agregar una ciudad para realizar el examen");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.selectedIndex++;
                        }
                    }
                } else if ($scope.properties.lugarexamen === "En el extranjero (solo si vives fuera de México)") {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatPaisExamen === null || $scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadExamen === "") {
                        alert("Debe seleccionar un pais y agregar una ciudad para realizar el examen");
                    } else {
                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                            $scope.properties.selectedIndex--;
                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                            $scope.properties.selectedIndex++;
                        }
                    }
                } else {
                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.selectedIndex++;
                    }
                }
            }*/
        } else if ($scope.properties.selectedIndex === 2) {
            $scope.faltacampo = false;
            console.log("validar 2");
            
            /*if($scope.properties.formInput.catSolicitudDeAdmisionInput.fechaNacimiento === undefined){
                alert("Debe agregar su fecha de nacimiento");
                $scope.faltacampo = true;
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatNacionalidad === null){
                alert("Debe seleccionar su nacionalidad");
                $scope.faltacampo = true;
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatReligion === null){
                alert("Debe seleccionar su religion");
                $scope.faltacampo = true;
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.curp === ""){
                alert("Debe agregar su CURP");
                $scope.faltacampo = true;
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatEstadoCivil === null){
                alert("Estado civil");
                $scope.faltacampo = true;
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatSexo === 0){
                alert("sexo");
                $scope.faltacampo = true;
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatPresentadoSolicitudAnahuac === null){
                alert("otra universidad");
                $scope.faltacampo = true;
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatPresentadoSolicitudAnahuac !== 0){
                if($scope.properties.presentadoSolicitud === "Si"){
                    if($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatCampusPresentadoSolicitud === 0){
                    alert("campus");
                    $scope.faltacampo = true;
                }
                }
                
            }
            if(!$scope.faltacampo){
                if($scope.properties.fotopasaporte === undefined || JSON.stringify($scope.properties.fotopasaporte)=='{}'){
                 alert("pasaporte");
            }else if($scope.properties.actanacimiento === undefined || JSON.stringify($scope.properties.actanacimiento)=='{}'){
                alert("acta de nacimiento");
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.calle === ""){
                alert("calle");
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.codigoPostal === ""){
                alert("cp");
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatPais === null){
                alert("pais");
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatEstado === null){
                alert("estado");
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudad === ""){
                alert("ciudad");
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.calle2 === ""){
                alert("calle2");
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.numExterior === ""){
                alert("num");
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.colonia === ""){
                alert("colonia");
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.telefono === ""){
                alert("telefono");
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.otroTelefonoContacto === ""){
                alert("otro tel");
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatPaisPreparatoria === null){
                alert("pais prepa");
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatEstadoPreparatoria === null){
                alert("estado prepa");
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.ciudadPreparatoria === ""){
                alert("ciudad prepa");
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.idCatPreparatoria === null){
                alert("prepa");
            }else if($scope.properties.formInput.catSolicitudDeAdmisionInput.promedioGeneral === ""){
                alert("promedio");
            }else if($scope.properties.kardex === undefined || JSON.stringify($scope.properties.actanacimiento)=='{}'){
                alert("constancia");
            }else{
                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.formInput.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                $scope.properties.formInput.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                $scope.properties.formInput.constanciaDocumentInput.push($scope.properties.kardex);
                $scope.properties.selectedIndex++;
            }
            }
            }
            /*if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.selectedIndex++;
            }*/
            $scope.properties.formInput.fotoPasaporteDocumentInput.push($scope.properties.fotopasaporte);
                $scope.properties.formInput.actaNacimientoDocumentInput.push($scope.properties.actanacimiento);
                $scope.properties.formInput.constanciaDocumentInput.push($scope.properties.kardex);
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.selectedIndex++;
                    }
        } else if ($scope.properties.selectedIndex === 3) {
            console.log("validar 3");
           /* if($scope.properties.formInput.catSolicitudDeAdmisionInput.tutor.length === 0){
                alert("tutor");
            }else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatTitulo === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatTitulo === null) {
                alert("titulo");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.nombre === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.nombre === undefined) {
                alert("nombre");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.apellidos === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.apellidos === undefined) {
                alert("apellidos");
            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.vive === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.vive === null) {
                alert("vive")
            } else if ($scope.properties.datosPadres.padrevive) {
                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEgresoAnahuac === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEgresoAnahuac === null) {
                    alert("egreso")
                } else if ($scope.properties.datosPadres.padreegresoanahuac) {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatCampusEgreso === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatCampusEgreso === null) {
                        alert("campus egreso")
                    } else {
                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatTrabaja === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatTrabaja === null) {
                            alert("trabaja")
                        } else if ($scope.properties.datosPadres.padretrabaja) {
                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.empresaTrabaja === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.empresaTrabaja === undefined) {
                                alert("empresa")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.puesto === undefined) {
                                alert("puesto")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.correoElectronico === undefined) {
                                alert("correo")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEscolaridad === null) {
                                alert("escolaridad")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatPais === null) {
                                alert("pais")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEstado === null) {
                                alert("estado")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.calle === undefined) {
                                alert("calle")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.codigoPostal === undefined) {
                                alert("codigoPostal")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.numeroExterior === undefined) {
                                alert("numeroExterior")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.ciudad === undefined) {
                                alert("ciudad")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.colonia === undefined) {
                                alert("colonia")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.telefono === undefined) {
                                alert("telefono")
                            } else {
                                ///Validar madre 1
                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTitulo === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTitulo === null) {
                                    alert("titulo");
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.nombre === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.nombre === undefined) {
                                    alert("nombre");
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.apellidos === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.apellidos === undefined) {
                                    alert("apellidos");
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.vive === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.vive === null) {
                                    alert("vive")
                                } else if ($scope.properties.datosPadres.madrevive) {
                                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEgresoAnahuac === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEgresoAnahuac === null) {
                                        alert("egreso")
                                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatCampusEgreso === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatCampusEgreso === null) {
                                            alert("campus egreso")
                                        } else {
                                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === null) {
                                                alert("trabaja")
                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === undefined) {
                                                    alert("empresa")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === undefined) {
                                                    alert("puesto")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                                    alert("correo")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                                    alert("escolaridad")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                                    alert("pais")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                                    alert("estado")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                                    alert("calle")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                                    alert("codigoPostal")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                                    alert("numeroExterior")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                                    alert("ciudad")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                                    alert("colonia")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                                    alert("telefono")
                                                } else {
                                                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                                        alert("contacto de emergencia")
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                                    alert("correo")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                                    alert("escolaridad")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                                    alert("pais")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                                    alert("estado")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                                    alert("calle")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                                    alert("codigoPostal")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                                    alert("numeroExterior")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                                    alert("ciudad")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                                    alert("colonia")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                                    alert("telefono")
                                                } else {
                                                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                                        alert("contacto de emergencia")
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === null) {
                                            alert("trabaja")
                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === undefined) {
                                                alert("empresa")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === undefined) {
                                                alert("puesto")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                                alert("correo")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                                alert("escolaridad")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                                alert("pais")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                                alert("estado")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                                alert("calle")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                                alert("codigoPostal")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                                alert("numeroExterior")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                                alert("ciudad")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                                alert("colonia")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                                alert("telefono")
                                            } else {
                                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                                    alert("contacto de emergencia")
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                        $scope.properties.selectedIndex++;
                                                    }
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                                alert("correo")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                                alert("escolaridad")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                                alert("pais")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                                alert("estado")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                                alert("calle")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                                alert("codigoPostal")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                                alert("numeroExterior")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                                alert("ciudad")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                                alert("colonia")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                                alert("telefono")
                                            } else {
                                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                                    alert("contacto de emergencia")
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
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
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.correoElectronico === undefined) {
                                alert("correo")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEscolaridad === null) {
                                alert("escolaridad")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatPais === null) {
                                alert("pais")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEstado === null) {
                                alert("estado")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.calle === undefined) {
                                alert("calle")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.codigoPostal === undefined) {
                                alert("codigoPostal")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.numeroExterior === undefined) {
                                alert("numeroExterior")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.ciudad === undefined) {
                                alert("ciudad")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.colonia === undefined) {
                                alert("colonia")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.telefono === undefined) {
                                alert("telefono")
                            } else {
                                //VALIDAR MADRE 2
                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTitulo === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTitulo === null) {
                                    alert("titulo");
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.nombre === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.nombre === undefined) {
                                    alert("nombre");
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.apellidos === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.apellidos === undefined) {
                                    alert("apellidos");
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.vive === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.vive === null) {
                                    alert("vive")
                                } else if ($scope.properties.datosPadres.madrevive) {
                                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEgresoAnahuac === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEgresoAnahuac === null) {
                                        alert("egreso")
                                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatCampusEgreso === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatCampusEgreso === null) {
                                            alert("campus egreso")
                                        } else {
                                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === null) {
                                                alert("trabaja")
                                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === undefined) {
                                                    alert("empresa")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === undefined) {
                                                    alert("puesto")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                                    alert("correo")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                                    alert("escolaridad")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                                    alert("pais")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                                    alert("estado")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                                    alert("calle")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                                    alert("codigoPostal")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                                    alert("numeroExterior")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                                    alert("ciudad")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                                    alert("colonia")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                                    alert("telefono")
                                                } else {
                                                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                                        alert("contacto de emergencia")
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            } else {
                                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                                    alert("correo")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                                    alert("escolaridad")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                                    alert("pais")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                                    alert("estado")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                                    alert("calle")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                                    alert("codigoPostal")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                                    alert("numeroExterior")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                                    alert("ciudad")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                                    alert("colonia")
                                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                                    alert("telefono")
                                                } else {
                                                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                                        alert("contacto de emergencia")
                                                    } else {
                                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                            $scope.properties.selectedIndex--;
                                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                            $scope.properties.selectedIndex++;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === null) {
                                            alert("trabaja")
                                        } else if ($scope.properties.datosPadres.madretrabaja) {
                                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === undefined) {
                                                alert("empresa")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === undefined) {
                                                alert("puesto")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                                alert("correo")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                                alert("escolaridad")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                                alert("pais")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                                alert("estado")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                                alert("calle")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                                alert("codigoPostal")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                                alert("numeroExterior")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                                alert("ciudad")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                                alert("colonia")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                                alert("telefono")
                                            } else {
                                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                                    alert("contacto de emergencia")
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                        $scope.properties.selectedIndex++;
                                                    }
                                                }
                                            }
                                        } else {
                                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                                alert("correo")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                                alert("escolaridad")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                                alert("pais")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                                alert("estado")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                                alert("calle")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                                alert("codigoPostal")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                                alert("numeroExterior")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                                alert("ciudad")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                                alert("colonia")
                                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                                alert("telefono")
                                            } else {
                                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                                    alert("contacto de emergencia")
                                                } else {
                                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                        $scope.properties.selectedIndex--;
                                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
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
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        }
                    }
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatTrabaja === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatTrabaja === null) {
                    alert("trabaja")
                } else if ($scope.properties.datosPadres.padretrabaja) {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.empresaTrabaja === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.empresaTrabaja === undefined) {
                        alert("empresa")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.puesto === undefined) {
                        alert("puesto")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.correoElectronico === undefined) {
                        alert("correo")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEscolaridad === null) {
                        alert("escolaridad")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatPais === null) {
                        alert("pais")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEstado === null) {
                        alert("estado")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.calle === undefined) {
                        alert("calle")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.codigoPostal === undefined) {
                        alert("codigoPostal")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.numeroExterior === undefined) {
                        alert("numeroExterior")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.ciudad === undefined) {
                        alert("ciudad")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.colonia === undefined) {
                        alert("colonia")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.telefono === undefined) {
                        alert("telefono")
                    } else {
                        ///Validar madre  3
                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTitulo === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTitulo === null) {
                            alert("titulo");
                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.nombre === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.nombre === undefined) {
                            alert("nombre");
                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.apellidos === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.apellidos === undefined) {
                            alert("apellidos");
                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.vive === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.vive === null) {
                            alert("vive")
                        } else if ($scope.properties.datosPadres.madrevive) {
                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEgresoAnahuac === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEgresoAnahuac === null) {
                                alert("egreso")
                            } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatCampusEgreso === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatCampusEgreso === null) {
                                    alert("campus egreso")
                                } else {
                                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === null) {
                                        alert("trabaja")
                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === undefined) {
                                            alert("empresa")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === undefined) {
                                            alert("puesto")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                            alert("correo")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                            alert("escolaridad")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                            alert("pais")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                            alert("estado")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                            alert("calle")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                            alert("codigoPostal")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                            alert("numeroExterior")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                            alert("ciudad")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                            alert("colonia")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                            alert("telefono")
                                        } else {
                                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                                alert("contacto de emergencia")
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                            alert("correo")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                            alert("escolaridad")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                            alert("pais")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                            alert("estado")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                            alert("calle")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                            alert("codigoPostal")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                            alert("numeroExterior")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                            alert("ciudad")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                            alert("colonia")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                            alert("telefono")
                                        } else {
                                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                                alert("contacto de emergencia")
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === null) {
                                    alert("trabaja")
                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === undefined) {
                                        alert("empresa")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === undefined) {
                                        alert("puesto")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                        alert("correo")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                        alert("escolaridad")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                        alert("pais")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                        alert("estado")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                        alert("calle")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                        alert("codigoPostal")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                        alert("numeroExterior")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                        alert("ciudad")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                        alert("colonia")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                        alert("telefono")
                                    } else {
                                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                            alert("contacto de emergencia")
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                $scope.properties.selectedIndex++;
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                        alert("correo")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                        alert("escolaridad")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                        alert("pais")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                        alert("estado")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                        alert("calle")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                        alert("codigoPostal")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                        alert("numeroExterior")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                        alert("ciudad")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                        alert("colonia")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                        alert("telefono")
                                    } else {
                                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                            alert("contacto de emergencia")
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
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
                                $scope.properties.selectedIndex++;
                            }
                        }
                    }
                } else {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.correoElectronico === undefined) {
                        alert("correo")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEscolaridad === null) {
                        alert("escolaridad")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatPais === null) {
                        alert("pais")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.idCatEstado === null) {
                        alert("estado")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.calle === undefined) {
                        alert("calle")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.codigoPostal === undefined) {
                        alert("codigoPostal")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.numeroExterior === undefined) {
                        alert("numeroExterior")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.ciudad === undefined) {
                        alert("ciudad")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.colonia === undefined) {
                        alert("colonia")
                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.padre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.padre.telefono === undefined) {
                        alert("telefono")
                    } else {
                        //VALIDAR MADRE 4
                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTitulo === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTitulo === null) {
                            alert("titulo");
                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.nombre === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.nombre === undefined) {
                            alert("nombre");
                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.apellidos === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.apellidos === undefined) {
                            alert("apellidos");
                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.vive === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.vive === null) {
                            alert("vive")
                        } else if ($scope.properties.datosPadres.madrevive) {
                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEgresoAnahuac === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEgresoAnahuac === null) {
                                alert("egreso")
                            } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatCampusEgreso === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatCampusEgreso === null) {
                                    alert("campus egreso")
                                } else {
                                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === null) {
                                        alert("trabaja")
                                    } else if ($scope.properties.datosPadres.madretrabaja) {
                                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === undefined) {
                                            alert("empresa")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === undefined) {
                                            alert("puesto")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                            alert("correo")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                            alert("escolaridad")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                            alert("pais")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                            alert("estado")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                            alert("calle")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                            alert("codigoPostal")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                            alert("numeroExterior")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                            alert("ciudad")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                            alert("colonia")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                            alert("telefono")
                                        } else {
                                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                                alert("contacto de emergencia")
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    } else {
                                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                            alert("correo")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                            alert("escolaridad")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                            alert("pais")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                            alert("estado")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                            alert("calle")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                            alert("codigoPostal")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                            alert("numeroExterior")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                            alert("ciudad")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                            alert("colonia")
                                        } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                            alert("telefono")
                                        } else {
                                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                                alert("contacto de emergencia")
                                            } else {
                                                if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                    $scope.properties.selectedIndex--;
                                                } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                    $scope.properties.selectedIndex++;
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {
                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === null) {
                                    alert("trabaja")
                                } else if ($scope.properties.datosPadres.madretrabaja) {
                                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === undefined) {
                                        alert("empresa")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === undefined) {
                                        alert("puesto")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                        alert("correo")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                        alert("escolaridad")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                        alert("pais")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                        alert("estado")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                        alert("calle")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                        alert("codigoPostal")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                        alert("numeroExterior")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                        alert("ciudad")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                        alert("colonia")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                        alert("telefono")
                                    } else {
                                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                            alert("contacto de emergencia")
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                                $scope.properties.selectedIndex++;
                                            }
                                        }
                                    }
                                } else {
                                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                        alert("correo")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                        alert("escolaridad")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                        alert("pais")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                        alert("estado")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                        alert("calle")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                        alert("codigoPostal")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                        alert("numeroExterior")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                        alert("ciudad")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                        alert("colonia")
                                    } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                        alert("telefono")
                                    } else {
                                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                            alert("contacto de emergencia")
                                        } else {
                                            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                                $scope.properties.selectedIndex--;
                                            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
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
                                $scope.properties.selectedIndex++;
                            }
                        }
                    }
                }

            } else {
                console.log("esta validando el la linea 373")
                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTitulo === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTitulo === null) {
                    alert("titulo");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.nombre === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.nombre === undefined) {
                    alert("nombre");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.apellidos === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.apellidos === undefined) {
                    alert("apellidos");
                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.vive === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.vive === null) {
                    alert("vive")
                } else if ($scope.properties.datosPadres.madrevive) {
                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEgresoAnahuac === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEgresoAnahuac === null) {
                        alert("egreso")
                    } else if ($scope.properties.datosPadres.madreegresoanahuac) {
                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatCampusEgreso === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatCampusEgreso === null) {
                            alert("campus egreso")
                        } else {
                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === null) {
                                alert("trabaja")
                            } else if ($scope.properties.datosPadres.madretrabaja) {
                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === undefined) {
                                    alert("empresa")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === undefined) {
                                    alert("puesto")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                    alert("correo")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                    alert("escolaridad")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                    alert("pais")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                    alert("estado")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                    alert("calle")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                    alert("codigoPostal")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                    alert("numeroExterior")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                    alert("ciudad")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                    alert("colonia")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                    alert("telefono")
                                } else {
                                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                        alert("contacto de emergencia")
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                }
                            } else {
                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                    alert("correo")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                    alert("escolaridad")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                    alert("pais")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                    alert("estado")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                    alert("calle")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                    alert("codigoPostal")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                    alert("numeroExterior")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                    alert("ciudad")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                    alert("colonia")
                                } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                    alert("telefono")
                                } else {
                                    if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                        alert("contacto de emergencia")
                                    } else {
                                        if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                            $scope.properties.selectedIndex--;
                                        } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                            $scope.properties.selectedIndex++;
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatTrabaja === null) {
                            alert("trabaja")
                        } else if ($scope.properties.datosPadres.madretrabaja) {
                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.empresaTrabaja === undefined) {
                                alert("empresa")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === undefined) {
                                alert("puesto")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                alert("correo")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                alert("escolaridad")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                alert("pais")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                alert("estado")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                alert("calle")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                alert("codigoPostal")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                alert("numeroExterior")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                alert("ciudad")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                alert("colonia")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                alert("telefono")
                            } else {
                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                    alert("contacto de emergencia")
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                                        $scope.properties.selectedIndex++;
                                    }
                                }
                            }
                        } else {
                            if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.correoElectronico === undefined) {
                                alert("correo")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEscolaridad === null) {
                                alert("escolaridad")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatPais === null) {
                                alert("pais")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === 0 || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.idCatEstado === null) {
                                alert("estado")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.calle === undefined) {
                                alert("calle")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.codigoPostal === undefined) {
                                alert("codigoPostal")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.numeroExterior === undefined) {
                                alert("numeroExterior")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.ciudad === undefined) {
                                alert("ciudad")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.colonia === undefined) {
                                alert("colonia")
                            } else if ($scope.properties.formInput.catSolicitudDeAdmisionInput.madre.puesto === "" || $scope.properties.formInput.catSolicitudDeAdmisionInput.madre.telefono === undefined) {
                                alert("telefono")
                            } else {
                                if ($scope.properties.formInput.catSolicitudDeAdmisionInput.contactoEmergencia.length === 0) {
                                    alert("contacto de emergencia")
                                } else {
                                    if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                                        $scope.properties.selectedIndex--;
                                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
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
                        $scope.properties.selectedIndex++;
                    }
                }
            }*/
if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                        $scope.properties.selectedIndex--;
                    } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                        $scope.properties.selectedIndex++;
                    }



        } else if ($scope.properties.selectedIndex === 4) {
            console.log("validar 4");
            if ($scope.properties.action === "Anterior" && $scope.properties.selectedIndex > 0) {
                $scope.properties.selectedIndex--;
            } else if ($scope.properties.action === "Siguiente" && $scope.properties.wizardLength > ($scope.properties.selectedIndex + 1)) {
                $scope.properties.selectedIndex++;
            }
        }

    }
}