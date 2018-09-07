package learning_akka_videos.section8

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import akka.http.scaladsl.model.StatusCodes._

/**
  * HostLevel
  *
  * @author 01372461
  */
object HostLevel extends App {
  import JsonProtocol._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  implicit val system = ActorSystem()
  implicit val materilizer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val poolClientFlow = Http().cachedHostConnectionPool[Int]("api.ipify.org")

  val responseFuture: Future[(Try[HttpResponse], Int)] = Source.single(HttpRequest(uri = "/?format=json") -> 4)
    .via(poolClientFlow)
    .runWith(Sink.head)

  responseFuture map {
    case (Success(res), _) =>
      res.status match {
        case OK =>
          Unmarshal(res.entity).to[IpInfo].map { info =>
            println(s"The information for my ip is $info")
            shutdown()
          }
        case _ =>
          Unmarshal(res.entity).to[String].map { body =>
            println(s"The response status is ${res.status} and response body is $body")
            shutdown()
          }
      }
    case (Failure(err), _) =>
      println(s"Error occur $err")
      shutdown()
  }

  def shutdown(): Unit = {
    Http().shutdownAllConnectionPools().onComplete(_ => system.terminate())
  }
}
