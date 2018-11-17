package utils

import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag

object CommonOpts {

  def using[T <: { def close() }](resource: T)(block: T => Unit) = {
    try {
      block(resource)
    } finally {
      if (resource != null) resource.close()
    }
  }

  def classOf[T](t: T)(implicit ev: ClassTag[T]) = ev.toString

  def typeOf[T](t: T)(implicit ev: TypeTag[T]) = ev.toString
}
