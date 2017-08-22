package ch_akka.iot

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import ch_akka.iot.DeviceGroup.{ReplyDeviceList, RequestDeviceList}

class DeviceGroup(groupId: String) extends Actor with ActorLogging {
  var deviceIdToActor = Map.empty[String, ActorRef]
  var actorToDeviceId = Map.empty[ActorRef, String]

  override def preStart(): Unit = log.info("DeviceGroup {} started", groupId)

  override def postStop(): Unit = log.info("DeviceGroup {} stopped", groupId)

  override def receive = {
    case trackMsg @ DeviceManager.RequestTrackDevice(`groupId`, _) =>
      val deviceActor: ActorRef = deviceIdToActor.getOrElse(trackMsg.deviceId, {
        val deviceActor = context.actorOf(Device.props(trackMsg.groupId, trackMsg.deviceId), s"device-${trackMsg.deviceId}")
        context.watch(deviceActor)
        deviceIdToActor += trackMsg.deviceId -> deviceActor
        actorToDeviceId += deviceActor -> trackMsg.deviceId
        deviceActor
      })
      deviceActor forward trackMsg

    case DeviceManager.RequestTrackDevice(groupId, _) =>
      log.warning("Ignoring TrackDevice request for {}. This actor is responsible for {}.", groupId, this.groupId)

    case RequestDeviceList(requestId) =>
      sender() ! ReplyDeviceList(requestId, deviceIdToActor.keySet)

    case Terminated(deviceActor) =>
      val deviceId = actorToDeviceId(deviceActor)
      log.info("Device actor for {} has been terminated", deviceId)
      actorToDeviceId -= deviceActor
      deviceIdToActor -= deviceId
  }
}

object DeviceGroup {
  def props(groupId: String): Props = Props(new DeviceGroup(groupId))

  final case class RequestDeviceList(requestId: Long)
  final case class ReplyDeviceList(requestId: Long, ids: Set[String])

  final case class RequestAllTemperatures(requestId: Long)
  final case class ResponseAllTemperature(requestId: Long, temperatures: Map[String, TemperatureReading])

  sealed trait TemperatureReading
  final case class Temperature(value: Double) extends TemperatureReading
  case object TemperatureNotAvailable extends TemperatureReading
  case object DeviceNotAvailable extends TemperatureReading
  case object DeviceTimedout extends TemperatureReading
}
