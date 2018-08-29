package learning_akka_videos.section4

import akka.actor.{ActorSystem, Props}

/**
  * PersistentFSMApp
  *
  * @author 01372461
  */
object PersistentFSMApp extends App {
  import Account._

  val system = ActorSystem("persistent-fsm")

  val account = system.actorOf(Props[Account])

  account ! Operation(1000, CR)
  account ! Operation(100, DR)

  Thread.sleep(1000)

  system.terminate()
}
