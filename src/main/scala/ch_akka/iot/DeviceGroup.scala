package ch_akka.iot

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

class DeviceGroup(groupId: String) extends Actor with ActorLogging {
  var deviceIdToActor = Map.empty[String, ActorRef]

  override def preStart(): Unit = log.info("DeviceGroup {} started", groupId)

  override def postStop(): Unit = log.info("DeviceGroup {} stopped", groupId)

  override def receive = {
    case trackMsg @ DeviceManager.RequestTrackDevice(`groupId`, _) =>
      val deviceActor: ActorRef = deviceIdToActor.getOrElse(trackMsg.deviceId, {
        val deviceActor = context.actorOf(Device.props(trackMsg.groupId, trackMsg.deviceId), s"device-${trackMsg.deviceId}")
        deviceIdToActor += trackMsg.deviceId -> deviceActor
        deviceActor
      })
      deviceActor forward trackMsg
    case DeviceManager.RequestTrackDevice(groupId, _) =>
      log.warning("Ignoring TrackDevice request for {}. This actor is responsible for {}.", groupId, this.groupId)
  }
}

object DeviceGroup {
  def props(groupId: String): Props = {
    Props(new DeviceGroup(groupId))
  }
}
