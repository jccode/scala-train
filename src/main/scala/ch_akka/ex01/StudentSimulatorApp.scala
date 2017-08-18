package ch_akka.ex01

import akka.actor._
import ch_akka.ex01.StudentProtocol.QuoteRequest

/**
 * Created by jcchen on 15-9-11.
 */
object StudentSimulatorApp {

  def main(args: Array[String]) {
    val actorSystem = ActorSystem("UniversityMessageSystem")
    val teacherActorRef:ActorRef = actorSystem.actorOf(Props[TeacherActor])
    teacherActorRef ! QuoteRequest
    Thread.sleep(2000)
    actorSystem.shutdown()
  }

}
