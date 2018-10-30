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
  import scala.util.Either
  import cats.data.EitherT
  import cats.syntax.applicative._


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

    val stringOrInt: Either[String, Int] = powerLevels.get(autobot)
      .fold(Left(s"autobot $autobot is unreachable"): Either[String, Int])(Right(_))

    stringOrInt.pure[Response[Int]]


    powerLevels.get(autobot)
      .fold(Left(s"autobot $autobot is unreachable"): Either[String, Int])(Right(_))
      .map(Future(_))
      .pure[Response]

    ???
  }


}