package ch_req

/**
  * Created by IT on 2017/7/17.
  */
object HtmlParseDemo extends App {

  stock()

  def stock(): Unit = {
    val root = TagSoupXmlLoader.get().load("http://emweb.securities.eastmoney.com/f10_v2/CompanySurvey.aspx?type=web&code=sh600001")
    val nodeSeq = root \ "head" \ "title"
    println(nodeSeq(0).text)
  }

  def baidu(): Unit = {
    val root = TagSoupXmlLoader.get().load("http://www.baidu.com")
    println(root)
    println("------")
    println(root \\ "a")
  }

}
