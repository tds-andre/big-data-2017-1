moment.locale("pt-br");
var config = {
	couchdb: "http://10.0.0.14:5984/timeseries"
}

var couch = {
	server: config.couchdb,
	view: "/_design/ts/_view/ts",

	fetchItems: function(options){
		var options = options || {};
		$.ajax({
			url: this.server + this.view + "?group_level=1",
			dataType: "json",
			success: function(data) { 
				console.log("lista", data);
				options.success(data.rows)
			}
		});
	},

	reduceItem: function(item, options){
		var options = options || {};
		if(options.success)
			options.success({
				item: item,
				volume: 3123 * Math.random(),
				minDate: "2015-01-01",
				maxDate: "2015-01-01"
			});
	},

	fetchModel: function(options){
		var datasim = {
			l1: 0.38749476098829316,
			l2: 0.48992667914070875,
			d1: 0,
			d2: 0,
			b: 27.587208276043565
		}

		if(options.success)
			options.success(datasim)
	},

	fetchItem: function(item, options){
		var options = options || {};
		var url =this.server + this.view + "?reduce=false&startkey=" + item + "&endkey="+ item;
		$.ajax({
			url: url,
			dataType: "json",
			success: function(data){
				var result = data.rows.map(el => {
					var val = el.value;
					val.date = "20" + val.y + "-" + val.m + "-" + "01";
					return val;
				});
				options.success(result);
			}
		});
	}
}//10075

var chart = c3.generate({
	bindto: "#ts-chart",
    data: {
        x: 'x',
        columns: []
    },
    axis: {
        x: {
            type: 'timeseries',
            tick: {
                format: '%Y-%m-%d'
            }
        }
    },
     legend: {
        position: 'right'
    }
});


$("#lookup-input").on("change keypress", function(ev){	
	if(!ev.keyCode || ev.keyCode == 13)
		renderItem($(ev.currentTarget).val(), model);
});

var model;
setTimeout(function () {
	$il = $("#item-lookup")
	
	couch.fetchModel({success: function(m){
	 	model = m;
	 	couch.fetchItems({success: function(data){
			data.forEach(datum => {
				$il.append("<option value=" + datum.key + ">")
			});
		}});
	}});   
}, 1000);


function predict(ts,model){
	var mom = ts.map(el => { return {date: moment(el.date), value: el.volume}});
	var sorted = mom.sort( function(a,b) { 
		var x = a.date.isAfter(b.date) ? -1 : 1;		
		return x;		
	});	
	
	var a1 = model.l1 * sorted[0].value + model.l2 * sorted[1].value + model.d2 * (sorted[0].value - sorted[1].value) + model.b
	
	var a2 = model.l1 * a1 + model.l2 * sorted[0].value + model.d2 * (a1 - sorted[0].value) + model.b
	var a3 = model.l1 * a2 + model.l2 * a1 + model.d2 * (a2 - a1) + model.b

	var d1 = sorted[0].date.clone();
	var d2 = sorted[0].date.clone();
	var d3 = sorted[0].date.clone();
	d1.add(1, "M");
	d2.add(2, "M");
	d3.add(3, "M");

	var arr = [{date: d1, volume: a1}, {date: d2, volume: a2}, {date: d3, volume: a3}];
	return arr.map(el => {return {date: el.date.format("YYYY-MM-DD"), volume: el.volume}});


}


function renderItem(item,model){

	couch.fetchItem(item, {success: function(data,){
		var prediction = predict(data, model);

		var dates = ["x"].concat(data.map(el => el.date)).concat(prediction.map(el=> el.date));
		var pred = ["Predição"].concat(data.map(el => el.volume).concat(prediction.map(el => el.volume)));
		var volumes = ["Histórico"].concat(data.map(el => el.volume));
		chart.load({
	        columns: [
	           dates,  pred, volumes
	        ]
    	});

    	couch.reduceItem(item, {success: function(data){    		
    		$("#js-count").html(data.volume);
    		$("#js-min-date").html(moment(data.minDate).format("MMMM/YYYY"));
    		$("#js-max-date").html(moment(data.maxDate).format("MMMM/YYYY"));
    	}});




		

	}});
}

