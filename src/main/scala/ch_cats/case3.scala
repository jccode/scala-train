package ch_cats

import cats.data.Validated
import cats.kernel.Semigroup
import cats.syntax.semigroup._
import cats.syntax.either._
import cats.syntax.validated._
import cats.syntax.apply._
import cats.instances.list._
import ch_cats.Part4.Predicate

object Part1 {
  final case class CheckF[E, A](func: A => Either[E, A]) {

    def apply(value: A): Either[E, A] = func(value)

    def and(that: CheckF[E, A])(implicit semi: Semigroup[E]): CheckF[E, A] = {
      CheckF((a: A) => (this(a), that(a)) match {
        case (Left(e1), Left(e2)) => (e1 |+| e2).asLeft
        case (Right(_), Right(_)) => a.asRight
        case (Left(e1), Right(_)) => e1.asLeft
        case (Right(_), Left(e2)) => e2.asLeft
      })
    }
  }

}


object CheckFTestApp extends App {
  import Part1._

  def checkF_test(): Unit = {
    val a = CheckF[List[String], Int](v => if (v < 2) v.asRight else List("Must be < 2").asLeft)
    val b = CheckF[List[String], Int](v => if (v > -2) v.asRight else List("Must be > -2").asLeft)

    val check: CheckF[List[String], Int] = a and b
    println(check(5))
    println(check(1))
    println(check(-5))
  }

  def checkF_without_semigroup_instance_test(): Unit = {
    val a = CheckF[Nothing, Int](v => v.asRight)
    val b = CheckF[Nothing, Int](v => v.asRight)
    // val c = a and b  // compiled error, no semigroup instance of Nothing found
  }

  checkF_test()
}

object Part2 {

  sealed trait Check[E, A] {

    def and(that: Check[E, A]): Check[E, A] = And(this, that)

    def apply(a: A)(implicit s: Semigroup[E]): Either[E, A] = this match {
      case Pure(func) => func(a)
      case And(left, right) => (left(a), right(a)) match {
        case (Left(e1), Left(e2)) => (e1 |+| e2).asLeft
        case (Right(_), Right(_)) => a.asRight
        case (Left(e1), Right(_)) => e1.asLeft
        case (Right(_), Left(e2)) => e2.asLeft
      }
    }

  }

  final case class And[E, A](left: Check[E, A], right: Check[E, A]) extends Check[E, A]
  final case class Pure[E, A](func: A => Either[E, A]) extends Check[E, A]

}

object Part2TestApp extends App {
  import Part2._

  val a = Pure[List[String], Int](v => if (v < 2) v.asRight else List("Must be < 2").asLeft)
  val b = Pure[List[String], Int](v => if (v > -2) v.asRight else List("Must be > -2").asLeft)
  val check = a and b

  println(check(5))
  println(check(1))
  println(check(-5))
}

object Part3 {

  sealed trait Check[E, A] {

    def and(that: Check[E, A]): Check[E, A] = And(this, that)

    def or(that: Check[E, A]): Check[E, A] = Or(this, that)

    def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] = this match {
      case Pure(func) => func(a)
//      case And(left, right) => left(a).product(right(a)).map(_ => a)
      case And(left, right) => (left(a), right(a)).mapN((_, _) => a)
      case Or(left, right) => left(a) match {
        case Validated.Valid(_) => a.valid
        case Validated.Invalid(e1) => right(a) match {
          case Validated.Valid(_) => a.valid
          case Validated.Invalid(e2) => (e1 |+| e2).invalid
        }
      }
    }

  }

  final case class And[E, A](left: Check[E, A], right: Check[E, A]) extends Check[E, A]
  final case class Or[E, A](left: Check[E, A], right: Check[E, A]) extends Check[E, A]
  final case class Pure[E, A](func: A => Validated[E, A]) extends Check[E, A]

}

object Part3TestApp extends App {
  import Part3._

  val a = Pure[List[String], Int](v => if (v > 2) v.valid else List("Must be > 2").invalid)
  val b = Pure[List[String], Int](v => if (v < -2) v.valid else List("Must be < -2").invalid)
  val check = a and b
  val check2 = a or b

  println(check(5))
  println(check(1))
  println(check(-5))

  println(check2(5))
  println(check2(1))
  println(check2(-5))
}

object Part4 {

  sealed trait Predicate[E, A] {
    import Predicate._

    def and(that: Predicate[E, A]): Predicate[E, A] = And(this, that)
    def or(that: Predicate[E, A]): Predicate[E, A] = Or(this, that)
    def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] = this match {
      case Pure(func) => func(a)
      case And(left, right) => (left(a), right(a)).mapN((_, _) => a)
      case Or(left, right) => left(a) match {
        case Validated.Valid(_) => a.valid
        case Validated.Invalid(e1) => right(a) match {
          case Validated.Valid(_) => a.valid
          case Validated.Invalid(e2) => (e1 |+| e2).invalid
        }
      }
    }
  }

  object Predicate {
    final case class And[E, A](left: Predicate[E, A], right: Predicate[E, A]) extends Predicate[E, A]
    final case class Or[E, A](left: Predicate[E, A], right: Predicate[E, A]) extends Predicate[E, A]
    final case class Pure[E, A](func: A => Validated[E, A]) extends Predicate[E, A]

    def apply[E, A](func: A => Validated[E, A]) = Pure(func)

    def lift[E, A](err: E, func: A => Boolean): Predicate[E, A] =
      Pure((a: A) => if (func(a)) a.valid else err.invalid)
  }


  sealed trait Check[E, A, B] {
    import Check._

    def apply(a: A)(implicit s: Semigroup[E]): Validated[E, B]

    def map[C](f: B => C): Check[E, A, C] = Map(this, f)
  }

  object Check {

    final case class Pure[E, A, B](func: A => Validated[E, B]) extends Check[E, A, B] {
      override def apply(a: A)(implicit s: Semigroup[E]): Validated[E, B] = func(a)
    }

    final case class PurePredicate[E, A](predicate: Predicate[E, A]) extends Check[E, A, A] {
      override def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] = predicate(a)
    }

    final case class Map[E, A, B, C](check: Check[E, A, B], func: B => C) extends Check[E, A, C] {
      override def apply(a: A)(implicit s: Semigroup[E]): Validated[E, C] = check(a).map(func)
    }
  }

}

object Part4TestApp extends App {
  import Part4._

  def check_pure(): Unit = {
    val a = Check.Pure[List[String], Int, String](v => if (v > 2) v.toString.valid else List("Must be > 2").invalid)
    println(a(5))
    println(a(-5))
  }

  def pure_predict(): Unit = {
    val pa = Predicate[List[String], Int](v => if (v > 2) v.valid else List("Must be > 2").invalid)
    val pb = Predicate.lift[List[String], Int](List("Must be < -2"), v => v < -2)

    val ca = Check.PurePredicate(pa and pb)
    val co = Check.PurePredicate(pa or pb)

    println(ca(5))
    println(ca(1))
    println(ca(-5))
    println(co(5))
    println(co(-5))
  }

  def map_test(): Unit = {
    val a = Check.Pure[List[String], Int, String](v => if (v > 2) v.toString.valid else List("Must be > 2").invalid)
    val b = a.map(_+"!")
    println(a(5))
    println(b(5))
  }

  map_test()
}