package learning_akka_videos.section2

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._


case class User(name: String, email: String)


class Recorder(checker: ActorRef, storage: ActorRef) extends Actor {
  import scala.concurrent.ExecutionContext.Implicits.global
  import Recorder._
  import Checker._
  import Storage._

  implicit val timeout: Timeout = 5 seconds

  override def receive: Receive = {
    case NewUser(user: User) =>
      checker ? CheckUser(user) map {
        case BlackUser(user) =>
          println(s"Recorder: $user is black user")

        case WhiteUser(user) =>
          println(s"Recorder: $user is white user")
          storage ! AddUser(user)
      }
  }
}

object Recorder {
  def props(checker: ActorRef, storage: ActorRef) = Props(new Recorder(checker, storage))

  sealed trait RecorderMsg

  case class NewUser(user: User) extends RecorderMsg
}


class Checker extends Actor {
  import Checker._

  val blackList = List(
    User("ada", "ada@gmail.com")
  )

  override def receive: Receive = {
    case CheckUser(user) if blackList.contains(user) =>
      println(s"Checker: $user is in blackList")
      sender() ! BlackUser(user)

    case CheckUser(user) =>
      println(s"Checker: $user is not in blackList")
      sender() ! WhiteUser(user)
  }
}

object Checker {
  def props = Props[Checker]

  sealed trait CheckerMsg

  // req msg
  case class CheckUser(user: User)

  // response msg
  case class BlackUser(user: User)
  case class WhiteUser(user: User)
}


class Storage extends Actor {
  import Storage._

  var users = List.empty[User]

  override def receive: Receive = {
    case AddUser(user) =>
      println(s"Storage: add user $user")
      users = user :: users
  }
}

object Storage {
  def props = Props[Storage]

  sealed trait StorageMsg

  case class AddUser(user: User)
}

object TalkToActor extends App {
  import Recorder._

  val system = ActorSystem("talk-to-actor")

  val checker = system.actorOf(Checker.props, "checker")

  val storage = system.actorOf(Storage.props, "storage")

  val recorder = system.actorOf(Recorder.props(checker, storage), "recorder")

  recorder ! NewUser(User("Tom", "tom@gmail.com"))

  Thread.sleep(100)

  system.terminate()
//  Await.ready(system.terminate(), 5.seconds)

}
