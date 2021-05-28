function UploadCustomImportacionPAA($scope, $http,blockUI) {
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
        
        var datos = [];
        //Add the data rows from Excel file.
        for (var i = 0; i < excelRows.length; i++) {
            datos.push(excelRows[i])
        }
        auditoria(datos)
        $scope.$apply();
    }
    
    $scope.errores = [];
    $scope.correctos = [];
    $scope.final = [];
    $scope.lstBanner = {'IDBANNER':""};
    function auditoria(row){
        
        var count = 0;
        $scope.errores = [];
        $scope.correctos = [];
        $scope.final = [];
        $scope.lstBanner = {'IDBANNER':""};

        blockUI.start();
        if(!isNullOrUndefined(row) ){
            row.forEach(datos =>{
                count++;
                var info = angular.copy(datos);
                info.fechaExamen = info['Fecha de examen']
                let paso = validacion(info);
                
                if(paso){
                    $scope.lstBanner.IDBANNER += `${$scope.lstBanner.length>0?",":""}'${info['IDBANNER']}'`;

                    $scope.correctos = [...$scope.correctos,info]
                }

                if(count === row.length){
                    doRequest("POST",$scope.properties.urlPost,$scope.lstBanner).then(function() {
                        if(count == row.length){
                            $scope.properties.lstErrores = angular.copy($scope.errores)
                            $scope.properties.lstAlumnosResultados = angular.copy($scope.final)
                            $scope.properties.tabla = "tabla";
                            //swal('¡Se han terminado la auditoria de los datos!',"","success")
                        }
                    })
                        
                }
            })
          
             
        }
        blockUI.stop();
        
    }
    
    function validacion(dato){
        
        var error = "";
        
        if( isNullOrUndefined(dato['IDBANNER']) || dato['IDBANNER'].length < 8){
            error+=(error.length>0?",":"")+"falta el dato id banner"
        }
        if(isNullOrUndefined(dato['Total']) ){
            error+=(error.length>0?",":"")+"falta el dato Puntuación Total"
        }
        if(isNullOrUndefined(dato['Fecha de examen']) ){
            error+=(error.length>0?",":"")+"falta el dato de la fecha de examen"
            
        }
        if(isNullOrUndefined(dato['PAAN']) ){
            error+=(error.length>0?",":"")+"falta el dato PAAN"
            
        }
        
        if(isNullOrUndefined(dato['PAAV']) ){
           error+=(error.length>0?",":"")+"falta el dato PAAV"
             
        }
        
        if(isNullOrUndefined(dato['PARA']) ){
           error+=(error.length>0?",":"")+"falta el dato PARA"
        }
        
        if(isNullOrUndefined(dato['LA1']) ){
           error+=(error.length>0?",":"")+"falta el dato LA1"
        }
        
        if(isNullOrUndefined(dato['LA2']) ){
           error+=(error.length>0?",":"")+"falta el dato LA2"
        }
        
        if(isNullOrUndefined(dato['LA3']) ){
           error+=(error.length>0?",":"")+"falta el dato LA3"
        }
        
        if(isNullOrUndefined(dato['PG1']) ){
           error+=(error.length>0?",":"")+"falta el dato PG1"
             
        }
        
        if(isNullOrUndefined(dato['PG2']) ){
           error+=(error.length>0?",":"")+"falta el dato PG2"
        }
        
        if(isNullOrUndefined(dato['PG3']) ){
           error+=(error.length>0?",":"")+"falta el dato PG3"
        }
        
        if(isNullOrUndefined(dato['PG4']) ){
           error+=(error.length>0?",":"")+"falta el dato PG4"
        }
        
        if(isNullOrUndefined(dato['PV1']) ){
           error+=(error.length>0?",":"")+"falta el dato PV1"
        }
        
        if(isNullOrUndefined(dato['PV2']) ){
           error+=(error.length>0?",":"")+"falta el dato PV2"
        }
        
        if(isNullOrUndefined(dato['PV3']) ){
           error+=(error.length>0?",":"")+"falta el dato PV3"
        }
        
        if(isNullOrUndefined(dato['PE1']) ){
           error+=(error.length>0?",":"")+"falta el dato PE1"
        }
        
        if(isNullOrUndefined(dato['PE2']) ){
           error+=(error.length>0?",":"")+"falta el dato PE2"
        }
        
        if(isNullOrUndefined(dato['PE3']) ){
           error+=(error.length>0?",":"")+"falta el dato PE3"
        }
        
        if(isNullOrUndefined(dato['PE4']) ){
           error+=(error.length>0?",":"")+"falta el dato PE4"
        }
        
        if(isNullOrUndefined(dato['LEO1']) ){
           error+=(error.length>0?",":"")+"falta el dato LEO1"
        }
        
        if(isNullOrUndefined(dato['LEO2']) ){
           error+=(error.length>0?",":"")+"falta el dato LEO2"
        }
        
        if(isNullOrUndefined(dato['LEO3']) ){
           error+=(error.length>0?",":"")+"falta el dato LEO3"
        }
        
        if(isNullOrUndefined(dato['LEO4']) ){
           error+=(error.length>0?",":"")+"falta el dato LEO4";
        }
        
        if(isNullOrUndefined(dato['LEO5']) ){
           error+=(error.length>0?",":"")+"falta el dato LEO5"
        }
        
        if(isNullOrUndefined(dato['CIT1']) ){
           error+=(error.length>0?",":"")+"falta el dato CIT1"
        }
        
        if(isNullOrUndefined(dato['CIT2']) ){
           error+=(error.length>0?",":"")+"falta el dato CIT2"
        }
        
        if(isNullOrUndefined(dato['HI1']) ){
           error+=(error.length>0?",":"")+"falta el dato HI1"
        }
        if(isNullOrUndefined(dato['HI2']) ){
           error+=(error.length>0?",":"")+"falta el dato HI2"
        }
        
        if(isNullOrUndefined( dato['HI3']) ){
           error+=(error.length>0?",":"")+"falta el dato HI3"
        }
        if(isNullOrUndefined(dato['HI4']) ){
           error+=(error.length>0?",":"")+"falta el dato HI4"
        }
        if(isNullOrUndefined(dato['HI5']) ){
           error+=(error.length>0?",":"")+"falta el dato HI5"
        }
        if(isNullOrUndefined(dato['HI6']) ){
           error+=(error.length>0?",":"")+"falta el dato HI6";
        }
        if(error.length > 0){
          $scope.errores = [ ...$scope.errores,{idBanner:dato['IDBANNER'],nombre:dato['Nombre'],Error:error}]
          return false;
        }
        return true;
    }
    
    function isNullOrUndefined(dato){
        if(dato === undefined || dato === null || dato.length <= 0 ){
            return true;
        }
        return false
    }
    
    
    function doRequest(method, url,datos) {
        var req = {
            method: method,
            url: url,
            data: angular.copy(datos)
        };
        return $http(req)
            .success(function (data, status) {
                revisarDatos(data,$scope.correctos)
            })
            .error(function (data, status) {
            })
    }


    function revisarDatos(data,datos){
        debugger;
        data.data.forEach( (info,index) =>{
            let indice = findData(info.idBanner.replaceAll("'",""))
            if(!info.Existe){
                $scope.errores = [ ...$scope.errores,{idBanner:datos[indice]['IDBANNER'],nombre:datos[indice]['Nombre'],Error:"no hay aspirante con ese idBanner"}]
            }
            else if(info.Registrado){
                $scope.errores = [ ...$scope.errores,{idBanner:datos[indice]['IDBANNER'],nombre:datos[indice]['Nombre'],Error:"el aspirante ya tiene puntuacion"}]
            }
            else if(!info.EstaEnCarga){
                $scope.errores = [ ...$scope.errores,{idBanner:datos[indice]['IDBANNER'],nombre:datos[indice]['Nombre'],Error:"el aspirante no se encuantra en carga y consulta de resultados"}]
            }
            else{
                //hacer la conversion segun la tabla y guardar los valores originales para mostrar
                datos[indice]['LEXIUM_PAAN'] = datos[indice]['PAAN']
                datos[indice]['PAAN'] = convertirDato(datos[indice]['PAAN'])
                datos[indice]['LEXIUM_PAAV'] = datos[indice]['PAAV']
                datos[indice]['PAAV'] = convertirDato(datos[indice]['PAAV'])
                datos[indice]['LEXIUM_PARA'] = datos[indice]['PARA']
                datos[indice]['PARA'] = convertirDato(datos[indice]['PARA'])
                $scope.final = [ ...$scope.final,datos[indice]]
            }
        })
        
    }


    function findData(valor){
        let index = $scope.correctos.findIndex(function(item,i){
           return item.IDBANNER === valor
        });
        return  index
    }
    
}