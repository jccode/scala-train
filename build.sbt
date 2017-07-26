name := "scala-train"

version := "1.0"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.4.19" // "2.5.3"


libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.1",
  "com.typesafe" % "config" % "1.3.1",

  "org.apache.kafka" % "kafka-clients" % "0.11.0.0",

//  "org.scala-lang" % "scala-actors" % "2.11.7",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.0.9",

  "com.typesafe.slick" %% "slick" % "3.2.0",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.zaxxer" % "HikariCP-java6" % "2.3.3",

  // xml, html
  "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6",
  "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2.1",

  // json
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.4",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.4"

)
