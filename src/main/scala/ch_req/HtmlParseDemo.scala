package ch_req

/**
  * Created by IT on 2017/7/17.
  */
object HtmlParseDemo extends App {

  val root = TagSoupXmlLoader.get().load("http://www.baidu.com")
  println(root)
  println("------")
  println(root \\ "a")

}
