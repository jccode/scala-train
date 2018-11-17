package ch_shapeless

import shapeless.{::, HList, HNil, LabelledGeneric, Witness}
import shapeless.labelled.FieldType


sealed trait JsonValue
final case class JsonString(value: String) extends JsonValue
final case class JsonBoolean(value: Boolean) extends JsonValue
final case class JsonNumber(value: Double) extends JsonValue
final case class JsonArray(items: List[JsonValue]) extends JsonValue
final case class JsonObject(fields: List[(String, JsonValue)]) extends JsonValue
final case object JsonNull extends JsonValue

trait JsonEncoder[A] {
  def encode(value: A): JsonValue
}

trait JsonObjectEncoder[A] extends JsonEncoder[A] {
  override def encode(value: A): JsonObject
}

object JsonEncoder {

  // "summoner" method
  def apply[A](implicit encoder: JsonEncoder[A]) = encoder

  // "constructor" method
  def pure[A](func: A => JsonValue) = new JsonEncoder[A] {
    override def encode(value: A): JsonValue = func(value)
  }

  def pureObject[A](func: A => JsonObject) = new JsonObjectEncoder[A] {
    override def encode(value: A): JsonObject = func(value)
  }


  // helper methods
  def getFieldValue[K, V](v: FieldType[K, V]): V = v
  def getFieldName[K <: Symbol, V](v: FieldType[K, V])(implicit w: Witness.Aux[K]): String =
    w.value.name

  // type class instance
  implicit val stringEncoder = pure[String](x => JsonString(x))
  implicit val doubleEncoder = pure[Double](x => JsonNumber(x))
  implicit val intEncoder = pure[Int](x => JsonNumber(x))
  implicit val booleanEncoder = pure[Boolean](x => JsonBoolean(x))
  implicit val nullEncoder = pure[Null](x => JsonNull)

  implicit def arrayEncoder[A](implicit encoder: JsonEncoder[A]): JsonEncoder[List[A]] =
    pure[List[A]](x => JsonArray(x.map(encoder.encode)))

  implicit def optionEncoder[A](implicit encoder: JsonEncoder[A]): JsonEncoder[Option[A]] =
    pure[Option[A]] {
      case None => JsonNull
      case Some(a) => encoder.encode(a)
    }

  // hlist implement
  implicit val hnilEncoder = pureObject[HNil](x => JsonObject(Nil))
  implicit def hlistEncoder[K <: Symbol, V, T <: HList](implicit w: Witness.Aux[K], hEncoder: JsonEncoder[V], tEncoder: JsonObjectEncoder[T]): JsonObjectEncoder[FieldType[K, V] :: T] =
    pureObject[FieldType[K, V] :: T] {
      case h :: t =>
        val fieldName: String = getFieldName(h)
        val fieldValue: V = getFieldValue(h)
        JsonObject((fieldName -> hEncoder.encode(fieldValue)) :: tEncoder.encode(t).fields) // 这里需要加约束,要不没法写.
    }

  // generic
  implicit def genericEncoder[A, R](implicit gen: LabelledGeneric.Aux[A, R], encoder: JsonEncoder[R]): JsonEncoder[A] =
    pure(x => encoder.encode(gen.to(x)))

}

object Json {

  def encode[A](a: A)(implicit encoder: JsonEncoder[A]) = encoder.encode(a)

  def stringify(value: JsonValue): String = value match {
    case JsonString(value) => s"""${escape(value)}"""
    case JsonBoolean(value) => s"$value"
    case JsonNumber(value) => s"$value"
    case JsonArray(items) => s"[${items.map(stringify).mkString(",")}]"
    case JsonObject(fields) => s"{${fields.map(stringifyField).mkString(",")}}"
    case JsonNull => "null"
  }

  private def stringifyField(value: (String, JsonValue)): String = value match {
    case (k, v) => s"""${escape(k)}: ${stringify(v)}"""
  }

  private def escape(str: String): String =
    "\"" + str.replaceAll("\"", "\\\\\"") + "\""
}


object JsonEncoderApp extends App {
  import Json._

  def employeeTest(): Unit = {
    val employee = Employee("Hello", 19, true)
    val json = encode(employee)
    println(stringify(json))
  }

  def simpleTest(): Unit = {
    List(
      encode("Hello"),
      encode(123),
      encode(123.0),
      encode(true),
      encode(null),
      encode(Option(123)),
      encode(None: Option[Int]),
      encode(List("h", "e", "l", "l", "o")),
    ).foreach(println)
  }

  simpleTest()
  employeeTest()

}
