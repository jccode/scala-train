package ch_akka

import akka.actor.{ActorRef, Props, ActorSystem}
import ch_akka.protocol.StudentProtocol.InitSignal

object DriverApp extends App {
  val system = ActorSystem("UniversityMessageSystem")
  val teacherRef = system.actorOf(Props[TeacherActor], "teacherActor")
  val studentRef = system.actorOf(Props(new StudentActor(teacherRef)), "studentActor")
  studentRef ! InitSignal
  Thread.sleep(2000)
  system.shutdown()
}

