package ch_cats

import cats.data.{NonEmptyList, Validated, Kleisli}
import cats.kernel.Semigroup
import cats.syntax.semigroup._
import cats.syntax.either._
import cats.syntax.validated._
import cats.instances.list._
import cats.instances.either._
import cats.syntax.apply._
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

    def flatMap[C](f: B => Check[E, A, C]) = FlatMap(this, f)

    def andThen[C](that: Check[E, B, C]) = AndThen(this, that)
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

    final case class FlatMap[E, A, B, C](check: Check[E, A, B], func: B => Check[E, A, C]) extends Check[E, A, C] {
      override def apply(a: A)(implicit s: Semigroup[E]): Validated[E, C] = check(a).withEither( _.flatMap{x: B =>
        func(x)(a).toEither
      })
    }

    final case class AndThen[E, A, B, C](check1: Check[E, A, B], check2: Check[E, B, C]) extends Check[E, A, C] {
      override def apply(a: A)(implicit s: Semigroup[E]): Validated[E, C] = check1(a).withEither(_.flatMap(b => check2(b).toEither))
    }

    def apply[E, A](predicate: Predicate[E, A]) = PurePredicate(predicate)

    def apply[E, A, B](func: A => Validated[E, B]) = Pure(func)
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

  def flatMap_test(): Unit = {
    val pa = Predicate[List[String], Int](v => if (v > 2) v.valid else List("Must be > 2").invalid)
    val pb = Predicate.lift[List[String], Int](List("Must be < -2"), v => v < -2)
    val c1 = Check.PurePredicate(pa)
    val c2 = Check.PurePredicate(pb)

    val t1 = c1.flatMap(_ => c2)
    println(t1(5))
    println(t1(1))
    println(t1(-5))
  }

  def andThen_test(): Unit = {
    val pa = Predicate[List[String], Int](v => if (v > 2) v.valid else List("Must be > 2").invalid)
    val pb = Predicate.lift[List[String], Int](List("Must be < -2"), v => v < -2)
    val c1 = Check.PurePredicate(pa)
    val c2 = Check.PurePredicate(pb)

    val t1 = c1 andThen c2
    println(t1(5))
    println(t1(1))
    println(t1(-5))
  }

}


object CheckTestApp extends App {
  import cats.data.{NonEmptyList, Validated}
  import Part4._

  type Errors = NonEmptyList[String]

  def error(s: String): NonEmptyList[String] = NonEmptyList(s, Nil)

  def longerThan(n: Int): Predicate[Errors, String] = Predicate.lift(
    error(s"Must be longer than $n characters"),
    s => s.size > n)

  def alphanumeric: Predicate[Errors, String] = Predicate.lift(
    error(s"Must be all alphanumeric characters"),
    s => s.forall(_.isLetterOrDigit)
  )

  def contains(char: Char): Predicate[Errors, String] = Predicate.lift(
    error(s"Must contain the character $char"),
    s => s.contains(char)
  )

  def containsOnce(char: Char): Predicate[Errors, String] = Predicate.lift(
    error(s"Must contain the character $char only once"),
    str => str.count(c => c == char) == 1
  )


  /**
    * A username must contain at least four characters and consist en rely of alphanumeric characters
    *
    * @return
    */
  def checkUsername: Check[Errors, String, String] =
    Check(longerThan(3) and alphanumeric)

  /**
    * An email address must contain an @ sign.
    * Split the string at the @. The string to the left  must not be empty.
    * The string to the right must be at least three characters long and contain a dot.
    *
    * @param email
    * @return
    */
  def checkEmail: Check[Errors, String, String] = {

    val splitEmail: Check[Errors, String, (String, String)] = Check(_.split('@') match {
      case Array(name, domain) => (name, domain).validNel
      case other => "Must contain a single @ character".invalidNel
    })
    val checkLeft = Check(longerThan(0))
    val checkRight = Check(longerThan(2) and contains('.'))

    val joinEmail: Check[Errors, (String, String), String] = Check[Errors, (String, String), String] {
      case (l, r) => (checkLeft(l), checkRight(r)).mapN(_ + "@" + _)
    }

    splitEmail andThen joinEmail
  }


  final case class User(username: String, email: String)

  def createUser(username: String, email: String): Validated[Errors, User] =
    (checkUsername(username), checkEmail(email)).mapN(User)


  // create user test
  List(
    createUser("Noel", "noel@underscore.io"),
    createUser("", "dave@underscore@io"),

  ).foreach(println)

}

object Part5 {

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

    def run(implicit s: Semigroup[E]): A => Either[E, A] =
      (a: A) => this(a).toEither
  }

  object Predicate {
    final case class And[E, A](left: Predicate[E, A], right: Predicate[E, A]) extends Predicate[E, A]
    final case class Or[E, A](left: Predicate[E, A], right: Predicate[E, A]) extends Predicate[E, A]
    final case class Pure[E, A](func: A => Validated[E, A]) extends Predicate[E, A]

    def apply[E, A](func: A => Validated[E, A]) = Pure(func)

    def lift[E, A](err: E, func: A => Boolean): Predicate[E, A] =
      Pure((a: A) => if (func(a)) a.valid else err.invalid)
  }

  type Errors = NonEmptyList[String]

  type Result[A] = Either[Errors, A]
  type Check[A, B] = Kleisli[Result, A, B]

  def check[A, B](func: A => Result[B]): Check[A, B] = Kleisli(func)
  def checkPred[A](pred: Predicate[Errors, A]): Check[A, A] = Kleisli[Result, A, A](pred.run)

}

object Part5App extends App {
  import Part5._
  import Part5.Predicate

  def error(s: String): NonEmptyList[String] = NonEmptyList(s, Nil)

  def longerThan(n: Int): Predicate[Errors, String] = Predicate.lift(
    error(s"Must be longer than $n characters"),
    s => s.size > n)

  def alphanumeric: Predicate[Errors, String] = Predicate.lift(
    error(s"Must be all alphanumeric characters"),
    s => s.forall(_.isLetterOrDigit)
  )

  def contains(char: Char): Predicate[Errors, String] = Predicate.lift(
    error(s"Must contain the character $char"),
    s => s.contains(char)
  )

  def containsOnce(char: Char): Predicate[Errors, String] = Predicate.lift(
    error(s"Must contain the character $char only once"),
    str => str.count(c => c == char) == 1
  )

  def checkUsername = checkPred(longerThan(3) and alphanumeric)

  def checkEmail: Check[String, String] = {
    val splitEmail = check[String, (String, String)](_.split("@") match {
      case Array(name, domain) => (name, domain).asRight
      case other => error("Must contain a single @ character").asLeft
    })
    val checkLeft = checkPred(longerThan(0))
    val checkRight = checkPred(longerThan(3) and contains('.'))
    val joinEmail = check[(String, String), String] {
      case (l, r) => (checkLeft(l), checkRight(r)).mapN(_ + "@" + _)
    }

    splitEmail andThen joinEmail
  }


  final case class User(username: String, email: String)

  def createUser(username: String, email: String): Result[User] =
    (checkUsername.run(username), checkEmail.run(email)).mapN(User)


  // create user test
  List(
    createUser("Noel", "noel@underscore.io"),
    createUser("", "dave@underscore@io"),

  ).foreach(println)
}