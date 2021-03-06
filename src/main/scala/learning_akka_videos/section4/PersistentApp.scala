package learning_akka_videos.section4

import akka.actor.{ActorSystem, Props}

object PersistentApp extends App {
  import Counter._

  val system = ActorSystem("persistent-actors")

  val counter = system.actorOf(Props[Counter], "counter")

  counter ! Cmd(Increment(3))
  counter ! Cmd(Increment(5))
  counter ! Cmd(Decrement(3))
  counter ! "print"

  Thread.sleep(1000)

  system.terminate()
}
