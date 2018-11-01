package ch_adt


sealed trait Tree

final case class Leaf(value: Int) extends Tree
final case class Node(value: Int, left: Tree, right: Tree) extends Tree

object TreeTest extends App {

  val leaf = Leaf(1)
  val tree = Node(5, Node(3, leaf, Leaf(2)), leaf)

  println(leaf)
  println(tree)
}