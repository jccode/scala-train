package ch_akka.plane

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.{MustMatchers, WordSpecLike}

object TestFlightAttendant {
  def apply(): FlightAttendant = new FlightAttendant() with AttendantResponsiveness {
    val maxResponseTimeMS: Int = 1
  }
}

class FlightAttendantSpec extends TestKit(ActorSystem("FlightAttendantSpec", ConfigFactory.parseString("akka.scheduler.tick-duration = 10ms")))
  with MustMatchers with WordSpecLike with ImplicitSender {

  import FlightAttendant._

  "FlightAttendant" should {
    "get a drink when ask" in {
      val actorRef = TestActorRef(Props(TestFlightAttendant()))
      actorRef ! GetDrink("Soda")
      expectMsg(Drink("Soda"))
    }
  }
}
