val lineColIndex = Array(0,1,5,6,9,10,11,35,36,49);
var lineFilename = "dados/OE_ORDER_LINES_ALL.csv";
var reducedFilename = "dados/lines-reduced";

val lineFile = sc.textFile(lineFilename);

val lineSplited = lineFile.map(_.split(",")).filter(_.size >= 340);

val reduced = lineSplited.filter(_.size >= 340).map(inner => lineColIndex.map(inner))

reduced.saveAsTextFile(reduced.map(_.mkString(",")));


def tryParseInt(v: String): Int = {try{return v.toInt}catch{case ex: Exception => {return 0}}}

val reducedFile = sc.textFile("dados/lines-reduced");

val splited = reducedFile.map(_.split(",")).filter(_.size == 10);

val items = splited.map(el => (el(9), tryParseInt(el(3))));
val itemsCount = items.reduceByKey( (a,b) => a + b )
val top10Items = itemsCount.takeOrdered(10)(Ordering[Int].reverse.on(el => el._2));

val transactions  = splited.map(el => (el(9), 1));
val transactionsCount = transactions.reduceByKey( (a,b) => a + b );
val top10Trans = transactionsCount.takeOrdered(10)(Ordering[Int].reverse.on(el => el._2))

val guy = splited.filter( el => el(9) == "1786941" );
val guyDateQuantKV = guy.map(el => (el(6),tryParseInt(el(3))))
val guyQuantByDate = guyDateQuantKV.reduceByKey( (a,b) => a + b )

guyQuantByDate.map(el => el._1.toString() + "\t" + el._2.toString()).coalesce(1,true).saveAsTextFile("dados/guyQuantByDate.tsv");





