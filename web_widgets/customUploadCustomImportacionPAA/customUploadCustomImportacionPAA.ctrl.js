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
        
        
        if (reader.readAsBinaryString) {
            reader.onload = function (e) {
                ProcessExcel(e.target.result);
            };
                reader.readAsBinaryString(file);
            } else {
                //For IE Browser.
                reader.onload = function (e) {
                    var data = "";
                    var bytes = new Uint8Array(e.target.result);
                    for (var i = 0; i < bytes.byteLength; i++) {
                        data += String.fromCharCode(bytes[i]);
                    }
                    ProcessExcel(data);
                };
                reader.readAsArrayBuffer(file);
            }
        //reader.onloadend = function () {
            
          //  reader.onload = function (e) {
            //    ProcessExcel(e.target.result);
        //    };
            
            
            //console.log(file);
            //$scope.fileName = file.name;
            
            // Since it contains the Data URI, we should remove the prefix and keep only Base64 string
            /*var b64 = reader.result.replace(/^data:.+;base64,/, '');
            let fileObject = {
                "fileBase64": b64
            }
            uploadFile(fileObject);*/
        //};
        
        //reader.readAsDataURL(file);
    };
    
    function uploadFile(fileObject){
        let url = "/bonita/API/extension/AnahuacRest?url=getInformacionResultado&p=0&c=100";
        $http.post(url, fileObject).success(function(succaess){
            $scope.properties.lstAlumnosResultados = success.data;
            $scope.properties.totalRows = success.data.length;
        }).error(function(){
            $scope.cleanInput();
            swal("Error", "El archivo proporcionado no es válido, favor de subir un archivo de resultados válido y en formato excel (.xlsx).", "error");
        });
    }
    
    function ProcessExcel(data) {
        //Read the Excel File data.
        var workbook = XLSX.read(data, {
            type: 'binary'
        });
 
        //Fetch the name of First Sheet.
        var firstSheet = workbook.SheetNames[0];
        console.log(firstSheet)
        //Read all rows from First Sheet into an JSON array.
        var excelRows = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[firstSheet]);
        console.log(excelRows)
 
 
        //Add the data rows from Excel file.
        for (var i = 0; i < excelRows.length; i++) {
            $scope.properties.lstAlumnosResultados.push(excelRows[i])
        }
        $scope.$apply();
    }
    
}