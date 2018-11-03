package ch_cats

import cats.kernel.Monoid
import cats.syntax.semigroup._
import cats.syntax.traverse._
import cats.syntax.foldable._
import cats.instances.int._
import cats.instances.string._
import cats.instances.future._
import cats.instances.vector._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


object MRDemo {

  /*
  def foldMap[A, B : Monoid](data: Vector[A])(func: A => B): B = {
    val monoid = implicitly[Monoid[B]]
    // data.foldLeft(monoid.empty)((b, a) => monoid.combine(b, func(a)) )
    data.map(func).fold(monoid.empty)(monoid.combine)
  }
  */

  def foldMap[A, B : Monoid](data: Vector[A])(func: A => B): B = {
    data.foldLeft(Monoid[B].empty)(_ |+| func(_))
    // data.map(func).foldLeft(Monoid[B].empty)(_ |+| _)
  }

  private [this] def groupSize(size: Int, n: Int) =
    // if (size % n == 0) size/n else (size/n)+1
    // (size/n) + Math.signum(size%n).toInt
    (size.toDouble/n).ceil.toInt


  def parallelFoldMap[A, B : Monoid](data: Vector[A])(func: A => B): Future[B] = {
    val n = Runtime.getRuntime.availableProcessors()
    /*
    val list = data.grouped(groupSize(data.size, n)).toList
    list.map(foldMap(_)(func)).fold(Monoid[B].empty)(_ |+| _)
    */

    /*
    data.grouped(groupSize(data.size, n))
      .map(x => Future(foldMap(x)(func)))
      .fold(Monoid[Future[B]].empty)(_ |+| _)
    */

    val futures: Iterator[Future[B]] = data.grouped(groupSize(data.size, n))
      .map(x => Future(foldMap(x)(func)))
    Future.sequence(futures).map {iterator =>
      iterator.fold(Monoid[B].empty)(_ |+| _)
    }
  }

  def parallelFoldMap2[A, B : Monoid](data: Vector[A])(func: A => B): Future[B] = {
    val n = Runtime.getRuntime.availableProcessors()
    val f: Vector[A] => Future[B] = (group: Vector[A]) => Future(group.foldMap(func))

    // 类型的变化:
    // Vector[A] ~> Vector[Vector[A]] -> (Vector[A] => Future[B]) ~> Future[Vector[B]] ~> Future[B]

    /*
    val a: Future[Vector[B]] = data
      .grouped(groupSize(data.size, n))
      .toVector
      .traverse(f)
    val b: Future[B] = a.map(_.combineAll)
    b
    */

    data.grouped(groupSize(data.size, n))
      .toVector
      .traverse(f)
      .map(_.combineAll)
  }
}

object MRDemoApp extends App {
  import MRDemo._


  def foldMap_test(): Unit = {
    val a1 = foldMap(Vector(1, 2, 3))(identity)
    val a2 = foldMap(Vector(1, 2, 3))(_.toString + "!")
    val a3 = foldMap("Hello world!".toVector)(_.toString.toUpperCase)

    List(a1,a2,a3).foreach(println)
  }

  def parallelFoldMap_test(): Unit = {
    val a1 = parallelFoldMap((1 to 100).toVector)(identity)
    val a2 = parallelFoldMap((1 to 100).toVector)(_.toString + "!")

    List(a1,a2).foreach {r =>
      println(Await.result(r, 1 second))
    }
  }

  def parallelFoldMap2_test(): Unit = {
    val a1 = parallelFoldMap2((1 to 100).toVector)(identity)
    val a2 = parallelFoldMap2((1 to 100).toVector)(_.toString + "!")

    List(a1,a2).foreach {r =>
      println(Await.result(r, 1 second))
    }
  }

  parallelFoldMap2_test()
}
