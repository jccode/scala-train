package ch_kafka

import java.util.Collections

import com.typesafe.scalalogging.Logger
import org.apache.kafka.clients.consumer.KafkaConsumer
import scala.collection.JavaConverters._

/**
  * Created by IT on 2017/7/14.
  */
object SimpleConsumer extends App {

  val logger = Logger("SimpleConsumer")
  val consumer = new KafkaConsumer[String, String](KafkaConfig.getProps())

  // MAIN
  logger.info("-----------------------------------------")
  logger.info("    SimpleConsumer application started.  ")
  logger.info("-----------------------------------------")

  consumer.subscribe(Collections.singletonList(KafkaConfig.TOPIC))
  while (true) {
    val records = consumer.poll(100)
    records.asScala.foreach(record => logger.info(record.value()))
  }
}
