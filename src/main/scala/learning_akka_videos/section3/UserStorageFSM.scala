package learning_akka_videos.section3

import akka.actor.{ActorSystem, FSM, Props, Stash}

/**
  * UserStorageFSM
  *
  * @author 01372461
  */


object UserStorageFSM {

  // FSM State
  sealed trait State
  case object Connected extends State
  case object Disconnected extends State

  // FSM Data
  sealed trait Data
  case object EmptyData extends Data

  trait DBOperation

  object DBOperation {
    case object Create extends DBOperation
    case object Update extends DBOperation
    case object Read extends DBOperation
    case object Delete extends DBOperation
  }

  case object Connect
  case object DisConnect
  case class Operation(dbOperation: DBOperation, user: Option[User])

}


class UserStorageFSM extends FSM[UserStorageFSM.State, UserStorageFSM.Data] with Stash {
  import UserStorageFSM._

  // 1. define startWith
  startWith(Disconnected, EmptyData)

  // 2. define states
  when(Disconnected) {
    case Event(Connect, _) =>
      println("User Storage connected to DB")
      unstashAll()
      goto(Connected) using EmptyData
    case Event(_, _) =>
      stash()
      stay using EmptyData
  }

  when(Connected) {
    case Event(DisConnect, _) =>
      println("User Storage disconnected to DB")
      goto(Disconnected) using EmptyData
    case Event(Operation(op, user), _) =>
      println(s"User Storage receive $op to do in user: $user")
      stay using EmptyData
  }

  // 3. initialize
  initialize()
}


object FSMHotswap extends App {
  import UserStorageFSM._

  val system = ActorSystem("Hotswap-FSM")

  val userStorage = system.actorOf(Props[UserStorageFSM], "userStorage-fsm")

  userStorage ! Operation(DBOperation.Create, Some(User("Admin", "admin@gmail.com")))
  userStorage ! Connect
  userStorage ! DisConnect

  Thread.sleep(100)

  system.terminate()
}