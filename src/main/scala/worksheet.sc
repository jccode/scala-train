import shapeless.Generic
import shapeless.{::, HList, HNil}
import shapeless.ops.hlist.{Diff, Intersection, Prepend}

case class IceCreamV1(name: String, numCherries: Int, inCone: Boolean)

// Remove fields
case class IceCreamV2a(name: String, inCone: Boolean)

// Reorder fields
case class IceCreamV2b(name: String, inCone: Boolean, numCherries: Int)

// Insert fields (provided we can determine a default value):
case class IceCreamV2c(name: String, inCone: Boolean, numCherries: Int, numWaffles: Int)

val g1 = Generic[IceCreamV1]
val g2a = Generic[IceCreamV2a]

val v1 = IceCreamV1("Sundae", 1, false)
val h1 = g1.to(v1)

val v2a = IceCreamV2a("Sundae", false)
val h2a = g2a.to(v2a)

type HV1 = String :: Int :: Boolean :: HNil
type HV2a = String :: Boolean :: HNil
type HV2b = String :: Boolean :: Int :: HNil
type HV2c = String :: Boolean :: Int :: Int :: HNil

val intera = Intersection[HV1, HV2a]
intera.apply(h1)

val interb = Intersection[HV1, HV2b]
interb.apply(h1)

val interc = Intersection[HV1, HV2c]
interc.apply(h1)

Intersection[HV1, String :: Int :: Boolean :: Int :: HNil]

Diff[HV1, HV2a]
Diff[HV1, HV2b]
Diff[HV2c, HV1]
Diff[Int :: Boolean :: String :: String :: HNil, HV1]
