package ch_cats

import cats.Monoid
import cats.syntax.semigroup._
import cats.syntax.foldable._
import cats.instances.map._
import cats.instances.list._
import cats.instances.int._


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

  implicit val intInstance: BoundedSemiLattice[Int] = new BoundedSemiLattice[Int] {
    override def empty: Int = 0
    override def combine(a1: Int, a2: Int): Int = a1 max a2
  }

  implicit def setInstance[A] = new BoundedSemiLattice[Set[A]] {
    override def empty: Set[A] = Set.empty[A]
    override def combine(a1: Set[A], a2: Set[A]): Set[A] = a1 union a2
  }
}

object GCounterTestApp extends App {
  val g1 = Map("a" -> 7, "b" -> 3)
  val g2 = Map("a" -> 2, "b" -> 5)
  val c1 = GCounter(g1)
  val c2 = GCounter(g2)

  val merge = c1.merge(c2)
  println(merge.total)
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

  trait GCounter[F[_, _], K, V] {
    def increment(f: F[K, V])(k: K, v: V)(implicit m: Monoid[V]): F[K, V]

    def merge(f1: F[K, V], f2: F[K, V])(implicit m: BoundedSemiLattice[V]): F[K, V]

    def total(f: F[K,V])(implicit m: Monoid[V]): V
  }

  object GCounter {

    def apply[F[_,_], K, V](implicit counter: GCounter[F, K, V]) =
      counter

    implicit def mapGCounter[K, V] = new GCounter[Map, K, V] {
      override def increment(f: Map[K, V])(k: K, v: V)(implicit m: Monoid[V]): Map[K, V] =
        f + (k -> (f.getOrElse(k, m.empty) |+| v))

      override def merge(f1: Map[K, V], f2: Map[K, V])(implicit m: BoundedSemiLattice[V]): Map[K, V] =
        f1 |+| f2

      override def total(f: Map[K, V])(implicit m: Monoid[V]): V =
        f.values.toList.combineAll
    }

  }
}

object GenericTypeClassApp extends App {
  import GenericGCounterTypeclass.{GCounter => GGCounter}

  val g1 = Map("a" -> 7, "b" -> 3)
  val g2 = Map("a" -> 2, "b" -> 5)

  val counter = GGCounter[Map, String, Int]
  val merge = counter.merge(g1, g2)
  println(counter.total(merge))
}

object KVTypeClass {

  trait KeyValueStore[F[_, _]] {
    def put[K, V](f: F[K, V])(k: K, v: V): F[K, V]

    def get[K, V](f: F[K, V])(k: K): Option[V]

    def getOrElse[K, V](f: F[K, V])(k: K, default: V): V =
      get(f)(k).getOrElse(default)

    def values[K, V](f: F[K, V]): List[V]
  }

  object KeyValueStore {

    implicit def mapInstance = new KeyValueStore[Map] {
      override def put[K, V](f: Map[K, V])(k: K, v: V): Map[K, V] = f + (k -> v)

      override def get[K, V](f: Map[K, V])(k: K): Option[V] = f.get(k)

      override def values[K, V](f: Map[K, V]): List[V] = f.values.toList
    }

  }


  implicit class KvsOps[F[_,_], K, V](f: F[K, V])(implicit kvs: KeyValueStore[F]) {
    def put(key: K, value: V): F[K, V] = kvs.put(f)(key, value)

    def get(key: K): Option[V] = kvs.get(f)(key)

    def getOrElse(key: K, default: V): V = kvs.getOrElse(f)(key, default)

    def values: List[V] = kvs.values(f)
  }
}

object GenericTypeClassIntance {

  import GenericGCounterTypeclass.{GCounter => GGCounter}
  import KVTypeClass._

  implicit def gcounterInstance[F[_, _], K, V](implicit kvs: KeyValueStore[F], km: Monoid[F[K, V]]) = new GGCounter[F, K, V] {

    override def increment(f: F[K, V])(k: K, v: V)(implicit m: Monoid[V]): F[K, V] = f.put(k, f.getOrElse(k, m.empty) |+| v)

    override def merge(f1: F[K, V], f2: F[K, V])(implicit m: BoundedSemiLattice[V]): F[K, V] = f1 |+| f2

    override def total(f: F[K, V])(implicit m: Monoid[V]): V = f.values.combineAll
  }

}
