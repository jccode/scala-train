package learning_akka_videos.section3

import akka.actor.{ActorSystem, Props}
import learning_akka_videos.section3.Worker.Work

object RouterGroupApp extends App {

  val system = ActorSystem("router-group")

  system.actorOf(Props[Worker], "w1")
  system.actorOf(Props[Worker], "w2")
  system.actorOf(Props[Worker], "w3")
  system.actorOf(Props[Worker], "w4")
  system.actorOf(Props[Worker], "w5")

  val works: List[String] = List(
    "/user/w1",
    "/user/w2",
    "/user/w3",
    "/user/w4",
    "/user/w5",
  )

  val routerGroup = system.actorOf(Props(classOf[RouterGroup], works), "group")

  routerGroup ! Work()
  routerGroup ! Work()
  routerGroup ! Work()

  Thread.sleep(100)

  system.terminate()
}
