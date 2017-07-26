package ch_script

import java.io.{File, FileWriter}

import utils.CommonOpts.using
import com.typesafe.scalalogging.Logger

import scala.io.Source
import scala.sys.process._

import ch_json.MarshallableImplicits._

object GenPoster {

  case class VideoItem(src: String, poster: String, duration: Int)
  case class Video(videos: List[VideoItem])

  val IMG_SAVE_PATH = "/Users/IT/temp/tubesite/img"
  val IMG_SCALE = "784:516"
  val IMG_PREFIX = "kkkk9"

  val logger = Logger("GenPoster")

  val urlFile = "/Users/IT/temp/tubesite/url_h10.txt"
  val outFile = "/Users/IT/temp/tubesite/scala_out.jl"


  def main(args: Array[String]): Unit = {
    genFromUrlFiles(urlFile, outFile)
  }

  def genFromUrlFiles(urlFile: String, outFile: String): Unit = {
    using (Source.fromFile(urlFile)) (input => {
      using (new FileWriter(new File(outFile))) (output => {
        for (url <- input.getLines()) {
          logger.info(s"Processing: $url")
          try {
            val video = genItem(url)
            output.write(s"${video.toJson}\n")
            output.flush()
          } catch {
            case e: Exception => logger.error(s"ERROR: $url", e)
          }
        }
      })
    })
  }

  def genItem(url: String): Video = {
    val name = filename(url)
    val path = s"$IMG_SAVE_PATH/$IMG_PREFIX-$name.png"
    genPoster(url, path)
    val duration = genDuration(url)
    Video(List(VideoItem(url, path, duration)))
  }

  def genPoster(url: String, path: String): Unit = {
    val cmd = s"ffmpeg -i $url -vf thumbnail,scale=$IMG_SCALE -vframes 1 $path"
    cmd !;
  }

  def genDuration(url: String): Int = {
    val cmd = s"ffprobe -i $url -show_format -v quiet"
    val result = cmd #| "sed -n s/duration=//p" !!;
    result.toFloat.toInt
  }

  def filename(url: String): String = url.split("/").takeRight(1)(0).dropRight(4)

}
