package learning_akka_videos.section7

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, MustMatchers}

import scala.concurrent.Await
import scala.concurrent.duration._


/**
  * SimpleActorSpec
  *
  * @author 01372461
  */
class SimpleActorSpec extends TestKit(ActorSystem("test-system"))
  with ImplicitSender with FlatSpecLike with MustMatchers with BeforeAndAfterAll {

  import akka.pattern.pipe
  import system.dispatcher

  override protected def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  implicit val flowMaterializer = ActorMaterializer()

  "With testkit" should "test actor receive elements from the sink " in {
    val source = Source(1 to 4).grouped(2)
    val testProbe = TestProbe()
    source.runWith(Sink.head).pipeTo(testProbe.ref)
    testProbe.expectMsg(Seq(1, 2))
  }

  it should "have a control over a receiving elements" in {
    case object Tick

    val source = Source.tick(0.millis, 200.millis, Tick)
    val probe = TestProbe()
    val sink = Sink.actorRef(probe.ref, "completed")
    val runnable = source.to(sink).run()

    probe.expectMsg(1.second, Tick)
    probe.expectNoMsg(100.millis)
    probe.expectMsg(200.millis, Tick)
    runnable.cancel()
    probe.expectMsg(200.millis, "completed")
  }

  it should "have a control over elements to be sent " in {
    val sink = Flow[Int].map(_.toString).toMat(Sink.fold("")(_ + _))(Keep.right)
    val source = Source.actorRef(8, OverflowStrategy.fail)
    val (ref, result) = source.toMat(sink)(Keep.both).run()

    ref ! 1
    ref ! 2
    ref ! 3
    ref ! akka.actor.Status.Success("done")

    Await.result(result, 200.millis) must equal("123")
  }
}
