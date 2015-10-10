package ch_01.concurrency

import java.net.{Socket, ServerSocket}

class NetworkService(port: Int, poorSize: Int) extends Runnable {
  val serverSocket = new ServerSocket(port)

  def run(): Unit = {
    while (true) {
      val socket = serverSocket.accept()
      (new Handler(socket)).run()
    }
  }
}

class Handler(socket: Socket) extends Runnable {
  def message = (Thread.currentThread().getName() + "\n").getBytes()

  def run: Unit = {
    socket.getOutputStream.write(message)
    socket.getOutputStream.close()
  }
}

object App {
  def main(args: Array[String]) {
    (new NetworkService(2020, 2)).run()
  }
}