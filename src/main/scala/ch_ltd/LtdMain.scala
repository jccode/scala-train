package ch_ltd

import ch_json.JsonUtil

import scala.io.Source

object LtdMain {

  val pattern = "(.+)\\((\\w+)\\)".r

  def getStocks(filename: String): List[Stock] =
    Source.fromResource(filename).getLines().map(l => {
      val pattern(name, code) = l
      new Stock(name, code)
    }).toList


  def hello(): Unit = {
//    val url = "http://emweb.securities.eastmoney.com/PC_HSF10/CompanySurvey/CompanySurveyAjax?code=sz300723"
    val url = "http://emweb.securities.eastmoney.com/PC_HSF10/CompanySurvey/CompanySurveyAjax?code=sh600000"
    val content = Source.fromURL(url).mkString
    println(content)
    val map = JsonUtil.toMap(content)
    println(map)
    val r :Map[String, Map[String, String]] = map("Result")
    println(toStock(r("jbzl"), new Stock()))
  }

  def toStock(map: Map[String, String], stock: Stock): Stock = {
//    val stock = new Stock()
    stock.fullName = map("gsmc")
    stock.engName = map("ywmc")
    stock.website = map("gswz")
    stock
  }

  def getStockInfo(stock: Stock): Stock = {
    val code = stock.code
    val pre = if (code.startsWith("6")) "sh" else "sz"
    val url = s"http://emweb.securities.eastmoney.com/PC_HSF10/CompanySurvey/CompanySurveyAjax?code=$pre$code"
    val content = Source.fromURL(url).mkString
    val map = JsonUtil.toMap(content)
    val r :Map[String, Map[String, String]] = map("Result")
    toStock(r("jbzl"), stock)
  }

  def main(args: Array[String]): Unit = {
    val stocks = getStocks("a.txt")
    val sh1 = stocks.filter(p => p.code.startsWith("6")).take(4)
    for (s <- sh1) {
      println(getStockInfo(s))
    }
//    hello()

  }

}


class Stock(var name: String = "", var code: String="", var fullName: String="", var engName: String="", var website: String="") {

  override def toString = s"Stock($name, $code, $fullName, $engName, $website)"

}
