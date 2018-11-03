import cats.Semigroup
import cats.instances.list._
import cats.syntax.semigroup._

val s = Semigroup[List[String]]

s.combine(List("Hello"), List("world"))

List("Hello") |+| List("world")
