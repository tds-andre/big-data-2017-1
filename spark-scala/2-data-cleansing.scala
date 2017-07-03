val reducedFilename = "dados/lines-reduced"
val outputFilename = "dados/cleansed"

def tryParseInt(v: String): Int = {try{return v.toInt}catch{case ex: Exception => {return 0}}}

val reducedFile = sc.textFile(reducedFilename);
val cleansed = reducedFile.map(_.split(",")).filter(_.size == 10).filter( el => el(3) != 0);

val header = sc.parallelize(Array("line\theader\titem_name\tquantity\titem_type\tordered_item\trequest_date\tsold_from\tsold_to\titem"));
val f = header.union(cleansed.map(_.mkString("\t")));
f.saveAsTextFile(outputFilename);



