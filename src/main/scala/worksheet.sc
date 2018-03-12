val p = "akka://default/user/$$d/group-group1/device-device1"
val arr = p.split('/')
arr.dropRight(1).mkString("/")
arr.mkString("/")

val pattern = "(\\w+)\\((\\w+)\\)".r
val s = "R014(201002)"
val pattern(name, code) = s
name
code

val m = Map("foo" -> "bar", "tom" -> "cat")
m("foo")

case class Foo(a: String, b: Int, c: Double)
Foo("a", 1, 1.0)

val xs = List(1,2,3,4,5)
val git = xs grouped 3
git.next()
git.next()
val sit = xs sliding 3
sit.next()
sit.next()
sit.next()
val ys = List("a", "b", "c", "d", "e", "f", "g")
val zs = xs.zip(ys)
val zs2 = xs.zipAll(ys, 0, 0)
xs.zipWithIndex

val x1 = List(1,2,3,4)
val x2 = Set(1,2,3,4)
val x3 = Map(1->"a", 2->"b", 3->"c", 4->"d")
x1(2)
x2(2)
x3(2)
x1 :+ 5
5 +: x1
x1 ++ x2


