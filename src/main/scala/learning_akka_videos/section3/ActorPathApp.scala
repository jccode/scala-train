package learning_akka_videos.section3

import akka.actor.{ActorSystem, PoisonPill, Props}
import learning_akka_videos.Counter

object ActorPathApp extends App {

  val system = ActorSystem("Actor-Paths")

  val counter1 = system.actorOf(Props[Counter], "Counter")

  println(s"Actor Reference for counter1: ${counter1}")

  val counterSelector1 = system.actorSelection("counter")

  println(s"Actor Selection for counter1: ${counterSelector1}")

  counter1 ! PoisonPill

  Thread.sleep(100)

  val counter2 = system.actorOf(Props[Counter], "counter")

  println(s"Actor Reference for counter2 is: ${counter2}")

  val counterSelector2 = system.actorSelection("counter")

  println(s"Actor Selection for counter2 is: ${counterSelector2}")

  system.terminate()

}
