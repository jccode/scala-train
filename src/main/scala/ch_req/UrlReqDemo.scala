package ch_req

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by IT on 2017/7/17.
  */
object UrlReqDemo extends App {

  val url = "http://101.110.118.31/kkkk9.bb149.com/m1.mp4"

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = url))
  responseFuture
    .filter(response => response.status.intValue() == 200)
    .map(response => {
      println(response)
      println(response.status)
      println(response.status.intValue())
    })
    .onComplete(_ => {
      system.terminate()
    })
}
