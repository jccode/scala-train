package ch_actor

import scala.actors.Actor

/**
 * Created by jcchen on 15-9-9.
 */
class HiActor extends Actor {
  override def act(): Unit = {
    while (true) {
      receive {
        case "Hi" => println("Hello")
      }
    }
  }
}

object Main {
  def main(args: Array[String]) {
    val actor1 = new HiActor
    actor1.start()
    actor1 ! "Hi"
  }
}