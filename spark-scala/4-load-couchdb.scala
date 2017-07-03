//uploads file final.parquet to couchdb

import org.apache.spark.sql.SparkSession
import scalaj.http._
import org.apache.spark.sql.functions.to_json

val inputFilename = "dados/final.parquet"
val couchdb = "http://10.0.0.14:5984/timeseries"

val sql = new  org.apache.spark.sql.SQLContext(sc)

val df = spark.read.parquet(inputFilename);

val ts5 = df.select(to_json(struct($"item", $"my", $"qtd")))

Http(couchdb).method("DELETE").asString
Http(couchdb).method("PUT").asString

ts5.map(json => Http(couchdb).postData(json.toString.substring(1,json.toString.length-1)).header("content-type", "application/json").asString.code ).collect()
