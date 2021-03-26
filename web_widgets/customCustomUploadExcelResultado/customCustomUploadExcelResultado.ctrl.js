function ($scope, $http) {
    $scope.fileName = "";
    
    var input = document.querySelector('input[type=file]');
    
    $scope.cleanInput = function(){
        var inputToClear = document.querySelector('input[type=file]');
        inputToClear.type = "text";
        inputToClear.type = "file";
    };
    
    input.onchange = function () {
        var file = input.files[0],
        reader = new FileReader();
        
        reader.onloadend = function () {
            console.log(file);
            $scope.fileName = file.name;
            // Since it contains the Data URI, we should remove the prefix and keep only Base64 string
            var b64 = reader.result.replace(/^data:.+;base64,/, '');
            let fileObject = {
                "fileBase64": b64
            }
            uploadFile(fileObject);
        };
        
        reader.readAsDataURL(file);
    };
    
    function uploadFile(fileObject){
        let url = "/bonita/API/extension/AnahuacRest?url=getInformacionResultado&p=0&c=100";
        $http.post(url, fileObject).success(function(success){
            $scope.properties.lstAlumnosResultados = success.data;
            $scope.properties.totalRows = success.data.length;
        }).error(function(){
            $scope.cleanInput();
            swal("Error", "El archivo proporcionado no es válido, favor de subir un archivo de resultados válido y en formato excel (.xlsx).", "error");
        });
    }
}