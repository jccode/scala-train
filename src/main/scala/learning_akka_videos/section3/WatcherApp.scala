package learning_akka_videos.section3

import akka.actor.{ActorSystem, Props}
import learning_akka_videos.Counter

object WatcherApp extends App {

  val system = ActorSystem("Watch-actor-selection")

  val counter = system.actorOf(Props[Counter], "counter")

  val watcher = system.actorOf(Props[Watcher], "watcher")

  Thread.sleep(100)

  system.terminate()
}
