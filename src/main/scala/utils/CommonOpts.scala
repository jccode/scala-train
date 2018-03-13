package utils

import scala.reflect.ClassTag

object CommonOpts {

  def using[T <: { def close() }](resource: T)(block: T => Unit) = {
    try {
      block(resource)
    } finally {
      if (resource != null) resource.close()
    }
  }

  def typeOf[T](t: T)(implicit ev: ClassTag[T]) = ev.toString
}
