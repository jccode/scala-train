package ch_cats


trait Printable[A] {
  def format(a: A): String
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
}

object PrintableApp extends App {
  import PrintableInstances._

  Printable.print("Hello")
  Printable.print(123)
}