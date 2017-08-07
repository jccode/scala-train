package ch_slick


import scala.concurrent.Await
import scala.concurrent.duration._
import ch_slick.gen.Tables._
import ch_slick.gen.Tables.profile.api._

import scala.concurrent.ExecutionContext.Implicits.global


object SlickGenUsageExample extends App {

  val dbfile = "db.sqlite3"
  val dbfilePath = getClass.getClassLoader.getResource(dbfile).getPath
  val jdbcDriver = "org.sqlite.JDBC"
  val url = s"jdbc:sqlite:${dbfilePath}"

  val db = Database.forURL(url, driver = jdbcDriver)

  Await.result(
    db.run(TubesiteVideo.result).map(result => {
      println(result.map(_.src).mkString("\n"))
    })
    , 60 seconds)

}
