package ch_akka.iot

import akka.actor.{Actor, ActorLogging, ActorRef, Terminated}
import ch_akka.iot.DeviceManager.{ReplyGroupList, RequestGroupList, RequestTrackDevice}

class DeviceManager extends Actor with ActorLogging {
  var groupIdToActor = Map.empty[String, ActorRef]
  var actorToGroupId = Map.empty[ActorRef, String]

  override def preStart(): Unit = log.info("DeviceManager started")

  override def postStop(): Unit = log.info("DeviceManager stopped")

  override def receive = {
    case trackMsg @ RequestTrackDevice(groupId, _) =>
      groupIdToActor.get(groupId) match {
        case Some(groupActor) =>
          groupActor forward trackMsg
        case None =>
          log.info("Creating device group actor for {}", groupId)
          val groupActor = context.actorOf(DeviceGroup.props(groupId), s"group-$groupId")
          context.watch(groupActor)
          groupIdToActor += groupId -> groupActor
          actorToGroupId += groupActor -> groupId
          groupActor forward trackMsg
      }

    case RequestGroupList(requestId) =>
      sender() ! ReplyGroupList(requestId, groupIdToActor.keySet)

    case Terminated(groupActor) =>
      val groupId = actorToGroupId(groupActor)
      log.info("Device group actor for {} has been terminated", groupId)
      groupIdToActor -= groupId
      actorToGroupId -= groupActor
  }
}

object DeviceManager {
  final case class RequestTrackDevice(groupId: String, deviceId: String)
  case object DeviceRegistered
  final case class RequestGroupList(requestId: Long)
  final case class ReplyGroupList(requestId: Long, ids: Set[String])
}
