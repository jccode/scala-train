package learning_akka_videos.section7

import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Broadcast, FileIO, Flow, GraphDSL, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.util.ByteString

/**
  * WriteStream
  *
  * @author 01372461
  */
object WriteStream extends App {

  implicit val actorSystem = ActorSystem()
  implicit val flowMaterializer = ActorMaterializer()

  // source
  val source = Source(1 to 10000).filter(_ % 3 == 0)

  // sink
  val sink = FileIO.toPath(Paths.get("target/abc.txt"))

  // file output sink
  val fileSink = Flow[Int]
    .map(i => ByteString(i.toString))
    .toMat(sink)

  // console output sink
  val consoleSink = Sink.foreach[Int](println)

  // sent to both file sink & console sink
  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>


    val broadCast = builder.add(Broadcast[Int](2))

//    source ~> broadCast ~> fileSink

    ClosedShape
  })

}
