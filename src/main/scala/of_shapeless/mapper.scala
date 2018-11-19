package of_shapeless

trait ProductMapper[A, B, P] {
  def apply(a: A): B
}

object ProductMapper {
  import shapeless._
  import shapeless.ops.hlist

  implicit def genericProductMapper[
  A, B,
  P <: Poly,
  ARepr <: HList,
  BRepr <: HList
  ](
     implicit
     aGen: Generic.Aux[A, ARepr],
     bGen: Generic.Aux[B, BRepr],
     mapper: hlist.Mapper.Aux[P, ARepr, BRepr]
   ): ProductMapper[A, B, P] =
    new ProductMapper[A, B, P] {
      def apply(a: A): B =
        bGen.from(mapper.apply(aGen.to(a)))
    }

  implicit class ProductMapperOps[A](a: A) {
    class Builder[B] {
      def apply[P <: Poly](poly: P)
                          (implicit pm: ProductMapper[A, B, P]): B =
        pm.apply(a)
    }

    def mapTo[B]: Builder[B] = new Builder[B]
  }

}

object ProductMapperApp extends App {
  import shapeless.Poly1
  import ProductMapper._

  object conversions extends Poly1 {
    implicit val intCase:  Case.Aux[Int, Boolean]   = at(_ > 0)
    implicit val boolCase: Case.Aux[Boolean, Int]   = at(if(_) 1 else 0)
    implicit val strCase:  Case.Aux[String, String] = at(identity)
  }

  case class IceCream1(name: String, numCherries: Int, inCone: Boolean)
  case class IceCream2(name: String, hasCherries: Boolean, numCones: Int)

  val iceCream2 = IceCream1("Sundae", 1, false).mapTo[IceCream2](conversions)
  println(iceCream2)
}

