package learning_akka.ch01

import akka.actor.Actor
import akka.event.Logging

import scala.collection.mutable


case class SetRequest(key: String, value: Object)

class AkkademyDb extends Actor {
  val map = new mutable.HashMap[String, Object]()
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case SetRequest(key, value) => {
      log.info("receive SetRequest - key: {} value: {}", key, value)
      map.put(key, value)
    }
    case o => log.info("received unknown message: {}", o);
  }
}