package ch_req

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

object JsoupParseDemo extends App {

  val url = "http://emweb.securities.eastmoney.com/f10_v2/CompanySurvey.aspx?type=web&code=sh600001"
  private val doc: Document = Jsoup.connect(url).get()
  private val els: Elements = doc.select("#Table0")
  println(doc)
  println(els.size())

}
