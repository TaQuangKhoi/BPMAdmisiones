function PbButtonCtrl($scope, $http) {
    var idioma = localStorage.getItem("idioma");
    $scope.titleSwal = "";
    $scope.titleCheckEmailb = "";
    $scope.traduccion = "";
    
    $scope.action = function(){
        if(validate()){
            $scope.cambioPasswordBonita();
        }
    };
    
    function validate(){
        const regexEmail = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
        let isValid = true;
        let errorMessage = "";
        if($scope.properties.objUser.email === ""){
            isValid = false;
            $scope.cambioIdioma("errorLlenadoCorreo");
            errorMessage = $scope.traduccion

        } else if (!regexEmail.test(String($scope.properties.objUser.email))){
            isValid = false;
            $scope.cambioIdioma("errorFormatoCorreo");
            errorMessage = $scope.traduccion
        }
        
        if(!isValid){
                $scope.cambioIdioma("default");
                Swal.fire($scope.titleSwal, errorMessage, "warning");
        }
        
        return isValid;
    }
    
    $scope.cambioPasswordBonita = function() {
        let data = {
            "nombreusuario": $scope.properties.objUser.email,
            "password": "password"
        };
        
        var req = {
            method: "POST",
            url: "/bonita/API/extension/AnahuacRest?url=recuparaPassword&p=0&c=10",
            data: data
        };

        return $http(req).success(function(data, status) {
            if(data.success){
                $scope.properties.navigationVar = "login";
                $scope.cambioIdioma("revisarCorreo");
                    let message = $scope.traduccion;
                    Swal.fire($scope.titleCheckEmail , message, "success");
    
            } else {
                Swal.fire("Error.", data.error, "error");
            }
        })
        .error(function(data, status) {
            $scope.cambioIdioma("errorInesperado");
            let errorMessage = $scope.traduccion;

            if(data.error.includes("SUserNotFoundException")){
                $scope.cambioIdioma("errorCorreoNoRegistrado");
                errorMessage = $scope.traduccion;
            }
            Swal.fire("Error.", errorMessage, "error");
           // notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
        })
        .finally(function() {
            
        });
    }
    
    $scope.cambioIdioma = function(text) {
        if (idioma == "ESP") {
            $scope.titleSwal = "¡Atención!";
            $scope.titleCheckEmail = "Ya casi está listo.";
            switch (text) {
                case "errorLlenadoCorreo":
                    $scope.traduccion = "Por favor llena el campo Correo electrónico .";
                    break;
                case "errorFormatoCorreo":
                    $scope.traduccion = "El formato del correo electrónico no es el correcto.";
                    break;
                case "revisarCorreo":
                    $scope.traduccion = "Revisa tu bandeja de entrada para continuar con el proceso de recuperación de contraseña";
                    break;
                case "errorInesperado":
                    $scope.traduccion = "Ocurrió un error inesperado. Inténtalo de nuevo mas tarde.";
                    break;
                case "errorCorreoNoRegistrado":
                    $scope.traduccion = "El Correo electrónico ingresado no está registrado.";
                    break;
                case "default":
                    $scope.traduccion = "";
                    break;
            }
        } else {
            $scope.titleSwal = "Attention!";
            $scope.titleCheckEmail = "It's almost ready.";
            switch (text) {
                case "errorLlenadoCorreo":
                    $scope.traduccion = "Please fill in the Email field.";
                    break;
                case "errorFormatoCorreo":
                    $scope.traduccion = "The email format is not correct.";
                    break;
                case "revisarCorreo":
                    $scope.traduccion = "Check your inbox to continue with the password recovery process.";
                    break;
                case "errorInesperado":
                    $scope.traduccion = "An unexpected error occurred. Try again later.";
                    break;
                case "errorCorreoNoRegistrado":
                    $scope.traduccion = "The email entered is not registered.";
                    break;
                case "default":
                    $scope.traduccion = "";
                    break;
            }
        }
    }
}
