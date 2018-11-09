package ch_shapeless

import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, Lazy}

/**
  * ch03
  *
  * @author 01372461
  */
object ch03 {

  trait CsvEncoder[A] {
    def encode(value: A): List[String]
  }

  object CsvEncoder {
    // "summoner" method
    def apply[A](implicit encoder: CsvEncoder[A]) = encoder

    // "constructor" method
    def instance[A](func: A => List[String]) = new CsvEncoder[A] {
      override def encode(value: A): List[String] = func(value)
    }

    // Global visible type class instance
    implicit val stringEncoder: CsvEncoder[String] = instance[String](x => List(x))
    implicit val intEncoder: CsvEncoder[Int] = instance[Int](x => List(x.toString))
    implicit val booleanEncoder: CsvEncoder[Boolean] = instance[Boolean](x => List(if (x) "yes" else "no"))
    implicit val doubleEncoder: CsvEncoder[Double] = instance[Double](x => List(x.toString))

    // HList encoder
    implicit val hnilEncoder: CsvEncoder[HNil] = instance[HNil](x => Nil)
    implicit def hlistEncoder[H, T <: HList](implicit hEncoder: CsvEncoder[H], tEncoder: CsvEncoder[T]): CsvEncoder[H :: T] = instance[H :: T] {
      case h :: t => hEncoder.encode(h) ++ tEncoder.encode(t)
    }

    // Coproduct encoder
    implicit val cnilEncoder: CsvEncoder[CNil] = instance[CNil](x => throw new Exception("Inconceivable!"))
    implicit def coproductEncoder[L, R <: Coproduct](implicit lEncoder: Lazy[CsvEncoder[L]], rEncoder: CsvEncoder[R]): CsvEncoder[L :+: R] = instance[L :+: R] {
      case Inl(l) => lEncoder.value.encode(l)
      case Inr(r) => rEncoder.encode(r)
    }

    // Given a type A and HList type R, an implicit Generic to map A to R, and a CsvEncoder for R, create a CsvEncoder for A
    implicit def genericEncoder[A, R](implicit gen: Generic.Aux[A, R], enc: Lazy[CsvEncoder[R]]): CsvEncoder[A] = instance[A](x => enc.value.encode(gen.to(x)))
  }

  def writeCsv[A](values: List[A])(implicit enc: CsvEncoder[A]): String = values.map(enc.encode(_).mkString(",")).mkString("\n")
}

object CsvEncoderApp extends App {
  import ch03._

  def hlistTest(): Unit = {
    //val reprEncoder: CsvEncoder[String :: Int :: Boolean :: HNil] = implicitly
    val reprEncoder: CsvEncoder[String :: Int :: Boolean :: HNil] = CsvEncoder[String :: Int :: Boolean :: HNil]
    println(reprEncoder.encode("Hello" :: 114 :: true :: HNil))
  }

  def productTest(): Unit = {
    val employeeEncoder = CsvEncoder[Employee]
    println(employeeEncoder.encode(Employee("Hello", 20, true)))

    val iceCreamEncoder = CsvEncoder[IceCream]
    println(iceCreamEncoder.encode(IceCream("Hello", 20, false)))
  }

  def coproductTest(): Unit = {
    val shapeEncoder = CsvEncoder[Shape]
    println(writeCsv(List[Shape](Rectangle(3.0, 4.0), Circle(1.0))))
  }

  def treeTest(): Unit = {
    import Tree._
    val treeEncoder = CsvEncoder[Tree[Int]]
    val tree = branch[Int](leaf(12), branch(leaf(10), leaf(5)))
    println(treeEncoder.encode(tree))
  }


  productTest()
  coproductTest()
  treeTest()
}

