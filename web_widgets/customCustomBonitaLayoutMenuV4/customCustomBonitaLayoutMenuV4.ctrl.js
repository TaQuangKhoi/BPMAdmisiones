function WidgetlivingApplicationMenuController($scope, $http, $window, $location, $timeout, modalService) {
    var ctrl = this;
    var vm = this;
    ctrl.goTo = function(_token) {
        window.location.href = "../" + token;
    }

    ctrl.filterChildren = function(parentId) {
        return (ctrl.applicationMenuList || []).filter(function(menu) {
            return menu.parentMenuId === '' + parentId;
        });
    };

    ctrl.isParentMenu = function(menu) {
        return menu.parentMenuId == -1 && menu.applicationPageId == -1;
    };

    ctrl.displayPage = function(token) {

        var previousToken = ctrl.pageToken;
        var previousPath = window.location.pathname;

        ctrl.pageToken = token;
        var urlPath = previousPath.substring(0, previousPath.length - previousToken.length - 1) + token + '/' + $window.location.search;
        var stateObject = { title: "" + token + "", url: "" + urlPath + "" };
        if (typeof($window.history.pushState) != "undefined") {
            //$window.history.pushState(stateObject, stateObject.title, stateObject.url );
        } else {
            alert("Browser does not support HTML5.");
        }
        //make sure the user is still logged in before refreshing the iframe
        verifySession().then(setTargetedUrl, refreshPage);

        return false;
    };

    ctrl.setParentPageActive = function(id) {

        ctrl.parentPageId = id;
        console.log("THIS IS THE PARENT PAGE ", ctrl.parentPageId);
    }

    ctrl.openCurrentSessionModal = function() {
        modalService.open($scope.properties.currentSessionModalId);
    };

    ctrl.openAppSelectionModal = function() {
        modalService.open($scope.properties.appSelectionModalId);
    };

    ctrl.logoutAndUpdateSuccessfulLogoutVariable = function() {
        return $http.get($scope.properties.logoutURL).success(function(data) {
            $scope.properties.successfulLogoutResponse = data;
        });
    };

    //handle the browser back button
    $window.addEventListener('popstate', function(e) {
        parseCurrentURL();
        //make sure the user is still logged in before refreshing the iframe
        setTargetedUrl();
        refreshPage();
    });

    function parseCurrentURL() {
        var pathArray = $window.location.pathname.split('/');
        ctrl.applicationToken = pathArray[pathArray.length - 3];
        ctrl.pageToken = pathArray[pathArray.length - 2];
    }

    function setApplicationMenuList(application) {
        return $http.get('../API/living/application-menu/?c=9999&f=applicationId%3D' + application.id + '&d=applicationPageId&o=menuIndex+ASC').success(function(data) {
            ctrl.applicationMenuList = data;
        });
    }

    function searchSeparator() {
        return $window.location.search ? "&" : "?";
    }

    function setTargetedUrl() {
        // angular hack to force the variable bound to refresh
        // so we change it's value to undefined and then delay to the correct value
        $scope.properties.targetUrl = undefined;
        $timeout(function() {
            $scope.properties.targetUrl = "/bonita/portal/resource/app/" + ctrl.applicationToken + "/" + ctrl.pageToken + "/content/" + $window.location.search + searchSeparator() + "app=" + ctrl.applicationToken;
        }, 0);
    }

    function refreshPage() {
        $window.location.reload();
    }

    function verifySession() {
        var userIdentity = '../API/identity/user/' + $scope.properties.userId;
        return $http.get(userIdentity);
    }

    function setApplication() {
        var application = $scope.properties.application;
        ctrl.applicationToken = application.token;
        ctrl.pageToken = $scope.properties.pageToken;
        ctrl.applicationName = $scope.properties.application.displayName;
        setApplicationMenuList(application);
        setTargetedUrl();
    }
    /**
     * Execute a get/post request to an URL
     * It also bind custom data from success|error to a data
     * @return {void}
     */
    function doRequest(method, url, params, dataToSend, callback) {
        vm.busy = true;
        var req = {
            method: method,
            url: url,
            data: dataToSend,
            params: params
        };

        return $http(req)
            .success(function(data, status) {
                callback(data)
            })
            .error(function(data, status) { console.error(data) })
            .finally(function() {
                vm.busy = false;
            });
    }
    $scope.getMenuAdministrativo = [];
    doRequest("GET", "/bonita/API/extension/AnahuacRestGet?url=getMenuAdministrativo&p=0&c=10", null, null, function(datos) {
        $scope.getMenuAdministrativo = datos;
    })

    setApplication();
}