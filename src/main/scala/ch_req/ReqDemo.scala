package ch_req


import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{Executors, TimeUnit}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, StatusCodes}
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


/**
  * Created by IT on 2017/7/17.
  */
object ReqDemo extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val log = Logger("ReqDemo")
  val scheduler = Executors.newSingleThreadScheduledExecutor()
  val i = new AtomicInteger(0)
  val conf = ConfigFactory.load()
  val max = conf.getInt("reqdemo.max")

  doVerify()


  def doVerify(): Unit = {
    verifyUrl(getUrl(), () => {
      if (i.get() < max) {
        scheduler.schedule(new Runnable {
          override def run(): Unit = doVerify()
        }, 1000 * randInt(1,10), TimeUnit.MILLISECONDS)

      } else {
        scheduler.shutdown()
        system.terminate()
      }
    })
  }

  def getUrl() = s"http://101.110.118.31/kkkk9.bb149.com/m${i.getAndAdd(1)}.mp4"

  def randInt(start: Int, end: Int): Int = {
    val r = new scala.util.Random()
    start + r.nextInt(end - start)
  }

  def verifyUrl(url: String, onComplete: () => Unit) = {
    Http()
      .singleRequest(HttpRequest(uri = url, method = HttpMethods.HEAD))
      .onComplete {
        case Success(resp) =>
          val code = resp.status match {
            case StatusCodes.OK => "[S]"
            case StatusCodes.NotFound => "[F]"
            case _ => "[E]"
          }
          resp.discardEntityBytes()
          log.info(s"${code}${url}")
          onComplete()

        case Failure(_) =>
          log.error(s"[E]${url}")
          onComplete()
      }
  }
}
