package ch_akka.iot

import akka.actor.{Actor, ActorLogging}

class DeviceManager extends Actor with ActorLogging {
  override def receive = Actor.emptyBehavior
}

object DeviceManager {

  final case class RequestTrackDevice(groupId: String, deviceId: String)
  case object DeviceRegistered
}
