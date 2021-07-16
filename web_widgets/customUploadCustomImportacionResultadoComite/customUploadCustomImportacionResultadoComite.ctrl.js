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
        
        if(excelRows.length >= 1){
            var datos = [];
            //Add the data rows from Excel file.
            for (var i = 0; i < excelRows.length; i++) {
                datos.push(needValues(excelRows[i]));
            }
            auditoria(datos)
            $scope.$apply(); 
        }else{
            swal("Error", "El archivo se encuentra en blanco", "error");
        }
    }
    
    $scope.errores = [];
    $scope.correctos = [];
    $scope.final = [];
    $scope.lstBanner = {'IDBANNER':"","PERIODO":""};
    function auditoria(row){
        var count = 0;
        $scope.errores = [];
        $scope.correctos = [];
        $scope.final = [];
        $scope.lstBanner = {'IDBANNER':"","PERIODO":""};
        try{
            blockUI.start();
            if(!isNullOrUndefined(row) ){
                row.forEach(datos =>{
                    count++;
                    var info = angular.copy(datos);
                    info.IDBANNER = info['Número de matrícula'];
                    info.decision = info['Decisión de admisión'];
                    if(validacion(info)){
                        $scope.correctos = [...$scope.correctos,info];
                    }
                    if(count === row.length){
                        $scope.correctos = findDuplicados($scope.correctos,"IDBANNER");
                        cargarlstBanner($scope.correctos);
                        if($scope.correctos.length > 0){
                            doRequest("POST",$scope.properties.urlPost,$scope.lstBanner).then(function() {
                                if(count == row.length){
                                    $scope.properties.lstErrores = angular.copy($scope.errores);
                                    $scope.properties.lstAlumnosResultados = angular.copy($scope.final);
                                    $scope.properties.tabla = "carga";
                                }
                            });
                        }else{
                            $scope.properties.lstErrores = angular.copy($scope.errores);
                            $scope.properties.lstAlumnosResultados = [];
                            $scope.properties.tabla = "carga";
                        }
                    }
                });
            }
        }catch(error){
        } finally{
            blockUI.stop();
        }
    }

    function needValues(data){
        let info= {};
        $scope.properties.revisar.forEach(valor =>{
            if(valor.includes("_1")){
                info = Object.assign({[valor]:(data[valor] || 'No')},info)
            }else{
                info = Object.assign({[valor]:(data[valor] || '')},info)
            }
        })
        info.nombre = (data.Nombre || '');
        info.observaciones = (data.Observaciones || '');
        info.isAdmitido = false;
        info.Sesion = (data["Sesión"] || '');
        info.isPropedeutico = false;
        return info;
    }
    
    function validacion(datos){
        var error = "";
        if(datos !== null && datos !== undefined){
            if(!$scope.properties.lstDecision.some(el =>  datos['Decisión de admisión'].includes(el.clave) )){
                let lstTipoDecision = "";
                $scope.properties.lstDecision.forEach(element => {
                    lstTipoDecision+= (lstTipoDecision.length>0?", ":" ")+`${element.clave}`;
                });
                error+=(error.length>0?", ":" ")+"Decisión de admisión tiene que tener uno de los estatus: "+lstTipoDecision;
            } else{
                let columna = datos;
                $scope.properties.lstDecision.forEach(element =>{
                     if(datos['Decisión de admisión'] == element.clave ){
                        datos.isAdmitido = element.isAdmitido;
                        datos.isPropedeutico = (element.clave == "AP"? true : false);
                     }
                });
                for(var key in columna){
                    
                    if( $scope.properties.revisar.includes(key) && key != "IDBANNER" && key != "número de matrícula" && datos.isAdmitido ){
                        let name = key.split("_1");
                        if(isNullOrUndefined(datos[key])){
                            error+=(error.length>0?", ":" ")+"falta el dato "+name[0]
                        }else if(!SioNO(datos[key]) && key.includes("_1")){
                            error+=(error.length>0?", ":" ")+name[0]+" tiene que ser Sí o No"
                        }
                    }else if(key == "IDBANNER"){
                        if(isNullOrUndefined(datos.IDBANNER)){
                            error+=(error.length>0?",":"")+"falta el dato id banner "
                        }else if(datos.IDBANNER.length != 8){
                            error+=(error.length>0?",":"")+"id banner tiene que ser de 8 digitos"
                        }
                    }
                }
            }
            if(error.length > 0){
                $scope.errores = [ ...$scope.errores,{idBanner:datos.IDBANNER,nombre:datos.nombre,Error:error}]
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
                $scope.errores = [ ...$scope.errores,{idBanner:datos[indice].IDBANNER,nombre:datos[indice].nombre,Error:"no hay aspirante con ese idBanner",usuario:$scope.properties.usuario,existe:true}]
            }else if(info.Registrado){
                $scope.errores = [ ...$scope.errores,{idBanner:datos[indice].IDBANNER,nombre:datos[indice].nombre,Error:`el aspirante ya tiene decision`,usuario:$scope.properties.usuario,existe:true}]
            }else if(!info.EstaEnCarga){
                $scope.errores = [ ...$scope.errores,{idBanner:datos[indice].IDBANNER,nombre:datos[indice].nombre,Error:"el aspirante no se encuantra en carga y consulta de resultados",usuario:$scope.properties.usuario,existe:true}]
            }else if(!info.puedePeriodo){
                $scope.errores = [ ...$scope.errores,{idBanner:datos[indice].IDBANNER,nombre:datos[indice].nombre,Error:"el aspirante no se encuentra en el periodo subido",usuario:$scope.properties.usuario,existe:true}]
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
    
    
    function findDuplicados(yourArray,prop){
       //encuantra los duplicados en vase a un campo del json-array
        let newArray = [];
        for (let i = 0;i<yourArray.length; i++){
                for(let j = 0; j<yourArray.length; j++){
                 if(yourArray[i][prop] == yourArray[j][[prop]] && i!=j ){
                     //se obtiene los duplicados
                     newArray.push(yourArray[i][prop])
                 }
             }
        }
        // limpia los duplicados [1,1] = [1]
        const Duplicates = [...new Set(newArray)]
        let count = yourArray.length;
        for (let i = 0;i<Duplicates.length; i++){
                while (count--) {
                 if (yourArray[count][prop] == Duplicates[i]) {
                     $scope.errores = [ ...$scope.errores,{idBanner:yourArray[count].IDBANNER,nombre:yourArray[count].nombre,Error:"Id banner se encuentra duplicado en el excel"}]
                     yourArray.splice(count, 1);
                 } 
             }
        }
        return yourArray;
    }
    
    
    function cargarlstBanner(arreglo){
        $scope.lstBanner = {'IDBANNER':"", "PERIODO":""};
		arreglo.forEach(info =>{
        	$scope.lstBanner.IDBANNER += `${$scope.lstBanner.IDBANNER.length>0?",":""}'${info.IDBANNER}'`;
            $scope.lstBanner.PERIODO += `${$scope.lstBanner.PERIODO.length>0?",":""}'${info.Periodo}'`;
        });
    }
    
    
   
    
}