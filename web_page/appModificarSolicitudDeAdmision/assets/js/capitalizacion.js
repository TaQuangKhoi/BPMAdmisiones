

/*function capitalizeName(name="") {
	name = name.trim();
    if( (/[A-Z][A-Z].*$/gm.test(name)) ){
        name = name.toLowerCase();
    }
	return name.replace(/(?:^|\s|['`‘’.-])[^\x00-\x60^\x7B-\xDF](?!(\s|$))/g, function (a) {
        return a.toUpperCase();
    });
}*/

function capitalizeText(name="") {
	name = name.trim();
    name = name.toLowerCase();
	return name.replace(/(?:^|\s|['`‘’.-])[^\x00-\x60^\x7B-\xDF](?!(\s|$))/g, function (a) {
        return a.toUpperCase();
    });
}


function capitalizeName(name="") {
    name = name.toLowerCase();
    let nombres =  name.split(" ");
    let final = "";
    if(nombres.length>1){
        for(let i = 0; i<nombres.length; i++){
            if(nombres[i] != "de" && nombres[i] != "la" && nombres[i] != "las"){
                if(nombres[i].length > 1){
                    final += (final.length>0?" ":"")+nombres[i].charAt(0).toUpperCase() + nombres[i].slice(1).toLowerCase();
                }else{
                    final +=(final.length>0?" ":"")+nombres[i];
                }
            }else{
                final +=(final.length>0?" ":"")+nombres[i];
            }
        }    
    }else{
        final = name.charAt(0).toUpperCase() + name.slice(1).toLowerCase()
    }
    
    return final;
}

function capitalizeFirstLetter(string = "") {
    return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
}

