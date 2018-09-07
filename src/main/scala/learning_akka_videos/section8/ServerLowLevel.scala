package learning_akka_videos.section8

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.{HttpEntity, HttpRequest, HttpResponse, Uri}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

import scala.concurrent.Future
import scala.io.StdIn

/**
  * ServerLowLevel
  *
  * @author 01372461
  */
object ServerLowLevel extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val serverSource = Http().bind(interface = "localhost", port = 8000)

  val bindFuture: Future[Http.ServerBinding] = serverSource.to(Sink.foreach { connection =>
    println(s"Accepted new connection from ${connection.remoteAddress}")
    connection handleWithSyncHandler requestHandler
  }).run()

  val requestHandler: HttpRequest => HttpResponse = {
    case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
      HttpResponse(entity = HttpEntity("Hello Akka HTTP Server Side API - Low Level!"))

    case req @ _ =>
      HttpResponse(404, entity = s"Unknown resource! ${req.uri.path}")
  }

  println("Server online at http://localhost:8000/ \nPress RETURN to stop...")
  StdIn.readLine()

  bindFuture
    .flatMap(_.unbind)
    .onComplete(_ => system.terminate())
}
