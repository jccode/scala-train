package ch_shapeless


import shapeless.ops.hlist.{IsHCons, Last}
import shapeless.{::, Generic, HList, HNil}

object SimpleGeneric {
  trait Generic[A] {
    type Repr
    def to(value: A): Repr
    def from(value: Repr): A
  }

  def getRepr[A](value: A)(implicit gen: Generic[A]) = gen.to(value)
}

object SecondTypeClass {
  trait Second[L <: HList] {
    type Out
    def apply(value: L): Out
  }

  object Second {
    type Aux[L <: HList, O] = Second[L] { type Out = O }

    // "summoner" method
    def apply[L <: HList](implicit inst: Second[L]): Aux[L, inst.Out] =
    //inst
      inst.asInstanceOf[Aux[L, inst.Out]]

    // "constructor" method
    def instance[L <: HList, O](func: L => O): Aux[L, O] = new Second[L] {
      override type Out = O
      override def apply(value: L): this.Out = func(value)
    }

    // type class instance
    implicit def hlistSecond[H,S,Rest <: HList]: Aux[H :: S :: Rest, S] = instance[H :: S :: Rest, S] {
      case h :: s :: rest => s
    }

    //    implicit def hlistSecond[A, B, Rest <: HList]: Aux[A :: B :: Rest, B] =
    //      new Second[A :: B :: Rest] {
    //        type Out = B
    //        def apply(value: A :: B :: Rest): B =
    //          value.tail.head
    //      }

  }
}

object ch04 {

  def lastField[A, R <: HList](value: A)(implicit gen: Generic.Aux[A, R], last: Last[R]): last.Out =
    last(gen.to(value))

  //def getWrappedValue0[A, H](value: A)(implicit gen: Generic.Aux[A, H::HNil]): H = gen.to(value).head  // compile error
  def getWrappedValue[A, R <: HList, H](value: A)(implicit gen: Generic.Aux[A, R], hcon: IsHCons.Aux[R, H, HNil]): H = gen.to(value).head


}

object Test1App extends App {
  import ch04._

  def lastTest(): Unit = {
    val last1 = Last[String :: Int :: Boolean :: HNil]
    println(last1)
    println(last1("foo" :: 123 :: true :: HNil))
  }

  def secondTest(): Unit = {
    import SecondTypeClass.Second
    val second1 = Second[String :: Int :: Boolean :: HNil]
    println(second1("foo" :: 123 :: true :: HNil))
  }

  def secondTest2(): Unit = {
    import SecondTypeClass.Second
    import scala.reflect.runtime.universe._
    println(reify(implicitly[Second[String :: Int :: HNil]]))
    println(reify(Second[String :: Int :: HNil]))
  }

  def lastFieldTest(): Unit = {

    println(lastField(Employee("tom", 18, true)))
  }

  def wrappedValueTest(): Unit = {
    case class Wrapper(value: Int)
    //println(getWrappedValue0(Wrapper(12)))  // compile error
    println(getWrappedValue(Wrapper(12)))
  }

  wrappedValueTest()
}

