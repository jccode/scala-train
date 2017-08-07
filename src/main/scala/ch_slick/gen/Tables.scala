package ch_slick.gen
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(AuthGroup.schema, AuthGroupPermissions.schema, AuthPermission.schema, AuthUser.schema, AuthUserGroups.schema, AuthUserUserPermissions.schema, DjangoAdminLog.schema, DjangoContentType.schema, DjangoMigrations.schema, DjangoSession.schema, EasyThumbnailsSource.schema, EasyThumbnailsThumbnail.schema, EasyThumbnailsThumbnaildimensions.schema, FilerClipboard.schema, FilerClipboarditem.schema, FilerFile.schema, FilerFolder.schema, FilerFolderpermission.schema, FilerImage.schema, FilerThumbnailoption.schema, TubesiteCategory.schema, TubesiteVideo.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table AuthGroup
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(80,true) */
  case class AuthGroupRow(id: Int, name: String)
  /** GetResult implicit for fetching AuthGroupRow objects using plain SQL queries */
  implicit def GetResultAuthGroupRow(implicit e0: GR[Int], e1: GR[String]): GR[AuthGroupRow] = GR{
    prs => import prs._
    AuthGroupRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table auth_group. Objects of this class serve as prototypes for rows in queries. */
  class AuthGroup(_tableTag: Tag) extends profile.api.Table[AuthGroupRow](_tableTag, "auth_group") {
    def * = (id, name) <> (AuthGroupRow.tupled, AuthGroupRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name)).shaped.<>({r=>import r._; _1.map(_=> AuthGroupRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(80,true) */
    val name: Rep[String] = column[String]("name", O.Length(80,varying=true))

    /** Index over (name) (database name auth_group_name_a6ea08ec_like) */
    val index1 = index("auth_group_name_a6ea08ec_like", name)
    /** Uniqueness Index over (name) (database name auth_group_name_key) */
    val index2 = index("auth_group_name_key", name, unique=true)
  }
  /** Collection-like TableQuery object for table AuthGroup */
  lazy val AuthGroup = new TableQuery(tag => new AuthGroup(tag))

  /** Entity class storing rows of table AuthGroupPermissions
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param groupId Database column group_id SqlType(int4)
   *  @param permissionId Database column permission_id SqlType(int4) */
  case class AuthGroupPermissionsRow(id: Int, groupId: Int, permissionId: Int)
  /** GetResult implicit for fetching AuthGroupPermissionsRow objects using plain SQL queries */
  implicit def GetResultAuthGroupPermissionsRow(implicit e0: GR[Int]): GR[AuthGroupPermissionsRow] = GR{
    prs => import prs._
    AuthGroupPermissionsRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table auth_group_permissions. Objects of this class serve as prototypes for rows in queries. */
  class AuthGroupPermissions(_tableTag: Tag) extends profile.api.Table[AuthGroupPermissionsRow](_tableTag, "auth_group_permissions") {
    def * = (id, groupId, permissionId) <> (AuthGroupPermissionsRow.tupled, AuthGroupPermissionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(groupId), Rep.Some(permissionId)).shaped.<>({r=>import r._; _1.map(_=> AuthGroupPermissionsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column group_id SqlType(int4) */
    val groupId: Rep[Int] = column[Int]("group_id")
    /** Database column permission_id SqlType(int4) */
    val permissionId: Rep[Int] = column[Int]("permission_id")

    /** Foreign key referencing AuthGroup (database name auth_group_permissions_group_id_b120cbf9_fk_auth_group_id) */
    lazy val authGroupFk = foreignKey("auth_group_permissions_group_id_b120cbf9_fk_auth_group_id", groupId, AuthGroup)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AuthPermission (database name auth_group_permiss_permission_id_84c5c92e_fk_auth_permission_id) */
    lazy val authPermissionFk = foreignKey("auth_group_permiss_permission_id_84c5c92e_fk_auth_permission_id", permissionId, AuthPermission)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (groupId,permissionId) (database name auth_group_permissions_group_id_0cd325b0_uniq) */
    val index1 = index("auth_group_permissions_group_id_0cd325b0_uniq", (groupId, permissionId), unique=true)
  }
  /** Collection-like TableQuery object for table AuthGroupPermissions */
  lazy val AuthGroupPermissions = new TableQuery(tag => new AuthGroupPermissions(tag))

  /** Entity class storing rows of table AuthPermission
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(255,true)
   *  @param contentTypeId Database column content_type_id SqlType(int4)
   *  @param codename Database column codename SqlType(varchar), Length(100,true) */
  case class AuthPermissionRow(id: Int, name: String, contentTypeId: Int, codename: String)
  /** GetResult implicit for fetching AuthPermissionRow objects using plain SQL queries */
  implicit def GetResultAuthPermissionRow(implicit e0: GR[Int], e1: GR[String]): GR[AuthPermissionRow] = GR{
    prs => import prs._
    AuthPermissionRow.tupled((<<[Int], <<[String], <<[Int], <<[String]))
  }
  /** Table description of table auth_permission. Objects of this class serve as prototypes for rows in queries. */
  class AuthPermission(_tableTag: Tag) extends profile.api.Table[AuthPermissionRow](_tableTag, "auth_permission") {
    def * = (id, name, contentTypeId, codename) <> (AuthPermissionRow.tupled, AuthPermissionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(contentTypeId), Rep.Some(codename)).shaped.<>({r=>import r._; _1.map(_=> AuthPermissionRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column content_type_id SqlType(int4) */
    val contentTypeId: Rep[Int] = column[Int]("content_type_id")
    /** Database column codename SqlType(varchar), Length(100,true) */
    val codename: Rep[String] = column[String]("codename", O.Length(100,varying=true))

    /** Foreign key referencing DjangoContentType (database name auth_permiss_content_type_id_2f476e4b_fk_django_content_type_id) */
    lazy val djangoContentTypeFk = foreignKey("auth_permiss_content_type_id_2f476e4b_fk_django_content_type_id", contentTypeId, DjangoContentType)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (contentTypeId,codename) (database name auth_permission_content_type_id_01ab375a_uniq) */
    val index1 = index("auth_permission_content_type_id_01ab375a_uniq", (contentTypeId, codename), unique=true)
  }
  /** Collection-like TableQuery object for table AuthPermission */
  lazy val AuthPermission = new TableQuery(tag => new AuthPermission(tag))

  /** Entity class storing rows of table AuthUser
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param password Database column password SqlType(varchar), Length(128,true)
   *  @param lastLogin Database column last_login SqlType(timestamptz), Default(None)
   *  @param isSuperuser Database column is_superuser SqlType(bool)
   *  @param username Database column username SqlType(varchar), Length(150,true)
   *  @param firstName Database column first_name SqlType(varchar), Length(30,true)
   *  @param lastName Database column last_name SqlType(varchar), Length(30,true)
   *  @param email Database column email SqlType(varchar), Length(254,true)
   *  @param isStaff Database column is_staff SqlType(bool)
   *  @param isActive Database column is_active SqlType(bool)
   *  @param dateJoined Database column date_joined SqlType(timestamptz) */
  case class AuthUserRow(id: Int, password: String, lastLogin: Option[java.sql.Timestamp] = None, isSuperuser: Boolean, username: String, firstName: String, lastName: String, email: String, isStaff: Boolean, isActive: Boolean, dateJoined: java.sql.Timestamp)
  /** GetResult implicit for fetching AuthUserRow objects using plain SQL queries */
  implicit def GetResultAuthUserRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[java.sql.Timestamp]], e3: GR[Boolean], e4: GR[java.sql.Timestamp]): GR[AuthUserRow] = GR{
    prs => import prs._
    AuthUserRow.tupled((<<[Int], <<[String], <<?[java.sql.Timestamp], <<[Boolean], <<[String], <<[String], <<[String], <<[String], <<[Boolean], <<[Boolean], <<[java.sql.Timestamp]))
  }
  /** Table description of table auth_user. Objects of this class serve as prototypes for rows in queries. */
  class AuthUser(_tableTag: Tag) extends profile.api.Table[AuthUserRow](_tableTag, "auth_user") {
    def * = (id, password, lastLogin, isSuperuser, username, firstName, lastName, email, isStaff, isActive, dateJoined) <> (AuthUserRow.tupled, AuthUserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(password), lastLogin, Rep.Some(isSuperuser), Rep.Some(username), Rep.Some(firstName), Rep.Some(lastName), Rep.Some(email), Rep.Some(isStaff), Rep.Some(isActive), Rep.Some(dateJoined)).shaped.<>({r=>import r._; _1.map(_=> AuthUserRow.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column password SqlType(varchar), Length(128,true) */
    val password: Rep[String] = column[String]("password", O.Length(128,varying=true))
    /** Database column last_login SqlType(timestamptz), Default(None) */
    val lastLogin: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("last_login", O.Default(None))
    /** Database column is_superuser SqlType(bool) */
    val isSuperuser: Rep[Boolean] = column[Boolean]("is_superuser")
    /** Database column username SqlType(varchar), Length(150,true) */
    val username: Rep[String] = column[String]("username", O.Length(150,varying=true))
    /** Database column first_name SqlType(varchar), Length(30,true) */
    val firstName: Rep[String] = column[String]("first_name", O.Length(30,varying=true))
    /** Database column last_name SqlType(varchar), Length(30,true) */
    val lastName: Rep[String] = column[String]("last_name", O.Length(30,varying=true))
    /** Database column email SqlType(varchar), Length(254,true) */
    val email: Rep[String] = column[String]("email", O.Length(254,varying=true))
    /** Database column is_staff SqlType(bool) */
    val isStaff: Rep[Boolean] = column[Boolean]("is_staff")
    /** Database column is_active SqlType(bool) */
    val isActive: Rep[Boolean] = column[Boolean]("is_active")
    /** Database column date_joined SqlType(timestamptz) */
    val dateJoined: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("date_joined")

    /** Index over (username) (database name auth_user_username_6821ab7c_like) */
    val index1 = index("auth_user_username_6821ab7c_like", username)
    /** Uniqueness Index over (username) (database name auth_user_username_key) */
    val index2 = index("auth_user_username_key", username, unique=true)
  }
  /** Collection-like TableQuery object for table AuthUser */
  lazy val AuthUser = new TableQuery(tag => new AuthUser(tag))

  /** Entity class storing rows of table AuthUserGroups
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(int4)
   *  @param groupId Database column group_id SqlType(int4) */
  case class AuthUserGroupsRow(id: Int, userId: Int, groupId: Int)
  /** GetResult implicit for fetching AuthUserGroupsRow objects using plain SQL queries */
  implicit def GetResultAuthUserGroupsRow(implicit e0: GR[Int]): GR[AuthUserGroupsRow] = GR{
    prs => import prs._
    AuthUserGroupsRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table auth_user_groups. Objects of this class serve as prototypes for rows in queries. */
  class AuthUserGroups(_tableTag: Tag) extends profile.api.Table[AuthUserGroupsRow](_tableTag, "auth_user_groups") {
    def * = (id, userId, groupId) <> (AuthUserGroupsRow.tupled, AuthUserGroupsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userId), Rep.Some(groupId)).shaped.<>({r=>import r._; _1.map(_=> AuthUserGroupsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column group_id SqlType(int4) */
    val groupId: Rep[Int] = column[Int]("group_id")

    /** Foreign key referencing AuthGroup (database name auth_user_groups_group_id_97559544_fk_auth_group_id) */
    lazy val authGroupFk = foreignKey("auth_user_groups_group_id_97559544_fk_auth_group_id", groupId, AuthGroup)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AuthUser (database name auth_user_groups_user_id_6a12ed8b_fk_auth_user_id) */
    lazy val authUserFk = foreignKey("auth_user_groups_user_id_6a12ed8b_fk_auth_user_id", userId, AuthUser)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (userId,groupId) (database name auth_user_groups_user_id_94350c0c_uniq) */
    val index1 = index("auth_user_groups_user_id_94350c0c_uniq", (userId, groupId), unique=true)
  }
  /** Collection-like TableQuery object for table AuthUserGroups */
  lazy val AuthUserGroups = new TableQuery(tag => new AuthUserGroups(tag))

  /** Entity class storing rows of table AuthUserUserPermissions
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(int4)
   *  @param permissionId Database column permission_id SqlType(int4) */
  case class AuthUserUserPermissionsRow(id: Int, userId: Int, permissionId: Int)
  /** GetResult implicit for fetching AuthUserUserPermissionsRow objects using plain SQL queries */
  implicit def GetResultAuthUserUserPermissionsRow(implicit e0: GR[Int]): GR[AuthUserUserPermissionsRow] = GR{
    prs => import prs._
    AuthUserUserPermissionsRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table auth_user_user_permissions. Objects of this class serve as prototypes for rows in queries. */
  class AuthUserUserPermissions(_tableTag: Tag) extends profile.api.Table[AuthUserUserPermissionsRow](_tableTag, "auth_user_user_permissions") {
    def * = (id, userId, permissionId) <> (AuthUserUserPermissionsRow.tupled, AuthUserUserPermissionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userId), Rep.Some(permissionId)).shaped.<>({r=>import r._; _1.map(_=> AuthUserUserPermissionsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column permission_id SqlType(int4) */
    val permissionId: Rep[Int] = column[Int]("permission_id")

    /** Foreign key referencing AuthPermission (database name auth_user_user_per_permission_id_1fbb5f2c_fk_auth_permission_id) */
    lazy val authPermissionFk = foreignKey("auth_user_user_per_permission_id_1fbb5f2c_fk_auth_permission_id", permissionId, AuthPermission)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AuthUser (database name auth_user_user_permissions_user_id_a95ead1b_fk_auth_user_id) */
    lazy val authUserFk = foreignKey("auth_user_user_permissions_user_id_a95ead1b_fk_auth_user_id", userId, AuthUser)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (userId,permissionId) (database name auth_user_user_permissions_user_id_14a6b632_uniq) */
    val index1 = index("auth_user_user_permissions_user_id_14a6b632_uniq", (userId, permissionId), unique=true)
  }
  /** Collection-like TableQuery object for table AuthUserUserPermissions */
  lazy val AuthUserUserPermissions = new TableQuery(tag => new AuthUserUserPermissions(tag))

  /** Entity class storing rows of table DjangoAdminLog
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param actionTime Database column action_time SqlType(timestamptz)
   *  @param objectId Database column object_id SqlType(text), Default(None)
   *  @param objectRepr Database column object_repr SqlType(varchar), Length(200,true)
   *  @param actionFlag Database column action_flag SqlType(int2)
   *  @param changeMessage Database column change_message SqlType(text)
   *  @param contentTypeId Database column content_type_id SqlType(int4), Default(None)
   *  @param userId Database column user_id SqlType(int4) */
  case class DjangoAdminLogRow(id: Int, actionTime: java.sql.Timestamp, objectId: Option[String] = None, objectRepr: String, actionFlag: Short, changeMessage: String, contentTypeId: Option[Int] = None, userId: Int)
  /** GetResult implicit for fetching DjangoAdminLogRow objects using plain SQL queries */
  implicit def GetResultDjangoAdminLogRow(implicit e0: GR[Int], e1: GR[java.sql.Timestamp], e2: GR[Option[String]], e3: GR[String], e4: GR[Short], e5: GR[Option[Int]]): GR[DjangoAdminLogRow] = GR{
    prs => import prs._
    DjangoAdminLogRow.tupled((<<[Int], <<[java.sql.Timestamp], <<?[String], <<[String], <<[Short], <<[String], <<?[Int], <<[Int]))
  }
  /** Table description of table django_admin_log. Objects of this class serve as prototypes for rows in queries. */
  class DjangoAdminLog(_tableTag: Tag) extends profile.api.Table[DjangoAdminLogRow](_tableTag, "django_admin_log") {
    def * = (id, actionTime, objectId, objectRepr, actionFlag, changeMessage, contentTypeId, userId) <> (DjangoAdminLogRow.tupled, DjangoAdminLogRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(actionTime), objectId, Rep.Some(objectRepr), Rep.Some(actionFlag), Rep.Some(changeMessage), contentTypeId, Rep.Some(userId)).shaped.<>({r=>import r._; _1.map(_=> DjangoAdminLogRow.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get, _7, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column action_time SqlType(timestamptz) */
    val actionTime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("action_time")
    /** Database column object_id SqlType(text), Default(None) */
    val objectId: Rep[Option[String]] = column[Option[String]]("object_id", O.Default(None))
    /** Database column object_repr SqlType(varchar), Length(200,true) */
    val objectRepr: Rep[String] = column[String]("object_repr", O.Length(200,varying=true))
    /** Database column action_flag SqlType(int2) */
    val actionFlag: Rep[Short] = column[Short]("action_flag")
    /** Database column change_message SqlType(text) */
    val changeMessage: Rep[String] = column[String]("change_message")
    /** Database column content_type_id SqlType(int4), Default(None) */
    val contentTypeId: Rep[Option[Int]] = column[Option[Int]]("content_type_id", O.Default(None))
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")

    /** Foreign key referencing AuthUser (database name django_admin_log_user_id_c564eba6_fk_auth_user_id) */
    lazy val authUserFk = foreignKey("django_admin_log_user_id_c564eba6_fk_auth_user_id", userId, AuthUser)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing DjangoContentType (database name django_admin_content_type_id_c4bce8eb_fk_django_content_type_id) */
    lazy val djangoContentTypeFk = foreignKey("django_admin_content_type_id_c4bce8eb_fk_django_content_type_id", contentTypeId, DjangoContentType)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table DjangoAdminLog */
  lazy val DjangoAdminLog = new TableQuery(tag => new DjangoAdminLog(tag))

  /** Entity class storing rows of table DjangoContentType
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param appLabel Database column app_label SqlType(varchar), Length(100,true)
   *  @param model Database column model SqlType(varchar), Length(100,true) */
  case class DjangoContentTypeRow(id: Int, appLabel: String, model: String)
  /** GetResult implicit for fetching DjangoContentTypeRow objects using plain SQL queries */
  implicit def GetResultDjangoContentTypeRow(implicit e0: GR[Int], e1: GR[String]): GR[DjangoContentTypeRow] = GR{
    prs => import prs._
    DjangoContentTypeRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table django_content_type. Objects of this class serve as prototypes for rows in queries. */
  class DjangoContentType(_tableTag: Tag) extends profile.api.Table[DjangoContentTypeRow](_tableTag, "django_content_type") {
    def * = (id, appLabel, model) <> (DjangoContentTypeRow.tupled, DjangoContentTypeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(appLabel), Rep.Some(model)).shaped.<>({r=>import r._; _1.map(_=> DjangoContentTypeRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column app_label SqlType(varchar), Length(100,true) */
    val appLabel: Rep[String] = column[String]("app_label", O.Length(100,varying=true))
    /** Database column model SqlType(varchar), Length(100,true) */
    val model: Rep[String] = column[String]("model", O.Length(100,varying=true))

    /** Uniqueness Index over (appLabel,model) (database name django_content_type_app_label_76bd3d3b_uniq) */
    val index1 = index("django_content_type_app_label_76bd3d3b_uniq", (appLabel, model), unique=true)
  }
  /** Collection-like TableQuery object for table DjangoContentType */
  lazy val DjangoContentType = new TableQuery(tag => new DjangoContentType(tag))

  /** Entity class storing rows of table DjangoMigrations
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param app Database column app SqlType(varchar), Length(255,true)
   *  @param name Database column name SqlType(varchar), Length(255,true)
   *  @param applied Database column applied SqlType(timestamptz) */
  case class DjangoMigrationsRow(id: Int, app: String, name: String, applied: java.sql.Timestamp)
  /** GetResult implicit for fetching DjangoMigrationsRow objects using plain SQL queries */
  implicit def GetResultDjangoMigrationsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[DjangoMigrationsRow] = GR{
    prs => import prs._
    DjangoMigrationsRow.tupled((<<[Int], <<[String], <<[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table django_migrations. Objects of this class serve as prototypes for rows in queries. */
  class DjangoMigrations(_tableTag: Tag) extends profile.api.Table[DjangoMigrationsRow](_tableTag, "django_migrations") {
    def * = (id, app, name, applied) <> (DjangoMigrationsRow.tupled, DjangoMigrationsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(app), Rep.Some(name), Rep.Some(applied)).shaped.<>({r=>import r._; _1.map(_=> DjangoMigrationsRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column app SqlType(varchar), Length(255,true) */
    val app: Rep[String] = column[String]("app", O.Length(255,varying=true))
    /** Database column name SqlType(varchar), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column applied SqlType(timestamptz) */
    val applied: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("applied")
  }
  /** Collection-like TableQuery object for table DjangoMigrations */
  lazy val DjangoMigrations = new TableQuery(tag => new DjangoMigrations(tag))

  /** Entity class storing rows of table DjangoSession
   *  @param sessionKey Database column session_key SqlType(varchar), PrimaryKey, Length(40,true)
   *  @param sessionData Database column session_data SqlType(text)
   *  @param expireDate Database column expire_date SqlType(timestamptz) */
  case class DjangoSessionRow(sessionKey: String, sessionData: String, expireDate: java.sql.Timestamp)
  /** GetResult implicit for fetching DjangoSessionRow objects using plain SQL queries */
  implicit def GetResultDjangoSessionRow(implicit e0: GR[String], e1: GR[java.sql.Timestamp]): GR[DjangoSessionRow] = GR{
    prs => import prs._
    DjangoSessionRow.tupled((<<[String], <<[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table django_session. Objects of this class serve as prototypes for rows in queries. */
  class DjangoSession(_tableTag: Tag) extends profile.api.Table[DjangoSessionRow](_tableTag, "django_session") {
    def * = (sessionKey, sessionData, expireDate) <> (DjangoSessionRow.tupled, DjangoSessionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(sessionKey), Rep.Some(sessionData), Rep.Some(expireDate)).shaped.<>({r=>import r._; _1.map(_=> DjangoSessionRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column session_key SqlType(varchar), PrimaryKey, Length(40,true) */
    val sessionKey: Rep[String] = column[String]("session_key", O.PrimaryKey, O.Length(40,varying=true))
    /** Database column session_data SqlType(text) */
    val sessionData: Rep[String] = column[String]("session_data")
    /** Database column expire_date SqlType(timestamptz) */
    val expireDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("expire_date")

    /** Index over (expireDate) (database name django_session_de54fa62) */
    val index1 = index("django_session_de54fa62", expireDate)
  }
  /** Collection-like TableQuery object for table DjangoSession */
  lazy val DjangoSession = new TableQuery(tag => new DjangoSession(tag))

  /** Entity class storing rows of table EasyThumbnailsSource
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param storageHash Database column storage_hash SqlType(varchar), Length(40,true)
   *  @param name Database column name SqlType(varchar), Length(255,true)
   *  @param modified Database column modified SqlType(timestamptz) */
  case class EasyThumbnailsSourceRow(id: Int, storageHash: String, name: String, modified: java.sql.Timestamp)
  /** GetResult implicit for fetching EasyThumbnailsSourceRow objects using plain SQL queries */
  implicit def GetResultEasyThumbnailsSourceRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[EasyThumbnailsSourceRow] = GR{
    prs => import prs._
    EasyThumbnailsSourceRow.tupled((<<[Int], <<[String], <<[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table easy_thumbnails_source. Objects of this class serve as prototypes for rows in queries. */
  class EasyThumbnailsSource(_tableTag: Tag) extends profile.api.Table[EasyThumbnailsSourceRow](_tableTag, "easy_thumbnails_source") {
    def * = (id, storageHash, name, modified) <> (EasyThumbnailsSourceRow.tupled, EasyThumbnailsSourceRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(storageHash), Rep.Some(name), Rep.Some(modified)).shaped.<>({r=>import r._; _1.map(_=> EasyThumbnailsSourceRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column storage_hash SqlType(varchar), Length(40,true) */
    val storageHash: Rep[String] = column[String]("storage_hash", O.Length(40,varying=true))
    /** Database column name SqlType(varchar), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column modified SqlType(timestamptz) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")

    /** Index over (name) (database name easy_thumbnails_source_b068931c) */
    val index1 = index("easy_thumbnails_source_b068931c", name)
    /** Index over (storageHash) (database name easy_thumbnails_source_b454e115) */
    val index2 = index("easy_thumbnails_source_b454e115", storageHash)
    /** Index over (name) (database name easy_thumbnails_source_name_5fe0edc6_like) */
    val index3 = index("easy_thumbnails_source_name_5fe0edc6_like", name)
    /** Uniqueness Index over (storageHash,name) (database name easy_thumbnails_source_storage_hash_481ce32d_uniq) */
    val index4 = index("easy_thumbnails_source_storage_hash_481ce32d_uniq", (storageHash, name), unique=true)
    /** Index over (storageHash) (database name easy_thumbnails_source_storage_hash_946cbcc9_like) */
    val index5 = index("easy_thumbnails_source_storage_hash_946cbcc9_like", storageHash)
  }
  /** Collection-like TableQuery object for table EasyThumbnailsSource */
  lazy val EasyThumbnailsSource = new TableQuery(tag => new EasyThumbnailsSource(tag))

  /** Entity class storing rows of table EasyThumbnailsThumbnail
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param storageHash Database column storage_hash SqlType(varchar), Length(40,true)
   *  @param name Database column name SqlType(varchar), Length(255,true)
   *  @param modified Database column modified SqlType(timestamptz)
   *  @param sourceId Database column source_id SqlType(int4) */
  case class EasyThumbnailsThumbnailRow(id: Int, storageHash: String, name: String, modified: java.sql.Timestamp, sourceId: Int)
  /** GetResult implicit for fetching EasyThumbnailsThumbnailRow objects using plain SQL queries */
  implicit def GetResultEasyThumbnailsThumbnailRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[EasyThumbnailsThumbnailRow] = GR{
    prs => import prs._
    EasyThumbnailsThumbnailRow.tupled((<<[Int], <<[String], <<[String], <<[java.sql.Timestamp], <<[Int]))
  }
  /** Table description of table easy_thumbnails_thumbnail. Objects of this class serve as prototypes for rows in queries. */
  class EasyThumbnailsThumbnail(_tableTag: Tag) extends profile.api.Table[EasyThumbnailsThumbnailRow](_tableTag, "easy_thumbnails_thumbnail") {
    def * = (id, storageHash, name, modified, sourceId) <> (EasyThumbnailsThumbnailRow.tupled, EasyThumbnailsThumbnailRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(storageHash), Rep.Some(name), Rep.Some(modified), Rep.Some(sourceId)).shaped.<>({r=>import r._; _1.map(_=> EasyThumbnailsThumbnailRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column storage_hash SqlType(varchar), Length(40,true) */
    val storageHash: Rep[String] = column[String]("storage_hash", O.Length(40,varying=true))
    /** Database column name SqlType(varchar), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column modified SqlType(timestamptz) */
    val modified: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified")
    /** Database column source_id SqlType(int4) */
    val sourceId: Rep[Int] = column[Int]("source_id")

    /** Foreign key referencing EasyThumbnailsSource (database name easy_thumbnails_source_id_5b57bc77_fk_easy_thumbnails_source_id) */
    lazy val easyThumbnailsSourceFk = foreignKey("easy_thumbnails_source_id_5b57bc77_fk_easy_thumbnails_source_id", sourceId, EasyThumbnailsSource)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (name) (database name easy_thumbnails_thumbnail_b068931c) */
    val index1 = index("easy_thumbnails_thumbnail_b068931c", name)
    /** Index over (storageHash) (database name easy_thumbnails_thumbnail_b454e115) */
    val index2 = index("easy_thumbnails_thumbnail_b454e115", storageHash)
    /** Index over (name) (database name easy_thumbnails_thumbnail_name_b5882c31_like) */
    val index3 = index("easy_thumbnails_thumbnail_name_b5882c31_like", name)
    /** Index over (storageHash) (database name easy_thumbnails_thumbnail_storage_hash_f1435f49_like) */
    val index4 = index("easy_thumbnails_thumbnail_storage_hash_f1435f49_like", storageHash)
    /** Uniqueness Index over (storageHash,name,sourceId) (database name easy_thumbnails_thumbnail_storage_hash_fb375270_uniq) */
    val index5 = index("easy_thumbnails_thumbnail_storage_hash_fb375270_uniq", (storageHash, name, sourceId), unique=true)
  }
  /** Collection-like TableQuery object for table EasyThumbnailsThumbnail */
  lazy val EasyThumbnailsThumbnail = new TableQuery(tag => new EasyThumbnailsThumbnail(tag))

  /** Entity class storing rows of table EasyThumbnailsThumbnaildimensions
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param thumbnailId Database column thumbnail_id SqlType(int4)
   *  @param width Database column width SqlType(int4), Default(None)
   *  @param height Database column height SqlType(int4), Default(None) */
  case class EasyThumbnailsThumbnaildimensionsRow(id: Int, thumbnailId: Int, width: Option[Int] = None, height: Option[Int] = None)
  /** GetResult implicit for fetching EasyThumbnailsThumbnaildimensionsRow objects using plain SQL queries */
  implicit def GetResultEasyThumbnailsThumbnaildimensionsRow(implicit e0: GR[Int], e1: GR[Option[Int]]): GR[EasyThumbnailsThumbnaildimensionsRow] = GR{
    prs => import prs._
    EasyThumbnailsThumbnaildimensionsRow.tupled((<<[Int], <<[Int], <<?[Int], <<?[Int]))
  }
  /** Table description of table easy_thumbnails_thumbnaildimensions. Objects of this class serve as prototypes for rows in queries. */
  class EasyThumbnailsThumbnaildimensions(_tableTag: Tag) extends profile.api.Table[EasyThumbnailsThumbnaildimensionsRow](_tableTag, "easy_thumbnails_thumbnaildimensions") {
    def * = (id, thumbnailId, width, height) <> (EasyThumbnailsThumbnaildimensionsRow.tupled, EasyThumbnailsThumbnaildimensionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(thumbnailId), width, height).shaped.<>({r=>import r._; _1.map(_=> EasyThumbnailsThumbnaildimensionsRow.tupled((_1.get, _2.get, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column thumbnail_id SqlType(int4) */
    val thumbnailId: Rep[Int] = column[Int]("thumbnail_id")
    /** Database column width SqlType(int4), Default(None) */
    val width: Rep[Option[Int]] = column[Option[Int]]("width", O.Default(None))
    /** Database column height SqlType(int4), Default(None) */
    val height: Rep[Option[Int]] = column[Option[Int]]("height", O.Default(None))

    /** Foreign key referencing EasyThumbnailsThumbnail (database name easy_thum_thumbnail_id_c3a0c549_fk_easy_thumbnails_thumbnail_id) */
    lazy val easyThumbnailsThumbnailFk = foreignKey("easy_thum_thumbnail_id_c3a0c549_fk_easy_thumbnails_thumbnail_id", thumbnailId, EasyThumbnailsThumbnail)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (thumbnailId) (database name easy_thumbnails_thumbnaildimensions_thumbnail_id_key) */
    val index1 = index("easy_thumbnails_thumbnaildimensions_thumbnail_id_key", thumbnailId, unique=true)
  }
  /** Collection-like TableQuery object for table EasyThumbnailsThumbnaildimensions */
  lazy val EasyThumbnailsThumbnaildimensions = new TableQuery(tag => new EasyThumbnailsThumbnaildimensions(tag))

  /** Entity class storing rows of table FilerClipboard
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(int4) */
  case class FilerClipboardRow(id: Int, userId: Int)
  /** GetResult implicit for fetching FilerClipboardRow objects using plain SQL queries */
  implicit def GetResultFilerClipboardRow(implicit e0: GR[Int]): GR[FilerClipboardRow] = GR{
    prs => import prs._
    FilerClipboardRow.tupled((<<[Int], <<[Int]))
  }
  /** Table description of table filer_clipboard. Objects of this class serve as prototypes for rows in queries. */
  class FilerClipboard(_tableTag: Tag) extends profile.api.Table[FilerClipboardRow](_tableTag, "filer_clipboard") {
    def * = (id, userId) <> (FilerClipboardRow.tupled, FilerClipboardRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userId)).shaped.<>({r=>import r._; _1.map(_=> FilerClipboardRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")

    /** Foreign key referencing AuthUser (database name filer_clipboard_user_id_b52ff0bc_fk_auth_user_id) */
    lazy val authUserFk = foreignKey("filer_clipboard_user_id_b52ff0bc_fk_auth_user_id", userId, AuthUser)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table FilerClipboard */
  lazy val FilerClipboard = new TableQuery(tag => new FilerClipboard(tag))

  /** Entity class storing rows of table FilerClipboarditem
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param clipboardId Database column clipboard_id SqlType(int4)
   *  @param fileId Database column file_id SqlType(int4) */
  case class FilerClipboarditemRow(id: Int, clipboardId: Int, fileId: Int)
  /** GetResult implicit for fetching FilerClipboarditemRow objects using plain SQL queries */
  implicit def GetResultFilerClipboarditemRow(implicit e0: GR[Int]): GR[FilerClipboarditemRow] = GR{
    prs => import prs._
    FilerClipboarditemRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table filer_clipboarditem. Objects of this class serve as prototypes for rows in queries. */
  class FilerClipboarditem(_tableTag: Tag) extends profile.api.Table[FilerClipboarditemRow](_tableTag, "filer_clipboarditem") {
    def * = (id, clipboardId, fileId) <> (FilerClipboarditemRow.tupled, FilerClipboarditemRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(clipboardId), Rep.Some(fileId)).shaped.<>({r=>import r._; _1.map(_=> FilerClipboarditemRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column clipboard_id SqlType(int4) */
    val clipboardId: Rep[Int] = column[Int]("clipboard_id")
    /** Database column file_id SqlType(int4) */
    val fileId: Rep[Int] = column[Int]("file_id")

    /** Foreign key referencing FilerClipboard (database name filer_clipboarditem_clipboard_id_7a76518b_fk_filer_clipboard_id) */
    lazy val filerClipboardFk = foreignKey("filer_clipboarditem_clipboard_id_7a76518b_fk_filer_clipboard_id", clipboardId, FilerClipboard)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing FilerFile (database name filer_clipboarditem_file_id_06196f80_fk_filer_file_id) */
    lazy val filerFileFk = foreignKey("filer_clipboarditem_file_id_06196f80_fk_filer_file_id", fileId, FilerFile)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table FilerClipboarditem */
  lazy val FilerClipboarditem = new TableQuery(tag => new FilerClipboarditem(tag))

  /** Entity class storing rows of table FilerFile
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param file Database column file SqlType(varchar), Length(255,true), Default(None)
   *  @param _FileSize Database column _file_size SqlType(int4), Default(None)
   *  @param sha1 Database column sha1 SqlType(varchar), Length(40,true)
   *  @param hasAllMandatoryData Database column has_all_mandatory_data SqlType(bool)
   *  @param originalFilename Database column original_filename SqlType(varchar), Length(255,true), Default(None)
   *  @param name Database column name SqlType(varchar), Length(255,true)
   *  @param description Database column description SqlType(text), Default(None)
   *  @param uploadedAt Database column uploaded_at SqlType(timestamptz)
   *  @param modifiedAt Database column modified_at SqlType(timestamptz)
   *  @param isPublic Database column is_public SqlType(bool)
   *  @param folderId Database column folder_id SqlType(int4), Default(None)
   *  @param ownerId Database column owner_id SqlType(int4), Default(None)
   *  @param polymorphicCtypeId Database column polymorphic_ctype_id SqlType(int4), Default(None) */
  case class FilerFileRow(id: Int, file: Option[String] = None, _FileSize: Option[Int] = None, sha1: String, hasAllMandatoryData: Boolean, originalFilename: Option[String] = None, name: String, description: Option[String] = None, uploadedAt: java.sql.Timestamp, modifiedAt: java.sql.Timestamp, isPublic: Boolean, folderId: Option[Int] = None, ownerId: Option[Int] = None, polymorphicCtypeId: Option[Int] = None)
  /** GetResult implicit for fetching FilerFileRow objects using plain SQL queries */
  implicit def GetResultFilerFileRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[Option[Int]], e3: GR[String], e4: GR[Boolean], e5: GR[java.sql.Timestamp]): GR[FilerFileRow] = GR{
    prs => import prs._
    FilerFileRow.tupled((<<[Int], <<?[String], <<?[Int], <<[String], <<[Boolean], <<?[String], <<[String], <<?[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[Boolean], <<?[Int], <<?[Int], <<?[Int]))
  }
  /** Table description of table filer_file. Objects of this class serve as prototypes for rows in queries. */
  class FilerFile(_tableTag: Tag) extends profile.api.Table[FilerFileRow](_tableTag, "filer_file") {
    def * = (id, file, _FileSize, sha1, hasAllMandatoryData, originalFilename, name, description, uploadedAt, modifiedAt, isPublic, folderId, ownerId, polymorphicCtypeId) <> (FilerFileRow.tupled, FilerFileRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), file, _FileSize, Rep.Some(sha1), Rep.Some(hasAllMandatoryData), originalFilename, Rep.Some(name), description, Rep.Some(uploadedAt), Rep.Some(modifiedAt), Rep.Some(isPublic), folderId, ownerId, polymorphicCtypeId).shaped.<>({r=>import r._; _1.map(_=> FilerFileRow.tupled((_1.get, _2, _3, _4.get, _5.get, _6, _7.get, _8, _9.get, _10.get, _11.get, _12, _13, _14)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column file SqlType(varchar), Length(255,true), Default(None) */
    val file: Rep[Option[String]] = column[Option[String]]("file", O.Length(255,varying=true), O.Default(None))
    /** Database column _file_size SqlType(int4), Default(None) */
    val _FileSize: Rep[Option[Int]] = column[Option[Int]]("_file_size", O.Default(None))
    /** Database column sha1 SqlType(varchar), Length(40,true) */
    val sha1: Rep[String] = column[String]("sha1", O.Length(40,varying=true))
    /** Database column has_all_mandatory_data SqlType(bool) */
    val hasAllMandatoryData: Rep[Boolean] = column[Boolean]("has_all_mandatory_data")
    /** Database column original_filename SqlType(varchar), Length(255,true), Default(None) */
    val originalFilename: Rep[Option[String]] = column[Option[String]]("original_filename", O.Length(255,varying=true), O.Default(None))
    /** Database column name SqlType(varchar), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column description SqlType(text), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Default(None))
    /** Database column uploaded_at SqlType(timestamptz) */
    val uploadedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("uploaded_at")
    /** Database column modified_at SqlType(timestamptz) */
    val modifiedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified_at")
    /** Database column is_public SqlType(bool) */
    val isPublic: Rep[Boolean] = column[Boolean]("is_public")
    /** Database column folder_id SqlType(int4), Default(None) */
    val folderId: Rep[Option[Int]] = column[Option[Int]]("folder_id", O.Default(None))
    /** Database column owner_id SqlType(int4), Default(None) */
    val ownerId: Rep[Option[Int]] = column[Option[Int]]("owner_id", O.Default(None))
    /** Database column polymorphic_ctype_id SqlType(int4), Default(None) */
    val polymorphicCtypeId: Rep[Option[Int]] = column[Option[Int]]("polymorphic_ctype_id", O.Default(None))

    /** Foreign key referencing AuthUser (database name filer_file_owner_id_b9e32671_fk_auth_user_id) */
    lazy val authUserFk = foreignKey("filer_file_owner_id_b9e32671_fk_auth_user_id", ownerId, AuthUser)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing DjangoContentType (database name filer_f_polymorphic_ctype_id_f44903c1_fk_django_content_type_id) */
    lazy val djangoContentTypeFk = foreignKey("filer_f_polymorphic_ctype_id_f44903c1_fk_django_content_type_id", polymorphicCtypeId, DjangoContentType)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing FilerFolder (database name filer_file_folder_id_af803bbb_fk_filer_folder_id) */
    lazy val filerFolderFk = foreignKey("filer_file_folder_id_af803bbb_fk_filer_folder_id", folderId, FilerFolder)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table FilerFile */
  lazy val FilerFile = new TableQuery(tag => new FilerFile(tag))

  /** Entity class storing rows of table FilerFolder
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(255,true)
   *  @param uploadedAt Database column uploaded_at SqlType(timestamptz)
   *  @param createdAt Database column created_at SqlType(timestamptz)
   *  @param modifiedAt Database column modified_at SqlType(timestamptz)
   *  @param lft Database column lft SqlType(int4)
   *  @param rght Database column rght SqlType(int4)
   *  @param treeId Database column tree_id SqlType(int4)
   *  @param level Database column level SqlType(int4)
   *  @param ownerId Database column owner_id SqlType(int4), Default(None)
   *  @param parentId Database column parent_id SqlType(int4), Default(None) */
  case class FilerFolderRow(id: Int, name: String, uploadedAt: java.sql.Timestamp, createdAt: java.sql.Timestamp, modifiedAt: java.sql.Timestamp, lft: Int, rght: Int, treeId: Int, level: Int, ownerId: Option[Int] = None, parentId: Option[Int] = None)
  /** GetResult implicit for fetching FilerFolderRow objects using plain SQL queries */
  implicit def GetResultFilerFolderRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Option[Int]]): GR[FilerFolderRow] = GR{
    prs => import prs._
    FilerFolderRow.tupled((<<[Int], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[Int], <<[Int], <<[Int], <<[Int], <<?[Int], <<?[Int]))
  }
  /** Table description of table filer_folder. Objects of this class serve as prototypes for rows in queries. */
  class FilerFolder(_tableTag: Tag) extends profile.api.Table[FilerFolderRow](_tableTag, "filer_folder") {
    def * = (id, name, uploadedAt, createdAt, modifiedAt, lft, rght, treeId, level, ownerId, parentId) <> (FilerFolderRow.tupled, FilerFolderRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(uploadedAt), Rep.Some(createdAt), Rep.Some(modifiedAt), Rep.Some(lft), Rep.Some(rght), Rep.Some(treeId), Rep.Some(level), ownerId, parentId).shaped.<>({r=>import r._; _1.map(_=> FilerFolderRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10, _11)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column uploaded_at SqlType(timestamptz) */
    val uploadedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("uploaded_at")
    /** Database column created_at SqlType(timestamptz) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column modified_at SqlType(timestamptz) */
    val modifiedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("modified_at")
    /** Database column lft SqlType(int4) */
    val lft: Rep[Int] = column[Int]("lft")
    /** Database column rght SqlType(int4) */
    val rght: Rep[Int] = column[Int]("rght")
    /** Database column tree_id SqlType(int4) */
    val treeId: Rep[Int] = column[Int]("tree_id")
    /** Database column level SqlType(int4) */
    val level: Rep[Int] = column[Int]("level")
    /** Database column owner_id SqlType(int4), Default(None) */
    val ownerId: Rep[Option[Int]] = column[Option[Int]]("owner_id", O.Default(None))
    /** Database column parent_id SqlType(int4), Default(None) */
    val parentId: Rep[Option[Int]] = column[Option[Int]]("parent_id", O.Default(None))

    /** Foreign key referencing AuthUser (database name filer_folder_owner_id_be530fb4_fk_auth_user_id) */
    lazy val authUserFk = foreignKey("filer_folder_owner_id_be530fb4_fk_auth_user_id", ownerId, AuthUser)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing FilerFolder (database name filer_folder_parent_id_308aecda_fk_filer_folder_id) */
    lazy val filerFolderFk = foreignKey("filer_folder_parent_id_308aecda_fk_filer_folder_id", parentId, FilerFolder)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Index over (rght) (database name filer_folder_3cfbd988) */
    val index1 = index("filer_folder_3cfbd988", rght)
    /** Index over (treeId) (database name filer_folder_656442a0) */
    val index2 = index("filer_folder_656442a0", treeId)
    /** Index over (level) (database name filer_folder_c9e9a848) */
    val index3 = index("filer_folder_c9e9a848", level)
    /** Index over (lft) (database name filer_folder_caf7cc51) */
    val index4 = index("filer_folder_caf7cc51", lft)
    /** Uniqueness Index over (parentId,name) (database name filer_folder_parent_id_bc773258_uniq) */
    val index5 = index("filer_folder_parent_id_bc773258_uniq", (parentId, name), unique=true)
  }
  /** Collection-like TableQuery object for table FilerFolder */
  lazy val FilerFolder = new TableQuery(tag => new FilerFolder(tag))

  /** Entity class storing rows of table FilerFolderpermission
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param `type` Database column type SqlType(int2)
   *  @param everybody Database column everybody SqlType(bool)
   *  @param canEdit Database column can_edit SqlType(int2), Default(None)
   *  @param canRead Database column can_read SqlType(int2), Default(None)
   *  @param canAddChildren Database column can_add_children SqlType(int2), Default(None)
   *  @param folderId Database column folder_id SqlType(int4), Default(None)
   *  @param groupId Database column group_id SqlType(int4), Default(None)
   *  @param userId Database column user_id SqlType(int4), Default(None) */
  case class FilerFolderpermissionRow(id: Int, `type`: Short, everybody: Boolean, canEdit: Option[Short] = None, canRead: Option[Short] = None, canAddChildren: Option[Short] = None, folderId: Option[Int] = None, groupId: Option[Int] = None, userId: Option[Int] = None)
  /** GetResult implicit for fetching FilerFolderpermissionRow objects using plain SQL queries */
  implicit def GetResultFilerFolderpermissionRow(implicit e0: GR[Int], e1: GR[Short], e2: GR[Boolean], e3: GR[Option[Short]], e4: GR[Option[Int]]): GR[FilerFolderpermissionRow] = GR{
    prs => import prs._
    FilerFolderpermissionRow.tupled((<<[Int], <<[Short], <<[Boolean], <<?[Short], <<?[Short], <<?[Short], <<?[Int], <<?[Int], <<?[Int]))
  }
  /** Table description of table filer_folderpermission. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class FilerFolderpermission(_tableTag: Tag) extends profile.api.Table[FilerFolderpermissionRow](_tableTag, "filer_folderpermission") {
    def * = (id, `type`, everybody, canEdit, canRead, canAddChildren, folderId, groupId, userId) <> (FilerFolderpermissionRow.tupled, FilerFolderpermissionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(`type`), Rep.Some(everybody), canEdit, canRead, canAddChildren, folderId, groupId, userId).shaped.<>({r=>import r._; _1.map(_=> FilerFolderpermissionRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column type SqlType(int2)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[Short] = column[Short]("type")
    /** Database column everybody SqlType(bool) */
    val everybody: Rep[Boolean] = column[Boolean]("everybody")
    /** Database column can_edit SqlType(int2), Default(None) */
    val canEdit: Rep[Option[Short]] = column[Option[Short]]("can_edit", O.Default(None))
    /** Database column can_read SqlType(int2), Default(None) */
    val canRead: Rep[Option[Short]] = column[Option[Short]]("can_read", O.Default(None))
    /** Database column can_add_children SqlType(int2), Default(None) */
    val canAddChildren: Rep[Option[Short]] = column[Option[Short]]("can_add_children", O.Default(None))
    /** Database column folder_id SqlType(int4), Default(None) */
    val folderId: Rep[Option[Int]] = column[Option[Int]]("folder_id", O.Default(None))
    /** Database column group_id SqlType(int4), Default(None) */
    val groupId: Rep[Option[Int]] = column[Option[Int]]("group_id", O.Default(None))
    /** Database column user_id SqlType(int4), Default(None) */
    val userId: Rep[Option[Int]] = column[Option[Int]]("user_id", O.Default(None))

    /** Foreign key referencing AuthGroup (database name filer_folderpermission_group_id_8901bafa_fk_auth_group_id) */
    lazy val authGroupFk = foreignKey("filer_folderpermission_group_id_8901bafa_fk_auth_group_id", groupId, AuthGroup)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing AuthUser (database name filer_folderpermission_user_id_7673d4b6_fk_auth_user_id) */
    lazy val authUserFk = foreignKey("filer_folderpermission_user_id_7673d4b6_fk_auth_user_id", userId, AuthUser)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing FilerFolder (database name filer_folderpermission_folder_id_5d02f1da_fk_filer_folder_id) */
    lazy val filerFolderFk = foreignKey("filer_folderpermission_folder_id_5d02f1da_fk_filer_folder_id", folderId, FilerFolder)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table FilerFolderpermission */
  lazy val FilerFolderpermission = new TableQuery(tag => new FilerFolderpermission(tag))

  /** Entity class storing rows of table FilerImage
   *  @param filePtrId Database column file_ptr_id SqlType(int4), PrimaryKey
   *  @param _Height Database column _height SqlType(int4), Default(None)
   *  @param _Width Database column _width SqlType(int4), Default(None)
   *  @param dateTaken Database column date_taken SqlType(timestamptz), Default(None)
   *  @param defaultAltText Database column default_alt_text SqlType(varchar), Length(255,true), Default(None)
   *  @param defaultCaption Database column default_caption SqlType(varchar), Length(255,true), Default(None)
   *  @param author Database column author SqlType(varchar), Length(255,true), Default(None)
   *  @param mustAlwaysPublishAuthorCredit Database column must_always_publish_author_credit SqlType(bool)
   *  @param mustAlwaysPublishCopyright Database column must_always_publish_copyright SqlType(bool)
   *  @param subjectLocation Database column subject_location SqlType(varchar), Length(64,true) */
  case class FilerImageRow(filePtrId: Int, _Height: Option[Int] = None, _Width: Option[Int] = None, dateTaken: Option[java.sql.Timestamp] = None, defaultAltText: Option[String] = None, defaultCaption: Option[String] = None, author: Option[String] = None, mustAlwaysPublishAuthorCredit: Boolean, mustAlwaysPublishCopyright: Boolean, subjectLocation: String)
  /** GetResult implicit for fetching FilerImageRow objects using plain SQL queries */
  implicit def GetResultFilerImageRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[Option[java.sql.Timestamp]], e3: GR[Option[String]], e4: GR[Boolean], e5: GR[String]): GR[FilerImageRow] = GR{
    prs => import prs._
    FilerImageRow.tupled((<<[Int], <<?[Int], <<?[Int], <<?[java.sql.Timestamp], <<?[String], <<?[String], <<?[String], <<[Boolean], <<[Boolean], <<[String]))
  }
  /** Table description of table filer_image. Objects of this class serve as prototypes for rows in queries. */
  class FilerImage(_tableTag: Tag) extends profile.api.Table[FilerImageRow](_tableTag, "filer_image") {
    def * = (filePtrId, _Height, _Width, dateTaken, defaultAltText, defaultCaption, author, mustAlwaysPublishAuthorCredit, mustAlwaysPublishCopyright, subjectLocation) <> (FilerImageRow.tupled, FilerImageRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(filePtrId), _Height, _Width, dateTaken, defaultAltText, defaultCaption, author, Rep.Some(mustAlwaysPublishAuthorCredit), Rep.Some(mustAlwaysPublishCopyright), Rep.Some(subjectLocation)).shaped.<>({r=>import r._; _1.map(_=> FilerImageRow.tupled((_1.get, _2, _3, _4, _5, _6, _7, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column file_ptr_id SqlType(int4), PrimaryKey */
    val filePtrId: Rep[Int] = column[Int]("file_ptr_id", O.PrimaryKey)
    /** Database column _height SqlType(int4), Default(None) */
    val _Height: Rep[Option[Int]] = column[Option[Int]]("_height", O.Default(None))
    /** Database column _width SqlType(int4), Default(None) */
    val _Width: Rep[Option[Int]] = column[Option[Int]]("_width", O.Default(None))
    /** Database column date_taken SqlType(timestamptz), Default(None) */
    val dateTaken: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("date_taken", O.Default(None))
    /** Database column default_alt_text SqlType(varchar), Length(255,true), Default(None) */
    val defaultAltText: Rep[Option[String]] = column[Option[String]]("default_alt_text", O.Length(255,varying=true), O.Default(None))
    /** Database column default_caption SqlType(varchar), Length(255,true), Default(None) */
    val defaultCaption: Rep[Option[String]] = column[Option[String]]("default_caption", O.Length(255,varying=true), O.Default(None))
    /** Database column author SqlType(varchar), Length(255,true), Default(None) */
    val author: Rep[Option[String]] = column[Option[String]]("author", O.Length(255,varying=true), O.Default(None))
    /** Database column must_always_publish_author_credit SqlType(bool) */
    val mustAlwaysPublishAuthorCredit: Rep[Boolean] = column[Boolean]("must_always_publish_author_credit")
    /** Database column must_always_publish_copyright SqlType(bool) */
    val mustAlwaysPublishCopyright: Rep[Boolean] = column[Boolean]("must_always_publish_copyright")
    /** Database column subject_location SqlType(varchar), Length(64,true) */
    val subjectLocation: Rep[String] = column[String]("subject_location", O.Length(64,varying=true))

    /** Foreign key referencing FilerFile (database name filer_image_file_ptr_id_3e21d4f0_fk_filer_file_id) */
    lazy val filerFileFk = foreignKey("filer_image_file_ptr_id_3e21d4f0_fk_filer_file_id", filePtrId, FilerFile)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table FilerImage */
  lazy val FilerImage = new TableQuery(tag => new FilerImage(tag))

  /** Entity class storing rows of table FilerThumbnailoption
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(100,true)
   *  @param width Database column width SqlType(int4)
   *  @param height Database column height SqlType(int4)
   *  @param crop Database column crop SqlType(bool)
   *  @param upscale Database column upscale SqlType(bool) */
  case class FilerThumbnailoptionRow(id: Int, name: String, width: Int, height: Int, crop: Boolean, upscale: Boolean)
  /** GetResult implicit for fetching FilerThumbnailoptionRow objects using plain SQL queries */
  implicit def GetResultFilerThumbnailoptionRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Boolean]): GR[FilerThumbnailoptionRow] = GR{
    prs => import prs._
    FilerThumbnailoptionRow.tupled((<<[Int], <<[String], <<[Int], <<[Int], <<[Boolean], <<[Boolean]))
  }
  /** Table description of table filer_thumbnailoption. Objects of this class serve as prototypes for rows in queries. */
  class FilerThumbnailoption(_tableTag: Tag) extends profile.api.Table[FilerThumbnailoptionRow](_tableTag, "filer_thumbnailoption") {
    def * = (id, name, width, height, crop, upscale) <> (FilerThumbnailoptionRow.tupled, FilerThumbnailoptionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(width), Rep.Some(height), Rep.Some(crop), Rep.Some(upscale)).shaped.<>({r=>import r._; _1.map(_=> FilerThumbnailoptionRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(100,true) */
    val name: Rep[String] = column[String]("name", O.Length(100,varying=true))
    /** Database column width SqlType(int4) */
    val width: Rep[Int] = column[Int]("width")
    /** Database column height SqlType(int4) */
    val height: Rep[Int] = column[Int]("height")
    /** Database column crop SqlType(bool) */
    val crop: Rep[Boolean] = column[Boolean]("crop")
    /** Database column upscale SqlType(bool) */
    val upscale: Rep[Boolean] = column[Boolean]("upscale")
  }
  /** Collection-like TableQuery object for table FilerThumbnailoption */
  lazy val FilerThumbnailoption = new TableQuery(tag => new FilerThumbnailoption(tag))

  /** Entity class storing rows of table TubesiteCategory
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(50,true)
   *  @param imageId Database column image_id SqlType(int4) */
  case class TubesiteCategoryRow(id: Int, name: String, imageId: Int)
  /** GetResult implicit for fetching TubesiteCategoryRow objects using plain SQL queries */
  implicit def GetResultTubesiteCategoryRow(implicit e0: GR[Int], e1: GR[String]): GR[TubesiteCategoryRow] = GR{
    prs => import prs._
    TubesiteCategoryRow.tupled((<<[Int], <<[String], <<[Int]))
  }
  /** Table description of table tubesite_category. Objects of this class serve as prototypes for rows in queries. */
  class TubesiteCategory(_tableTag: Tag) extends profile.api.Table[TubesiteCategoryRow](_tableTag, "tubesite_category") {
    def * = (id, name, imageId) <> (TubesiteCategoryRow.tupled, TubesiteCategoryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(imageId)).shaped.<>({r=>import r._; _1.map(_=> TubesiteCategoryRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(50,true) */
    val name: Rep[String] = column[String]("name", O.Length(50,varying=true))
    /** Database column image_id SqlType(int4) */
    val imageId: Rep[Int] = column[Int]("image_id")

    /** Foreign key referencing FilerImage (database name tubesite_category_image_id_edac9b80_fk_filer_image_file_ptr_id) */
    lazy val filerImageFk = foreignKey("tubesite_category_image_id_edac9b80_fk_filer_image_file_ptr_id", imageId, FilerImage)(r => r.filePtrId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table TubesiteCategory */
  lazy val TubesiteCategory = new TableQuery(tag => new TubesiteCategory(tag))

  /** Entity class storing rows of table TubesiteVideo
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(100,true)
   *  @param desc Database column desc SqlType(text)
   *  @param poster Database column poster SqlType(varchar), Length(200,true)
   *  @param src Database column src SqlType(varchar), Length(200,true)
   *  @param duration Database column duration SqlType(int4)
   *  @param quality Database column quality SqlType(int4)
   *  @param multiple Database column multiple SqlType(bool)
   *  @param extra Database column extra SqlType(jsonb), Length(2147483647,false), Default(None)
   *  @param categoryId Database column category_id SqlType(int4), Default(None) */
  case class TubesiteVideoRow(id: Int, name: String, desc: String, poster: String, src: String, duration: Int, quality: Int, multiple: Boolean, extra: Option[String] = None, categoryId: Option[Int] = None)
  /** GetResult implicit for fetching TubesiteVideoRow objects using plain SQL queries */
  implicit def GetResultTubesiteVideoRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Boolean], e3: GR[Option[String]], e4: GR[Option[Int]]): GR[TubesiteVideoRow] = GR{
    prs => import prs._
    TubesiteVideoRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[String], <<[Int], <<[Int], <<[Boolean], <<?[String], <<?[Int]))
  }
  /** Table description of table tubesite_video. Objects of this class serve as prototypes for rows in queries. */
  class TubesiteVideo(_tableTag: Tag) extends profile.api.Table[TubesiteVideoRow](_tableTag, "tubesite_video") {
    def * = (id, name, desc, poster, src, duration, quality, multiple, extra, categoryId) <> (TubesiteVideoRow.tupled, TubesiteVideoRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(desc), Rep.Some(poster), Rep.Some(src), Rep.Some(duration), Rep.Some(quality), Rep.Some(multiple), extra, categoryId).shaped.<>({r=>import r._; _1.map(_=> TubesiteVideoRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9, _10)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(100,true) */
    val name: Rep[String] = column[String]("name", O.Length(100,varying=true))
    /** Database column desc SqlType(text) */
    val desc: Rep[String] = column[String]("desc")
    /** Database column poster SqlType(varchar), Length(200,true) */
    val poster: Rep[String] = column[String]("poster", O.Length(200,varying=true))
    /** Database column src SqlType(varchar), Length(200,true) */
    val src: Rep[String] = column[String]("src", O.Length(200,varying=true))
    /** Database column duration SqlType(int4) */
    val duration: Rep[Int] = column[Int]("duration")
    /** Database column quality SqlType(int4) */
    val quality: Rep[Int] = column[Int]("quality")
    /** Database column multiple SqlType(bool) */
    val multiple: Rep[Boolean] = column[Boolean]("multiple")
    /** Database column extra SqlType(jsonb), Length(2147483647,false), Default(None) */
    val extra: Rep[Option[String]] = column[Option[String]]("extra", O.Length(2147483647,varying=false), O.Default(None))
    /** Database column category_id SqlType(int4), Default(None) */
    val categoryId: Rep[Option[Int]] = column[Option[Int]]("category_id", O.Default(None))

    /** Foreign key referencing TubesiteCategory (database name tubesite_video_category_id_7832f031_fk_tubesite_category_id) */
    lazy val tubesiteCategoryFk = foreignKey("tubesite_video_category_id_7832f031_fk_tubesite_category_id", categoryId, TubesiteCategory)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table TubesiteVideo */
  lazy val TubesiteVideo = new TableQuery(tag => new TubesiteVideo(tag))
}
