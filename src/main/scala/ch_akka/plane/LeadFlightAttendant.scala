package ch_akka.plane

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.util.Random


trait AttendantCreationPolicy {
  val numberOfAttendants: Int = 8
  def createAttendant: Actor = FlightAttendant()
}

object LeadFlightAttendant {

  // request msg
  case object GetAttendant

  // response msg
  case class Attendant(a: ActorRef)

  def apply: LeadFlightAttendant = new LeadFlightAttendant() with AttendantCreationPolicy
}

class LeadFlightAttendant extends Actor { this: AttendantCreationPolicy =>

  import LeadFlightAttendant._

  // create attendants
  override def preStart(): Unit = {
    import scala.collection.JavaConverters._
    val attendantNames = context.system.settings.config.getStringList("plane.avionics.flightcrew.attendantNames").asScala
    attendantNames take numberOfAttendants foreach { name =>
      context.actorOf(Props(createAttendant), name)
    }
  }

  def randomAttendant(): ActorRef = {
    context.children.take(Random.nextInt(numberOfAttendants) + 1).last
  }

  override def receive: Receive = {
    case GetAttendant =>
      sender() ! Attendant(randomAttendant())
    case m =>
      randomAttendant() forward m
  }
}


object LeadFlightAttendantPathChecker extends App {
  val system = ActorSystem("FlightAttendantPathChecker")
  val lead = system.actorOf(Props(new LeadFlightAttendant with AttendantCreationPolicy), "LeadFlightAttendant")
  Thread.sleep(2000)
  system.terminate()
}