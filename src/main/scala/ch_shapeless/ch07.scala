package ch_shapeless

import shapeless.ops.hlist.Mapper
import shapeless.{::, HList, HNil, LabelledGeneric, Poly, Poly1, Poly2}

object ch07 {

  object myPoly extends Poly1 {
    implicit val intCase: Case.Aux[Int, Double] = at(x => x / 2.0)
    implicit val stringCase: Case.Aux[String, Int] = at(x => x.length)
  }

  object multipoly extends Poly2 {
    implicit val intIntCase: Case.Aux[Int, Int, Int] = at((x, y) => x * y)
    implicit val intStringCase: Case.Aux[Int, String, String] = at((x, y) => y * x)
  }

  object total extends Poly1 {
    implicit def base[A](implicit num: Numeric[A]): Case.Aux[A, Double] = at(x => num.toDouble(x))
    implicit def option[A](implicit num: Numeric[A]): Case.Aux[Option[A], Double] = at(x => x.map(num.toDouble).getOrElse(0.0))
    implicit def list[A](implicit num: Numeric[A]): Case.Aux[List[A], Double] = at(x => x.map(num.toDouble).sum)
  }
}

object Ch07App extends App {
  import ch07._

  def testMyPoly(): Unit = {
    println(myPoly(12))
    println(myPoly("Hello"))
  }

  def testMultipoly(): Unit = {
    println(multipoly(4,5))
    println(multipoly(4,"5"))
  }

  def testTotalPoly(): Unit = {
    println(total(10))
    println(total(Option(20.0)))
    println(total(List(1L, 2L, 3L)))
  }

  def mapTest(): Unit = {
    //import total._
    val hlist = 10 :: Option(20.0) :: List(1L, 2L, 3L) :: HNil
    val l2 = hlist.map(total)
    println(l2)
  }

//  testMyPoly()
//  testMultipoly()
  testTotalPoly()
  mapTest()
}


trait ProductMapper[A, B, P] {
  def apply(a: A): B
}

object ProductMapper {

  implicit def genericProductMapper[A, B, P <: Poly, ARepr <: HList, BRepr <: HList]
  (implicit aGen: LabelledGeneric.Aux[A, ARepr],
   bGen: LabelledGeneric.Aux[B, BRepr],
   mapper: Mapper.Aux[P, ARepr, BRepr]): ProductMapper[A, B, P] = (a: A) => {
    val aList = aGen.to(a)
    val bList = mapper.apply(aList)
    bGen.from(bList)
  }


  // syntax
  /*
  implicit class ProductMapperOps[A, P](a: A) {
    def mapTo[B](implicit productMapper: ProductMapper[A, B, P]): B = productMapper.apply(a)
  }
  */

  implicit class ProductMapperOps[A](a: A) {
    def mapTo[B]: Builder[B] = new Builder[B]

    /*
    class Builder[B, P](p: P) {
      def apply(implicit productMapper: ProductMapper[A, B, P]) = productMapper.apply(a)
    }
    */

    class Builder[B] {
      def apply[P <: Poly](p: P)(implicit productMapper: ProductMapper[A, B, P]): B = productMapper.apply(a)
    }
  }

}


object ProductMapperApp extends App {
  import ProductMapper._
  case class IceCream1(name: String, numCherries: Int, inCone: Boolean)
  case class IceCream2(name: String, hasCherries: Boolean, numCones: Int)

  println("Hello")

  object conversions extends Poly1 {
    implicit val stringCase: Case.Aux[String, String] = at(x => x.toUpperCase)
    implicit val intCase: Case.Aux[Int, Boolean] = at(x => if (x == 0) false else true)
    implicit val booleanCase: Case.Aux[Boolean, Int] = at(x => if (x) 1 else 0)
  }

//  val iceCream2 = IceCream1("Sundae", 1, false).mapTo[IceCream2](conversions)
//  println(iceCream2)
}


