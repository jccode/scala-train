package ch_ltd


import scala.io.Source
import Tables.Company
import ch_json.JsonUtil


case class Record(name: String, code: String)

/**
  * 加载txt文件中的数据.
  */
object RecordLoader {
  private final val pattern = "(.+)\\((\\w+)\\)".r

  def load(file: String): Iterator[Record] =
    Source.fromResource(file).getLines().map(l => {
      val pattern(name, code) = l
      Record(name, code)
    })
}

/**
  * 获取股票信息
  */
object StockFetcher {

  /**
    * 远程url
    * @param code
    * @return
    */
  private def url(code: String): String = {
    val pre = if (code.startsWith("6")) "sh" else "sz"
    s"http://emweb.securities.eastmoney.com/PC_HSF10/CompanySurvey/CompanySurveyAjax?code=$pre$code"
  }

  def fetch(company: Company): Company = {
    if (company.fullName.isDefined || company.lastUpdate.isDefined) {
      return company
    }
    if (company.code.isEmpty) return company
    val content = Source.fromURL(url(company.code.get)).mkString
    val map = JsonUtil.toMap(content)
    val retMap: Map[String, Map[String, String]] = map("Result")
    val m = retMap("jbzl")
    Company(company.id, company.name, company.code, Some(m("gsmc")), Some(m("ywmc")), Some(m("gswz")), Some(System.currentTimeMillis().toString))
  }
}