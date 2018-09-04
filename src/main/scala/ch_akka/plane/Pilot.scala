package ch_akka.plane

import akka.actor.{Actor, ActorIdentity, ActorRef, Identify}
import ch_akka.plane.Pilot.{ReadyToGo, RelinquishControl}

object Pilot {
  case object ReadyToGo
  case object RelinquishControl
}


class Pilot(plane: ActorRef, autopilot: ActorRef) extends Actor {
  import Plane._

  var controls: ActorRef = context.system.deadLetters
  var copilot: ActorRef = context.system.deadLetters
//  var autopilot: ActorRef = context.system.deadLetters
  val copilotName: String = context.system.settings.config.getString("plane.avionics.flightcrew.copilotName")


  override def receive: Receive = {
    case ReadyToGo =>
      plane ! Plane.GiveMeControl
      context.actorSelection(s"../$copilotName") ! Identify("copilot")
//      context.actorSelection("../AutoPilot") ! Identify("autopilot")

    case ActorIdentity(id, Some(ref)) =>
      id match {
        case "copilot" => copilot = ref
//        case "autopilot" => autopilot = ref
      }

    case Control(controlSurfaces) =>
      controls = controlSurfaces

    case RelinquishControl =>
      controls = context.system.deadLetters
      copilot = context.system.deadLetters
//      autopilot = context.system.deadLetters
  }
}

