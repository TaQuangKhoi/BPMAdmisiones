function($scope, $http) {
    $(function() {
        var req = {
            method: "POST",
            url: '../API/extension/AnahuacRest?url=getApiCrispChat&p=0&c=100',
            data: {}
        };

        return $http(req)
            .success(function(data, status) {
                window.$crisp = [];
                window.CRISP_WEBSITE_ID = data.data[0];
                (
                    function() {
                        d = document;
                        s = d.createElement("script");
                        s.src = "https://client.crisp.chat/l.js";
                        s.async = 1;
                        d.getElementsByTagName("head")[0].appendChild(s);
                    }
                )();
            })
            .error(function(data, status) {
                notifyParentFrame({ message: 'error', status: status, dataFromError: data, dataFromSuccess: undefined, responseStatusCode: status });
            }).finally(function() {
                blockUI.stop();
            });


    });
}