package ch_cats



object FormValidateExercise {
  import cats.data.Validated
  import cats.syntax.either._
  import cats.syntax.apply._ // for mapN
  import cats.instances.list._ // for semigroupal

  case class User(name: String, age: Int)

  type FormData = Map[String, String]
  type FailFast[A] = Either[List[String], A]
  type FailSlow[A] = Validated[List[String], A]

  def getValue(name: String)(data: FormData): FailFast[String] =
    data.get(name).toRight(List(s"$name field not specified"))

  val getName = getValue("name") _

  type NumFmtExn = NumberFormatException

  def parseInt(name: String)(data: String): FailFast[Int] =
    Either.catchOnly[NumFmtExn](data.toInt)
      .leftMap(_ => List(s"$name must be an integer"))

  def nonBlank(name: String)(data: String): FailFast[String] =
    Right[List[String], String](data).ensure(List(s"$name cannot be blank"))(_.nonEmpty)

  def nonNegative(name: String)(data: Int): FailFast[Int] =
    Right[List[String], Int](data).ensure(List(s"$name must be non-negative"))(_ >= 0)

  def readName(data: FormData): FailFast[String] =
    getName(data).flatMap(nonBlank("name"))

  def readAge(data: FormData): FailFast[Int] =
    getValue("age")(data)
      .flatMap(nonBlank("age"))
      .flatMap(parseInt("age"))
      .flatMap(nonNegative("age"))

  def readUser(data: FormData): FailSlow[User] =
    (readName(data).toValidated, readAge(data).toValidated).mapN(User.apply _)
}


object FormValidateApp extends App {
  import FormValidateExercise._

  def getValue_getName_test(): Unit = {
    println(getName(Map("name" -> "Dade Murphy")))
    println(getName(Map()))
  }

  def parseInt_test(): Unit = {
    println(parseInt("age")("11"))
    println(parseInt("age")("foo"))
  }

  def nonBlank_nonNegative_test(): Unit = {
    println(nonBlank("name")("Tomcat"))
    println(nonBlank("name")(""))
    println(nonNegative("age")(11))
    println(nonNegative("age")(-1))
  }

  def readName_readValue_test(): Unit = {
    val t = List(
      readName(Map("name" -> "Dade Murphy")),
      readName(Map("name" -> "")),
      readName(Map()),
      readAge(Map("age" -> "11")),
      readAge(Map("age" -> "-1")),
      readAge(Map()),
    )
    println(t)
  }

  def readUser_test(): Unit = {
    val t = List(
      readUser(Map("name" -> "Dave", "age" -> "37")),
      readUser(Map("age" -> "-1")),
    )
    println(t)
  }

  readUser_test()
}


/**
  * semigroupal
  *
  * @author 01372461
  */
object semigroupal extends App {

  import cats.Semigroupal
  import cats.instances.option._

  val semi: Semigroupal[Option] = Semigroupal[Option]
  semi.product(Some(123), Some("abc"))
  println(Semigroupal.tuple2(Option(123), Option("abc")))
}
