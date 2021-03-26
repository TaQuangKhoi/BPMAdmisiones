function ($scope) {
     $(function(){
        window.$crisp=[];
        //window.CRISP_WEBSITE_ID="5760f1e1-1fbb-432c-ae13-f743ecb1d17f";
        window.CRISP_WEBSITE_ID="d9614e42-c9fc-4cfa-9cd6-109d47c98464";
        (
            function(){
                d=document;
                s=d.createElement("script");
                s.src="https://client.crisp.chat/l.js";
                s.async=1;
                d.getElementsByTagName("head")[0].appendChild(s);
            }
        )();
       
   });
   
   $scope.$watch("properties.datosUsuario",function(newValue,oldValue){
      if(newValue !== undefined){
           
        $crisp.push(["set", "user:email", [`${$scope.properties.datosUsuario.correoelectronico}`]]);
        //var info=$scope.properties.datosUsuario.primernombre+" "+$scope.properties.datosUsuario.segundonombre+" - "+$scope.properties.datosUsuario.claveCampus+" - "+$scope.properties.datosUsuario.claveLicenciatura
        var info =$scope.properties.datosUsuario.primernombre+" "+$scope.properties.datosUsuario.apellidopaterno+" - "+$scope.properties.datosUsuario.campus+" - "+$scope.properties.datosUsuario.licenciatura
        $crisp.push(["set", "user:nickname", [info+""]]);
        $crisp.push(["set", "session:data", [
            [
            ["universidad", `${$scope.properties.datosUsuario.campus}`],
            ["nombre_completo", `${$scope.properties.datosUsuario.primernombre+" "+$scope.properties.datosUsuario.segundonombre+" "+$scope.properties.datosUsuario.apellidopaterno+" "+$scope.properties.datosUsuario.apellidomaterno}`],
            ["licenciatura", `${$scope.properties.datosUsuario.licenciatura}`],
            ["estatus_solicitud",`${$scope.properties.datosUsuario.estatussolicitud}`]
            ]
            ]]);
      } 
   });
}