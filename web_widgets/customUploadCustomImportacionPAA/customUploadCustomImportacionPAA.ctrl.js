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
        $scope.lstBanner = {'IDBANNER':"", "FECHA":""};

        blockUI.start();
        if(!isNullOrUndefined(row) ){
            row.forEach(datos =>{
                count++;
                var info = angular.copy(datos);
                info.fechaExamen = info['Fecha de examen']
                let paso = validacion(info);
                
                if(paso){
                    $scope.lstBanner.IDBANNER += `${$scope.lstBanner.IDBANNER.length>0?",":""}'${info['IDBANNER']}'`;
                    $scope.lstBanner.FECHA += `${$scope.lstBanner.FECHA.length>0?",":""}'${info['fechaExamen']}'`;
                    info.tipoExamen = "KP";
                    info.INVP = "";
                    $scope.correctos = [...$scope.correctos,info];
                }

                if(count === row.length){
                    if($scope.correctos.length > 0){
                        doRequest("POST",$scope.properties.urlPost,$scope.lstBanner).then(function() {
                            if(count == row.length){
                                $scope.properties.lstErrores = angular.copy($scope.errores)
                                $scope.properties.lstAlumnosResultados = angular.copy($scope.final)
                                $scope.properties.tabla = "carga";
                                //swal('¡Se han terminado la auditoria de los datos!',"","success")
                            }
                        })
                    }else{
                            $scope.properties.lstErrores = angular.copy($scope.errores)
                            $scope.properties.lstAlumnosResultados = [];
                            $scope.properties.tabla = "carga";

                    }
                    
                        
                }
            })
          
             
        }
        
        blockUI.stop();
        
    }
    
    function validacion(data){
        var datos = data;
        var error = "";
        if(datos !== null && datos !== undefined){
            let columna = datos;
            for(var key in columna){
                if( $scope.properties.revisar.includes(key) && key != "fechaExamen" && key != "IDBANNER"){
                    //json[key.toUpperCase()] = data[key]
                    if(isNullOrUndefined(data[key])){
                        error+=(error.length>0?",":"")+"falta el dato "+key
                    }
                    
                }else if(key == "IDBANNER" && isNullOrUndefined(data['IDBANNER']) || data['IDBANNER'].length < 8){
                    error+=(error.length>0?",":"")+"falta el dato id banner"
                }
            }
            
            if(error.length > 0){
                $scope.errores = [ ...$scope.errores,{idBanner:data['IDBANNER'],nombre:data['Nombre'],Error:error}]
                return false;
              }
              return true;
        }
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
        data.data.forEach( (info,index) =>{
            //let lstidBanner = info.idBanner.split(',')
            let indice = findData(info.idBanner.replaceAll("'",""))
            if(!info.Existe){
                $scope.errores = [ ...$scope.errores,{idBanner:datos[indice]['IDBANNER'],nombre:datos[indice]['Nombre'],Error:"no hay aspirante con ese idBanner"}]
            }
            else if(info.mismaFecha){
                $scope.errores = [ ...$scope.errores,{idBanner:datos[indice]['IDBANNER'],nombre:datos[indice]['Nombre'],Error:`el aspirante ya tiene puntuacion en la fecha ${datos[indice]['fechaExamen']}`}]
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
                datos[indice]['LEXIUM_Total'] = datos[indice]['Total']
                datos[indice]['Total'] = convertirDato(datos[indice]['Total'])
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
    
    
    function sliceDate(date){
        
        return `${date.slice(0,2)}-${date.slice(3,5)}-${date.slice(6,11)}`
    }
    
   
    
}