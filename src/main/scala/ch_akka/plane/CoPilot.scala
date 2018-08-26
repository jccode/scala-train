package ch_akka.plane

import akka.actor.{Actor, ActorIdentity, ActorRef, Identify}
import ch_akka.plane.Pilot.{ReadyToGo, RelinquishControl}
import ch_akka.plane.Plane.Control

class CoPilot extends Actor {

  var controls: ActorRef = context.system.deadLetters
  var pilot: ActorRef = context.system.deadLetters
  var autopilot: ActorRef = context.system.deadLetters
  val pilotName: String = context.system.settings.config.getString("plane.avionics.flightcrew.pilotName")

  override def receive: Receive = {
    case ReadyToGo =>
      context.actorSelection(s"../$pilotName") ! Identify("pilot")
      context.actorSelection("../AutoPilot") ! Identify("autopilot")

    case ActorIdentity(id, Some(ref)) =>
      id match {
        case "pilot" => pilot = ref
        case "autopilot" => autopilot = ref
      }

    case Control(controlSurfaces) =>
      controls = controlSurfaces

    case RelinquishControl =>
      controls = context.system.deadLetters
      pilot = context.system.deadLetters
      autopilot = context.system.deadLetters
  }
}
