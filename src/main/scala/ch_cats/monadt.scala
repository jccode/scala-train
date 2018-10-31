package ch_cats

/**
  * monadt
  *
  * @author 01372461
  */
object monadt extends App {

  import cats.data.{EitherT, OptionT}
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

//  import scala.concurrent.Await
//  import scala.concurrent.duration._

  import cats.syntax.applicative._
  import cats.instances.future._

  type FutureEither[A] = EitherT[Future, String, A]
  type FutureEitherOption[A] = OptionT[FutureEither, A]

  val futureEitherOr = for {
    a <- 10.pure[FutureEitherOption]
    b <- 32.pure[FutureEitherOption]
  } yield a+b

  println(futureEitherOr)

}

object MonadTransformExerciseApp extends App {
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Await
  import scala.concurrent.duration._
  import scala.util.Either
  import cats.data.EitherT
  import cats.syntax.applicative._
  import cats.syntax.either._
  import cats.instances.future._

  // type Response[A] = Future[Either[String, A]]
  type Response[A] = EitherT[Future, String, A]

  val powerLevels = Map(
    "Jazz"      -> 6,
    "Bumblebee" -> 8,
    "Hot Rod"   -> 10
  )

  /*
  def getPowerLevel(autobot: String): Response[Int] =
    powerLevels.get(autobot) match {
      case None => EitherT[Future, String, Int](Future(Left(s"autobot $autobot is unreachable")))
      case Some(value) => EitherT[Future, String, Int](Future(Right(value)))
    }
  */

  def getPowerLevel(autobot: String): Response[Int] = {
    powerLevels
      .get(autobot)
      .fold(EitherT.left[Int](Future(s"$autobot unreachable")))(value => EitherT.right[String](Future(value)))
  }

  /*
  def canSpecialMove(au1: String, au2: String): Response[Boolean] = {
    getPowerLevel(au1).flatMap(a1 => getPowerLevel(au2).map(a2 => a1 + a2)).map(_ > 15)
  }
  */

  def canSpecialMove(au1: String, au2: String): Response[Boolean] =
    for {
      a1 <- getPowerLevel(au1)
      a2 <- getPowerLevel(au2)
    } yield a1+a2 > 15


  def tacticalReport(au1: String, au2: String): String =
    Await.result(canSpecialMove(au1, au2).value, 1 second)
      .fold[String](v => s"Comm error: $v", v => if (v) s"$au1 and $au2 are ready to roll out!" else s"$au1 and $au2 need a recharge.")


  def test01(): Unit = {
    val p1 = getPowerLevel("jaxx")
    val p2 = getPowerLevel("Jazz")

    val f: Future[List[Either[String, Int]]] = Future.sequence(List(p1, p2).map(_.value))
    println(f)
  }

  def test02(): Unit = {
    println(tacticalReport("Jazz", "Bumblebee"))
    println(tacticalReport("Bumblebee", "Hot Rod"))
    println(tacticalReport("Jazz", "Ironhide"))
  }

  test02()
}