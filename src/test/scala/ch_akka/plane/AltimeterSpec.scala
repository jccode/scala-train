package ch_akka.plane

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit}
import ch_akka.plane.EventSource.RegisterListener
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}


class AltimeterSpec extends TestKit(ActorSystem("AltimeterSpec"))
  with WordSpecLike with MustMatchers with BeforeAndAfterAll {

  import Altimeter._

  override protected def afterAll(): Unit = system.terminate()

  def slicedAltimeter = new Altimeter with EventSourceSpy

  def actor() = {
    val actorRef = TestActorRef[Altimeter](Props(slicedAltimeter))
    (actorRef, actorRef.underlyingActor)
  }

  "Altimeter" should {
    "record rate of climb changes" in {
      val (_, real) = actor()
      real.receive(RateChange(1f))
      real.rateOfClimb must be (real.maxRateOfClimb)
    }

    "keep rate of climb change within bounds" in {
      val (_, real) = actor()
      real.receive(RateChange(2f))
      real.rateOfClimb must be (real.maxRateOfClimb)
    }

    "calculate altitude changes" in {

      // val (_, real) = actor()
      // 这里不能通过 actor() 方法来取 Actor;
      // 这是因为,我们这里是要测试 altitude 是否改变了,它是通过 sendEvent(AltitudeUpdate()) 事件来的;
      // 而 actor() 方法中,我们用 EventSourceSpy 替代了真实的 EventSource,它并不会真正发出 AltitudeUpdate 事件.

      // 因此,我们这里需要用真正的 Altimeter(混入 ProductionEventSource ).
      // 使用 system.actorOf(Props(Altimeter())) 创建.

      val real = system.actorOf(Props(Altimeter()))
      real ! RegisterListener(testActor)
      real ! RateChange(1f)

      // fishForMessage:
      // 如果偏函数的执行结果为false,则反复执行,直到超时为止;
      // - 如果这整个过程,有一次返回true,则测试通过;
      // - 如果直到超过,都一直没有true返回,则测试不通过.
      fishForMessage() {
        case AltitudeUpdate(altitude) if altitude == 0f => false
        case AltitudeUpdate(altitude) => true
      }
    }

    "send events" in {
      val (ref, real) = actor()

      // 如果这里暂停1秒, Altimeter内部会200ms发一个Tick事件,进而调一次 sendEvent 接口.
      // Thread.sleep(1000)

      // EventSourceSpy 使用一个 CountDownLatch 限制并发线程等待,
      // 一旦有 sendEvent 调用, CountDownLatch 给线程放行.
      // 因此, 下面的语句 await 等待1秒,必然保证返回值为 true; 如果不为true,则测试不通过.
      EventSourceSpy.latch.await(1, TimeUnit.SECONDS) must be (true)
    }
  }
}
