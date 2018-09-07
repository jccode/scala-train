package learning_akka_videos.section7

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Framing, Sink}
import akka.util.ByteString

/**
  * ReadStream
  *
  * @author 01372461
  */
object ReadStream extends App {
  implicit val actorSystem = ActorSystem()
  implicit val flowMaterializer = ActorMaterializer()
  implicit val ec = actorSystem.dispatcher

  // source
  val source = FileIO.fromPath(Paths.get(ClassLoader.getSystemResource("a.txt").toURI))

  // parse chunks of bytes into lines
  val flow = Framing.delimiter(ByteString("\n"), maximumFrameLength = 256, allowTruncation = true)
    .map(_.utf8String)

  // sink
  val sink = Sink.foreach(println)

  source.via(flow).runWith(sink).onComplete(_ => actorSystem.terminate())

}
