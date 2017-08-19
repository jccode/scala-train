package ch_akka.iot

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestProbe}
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.duration._

class DeviceSpec extends FunSpecLike with Matchers {

  implicit val system = ActorSystem()
  implicit val timeout = Timeout(5 seconds)

  describe("Device Actor") {
    it("should reply with latest temperature reading") {
      val probe = TestProbe()
      val deviceActor = TestActorRef(Device.props("group", "device"))
      // val deviceActor = system.actorOf(Device.props("group", "device"))

      deviceActor.tell(Device.RecordTemperature(requestId = 1, 24.0), probe.ref)
      probe.expectMsg(Device.TemperatureRecorded(requestId = 1))

      deviceActor.tell(Device.ReadTemperature(requestId = 2), probe.ref)
      val response1 = probe.expectMsgType[Device.RespondTemperature]
      response1.requestId should equal(2)
      response1.value should equal(Some(24.0))

      deviceActor.tell(Device.RecordTemperature(requestId = 3, 55.0), probe.ref)
      probe.expectMsg(Device.TemperatureRecorded(requestId = 3))

      deviceActor.tell(Device.ReadTemperature(requestId = 3), probe.ref)
      val response2 = probe.expectMsgType[Device.RespondTemperature]
      response2.requestId should equal(3)
      response2.value should equal(Some(55.0))
    }
  }
}
