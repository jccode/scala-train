package ch_types

import utils.CommonOpts

/**
  * ProjectionType
  * ref:
  * - https://hongjiang.info/scala-type-system-inner-type-and-type-projection/
  * - http://blog.csdn.net/pofengliuming/article/details/52163899
  *
  * @author 01372461
  */
object ProjectionType extends App {

  val out1 = new Outer("out1")
  val in1 = new out1.Inner("in1")
  println(out1)
  println(in1)
  println(CommonOpts.classOf(in1))

  val out2 = new Outer("out2")
  val in2 = new out2.Inner("in2")
  println(out2)
  println(in2)
  println(CommonOpts.classOf(in2))

  println("-------------------")
  println(out1.getInnerNameBad(in1))
//  println(out1.getInnerNameBad(in2)) // cannot compile
  println(out2.getInnerNameBad(in2))
//  println(out2.getInnerNameBad(in1)) // cannot compile

  println(out1.getInnerNameGood(in1))
  println(out1.getInnerNameGood(in2)) // can compile
}

class Outer(var name: String) {
  override def toString = s"Outer($name)"

  def getInnerNameBad(inner: Inner): String = inner.name
  def getInnerNameGood(inner: Outer#Inner): String = inner.name

  class Inner(var name: String) {
    override def toString = s"Inner($name)"
  }
}