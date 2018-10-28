import cats.{Contravariant, Functor}
import cats.instances.function._
import cats.syntax.functor._

val func1: Int => Double = (x: Int) => x.toDouble
val func2: Double => Double = (x: Double) => x * 2

(func1 map func2)(1)
(func1 andThen func2)(1)
func2(func1(1))
(func2 compose func1)(1)


type F1[A] = Int => A
val functor1 = Functor[F1]
val fund1 = functor1.map(func1)(func2)
fund1(1)



import cats.syntax.contravariant._

val func3a: Int => Double = (a: Int) => func2(func1(a))
val func3b: Int => Double = func2 compose func1
//val func3c: Int => Double = func2 contramap func1


type F2[A] = A => Double
// Compile failed, because of left-to-right bias
//val functor2 = Functor[F2]

type <=[B, A] = A => B
type F3[A] = Double <= A

// Compile failed.
//val contraFunctor = Contravariant[F2]
//val func3d: Int => Double = contraFunctor.contramap(func2)(func1)

// flip
val func2b: F3[Double] = func2
val contraFunctor2 = Contravariant[F3]
val func3e: Int => Double = contraFunctor2.contramap(func2b)(func1)
func3e(1)


type Id[A] = A
123: Id[Int]
