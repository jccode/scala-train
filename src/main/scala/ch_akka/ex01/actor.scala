package ch_akka.ex01

import akka.actor.{Actor, ActorLogging, ActorRef}
import ch_akka.ex01.StudentProtocol.{InitSignal, QuoteRequest}
import ch_akka.ex01.TeacherProtocol.QuoteResponse

import scala.util.Random


class TeacherActor extends Actor with ActorLogging {
  val quotes = List(
    "Moderation is for cowards",
    "Anything worth doing is worth overdoing",
    "The trouble is you think you have time",
    "You never gonna know if you never even try"
  )
  override def receive: Receive = {
    case QuoteRequest => {
      val quoteResponse = QuoteResponse(quotes(Random.nextInt(quotes.size)))
      log.info(quoteResponse.toString)
      sender() ! quoteResponse
    }
  }
}

class StudentActor(teacherRef: ActorRef) extends Actor with ActorLogging {
  override def receive: Actor.Receive = {
    case InitSignal => {
      log.info("receive init signal")
      teacherRef ! QuoteRequest
    }

    case QuoteResponse(quoteString) => {
      log.info("Received QuoteResponse from Teacher")
      log.info(s"Printing from Student Actor $quoteString")
    }
  }
}