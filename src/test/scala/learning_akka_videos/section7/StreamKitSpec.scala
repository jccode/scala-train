package learning_akka_videos.section7

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.stream.testkit.javadsl.TestSource
import akka.stream.testkit.scaladsl.TestSink
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}

/**
  * StreamKitSpec
  *
  * @author 01372461
  */
class StreamKitSpec extends TestKit(ActorSystem("test-system"))
  with ImplicitSender with FlatSpecLike with MustMatchers with BeforeAndAfterAll {

  override protected def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  import system.dispatcher
  implicit val materializer = ActorMaterializer()

  "With Stream Test Kit" should "use a TestSink to test a source" in {
    val sourceUnderTest = Source(1 to 4).filter(_ < 3).map(_ * 2)

    sourceUnderTest.runWith(TestSink.probe[Int])
      .request(2)
      .expectNext(2,4)
//      .expectComplete()
  }

  it should "use a TestSource to test a sink" in {
    val sinkUnderTest = Sink.cancelled

//    TestSource.probe[Int]
//      .toMat(sinkUnderTest)(Keep.left)
  }
}
