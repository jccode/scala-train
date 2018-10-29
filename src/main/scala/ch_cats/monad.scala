package ch_cats

import scala.language.higherKinds


trait Monad[F[_]] {
  def pure[A](a: A): F[A]

  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]

  def map[A, B](value: F[A])(func: A => B): F[B] =
    flatMap(value)(x => pure(func(x)))
}

object Monad {
  def apply[F[_]](implicit m: Monad[F]) = m
}


object EvalExercise {
  def foldRight[A, B](as: List[A], acc: B)(fn: (A,B) => B): B =
    as match {
      case head :: tail =>
        foldRight(tail, fn(head, acc))(fn)
      case Nil => acc
    }

  import cats.Eval
  def foldRightSafe[A, B](as: List[A], acc: B)(fn: (A,B) => B): Eval[B] =
    as match {
      case head :: tail =>
        Eval.defer(foldRightSafe(tail, fn(head, acc))(fn))
      case Nil => Eval.now(acc)
    }
}

object EvalExerciseApp extends App {
  import EvalExercise._

  val s1 = foldRight(List(1,2,3,4), 0)(_ + _)
  println(s1)

  val s2 = foldRightSafe(List(1,2,3,4), 0)(_ + _)
  println(s2.value)
}


object IdExample {
  type Id[A] = A
  implicit val idMonad = new Monad[Id] {
    override def pure[A](a: A): Id[A] = a
    override def flatMap[A, B](value: Id[A])(func: A => Id[B]): Id[B] = func(value)
  }
}

object IdExampleApp extends App {
  import IdExample._

  val m = Monad[Id]
  println(m.pure(123))
  println(m.pure("abc"))
  println(m.flatMap(123)(_ + "3"))
}

object WriterMonadExercise {

  def slowly[A](body: => A) =
    try body finally Thread.sleep(100)

  def factorial(n: Int): Int = {
    val tname = Thread.currentThread().getName
    println(s"[$tname] trying $n")
    val ans = slowly(if (n == 0) 1 else n * factorial(n-1))
    println(s"[$tname] fact $n $ans")
    ans
  }

  import cats.data.Writer
  import cats.instances.vector._
  import cats.syntax.writer._
  import cats.syntax.applicative._

  type Logged[A] = Writer[Vector[String], A]

  def factorial2(n: Int): Logged[Int] = {
    val tname = Thread.currentThread().getName
    for {
      _ <- Vector(s"[$tname] trying $n").tell
      ans <- slowly(if (n == 0) 1.pure[Logged] else factorial2(n-1).map(_ * n))
      _ <- Vector(s"[$tname] fact $n $ans").tell
    } yield ans
  }

  def factorial3(n: Int): Logged[Int] = {
    val tname = Thread.currentThread().getName
    Vector(s"[$tname] trying $n").tell
      .flatMap(_ => slowly(if (n == 0) 1.pure[Logged] else factorial3(n-1).map(_ * n)))
      .flatMap(ans => Writer(Vector(s"[$tname] fact $n $ans"), ans))
  }
}

object WriterMonadExerciseApp extends App {
  import WriterMonadExercise._
  import scala.concurrent._
  import scala.concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global

  def singleThreadFactorial(): Unit = {
    factorial(5)
  }

  // log message interleaved in concurrent environment
  def multiThreadFacotrial(): Unit = {
    Await.result(Future.sequence(List(
      Future(factorial(3)),
      Future(factorial(3)),
    )), 5 seconds)
  }

  // writer monad
  def multiThreadWriter(): Unit = {
    val list = Await.result(Future.sequence(List(
      Future(factorial2(3)),
      Future(factorial3(3)),
    )), 5 seconds)

    list.foreach { w =>
      val (logs, value) = w.run
      println(s"result: $value")
      logs.foreach(println)
      println("-----------")
    }
  }

  multiThreadFacotrial()
  println("=============")
  multiThreadWriter()

}

object ReaderExercise {
  case class Db(usernames: Map[Int, String], passwords: Map[String, String])

  import cats.syntax.applicative._
  import cats.data.Reader
  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] =
    Reader[Db, Option[String]](_.usernames.get(userId))

  def checkPassword(username: String, password: String): DbReader[Boolean] =
    Reader[Db, Boolean](_.passwords.get(username).exists(_ == password))

  def checkLogin(userId: Int, password: String): DbReader[Boolean] =
    for {
      username <- findUsername(userId)
      ans <- if (username.isEmpty) false.pure[DbReader] else checkPassword(username.get, password)
    } yield ans

  def checkLogin2(userId: Int, password: String): DbReader[Boolean] =
    findUsername(userId).flatMap {
      case None => false.pure[DbReader]
      case Some(username) => checkPassword(username, password)
    }
}

object ReaderExerciseApp extends App {
  import ReaderExercise._

  val users = Map(
    1 -> "dade",
    2 -> "kate",
    3 -> "margo"
  )
  val passwords = Map(
    "dade" -> "zerocool",
    "kate" -> "acidburn",
    "margo" -> "secret"
  )
  val db = Db(users, passwords)

  println(checkLogin(1, "zerocool").run(db))
  println(checkLogin(4, "davini").run(db))
}