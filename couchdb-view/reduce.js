function (keys, values, rereduce) {
	var ret = {};
	if(rereduce){
		ret = values.reduce( function(acum, curr) {
			return acum + curr
		}, 0)
	}else{
		ret = values.reduce( function(acum, curr) {
			return acum + curr.volume
		}, 0);
	}
	return ret;
}