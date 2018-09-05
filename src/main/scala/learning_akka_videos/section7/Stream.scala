package learning_akka_videos.section7

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Stream
  *
  * @author 01372461
  */
object Stream extends App {

  implicit val actorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  import actorSystem.dispatcher   // ExecutorContext

  // Source
  val input = Source(1 to 1000)

  // Flow
  val normalize = Flow[Int].map(_ * 2)

  // Sink
  val output = Sink.foreach[Int](println)

  input.via(normalize).runWith(output).andThen {
    case _ =>
      actorSystem.terminate()
//      println("terminate")
//      Await.ready(actorSystem.whenTerminated, 5 seconds)
//      println("already terminate")
  }
}
