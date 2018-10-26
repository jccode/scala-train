import cats.instances.function._
import cats.syntax.functor._

val func1: Int => Double = (x: Int) => x.toDouble
val func2: Double => Double = (x: Double) => x * 2

(func1 map func2)(1)
(func1 andThen func2)(1)
func2(func1(1))

type F[A] = Int => A
