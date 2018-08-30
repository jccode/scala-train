package learning_akka_videos.section5

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import learning_akka_videos.Worker

/**
  * MemberService
  *
  * @author 01372461
  */
object MemberService extends App {

  val config = ConfigFactory.load().getConfig("MemberService")

  val system = ActorSystem("MemberService", config)

  val worker = system.actorOf(Props[Worker], "remote-worker")

  println(s"Worker actor path is ${worker.path}")

}

object MemberServiceLookup extends App {
  val config = ConfigFactory.load().getConfig("MemberServiceLookup")

  val system = ActorSystem("MemberServiceLookup", config)

  val worker = system.actorSelection("akka.tcp://MemberService@127.0.0.1:2552/user/remote-worker")

  worker ! Worker.Work("Hi, remote actor")
}

object MemberServiceRemoteCreation extends App {
  val config = ConfigFactory.load().getConfig("MemberServiceRemoteCreation")

  val system = ActorSystem("MemberServiceRemoteCreation", config)

  val worker = system.actorOf(Props[Worker], "workerActorRemote")

  println(s"The remote path of worker Actor is ${worker.path}")

  worker ! Worker.Work("Hi, remote actor")
}