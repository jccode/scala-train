package ch_proxy

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.scalalogging.LazyLogging

/**
  * Proxy
  *
  * @author 01372461
  */
object Proxy extends App with LazyLogging {

  implicit val system = ActorSystem("Proxy")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val proxy = Route { context =>
    val request = context.request
    logger.info("Opening connection to " + request.uri.authority.host.address + "; uri:" + request.uri)
    val flow = Http(system).outgoingConnection(request.uri.authority.host.address(), 80)
    val handler = Source.single(context.request)
      .via(flow)
      .runWith(Sink.head)
      .flatMap(context.complete(_))
    handler
  }

  val binding = Http().bindAndHandle(handler = proxy, interface = "localhost", port = 10800)

}
