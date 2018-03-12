package ch_ltd

import Tables._
import Tables.profile.api._

import scala.concurrent.Future


trait Repo[T] {
  val db: Database
  def save(list: Iterable[T]): Future[Option[Int]]
  def exec[R](program: DBIO[R]): Future[R] = db.run(program)
}

object Repo {
}

class DbConfig {
  val db: Database = Database.forConfig("ltddb")
}

object CompanyRepo extends DbConfig with Repo[Company] {
  override def save(list: Iterable[Company]): Future[Option[Int]] =
    db.run(companies ++= list)
}
