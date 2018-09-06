package learning_akka_videos.section7

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * SimpleStreamSpec
  *
  * @author 01372461
  */
class SimpleStreamSpec extends TestKit(ActorSystem("test-system"))
  with ImplicitSender with MustMatchers with FlatSpecLike with BeforeAndAfterAll {

  override def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  implicit val flowMaterializer = ActorMaterializer()

  "Simple Sink" should "return the correct result" in {
    val sinkUnerTest = Sink.fold[Int, Int](0)(_ + _)
    val source = Source(1 to 4)
    val result = source.runWith(sinkUnerTest)
    Await.result(result, 100.millis) must be (10)
  }

  "Simple Source" should "contains a correct elements" in {
    val source = Source(1 to 10)
    val result = source.grouped(2).runWith(Sink.head)
    Await.result(result, 100.millis) must equal(1 to 2)
  }

  "Simple Flow" should "do right transform" in {
    val flow = Flow[Int].takeWhile(_ < 5)
    val result = Source(1 to 10).via(flow).runWith(Sink.fold(Seq.empty[Int])(_ :+ _))
    Await.result(result, 100.millis) must equal(1 to 4)
  }

}
