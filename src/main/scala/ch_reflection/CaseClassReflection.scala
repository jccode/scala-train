package ch_reflection

import scala.reflect.runtime.universe._

object CaseClassReflection extends App {

  // Case classes automatically mix in the Product trait,
  // providing untyped, indexed access to the fields without any reflection:
  // via: https://coderwall.com/p/rbzlmq/scala-case-class-reflection

  case class Person(name: String, age: Int)

  val p = Person("Tom", 18)
  val name = p.productElement(0)
  val age = p.productElement(1)
  val values = p.productIterator.toList

  println(values)

  // via: https://stackoverflow.com/questions/16079113/scala-2-10-reflection-how-do-i-extract-the-field-values-from-a-case-class
  def getFields[T: TypeTag] = typeOf[T].members.collect {
    case m: MethodSymbol if m.isCaseAccessor => m
  }.toList

  // via: https://stackoverflow.com/questions/11062166/dynamic-method-invocation-with-new-scala-reflection-api
  def invoke(ins: Person)(method: MethodSymbol) =
    runtimeMirror(getClass.getClassLoader).reflect(ins).reflectMethod(method)()

  // test
  getFields[Person].foreach {m =>
    println(s"field: ${m.name}, value: ${invoke(p)(m)}")
  }
}
