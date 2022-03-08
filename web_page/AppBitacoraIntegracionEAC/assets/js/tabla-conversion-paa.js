function convertirDato (dato){
    if(!checkNumber2(dato)){return ""}
    if(dato.length < 1){return ""}
    var escala = 500;
    let cantidad = 0;
    let calculo = 0;
    if(dato >= 0 && dato <= 269){
        cantidad = (270 - dato)
        calculo = parseFloat(300/270);
        for(let i = 0; i < cantidad; i++){
            escala =  parseFloat(escala - (calculo) );
        }
        let floor = Math.floor(escala)
        if((floor % 10) == 5){
        	return floor +"";
        }
        return Math.round(escala) + "";

    }else if(dato >= 270 && dato <=690){
        cantidad = ((dato-269) - 422 )
        cantidad = (cantidad + 421)
        calculo = parseFloat(300/420);
        for(let i = 0; i < cantidad; i++){
            escala =  parseFloat(escala + (calculo) );
        }
        let floor = Math.floor(escala)
        if((floor % 10) == 5){
        	return floor+"";
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
