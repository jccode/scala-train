name := "scala-train"

version := "1.0"

scalaVersion := "2.12.3"

lazy val akkaVersion = "2.5.4" // "2.5.3", "2.4.19"
lazy val slickVersion = "3.2.1"


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",

  // logging
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.1",

  // config
  "com.typesafe" % "config" % "1.3.1",

  // kafka
  "org.apache.kafka" % "kafka-clients" % "0.11.0.0",

  // akka
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-http" % "10.0.9",

  // akka persistence
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-query" % akkaVersion,
  "org.iq80.leveldb"            % "leveldb"          % "0.8",
  "org.fusesource.leveldbjni"   % "leveldbjni-all"   % "1.8",

  // akka remote
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,

  // akka stream
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,

  // akka http
  "com.typesafe.akka" %% "akka-http" % "10.0.9",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.9" % Test,
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.5",


// slick
  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
  "com.typesafe.slick" %% "slick-codegen" % slickVersion,

  // db driver
  "mysql" % "mysql-connector-java" % "5.1.36",
  "org.xerial" % "sqlite-jdbc" % "3.8.7",
  "org.postgresql" % "postgresql" % "42.1.4",

  // xml, html
  "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6",
  "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2.1",

  // json
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.4",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.4",

  // jsoup
  "org.jsoup" % "jsoup" % "1.9.1",

  "org.twitter4j" % "twitter4j-stream" % "4.0.7",

  // cats
  "org.typelevel" %% "cats-core" % "1.4.0",

  // shapeless
  "com.chuusai" %% "shapeless" % "2.3.3"
)

// cats
scalacOptions += "-Ypartial-unification"

// sbt custom task demo

// hello task
lazy val hello = taskKey[Unit]("An example task")
hello := {
  println("Hello task")
}


// custom functions
val slickOutputFile = (basedir:String, pkg:String) => s"${basedir}/${pkg.replace(".","/")}/Tables.scala"

def slickMySQL (url: String, user: String, password: String, pkg: String, srcdir: String) = {
  val profile = "slick.jdbc.MySQLProfile"
  val jdbcDriver = "com.mysql.jdbc.Driver"
  (Array(profile, jdbcDriver, url, srcdir, pkg, user, password), slickOutputFile(srcdir, pkg))
}

def slickSQLite (dbfile: String, pkg: String, srcdir: String) = {
  val profile = "slick.jdbc.SQLiteProfile"
  val jdbcDriver = "org.sqlite.JDBC"
  val url = s"jdbc:sqlite:${dbfile}"
  (Array(profile, jdbcDriver, url, srcdir, pkg), slickOutputFile(srcdir, pkg))
}

def slickPostgres (url: String, user: String, password: String, pkg: String, srcdir: String) = {
  val profile = "slick.jdbc.PostgresProfile"
  val jdbcDriver = "org.postgresql.Driver"
  (Array(profile, jdbcDriver, url, srcdir, pkg, user, password), slickOutputFile(srcdir, pkg))
}


// Slick task, code generator
// ref: https://github.com/slick/slick-codegen-example
lazy val slick = taskKey[Seq[File]]("gen-tables")
slick := {
  val dir = (scalaSource in Compile).value
  val cp = (dependencyClasspath in Compile).value
  val r = (runner in Compile).value
  val s = streams.value

  // val outputDir = (dir / "com/suyun/train/slick").getPath
  val outputDir = dir.getPath

//  val t = slickMySQL("jdbc:mysql://10.0.0.200:3306/vehicledb?useUnicode=true&amp;characterEncoding=utf-8", "suyun", "suyun123", "com.suyun.train.slick.gen", outputDir)
//  val t = slickPostgres("jdbc:postgresql://127.0.0.1:6432/postgres", "postgres", "postgres", "ch_slick.gen", outputDir)
  val t = slickSQLite("/Users/chenjunchang/code/scala-train/src/main/resources/ltd.sqlite3", "ch_ltd.gen", outputDir)

  // toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, t._1, s.log))
  // Migrate to sbt 1.0.1 (ref: http://www.scala-sbt.org/1.0/docs/sbt-1.0-Release-Notes.html)
  r.run("slick.codegen.SourceCodeGenerator", cp.files, t._1, s.log).failed foreach (sys error _.getMessage)

  Seq(file(t._2))
}
