package learning_akka_videos.section7

import java.nio.file.Paths

import akka.Done
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Broadcast, FileIO, Flow, GraphDSL, Keep, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape, IOResult}
import akka.util.ByteString

import scala.concurrent.Future

/**
  * WriteStream
  *
  * @author 01372461
  */
object WriteStream extends App {

  implicit val actorSystem = ActorSystem()
  implicit val flowMaterializer = ActorMaterializer()
  implicit val ec = actorSystem.dispatcher

  // source
  val source = Source(1 to 10000).filter(_ % 3 == 0)

  // sink
  val sink = FileIO.toPath(Paths.get("target/abc.txt"))

  // file output sink
  val fileSink: Sink[Int, Future[IOResult]] = Flow[Int]
    .map(i => ByteString(i.toString+"\r\n"))
    .toMat(sink)(Keep.right)

  // console output sink
  val consoleSink: Sink[Int, Future[Done]] = Sink.foreach[Int](println)

  // sent to both file sink & console sink
  val g = RunnableGraph.fromGraph(GraphDSL.create(fileSink, consoleSink)((_, _)) { implicit builder => (file, console) =>
    import GraphDSL.Implicits._
    val broadCast = builder.add(Broadcast[Int](2))

    source ~> broadCast ~> file
              broadCast ~> console

    ClosedShape
  })

  val (fIO, fConsole) = g.run()
  Future.sequence(List(fIO, fConsole)).onComplete { _ =>
    println("Ok!")
    actorSystem.terminate()
  }
}
