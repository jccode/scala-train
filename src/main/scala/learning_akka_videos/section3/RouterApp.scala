package learning_akka_videos.section3

import akka.actor.{ActorSystem, Props}
import learning_akka_videos.section3.Worker.Work

object RouterApp extends App {

  val system = ActorSystem("router")

  val router = system.actorOf(Props[Router])

  router ! Work()
  router ! Work()
  router ! Work()

  Thread.sleep(100)

  system.terminate()
}
