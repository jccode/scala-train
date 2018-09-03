package ch_akka.plane

import akka.actor.SupervisorStrategy.{Decider, Escalate, Resume, Stop}
import akka.actor.{Actor, ActorInitializationException, ActorKilledException, AllForOneStrategy, OneForOneStrategy, SupervisorStrategy}
import ch_akka.plane.IsolatedLifeCycleSupervisor.{Started, WaitForStart}

import scala.concurrent.duration.Duration

object IsolatedLifeCycleSupervisor {

  case object WaitForStart
  case object Started

}

/**
  * IsolatedLifeCycleSupervisor
  *
  */
trait IsolatedLifeCycleSupervisor extends Actor {

  override def receive: Receive = {
    case WaitForStart => sender ! Started

    // We don't handle anything else, but we give a decent error message starting the error
    case m => throw new Exception(s"Don't call ${self.path.name} directly ($m)")
  }

  // To be implement by subclass
  def childStarter(): Unit

  // Only start the children when we're started
  override def preStart(): Unit = childStarter()

  // Don't stop the children, which would be the default behaviour
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {}

  // Don't call preStart(), which would be the default behaviour
  override def postRestart(reason: Throwable): Unit = {}
}


// SupervisionStrategyFactory

trait SupervisionStrategyFactory {
  def makeStrategy(maxNrRetry: Int, withinTimeRange: Duration)(decider: Decider): SupervisorStrategy
}

trait OneForOneStrategyFactory extends SupervisionStrategyFactory {
  def makeStrategy(maxNrRetry: Int, withinTimeRange: Duration)(decider: Decider): SupervisorStrategy =
    OneForOneStrategy(maxNrRetry, withinTimeRange)(decider)
}

trait AllForOneStrategyFactory extends SupervisionStrategyFactory {
  def makeStrategy(maxNrRetry: Int, withinTimeRange: Duration)(decider: Decider): SupervisorStrategy =
    AllForOneStrategy(maxNrRetry, withinTimeRange)(decider)
}


// Supervisors

abstract class IsolatedResumeSupervisor(maxNrRetry: Int = -1, withinTimeRange: Duration = Duration.Inf)
  extends IsolatedLifeCycleSupervisor { this: SupervisionStrategyFactory =>
  override def supervisorStrategy: SupervisorStrategy = makeStrategy(maxNrRetry, withinTimeRange) {
    case _: ActorInitializationException => Stop
    case _: ActorKilledException => Stop
    case _: Exception => Resume
    case _ => Escalate
  }
}

abstract class IsolatedStopSupervisor(maxNrRetry: Int = -1, withinTimeRange: Duration = Duration.Inf)
  extends IsolatedLifeCycleSupervisor { this: SupervisionStrategyFactory =>
  override def supervisorStrategy: SupervisorStrategy = makeStrategy(maxNrRetry, withinTimeRange) {
    case _: ActorInitializationException => Stop
    case _: ActorKilledException => Stop
    case _: Exception => Stop
    case _ => Escalate
  }
}

