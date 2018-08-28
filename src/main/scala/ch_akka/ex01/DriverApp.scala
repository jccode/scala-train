package ch_akka.ex01

import akka.actor.{ActorSystem, Props}
import ch_akka.ex01.StudentProtocol.InitSignal

object DriverApp extends App {
  val system = ActorSystem("UniversityMessageSystem")
  val teacherRef = system.actorOf(Props[TeacherActor], "teacherActor")
  val studentRef = system.actorOf(Props(new StudentActor(teacherRef)), "studentActor")
  studentRef ! InitSignal
  Thread.sleep(2000)
  system.terminate()
}

