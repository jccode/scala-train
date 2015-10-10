package ch_slick

import slick.backend.DatabasePublisher
import slick.driver.MySQLDriver.api._
import slick.jdbc.meta.MTable
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


/**
 * Created by jcchen on 15-10-10.
 */
object SlickMain {

  val db = Database.forConfig("mydb")
  val suppliers = TableQuery[Suppliers]
  val coffees = TableQuery[Coffee]

  def main(args: Array[String]) {
    printDDLs()
  }

  def query1(db: Database = db): Unit = {
    try {
      val filterQuery = coffees.filter(_.price > 9.0)
      val f = db.run(filterQuery.result.map(println))
      Await.result(f, Duration.Inf)
    } finally db.close()
  }

  def query2(db: Database = db): Unit = {
    try {
      val joinQuery = for {
        c <- coffees if c.price < 9.0
        s <- suppliers if s.id === c.supID
      } yield (c.name, s.name)
      val f = db.run(joinQuery.result).map(println)
      Await.result(f, Duration.Inf)
    } finally db.close()
  }

  def query3(db: Database = db): Unit = {
    try {
      val q3 = for {
        c <- coffees if c.price < 9.0
        s <- c.supplier
      } yield (c.name, s.name)
      val f = db.run(q3.result).map(println)
      Await.result(f, Duration.Inf)
    } finally db.close()
  }

  def update(db: Database = db): Unit = {
    val updateQuery = coffees.map(_.sales)
    val updateAction = updateQuery.update(1)
    println("update statement is: " + updateQuery.updateStatement)
    val f = db.run(updateAction.map { numUpdatedRows =>
      println(s"Updated $numUpdatedRows rows")
    })
    Await.result(f, Duration.Inf)
  }

  def delete(db: Database = db): Unit = {
    val deleteQuery = coffees.filter(_.price < 9.0)
    val deleteAction = deleteQuery.delete
    println("delete statement is: " + deleteAction.statements)
    val f = db.run(deleteAction.map { numDeletedRows =>
        println(s"Deleted $numDeletedRows rows")
    })
    Await.result(f, Duration.Inf)
  }

  def listCoffeeData(db: Database = db): Unit = {
    try {
      db.run(coffees.result).map(_.foreach({
        case (name, supId, price, sales, total) =>
          println(" "+name+"\t"+supId+"\t"+price+"\t"+sales+"\t"+total)
      }))
    } finally db.close()
  }

  def listCoffeeData2(db: Database = db): Unit = {
    try {
      val coffeeNameAction: StreamingDBIO[Seq[String], String] = coffees.map(_.name).result
      val coffeeNamePublisher: DatabasePublisher[String] = db.stream(coffeeNameAction)
      val f = coffeeNamePublisher.foreach(println)
      Await.result(f, Duration.Inf)
    } finally db.close()
  }

  def insertData(db: Database = db): Unit = {
    val populatingData = DBIO.seq(
      suppliers ++= Seq(
        (101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199"),
        ( 49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460"),
        (150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966")
      ),
      coffees ++= Seq(
        ("Colombian",         101, 7.99, 0, 0),
        ("French_Roast",       49, 8.99, 0, 0),
        ("Espresso",          150, 9.99, 0, 0),
        ("Colombian_Decaf",   101, 8.99, 0, 0),
        ("French_Roast_Decaf", 49, 9.99, 0, 0)
      )
    )
    val f = db.run(populatingData)
    Await.result(f, Duration.Inf)
  }

  def createTables(db: Database = db): Unit = {
    try {
      val f = db.run((suppliers.schema ++ coffees.schema).create)
      Await.result(f, Duration.Inf)
    } finally db.close()
  }

  def printDDLs(): Unit = {
    println( (coffees.schema ++ suppliers.schema).createStatements.toList.mkString("; ") )
  }

  def listTables(db: Database = db) = {
    try {
      val r: Future[Vector[MTable]] = db.run(MTable.getTables)
      val tables = Await.result(r ,1.seconds).toList.map((t: MTable) => t.name.name)
      println(tables.mkString(", "))
    } finally db.close()
  }

}
