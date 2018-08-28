package learning_akka_videos.section4

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, SnapshotOffer}


object Counter {
  sealed trait Operation {
    val count: Int
  }

  case class Increment(count: Int) extends Operation
  case class Decrement(count: Int) extends Operation

  case class Cmd(op: Operation)
  case class Evt(op: Operation)

  case class State(count: Int)
}

class Counter extends PersistentActor with ActorLogging{

  import Counter._

  override def persistenceId: String = "counter-example"

  var state: State = State(0)

  def updateState(evt: Evt): Unit = evt match {
    case Evt(Increment(count)) =>
      state = State(state.count + count)
    case Evt(Decrement(count)) =>
      state = State(state.count - count)
  }

  override def receiveRecover: Receive = {
    case evt: Evt =>
      println(s"Counter receive $evt on recovering mood")
      updateState(evt)
    case SnapshotOffer(_, snapshot: State) =>
      println(s"Counter receive snapshot with data: $snapshot on recovering mood")
      state = snapshot
  }

  override def receiveCommand: Receive = {
    case cmd @ Cmd(op) =>
      println(s"Counter receive $cmd")
      persist(Evt(op)) { evt =>
        updateState(evt)
      }
    case "print" =>
      println(s"Counter's current state is $state")
  }

}
