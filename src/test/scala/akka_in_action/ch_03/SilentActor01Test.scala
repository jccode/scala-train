package akka_in_action.ch_03

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit}
import akka_in_action.StopSystemAfterAll
import org.scalatest.{MustMatchers, WordSpecLike}

class SilentActor01Test extends TestKit(ActorSystem("testSystem"))
  with WordSpecLike with MustMatchers with StopSystemAfterAll {

  import SilentActor._

  "A silent actor" must {
    "change state when it receives a message, single thread" in {
      val silentActor = TestActorRef[SilentActor]
      silentActor ! SilentMessage("whisper")
      silentActor.underlyingActor.internalState must(contain("whisper"))
    }

    "change state when it receives a message, multi thread" in {
      val silentActor = system.actorOf(Props[SilentActor])
      silentActor ! SilentMessage("whisper1")
      silentActor ! SilentMessage("whisper2")
      silentActor ! GetState(testActor)
      expectMsg(Vector("whisper1", "whisper2"))

    }
  }



}
