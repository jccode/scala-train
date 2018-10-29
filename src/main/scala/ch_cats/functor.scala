package ch_cats


// cats instance
object TreeFunctorInstances {
  import cats.Functor

  // Functor instances
  implicit val treeFunctor = new Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] =
      fa match {
        case Branch(left, right) => Branch(map(left)(f), map(right)(f))
        case Leaf(value) => Leaf(f(value))
      }
  }
}


// Test app
object TreeApp extends App {
  import cats._
  import cats.syntax.functor._
  import Tree._
  import TreeFunctorInstances._

  val tree: Tree[Int] = Branch(Branch(Leaf(1), Leaf(2)), Leaf(4))
  println(tree)

  val rtree2: Tree[Int] = Functor[Tree].map(tree)(_ * 2)
  println(rtree2)

  val rtree3 = tree.map(_ * 3)
  println(rtree3)

  val l1 = leaf(2).map(_ * 2)
  println(l1)

  val tree2 = branch(branch(leaf(1), leaf(2)), leaf(4))
  val rtree21 = tree2.map(_ * 4)
  println(rtree21)
}



// p65. codec
trait Codec[A] {
  self =>

  def encode(value: A): String
  def decode(value: String): A

  def imap[B](dec: A => B, enc: B => A): Codec[B] =
    new Codec[B] {
      override def encode(value: B): String = self.encode(enc(value))
      override def decode(value: String): B = dec(self.decode(value))
    }
}

object Codec {
  def encode[A](value: A)(implicit codec: Codec[A]): String = codec.encode(value)
  def decode[A](value: String)(implicit codec: Codec[A]): A = codec.decode(value)
}

object CodecInstance {
  implicit val stringCodec = new Codec[String] {
    override def encode(value: String): String = value
    override def decode(value: String): String = value
  }

  implicit val doubleCodec = stringCodec.imap[Double](_.toDouble, _.toString)

  implicit def boxCodec[A](implicit codec: Codec[A]): Codec[Box[A]] =
    codec.imap[Box[A]](Box(_), _.value)
}

object CodecApp extends App {
  import Codec._
  import CodecInstance._

  println(encode("Hello"))
  println(encode(2.36))

  println(decode[Double]("2.36"))
  println(decode[String]("2.36"))
  println(decode[Box[Double]]("2.36"))
}