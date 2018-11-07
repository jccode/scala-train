package ch_shapeless

import shapeless._

/**
  * ch01
  *
  * @author 01372461
  */
object ch01 extends App {

  case class Employee(name: String, number: Int, manager: Boolean)
  case class IceCream(name: String, numCherries: Int, inCone: Boolean)

  val genericEmployee = Generic[Employee]
  val genericIceCream = Generic[IceCream]

  def genericCsv(gen: String :: Int :: Boolean :: HNil): List[String] =
    List(gen(0), gen(1).toString, gen(2).toString)

  println(genericCsv(genericEmployee.to(Employee("Dave", 123, false))))
  println(genericCsv(genericIceCream.to(IceCream("Sundae", 1, false))))

}
