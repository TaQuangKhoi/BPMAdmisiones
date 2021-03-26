function PbTableCtrl($scope) {

    this.isArray = Array.isArray;
  
    this.isClickable = function () {
      return $scope.properties.isBound('selectedRow');
    };
  
    this.selectRow = function (row) {
      if (this.isClickable()) {
        $scope.properties.selectedRow = row;
      }
    };
  
    this.isSelected = function(row) {
      return angular.equals(row, $scope.properties.selectedRow);
    }
    $scope.redirecc = function(row, op){
        let ipBonita = window.location.protocol + "//" + window.location.host + "";
       
        if(row==1 && op==1){
            var url = "../Universidades";
            console.log(url)
            //window.top.location.href = url;
            //window.location.replace(url);
        }
        else if(row==1 && op==2){
             //var url = ipBonita +"/apps/administrativo/CatCampusAcciones/";
             var url = ipBonita + "/portal/resource/app/administrativo/CatCampusAcciones/content/";
             //var url = "../CatCampusAcciones/";
            console.log(url)
           window.location.replace(url); 
        }
        else if(row==2 && op==1){
            var url = "../Preparatorias";
            console.log(url)
            //window.top.location.href = url;
        }
        else if(row==2 && op==2){
            var url = ipBonita +"/portal/resource/app/administrativo/bachillerato/content/";
            console.log(url)
             window.location.replace(url); 
        }
        
        else if(row==3 && op==1){
            var url = "../Periodo";
            console.log(url)
            //window.top.location.href = url;
        }
        else if(row==3 && op==2){
           var url = ipBonita +"/portal/resource/app/administrativo/catPeriodoAcciones/content/";
            console.log(url)
            window.location.replace(url); 
        }
        else if(row==4 && op==1){
            var url = "../País";
            console.log(url)
            //window.top.location.href = url;
        }
        else if(row==4 && op==2){
             var url = ipBonita +"/portal/resource/app/administrativo/pais/content/";
            console.log(url)
             window.location.replace(url); 
        }
        else if(row==5 && op==1){
            var url = "../Estado";
            console.log(url)
            //window.top.location.href = url;
        }
        else if(row==5 && op==2){
             var url = ipBonita +"/portal/resource/app/administrativo/estados/content/";
            console.log(url)
            window.location.replace(url); 
        }
        else if(row==6 && op==1){
            var url = "../Territorios-Ciudad";
            console.log(url)
            //window.top.location.href = url;
        }
        else if(row==6 && op==2){
             var url = ipBonita +"/portal/resource/app/administrativo/CatCiudadAcciones/content/";
            console.log(url)
            window.location.replace(url); 
        }else if(row==7 && op==1){
            var url = "../Escolaridad";
            console.log(url)
            //window.top.location.href = url;
        }
        else if(row==7 && op==2){
            var url = ipBonita +"/portal/resource/app/administrativo/catEscolaridad/content/";
            console.log(url)
            window.location.replace(url); 
        }else if(row==8 && op==1){
            var url = "../Parentesco";
            console.log(url)
            //window.top.location.href = url;
        }
        else if(row==8 && op==2){
             var url = ipBonita +"/portal/resource/app/administrativo/catParentesco/content/";
            console.log(url)
           window.location.replace(url); 
        }else if(row==9 && op==1){
            var url = "../Sexo";
            console.log(url)
            //window.top.location.href = url;
        }
        else if(row==9 && op==2){
             var url = ipBonita +"/portal/resource/app/administrativo/catSexo/content/";
            console.log(url)
             window.location.replace(url); 
        }else if(row==10 && op==1){
            var url = "../Nacionalidad";
            console.log(url)
            //window.top.location.href = url;
        }
        else if(row==10 && op==2){
             var url = ipBonita +"/portal/resource/app/administrativo/CatNacionalidades/content/";
            console.log(url)
            window.location.replace(url); 
        }
        else if(row==11 && op==1){
            var url = "../Estado civil";
            console.log(url)
            //window.top.location.href = url;
        }
        else if(row==11 && op==2){
             var url = ipBonita +"/portal/resource/app/administrativo/catEstado_civil/content/";
            console.log(url)
           window.location.replace(url); 
        }
        else if(row==12 && op==1){
            var url = "../Titulo";
            console.log(url)
            //window.top.location.href = url;
        }
        else if(row==12 && op==2){
            var url = ipBonita +"/portal/resource/app/administrativo/CatTitulo/content/";
            console.log(url)
           window.location.replace(url); 
        }
     
  }
 /**/$scope.sendDataEnabled = function(row,checked){
//Funcion si quieren que cuando se habilite haga redireccion o haga algo instantaneo
    console.log(checked+" "+row)
  }
  
  $scope.seleccion = function(row){
//Funcion para cambiar el estado       
    // console.log($scope.properties.content[row-1].Seleccionado)
     if($scope.properties.content[row-1].Seleccionado==true){
         $scope.properties.content[row-1].Seleccionado=false
     }
     else if($scope.properties.content[row-1].Seleccionado==false){
        $scope.properties.content[row-1].Seleccionado=true
     }else{
          $scope.properties.content[row-1].Seleccionado=true
     }
    //console.log($scope.properties.content[row-1].Seleccionado)
  }
  
  }