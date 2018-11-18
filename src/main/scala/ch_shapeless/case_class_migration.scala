package ch_shapeless

import cats.Monoid
import shapeless.labelled.{FieldType, field}
import shapeless.{::, HList, HNil, LabelledGeneric, Lazy, Witness}
import shapeless.ops.hlist.{Align, Diff, Intersection, Prepend, Union}


case class IceCreamV1(name: String, numCherries: Int, inCone: Boolean)

// Remove fields
case class IceCreamV2a(name: String, inCone: Boolean)

// Reorder fields
case class IceCreamV2b(name: String, inCone: Boolean, numCherries: Int)

// Insert fields (provided we can determine a default value):
case class IceCreamV2c(name: String, inCone: Boolean, numCherries: Int, numWaffles: Int)


object MigrateTestApp extends App {
  import Migration._
  import cats.instances._

  println("hello")
  val v1 = IceCreamV1("Sundae", 1, false)
  val v2a = v1.migrateTo[IceCreamV2a]
  val v2b = v1.migrateTo[IceCreamV2b]
//  val v2c = v1.migrateTo[IceCreamV2c]
  println(v2a)
  println(v2b)
//  println(v2c)
}



trait Migration[A, B] {
  def apply(a: A): B
}

object Migration {

  // "summoner" method
  def apply[A, B](implicit m: Migration[A, B]) = m


  // implements
  implicit def migrationInstance[A, B, ARepr <: HList, BRepr <: HList, InterOut <: HList, DiffOut <: HList, PrependOut <: HList]
  (implicit aGen: LabelledGeneric.Aux[A, ARepr],
   bGen: LabelledGeneric.Aux[B, BRepr],
   inter: Intersection.Aux[ARepr, BRepr, InterOut],
   diff: Diff.Aux[BRepr, InterOut, DiffOut],
   m: Monoid[DiffOut],
   prepend: Prepend.Aux[DiffOut,InterOut,PrependOut],
   align: Align[PrependOut, BRepr]
  ): Migration[A, B] =
    (a: A) => {
      val ra: ARepr = aGen.to(a)
      val common: InterOut = inter.apply(ra)
      val prependOut: PrependOut = prepend.apply(m.empty,common)
      val rb = align.apply(prependOut)
      bGen.from(rb)
    }

  // migrateTo syntax
  implicit class MigrationOps[A](a: A) {
    def migrateTo[B](implicit m: Migration[A, B]): B = m.apply(a)
  }



  // hlist monoid implement
  def createMonoid[A](zero: A)(combineFunc: (A, A) => A): Monoid[A] = new Monoid[A] {
    override def empty: A = zero
    override def combine(x: A, y: A): A = combineFunc(x, y)
  }

  implicit val hnilMonoid: Monoid[HNil] = createMonoid[HNil](HNil)((_, _) => HNil)

  /*
  implicit def hlistMonoid[H, L <: HList](implicit hMonoid: Lazy[Monoid[H]], tMonoid: Monoid[L]): Monoid[H :: L] =
    createMonoid[H :: L](hMonoid.value.empty :: tMonoid.empty)((x, y) =>
      hMonoid.value.combine(x.head, y.head) :: tMonoid.combine(x.tail, y.tail)
    )
    */

  implicit def hlistMonoid[K <: Symbol, H, L <: HList](implicit hMonoid: Lazy[Monoid[H]], tMonoid: Monoid[L]): Monoid[FieldType[K, H] :: L] =
    createMonoid[FieldType[K, H] :: L](field[K](hMonoid.value.empty) :: tMonoid.empty)((x, y) =>
      field[K](hMonoid.value.combine(x.head, y.head)) :: tMonoid.combine(x.tail, y.tail)
    )


  /*
  implicit def hlistMonoid[K <: Symbol, H, L <: HList]
  (implicit hMonoid: Lazy[Monoid[H]], tMonoid: Monoid[L]): Monoid[FieldType[K, H] :: L] =
    ???
    */

}

