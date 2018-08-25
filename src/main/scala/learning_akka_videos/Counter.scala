package learning_akka_videos

import akka.actor.Actor

class Counter extends Actor {
  import Counter._

  var count = 0

  override def receive: Receive = {
    case Inc(num) =>
      count += num
    case Dec(num) =>
      count -= num
  }
}

object Counter {
  final case class Inc(num: Int)
  final case class Dec(num: Int)
}
