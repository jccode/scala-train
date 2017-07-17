package ch_req

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl

import scala.xml.{Elem, XML}
import scala.xml.factory.XMLLoader

/**
  * Created by IT on 2017/7/17.
  */
object TagSoupXmlLoader {

  private val factory = new SAXFactoryImpl()

  def get(): XMLLoader[Elem] = {
    XML.withSAXParser(factory.newSAXParser())
  }
}
