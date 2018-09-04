package ch_akka.plane

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import ch_akka.plane.Altimeter.AltitudeUpdate
import ch_akka.plane.IsolatedLifeCycleSupervisor.WaitForStart

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Plane
  *
  * @author 01372461
  */
class Plane extends Actor with ActorLogging {
  this: AltimeterProvider with PilotProvider with LeadFlightAttendantProvider =>

  import Plane._
  import EventSource._

  val config = context.system.settings.config
  private val pilotName: String = config.getString("plane.avionics.flightcrew.pilotName")
  private val copilotName: String = config.getString("plane.avionics.flightcrew.copilotName")
  private val leadAttendantName: String = config.getString("plane.avionics.flightcrew.leadAttendantName")

  val altimeter = context.actorOf(Props(newAltimeter))
  val controls = context.actorOf(Props(new ControlSurfaces(altimeter)))

  val pilot = context.actorOf(Props(newPilot), pilotName)
  val copilot = context.actorOf(Props(newCoPilot), copilotName)
  val autopilot = context.actorOf(Props(newAutoPilot), "AutoPilot")
  val flightAttendant = context.actorOf(Props(newLeadFlightAttendant), leadAttendantName)

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



  implicit val timeout: Timeout = 5 seconds

  def startControls(): Unit = {
    val controls = context.actorOf(Props(new IsolatedResumeSupervisor with OneForOneStrategyFactory {
      override def childStarter(): Unit = {
        val alt = context.actorOf(Props(newAltimeter), "Altimeter")
        context.actorOf(Props(newAutoPilot), "AutoPilot")
        context.actorOf(Props(new ControlSurfaces(alt)), "ControlSurfaces")
      }
    }), "Controls")
    Await.result(controls ? WaitForStart, 1.second)
  }

  def startPeople(): Unit = {
    val people = context.actorOf(Props(new IsolatedStopSupervisor with OneForOneStrategyFactory {
      override def childStarter(): Unit = {
        context.actorOf(Props(newPilot), pilotName)
        context.actorOf(Props(newCoPilot), copilotName)
      }
    }), "Pilots")
    context.actorOf(Props(newLeadFlightAttendant), leadAttendantName)
    Await.result(people ? WaitForStart, 1.second)
  }
}

object Plane {
  case object GiveMeControl
  case class Control(controlSurfaces: ActorRef)

  def apply(): Plane = new Plane with AltimeterProvider with PilotProvider with LeadFlightAttendantProvider
}
