package ch_cats


trait Printable[A] {
  self =>

  def format(a: A): String

  def contramap[B](func: B => A): Printable[B] =
    (b: B) => self.format(func(b))
}

object Printable {
  def format[A](a: A)(implicit printable: Printable[A]) = printable.format(a)
  def print[A](a: A)(implicit printable: Printable[A]): Unit = println(format(a))
}


object PrintableInstances {
  implicit val stringPrintable = new Printable[String] {
    override def format(a: String): String = a
  }
  implicit val intPrintable = new Printable[Int] {
    override def format(a: Int): String = a.toString
  }

  implicit def boxPrintable[A](implicit printable: Printable[A]): Printable[Box[A]] =
    stringPrintable.contramap[Box[A]](_.value.toString)
}

object PrintableApp extends App {
  import PrintableInstances._

  Printable.print("Hello")
  Printable.print(123)
  Printable.print(Box(321))
  Printable.print(Box("box321"))
}