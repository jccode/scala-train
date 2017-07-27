package ch_json

import scala.reflect.ClassTag

object JsonConvertImplicits {

  implicit def marshallOps[T](marshallMe: T) = new {
    def toJson: String = JsonUtil.toJson(marshallMe)
  }

  implicit def unmarshallOps(unmarshallMe: String) = new {
    def jsonToMap: Map[String, Any] = JsonUtil.toMap(unmarshallMe)
    def fromJson[T: ClassTag]:T = JsonUtil.fromJson[T](unmarshallMe)
  }
}
