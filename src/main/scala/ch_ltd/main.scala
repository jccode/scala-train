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

//  CompanyRepo.exec(companies.take(10).result).foreach(println)
  CompanyRepo.exec(companies.filter(_.code.startsWith("6")).take(10).result).map { list =>
    list.foreach(println)
    println("--- fetch ---")
    list.foreach(c => {
      println(StockFetcher.fetch(c))
    })
  }

  // block until future return.
  System.in.read()
}
