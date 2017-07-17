package ch_kafka

import java.util.Properties

import com.typesafe.config.ConfigFactory

/**
  * Created by IT on 2017/7/14.
  */
object KafkaConfig {

  val TOPIC = "simple-test"

  def getProps(): Properties = {
    val conf = ConfigFactory.load()
    val props = new Properties()
    props.put("bootstrap.servers", conf.getString("kafka.bootstrap.servers"))
    props.put("group.id", conf.getString("kafka.group.id"))
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props
  }

}
