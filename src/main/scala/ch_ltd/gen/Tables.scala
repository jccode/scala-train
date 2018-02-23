package ch_ltd.gen
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.SQLiteProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Company.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Company
   *  @param id Database column id SqlType(INTEGER), PrimaryKey
   *  @param name Database column name SqlType(VARCHAR)
   *  @param code Database column code SqlType(VARCHAR)
   *  @param fullName Database column full_name SqlType(VARCHAR)
   *  @param engName Database column eng_name SqlType(VARCHAR)
   *  @param webSite Database column web_site SqlType(VARCHAR)
   *  @param lastUpdate Database column last_update SqlType(DATETIME) */
  case class CompanyRow(id: Int, name: String, code: Option[String], fullName: Option[String], engName: Option[String], webSite: Option[String], lastUpdate: Option[String])
  /** GetResult implicit for fetching CompanyRow objects using plain SQL queries */
  implicit def GetResultCompanyRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]]): GR[CompanyRow] = GR{
    prs => import prs._
    CompanyRow.tupled((<<[Int], <<[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table company. Objects of this class serve as prototypes for rows in queries. */
  class Company(_tableTag: Tag) extends profile.api.Table[CompanyRow](_tableTag, "company") {
    def * = (id, name, code, fullName, engName, webSite, lastUpdate) <> (CompanyRow.tupled, CompanyRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), code, fullName, engName, webSite, lastUpdate).shaped.<>({r=>import r._; _1.map(_=> CompanyRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INTEGER), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column name SqlType(VARCHAR) */
    val name: Rep[String] = column[String]("name")
    /** Database column code SqlType(VARCHAR) */
    val code: Rep[Option[String]] = column[Option[String]]("code")
    /** Database column full_name SqlType(VARCHAR) */
    val fullName: Rep[Option[String]] = column[Option[String]]("full_name")
    /** Database column eng_name SqlType(VARCHAR) */
    val engName: Rep[Option[String]] = column[Option[String]]("eng_name")
    /** Database column web_site SqlType(VARCHAR) */
    val webSite: Rep[Option[String]] = column[Option[String]]("web_site")
    /** Database column last_update SqlType(DATETIME) */
    val lastUpdate: Rep[Option[String]] = column[Option[String]]("last_update")
  }
  /** Collection-like TableQuery object for table Company */
  lazy val Company = new TableQuery(tag => new Company(tag))
}
