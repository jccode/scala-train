package ch_future

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  * t
  *
  * @author 01372461
  */
object FutureTest extends App {

  val f: Future[Int] = Future {
    Thread.sleep(3000) // 这里等个3秒再返回.
    10
  }

  def test1 = {
    println("不使用await进行等待,主进程正常结束,得不到future的结果.")
    f.map(println)
    println("--看有结果输出没")
  }

  def test2 = {
    println("必须等待3秒以上,等future返回.")
    val finalResult: Future[Unit] = f.map(println)
    // 1.必须等待3秒以上,3秒以下会超时; 2.如果不等待,主进程会正常结束,无法得到返回结果.
    Await.result(finalResult, 3 seconds)
    println("--看有结果输出没")
  }

  def test3 = {
    println("用system.in.read阻塞主线程,等待future返回. 等待ing...")
    f.map(println)
    println("--看有结果输出没")
    System.in.read()
  }

  test3
}
