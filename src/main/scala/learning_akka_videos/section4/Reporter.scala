package learning_akka_videos.section4

import akka.NotUsed
import akka.actor.ActorSystem
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal
import akka.persistence.query.{EventEnvelope, PersistenceQuery}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source

/**
  * Reporter
  *
  * @author 01372461
  */
object Reporter extends App {

  val system = ActorSystem("reporter")

  implicit val mat = ActorMaterializer()(system)

  val query: LeveldbReadJournal = PersistenceQuery(system).readJournalFor(LeveldbReadJournal.Identifier)

  val evts: Source[EventEnvelope, NotUsed] = query.eventsByPersistenceId("account", 0, Long.MaxValue)

  evts runForeach { evt => println(s"Event $evt") }

  Thread.sleep(1000)

  system.terminate()
}
