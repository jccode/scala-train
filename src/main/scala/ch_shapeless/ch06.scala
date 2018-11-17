package ch_shapeless

import shapeless.{HList, HNil, ::}


object ch06 {

  trait Penultimate[A] {
    type Out
    def apply(a: A): Out
  }

  object Penultimate {
    import shapeless.ops.hlist.{Init, Last}

    type Aux[A, O] = Penultimate[A] { type Out = O }

    def apply[A](implicit inst: Penultimate[A]): Aux[A, inst.Out] = inst

    implicit def penultimateInstance[A <: HList, InitOut <: HList, LastOut]
    (implicit init: Init.Aux[A, InitOut], last: Last.Aux[InitOut, LastOut]): Penultimate.Aux[A, LastOut] =
      new Penultimate[A] {
        override type Out = LastOut
        override def apply(a: A): Out = last(init(a))
      }
  }

  implicit class PenultimateOps[A](a: A) {
    def penultimate(implicit inst: Penultimate[A]) = inst.apply(a)
  }

}

object PenultimateApp extends App {
  import ch06._

  def penultimate[A](a: A)(implicit inst: Penultimate[A]) = inst.apply(a)

  val list = "hello" :: 123 :: true :: HNil
  val p = Penultimate[String :: Int :: Boolean :: HNil]
  println(p.apply(list))
  println(penultimate(list))
  println(list.penultimate)
}

