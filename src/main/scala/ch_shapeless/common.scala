package ch_shapeless


case class Employee(name: String, number: Int, manager: Boolean)
case class IceCream(name: String, numCherries: Int, inCone: Boolean)


sealed trait Shape
final case class Rectangle(width: Double, height: Double) extends Shape
final case class Circle(radius: Double) extends Shape


sealed trait Tree[A]
final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

object Tree {
  def leaf[A](value: A): Tree[A] = Leaf(value)
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)
}
