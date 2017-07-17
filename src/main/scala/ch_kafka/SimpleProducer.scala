package ch_kafka

import java.util.concurrent.{Executors, TimeUnit}

import com.typesafe.scalalogging.Logger
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

/**
  * Created by IT on 2017/7/14.
  */

object SimpleProducer extends App {

  val logger = Logger("SimpleProducer")
  val producer = new KafkaProducer[String, String](KafkaConfig.getProps())
  val scheduler = Executors.newSingleThreadScheduledExecutor()
  val contents = foreverYong().split("\n")

  // MAIN
  logger.info("-----------------------------------------")
  logger.info("    SimpleProducer application started.  ")
  logger.info("-----------------------------------------")
  scheduler.scheduleAtFixedRate(() => {
     doSend(contents(randInt(0, contents.length)))
  }, 0, 3000, TimeUnit.MILLISECONDS)


  def doSend(content: String): Unit = {
    val data = content + "\t(" + System.currentTimeMillis + ")"
    val record = new ProducerRecord[String, String](KafkaConfig.TOPIC, "key", data)
    producer.send(record)
    logger.info(s"[S] ${data}")
  }

  def randInt(start: Int, end: Int): Int = {
    val r = new scala.util.Random()
    start + r.nextInt(end - start)
  }

  def foreverYong(): String = {
      "May God bless and keep you always,\n" +
      "愿上帝的庇护与你同在\n" +
      "May your wishes all come true,\n" +
      "愿你能够梦想成真\n" +
      "May you always do for others\n" +
      "愿你永远帮助别人\n" +
      "And let others do for you.\n" +
      "也接受别人的恩惠\n" +
      "May you build a ladder to the stars\n" +
      "愿你可以造一把采摘繁星的云梯\n" +
      "And climb on every rung,\n" +
      "稳妥沿它而上\n" +
      "May you stay forever young,\n" +
      "愿你永远年轻\n" +
      "May you grow up to be righteous,\n" +
      "愿你长大后正直无私\n" +
      "May you grow up to be true,\n" +
      "愿你懂事时真实善良\n" +
      "May you always know the truth\n" +
      "愿你永远了解真理的方向\n" +
      "And see the lights surrounding you.\n" +
      "所到之处都有高灯明照\n" +
      "May you always be courageous,\n" +
      "愿你永远勇敢无畏\n" +
      "Stand upright and be strong,\n" +
      "坚韧不拔，意志坚强\n" +
      "May you stay forever young,\n" +
      "愿你永远年轻\n" +
      "May your hands always be busy,\n" +
      "愿你总是忙碌充实\n" +
      "May your feet always be swift,\n" +
      "愿你脚步永远轻盈\n" +
      "May you have a strong foundation\n" +
      "愿你根基牢固\n" +
      "When the winds of changes shift.\n" +
      "在变故横生之时\n" +
      "May your heart always be joyful,\n" +
      "愿你的心总是充满快乐\n" +
      "May your song always be sung,\n" +
      "愿你的歌曲能够永远被人传唱\n" +
      "May you stay forever young,\n" +
      "愿你永远年轻";
  }

}