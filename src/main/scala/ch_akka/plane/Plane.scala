package ch_akka.plane

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import ch_akka.plane.Altimeter.AltitudeUpdate

/**
  * Plane
  *
  * @author 01372461
  */
class Plane extends Actor with ActorLogging {
  import Plane._
  import EventSource._

  val altimeter = context.actorOf(Props(Altimeter()))
  val controls = context.actorOf(Props(new ControlSurfaces(altimeter)))

  val config = context.system.settings.config
  val pilot = context.actorOf(Props[Pilot], config.getString("plane.avionics.flightcrew.pilotName"))
  val copilot = context.actorOf(Props[CoPilot], config.getString("plane.avionics.flightcrew.copilotName"))
  val autopilot = context.actorOf(Props[AutoPilot], "AutoPilot")
  val flightAttendant = context.actorOf(Props(LeadFlightAttendant()), config.getString("plane.avionics.flightcrew.leadAttendantName"))

  override def receive: Receive = {
    case GiveMeControl =>
      log.info("Plane giving control")
      sender() ! Control(controls)

    case AltitudeUpdate(altitude) =>
      log.info(s"Altitude is now: $altitude")
  }

  override def preStart(): Unit = {
    altimeter ! RegisterListener(self)
    List(pilot, copilot) foreach { _ ! Pilot.ReadyToGo }
  }
}

object Plane {
  case object GiveMeControl
  case class Control(controlSurfaces: ActorRef)
}
