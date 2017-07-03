
import org.apache.spark.sql.SparkSession
val inputFilename = "dados/cleansed"
val outputFilename = "dados/final.parquet"

val sql = new  org.apache.spark.sql.SQLContext(sc)

val df = sql.read.format("com.databricks.spark.csv").option("header", "false").option("inferSchema", "true").option("delimiter", "\t").option("dateFormat", "dd/MM/YYYY").load(inputFilename)


val colRename = Seq("LINE_ID","HEADER_ID","ORDERED_ITEM","ORDERED_QUANTITY","ITEM_TYPE_CODE","ORDERED_ITEM_ID","REQUEST_DATE","SOLD_FROM_ORG_ID","SOLD_TO_ORG_ID","INVENTORY_ITEM_ID")
val dfr = df.toDF(colRename: _*)
dfr.createOrReplaceTempView("sales")


val grouped = sql.sql("select INVENTORY_ITEM_ID, TO_DATE(CAST(UNIX_TIMESTAMP(REQUEST_DATE, 'dd/MM/yyyy') AS TIMESTAMP)) as boughtAt, sum(ORDERED_QUANTITY) as QTD from sales group by INVENTORY_ITEM_ID, REQUEST_DATE")
grouped.createOrReplaceTempView("salesByDay")

val groupedWithMY = sql.sql("select inventory_item_id, month(boughtAt) as m, year(boughtAt) as y, concat(month(boughtAt), '-', year(boughtAt)) as my, qtd from salesByDay")
groupedWithMY.createOrReplaceTempView("salesByDayWithMY")


val finalGroup = sql.sql("select inventory_item_id as item, first(m) as m, first(y) as y, my, sum(qtd) as qtd from salesByDayWithMY group by inventory_item_id, my")
finalGroup.createOrReplaceTempView("timeseries")


val notYet = sql.sql("select *, TO_DATE(CAST(UNIX_TIMESTAMP(concat('1-',my), 'dd-MM-yy') AS TIMESTAMP)) as at from timeseries")
notYet.createOrReplaceTempView("notYet")


val almost = sql.sql("select *, add_months(at, -1) as at1,  add_months(at, -2) as at2 from notYet")
almost.createOrReplaceTempView("almost")
almost.createOrReplaceTempView("almost2")


val there = sql.sql("select a.item, a.m, a.y, a.my, a.at, a.qtd, b.qtd as l1, c.qtd as l2 from almost a inner join almost2 b on a.at1 = b.at and a.item = b.item inner join almost2 c on a.at2 = c.at and a.item = c.item")
there.createOrReplaceTempView("there")

val diff = sql.sql("select *, (qtd - l1) as d1 , (l1 - l2) as d2 from there")
diff.write.save(outputFilename)





