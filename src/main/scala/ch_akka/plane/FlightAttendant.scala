package ch_akka.plane

import akka.actor.Actor

import scala.util.Random
import scala.concurrent.duration._


trait AttendantResponsiveness {
  val maxResponseTimeMS: Int
  def responseDuration = Random.nextInt(maxResponseTimeMS).millis
}

object FlightAttendant {

  case class GetDrink(drinkname: String)
  case class Drink(drinkname: String)

  def apply(): FlightAttendant = new FlightAttendant() with AttendantResponsiveness {
    val maxResponseTimeMS = 5 * 60 * 1000 // 5 minutes
  }
}

class FlightAttendant extends Actor { this: AttendantResponsiveness =>
  import FlightAttendant._
  import scala.concurrent.ExecutionContext.Implicits.global

  override def receive: Receive = {
    case GetDrink(drinkname) =>
      // We don't respond right away, but use the scheduler to ensure we do eventually
      context.system.scheduler.scheduleOnce(responseDuration, sender, Drink(drinkname))
  }
}