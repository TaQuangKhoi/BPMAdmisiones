

function capitalizeName(name="") {
	name = name.trim();
    if( (/[A-Z][A-Z].*$/gm.test(name)) ){
        name = name.toLowerCase();
    }
	return name.replace(/(?:^|\s|['`‘’.-])[^\x00-\x60^\x7B-\xDF](?!(\s|$))/g, function (a) {
        return a.toUpperCase();
    });
}

function capitalizeFirstLetter(string = "") {
    return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
}

