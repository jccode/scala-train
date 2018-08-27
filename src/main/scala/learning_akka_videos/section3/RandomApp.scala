package learning_akka_videos.section3

import akka.actor.{ActorSystem, Props}
import akka.routing.FromConfig
import learning_akka_videos.section3.Worker.Work

object RandomApp extends App {

  val system = ActorSystem("Random-Router")

  val routerPool = system.actorOf(FromConfig.props(Props[Worker]), "random-router-pool")

  routerPool ! Work()
  routerPool ! Work()
  routerPool ! Work()
  routerPool ! Work()

  Thread.sleep(100)

  system.terminate()
}
