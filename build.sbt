name := "scala-train"

version := "1.0"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.4.19" // "2.5.3"
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

  // slick
  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
  "com.typesafe.slick" %% "slick-codegen" % slickVersion,
  "mysql" % "mysql-connector-java" % "5.1.36",

  // xml, html
  "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6",
  "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2.1",

  // json
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.4",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.4"
)


// sbt custom task demo

// hello task
lazy val hello = taskKey[Unit]("An example task")
hello := {
  println("Hello task")
}


// Slick task, code generator
// ref: https://github.com/slick/slick-codegen-example
lazy val slick = taskKey[Seq[File]]("gen-tables")
slick := {
  val dir = (scalaSource in Compile).value
  val cp = (dependencyClasspath in Compile).value
  val r = (runner in Compile).value
  val s = streams.value

  // place generated files in sbt's managed sources folder
  // val outputDir = (dir / "com/suyun/train/slick").getPath
  val outputDir = dir.getPath
  val profile = "slick.jdbc.MySQLProfile"
  val jdbcDriver = "com.mysql.jdbc.Driver"
  val url = "jdbc:mysql://10.0.0.200:3306/vehicledb?useUnicode=true&amp;characterEncoding=utf-8"
  val user = "suyun"
  val password = "suyun123"

  val pkg = "ch_slick.gen"
  toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(profile, jdbcDriver, url, outputDir, pkg, user, password), s.log))
  val fname = outputDir + "/ch_slick/gen/Tables.scala"
  //  println(s"${dir}, ${cp}, ${r}, ${s}, ${outputDir}")
  Seq(file(fname))
}
