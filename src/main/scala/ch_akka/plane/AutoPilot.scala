package ch_akka.plane

import akka.actor.{Actor, ActorRef}
import ch_akka.plane.Pilot.{ReadyToGo, RelinquishControl}
import ch_akka.plane.Plane.Control

class AutoPilot extends Actor {

  var controls: ActorRef = context.system.deadLetters

  override def receive: Receive = {
    case RelinquishControl =>
      context.parent ! Plane.GiveMeControl

    case Control(controlSurfaces) =>
      controls = controlSurfaces

    case ReadyToGo =>
      controls = context.system.deadLetters
  }
}
