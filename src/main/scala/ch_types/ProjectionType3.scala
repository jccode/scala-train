package ch_types

import utils.CommonOpts

/**
  * ProjectionType3
  *
  * @author 01372461
  */
object ProjectionType3 extends App {
  private val foo = new Foo
  private val holder = new FooTypeHolder
  private val client = new Client(holder)
//  client.showElementType(foo) // TODO why compile error this line?
  println("--------")
  println(CommonOpts.classOf(client.holder))
}

class Foo

trait TypeHolder[T] {
  type ElementType = T
}

class FooTypeHolder extends TypeHolder[Foo]

class Client[T <: TypeHolder[_]](val holder: T) {

  def showElementType(t: T#ElementType): Unit = {
    println("show element type")
    println(CommonOpts.classOf(t))
  }
}