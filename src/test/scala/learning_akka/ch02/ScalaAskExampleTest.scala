package learning_akka.ch02

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class ScalaAskExampleTest extends FunSpecLike with Matchers {

  val system = ActorSystem()
  implicit val timeout = Timeout(5 seconds)
  val pongActor = system.actorOf(Props(classOf[ScalaPongActor]))

  def askPong(message: String): Future[String] = (pongActor ? message).mapTo[String]

  describe("Pong Actor") {
    it("should respond with pong") {
      val future = pongActor ? "Ping"
      val result = Await.result(future.mapTo[String], 1 second)
      assert(result == "Pong")
    }

    it("should fail on unknown message") {
      val future = pongActor ? "unknown"
      intercept[Exception] {
        Await.result(future.mapTo[String], 1 second)
      }
    }
  }

  describe("Future example") {
    import scala.concurrent.ExecutionContext.Implicits.global

    it("should print to console") {
      (pongActor ? "Ping").onSuccess({
        case x: String => println("replied with: " + x)
      })
      Thread.sleep(100)

      askPong("unknown").onFailure({
        case e: Exception => println("Got exception")
      })

      val f = askPong("unknown").recover({
        case e: Exception => "default"
      })
      val fResult = Await.result(f.mapTo[String], 1 second)
      assert(fResult == "default")

    }

  }
}
