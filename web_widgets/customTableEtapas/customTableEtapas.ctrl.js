function PbTableCtrl($scope) {
    $scope.eliminarRegistro= function(inx){
        debugger;
        var contenido = angular.copy($scope.filterItems($scope.properties.codigo));
        for(var i = 0; i < $scope.properties.contenido.length; i++) {
            var obj = $scope.properties.contenido[i];
            if(contenido[inx].descripcion==obj.descripcion && contenido[inx].texto==obj.texto && contenido[inx].titulo==obj.titulo){
                $scope.properties.contenido.splice(i,1);
            }
        }
    }

    /**
     * Filtra la matríz en función de un criterio de búsqueda (query)
     */
     $scope.filterItems=function(query) {
      return $scope.properties.contenido.filter(function(el) {
          return el.codigo==query
      })
    }

   $scope.getFilterArray=function(){
       console.log($scope.filterItems($scope.properties.codigo));
   }
   var hidden = document.getElementsByClassName("oculto");
    hidden[0].classList.add("invisible")
}
