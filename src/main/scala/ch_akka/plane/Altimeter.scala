package ch_akka.plane

import akka.actor.{Actor, ActorLogging}

/**
  * Altimeter
  *
  * @author 01372461
  */
class Altimeter extends Actor with ActorLogging { this: EventSource =>
  import Altimeter._
  import scala.concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global

  val ceiling =  43000

  val maxRateOfClimb = 5000

  var rateOfClimb: Float = 0

  var altitude: Double = 0

  var lastTick = System.currentTimeMillis

  val ticker = context.system.scheduler.schedule(100.millis, 100.millis, self, Tick)

  case object Tick

  def altimeterReceive: Receive = {
    case RateChange(amount) =>
      // Keep the value of rateOfClimb within [1,1]
      rateOfClimb = amount.min(1.0f).max(-1.0f) * maxRateOfClimb
      log.info(s"Altimeter changed rate of climb to $rateOfClimb.")

    case Tick =>
      val tick = System.currentTimeMillis()
      altitude = altitude + ((tick - lastTick) / 60000.0) * rateOfClimb
      lastTick = tick
      sendEvent(AltitudeUpdate(altitude))
  }

  override def receive: Receive = eventSourceReceive orElse altimeterReceive

  override def postStop(): Unit = ticker.cancel()
}

object Altimeter {

  case class RateChange(amount: Float)

  case class AltitudeUpdate(altitude: Double)

  def apply(): Altimeter = new Altimeter with ProductionEventSource
}
