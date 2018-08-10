package ch_akka.plane

import akka.actor.{Actor, ActorLogging, ActorRef}

/**
  * ControlSurfaces
  *
  * @author 01372461
  */
class ControlSurfaces(altimeter: ActorRef) extends Actor with ActorLogging {
  import ControlSurfaces._
  import Altimeter._

  override def receive: Receive = {
    // Pilot pulled the stick back by a certain amount, and we inform
    // the Altimeter that we're climbing
    case StickBack(amount) =>
      altimeter ! RateChange(amount)

    // Pilot pushes the stick forward and we inform the Altimeter that
    // we're descending
    case StickForward(amount) =>
      altimeter ! RateChange(-1 * amount)
  }
}

object ControlSurfaces {

  // amount is a value between 1 and 1. The altimeter ensures that any
  // value outside that range is truncated to be within it.
  case class StickBack(amount: Float)
  case class StickForward(amount: Float)

}
