package ch_shapeless

import shapeless.{HList, LabelledGeneric}
import shapeless.ops.hlist.{Align, Diff, Intersection, Union}


case class IceCreamV1(name: String, numCherries: Int, inCone: Boolean)

// Remove fields
case class IceCreamV2a(name: String, inCone: Boolean)

// Reorder fields
case class IceCreamV2b(name: String, inCone: Boolean, numCherries: Int)

// Insert fields (provided we can determine a default value):
case class IceCreamV2c(name: String, inCone: Boolean, numCherries: Int, numWaffles: Int)


object MigrateTestApp extends App {
  import Migration._

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
  implicit def migrationInstance[A, B, ARepr <: HList, BRepr <: HList, InterOut <: HList]
  (implicit aGen: LabelledGeneric.Aux[A, ARepr],
   bGen: LabelledGeneric.Aux[B, BRepr],
   inter: Intersection.Aux[ARepr, BRepr, InterOut],
   align: Align[InterOut, BRepr]
  ): Migration[A, B] =
    (a: A) => {
      val ra: ARepr = aGen.to(a)
      val rb: BRepr = inter.apply(ra)
      bGen.from(rb)
    }

  // migrateTo syntax
  implicit class MigrationOps[A](a: A) {
    def migrateTo[B](implicit m: Migration[A, B]): B = m.apply(a)
  }
}

