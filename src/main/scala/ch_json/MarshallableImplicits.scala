package ch_json


import scala.reflect.ClassTag

object MarshallableImplicits {

  implicit class Unmarshallable(unMarshallMe: String) {

    // toMap will conflict with Predef: String->StringOpts -> toMap, use jsonToMap instead.
    def jsonToMap: Map[String, Any] = JsonUtil.toMap(unMarshallMe)

    def fromJson[T]()(implicit m: ClassTag[T]): T = JsonUtil.fromJson[T](unMarshallMe)
  }

  implicit class Marshallable[T](marshallMe: T) {
    def toJson: String = JsonUtil.toJson(marshallMe)
  }

}