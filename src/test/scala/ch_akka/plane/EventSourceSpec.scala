package ch_akka.plane

import akka.actor.{Actor, ActorSystem}
import akka.testkit.{TestActorRef, TestKit}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}


class TestEventSource extends Actor with ProductionEventSource {
  override def receive: Receive = eventSourceReceive
}

class EventSourceSpec extends TestKit(ActorSystem("EventSourceSpec"))
  with WordSpecLike with MustMatchers with BeforeAndAfterAll {

  import EventSource._

  override protected def afterAll(): Unit = system.terminate()

  "EventSource" should {
    "allow us to register a listen" in {
      val actorRef = TestActorRef[TestEventSource]
      actorRef ! RegisterListener(testActor)
      actorRef.underlyingActor.listeners must contain (testActor)
    }

    "allow us to unregister a listener" in {
      val actor = TestActorRef[TestEventSource].underlyingActor
      actor.receive(RegisterListener(testActor))
      actor.listeners must contain (testActor)
      actor.receive(UnregisterListener(testActor))
      actor.listeners.size must be (0)
    }

    "send the event to our test actor" in {
      val actorRef = TestActorRef[TestEventSource]
      actorRef ! RegisterListener(testActor)
      actorRef.underlyingActor.sendEvent("Fibonacci")
      expectMsg("Fibonacci")
    }
  }
}
