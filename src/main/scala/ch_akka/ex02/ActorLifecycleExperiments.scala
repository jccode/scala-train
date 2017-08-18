package ch_akka.ex02

import akka.actor.{Actor, ActorSystem, Props}

import scala.io.StdIn


class StartStopActor1 extends Actor {

  override def preStart(): Unit = {
    println("first started")
    context.actorOf(Props[StartStopActor2], "second")
  }

  override def postStop(): Unit = println("first stop")

  override def receive: Receive = {
    case "stop" => context.stop(self)
  }
}


class StartStopActor2 extends Actor {
  override def receive: Receive = Actor.emptyBehavior

  override def preStart(): Unit = println("second started")

  override def postStop(): Unit = println("second stopped")
}


object ActorLifecycleExperiments extends App {
  val system = ActorSystem()

  val actor1Ref = system.actorOf(Props[StartStopActor1], "actor1")
  actor1Ref ! "stop"

  println(">>> Press ENTER to exit <<<")
  try StdIn.readLine()
  finally system.terminate()
}
