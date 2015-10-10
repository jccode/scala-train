package ch_akka.protocol

object StudentProtocol {
  case class InitSignal()
  case class QuoteRequest()
}

object TeacherProtocol {
  case class QuoteResponse(quoteString:String)
}