package ch_json

//import MarshallableImplicits._
import JsonConvertImplicits._

object JsonTest2 extends App {

  case class Person(name: String, age: Int)

  val jeroen = Person("Jeroen", 26)

  val jeroenJson = jeroen.toJson

  // jeroenJson:  String = {"name":"Jeroen","age":26}


  val jeroenMap = jeroenJson.jsonToMap
  // jeroenMap: Map[String,Any] = Map(name -> Jeroen, age -> 26)

  val p = jeroenJson.fromJson[Person]

}
