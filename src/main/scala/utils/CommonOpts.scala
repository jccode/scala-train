package utils

object CommonOpts {

  def using[T <: { def close() }](resource: T)(block: T => Unit) = {
    try {
      block(resource)
    } finally {
      if (resource != null) resource.close()
    }
  }

}
