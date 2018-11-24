package ch_typelevel_01


// Practice: https://jto.github.io/articles/typelevel_quicksort/

// Nat
sealed trait Nat

final class _0 extends Nat
final class Succ[P <: Nat] extends Nat


// sum
trait Sum[A <: Nat, B <: Nat] { type Out <: Nat }

object Sum {
  type Aux[A <: Nat, B <: Nat, O <: Nat] = Sum[A, B] { type Out = O }

  def apply[A <: Nat, B <: Nat](implicit sum: Sum[A, B]): Aux[A, B, sum.Out] = sum.asInstanceOf[Aux[A, B, sum.Out]]

  // 0 + b = b
  implicit def sum1[B <: Nat]: Aux[_0, B, B] = new Sum[_0, B] { type Out = B }
  // S(a) + b = a + S(b)
  //implicit def sum2[A <: Nat, B <: Nat]
}

object NatTestApp extends App {
  type _1 = Succ[_0]
  type _2 = Succ[_1]
  type _3 = Succ[_2]
  type _4 = Succ[_3]
  type _5 = Succ[_4]


}