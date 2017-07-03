function(doc) {
	var my = doc.my.split("-");	
	var d = new Date("20" + doc.y, doc.m, 1);
		
	emit(Number(doc.item), {
		item: doc.item,
		my: doc.my,
		y: Number(my[1]),
		m: Number(my[0]),		
		volume: doc.qtd,
		date: d.getYear() + "-" + d.getMonth() + "-" + d.getYear()
	});	
}