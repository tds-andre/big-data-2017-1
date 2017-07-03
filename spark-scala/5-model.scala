//trains model

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.to_json
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.ml.linalg.{Vector, Vectors}
import scalaj.http._


val inputFilename = "dados/final.parquet"
val couchdb = "http://10.0.0.14:5984/timeseries"

val sql = new  org.apache.spark.sql.SQLContext(sc)

val df = spark.read.parquet(inputFilename);
df.createOrReplaceTempView("input")

val nn = sql.sql("select qtd, l1, l2, d2 from input where qtd is not null and l1 is not null and l2 is not null and d2 is not null")

val sel = nn.select($"qtd", $"l1", $"l2", $"d2")


val toVec4    = udf[Vector, Double, Double , Double] { (a,b,d) =>  Vectors.dense(a, b,d) }
val encodeLabel    = udf[Double, Double]{ a => a}

val training = sel.withColumn("features",toVec4(sel("l1"),sel("l2"),sel("d2"))).withColumn("label", encodeLabel(sel("qtd"))).select("features", "label")

val lr = new LinearRegression().setMaxIter(10).setRegParam(0.3).setElasticNetParam(0.8)


val lrModel = lr.fit(training)


println(s”Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}”)

val trainingSummary = lrModel.summary
println(s”numIterations: ${trainingSummary.totalIterations}”)
println(s”objectiveHistory: ${trainingSummary.objectiveHistory.toList}”)
trainingSummary.residuals.show()
println(s”RMSE: ${trainingSummary.rootMeanSquaredError}”)
println(s”r2: ${trainingSummary.r2}”)*/



