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

