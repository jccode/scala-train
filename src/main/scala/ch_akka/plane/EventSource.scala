package ch_akka.plane

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef}

/**
  * EventSource
  *
  * @author 01372461
  */
trait EventSource {
  def sendEvent[T](event: T)
  def eventSourceReceive: Receive
}

trait ProductionEventSource extends EventSource { this: Actor =>
  import EventSource._

  var listeners = Vector.empty[ActorRef]

  def sendEvent[T](event: T) = listeners foreach { _ ! event }

  def eventSourceReceive: Receive = {
    case RegisterListener(listener) =>
      listeners = listeners :+ listener

    case UnregisterListener(listener) =>
      listeners.filter { _ != listener}
  }
}

object EventSource {

  case class RegisterListener(listener: ActorRef)

  case class UnregisterListener(listener: ActorRef)
}
