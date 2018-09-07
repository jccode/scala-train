package learning_akka_videos.section8

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn

/**
  * ServerHighLevel
  *
  * @author 01372461
  */
object ServerHighLevel extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val route = path("") {
    get {
      complete("Hello Akka HTTP Server Side API - High Level")
    }
  }

  val bindFuture = Http().bindAndHandle(route, "localhost", 8000)

  println("Server online at http://localhost:8000/ \nPress RETURN to stop...")
  StdIn.readLine()

  bindFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}
