package ch_03

import scala.collection.mutable.ListBuffer

/**
  * QuickSort
  *
  * @author 01372461
  */
object SortApp extends App {

  val arr = List(1, 9, 4, 5, 7, 6, 2, 8)

  def quickSort(arr: Seq[Int]): Seq[Int] = {
    arr match {
      case Nil => arr
      case head :: tail =>
        val smallerSorted = quickSort(for (i <- tail if i <= head) yield i)
        val biggerSorted = quickSort(for (i <- tail if i > head) yield i)
        (smallerSorted :+ head) ++ biggerSorted
    }
  }

  def quickSort2[T : Ordering](seq : Seq[T]): Seq[T] = {
    val o = implicitly[Ordering[T]]
    seq match {
      case Nil => seq
      case head :: tail =>
        val smallerSorted = quickSort2(for (i <- tail if o.lteq(i, head)) yield i)
        val biggerSorted = quickSort2(for (i <- tail if o.gt(i, head)) yield i)
        (smallerSorted :+ head) ++ biggerSorted
    }
  }

  def bubblePop(arr: Seq[Int]): Seq[Int] = {
    val len = arr.size
    if (len == 0) return arr

    val ret = ListBuffer(arr: _*)
    for (i <- 0 until len) {
      for (j <- i+1 until len) {
        if (ret(i) > ret(j)) {
          val ei = ret(i)
          ret(i) = ret(j)
          ret(j) = ei
        }
      }
    }
    ret.toList
  }


  println(s"Original:\t $arr")
  println("QuickSort1:\t " + quickSort(arr))
  println("QuickSort2:\t " + quickSort2(arr))
  println("BubblePop:\t " + bubblePop(arr))

}
