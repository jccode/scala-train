package ch_02

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ThrottleMode}
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * ReqApp
  *
  * @author 01372461
  */
object ReqApp extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  /*
  // source
  val source = Source(1 to 100).throttle(1, 1.second, 10, ThrottleMode.Shaping)

  // flow
  val flow = Flow[Int]

  // sink
  val sink = Sink.foreach[Int](println)

  source.via(flow).runWith(sink).onComplete { _ =>
    system.terminate()
  }
  */

  val source = Source.tick(0.second, 1.second, 1)
  val flow = Flow[Int].map(_ => Future { 10 } )
  val sink = Sink.foreach[Int](println)
  source.runWith(sink).onComplete(_ => system.terminate())
}
