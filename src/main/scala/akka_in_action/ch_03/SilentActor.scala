package akka_in_action.ch_03

import akka.actor.{Actor, ActorRef}
import akka_in_action.ch_03.SilentActor.{GetState, SilentMessage}

class SilentActor extends Actor {
  var internalState = Vector[String]()

  override def receive: Receive = {
    case SilentMessage(data) =>
      internalState = internalState :+ data
    case GetState(sender) =>
      sender ! internalState
    case msg =>
  }
}

object SilentActor {
  case class SilentMessage(data: String)
  case class GetState(actorRef: ActorRef)
}
