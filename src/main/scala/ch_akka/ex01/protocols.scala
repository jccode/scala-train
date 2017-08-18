package ch_akka.ex01

object StudentProtocol {
  case class InitSignal()
  case class QuoteRequest()
}

object TeacherProtocol {
  case class QuoteResponse(quoteString:String)
}