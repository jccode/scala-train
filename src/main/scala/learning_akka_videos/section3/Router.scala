package learning_akka_videos.section3

import akka.actor.{Actor, ActorRef, Props}
import learning_akka_videos.Worker

import scala.util.Random

class Router extends Actor {
  import Worker._

  var routees: List[ActorRef] = _

  override def preStart(): Unit = {
    routees = List.fill(5) {
      context.actorOf(Props[Worker])
    }
  }

  override def receive: Receive = {
    case msg: Work =>
      println("I'm a Router and I received a message ....")
      routees(Random.nextInt(routees.size)) forward msg
  }
}
