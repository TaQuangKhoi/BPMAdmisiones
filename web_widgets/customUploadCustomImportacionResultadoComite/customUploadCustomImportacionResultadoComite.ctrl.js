function UploadCustomImportacionResultadoComite($scope, $http,blockUI) {
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
            datos.push(needValues(excelRows[i]))
        }
        console.log(datos)
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
        $scope.lstBanner = {'IDBANNER':""}
        try{
            blockUI.start();
            if(!isNullOrUndefined(row) ){
                row.forEach(datos =>{
                    count++;
                    var info = angular.copy(datos);
                    info.IDBANNER = info['número de matrícula'];
                    info.decision = info['decisión de admisión'];
                    //let paso = validacion(info);
                    if(validacion(info)){
                        $scope.lstBanner.IDBANNER += `${$scope.lstBanner.IDBANNER.length>0?",":""}'${info['IDBANNER']}'`;
                        $scope.correctos = [...$scope.correctos,info];
                    }

                    if(count === row.length){
                        if($scope.correctos.length > 0){
                            doRequest("POST",$scope.properties.urlPost,$scope.lstBanner).then(function() {
                                if(count == row.length){
                                    $scope.properties.lstErrores = angular.copy($scope.errores)
                                    $scope.properties.lstAlumnosResultados = angular.copy($scope.final)
                                    $scope.properties.tabla = "carga";
                                }
                            })
                        }else{
                            $scope.properties.lstErrores = angular.copy($scope.errores)
                            $scope.properties.tabla = "carga";
                        }
                        

                    }
                })
            

            }

        }catch(error){

        } finally{
            
            blockUI.stop();
        }
        
        
    }

    function needValues(data){
        let info= {};
        $scope.properties.revisar.forEach(valor =>{
            info = Object.assign({[valor]:(data[valor] || '')},info)
        })
        info.nombre = (data['nombre'] || '');
        info.observaciones = (data['observaciones'] || '');
        return info;
    }
    
    function validacion(datos){
        var error = "";
        if(datos !== null && datos !== undefined){
            if(!$scope.properties.tipoAdmision.some(el => datos['decisión de admisión'].includes(el))){
                error+=(error.length>0?", ":" ")+"decisión de admisión tiene que tener uno de los tres estatus: Aceptado, Rechazado, Revisión"
            } else{
                let columna = datos;
                for(var key in columna){
                    
                    if( $scope.properties.revisar.includes(key) && key != "IDBANNER" && key != "número de matrícula" && datos["decisión de admisión"]== "Aceptado"){
                        let name = key.split("_1");
                        if(isNullOrUndefined(datos[key])){
                            error+=(error.length>0?", ":" ")+"falta el dato "+name[0]
                        }else if(!SioNO(datos[key]) && key.includes("_1")){
                            
                            error+=(error.length>0?", ":" ")+name[0]+" tiene que ser Sí o No"
                            
                        } 
                    }else if(key == "IDBANNER" && isNullOrUndefined(datos['IDBANNER']) || datos['IDBANNER'].length < 8){
                        error+=(error.length>0?", ":" ")+"falta el dato id banner "
                    }
                }
            }
            if(error.length > 0){
                $scope.errores = [ ...$scope.errores,{idBanner:datos['IDBANNER'],nombre:datos['nombre'],Error:error}]
                return false;
            }
              return true;
        }
    }
    
    function contains(target, pattern){
        var value = 0;
        pattern.forEach(function(word){
            value = value + target.includes(word);
        });
        return (value === 1)
    }
    
    function isNullOrUndefined(dato){
        if(dato === undefined || dato === null || dato.length <= 0 ){
            return true;
        }
        return false
    }

    function SioNO(valor){
        if(valor === "Sí" || valor === "No" ){
            return true
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
                $scope.errores = [ ...$scope.errores,{idBanner:datos[indice]['IDBANNER'],nombre:datos[indice]['nombre'],Error:"no hay aspirante con ese idBanner",usuario:$scope.properties.usuario,existe:true}]
            }
            else if(info.Registrado){
                $scope.errores = [ ...$scope.errores,{idBanner:datos[indice]['IDBANNER'],nombre:datos[indice]['nombre'],Error:`el aspirante ya tiene decision`,usuario:$scope.properties.usuario,existe:true}]
            }
            else if(!info.EstaEnCarga){
                $scope.errores = [ ...$scope.errores,{idBanner:datos[indice]['IDBANNER'],nombre:datos[indice]['nombre'],Error:"el aspirante no se encuantra en carga y consulta de resultados",usuario:$scope.properties.usuario,existe:true}]
            }
            else{
                //hacer la conversion segun la tabla y guardar los valores originales para mostrar
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