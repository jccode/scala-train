package ch_akka.plane

import java.util.concurrent.CountDownLatch

import akka.actor.Actor.Receive

trait EventSourceSpy extends EventSource {

  override def sendEvent[T](event: T): Unit = {
    println("send event")
    EventSourceSpy.latch.countDown()
  }

  // We don't care about processing the messages that EventSource usually
  // processes so we simply don't worry about them.
  override def eventSourceReceive: Receive = {
    case "" =>
  }
}

object EventSourceSpy {
  val latch = new CountDownLatch(1)
}
