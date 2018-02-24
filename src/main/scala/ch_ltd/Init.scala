package ch_ltd

import scala.io.Source

object Init {

  val pattern = "(.+)\\((\\w+)\\)".r

  def loadRecordsFromFile(file: String): List[Record] =
    Source.fromResource(file).getLines().map(l => {
      val pattern(name, code) = l
      Record(name, code)
    }).toList

  def saveToSQLite(records: List[Record]): Unit = {

  }

  def main(args: Array[String]): Unit = {
    println("Load init company data")
//    val records = loadRecordsFromFile("a.txt")
//    println(records)

  }
}

case class Record(name: String, code: String);
