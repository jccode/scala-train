package ch_cats

import cats.Monoid
import cats.syntax.semigroup._
import cats.syntax.foldable._
import cats.instances.map._
import cats.instances.list._

final case class GCounter(counters: Map[String, Int]) {
  def increment(machine: String, amount: Int) =
    GCounter(counters + (machine -> (counters.getOrElse(machine, 0) + amount)))

  def merge(that: GCounter) = {
    GCounter(that.counters ++ counters.map {
      case (k, v) => k -> v.max(that.counters.getOrElse(k, 0))
    })
  }

  def total: Int = counters.values.sum
}

trait BoundedSemiLattice[A] extends Monoid[A] {
  def empty: A
  def combine(a1: A, a2: A): A
}

object BoundedSemiLattice {
  def apply[A](implicit m: BoundedSemiLattice[A]) = m

  implicit val intInstance = new BoundedSemiLattice[Int] {
    override def empty: Int = 0
    override def combine(a1: Int, a2: Int): Int = a1 max a2
  }

  implicit def setInstance[A] = new BoundedSemiLattice[Set[A]] {
    override def empty: Set[A] = Set.empty[A]
    override def combine(a1: Set[A], a2: Set[A]): Set[A] = a1 union a2
  }
}

object GenericGCounter {

  final case class GCounter[A](counters: Map[String, A]) {
    def increment(machine: String, a: A)(implicit m: Monoid[A]) =
      GCounter(counters + (machine -> (counters.getOrElse(machine, m.empty) |+| a)))

    def merge(that: GCounter[A])(implicit m: BoundedSemiLattice[A]) =
      GCounter(this.counters |+| that.counters)

    def total(implicit m: Monoid[A]): A = counters.values.toList.combineAll
  }
}

object GenericGCounterTypeclass {

  trait GCounter {
    def increment
    def merge
    def total
  }
}