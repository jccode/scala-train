package ch_ltd

import Tables.{profile, _}
import Tables.profile.api._

import scala.concurrent.Future

// TODO 这里是不是只需要一个类型参数就行了? E 通过 T#TableElementType 来表达就行了?
trait Repo[E, T <: Table[E]] {
  val db: Database
  val rows: TableQuery[T]

  def exec[R](program: DBIO[R]): Future[R] = db.run(program)
//  def save(list: Iterable[E]): Future[Option[Int]]
//  def find(filter: TableQuery[T] => Query[T, E, Seq] = null): Future[Seq[E]]

  def save(list: Iterable[E]): Future[Option[Int]] =
    db.run(rows ++= list)

  def find(filter: TableQuery[T] => Query[T, E, Seq] = null): Future[Seq[E]] =
    db.run((if(filter == null) rows else filter(rows)).result)

}

object Repo {
}

class DbConfig {
  val db: Database = Database.forConfig("ltddb")
}

object CompanyRepo extends DbConfig with Repo[Company, CompanyTable] {
  override val rows: TableQuery[CompanyTable] = companies

//  override def save(list: Iterable[Company]): Future[Option[Int]] =
//    db.run(companies ++= list)
//  override def find(filter: TableQuery[CompanyTable] => Query[CompanyTable, Company, Seq] = identity): Future[Seq[Tables.Company]] =
//    db.run(filter(companies).result)

}
