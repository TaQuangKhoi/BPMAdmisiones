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
   
    if(op==1){
        var url = "../"+row.Direccion;
        console.log(url)
        //window.top.location.href = url;
        //window.location.replace(url);
    }
    else if(op==2){
         //var url = ipBonita +"/apps/administrativo/CatCampusAcciones/";
         var url = ipBonita + "/portal/resource/app/administrativo/"+row.Direccion+"/content/";
         //var url = "../CatCampusAcciones/";
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