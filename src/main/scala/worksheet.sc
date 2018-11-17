import shapeless.{Generic, LabelledGeneric, Witness}
import shapeless.syntax.singleton._
import shapeless.labelled.{FieldType, KeyTag, field}

case class Employee(name: String, number: Int, manager: Boolean)

val gen = Generic[Employee]
val gen2 = LabelledGeneric[Employee]

val employee = Employee("tom", 19, true)
val v1 = gen.to(employee)
val v2 = gen2.to(employee)

def getFieldValue[K, V](v: FieldType[K, V]): V = v
def getFieldName[K <: Symbol, V](v: FieldType[K, V])(implicit w: Witness.Aux[K]): String =
  w.value.name

v2.head

getFieldName(v2.last)
getFieldValue(v2.last)

("hello", 132)
"Hello" ->> 1132
'Hello
'Hello ->> 123
"hello".narrow

val list = 1 to 100
list.head
list.tail
list.init
list.last