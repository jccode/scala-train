package ch_akka.plane

import akka.actor.{Actor, ActorLogging, Props}
import ch_akka.plane.Altimeter.AltitudeUpdate

/**
  * Plane
  *
  * @author 01372461
  */
class Plane extends Actor with ActorLogging {
  import Plane._
  import EventSource._

  val altimeter = context.actorOf(Props[Altimeter])
  val controls = context.actorOf(Props(new ControlSurfaces(altimeter)))

  override def receive: Receive = {
    case GiveMeControl =>
      log.info("Plane giving control")
      sender() ! controls

    case AltitudeUpdate(altitude) =>
      log.info(s"Altitude is now: $altitude")
  }

  override def preStart(): Unit = altimeter ! RegisterListener(self)
}

object Plane {
  case object GiveMeControl
}
