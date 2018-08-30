package learning_akka_videos.section3

import akka.actor.Actor
import learning_akka_videos.Worker.Work

import scala.util.Random

class RouterGroup(routees: List[String]) extends Actor {


  override def receive: Receive = {
    case msg: Work =>
      println("I'm a Router Group and I received a message ....")
      context.actorSelection(routees(Random.nextInt(routees.size))) forward msg
  }
}