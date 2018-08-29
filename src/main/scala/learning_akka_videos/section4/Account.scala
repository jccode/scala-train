package learning_akka_videos.section4

import akka.persistence.fsm.PersistentFSM
import akka.persistence.fsm.PersistentFSM.FSMState

import scala.reflect._

/**
  * Account
  *
  * @author 01372461
  */

object Account {

  // Account State
  sealed trait State extends FSMState

  case object Empty extends State {
    override def identifier: String = "Empty"
  }

  case object Active extends State {
    override def identifier: String = "Active"
  }


  // Account Data
  sealed trait Data {
    val amount: Float
  }

  case object ZeroBalance extends Data {
    override val amount: Float = 0
  }

  case class Balance(amount: Float) extends Data

  // Domain Events
  sealed trait DomainEvent

  case class AcceptedTransaction(amount: Float, `type`: TransactionType) extends DomainEvent

  case class RejectedTransaction(amount: Float, `type`: TransactionType, reason: String) extends DomainEvent


  // Transaction Types
  trait TransactionType

  case object CR extends TransactionType
  case object DR extends TransactionType

  // Commands
  case class Operation(amount: Float, `type`: TransactionType)

}


class Account extends PersistentFSM[Account.State, Account.Data, Account.DomainEvent] {
  import Account._

  override def persistenceId: String = "account"


  override def applyEvent(evt: Account.DomainEvent, currentData: Account.Data): Account.Data = {
    evt match {
      case AcceptedTransaction(amount, CR) =>
        val newAmount = currentData.amount + amount
        println(s"Your new balance is $newAmount")
        Balance(newAmount)
      case AcceptedTransaction(amount, DR) =>
        val newAmount = currentData.amount - amount
        println(s"Your new balance is $newAmount")
        if (newAmount > 0) {
          Balance(newAmount)
        } else {
          ZeroBalance
        }
      case RejectedTransaction(amount, _, reason) =>
        println(s"Reject transaction with reason: $reason")
        currentData
    }
  }

  override def domainEventClassTag: ClassTag[DomainEvent] = classTag[DomainEvent]


  startWith(Empty, ZeroBalance)

  when(Empty) {
    case Event(Operation(amount, CR), _) =>
      goto(Active) applying AcceptedTransaction(amount, CR)
    case Event(Operation(amount, DR), _) =>
      stay applying RejectedTransaction(amount, DR, "balance is zero")
  }

  when(Active) {
    case Event(Operation(amount, CR), _) =>
      stay applying AcceptedTransaction(amount, CR)
    case Event(Operation(amount, DR), balance) =>
      val newAmount = balance.amount - amount
      if (newAmount > 0) {
        stay applying AcceptedTransaction(amount, DR)
      }
      else if(newAmount == 0) {
        goto(Empty) applying AcceptedTransaction(amount, DR)
      }
      else {
        stay applying RejectedTransaction(amount, DR, "Balance doesn't cover this operation")
      }
  }

}

