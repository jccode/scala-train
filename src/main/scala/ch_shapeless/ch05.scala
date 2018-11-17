package ch_shapeless

import shapeless.labelled.FieldType
import shapeless.{Generic, LabelledGeneric, Witness}


object LabelledGenericTest extends App {
  val gen = Generic[Employee]
  val gen2 = LabelledGeneric[Employee]

  val employee = Employee("tom", 19, true)
  val v1 = gen.to(employee)
  val v2 = gen2.to(employee)

  def getFieldValue[K, V](v: FieldType[K, V]): V = v
  def getFieldName[K <: Symbol, V](v: FieldType[K, V])(implicit w: Witness.Aux[K]): String =
    w.value.name

  //v2.head

  println(getFieldName(v2.last))
  println(getFieldValue(v2.last))
}