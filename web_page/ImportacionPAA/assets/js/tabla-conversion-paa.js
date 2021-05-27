function convertirDato (dato){
    var escala = 500;
    let cantidad = 0;
    let calculo = 0;
    if(dato <= 248){
        cantidad = (248 - dato)
        calculo = parseFloat(300/248);
        for(let i = 0; i < cantidad; i++){
            escala =  parseFloat(escala - (calculo) )
        }
        return Math.round(escala);

    }else{
        cantidad = ((dato-249) - 442 )
        cantidad = (cantidad + 442)
        calculo = parseFloat(300/442);
        for(let i = 0; i <= cantidad; i++){
            escala =  parseFloat(escala + (calculo) )
        }
        return Math.round(escala);
    }
}