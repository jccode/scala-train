package ch_akka.plane

import akka.actor.{Actor, ActorLogging, Props}

/**
  * Plane
  *
  * @author 01372461
  */
class Plane extends Actor with ActorLogging {
  import Plane._

  val altimeter = context.actorOf(Props[Altimeter])
  val controls = context.actorOf(Props(new ControlSurfaces(altimeter)))

  override def receive: Receive = {
    case GiveMeControl =>
      log.info("Plane giving control")
      sender() ! controls
  }
}

object Plane {
  case object GiveMeControl
}
