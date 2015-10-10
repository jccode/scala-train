name := "scala-train"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-actors" % "2.11.7",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.akka" %% "akka-actor" % "2.3.13",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.13",

  "com.typesafe.slick" %% "slick" % "3.0.3",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.zaxxer" % "HikariCP-java6" % "2.3.3"
)