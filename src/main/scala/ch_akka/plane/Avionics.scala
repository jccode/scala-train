package ch_akka.plane

import akka.actor.{ActorRef, ActorSystem, Props}

import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
import Plane.GiveMeControl
import ch_akka.plane.ControlSurfaces.StickBack

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Avionics
  *
  * @author 01372461
  */
object Avionics extends App {

  implicit val timeout = Timeout(5.seconds)
  val system = ActorSystem("PlaneSimulation")
  val plane = system.actorOf(Props(Plane()), "Plane")

  // Grab the controls
  val controls = Await.result((plane ? GiveMeControl).mapTo[Plane.Control], 5.seconds).controlSurfaces

  // Takeoff
  system.scheduler.scheduleOnce(200.millis) {
    controls ! StickBack(1f)
  }
  // Leave out
  system.scheduler.scheduleOnce(1.second) { controls ! StickBack(0f) }
  // Climb
  system.scheduler.scheduleOnce(3.second) { controls ! StickBack(0.5f) }
  // Leave out
  system.scheduler.scheduleOnce(4.second) { controls ! StickBack(0f) }
  // Shutdown
  system.scheduler.scheduleOnce(5.second) { system.terminate() }

}
