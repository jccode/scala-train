package ch_akka.iot

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import ch_akka.iot.Device.{ReadTemperature, RespondTemperature}
import ch_akka.iot.DeviceGroup._

import scala.concurrent.duration.FiniteDuration


class DeviceGroupQuery(actorToDeviceId: Map[ActorRef, String],
                       requestId: Long,
                       requester: ActorRef,
                       timeout: FiniteDuration) extends Actor with ActorLogging {

  import DeviceGroupQuery._
  import scala.concurrent.ExecutionContext.Implicits.global

  val queryTimeoutTimer = context.system.scheduler.scheduleOnce(timeout, self, CollectionTimeout)

  def receiveResponse(deviceActor: ActorRef, reading: TemperatureReading, stillWaiting: Set[ActorRef], repliesSoFar: Map[String, TemperatureReading]): Unit = {
    context.unwatch(deviceActor)
    val newStillWaiting = stillWaiting - deviceActor
    val newRepliesSoFor = repliesSoFar + (actorToDeviceId(deviceActor) -> reading)
    if(newStillWaiting.isEmpty) {
      requester ! ResponseAllTemperature(requestId, newRepliesSoFor)
      context.stop(self)
    } else {
      context.become(waitingForReplies(newRepliesSoFor, newStillWaiting))
    }
  }

  def waitingForReplies(repliesSoFar: Map[String, TemperatureReading], stillWaiting: Set[ActorRef]): Receive = {
    case RespondTemperature(_, valueOption) =>
      val reading: TemperatureReading = valueOption.map(v => Temperature(v)).getOrElse(TemperatureNotAvailable)
      receiveResponse(sender(), reading, stillWaiting, repliesSoFar)

    case Terminated(deviceActor) =>
      receiveResponse(deviceActor, DeviceNotAvailable, stillWaiting, repliesSoFar)

    case CollectionTimeout =>
      val timeoutReplies = stillWaiting.map { deviceActor =>
        actorToDeviceId(deviceActor) -> DeviceTimedout
      }
      requester ! ResponseAllTemperature(requestId, repliesSoFar ++ timeoutReplies)
      context.stop(self)
  }

  override def receive = waitingForReplies(Map.empty, actorToDeviceId.keySet)

  override def preStart(): Unit = {
    actorToDeviceId.keys.foreach { deviceActor =>
      context.watch(deviceActor)
      deviceActor ! ReadTemperature(requestId)
    }
  }

  override def postStop(): Unit = queryTimeoutTimer.cancel()
}


object DeviceGroupQuery {

  def props(actorToDeviceId: Map[ActorRef, String],
            requestId: Long,
            requester: ActorRef,
            timeout: FiniteDuration): Props = {
    Props(new DeviceGroupQuery(actorToDeviceId, requestId, requester, timeout))
  }

  case object CollectionTimeout
}
