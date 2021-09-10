function convertirDato (dato){
    if(!checkNumber2(dato)){return ""}
    if(dato.length < 1){return ""}
    var escala = 500;
    let cantidad = 0;
    let calculo = 0;
    if(dato >= 0 && dato <= 248){
        cantidad = (248 - dato)
        calculo = parseFloat(300/248);
        for(let i = 0; i < cantidad; i++){
            escala =  parseFloat(escala - (calculo) )
        }
        return Math.round(escala) + "";

    }else if(dato >= 249 && dato <=690){
        cantidad = ((dato-249) - 442 )
        cantidad = (cantidad + 442)
        calculo = parseFloat(300/442);
        for(let i = 0; i <= cantidad; i++){
            escala =  parseFloat(escala + (calculo) )
        }
        return Math.round(escala) + "";
    } else {
        return "0"
    }
}

function checkNumber(x) {

    // check if the passed value is a number
    if(!isNaN(x)){
    
        // check if it is integer
        if (Number.isInteger(x)) {
            return true;
        }
        else {
            return false;
        }
    
    } else {
        console.log(`${x} is not a number`);
        return false;
    }
}

function checkNumber2(x) {

    // check if the passed value is a number typeof x == 'number' && 
    if(!isNaN(x)){
        return true;
    
    } else {
        return false;
    }
}