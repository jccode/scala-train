package ch_akka.ex02

import akka.actor.{Actor, ActorSystem, Props}

import scala.io.StdIn


class SupervisingActor extends Actor {
  val child = context.actorOf(Props[SupervisedActor], "supervised-actor")

  override def receive: Receive = {
    case "failChild" => child ! "fail"
  }
}

class SupervisedActor extends Actor {
  override def preStart(): Unit = println("supervisied actor started")

  override def postStop(): Unit = println("supervisied actor stopped")

  override def receive: Receive = {
    case "fail" =>
      println("supervised actor fails now")
      throw new Exception("I failed!")
  }
}

object ActorFailureHandlingExperiments extends App {
  val system = ActorSystem()

  val supervisingActor = system.actorOf(Props[SupervisingActor], "supervising-actor")
  supervisingActor ! "failChild"

  println(">>> Press ENTER to exit <<<")
  try StdIn.readLine()
  finally system.terminate()
}
