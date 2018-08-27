package learning_akka_videos.section3

import akka.actor.Actor

class Worker extends Actor {
  import Worker._

  override def receive: Receive = {
    case msg: Work =>
      println(s"I received Work Message and My ActorRef is: $self")
  }
}

object Worker {
  case class Work()
}
