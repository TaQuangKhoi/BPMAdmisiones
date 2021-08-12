function guardarEscala(jsonArray,newObject) {
  let objectAlreadyExisted = false

  let result = jsonArray.map(element => {
    let newElement = { ...element };

    if (newElement.letra === newObject.letra && newElement.valorOriginal === newObject.valorOriginal) {
      objectAlreadyExisted = true
      newElement.valorConvertido = newObject.valorConvertido
    }
    return newElement
  });

  if (!objectAlreadyExisted) {
    result.push(newObject)
  }
  
  return  result;
}


function conversionEscala(jsonArray,letra,equivalente){
    let convert = "30";
    jsonArray.data.forEach( element =>{
        if(element.equivalente === equivalente && element.letra === letra){
            convert = element.totc;
        }
    });
    
    return convert;
}