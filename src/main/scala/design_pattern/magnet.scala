package design_pattern

sealed trait MyMagnet {
  type Result
  def apply(): Result
}

object MyMagnet {

  implicit def fromInt(i: Int): MyMagnet = {
    new MyMagnet {
      override type Result = Int
      override def apply: Int = i + 1
    }
  }

  implicit def fromString(s: String): MyMagnet = {
    new MyMagnet {
      override def apply: Result = "hello " + s
      override type Result = String
    }
  }

  implicit def fromStringAndInt(tuple: (String, Int)) =
    new MyMagnet {
      override def apply(): String = tuple._1 + tuple._2.toString()
      override type Result = String
    }
}



object Magnet {

  def main(args: Array[String]): Unit = {
    println(add(1))
    println(add("world"))
    println(add("world", 1))
  }

  def add(magnet: MyMagnet): magnet.Result = magnet()
}
