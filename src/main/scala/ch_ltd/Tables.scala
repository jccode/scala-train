package ch_ltd

/**
  * Tables
  *
  * @author 01372461
  */
object Tables extends Tables {
  override val profile = slick.jdbc.SQLiteProfile
}

trait Tables {
  val profile: slick.jdbc.JdbcProfile

  import profile.api._

  final case class Company(id: Int, name: String, code: Option[String], fullName: Option[String], engName: Option[String], webSite: Option[String], lastUpdate: Option[String])

  final class CompanyTable(tag: Tag) extends Table[Company](tag, "company2") {

    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name")
    val code = column[Option[String]]("code")
    val fullName = column[Option[String]]("full_name")
    val engName = column[Option[String]]("eng_name")
    val webSite = column[Option[String]]("website")
    val lastUpdate = column[Option[String]]("last_update")

    override def * = (id, name, code, fullName, engName, webSite, lastUpdate).mapTo[Company]
  }

  lazy val companies = TableQuery[CompanyTable]
}
