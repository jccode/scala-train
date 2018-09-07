package learning_akka_videos.section8

import spray.json.DefaultJsonProtocol


case class IpInfo(ip: String)

object JsonProtocol extends DefaultJsonProtocol {
  implicit val format = jsonFormat1(IpInfo.apply)
}
