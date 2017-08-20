package ch_akka.iot

import akka.actor.{ActorSystem, PoisonPill}
import akka.testkit.{TestActorRef, TestKit, TestProbe}
import ch_akka.iot.Device.{RecordTemperature, TemperatureRecorded}
import ch_akka.iot.DeviceManager.{DeviceRegistered, ReplyGroupList, RequestGroupList, RequestTrackDevice}
import org.scalatest._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class DeviceManagerSpec(_system: ActorSystem) extends TestKit(_system) with FlatSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("DeviceManagerSpec"))

  override protected def afterAll(): Unit = shutdown(system)


  it should "be able to register a device actor" in {
    val probe = TestProbe()
    val managerActor = TestActorRef[DeviceManager]

    managerActor.tell(RequestTrackDevice("group", "device1"), probe.ref)
    probe.expectMsg(DeviceRegistered)
    val deviceActor1 = probe.lastSender

    managerActor.tell(RequestTrackDevice("group", "device2"), probe.ref)
    probe.expectMsg(DeviceRegistered)
    val deviceActor2 = probe.lastSender
    deviceActor1 should not equal deviceActor2

    // Check that the device actor are working
    deviceActor1.tell(RecordTemperature(requestId = 0, 1.0), probe.ref)
    probe.expectMsg(TemperatureRecorded(requestId = 0))
    deviceActor2.tell(RecordTemperature(requestId = 1, 2.0), probe.ref)
    probe.expectMsg(TemperatureRecorded(requestId = 1))
  }

  it should "return same device group actor for same groupId" in {
    val probe = TestProbe()
    val managerActor = TestActorRef[DeviceManager]

    managerActor.tell(RequestTrackDevice("group", "device1"), probe.ref)
    probe.expectMsg(DeviceRegistered)
    val deviceActor1 = probe.lastSender

    managerActor.tell(RequestTrackDevice("group", "device1"), probe.ref)
    probe.expectMsg(DeviceRegistered)
    val deviceActor2 = probe.lastSender

    deviceActor1 should equal(deviceActor2)
  }

  it should "be able to list active groups" in {
    val probe = TestProbe()
    val managerActor = TestActorRef[DeviceManager]

    managerActor.tell(RequestTrackDevice("group1", "device1"), probe.ref)
    probe.expectMsg(DeviceRegistered)

    managerActor.tell(RequestTrackDevice("group2", "device2"), probe.ref)
    probe.expectMsg(DeviceRegistered)

    managerActor.tell(RequestGroupList(requestId = 0), probe.ref)
    probe.expectMsg(ReplyGroupList(requestId = 0, Set("group1", "group2")))
  }

  it should "be able to list active groups after one shuts down" in {
    val probe = TestProbe()
    val managerActor = TestActorRef[DeviceManager]

    managerActor.tell(RequestTrackDevice("group1", "device1"), probe.ref)
    probe.expectMsg(DeviceRegistered)
    val deviceActor1 = probe.lastSender

    managerActor.tell(RequestTrackDevice("group2", "device2"), probe.ref)
    probe.expectMsg(DeviceRegistered)

    managerActor.tell(RequestGroupList(requestId = 0), probe.ref)
    val response = probe.expectMsgType[ReplyGroupList]
    response.requestId should equal(0)
    response.ids should equal(Set("group1", "group2"))

    val groupPath = deviceActor1.path.parent

    system.actorSelection(groupPath).resolveOne(5 seconds).map {
      group1ActorRef => {
        probe.watch(group1ActorRef)
        group1ActorRef ! PoisonPill
        probe.expectTerminated(group1ActorRef)

        probe.awaitAssert {
          managerActor.tell(RequestGroupList(requestId = 1), probe.ref)
          probe.expectMsg(ReplyGroupList(requestId = 1, Set("group2")))
        }
      }
    }

  }


}
