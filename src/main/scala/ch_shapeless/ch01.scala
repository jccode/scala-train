package ch_shapeless

import shapeless._

/**
  * ch01
  *
  * @author 01372461
  */
object ch01 extends App {

  val genericEmployee = Generic[Employee]
  val genericIceCream = Generic[IceCream]

  def genericCsv(gen: String :: Int :: Boolean :: HNil): List[String] =
    //List(gen(0), gen(1).toString, gen(2).toString)
    List(gen.head, gen.tail.head.toString, gen.tail.tail.head.toString)

  println(genericCsv(genericEmployee.to(Employee("Dave", 123, false))))
  println(genericCsv(genericIceCream.to(IceCream("Sundae", 1, false))))

}
