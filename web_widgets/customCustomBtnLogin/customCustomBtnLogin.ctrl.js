function PbButtonCtrl($scope, $http, $location, $log, $window, localStorageService, modalService) {

    'use strict';
  
    var vm = this;
    $scope.traduccion = "";
    $scope.titleSwal = "";
    var captchaElement;
    $scope.errorLoginCount = 0;
    $scope.isCaptchaLoaded = false;
    $scope.dataIdioma = "";
    var idioma = localStorage.getItem("idioma");
    
    $scope.showLoading = function(){
        $("#loading").modal("show");
    }
    
    $scope.hideLoading = function(){
        $("#loading").modal("hide");
    }
    
    $scope.renderCaptcha = function(){
        $scope.showLoading();
        $scope.isCaptchaLoaded = false;
        captchaElement = grecaptcha.render(document.getElementById('captchaElement'), {
            'sitekey' : $scope.properties.reCAPTCHAkey
        });
        
        $scope.isCaptchaLoaded = true;
        
        setTimeout(function(){
            if($(window).width() >= 768){
                let height = $(".b-left").height() + "px";
                $(".re-dimension").css("height", height);
            }
        }, 200);
        
        $scope.hideLoading();
    }
    
    $scope.resetCaptcha = function(){
        $scope.showLoading();
        $scope.isCaptchaLoaded = false;
        grecaptcha.reset();   
        $scope.isCaptchaLoaded = true;
        
        setTimeout(function(){
            if($(window).width() >= 768){
                let height = $(".b-left").height() + "px";
                $(".re-dimension").css("height", height);
            }
        }, 200);
        
        $scope.hideLoading();
    };
    
    this.action = function action() {
        
        if($scope.errorLoginCount === 2){
            let captchaResponse = grecaptcha.getResponse();
            
            if(captchaResponse !== ""){
                // doRequest($scope.properties.action, $scope.properties.url);
                $scope.validateForm();
            } else {
                $scope.cambioIdioma("errorCaptcha");
                Swal.fire($scope.titleSwal, $scope.traduccion , "warning");
                $scope.resetCaptcha();
            }
        } else {
            // doRequest($scope.properties.action, $scope.properties.url);
            $scope.validateForm();
        }
    };
    
    $scope.validateForm = function(){
        let username = $scope.properties.dataToSend.username;
        let password = $scope.properties.dataToSend.password;
  
        // const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        const re = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
        $scope.getIdiomaLogin($scope.properties.dataToSend.username);
        if(username  === ""){
            $scope.cambioIdioma("errorCorreo");
            Swal.fire($scope.titleSwal, $scope.traduccion , "warning");
        } else if (!re.test(String(username))){
            $scope.cambioIdioma("errorFormatoCorreo");
            Swal.fire($scope.titleSwal, $scope.traduccion , "warning");
        } else if (password === ""){
            $scope.cambioIdioma("errorConstrasena");
            Swal.fire($scope.titleSwal, $scope.traduccion , "warning");
        } 
        
        // else if (password.length > 8){
        //     Swal.fire("Error", "Tu contraseña es muy corta", "warning");
        // } 
        
        else {
            doRequest($scope.properties.action, $scope.properties.url);
        }
    }
  
    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params) {
        $scope.showLoading();
        vm.busy = true;
      
        // let data = {
        //     "username" : $scope.properties.dataToSend.username,
        //     "password" : $scope.properties.dataToSend.password
        // }

        let data = "redirect=false&username=" + $scope.properties.dataToSend.username + "&password=" + $scope.properties.dataToSend.password;

        // let url2 = "/bonita/loginservice?&redirect=false&username=" + $scope.properties.dataToSend.username + "&password=" + $scope.properties.dataToSend.password;
      
        let url2 = "/bonita/loginservice";

        var req = {
            method: method,
            url: url2,
            data: data,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        };
      
        return $http(req).success(function(data, status) {
            $scope.properties.dataFromSuccess = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromError = undefined;
            
            redirectIfNeeded();
        })
        .error(function(data, status) {
        	console.error(data);
        	if(idioma == "ESP") {
            Swal.fire({
                title: '<strong>Atención</strong>',
                icon: 'error',
                html:($scope.properties.targetUrlOnSuccess.includes('administrativo'))?'Correo electronico o Contraseña incorrecta.':'Correo electronico o Contraseña incorrecta. <br><br><br><br><p class="swal2-title">Recuerda</p> <p>Si iniciaste tu registro <strong>hasta</strong> el jueves 29 de abril del 2021 <br>da clic aquí </p>' + '<a class="btn btn-primary" href="https://servicios.redanahuac.mx/admisiones.php">Iniciar sesión</a> ', showCloseButton: false
            });
        	} else {
        	    Swal.fire({
                title: '<strong>Attention</strong>',
                icon: 'error',
                html:($scope.properties.targetUrlOnSuccess.includes('administrativo'))?'Incorrect email or password.':'Incorrect email or password. <br><br><br><br><p class="swal2-title">Remember</p> <p>If you started your registration <strong>before</strong> Thursday, April 29, 2021 <br>click here </p>' + '<a class="btn btn-primary" href="https://servicios.redanahuac.mx/admisiones.php">Log in</a> ', showCloseButton: false
            });
        	}
            $scope.errorLoginCount ++;
            if($scope.errorLoginCount === 2){
                $scope.renderCaptcha();
            } else if ($scope.errorLoginCount > 2){
                $scope.resetCaptcha();
            }
            $scope.properties.dataFromError = data;
            $scope.properties.responseStatusCode = status;
            $scope.properties.dataFromSuccess = undefined;
            
        })
        .finally(function() {
            $scope.hideLoading();
            vm.busy = false;
        });
    }
    
    function redirectIfNeeded() {
        let ipBonita = window.location.protocol + "//" + window.location.host;
        let url = ipBonita + $scope.properties.targetUrlOnSuccess;
        window.top.location.href = url;
    }

    $scope.cambioIdioma = function(text) {
        if (idioma == "ESP") {
            $scope.titleSwal = "¡Atención!";
            switch (text) {
                case "errorCorreo":
                    $scope.traduccion = "El Correo electrónico no debe ir vacío.";
                    break;
                case "errorCaptcha":
                    $scope.traduccion = "Captcha inválido.";
                    break;
                case "errorFormatoCorreo":
                    $scope.traduccion = "El formato de correo electrónico es inválido.";
                    break;
                case "errorConstrasena":
                    $scope.traduccion = "La Contraseña no debe ir vacía.";
                    break;
            }
        } else {
            $scope.titleSwal = "Attention!";
            switch (text) {
                case "errorCorreo":
                    $scope.traduccion = "The Email must not be empty.";
                    break;
                case "errorCaptcha":
                    $scope.traduccion = "Invalid Captcha.";
                    break;
                case "errorFormatoCorreo":
                    $scope.traduccion = "The email format is invalid.";
                    break;
                case "errorConstrasena":
                    $scope.traduccion = "The Password must not be empty.";
                    break;
            }
        }
    }
    
    $scope.getIdiomaLogin = function(username) {
        debugger;
        var req = {
            method: "GET",
            url: "../API/extension/AnahuacRestGet?url=getIdiomaByUsername&username=" + username
        };

        return $http(req).success(function(data, status) {
            $scope.dataIdioma = data;
            localStorage.setItem("idioma", $scope.dataIdioma.data[0].idioma);
            })
            .error(function(data, status) {
                console.log("Error-idioma: ", data, "Estado: ", status);
            })
            .finally(function() {
                
            });
    }
    
  }