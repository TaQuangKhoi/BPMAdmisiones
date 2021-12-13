function PbSelectCtrl($scope, $parse, $log, widgetNameFactory, $timeout, $window, $element) {
    var ctrl = this;

    $scope.picture = "";
    var local = "";
    var idiomaSelected = localStorage.getItem('idioma');
    localStorage.setItem('idioma', "ESP");

    $scope.$watch("local", function(value) {
        $scope.checkValue();
    });

    this.setCookie = function(keyCode) {
        if (keyCode !== null || keyCode !== "") {
            localStorage.setItem('idioma', keyCode);
            switch (keyCode) {
                case "ESP":
                    language = document.cookie = "BOS_Locale=es_ES;path=/";
                    location.reload();
                    break;
                case "ENG":
                    language = document.cookie = "BOS_Locale=en_EN;path=/";
                    location.reload();
                    break;
            }
        }
    }

    $scope.checkValue = function() {
        debugger
        local = localStorage.getItem('idioma');
        if (local === undefined || local === null || local === "") {
            $scope.picture = $scope.properties.banderaSpain;
        } else if (idiomaSelected !== "" && idiomaSelected != local) {
            switch (idiomaSelected) {
                case "ESP":
                    $scope.picture = $scope.properties.banderaSpain;
                    localStorage.setItem('idioma', idiomaSelected);
                    break;
                case "ENG":
                    $scope.picture = $scope.properties.banderaEU;
                    localStorage.setItem('idioma', idiomaSelected);
                    break;
            }
        } else {
            switch (idiomaSelected) {
                case "ESP":
                    $scope.picture = $scope.properties.banderaSpain;
                    localStorage.setItem('idioma', idiomaSelected);
                    break;
                case "ENG":
                    $scope.picture = $scope.properties.banderaEU;
                    localStorage.setItem('idioma', idiomaSelected);
                    break;
            }
        }
        var cookieValue = $scope.readCookie('BOS_Locale');
        if(idiomaSelected == "ENG" && cookieValue == "es" || cookieValue == "ES") {
            $scope.picture = $scope.properties.banderaSpain;
            localStorage.setItem('idioma', "ESP");
        }
    }
    
    $scope.readCookie = function (cookieName) {
        var cookiestring=RegExp(""+cookieName+"[^;]+").exec(document.cookie);
        return decodeURIComponent(!!cookiestring ? cookiestring.toString().replace(/^[^=]+./,"") : "");
    }
}