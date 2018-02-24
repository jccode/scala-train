package ch_ltd

import ch_ltd.gen.Tables._
import ch_ltd.gen.Tables.profile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * DbService
  *
  * @author 01372461
  */
class DbService {

  val dbfile = "ltd.sqlite3"
  val dbfilePath = getClass.getClassLoader.getResource(dbfile).getPath
  val jdbcDriver = "org.sqlite.JDBC"
  val url = s"jdbc:sqlite:${dbfilePath}"
  val db = Database.forURL(url, driver = jdbcDriver)


}

object DbService extends App {

  val db = Database.forConfig("ltddb")

  def query(): Unit = {
    Await.result(
      db.run(Company.result).map(result => {
        println(result.size)
      })
      , 60 seconds)
  }

  def insert(): Unit = {
//    println(Company.insertStatement)

    val insertAction = DBIO.seq(
      Company ++= Seq(
        CompanyRow(1, "Hello", Option.empty, Option.empty, Option.empty, Option.empty, Option.empty),
        CompanyRow(2, "World", Option.empty, Option.empty, Option.empty, Option.empty, Option.empty)
      ),
      Company.result
    )

    Await.result(
      db.run(insertAction)
    , 60 seconds)

  }

  query()
}