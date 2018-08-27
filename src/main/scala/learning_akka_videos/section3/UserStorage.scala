package learning_akka_videos.section3

import akka.actor.{Actor, ActorSystem, Props, Stash}


case class User(username: String, email: String)

object UserStorage {

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

class UserStorage extends Actor with Stash {
  import UserStorage._

  override def receive: Receive = disconnected

  def connected: Receive = {
    case DisConnect =>
      println("User Storage Disconnect from DB")
      context.unbecome()
    case Operation(op, user) =>
      println(s"User Storage receive $op to do in user: $user")
  }

  def disconnected: Receive = {
    case Connect =>
      println("User Storage connected to DB")
      unstashAll()
      context.become(connected)
    case _ =>
      stash()
  }
}

object BecomeHotswap extends App {
  import UserStorage._

  val system = ActorSystem("Hotswap-Become")

  val userStorage = system.actorOf(Props[UserStorage], "userStorage")

  userStorage ! Operation(DBOperation.Create, Some(User("Admin", "admin@gmail.com")))
  userStorage ! Connect
  userStorage ! DisConnect

  Thread.sleep(100)

  system.terminate()
}