package ch_shapeless

import shapeless.{Poly1, Poly2}
import shapeless.{HList, ::, HNil}

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