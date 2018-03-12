package ch_ltd

import Tables.Company
import scala.concurrent.ExecutionContext.Implicits.global


object Init extends App {

  val file = "a.txt"
  val companies = RecordLoader.load(file).map(x => Company(0, x.name, Some(x.code), None, None, None, None)).toSeq
//  companies.foreach(println)
  CompanyRepo.save(companies).map(r => println(s"Success write ${r.get} records into database"))
  println("Waiting for writing database ...")
  System.in.read()

}


