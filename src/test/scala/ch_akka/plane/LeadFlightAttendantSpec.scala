package ch_akka.plane

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import akka.util.Timeout
import ch_akka.plane.FlightAttendant.{Drink, GetDrink}
import com.typesafe.config.ConfigFactory
import org.scalatest.{MustMatchers, WordSpecLike}

import scala.concurrent.duration._

class LeadFlightAttendantSpec extends TestKit(ActorSystem("LeadFlightAttendantSpec",
  ConfigFactory.parseString("""plane.avionics.flightcrew.attendantNames = ["Sally","Jimmy"]""")))
  with WordSpecLike with MustMatchers with ImplicitSender {

  import LeadFlightAttendant._

  def leadFlightAttendant = new LeadFlightAttendant with AttendantCreationPolicy {
    override val numberOfAttendants: Int = 2
    override def createAttendant: Actor = TestFlightAttendant()
  }

  implicit val timeout: Timeout = 5 seconds

  "LeadFlightAttendant" should {

    "get a random flight attendant" in {
      val a = TestActorRef(Props(leadFlightAttendant))
      a ! GetAttendant
      expectMsgPF() {
        case Attendant(ref) if List("Sally", "Jimmy") contains ref.path.name => ()
      }
    }

    "forward the message to flight attendant" in {
      val a = TestActorRef(Props(leadFlightAttendant))
      a ! GetDrink("Soda")
      expectMsg(Drink("Soda"))
    }

  }

}
