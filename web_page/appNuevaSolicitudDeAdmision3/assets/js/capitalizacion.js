

function capitalizeName(name="") {
	name = name.trim();
    if( (/[A-Z][A-Z].*$/gm.test(name)) ){
        name = name.toLowerCase();
    }
	return name.replace(/\b(\w)/g, s => s.toUpperCase());
}


function capitalizeFirstLetter(string = "") {
    return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
}