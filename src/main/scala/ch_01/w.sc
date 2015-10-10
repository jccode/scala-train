val hello = new Thread(new Runnable {
  override def run(): Unit = {
    println("hello world")
  }
})
hello.start()


// -------------------

implicit def strToInt(x: String) = x.toInt
class Container[A <% Int] { def addIt(x: A) = 123 + x }
(new Container[String]).addIt("123")
(new Container[Int]).addIt(123)
// (new Container[Float]).addIt(123.2F)

class Container2[A](value: A) {
  def addIt(implicit evidence: A => Int) = 123 + value
}
(new Container2(123)).addIt
(new Container2("123")).addIt
(new Container2(129F)).addIt(math.round)
trait Foo2[M[_]] { type t[A] = M[A] }
val x2: Foo2[List]#t[Int] = List(1)
// -------------------
def drop1[A](l: List[A]) = l.tail
drop1(List(1,2,3,4))
def drop2(l: List[_]) = l.tail
drop2(List(1,2,3,4))
def id[T](x: T) = x
def count[A](l: List[A]) = l.size
def count2(l: List[_]) = l.size
count2(List(1,2,3))
def count3(l: List[T forSome {type T}]) = l.size
count3(List(1,2,3))
class Covariant[+A]
val cv: Covariant[AnyRef] = new Covariant[String]
// val cv2: Covariant[String] = new Covariant[AnyRef]  // compile error

class Contravarient[-A]
val conv: Contravarient[String] = new Contravarient[AnyRef]
// val conv2: Contravarient[AnyRef] = new Contravarient[String] // compile error
class Animal { val sound = "rustle" }
class Bird extends Animal { override val sound = "call" }
class Chicken extends Bird { override val sound = "cluck" }
val getTweet: (Bird => String) = (a: Animal) => a.sound
def biophony[T <: Animal](things: Seq[T]) = things map (_.sound)
biophony(Seq(new Chicken, new Bird))
val flock = List(new Bird, new Bird)
new Chicken :: flock
// -------------------
val one: PartialFunction[Int, String] = { case 1 => "one" }
one.isDefinedAt(1)
one.isDefinedAt(2)
one(1)
val two: PartialFunction[Int, String] = { case 2 => "two" }
val three: PartialFunction[Int, String] = { case 3 => "three" }
val wildcard: PartialFunction[Int, String] = { case _ => "something else" }
val partial = one orElse two orElse three orElse wildcard
partial(5)
partial(3)
partial(1)
case class PhoneExt(name: String, ext: Int)
val extensions = List(PhoneExt("steve", 100), PhoneExt("robey", 200))
extensions.filter({
  case PhoneExt(name, extension) => extension < 200
})
extensions.filter((p: PhoneExt) => p.ext < 200)
// -------------------

class C {
  var acc = 0
  def minc = { acc += 1 }
  val finc = { () => acc+= 1 }
}

val c = new C
c.minc
c.finc
c minc
class Foo {}
object FooMaker {
  def apply() = new Foo
}
val foo = FooMaker()
object addOne extends Function1[Int, Int] {
  def apply(m: Int): Int = m+1
}
addOne(1)
class AddOne2 extends Function1[Int, Int] {
  def apply(m: Int): Int = m+1
}
val plusOne = new AddOne2
plusOne(1)
class AddOne3 extends (Int => Int) {
  def apply(m: Int): Int = m+1
}
val plusOne3 = new AddOne3
plusOne3(1)

case class Calculator(brand: String, model: String)
val hp20b = Calculator("hp", "20b")
val hp20B = Calculator("hp", "20b")
hp20b == hp20B
def calcType(calc: Calculator) = calc match {
  case Calculator("hp", "20B") => "financial"
  case Calculator("hp", "48G") => "scientific"
  case Calculator(ourBrand, ourModel) => "Calculator: %s %s is of unkonw type".format(ourBrand, ourModel)
}
val numbers = List(1,2,3,4)
Set(1, 1, 2)
val hostPort = ("localhost", 80)
hostPort._1
hostPort._2
val hostPort2 = "localhost" -> 80
Map("foo"->"bar")
numbers.map((i: Int) => i*2)
numbers.foreach(i => {
  print(i)
  i*2
})
numbers.filter(i => i % 2 == 0)
List(1,2,3).zip(List("a","b","c"))
val numbers2 = List(1,2,3,4,5,6,7,8,9,10)
numbers2.partition(_ % 2 == 0)
numbers2.find(_ > 5)
numbers2.drop(5)
numbers2.dropWhile(_ % 2 != 0)
numbers2.foldLeft(0) {(m: Int, n: Int) =>
  println("m: %s, n: %s".format(m, n))
  m+n
}
numbers2.foldRight(0) {
  (m: Int, n: Int) =>
    println("m: %s, n:%s".format(m, n))
    m+n
}
List(List(1, 2), List(3,4)).flatten
val nestedNumbers = List(List(1, 2), List(3, 4))
nestedNumbers.flatMap(x => x.map(_ * 2))
def f(s: String) = "f(%s)".format(s)
def g(s: String) = "g(%s)".format(s)
f("a")
g("a")
val f_c_g = f _ compose g _
f_c_g("a")
val f_t_g = f _ andThen g _
f_t_g("a")
// --------------------------
sealed trait TBool {
  type If[TrueType <: Up, FalseType <: Up, Up] <: Up
}
class TTrue extends TBool {
  type If[TrueType <: Up, FalseType <: Up, Up] = TrueType
}
class TFalse extends TBool {
  type If[TrueType <: Up, FalseType <: Up, Up] = FalseType
}
type X[T <: TBool] = T#If[String, Int, Any]

val x: X[TTrue] = "Hi"

val y = "Hello" :: 5 :: false :: Nil
