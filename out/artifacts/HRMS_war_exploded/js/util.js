function getKeyArray(array,key){
    var keys = new Array();
    for(var i in array){
        if($.inArray(array[i][key],keys) == -1) {
            keys.push(array[i][key]);
        }
    }
    return keys;
}

function getKeySerial(array,key){
	return getKeyArray(array,key).toString();
}