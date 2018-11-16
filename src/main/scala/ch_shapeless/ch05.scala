package ch_shapeless

import shapeless.labelled.FieldType
import shapeless.{::, Generic, HList, HNil, LabelledGeneric, Poly, Poly1, Witness}


object LabelledGenericTest extends App {
  val gen = Generic[Employee]
  val gen2 = LabelledGeneric[Employee]

  val employee = Employee("tom", 19, true)
  val v1 = gen.to(employee)
  val v2 = gen2.to(employee)

  def getFieldValue[K, V](v: FieldType[K, V]): V = v
  def getFieldName[K <: Symbol, V](v: FieldType[K, V])(implicit w: Witness.Aux[K]): String =
    w.value.name

  //v2.head

  println(getFieldName(v2.last))
  println(getFieldValue(v2.last))
}


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

trait JsonObjectEncoder[A <: HList] extends JsonEncoder[A] {
  def encode(value: A): JsonObject
}

object JsonObjectEncoder {
  def instance[A <: HList](func: A => JsonObject): JsonObjectEncoder[A] = (value: A) => func(value)
}


object JsonEncoder {
  // "summoner" method
  def apply[A](implicit encoder: JsonEncoder[A]) = encoder

  // "constructor" method
  def instance[A](func: A => JsonValue) = new JsonEncoder[A] {
    override def encode(value: A): JsonValue = func(value)
  }

  // helper methods
  def getFieldValue[K, V](v: FieldType[K, V]): V = v
  def getFieldName[K <: Symbol, V](v: FieldType[K, V])(implicit w: Witness.Aux[K]): String =
    w.value.name

  // type class instance
  implicit val stringEncoder = instance[String](x => JsonString(x))
  implicit val numberEncoder = instance[Double](x => JsonNumber(x))
  implicit val booleanEncoder = instance[Boolean](x => JsonBoolean(x))
  implicit val nullEncoder = instance[Null](x => JsonNull)

  implicit def arrayEncoder[A](implicit encoder: JsonEncoder[A]): JsonEncoder[List[A]] =
    instance[List[A]](x => JsonArray(x.map(encoder.encode)))

  implicit def optionEncoder[A](implicit encoder: JsonEncoder[A]): JsonEncoder[Option[A]] =
    instance[Option[A]] {
      case None => JsonNull
      case Some(a) => encoder.encode(a)
    }

  // implement 01, map over HList.
  /*
  implicit def objectEncoder[A, K <: Symbol, V, R <: HList, P <: Poly]
  (implicit gen: LabelledGeneric.Aux[A, R], mapper: Mapper.Aux[P, FieldType[K, V], JsonValue], w: Witness.Aux[K]) : JsonEncoder[A] =
    instance[A]((a: A) => {
      val record: HList = gen.to(a)
      //record.map()
      ???
    })
    */


  // hlist implement
  implicit val hnilEncoder = instance[HNil](x => JsonNull)
  implicit def hlistEncoder[K <: Symbol, V, T <: HList](implicit w: Witness.Aux[K], hEncoder: JsonEncoder[V], tEncoder: JsonObjectEncoder[T]) =
    instance[FieldType[K, V] :: T] {
      case h :: t => {
        val fieldName: String = getFieldName(h)
        val fieldValue: V = getFieldValue(h)
        //val list = tEncoder.encode(t)
        JsonObject((fieldName -> hEncoder.encode(fieldValue)) :: tEncoder.encode(t).fields) // 这里需要加约束,要不没法写.
      }
  }


  implicit def genericEncoder[A, R <: HList](implicit gen: LabelledGeneric.Aux[A, R], encoder: JsonObjectEncoder[R]): JsonEncoder[A] =
    JsonObjectEncoder.instance[A](x => encoder.encode(gen.to(x)))

  // TODO implement 02, 例子中是怎么实现的？
}


object JsonEncoderApp extends App {

//  def employeeTest(): Unit = {
//    val employee = Employee("Hello", 19, true)
//    val encoder = JsonEncoder[Employee]
//    val json = encoder.encode(employee)
//    println(json)
//  }

  def simpleTest(): Unit = {
    val encoder = JsonEncoder[Option[String]]
    println(encoder.encode(Some("hello")))
  }

  simpleTest()
//  employeeTest()


  import scala.reflect.runtime.universe._

  println(reify(JsonEncoder[Employee]))
}
