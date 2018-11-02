package ch_cats

import cats.Applicative
import cats.instances.future._
import cats.instances.list._
import cats.instances.option._
import cats.instances.vector._
import cats.syntax.applicative._
import cats.syntax.apply._
import cats.syntax.validated._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  * traverse
  *
  * @author 01372461
  */
object traverse {

  def listTraverse[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
    list.foldLeft(List.empty[B].pure[F])((acc: F[List[B]], el: A) => {
      (acc, func(el)).mapN(_ :+ _)
    })


  def listSequence[F[_]: Applicative, A](list: List[F[A]]): F[List[A]] =
    listTraverse(list)(identity)
}

object TraverseApp extends App {
  import traverse._

  def ex01(): Unit = {
    val hostnames = List(
      "alpha.example.com",
      "beta.example.com",
      "gamma.demo.com"
    )

    def getUptime(hostname: String): Future[Int] = Future(hostname.length * 60)

    val f1 = listTraverse(hostnames)(getUptime)
    val r1 = Await.result(f1, 1 second)
    println(r1)

    val f2 = listTraverse(hostnames)(i => Option(i+"!"))
    println(f2)

    val f3 = listSequence(List(Option(1), Option(2), Option(3)))
    println(f3)

    val f4 = listSequence(List(Vector(1,2), Vector(3,4)))
    val f5 = listSequence(List(Vector(1,2), Vector(3,4), Vector(5,6)))
    println(f4)
    println(f5)
  }


  def ex02(): Unit = {
    def process(inputs: List[Int]) =
      listTraverse(inputs)(n => if(n % 2 == 0) Some(n) else None)

    def process2(inputs: List[Int]) =
      listTraverse(inputs) { n => if (n % 2 == 0) n.valid else List(s"$n is not even").invalid }

    val c1 = process(List(2,4,6))
    val c2 = process(List(1,2,3))

    val d1 = process2(List(2,4,6))
    val d2 = process2(List(1,2,3))

    println(c1)
    println(c2)
    println(d1)
    println(d2)
  }

  def ex03(): Unit = {
    import cats.syntax.traverse._

    val a1 = List(1,2,3).traverse(Option(_))
    println(a1)
    val a2 = List(Option(1), Option(2), Option(3)).sequence[Option, Int]
    println(a2)
  }

  ex03()

}