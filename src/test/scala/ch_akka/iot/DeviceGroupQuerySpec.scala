package ch_akka.iot

import akka.actor.{ActorSystem, PoisonPill}
import akka.testkit.{TestKit, TestProbe}
import ch_akka.iot.Device.{ReadTemperature, RespondTemperature}
import ch_akka.iot.DeviceGroup.{DeviceNotAvailable, Temperature, TemperatureNotAvailable}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

import scala.concurrent.duration._

class DeviceGroupQuerySpec(_system: ActorSystem) extends TestKit(_system)
  with FlatSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("DeviceGroupQuerySpec"))

  override protected def afterAll(): Unit = shutdown(system)

  it should "return temperature value for working device" in {
    val requester = TestProbe()
    val device1 = TestProbe()
    val device2 = TestProbe()

    val queryActor = system.actorOf(DeviceGroupQuery.props(
      actorToDeviceId = Map(device1.ref -> "device1", device2.ref -> "device2"),
      requestId = 1,
      requester = requester.ref,
      timeout = 3 seconds
    ))

    device1.expectMsg(ReadTemperature(requestId = 1))
    device2.expectMsg(ReadTemperature(requestId = 1))

    queryActor.tell(RespondTemperature(requestId = 1, Some(1.0)), device1.ref)
    queryActor.tell(RespondTemperature(requestId = 1, Some(2.0)), device2.ref)

    requester.expectMsg(DeviceGroup.ResponseAllTemperature(
      requestId = 1,
      temperatures = Map("device1" -> Temperature(1.0), "device2" -> Temperature(2.0))
    ))
  }

  it should "return TemperatureNotAvailable for devices with no readings" in {
    val requester = TestProbe()
    val device1 = TestProbe()
    val device2 = TestProbe()

    val queryActor = system.actorOf(DeviceGroupQuery.props(
      actorToDeviceId = Map(device1.ref -> "device1", device2.ref -> "device2"),
      requestId = 1,
      requester = requester.ref,
      timeout = 3 seconds
    ))

    device1.expectMsg(ReadTemperature(requestId = 1))
    device2.expectMsg(ReadTemperature(requestId = 1))

    queryActor.tell(RespondTemperature(requestId = 1, None), device1.ref)
    queryActor.tell(RespondTemperature(requestId = 1, Some(2.0)), device2.ref)

    requester.expectMsg(DeviceGroup.ResponseAllTemperature(
      requestId = 1,
      temperatures = Map("device1" -> TemperatureNotAvailable, "device2" -> Temperature(2.0))
    ))
  }

  it should "return DeviceNotAvailable if device stops before answering" in {
    val requester = TestProbe()
    val device1 = TestProbe()
    val device2 = TestProbe()

    val queryActor = system.actorOf(DeviceGroupQuery.props(
      actorToDeviceId = Map(device1.ref -> "device1", device2.ref -> "device2"),
      requestId = 1,
      requester = requester.ref,
      timeout = 3 seconds
    ))

    device1.expectMsg(ReadTemperature(requestId = 1))
    device2.expectMsg(ReadTemperature(requestId = 1))

    queryActor.tell(RespondTemperature(requestId = 1, Some(1.0)), device1.ref)
    device2.ref ! PoisonPill

    requester.expectMsg(DeviceGroup.ResponseAllTemperature(
      requestId = 1,
      temperatures = Map("device1" -> Temperature(1.0), "device2" -> DeviceNotAvailable)
    ))
  }

  it should "return temperature reading even if device stops after answering" in {
    val requester = TestProbe()
    val device1 = TestProbe()
    val device2 = TestProbe()

    val queryActor = system.actorOf(DeviceGroupQuery.props(
      actorToDeviceId = Map(device1.ref -> "device1", device2.ref -> "device2"),
      requestId = 1,
      requester = requester.ref,
      timeout = 3 seconds
    ))

    device1.expectMsg(ReadTemperature(requestId = 1))
    device2.expectMsg(ReadTemperature(requestId = 1))

    queryActor.tell(RespondTemperature(requestId = 1, Some(1.0)), device1.ref)
    queryActor.tell(RespondTemperature(requestId = 1, Some(2.0)), device2.ref)
    device2.ref ! PoisonPill

    requester.expectMsg(DeviceGroup.ResponseAllTemperature(
      requestId = 1,
      temperatures = Map("device1" -> Temperature(1.0), "device2" -> Temperature(2.0))
    ))
  }
}
