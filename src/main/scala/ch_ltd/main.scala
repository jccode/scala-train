package ch_ltd

import Tables._
import Tables.profile.api._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * main
  *
  * @author 01372461
  */
object main extends App {

  def t1 = {
    CompanyRepo.exec(companies.filter(_.code.startsWith("6")).take(10).result).map { list =>
      list.foreach(println)
      list.foreach(c => {
        StockFetcher.fetch(c).foreach(println)
      })
    }
    // block until future return.
    System.in.read()
  }

  def t2 = {
//    CompanyRepo.find().map(l => l.foreach(println))
    CompanyRepo.find(_.filter(_.code.startsWith("6")).take(10)).map{l => l.foreach(println)}
    System.in.read()
  }

  t2
}
